package dellemuse.serverapp.serverdb.service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.DellemuseObjectMapper;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.audit.AuditKey;
import dellemuse.serverapp.serverdb.model.AuditAction;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.DelleMuseObject;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionSectionRecord;
import dellemuse.serverapp.serverdb.model.record.InstitutionRecord;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.serverdb.service.record.ArtExhibitionGuideRecordDBService;
import dellemuse.serverapp.serverdb.service.record.ArtExhibitionRecordDBService;
 
import dellemuse.serverapp.serverdb.service.record.GuideContentRecordDBService;
import dellemuse.serverapp.serverdb.service.security.RoleGeneralDBService;
import dellemuse.serverapp.serverdb.service.security.RoleInstitutionDBService;
import dellemuse.serverapp.serverdb.service.security.RoleSiteDBService;
import dellemuse.serverapp.service.SystemService;
import dellemuse.serverapp.service.language.LanguageService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

/**
 * 
 * @param <T> Type
 * @param <M> ModelType
 * @param <I> Type Index
 */

public abstract class DBService<T extends DelleMuseObject, I> extends BaseDBService<T, I> implements SystemService {

	static private Logger logger = Logger.getLogger(DBService.class.getName());

	@JsonIgnore
	static final private ObjectMapper mapper = new DellemuseObjectMapper();

	@JsonIgnore
	@Autowired
	private final CrudRepository<T, I> repository;

	@JsonIgnore
	@PersistenceContext
	private EntityManager entityManager;

	private static Map<Class<?>, DBService<?, Long>> map = new HashMap<Class<?>, DBService<?, Long>>();

	public static void register(Class<?> entityClass, DBService<?, Long> dbService) {
		map.put(entityClass, dbService);
	}

	public static DBService<?, Long> getDBService(Class<?> entityClass) {
		return map.get(entityClass);
	}

	/**
	 * @param repository
	 * @param settings
	 */
	public DBService(CrudRepository<T, I> repository, ServerDBSettings settings) {
		super(repository, settings);
		this.repository = repository;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public <S extends T> S saveViaBaseClass(DelleMuseObject entity, User user, String auditMsg) {
		entity.setLastModified(OffsetDateTime.now());

		getDelleMuseAuditDBService().save(DelleMuseAudit.of(entity, user, AuditAction.UPDATE, auditMsg));
		return getRepository().save((S) entity);
	}

	@Transactional
	public <S extends T> S save(S entity, String auditMsg, User user) {

		OffsetDateTime now = OffsetDateTime.now();
		entity.setLastModified(now);

		getDelleMuseAuditDBService().save(DelleMuseAudit.of(entity, user, AuditAction.UPDATE, auditMsg));
		return getRepository().save(entity);
	}

	/**@Transactional
	public <S extends T> void save(S o, User user, List<String> updatedParts) {
		super.save(o);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(o, user, AuditAction.UPDATE, String.join(", ", updatedParts)));
	}**/

	@Transactional
	public <S extends T> S save(S entity, User user) {

		OffsetDateTime now = OffsetDateTime.now();
		entity.setLastModified(now);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(entity, user, AuditAction.UPDATE));
		return getRepository().save(entity);
	}

	@Transactional
	public void delete(T c, User user) {
		 
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, user, AuditAction.DELETE));
		getRepository().delete(c);
	}

	@Transactional
	public void markAsDeleted(T c, User deletedBy) {

		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(deletedBy);
		c.setState(ObjectState.DELETED);

		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, deletedBy, AuditAction.DELETE, AuditKey.MARK_AS_DELETED));

	}

	@Transactional
	public void restore(T c, User restoredBy) {
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(restoredBy);
		c.setState(ObjectState.EDITION);

		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, restoredBy, AuditAction.UPDATE, AuditKey.RESTORE));
	}

	@Transactional
	public Iterable<T> findAll() {
		return getRepository().findAll();
	}

	@Transactional
	public Optional<T> findById(I id) {
		return getRepository().findById(id);
	}

	public Optional<T> findWithDeps(I id) {
		logger.error("findWithDeps ia null");
		return null;
	}

	@Transactional
	public boolean existsById(I id) {
		return getRepository().existsById(id);
	}

	@Transactional
	public Iterable<T> findAllSorted() {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(getEntityClass());
		Root<T> root = cq.from(getEntityClass());
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public Iterable<T> findAllSorted(ObjectState os) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(getEntityClass());
		Root<T> root = cq.from(getEntityClass());
		cq.select(root).where(cb.equal(root.get("state"), os));
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public Iterable<T> findAllSorted(ObjectState os1, ObjectState os2) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(getEntityClass());
		Root<T> root = cq.from(getEntityClass());
		Predicate p1 = cb.equal(root.get("state"), os1);
		Predicate p2 = cb.equal(root.get("state"), os2);
		Predicate combinedPredicate = cb.or(p1, p2);
		cq.select(root).where(combinedPredicate);
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public void flush() {
		getEntityManager().flush();
	}

	@Transactional
	public void evict(T o) {
		logger.debug("evict -> " + o.getDisplayname());
		getEntityManager().detach(o);
	}

	@Transactional
	public List<T> getByName(String name) {
		return createNameQuery(name, false).getResultList();
	}

	@Transactional
	public List<T> getNameLike(String name) {
		return createNameQuery(name, true).getResultList();
	}

	public TypedQuery<T> createNameQuery(String name) {
		return createNameQuery(name, false);
	}

	public TypedQuery<T> createNameQuery(String name, boolean isLike) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(getEntityClass());
		Root<T> root = cq.from(getEntityClass());
		if (isLike) {
			cq.select(root).where(cb.like(cb.lower(root.get(getNameColumn())), "%" + name.toLowerCase() + "%"));
		} else {
			cq.select(root).where(cb.equal(cb.lower(root.get(getNameColumn())), name.toLowerCase()));
		}
		return getEntityManager().createQuery(cq);
	}

	@Transactional
	public void initAudit() {
		this.findAll().forEach(i -> {
			List<DelleMuseAudit> list = getDelleMuseAuditDBService().getAudit(i.getId(), i.getObjectClassName());
			if (list == null || list.isEmpty()) {
				logger.debug("Adding Audit to -> " + i.getName());
				getDelleMuseAuditDBService().save(DelleMuseAudit.of(i, getUserDBService().findRoot(), AuditAction.CREATE));
			}
		});
	}

	public String normalize(String name) {
		return this.getEntityClass().getSimpleName().toLowerCase() + "-" + name.toLowerCase().trim();
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public CrudRepository<T, I> getRepository() {
		return repository;
	}

	protected abstract Class<T> getEntityClass();

	public String getObjectClassName() {
		return this.getEntityClass().getSimpleName().toLowerCase();
	}

	protected String getNameColumn() {
		return "name";
	}
	
   protected  ArtWorkDBService getArtWorkDBService() {
    	return (ArtWorkDBService) ServiceLocator.getInstance().getBean(ArtWorkDBService.class);
	}

	protected ArtExhibitionItemDBService getArtExhibitionItemDBService() {
		return (ArtExhibitionItemDBService) ServiceLocator.getInstance().getBean(ArtExhibitionItemDBService.class);
	}

	protected ArtExhibitionDBService getArtExhibitionDBService() {
		return (ArtExhibitionDBService) ServiceLocator.getInstance().getBean(ArtExhibitionDBService.class);
	}
	
	protected InstitutionDBService getInstitutionDBService() {
		return (InstitutionDBService) ServiceLocator.getInstance().getBean(InstitutionDBService.class);
	}

	protected ResourceDBService getResourceDBService() {
		return (ResourceDBService) ServiceLocator.getInstance().getBean(ResourceDBService.class);
	}

	protected UserDBService getUserDBService() {
		return (UserDBService) ServiceLocator.getInstance().getBean(UserDBService.class);
	}

	protected LanguageService getLanguageService() {
		return (LanguageService) ServiceLocator.getInstance().getBean(LanguageService.class);
	}

	protected ArtExhibitionRecordDBService getArtExhibitionRecordDBService() {
		return (ArtExhibitionRecordDBService) ServiceLocator.getInstance().getBean(ArtExhibitionRecordDBService.class);
	}

	protected ArtExhibitionGuideDBService getArtExhibitionGuideDBService() {
		return (ArtExhibitionGuideDBService) ServiceLocator.getInstance().getBean(ArtExhibitionGuideDBService.class);
	}


	protected ArtExhibitionSectionDBService getArtExhibitionSectionDBService() {
		return (ArtExhibitionSectionDBService) ServiceLocator.getInstance().getBean(ArtExhibitionSectionDBService.class);
	}
	
	
	protected ArtExhibitionGuideRecordDBService getArtExhibitionGuideRecordDBService() {
		return (ArtExhibitionGuideRecordDBService) ServiceLocator.getInstance().getBean(ArtExhibitionGuideRecordDBService.class);
	}

	protected GuideContentDBService getGuideContentDBService() {
		return (GuideContentDBService) ServiceLocator.getInstance().getBean(GuideContentDBService.class);
	}

	protected GuideContentRecordDBService getGuideContentRecordDBService() {
		return (GuideContentRecordDBService) ServiceLocator.getInstance().getBean(GuideContentRecordDBService.class);
	}

	protected AudioStudioDBService getAudioStudioDBService() {
		return (AudioStudioDBService) ServiceLocator.getInstance().getBean(AudioStudioDBService.class);
	}

	protected DelleMuseAuditDBService getDelleMuseAuditDBService() {
		return (DelleMuseAuditDBService) ServiceLocator.getInstance().getBean(DelleMuseAuditDBService.class);
	}

	protected PersonDBService getPersonDBService() {
		return (PersonDBService) ServiceLocator.getInstance().getBean(PersonDBService.class);
	}
	
	protected RoleGeneralDBService getRoleGeneralDBService() {
		return (RoleGeneralDBService) ServiceLocator.getInstance().getBean(RoleGeneralDBService.class);
	}

	protected RoleInstitutionDBService getRoleInstitutionDBService() {
		return (RoleInstitutionDBService) ServiceLocator.getInstance().getBean(RoleInstitutionDBService.class);
	}

	protected RoleSiteDBService getRoleSiteDBService() {
		return (RoleSiteDBService) ServiceLocator.getInstance().getBean(RoleSiteDBService.class);
	}

	protected SiteDBService getSiteDBService() {
		return (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
	}

	protected String getDefaultMasterLanguage() {
		return getLanguageService().getDefaultLanguage().getLanguageCode();
	}

	protected String nameKey(String name) {
		if (name == null)
			return null;
		return name.toLowerCase().replaceAll("[^a-z0-9]+", "-") // Replace non-ASCII alphanumerics with hyphen
				.replaceAll("(^-+|-+$)", ""); // Trim leading/trailing hyphens
	}
}
