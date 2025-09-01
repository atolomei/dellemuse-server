package dellemuse.serverdb.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.serverdb.ServerDBSettings;
import dellemuse.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverdb.model.GuideContent;
import dellemuse.serverdb.model.Person;
import dellemuse.serverdb.model.User;
import dellemuse.serverdb.service.base.ServiceLocator;
import dellemuse.model.logging.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;


@Service
public class ArtExhibitionGuideDBService extends DBService<ArtExhibitionGuide, Long> {

    private static final Logger logger = Logger.getLogger(ArtExhibitionGuideDBService.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    public ArtExhibitionGuideDBService(CrudRepository<ArtExhibitionGuide, Long> repository, ServerDBSettings settings) {
        super(repository, settings);
    }
    
    @PostConstruct
    protected void onInitialize() {
    	ServiceLocator.getInstance().register(getEntityClass(), this);
    }

    @Transactional
    @Override
    public ArtExhibitionGuide create(String name, User createdBy) {
        ArtExhibitionGuide c = new ArtExhibitionGuide();
        c.setName(name);
        c.setNameKey(nameKey(name));
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        return getRepository().save(c);
    }

    @Transactional
    public List<ArtExhibitionGuide> getByName(String name) {
        return createNameQuery(name).getResultList();
    }

    @Transactional
    public List<ArtExhibitionGuide> findByNameKey(String nameKey) {
        return getByNameKey(nameKey);
    }

    @Transactional
    public List<GuideContent> getArtExhibitionGuideContents(ArtExhibitionGuide exhibitionGuide) {
        return getArtExhibitionGuideContents(exhibitionGuide.getId());
    }

    @Transactional
    public List<GuideContent> getArtExhibitionGuideContents(Long guideId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<GuideContent> cq = cb.createQuery(GuideContent.class);
        Root<GuideContent> root = cq.from(GuideContent.class);

        cq.select(root).where(cb.equal(root.get("artExhibitionGuide").get("id"), guideId));
        cq.orderBy(cb.asc(root.get("title")));

        return entityManager.createQuery(cq).getResultList();
    }

    @Transactional
    public List<GuideContent> getArtExhibitionGuidePublishedBy(Person person) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<GuideContent> cq = cb.createQuery(GuideContent.class);
        Root<GuideContent> root = cq.from(GuideContent.class);

        cq.select(root).where(cb.equal(root.get("publisher").get("id"), person.getId()));
        cq.orderBy(cb.asc(root.get("title")));

        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    protected Class<ArtExhibitionGuide> getEntityClass() {
        return ArtExhibitionGuide.class;
    }
}




/**
@Service
public class ArtExhibitionGuideDBService extends DBService<ArtExhibitionGuide, Long> {

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(ArtExhibitionGuideDBService.class.getName());

    public ArtExhibitionGuideDBService(CrudRepository<ArtExhibitionGuide, Long> repository,
            EntityManagerFactory entityManagerFactory, Settings settings) {
        super(repository, entityManagerFactory, settings);
    }

    
    @Transactional
    @Override
    public ArtExhibitionGuide create(String name, User createdBy) {
        ArtExhibitionGuide c = new ArtExhibitionGuide();
        c.setName(name);
        c.setNameKey(nameKey(name));
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        return getRepository().save(c);
    }

    
    
    @Transactional
    public List<ArtExhibitionGuide> getByName(String name) {
        return createNameQuery(name).getResultList();
    }

    @Transactional
    public List<ArtExhibitionGuide> findByNameKey(String nameKey) {
        return getByNameKey(nameKey);
    }

 

    @Transactional
    public List<GuideContent> getArtExhibitionGuideContents(ArtExhibitionGuide exhibitionGuide) {
        return  getArtExhibitionGuideContents(exhibitionGuide.getId());
    }

    @Transactional
    public List<GuideContent> getArtExhibitionGuideContents(Long guideid) {
        TypedQuery<GuideContent> query;
        CriteriaBuilder criteriabuilder = getSessionFactory().getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<GuideContent> criteria = criteriabuilder.createQuery(GuideContent.class);
        Root<GuideContent> loaders = criteria.from(GuideContent.class);
        
        criteria.orderBy(criteriabuilder.asc(loaders.get("title")));
        
        ParameterExpression<Long> idparameter = criteriabuilder.parameter(Long.class);
        criteria.select(loaders).where(criteriabuilder.equal(loaders.get("artExhibitionGuide").get("id"), idparameter));
        query = getSessionFactory().getCurrentSession().createQuery(criteria);
        query.setHint("org.hibernate.cacheable", true);
        query.setFlushMode(FlushModeType.COMMIT);
        query.setParameter(idparameter, guideid);
        return query.getResultList();
    }

    @Transactional
    public List<GuideContent> getArtExhibitionGuidePublishedBy(Person person) {
        TypedQuery<GuideContent> query;
        CriteriaBuilder criteriabuilder = getSessionFactory().getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<GuideContent> criteria = criteriabuilder.createQuery(GuideContent.class);
        Root<GuideContent> loaders = criteria.from(GuideContent.class);

        criteria.orderBy(criteriabuilder.asc(loaders.get("title")));
        
        ParameterExpression<Long> idparameter = criteriabuilder.parameter(Long.class);
        criteria.select(loaders).where(criteriabuilder.equal(loaders.get("publisher").get("id"), idparameter));
        query = getSessionFactory().getCurrentSession().createQuery(criteria);
        query.setHint("org.hibernate.cacheable", true);
        query.setFlushMode(FlushModeType.COMMIT);
        query.setParameter(idparameter, person.getId());
        return query.getResultList();
    }

    @Override
    protected Class<ArtExhibitionGuide> getEntityClass() {
        return ArtExhibitionGuide.class;
    }

**/


