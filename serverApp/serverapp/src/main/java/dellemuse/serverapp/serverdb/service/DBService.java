package dellemuse.serverapp.serverdb.service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.DelleMuseObject;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.base.BaseService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.SystemService;
import jakarta.annotation.PostConstruct;
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

    @JsonIgnore
    static final private ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

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

	public static DBService<?, Long> getDBService( Class<?> entityClass ) {
		return map.get(entityClass);
	}
    
    
    public DBService(CrudRepository<T, I> repository, ServerDBSettings settings) {
        super(repository, settings);
        this.repository = repository;
    }

    public abstract T create(String name, User createdBy);

    @Transactional
    public <S extends T> S save(S entity) {
    	entity.setLastModified(OffsetDateTime.now());
   	    return repository.save(entity);
    }

    @Transactional
    public Optional<T> findById(I id) {
        return repository.findById(id);
    }
    
    public Optional<T> findByIdWithDeps(I id) {
    	return null;
    }
    
    

    @Transactional
    public boolean existsById(I id) {
        return repository.existsById(id);
    }

    @Transactional
    public Iterable<T> findAll() {
        return repository.findAll();
    }

    @Transactional
    public Iterable<T> findAllSorted() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(getEntityClass());
        Root<T> root = cq.from(getEntityClass());
        cq.orderBy(cb.asc(root.get("title")));

        return entityManager.createQuery(cq).getResultList();
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

    public CrudRepository<T, I> getRepository() {
        return repository;
    }

    public TypedQuery<T> createNameQuery(String name) {
        return createNameQuery(name, false);
    }
    
    public TypedQuery<T> createNameKeyQuery(String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(getEntityClass());
        Root<T> root = cq.from(getEntityClass());
        cq.select(root).where(cb.equal(root.get(getNameKeyColumn()), name));

        return entityManager.createQuery(cq);
    }

    public TypedQuery<T> createNameQuery(String name, boolean isLike) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(getEntityClass());
        Root<T> root = cq.from(getEntityClass());

        if (isLike) {
            cq.select(root).where(cb.like(cb.lower(root.get(getNameColumn())), "%" + name.toLowerCase() + "%"));
        } else {
            cq.select(root).where(cb.equal(cb.lower(root.get(getNameColumn())), name.toLowerCase()));
        }

        return entityManager.createQuery(cq);
    }

	public EntityManager getEntityManager() {
		return entityManager;
	}

	
    protected abstract Class<T> getEntityClass();

    protected String getNameColumn() {
        return "name";
    }

    protected String getNameKeyColumn() {
        return "nameKey";
    }

    public String normalize(String name) {
        return this.getEntityClass().getSimpleName().toLowerCase() + "-" + name.toLowerCase().trim();
    }

    protected String nameKey(String name) {
    	if (name==null)
    		return null;
        return name.toLowerCase().replaceAll("[^a-z0-9]+", "-") // Replace non-ASCII alphanumerics with hyphen
                .replaceAll("(^-+|-+$)", ""); // Trim leading/trailing hyphens
    }

}
