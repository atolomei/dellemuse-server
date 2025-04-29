package dellemuse.server.db.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.server.Settings;
import dellemuse.server.db.model.User;
import dellemuse.server.error.ObjectNotFoundException;
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
public class UserDBService extends DBService<User, Long> {

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(UserDBService.class.getName());

    public UserDBService(CrudRepository<User, Long> repository, EntityManagerFactory entityManagerFactory, Settings settings) {
        super(repository, entityManagerFactory, settings);
    }

    /**
     * <p>Annotation Transactional is required to store values into the Database</p>
     * 
     * @param name
     * @param createdBy
     */
    @Transactional
    @Override
    public User create(String name,User createdBy) {
        User c = new User();
        c.setUsername(name);
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        return getRepository().save(c);
    }

    /**
     * @param name
     * @return
     */
    @Transactional
    @Override
    public List<User> getByName(String name) {
        return createNameQuery().getResultList();
    }


    @Transactional
    public User findRoot() {
        
        TypedQuery<User> query;
        CriteriaBuilder criteriabuilder = getSessionFactory().getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<User> criteria = criteriabuilder.createQuery(getEntityClass());
        Root<User> loaders = criteria.from(getEntityClass());
        ParameterExpression<String> nameparameter = criteriabuilder.parameter(String.class);
        criteria.select(loaders).where(criteriabuilder.equal(loaders.get("username"), nameparameter));
        query = getSessionFactory().getCurrentSession().createQuery(criteria);
        query.setHint("org.hibernate.cacheable", true);
        query.setFlushMode(FlushModeType.COMMIT);
        query.setParameter(nameparameter, "root");
        
        List<User> users = query.getResultList();
        
        if ((users!=null) && !users.isEmpty())
            return users.get(0);
        
        throw new ObjectNotFoundException("Database does not have user with username=='root'");
    }
    
    @Override
    protected String getNameColumn() {
        return "username";
    }
    
    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

}
