package dellemuse.serverdb.object.service;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.serverdb.model.Resource;
import dellemuse.serverdb.objectstorage.ObjectStorageService;
import io.odilon.client.error.ODClientException;

@Component
@Scope("prototype")
public class ResourceService extends BaseObjectService {
    
    @Autowired
    @JsonIgnore
    private ObjectStorageService objectStorageService; 
    
    public ResourceService(Resource resource) {
        super(resource);
    }
    
    public InputStream getInputStream() throws IOException {
        return getObjectStorageService().getObject(getObject().getBucketName(), getObject().getObjectName());
   }
    

    public ObjectStorageService getObjectStorageService() {
        return objectStorageService;
    }

    public void setObjectStorageService(ObjectStorageService objectStorageService) {
        this.objectStorageService = objectStorageService;
    }

    public String getPresignedUrl() {
        try {
            Resource resource = (Resource) getObject();
            return getObjectStorageService().getClient().getPresignedObjectUrl(resource.getBucketName(), resource.getObjectName());
        } catch (ODClientException e) {
            throw new RuntimeException(e);
        }
    }
    
    
    
    
    @Override
    public  Resource getObject() {
        return (Resource) getObject();
    }

    
}
