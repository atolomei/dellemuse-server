package dellemuse.server.db.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.server.Settings;

import dellemuse.server.db.model.ArtExhibitionStatusType;
import dellemuse.server.db.model.User;
import dellemuse.model.logging.Logger;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;

@Service
public class ArtExhibitionStatusTypeDBService extends DBService<ArtExhibitionStatusType, Long> {

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(ArtExhibitionStatusTypeDBService.class.getName());

    public ArtExhibitionStatusTypeDBService(CrudRepository<ArtExhibitionStatusType, Long> repository,
             Settings settings) {
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
    public ArtExhibitionStatusType create(String name, User createdBy) {
        ArtExhibitionStatusType c = new ArtExhibitionStatusType();
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
    public List<ArtExhibitionStatusType> getByName(String name) {
        return createNameQuery(name).getResultList();
    }

    @Override
    protected Class<ArtExhibitionStatusType> getEntityClass() {
        return ArtExhibitionStatusType.class;
    }
}
