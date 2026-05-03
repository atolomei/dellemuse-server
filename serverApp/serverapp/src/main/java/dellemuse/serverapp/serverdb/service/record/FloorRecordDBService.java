package dellemuse.serverapp.serverdb.service.record;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.AuditAction;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.Floor;
import dellemuse.serverapp.serverdb.model.FloorRecord;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.RecordDBService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class FloorRecordDBService extends RecordDBService<FloorRecord, Long> {

    static private Logger logger = Logger.getLogger(FloorRecordDBService.class.getName());

    public FloorRecordDBService(CrudRepository<FloorRecord, Long> repository, ServerDBSettings settings) {
        super(repository, settings);
    }

    @Override
    @Transactional
    public Optional<FloorRecord> findByParentObject(MultiLanguageObject o, String lang) {
        return findByFloor((Floor) o, lang);
    }

    /**
     * Create a FloorRecord with a name, floor and user.
     */
    @Transactional
    public FloorRecord create(String name, Floor floor, User createdBy) {
        FloorRecord c = new FloorRecord();
        c.setName(name);
        c.setFloor(floor);
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        c.setState(ObjectState.EDITION);
        getRepository().save(c);
        getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy, AuditAction.CREATE));
        return c;
    }

    /**
     * Create a FloorRecord for a Floor and language (used when initialising all language records).
     */
    @Transactional
    public FloorRecord create(Floor floor, String lang, User createdBy) {
        FloorRecord c = new FloorRecord();
        c.setFloor(floor);
        c.setName(floor.getName());
        c.setLanguage(lang);
        c.setState(floor.getState());
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        getRepository().save(c);
        getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy, AuditAction.CREATE));
        return c;
    }

    @Transactional
    public void save(FloorRecord o, User user, List<String> updatedParts) {
        super.save(o);
        
        
        getDelleMuseAuditDBService().save(DelleMuseAudit.of(o, user, AuditAction.UPDATE, String.join(", ", updatedParts)));
    }

    @Transactional
    public void markAsDeleted(FloorRecord c, User deletedBy) {
        super.markAsDeleted(c, deletedBy);
    }

    @Transactional
    public void restore(FloorRecord c, User by) {
        super.restore(c, by);
    }

    /**
     * Find the FloorRecord for a given Floor and language.
     */
    @Transactional
    public Optional<FloorRecord> findByFloor(Floor floor, String lang) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<FloorRecord> cq = cb.createQuery(FloorRecord.class);
        Root<FloorRecord> root = cq.from(FloorRecord.class);

        Predicate p1 = cb.equal(root.get("floor").get("id"), floor.getId());
        Predicate p2 = cb.equal(root.get("language"), getLanguageService().normalizeLanguage(lang));

        cq.select(root).where(cb.and(p1, p2));

        List<FloorRecord> list = getEntityManager().createQuery(cq).getResultList();
        return list == null || list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    /**
     * Find all FloorRecords for a given Floor (all languages).
     */
    @Transactional
    public List<FloorRecord> findAllByFloor(Floor floor) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<FloorRecord> cq = cb.createQuery(FloorRecord.class);
        Root<FloorRecord> root = cq.from(FloorRecord.class);

        cq.select(root).where(cb.equal(root.get("floor").get("id"), floor.getId()));

        List<FloorRecord> list = getEntityManager().createQuery(cq).getResultList();
        return list == null ? new ArrayList<>() : list;
    }

    @Transactional
    public Optional<FloorRecord> findWithDeps(Long id) {
        Optional<FloorRecord> o = super.findById(id);
        if (o.isEmpty())
            return o;

        FloorRecord fr = o.get();

        Resource photo = fr.getPhoto();
        if (photo != null)
            fr.setPhoto(getResourceDBService().findById(photo.getId()).get());

        Resource audio = fr.getAudio();
        if (audio != null)
            fr.setAudio(getResourceDBService().findById(audio.getId()).get());

        Resource audioAccessible = fr.getAudioAccessible();
        if (audioAccessible != null)
            fr.setAudioAccessible(getResourceDBService().findById(audioAccessible.getId()).get());

        User user = fr.getLastModifiedUser();
        if (user != null)
            fr.setLastModifiedUser(getUserDBService().findById(user.getId()).get());

        if (fr.getParentObject() != null) {
            Floor f = (Floor) fr.getParentObject();
            fr.setFloor(getFloorDBService().findById(f.getId()).get());
        }

        fr.setDependencies(true);
        return o;
    }

    @Transactional
    @Override
    public Iterable<FloorRecord> findAllSorted() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<FloorRecord> cq = cb.createQuery(getEntityClass());
        Root<FloorRecord> root = cq.from(getEntityClass());
        cq.orderBy(cb.asc(cb.lower(root.get("name"))));
        return getEntityManager().createQuery(cq).getResultList();
    }

    @Transactional
    public List<FloorRecord> getByName(String name) {
        return createNameQuery(name).getResultList();
    }

    @Override
    protected Class<FloorRecord> getEntityClass() {
        return FloorRecord.class;
    }

    @PostConstruct
    protected void onInitialize() {
        super.register(getEntityClass(), this);
    }
}
