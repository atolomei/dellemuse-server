package dellemuse.serverapp.serverdb.service;

import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Resource;
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
public class ArtExhibitionDBService extends DBService<ArtExhibition, Long> {

    private static final Logger logger = Logger.getLogger(ArtExhibitionDBService.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    public ArtExhibitionDBService(CrudRepository<ArtExhibition, Long> repository, ServerDBSettings settings) {
        super(repository, settings);
    }
    
    
    @Transactional
	public Optional<ArtExhibition> findWithDeps(Long id) {

		Optional<ArtExhibition> o = super.findById(id);

		if (o.isEmpty())
			return o;

		ArtExhibition a = o.get();
		
		
		Long siteId=a.getSite().getId();
		
		if (siteId!=null) {
			SiteDBService se=(SiteDBService) ServiceLocator.getInstance().getBean( SiteDBService.class);
			a.setSite(se.findById(siteId).get() );
		}
		
		if (a.getArtExhibitionItems()!=null) {
			ArtExhibitionDBService se=(ArtExhibitionDBService) ServiceLocator.getInstance().getBean( ArtExhibitionDBService.class);
			a.setArtExhibitionItems(se.getArtExhibitionItems(a));
			//a.getArtExhibitionItems().forEach( i -> i.getDisplayname() );
		}
		
		Resource photo = a.getPhoto();

		if (photo != null)
			photo.getBucketName();

		a.setDependencies(true);

		return o;
	}
    
    @PostConstruct
    protected void onInitialize() {
    	super.register(getEntityClass(), this);
    }
    
    
    @Transactional
	public void addItem(ArtExhibition exhibition, ArtWork artwork, User addedBy) {

    	boolean contains = false;
    	
    	
    	List<ArtExhibitionItem> list = getArtExhibitionItems(exhibition);
    	
    	for (ArtExhibitionItem i: list) {
    		if (artwork.getId().equals(i.getArtWork().getId())) {
    			contains=true;
    			break;
    		}
    	}
    	
    	if (contains)
    		return;

    	ArtExhibitionItem item = getArtExhibitionItemDBService().create( 	
    			artwork.getName(), 
				exhibition, 
				artwork, 
				addedBy );

    	
    	
    	list.add(item);
    	exhibition.setArtExhibitionItems(list);
    	
    	exhibition.setLastModified(OffsetDateTime.now());
    	exhibition.setLastModifiedUser(addedBy);
        getRepository().save(exhibition);
     	
	}

	@Transactional
    public void removeItem(ArtExhibition c, ArtExhibitionItem item, User removedBy) {
    	
    	boolean contains = false;
    	int index = -1;
    	

    	List<ArtExhibitionItem> list = getArtExhibitionItems(c);
    	
    		
    	for (ArtExhibitionItem i: list) {
    		index++;
    		if (item.getId().equals(i.getId())) {
    			contains=true;
    			break;
    		}
    	}
    	
    	if (!contains)
    		return;
    	
    	list.remove(index);
    	c.setArtExhibitionItems(list);
    	c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(removedBy);
        getRepository().save(c);
    }
    
    
    @Transactional
    @Override
    public ArtExhibition create(String name, User createdBy) {
        ArtExhibition c = new ArtExhibition();
        c.setName(name);
        c.setNameKey(nameKey(name));
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        return getRepository().save(c);
    }
    
    
    @Transactional
    public ArtExhibition create(String name, Site site, User createdBy) {
        ArtExhibition c = new ArtExhibition();
        c.setName(name);
        c.setNameKey(nameKey(name));
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        c.setSite(site);
        
        return getRepository().save(c);
    }
    
    

    @Transactional
    public List<ArtExhibition> getByName(String name) {
        return createNameQuery(name).getResultList();
    }

    @Transactional
    public Optional<ArtExhibition> findByNameKey(String name) {
        CriteriaBuilder cb =  getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ArtExhibition> cq = cb.createQuery(ArtExhibition.class);
        Root<ArtExhibition> root = cq.from(ArtExhibition.class);
        cq.select(root).where(cb.equal(root.get("nameKey"), name));
        cq.orderBy(cb.asc(root.get("id")));

        List<ArtExhibition> list = entityManager.createQuery(cq).getResultList();
        return list == null || list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Transactional
    public List<ArtExhibitionGuide> getArtExhibitionGuides(ArtExhibition exhibition) {
        CriteriaBuilder cb =  getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ArtExhibitionGuide> cq = cb.createQuery(ArtExhibitionGuide.class);
        Root<ArtExhibitionGuide> root = cq.from(ArtExhibitionGuide.class);
        cq.select(root).where(cb.equal(root.get("artExhibition").get("id"), exhibition.getId()));
        cq.orderBy(cb.asc( cb.lower(root.get("name"))));

        return  getEntityManager().createQuery(cq).getResultList();
    }

    @Transactional
    public List<ArtExhibitionItem> getArtExhibitionItems(ArtExhibition exhibition) {
        CriteriaBuilder cb =  getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ArtExhibitionItem> cq = cb.createQuery(ArtExhibitionItem.class);
        Root<ArtExhibitionItem> root = cq.from(ArtExhibitionItem.class);
        cq.select(root).where(cb.equal(root.get("artExhibition").get("id"), exhibition.getId()));
        cq.orderBy(cb.asc( cb.lower(root.get("name"))));
        return  getEntityManager().createQuery(cq).getResultList();
    }

    @Transactional
    public List<ArtExhibition> getArtExhibitions(Site site) {
        CriteriaBuilder cb =  getEntityManager().getCriteriaBuilder();
        CriteriaQuery<ArtExhibition> cq = cb.createQuery(ArtExhibition.class);
        Root<ArtExhibition> root = cq.from(ArtExhibition.class);
        cq.select(root).where(cb.equal(root.get("site").get("id"), site.getId()));
        cq.orderBy(cb.asc( cb.lower(root.get("name"))));

        return getEntityManager().createQuery(cq).getResultList();
    }

    @Override
    protected Class<ArtExhibition> getEntityClass() {
        return ArtExhibition.class;
    }

	
}
    
    
    
    
    
    
     