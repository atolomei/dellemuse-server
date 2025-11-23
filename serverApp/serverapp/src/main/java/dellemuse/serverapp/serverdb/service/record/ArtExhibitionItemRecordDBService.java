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
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionGuideRecord;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionItemRecord;
import dellemuse.serverapp.serverdb.service.DBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import jakarta.annotation.PostConstruct;
 
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class ArtExhibitionItemRecordDBService extends DBService<ArtExhibitionItemRecord, Long> {

	static private Logger logger = Logger.getLogger(ArtExhibitionItemRecordDBService.class.getName());

	public ArtExhibitionItemRecordDBService(CrudRepository<ArtExhibitionItemRecord, Long> repository, ServerDBSettings settings) {
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
	public ArtExhibitionItemRecord create(String name, User createdBy) {
		throw new RuntimeException("can not call create without language");
	}
	

	/**
	 * 
	 * 
	 * @param name
	 * @param ArtExhibitionItem
	 * @param createdBy
	 * @return
	 */
	@Transactional
	public ArtExhibitionItemRecord create(String name, ArtExhibitionItem i, User createdBy) {
		ArtExhibitionItemRecord c = new ArtExhibitionItemRecord();
		
		c.setName(name);
		c.setArtExhibitionItem(i); 
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);

		c.setLanguage(i.getLanguage());
		c.setState(ObjectState.EDTION);
		
		return getRepository().save(c);
	}
	
	
	@Transactional
	public ArtExhibitionItemRecord create(ArtExhibitionItem a, String lang, User createdBy) {

		ArtExhibitionItemRecord c = new ArtExhibitionItemRecord();

		c.setArtExhibitionItem(a);
		c.setName(a.getName());
		c.setLanguage(lang);
		
		c.setState(ObjectState.EDTION);
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
 	
		return getRepository().save(c);
	}

	@Transactional
	public void markAsDeleted(ArtExhibitionItemRecord  c, User deletedBy) {
		super.markAsDeleted(c, deletedBy);
	}
	
	@Transactional
	public void restore(ArtExhibitionItemRecord  c, User deletedBy) {
		super.restore(c, deletedBy);
	}

	/**
	 * 
	 * @param a
	 * @param lang
	 * @return
	 */
	@Transactional
	public Optional<ArtExhibitionItemRecord> findByArtExhibitionItem(ArtExhibitionItem a, String lang) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArtExhibitionItemRecord> cq = cb.createQuery(ArtExhibitionItemRecord.class);
		Root<ArtExhibitionItemRecord> root = cq.from(ArtExhibitionItemRecord.class);
		
	     Predicate p1 = cb.equal(root.get("artExhibitionItem").get("id"), a.getId() );
	     Predicate p2 = cb.equal(root.get("language"), lang );

	     Predicate combinedPredicate = cb.and(p1, p2);
	     
	     cq.select(root).where(combinedPredicate);
	
		List<ArtExhibitionItemRecord> list = this.getEntityManager().createQuery(cq).getResultList();
		return list == null || list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
		
	}

	@Transactional
	public List<ArtExhibitionItemRecord> findAllByArtExhibitionItem(ArtExhibitionItem  a) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArtExhibitionItemRecord> cq = cb.createQuery(ArtExhibitionItemRecord.class);
		Root<ArtExhibitionItemRecord> root = cq.from(ArtExhibitionItemRecord.class);
		
	     Predicate p1 = cb.equal(root.get("artExhibitionItem").get("id"), a.getId() );
	     cq.select(root).where(p1);
	
		List<ArtExhibitionItemRecord> list = this.getEntityManager().createQuery(cq).getResultList();

		if (list==null)
			return new ArrayList<ArtExhibitionItemRecord>();
		
		return list;
	}


	 
	@Transactional
	public void delete(Long id) {
		deleteResources(id);
		super.deleteById(id);
	}

	@Transactional
	public void delete(ArtExhibitionItemRecord o) {
		this.delete(o.getId()); 
	}
	
	
	@Transactional
	public Optional<ArtExhibitionItemRecord> findWithDeps(Long id) {

		Optional<ArtExhibitionItemRecord> o_aw = super.findById(id);

		if (o_aw.isEmpty())
			return  o_aw;
		
		ArtExhibitionItemRecord aw = o_aw.get();
		 
		Resource photo = aw.getPhoto();

		if (photo != null)
			photo.getBucketName();
		
		aw.setDependencies(true);

		return o_aw;
	}
	
    @Transactional
    @Override
    public Iterable<ArtExhibitionItemRecord> findAllSorted() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ArtExhibitionItemRecord> cq = cb.createQuery(getEntityClass());
        Root<ArtExhibitionItemRecord> root = cq.from(getEntityClass());
        cq.orderBy(cb.asc( cb.lower(root.get("name"))));
        return getEntityManager().createQuery(cq).getResultList();
    }
   
	public boolean isDetached(ArtExhibitionItemRecord entity) {
		return !getEntityManager().contains(entity);
	}
	
	@Transactional
	public void reloadIfDetached(ArtExhibitionItemRecord src) {
		if (!getEntityManager().contains(src)) {
			src = findById(src.getId()).get();
		}
	}

	@Transactional
	public List<ArtExhibitionItemRecord> getByName(String name) {
		return createNameQuery(name).getResultList();
	}

	@Override
	protected Class<ArtExhibitionItemRecord> getEntityClass() {
		return ArtExhibitionItemRecord.class;
	}
	
	@PostConstruct
	protected void onInitialize() {
		super.register(getEntityClass(), this);
	}

	
	
	
	@Transactional
	private void deleteResources(Long id) {
		
		Optional<ArtExhibitionItemRecord> o_aw = super.findWithDeps(id);

		if (o_aw.isEmpty())
			return;
		
		ArtExhibitionItemRecord a=o_aw.get();
		
		getResourceDBService().delete(a.getPhoto());
		getResourceDBService().delete(a.getAudio());
		getResourceDBService().delete(a.getVideo());
		
	}
}
