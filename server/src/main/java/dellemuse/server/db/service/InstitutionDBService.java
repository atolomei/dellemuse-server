package dellemuse.server.db.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.server.Settings;
import dellemuse.server.db.model.ArtExhibition;
import dellemuse.server.db.model.Institution;
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

@Service
public class InstitutionDBService extends DBService<Institution, Long> {

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(InstitutionDBService.class.getName());

    public InstitutionDBService(CrudRepository<Institution, Long> repository, EntityManagerFactory entityManagerFactory, Settings settings) {
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
    public Institution create(String name,User createdBy) {
        Institution c = new Institution();
        c.setName(name);
        c.setNameKey(normalize(name));
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        return getRepository().save(c);
    }

    
    @Transactional
    public List<Site> getSites(Long institutionid) {
                
        TypedQuery<Site> query;
        CriteriaBuilder criteriabuilder = getSessionFactory().getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Site> criteria = criteriabuilder.createQuery(Site.class);
        Root<Site> loaders = criteria.from(Site.class);
        criteria.orderBy(criteriabuilder.asc(loaders.get("title")));
        ParameterExpression<Long> idparameter = criteriabuilder.parameter(Long.class);
        criteria.select(loaders).where(criteriabuilder.equal(loaders.get("institution").get("id"), idparameter));
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
    public List<Institution> getByName(String name) {
        return createNameQuery().getResultList();
    }

    @Override
    protected Class<Institution> getEntityClass() {
        return Institution.class;
    }
}
