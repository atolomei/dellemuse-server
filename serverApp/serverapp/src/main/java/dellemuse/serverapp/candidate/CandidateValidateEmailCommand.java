package dellemuse.serverapp.candidate;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.commons.io.FilenameUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.FSUtil;
import dellemuse.serverapp.DelleMuseServerDBVersion;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.command.Command;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Candidate;
import dellemuse.serverapp.serverdb.model.CandidateStatus;
import dellemuse.serverapp.serverdb.model.PersistentToken;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.ArtWorkDBService;
import dellemuse.serverapp.serverdb.service.ResourceDBService;
import dellemuse.serverapp.serverdb.service.UserDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;

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
    	
    	if (c.getStatus()!=CandidateStatus.SUBMITTED)
    		return;
		
    	
    	logger.debug("Executing " + this.getClass().getSimpleName() + " for candidate -> "+ c.getDisplayname());
    	
    	
    	String tokenValue = getSecurityService().nextSecureToken();
    	 
    	PersistentToken token = getPersistentTokenDBServiceDBService().create(
				c.getId().toString(), 
				Candidate.class.getSimpleName(), 
				tokenValue,
				OffsetDateTime.now().plusDays(7) );
    	
    	Long tid = null;
    
    	try {
    		
    		tid = token.getId();
    		
    		String from = getSettings().getEmailFrom();
    		String to = c.getEmail();
    		
    		String subject = "DelleMuse - Please validate your email address";
    		String text = "please click the following link to validate your email address: " + getSettings().getEmailValidationServer() + "/candidate-validate-email?token=" +c.getId().toString()+"-"+tokenValue;
    		
    		String sendEmail= getEmailService().send(from, to, subject, text);
    	
    		logger.debug("Email sent response -> " + sendEmail);
    		
    	} catch (Exception e) {
    		if (tid!=null)
    			getPersistentTokenDBServiceDBService().findById(tid).ifPresent(t -> getPersistentTokenDBServiceDBService().delete(t));
    		logger.error(e, ServerConstant.NOT_THROWN);
		}
 	}
 

}
