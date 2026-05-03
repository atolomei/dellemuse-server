package dellemuse.serverapp.serverdb.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.Check;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.AuditAction;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.Floor;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class FloorDBService extends DBService<Floor, Long> {

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(FloorDBService.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    public FloorDBService(CrudRepository<Floor, Long> repository, ServerDBSettings settings) {
        super(repository, settings);
    }

    @Transactional
    public Floor create(String name, User createdBy) {
        Floor c = new Floor();
        c.setName(name);
        
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        c.setState(ObjectState.EDITION);
        
    	c.setMasterLanguage(getDefaultMasterLanguage());
		c.setLanguage(getDefaultMasterLanguage());

        getRepository().save(c);
        getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy, AuditAction.CREATE));

    	for (Language la : getLanguageService().getLanguages())
			getFloorRecordDBService().create(c, la.getLanguageCode(), createdBy);
        
        return c;
    }

    @Transactional
    public Floor create(String name, Site site, User createdBy) {
    	
    	Check.requireNonNullArgument(createdBy, "createdBy is null");
		Check.requireNonNullArgument(name, "name is null");
		Check.requireNonNullArgument(site, "site is null");
		
		
        Floor c = new Floor();
        c.setName(name);
        c.setSite(site);
        
        c.setMasterLanguage(site.getMasterLanguage());
		c.setLanguage(site.getLanguage());
		
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        c.setState(ObjectState.EDITION);
        getRepository().save(c);
        getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy, AuditAction.CREATE));
        
		for (Language la : getLanguageService().getLanguages())
			getFloorRecordDBService().create(c, la.getLanguageCode(), createdBy);

        return c;
    }

    @Transactional
    public Floor save(Floor c, User user, List<String> updatedParts) {
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(user);
        getRepository().save(c);
        getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, user, AuditAction.UPDATE, String.join(", ", updatedParts)));
        return c;
    }

    @Transactional
    public void markAsDeleted(Floor c, User deletedBy) {
        c.setLastModifiedUser(deletedBy);
        c.setState(ObjectState.DELETED);
        getRepository().save(c);
        getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, deletedBy, AuditAction.DELETE));
    }

    @Transactional
    public void restore(Floor c, User restoredBy) {
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(restoredBy);
        c.setState(ObjectState.EDITION);
        getRepository().save(c);
        getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, restoredBy, AuditAction.UPDATE));
    }

    @Transactional
    public Optional<Floor> findWithDeps(Long id) {
        Optional<Floor> o = super.findById(id);
        if (o.isEmpty())
            return o;
        Floor f = o.get();
        Long siteId = f.getSite() != null ? f.getSite().getId() : null;
        Long userId = f.getLastModifiedUser() != null ? f.getLastModifiedUser().getId() : null;
        getEntityManager().detach(f);
        if (siteId != null) {
            SiteDBService se = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
            f.setSite(se.findById(siteId).get());
        }
        if (userId != null)
            f.setLastModifiedUser(getUserDBService().findById(userId).get());
        f.setDependencies(true);
        return o;
    }

    public List<Floor> getFloors(Site site) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Floor> cq = cb.createQuery(Floor.class);
        Root<Floor> root = cq.from(Floor.class);
        cq.select(root).where(cb.equal(root.get("site").get("id"), site.getId()));
        cq.orderBy(cb.asc(cb.lower(root.get("name"))));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<Floor> getFloors(Long siteId) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Floor> cq = cb.createQuery(Floor.class);
        Root<Floor> root = cq.from(Floor.class);
        cq.select(root).where(cb.equal(root.get("site").get("id"), siteId));
        cq.orderBy(cb.asc(cb.lower(root.get("name"))));
        return getEntityManager().createQuery(cq).getResultList();
    }

    @PostConstruct
    protected void onInitialize() {
        super.register(getEntityClass(), this);
    }

    /**
     * @param name
     * @return
     */
    public List<Floor> getByName(String name) {
        return createNameQuery(name).getResultList();
    }

    @Override
    protected Class<Floor> getEntityClass() {
        return Floor.class;
    }

    @Override
    public String getObjectClassName() {
        return Floor.class.getSimpleName().toLowerCase();
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
}