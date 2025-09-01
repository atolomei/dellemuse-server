package dellemuse.serverapp.serverdb.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.PersistenceContext;
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

    private final PersonDBService personDBService;

    @PersistenceContext
    private EntityManager entityManager;

    public UserDBService(CrudRepository<User, Long> repository,
                         ServerDBSettings settings,
                         PersonDBService personDBService) {
        super(repository, settings);
        this.personDBService = personDBService;
    }

    @PostConstruct
    protected void onInitialize() {
    	super.register(getEntityClass(), this);
    	//ServiceLocator.getInstance().register(getEntityClass(), this);
    }
    
    @Transactional
    @Override
    public User create(String name, User createdBy) {
        return create(name, null, createdBy);
    }

    /**
     * @param name
     * @param person optional associated Person entity
     * @param createdBy user who creates this User
     */
    @Transactional
    public User create(String name, Person person, User createdBy) {
        User c = new User();
        c.setName(name);
        c.setNameKey(nameKey(name));
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);

        getRepository().save(c);

        if (person != null) {
            person.setUser(c);
            personDBService.save(person);
        }
        return c;
    }

    @Transactional
    @Override
    public List<User> getByName(String name) {
        return createNameQuery(name).getResultList();
    }

    @Transactional
    public User findRoot() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(getEntityClass());
        Root<User> root = criteria.from(getEntityClass());

        ParameterExpression<String> nameParam = cb.parameter(String.class);
        criteria.select(root).where(cb.equal(root.get(getNameColumn()), nameParam));

        TypedQuery<User> query = entityManager.createQuery(criteria);
        query.setHint("org.hibernate.cacheable", true);
        query.setFlushMode(FlushModeType.COMMIT);
        query.setParameter(nameParam, "root");

        List<User> users = query.getResultList();

        if (users != null && !users.isEmpty()) {
            return users.get(0);
        }
        throw new RuntimeException("Database does not have user with name=='root'");
    }

    @Override
    protected String getNameColumn() {
        return "name";
    }

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }
    
    
    public User getSessionUser() {
    	
    	return null;
    	
    }
/**
    	try {
			if (SecurityContextHolder.getContext().getAuthentication()==null) 
				return null;
			
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String username;
			
			user = getSecurityDao().findUserByName(username);
			return user;
		} 
		catch (Exception e) {
			logger.error(e, "inside getSessionUser(). returns null");
			return null;
		}
	}
   **/ 
}
