package dellemuse.serverapp.jpa.events;

import dellemuse.model.logging.Logger;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import dellemuse.serverapp.command.CommandService;
 
import dellemuse.serverapp.command.QRSiteCodeGenerationCommand;
 
import dellemuse.serverapp.serverdb.model.Site;
 
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;

public class SiteEventListener {

	static private Logger logger = Logger.getLogger( SiteEventListener.class.getName());
	
	@PrePersist
    public void prePersist(Object entidad) {
    }
 
    @PostUpdate
    public void postUpdate(Object o) {
    	logger.debug("postUpdate");    
    	Site s = (Site) o;
    	getCommandService().run(new QRSiteCodeGenerationCommand(s.getId()));
    }
    
    @PostPersist
    public void postPersist(Object o) {
    	logger.debug("postPersist");    
    	// ArtWork a = (ArtWork) o;
    	// getCommandService().run(new QRCodeGenerationCommand(a.getId()));
    }

    protected CommandService getCommandService() {
		return (CommandService) ServiceLocator.getInstance().getBean(CommandService.class);
	}
	
    protected SiteDBService getSiteDBService() {
    	SiteDBService service = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
    	return service;
    }
	
    /**
      
        @PrePersist: 	Se ejecuta antes de que la entidad se guarde en la base de datos. 
		@PostPersist: 	Se ejecuta después de que la entidad ha sido guardada. 
		@PreRemove: 	Se ejecuta antes de que la entidad se elimine. 
		@PostRemove: 	Se ejecuta después de que la entidad ha sido eliminada. 
		@PreUpdate: 	Se ejecuta antes de que la entidad sea actualizada. 
		@PostUpdate: 	Se ejecuta después de que la entidad ha sido actualizada. 
		@PostLoad
     
     */
    
}
