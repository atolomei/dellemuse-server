package dellemuse.serverapp.serverdb.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.AudioStudio;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.GuideContentRecord;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.serverdb.service.record.ArtExhibitionGuideRecordDBService;
import dellemuse.serverapp.serverdb.service.record.GuideContentRecordDBService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class GuideContentDBService extends DBService<GuideContent, Long> {

	@SuppressWarnings("unused")
	static private Logger logger = Logger.getLogger(GuideContentDBService.class.getName());

	@JsonIgnore
	@Autowired
	final GuideContentRecordDBService guideContentRecordDBService;

	public GuideContentDBService(CrudRepository<GuideContent, Long> repository, ServerDBSettings settings, GuideContentRecordDBService guideContentRecordDBService) {
		super(repository, settings);
		this.guideContentRecordDBService = guideContentRecordDBService;
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
	public GuideContent create(String name, User createdBy) {
		GuideContent c = new GuideContent();
		c.setName(name);
		 
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);

		c.setMasterLanguage(getDefaultMasterLanguage());
		c.setLanguage(getDefaultMasterLanguage());
		
		c.setState(ObjectState.EDITION);
		
		getRepository().save(c);

		for (Language la : getLanguageService().getLanguages())
			getGuideContentRecordDBService().create(c, la.getLanguageCode(), createdBy);

		return getRepository().save(c);
	}
	
	@Transactional
	public GuideContent create(ArtExhibitionGuide guide, ArtExhibitionItem item, User createdBy) {

		boolean exists = existsInGuide(guide, item);

		if (exists) {
			List<GuideContent> list = findByArtExhibitionItem(guide, item);

			if (list.size() > 0)
				return list.get(0);

			return null;
		}

		GuideContent c = new GuideContent();

		c.setMasterLanguage(item.getMasterLanguage());
		c.setLanguage(item.getLanguage());
		
		c.setState(ObjectState.EDITION);
		
		c.setName(item.getName());
		c.setArtExhibitionGuide(guide);
		c.setArtExhibitionItem(item);
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);

		getRepository().save(c);

		for (Language la : getLanguageService().getLanguages())
			getGuideContentRecordDBService().create(c, la.getLanguageCode(), createdBy);

		return getRepository().save(c);
	}
	
	@Transactional
	public void delete(GuideContent c, User deletedBy) {
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(deletedBy);
		c.setState(ObjectState.DELETED);
		getRepository().save(c);
	}
	
	/**
	 * guideContent (1)
	 * guideContentRecord (n)
	 * AudioStudio (1)
	 * 
	 */
	@Transactional
	public void markAsDeleted(GuideContent c, User deletedBy) {
		
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(deletedBy);
		c.setState(ObjectState.DELETED);
		
		getRepository().save(c);		
	
		for (GuideContentRecord g: getGuideContentRecordDBService(). findAllByGuideContent(c)) {
			getGuideContentRecordDBService().markAsDeleted(g, deletedBy);		
		}
		
		Optional<AudioStudio> o = getAudioStudioDBService().findByGuideContent(c);
		if (o.isPresent()) 
			getAudioStudioDBService().markAsDeleted(o.get(), deletedBy);
	}
	
	/**
	 * 
	 * guideContent (1)
	 * guideContentRecord (n)
	 * AudioStudio (1)
	 * 
	 */
	@Transactional
	public void restore(GuideContent c, User restoredBy) {
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(restoredBy);
		c.setState(ObjectState.EDITION);
		getRepository().save(c);		
		
		for (GuideContentRecord g: getGuideContentRecordDBService(). findAllByGuideContent(c)) {
			getGuideContentRecordDBService().restore(g, restoredBy);		
		}
		
		Optional<AudioStudio> o = getAudioStudioDBService().findByGuideContent(c);
		if (o.isPresent()) {
			getAudioStudioDBService().restore(o.get(), restoredBy);
		}
	}

 	@Transactional
	public boolean existsInGuide(ArtExhibitionGuide guide, ArtExhibitionItem item) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<GuideContent> cq = cb.createQuery(getEntityClass());
		Root<GuideContent> root = cq.from(getEntityClass());
		cq.select(root).where(cb.equal(root.get("artExhibitionItem").get("id"), item.getId()));
		return getEntityManager().createQuery(cq).getResultList().size() > 0;
	}

	@Transactional
	public List<GuideContent> findByArtExhibitionItem(ArtExhibitionGuide guide, ArtExhibitionItem item) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<GuideContent> cq = cb.createQuery(getEntityClass());
		Root<GuideContent> root = cq.from(getEntityClass());
		cq.select(root).where(cb.equal(root.get("artExhibitionItem").get("id"), item.getId()));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@PostConstruct
	protected void onInitialize() {
		super.register(getEntityClass(), this);
	}

	@Transactional
	public void delete(GuideContent c) {
		this.getRepository().delete(c);
	}

	@Transactional
	public Optional<GuideContent> findWithDeps(Long id) {

		Optional<GuideContent> o = super.findById(id);

		if (o.isEmpty())
			return o;

		GuideContent a = o.get();

		if (a.getArtExhibitionGuide() != null)
			a.getArtExhibitionGuide().getDisplayname();

		if (a.getArtExhibitionItem() != null)
			a.getArtExhibitionItem().getDisplayname();

		if (a.getPhoto() != null)
			a.getPhoto().getBucketName();

		if (a.getAudio() != null)
			a.getAudio().getBucketName();

		a.setDependencies(true);

		return o;
	}

	/**
	 * @param name
	 * @return
	 */
	public List<GuideContent> getByName(String name) {
		return createNameQuery(name).getResultList();
	}

	@Override
	protected Class<GuideContent> getEntityClass() {
		return GuideContent.class;
	}

	public GuideContentRecordDBService getGuideContentRecordDBService() {
		return guideContentRecordDBService;
	}

	

	
}
