package dellemuse.serverapp.serverdb.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
 
import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.AuditAction;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.User;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
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

    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    public UserDBService(CrudRepository<User, Long> repository,
                         ServerDBSettings settings,
                         PersonDBService personDBService) {
        super(repository, settings);
        this.personDBService = personDBService;
    }

    @Transactional
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
        
    	c.setLanguage(getDefaultMasterLanguage());
	
    	c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        c.setZoneId( getSettings().getDefaultZoneId() );
        
        String hash = new BCryptPasswordEncoder().encode("dellemuse");
        c.setPassword(hash);
        // BCrypt.checkpw(plainPassword, hashedPasswordFromDB);
       
        getRepository().save(c);
        getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy,  AuditAction.CREATE));
        
        if (person != null) {
            person.setUser(c);
            getPersonDBService().save(person);
        }
        return c;
    }


	@Transactional	
	public void save(User o, User user, List<String> updatedParts) {
			super.save(o);
			getDelleMuseAuditDBService().save(DelleMuseAudit.of(o, user, AuditAction.UPDATE, String.join(", ", updatedParts)));
	}

    
    
    @Transactional
    @Override
    public List<User> getByName(String name) {
        return createNameQuery(name).getResultList();
    }
    
   
	@Transactional
	public Optional<User> findWithDeps(Long id) {
		Optional<User> o_u = super.findById(id);
		if (o_u.isEmpty())
			return  o_u;
		User user = o_u.get();
		user.setDependencies(true);
		return o_u;
	}

	public Optional<User> findByUsername(String username) {
		
			CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
	        CriteriaQuery<User> criteria = cb.createQuery(getEntityClass());
	        Root<User> root = criteria.from(getEntityClass());

	        ParameterExpression<String> nameParam = cb.parameter(String.class);
	        criteria.select(root).where(cb.equal(root.get(getNameColumn()), nameParam));

	        TypedQuery<User> query = entityManager.createQuery(criteria);
	        query.setHint("org.hibernate.cacheable", true);
	        query.setFlushMode(FlushModeType.COMMIT);
	        query.setParameter(nameParam, username);
	        List<User> users = query.getResultList();
	        if (users != null && !users.isEmpty()) {
	            return Optional.of(users.get(0));
	        }
	        return Optional.empty();
	}

	
    @Transactional
    public User findRoot() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
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
    
    public User getSessionUser() {
    	return findRoot();
    }
 
	public PersonDBService getPersonDBService() {
		return personDBService;
	}

    @Override
    protected String getNameColumn() {
        return "name";
    }

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }
    
    @Override
   	public String getObjectClassName() {
   		 return  User.class.getSimpleName().toLowerCase();
   	} 

    @PostConstruct
    protected void onInitialize() {
    	super.register(getEntityClass(), this);
    }
    
}
