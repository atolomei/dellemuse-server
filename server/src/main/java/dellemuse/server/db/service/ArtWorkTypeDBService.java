package dellemuse.server.db.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.server.Settings;
import dellemuse.server.db.model.ArtWorkType;
import dellemuse.server.db.model.User;
import dellemuse.model.ArtWorkTypeModel;
import dellemuse.model.logging.Logger;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;

@Service
public class ArtWorkTypeDBService extends DBService<ArtWorkType, Long> {

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(ArtWorkTypeDBService.class.getName());

    
    public ArtWorkTypeDBService(CrudRepository<ArtWorkType, Long> repository, EntityManagerFactory entityManagerFactory, Settings settings) {
        super(repository, entityManagerFactory, settings);
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
    public ArtWorkType create(String name, User createdBy) {
        ArtWorkType c = new ArtWorkType();
        c.setName(name);
        c.setNameKey(normalize(name));
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        return getRepository().save(c);
    }

    @Transactional
    public ArtWorkType create(ArtWorkTypeModel model, User createdBy) {
        ArtWorkType c = new ArtWorkType();
        c.setName(model.getName());
        c.setNameKey(normalize(model.getName()));
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        return getRepository().save(c);
    }

    /**
     * @param name
     * @return
     */
    public List<ArtWorkType> getByName(String name) {
        return createNameQuery().getResultList();
    }

    @Override
    protected Class<ArtWorkType> getEntityClass() {
        return ArtWorkType.class;
    }
    
   
}
