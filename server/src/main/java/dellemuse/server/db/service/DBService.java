package dellemuse.server.db.service;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dellemuse.server.BaseService;
import dellemuse.server.Settings;
import dellemuse.server.SystemService;
import dellemuse.server.db.model.User;
import dellemuse.model.logging.Logger;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;

/**
 * 
 * 
 * @param <T> Type
 * @param <M> ModelType
 * @param <I> Type Index
 */

public abstract class DBService<T, I> extends BaseService implements SystemService {

    @JsonIgnore
    static final private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(DBService.class.getName());

    static {
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @JsonIgnore
    @Autowired
    private final CrudRepository<T, I> repository;

    @JsonIgnore
    @Autowired
    EntityManagerFactory entityManagerFactory;


    public DBService(CrudRepository<T, I> repository, EntityManagerFactory entityManagerFactory, Settings settings) {
        super(settings);
        this.repository = repository;
        this.entityManagerFactory = entityManagerFactory;
    }

    public abstract T create(String name, User createdBy);

    
    public <S extends T> S save(S entity) {
        return getRepository().save(entity);
    }

    public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
        return getRepository().saveAll(entities);
    }

    public Optional<T> findById(I id) {
        return getRepository().findById(id);
    }

    public boolean existsById(I id) {
        return getRepository().existsById(id);
    }

    public Iterable<T> findAll() {
        return getRepository().findAll();
    }

    public Iterable<T> findAllById(Iterable<I> ids) {
        return getRepository().findAllById(ids);
    }

    public long count() {
        return getRepository().count();
    }

    public void deleteById(I id) {
        getRepository().deleteById(id);
    }

    public void delete(T entity) {
        getRepository().delete(entity);
    }

    public void deleteAllById(Iterable<? extends I> ids) {
        getRepository().deleteAllById(ids);
    }

    public void deleteAll(Iterable<? extends T> entities) {
        getRepository().deleteAll(entities);
    }

    public void deleteAll() {
        getRepository().deleteAll();
    }

    
    public TypedQuery<T> createNameQuery() {
        TypedQuery<T> query;
        CriteriaBuilder criteriabuilder = getSessionFactory().getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<T> criteria = criteriabuilder.createQuery(getEntityClass());
        Root<T> loaders = criteria.from(getEntityClass());
        ParameterExpression<String> nameparameter = criteriabuilder.parameter(String.class);
        criteria.select(loaders).where(criteriabuilder.equal(loaders.get(getNameColumn()), nameparameter));
        query = getSessionFactory().getCurrentSession().createQuery(criteria);
        query.setHint("org.hibernate.cacheable", true);
        query.setFlushMode(FlushModeType.COMMIT);
        query.setParameter(nameparameter, "root");
        return query;
    }

    public List<T> getByName(String name) {
        return createNameQuery().getResultList();
    }

    /**
     * Set up by Spring
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    public CrudRepository<T, I> getRepository() {
        return repository;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(this.getClass().getSimpleName());
        str.append(toJSON());
        return str.toString();
    }

    public String normalize(String name) {
        return this.getEntityClass().getSimpleName().toLowerCase() + "-" + name.toLowerCase().trim();
    }
    
    protected abstract Class<T> getEntityClass();
    
    protected String getNameColumn() {
        return "name";
    }

    // @Bean
    // public SessionFactory getSessionFactory() {
    // if (entityManagerFactory.unwrap(SessionFactory.class) == null) {
    // throw new NullPointerException("factory is not a hibernate factory");
    // }
    // return entityManagerFactory.unwrap(SessionFactory.class);
    // }

}
