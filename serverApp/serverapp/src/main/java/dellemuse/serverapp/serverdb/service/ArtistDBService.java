package dellemuse.serverapp.serverdb.service;

import java.time.OffsetDateTime;
 
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Artist;
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
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.transaction.Transactional;

@Service
public class ArtistDBService extends MultiLanguageObjectDBservice<Artist, Long> {

	private static final Logger logger = Logger.getLogger(ArtistDBService.class.getName());

	@PersistenceContext
	private EntityManager entityManager;

	@JsonIgnore
	@Autowired
	final PersonRecordDBService personRecordDBService;

	public ArtistDBService(CrudRepository<Artist, Long> repository, ServerDBSettings settings, PersonRecordDBService personRecordDBService) {
		super(repository, settings);
		this.personRecordDBService = personRecordDBService;
	}
	
	@Transactional
	public Artist create(Site site, User createdBy) {
		return create( Optional.empty(), Optional.of(site), createdBy);
		
		 
	}

	

	@Transactional
	public Artist create(Optional<Person> operson, Optional<Site> osite, User createdBy) {
	
		Artist c = new Artist();

		if (operson.isPresent()) {
			c.setPerson(operson.get());
			c.setMasterLanguage(operson.get().getMasterLanguage());
			c.setLanguage(operson.get().getLanguage());
			c.setName(operson.get().getName());
			c.setLastname(operson.get().getLastname());
		}

		if (osite.isPresent()) {
			c.setSite(osite.get());
			c.setMasterLanguage(osite.get().getMasterLanguage());
			c.setLanguage(osite.get().getLanguage());
			
			if (c.getLastname()==null) 
				c.setLastname("artist " + osite.get().getName());
			}

		if (c.getMasterLanguage()==null) {
			c.setMasterLanguage(this.getDefaultMasterLanguage());
		}

		if (c.getLanguage()==null) {
			c.setLanguage(this.getDefaultMasterLanguage());
		}
		
		if (c.getLastname()==null) 
			c.setLastname("new");
		
		c.setObjectState(ObjectState.PUBLISHED);
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);

		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy, AuditAction.CREATE));

		for ( Language la:getLanguageService().getLanguages() )
		 getArtistRecordDBService().create(c, la.getLanguageCode(), createdBy);

		return c;
	}

	
	@Transactional
	public void save(Artist o, User user, List<String> updatedParts) {
		super.save(o);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(o, user, AuditAction.UPDATE, String.join(", ", updatedParts)));
	}
	
	
    @Transactional
	public Optional<Artist> getByPerson(Person u) {
    	CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Artist> cq = cb.createQuery(Artist.class);
        Root<Artist> root = cq.from(Artist.class);
        cq.select(root).where(cb.equal(root.get("person").get("id"), u.getId().toString()));
        List<Artist> list = getEntityManager().createQuery(cq).getResultList();
        return list.stream().findFirst();
	}

    
	
	@Transactional
	public Optional<Artist> findWithDeps(Long id) {

		Optional<Artist> o_aw = super.findById(id);

		if (o_aw.isEmpty())
			return o_aw;

		Artist aw = o_aw.get();

		Set<ArtWork> set = new HashSet<ArtWork>();

		aw.getArtworks().forEach(x -> set.add(getArtWorkDBService().findById(x.getId()).get()));
		aw.setArtworks(set);
		
		if (aw.getPerson()!=null)
			aw.setPerson( getPersonDBService().findById(aw.getPerson().getId()).get());
		
		User user = aw.getLastModifiedUser();
		if (user!=null)
			aw.setLastModifiedUser(getUserDBService().findById(user.getId()).get());
		
		if (aw.getSite()!=null) {
			aw.setSite(getSiteDBService().findById(aw.getSite().getId()).get());
		}
		
		if (aw.getArtistSites()!=null) {
			Set<Site> s = new HashSet<Site>();
			aw.getArtistSites().forEach( b -> s.add(getSiteDBService().findById(b.getId()).get()));
			aw.setArtistSites(s);
		}
		
		aw.setDependencies(true);
		return o_aw;
	}

	@Transactional
	public Iterable<Artist> findAllSorted() {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Artist> cq = cb.createQuery(getEntityClass());
		Root<Artist> root = cq.from(getEntityClass());
		cq.orderBy(cb.asc(root.get("sortlastfirstname")));
		return getEntityManager().createQuery(cq).getResultList();
	}
	

	@Transactional
	public Iterable<Artist> findAllSorted(ObjectState os1) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Artist> cq = cb.createQuery(getEntityClass());
		Root<Artist> root = cq.from(getEntityClass());
		Predicate p1 = cb.equal(root.get("state"), os1);
		cq.select(root).where(p1);
		cq.orderBy(cb.asc(root.get("sortlastfirstname")));

		return getEntityManager().createQuery(cq).getResultList();
	}
	
	@Transactional
	public Iterable<Artist> findAllSorted(ObjectState os1, ObjectState os2) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Artist> cq = cb.createQuery(getEntityClass());
		Root<Artist> root = cq.from(getEntityClass());
		
		Predicate p1 = cb.equal(root.get("state"), os1);
		Predicate p2 = cb.equal(root.get("state"), os2);
		Predicate combinedPredicate = cb.or(p1, p2);
		cq.select(root).where(combinedPredicate);
		
		cq.orderBy(cb.asc(root.get("sortlastfirstname")));

		return getEntityManager().createQuery(cq).getResultList();
	}
	
	@Transactional
	public Iterable<Artist> findSiteAllSorted(Site site, ObjectState os1, ObjectState os2) {

		  CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		    CriteriaQuery<Artist> cq = cb.createQuery(getEntityClass());
		    Root<Artist> root = cq.from(getEntityClass());

		    Predicate statePredicate = cb.or(
		        cb.equal(root.get("state"), os1),
		        cb.equal(root.get("state"), os2)
		    );

			Predicate sitePredicate = cb.equal(root.get("site").get("id"), String.valueOf(site.getId()));

			Predicate finalPredicate = cb.and(statePredicate, sitePredicate);

			cq.select(root).where(finalPredicate);
			cq.orderBy(cb.asc(cb.lower(root.get("sortlastfirstname"))));
		    		    
		    return getEntityManager().createQuery(cq).getResultList();
	}
	
	
	
	@Transactional
	public Iterable<Artist> findortedMultiSitesAllSorted(Site site, ObjectState os1, ObjectState os2) {

		  CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		    CriteriaQuery<Artist> cq = cb.createQuery(getEntityClass());
		    Root<Artist> root = cq.from(getEntityClass());

		    Predicate statePredicate = cb.or(
		        cb.equal(root.get("state"), os1),
		        cb.equal(root.get("state"), os2)
		    );

		    // EXISTS subquery instead of JOIN
		    Subquery<Long> sub = cq.subquery(Long.class);
		    Root<Artist> subRoot = sub.from(Artist.class);
		    Join<Artist, Site> subSites = subRoot.join("artistSites");

		    sub.select(subRoot.get("id"))
		       .where(
		           cb.equal(subRoot.get("id"), root.get("id")),
		           cb.equal(subSites.get("id"), site.getId())
		       );

		    cq.select(root)
		      .where(cb.and(statePredicate, cb.exists(sub)))
		      //.orderBy(cb.asc(cb.lower(root.get("name"))));
		      .orderBy(cb.asc(root.get("sortlastfirstname")));
		    
		    return getEntityManager().createQuery(cq).getResultList();
	}
	
	
	
	@Override
	protected Class<Artist> getEntityClass() {
		return Artist.class;
	}

	@Override
	public String getObjectClassName() {
		return Artist.class.getSimpleName().toLowerCase();
	}

	//@PostConstruct
	public void onInitialize() {
		super.registerRecordDB(getEntityClass(), getArtistRecordDBService());
		super.register(getEntityClass(), this);
	}

	protected PersonRecordDBService getPersonRecordDBService() {
		return this.personRecordDBService;
	}

	public ArtWorkDBService getArtWorkDBService() {
		return (ArtWorkDBService) ServiceLocator.getInstance().getBean(ArtWorkDBService.class);

	}

}
