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
import dellemuse.serverapp.serverdb.model.DelleMuseObject;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.SystemService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
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

	public DBService(CrudRepository<T, I> repository, ServerDBSettings settings) {
		super(repository, settings);
		this.repository = repository;
	}

	public abstract T create(String name, User createdBy);

	
	@SuppressWarnings("unchecked")
	@Transactional
	public <S extends T> S saveViaBaseClass(DelleMuseObject entity) {
		entity.setLastModified(OffsetDateTime.now());
		return repository.save((S) entity);
	}

	
	@Transactional
	public <S extends T> S save(S entity) {
		entity.setLastModified(OffsetDateTime.now());
		return repository.save(entity);
	}

	@Transactional
	public Optional<T> findById(I id) {
		return repository.findById(id);
	}

	public Optional<T> findWithDeps(I id) {
		logger.error("findWithDeps ia null");
		return null;
	}

	@Transactional
	public boolean existsById(I id) {
		return repository.existsById(id);
	}

	@Transactional
	public void delete(I id) {
		repository.deleteById(id); 
	}

	@Transactional
	public void delete(T o) {
		repository.delete(o); 
	}

	
	@Transactional
	public Iterable<T> findAll() {
		return repository.findAll();
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
	public void flush() {
		 getEntityManager().flush();
	}

	@Transactional
	public void evict(T o) {
		logger.debug("evict -> " + o.getDisplayname());
		getEntityManager().detach(o);
	}
	
	@Transactional
	public List<T> getByNameKey(String name) {
		return createNameKeyQuery(name).getResultList();
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

	public TypedQuery<T> createNameKeyQuery(String name) {
		CriteriaBuilder cb =  getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(getEntityClass());
		Root<T> root = cq.from(getEntityClass());
		cq.select(root).where(cb.equal(root.get(getNameKeyColumn()), name));

		return  getEntityManager().createQuery(cq);
	}

	public TypedQuery<T> createNameQuery(String name, boolean isLike) {
		CriteriaBuilder cb =  getEntityManager().getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(getEntityClass());
		Root<T> root = cq.from(getEntityClass());

		if (isLike) {
			cq.select(root).where(cb.like(cb.lower(root.get(getNameColumn())), "%" + name.toLowerCase() + "%"));
		} else {
			cq.select(root).where(cb.equal(cb.lower(root.get(getNameColumn())), name.toLowerCase()));
		}

		return  getEntityManager().createQuery(cq);
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

	protected String getNameColumn() {
		return "name";
	}

	protected String getNameKeyColumn() {
		return "nameKey";
	}

	protected ArtExhibitionItemDBService getArtExhibitionItemDBService() {
		return (ArtExhibitionItemDBService) ServiceLocator.getInstance().getBean(ArtExhibitionItemDBService.class);
	}

 	protected ArtExhibitionDBService getArtExhibitionDBService() {
		return (ArtExhibitionDBService) ServiceLocator.getInstance().getBean(ArtExhibitionDBService.class);
	}

	protected ResourceDBService getResourceDBService() {
		return (ResourceDBService) ServiceLocator.getInstance().getBean(ResourceDBService.class);
	}
	
	protected UserDBService getUserDBService() {
		return (UserDBService) ServiceLocator.getInstance().getBean(UserDBService.class);
	}
 	
	protected String nameKey(String name) {
		if (name == null)
			return null;
		return name.toLowerCase().replaceAll("[^a-z0-9]+", "-") // Replace non-ASCII alphanumerics with hyphen
				.replaceAll("(^-+|-+$)", ""); // Trim leading/trailing hyphens
	}


	

}
