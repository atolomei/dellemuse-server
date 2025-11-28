package dellemuse.serverapp.serverdb.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.model.ArtWorkTypeModel;
import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.ArtWorkType;
import dellemuse.serverapp.serverdb.model.AuditAction;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;

@Service
public class ArtWorkTypeDBService extends DBService<ArtWorkType, Long> {

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(ArtWorkTypeDBService.class.getName());

    
    public ArtWorkTypeDBService(CrudRepository<ArtWorkType, Long> repository,  ServerDBSettings settings) {
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
    public ArtWorkType create(String name, User createdBy) {
        ArtWorkType c = new ArtWorkType();
        c.setName(name);
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
       
        getRepository().save(c);
        getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy,  AuditAction.CREATE));
        
	    return c;
    }

    @PostConstruct
    protected void onInitialize() {
    	super.register(getEntityClass(), this);
    }
    
    @Transactional
    public ArtWorkType create(ArtWorkTypeModel model, User createdBy) {
        ArtWorkType c = new ArtWorkType();
        c.setName(model.getName());
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
    public List<ArtWorkType> findByName(String name) {
        return createNameQuery(name).getResultList();
    }

    @Override
    protected Class<ArtWorkType> getEntityClass() {
        return ArtWorkType.class;
    }
    
	@Override
	public String getObjectClassName() {
		 return ArtWorkType.class.getSimpleName().toLowerCase();
	} 

   
}
