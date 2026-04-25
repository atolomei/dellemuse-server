package dellemuse.serverapp.serverdb.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.ElevenLabsRequest;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class ElevenLabsRequestDBService extends BaseDBService<ElevenLabsRequest, Long> {

	static private Logger logger = Logger.getLogger(ElevenLabsRequestDBService.class.getName());

	public ElevenLabsRequestDBService(CrudRepository<ElevenLabsRequest, Long> repository, ServerDBSettings settings) {
		super(repository, settings);
	}

	@Transactional
	public <S extends ElevenLabsRequest> S save(S entity) {
		entity.setLastModified(OffsetDateTime.now());
		return getRepository().save(entity);
	}

	@Transactional
	public Optional<ElevenLabsRequest> findById(Long id) {
		return getRepository().findById(id);
	}

	@Transactional
	public boolean existsById(Long id) {
		return getRepository().existsById(id);
	}

	@Transactional
	public Iterable<ElevenLabsRequest> findAll() {
		return getRepository().findAll();
	}

	@Transactional
	public void delete(ElevenLabsRequest o) {
		getRepository().delete(o);
	}

	@Transactional
	public List<ElevenLabsRequest> getBySiteId(Long siteId) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ElevenLabsRequest> cq = cb.createQuery(ElevenLabsRequest.class);
		Root<ElevenLabsRequest> root = cq.from(ElevenLabsRequest.class);

		Predicate p = cb.equal(root.get("siteId"), siteId);
		cq.select(root).where(p);
		cq.orderBy(cb.desc(root.get("lastModified")));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public List<ElevenLabsRequest> getByUser(Long userId) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ElevenLabsRequest> cq = cb.createQuery(ElevenLabsRequest.class);
		Root<ElevenLabsRequest> root = cq.from(ElevenLabsRequest.class);

		Predicate p = cb.equal(root.get("user").get("id"), userId);
		cq.select(root).where(p);
		cq.orderBy(cb.desc(root.get("lastModified")));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public Iterable<ElevenLabsRequest> findAllSorted() {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ElevenLabsRequest> cq = cb.createQuery(ElevenLabsRequest.class);
		Root<ElevenLabsRequest> root = cq.from(ElevenLabsRequest.class);
		cq.orderBy(cb.desc(root.get("lastModified")));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public long countByUser(Long userId) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<ElevenLabsRequest> root = cq.from(ElevenLabsRequest.class);

		cq.select(cb.count(root));
		cq.where(cb.equal(root.get("user").get("id"), userId));
		return getEntityManager().createQuery(cq).getSingleResult();
	}

	@Transactional
	public long totalCharactersByUser(Long userId) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<ElevenLabsRequest> root = cq.from(ElevenLabsRequest.class);

		cq.select(cb.coalesce(cb.sum(root.get("size").as(Long.class)), 0L));
		cq.where(cb.equal(root.get("user").get("id"), userId));
		return getEntityManager().createQuery(cq).getSingleResult();
	}

	@Transactional
	public long countBySite(Long siteId) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<ElevenLabsRequest> root = cq.from(ElevenLabsRequest.class);

		cq.select(cb.count(root));
		cq.where(cb.equal(root.get("siteId"), siteId));
		return getEntityManager().createQuery(cq).getSingleResult();
	}

	@Transactional
	public long totalCharactersBySite(Long siteId) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<ElevenLabsRequest> root = cq.from(ElevenLabsRequest.class);

		cq.select(cb.coalesce(cb.sum(root.get("size").as(Long.class)), 0L));
		cq.where(cb.equal(root.get("siteId"), siteId));
		return getEntityManager().createQuery(cq).getSingleResult();
	}

	protected UserDBService getUserDBService() {
		return (UserDBService) ServiceLocator.getInstance().getBean(UserDBService.class);
	}

	@Override
	protected Class<ElevenLabsRequest> getEntityClass() {
		return ElevenLabsRequest.class;
	}

}
