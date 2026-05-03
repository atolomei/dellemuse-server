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
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Room;
import dellemuse.serverapp.serverdb.model.RoomRecord;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.RecordDBService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class RoomRecordDBService extends RecordDBService<RoomRecord, Long> {

    static private Logger logger = Logger.getLogger(RoomRecordDBService.class.getName());

    public RoomRecordDBService(CrudRepository<RoomRecord, Long> repository, ServerDBSettings settings) {
        super(repository, settings);
    }

    @Override
    @Transactional
    public Optional<RoomRecord> findByParentObject(MultiLanguageObject o, String lang) {
        return findByRoom((Room) o, lang);
    }

    @Transactional
    public RoomRecord create(String name, Room room, User createdBy) {
        RoomRecord c = new RoomRecord();
        c.setName(name);
        c.setRoom(room);
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        c.setState(ObjectState.EDITION);
        getRepository().save(c);
        getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy, AuditAction.CREATE));
        return c;
    }

    @Transactional
    public RoomRecord create(Room room, String lang, User createdBy) {
        RoomRecord c = new RoomRecord();
        c.setRoom(room);
        c.setName(room.getName());
        c.setLanguage(lang);
        c.setState(room.getState());
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        getRepository().save(c);
        getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy, AuditAction.CREATE));
        return c;
    }

    @Transactional
    public void save(RoomRecord o, User user, List<String> updatedParts) {
        super.save(o);
        getDelleMuseAuditDBService().save(DelleMuseAudit.of(o, user, AuditAction.UPDATE, String.join(", ", updatedParts)));
    }

    @Transactional
    public void markAsDeleted(RoomRecord c, User deletedBy) {
        super.markAsDeleted(c, deletedBy);
    }

    @Transactional
    public void restore(RoomRecord c, User by) {
        super.restore(c, by);
    }

    @Transactional
    public Optional<RoomRecord> findByRoom(Room room, String lang) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<RoomRecord> cq = cb.createQuery(RoomRecord.class);
        Root<RoomRecord> root = cq.from(RoomRecord.class);

        Predicate p1 = cb.equal(root.get("room").get("id"), room.getId());
        Predicate p2 = cb.equal(root.get("language"), getLanguageService().normalizeLanguage(lang));

        cq.select(root).where(cb.and(p1, p2));

        List<RoomRecord> list = getEntityManager().createQuery(cq).getResultList();
        return list == null || list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Transactional
    public List<RoomRecord> findAllByRoom(Room room) {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<RoomRecord> cq = cb.createQuery(RoomRecord.class);
        Root<RoomRecord> root = cq.from(RoomRecord.class);
        cq.select(root).where(cb.equal(root.get("room").get("id"), room.getId()));
        List<RoomRecord> list = getEntityManager().createQuery(cq).getResultList();
        return list == null ? new ArrayList<>() : list;
    }

    @Transactional
    public Optional<RoomRecord> findWithDeps(Long id) {
        Optional<RoomRecord> o = super.findById(id);
        if (o.isEmpty()) return o;

        RoomRecord rr = o.get();

        Resource photo = rr.getPhoto();
        if (photo != null)
            rr.setPhoto(getResourceDBService().findById(photo.getId()).get());

        Resource audio = rr.getAudio();
        if (audio != null)
            rr.setAudio(getResourceDBService().findById(audio.getId()).get());

        Resource audioAccessible = rr.getAudioAccessible();
        if (audioAccessible != null)
            rr.setAudioAccessible(getResourceDBService().findById(audioAccessible.getId()).get());

        User user = rr.getLastModifiedUser();
        if (user != null)
            rr.setLastModifiedUser(getUserDBService().findById(user.getId()).get());

        if (rr.getParentObject() != null) {
            Room r = (Room) rr.getParentObject();
            rr.setRoom(getRoomDBService().findById(r.getId()).get());
        }

        rr.setDependencies(true);
        return o;
    }

    @Transactional
    @Override
    public Iterable<RoomRecord> findAllSorted() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<RoomRecord> cq = cb.createQuery(getEntityClass());
        Root<RoomRecord> root = cq.from(getEntityClass());
        cq.orderBy(cb.asc(cb.lower(root.get("name"))));
        return getEntityManager().createQuery(cq).getResultList();
    }

    @Transactional
    public List<RoomRecord> getByName(String name) {
        return createNameQuery(name).getResultList();
    }

    @Override
    protected Class<RoomRecord> getEntityClass() {
        return RoomRecord.class;
    }

    @PostConstruct
    protected void onInitialize() {
        super.register(getEntityClass(), this);
    }
}
