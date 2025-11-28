package dellemuse.serverapp.serverdb.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.ArtExhibitionSection;
import dellemuse.serverapp.serverdb.model.ArtExhibitionStatusType;
import dellemuse.serverapp.serverdb.model.AuditAction;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;

@Service
public class ArtExhibitionStatusTypeDBService extends DBService<ArtExhibitionStatusType, Long> {

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(ArtExhibitionStatusTypeDBService.class.getName());

    public ArtExhibitionStatusTypeDBService(CrudRepository<ArtExhibitionStatusType, Long> repository,
             ServerDBSettings settings) {
        super(repository, settings);
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
     
    public ArtExhibitionStatusType create(String name, User createdBy) {
        ArtExhibitionStatusType c = new ArtExhibitionStatusType();
        c.setName(name);
        
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
 
        getRepository().save(c);
        
    	getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy,  AuditAction.CREATE));
    	return c;
    	
    }

  
    
    /**
     * @param name
     * @return
     */
    public List<ArtExhibitionStatusType> getByName(String name) {
        return createNameQuery(name).getResultList();
    }

    @Override
    protected Class<ArtExhibitionStatusType> getEntityClass() {
        return ArtExhibitionStatusType.class;
    }
    
	@Override
	public String getObjectClassName() {
		 return ArtExhibitionStatusType.class.getSimpleName().toLowerCase();
	} 

	  @PostConstruct
	    protected void onInitialize() {
	    	super.register(getEntityClass(), this);
	    }
}
