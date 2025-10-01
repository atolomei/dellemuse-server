package dellemuse.serverapp.serverdb.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.InstitutionalContent;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;


@Service
public class InstitutionalContentDBService extends DBService< InstitutionalContent, Long> {

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(InstitutionalContentDBService.class.getName());

    public InstitutionalContentDBService(CrudRepository< InstitutionalContent, Long> repository,   ServerDBSettings settings) {
        super(repository,   settings);
    }

    

	@Transactional
	public Optional<InstitutionalContent> findWithDeps(Long id) {

		Optional< InstitutionalContent> o = super.findById(id);

		if (o.isEmpty())
			return o;

		 InstitutionalContent a = o.get();


		a.setDependencies(true);

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
    public  InstitutionalContent create(String name,User createdBy) {
        InstitutionalContent c = new  InstitutionalContent();
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
    public List< InstitutionalContent> getByName(String name) {
        return createNameQuery(name).getResultList();
    }

    @Override
    protected Class< InstitutionalContent> getEntityClass() {
        return  InstitutionalContent.class;
    }
}
