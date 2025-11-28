package dellemuse.serverapp.serverdb.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class DelleMuseAuditDBService extends BaseDBService<DelleMuseAudit, Long> {

	static private Logger logger = Logger.getLogger(DelleMuseAuditDBService.class.getName());

	public DelleMuseAuditDBService(CrudRepository<DelleMuseAudit, Long> repository, ServerDBSettings settings) {
		super(repository, settings);
	}

	 

	@Override
	protected Class<DelleMuseAudit> getEntityClass() {
		return DelleMuseAudit.class;
	}

	@Transactional
	public <S extends DelleMuseAudit> S save(S entity) {
		entity.setLastModified(OffsetDateTime.now());
		return getRepository().save(entity);
	}

	@Transactional
	public Optional<DelleMuseAudit> findWithDeps(Long id) {

		Optional<DelleMuseAudit> o_aw = super.findById(id);

		if (o_aw.isEmpty())
			return o_aw;

		DelleMuseAudit aw = o_aw.get();


		User u = aw.getLastModifiedUser();

		if (u != null)
			u.getDisplayname();

		aw.setDependencies(true);

		return o_aw;
	}
	
	@Transactional
	public Optional<DelleMuseAudit> findById(Long id) {
		return getRepository().findById(id);
	}

	@Transactional
	public boolean existsById(Long id) {
		return getRepository().existsById(id);
	}

	@Transactional
	public void delete(DelleMuseAudit o) {
		getRepository().delete(o);
	}
	
	
	@Transactional
	public List<DelleMuseAudit> getAudit(Long objectId, String objectClassName) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<DelleMuseAudit> cq = cb.createQuery(DelleMuseAudit.class);
		Root<DelleMuseAudit> root = cq.from(DelleMuseAudit.class);
		
		Predicate p0 = cb.equal(root.get("objectId"), String.valueOf(objectId));
		Predicate p1 = cb.equal(root.get("objectClassName"), String.valueOf(objectClassName));

		Predicate combinedPredicate = cb.and(p0, p1);

		cq.select(root).where(combinedPredicate);
		cq.orderBy(cb.asc( root.get("lastModified") ));
		return getEntityManager().createQuery(cq).getResultList();
	}
	
	
	
	

}
