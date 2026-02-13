package dellemuse.serverapp.serverdb.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.audit.AuditKey;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.ArtExhibitionSection;
import dellemuse.serverapp.serverdb.model.ArtWork;
 
import dellemuse.serverapp.serverdb.model.AuditAction;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionRecord;
 
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.serverdb.service.record.ArtExhibitionRecordDBService;
 
import io.odilon.util.Check;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class ArtExhibitionDBService extends  MultiLanguageObjectDBservice<ArtExhibition, Long> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(ArtExhibitionDBService.class.getName());

	@JsonIgnore
	@Autowired
	final ArtExhibitionRecordDBService artExhibitionRecordDBService;

	@JsonIgnore
	@Autowired
	@PersistenceContext
	private EntityManager entityManager;

	public ArtExhibitionDBService(CrudRepository<ArtExhibition, Long> repository, ServerDBSettings settings, ArtExhibitionRecordDBService artExhibitionRecordDBService) {
		super(repository, settings);
		this.artExhibitionRecordDBService = artExhibitionRecordDBService;
	}
	 
	@Transactional
	public ArtExhibition create(String name, User createdBy) {
		
		Check.requireNonNullArgument(createdBy, "createdBy is null");
		Check.requireNonNullArgument(name, "name is null");
		
		ArtExhibition c = new ArtExhibition();
		c.setName(name);

		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
		c.setState(ObjectState.EDITION);
		c.setMasterLanguage(getDefaultMasterLanguage());
		c.setLanguage(getDefaultMasterLanguage());

		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy, AuditAction.CREATE));
		
		for (Language la : getLanguageService().getLanguages())
			getArtExhibitionRecordDBService().create(c, la.getLanguageCode(), createdBy);
		
		return c;
	}

	@Transactional
	public ArtExhibition create(String name, Site site, User createdBy) {

		Check.requireNonNullArgument(createdBy, "createdBy is null");
		Check.requireNonNullArgument(name, "name is null");
		Check.requireNonNullArgument(site, "site is null");

		Check.requireTrue(site.getState()!=null, "site state is null");
		Check.requireTrue(site.getLanguage()!=null, "site language is null");
		Check.requireTrue(site.getMasterLanguage()!=null, "site Master language is null");

		ArtExhibition c = new ArtExhibition();
		c.setName(name);
			
		c.setMasterLanguage(site.getMasterLanguage());
		c.setLanguage(site.getLanguage());

		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
		c.setState(ObjectState.EDITION);
		c.setSite(site);

		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy, AuditAction.CREATE));
			
		for (Language la: getLanguageService().getLanguages())
			getArtExhibitionRecordDBService().create(c, la.getLanguageCode(), createdBy);

		return c;
	}

	
	
	@Transactional	
	public void save(ArtExhibition o, User user, List<String> updatedParts) {
		super.save(o);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(o, user, AuditAction.UPDATE, String.join(", ", updatedParts)));
	}

	/**
	 *  ArtExhibition (1)
	 *  ArtExhibitionRecord(n) 
	 *  ArtExhibitionItem (n)
	 */
	@Transactional
	public void markAsDeleted(ArtExhibition c, User deletedBy) {
		
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(deletedBy);
		c.setState(ObjectState.DELETED);
		
		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, deletedBy, AuditAction.DELETE, AuditKey.MARK_AS_DELETED));
		
		for (ArtExhibitionRecord g : getArtExhibitionRecordDBService().findAllByGuideContent(c)) {
			getArtExhibitionRecordDBService().markAsDeleted(g, deletedBy);
		}
		
		c.getArtExhibitionItems().forEach(gc -> {
			getArtExhibitionItemDBService().markAsDeleted(gc, deletedBy);
		});
	}
 	
	@Transactional
	public void restore(ArtExhibition c, User restoredBy) {

		OffsetDateTime date = OffsetDateTime.now();
		c.setLastModified(date);
		c.setLastModifiedUser(restoredBy);
		c.setState(ObjectState.EDITION);
		
		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, restoredBy,  AuditAction.UPDATE, AuditKey.RESTORE));
		
		for (ArtExhibitionRecord g : getArtExhibitionRecordDBService().findAllByGuideContent(c)) {
			getArtExhibitionRecordDBService().restore(g, restoredBy);
		}
		
		c.getArtExhibitionItems().forEach(gc -> {
			getArtExhibitionItemDBService().restore(gc, restoredBy);
		});
	}

	@Transactional
	public Optional<ArtExhibition> findWithDeps(Long id) {

		Optional<ArtExhibition> o = super.findById(id);

		if (o.isEmpty())
			return o;

		ArtExhibition a = o.get();

		Long siteId = a.getSite().getId();

		if (siteId != null) {
			SiteDBService se = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
			a.setSite(se.findById(siteId).get());
		}

		if (a.getArtExhibitionItems() != null) {
			ArtExhibitionDBService se = (ArtExhibitionDBService) ServiceLocator.getInstance().getBean(ArtExhibitionDBService.class);
			a.setArtExhibitionItems(se.getArtExhibitionItems(a));
		}

		Resource photo = a.getPhoto();

		if (photo != null)
			photo.getBucketName();

		a.setDependencies(true);

		return o;
	}

	@Transactional
	public void addItem(ArtExhibition exhibition, ArtWork artwork, User addedBy) {

		boolean contains = false;

		List<ArtExhibitionItem> list = getArtExhibitionItems(exhibition);

		for (ArtExhibitionItem i : list) {
			if (artwork.getId().equals(i.getArtWork().getId())) {
				contains = true;
				break;
			}
		}

		if (contains)
			return;

		ArtExhibitionItem item = getArtExhibitionItemDBService().create(artwork.getName(), exhibition, artwork, addedBy);

		list.add(item);
		exhibition.setArtExhibitionItems(list);
		exhibition.setLastModified(OffsetDateTime.now());
		exhibition.setLastModifiedUser(addedBy);
		
		getRepository().save(exhibition);
		getDelleMuseAuditDBService().save(DelleMuseAudit.ofArtExhibition(exhibition, addedBy, AuditAction.UPDATE, AuditKey.ADD_ITEM, item));
		
	}

	@Transactional
	public void removeItem(ArtExhibition c, ArtExhibitionItem item, User removedBy) {

		boolean contains = false;
		int index = -1;

		List<ArtExhibitionItem> list = getArtExhibitionItems(c);

		for (ArtExhibitionItem i : list) {
			index++;
			if (item.getId().equals(i.getId())) {
				contains = true;
				break;
			}
		}

		if (!contains)
			return;

		list.remove(index);
		c.setArtExhibitionItems(list);
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(removedBy);

		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, removedBy, AuditAction.UPDATE));
		getRepository().save(c);

	}

	@Transactional
	public void removeSection(ArtExhibition ex, ArtExhibitionSection item, User removedBy) {

		boolean contains = false;
		int index = -1;

		List<ArtExhibitionSection> list = getArtExhibitionSections(ex);

		for (ArtExhibitionSection i : list) {
			index++;
			if (item.getId().equals(i.getId())) {
				contains = true;
				break;
			}
		}

		if (!contains)
			return;

		list.remove(index);
		ex.setArtExhibitionSections(list);
		ex.setLastModified(OffsetDateTime.now());
		ex.setLastModifiedUser(removedBy);
		
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(ex, removedBy, AuditAction.UPDATE));
		getRepository().save(ex);
 	}
 
	@Transactional
	public List<ArtExhibition> getByName(String name) {
		return createNameQuery(name).getResultList();
	}
	
	@Transactional
	public Boolean isArtExhibitionGuides(ArtExhibition exhibition) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArtExhibitionGuide> cq = cb.createQuery(ArtExhibitionGuide.class);
		
		Root<ArtExhibitionGuide> root = cq.from(ArtExhibitionGuide.class);
	
		Predicate p0 = cb.equal(root.get("artExhibition").get("id"), String.valueOf(exhibition.getId()));
		Predicate p1 = cb.equal(root.get("state"), ObjectState.EDITION);
		Predicate p2 = cb.equal(root.get("state"), ObjectState.PUBLISHED);
		Predicate statePredicate = cb.or(p1, p2);
		Predicate combinedPredicate = cb.and(p0, statePredicate);
		cq.select(root).where(combinedPredicate);
		
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));
		List<ArtExhibitionGuide> list = getEntityManager().createQuery(cq).getResultList();
	
		return (list!=null && list.size()>0);
	}
	
	@Transactional
	public List<ArtExhibitionGuide> getArtExhibitionGuides(ArtExhibition exhibition) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArtExhibitionGuide> cq = cb.createQuery(ArtExhibitionGuide.class);
		Root<ArtExhibitionGuide> root = cq.from(ArtExhibitionGuide.class);
		cq.select(root).where(cb.equal(root.get("artExhibition").get("id"), exhibition.getId()));
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));

		return getEntityManager().createQuery(cq).getResultList();
	}


	
	@Transactional
	public List<ArtExhibitionGuide> getArtExhibitionGuides(ArtExhibition exhibition, ObjectState o1, ObjectState o2) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArtExhibitionGuide> cq = cb.createQuery(ArtExhibitionGuide.class);
		Root<ArtExhibitionGuide> root = cq.from(ArtExhibitionGuide.class);
		
		
		
		Predicate p0 = cb.equal(root.get("artExhibition").get("id"), String.valueOf(exhibition.getId()));
		Predicate p1 = cb.equal(root.get("state"), ObjectState.EDITION);
		Predicate p2 = cb.equal(root.get("state"), ObjectState.PUBLISHED);
		Predicate statePredicate = cb.or(p1, p2);
		Predicate combinedPredicate = cb.and(p0, statePredicate);
		cq.select(root).where(combinedPredicate);
		
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));

		return getEntityManager().createQuery(cq).getResultList();
	}
	
	
	
	
	
	
	@Transactional
	public List<ArtExhibitionItem> getArtExhibitionItems(ArtExhibition exhibition) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArtExhibitionItem> cq = cb.createQuery(ArtExhibitionItem.class);
		Root<ArtExhibitionItem> root = cq.from(ArtExhibitionItem.class);
		cq.select(root).where(cb.equal(root.get("artExhibition").get("id"), exhibition.getId()));
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public List<ArtExhibitionItem> getArtExhibitionItems(ArtExhibition exhibition, ObjectState os1) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArtExhibitionItem> cq = cb.createQuery(ArtExhibitionItem.class);
		Root<ArtExhibitionItem> root = cq.from(ArtExhibitionItem.class);
		
		Predicate p0 = cb.equal(root.get("artExhibition").get("id"), String.valueOf(exhibition.getId()));
		Predicate p1 = cb.equal(root.get("state"), os1);
			 
		Predicate finalredicate = cb.and(p0, p1);
		cq.select(root).where(finalredicate);
		
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public List<ArtExhibitionItem> getArtExhibitionItems(ArtExhibition exhibition, ObjectState os1, ObjectState os2) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArtExhibitionItem> cq = cb.createQuery(ArtExhibitionItem.class);
		Root<ArtExhibitionItem> root = cq.from(ArtExhibitionItem.class);
			
		Predicate p0 = cb.equal(root.get("artExhibition").get("id"), String.valueOf(exhibition.getId()));
		Predicate p1 = cb.equal(root.get("state"), os1);
		Predicate p2 = cb.equal(root.get("state"), os2);

		Predicate combinedPredicate = cb.or(p1, p2);
		Predicate finalredicate = cb.and(p0, combinedPredicate);
		cq.select(root).where(finalredicate);
		
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));
		return getEntityManager().createQuery(cq).getResultList();
	}
	
	@Transactional
	public List<ArtExhibitionSection> getArtExhibitionSections(ArtExhibition exhibition) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArtExhibitionSection> cq = cb.createQuery(ArtExhibitionSection.class);
		Root<ArtExhibitionSection> root = cq.from(ArtExhibitionSection.class);
		cq.select(root).where(cb.equal(root.get("artExhibition").get("id"), exhibition.getId()));
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public List<ArtExhibition> getArtExhibitions(Site site) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArtExhibition> cq = cb.createQuery(ArtExhibition.class);
		Root<ArtExhibition> root = cq.from(ArtExhibition.class);
		cq.select(root).where(cb.equal(root.get("site").get("id"), site.getId()));
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));

		return getEntityManager().createQuery(cq).getResultList();
	}

	
	@Transactional
	public List<ArtExhibition> getArtExhibitionsByOrdinal(Site site) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArtExhibition> cq = cb.createQuery(ArtExhibition.class);
		Root<ArtExhibition> root = cq.from(ArtExhibition.class);
		cq.select(root).where(cb.equal(root.get("site").get("id"), site.getId()));
		cq.orderBy(cb.asc(root.get("ordinal")));

		return getEntityManager().createQuery(cq).getResultList();
	}
	
	@Override
	public String getObjectClassName() {
		 return ArtExhibition.class.getSimpleName().toLowerCase();
	}

	@Override
	protected Class<ArtExhibition> getEntityClass() {
		return ArtExhibition.class;
	}
	
	protected ArtExhibitionRecordDBService getArtExhibitionRecordDBService() {
		return this.artExhibitionRecordDBService;
	}

	@PostConstruct
	protected void onInitialize() {
		super.registerRecordDB(getEntityClass(), getArtExhibitionRecordDBService());
		super.register(getEntityClass(), this);
	}

}
