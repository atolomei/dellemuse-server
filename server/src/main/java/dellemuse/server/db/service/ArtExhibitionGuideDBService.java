package dellemuse.server.db.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.server.Settings;
import dellemuse.server.db.model.ArtExhibition;
import dellemuse.server.db.model.ArtExhibitionGuide;
import dellemuse.server.db.model.ArtExhibitionItem;
import dellemuse.server.db.model.GuideContent;
import dellemuse.server.db.model.Person;
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

@Service
public class ArtExhibitionGuideDBService extends DBService<ArtExhibitionGuide, Long> {

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(ArtExhibitionGuideDBService.class.getName());

    public ArtExhibitionGuideDBService(CrudRepository<ArtExhibitionGuide, Long> repository,
            EntityManagerFactory entityManagerFactory, Settings settings) {
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
    public ArtExhibitionGuide create(String name, User createdBy) {
        ArtExhibitionGuide c = new ArtExhibitionGuide();
        c.setName(name);
        c.setNameKey(normalize(name));
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        return getRepository().save(c);
    }

    /**
     * @param name
     * @return
     */
    public List<ArtExhibitionGuide> getByName(String name) {
        return createNameQuery().getResultList();
    }

    
    @Transactional
    public List<GuideContent> getArtExhibitionGuide(ArtExhibitionGuide exhibitionGuide) {
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
        query.setParameter(idparameter, exhibitionGuide.getId());
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
    
    
}
