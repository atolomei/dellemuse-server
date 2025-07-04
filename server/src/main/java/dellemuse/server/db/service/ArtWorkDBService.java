package dellemuse.server.db.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.server.Settings;
import dellemuse.server.db.model.ArtWork;
import dellemuse.server.db.model.User;
import dellemuse.model.logging.Logger;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;

@Service
public class ArtWorkDBService extends DBService<ArtWork, Long> {

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(ArtWorkDBService.class.getName());

    public ArtWorkDBService(CrudRepository<ArtWork, Long> repository,  Settings settings) {
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
    @Override
    public ArtWork create(String name,User createdBy) {
        ArtWork c = new ArtWork();
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
    public List<ArtWork> getByName(String name) {
        return createNameQuery(name).getResultList();
    }

    @Override
    protected Class<ArtWork> getEntityClass() {
        return ArtWork.class;
    }


    
}
