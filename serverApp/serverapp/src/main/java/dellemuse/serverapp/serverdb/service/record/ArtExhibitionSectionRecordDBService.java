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
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.ArtExhibitionSection;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.AuditAction;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;

import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionItemRecord;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionRecord;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionSectionRecord;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionSectionRecord;
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
public class ArtExhibitionSectionRecordDBService extends RecordDBService<ArtExhibitionSectionRecord, Long> {

	static private Logger logger = Logger.getLogger(ArtExhibitionSectionRecordDBService.class.getName());

	
	@Override
	@Transactional
	public Optional<ArtExhibitionSectionRecord> findByParentObject(MultiLanguageObject o, String lang) {
		//return findByArtExhibitionSection((ArtExhibitionSection) o, lang);
	
		throw new RuntimeException ("not done");
	}

	public ArtExhibitionSectionRecordDBService(CrudRepository<ArtExhibitionSectionRecord, Long> repository, ServerDBSettings settings) {
		super(repository, settings);
	}

	@Transactional
	public ArtExhibitionSectionRecord create(ArtExhibitionSection a, String lang, User createdBy) {

		ArtExhibitionSectionRecord c = new ArtExhibitionSectionRecord();

		c.setArtExhibitionSection(a);
		c.setName(a.getName());
	 
		c.setLanguage(lang);

		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);

		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy, AuditAction.CREATE));

		return c;
	}

	/**
	 * 
	 * 
	 * @param name
	 * @param Role
	 * @param createdBy
	 * @return
	 */
	@Transactional
	public ArtExhibitionSectionRecord create(String name, ArtExhibitionSection artExhibitionSection, User createdBy) {

		ArtExhibitionSectionRecord c = new ArtExhibitionSectionRecord();

		c.setName(name);
		c.setArtExhibitionSection(artExhibitionSection);
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);

		getRepository().save(c);
		getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy, AuditAction.CREATE));

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
	public Optional<ArtExhibitionSectionRecord> findByArtExhibition(ArtExhibition a, String lang) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArtExhibitionSectionRecord> cq = cb.createQuery(ArtExhibitionSectionRecord.class);
		Root<ArtExhibitionSectionRecord> root = cq.from(ArtExhibitionSectionRecord.class);

		Predicate p1 = cb.equal(root.get("artExhibition").get("id"), a.getId());
		Predicate p2 = cb.equal(root.get("language"), lang);

		Predicate combinedPredicate = cb.and(p1, p2);

		cq.select(root).where(combinedPredicate);

		List<ArtExhibitionSectionRecord> list = this.getEntityManager().createQuery(cq).getResultList();
		return list == null || list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));

	}

	@Transactional
	public List<ArtExhibitionSectionRecord> findAllByGuideContent(ArtExhibitionSection a) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArtExhibitionSectionRecord> cq = cb.createQuery(ArtExhibitionSectionRecord.class);
		Root<ArtExhibitionSectionRecord> root = cq.from(ArtExhibitionSectionRecord.class);

		Predicate p1 = cb.equal(root.get("artExhibitionSection").get("id"), a.getId());
		cq.select(root).where(p1);

		List<ArtExhibitionSectionRecord> list = this.getEntityManager().createQuery(cq).getResultList();

		if (list == null)
			return new ArrayList<ArtExhibitionSectionRecord>();

		return list;
	}



	@Transactional
	private void deleteResources(Long id) {

		Optional<ArtExhibitionSectionRecord> o_aw = super.findWithDeps(id);

		if (o_aw.isEmpty())
			return;

		ArtExhibitionSectionRecord a = o_aw.get();

		getResourceDBService().delete(a.getPhoto());
		getResourceDBService().delete(a.getAudio());
		getResourceDBService().delete(a.getVideo());

	}

	/**@Transactional
	public void delete(Long id) {
		deleteResources(id);
		super.deleteById(id);
	}

	@Transactional
	public void delete(ArtExhibitionSectionRecord o) {
		this.delete(o.getId());
	}
	**/
	

	@Transactional
	public Optional<ArtExhibitionSectionRecord> findWithDeps(Long id) {

		Optional<ArtExhibitionSectionRecord> o_aw = super.findById(id);

		if (o_aw.isEmpty())
			return o_aw;

		ArtExhibitionSectionRecord aw = o_aw.get();

		Resource photo = aw.getPhoto();

		if (photo != null)
			photo.getBucketName();

		aw.setDependencies(true);

		return o_aw;
	}

	@Transactional
	@Override
	public Iterable<ArtExhibitionSectionRecord> findAllSorted() {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArtExhibitionSectionRecord> cq = cb.createQuery(getEntityClass());
		Root<ArtExhibitionSectionRecord> root = cq.from(getEntityClass());
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));
		return getEntityManager().createQuery(cq).getResultList();
	}

	public boolean isDetached(ArtExhibitionSectionRecord entity) {
		return !getEntityManager().contains(entity);
	}

	@Transactional
	public void reloadIfDetached(ArtExhibitionSectionRecord src) {
		if (!getEntityManager().contains(src)) {
			src = findById(src.getId()).get();
		}
	}

	@Transactional
	public List<ArtExhibitionSectionRecord> getByName(String name) {
		return createNameQuery(name).getResultList();
	}

	@Override
	protected Class<ArtExhibitionSectionRecord> getEntityClass() {
		return ArtExhibitionSectionRecord.class;
	}

	@PostConstruct
	protected void onInitialize() {
		super.register(getEntityClass(), this);
	}

}
