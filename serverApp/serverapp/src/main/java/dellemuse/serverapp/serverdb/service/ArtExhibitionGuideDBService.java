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
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
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
    
    @Transactional
	public Optional<ArtExhibitionGuide> findByIdWithDeps(Long id) {

		Optional<ArtExhibitionGuide> o = super.findById(id);

		if (o.isEmpty())
			return o;

		ArtExhibitionGuide a = o.get();

		a.setDependencies(true);

		a.getArtExhibition().getDisplayname();
		
		Resource photo = a.getPhoto();
		
		if (photo != null)
			photo.getBucketName();

		return o;
	}

    
    @PostConstruct
    protected void onInitialize() {
    	super.register(getEntityClass(), this);
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


