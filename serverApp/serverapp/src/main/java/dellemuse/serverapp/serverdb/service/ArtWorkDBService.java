package dellemuse.serverapp.serverdb.service;

import java.time.OffsetDateTime;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.audit.AuditKey;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Artist;
import dellemuse.serverapp.serverdb.model.AuditAction;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.ObjectType;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;

import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.serverdb.service.record.ArtWorkRecordDBService;
import dellemuse.serverapp.service.language.LanguageService;
import jakarta.annotation.PostConstruct;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class ArtWorkDBService extends MultiLanguageObjectDBservice<ArtWork, Long> {

	static private Logger logger = Logger.getLogger(ArtWorkDBService.class.getName());

	@JsonIgnore
	final ArtWorkRecordDBService artWorkRecordDBService;

	@JsonIgnore
	final LanguageService languageService;

	public ArtWorkDBService(CrudRepository<ArtWork, Long> repository, ServerDBSettings settings, LanguageService languageService, ArtWorkRecordDBService artWorkRecordDBService) {
		super(repository, settings);
		this.artWorkRecordDBService = artWorkRecordDBService;
		this.languageService = languageService;
	}

	/**
	 * <p>
	 * Annotation Transactional is required to store values into the Database
	 * </p>
	 * 
	 * @param name
	 * @param createdBy
	 */
	@Transactional
	public ArtWork create(String name, User createdBy) {
		ArtWork c = new ArtWork();
		c.setName(name);

		c.setMasterLanguage(getDefaultMasterLanguage());
		c.setLanguage(getDefaultMasterLanguage());

		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
		c.setState(ObjectState.EDITION);
		c.setObjectType(ObjectType.ARTWORK);

		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy, AuditAction.CREATE));
		getRepository().save(c);

		for (Language la : getLanguageService().getLanguages())
			getArtWorkRecordDBService().create(c, la.getLanguageCode(), createdBy);

		return c;
	}

	@Transactional
	public ArtWork create(String name, Site site, User createdBy) {
		ArtWork c = new ArtWork();

		c.setName(name);

		c.setSite(site);
		c.setMasterLanguage(site.getMasterLanguage());
		c.setLanguage(site.getLanguage());
		c.setObjectType(ObjectType.ARTWORK);
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
		c.setState(ObjectState.EDITION);

		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy, AuditAction.CREATE));

		for (Language la : getLanguageService().getLanguages())
			getArtWorkRecordDBService().create(c, la.getLanguageCode(), createdBy);

		return c;
	}

	@Transactional
	public void save(ArtWork o, User user, String updatedPart) {
		super.save(o);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(o, user, AuditAction.UPDATE, updatedPart));
	}

	@Transactional
	public void save(ArtWork o, User user, List<String> updatedParts) {
		super.save(o);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(o, user, AuditAction.UPDATE, String.join(", ", updatedParts)));
	}

	@Transactional
	public ArtWork addQR(ArtWork aw, String text, String bucketName, String objectName, String name, String media, long size, User createdBy) {
		ResourceDBService rdbs = (ResourceDBService) ServiceLocator.getInstance().getBean(ResourceDBService.class);
		Resource res = rdbs.create(bucketName, objectName, name, media, size, ServerConstant.QR_CODE, createdBy, name, true);
		aw.setQRCode(res);
		aw.setQrCodeText(text);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(aw, createdBy, AuditAction.UPDATE, AuditKey.ADD_QR));
		return getRepository().save(aw);
	}

	@Transactional
	private void deleteResources(Long id) {

		Optional<ArtWork> o_aw = super.findWithDeps(id);

		if (o_aw.isEmpty())
			return;

		ArtWork a = o_aw.get();

		getResourceDBService().delete(a.getPhoto());
		getResourceDBService().delete(a.getAudio());
		getResourceDBService().delete(a.getVideo());
		getResourceDBService().delete(a.getQRCode());

	}

	@Transactional
	public void generateAudioId(ArtWork a, User user) {

		Site site = getSiteDBService().findById(a.getSite().getId()).get();
		Long aid = getSiteDBService().newAudioId(site.getId());
		a.setAudioId(aid);
		logger.debug("adding audioid to ArtWork -> " + a.getDisplayname());
		save(a, user, "audioid");

		getSiteDBService().getSiteArtWorkGuideContents(site.getId(), a).forEach(c -> {
			c.setArtWorkAudioId(aid);
			logger.debug("adding audioid to GuideContent -> " + c.getDisplayname());
			getGuideContentDBService().save(c, user, List.of("audioid"));
		});
	}

	@Transactional
	public Optional<ArtWork> findWithDeps(Long id) {

		Optional<ArtWork> o_aw = super.findById(id);

		if (o_aw.isEmpty())
			return o_aw;

		ArtWork aw = o_aw.get();

		// Read all lazy proxy IDs while entity is still attached
		Long siteId = aw.getSite() != null ? aw.getSite().getId() : null;

		Set<Long> artistIds = new HashSet<Long>();
		if (aw.getArtists() != null)
			aw.getArtists().forEach(p -> artistIds.add(p.getId()));

		Long photoId = aw.getPhoto() != null ? aw.getPhoto().getId() : null;
		Long qrcodeId = aw.getQRCode() != null ? aw.getQRCode().getId() : null;
		Long qrPdfId = aw.getQRCodePdf() != null ? aw.getQRCodePdf().getId() : null;
		Long audioNumberPngId = aw.getAudioNumberPng() != null ? aw.getAudioNumberPng().getId() : null;
		Long userId = aw.getLastModifiedUser() != null ? aw.getLastModifiedUser().getId() : null;

		// Detach to prevent dirty-checking from triggering @PostUpdate
		getEntityManager().detach(aw);

		if (siteId != null)
			aw.setSite(getSiteDBService().findById(siteId).get());

		Set<Artist> set = new HashSet<Artist>();
		artistIds.forEach(aid -> set.add(getArtistDBService().findById(aid).get()));
		aw.setArtists(set);

		if (photoId != null)
			aw.setPhoto(getResourceDBService().findById(photoId).get());

		if (qrcodeId != null)
			aw.setQrcode(getResourceDBService().findById(qrcodeId).get());

		if (qrPdfId != null)
			aw.setQRCodePdf(getResourceDBService().findById(qrPdfId).get());

		if (audioNumberPngId != null)
			aw.setAudioNumberPng(getResourceDBService().findById(audioNumberPngId).get());

		if (userId != null)
			aw.setLastModifiedUser(getUserDBService().findById(userId).get());

		aw.setDependencies(true);

		return o_aw;
	}

	public ArtistDBService getArtistDBService() {
		return (ArtistDBService) ServiceLocator.getInstance().getBean(ArtistDBService.class);

	}

	public PersonDBService getPersonDBService() {
		return (PersonDBService) ServiceLocator.getInstance().getBean(PersonDBService.class);

	}

	@Transactional
	@Override
	public Iterable<ArtWork> findAllSorted() {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArtWork> cq = cb.createQuery(getEntityClass());
		Root<ArtWork> root = cq.from(getEntityClass());
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));
		return getEntityManager().createQuery(cq).getResultList();
	}

	public boolean isDetached(ArtWork entity) {
		return !getEntityManager().contains(entity);
	}

	public LanguageService getLanguageService() {
		return this.languageService;
	}

	@Transactional
	public void reloadIfDetached(ArtWork src) {
		if (!getEntityManager().contains(src)) {
			src = findById(src.getId()).get();
		}
	}

	@Transactional
	public Site loadSite(ArtWork aw) {
		SiteDBService service = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
		Site site = service.findById(aw.getSite().getId()).get();
		return site;
	}

	@Transactional
	public List<Person> getArtists(ArtWork aw) {

		/**
		 * aw = findById(aw.getId()).get(); PersonDBService service = (PersonDBService)
		 * ServiceLocator.getInstance().getBean(PersonDBService.class); List<Person>
		 * list = new ArrayList<Person>(); aw.getAwArtists().forEach( i-> { list.add(
		 * service.findById(i.getPerson().getId()).get() ); }); return list;
		 **/

		throw new RuntimeException("not done");

	}

	@Transactional
	public ArtWork addQRPdf(ArtWork aw, String bucketName, String objectName, String name, String media, long size, User createdBy) {
		ResourceDBService rdbs = (ResourceDBService) ServiceLocator.getInstance().getBean(ResourceDBService.class);
		Resource res = rdbs.create(bucketName, objectName, name, media, size, ServerConstant.QR_CODE_PDF, createdBy, name, true);
		aw.setQRCodePdf(res);
		getRepository().save(aw);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(aw, createdBy, AuditAction.UPDATE, AuditKey.ADD_QR_PDF));
		return aw;
	}

	@Transactional
	public ArtWork addAudioNumberPng(ArtWork aw, String bucketName, String objectName, String name, String media, long size, User createdBy) {
		ResourceDBService rdbs = (ResourceDBService) ServiceLocator.getInstance().getBean(ResourceDBService.class);
		Resource res = rdbs.create(bucketName, objectName, name, media, size, ServerConstant.AUDIO_NUMBER_PNG, createdBy, name, true);
		aw.setAudioNumberPng(res);
		getRepository().save(aw);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(aw, createdBy, AuditAction.UPDATE, AuditKey.ADD_AUDIO_NUMBER_PNG));
		return aw;
	}

	/**
	 * @param name
	 * @return
	 */
	@Transactional
	public List<ArtWork> getByName(String name) {
		return createNameQuery(name).getResultList();
	}

	@Override
	protected Class<ArtWork> getEntityClass() {
		return ArtWork.class;
	}

	public ArtWorkRecordDBService getArtWorkRecordDBService() {
		return this.artWorkRecordDBService;
	}

	@Override
	public String getObjectClassName() {
		return ArtWork.class.getSimpleName().toLowerCase();
	}

	@PostConstruct
	protected void onInitialize() {
		super.registerRecordDB(getEntityClass(), getArtWorkRecordDBService());
		super.register(getEntityClass(), this);
	}

}
