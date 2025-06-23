package dellemuse.server.db.service;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.server.Settings;
import dellemuse.server.db.model.Person;
import dellemuse.server.db.model.User;
import dellemuse.server.error.ObjectNotFoundException;
import dellemuse.model.logging.Logger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class UserDBService extends DBService<User, Long> {

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(UserDBService.class.getName());

    private final PersonDBService personDBService;

    @PersistenceContext
    private EntityManager entityManager;

    public UserDBService(CrudRepository<User, Long> repository,
                         Settings settings,
                         PersonDBService personDBService) {
        super(repository, settings);
        this.personDBService = personDBService;
    }

    @Transactional
    @Override
    public User create(String name, User createdBy) {
        return create(name, null, createdBy);
    }

    /**
     * @param name
     * @param person optional associated Person entity
     * @param createdBy user who creates this User
     */
    @Transactional
    public User create(String name, Person person, User createdBy) {
        User c = new User();
        c.setName(name);
        c.setNameKey(nameKey(name));
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);

        getRepository().save(c);

        if (person != null) {
            person.setUser(c);
            personDBService.save(person);
        }
        return c;
    }

    @Transactional
    @Override
    public List<User> getByName(String name) {
        return createNameQuery(name).getResultList();
    }

    @Transactional
    public User findRoot() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(getEntityClass());
        Root<User> root = criteria.from(getEntityClass());

        ParameterExpression<String> nameParam = cb.parameter(String.class);
        criteria.select(root).where(cb.equal(root.get(getNameColumn()), nameParam));

        TypedQuery<User> query = entityManager.createQuery(criteria);
        query.setHint("org.hibernate.cacheable", true);
        query.setFlushMode(FlushModeType.COMMIT);
        query.setParameter(nameParam, "root");

        List<User> users = query.getResultList();

        if (users != null && !users.isEmpty()) {
            return users.get(0);
        }
        throw new ObjectNotFoundException("Database does not have user with name=='root'");
    }

    @Override
    protected String getNameColumn() {
        return "name";
    }

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }
}
