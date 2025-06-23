package dellemuse.server.db.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.server.Settings;
import dellemuse.server.db.model.Person;
import dellemuse.server.db.model.Room;
import dellemuse.server.db.model.Site;
import dellemuse.server.db.model.SiteType;
import dellemuse.server.db.model.User;
import dellemuse.model.logging.Logger;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;

@Service
public class SiteTypeDBService extends DBService<SiteType, Long> {

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(SiteTypeDBService.class.getName());

    public SiteTypeDBService(CrudRepository<SiteType, Long> repository,   Settings settings) {
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
    public SiteType create(String name,User createdBy) {
        SiteType c = new SiteType();
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
    public List<SiteType> getByName(String name) {
        return createNameQuery(name).getResultList();
    }

    @Override
    protected Class<SiteType> getEntityClass() {
        return SiteType.class;
    }
}
