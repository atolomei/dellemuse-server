package dellemuse.serverapp.serverdb.service.security;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.apache.wicket.Component;
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
import dellemuse.serverapp.serverdb.model.security.Role;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
import dellemuse.serverapp.serverdb.model.security.RoleInstitution;
import dellemuse.serverapp.serverdb.model.security.RoleSite;
import dellemuse.serverapp.serverdb.service.DBService;
import jakarta.annotation.PostConstruct;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class RoleDBService extends DBService<Role, Long> {

	private static final Logger logger = Logger.getLogger(RoleDBService.class.getName());

	public RoleDBService(CrudRepository<Role, Long> repository, ServerDBSettings settings) {
		super(repository, settings);
	}

	@Transactional
	public Optional<Role> findWithDeps(Role role) {

		if (role instanceof RoleGeneral) {
			Optional<RoleGeneral> o = getRoleGeneralDBService().findWithDeps(role.getId());
			Optional<Role> or = Optional.of(o.get());
			return or;
		}

		if (role instanceof RoleSite) {
			Optional<RoleSite> o = getRoleSiteDBService().findWithDeps(role.getId());
			Optional<Role> or = Optional.of(o.get());
			return or;
		}

		if (role instanceof RoleInstitution) {
			Optional<RoleInstitution> o = getRoleInstitutionDBService().findWithDeps(role.getId());
			Optional<Role> or = Optional.of(o.get());
			return or;
		}

		throw new RuntimeException("not supported -> " + role.getClass().getName());
	}

	@Transactional
	public Iterable<Role> findAllSorted() {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Role> cq = cb.createQuery(getEntityClass());

		Root<Role> root = cq.from(getEntityClass());
		
		
		cq.select(root);
		// cq.orderBy(cb.asc(cb.lower(root.get("name"))));
		// Example using a Comparator for an associated collection
		//@OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
		//@SortComparator(MyCustomComparator.class) // Hibernate specific annotation
		//
		
		
		Set<Role> roles = new TreeSet<>(new Comparator<Role>() {
			@Override
			public int compare(Role o1, Role o2) {
				return o1.getRoleDisplayName().compareToIgnoreCase(o2.getRoleDisplayName());
			}
		});
		
		getEntityManager().createQuery(cq).getResultList().forEach( r -> roles.add(r));
		return roles;
		
	}

	@Transactional
	public Iterable<Role> findAllSorted(ObjectState os) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Role> cq = cb.createQuery(getEntityClass());
		Root<Role> root = cq.from(getEntityClass());

		cq.select(root).where(cb.equal(root.get("state"), os));
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));

		getEntityManager().createQuery(cq).getResultList().forEach(c -> logger.debug(c.getName()));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public Iterable<Role> findAllSorted(ObjectState os1, ObjectState os2) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Role> cq = cb.createQuery(getEntityClass());
		Root<Role> root = cq.from(getEntityClass());

		Predicate p1 = cb.equal(root.get("state"), os1);
		Predicate p2 = cb.equal(root.get("state"), os2);
		Predicate combinedPredicate = cb.or(p1, p2);
		cq.select(root).where(combinedPredicate);

		// cq.orderBy(cb.asc(cb.lower(root.get("name"))));

		Iterable<Role> it = getEntityManager().createQuery(cq).getResultList();

		List<Role> list = new ArrayList<Role>();
		it.forEach(r -> list.add(r));

		list.sort(new Comparator<Role>() {
			@Override
			public int compare(Role r1, Role r2) {
				return r1.getRoleDisplayName().compareToIgnoreCase(r2.getRoleDisplayName());
			}
		});
		return list;
	}

	@Transactional
	public Iterable<RoleGeneral> findGeneralAllSorted(ObjectState os1, ObjectState os2) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<RoleGeneral> cq = cb.createQuery(RoleGeneral.class);
		Root<RoleGeneral> root = cq.from(RoleGeneral.class);

		Predicate p1 = cb.equal(root.get("state"), os1);
		Predicate p2 = cb.equal(root.get("state"), os2);
		Predicate combinedPredicate = cb.or(p1, p2);
		cq.select(root).where(combinedPredicate);

		cq.orderBy(cb.asc(cb.lower(root.get("name"))));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public Iterable<RoleSite> findSiteAllSorted(ObjectState os1, ObjectState os2) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<RoleSite> cq = cb.createQuery(RoleSite.class);
		Root<RoleSite> root = cq.from(RoleSite.class);

		Predicate p1 = cb.equal(root.get("state"), os1);
		Predicate p2 = cb.equal(root.get("state"), os2);
		Predicate combinedPredicate = cb.or(p1, p2);
		cq.select(root).where(combinedPredicate);

		cq.orderBy(cb.asc(root.get("site").get("name")), cb.asc(root.get("name")));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public Iterable<RoleInstitution> findInstitutionAllSorted(ObjectState os1, ObjectState os2) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<RoleInstitution> cq = cb.createQuery(RoleInstitution.class);
		Root<RoleInstitution> root = cq.from(RoleInstitution.class);

		Predicate p1 = cb.equal(root.get("state"), os1);
		Predicate p2 = cb.equal(root.get("state"), os2);
		Predicate combinedPredicate = cb.or(p1, p2);
		cq.select(root).where(combinedPredicate);

		cq.orderBy(cb.asc(root.get("institution").get("name")), cb.asc(root.get("name")));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public List<Role> getByName(String name) {
		return createNameQuery(name).getResultList();
	}

	@Override
	protected Class<Role> getEntityClass() {
		return Role.class;
	}

	@Override
	public String getObjectClassName() {
		return Role.class.getSimpleName().toLowerCase();
	}

	@PostConstruct
	protected void onInitialize() {
		super.register(getEntityClass(), this);
	}

}
