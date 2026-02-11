package dellemuse.server.db.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.server.Settings;
import dellemuse.server.db.model.ArtWork;
import dellemuse.server.db.model.ArtWorkArtist;
import dellemuse.server.db.model.Person;
import dellemuse.server.db.model.Site;
import dellemuse.server.db.model.User;
import dellemuse.model.logging.Logger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class PersonDBService extends DBService<Person, Long> {

    private static final Logger logger = Logger.getLogger(PersonDBService.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    public PersonDBService(CrudRepository<Person, Long> repository, Settings settings) {
        super(repository, settings);
    }

    @Override
    public Person create(String name, User createdBy) {
        return create(name, null, createdBy);
    }
    
    @Transactional
    public Iterable<Person> findAllSorted() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> cq = cb.createQuery(getEntityClass());
        Root<Person> root = cq.from(getEntityClass());
        cq.orderBy(cb.asc(root.get("sortlastfirstname")));
        return entityManager.createQuery(cq).getResultList();
    }
    
    @Transactional
    public Person create(String name, String lastname, User createdBy) {
        Person c = new Person();
        c.setName(name);
        c.setNameKey(nameKey(name));
        c.setLastname(lastname);
        c.setLastnameKey(nameKey(lastname));
        c.setTitle(name + " " + lastname);
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        return getRepository().save(c);
    }

    @Transactional
    public Person create(String name, User createdBy, Optional<String> o_lastname, Optional<String> o_sex,
                         Optional<String> o_pid, Optional<String> o_address, Optional<String> o_zip,
                         Optional<String> o_phone, Optional<String> o_email) {
        
    	Person c = new Person();
        c.setName(name);
        c.setNameKey(nameKey(name));

        if (o_lastname.isPresent()) {
            c.setLastname(o_lastname.get());
            c.setLastnameKey(nameKey(o_lastname.get()));
            c.setTitle(name + " " + o_lastname.get());
        } else {
            c.setTitle(name);
        }

        o_sex.ifPresent(c::setSex);
        o_pid.ifPresent(c::setPhysicalid);
        o_address.ifPresent(c::setAddress);
        o_zip.ifPresent(c::setZipcode);
        o_phone.ifPresent(c::setPhone);
        o_email.ifPresent(c::setEmail);

        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);

        return getRepository().save(c);
    }

    

    
    
    @Transactional
    public List<ArtWork> getArtWorks(Person person) {
        List<ArtWorkArtist> artWorkArtists = entityManager
                .createQuery("FROM #ArtWorkArtist WHERE artist.id = :artistid", ArtWorkArtist.class)
                .setParameter("artistid", person.getId())
                .getResultList();

        return artWorkArtists.stream()
                .map(ArtWorkArtist::getArtwork)
                .collect(Collectors.toList());
    }

    public List<Person> getByName(String name) {
        return createNameQuery(name).getResultList();
    }

    @Transactional
    public Optional<Person> findByName(String name) {
        return findByDisplayName(name);
    }

    @Transactional
    public Optional<Person> findByDisplayName(String displayName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> cq = cb.createQuery(Person.class);
        Root<Person> root = cq.from(Person.class);

        cq.select(root).where(
                cb.like(cb.lower(root.get("displayname")), "%" + displayName.toLowerCase() + "%")
        );

        List<Person> list = entityManager.createQuery(cq).getResultList();
        return list.stream().findFirst();
    }

    @Transactional
    public Optional<Person> findByName(String name, Optional<String> lastName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> cq = cb.createQuery(Person.class);
        Root<Person> root = cq.from(Person.class);
        List<Predicate> predicates = new ArrayList<>();
        lastName.ifPresent(lname -> {
            predicates.add(cb.like(cb.lower(root.get("lastname")), "%" + lname.toLowerCase() + "%"));
        });
        predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        cq.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
        List<Person> list = entityManager.createQuery(cq).getResultList();
        return list.stream().findFirst();

    }

    @Transactional
    public Optional<Person> findByLastName(String lastName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> cq = cb.createQuery(Person.class);
        Root<Person> root = cq.from(Person.class);

        cq.select(root).where(
                cb.like(cb.lower(root.get("lastname")), "%" + lastName.toLowerCase() + "%")
        );

        List<Person> list = entityManager.createQuery(cq).getResultList();
        return list.stream().findFirst();
    }

    @Override
    protected Class<Person> getEntityClass() {
        return Person.class;
    }
}
                
        