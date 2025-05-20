package dellemuse.server.db.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.server.Settings;
import dellemuse.server.db.model.ArtWork;
import dellemuse.server.db.model.ArtWorkArtist;
import dellemuse.server.db.model.Person;
import dellemuse.server.db.model.User;
import dellemuse.model.logging.Logger;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;

//@Service
public class ArtWorkArtistDBService extends DBService<ArtWorkArtist, Long> {

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(ArtWorkArtistDBService.class.getName());

    public ArtWorkArtistDBService(CrudRepository<ArtWorkArtist, Long> repository, EntityManagerFactory entityManagerFactory, Settings settings) {
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
    public ArtWorkArtist create(String name,User createdBy) {
        ArtWorkArtist c = new ArtWorkArtist();
        c.setName(name);
        c.setNameKey(nameKey(name));
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        return getRepository().save(c);
    }


    @Transactional
    public ArtWorkArtist create(ArtWork aw, Person person, User createdBy) {
        ArtWorkArtist c = new ArtWorkArtist();
        c.setArtwork(aw);
        c.setPerson(person);
        c.setName(aw.getName() + "-" + person.getDisplayname());
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        return getRepository().save(c);
    }

    
    /**
     * @param name
     * @return
     */
    public List<ArtWorkArtist> getByName(String name) {
        return createNameQuery(name).getResultList();
    }

    @Override
    protected Class<ArtWorkArtist> getEntityClass() {
        return ArtWorkArtist.class;
    }


    
}
