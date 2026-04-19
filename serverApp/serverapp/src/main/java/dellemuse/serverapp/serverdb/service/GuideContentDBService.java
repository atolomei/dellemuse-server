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
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.AudioStudio;
import dellemuse.serverapp.serverdb.model.AuditAction;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.ObjectState;
 
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.GuideContentRecord;
import dellemuse.serverapp.serverdb.service.record.GuideContentRecordDBService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class GuideContentDBService extends MultiLanguageObjectDBservice<GuideContent, Long> {

	@SuppressWarnings("unused")
	static private Logger logger = Logger.getLogger(GuideContentDBService.class.getName());

	@JsonIgnore
	@Autowired
	final GuideContentRecordDBService guideContentRecordDBService;

	public GuideContentDBService(CrudRepository<GuideContent, Long> repository, ServerDBSettings settings, GuideContentRecordDBService guideContentRecordDBService) {
		super(repository, settings);
		this.guideContentRecordDBService = guideContentRecordDBService;
	}

	@Transactional
	public GuideContent create(ArtExhibitionGuide guide, ArtExhibitionItem item, User creeatedBy) {

		boolean exists = existsInGuide(guide, item);

		if (exists) {
			List<GuideContent> list = findByArtExhibitionItem(guide, item);

			if (list.size() > 0)
				return list.get(0);

			return null;
		}

		// Re-fetch the user within this transaction to avoid shared collection references
	    User managedUser = getUserDBService().findById(creeatedBy.getId()).orElse(creeatedBy);
	    
		GuideContent c = new GuideContent();

		c.setMasterLanguage(item.getMasterLanguage());
		c.setLanguage(item.getLanguage());

		c.setState(ObjectState.EDITION);

		if (!guide.isDependencies())
			guide = getArtExhibitionGuideDBService().findWithDeps(guide.getId()).get();

		if (!item.isDependencies())
			item = getArtExhibitionItemDBService().findWithDeps(item.getId()).get();

		ArtExhibition aex = guide.getArtExhibition();
		if (!aex.isDependencies())
			aex = getArtExhibitionDBService().findWithDeps(aex.getId()).get();

		Site site = aex.getSite();
		
		// Flush and clear session before native query to avoid shared collection flush issues
	    getEntityManager().flush();
	    getEntityManager().clear();

	    
		c.setAudioId(newAudioId(site.getId()));
		c.setName(item.getName());
		
		// Re-fetch associations after clear since they are now detached
	    guide = getArtExhibitionGuideDBService().findById(guide.getId()).get();
	    item = getArtExhibitionItemDBService().findById(item.getId()).get();
	    managedUser = getUserDBService().findById(managedUser.getId()).orElse(creeatedBy);
	    
		c.setArtExhibitionGuide(guide);
		c.setArtExhibitionItem(item);
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser( managedUser);

		if (item.getArtWork().getAudioId() == null) {
			item.getArtWork().setAudioId(newAudioId(site.getId()));
			getArtWorkDBService().save(item.getArtWork(), getUserDBService().findRoot(), "audioid");
		}

		
		Long artworkaudioid = item.getArtWork().getAudioId();
		c.setArtWorkAudioId(artworkaudioid);
		
		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c,  managedUser, AuditAction.CREATE));

		for (Language la : getLanguageService().getLanguages())
			getGuideContentRecordDBService().create(c, la.getLanguageCode(), managedUser);

		return c;
	}

	/**
	 * 
	 * 
	 * @param o
	 * @param user
	 * @param updatedParts
	 */
	@Transactional
	public void save(GuideContent o, User user, List<String> updatedParts) {
		super.save(o);

		getDelleMuseAuditDBService().save(DelleMuseAudit.of(o, user, AuditAction.UPDATE, String.join(", ", updatedParts)));

		ArtExhibitionItem item = o.getArtExhibitionItem();

		if (item != null) {
			item = getArtExhibitionItemDBService().findWithDeps(item.getId()).get();
			ArtWork a = item.getArtWork();
			if (a.getAudioId() == null) {
				Site site = a.getSite();
				item.getArtWork().setAudioId(newAudioId(site.getId()));
				getArtWorkDBService().save(item.getArtWork(), user, "audioid");
				o.setArtWorkAudioId(item.getArtWork().getAudioId());
			}
		}

		Optional<AudioStudio> oa = getAudioStudioDBService().findByGuideContent(o);

		if (oa.isPresent()) {
			oa.get().setName(o.getName());
			oa.get().setInfo(o.getInfo());
			oa.get().setInfoAccessible(o.getInfoAccessible());
			getAudioStudioDBService().save(oa.get());
		}
	}

	
	@Transactional
	public void setArtworkAudioId(GuideContent g) {
		
		
		Optional<ArtExhibitionItem> o = getArtExhibitionItemDBService().findById(g.getArtExhibitionItem().getId());
		
				if (o.isEmpty())
					return;
		
				ArtExhibitionItem item = o.get();
				
				if (item.getArtWork() == null)
					return;
				
				Long id=item.getArtWork().getId();
		
				if (id==null)
					return;
		
				ArtWork a = getArtWorkDBService().findById(id).get();
	
				Long aid = a.getAudioId();

				if (aid==null)
					return;
		
			g.setArtWorkAudioId(aid);
			save(g);

	}
	
	
	@Transactional
	public void delete(GuideContent c, User deletedBy) {

		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(deletedBy);
		c.setState(ObjectState.DELETED);

		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, deletedBy, AuditAction.DELETE));
	}

	/**
	 * guideContent (1) guideContentRecord (n) AudioStudio (1)
	 * 
	 */
	@Transactional
	public void markAsDeleted(GuideContent c, User deletedBy) {

		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(deletedBy);
		c.setState(ObjectState.DELETED);

		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, deletedBy, AuditAction.DELETE, AuditKey.MARK_AS_DELETED));

		for (GuideContentRecord g : getGuideContentRecordDBService().findAllByGuideContent(c)) {
			getGuideContentRecordDBService().markAsDeleted(g, deletedBy);
		}

		Optional<AudioStudio> o = getAudioStudioDBService().findByGuideContent(c);
		if (o.isPresent())
			getAudioStudioDBService().markAsDeleted(o.get(), deletedBy);
	}

	/**
	 * guideContent (1) guideContentRecord (n) AudioStudio (1)
	 */
	@Transactional
	public void restore(GuideContent c, User restoredBy) {
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(restoredBy);
		c.setState(ObjectState.EDITION);

		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, restoredBy, AuditAction.UPDATE));

		for (GuideContentRecord g : getGuideContentRecordDBService().findAllByGuideContent(c)) {
			getGuideContentRecordDBService().restore(g, restoredBy);
		}

		Optional<AudioStudio> o = getAudioStudioDBService().findByGuideContent(c);
		if (o.isPresent()) {
			getAudioStudioDBService().restore(o.get(), restoredBy);
		}
	}

	@Transactional
	public boolean existsInGuide(ArtExhibitionGuide guide, ArtExhibitionItem item) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<GuideContent> cq = cb.createQuery(getEntityClass());
		Root<GuideContent> root = cq.from(getEntityClass());

		Predicate p_item = cb.equal(root.get("artExhibitionItem").get("id"), item.getId());
		Predicate p_guide = cb.equal(root.get("artExhibitionGuide").get("id"), guide.getId());

		Predicate combinedPredicate = cb.and(p_item, p_guide);
		cq.select(root).where(combinedPredicate);

		List<GuideContent> list = getEntityManager().createQuery(cq).getResultList();

		return list.size() > 0;
	}

	@Transactional
	public List<GuideContent> findByArtExhibitionItem(ArtExhibitionGuide guide, ArtExhibitionItem item) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<GuideContent> cq = cb.createQuery(getEntityClass());
		Root<GuideContent> root = cq.from(getEntityClass());
		cq.select(root).where(cb.equal(root.get("artExhibitionItem").get("id"), item.getId()));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public List<GuideContent> getBySite(Site site) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<GuideContent> cq = cb.createQuery(getEntityClass());
		Root<GuideContent> root = cq.from(getEntityClass());

		Predicate p1 = cb.equal(root.get("artExhibitionItem").get("artExhibition").get("site").get("id"), site.getId());
		cq.select(root).where(p1);

		cq.orderBy(cb.asc(cb.lower(root.get("name"))));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public List<GuideContent> getByAudioId(Site site) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<GuideContent> cq = cb.createQuery(getEntityClass());

		Root<GuideContent> root = cq.from(getEntityClass());

		Predicate p1 = cb.equal(root.get("artExhibitionItem").get("artExhibition").get("site").get("id"), site.getId());
		cq.select(root).where(p1);
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));

		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public List<GuideContent> getByAudioId(Site site, Long aid) {

		if (aid == null)
			return getByAudioId(site);

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<GuideContent> cq = cb.createQuery(getEntityClass());

		Root<GuideContent> root = cq.from(getEntityClass());

		Predicate p1 = cb.equal(root.get("artExhibitionItem").get("artExhibition").get("site").get("id"), site.getId());
		Predicate p2 = cb.equal(root.get("audioId"), aid);

		Predicate combinedPredicate = cb.and(p1, p2);
		cq.select(root).where(combinedPredicate);
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));

		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public List<GuideContent> getByAudioId(Site site, Long aid, ObjectState os1) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<GuideContent> cq = cb.createQuery(getEntityClass());

		Root<GuideContent> root = cq.from(getEntityClass());

		Predicate p1;
		Predicate p2;
		Predicate p3;
		Predicate combinedPredicate;

		p1 = cb.equal(root.get("artExhibitionItem").get("artExhibition").get("site").get("id"), site.getId());

		if (aid != null) {
			p2 = cb.equal(root.get("audioId"), aid);
			p3 = cb.equal(root.get("state"), os1);
			combinedPredicate = cb.and(p1, p2, p3);
		} else {
			p3 = cb.equal(root.get("state"), os1);
			combinedPredicate = cb.and(p1, p3);
		}
		cq.select(root).where(combinedPredicate);
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public List<GuideContent> getByArtWorkAudioId(Site site, Long aid) {

		if (aid == null)
			return getByAudioId(site);

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<GuideContent> cq = cb.createQuery(getEntityClass());

		Root<GuideContent> root = cq.from(getEntityClass());

		Predicate p1 = cb.equal(root.get("artExhibitionItem").get("artExhibition").get("site").get("id"), site.getId());
		Predicate p2 = cb.equal(root.get("artWorkAudioId"), aid);

		Predicate combinedPredicate = cb.and(p1, p2);
		cq.select(root).where(combinedPredicate);
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));

		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public List<GuideContent> getByArtWorkId(Long aid) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<GuideContent> cq = cb.createQuery(getEntityClass());

		Root<GuideContent> root = cq.from(getEntityClass());
	 
		Predicate p1 = cb.equal(root.get("artExhibitionItem").get("artWork").get("id"), aid);
		Predicate p2 = cb.equal(root.get("state"), ObjectState.PUBLISHED);

		Predicate combinedPredicate = cb.and(p1, p2);
		cq.select(root).where(combinedPredicate);
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));

		return getEntityManager().createQuery(cq).getResultList();
	}
	
	
	
	@Transactional
	public List<GuideContent> getByArtWorkAudioId(Site site, Long aid, ObjectState os1) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<GuideContent> cq = cb.createQuery(getEntityClass());

		Root<GuideContent> root = cq.from(getEntityClass());

		Predicate p1;
		Predicate p2;
		Predicate p3;
		Predicate combinedPredicate;

		p1 = cb.equal(root.get("artExhibitionItem").get("artExhibition").get("site").get("id"), site.getId());

		if (aid != null) {
			p2 = cb.equal(root.get("artWorkAudioId"), aid);
			p3 = cb.equal(root.get("state"), os1);
			combinedPredicate = cb.and(p1, p2, p3);
		} else {
			p3 = cb.equal(root.get("state"), os1);
			combinedPredicate = cb.and(p1, p3);
		}
		cq.select(root).where(combinedPredicate);
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));
		return getEntityManager().createQuery(cq).getResultList();
	}
	

	@Transactional
	public List<GuideContent> getByArtWorkAudioId(Site site, Long aid, ObjectState os1, ObjectState os2) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<GuideContent> cq = cb.createQuery(getEntityClass());

		Root<GuideContent> root = cq.from(getEntityClass());

		Predicate p1 = cb.equal(root.get("artExhibitionItem").get("artExhibition").get("site").get("id"), site.getId());

		Predicate s1 = cb.equal(root.get("state"), os1);
		Predicate s2 = cb.equal(root.get("state"), os2);

		Predicate statePredicate = cb.or(s1, s2);
		Predicate combinedPredicate;

		if (aid != null) {
			Predicate paid = cb.equal(root.get("artWorkAudioId"), aid);
			combinedPredicate = cb.and(p1, paid, statePredicate);
		} else {
			combinedPredicate = cb.and(p1, statePredicate);
		}

		cq.select(root).where(combinedPredicate);
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));

		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public List<GuideContent> getByAudioId(Site site, Long aid, ObjectState os1, ObjectState os2) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<GuideContent> cq = cb.createQuery(getEntityClass());

		Root<GuideContent> root = cq.from(getEntityClass());

		Predicate p1 = cb.equal(root.get("artExhibitionItem").get("artExhibition").get("site").get("id"), site.getId());

		Predicate s1 = cb.equal(root.get("state"), os1);
		Predicate s2 = cb.equal(root.get("state"), os2);

		Predicate statePredicate = cb.or(s1, s2);
		Predicate combinedPredicate;

		if (aid != null) {
			Predicate paid = cb.equal(root.get("audioId"), aid);
			combinedPredicate = cb.and(p1, paid, statePredicate);
		} else {
			combinedPredicate = cb.and(p1, statePredicate);
		}

		cq.select(root).where(combinedPredicate);
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));

		return getEntityManager().createQuery(cq).getResultList();
	}

	public Long newAudioId(Long siteId) {
		return getSiteDBService().newAudioId(siteId);
	}

	@Transactional
	public void delete(GuideContent c) {
		this.getRepository().delete(c);
	}

	@Transactional
	public Optional<GuideContent> findWithDeps(Long id) {

		Optional<GuideContent> o = super.findById(id);

		if (o.isEmpty())
			return o;

		GuideContent a = o.get();

		// Read all lazy proxy IDs while entity is still attached
		Long guideId = a.getArtExhibitionGuide() != null ? a.getArtExhibitionGuide().getId() : null;
		Long itemId = a.getArtExhibitionItem() != null ? a.getArtExhibitionItem().getId() : null;
		Long photoId = a.getPhoto() != null ? a.getPhoto().getId() : null;
		Long audioId = a.getAudio() != null ? a.getAudio().getId() : null;
		Long userId = a.getLastModifiedUser() != null ? a.getLastModifiedUser().getId() : null;

		// Detach to prevent dirty-checking from triggering @PostUpdate
		getEntityManager().detach(a);

		if (guideId != null)
			a.setArtExhibitionGuide(getArtExhibitionGuideDBService().findById(guideId).get());

		if (itemId != null)
			a.setArtExhibitionItem(getArtExhibitionItemDBService().findById(itemId).get());

		if (photoId != null)
			a.setPhoto(getResourceDBService().findById(photoId).get());

		if (audioId != null)
			a.setAudio(getResourceDBService().findById(audioId).get());

		if (userId != null)
			a.setLastModifiedUser(getUserDBService().findById(userId).get());

		a.setDependencies(true);

		return o;
	}

	/**
	 * @param name
	 * @return
	 */
	public List<GuideContent> getByName(String name) {
		return createNameQuery(name).getResultList();
	}

	@Override
	protected Class<GuideContent> getEntityClass() {
		return GuideContent.class;
	}

	@Override
	public String getObjectClassName() {
		return GuideContentRecord.class.getSimpleName().toLowerCase();
	}

	public GuideContentRecordDBService getGuideContentRecordDBService() {
		return guideContentRecordDBService;
	}

	@PostConstruct
	protected void onInitialize() {
		super.registerRecordDB(getEntityClass(), getGuideContentRecordDBService());
		super.register(getEntityClass(), this);
	}

	/**
	 * Returns the ArtWork associated with a GuideContent via its ArtExhibitionItem.
	 * Returns {@code Optional.empty()} if the GuideContent has no ArtExhibitionItem
	 * or the ArtExhibitionItem has no ArtWork.
	 */
	@Transactional
	public Optional<ArtWork> getArtWork(GuideContent g) {
		if (g == null || g.getId() == null)
			return Optional.empty();
		// Re-fetch within current session to avoid LazyInitializationException
		GuideContent managed = getEntityManager().find(GuideContent.class, g.getId());
		if (managed == null)
			return Optional.empty();
		ArtExhibitionItem item = managed.getArtExhibitionItem();
		if (item == null)
			return Optional.empty();
		ArtWork aw = item.getArtWork();
		if (aw == null)
			return Optional.empty();
		return Optional.of(aw);
	}

}
