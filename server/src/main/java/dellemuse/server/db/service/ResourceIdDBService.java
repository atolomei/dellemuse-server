package dellemuse.server.db.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import dellemuse.server.Settings;
import dellemuse.server.db.model.ResourceId;
import dellemuse.server.db.model.User;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;


public class ResourceIdDBService extends DBService<ResourceId, Long> {

    
    public ResourceIdDBService(CrudRepository<ResourceId, Long> repository, 
            Settings settings) {
        super(repository,   settings);
    }

    @Override
    @Transactional
    public ResourceId create(String name, User createdBy) {
        ResourceId c =new ResourceId();
        return getRepository().save(c);
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
