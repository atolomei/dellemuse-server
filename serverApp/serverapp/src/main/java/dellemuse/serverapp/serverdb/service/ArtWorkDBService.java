package dellemuse.serverapp.serverdb.service;

import java.io.File;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.ArtExhibitionStatusType;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.AuditAction;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.ArtWorkRecord;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.serverdb.service.record.ArtWorkRecordDBService;
import dellemuse.serverapp.service.language.LanguageService;
import jakarta.annotation.PostConstruct;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class ArtWorkDBService extends  MultiLanguageObjectDBservice<ArtWork, Long> {

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

		
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy,  AuditAction.CREATE));
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
		
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
		 

		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy,  AuditAction.CREATE));
		
		for (Language la : getLanguageService().getLanguages())
			getArtWorkRecordDBService().create(c, la.getLanguageCode(), createdBy);

		return c;
	}

	
	 @Transactional	
	public void save(ArtWork o, User user, List<String> updatedParts) {
		super.save(o);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(o, user, AuditAction.UPDATE, String.join(", ", updatedParts)));
	}
	
	
	@Transactional
	public ArtWork addQR(ArtWork aw, String bucketName, String objectName, String name, String media, long size, User createdBy) {
		ResourceDBService rdbs = (ResourceDBService) ServiceLocator.getInstance().getBean(ResourceDBService.class);
		Resource res = rdbs.create(bucketName, objectName, name, media, size, ServerConstant.QR_CODE, createdBy, name);
		aw.setQRCode(res);
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

	/**@Transactional
	public void delete(Long id) {
		deleteResources(id);
		super.deleteById(id);
	}
	**/

 

	@SuppressWarnings("unused")
	//@EntityGraph(attributePaths = {"artists"})
	@Transactional
	public Optional<ArtWork> findWithDeps(Long id) {

		Optional<ArtWork> o_aw = super.findById(id);

		if (o_aw.isEmpty())
			return o_aw;

		ArtWork aw = o_aw.get();

		aw.getSite().getDisplayname();

		Set<Person> set= new HashSet<Person>();
		
		aw.getArtists().forEach( p -> set.add(getPersonDBService().findById( p.getId()).get() ));
		aw.setArtists(set);
		
		Resource photo = aw.getPhoto();

		User u = aw.getLastModifiedUser();

		if (u != null)
			u.getDisplayname();

		if (photo != null)
			photo.getBucketName();

		Resource qrcode = aw.getQRCode();

		if (qrcode != null) {
			User qu = qrcode.getLastModifiedUser();
			qrcode.getBucketName();
		}
		 
		
		aw.setDependencies(true);

		return o_aw;
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

		/**aw = findById(aw.getId()).get();
		PersonDBService service = (PersonDBService) ServiceLocator.getInstance().getBean(PersonDBService.class);
		List<Person> list = new ArrayList<Person>();
		aw.getAwArtists().forEach( i-> {
				list.add( service.findById(i.getPerson().getId()).get() );
		});
		return list;
**/
		return null;
		
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
		super.registerRecordDB(getEntityClass(),getArtWorkRecordDBService());
		super.register(getEntityClass(), this);
	}

}
