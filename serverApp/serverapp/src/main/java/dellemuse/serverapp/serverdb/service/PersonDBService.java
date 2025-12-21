package dellemuse.serverapp.serverdb.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.ArtWork;
 
import dellemuse.serverapp.serverdb.model.AuditAction;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.serverdb.service.record.ArtExhibitionGuideRecordDBService;
import dellemuse.serverapp.serverdb.service.record.PersonRecordDBService;
import jakarta.annotation.PostConstruct;
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
public class PersonDBService extends  MultiLanguageObjectDBservice<Person, Long> {

    private static final Logger logger = Logger.getLogger(PersonDBService.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    @JsonIgnore
	@Autowired
    final PersonRecordDBService personRecordDBService;
    
    public PersonDBService(CrudRepository<Person, Long> repository, ServerDBSettings settings, PersonRecordDBService personRecordDBService) {
        super(repository, settings);
        this.personRecordDBService=personRecordDBService;
    }


    @Transactional
    public Person create(String name, String lastname, User createdBy) {
        Person c = new Person();
        c.setName(name);
        
        
        c.setMasterLanguage(getDefaultMasterLanguage());
        c.setLanguage(getDefaultMasterLanguage());
        
        c.setLastname(lastname);
        c.setLastnameKey(nameKey(lastname));
         
        c.setObjectState(ObjectState.EDITION);
        
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        
 		getRepository().save(c);
        getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy,  AuditAction.CREATE));

		for ( Language la:getLanguageService().getLanguages() )
			getPersonRecordDBService().create(c, la.getLanguageCode(),  createdBy);
		
        return c;
    }

    @Transactional
    public Person create(String name, User createdBy, Optional<String> o_lastname, Optional<String> o_sex,
                         Optional<String> o_pid, Optional<String> o_address, Optional<String> o_zip,
                         Optional<String> o_phone, Optional<String> o_email) {
        Person c = new Person();
        c.setName(name);
        
        if (o_lastname.isPresent()) {
            c.setLastname(o_lastname.get());
            c.setLastnameKey(nameKey(o_lastname.get()));
        }

        o_sex.ifPresent(c::setSex);
        o_pid.ifPresent(c::setPhysicalid);
        o_address.ifPresent(c::setAddress);
        o_zip.ifPresent(c::setZipcode);
        o_phone.ifPresent(c::setPhone);
        o_email.ifPresent(c::setEmail);

        c.setObjectState(ObjectState.EDITION);
        
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);

        c.setMasterLanguage(getDefaultMasterLanguage());
        c.setLanguage(getDefaultMasterLanguage());
 
        getRepository().save(c);
        getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy,  AuditAction.CREATE));
    	
       for ( Language la:getLanguageService().getLanguages() )
			getPersonRecordDBService().create(c, la.getLanguageCode(),  createdBy);

		return c;
    }
    
    
    @Transactional
	public void reloadIfDetached(Person src) {

		if (!getEntityManager().contains(src)) {
			src = findById(src.getId()).get();
		}
	}

	@Transactional
	public Optional<Person> findWithDeps(Long id) {

		Optional<Person> o_aw = super.findById(id);

		if (o_aw.isEmpty())
			return  o_aw;
		
		Person aw = o_aw.get();
	
		Resource photo = aw.getPhoto();

		User u = aw.getLastModifiedUser();
		
		if (u!=null)
			u.getDisplayname();
		
		if (photo != null)
			photo.getBucketName();
		
		if (aw.getArtworks()!=null && aw.getArtworks().size()>0) {
			Set<ArtWork> set= new HashSet<ArtWork>();
			aw.getArtworks().forEach( p -> set.add(getArtWorkDBService().findById( p.getId()).get() ));
			aw.setArtworks(set);
		}
		
		aw.setDependencies(true);

		return o_aw;
	}
	 
    
    @Transactional
    public Iterable<Person> findAllSorted() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> cq = cb.createQuery(getEntityClass());
        Root<Person> root = cq.from(getEntityClass());
        cq.orderBy(cb.asc(root.get("sortlastfirstname")));
        
        return getEntityManager().createQuery(cq).getResultList();
    }
    
    
    public Person create(String name, User createdBy) {
        return create(name, name, createdBy);
    }


    @Transactional
    public List<ArtWork> getArtWorks(Person person) {
       
    	if (!person.isDependencies()) {
    		person = findWithDeps( person.getId()).get();
    	}
    	return person.getArtworks().stream().collect(Collectors.toList());
    	
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
	public Optional<Person> getByUser(User u) {
    	CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Person> cq = cb.createQuery(Person.class);
        Root<Person> root = cq.from(Person.class);
        cq.select(root).where(
                cb.equal(root.get("user").get("id"), u.getId().toString())
        );
        List<Person> list = getEntityManager().createQuery(cq).getResultList();
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
    
    @Override
	public String getObjectClassName() {
		 return  Person.class.getSimpleName().toLowerCase();
	} 


    @PostConstruct
    protected void onInitialize() {
    	super.registerRecordDB(getEntityClass(), getPersonRecordDBService());
		super.register(getEntityClass(), this);
    }

    protected PersonRecordDBService getPersonRecordDBService() {
		return this.personRecordDBService;
	}


	public ArtWorkDBService getArtWorkDBService() {
		return (ArtWorkDBService) ServiceLocator.getInstance().getBean(ArtWorkDBService.class);
		 
	}



	

}
                
        