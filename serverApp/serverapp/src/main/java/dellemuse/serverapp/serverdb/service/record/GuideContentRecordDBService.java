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
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.GuideContentRecord;
import dellemuse.serverapp.serverdb.service.DBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import jakarta.annotation.PostConstruct;
 
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class GuideContentRecordDBService extends DBService<GuideContentRecord, Long> {

	static private Logger logger = Logger.getLogger(GuideContentRecordDBService.class.getName());

	public GuideContentRecordDBService(CrudRepository<GuideContentRecord, Long> repository, ServerDBSettings settings) {
		super(repository, settings);
	}
	
	
	@Transactional
	public void markAsDeleted(GuideContentRecord c, User deletedBy) {
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(deletedBy);
		c.setState(ObjectState.DELETED);
		getRepository().save(c);		
	}
	
	@Transactional
	public void restore(GuideContentRecord c, User by) {
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(by);
		c.setState(ObjectState.EDITION);
		getRepository().save(c);		
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
	public GuideContentRecord create(String name, User createdBy) {
		
		//GuideContentRecord c = new GuideContentRecord();
		//c.setName(name);
		
		/**
		c.setLanguage(Language.EN);
		
		c.setNameKey(nameKey(name));
		c.setCreated(OffsetDateTime.now());
		c.setUsethumbnail(true);
	
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
		
		logger.debug("Creating GuideContentRecord -> " + c.getName()+" | " + c.getLanguage());

		
		return getRepository().save(c);
	*/
		
		throw new RuntimeException("can not call create without language");
	}
	
	@Transactional
	public GuideContentRecord create(GuideContent a, String lang, User createdBy) {

		GuideContentRecord c = new GuideContentRecord();

		c.setGuideContent(a);
		c.setName(a.getName());
		c.setUsethumbnail(c.isUsethumbnail());
		c.setLanguage(lang);
		
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
		
		logger.debug("Creating GuideContentRecord -> " + c.getName()+" | " + c.getLanguage());

		
		return getRepository().save(c);
	}

	/**
	 * 
	 * @param a
	 * @param lang
	 * @return
	 */
	@Transactional
	public Optional<GuideContentRecord> findByGuideContent(GuideContent a, String lang) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<GuideContentRecord> cq = cb.createQuery(GuideContentRecord.class);
		Root<GuideContentRecord> root = cq.from(GuideContentRecord.class);
		
	     Predicate p1 = cb.equal(root.get("guideContent").get("id"), a.getId() );
	     Predicate p2 = cb.equal(root.get("language"), lang );

	     Predicate combinedPredicate = cb.and(p1, p2);
	     
	     cq.select(root).where(combinedPredicate);
	
		List<GuideContentRecord> list = this.getEntityManager().createQuery(cq).getResultList();
		return list == null || list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
		
	}


	/**
	 * 
	 * @param a
	 * @param lang
	 * @return
	 */
	@Transactional
	public List<GuideContentRecord> findAllByGuideContent(GuideContent a) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<GuideContentRecord> cq = cb.createQuery(GuideContentRecord.class);
		Root<GuideContentRecord> root = cq.from(GuideContentRecord.class);
		
	     Predicate p1 = cb.equal(root.get("guideContent").get("id"), a.getId() );
	     cq.select(root).where(p1);
	
		List<GuideContentRecord> list = this.getEntityManager().createQuery(cq).getResultList();

		if (list==null)
			return new ArrayList<GuideContentRecord>();
		
		return list;
	}

 
	/**
	 * 
	 * 
	 * @param name
	 * @param GuideContent
	 * @param createdBy
	 * @return
	 */
	@Transactional
	public GuideContentRecord create(String name, GuideContent GuideContent, User createdBy) {
		GuideContentRecord c = new GuideContentRecord();
		
		c.setName(name);
		//c.setNameKey(nameKey(name));
		c.setGuideContent(GuideContent); 
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
		c.setUsethumbnail(true);
		
		return getRepository().save(c);
	}

 	@Transactional
	private void deleteResources(Long id) {
		
		Optional<GuideContentRecord> o_aw = super.findWithDeps(id);

		if (o_aw.isEmpty())
			return;
		
		GuideContentRecord a=o_aw.get();
		
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
	public void delete(GuideContentRecord o) {
		this.delete(o.getId()); 
	}
	
	
	@Transactional
	public Optional<GuideContentRecord> findWithDeps(Long id) {

		Optional<GuideContentRecord> o_aw = super.findById(id);

		if (o_aw.isEmpty())
			return  o_aw;
		
		GuideContentRecord aw = o_aw.get();
		 
		Resource photo = aw.getPhoto();

//		User u = aw.getLastModifiedUser();
		
//		if (u!=null)
//			u.getDisplayname();
		
		if (photo != null)
			photo.getBucketName();
		
		aw.setDependencies(true);

		return o_aw;
	}
	
    @Transactional
    @Override
    public Iterable<GuideContentRecord> findAllSorted() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<GuideContentRecord> cq = cb.createQuery(getEntityClass());
        Root<GuideContentRecord> root = cq.from(getEntityClass());
        cq.orderBy(cb.asc( cb.lower(root.get("name"))));
        return getEntityManager().createQuery(cq).getResultList();
    }
    

	public boolean isDetached(GuideContentRecord entity) {
		return !getEntityManager().contains(entity);
	}
	
	@Transactional
	public void reloadIfDetached(GuideContentRecord src) {
		if (!getEntityManager().contains(src)) {
			src = findById(src.getId()).get();
		}
	}

	@Transactional
	public List<GuideContentRecord> getByName(String name) {
		return createNameQuery(name).getResultList();
	}

	@Override
	protected Class<GuideContentRecord> getEntityClass() {
		return GuideContentRecord.class;
	}
	
	@PostConstruct
	protected void onInitialize() {
		super.register(getEntityClass(), this);
	}

	 


}
