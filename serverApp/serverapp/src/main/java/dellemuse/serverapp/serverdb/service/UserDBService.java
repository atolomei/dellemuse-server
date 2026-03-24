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
import dellemuse.serverapp.serverdb.model.Candidate;
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

	static private Logger logger = Logger.getLogger(UserDBService.class.getName());

	private final PersonDBService personDBService;

	@Autowired
	@PersistenceContext
	private EntityManager entityManager;

	public UserDBService(CrudRepository<User, Long> repository, ServerDBSettings settings, PersonDBService personDBService) {
		super(repository, settings);
		this.personDBService = personDBService;
	}

	@Transactional
	public String generateUserName(String name, String lastName) {

		if (name == null || name.trim().isEmpty() || lastName == null || lastName.trim().isEmpty()) {
			throw new IllegalArgumentException("Name and last name can not be empty");
		}

		String cleanName = name.toLowerCase().trim();
		String cleanLastName = lastName.toLowerCase().trim().replaceAll("\\s+", "");

		// 1. first letter of name plus lastname
		String username = cleanName.substring(0, 1) + cleanLastName;

		if (findByUsername(username).isEmpty()) {
			return username;
		}

		// 2. name dot lastname
		username = cleanName + "." + cleanLastName;
		if (findByUsername(username).isEmpty()) {
			return username;
		}

		// 3. name dot lastname some number
		int counter = 1;
		while (true) {
			String numberedUsername = username + counter;
			if (findByUsername(numberedUsername).isEmpty()) {
				return numberedUsername;
			}
			counter++;
		}
	}

	@Transactional
	public User create(String name, User createdBy) {
		return create(name, Optional.empty(), createdBy);
	}

	/**
	 * @param name
	 * @param person    optional associated Person entity
	 * @param createdBy user who creates this User
	 */
	@Transactional
	public User create(String name, Optional<Person> person, User createdBy) {

		User user = new User();
		user.setName(name);
		user.setUsername(name);

		user.setLanguage(getDefaultMasterLanguage());

		user.setCreated(OffsetDateTime.now());
		user.setLastModified(OffsetDateTime.now());
		user.setLastModifiedUser(createdBy);
		user.setZoneId(getSettings().getDefaultZoneId());
		user.setState(ObjectState.PUBLISHED);

		String hash = new BCryptPasswordEncoder().encode("dellemuse");
		user.setPassword(hash);
		// BCrypt.checkpw(plainPassword, hashedPasswordFromDB);

		getRepository().save(user);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(user, createdBy, AuditAction.CREATE));

		if (person.isPresent()) {
			user.setSortLastFirstname(person.get().getSortLastFirstname());
			user.setFirstLastname(person.get().getFirstLastname());
			user.setPhone(person.get().getPhone());
			user.setEmail(person.get().getEmail());
			person.get().setUser(user);
			getPersonDBService().save(person.get());
		}

		else {
			getPersonDBService().create(null, name, user, createdBy);
		}
		return user;
	}

	@Transactional
	public Optional<User> findById(Long id) {
		return getRepository().findById(id);
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

		getDelleMuseAuditDBService().save(DelleMuseAudit.ofUser(u, by, AuditAction.UPDATE, AuditKey.REMOVE_ROLE, r));

	}

	@Transactional
	public void addRole(User u, Role r, User by) {

		if (!u.isDependencies())
			u = this.findWithDeps(u.getId()).get();

		if (u.hasRole(r))
			return;

		u.addRole(r);
		super.save(u);
		getDelleMuseAuditDBService().save(DelleMuseAudit.ofUser(u, by, AuditAction.UPDATE, AuditKey.ADD_ROLE, r));

	}

	@Transactional
	public List<Role> getUserRoles(User u) {

		List<Role> list = new ArrayList<Role>();

		if (!u.isDependencies()) {
			u = findWithDeps(u.getId()).get();
		}
		
		u.getRolesGeneral().forEach(r -> list.add(r));
		u.getRolesInstitution().forEach(r -> list.add(r));
		u.getRolesSite().forEach(r -> list.add(r));

		list.sort(new Comparator<Role>() {
			@Override
			public int compare(Role o1, Role o2) {
				return o1.getRoleDisplayName().compareToIgnoreCase(o2.getRoleDisplayName());
			}
		});

		return list;
	}

	@Transactional
	public Iterable<Site> getUserAuthorizedSites(User u) {

		if (!u.isDependencies()) {
			u = findWithDeps(u.getId()).get();
		}

		Set<Site> list = new HashSet<Site>();

		u.getRolesInstitution().forEach(r -> {
			((RoleInstitution) getRoleDBService().findById(r.getId()).get()).getInstitution().getSites().forEach(s -> list.add(s));
		});

		u.getRolesSite().forEach(r -> list.add(r.getSite()));

		/**
		 * list.sort( new Comparator<Site>() {
		 * 
		 * @Override public int compare(Site o1, Site o2) { return
		 *           o1.getName().compareToIgnoreCase(o2.getName()); } });
		 **/
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
			return o_u;

		User user = o_u.get();

		// Read lazy proxy IDs while entity is still attached
		Set<Long> roleGeneralIds = new HashSet<Long>();
		if (user.getRolesGeneral() != null)
			user.getRolesGeneral().forEach(r -> roleGeneralIds.add(r.getId()));

		Set<Long> roleInstitutionIds = new HashSet<Long>();
		if (user.getRolesInstitution() != null)
			user.getRolesInstitution().forEach(r -> roleInstitutionIds.add(r.getId()));

		Set<Long> roleSiteIds = new HashSet<Long>();
		if (user.getRolesSite() != null)
			user.getRolesSite().forEach(r -> roleSiteIds.add(r.getId()));

		Long lastModUserId = user.getLastModifiedUser() != null ? user.getLastModifiedUser().getId() : null;

		// Detach to prevent dirty-checking from triggering @PostUpdate
		getEntityManager().detach(user);

		{
			Set<RoleGeneral> set = new HashSet<RoleGeneral>();
			roleGeneralIds.forEach(rid -> set.add(getRoleGeneralDBService().findById(rid).get()));
			user.setRolesGeneral(set);
		}

		{
			Set<RoleInstitution> set = new HashSet<RoleInstitution>();
			roleInstitutionIds.forEach(rid -> set.add(getRoleInstitutionDBService().findById(rid).get()));
			user.setRolesInstitution(set);
		}

		{
			Set<RoleSite> set = new HashSet<RoleSite>();
			roleSiteIds.forEach(rid -> set.add(getRoleSiteDBService().findById(rid).get()));
			user.setRolesSite(set);
		}

		if (lastModUserId != null)
			user.setLastModifiedUser(findById(lastModUserId).get());

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

		getRoleSiteDBService().findBySite(site).forEach(r -> {

			logger.debug(r.getRoleDisplayName());
			r.getUsers().forEach(u -> list.add(u));
		});

		return list;
	}

	@Transactional
	public User createUserFromCandidate(String userName, Candidate c, User user) {

		if (c.getInstitution() == null)
			throw new IllegalArgumentException("Candidate must have an institution");

		logger.debug("Creating person from candidate -> " + c.getPersonLastname());
		Person p = getPersonDBService().create(c.getPersonName(), c.getPersonLastname(), user);
		p.setEmail(c.getEmail());
		p.setPhone(c.getPhone());
		p.setAddress(c.getInstitutionAddress());
		getPersonDBService().save(p);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(p, user, AuditAction.UPDATE, "from candidate"));

		logger.debug("Creating user with username -> " + userName);
		User u = create(userName, Optional.of(p), user);
		
		
		if (c.getPassword()==null || c.getPassword().trim().isEmpty()) {
				String hash = new BCryptPasswordEncoder().encode("dellemuse");
				u.setPassword(hash);
		}
		else
			u.setPassword(c.getPassword());
		
		u.setEmail(p.getEmail());

		Institution in = getInstitutionDBService().findById(c.getInstitution().getId()).get();
		
		logger.debug("Adding roles from candidate. Institution -> " + in.getName());

		getRoleInstitutionDBService().findByInstitution(in).forEach(r -> u.addRole(r));

		for (Site s : in.getSites()) {
			getRoleSiteDBService().findBySite(s).forEach(r -> u.addRole(r));
		}
		
		save(u);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(u, user, AuditAction.UPDATE, "Roles from candidate institution"));

		c.setUser(u);
		getCandidateDBService().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, user, AuditAction.UPDATE, "created candidate user"));

		return u;

	}

	@Transactional
	public Iterable<User> getInstitutionUsers(Institution i) {
		Set<User> list = new HashSet<User>();
		getRoleInstitutionDBService().findByInstitution(i).forEach(r -> {
			r.getUsers().forEach(u -> list.add(u));
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
		if (user.isPresent())
			return user;

		return findByEmail(value);
	}

	@Transactional
	public Optional<User> findByPhone(String phone) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<User> criteria = cb.createQuery(getEntityClass());
		Root<User> root = criteria.from(getEntityClass());

		ParameterExpression<String> nameParam = cb.parameter(String.class);
		criteria.select(root).where(cb.equal(root.get("phone"), nameParam));

		TypedQuery<User> query = getEntityManager().createQuery(criteria);
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

		TypedQuery<User> query = getEntityManager().createQuery(criteria);
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
	public Iterable<User> findAllSorted() {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(getEntityClass());
		Root<User> root = cq.from(getEntityClass());
		cq.orderBy(cb.asc(cb.lower(root.get("sortLastFirstname"))));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public Iterable<User> findAllSorted(ObjectState os) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(getEntityClass());
		Root<User> root = cq.from(getEntityClass());
		cq.select(root).where(cb.equal(root.get("state"), os));
		cq.orderBy(cb.asc(cb.lower(root.get("sortLastFirstname"))));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public Iterable<User> findAllSorted(ObjectState os1, ObjectState os2) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(getEntityClass());
		Root<User> root = cq.from(getEntityClass());
		Predicate p1 = cb.equal(root.get("state"), os1);
		Predicate p2 = cb.equal(root.get("state"), os2);
		Predicate combinedPredicate = cb.or(p1, p2);
		cq.select(root).where(combinedPredicate);
		cq.orderBy(cb.asc(cb.lower(root.get("sortLastFirstname"))));
		return getEntityManager().createQuery(cq).getResultList();
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
		if ((users != null) && (!users.isEmpty())) {
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
		return User.class.getSimpleName().toLowerCase();
	}

	@PostConstruct
	protected void onInitialize() {
		super.register(getEntityClass(), this);
	}

}