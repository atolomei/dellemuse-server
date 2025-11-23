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
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.AudioStudio;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
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
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class ArtExhibitionGuideDBService extends DBService<ArtExhibitionGuide, Long> {

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
	@Override
	public ArtExhibitionGuide create(String name, User createdBy) {

		ArtExhibitionGuide c = new ArtExhibitionGuide();
		c.setName(name);
		c.setOfficial(true);

		c.setState(ObjectState.EDTION);
		c.setLanguage(getDefaultMasterLanguage());
        c.setMasterLanguage(getDefaultMasterLanguage());

		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
        
		getRepository().save(c);

		for ( Language la:getLanguageService().getLanguages() )
			getArtExhibitionGuideRecordDBService().create(c, la.getLanguageCode(),  createdBy);
    	
        return getRepository().save(c);
	}


	@Transactional
	public ArtExhibitionGuide create(String name, ArtExhibition ex, User createdBy) {
		ArtExhibitionGuide c = new ArtExhibitionGuide();
		c.setName(name);
	 

		c.setOfficial(true);
		c.setArtExhibition(ex);

		c.setState(ex.getState());
		c.setMasterLanguage(ex.getMasterLanguage());
		c.setLanguage(ex.getLanguage());
		
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
		
		getRepository().save(c);

		for ( Language la:getLanguageService().getLanguages() )
			getArtExhibitionGuideRecordDBService().create(c, la.getLanguageCode(),  createdBy);


		return getRepository().save(c);
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
		
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(deletedBy);
		c.setState(ObjectState.DELETED);
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
		c.setState(ObjectState.EDTION);
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
			a.getPublisher().getDisplayname();

		if (a.getGuideContents() != null)
			a.getGuideContents().size();

		if (a.getPhoto() != null)
			a.getPhoto().getBucketName();

		if (a.getAudio() != null)
			a.getAudio().getBucketName();

		a.setDependencies(true);

		return o;
	}

	@PostConstruct
	protected void onInitialize() {
		super.register(getEntityClass(), this);
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

	protected ArtExhibitionGuideRecordDBService getArtExhibitionGuideRecordDBService() {
		return this.artExhibitionGuideRecordDBService;
	}

}

