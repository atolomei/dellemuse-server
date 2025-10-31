package dellemuse.serverapp.serverdb.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Room;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
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
    	super.register(getEntityClass(), this);
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
        //c.setNameKey(nameKey(name));
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        return getRepository().save(c);
    }

    @Transactional
	public Optional<Room> findWithDeps(Long id) {

		Optional<Room> o_aw = super.findById(id);

		if (o_aw.isEmpty())
			return  o_aw;
		
		Room aw = o_aw.get();
		

		User u = aw.getLastModifiedUser();
		
		if (u!=null)
			u.getDisplayname();

		aw.setDependencies(true);

		return o_aw;
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
