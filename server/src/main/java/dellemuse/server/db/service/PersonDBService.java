package dellemuse.server.db.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
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
import jakarta.persistence.FlushModeType;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;


@Service
public class PersonDBService extends DBService<Person, Long> {

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(PersonDBService.class.getName());

    public PersonDBService(CrudRepository<Person, Long> repository, EntityManagerFactory entityManagerFactory, Settings settings) {
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
    public Person create(String name,User createdBy) {
        Person c = new Person();
        c.setName(name);
        c.setNameKey(normalize(name));
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        return getRepository().save(c);
    }

    
    @Transactional
    public List<ArtWork> getArtWorks(Person person) {
        List<ArtWorkArtist> artWorkArtists =
                getSessionFactory().getCurrentSession().createSelectionQuery("from "+ArtWorkArtist.class.getSimpleName()+" where artist.id = :artistid", ArtWorkArtist.class)
                .setParameter("artistid", person.getId())
                .getResultList();
        
        List<ArtWork> list = new ArrayList<ArtWork>();
        
        for (ArtWorkArtist a:artWorkArtists) {
            list.add(a.getArtwork());
        }
        return list;
    }

    /**
     * @param name
     * @return
     */
    public List<Person> getByName(String name) {
        return createNameQuery().getResultList();
    }

    @Override
    protected Class<Person> getEntityClass() {
        return Person.class;
    }
}
