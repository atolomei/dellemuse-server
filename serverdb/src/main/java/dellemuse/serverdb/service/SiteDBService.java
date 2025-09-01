package dellemuse.serverdb.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.serverdb.ServerDBSettings;
import dellemuse.serverdb.model.ArtExhibition;
import dellemuse.serverdb.model.ArtExhibitionItem;
import dellemuse.serverdb.model.ArtWork;
import dellemuse.serverdb.model.Floor;
import dellemuse.serverdb.model.GuideContent;
import dellemuse.serverdb.model.Institution;
import dellemuse.serverdb.model.Room;
import dellemuse.serverdb.model.Site;
import dellemuse.serverdb.model.User;
import dellemuse.serverdb.service.base.ServiceLocator;
import dellemuse.model.FloorModel;
import dellemuse.model.logging.Logger;
import dellemuse.model.util.Check;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class SiteDBService extends DBService<Site, Long> {

	private static final Logger logger = Logger.getLogger(SiteDBService.class.getName());

	@PersistenceContext
	private EntityManager entityManager;

	public SiteDBService(CrudRepository<Site, Long> repository, ServerDBSettings settings) {
		super(repository, settings);
	}

	   @PostConstruct
	    protected void onInitialize() {
	    	ServiceLocator.getInstance().register(getEntityClass(), this);
	    }
	   
	@Transactional
	@Override
	public Site create(String name, User createdBy) {
		Site c = new Site();
		c.setName(name);
		c.setNameKey(nameKey(name));
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
		return getRepository().save(c);
	}

	@Transactional
	public Site create(String name, Institution institution, Optional<String> shortName, Optional<String> address,
			Optional<String> info, User createdBy) {
		Site c = new Site();
		c.setName(name);
		c.setInstitution(institution);
		c.setNameKey(nameKey(name));
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
		shortName.ifPresent(c::setShortName);
		address.ifPresent(c::setAddress);
		return getRepository().save(c);
	}

	@Transactional
	public List<Site> getByName(String name) {
		return createNameQuery(name).getResultList();
	}

	@Transactional
	public Optional<Site> findByShortName(String name) {
		Check.requireNonNullStringArgument(name, "name is null");
		CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<Site> cq = cb.createQuery(Site.class);
		Root<Site> root = cq.from(Site.class);
		cq.select(root).where(cb.equal(cb.lower(root.get("shortName")), name.toLowerCase()));
		cq.orderBy(cb.asc(root.get("id")));

		List<Site> list = this.entityManager.createQuery(cq).getResultList();
		return list == null || list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
	}

	@Transactional
	public List<ArtExhibition> getArtExhibitions(Site site) {
		return getArtExhibitions(site.getId());
	}

	@Transactional
	public List<ArtExhibition> getArtExhibitions(Long siteId) {
		CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<ArtExhibition> cq = cb.createQuery(ArtExhibition.class);
		Root<ArtExhibition> root = cq.from(ArtExhibition.class);
		cq.select(root).where(cb.equal(root.get("site").get("id"), siteId));
		cq.orderBy(cb.asc(root.get("title")));

		return entityManager.createQuery(cq).getResultList();
	}

	@Transactional
	public List<ArtExhibitionItem> getSiteArtExhibitionItems(Long siteId) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ArtExhibitionItem> cq = cb.createQuery(ArtExhibitionItem.class);
		Root<ArtExhibitionItem> root = cq.from(ArtExhibitionItem.class);
		cq.select(root).where(cb.equal(root.get("artexhibition").get("site").get("id"), siteId));
		cq.orderBy(cb.asc(root.get("title")));

		return entityManager.createQuery(cq).getResultList();
	}

	@Transactional
	public List<GuideContent> getSiteGuideContent(Long siteId) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<GuideContent> cq = cb.createQuery(GuideContent.class);
		Root<GuideContent> root = cq.from(GuideContent.class);
		cq.select(root)
				.where(cb.equal(root.get("artexhibitionitem").get("artexhibition").get("site").get("id"), siteId));
		cq.orderBy(cb.asc(root.get("title")));

		return entityManager.createQuery(cq).getResultList();
	}

	@Transactional
	public List<Floor> getFloors(Long siteId) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Floor> cq = cb.createQuery(Floor.class);
		Root<Floor> root = cq.from(Floor.class);
		cq.select(root).where(cb.equal(root.get("site").get("id"), siteId));
		cq.orderBy(cb.asc(root.get("title")));

		return entityManager.createQuery(cq).getResultList();
	}

	@Transactional
	public List<Room> getRooms(Long floorId) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Room> cq = cb.createQuery(Room.class);
		Root<Room> root = cq.from(Room.class);
		cq.select(root).where(cb.equal(root.get("floor").get("id"), floorId));
		cq.orderBy(cb.asc(root.get("title")));

		return entityManager.createQuery(cq).getResultList();
	}

	@Override
	protected Class<Site> getEntityClass() {
		return Site.class;
	}

	@Transactional
	public List<ArtWork> getSiteArtWork(Long siteId) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ArtWork> cq = cb.createQuery(ArtWork.class);
		Root<ArtWork> root = cq.from(ArtWork.class);

		cq.select(root).where(cb.equal(root.get("site").get("id"), siteId));
		cq.orderBy(cb.asc(root.get("title")));

		return entityManager.createQuery(cq).getResultList();
	}

}

/**
 * @Service public class SiteDBService extends DBService<Site, Long> {
 * 
 *          @SuppressWarnings("unused") static private Logger logger =
 *          Logger.getLogger(SiteDBService.class.getName());
 * 
 *          public SiteDBService(CrudRepository<Site, Long> repository,
 *          EntityManagerFactory entityManagerFactory, Settings settings) {
 *          super(repository, entityManagerFactory, settings); }
 * 
 * @Transactional
 * @Override public Site create(String name, User createdBy) { Site c = new
 *           Site(); c.setName(name); c.setNameKey(nameKey(name));
 *           c.setCreated(OffsetDateTime.now());
 *           c.setLastModified(OffsetDateTime.now());
 *           c.setLastModifiedUser(createdBy); return getRepository().save(c); }
 * 
 * @Transactional public Site create(String name, Institution institution,
 *                Optional<String> shortName, Optional<String> address,
 *                Optional<String> info, User createdBy) { Site c = new Site();
 *                c.setName(name); c.setInstitution(institution);
 *                c.setNameKey(nameKey(name));
 *                c.setCreated(OffsetDateTime.now());
 *                c.setLastModified(OffsetDateTime.now());
 *                c.setLastModifiedUser(createdBy);
 *                c.setShortName(shortName.get()); c.setAddress(address.get());
 *                return getRepository().save(c); }
 * 
 * @Transactional public List<Site> getByName(String name) { return
 *                createNameQuery(name).getResultList(); }
 * 
 * @Transactional public Optional<Site> findByShortName(String name) {
 *                TypedQuery<Site> query; CriteriaBuilder criteriabuilder =
 *                getSessionFactory().getCurrentSession().getCriteriaBuilder();
 *                CriteriaQuery<Site> criteria =
 *                criteriabuilder.createQuery(Site.class); Root<Site> loaders =
 *                criteria.from(Site.class);
 *                criteria.orderBy(criteriabuilder.asc(loaders.get("id")));
 *                ParameterExpression<String> idparameter =
 *                criteriabuilder.parameter(String.class);
 *                criteria.select(loaders).where(criteriabuilder.equal(loaders.get("shortName"),
 *                idparameter)); query =
 *                getSessionFactory().getCurrentSession().createQuery(criteria);
 *                query.setHint("org.hibernate.cacheable", true);
 *                query.setFlushMode(FlushModeType.COMMIT);
 *                query.setParameter(idparameter, name); List<Site> list =
 *                query.getResultList(); if (list == null || list.isEmpty())
 *                return Optional.empty(); return Optional.of(list.get(0)); }
 * 
 * @Transactional public List<ArtExhibition> getArtExhibitions(Site site) {
 *                return getArtExhibitions(site.getId()); }
 * 
 * @Transactional public List<ArtExhibition> getArtExhibitions(Long siteid) {
 *                TypedQuery<ArtExhibition> query; CriteriaBuilder
 *                criteriabuilder =
 *                getSessionFactory().getCurrentSession().getCriteriaBuilder();
 *                CriteriaQuery<ArtExhibition> criteria =
 *                criteriabuilder.createQuery(ArtExhibition.class);
 *                Root<ArtExhibition> loaders =
 *                criteria.from(ArtExhibition.class);
 *                criteria.orderBy(criteriabuilder.asc(loaders.get("title")));
 *                ParameterExpression<Long> idparameter =
 *                criteriabuilder.parameter(Long.class);
 *                criteria.select(loaders).where(criteriabuilder.equal(loaders.get("site").get("id"),
 *                idparameter)); query =
 *                getSessionFactory().getCurrentSession().createQuery(criteria);
 *                query.setHint("org.hibernate.cacheable", true);
 *                query.setFlushMode(FlushModeType.COMMIT);
 *                query.setParameter(idparameter, siteid); return
 *                query.getResultList(); }
 * 
 * @Transactional public List<Floor> getFloors(Long siteid) { TypedQuery<Floor>
 *                query; CriteriaBuilder criteriabuilder =
 *                getSessionFactory().getCurrentSession().getCriteriaBuilder();
 *                CriteriaQuery<Floor> criteria =
 *                criteriabuilder.createQuery(Floor.class); Root<Floor> loaders
 *                = criteria.from(Floor.class);
 *                criteria.orderBy(criteriabuilder.asc(loaders.get("title")));
 *                ParameterExpression<Long> idparameter =
 *                criteriabuilder.parameter(Long.class);
 *                criteria.select(loaders).where(criteriabuilder.equal(loaders.get("site").get("id"),
 *                idparameter)); query =
 *                getSessionFactory().getCurrentSession().createQuery(criteria);
 *                query.setHint("org.hibernate.cacheable", true);
 *                query.setFlushMode(FlushModeType.COMMIT);
 *                query.setParameter(idparameter, siteid); return
 *                query.getResultList(); }
 * 
 * @Transactional public List<Room> getRooms(Long floorid) { TypedQuery<Room>
 *                query; CriteriaBuilder criteriabuilder =
 *                getSessionFactory().getCurrentSession().getCriteriaBuilder();
 *                CriteriaQuery<Room> criteria =
 *                criteriabuilder.createQuery(Room.class); Root<Room> loaders =
 *                criteria.from(Room.class);
 *                criteria.orderBy(criteriabuilder.asc(loaders.get("title")));
 *                ParameterExpression<Long> idparameter =
 *                criteriabuilder.parameter(Long.class);
 *                criteria.select(loaders).where(criteriabuilder.equal(loaders.get("floor").get("id"),
 *                idparameter)); query =
 *                getSessionFactory().getCurrentSession().createQuery(criteria);
 *                query.setHint("org.hibernate.cacheable", true);
 *                query.setFlushMode(FlushModeType.COMMIT);
 *                query.setParameter(idparameter, floorid); return
 *                query.getResultList(); }
 * 
 * @Override protected Class<Site> getEntityClass() { return Site.class; } }
 **/
