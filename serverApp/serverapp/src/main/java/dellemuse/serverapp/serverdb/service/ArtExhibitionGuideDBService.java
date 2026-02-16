package dellemuse.serverapp.serverdb.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
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
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionGuideRecord;
import dellemuse.serverapp.serverdb.model.record.GuideContentRecord;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.serverdb.service.record.ArtExhibitionGuideRecordDBService;
import dellemuse.serverapp.serverdb.service.record.ArtExhibitionRecordDBService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class ArtExhibitionGuideDBService extends  MultiLanguageObjectDBservice<ArtExhibitionGuide, Long> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(ArtExhibitionGuideDBService.class.getName());

	@JsonIgnore
	@PersistenceContext
	private EntityManager entityManager;
	
	@JsonIgnore
	@Autowired
    final ArtExhibitionGuideRecordDBService artExhibitionGuideRecordDBService;
    
	public ArtExhibitionGuideDBService(CrudRepository<ArtExhibitionGuide, Long> repository, ServerDBSettings settings, ArtExhibitionGuideRecordDBService artExhibitionGuideRecordDBService) {
		super(repository, settings);
		this.artExhibitionGuideRecordDBService=artExhibitionGuideRecordDBService;
	}
	 
	@Transactional
	public ArtExhibitionGuide create(String name, ArtExhibition ex, User createdBy) {

		ArtExhibitionGuide c = new ArtExhibitionGuide();
		c.setName(name);
	 
		c.setOfficial(true);
		c.setAccessible(false);
		c.setArtExhibition(ex);
		
		if (!ex.isDependencies())
				ex=getArtExhibitionDBService().findWithDeps( ex.getId()).get();
		c.setAudioId(newAudioId(ex.getSite()));
		
		c.setState(ex.getState());
		c.setMasterLanguage(ex.getMasterLanguage());
		c.setLanguage(ex.getLanguage());
		
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);

		getRepository().save(c);

		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy,  AuditAction.CREATE));

		for ( Language la:getLanguageService().getLanguages() )
			getArtExhibitionGuideRecordDBService().create(c, la.getLanguageCode(),  createdBy);


		return c;
	}
	
	@Transactional
	public void save(ArtExhibitionGuide o, User user, List<String> updatedParts) {
		super.save(o);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(o, user, AuditAction.UPDATE, String.join(", ", updatedParts)));
	
		Optional<AudioStudio> oa = getAudioStudioDBService().findByArtExhibitionGuide(o);
		
		if (oa.isPresent()) {
			oa.get().setName(o.getName());
			oa.get().setInfo(o.getInfo());
			getAudioStudioDBService().save(oa.get());
		}
	}
	

	/**
	 * 
	 * ArtExhibitionGuide (1)
	 * AudioStudio (1)
	 * ArtExhibitionGuideRecord (n)
	 * GuideContent (n)
	 * 
	 */
	@Transactional
	public void markAsDeleted(ArtExhibitionGuide c, User deletedBy) {

		if (!c.isDependencies())
			c=this.findWithDeps(c.getId()).get();

		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(deletedBy);
		c.setState(ObjectState.DELETED);
		
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, deletedBy, AuditAction.DELETE, AuditKey.MARK_AS_DELETED));
		getRepository().save(c);		
		
		Optional<AudioStudio> o = getAudioStudioDBService().findByArtExhibitionGuide(c);
		if (o.isPresent()) 
			getAudioStudioDBService().markAsDeleted(o.get(), deletedBy);
		
		for (ArtExhibitionGuideRecord g: getArtExhibitionGuideRecordDBService(). findAllByArtExhibitionGuide(c)) {
			getArtExhibitionGuideRecordDBService().markAsDeleted(g, deletedBy);		
		}
	
		
		/** GuideContent (n) */
		c.getGuideContents().forEach( gc -> {
			getGuideContentDBService().markAsDeleted(gc, deletedBy);
		});
		
	}

	@Transactional
	public void restore(ArtExhibitionGuide c, User restoredBy) {

		OffsetDateTime date = OffsetDateTime.now();
		c.setLastModified(date);
		c.setLastModifiedUser(restoredBy);
		c.setState(ObjectState.EDITION);
		
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, restoredBy,  AuditAction.UPDATE, AuditKey.RESTORE));	
		getRepository().save(c);		
		
		
		Optional<AudioStudio> o = getAudioStudioDBService().findByArtExhibitionGuide(c);

		if (o.isPresent()) 
			getAudioStudioDBService().restore(o.get(), restoredBy);

		for (ArtExhibitionGuideRecord g: getArtExhibitionGuideRecordDBService(). findAllByArtExhibitionGuide(c)) {
			getArtExhibitionGuideRecordDBService().restore(g, restoredBy);		
		}
	
		c.getGuideContents().forEach( gc -> {
			if ( !gc.getLastModified().isBefore(date) && gc.getState()==ObjectState.DELETED)
					getGuideContentDBService().restore(gc, restoredBy);
		});
	}
	
	
	@Transactional
    public void removeItem(ArtExhibitionGuide c, GuideContent  item, User removedBy) {
    	
    	boolean contains = false;
    	int index = -1;
    	
    	List<GuideContent> list = getGuideContents(c);
    		
    	for (GuideContent i: list) {
    		index++;
    		if (item.getId().equals(i.getId())) {
    			contains=true;
    			break;
    		}
    	}
    	
    	if (!contains)
    		return;
    	
    	list.remove(index);
    	c.setContents(list);
    	c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(removedBy);
        
        getRepository().save(c);
    	getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, removedBy,  AuditAction.UPDATE, AuditKey.RESTORE));	
		
	
	}
	 @Transactional
	 public Long newAudioId(Site site) {
		String seqName = site.getAudioIdSequencerName();
	    return ((Number) getEntityManager().createNativeQuery("SELECT nextval('"+ seqName +"')").getSingleResult()).longValue();
	 }


	@Transactional
	public Optional<ArtExhibitionGuide> findWithDeps(Long id) {

		Optional<ArtExhibitionGuide> o = super.findById(id);

		if (o.isEmpty())
			return o;

		ArtExhibitionGuide a = o.get();

		if (a.getArtExhibition() != null)
			a.getArtExhibition().getDisplayname();

		if (a.getPublisher() != null)
			a.setPublisher(getPersonDBService().findById( a.getPublisher().getId() ).get());

		if (a.getGuideContents() != null)
			a.getGuideContents().size();

		Resource photo = a.getPhoto();
		if (photo!=null)
			a.setPhoto( getResourceDBService().findById(photo.getId()).get());

		Resource audio = a.getAudio();
		if (audio!=null)
			a.setAudio( getResourceDBService().findById(audio.getId()).get());

		User user = a.getLastModifiedUser();
		
		if (user!=null)
			a.setLastModifiedUser(getUserDBService().findById(user.getId()).get());
		
		
		List<GuideContent> li = new ArrayList<GuideContent>();
		a.getGuideContents().forEach( c -> {
			li.add(getGuideContentDBService().findById(c.getId()).get());
		});
		
		a.setDependencies(true);
		return o;
	}

	@Override
	public String getObjectClassName() {
		 return ArtExhibitionGuide.class.getSimpleName().toLowerCase();
	} 
	
	@Transactional
	public List<ArtExhibitionGuide> getByName(String name) {
		return createNameQuery(name).getResultList();
	}

	
	@Transactional
	public List<GuideContent> getGuideContents(ArtExhibitionGuide exhibitionGuide) {
		return getArtExhibitionGuideContents(exhibitionGuide.getId());
	}

	@Transactional
	public List<GuideContent> getArtExhibitionGuideContents(Long guideId) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<GuideContent> cq = cb.createQuery(GuideContent.class);
		Root<GuideContent> root = cq.from(GuideContent.class);
		cq.select(root).where(cb.equal(root.get("artExhibitionGuide").get("id"), guideId));
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));
		return getEntityManager().createQuery(cq).getResultList();
	}

	
	@Transactional
	public List<GuideContent> getArtExhibitionGuideContents(ArtExhibitionGuide guide, ObjectState o1) {
		return  getArtExhibitionGuideContents(guide.getId(), o1);
	}
	

	@Transactional
	public List<GuideContent> getArtExhibitionGuideContents(ArtExhibitionGuide guide, ObjectState o1, ObjectState o2) {
		return  getArtExhibitionGuideContents(guide.getId(), o1, o2);
	}
	
	@Transactional
	public List<ArtExhibitionGuide> getArtExhibitionGuidesBySite(Site s, ObjectState o1, ObjectState o2) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArtExhibitionGuide> cq = cb.createQuery(ArtExhibitionGuide.class);
		Root<ArtExhibitionGuide> root = cq.from(ArtExhibitionGuide.class);
		
		// cq.select(root).where(cb.equal(root.get("artExhibitionGuide").get("id"), guideId));
	
		Predicate p_site = cb.equal(root.get("artExhibitionGuide").get("artExhibition").get("site").get("id"), String.valueOf(s.getId()));

		Predicate p_o1 = cb.equal(root.get("state"), o1);
		Predicate p_o2 = cb.equal(root.get("state"), o2);

		Predicate statePredicate = cb.or(p_o1, p_o2);
		Predicate combinedPredicate = cb.and(p_site, statePredicate);

		cq.select(root).where(combinedPredicate);
		
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));
		return getEntityManager().createQuery(cq).getResultList();
	}

	
	
	@Transactional
	public List<GuideContent> getArtExhibitionGuideContents(Long guideId, ObjectState o1) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<GuideContent> cq = cb.createQuery(GuideContent.class);
		Root<GuideContent> root = cq.from(GuideContent.class);
		
		//cq.select(root).where(cb.equal(root.get("artExhibitionGuide").get("id"), guideId));
		

		Predicate p0 = cb.equal(root.get("artExhibitionGuide").get("id"), String.valueOf(guideId));
		Predicate p1 = cb.equal(root.get("state"), o1);

		Predicate combinedPredicate = cb.and(p0, p1);

		cq.select(root).where(combinedPredicate);
		
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public List<GuideContent> getArtExhibitionGuideContents(Long guideId, ObjectState o1, ObjectState o2) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<GuideContent> cq = cb.createQuery(GuideContent.class);
		
		Root<GuideContent> root = cq.from(GuideContent.class);
		
		cq.select(root).where(cb.equal(root.get("artExhibitionGuide").get("id"), guideId));
		
	//	Predicate p0 = cb.equal(root.get("site").get("id"), String.valueOf(siteId));


		Predicate p0 = cb.equal(root.get("artExhibitionGuide").get("id"), String.valueOf(guideId));
	
		Predicate p1 = cb.equal(root.get("state"), o1);
		Predicate p2 = cb.equal(root.get("state"), o2);

		
		Predicate combinedPredicate = cb.or(p1, p2);
		Predicate finalredicate = cb.and(p0, combinedPredicate);

		cq.select(root).where(finalredicate);
		
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));
		return getEntityManager().createQuery(cq).getResultList();
	}
	
	
	@Transactional
	public List<GuideContent> getArtExhibitionGuidePublishedBy(Person person) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<GuideContent> cq = cb.createQuery(GuideContent.class);
		Root<GuideContent> root = cq.from(GuideContent.class);
		cq.select(root).where(cb.equal(root.get("publisher").get("id"), person.getId()));
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));

		return getEntityManager().createQuery(cq).getResultList();
	}

	@Override
	protected Class<ArtExhibitionGuide> getEntityClass() {
		return ArtExhibitionGuide.class;
	}

	@PostConstruct
	protected void onInitialize() {
		super.registerRecordDB(getEntityClass(), getArtExhibitionGuideRecordDBService());
		super.register(getEntityClass(), this);
	}
	
	
	protected ArtExhibitionGuideRecordDBService getArtExhibitionGuideRecordDBService() {
		return this.artExhibitionGuideRecordDBService;
	}

}

