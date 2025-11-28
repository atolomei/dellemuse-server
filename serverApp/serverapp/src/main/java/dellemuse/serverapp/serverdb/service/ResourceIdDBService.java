package dellemuse.serverapp.serverdb.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.ResourceId;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;


public class ResourceIdDBService extends BaseDBService<ResourceId, Long> {

    
    public ResourceIdDBService(CrudRepository<ResourceId, Long> repository, 
            ServerDBSettings settings) {
        super(repository,   settings);
    }

    @Transactional
    public ResourceId create(String name, User createdBy) {
        ResourceId c =new ResourceId();
        return getRepository().save(c);
    }

    @PostConstruct
    protected void onInitialize() {
    	super.register(getEntityClass(), this);
    }
    
    @Override
    protected Class<ResourceId> getEntityClass() {
        return ResourceId.class;
    }

    
    public ResourceId create() {
        ResourceId c =new ResourceId();
        return getRepository().save(c);
    }

}
