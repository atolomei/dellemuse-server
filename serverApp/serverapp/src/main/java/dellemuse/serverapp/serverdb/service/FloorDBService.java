package dellemuse.serverapp.serverdb.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.AudioStudio;
import dellemuse.serverapp.serverdb.model.AuditAction;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.Floor;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;

@Service
public class FloorDBService extends DBService<Floor, Long> {

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(FloorDBService.class.getName());

    public FloorDBService(CrudRepository<Floor, Long> repository,  ServerDBSettings settings) {
        super(repository,   settings);
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
 
    public Floor create(String name,User createdBy) {
        Floor c = new Floor();
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
   
    /**
     * @param name
     * @return
     */
    public List<Floor> getByName(String name) {
        return createNameQuery(name).getResultList();
    }

    @Override
    protected Class<Floor> getEntityClass() {
        return Floor.class;
    }
    
	@Override
	public String getObjectClassName() {
		 return Floor.class.getSimpleName().toLowerCase();
	} 

	
}
