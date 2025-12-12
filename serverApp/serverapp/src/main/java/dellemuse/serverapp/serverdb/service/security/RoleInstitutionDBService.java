package dellemuse.serverapp.serverdb.service.security;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.audit.AuditKey;
import dellemuse.serverapp.serverdb.model.AuditAction;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.Institution;

import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.security.RoleInstitution;
import dellemuse.serverapp.serverdb.service.DBService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class RoleInstitutionDBService extends DBService<RoleInstitution, Long> {

	private static final Logger logger = Logger.getLogger(RoleInstitutionDBService.class.getName());

	@PersistenceContext
	private EntityManager entityManager;


	public RoleInstitutionDBService(CrudRepository<RoleInstitution, Long> repository, ServerDBSettings settings) {
		super(repository, settings);
	}
 
	@Transactional
	public RoleInstitution create(String name, Institution i, User createdBy) {
	
		RoleInstitution c = new RoleInstitution();

		c.setInstitution(i);
		c.setName(name);
		c.setState(ObjectState.PUBLISHED);
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
		getRepository().save(c);

		logger.debug("Create RoleInstitution -> " + c.getName()+" - " + i.getName());

		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy, AuditAction.CREATE));
		return c;
	}
	
	@Transactional
	public void markAsDeleted(RoleInstitution c, User deletedBy) {
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(deletedBy);
		c.setState(ObjectState.DELETED);

		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, deletedBy, AuditAction.DELETE, AuditKey.MARK_AS_DELETED));
		getRepository().save(c);
	}

	@Transactional
	public void restore(RoleInstitution c, User restoredBy) {
		OffsetDateTime date = OffsetDateTime.now();
		c.setLastModified(date);
		c.setLastModifiedUser(restoredBy);
		c.setState(ObjectState.EDITION);

		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, restoredBy, AuditAction.UPDATE, AuditKey.RESTORE));
		getRepository().save(c);
	}
	
	@Transactional
	public Optional<RoleInstitution> findWithDeps(Long id) {

		Optional<RoleInstitution> o_i = super.findById(id);

		if (o_i.isEmpty())
			return o_i;

		RoleInstitution i = o_i.get();

		
		Set<User> users = new HashSet<User>();
		i.getUsers().forEach( u -> users.add( getUserDBService().findById(u.getId()).get()));
		i.setUsers(users);
		
		i.setDependencies(true);
		return o_i;
	}

	
	@Transactional
	public Iterable<RoleInstitution> findByInstitution(Institution i) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<RoleInstitution> cq = cb.createQuery(getEntityClass());
		Root<RoleInstitution> root = cq.from(getEntityClass());
	
		cq.select(root).where(cb.equal(root.get("institution").get("id"), i.getId()));
		
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));
		return getEntityManager().createQuery(cq).getResultList();
	}
	
	@Transactional
	public Iterable<RoleInstitution> findAllSorted() {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<RoleInstitution> cq = cb.createQuery(getEntityClass());
		Root<RoleInstitution> root = cq.from(getEntityClass());
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public Iterable<RoleInstitution> findAllSorted(ObjectState os) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<RoleInstitution> cq = cb.createQuery(getEntityClass());
		Root<RoleInstitution> root = cq.from(getEntityClass());

		cq.select(root).where(cb.equal(root.get("state"), os));
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));

		//getEntityManager().createQuery(cq).getResultList().forEach(c -> logger.debug(c.getName()));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public Iterable<RoleInstitution> findAllSorted(ObjectState os1, ObjectState os2) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<RoleInstitution> cq = cb.createQuery(getEntityClass());
		Root<RoleInstitution> root = cq.from(getEntityClass());

		Predicate p1 = cb.equal(root.get("state"), os1);
		Predicate p2 = cb.equal(root.get("state"), os2);
		Predicate combinedPredicate = cb.or(p1, p2);
		cq.select(root).where(combinedPredicate);

		cq.orderBy(cb.asc(cb.lower(root.get("name"))));
		return getEntityManager().createQuery(cq).getResultList();
	}
 
	@Transactional
	public List<RoleInstitution> getByName(String name) {
		return createNameQuery(name).getResultList();
	}

	@Override
	protected Class<RoleInstitution> getEntityClass() {
		return RoleInstitution.class;
	}

	@Override
	public String getObjectClassName() {
		return Institution.class.getSimpleName().toLowerCase();
	}

	@PostConstruct
	protected void onInitialize() {
		super.register(getEntityClass(), this);
	}
}
