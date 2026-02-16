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
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtExhibitionSection;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.AuditAction;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionGuideRecord;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionRecord;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionSectionRecord;
import dellemuse.serverapp.serverdb.model.record.ArtWorkRecord;
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
public class ArtWorkRecordDBService extends RecordDBService<ArtWorkRecord, Long> {

	static private Logger logger = Logger.getLogger(ArtWorkRecordDBService.class.getName());

	public ArtWorkRecordDBService(CrudRepository<ArtWorkRecord, Long> repository, ServerDBSettings settings) {
		super(repository, settings);
	}
	

	@Override
	@Transactional
	public Optional<ArtWorkRecord> findByParentObject(MultiLanguageObject o, String lang) {
		return findByArtWork ((ArtWork) o, lang);
	}
	/**
	 * @param name
	 * @param site
	 * @param createdBy
	 * @return
	 */
	@Transactional
	public ArtWorkRecord create(String name, Site site, User createdBy) {
		ArtWorkRecord c = new ArtWorkRecord();
		
		c.setName(name);
	 	c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
		c.setState(ObjectState.EDITION);

		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy,  AuditAction.CREATE));
		
		return c;
	}

	
	@Transactional
	public ArtWorkRecord create(ArtWork a, String lang, User createdBy) {

		ArtWorkRecord c = new ArtWorkRecord();

		c.setArtWork(a);
		c.setName(a.getName());
	 
		c.setLanguage(lang);
		
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
		c.setState(a.getState());

		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy,  AuditAction.CREATE));
		
		return c;
	}

	/**
	 * 
	 * 
	 * @param a
	 * @param lang
	 * @return
	 */
	@Transactional
	public Optional<ArtWorkRecord> findByArtWork(ArtWork a, String lang) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArtWorkRecord> cq = cb.createQuery(ArtWorkRecord.class);
		Root<ArtWorkRecord> root = cq.from(ArtWorkRecord.class);
		
	     Predicate p1 = cb.equal(root.get("artWork").get("id"), a.getId() );
	     Predicate p2 = cb.equal(root.get("language"), lang );

	     Predicate combinedPredicate = cb.and(p1, p2);
	     
	     cq.select(root).where(combinedPredicate);
	
		List<ArtWorkRecord> list = this.getEntityManager().createQuery(cq).getResultList();
		return list == null || list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
		
	}

	@Transactional
	public List<ArtWorkRecord> findAllByGuideContent(ArtWork  a) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArtWorkRecord> cq = cb.createQuery(ArtWorkRecord.class);
		Root<ArtWorkRecord> root = cq.from(ArtWorkRecord.class);
		
	     Predicate p1 = cb.equal(root.get("artWork").get("id"), a.getId() );
	     cq.select(root).where(p1);
	
		List<ArtWorkRecord> list = this.getEntityManager().createQuery(cq).getResultList();

		if (list==null)
			return new ArrayList<ArtWorkRecord>();
		
		return list;
	}

	
	@Transactional
	public void save(ArtWorkRecord o, User user, List<String> updatedParts) {
		super.save(o);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(o, user, AuditAction.UPDATE, String.join(", ", updatedParts)));
	}

	
	 
	
	 
	
	@Transactional
	public Optional<ArtWorkRecord> findWithDeps(Long id) {

		Optional<ArtWorkRecord> o_aw = super.findById(id);

		if (o_aw.isEmpty())
			return  o_aw;
		
		ArtWorkRecord aw = o_aw.get();
		 
		Resource photo = aw.getPhoto();
		if (photo!=null)
			aw.setPhoto(getResourceDBService().findById(photo.getId()).get());
		
		Resource audio = aw.getAudio();
		if (audio!=null)
			aw.setAudio(getResourceDBService().findById(audio.getId()).get());

		User user = aw.getLastModifiedUser();
		if (user!=null)
			aw.setLastModifiedUser(getUserDBService().findById(user.getId()).get());
		
		if (aw.getParentObject()!=null) {
			ArtWork c = (ArtWork) aw.getParentObject();
			aw.setArtWork( getArtWorkDBService().findById(c.getId()).get());
		}
		
		aw.setDependencies(true);

		return o_aw;
	}
	


	@Transactional
    @Override
    public Iterable<ArtWorkRecord> findAllSorted() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ArtWorkRecord> cq = cb.createQuery(getEntityClass());
        Root<ArtWorkRecord> root = cq.from(getEntityClass());
        cq.orderBy(cb.asc( cb.lower(root.get("name"))));
        return getEntityManager().createQuery(cq).getResultList();
    }
    

	public boolean isDetached(ArtWorkRecord entity) {
		return !getEntityManager().contains(entity);
	}
	
	@Transactional
	public void reloadIfDetached(ArtWorkRecord src) {
		if (!getEntityManager().contains(src)) {
			src = findById(src.getId()).get();
		}
	}

	@Transactional
	public List<ArtWorkRecord> getByName(String name) {
		return createNameQuery(name).getResultList();
	}

	@Override
	protected Class<ArtWorkRecord> getEntityClass() {
		return ArtWorkRecord.class;
	}
	
	@PostConstruct
	protected void onInitialize() {
		super.register(getEntityClass(), this);
	}

	@Transactional
	private void deleteResources(Long id) {
		
		Optional<ArtWorkRecord> o_aw = super.findWithDeps(id);

		if (o_aw.isEmpty())
			return;
		
		ArtWorkRecord a=o_aw.get();
		
		getResourceDBService().delete(a.getPhoto());
		getResourceDBService().delete(a.getAudio());
		getResourceDBService().delete(a.getVideo());
		
	}
	



}
