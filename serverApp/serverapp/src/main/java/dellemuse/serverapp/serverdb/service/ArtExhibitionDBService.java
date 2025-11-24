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
import dellemuse.serverapp.serverdb.model.AudioStudio;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionRecord;
import dellemuse.serverapp.serverdb.model.record.GuideContentRecord;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.serverdb.service.record.ArtExhibitionRecordDBService;
import dellemuse.serverapp.serverdb.service.record.ArtWorkRecordDBService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;

import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class ArtExhibitionDBService extends DBService<ArtExhibition, Long> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(ArtExhibitionDBService.class.getName());

	@JsonIgnore
	@Autowired
	final ArtExhibitionRecordDBService artExhibitionRecordDBService;

	@JsonIgnore
	@Autowired
	@PersistenceContext
	private EntityManager entityManager;

	public ArtExhibitionDBService(CrudRepository<ArtExhibition, Long> repository, ServerDBSettings settings, ArtExhibitionRecordDBService artExhibitionRecordDBService) {
		super(repository, settings);
		this.artExhibitionRecordDBService = artExhibitionRecordDBService;
	}


	/**
	 * 
	 */
	@Transactional
	@Override
	public ArtExhibition create(String name, User createdBy) {
		ArtExhibition c = new ArtExhibition();
		c.setName(name);

		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
		c.setState(ObjectState.EDITION);

		c.setMasterLanguage(getDefaultMasterLanguage());
		c.setLanguage(getDefaultMasterLanguage());

		getRepository().save(c);

		for (Language la : getLanguageService().getLanguages())
			getArtExhibitionRecordDBService().create(c, la.getLanguageCode(), createdBy);

		return getRepository().save(c);
	}

	@Transactional
	public ArtExhibition create(String name, Site site, User createdBy) {
		ArtExhibition c = new ArtExhibition();
		c.setName(name);

		c.setState(ObjectState.EDITION);

		c.setMasterLanguage(site.getMasterLanguage());
		c.setLanguage(site.getLanguage());

		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
		c.setSite(site);

		getRepository().save(c);
		for (Language la : getLanguageService().getLanguages())
			getArtExhibitionRecordDBService().create(c, la.getLanguageCode(), createdBy);

		return getRepository().save(c);
	}

	/**
	 * 
	 *  ArtExhibition (1)
	 *  ArtExhibitionRecord(n) 
	 *  ArtExhibitionItem (n)
	 */
	@Transactional
	public void markAsDeleted(ArtExhibition c, User deletedBy) {
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(deletedBy);
		c.setState(ObjectState.DELETED);
		getRepository().save(c);

		for (ArtExhibitionRecord g : getArtExhibitionRecordDBService().findAllByGuideContent(c)) {
			getArtExhibitionRecordDBService().markAsDeleted(g, deletedBy);
		}
		
		c.getArtExhibitionItems().forEach(gc -> {
			getArtExhibitionItemDBService().markAsDeleted(gc, deletedBy);
		});
	}

	@Transactional
	public void restore(ArtExhibition c, User restoredBy) {

		OffsetDateTime date = OffsetDateTime.now();
		c.setLastModified(date);
		c.setLastModifiedUser(restoredBy);
		c.setState(ObjectState.EDITION);
		getRepository().save(c);

		for (ArtExhibitionRecord g : getArtExhibitionRecordDBService().findAllByGuideContent(c)) {
			getArtExhibitionRecordDBService().restore(g, restoredBy);
		}
		
		c.getArtExhibitionItems().forEach(gc -> {
			getArtExhibitionItemDBService().restore(gc, restoredBy);
		});
	}

	@Transactional
	public Optional<ArtExhibition> findWithDeps(Long id) {

		Optional<ArtExhibition> o = super.findById(id);

		if (o.isEmpty())
			return o;

		ArtExhibition a = o.get();

		Long siteId = a.getSite().getId();

		if (siteId != null) {
			SiteDBService se = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
			a.setSite(se.findById(siteId).get());
		}

		if (a.getArtExhibitionItems() != null) {
			ArtExhibitionDBService se = (ArtExhibitionDBService) ServiceLocator.getInstance().getBean(ArtExhibitionDBService.class);
			a.setArtExhibitionItems(se.getArtExhibitionItems(a));
		}

		Resource photo = a.getPhoto();

		if (photo != null)
			photo.getBucketName();

		a.setDependencies(true);

		return o;
	}

	@PostConstruct
	protected void onInitialize() {
		super.register(getEntityClass(), this);
	}

	@Transactional
	public void addItem(ArtExhibition exhibition, ArtWork artwork, User addedBy) {

		boolean contains = false;

		List<ArtExhibitionItem> list = getArtExhibitionItems(exhibition);

		for (ArtExhibitionItem i : list) {
			if (artwork.getId().equals(i.getArtWork().getId())) {
				contains = true;
				break;
			}
		}

		if (contains)
			return;

		ArtExhibitionItem item = getArtExhibitionItemDBService().create(artwork.getName(), exhibition, artwork, addedBy);

		list.add(item);
		exhibition.setArtExhibitionItems(list);

		exhibition.setLastModified(OffsetDateTime.now());
		exhibition.setLastModifiedUser(addedBy);
		getRepository().save(exhibition);
	}

	@Transactional
	public void removeItem(ArtExhibition c, ArtExhibitionItem item, User removedBy) {

		boolean contains = false;
		int index = -1;

		List<ArtExhibitionItem> list = getArtExhibitionItems(c);

		for (ArtExhibitionItem i : list) {
			index++;
			if (item.getId().equals(i.getId())) {
				contains = true;
				break;
			}
		}

		if (!contains)
			return;

		list.remove(index);
		c.setArtExhibitionItems(list);
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(removedBy);
		getRepository().save(c);
	}

	@Transactional
	public void removeSection(ArtExhibition ex, ArtExhibitionSection item, User removedBy) {

		boolean contains = false;
		int index = -1;

		List<ArtExhibitionSection> list = getArtExhibitionSections(ex);

		for (ArtExhibitionSection i : list) {
			index++;
			if (item.getId().equals(i.getId())) {
				contains = true;
				break;
			}
		}

		if (!contains)
			return;

		list.remove(index);
		ex.setArtExhibitionSections(list);
		ex.setLastModified(OffsetDateTime.now());
		ex.setLastModifiedUser(removedBy);
		getRepository().save(ex);
 	}
 
	@Transactional
	public List<ArtExhibition> getByName(String name) {
		return createNameQuery(name).getResultList();
	}

	@Transactional
	public List<ArtExhibitionGuide> getArtExhibitionGuides(ArtExhibition exhibition) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArtExhibitionGuide> cq = cb.createQuery(ArtExhibitionGuide.class);
		Root<ArtExhibitionGuide> root = cq.from(ArtExhibitionGuide.class);
		cq.select(root).where(cb.equal(root.get("artExhibition").get("id"), exhibition.getId()));
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));

		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public List<ArtExhibitionItem> getArtExhibitionItems(ArtExhibition exhibition) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArtExhibitionItem> cq = cb.createQuery(ArtExhibitionItem.class);
		Root<ArtExhibitionItem> root = cq.from(ArtExhibitionItem.class);
		cq.select(root).where(cb.equal(root.get("artExhibition").get("id"), exhibition.getId()));
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public List<ArtExhibitionSection> getArtExhibitionSections(ArtExhibition exhibition) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArtExhibitionSection> cq = cb.createQuery(ArtExhibitionSection.class);
		Root<ArtExhibitionSection> root = cq.from(ArtExhibitionSection.class);
		cq.select(root).where(cb.equal(root.get("artExhibition").get("id"), exhibition.getId()));
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public List<ArtExhibition> getArtExhibitions(Site site) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArtExhibition> cq = cb.createQuery(ArtExhibition.class);
		Root<ArtExhibition> root = cq.from(ArtExhibition.class);
		cq.select(root).where(cb.equal(root.get("site").get("id"), site.getId()));
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));

		return getEntityManager().createQuery(cq).getResultList();
	}

	@Override
	protected Class<ArtExhibition> getEntityClass() {
		return ArtExhibition.class;
	}
	
	protected ArtExhibitionRecordDBService getArtExhibitionRecordDBService() {
		return this.artExhibitionRecordDBService;
	}

}
