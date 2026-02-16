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
import dellemuse.serverapp.audit.AuditKey;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.AudioStudio;
import dellemuse.serverapp.serverdb.model.AuditAction;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.SiteType;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionGuideRecord;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionRecord;
import dellemuse.serverapp.serverdb.model.record.GuideContentRecord;
import dellemuse.serverapp.serverdb.service.DBService;
import dellemuse.serverapp.serverdb.service.RecordDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import jakarta.annotation.PostConstruct;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class ArtExhibitionGuideRecordDBService extends RecordDBService<ArtExhibitionGuideRecord, Long> {

	static private Logger logger = Logger.getLogger(ArtExhibitionGuideRecordDBService.class.getName());

	public ArtExhibitionGuideRecordDBService(CrudRepository<ArtExhibitionGuideRecord, Long> repository, ServerDBSettings settings) {
		super(repository, settings);
	}

	/**
	 * 
	 * 
	 * @param name
	 * @param ArtExhibitionGuide
	 * @param createdBy
	 * @return
	 */
	@Transactional
	public ArtExhibitionGuideRecord create(String name, ArtExhibitionGuide ArtExhibitionGuide, User createdBy) {
		ArtExhibitionGuideRecord c = new ArtExhibitionGuideRecord();

		c.setName(name);
		c.setArtExhibitionGuide(ArtExhibitionGuide);
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
		c.setState(ObjectState.EDITION);
		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy, AuditAction.UPDATE));

		return c;
	}
	
	@Override
	@Transactional
	public Optional<ArtExhibitionGuideRecord> findByParentObject(MultiLanguageObject o, String lang) {
		return findByArtExhibitionGuide((ArtExhibitionGuide) o, lang);
	}
	
	
	@Transactional
	public ArtExhibitionGuideRecord create(ArtExhibitionGuide a, String lang, User createdBy) {

		ArtExhibitionGuideRecord c = new ArtExhibitionGuideRecord();

		c.setArtExhibitionGuide(a);
		c.setName(a.getName());
	 

		c.setLanguage(lang);
		c.setState(a.getState());

		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);

		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy, AuditAction.CREATE));
		
		return c;
	}

	@Transactional
	public void save(ArtExhibitionGuideRecord o, User user, List<String> updatedParts) {
		super.save(o);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(o, user, AuditAction.UPDATE, String.join(", ", updatedParts)));
		

		Optional<AudioStudio> oa = getAudioStudioDBService().findByArtExhibitionGuideRecord(o);
		
		if (oa.isPresent()) {
			oa.get().setName(o.getName());
			oa.get().setInfo(o.getInfo());
			getAudioStudioDBService().save(oa.get());
		}
	}
	
	
	@Transactional
	public void markAsDeleted(ArtExhibitionGuideRecord c, User deletedBy) {
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(deletedBy);
		c.setState(ObjectState.DELETED);

		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, deletedBy, AuditAction.DELETE, AuditKey.MARK_AS_DELETED));

		Optional<AudioStudio> o = getAudioStudioDBService().findByArtExhibitionGuideRecord(c);
		if (o.isPresent())
			getAudioStudioDBService().markAsDeleted(o.get(), deletedBy);

	}

	@Transactional
	public void restore(ArtExhibitionGuideRecord c, User by) {

		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(by);
		c.setState(ObjectState.EDITION);

		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, by, AuditAction.UPDATE,  AuditKey.RESTORE));

		Optional<AudioStudio> o = getAudioStudioDBService().findByArtExhibitionGuideRecord(c);
		if (o.isPresent())
			getAudioStudioDBService().restore(o.get(), by);
	}

 	 
	
	/**
	 * 
	 * 
	 * @param a
	 * @param lang
	 * @return
	 */
	@Transactional
	public Optional<ArtExhibitionGuideRecord> findByArtExhibitionGuide(ArtExhibitionGuide a, String lang) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArtExhibitionGuideRecord> cq = cb.createQuery(ArtExhibitionGuideRecord.class);
		Root<ArtExhibitionGuideRecord> root = cq.from(ArtExhibitionGuideRecord.class);

		Predicate p1 = cb.equal(root.get("artExhibitionGuide").get("id"), a.getId());
		Predicate p2 = cb.equal(root.get("language"), lang);

		Predicate combinedPredicate = cb.and(p1, p2);

		cq.select(root).where(combinedPredicate);

		List<ArtExhibitionGuideRecord> list = this.getEntityManager().createQuery(cq).getResultList();
		return list == null || list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
	}

	@Transactional
	public List<ArtExhibitionGuideRecord> findAllByArtExhibitionGuide(ArtExhibitionGuide a) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArtExhibitionGuideRecord> cq = cb.createQuery(ArtExhibitionGuideRecord.class);
		Root<ArtExhibitionGuideRecord> root = cq.from(ArtExhibitionGuideRecord.class);

		Predicate p1 = cb.equal(root.get("artExhibitionGuide").get("id"), a.getId());
		cq.select(root).where(p1);

		List<ArtExhibitionGuideRecord> list = this.getEntityManager().createQuery(cq).getResultList();

		if (list == null)
			return new ArrayList<ArtExhibitionGuideRecord>();

		return list;
	}

	

	@Transactional
	private void deleteResources(Long id) {

		Optional<ArtExhibitionGuideRecord> o_aw = super.findWithDeps(id);

		if (o_aw.isEmpty())
			return;

		ArtExhibitionGuideRecord a = o_aw.get();

		getResourceDBService().delete(a.getPhoto());
		getResourceDBService().delete(a.getAudio());
		getResourceDBService().delete(a.getVideo());

	}

	

	@Transactional
	public Optional<ArtExhibitionGuideRecord> findWithDeps(Long id) {

		Optional<ArtExhibitionGuideRecord> o_aw = super.findById(id);

		if (o_aw.isEmpty())
			return o_aw;

		ArtExhibitionGuideRecord aw = o_aw.get();

	 
		o_aw.get().setArtExhibitionGuide(this.getArtExhibitionGuideDBService().findById(o_aw.get().getParentObject().getId()).get());

		Resource photo = aw.getPhoto();
		if (photo!=null)
			aw.setPhoto(getResourceDBService().findById(photo.getId()).get());
		
		Resource audio = aw.getAudio();
		if (audio!=null)
			aw.setAudio(getResourceDBService().findById(audio.getId()).get());

		User user = aw.getLastModifiedUser();
		if (user!=null)
			aw.setLastModifiedUser(getUserDBService().findById(user.getId()).get());
		
		aw.setDependencies(true);

		return o_aw;
	}

	@Transactional
	@Override
	public Iterable<ArtExhibitionGuideRecord> findAllSorted() {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArtExhibitionGuideRecord> cq = cb.createQuery(getEntityClass());
		Root<ArtExhibitionGuideRecord> root = cq.from(getEntityClass());
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));
		return getEntityManager().createQuery(cq).getResultList();
	}

	public boolean isDetached(ArtExhibitionGuideRecord entity) {
		return !getEntityManager().contains(entity);
	}

	@Transactional
	public void reloadIfDetached(ArtExhibitionGuideRecord src) {
		if (!getEntityManager().contains(src)) {
			src = findById(src.getId()).get();
		}
	}

	@Transactional
	public List<ArtExhibitionGuideRecord> getByName(String name) {
		return createNameQuery(name).getResultList();
	}

	@Override
	protected Class<ArtExhibitionGuideRecord> getEntityClass() {
		return ArtExhibitionGuideRecord.class;
	}

	@Override
	public String getObjectClassName() {
		return ArtExhibitionGuideRecord.class.getSimpleName().toLowerCase();
	}

	@PostConstruct
	protected void onInitialize() {
		super.register(getEntityClass(), this);
	}

}
