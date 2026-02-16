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
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Artist;
import dellemuse.serverapp.serverdb.model.AuditAction;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Artist;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.GuideContentRecord;
import dellemuse.serverapp.serverdb.model.record.InstitutionRecord;
import dellemuse.serverapp.serverdb.model.record.ArtistRecord;
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
public class ArtistRecordDBService extends RecordDBService<ArtistRecord, Long> {

	static private Logger logger = Logger.getLogger(ArtistRecordDBService.class.getName());

	public ArtistRecordDBService(CrudRepository<ArtistRecord, Long> repository, ServerDBSettings settings) {
		super(repository, settings);
	}
	
	@Override
	@Transactional
	public Optional<ArtistRecord> findByParentObject(MultiLanguageObject o, String lang) {
		return findByArtist((Artist) o, lang);
	}
	/**
	 * 
	 * 
	 * @param name
	 * @param site
	 * @param createdBy
	 * @return
	 */
	@Transactional
	public ArtistRecord create(String name, Artist site, User createdBy) {
		ArtistRecord c = new ArtistRecord();
		
		c.setName(name);
		 
		c.setArtist(site); 
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
	 	c.setObjectState(ObjectState.EDITION);
		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy,  AuditAction.CREATE));
		
		return c;
	}

	
	@Transactional
	public ArtistRecord create(Artist a, String lang, User createdBy) {

		ArtistRecord c = new ArtistRecord();

		c.setArtist(a);
		c.setName(a.getName());
		 
		c.setLanguage(lang);
		
		c.setObjectState(ObjectState.EDITION);
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
		
		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy,  AuditAction.CREATE));
		
		return c;
	}

	
	@Transactional
	public void restore(ArtistRecord c, User by) {
		 super.restore(c, by);
	}
	
	
	/**
	 * 
	 * @param a
	 * @param lang
	 * @return
	 */
	@Transactional
	public Optional<ArtistRecord> findByArtist(Artist a, String lang) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArtistRecord> cq = cb.createQuery(ArtistRecord.class);
		Root<ArtistRecord> root = cq.from(ArtistRecord.class);
		
	     Predicate p1 = cb.equal(root.get("artist").get("id"), a.getId() );
	     Predicate p2 = cb.equal(root.get("language"), lang );

	     Predicate combinedPredicate = cb.and(p1, p2);
	     
	     cq.select(root).where(combinedPredicate);
	
		List<ArtistRecord> list = this.getEntityManager().createQuery(cq).getResultList();
		return list == null || list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
		
	}

	@Transactional
	public List<ArtistRecord> findAllByGuideContent(Artist  a) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArtistRecord> cq = cb.createQuery(ArtistRecord.class);
		Root<ArtistRecord> root = cq.from(ArtistRecord.class);
		
	     Predicate p1 = cb.equal(root.get("artist").get("id"), a.getId() );
	     cq.select(root).where(p1);
	
		List<ArtistRecord> list = this.getEntityManager().createQuery(cq).getResultList();

		if (list==null)
			return new ArrayList<ArtistRecord>();
		
		return list;
	}	

	@Transactional
	public void save(ArtistRecord o, User user, List<String> updatedParts) {
		super.save(o);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(o, user, AuditAction.UPDATE, String.join(", ", updatedParts)));
	}
 
	
	@Transactional
	public Optional<ArtistRecord> findWithDeps(Long id) {

		Optional<ArtistRecord> o_aw = super.findById(id);

		if (o_aw.isEmpty())
			return  o_aw;
		
		ArtistRecord aw = o_aw.get();

		User u = aw.getLastModifiedUser();
		
		if (u!=null)
			u.getDisplayname();
		
	 	
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
			Artist c = (Artist) aw.getParentObject();
			aw.setArtist( getArtistDBService().findById(c.getId()).get());
		}
		aw.setDependencies(true);

		return o_aw;
	}
	
    @Transactional
    @Override
    public Iterable<ArtistRecord> findAllSorted() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ArtistRecord> cq = cb.createQuery(getEntityClass());
        Root<ArtistRecord> root = cq.from(getEntityClass());
        cq.orderBy(cb.asc( cb.lower(root.get("name"))));
        return getEntityManager().createQuery(cq).getResultList();
    }
    

	public boolean isDetached(ArtistRecord entity) {
		return !getEntityManager().contains(entity);
	}
	
	@Transactional
	public void reloadIfDetached(ArtistRecord src) {
		if (!getEntityManager().contains(src)) {
			src = findById(src.getId()).get();
		}
	}

	@Transactional
	public List<ArtistRecord> getByName(String name) {
		return createNameQuery(name).getResultList();
	}

	@Override
	protected Class<ArtistRecord> getEntityClass() {
		return ArtistRecord.class;
	}
	
	@PostConstruct
	protected void onInitialize() {
		super.register(getEntityClass(), this);
	}


}
