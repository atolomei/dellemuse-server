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
import dellemuse.serverapp.serverdb.model.Music;
import dellemuse.serverapp.serverdb.model.ObjectState;
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
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class MusicDBService extends DBService<Music, Long> {

	static private Logger logger = Logger.getLogger(MusicDBService.class.getName());

	@JsonIgnore
	final LanguageService languageService;

	public MusicDBService(CrudRepository<Music, Long> repository, ServerDBSettings settings, LanguageService languageService) {
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
	public Music  create(String name,   User createdBy) {
		Music c = new Music();
		c.setName(name);
		 
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
		
		c.setState(ObjectState.EDITION);
		
		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy, AuditAction.CREATE));

		return c;
	}
	
	@Transactional
	public void save(Music o, User user, List<String> updatedParts) {
		super.save(o);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(o, user, AuditAction.UPDATE, String.join(", ", updatedParts)));
	}
	
	
	@Transactional
	public void save(Music o, User user, String msg) {
		super.save(o);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(o, user, AuditAction.UPDATE, msg));
	}
	
	@Transactional
	public void delete(Music o, User deletedBy) {
		deleteResources(o.getId());
		super.deleteById(o.getId());
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(o, deletedBy, AuditAction.CREATE));
	}

	@Transactional
	public Optional<Music> findWithDeps(Long id) {

		Optional<Music> o_aw = super.findById(id);

		if (o_aw.isEmpty())
			return o_aw;

		Music aw = o_aw.get();

		User u = aw.getLastModifiedUser();

		if (u != null)
			aw.setLastModifiedUser(getUserDBService().findById(u.getId()).get());

		Resource audio = aw.getAudio();

		if (audio != null) 
			aw.setAudio(getResourceDBService().findById(audio.getId()).get());
			
		aw.setDependencies(true);
		return o_aw;
	}
	
	@Transactional
	@Override
	public Iterable<Music> findAllSorted() {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Music> cq = cb.createQuery(getEntityClass());
		Root<Music> root = cq.from(getEntityClass());
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));
		return getEntityManager().createQuery(cq).getResultList();
	}

	
	
	
	/**
	 * @param name
	 * @return
	 */
	@Transactional
	public List<Music> getByName(String name) {
		return createNameQuery(name).getResultList();
	}

	@Override
	protected Class<Music> getEntityClass() {
		return Music.class;
	}

	@Override
	public String getObjectClassName() {
		return Music.class.getSimpleName().toLowerCase();
	}

	public boolean isDetached(Music entity) {
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

		Optional<Music> o_aw = super.findWithDeps(id);

		if (o_aw.isEmpty())
			return;

		Music a = o_aw.get();

		getResourceDBService().delete(a.getAudio());
		 
	}

}
