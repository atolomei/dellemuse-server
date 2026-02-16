package dellemuse.serverapp.serverdb.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.Check;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.audiostudio.AudioStudioParentObject;
import dellemuse.serverapp.audit.AuditKey;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;

import dellemuse.serverapp.serverdb.model.AudioStudio;
import dellemuse.serverapp.serverdb.model.AuditAction;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.GuideContent;

import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionGuideRecord;

import dellemuse.serverapp.serverdb.model.record.GuideContentRecord;
import dellemuse.serverapp.serverdb.model.record.TranslationRecord;
import dellemuse.serverapp.service.language.LanguageService;
import jakarta.annotation.PostConstruct;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class AudioStudioDBService extends DBService<AudioStudio, Long> {

	static private Logger logger = Logger.getLogger(AudioStudioDBService.class.getName());

	@JsonIgnore
	final LanguageService languageService;

	public AudioStudioDBService(CrudRepository<AudioStudio, Long> repository, ServerDBSettings settings, LanguageService languageService) {
		super(repository, settings);
		this.languageService = languageService;
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
	public AudioStudio create(String name, ArtExhibitionGuide guide, User createdBy) {
		AudioStudio c = new AudioStudio();
		c.setName(name);
		c.setLanguage(guide.getLanguage());
		c.setInfo(guide.getInfo());
		c.setState(guide.getState());
		c.setArtExhibitionGuide(guide);
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);

		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy, AuditAction.CREATE));

		return c;
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
	public AudioStudio create(String name, GuideContent content, User createdBy) {
		AudioStudio c = new AudioStudio();
		c.setName(name);
		c.setLanguage(content.getLanguage());
		c.setInfo(content.getInfo());
		c.setState(content.getState());
		c.setGuideContent(content);
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);

		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy, AuditAction.CREATE));

		return c;
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
	public AudioStudio create(String name, GuideContentRecord content, User createdBy) {

		AudioStudio c = new AudioStudio();
		c.setLanguage(content.getLanguage());
		c.setName(name);
		c.setInfo(content.getInfo());
		c.setState(content.getState());
		c.setGuideContentRecord(content);
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);

		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy, AuditAction.CREATE));

		return c;
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
	public AudioStudio create(String name, ArtExhibitionGuideRecord content, User createdBy) {

		AudioStudio c = new AudioStudio();
		c.setName(name);
		c.setLanguage(content.getLanguage());
		c.setInfo(content.getInfo());
		c.setState(content.getState());
		c.setArtExhibitionGuideRecord(content);
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);

		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy, AuditAction.CREATE));

		return c;
	}

	@Transactional
	public void save(AudioStudio o, User user, String msg) {
		super.save(o);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(o, user, AuditAction.UPDATE, msg));
	}
	
	
	
	@Transactional
	public Optional<AudioStudio> findOrCreate(ArtExhibitionGuide o, User createdBy) {

		Optional<AudioStudio> oa = findByArtExhibitionGuide(o);
		if (oa.isPresent())
			return oa;
		AudioStudio a = create(o.getName(), o, createdBy);
		return Optional.of(a);
	}

	@Transactional
	public Optional<AudioStudio> findOrCreate(ArtExhibitionGuideRecord o, User createdBy) {

		Optional<AudioStudio> oa = findByArtExhibitionGuideRecord(o);
		if (oa.isPresent())
			return oa;
		AudioStudio a = create(o.getName(), o, createdBy);
		return Optional.of(a);
	}

	@Transactional
	public Optional<AudioStudio> findOrCreate(GuideContent o, User createdBy) {

		Optional<AudioStudio> oa = findByGuideContent(o);
		if (oa.isPresent())
			return oa;
		AudioStudio a = create(o.getName(), o, createdBy);
		return Optional.of(a);
	}

	@Transactional
	public Optional<AudioStudio> findOrCreate(GuideContentRecord o, User createdBy) {

		Optional<AudioStudio> oa = findByGuideContentRecord(o);
		if (oa.isPresent())
			return oa;
		AudioStudio a = create(o.getName(), o, createdBy);
		return Optional.of(a);
	}

	@Transactional
	public <R extends TranslationRecord> Optional<AudioStudio> findOrCreate(R o, User sessionUser) {

		if (o instanceof ArtExhibitionGuideRecord) {
			Optional<AudioStudio> oa = findByArtExhibitionGuideRecord((ArtExhibitionGuideRecord) o);
			if (oa.isPresent())
				return oa;
			AudioStudio a = create(o.getName(), (ArtExhibitionGuideRecord) o, sessionUser);
			return Optional.of(a);
		}

		if (o instanceof GuideContentRecord) {
			Optional<AudioStudio> oa = findByGuideContentRecord((GuideContentRecord) o);
			if (oa.isPresent())
				return oa;
			AudioStudio a = create(o.getName(), (GuideContentRecord) o, sessionUser);
			return Optional.of(a);
		}

		logger.error("can not create  " + AudioStudio.class.getSimpleName() + " for class " + o.getClass().getSimpleName());
		return Optional.empty();
	}

	@Transactional
	public Optional<AudioStudio> findByArtExhibitionGuide(ArtExhibitionGuide o) {
		Check.requireNonNullArgument(o, "ArtExhibitionGuide is null");
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<AudioStudio> cq = cb.createQuery(AudioStudio.class);
		Root<AudioStudio> root = cq.from(AudioStudio.class);
		cq.select(root).where(cb.equal(root.get("artExhibitionGuide").get("id"), o.getId().toString()));
		cq.orderBy(cb.asc(root.get("id")));
		List<AudioStudio> list = this.getEntityManager().createQuery(cq).getResultList();
		return list == null || list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));

	}

	@Transactional
	public Optional<AudioStudio> findByArtExhibitionGuideRecord(ArtExhibitionGuideRecord o) {
		Check.requireNonNullArgument(o, "ArtExhibitionGuideRecord is null");
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<AudioStudio> cq = cb.createQuery(AudioStudio.class);
		Root<AudioStudio> root = cq.from(AudioStudio.class);
		cq.select(root).where(cb.equal(root.get("artExhibitionGuideRecord").get("id"), o.getId().toString()));
		cq.orderBy(cb.asc(root.get("id")));
		List<AudioStudio> list = this.getEntityManager().createQuery(cq).getResultList();
		return list == null || list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
	}

	@Transactional
	public Optional<AudioStudio> findByGuideContent(GuideContent o) {
		Check.requireNonNullArgument(o, "GuideContent is null");
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<AudioStudio> cq = cb.createQuery(AudioStudio.class);
		Root<AudioStudio> root = cq.from(AudioStudio.class);
		cq.select(root).where(cb.equal(root.get("guideContent").get("id"), o.getId().toString()));
		cq.orderBy(cb.asc(root.get("id")));

		List<AudioStudio> list = this.getEntityManager().createQuery(cq).getResultList();
		return list == null || list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
	}

	@Transactional
	public Optional<AudioStudio> findByGuideContentRecord(GuideContentRecord o) {
		Check.requireNonNullArgument(o, "GuideContentRecord is null");
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<AudioStudio> cq = cb.createQuery(AudioStudio.class);
		Root<AudioStudio> root = cq.from(AudioStudio.class);
		cq.select(root).where(cb.equal(root.get("guideContentRecord").get("id"), o.getId().toString()));
		cq.orderBy(cb.asc(root.get("id")));
		List<AudioStudio> list = this.getEntityManager().createQuery(cq).getResultList();
		return list == null || list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
	}

	@Transactional
	public void delete(AudioStudio o, User deletedBy) {
		deleteResources(o.getId());
		super.deleteById(o.getId());
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(o, deletedBy, AuditAction.CREATE));
	}

	@Transactional
	public void reloadIfDetached(AudioStudio src) {
		if (!getEntityManager().contains(src)) {
			src = findById(src.getId()).get();
		}
	}

	@Transactional
	public Optional<AudioStudio> findWithDeps(Long id) {

		Optional<AudioStudio> o_aw = super.findById(id);

		if (o_aw.isEmpty())
			return o_aw;

		AudioStudio aw = o_aw.get();

		User u = aw.getLastModifiedUser();

		User user = aw.getLastModifiedUser();
		if (user!=null)
			aw.setLastModifiedUser(getUserDBService().findById(user.getId()).get());

		Resource audio = aw.getAudioSpeech();
		if (audio != null) 
			aw.setAudioSpeech(getResourceDBService().findById(audio.getId()).get());
			
		Resource s_audio = aw.getAudioSpeechMusic();
		if (s_audio != null) 
			aw.setAudioSpeechMusic(getResourceDBService().findById(s_audio.getId()).get());
		
		GuideContent gc = aw.getGuideContent();
		if (gc!=null) {
			aw.setGuideContent( getGuideContentDBService().findById(gc.getId()).get() );
		}
		
		ArtExhibitionGuide ae = aw.getArtExhibitionGuide();
		if (ae!=null) {
			aw.setArtExhibitionGuide( getArtExhibitionGuideDBService().findById(ae.getId()).get() );
		}
		
		GuideContentRecord gc_r = aw.getGuideContentRecord();
		if ( gc_r!=null) {
			aw.setGuideContentRecord( getGuideContentRecordDBService().findById(gc_r.getId()).get() );

		}
		
		ArtExhibitionGuideRecord aeg_r = aw.getArtExhibitionGuideRecord();
		if (aeg_r !=null) {
			aw.setArtExhibitionGuideRecord( getArtExhibitionGuideRecordDBService().findById(aeg_r .getId()).get() );

		}
		
		 
		
		
		aw.setDependencies(true);

		return o_aw;
	}
	
	
	@Transactional
	public Optional<Site> getSite(AudioStudio audioStudio) {
		
		Check.requireNonNullArgument(audioStudio, "audioStudio is null");
		
		if (!audioStudio.isDependencies())
			audioStudio = this.findWithDeps(audioStudio.getId()).get();
		
		if (audioStudio.getArtExhibitionGuide()!=null) {
			ArtExhibitionGuide g = audioStudio.getArtExhibitionGuide();
			g=getArtExhibitionGuideDBService().findWithDeps(g.getId()).get();
			Site s=g.getArtExhibition().getSite();
			return getSiteDBService().findById(s.getId());
		}

		if (audioStudio.getGuideContent()!=null) {
			GuideContent g = audioStudio.getGuideContent();
			g=getGuideContentDBService().findWithDeps(g.getId()).get();
			ArtExhibition a = g.getArtExhibitionGuide().getArtExhibition();
			return getSiteDBService().findById(a.getSite().getId());
		}
		
		return Optional.empty();
	}

	@Transactional
	public Optional<AudioStudioParentObject> findParentObjectWithDeps(AudioStudio audioStudio) {

		AudioStudioParentObject b = audioStudio.getParentOject();

		if (b instanceof ArtExhibitionGuide) {
			ArtExhibitionGuide guide = getArtExhibitionGuideDBService().findWithDeps(((ArtExhibitionGuide) audioStudio.getParentOject()).getId()).get();
			return Optional.of((AudioStudioParentObject) guide);
		}

		if (b instanceof GuideContent) {
			GuideContent content = getGuideContentDBService().findWithDeps(((GuideContent) audioStudio.getParentOject()).getId()).get();
			return Optional.of((AudioStudioParentObject) content);
		}

		if (b instanceof ArtExhibitionGuideRecord) {
			ArtExhibitionGuideRecord guide = getArtExhibitionGuideRecordDBService().findWithDeps(((ArtExhibitionGuideRecord) audioStudio.getParentOject()).getId()).get();
			return Optional.of((AudioStudioParentObject) guide);
		}

		if (b instanceof GuideContentRecord) {
			GuideContentRecord content = getGuideContentRecordDBService().findWithDeps(((GuideContentRecord) audioStudio.getParentOject()).getId()).get();
			return Optional.of((AudioStudioParentObject) content);
		}
		return Optional.empty();
	}

	@Transactional
	@Override
	public Iterable<AudioStudio> findAllSorted() {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<AudioStudio> cq = cb.createQuery(getEntityClass());
		Root<AudioStudio> root = cq.from(getEntityClass());
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));
		return getEntityManager().createQuery(cq).getResultList();
	}

	/**
	 * @param name
	 * @return
	 */
	@Transactional
	public List<AudioStudio> getByName(String name) {
		return createNameQuery(name).getResultList();
	}

	@Override
	protected Class<AudioStudio> getEntityClass() {
		return AudioStudio.class;
	}

	@Override
	public String getObjectClassName() {
		return AudioStudio.class.getSimpleName().toLowerCase();
	}

	public boolean isDetached(AudioStudio entity) {
		return !getEntityManager().contains(entity);
	}

	public LanguageService getLanguageService() {
		return this.languageService;
	}

	@PostConstruct
	protected void onInitialize() {
		super.register(getEntityClass(), this);
	}

	@Transactional
	private void deleteResources(Long id) {

		Optional<AudioStudio> o_aw = super.findWithDeps(id);

		if (o_aw.isEmpty())
			return;

		AudioStudio a = o_aw.get();

		getResourceDBService().delete(a.getAudioSpeech());
		getResourceDBService().delete(a.getAudioSpeechMusic());
	}


}
