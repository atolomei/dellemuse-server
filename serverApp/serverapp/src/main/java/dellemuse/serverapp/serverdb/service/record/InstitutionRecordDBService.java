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
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.ArtWorkRecord;
import dellemuse.serverapp.serverdb.model.record.InstitutionRecord;
import dellemuse.serverapp.serverdb.service.DBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import jakarta.annotation.PostConstruct;
 
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class InstitutionRecordDBService extends DBService<InstitutionRecord, Long> {

	static private Logger logger = Logger.getLogger(InstitutionRecordDBService.class.getName());

	public InstitutionRecordDBService(CrudRepository<InstitutionRecord, Long> repository, ServerDBSettings settings) {
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
	public InstitutionRecord create(String name, User createdBy) {
		throw new RuntimeException("can not call create without language");
	}
	
	@Transactional
	public InstitutionRecord create(Institution a, String lang, User createdBy) {

		InstitutionRecord c = new InstitutionRecord();

		c.setInstitution(a);
		c.setName(a.getName());
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
	public Optional<InstitutionRecord> findByInstitution(Institution a, String lang) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<InstitutionRecord> cq = cb.createQuery(InstitutionRecord.class);
		Root<InstitutionRecord> root = cq.from(InstitutionRecord.class);
		
	     Predicate p1 = cb.equal(root.get("institution").get("id"), a.getId() );
	     Predicate p2 = cb.equal(root.get("language"), lang );

	     Predicate combinedPredicate = cb.and(p1, p2);
	     
	     cq.select(root).where(combinedPredicate);
	
		List<InstitutionRecord> list = this.getEntityManager().createQuery(cq).getResultList();
		return list == null || list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
		
	}
	
	@Transactional
	public List<InstitutionRecord> findAllByGuideContent(Institution  a) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<InstitutionRecord> cq = cb.createQuery(InstitutionRecord.class);
		Root<InstitutionRecord> root = cq.from(InstitutionRecord.class);
		
	     Predicate p1 = cb.equal(root.get("institution").get("id"), a.getId() );
	     cq.select(root).where(p1);
	
		List<InstitutionRecord> list = this.getEntityManager().createQuery(cq).getResultList();

		if (list==null)
			return new ArrayList<InstitutionRecord>();
		
		return list;
	}

	@Transactional
	private void deleteResources(Long id) {
		
		Optional<InstitutionRecord> o_aw = super.findWithDeps(id);

		if (o_aw.isEmpty())
			return;
		
		InstitutionRecord a=o_aw.get();
		
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
	public void delete(InstitutionRecord o) {
		this.delete(o.getId()); 
	}
	
 	@Transactional
	public Optional<InstitutionRecord> findWithDeps(Long id) {

		Optional<InstitutionRecord> o_aw = super.findById(id);

		if (o_aw.isEmpty())
			return  o_aw;
		
		InstitutionRecord aw = o_aw.get();
		 
		Resource photo = aw.getPhoto();

		//User u = aw.getLastModifiedUser();
		
		//if (u!=null)
		//	u.getDisplayname();
		
		if (photo != null)
			photo.getBucketName();
		
		aw.setDependencies(true);

		return o_aw;
	}
	
	@Override
	protected Class<InstitutionRecord> getEntityClass() {
		return InstitutionRecord.class;
	}
	
	@PostConstruct
	protected void onInitialize() {
		super.register(getEntityClass(), this);
	}

	 


}
