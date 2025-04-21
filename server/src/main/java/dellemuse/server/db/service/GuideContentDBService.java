package dellemuse.server.db.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.server.Settings;
import dellemuse.server.db.model.GuideContent;
import dellemuse.server.db.model.User;
import dellemuse.model.logging.Logger;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;

@Service
public class GuideContentDBService extends DBService<GuideContent, Long> {

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(GuideContentDBService.class.getName());

    public GuideContentDBService(CrudRepository<GuideContent, Long> repository, EntityManagerFactory entityManagerFactory, Settings settings) {
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
    public GuideContent create(String name,User createdBy) {
        GuideContent c = new GuideContent();
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
    public List<GuideContent> getByName(String name) {
        return createNameQuery().getResultList();
    }

    @Override
    protected Class<GuideContent> getEntityClass() {
        return GuideContent.class;
    }
}
