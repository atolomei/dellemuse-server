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
	 * If {@code to} is not null, only counts visits with timestamp &lt; to (exclusive).
	 */
	@Transactional
	public long countBySiteInRange(Long siteId, OffsetDateTime from, OffsetDateTime to) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Stat> root = cq.from(Stat.class);

		cq.select(cb.countDistinct(root.get("sessionId")));
		Predicate sitePredicate = cb.equal(root.get("site").get("id"), siteId);
		Predicate combined = sitePredicate;
		if (from != null) {
			combined = cb.and(combined, cb.greaterThanOrEqualTo(root.get("timestamp"), from));
		}
		if (to != null) {
			combined = cb.and(combined, cb.lessThan(root.get("timestamp"), to));
		}
		cq.where(combined);
		return getEntityManager().createQuery(cq).getSingleResult();
	}

	/**
	 * Counts visits to an ArtExhibitionGuide within a date range.
	 * If {@code from} is null, counts all visits.
	 * If {@code to} is not null, only counts visits with timestamp &lt; to (exclusive).
	 */
	@Transactional
	public long countByArtExhibitionGuideInRange(Long artExhibitionGuideId, OffsetDateTime from, OffsetDateTime to) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Stat> root = cq.from(Stat.class);

		cq.select(cb.countDistinct(root.get("sessionId")));
		Predicate combined = cb.equal(root.get("artExhibitionGuide").get("id"), artExhibitionGuideId);
		if (from != null) {
			combined = cb.and(combined, cb.greaterThanOrEqualTo(root.get("timestamp"), from));
		}
		if (to != null) {
			combined = cb.and(combined, cb.lessThan(root.get("timestamp"), to));
		}
		cq.where(combined);
		return getEntityManager().createQuery(cq).getSingleResult();
	}

	/**
	 * Counts visits to a GuideContent within a date range.
	 * If {@code from} is null, counts all visits.
	 * If {@code to} is not null, only counts visits with timestamp &lt; to (exclusive).
	 */
	@Transactional
	public long countByGuideContentInRange(Long guideContentId, OffsetDateTime from, OffsetDateTime to) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Stat> root = cq.from(Stat.class);

		cq.select(cb.countDistinct(root.get("sessionId")));
		Predicate combined = cb.equal(root.get("guideContent").get("id"), guideContentId);
		if (from != null) {
			combined = cb.and(combined, cb.greaterThanOrEqualTo(root.get("timestamp"), from));
		}
		if (to != null) {
			combined = cb.and(combined, cb.lessThan(root.get("timestamp"), to));
		}
		cq.where(combined);
		return getEntityManager().createQuery(cq).getSingleResult();
	}

	/**
	 * Sum of all GuideContent visits for a given ArtExhibitionGuide within a date range.
	 * If {@code from} is null, counts all visits.
	 * If {@code to} is not null, only counts visits with timestamp &lt; to (exclusive).
	 */
	@Transactional
	public long countGuideContentsByArtExhibitionGuideInRange(Long artExhibitionGuideId, OffsetDateTime from, OffsetDateTime to) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Stat> root = cq.from(Stat.class);

		cq.select(cb.countDistinct(root.get("sessionId")));
		Predicate contentNotNull = cb.isNotNull(root.get("guideContent"));
		Predicate combined = cb.and(contentNotNull, cb.equal(root.get("artExhibitionGuide").get("id"), artExhibitionGuideId));
		if (from != null) {
			combined = cb.and(combined, cb.greaterThanOrEqualTo(root.get("timestamp"), from));
		}
		if (to != null) {
			combined = cb.and(combined, cb.lessThan(root.get("timestamp"), to));
		}
		cq.where(combined);
		return getEntityManager().createQuery(cq).getSingleResult();
	}

	// ── ArtWork count methods ─────────────────────────────────────────────

	/**
	 * Counts total distinct sessions for a given ArtExhibitionGuide within a date range,
	 * including both guide page visits and guide content visits.
	 * If {@code from} is null, counts all visits.
	 * If {@code to} is not null, only counts visits with timestamp &lt; to (exclusive).
	 */
	@Transactional
	public long countTotalByArtExhibitionGuideInRange(Long artExhibitionGuideId, OffsetDateTime from, OffsetDateTime to) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Stat> root = cq.from(Stat.class);

		cq.select(cb.countDistinct(root.get("sessionId")));
		Predicate combined = cb.equal(root.get("artExhibitionGuide").get("id"), artExhibitionGuideId);
		if (from != null) {
			combined = cb.and(combined, cb.greaterThanOrEqualTo(root.get("timestamp"), from));
		}
		if (to != null) {
			combined = cb.and(combined, cb.lessThan(root.get("timestamp"), to));
		}
		cq.where(combined);
		return getEntityManager().createQuery(cq).getSingleResult();
	}

	/**
	 * Counts distinct sessions for a given ArtWork within a date range (across all guides).
	 * If {@code to} is not null, only counts visits with timestamp &lt; to (exclusive).
	 */
	@Transactional
	public long countByArtWorkInRange(Long artWorkId, OffsetDateTime from, OffsetDateTime to) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Stat> root = cq.from(Stat.class);

		cq.select(cb.countDistinct(root.get("sessionId")));
		Predicate combined = cb.equal(root.get("artWork").get("id"), artWorkId);
		if (from != null) {
			combined = cb.and(combined, cb.greaterThanOrEqualTo(root.get("timestamp"), from));
		}
		if (to != null) {
			combined = cb.and(combined, cb.lessThan(root.get("timestamp"), to));
		}
		cq.where(combined);
		return getEntityManager().createQuery(cq).getSingleResult();
	}

	/**
	 * Counts distinct sessions for a given ArtWork and ArtExhibitionGuide within a date range.
	 * If {@code to} is not null, only counts visits with timestamp &lt; to (exclusive).
	 */
	@Transactional
	public long countByArtWorkAndGuideInRange(Long artWorkId, Long artExhibitionGuideId, OffsetDateTime from, OffsetDateTime to) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Stat> root = cq.from(Stat.class);

		cq.select(cb.countDistinct(root.get("sessionId")));
		Predicate combined = cb.and(
				cb.equal(root.get("artWork").get("id"), artWorkId),
				cb.equal(root.get("artExhibitionGuide").get("id"), artExhibitionGuideId));
		if (from != null) {
			combined = cb.and(combined, cb.greaterThanOrEqualTo(root.get("timestamp"), from));
		}
		if (to != null) {
			combined = cb.and(combined, cb.lessThan(root.get("timestamp"), to));
		}
		cq.where(combined);
		return getEntityManager().createQuery(cq).getSingleResult();
	}

	/**
	 * Returns the list of distinct ArtExhibitionGuide IDs that have sessions for a given ArtWork within a date range.
	 * If {@code to} is not null, only counts visits with timestamp &lt; to (exclusive).
	 */
	@Transactional
	public List<Long> getGuideIdsWithSessionsForArtWork(Long artWorkId, OffsetDateTime from, OffsetDateTime to) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Stat> root = cq.from(Stat.class);

		cq.select(root.get("artExhibitionGuide").get("id")).distinct(true);
		Predicate combined = cb.and(
				cb.equal(root.get("artWork").get("id"), artWorkId),
				cb.isNotNull(root.get("artExhibitionGuide")));
		if (from != null) {
			combined = cb.and(combined, cb.greaterThanOrEqualTo(root.get("timestamp"), from));
		}
		if (to != null) {
			combined = cb.and(combined, cb.lessThan(root.get("timestamp"), to));
		}
		cq.where(combined);
		return getEntityManager().createQuery(cq).getResultList();
	}

	// ── Language-filtered count methods ───────────────────────────────────

	/**
	 * Counts distinct sessions for a Site filtered by language code within a date range.
	 */
	@Transactional
	public long countBySiteAndLanguageInRange(Long siteId, String languageCode, OffsetDateTime from, OffsetDateTime to) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Stat> root = cq.from(Stat.class);

		cq.select(cb.countDistinct(root.get("sessionId")));
		Predicate combined = cb.and(
				cb.equal(root.get("site").get("id"), siteId),
				cb.equal(root.get("language"), languageCode));
		if (from != null) {
			combined = cb.and(combined, cb.greaterThanOrEqualTo(root.get("timestamp"), from));
		}
		if (to != null) {
			combined = cb.and(combined, cb.lessThan(root.get("timestamp"), to));
		}
		cq.where(combined);
		return getEntityManager().createQuery(cq).getSingleResult();
	}

	/**
	 * Counts distinct sessions for an ArtExhibitionGuide filtered by language code within a date range.
	 */
	@Transactional
	public long countByArtExhibitionGuideAndLanguageInRange(Long artExhibitionGuideId, String languageCode, OffsetDateTime from, OffsetDateTime to) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Stat> root = cq.from(Stat.class);

		cq.select(cb.countDistinct(root.get("sessionId")));
		Predicate combined = cb.and(
				cb.equal(root.get("artExhibitionGuide").get("id"), artExhibitionGuideId),
				cb.equal(root.get("language"), languageCode));
		if (from != null) {
			combined = cb.and(combined, cb.greaterThanOrEqualTo(root.get("timestamp"), from));
		}
		if (to != null) {
			combined = cb.and(combined, cb.lessThan(root.get("timestamp"), to));
		}
		cq.where(combined);
		return getEntityManager().createQuery(cq).getSingleResult();
	}

	/**
	 * Counts distinct sessions for a GuideContent filtered by language code within a date range.
	 */
	@Transactional
	public long countByGuideContentAndLanguageInRange(Long guideContentId, String languageCode, OffsetDateTime from, OffsetDateTime to) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Stat> root = cq.from(Stat.class);

		cq.select(cb.countDistinct(root.get("sessionId")));
		Predicate combined = cb.and(
				cb.equal(root.get("guideContent").get("id"), guideContentId),
				cb.equal(root.get("language"), languageCode));
		if (from != null) {
			combined = cb.and(combined, cb.greaterThanOrEqualTo(root.get("timestamp"), from));
		}
		if (to != null) {
			combined = cb.and(combined, cb.lessThan(root.get("timestamp"), to));
		}
		cq.where(combined);
		return getEntityManager().createQuery(cq).getSingleResult();
	}

	/**
	 * Counts distinct sessions for an ArtWork filtered by language code within a date range.
	 */
	@Transactional
	public long countByArtWorkAndLanguageInRange(Long artWorkId, String languageCode, OffsetDateTime from, OffsetDateTime to) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Stat> root = cq.from(Stat.class);

		cq.select(cb.countDistinct(root.get("sessionId")));
		Predicate combined = cb.and(
				cb.equal(root.get("artWork").get("id"), artWorkId),
				cb.equal(root.get("language"), languageCode));
		if (from != null) {
			combined = cb.and(combined, cb.greaterThanOrEqualTo(root.get("timestamp"), from));
		}
		if (to != null) {
			combined = cb.and(combined, cb.lessThan(root.get("timestamp"), to));
		}
		cq.where(combined);
		return getEntityManager().createQuery(cq).getSingleResult();
	}

	/**
	 * Counts distinct sessions for a given sessionId filtered by language code within a date range.
	 */
	@Transactional
	public long countBySessionIdAndLanguageInRange(String sessionId, String languageCode, OffsetDateTime from, OffsetDateTime to) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Stat> root = cq.from(Stat.class);

		cq.select(cb.count(root));
		Predicate combined = cb.and(
				cb.equal(root.get("sessionId"), sessionId),
				cb.equal(root.get("language"), languageCode));
		if (from != null) {
			combined = cb.and(combined, cb.greaterThanOrEqualTo(root.get("timestamp"), from));
		}
		if (to != null) {
			combined = cb.and(combined, cb.lessThan(root.get("timestamp"), to));
		}
		cq.where(combined);
		return getEntityManager().createQuery(cq).getSingleResult();
	}

}
