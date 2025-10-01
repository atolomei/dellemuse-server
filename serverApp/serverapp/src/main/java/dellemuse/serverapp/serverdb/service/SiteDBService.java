package dellemuse.serverapp.serverdb.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.model.FloorModel;
import dellemuse.model.logging.Logger;
import dellemuse.model.util.Check;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Floor;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Room;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.repository.PersonRepository;
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
public class SiteDBService extends DBService<Site, Long> {

	private static final Logger logger = Logger.getLogger(SiteDBService.class.getName());

	@PersistenceContext
	private EntityManager entityManager;
	
	private final PersonRepository personRepository;

	

	public SiteDBService(CrudRepository<Site, Long> repository, ServerDBSettings settings, PersonRepository personRepository) {
		super(repository, settings);
		 this.personRepository=personRepository;
	}

	@Transactional
	@Override
	public Site create(String name, User createdBy) {
		Site c = new Site();
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
	public Site create(String name, Institution institution, Optional<String> shortName, Optional<String> address,
			Optional<String> info, User createdBy) {

		Site c = new Site();
		
		c.setName(name);
		c.setTitle(name);

		c.setInstitution(institution);
		c.setNameKey(nameKey(name));
		c.setTitleKey(nameKey(name));
		
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
		shortName.ifPresent(c::setShortName);
		address.ifPresent(c::setAddress);
		
		return getRepository().save(c);
	}

	@Transactional
	public Site create(Institution in, User createdBy) {
		
		Site c = new Site();
		
		c.setInstitution(in);
		
		c.setName(in.getName());
		c.setNameKey(nameKey(in.getName()));

		c.setShortName(in.getShortName());

		c.setTitle(in.getTitle());
		c.setTitleKey(nameKey(in.getTitle()));

		c.setSubtitle(in.getSubtitle());
		
		c.setCreated(OffsetDateTime.now());
		c.setLastModified(OffsetDateTime.now());
		c.setLastModifiedUser(createdBy);
		
		c.setAddress(in.getAddress());
		c.setWebsite(in.getWebsite());
		c.setMapurl(in.getMapUrl());
		c.setEmail(in.getEmail());
		
		c.setPhone(in.getPhone());
		c.setPhoto(in.getPhoto());
		c.setLogo(in.getLogo());
		
		c.setInstagram(in.getInstagram());
		c.setWhatsapp(in.getWhatsapp());
		
		c.setInfo(in.getInfo());
		
		return getRepository().save(c);
	}
	
    @Transactional
    public Iterable<Site> findAllSorted() {
        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Site> cq = cb.createQuery(getEntityClass());
        Root<Site> root = cq.from(getEntityClass());
        cq.orderBy(cb.asc( cb.lower(root.get("name"))));
        return getEntityManager().createQuery(cq).getResultList();
    }


	@Transactional
	public Optional<Site> findWithDeps(Long id) {

		Optional<Site> o_site = super.findById(id);

		if (o_site.isEmpty())
			return o_site;

		Site site = o_site.get();

		site.setDependencies(true);

		site.getInstitution().getDisplayname();
		
		Resource photo = site.getPhoto();
		
		if (photo != null)
			photo.getBucketName();

		Resource logo = site.getLogo();
		if (logo != null)
			logo.getBucketName();

		return o_site;
	}


	@Transactional
	public Optional<Resource> loadPhoto(Site src) {
		ResourceDBService s = (ResourceDBService) ServiceLocator.getInstance().getBean(ResourceDBService.class);
		return s.findById(src.getPhoto().getId());
	}

	@Transactional
	public void reloadIfDetached(Site src) {

		if (!getEntityManager().contains(src)) {
			src = findById(src.getId()).get();
			@SuppressWarnings("unused")
			Institution i = src.getInstitution();
		}
	}

	@Transactional
	public List<Site> getByName(String name) {
		return createNameQuery(name).getResultList();
	}

	@Transactional
	public Optional<Site> findByShortName(String name) {
		Check.requireNonNullStringArgument(name, "name is null");
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Site> cq = cb.createQuery(Site.class);
		Root<Site> root = cq.from(Site.class);
		cq.select(root).where(cb.equal(cb.lower(root.get("shortName")), name.toLowerCase()));
		cq.orderBy(cb.asc(root.get("id")));

		List<Site> list = this.entityManager.createQuery(cq).getResultList();
		return list == null || list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
	}

	@Transactional
	public List<ArtExhibition> getArtExhibitions(Site site) {
		return getArtExhibitions(site.getId());
	}

	@Transactional
	public List<ArtExhibition> getArtExhibitions(Long siteId) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArtExhibition> cq = cb.createQuery(ArtExhibition.class);
		Root<ArtExhibition> root = cq.from(ArtExhibition.class);
		cq.select(root).where(cb.equal(root.get("site").get("id"), siteId));
		cq.orderBy(cb.asc( cb.lower(root.get("name"))));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public List<ArtExhibitionItem> getSiteArtExhibitionItems(Long siteId) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArtExhibitionItem> cq = cb.createQuery(ArtExhibitionItem.class);
		Root<ArtExhibitionItem> root = cq.from(ArtExhibitionItem.class);
		cq.select(root).where(cb.equal(root.get("artExhibition").get("site").get("id"), siteId));
		cq.orderBy(cb.asc( cb.lower(root.get("name"))));

		return getEntityManager().createQuery(cq).getResultList();
	}

	
	/**
	 * 
	 * select name from person where id in (select person_id from artworkArtist where artwork_id in (select id from artwork where site_owner_id = 137));
	 * 
	 * select distinct p.lastname from person p where p.id in (select person_id from artworkartist AA, artwork A  where A.id=AA.artwork_id and A.site_owner_id=137) order by p.lastname;
	 * 
	 * @param siteId
	 * @return
	 */
	
	@Transactional
	public Set<Person> getSiteArtists(Long siteId) {
		
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArtWork> cq = cb.createQuery(ArtWork.class);

		Root<ArtWork> root = cq.from(ArtWork.class);
		cq.select(root).where(cb.equal(root.get("site").get("id"), siteId));

		List<ArtWork> list = getEntityManager().createQuery(cq).getResultList();
		
		Set<Person> persons = new HashSet<Person>();
		
		list.forEach( i -> { 
			i.getArtists().forEach( k -> 	
			persons.add(k) );
		});
		
		return persons;
	}
	
	
	
	
     
	@Transactional
    public List<Person> getArtistsBySiteId(Long siteId) {
        return getPersonRepository().findDistinctPersonsBySiteId(siteId);
    }
    

	@Transactional
	public Iterable<ArtWork> findDistinctArtWorkByPersonId(Long id) {
		return getPersonRepository().findDistinctArtWorkByPersonId(id);
	}

	
	
	public boolean isDetached(Site entity) {
		return !getEntityManager().contains(entity);
	}
	
	
	/**
	 * 
	 * 
	 * 
	 * 
	 
	 
	 public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query("""
        SELECT DISTINCT aa.person
        FROM ArtworkArtist aa
        JOIN aa.artwork a
        WHERE a.site.id = :siteId
    """)
    List<Person> findDisti nctPersonsBySiteId(@Param("siteId") Long siteId);
}


 
	 * @param siteId
	 * @return
	 */
	
	@Transactional
	public List<GuideContent> getSiteGuideContent(Long siteId) {

		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<GuideContent> cq = cb.createQuery(GuideContent.class);
		Root<GuideContent> root = cq.from(GuideContent.class);
		cq.select(root)
				.where(cb.equal(root.get("artExhibitionItem").get("artExhibition").get("site").get("id"), siteId));
		cq.orderBy(cb.asc( cb.lower(root.get("name"))));

		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public List<Floor> getFloors(Long siteId) {
		CriteriaBuilder cb =getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Floor> cq = cb.createQuery(Floor.class);
		Root<Floor> root = cq.from(Floor.class);
		cq.select(root).where(cb.equal(root.get("site").get("id"), siteId));
		cq.orderBy(cb.asc( cb.lower(root.get("name"))));
		return getEntityManager().createQuery(cq).getResultList();
	}

	@Transactional
	public List<Room> getRooms(Long floorId) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Room> cq = cb.createQuery(Room.class);
		Root<Room> root = cq.from(Room.class);
		cq.select(root).where(cb.equal(root.get("floor").get("id"), floorId));
		cq.orderBy(cb.asc( cb.lower(root.get("name"))));

		return getEntityManager().createQuery(cq).getResultList();
	}
	

	@Transactional
	public List<ArtWork> getSiteArtWorks(Site site) {
		Check.requireNonNull(site, "site is null");
		return this.getSiteArtWorks(site.getId());
	}

	@Transactional
	public List<ArtWork> getSiteArtWorks(Long siteId) {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ArtWork> cq = cb.createQuery(ArtWork.class);
		Root<ArtWork> root = cq.from(ArtWork.class);

		cq.select(root).where(cb.equal(root.get("site").get("id"), siteId));
		cq.orderBy(cb.asc( cb.lower(root.get("name"))));
		   
		return getEntityManager().createQuery(cq).getResultList();
	}
	
	
	@PostConstruct
	protected void onInitialize() {
		super.register(getEntityClass(), this);
	}

	@Override
	protected Class<Site> getEntityClass() {
		return Site.class;
	}

	
	private PersonRepository getPersonRepository() {
		 return this.personRepository;
	}

}
