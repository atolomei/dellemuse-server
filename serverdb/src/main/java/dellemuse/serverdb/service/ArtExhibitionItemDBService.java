package dellemuse.serverdb.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.serverdb.ServerDBSettings;
import dellemuse.serverdb.model.ArtExhibitionItem;
import dellemuse.serverdb.model.User;
import dellemuse.serverdb.service.base.ServiceLocator;
import dellemuse.model.logging.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;

@Service
public class ArtExhibitionItemDBService extends DBService<ArtExhibitionItem, Long> {

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(ArtExhibitionItemDBService.class.getName());

    
    public ArtExhibitionItemDBService(CrudRepository<ArtExhibitionItem, Long> repository, ServerDBSettings settings) {
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
    public ArtExhibitionItem create(String name, User createdBy) {
        ArtExhibitionItem c = new ArtExhibitionItem();
        c.setName(name);
        c.setNameKey(nameKey(name));
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        return getRepository().save(c);
    }

    @PostConstruct
    protected void onInitialize() {
    	ServiceLocator.getInstance().register(getEntityClass(), this);
    }
    
    /**
     * @param name
     * @return
     */
    public List<ArtExhibitionItem> getByName(String name) {
        return createNameQuery(name).getResultList();
    }

    @Override
    protected Class<ArtExhibitionItem> getEntityClass() {
        return ArtExhibitionItem.class;
    }
}
