package dellemuse.server.db.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.server.Settings;
import dellemuse.server.db.model.ArtExhibition;
import dellemuse.server.db.model.ArtExhibitionGuide;
import dellemuse.server.db.model.ArtExhibitionItem;
import dellemuse.server.db.model.Site;
import dellemuse.server.db.model.User;
import dellemuse.model.logging.Logger;
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

    public ArtExhibitionDBService(CrudRepository<ArtExhibition, Long> repository, Settings settings) {
        super(repository, settings);
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
        cq.orderBy(cb.asc(root.get("name")));

        return entityManager.createQuery(cq).getResultList();
    }

    @Transactional
    public List<ArtExhibitionItem> getArtExhibitionItem(ArtExhibition exhibition) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ArtExhibitionItem> cq = cb.createQuery(ArtExhibitionItem.class);
        Root<ArtExhibitionItem> root = cq.from(ArtExhibitionItem.class);
        cq.select(root).where(cb.equal(root.get("artExhibition").get("id"), exhibition.getId()));
        cq.orderBy(cb.asc(root.get("name")));

        return entityManager.createQuery(cq).getResultList();
    }

    @Transactional
    public List<ArtExhibition> getArtExhibitions(Site site) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ArtExhibition> cq = cb.createQuery(ArtExhibition.class);
        Root<ArtExhibition> root = cq.from(ArtExhibition.class);
        cq.select(root).where(cb.equal(root.get("site").get("id"), site.getId()));
        cq.orderBy(cb.asc(root.get("name")));

        return getEntityManager().createQuery(cq).getResultList();
    }

    @Override
    protected Class<ArtExhibition> getEntityClass() {
        return ArtExhibition.class;
    }
}
    
    
    
    
    
    
    
    
    
    
    