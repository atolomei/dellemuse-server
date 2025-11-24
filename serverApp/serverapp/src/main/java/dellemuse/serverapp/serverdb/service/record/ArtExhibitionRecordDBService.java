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
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;

import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionItemRecord;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionRecord;
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
public class ArtExhibitionRecordDBService extends DBService<ArtExhibitionRecord, Long> {

	static private Logger logger = Logger.getLogger(ArtExhibitionRecordDBService.class.getName());

	public ArtExhibitionRecordDBService(CrudRepository<ArtExhibitionRecord, Long> repository, ServerDBSettings settings) {
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
	public ArtExhibitionRecord create(String name, User createdBy) {
		throw new RuntimeException("can not call create without language");
	}

	@Transactional
	public ArtExhibitionRecord create(ArtExhibition a, String lang, User createdBy) {

		ArtExhibitionRecord c = new ArtExhibitionRecord();

		c.setArtExhibition(a);
		c.setName(a.getName());
		c.setUsethumbnail(c.isUsethumbnail());
		c.setLanguage(lang);

		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
		c.setState(ObjectState.EDITION);

		return getRepository().save(c);
	}

	/**
	 * 
	 * 
	 * @param name
	 * @param ArtExhibition
	 * @param createdBy
	 * @return
	 * @Transactional public ArtExhibitionRecord create(String name, ArtExhibition
	 *                ArtExhibition, User createdBy) {
	 * 
	 *                ArtExhibitionRecord c = new ArtExhibitionRecord();
	 * 
	 *                c.setName(name);
	 * 
	 *                c.setArtExhibition(ArtExhibition);
	 *                c.setCreated(OffsetDateTime.now());
	 *                c.setLastModified(OffsetDateTime.now());
	 *                c.setLastModifiedUser(createdBy); return
	 *                getRepository().save(c); }
	 */
 
	
	
	/**
	 * 
	 * 
	 * @param a
	 * @param lang
	 * @return
	 */
	@Transactional
	public Optional<ArtExhibitionRecord> findByArtExhibition(ArtExhibition a, String lang) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArtExhibitionRecord> cq = cb.createQuery(ArtExhibitionRecord.class);
		Root<ArtExhibitionRecord> root = cq.from(ArtExhibitionRecord.class);

		Predicate p1 = cb.equal(root.get("artExhibition").get("id"), a.getId());
		Predicate p2 = cb.equal(root.get("language"), lang);

		Predicate combinedPredicate = cb.and(p1, p2);

		cq.select(root).where(combinedPredicate);

		List<ArtExhibitionRecord> list = this.getEntityManager().createQuery(cq).getResultList();
		return list == null || list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));

	}

	@Transactional
	public List<ArtExhibitionRecord> findAllByGuideContent(ArtExhibition a) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArtExhibitionRecord> cq = cb.createQuery(ArtExhibitionRecord.class);
		Root<ArtExhibitionRecord> root = cq.from(ArtExhibitionRecord.class);

		Predicate p1 = cb.equal(root.get("artExhibition").get("id"), a.getId());
		cq.select(root).where(p1);

		List<ArtExhibitionRecord> list = this.getEntityManager().createQuery(cq).getResultList();

		if (list == null)
			return new ArrayList<ArtExhibitionRecord>();

		return list;
	}

	@Transactional
	public void delete(Long id) {
		deleteResources(id);
		super.deleteById(id);
	}

	@Transactional
	public void delete(ArtExhibitionRecord o) {
		this.delete(o.getId());
	}

	@Transactional
	public Optional<ArtExhibitionRecord> findWithDeps(Long id) {

		Optional<ArtExhibitionRecord> o_aw = super.findById(id);

		if (o_aw.isEmpty())
			return o_aw;

		ArtExhibitionRecord aw = o_aw.get();

		Resource photo = aw.getPhoto();

		// User u = aw.getLastModifiedUser();

		// if (u!=null)
		// u.getDisplayname();

		if (photo != null)
			photo.getBucketName();

		aw.setDependencies(true);

		return o_aw;
	}

	@Transactional
	@Override
	public Iterable<ArtExhibitionRecord> findAllSorted() {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArtExhibitionRecord> cq = cb.createQuery(getEntityClass());
		Root<ArtExhibitionRecord> root = cq.from(getEntityClass());
		cq.orderBy(cb.asc(cb.lower(root.get("name"))));
		return getEntityManager().createQuery(cq).getResultList();
	}

	public boolean isDetached(ArtExhibitionRecord entity) {
		return !getEntityManager().contains(entity);
	}

	@Transactional
	public void reloadIfDetached(ArtExhibitionRecord src) {
		if (!getEntityManager().contains(src)) {
			src = findById(src.getId()).get();
		}
	}

	@Transactional
	public List<ArtExhibitionRecord> getByName(String name) {
		return createNameQuery(name).getResultList();
	}

	@Override
	protected Class<ArtExhibitionRecord> getEntityClass() {
		return ArtExhibitionRecord.class;
	}

	@PostConstruct
	protected void onInitialize() {
		super.register(getEntityClass(), this);
	}

	@Transactional
	private void deleteResources(Long id) {

		Optional<ArtExhibitionRecord> o_aw = super.findWithDeps(id);

		if (o_aw.isEmpty())
			return;

		ArtExhibitionRecord a = o_aw.get();

		getResourceDBService().delete(a.getPhoto());
		getResourceDBService().delete(a.getAudio());
		getResourceDBService().delete(a.getVideo());

	}

}
