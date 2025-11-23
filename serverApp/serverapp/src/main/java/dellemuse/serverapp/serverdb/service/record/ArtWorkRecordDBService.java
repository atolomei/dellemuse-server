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
import dellemuse.serverapp.serverdb.model.ArtExhibitionSection;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionSectionRecord;
import dellemuse.serverapp.serverdb.model.record.ArtWorkRecord;
import dellemuse.serverapp.serverdb.service.DBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import jakarta.annotation.PostConstruct;
 
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class ArtWorkRecordDBService extends DBService<ArtWorkRecord, Long> {

	static private Logger logger = Logger.getLogger(ArtWorkRecordDBService.class.getName());

	public ArtWorkRecordDBService(CrudRepository<ArtWorkRecord, Long> repository, ServerDBSettings settings) {
		super(repository, settings);
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
	public ArtWorkRecord create(String name, User createdBy) {
		
		//ArtWorkRecord c = new ArtWorkRecord();
		//c.setName(name);
		
		/**
		c.setLanguage(Language.EN);
		
		c.setNameKey(nameKey(name));
		c.setCreated(OffsetDateTime.now());
		c.setUsethumbnail(true);
	
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
		
		logger.debug("Creating ArtWorkRecord -> " + c.getName()+" | " + c.getLanguage());

		
		return getRepository().save(c);
	*/
		
		throw new RuntimeException("can not call create without language");
	}
	
	@Transactional
	public ArtWorkRecord create(ArtWork a, String lang, User createdBy) {

		ArtWorkRecord c = new ArtWorkRecord();

		c.setArtwork(a);
		c.setName(a.getName());
		c.setUsethumbnail(c.isUsethumbnail());
		c.setLanguage(lang);
		
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
		
		logger.debug("Creating ArtWorkRecord -> " + c.getName()+" | " + c.getLanguage());

		
		return getRepository().save(c);
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
		
	     Predicate p1 = cb.equal(root.get("artwork").get("id"), a.getId() );
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
		//c.setNameKey(nameKey(name));
		 
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
		c.setUsethumbnail(true);
		
		return getRepository().save(c);
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
	 
	@Transactional
	public void delete(Long id) {
		deleteResources(id);
		super.deleteById(id);
	}

	@Transactional
	public void delete(ArtWorkRecord o) {
		this.delete(o.getId()); 
	}
	
	
	@Transactional
	public Optional<ArtWorkRecord> findWithDeps(Long id) {

		Optional<ArtWorkRecord> o_aw = super.findById(id);

		if (o_aw.isEmpty())
			return  o_aw;
		
		ArtWorkRecord aw = o_aw.get();
		 
		Resource photo = aw.getPhoto();

	//	User u = aw.getLastModifiedUser();
	//	if (u!=null)
	//		u.getDisplayname();
		
		if (photo != null)
			photo.getBucketName();
		
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

	 


}
