package dellemuse.serverapp.serverdb.service.security;

 

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.audit.AuditKey;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.AuditAction;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.Institution;

 
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
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
public class RoleGeneralDBService extends DBService<RoleGeneral, Long> {

	private static final Logger logger = Logger.getLogger(RoleGeneralDBService.class.getName());

	@PersistenceContext
	private EntityManager entityManager;


	public RoleGeneralDBService(CrudRepository<RoleGeneral, Long> repository, ServerDBSettings settings) {
		super(repository, settings);
	}
 
	@Transactional
	public RoleGeneral create(String name, User createdBy) {
	
		RoleGeneral c = new RoleGeneral();
		c.setName(name);
		c.setState(ObjectState.EDITION);
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy, AuditAction.CREATE));
		return c;
	}
	
	@Transactional
	public void markAsDeleted(RoleGeneral c, User deletedBy) {
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(deletedBy);
		c.setState(ObjectState.DELETED);

		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, deletedBy, AuditAction.DELETE, AuditKey.MARK_AS_DELETED));
		getRepository().save(c);
	}

	@Transactional
	public void restore(RoleGeneral c, User restoredBy) {
		OffsetDateTime date = OffsetDateTime.now();
		c.setLastModified(date);
		c.setLastModifiedUser(restoredBy);
		c.setState(ObjectState.EDITION);

		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, restoredBy, AuditAction.UPDATE, AuditKey.RESTORE));
		getRepository().save(c);
	}
	
	@Transactional
	public Optional<RoleGeneral> findWithDeps(Long id) {

		Optional<RoleGeneral> o_i = super.findById(id);

		if (o_i.isEmpty())
			return o_i;

		RoleGeneral i = o_i.get();
		
		Set<User> users = new HashSet<User>();
		i.getUsers().forEach( u -> users.add( getUserDBService().findById(u.getId()).get()));
		i.setUsers(users);
		
		i.setDependencies(true);

		return o_i;
	}

	@Transactional
	public Iterable<RoleGeneral> findAllSorted() {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<RoleGeneral> cq = cb.createQuery(getEntityClass());
		Root<RoleGeneral> root = cq.from(getEntityClass());
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public Iterable<RoleGeneral> findAllSorted(ObjectState os) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<RoleGeneral> cq = cb.createQuery(getEntityClass());
		Root<RoleGeneral> root = cq.from(getEntityClass());

		cq.select(root).where(cb.equal(root.get("state"), os));
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));

		getEntityManager().createQuery(cq).getResultList().forEach(c -> logger.debug(c.getName()));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public Iterable<RoleGeneral> findAllSorted(ObjectState os1, ObjectState os2) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<RoleGeneral> cq = cb.createQuery(getEntityClass());
		Root<RoleGeneral> root = cq.from(getEntityClass());

		Predicate p1 = cb.equal(root.get("state"), os1);
		Predicate p2 = cb.equal(root.get("state"), os2);
		Predicate combinedPredicate = cb.or(p1, p2);
		cq.select(root).where(combinedPredicate);

		cq.orderBy(cb.asc(cb.lower(root.get("name"))));
		return getEntityManager().createQuery(cq).getResultList();
	}
 
	@Transactional
	public List<RoleGeneral> getByName(String name) {
		return createNameQuery(name).getResultList();
	}

	@Override
	protected Class<RoleGeneral> getEntityClass() {
		return RoleGeneral.class;
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
