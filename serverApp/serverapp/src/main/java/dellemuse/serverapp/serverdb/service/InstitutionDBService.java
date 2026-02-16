package dellemuse.serverapp.serverdb.service;

import static org.mockito.Mockito.CALLS_REAL_METHODS;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.audit.AuditKey;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.AuditAction;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.Institution;

import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.record.InstitutionRecordDBService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class InstitutionDBService extends  MultiLanguageObjectDBservice<Institution, Long> {

	private static final Logger logger = Logger.getLogger(InstitutionDBService.class.getName());

	@PersistenceContext
	private EntityManager entityManager;

	@JsonIgnore
	@Autowired
	final InstitutionRecordDBService institutionRecordDBService;

	public InstitutionDBService(CrudRepository<Institution, Long> repository, ServerDBSettings settings, InstitutionRecordDBService institutionRecordDBService) {
		super(repository, settings);
		this.institutionRecordDBService = institutionRecordDBService;
	}
 
	@Transactional
	public Institution create(String name, User createdBy) {
		Institution c = new Institution();

		c.setName(name);
		c.setState(ObjectState.EDITION);
		
	 

		c.setMasterLanguage(getDefaultMasterLanguage());
		c.setLanguage(getDefaultMasterLanguage());
		c.setLanguages( Language.getDefaultLanguages() );
		
		
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);

		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy, AuditAction.CREATE));

		for (Language la : getLanguageService().getLanguages())
			getInstitutionRecordDBService().create(c, la.getLanguageCode(), createdBy);

		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy, AuditAction.UPDATE));
		
		
		getRoleInstitutionDBService().create("admin", c, createdBy);
		
		return c;
	}

	@Transactional
	public Institution create(String name, Optional<String> shortName, Optional<String> address, Optional<String> info, User createdBy) {

		Institution c = new Institution();
		c.setName(name);

		c.setState(ObjectState.EDITION);

		c.setMasterLanguage(getDefaultMasterLanguage());
		c.setLanguage(getDefaultMasterLanguage());
		c.setLanguages( Language.getDefaultLanguages() );

		shortName.ifPresent(c::setShortName);
		address.ifPresent(c::setAddress);
		info.ifPresent(c::setInfo); // corregido: antes se usaba setAddress por error

		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);

		c.setMasterLanguage(getDefaultMasterLanguage());

		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy, AuditAction.CREATE));
		
		for (Language la : getLanguageService().getLanguages())
			getInstitutionRecordDBService().create(c, la.getLanguageCode(), createdBy);

		getRoleInstitutionDBService().create("admin", c, createdBy);

		return c;
	}
	

	@Transactional	
	public void save(Institution o, User user, String updatedPart) {
			super.save(o);
			getDelleMuseAuditDBService().save(DelleMuseAudit.of(o, user, AuditAction.UPDATE, updatedPart));
	}

	 
	@Transactional	
	public void save(Institution o, User user, List<String> updatedParts) {
			super.save(o);
			getDelleMuseAuditDBService().save(DelleMuseAudit.of(o, user, AuditAction.UPDATE, String.join(", ", updatedParts)));
		}
		

	@Transactional
	public void markAsDeleted(Institution c, User deletedBy) {
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(deletedBy);
		c.setState(ObjectState.DELETED);
		getRepository().save(c);
		
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, deletedBy, AuditAction.DELETE, AuditKey.MARK_AS_DELETED));
		
	}

	@Transactional
	public void restore(Institution c, User restoredBy) {
		OffsetDateTime date = OffsetDateTime.now();
		c.setLastModified(date);
		c.setLastModifiedUser(restoredBy);
		c.setState(ObjectState.EDITION);
		getRepository().save(c);
		
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, restoredBy, AuditAction.UPDATE, AuditKey.RESTORE));
		
	}


	
	
	
	
	
	@Transactional
	public Optional<Institution> findWithDeps(Long id) {

		Optional<Institution> o_i = super.findById(id);

		if (o_i.isEmpty())
			return o_i;

		Institution i = o_i.get();

		i.setDependencies(true);

		Resource photo = i.getPhoto();

		if (photo != null)
			photo.getBucketName();

		Resource logo = i.getLogo();

		if (logo != null)
			logo.getBucketName();

		return o_i;
	}

	@Transactional
	public Iterable<Institution> findAllSorted() {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Institution> cq = cb.createQuery(getEntityClass());
		Root<Institution> root = cq.from(getEntityClass());
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public Iterable<Institution> findAllSorted(ObjectState os) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Institution> cq = cb.createQuery(getEntityClass());
		Root<Institution> root = cq.from(getEntityClass());

		cq.select(root).where(cb.equal(root.get("state"), os));
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));

		getEntityManager().createQuery(cq).getResultList().forEach(c -> logger.debug(c.getName()));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public Iterable<Institution> findAllSorted(ObjectState os1, ObjectState os2) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Institution> cq = cb.createQuery(getEntityClass());
		Root<Institution> root = cq.from(getEntityClass());

		Predicate p1 = cb.equal(root.get("state"), os1);
		Predicate p2 = cb.equal(root.get("state"), os2);
		Predicate combinedPredicate = cb.or(p1, p2);
		cq.select(root).where(combinedPredicate);

		cq.orderBy(cb.asc(cb.lower(root.get("name"))));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public List<Site> getSites(Long institutionId) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Site> cq = cb.createQuery(Site.class);
		Root<Site> root = cq.from(Site.class);
		cq.select(root).where(cb.equal(root.get("institution").get("id"), institutionId));
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));

		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public Optional<Institution> findByShortName(String name) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Institution> cq = cb.createQuery(Institution.class);
		Root<Institution> root = cq.from(Institution.class);
		cq.select(root).where(cb.equal(root.get("shortName"), name));
		cq.orderBy(cb.asc(root.get("id")));

		List<Institution> list = entityManager.createQuery(cq).getResultList();
		return list == null || list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
	}

	@Transactional
	public List<Institution> getByName(String name) {
		return createNameQuery(name).getResultList();
	}

	@Override
	protected Class<Institution> getEntityClass() {
		return Institution.class;
	}

	@Override
	public String getObjectClassName() {
		return Institution.class.getSimpleName().toLowerCase();
	}

	protected InstitutionRecordDBService getInstitutionRecordDBService() {
		return this.institutionRecordDBService;
	}

	@PostConstruct
	protected void onInitialize() {
		super.registerRecordDB(getEntityClass(), getInstitutionRecordDBService());
		super.register(getEntityClass(), this);
	}
}
