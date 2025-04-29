package dellemuse.server.db.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.server.Settings;
import dellemuse.server.db.model.ArtExhibition;
import dellemuse.server.db.model.Institution;
import dellemuse.server.db.model.Person;
import dellemuse.server.db.model.Room;
import dellemuse.server.db.model.Site;
import dellemuse.server.db.model.User;
import dellemuse.model.ArtExhibitionModel;
import dellemuse.model.InstitutionModel;
import dellemuse.model.SiteModel;
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
public class SiteDBService extends DBService<Site, Long> {

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(SiteDBService.class.getName());

    public SiteDBService(CrudRepository<Site, Long> repository, EntityManagerFactory entityManagerFactory, Settings settings) {
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
    public Site create(String name, User createdBy) {
        Site c = new Site();
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
    public List<Site> getByName(String name) {
        return createNameQuery().getResultList();
    }

    @Override
    protected Class<Site> getEntityClass() {
        return Site.class;
    }

    @Transactional
    public List<ArtExhibition> getArtExhibitions(Site site) {
        return getArtExhibitions(site.getId());
    }
    
    @Transactional
    public List<ArtExhibition> getArtExhibitions(Long siteid) {
        
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
        query.setParameter(idparameter, siteid);
        return query.getResultList();
    }

}
