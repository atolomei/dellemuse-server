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
import dellemuse.serverapp.serverdb.model.AuditAction;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionItemRecord;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionRecord;
import dellemuse.serverapp.serverdb.service.record.ArtExhibitionItemRecordDBService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class ArtExhibitionItemDBService extends  MultiLanguageObjectDBservice<ArtExhibitionItem, Long> {

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(ArtExhibitionItemDBService.class.getName());
    
    
	@JsonIgnore
	@Autowired
    final ArtExhibitionItemRecordDBService artExhibitionItemRecordDBService;

    
    public ArtExhibitionItemDBService(CrudRepository<ArtExhibitionItem, Long> repository, ServerDBSettings settings,  ArtExhibitionItemRecordDBService artExhibitionItemRecordDBService) {
        super(repository, settings);
        this.artExhibitionItemRecordDBService=artExhibitionItemRecordDBService;
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
    public ArtExhibitionItem create(String name, User createdBy) {
        ArtExhibitionItem c = new ArtExhibitionItem();
        c.setName(name);
        
		c.setMasterLanguage(getDefaultMasterLanguage());
		c.setLanguage(getDefaultMasterLanguage());
		
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        c.setState(ObjectState.EDITION);

    	getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy,  AuditAction.CREATE));
    	
		getRepository().save(c);

		for (Language la:getLanguageService().getLanguages())
			getArtExhibitionItemRecordDBService().create(c, la.getLanguageCode(),  createdBy);


        return c;
    }

    @Transactional
    public ArtExhibitionItem create(String name, ArtExhibition ex, ArtWork artWork, User createdBy) {

    	ArtExhibitionItem c = new ArtExhibitionItem();
        
    	if (!ex.isDependencies())  
    		ex=getArtExhibitionDBService().findById(ex.getId()).get();
    
    	int size = 	ex.getArtExhibitionItems().size();

    	c.setName(name);
		c.setMasterLanguage(ex.getMasterLanguage());
		c.setLanguage(ex.getLanguage());
		
        c.setArtWork(artWork);
        c.setArtExhibition(ex);
        c.setArtExhibitionOrder(size);
        
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        c.setState(ObjectState.EDITION);
        
		getRepository().save(c);
    	getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy,  AuditAction.CREATE));
    	
		for (Language la:getLanguageService().getLanguages())
			getArtExhibitionItemRecordDBService().create(c, la.getLanguageCode(),  createdBy);
		
        return c;
    }

    @Transactional	
	public void save(ArtExhibitionItem o, User user, List<String> updatedParts) {
		super.save(o);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(o, user, AuditAction.UPDATE, String.join(", ", updatedParts)));
	}
    /**
     * 
     * 
     */
	@Transactional
	public void markAsDeleted(ArtExhibitionItem c, User deletedBy) {

		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(deletedBy);
		c.setState(ObjectState.DELETED);
		
		getRepository().save(c);		
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, deletedBy, AuditAction.DELETE, AuditKey.MARK_AS_DELETED));
		
		for (ArtExhibitionItemRecord g : getArtExhibitionItemRecordDBService().findAllByArtExhibitionItem(c)) {
			getArtExhibitionItemRecordDBService().markAsDeleted(g, deletedBy);
		}
	}

	
	@Transactional
	public void restore(ArtExhibitionItem c, User restoredBy) {

		OffsetDateTime date = OffsetDateTime.now();
		c.setLastModified(date);
		c.setLastModifiedUser(restoredBy);
		c.setState(ObjectState.EDITION);
		getRepository().save(c);		
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, restoredBy,  AuditAction.UPDATE, AuditKey.RESTORE));
			
		
		for (ArtExhibitionItemRecord g : getArtExhibitionItemRecordDBService().findAllByArtExhibitionItem(c)) {
			getArtExhibitionItemRecordDBService().restore(g,restoredBy);
		}
	}

	
	@Override
	public String getObjectClassName() {
		 return ArtExhibitionItem.class.getSimpleName().toLowerCase();
	} 

	
	@Transactional
	public Optional<ArtExhibitionItem> findWithDeps(Long id) {

		Optional<ArtExhibitionItem> o = super.findById(id);

		if (o.isEmpty())
			return o;

		ArtExhibitionItem a = o.get();

		a.setDependencies(true);

		if (a.getArtExhibition()!=null)
			a.getArtExhibition().getDisplayname();
		
		if (a.getArtWork()!=null)
			a.getArtWork().getDisplayname();

		if (a.getFloor()!=null)
			a.getFloor().getDisplayname();
		
		if (a.getRoom()!=null)
			a.getRoom().getDisplayname();
		
		if (a.getLastModifiedUser()!=null)
			a.getLastModifiedUser().getDisplayname();
		
		return o;
	}

	@Transactional
	public Iterable<GuideContent> getGuideContents(ArtExhibitionItem o) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<GuideContent> cq = cb.createQuery(GuideContent.class);
		Root<GuideContent> root = cq.from(GuideContent.class);
		cq.select(root).where(cb.equal(root.get("artExhibitionItem").get("id"), o.getId()));
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));
		return getEntityManager().createQuery(cq).getResultList();
	}
	
    
    /**
     * @param name
     * @return
     */
	@Transactional
    public List<ArtExhibitionItem> getByName(String name) {
        return createNameQuery(name).getResultList();
    }

    @Override
    protected Class<ArtExhibitionItem> getEntityClass() {
        return ArtExhibitionItem.class;
    }
    
	protected ArtExhibitionItemRecordDBService getArtExhibitionItemRecordDBService() {
		return this.artExhibitionItemRecordDBService;
	}

    @PostConstruct
    protected void onInitialize() {
    	super.registerRecordDB(getEntityClass(), getArtExhibitionItemRecordDBService());
		super.register(getEntityClass(), this);
    }


}
