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
	public Artist create(Person person, User createdBy) {
		Artist c = new Artist();
		c.setPerson(person);

		c.setMasterLanguage(person.getMasterLanguage());
		c.setLanguage(person.getLanguage());

		c.setObjectState(ObjectState.PUBLISHED);
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy, AuditAction.CREATE));

		// for ( Language la:getLanguageService().getLanguages() )
		// getPersonRecordDBService().create(c, la.getLanguageCode(), createdBy);

		return c;
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

		aw.setDependencies(true);
		return o_aw;
	}

	@Transactional
	public Iterable<Artist> findAllSorted() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Artist> cq = cb.createQuery(getEntityClass());
		Root<Artist> root = cq.from(getEntityClass());
		cq.orderBy(cb.asc(root.get("person").get("sortlastfirstname")));

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

	@PostConstruct
	protected void onInitialize() {
		super.register(getEntityClass(), this);
	}

	protected PersonRecordDBService getPersonRecordDBService() {
		return this.personRecordDBService;
	}

	public ArtWorkDBService getArtWorkDBService() {
		return (ArtWorkDBService) ServiceLocator.getInstance().getBean(ArtWorkDBService.class);

	}

}
