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
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.ObjectState;
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
public class ArtWorkDBService extends DBService<ArtWork, Long> {

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

		
		// c.setUsethumbnail(true);
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
		c.setState(ObjectState.EDITION);

		getRepository().save(c);
		for (Language la : getLanguageService().getLanguages()) {
			getArtWorkRecordDBService().create(c, la.getLanguageCode(), createdBy);
		}
		return getRepository().save(c);
	}

	@Transactional
	public ArtWork create(String name, Site site, User createdBy) {
		ArtWork c = new ArtWork();

		c.setName(name);
		// c.setNameKey(nameKey(name));

		c.setSite(site);
		c.setMasterLanguage(site.getMasterLanguage());
		c.setLanguage(site.getLanguage());
		
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
		// c.setUsethumbnail(true);

		getRepository().save(c);

		for (Language la : getLanguageService().getLanguages())
			getArtWorkRecordDBService().create(c, la.getLanguageCode(), createdBy);

		return getRepository().save(c);
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

	@Transactional
	public void delete(Long id) {
		deleteResources(id);
		super.deleteById(id);
	}

	@Transactional
	public void delete(ArtWork o) {
		this.delete(o.getId());
	}

	@SuppressWarnings("unused")
	@Transactional
	public Optional<ArtWork> findWithDeps(Long id) {

		Optional<ArtWork> o_aw = super.findById(id);

		if (o_aw.isEmpty())
			return o_aw;

		ArtWork aw = o_aw.get();

		aw.getSite().getDisplayname();

		for (Person p : aw.getArtists()) {
			p.getDisplayName();
		}

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

		for (Person p : aw.getArtists()) {
			Long p_id = p.getId();
		}

		aw.setDependencies(true);

		return o_aw;
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
	public List<Person> loadArtists(ArtWork aw) {

		aw = findById(aw.getId()).get();
		PersonDBService service = (PersonDBService) ServiceLocator.getInstance().getBean(PersonDBService.class);
		List<Person> list = new ArrayList<Person>();
		for (Person person : aw.getArtists()) {
			list.add(service.findById(person.getId()).get());
		}
		return list;
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

	@PostConstruct
	protected void onInitialize() {
		super.register(getEntityClass(), this);
	}

}
