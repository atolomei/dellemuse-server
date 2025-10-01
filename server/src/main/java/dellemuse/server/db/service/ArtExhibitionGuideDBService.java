package dellemuse.server.db.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.server.Settings;
import dellemuse.server.db.model.ArtExhibitionGuide;
import dellemuse.server.db.model.GuideContent;
import dellemuse.server.db.model.Person;
import dellemuse.server.db.model.User;
import dellemuse.model.logging.Logger;
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

    public ArtExhibitionGuideDBService(CrudRepository<ArtExhibitionGuide, Long> repository, Settings settings) {
        super(repository, settings);
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
        cq.orderBy(cb.asc(root.get("name")));

        return entityManager.createQuery(cq).getResultList();
    }

    @Transactional
    public List<GuideContent> getArtExhibitionGuidePublishedBy(Person person) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<GuideContent> cq = cb.createQuery(GuideContent.class);
        Root<GuideContent> root = cq.from(GuideContent.class);

        cq.select(root).where(cb.equal(root.get("publisher").get("id"), person.getId()));
        cq.orderBy(cb.asc(root.get("name")));

        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    protected Class<ArtExhibitionGuide> getEntityClass() {
        return ArtExhibitionGuide.class;
    }
}


 

