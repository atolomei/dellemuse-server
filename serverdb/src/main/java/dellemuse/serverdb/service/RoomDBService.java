package dellemuse.serverdb.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.serverdb.ServerDBSettings;
import dellemuse.serverdb.model.Person;
import dellemuse.serverdb.model.Room;
import dellemuse.serverdb.model.User;
import dellemuse.serverdb.service.base.ServiceLocator;
import dellemuse.model.logging.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;

@Service
public class RoomDBService extends DBService<Room, Long> {

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(RoomDBService.class.getName());

    public RoomDBService(CrudRepository<Room, Long> repository,  ServerDBSettings settings) {
        super(repository,  settings);
    }

    @PostConstruct
    protected void onInitialize() {
    	ServiceLocator.getInstance().register(getEntityClass(), this);
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
    public Room create(String name,User createdBy) {
        Room c = new Room();
        c.setName(name);
        c.setNameKey(nameKey(name));
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        return getRepository().save(c);
    }

    /**
     * @param name
     * @return
     */
    public List<Room> getByName(String name) {
        return createNameQuery(name).getResultList();
    }

    @Override
    protected Class<Room> getEntityClass() {
        return Room.class;
    }
}
