package dellemuse.server.db.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.server.db.model.ArtExhibition;
import dellemuse.server.db.model.ArtExhibitionItem;
import dellemuse.server.db.model.User;
import dellemuse.util.Logger;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;

@Service
public class ArtExhibitionItemDBService extends DBService<ArtExhibitionItem, Long> {

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(ArtExhibitionItemDBService.class.getName());

    public ArtExhibitionItemDBService(CrudRepository<ArtExhibitionItem, Long> repository, EntityManagerFactory entityManagerFactory) {
        super(repository, entityManagerFactory);
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
    public ArtExhibitionItem create(String name,User createdBy) {
        ArtExhibitionItem c = new ArtExhibitionItem();
        c.setName(name);
        c.setNameKey(normalize(name));
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifidUser(createdBy);
        return getRepository().save(c);
    }

    /**
     * @param name
     * @return
     */
    public List<ArtExhibitionItem> getByName(String name) {
        return createNameQuery().getResultList();
    }

    @Override
    protected Class<ArtExhibitionItem> getEntityClass() {
        return ArtExhibitionItem.class;
    }
}
