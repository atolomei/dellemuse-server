package dellemuse.serverapp.serverdb.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;

import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class ArtExhibitionDBService extends DBService<ArtExhibition, Long> {

    private static final Logger logger = Logger.getLogger(ArtExhibitionDBService.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    public ArtExhibitionDBService(CrudRepository<ArtExhibition, Long> repository, ServerDBSettings settings) {
        super(repository, settings);
    }

    
    @PostConstruct
    protected void onInitialize() {
    	super.register(getEntityClass(), this);
    	//ServiceLocator.getInstance().register(getEntityClass(), this);
    }
    
    @Transactional
    @Override
    public ArtExhibition create(String name, User createdBy) {
        ArtExhibition c = new ArtExhibition();
        c.setName(name);
        c.setNameKey(nameKey(name));
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        return getRepository().save(c);
    }

    @Transactional
    public List<ArtExhibition> getByName(String name) {
        return createNameQuery(name).getResultList();
    }

    @Transactional
    public Optional<ArtExhibition> findByNameKey(String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ArtExhibition> cq = cb.createQuery(ArtExhibition.class);
        Root<ArtExhibition> root = cq.from(ArtExhibition.class);
        cq.select(root).where(cb.equal(root.get("nameKey"), name));
        cq.orderBy(cb.asc(root.get("id")));

        List<ArtExhibition> list = entityManager.createQuery(cq).getResultList();
        return list == null || list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Transactional
    public List<ArtExhibitionGuide> getArtExhibitionGuides(ArtExhibition exhibition) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ArtExhibitionGuide> cq = cb.createQuery(ArtExhibitionGuide.class);
        Root<ArtExhibitionGuide> root = cq.from(ArtExhibitionGuide.class);
        cq.select(root).where(cb.equal(root.get("artExhibition").get("id"), exhibition.getId()));
        cq.orderBy(cb.asc(root.get("title")));

        return entityManager.createQuery(cq).getResultList();
    }

    @Transactional
    public List<ArtExhibitionItem> getArtExhibitionItem(ArtExhibition exhibition) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ArtExhibitionItem> cq = cb.createQuery(ArtExhibitionItem.class);
        Root<ArtExhibitionItem> root = cq.from(ArtExhibitionItem.class);
        cq.select(root).where(cb.equal(root.get("artExhibition").get("id"), exhibition.getId()));
        cq.orderBy(cb.asc(root.get("title")));

        return entityManager.createQuery(cq).getResultList();
    }

    @Transactional
    public List<ArtExhibition> getArtExhibitions(Site site) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ArtExhibition> cq = cb.createQuery(ArtExhibition.class);
        Root<ArtExhibition> root = cq.from(ArtExhibition.class);
        cq.select(root).where(cb.equal(root.get("site").get("id"), site.getId()));
        cq.orderBy(cb.asc(root.get("title")));

        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    protected Class<ArtExhibition> getEntityClass() {
        return ArtExhibition.class;
    }
}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * 
     * @Service
public class ArtExhibitionDBService extends DBService<ArtExhibition, Long> {




    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(ArtExhibitionDBService.class.getName());

    public ArtExhibitionDBService(CrudRepository<ArtExhibition, Long> repository, EntityManagerFactory entityManagerFactory,
            Settings settings) {
        super(repository, entityManagerFactory, settings);
    }

    @Transactional
    @Override
    public ArtExhibition create(String name, User createdBy) {
        ArtExhibition c = new ArtExhibition();
        c.setName(name);
        c.setNameKey(nameKey(name));
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        return getRepository().save(c);
    }
    

    @Transactional
    public List<ArtExhibition> getByName(String name) {
        return createNameQuery(name).getResultList();
    }

    @PersistenceContext
    private EntityManager entityManager;
    
    
    @Transactional
    public Optional<ArtExhibition> findByNameKey(String name) {
        
        TypedQuery<ArtExhibition> query;
        CriteriaBuilder criteriabuilder = getSessionFactory().getCurrent Session().getCriteriaBuilder();
        CriteriaQuery<ArtExhibition> criteria = criteriabuilder.createQuery(ArtExhibition.class);
        Root<ArtExhibition> loaders = criteria.from(ArtExhibition.class);
        criteria.orderBy(criteriabuilder.asc(loaders.get("id")));
        ParameterExpression<String> idparameter = criteriabuilder.parameter(String.class);
        criteria.select(loaders).where(criteriabuilder.equal(loaders.get("nameKey"), idparameter));
        query = getSessionFactory().getCurrent Session().createQuery(criteria);
        query.setHint("org.hibernate.cacheable", true);
        query.setFlushMode(FlushModeType.COMMIT);
        query.setParameter(idparameter, name);
        List<ArtExhibition> list = query.getResultList();
        if (list == null || list.isEmpty())
            return Optional.empty();
        return Optional.of(list.get(0));
    }
    
    @Override
    protected Class<ArtExhibition> getEntityClass() {
        return ArtExhibition.class;
    }

    @Transactional
    public List<ArtExhibitionGuide> getArtExhibitionGuides(ArtExhibition exhibition) {

        TypedQuery<ArtExhibitionGuide> query;
        CriteriaBuilder criteriabuilder = getSessionFactory().getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<ArtExhibitionGuide> criteria = criteriabuilder.createQuery(ArtExhibitionGuide.class);
        Root<ArtExhibitionGuide> loaders = criteria.from(ArtExhibitionGuide.class);
        criteria.orderBy(criteriabuilder.asc(loaders.get("title")));
        ParameterExpression<Long> idparameter = criteriabuilder.parameter(Long.class);
        criteria.select(loaders).where(criteriabuilder.equal(loaders.get("artExhibition").get("id"), idparameter));
        query = getSessionFactory().getCurrentSession().createQuery(criteria);
        query.setHint("org.hibernate.cacheable", true);
        query.setFlushMode(FlushModeType.COMMIT);
        query.setParameter(idparameter, exhibition.getId());
        
        return query.getResultList();
    }
    
    @Transactional
    public List<ArtExhibitionItem> getArtExhibitionItem(ArtExhibition exhibition) {
        TypedQuery<ArtExhibitionItem> query;
        CriteriaBuilder criteriabuilder = getSessionFactory().getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<ArtExhibitionItem> criteria = criteriabuilder.createQuery(ArtExhibitionItem.class);
        Root<ArtExhibitionItem> loaders = criteria.from(ArtExhibitionItem.class);
        criteria.orderBy(criteriabuilder.asc(loaders.get("title")));
        ParameterExpression<Long> idparameter = criteriabuilder.parameter(Long.class);
        criteria.select(loaders).where(criteriabuilder.equal(loaders.get("artExhibition").get("id"), idparameter));
        query = getSessionFactory().getCurrentSession().createQuery(criteria);
        query.setHint("org.hibernate.cacheable", true);
        query.setFlushMode(FlushModeType.COMMIT);
        query.setParameter(idparameter, exhibition.getId());
        return query.getResultList();
    }

    
    @Transactional
    public List<ArtExhibition> getArtExhibitions(Site site) {
        TypedQuery<ArtExhibition> query;
        CriteriaBuilder criteriabuilder = getSessionFactory().getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<ArtExhibition> criteria = criteriabuilder.createQuery(ArtExhibition.class);
        
        Root<ArtExhibition> loaders = criteria.from(ArtExhibition.class);

        criteria.orderBy(criteriabuilder.asc(loaders.get("title")));
        
        ParameterExpression<Long> idparameter = criteriabuilder.parameter(Long.class);
        criteria.select(loaders).where(criteriabuilder.equal(loaders.get("site").get("id"), idparameter));
        query = getSessionFactory().getCurrentSession().createQuery(criteria);
        query.setHint("org.hibernate.cacheable", true);
        query.setFlushMode(FlushModeType.COMMIT);
        query.setParameter(idparameter, site.getId());
        return query.getResultList();
    }


    
    
*/

