package dellemuse.serverapp.serverdb.service;

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
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class ArtWorkDBService extends DBService<ArtWork, Long> {

	static private Logger logger = Logger.getLogger(ArtWorkDBService.class.getName());

	public ArtWorkDBService(CrudRepository<ArtWork, Long> repository, ServerDBSettings settings) {
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
	public ArtWork create(String name, User createdBy) {
		ArtWork c = new ArtWork();
		c.setName(name);
		c.setNameKey(nameKey(name));
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
		return getRepository().save(c);
	}


	@Transactional
	public ArtWork create(String name, Site site, User createdBy) {
		ArtWork c = new ArtWork();
		
		c.setName(name);
		c.setNameKey(nameKey(name));
		
		c.setSite(site);
		
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
		
		return getRepository().save(c);
	}

    
	@Transactional
	public Optional<ArtWork> findByIdWithDeps(Long id) {

		Optional<ArtWork> o_aw = super.findById(id);

		if (o_aw.isEmpty())
			return  o_aw;
		
		ArtWork aw = o_aw.get();
		
		aw.getSite().getDisplayname();
		for ( Person p: aw.getArtists()) {
			p.getDisplayName();
		}

		Resource photo = aw.getPhoto();

		User u = aw.getLastModifiedUser();
		
		if (u!=null)
			u.getDisplayname();
		
		if (photo != null)
			photo.getBucketName();

		aw.setDependencies(true);

		return o_aw;
	}
	
    @Transactional
    @Override
    public Iterable<ArtWork> findAllSorted() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ArtWork> cq = cb.createQuery(getEntityClass());
        Root<ArtWork> root = cq.from(getEntityClass());
        cq.orderBy(cb.asc( cb.lower(root.get("name"))));
        return getEntityManager().createQuery(cq).getResultList();
    }
    

	public boolean isDetached(ArtWork entity) {
		return !getEntityManager().contains(entity);
	}

	@Transactional
	public ArtWork lazyLoad(ArtWork s) {
		ArtWork src = findById(s.getId()).get();

		SiteDBService service = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
		Site site = service.findById(src.getSite().getId()).get();
		src.setSite(site);

		PersonDBService p_service = (PersonDBService) ServiceLocator.getInstance().getBean(PersonDBService.class);
		Set<Person> list = new HashSet<Person>();
		for (Person person : src.getArtists()) {
			list.add(p_service.findById(person.getId()).get());
		}
		src.setArtists(list);
		return src;

	}

	@Transactional
	public void reloadIfDetached(ArtWork src) {
		if (!getEntityManager().contains(src)) {
			src = findById(src.getId()).get();
		}
	}

	@Transactional
	public Site loadSite(ArtWork aw) {
		SiteDBService service = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
		Site site = service.findById(aw.getSite().getId()).get();
		return site;
	}

	@Transactional
	public List<Person> loadArtists(ArtWork aw) {

		aw = findById(aw.getId()).get();
		PersonDBService service = (PersonDBService) ServiceLocator.getInstance().getBean(PersonDBService.class);
		List<Person> list = new ArrayList<Person>();
		for (Person person : aw.getArtists()) {
			list.add(service.findById(person.getId()).get());
		}
		return list;
	}

	/**
	 * @param name
	 * @return
	 */
	@Transactional
	public List<ArtWork> getByName(String name) {
		return createNameQuery(name).getResultList();
	}

	@Override
	protected Class<ArtWork> getEntityClass() {
		return ArtWork.class;
	}
	
	@PostConstruct
	protected void onInitialize() {
		super.register(getEntityClass(), this);
	}


}
