package dellemuse.server.db.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.server.Settings;
import dellemuse.server.db.model.ArtWork;
import dellemuse.server.db.model.ArtWorkArtist;
import dellemuse.server.db.model.Person;
import dellemuse.server.db.model.Site;
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

    @Override
    public Person create(String name, User createdBy) {
        return create(name, null, createdBy);
    }
    
    @Transactional
    public Person create(String name, String lastname, User createdBy) {
        
        Person c = new Person();

        c.setName(name);
        c.setNameKey(nameKey(name));
        c.setLastname(lastname);
        c.setLastnameKey(nameKey(lastname));
        
        c.setDisplayname(name + " " + lastname);

        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        
        return getRepository().save(c);

    }

    public String normalize(String name) {
        return this.getEntityClass().getSimpleName().toLowerCase() + "-" + name.toLowerCase().trim();
    }

    
    @Transactional
    public Person create( String name, 
                          User createdBy, 
                          Optional<String> o_lastname, 
                          Optional<String> o_sex, 
                          Optional<String> o_pid,
                          Optional<String> o_address,
                          Optional<String> o_zip, 
                          Optional<String> o_phone, 
                          Optional<String> o_email) {
        
        Person c = new Person();
        c.setName(name);
        c.setNameKey(name.toLowerCase().trim());
        
        if (o_lastname.isPresent()) {
            c.setLastname(o_lastname.get());
            c.setLastnameKey(o_lastname.get().toLowerCase().trim());
            c.setDisplayname(name + " " + o_lastname.get());
        }
        c.setDisplayname(name);
        
        
        if (o_sex.isPresent())
            c.setSex(o_sex.get());

        if (o_pid.isPresent())
            c.setPhysicalid(o_pid.get());
        
        if (o_zip.isPresent())
            c.setZipcode(o_zip.get());
        
        if (o_phone.isPresent())
            c.setPhone(o_phone.get());
        
        if (o_email.isPresent())
            c.setEmail(o_email.get());
        
        
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
        return createNameQuery(name).getResultList();
    }

    @Override
    protected Class<Person> getEntityClass() {
        return Person.class;
    }

    @Transactional
    public Optional<Person> findByName(String firstLastname) {
        
        TypedQuery<Person> query;
        CriteriaBuilder criteriabuilder = getSessionFactory().getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Person> criteria = criteriabuilder.createQuery(Person.class);
        Root<Person> loaders = criteria.from(Person.class);
        criteria.select(loaders).where(criteriabuilder.and( criteriabuilder.like(criteriabuilder.lower(loaders.get("display")), "%"+firstLastname.toLowerCase()+"%")));
        query = getSessionFactory().getCurrentSession().createQuery(criteria);
        query.setHint("org.hibernate.cacheable", true);
        query.setFlushMode(FlushModeType.COMMIT);
        
        List<Person> list = query.getResultList();
        
        if (list==null || list.isEmpty())
            return Optional.empty();

        list.sort( new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return 0;
            }
        });
        
        return Optional.of(list.get(0));
    }


    @Transactional
    public Optional<Person> findByDisplayName(String firstLastname) {
        
        TypedQuery<Person> query;
        CriteriaBuilder criteriabuilder = getSessionFactory().getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Person> criteria = criteriabuilder.createQuery(Person.class);
        Root<Person> loaders = criteria.from(Person.class);
        criteria.select(loaders).where(criteriabuilder.like(criteriabuilder.lower(loaders.get("displayname")), "%"+firstLastname.toLowerCase()+"%"));
        query = getSessionFactory().getCurrentSession().createQuery(criteria);
        query.setHint("org.hibernate.cacheable", true);
        query.setFlushMode(FlushModeType.COMMIT);
        
        List<Person> list = query.getResultList();
        
        if (list==null || list.isEmpty())
            return Optional.empty();

        list.sort( new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return 0;
            }
        });
        
        return Optional.of(list.get(0));
    }

    
    @Transactional
    public Optional<Person> findByName(String name, Optional<String> lastName) {

        TypedQuery<Person> query;
        CriteriaBuilder criteriabuilder = getSessionFactory().getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Person> criteria = criteriabuilder.createQuery(Person.class);
        Root<Person> loaders = criteria.from(Person.class);
        

        if (lastName.isPresent()) {
        criteria.select(loaders).where(criteriabuilder.and( criteriabuilder.like(criteriabuilder.lower(loaders.get("lastname")), "%"+lastName.get().toLowerCase()+"%"), 
                                                            criteriabuilder.like(criteriabuilder.lower(loaders.get("name")), "%"+name.toLowerCase()+"%") 
                                                          )
                                      );
        }
        else {
            criteria.select(loaders).where( criteriabuilder.like(criteriabuilder.lower(loaders.get("lastname")), lastName.get().toLowerCase()+"%"));
        }
        
        query = getSessionFactory().getCurrentSession().createQuery(criteria);
        query.setHint("org.hibernate.cacheable", true);
        query.setFlushMode(FlushModeType.COMMIT);
        
        //query.setParameter(idparameter, name);
        
        List<Person> list = query.getResultList();

        list.sort( new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return 0;
            }
        });
        
        
        if (list==null || list.isEmpty())
            return Optional.empty();
        
        return Optional.of(list.get(0));
    }


    @Transactional
    public Optional<Person> findByLastName(String lastName) {
        TypedQuery<Person> query;
        CriteriaBuilder criteriabuilder = getSessionFactory().getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Person> criteria = criteriabuilder.createQuery(Person.class);
        Root<Person> loaders = criteria.from(Person.class);
        
        criteria.select(loaders).where(criteriabuilder.like(criteriabuilder.lower(loaders.get("lastname")), "%"+lastName.toLowerCase()+"%"));
        query = getSessionFactory().getCurrentSession().createQuery(criteria);
        query.setHint("org.hibernate.cacheable", true);
        query.setFlushMode(FlushModeType.COMMIT);
        List<Person> list = query.getResultList();
        list.sort( new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return 0;
            }
        });
        if (list==null || list.isEmpty())
            return Optional.empty();
        return Optional.of(list.get(0));

    }


}
