package dellemuse.server.db.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.server.Settings;
import dellemuse.server.db.model.Institution;
import dellemuse.server.db.model.InstitutionType;
import dellemuse.server.db.model.Site;
import dellemuse.server.db.model.User;
import dellemuse.model.logging.Logger;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;


/**
*  <S extends T> S save(S entity);
*  <S extends T> Iterable<S> saveAll(Iterable<S> entities);
*  Optional<T> findById(ID id);
*  boolean existsById(ID id);
*  Iterable<T> findAll();
*  Iterable<T> findAllById(Iterable<ID> ids);
*  long count();
*  void deleteById(ID id);
*  void delete(T entity);
*  void deleteAllById(Iterable<? extends ID> ids);
*  void deleteAll(Iterable<? extends T> entities);
*  void deleteAll();
**/

@Service
public class InstitutionTypeDBService extends DBService<InstitutionType, Long> {

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(InstitutionTypeDBService.class.getName());

    public InstitutionTypeDBService(CrudRepository<InstitutionType, Long> repository, EntityManagerFactory entityManagerFactory, Settings settings) {
        super(repository, entityManagerFactory, settings);
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
    @Override
    public InstitutionType create(String name,User createdBy) {
        InstitutionType c = new InstitutionType();
        c.setName(name);
        c.setNameKey(nameKey(name));
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        return getRepository().save(c);
    }

    
    @Transactional
    public List<Site> findSites(Institution institution) {
        return findSites(institution.getId());
    }

    
    @Transactional
    public List<Site> findSites(Long institutionid) {
        TypedQuery<Site> query;
        CriteriaBuilder criteriabuilder = getSessionFactory().getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Site> criteria = criteriabuilder.createQuery(Site.class);
        Root<Site> loaders = criteria.from(Site.class);
        criteria.orderBy(criteriabuilder.asc(loaders.get("title")));
        ParameterExpression<Long> idparameter = criteriabuilder.parameter(Long.class);
        criteria.select(loaders).where(criteriabuilder.equal(loaders.get("institution.id"), idparameter));
        query = getSessionFactory().getCurrentSession().createQuery(criteria);
        query.setHint("org.hibernate.cacheable", true);
        query.setFlushMode(FlushModeType.COMMIT);
        query.setParameter(idparameter, institutionid);
        return query.getResultList();

    }

    
    
    
    /**
     * @param name
     * @return
     */
    @Override
    public List<InstitutionType> getByName(String name) {
        return createNameQuery(name).getResultList();
    }

    @Override
    protected Class<InstitutionType> getEntityClass() {
        return InstitutionType.class;
    }
}
