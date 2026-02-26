package dellemuse.serverapp.serverdb.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.wicket.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
 
import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.audit.AuditKey;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.AuditAction;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.security.Role;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
import dellemuse.serverapp.serverdb.model.security.RoleInstitution;
import dellemuse.serverapp.serverdb.model.security.RoleSite;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.serverdb.service.security.RoleDBService;
import dellemuse.serverapp.serverdb.service.security.RoleGeneralDBService;
import dellemuse.serverapp.serverdb.service.security.RoleInstitutionDBService;
import dellemuse.serverapp.serverdb.service.security.RoleSiteDBService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Predicate;
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
        return create(name, Optional.empty(), createdBy);
    }

    /**
     * @param name
     * @param person optional associated Person entity
     * @param createdBy user who creates this User
     */
    @Transactional
    public User create(String name, Optional<Person> person, User createdBy) {
      
    	User c = new User();
        c.setName(name);
        c.setUsername(name);

        c.setLanguage(getDefaultMasterLanguage());
	
    	c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        c.setZoneId( getSettings().getDefaultZoneId() );
        c.setState(ObjectState.PUBLISHED);
        
        String hash = new BCryptPasswordEncoder().encode("dellemuse");
        c.setPassword(hash);
        // BCrypt.checkpw(plainPassword, hashedPasswordFromDB);
       
        getRepository().save(c);
        getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy,  AuditAction.CREATE));
        
        if (person.isPresent()) {
        	c.setPhone(person.get().getPhone());
            c.setEmail(person.get().getEmail());
            person.get().setUser(c);
            getPersonDBService().save(person.get());
        }
        
        else {
        	getPersonDBService().create(null, name, createdBy);
        	 getPersonDBService().save(person.get());
        }
        return c;
    }


	@Transactional	
	public void save(User o, User user, List<String> updatedParts) {
			super.save(o);
			getDelleMuseAuditDBService().save(DelleMuseAudit.of(o, user, AuditAction.UPDATE, String.join(", ", updatedParts)));
	}
	

	@Transactional
	public void markAsDeleted(User c, User deletedBy) {
		
		if (c.isRoot())
			throw new IllegalArgumentException("root user can not be deleted");
		
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(deletedBy);
		c.setState(ObjectState.DELETED);

		getRepository().save(c);	
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, deletedBy, AuditAction.DELETE, AuditKey.MARK_AS_DELETED));
	}
	
	@Transactional	
	public void delete(User o, User by) {
		if (o.isRoot())
			throw new IllegalArgumentException("root user can not be deleted");
		super.delete(o, by);
	}
    
	@Transactional	
	public void removeRole(User u, Role r, User by) {
		
		if (!u.isDependencies())
			u = this.findWithDeps(u.getId()).get();
		
		if (!u.hasRole(r))
			return;
			
		u = getEntityManager().merge(u);
		 
		
		u.removeRole(r);
		
		super.save(u);
		
		getDelleMuseAuditDBService().save(DelleMuseAudit.ofUser(u, by, AuditAction.UPDATE,  AuditKey.REMOVE_ROLE, r ));

	}

	@Transactional	
	public void addRole(User u, Role r, User by) {
		
		if (!u.isDependencies())
			u = this.findWithDeps(u.getId()).get();
		
		if (u.hasRole(r))
			return;
			
		u.addRole(r);
		super.save(u);
		getDelleMuseAuditDBService().save(DelleMuseAudit.ofUser(u, by, AuditAction.UPDATE,  AuditKey.ADD_ROLE, r ));

	}
	
	 @Transactional
	 public Iterable<Role> getUserRoles(User u) {

		List<Role> list = new ArrayList<Role>();

		// if (u.isDe)
		u.getRolesGeneral().forEach( r -> list.add(r));
		u.getRolesInstitution().forEach( r -> list.add(r)); 
		u.getRolesSite().forEach( r -> list.add(r)); 
		
		list.sort( new Comparator<Role>() {
			@Override
			public int compare(Role o1, Role o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});
		
		return list;
	}
	 
	 
	 @Transactional
	 public Iterable<Site> getUserAuthorizedSites(User u) {

		 
		 if (!u.isDependencies()) {
			 u=findWithDeps(u.getId()).get();
		 }
	
		 List<Site> list = new ArrayList<Site>();

		u.getRolesInstitution().forEach( r -> 
		{
			((RoleInstitution) getRoleDBService().findById(r.getId()).get()).getInstitution().getSites().forEach( s -> 
				list.add(s));
		}); 
	
		u.getRolesSite().forEach( r -> list.add(r.getSite()) ); 
		
		list.sort( new Comparator<Site>() {
			@Override
			public int compare(Site o1, Site o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});
		
		return list;
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

		{
			Set<RoleGeneral> set =new HashSet<RoleGeneral>();
			user.getRolesGeneral().forEach( r ->set.add( getRoleGeneralDBService().findById(r.getId()).get()) );
			user.setRolesGeneral(set);
		}
		
		{
			Set<RoleInstitution> set =new HashSet<RoleInstitution>();
			user.getRolesInstitution().forEach( r ->set.add( getRoleInstitutionDBService().findById(r.getId()).get()) );
			user.setRolesInstitution(set);
		}

		{
			Set<RoleSite> set =new HashSet<RoleSite>();
			user.getRolesSite().forEach( r ->set.add( getRoleSiteDBService().findById(r.getId()).get()) );
			user.setRolesSite(set);
		}
		
		user.setDependencies(true);
		return o_u;
	}
	
	protected RoleDBService getRoleDBService() {
		return (RoleDBService) ServiceLocator.getInstance().getBean(RoleDBService.class);
	}

	
	protected RoleSiteDBService getRoleSiteDBService() {
		return (RoleSiteDBService) ServiceLocator.getInstance().getBean(RoleSiteDBService.class);
	}

	protected RoleInstitutionDBService getRoleInstitutionDBService() {
		return (RoleInstitutionDBService) ServiceLocator.getInstance().getBean(RoleInstitutionDBService.class);
	}
	
	
	@Transactional
	public Iterable<User> getSiteUsers(Site site) {
		Set<User> list = new HashSet<User>();
		getRoleSiteDBService().findBySite(site).forEach( r -> {
					r.getUsers().forEach( u -> list.add(u));
		});
		return list;	
	}
	
	
	 
	
	@Transactional
	public Iterable<User> getInstitutionUsers(Institution i) {
		Set<User> list = new HashSet<User>();
		getRoleInstitutionDBService().findByInstitution(i).forEach( r -> {
					r.getUsers().forEach( u -> list.add(u));
		});
		return list;	
	}
	
	 @Transactional
		public Optional<User> findByUsernameOrEmailOrPhone(String value) {
		    
		 Optional<User> user_n = findByUsername(value);
		 if (user_n.isPresent()) 
		    	return user_n;
		 
		 Optional<User> user_e = findByEmail(value);
		 if (user_e.isPresent()) 
		    	return user_e;

	 	  return findByPhone(value);
	}
	 
	@Transactional
	public Optional<User> findByUsernameOrEmail(String value) {
	    Optional<User> user = findByUsername(value);
	    if (user.isPresent()) return user;

	    return findByEmail(value);
	}
	 
	 
	

	 @Transactional
	public Optional<User> findByPhone(String phone) {
		
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(getEntityClass());
        Root<User> root = criteria.from(getEntityClass());

        ParameterExpression<String> nameParam = cb.parameter(String.class);
        criteria.select(root).where(cb.equal(root.get("phone"), nameParam));

        TypedQuery<User> query =getEntityManager().createQuery(criteria);
        query.setHint("org.hibernate.cacheable", true);
        query.setFlushMode(FlushModeType.COMMIT);
        query.setParameter(nameParam, phone);
        List<User> users = query.getResultList();
        if (users != null && !users.isEmpty()) {
            return Optional.of(users.get(0));
        }
        return Optional.empty();
	}
	 
	 
	
	 @Transactional
	public Optional<User> findByEmail(String email) {
		
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(getEntityClass());
        Root<User> root = criteria.from(getEntityClass());

        ParameterExpression<String> nameParam = cb.parameter(String.class);
        criteria.select(root).where(cb.equal(root.get("email"), nameParam));

        TypedQuery<User> query =getEntityManager().createQuery(criteria);
        query.setHint("org.hibernate.cacheable", true);
        query.setFlushMode(FlushModeType.COMMIT);
        query.setParameter(nameParam, email);
        List<User> users = query.getResultList();
        if (users != null && !users.isEmpty()) {
            return Optional.of(users.get(0));
        }
        return Optional.empty();
	}
	


	
	 @Transactional
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
