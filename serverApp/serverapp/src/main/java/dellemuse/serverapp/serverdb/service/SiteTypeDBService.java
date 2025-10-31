package dellemuse.serverapp.serverdb.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Room;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.SiteType;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;

@Service
public class SiteTypeDBService extends DBService<SiteType, Long> {

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(SiteTypeDBService.class.getName());

    public SiteTypeDBService(CrudRepository<SiteType, Long> repository,   ServerDBSettings settings) {
        super(repository,   settings);
    }

    @PostConstruct
    protected void onInitialize() {
    	super.register(getEntityClass(), this);
    	//ServiceLocator.getInstance().register(getEntityClass(), this);
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
    @Override
    public SiteType create(String name,User createdBy) {
        SiteType c = new SiteType();
        c.setName(name);
        //c.setNameKey(nameKey(name));
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        return getRepository().save(c);
    }

    /**
     * @param name
     * @return
     */
    public List<SiteType> getByName(String name) {
        return createNameQuery(name).getResultList();
    }

    @Override
    protected Class<SiteType> getEntityClass() {
        return SiteType.class;
    }
}
