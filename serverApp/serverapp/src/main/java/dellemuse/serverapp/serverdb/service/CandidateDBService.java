package dellemuse.serverapp.serverdb.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;

import dellemuse.serverapp.serverdb.model.AuditAction;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Candidate;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.CandidateStatus;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class CandidateDBService extends DBService<Candidate, Long> {

	@SuppressWarnings("unused")
	static private Logger logger = Logger.getLogger(CandidateDBService.class.getName());

	public CandidateDBService(CrudRepository<Candidate, Long> repository, ServerDBSettings settings) {
		super(repository, settings);
	}

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Deletes all Candidate entities with status DRAFT or state DELETED.
	 */
	@Transactional
	public int deleteDrafts() {
		String jpql = "DELETE FROM Candidate c WHERE (c.status = :status OR c.status is null) OR c.state = :deletedState";
		return getEntityManager().createQuery(jpql).setParameter("status", dellemuse.serverapp.serverdb.model.CandidateStatus.DRAFT).setParameter("deletedState", dellemuse.serverapp.serverdb.model.ObjectState.DELETED).executeUpdate();
	}

	@Transactional
	public Optional<Candidate> findById(Long id) {
		return getRepository().findById(id);
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
	public Candidate create(Map<String, String> values, User createdBy) {

		Candidate c = new Candidate();

		c.setName(values.get("name"));
		c.setEmail(values.get("email"));
		c.setPhone(values.get("phone"));
		c.setInstitutionAddress(values.get("institutionAddress"));
		c.setInstitutionName(values.get("institutionName"));
		c.setComments(values.get("comments"));
		c.setReferences(values.get("references"));
		c.setLanguage(values.get("language"));

		c.setObjectState(ObjectState.DRAFT);
		c.setStatus(dellemuse.serverapp.serverdb.model.CandidateStatus.DRAFT);

		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);

		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy, AuditAction.CREATE));

		return c;
	}

	@Transactional
	public Optional<Candidate> findWithDeps(Long id) {

		Optional<Candidate> o_aw = super.findById(id);

		if (o_aw.isEmpty())
			return o_aw;

		Candidate aw = o_aw.get();

		// Read lazy proxy IDs while entity is still attached
		Long lastModUserId = aw.getLastModifiedUser() != null ? aw.getLastModifiedUser().getId() : null;
		Long evaluatedById = aw.getEvaluatedBy() != null ? aw.getEvaluatedBy().getId() : null;
		Long institutionId = aw.getInstitution() != null ? aw.getInstitution().getId() : null;
		Long userId = aw.getUser() != null ? aw.getUser().getId() : null;

		// Detach to prevent dirty-checking from triggering @PostUpdate
		getEntityManager().detach(aw);

		if (lastModUserId != null)
			aw.setLastModifiedUser(getUserDBService().findById(lastModUserId).get());

		if (evaluatedById != null)
			aw.setEvaluatedBy(getUserDBService().findById(evaluatedById).get());

		if (institutionId != null)
			aw.setInstitution(getInstitutionDBService().findById(institutionId).get());

		if (userId != null)
			aw.setUser(getUserDBService().findById(userId).get());

		aw.setDependencies(true);
		return o_aw;
	}

	@Transactional
	@Override
	public Iterable<Candidate> findAllSorted() {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Candidate> cq = cb.createQuery(getEntityClass());
		Root<Candidate> root = cq.from(getEntityClass());
		cq.orderBy(cb.asc(cb.lower(root.get("institutionName"))));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public List<Candidate> findByStatus(CandidateStatus... statuses) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Candidate> cq = cb.createQuery(getEntityClass());
		Root<Candidate> root = cq.from(getEntityClass());
		cq.select(root).where(root.get("status").in((Object[]) statuses));
		cq.orderBy(cb.asc(cb.lower(root.get("institutionName"))));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@PostConstruct
	protected void onInitialize() {
		super.register(getEntityClass(), this);
	}

	public List<Candidate> getByName(String name) {
		return createNameQuery(name).getResultList();
	}

	@Override
	protected Class<Candidate> getEntityClass() {
		return Candidate.class;
	}

	@Override
	public String getObjectClassName() {
		return Candidate.class.getSimpleName().toLowerCase();
	}

}