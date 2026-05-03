package dellemuse.serverapp.serverdb.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.AuditAction;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.Floor;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Room;
import dellemuse.serverapp.serverdb.model.User;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class RoomDBService extends DBService<Room, Long> {

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(RoomDBService.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    public RoomDBService(CrudRepository<Room, Long> repository, ServerDBSettings settings) {
        super(repository, settings);
    }

    @PostConstruct
    protected void onInitialize() {
        super.register(getEntityClass(), this);
    }

    @Transactional
    public Room create(String name, User createdBy) {
        Room c = new Room();
        c.setName(name);
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        c.setState(ObjectState.EDITION);
    	c.setMasterLanguage(getDefaultMasterLanguage());
		c.setLanguage(getDefaultMasterLanguage());
		
        getRepository().save(c);
        getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy, AuditAction.CREATE));
        
        
        //for (Language la : getLanguageService().getLanguages())
		//	getFloorRecordDBService().create(c, la.getLanguageCode(), createdBy);
        for (Language la : getLanguageService().getLanguages())
			getRoomRecordDBService().create(c, la.getLanguageCode(), createdBy);
        
        return c;
    }

    @Transactional
    public Room create(String name, Floor floor, User createdBy) {
     
    	Room c = new Room();
        
    	c.setName(name);
        c.setFloor(floor);
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        c.setState(ObjectState.EDITION);
        getRepository().save(c);
        getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy, AuditAction.CREATE));
        
        c.setMasterLanguage(floor.getMasterLanguage());
		c.setLanguage(floor.getLanguage());
		
		//for (Language la : getLanguageService().getLanguages())
		//	getFloorRecordDBService().create(c, la.getLanguageCode(), createdBy);
		for (Language la : getLanguageService().getLanguages())
			getRoomRecordDBService().create(c, la.getLanguageCode(), createdBy);

        return c;
    }

    @Transactional
    public Room save(Room c, User user, List<String> updatedParts) {
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(user);
        getRepository().save(c);
        getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, user, AuditAction.UPDATE, String.join(", ", updatedParts)));
        return c;
    }

    @Transactional
    public void markAsDeleted(Room c, User deletedBy) {
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(deletedBy);
        c.setState(ObjectState.DELETED);
        getRepository().save(c);
        getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, deletedBy, AuditAction.DELETE));
    }

    @Transactional
    public void restore(Room c, User restoredBy) {
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(restoredBy);
        c.setState(ObjectState.EDITION);
        getRepository().save(c);
        getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, restoredBy, AuditAction.UPDATE));
    }

    @Transactional
    public Optional<Room> findWithDeps(Long id) {
        Optional<Room> o = super.findById(id);
        if (o.isEmpty())
            return o;
        Room r = o.get();
        Long userId = r.getLastModifiedUser() != null ? r.getLastModifiedUser().getId() : null;
        Long floorId = r.getFloor() != null ? r.getFloor().getId() : null;
        getEntityManager().detach(r);
        if (floorId != null) {
            FloorDBService se = (FloorDBService) dellemuse.serverapp.serverdb.service.base.ServiceLocator.getInstance().getBean(FloorDBService.class);
            r.setFloor(se.findById(floorId).get());
        }
        if (userId != null)
            r.setLastModifiedUser(getUserDBService().findById(userId).get());
        r.setDependencies(true);
        return o;
    }

    public List<Room> getRooms(Floor floor) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Room> cq = cb.createQuery(Room.class);
        Root<Room> root = cq.from(Room.class);
        cq.select(root).where(cb.equal(root.get("floor").get("id"), floor.getId()));
        cq.orderBy(cb.asc(cb.lower(root.get("name"))));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<Room> getRooms(Long floorId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Room> cq = cb.createQuery(Room.class);
        Root<Room> root = cq.from(Room.class);
        cq.select(root).where(cb.equal(root.get("floor").get("id"), floorId));
        cq.orderBy(cb.asc(cb.lower(root.get("name"))));
        return getEntityManager().createQuery(cq).getResultList();
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

    @Override
    public String getObjectClassName() {
        return Room.class.getSimpleName().toLowerCase();
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
}