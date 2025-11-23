package dellemuse.serverapp.jpa.events;

import dellemuse.model.logging.Logger;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.command.CommandService;
import dellemuse.serverapp.command.ResourceMetadataCommand;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.odilon.client.error.ODClientException;


public class ResourceEventListener {

	static private Logger logger = Logger.getLogger( ResourceEventListener.class.getName());
	
	@PrePersist
    public void prePersist(Object entidad) {
    }

    @PostUpdate
    public void postUpdate(Object entidad) {
    	logger.debug("postUpdate");    
    }
    
    
    /**
     * lastmodifieduser
   
     * @param o
     */
    @PostRemove
    public void postRemove(Object o) {
    	try {
    		logger.debug("postRemove");    
        	Resource resource = (Resource) o;
    		getObjectStorageService().getClient().deleteObject(resource.getBucketName(), resource.getObjectName());
	
    	} catch (ODClientException e) {
		 logger.error(e, ServerConstant.NOT_THROWN);
		}
    }
    
    @PostPersist
    public void postPersist(Object o) {
    	logger.debug("postPersist");    
    	Resource resource = (Resource) o;

    	
    	getCommandService().run(new ResourceMetadataCommand(resource.getId()));
    }

    protected CommandService getCommandService() {
		return (CommandService) ServiceLocator.getInstance().getBean(CommandService.class);
	}
	
    protected ObjectStorageService getObjectStorageService() {
		return (ObjectStorageService) ServiceLocator.getInstance().getBean(ObjectStorageService.class);
	}
    
   /**
     * 
     *  @PrePersist: Se ejecuta antes de que la entidad se guarde en la base de datos. 
		@PostPersist: Se ejecuta después de que la entidad ha sido guardada. 
		@PreRemove: Se ejecuta antes de que la entidad se elimine. 
		@PostRemove: Se ejecuta después de que la entidad ha sido eliminada. 
		@PreUpdate: Se ejecuta antes de que la entidad sea actualizada. 
		@PostUpdate: Se ejecuta después de que la entidad ha sido actualizada. 
		@PostLoad
     */
    
}
