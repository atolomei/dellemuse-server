package dellemuse.serverapp.serverdb.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.InstitutionType;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.PersistenceContext;
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

    private static final Logger logger = Logger.getLogger(InstitutionTypeDBService.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    public InstitutionTypeDBService(CrudRepository<InstitutionType, Long> repository, ServerDBSettings settings) {
        super(repository, settings);
    }

    @PostConstruct
    protected void onInitialize() {
    	super.register(getEntityClass(), this);
    	//ServiceLocator.getInstance().register(getEntityClass(), this);
    }
    
    @Transactional
    @Override
    public InstitutionType create(String name, User createdBy) {
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
    public List<Site> findSites(Long institutionId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Site> cq = cb.createQuery(Site.class);
        Root<Site> root = cq.from(Site.class);

        cq.select(root)
          .where(cb.equal(root.get("institution").get("id"), institutionId));

        cq.orderBy(cb.asc( cb.lower(root.get("name"))));

        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    public List<InstitutionType> getByName(String name) {
        return createNameQuery(name).getResultList();
    }

    @Override
    protected Class<InstitutionType> getEntityClass() {
        return InstitutionType.class;
    }
}


 