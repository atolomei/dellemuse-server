package dellemuse.serverapp.serverdb.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;

@Service
public class ArtExhibitionItemDBService extends DBService<ArtExhibitionItem, Long> {

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(ArtExhibitionItemDBService.class.getName());

    
    public ArtExhibitionItemDBService(CrudRepository<ArtExhibitionItem, Long> repository, ServerDBSettings settings) {
        super(repository, settings);
    }
    
    
    @Transactional
	public Optional<ArtExhibitionItem> findByIdWithDeps(Long id) {

		Optional<ArtExhibitionItem> o = super.findById(id);

		if (o.isEmpty())
			return o;

		ArtExhibitionItem a = o.get();

		a.setDependencies(true);

		a.getArtExhibition().getDisplayname();

		return o;
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
    public ArtExhibitionItem create(String name, User createdBy) {
        ArtExhibitionItem c = new ArtExhibitionItem();
        c.setName(name);
        c.setNameKey(nameKey(name));
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        return getRepository().save(c);
    }

    @PostConstruct
    protected void onInitialize() {
    	super.register(getEntityClass(), this);
    	//ServiceLocator.getInstance().register(getEntityClass(), this);
    }
    
    /**
     * @param name
     * @return
     */
    public List<ArtExhibitionItem> getByName(String name) {
        return createNameQuery(name).getResultList();
    }

    @Override
    protected Class<ArtExhibitionItem> getEntityClass() {
        return ArtExhibitionItem.class;
    }
}
