package dellemuse.serverapp.jpa.events;

import dellemuse.model.logging.Logger;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import dellemuse.serverapp.command.CommandService;
import dellemuse.serverapp.command.EvictLanguageCacheMLOCommand;
import dellemuse.serverapp.command.QRCodeGenerationCommand;
import dellemuse.serverapp.command.ResourceMetadataCommand;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.service.ArtWorkDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;



public class MultiLanguageObjectEventListener {

	static private Logger logger = Logger.getLogger( MultiLanguageObjectEventListener.class.getName());
	
    @PostUpdate
    public void postUpdate(Object o) {
    	
    	if (o instanceof MultiLanguageObject) {
	    	MultiLanguageObject a = (MultiLanguageObject) o;
	    	logger.debug("postUpdate -> " + a.getDisplayname());    
		    getCommandService().run(new EvictLanguageCacheMLOCommand (a.getObjectClassName() , a.getId()));
	    }
    }
    
    @PostPersist
    public void postPersist(Object o) {
    	if (o instanceof MultiLanguageObject) {
	    	MultiLanguageObject a = (MultiLanguageObject) o;
	    	logger.debug("postPersist -> " + a.getDisplayname());    
		    getCommandService().run(new EvictLanguageCacheMLOCommand (a.getObjectClassName() , a.getId()));

	    }
    }
    
    @PostRemove
    public void postRemove(Object o) {
    	if (o instanceof MultiLanguageObject) {
	    	MultiLanguageObject a = (MultiLanguageObject) o;
	    	logger.debug("postRemove -> " + a.getDisplayname());    
	    	getCommandService().run(new EvictLanguageCacheMLOCommand (a.getObjectClassName() , a.getId()));
	    }
    }

    protected CommandService getCommandService() {
		return (CommandService) ServiceLocator.getInstance().getBean(CommandService.class);
	}
	
	
    /**
      
        @PrePersist: Se ejecuta antes de que la entidad se guarde en la base de datos. 
		@PostPersist: Se ejecuta después de que la entidad ha sido guardada. 
		@PreRemove: Se ejecuta antes de que la entidad se elimine. 
		@PostRemove: Se ejecuta después de que la entidad ha sido eliminada. 
		@PreUpdate: Se ejecuta antes de que la entidad sea actualizada. 
		@PostUpdate: Se ejecuta después de que la entidad ha sido actualizada. 
		@PostLoad
     */
    
}
