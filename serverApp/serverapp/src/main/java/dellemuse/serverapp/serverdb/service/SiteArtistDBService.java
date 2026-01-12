package dellemuse.serverapp.serverdb.service;

import java.time.OffsetDateTime;
 
import java.util.HashSet;
 
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
import dellemuse.serverapp.serverdb.model.SiteArtist;
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


/**
 * 
 * Person
 * Artist
 * -----------------
 * Site -> create SiteArtist
 * 
 * ---
 * if Person + Artist not exist ??? -> Site creates using control. if estimation is that the artist exist -> sorry can not 
 * use site joker artist and suggest.
 * 
 * ---
 * 
 */
@Service
public class SiteArtistDBService extends MultiLanguageObjectDBservice<SiteArtist, Long> {

	private static final Logger logger = Logger.getLogger(SiteArtistDBService.class.getName());

	@PersistenceContext
	private EntityManager entityManager;

	 

	public SiteArtistDBService(CrudRepository<SiteArtist, Long> repository, ServerDBSettings settings) {
		super(repository, settings);
		 
	}

	@Transactional
	public SiteArtist create(Site site, Artist artist, User createdBy) {
		
		SiteArtist c = new SiteArtist();
		c.setSite(site);
		c.setArtist(artist);
		c.setObjectState(ObjectState.PUBLISHED);
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy, AuditAction.CREATE));

		return c;
	}

	
	 @Transactional
	public Optional<SiteArtist> findWithDeps(Long id) {

		Optional<SiteArtist> o_sa = super.findById(id);

		if (o_sa.isEmpty())
			return o_sa;

		SiteArtist sa = o_sa.get();
		sa.setLastModifiedUser( getUserDBService().findById( sa.getLastModifiedUser().getId()).get());
		sa.setDependencies(true);

		return o_sa;
	}
 
	
	@Transactional
	public Iterable<SiteArtist> findAllSorted() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<SiteArtist> cq = cb.createQuery(getEntityClass());
		Root<SiteArtist> root = cq.from(getEntityClass());
		cq.orderBy(cb.asc(root.get("artist").get("person").get("sortlastfirstname")));

		return getEntityManager().createQuery(cq).getResultList();
	}

	@Override
	protected Class<SiteArtist> getEntityClass() {
		return SiteArtist.class;
	}

	@Override
	public String getObjectClassName() {
		return SiteArtist.class.getSimpleName().toLowerCase();
	}

	@PostConstruct
	protected void onInitialize() {
		super.register(getEntityClass(), this);
	}


	public ArtWorkDBService getArtWorkDBService() {
		return (ArtWorkDBService) ServiceLocator.getInstance().getBean(ArtWorkDBService.class);

	}

}
