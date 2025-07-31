package dellemuse.server.db.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.server.Settings;
import dellemuse.server.db.model.ArtWorkType;
import dellemuse.server.db.model.Floor;
import dellemuse.server.db.model.FloorType;
import dellemuse.server.db.model.User;
import dellemuse.model.logging.Logger;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;

@Service
public class FloorTypeDBService extends DBService<FloorType, Long> {

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(FloorTypeDBService.class.getName());

    public FloorTypeDBService(CrudRepository<FloorType, Long> repository,  Settings settings) {
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
    @Override
    public FloorType create(String name,User createdBy) {
        FloorType c = new FloorType();
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
    public List<FloorType> getByName(String name) {
        return createNameQuery(name).getResultList();
    }

    @Override
    protected Class<FloorType> getEntityClass() {
        return FloorType.class;
    }
}
