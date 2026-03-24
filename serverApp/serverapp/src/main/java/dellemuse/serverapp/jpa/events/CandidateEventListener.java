package dellemuse.serverapp.jpa.events;

import dellemuse.model.logging.Logger;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import dellemuse.serverapp.candidate.CandidateValidateEmailCommand;
import dellemuse.serverapp.command.CommandService;
 
import dellemuse.serverapp.serverdb.model.Candidate;
import dellemuse.serverapp.serverdb.model.CandidateStatus;
 
import dellemuse.serverapp.serverdb.service.CandidateDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;


public class CandidateEventListener {

	static private Logger logger = Logger.getLogger( CandidateEventListener.class.getName());
	
	@PrePersist
    public void prePersist(Object entidad) {
    }

    @PostUpdate
    public void postUpdate(Object o) {
    	
    	logger.debug("postUpdate");    

    	Candidate c = (Candidate) o;

    	if (c.isEmailValidated())
    		return;
    	
    	if (c.getValidationEmailSent()!=null)
			return;
    	
    	if (c.getStatus()!=CandidateStatus.SUBMITTED)
    		return;
    	
    	logger.debug("Scheduling email validation for candidate -> : "+ c.getDisplayname());
    	
    	getCommandService().run(new CandidateValidateEmailCommand(c.getId()));
    }

    @PostPersist
    public void postPersist(Object o) {
    	logger.debug("postPersist");    
    }

    protected CommandService getCommandService() {
		return (CommandService) ServiceLocator.getInstance().getBean(CommandService.class);
	}
	
    protected CandidateDBService getCandidateDBService() {
    	CandidateDBService service = (CandidateDBService) ServiceLocator.getInstance().getBean(CandidateDBService.class);
    	return service;
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
