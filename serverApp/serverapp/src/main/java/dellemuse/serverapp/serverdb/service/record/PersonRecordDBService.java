package dellemuse.serverapp.serverdb.service.record;

import java.io.File;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.InstitutionRecord;
import dellemuse.serverapp.serverdb.model.record.PersonRecord;
import dellemuse.serverapp.serverdb.service.DBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import jakarta.annotation.PostConstruct;
 
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class PersonRecordDBService extends DBService<PersonRecord, Long> {

	static private Logger logger = Logger.getLogger(PersonRecordDBService.class.getName());

	public PersonRecordDBService(CrudRepository<PersonRecord, Long> repository, ServerDBSettings settings) {
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
	public PersonRecord create(String name, User createdBy) {
		throw new RuntimeException("can not call create without language");
	}
	
	@Transactional
	public PersonRecord create(Person a, String lang, User createdBy) {

		PersonRecord c = new PersonRecord();

		c.setPerson(a);
		c.setName(a.getName());
		c.setUsethumbnail(c.isUsethumbnail());
		c.setLanguage(lang);
		
		c.setObjectState(ObjectState.EDITION);
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
		
		return getRepository().save(c);
	}

	/**
	 * 
	 * 
	 * @param a
	 * @param lang
	 * @return
	 */
	@Transactional
	public Optional<PersonRecord> findByPerson(Person a, String lang) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<PersonRecord> cq = cb.createQuery(PersonRecord.class);
		Root<PersonRecord> root = cq.from(PersonRecord.class);
		
	     Predicate p1 = cb.equal(root.get("person").get("id"), a.getId() );
	     Predicate p2 = cb.equal(root.get("language"), lang );

	     Predicate combinedPredicate = cb.and(p1, p2);
	     
	     cq.select(root).where(combinedPredicate);
	
		List<PersonRecord> list = this.getEntityManager().createQuery(cq).getResultList();
		return list == null || list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
		
	}

	@Transactional
	public List<PersonRecord> findAllByGuideContent(Person  a) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<PersonRecord> cq = cb.createQuery(PersonRecord.class);
		Root<PersonRecord> root = cq.from(PersonRecord.class);
		
	     Predicate p1 = cb.equal(root.get("person").get("id"), a.getId() );
	     cq.select(root).where(p1);
	
		List<PersonRecord> list = this.getEntityManager().createQuery(cq).getResultList();

		if (list==null)
			return new ArrayList<PersonRecord>();
		
		return list;
	}	

	/**
	 * 
	 * 
	 * @param name
	 * @param site
	 * @param createdBy
	 * @return
	 */
	@Transactional
	public PersonRecord create(String name, Person site, User createdBy) {
		PersonRecord c = new PersonRecord();
		
		c.setName(name);
		//c.setNameKey(nameKey(name));
		c.setPerson(site); 
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
		//c.setUsethumbnail(true);
		
		return getRepository().save(c);
	}

	
	 
	@Transactional
	private void deleteResources(Long id) {
		
		Optional<PersonRecord> o_aw = super.findWithDeps(id);

		if (o_aw.isEmpty())
			return;
		
		PersonRecord a=o_aw.get();
		
		getResourceDBService().delete(a.getPhoto());
		getResourceDBService().delete(a.getAudio());
		getResourceDBService().delete(a.getVideo());
		
	}
	 
	@Transactional
	public void delete(Long id) {
		deleteResources(id);
		super.deleteById(id);
	}

	@Transactional
	public void delete(PersonRecord o) {
		this.delete(o.getId()); 
	}
	
	
	@Transactional
	public Optional<PersonRecord> findWithDeps(Long id) {

		Optional<PersonRecord> o_aw = super.findById(id);

		if (o_aw.isEmpty())
			return  o_aw;
		
		PersonRecord aw = o_aw.get();
		 
		Resource photo = aw.getPhoto();

		User u = aw.getLastModifiedUser();
		
		if (u!=null)
			u.getDisplayname();
		
		if (photo != null)
			photo.getBucketName();
		
		aw.setDependencies(true);

		return o_aw;
	}
	
    @Transactional
    @Override
    public Iterable<PersonRecord> findAllSorted() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<PersonRecord> cq = cb.createQuery(getEntityClass());
        Root<PersonRecord> root = cq.from(getEntityClass());
        cq.orderBy(cb.asc( cb.lower(root.get("name"))));
        return getEntityManager().createQuery(cq).getResultList();
    }
    

	public boolean isDetached(PersonRecord entity) {
		return !getEntityManager().contains(entity);
	}
	
	@Transactional
	public void reloadIfDetached(PersonRecord src) {
		if (!getEntityManager().contains(src)) {
			src = findById(src.getId()).get();
		}
	}

	@Transactional
	public List<PersonRecord> getByName(String name) {
		return createNameQuery(name).getResultList();
	}

	@Override
	protected Class<PersonRecord> getEntityClass() {
		return PersonRecord.class;
	}
	
	@PostConstruct
	protected void onInitialize() {
		super.register(getEntityClass(), this);
	}

	 


}
