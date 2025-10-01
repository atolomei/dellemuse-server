package dellemuse.serverapp.serverdb.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import jakarta.annotation.PostConstruct;
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
public class InstitutionDBService extends DBService<Institution, Long> {

    private static final Logger logger = Logger.getLogger(InstitutionDBService.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    public InstitutionDBService(CrudRepository<Institution, Long> repository, ServerDBSettings settings) {
        super(repository, settings);
    }

    @PostConstruct
    protected void onInitialize() {
    	super.register(getEntityClass(), this);
    }
  
	@Transactional
    public Optional<Institution> findWithDeps(Long id) {
    
		Optional<Institution> o_i = super.findById(id);
		
		if (o_i.isEmpty())
			return o_i;
		
		Institution i = o_i.get();
		
		i.setDependencies(true);
		
		Resource photo=i.getPhoto();
		
		if (photo!=null)
			photo.getBucketName();
		
		Resource logo=i.getLogo();
		
		if (logo!=null)
			logo.getBucketName();
		
		return o_i;
    }

	
    @Transactional
    public Iterable<Institution> findAllSorted() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Institution> cq = cb.createQuery(getEntityClass());
        Root<Institution> root = cq.from(getEntityClass());
        cq.orderBy(cb.asc( cb.lower(root.get("name"))));
        return entityManager.createQuery(cq).getResultList();
    }

    
    @Transactional
    @Override
    public Institution create(String name, User createdBy) {
        Institution c = new Institution();

        c.setName(name);
		c.setNameKey(nameKey(name));

		c.setTitle(name);
		c.setTitleKey(nameKey(name));

		c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        return getRepository().save(c);
    }

    @Transactional
    public Institution create(String name, Optional<String> shortName, Optional<String> address, Optional<String> info,
                              User createdBy) {

        Institution c = new Institution();
		c.setName(name);
		c.setNameKey(nameKey(name));

		c.setTitle(name);
		c.setTitleKey(nameKey(name));

		shortName.ifPresent(c::setShortName);
        address.ifPresent(c::setAddress);
        info.ifPresent(c::setInfo); // corregido: antes se usaba setAddress por error

        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);

        return getRepository().save(c);
    }

    @Transactional
    public List<Site> getSites(Long institutionId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Site> cq = cb.createQuery(Site.class);
        Root<Site> root = cq.from(Site.class);
        cq.select(root).where(cb.equal(root.get("institution").get("id"), institutionId));
        cq.orderBy(cb.asc( cb.lower(root.get("name"))));

        return entityManager.createQuery(cq).getResultList();
    }

    @Transactional
    public Optional<Institution> findByShortName(String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Institution> cq = cb.createQuery(Institution.class);
        Root<Institution> root = cq.from(Institution.class);
        cq.select(root).where(cb.equal(root.get("shortName"), name));
        cq.orderBy(cb.asc(root.get("id")));

        List<Institution> list = entityManager.createQuery(cq).getResultList();
        return list == null || list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Transactional
    public List<Institution> getByName(String name) {
        return createNameQuery(name).getResultList();
    }

    @Override
    protected Class<Institution> getEntityClass() {
        return Institution.class;
    }
}
 