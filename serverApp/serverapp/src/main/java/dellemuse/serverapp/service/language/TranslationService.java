package dellemuse.serverapp.service.language;


import java.io.FileInputStream;
import java.time.OffsetDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.ServiceStatus;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.record.SiteRecord;
import dellemuse.serverapp.serverdb.model.record.TranslationRecord;
import dellemuse.serverapp.serverdb.service.base.BaseService;
import dellemuse.serverapp.service.LockService;
import jakarta.annotation.PostConstruct;

@Service
public class TranslationService extends BaseService {

	private static Logger logger = Logger.getLogger(TranslationService.class.getName());
 
	static private Logger startupLogger = Logger.getLogger("StartupLogger");
	
	@JsonIgnore
	private Translate translate;

	@JsonIgnore
	private AtomicBoolean serviceEnabled = new AtomicBoolean(false);
		
	private final OffsetDateTime created = OffsetDateTime.now();

	@JsonIgnore
	@Autowired
	private final LockService lockService;

	/**
	 * 
	 * 
	 * @param settings
	 * @param lockService
	 */
	public TranslationService(ServerDBSettings settings, LockService lockService) {
		super(settings);
		this.lockService=lockService;
	}

	public boolean translateSite(Site src, SiteRecord dest) {
		
		if (src==null || dest==null  ) {
			logger.error("null values not valid");
			return false;
		}
		
		if (!isServiceEnabled()) {
			logger.error("Translation Service is not enabled | can not translate -> " + src.getDisplayname() +" to " + dest.getLanguage());
			return false;
		}
		
		getLockService().getObjectLock(src.getId()).writeLock().lock();
		
		try {
		
			String name=src.getName();
			String subtitle=src.getSubtitle();
			String info=src.getInfo();
			String intro = src.getIntro();
			String spec = src.getSpec();
	 		
			String address = src.getAddress();
			String opens = src.getOpens();	
			//String abstract = src.getAbstract();

			
			//
			// hashcode name
			// if hashcode name == saved hashcode name -> no llama a google
			//
			boolean translated = false;
			
			if (requiresTranslation(name, dest.getName(), dest.getNameHash())) {
						
				Translation translation = translate.translate(	name, 
																Translate.TranslateOption.format("text"),
																Translate.TranslateOption.sourceLanguage(src.getMasterLanguage()),
																Translate.TranslateOption.targetLanguage(dest.getLanguage()));
						 
						String name_trad = translation.getTranslatedText();
						int name_hash = hash(name);
						
						dest.setName(name_trad);
						dest.setNameHash(name_hash);
				
						translated=true;
			}
	
			if (requiresTranslation(subtitle, dest.getSubtitle(), dest.getSubtitleHash())) {
				
				
				Translation translation = translate.translate(	subtitle, 
						Translate.TranslateOption.format("text"),
						Translate.TranslateOption.sourceLanguage(src.getMasterLanguage()),
						Translate.TranslateOption.targetLanguage(dest.getLanguage()));

				
				String subtitle_trad = translation.getTranslatedText();
				
				
				//subtitle_trad = subtitle_trad.replace("&#39;", "'");
				int subtitle_hash = hash(subtitle);
				
				dest.setSubtitle(subtitle_trad);
				dest.setSubtitleHash(subtitle_hash);
				translated=true;
			}
			
			
			if (requiresTranslation(info, dest.getInfo(), dest.getInfoHash())) {
				
				Translation translation = translate.translate(info, 
						Translate.TranslateOption.format("text"),
						Translate.TranslateOption.sourceLanguage(src.getMasterLanguage()),
						Translate.TranslateOption.targetLanguage(dest.getLanguage()));
				
				String info_trad = translation.getTranslatedText();
				int info_hash = hash(info);
				
				dest.setInfo(info_trad);
				dest.setInfoHash(info_hash);
				translated=true;
			}
			
			

			if (requiresTranslation(intro, dest.getIntro(), dest.getIntroHash())) {
				
				Translation translation = translate.translate(intro, 
						Translate.TranslateOption.format("text"),
						Translate.TranslateOption.sourceLanguage(src.getMasterLanguage()),
						Translate.TranslateOption.targetLanguage(dest.getLanguage()));
				
				String intro_trad = translation.getTranslatedText();
				int intro_hash = hash(intro);
				
				dest.setIntro(intro_trad);
				dest.setIntroHash(intro_hash);
				translated=true;
			}
			
			
			
			if (requiresTranslation(address, dest.getAddress(), dest.getAddressHash())) {
				
				Translation translation = translate.translate(address, 
						Translate.TranslateOption.format("text"),
						Translate.TranslateOption.sourceLanguage(src.getMasterLanguage()),
						Translate.TranslateOption.targetLanguage(dest.getLanguage()));
				
				String address_trad = translation.getTranslatedText();
				int address_hash = hash(address);
				
				dest.setAddress(address_trad);
				dest.setAddressHash(address_hash);
				translated=true;
			}
			

			
			if (requiresTranslation(opens, dest.getAddress(), dest.getOpensHash())) {
				
				Translation translation = translate.translate(opens, 
						Translate.TranslateOption.format("text"),
						Translate.TranslateOption.sourceLanguage(src.getMasterLanguage()),
						Translate.TranslateOption.targetLanguage(dest.getLanguage()));
				
				String opens_trad = translation.getTranslatedText();
				int opens_hash = hash(opens);
				
				dest.setOpens(opens_trad);
				dest.setOpensHash(opens_hash);
				translated=true;
			}
			
			
			if (requiresTranslation(spec, dest.getSpec(), dest.getSpecHash())) {
				
				Translation translation = translate.translate(spec, 
						Translate.TranslateOption.format("text"),
						Translate.TranslateOption.sourceLanguage(src.getMasterLanguage()),
						Translate.TranslateOption.targetLanguage(dest.getLanguage()));
				
				String spec_trad = translation.getTranslatedText();
				int spec_hash = hash(spec);
				
				dest.setSpec(spec_trad);
				dest.setSpecHash(spec_hash);
				translated=true;
			}



			return translated;
		
		} finally {
			getLockService().getObjectLock(src.getId()).writeLock().unlock();
		}
	}

	
 	/**
 	 * 
 	 * @param src
 	 * @param dest
 	 * @return
 	 */
	public boolean translate(MultiLanguageObject src, TranslationRecord dest) {
		
		if (src==null || dest==null  ) {
			logger.error("null values not valid");
			return false;
		}
		
		
		if (!isServiceEnabled()) {
			logger.error("Translation Service is not enabled | can not translate -> " + src.getDisplayname() +" to " + dest.getLanguage());
			return false;
		}
		
		getLockService().getObjectLock(src.getId()).writeLock().lock();
		
		try {
		
			String name=src.getName();
			String subtitle=src.getSubtitle();
			String info=src.getInfo();
			String intro = src.getIntro();
			String spec = src.getSpec();	
			String opens = src.getOpens();
			
		 	boolean translated = false;
			
			if (requiresTranslation(name, dest.getName(), dest.getNameHash())) {
						
				if (name==null) {
					dest.setName(null);
					dest.setNameHash(0);
					translated=true;
				}
				else {
					
				Translation translation = translate.translate(	name, 
																Translate.TranslateOption.format("text"),
																Translate.TranslateOption.sourceLanguage(src.getMasterLanguage()),
																Translate.TranslateOption.targetLanguage(dest.getLanguage()));
						 
						String name_trad = translation.getTranslatedText();
						int name_hash = hash(name);
						
						dest.setName(name_trad);
						dest.setNameHash(name_hash);
				
						translated=true;
				}
			}
	
			if (requiresTranslation(subtitle, dest.getSubtitle(), dest.getSubtitleHash())) {
				
				
				if (subtitle==null) {
					dest.setSubtitle(null);
					dest.setSubtitleHash(0);
					translated=true;
				}
				else {
				Translation translation = translate.translate(	subtitle, 
						Translate.TranslateOption.format("text"),
						Translate.TranslateOption.sourceLanguage(src.getMasterLanguage()),
						Translate.TranslateOption.targetLanguage(dest.getLanguage()));

				
				String subtitle_trad = translation.getTranslatedText();
				int subtitle_hash = hash(subtitle);
				
				dest.setSubtitle(subtitle_trad);
				dest.setSubtitleHash(subtitle_hash);
				translated=true;
				}
				
			}
			
			
			if (requiresTranslation(info, dest.getInfo(), dest.getInfoHash())) {
				
				if (info==null) {
					dest.setInfo(null);
					dest.setInfoHash(0);
					translated=true;
				}
				else {
					
					
					Translation translation = translate.translate(info, 
							Translate.TranslateOption.format("text"),
							Translate.TranslateOption.sourceLanguage(src.getMasterLanguage()),
							Translate.TranslateOption.targetLanguage(dest.getLanguage()));
					
					String info_trad = translation.getTranslatedText();
					int info_hash = hash(info);
					
					dest.setInfo(info_trad);
					dest.setInfoHash(info_hash);
					translated=true;
				}
			}
			
			

			if (requiresTranslation(intro, dest.getIntro(), dest.getIntroHash())) {
				
				if (intro==null) {
					dest.setIntro(null);
					dest.setIntroHash(0);
					translated=true;
					
				}
				else {
					Translation translation = translate.translate(intro, 
							Translate.TranslateOption.format("text"),
							Translate.TranslateOption.sourceLanguage(src.getMasterLanguage()),
							Translate.TranslateOption.targetLanguage(dest.getLanguage()));
					
					String intro_trad = translation.getTranslatedText();
					int intro_hash = hash(intro);
					
					dest.setIntro(intro_trad);
					dest.setIntroHash(intro_hash);
					translated=true;
				}
			}
			
			
			
			if (requiresTranslation(spec, dest.getSpec(), dest.getSpecHash())) {
				
				Translation translation = translate.translate(spec, 
						Translate.TranslateOption.format("text"),
						Translate.TranslateOption.sourceLanguage(src.getMasterLanguage()),
						Translate.TranslateOption.targetLanguage(dest.getLanguage()));
				
				String spec_trad = translation.getTranslatedText();
				int spec_hash = hash(spec);
				
				dest.setSpec(spec_trad);
				dest.setSpecHash(spec_hash);
				translated=true;
			}
			
			
			if (requiresTranslation(opens, dest.getOpens(), dest.getOpensHash())) {
				
				Translation translation = translate.translate(opens, 
						Translate.TranslateOption.format("text"),
						Translate.TranslateOption.sourceLanguage(src.getMasterLanguage()),
						Translate.TranslateOption.targetLanguage(dest.getLanguage()));
				
				String opens_trad = translation.getTranslatedText();
				int opens_hash = hash(opens);
				
				dest.setOpens(opens_trad);
				dest.setOpensHash(opens_hash);
				translated=true;
			}
			
			return translated;
		}
		catch (Exception e) {
			
			logger.error(e);
			
			throw(e);
			
			
		} finally {
			getLockService().getObjectLock(src.getId()).writeLock().unlock();
		}
	}
 
	public OffsetDateTime getOffsetDateTimeCreated() {
		return this.created;
	}
 	
	@PostConstruct
	protected void onInit() {
		try {
			initializeTranslateClient();
			setStatus(ServiceStatus.RUNNING);
		} catch (Exception e) {
			setStatus(ServiceStatus.STOPPED);
		}
	}
	
	public boolean isServiceEnabled() {
		return this.serviceEnabled.get();
	}
	
	private void initializeTranslateClient() {
	       
		if (translate == null) {
			
	            try {
	            	
	            	String filePath = getSettings().getGoogleTranslateAuthPath();
	           
  	            	GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(filePath));

	                this.translate = TranslateOptions.newBuilder()
	                  .setCredentials(credentials)
	                  .build()
	                  .getService();
	                
	               	serviceEnabled.set(true);
	               	
	               	startupLogger.info("Google Translate client initialized.");
	                
	            } catch (Exception e) {
	            	
                	serviceEnabled.set(false);
                	startupLogger.error(e, "Failed to initialize Google Translate client.");
	            
	            }
	        }
	    }

	
	private boolean requiresTranslation( String src, String dest, int current_hash) {
		
		if (src==null && dest!=null)
			return false;
		
		if (dest==null && src!=null)
			return true;
		
		if (dest==null && src==null)
			return false;
		
		
		int name_hash = hash(src); 
		
		if (name_hash==current_hash)
			return false;
		
		return true;
	}
	
	private int hash(String str) {
		if (str==null)
			return 0;
		return str.trim().toLowerCase().hashCode();
	}

	private LockService getLockService() {
		return this.lockService;
	}

}
