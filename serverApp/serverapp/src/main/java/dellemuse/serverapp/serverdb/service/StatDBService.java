package dellemuse.serverapp.serverdb.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.stat.Stat;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class StatDBService extends BaseDBService<Stat, Long> {

	static private Logger logger = Logger.getLogger(StatDBService.class.getName());

	public StatDBService(CrudRepository<Stat, Long> repository, ServerDBSettings settings) {
		super(repository, settings);
	}

	@Transactional
	public <S extends Stat> S save(S entity) {
		return getRepository().save(entity);
	}

	@Transactional
	public Optional<Stat> findById(Long id) {
		return getRepository().findById(id);
	}

	@Transactional
	public boolean existsById(Long id) {
		return getRepository().existsById(id);
	}

	@Transactional
	public Iterable<Stat> findAll() {
		return getRepository().findAll();
	}

	@Transactional
	public void delete(Stat o) {
		getRepository().delete(o);
	}

	@Transactional
	public List<Stat> getBySiteId(Long siteId) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Stat> cq = cb.createQuery(Stat.class);
		Root<Stat> root = cq.from(Stat.class);

		Predicate p = cb.equal(root.get("site").get("id"), siteId);
		cq.select(root).where(p);
		cq.orderBy(cb.desc(root.get("timestamp")));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public List<Stat> getByArtExhibitionGuideId(Long artExhibitionGuideId) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Stat> cq = cb.createQuery(Stat.class);
		Root<Stat> root = cq.from(Stat.class);

		Predicate p = cb.equal(root.get("artExhibitionGuide").get("id"), artExhibitionGuideId);
		cq.select(root).where(p);
		cq.orderBy(cb.desc(root.get("timestamp")));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public List<Stat> getByGuideContentId(Long guideContentId) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Stat> cq = cb.createQuery(Stat.class);
		Root<Stat> root = cq.from(Stat.class);

		Predicate p = cb.equal(root.get("guideContent").get("id"), guideContentId);
		cq.select(root).where(p);
		cq.orderBy(cb.desc(root.get("timestamp")));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public List<Stat> getBySessionId(String sessionId) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Stat> cq = cb.createQuery(Stat.class);
		Root<Stat> root = cq.from(Stat.class);

		Predicate p = cb.equal(root.get("sessionId"), sessionId);
		cq.select(root).where(p);
		cq.orderBy(cb.desc(root.get("timestamp")));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public Iterable<Stat> findAllSorted() {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Stat> cq = cb.createQuery(Stat.class);
		Root<Stat> root = cq.from(Stat.class);
		cq.orderBy(cb.desc(root.get("timestamp")));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public long countBySite(Long siteId) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Stat> root = cq.from(Stat.class);

		cq.select(cb.countDistinct(root.get("sessionId")));
		cq.where(cb.equal(root.get("site").get("id"), siteId));
		return getEntityManager().createQuery(cq).getSingleResult();
	}

	@Transactional
	public long countByArtExhibitionGuide(Long artExhibitionGuideId) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Stat> root = cq.from(Stat.class);

		cq.select(cb.countDistinct(root.get("sessionId")));
		cq.where(cb.equal(root.get("artExhibitionGuide").get("id"), artExhibitionGuideId));
		return getEntityManager().createQuery(cq).getSingleResult();
	}

	@Transactional
	public long countByGuideContent(Long guideContentId) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Stat> root = cq.from(Stat.class);

		cq.select(cb.countDistinct(root.get("sessionId")));
		cq.where(cb.equal(root.get("guideContent").get("id"), guideContentId));
		return getEntityManager().createQuery(cq).getSingleResult();
	}

	@Override
	protected Class<Stat> getEntityClass() {
		return Stat.class;
	}

	// ── Date-range count methods ──────────────────────────────────────────

	/**
	 * Counts visits to a site within a date range.
	 * If {@code from} is null, counts all visits.
	 */
	@Transactional
	public long countBySiteInRange(Long siteId, OffsetDateTime from) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Stat> root = cq.from(Stat.class);

		cq.select(cb.countDistinct(root.get("sessionId")));
		Predicate sitePredicate = cb.equal(root.get("site").get("id"), siteId);
		if (from != null) {
			Predicate datePredicate = cb.greaterThanOrEqualTo(root.get("timestamp"), from);
			cq.where(cb.and(sitePredicate, datePredicate));
		} else {
			cq.where(sitePredicate);
		}
		return getEntityManager().createQuery(cq).getSingleResult();
	}

	/**
	 * Counts visits to an ArtExhibitionGuide within a date range.
	 * If {@code from} is null, counts all visits.
	 */
	@Transactional
	public long countByArtExhibitionGuideInRange(Long artExhibitionGuideId, OffsetDateTime from) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Stat> root = cq.from(Stat.class);

		cq.select(cb.countDistinct(root.get("sessionId")));
		Predicate guidePredicate = cb.equal(root.get("artExhibitionGuide").get("id"), artExhibitionGuideId);
		if (from != null) {
			Predicate datePredicate = cb.greaterThanOrEqualTo(root.get("timestamp"), from);
			cq.where(cb.and(guidePredicate, datePredicate));
		} else {
			cq.where(guidePredicate);
		}
		return getEntityManager().createQuery(cq).getSingleResult();
	}

	/**
	 * Counts visits to a GuideContent within a date range.
	 * If {@code from} is null, counts all visits.
	 */
	@Transactional
	public long countByGuideContentInRange(Long guideContentId, OffsetDateTime from) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Stat> root = cq.from(Stat.class);

		cq.select(cb.countDistinct(root.get("sessionId")));
		Predicate contentPredicate = cb.equal(root.get("guideContent").get("id"), guideContentId);
		if (from != null) {
			Predicate datePredicate = cb.greaterThanOrEqualTo(root.get("timestamp"), from);
			cq.where(cb.and(contentPredicate, datePredicate));
		} else {
			cq.where(contentPredicate);
		}
		return getEntityManager().createQuery(cq).getSingleResult();
	}

	/**
	 * Sum of all GuideContent visits for a given ArtExhibitionGuide within a date range.
	 * If {@code from} is null, counts all visits.
	 */
	@Transactional
	public long countGuideContentsByArtExhibitionGuideInRange(Long artExhibitionGuideId, OffsetDateTime from) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Stat> root = cq.from(Stat.class);

		cq.select(cb.countDistinct(root.get("sessionId")));
		Predicate contentNotNull = cb.isNotNull(root.get("guideContent"));
		Predicate guidePredicate = cb.equal(root.get("artExhibitionGuide").get("id"), artExhibitionGuideId);
		if (from != null) {
			Predicate datePredicate = cb.greaterThanOrEqualTo(root.get("timestamp"), from);
			cq.where(cb.and(contentNotNull, guidePredicate, datePredicate));
		} else {
			cq.where(cb.and(contentNotNull, guidePredicate));
		}
		return getEntityManager().createQuery(cq).getSingleResult();
	}

}
