package dellemuse.server.db.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.server.Settings;
import dellemuse.server.db.model.ArtExhibition;
import dellemuse.server.db.model.ArtExhibitionGuide;
import dellemuse.server.db.model.User;
import dellemuse.model.logging.Logger;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;

@Service
public class ArtExhibitionGuideDBService extends DBService<ArtExhibitionGuide, Long> {

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(ArtExhibitionGuideDBService.class.getName());

    public ArtExhibitionGuideDBService(CrudRepository<ArtExhibitionGuide, Long> repository, EntityManagerFactory entityManagerFactory, Settings settings) {
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
    public ArtExhibitionGuide create(String name,User createdBy) {
        ArtExhibitionGuide c = new ArtExhibitionGuide();
        c.setName(name);
        c.setNameKey(normalize(name));
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        return getRepository().save(c);
    }

    /**
     * @param name
     * @return
     */
    public List<ArtExhibitionGuide> getByName(String name) {
        return createNameQuery().getResultList();
    }

    @Override
    protected Class<ArtExhibitionGuide> getEntityClass() {
        return ArtExhibitionGuide.class;
    }
}
