package dellemuse.serverapp.serverdb.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.ArtWorkType;
import dellemuse.serverapp.serverdb.model.AuditAction;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.Floor;
import dellemuse.serverapp.serverdb.model.FloorType;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;

@Service
public class FloorTypeDBService extends DBService<FloorType, Long> {

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(FloorTypeDBService.class.getName());

    public FloorTypeDBService(CrudRepository<FloorType, Long> repository,  ServerDBSettings settings) {
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
     
    public FloorType create(String name,User createdBy) {
        FloorType c = new FloorType();
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
    public List<FloorType> getByName(String name) {
        return createNameQuery(name).getResultList();
    }

    @Override
    protected Class<FloorType> getEntityClass() {
        return FloorType.class;
    }

	@Override
	public String getObjectClassName() {
		 return FloorType.class.getSimpleName().toLowerCase();
	} 

}
