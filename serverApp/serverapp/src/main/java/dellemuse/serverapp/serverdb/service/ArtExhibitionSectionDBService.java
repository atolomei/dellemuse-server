package dellemuse.serverapp.serverdb.service;

import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.ArtExhibitionSection;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.AuditAction;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionRecord;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionSectionRecord;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.serverdb.service.record.ArtExhibitionGuideRecordDBService;
import dellemuse.serverapp.serverdb.service.record.ArtExhibitionSectionRecordDBService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class ArtExhibitionSectionDBService extends  MultiLanguageObjectDBservice<ArtExhibitionSection, Long> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(ArtExhibitionSectionDBService.class.getName());

	@JsonIgnore
	@PersistenceContext
	private EntityManager entityManager;

	@JsonIgnore
	@Autowired
	final ArtExhibitionSectionRecordDBService artExhibitionSectionRecordDBService;

	public ArtExhibitionSectionDBService(CrudRepository<ArtExhibitionSection, Long> repository, ServerDBSettings settings, ArtExhibitionSectionRecordDBService artExhibitionSectionRecordDBService) {
		super(repository, settings);
		this.artExhibitionSectionRecordDBService = artExhibitionSectionRecordDBService;
	}

	
	@Transactional
	public ArtExhibitionSection create(String name, User createdBy) {
		ArtExhibitionSection c = new ArtExhibitionSection();
		c.setName(name);

		c.setMasterLanguage(getDefaultMasterLanguage());
		c.setLanguage(getDefaultMasterLanguage());
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);

		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy, AuditAction.CREATE));

		return c;
	}

	@Transactional
	public ArtExhibitionSection create(String name, Site site, User createdBy) {
		ArtExhibitionSection c = new ArtExhibitionSection();

		c.setName(name);
		c.setMasterLanguage(site.getMasterLanguage());
		c.setLanguage(site.getLanguage());

		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);

		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy, AuditAction.CREATE));

		return c;
	}

	
	
	
	
	
	
	@Transactional
	public Optional<ArtExhibitionSection> findWithDeps(Long id) {

		Optional<ArtExhibitionSection> o = super.findById(id);

		if (o.isEmpty())
			return o;

		ArtExhibitionSection a = o.get();

		Resource photo = a.getPhoto();

		if (photo != null)
			photo.getBucketName();

		User user = a.getLastModifiedUser();
		if (user!=null)
			a.setLastModifiedUser(getUserDBService().findById(user.getId()).get());

		
		a.setDependencies(true);

		return o;
	}


	/**
	 * @param a
	 * @param lang
	 * @return
	 */
	@Transactional
	public Optional<ArtExhibitionSectionRecord> findByArtExhibitionSection(ArtExhibitionSection a, String lang) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArtExhibitionSectionRecord> cq = cb.createQuery(ArtExhibitionSectionRecord.class);
		Root<ArtExhibitionSectionRecord> root = cq.from(ArtExhibitionSectionRecord.class);

		Predicate p1 = cb.equal(root.get("artExhibitionSection").get("id"), a.getId());
		Predicate p2 = cb.equal(root.get("language"), lang);

		Predicate combinedPredicate = cb.and(p1, p2);

		cq.select(root).where(combinedPredicate);

		List<ArtExhibitionSectionRecord> list = this.getEntityManager().createQuery(cq).getResultList();
		return list == null || list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));

	}

	
	@Transactional
	public List<ArtExhibitionSection> getByName(String name) {
		return createNameQuery(name).getResultList();
	}

	@Override
	public String getObjectClassName() {
		return ArtExhibitionSection.class.getSimpleName().toLowerCase();
	}

	@Override
	protected Class<ArtExhibitionSection> getEntityClass() {
		return ArtExhibitionSection.class;
	}

	protected ArtExhibitionSectionRecordDBService getArtExhibitionSectionRecordDBService() {
		return this.artExhibitionSectionRecordDBService;
	}
	@PostConstruct
	protected void onInitialize() {
		super.registerRecordDB(getEntityClass(), getArtExhibitionSectionRecordDBService());
		super.register(getEntityClass(), this);
	}

}
