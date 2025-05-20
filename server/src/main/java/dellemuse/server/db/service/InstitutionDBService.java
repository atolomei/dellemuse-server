package dellemuse.server.db.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

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
        c.setNameKey(nameKey(name));
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        return getRepository().save(c);
    }

    @Transactional
    public Institution create(  String name, 
                                Optional<String> shortName, 
                                Optional<String> address, 
                                Optional<String> info,
                                User createdBy) {
        
        Institution c = new Institution();
        c.setName(name);
        c.setNameKey(nameKey(name));

        if (shortName.isPresent())
            c.setShortName(shortName.get());
        
        if (address.isPresent())
            c.setAddress(address.get());
        
        if (info.isPresent())
            c.setAddress(info.get());
        
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
    

    public Optional<Institution> findByShortName(String name) {
        TypedQuery<Institution> query;
        CriteriaBuilder criteriabuilder = getSessionFactory().getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Institution> criteria = criteriabuilder.createQuery(Institution.class);
        Root<Institution> loaders = criteria.from(Institution.class);
        criteria.orderBy(criteriabuilder.asc(loaders.get("id")));
        ParameterExpression<String> idparameter = criteriabuilder.parameter(String.class);
        criteria.select(loaders).where(criteriabuilder.equal(loaders.get("shortName"), idparameter));
        query = getSessionFactory().getCurrentSession().createQuery(criteria);
        query.setHint("org.hibernate.cacheable", true);
        query.setFlushMode(FlushModeType.COMMIT);
        query.setParameter(idparameter, name);
        List<Institution> list = query.getResultList();
        if (list==null || list.isEmpty())
            return Optional.empty();
        return Optional.of(list.get(0));
    }
    
    
    /**
     * @param name
     * @return
     */
    public List<Institution> getByName(String name) {
        return createNameQuery(name).getResultList();
    }

    @Override
    protected Class<Institution> getEntityClass() {
        return Institution.class;
    }

    


}
