package dellemuse.serverdb.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.serverdb.ServerDBSettings;
import dellemuse.serverdb.model.ResourceId;
import dellemuse.serverdb.model.User;
import dellemuse.serverdb.service.base.ServiceLocator;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;


public class ResourceIdDBService extends DBService<ResourceId, Long> {

    
    public ResourceIdDBService(CrudRepository<ResourceId, Long> repository, 
            ServerDBSettings settings) {
        super(repository,   settings);
    }

    @Override
    @Transactional
    public ResourceId create(String name, User createdBy) {
        ResourceId c =new ResourceId();
        return getRepository().save(c);
    }

    @PostConstruct
    protected void onInitialize() {
    	ServiceLocator.getInstance().register(getEntityClass(), this);
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
