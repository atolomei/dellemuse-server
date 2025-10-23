package dellemuse.serverapp.serverdb.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class ArtExhibitionGuideDBService extends DBService<ArtExhibitionGuide, Long> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(ArtExhibitionGuideDBService.class.getName());

	@PersistenceContext
	private EntityManager entityManager;

	public ArtExhibitionGuideDBService(CrudRepository<ArtExhibitionGuide, Long> repository, ServerDBSettings settings) {
		super(repository, settings);
	}

	@Transactional
    public void removeItem(ArtExhibitionGuide c, GuideContent  item, User removedBy) {
    	
    	boolean contains = false;
    	int index = -1;
    	
    	List<GuideContent> list = getGuideContents(c);
    		
    	for (GuideContent i: list) {
    		index++;
    		if (item.getId().equals(i.getId())) {
    			contains=true;
    			break;
    		}
    	}
    	
    	if (!contains)
    		return;
    	
    	list.remove(index);
    	c.setContents(list);
    	c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(removedBy);
        
        getRepository().save(c);
    }
	
	@Transactional
	public Optional<ArtExhibitionGuide> findWithDeps(Long id) {

		Optional<ArtExhibitionGuide> o = super.findById(id);

		if (o.isEmpty())
			return o;

		ArtExhibitionGuide a = o.get();

		if (a.getArtExhibition() != null)
			a.getArtExhibition().getDisplayname();

		if (a.getPublisher() != null)
			a.getPublisher().getDisplayname();

		if (a.getGuideContents() != null)
			a.getGuideContents().size();

		if (a.getPhoto() != null)
			a.getPhoto().getBucketName();

		if (a.getAudio() != null)
			a.getAudio().getBucketName();

		a.setDependencies(true);

		return o;
	}

	@PostConstruct
	protected void onInitialize() {
		super.register(getEntityClass(), this);
	}

	@Transactional
	@Override
	public ArtExhibitionGuide create(String name, User createdBy) {

		ArtExhibitionGuide c = new ArtExhibitionGuide();
		c.setName(name);
		c.setOfficial(true);
		c.setNameKey(nameKey(name));
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
		return getRepository().save(c);
	}

	@Transactional
	public ArtExhibitionGuide create(String name, ArtExhibition ex, User createdBy) {
		ArtExhibitionGuide c = new ArtExhibitionGuide();
		c.setName(name);
		c.setNameKey(nameKey(name));

		c.setOfficial(true);
		c.setArtExhibition(ex);

		c.setMasterLanguage(ex.getMasterLanguage());

		
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);

		return getRepository().save(c);
	}

	@Transactional
	public List<ArtExhibitionGuide> getByName(String name) {
		return createNameQuery(name).getResultList();
	}

	@Transactional
	public List<ArtExhibitionGuide> findByNameKey(String nameKey) {
		return getByNameKey(nameKey);
	}

	@Transactional
	public List<GuideContent> getGuideContents(ArtExhibitionGuide exhibitionGuide) {
		return getArtExhibitionGuideContents(exhibitionGuide.getId());
	}

	@Transactional
	public List<GuideContent> getArtExhibitionGuideContents(Long guideId) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<GuideContent> cq = cb.createQuery(GuideContent.class);
		Root<GuideContent> root = cq.from(GuideContent.class);
		cq.select(root).where(cb.equal(root.get("artExhibitionGuide").get("id"), guideId));
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public List<GuideContent> getArtExhibitionGuidePublishedBy(Person person) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<GuideContent> cq = cb.createQuery(GuideContent.class);
		Root<GuideContent> root = cq.from(GuideContent.class);

		cq.select(root).where(cb.equal(root.get("publisher").get("id"), person.getId()));
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));

		return getEntityManager().createQuery(cq).getResultList();
	}

	@Override
	protected Class<ArtExhibitionGuide> getEntityClass() {
		return ArtExhibitionGuide.class;
	}
}
