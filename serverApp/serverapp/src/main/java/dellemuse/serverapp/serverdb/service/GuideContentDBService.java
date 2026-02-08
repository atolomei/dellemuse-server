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
import dellemuse.serverapp.serverdb.model.AudioStudio;
import dellemuse.serverapp.serverdb.model.AuditAction;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Resource;
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
	public GuideContent create(ArtExhibitionGuide guide, ArtExhibitionItem item, User createdBy) {

		boolean exists = existsInGuide(guide, item);

		if (exists) {
			List<GuideContent> list = findByArtExhibitionItem(guide, item);

			if (list.size() > 0)
				return list.get(0);

			return null;
		}

		GuideContent c = new GuideContent();

		c.setMasterLanguage(item.getMasterLanguage());
		c.setLanguage(item.getLanguage());

		c.setState(ObjectState.EDITION);

		if (!guide.isDependencies())
			guide = getArtExhibitionGuideDBService().findWithDeps(guide.getId()).get();

		ArtExhibition aex = guide.getArtExhibition();

		if (!aex.isDependencies())
			aex = getArtExhibitionDBService().findWithDeps(aex.getId()).get();

		Site site = aex.getSite();
		c.setAudioId(newAudioId(site));
		c.setName(item.getName());
		c.setArtExhibitionGuide(guide);
		c.setArtExhibitionItem(item);
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);

		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy, AuditAction.CREATE));

		for (Language la : getLanguageService().getLanguages())
			getGuideContentRecordDBService().create(c, la.getLanguageCode(), createdBy);

		return c;
	}

	@Transactional
	public void save(GuideContent o, User user, List<String> updatedParts) {
		super.save(o);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(o, user, AuditAction.UPDATE, String.join(", ", updatedParts)));

		Optional<AudioStudio> oa = getAudioStudioDBService().findByGuideContent(o);

		if (oa.isPresent()) {
			oa.get().setName(o.getName());
			oa.get().setInfo(o.getInfo());
			oa.get().setInfoAccessible(o.getInfoAccessible());
			
			getAudioStudioDBService().save(oa.get());
		}
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
		
		Predicate p_item 	= cb.equal(root.get("artExhibitionItem").get("id"), item.getId());
		Predicate p_guide 	= cb.equal(root.get("artExhibitionGuide").get("id"), guide.getId());
		
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
		
		if (aid==null)
			return getByAudioId(site);
		
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<GuideContent> cq = cb.createQuery(getEntityClass());

		Root<GuideContent> root = cq.from(getEntityClass());

	 	Predicate p1 = cb.equal(root.get("artExhibitionItem").get("artExhibition").get("site").get("id"), site.getId());
		Predicate p2 = cb.equal(root.get("audioId"), aid );
		
		Predicate combinedPredicate = cb.and(p1, p2);
		cq.select(root).where(combinedPredicate);
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));

		return getEntityManager().createQuery(cq).getResultList();
	}
	
	/**
	 * 
	 * 
	 * @param site
	 * @param aid
	 * @param os1
	 * 
	 * @return
	 */
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
		
		if (aid!=null) {
			p2 = cb.equal(root.get("audioId"), aid );
			p3 = cb.equal(root.get("state"), os1);
			combinedPredicate = cb.and(p1, p2, p3);
		}	
		else {
			p3 = cb.equal(root.get("state"), os1);
			combinedPredicate = cb.and(p1, p3);
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
		
		if (aid!=null) {
			Predicate paid = cb.equal(root.get("audioId"), aid );
			combinedPredicate = cb.and(p1, paid, statePredicate);
		}
		else {
			combinedPredicate = cb.and(p1, statePredicate);
		}

		cq.select(root).where(combinedPredicate);
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));

		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public Long newAudioId(Site site) {
		String seqName = site.getAudioIdSequencerName();
		return ((Number) getEntityManager().createNativeQuery("SELECT nextval('" + seqName + "')").getSingleResult()).longValue();
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

		if (a.getArtExhibitionGuide() != null) {
			ArtExhibitionGuide g = a.getArtExhibitionGuide();
			a.setArtExhibitionGuide( getArtExhibitionGuideDBService().findById(g.getId()).get());
		}
		
		if (a.getArtExhibitionItem() != null) {
			ArtExhibitionItem item = a.getArtExhibitionItem();
			a.setArtExhibitionItem( getArtExhibitionItemDBService().findById(item.getId()).get());
		}
		
		if (a.getPhoto() != null) {
			Resource r=getResourceDBService().findById( a.getPhoto().getId()).get();
			a.setPhoto(r);
		}
		
		if (a.getAudio() != null) {
			Resource r=getResourceDBService().findById( a.getAudio().getId()).get();
			a.setAudio(r);
		}
		
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

}
