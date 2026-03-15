package dellemuse.serverapp.candidate;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.DellemuseServer;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.command.Command;
import dellemuse.serverapp.email.EmailTemplateService;
import dellemuse.serverapp.serverdb.model.Candidate;
import dellemuse.serverapp.serverdb.model.CandidateStatus;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.PersistentToken;

public class CandidateValidateEmailCommand extends Command {

	static private Logger logger = Logger.getLogger(CandidateValidateEmailCommand.class.getName());

	@JsonProperty("candidateId")
	private Long candidateId;

	public CandidateValidateEmailCommand(Long aId) {
		this.candidateId = aId;
	}

	@Override
	public void execute() {
		
		if (this.candidateId == null) {
			logger.error(this.getClass().getSimpleName() + ": candidateId is null", ServerConstant.NOT_THROWN);
			return;
		}
		 
		Candidate c = getCandidateDBService().findById(candidateId).orElse(null);
		
		if (c == null)
			return;
		
		if (c.isEmailValidated())
    		return;
    	
    	if (c.getValidationEmailSent()!=null)
			return;
    	
    	if (c.getState()==ObjectState.DELETED)
    		return;

    	if (c.getState()==ObjectState.EDITION)
    		return;

    	if (c.getStatus()!=CandidateStatus.SUBMITTED)
    		return;
		
    	
    	if (c.getEmail()==null || c.getEmail().length()==0) {
    		logger.error("email is null", ServerConstant.NOT_THROWN);
    		return;
    	}
    	
    	logger.debug("Executing " + this.getClass().getSimpleName() + " for candidate -> "+ c.getDisplayname());
    	
    
    	
    	// -- token for email validation -------------------------------------------------------
    	//
    	
    	String tokenValue = getSecurityService().nextSecureToken();
    	 
    	@SuppressWarnings("unused")
		PersistentToken token = getPersistentTokenDBServiceDBService().create(
				c.getId().toString(), 
				Candidate.class.getSimpleName(), 
				tokenValue,
				OffsetDateTime.now().plusDays(7) );
    	
    	String personName 		= c.getPersonName() !=null ?  c.getPersonName() : "";
    	String personLastname 	= c.getPersonLastname() !=null ?  c.getPersonLastname() : "";
        String name = (personName + " " + personLastname).trim();
    	
    	String to = c.getEmail();
 		
 		String subject = "candidate validate email";
	    	
 		String url=getServerDBSettings().getEmailValidationServer() + "/"+ DellemuseServer.URL_CANDIDATE_VALIDATE_EMAIL + "/" + c.getId().toString()+"-"+tokenValue+"-"+ c.getLanguage();
 		
 		// --------- Send email to Candidate to validate email -----------
 	   	//
 		 
     	String text= getEmailTemplateService().render(EmailTemplateService.CANDIDATE_EMAIL_VALIDATION, 
 		
     			Map.of(
 				"confirmationLink", url,
 			    "application",  DellemuseServer.APPNAME,
 			    "personName",   name));

 		try {
 	 		String sendEmail;
			sendEmail = getEmailService().sendHTML(to, subject, text);
			
			
			c.setValidationEmailSent(OffsetDateTime.now());
			getCandidateDBService().save(c);
			
			
	 		logger.debug("Candidate email validation response -> " + sendEmail);
	 		
		} catch (IOException | InterruptedException e) {
			logger.error(e, ServerConstant.NOT_THROWN);
		}
 	
 		
 		// --------- Send email to Admin ------------------------------------
 	   	//
 		 
 		try {

 			String textAdmin= getEmailTemplateService().render(EmailTemplateService.CANDIDATE_SUBMT_NOTIFY_ADMIN, 
					Map.of(
				    "application",  DellemuseServer.APPNAME,
				    "name",   		c.getPersonName(),
				    "lastname",  	c.getPersonLastname(),
				    "institution",  c.getInstitutionName(),
				    "address",   	c.getInstitutionAddress(),
				    "email",   		c.getEmail(),
				    "phone",   		c.getPhone(),
				    "comments",   	c.getComments())
				   	);
	
	    	String toAdmin = getRootUser().getEmail();
	    	String subjectAdmin = "Institution registration";
			String sendEmailAdmin = getEmailService().sendText(toAdmin, subjectAdmin, textAdmin);
		
			logger.debug("Email sent response -> " + sendEmailAdmin);
			
 		} catch (IOException | InterruptedException e) {
			logger.error(e, ServerConstant.NOT_THROWN);
		}
 	}
 
}
