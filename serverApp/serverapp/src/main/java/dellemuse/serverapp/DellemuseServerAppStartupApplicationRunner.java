package dellemuse.serverapp;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.ArtWorkDBService;
import dellemuse.serverapp.serverdb.service.ArtistDBService;
import dellemuse.serverapp.serverdb.service.ResourceDBService;
import dellemuse.serverapp.serverdb.service.UserDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.odilon.client.error.ODClientException;
import io.odilon.model.ObjectMetadata;

@Component
public class DellemuseServerAppStartupApplicationRunner implements ApplicationRunner {

	@SuppressWarnings("unused")
	static private Logger logger = Logger.getLogger(DellemuseServerAppStartupApplicationRunner.class.getName());
	static private Logger startupLogger = Logger.getLogger("StartupLogger");

	// static public final String SEPARATOR = "---------------------------------";

	@Autowired
	@JsonIgnore
	private final ApplicationContext appContext;

	public DellemuseServerAppStartupApplicationRunner(ApplicationContext appContext) {
		this.appContext = appContext;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

		if (startupLogger.isDebugEnabled()) {
			startupLogger.debug("Command line args:");
			args.getNonOptionArgs().forEach(item -> startupLogger.debug(item));
		}

		Locale.setDefault(Locale.ENGLISH);

		startupLogger.info(ServerAppConstant.SEPARATOR);

		ServerDBSettings settings = getAppContext().getBean(ServerDBSettings.class);
		startupLogger.info("App name -> " + settings.getAppName());
		startupLogger.info("Port -> " + settings.getPort());

		startupLogger.info("Object Storage endpoint -> " + settings.getObjectStorageUrl());
		startupLogger.info("Object Storage  port -> " + String.valueOf(settings.getObjectStoragePort()));

		startupLogger.info(ServerAppConstant.SEPARATOR);

		
		ArtistDBService s= getAppContext().getBean(ArtistDBService.class);
		s.onInitialize();
		
		
		startupLogger.info("Startup at -> " + DateTimeFormatter.RFC_1123_DATE_TIME.format(OffsetDateTime.now()));

		/**
		 * CommandService service = (CommandService)
		 * ServiceLocator.getInstance().getBean(CommandService.class); ResourceDBService
		 * rs = (ResourceDBService)
		 * ServiceLocator.getInstance().getBean(ResourceDBService.class);
		 * rs.findAll().forEach( r -> { service.run( new
		 * ImageCalculateDimensionsCommand(r.getId())); logger.debug(r.toString()); });
		 * logger.debug("done");
		 **/

		// CommandService service = (CommandService)
		// ServiceLocator.getInstance().getBean(CommandService.class);
		// service.run( new EmtyQRCodesCommand() );

		//
		// ResourceDBService service = (ResourceDBService)
		// ServiceLocator.getInstance().getBean( ResourceDBService.class);
		// service.findAll().forEach( r -> service.checkAndSetSize(r) );
		//

		// GoogleAuth g = new GoogleAuth();
		// g.execute();
	}

	/**
	@Bean
	CommandLineRunner updateResourcesMetadata() {
		
		return args -> {
			
			ResourceDBService resourceDBService = ((ResourceDBService) ServiceLocator.getInstance().getBean(ResourceDBService.class));
			ObjectStorageService objectStorageService = ((ObjectStorageService) ServiceLocator.getInstance().getBean(ObjectStorageService.class));
			
			resourceDBService.findAll().forEach( item -> {
				
				try {

					ObjectMetadata meta = objectStorageService.getClient().getObjectMetadata(item.getBucketName(), item.getObjectName());
					
					if (!meta.isPublicAccess()) {
						meta = objectStorageService.getClient().setPublicAccess(item.getBucketName(), item.getObjectName(), true );
						logger.debug(item.getBucketName(), item.getObjectName() + " -> " + (meta.isPublicAccess() ? "true" : "false"));
					}
					
					
				} catch (ODClientException e) {
					 logger.error(e);
				}
			});
		};
	}
	**/
	
/**	@Bean
	CommandLineRunner audioIds() {

		return args -> {
	
			
			UserDBService userDBService = (UserDBService) ServiceLocator.getInstance().getBean(UserDBService.class);
			ArtWorkDBService artWorkDBService = ((ArtWorkDBService) ServiceLocator.getInstance().getBean(ArtWorkDBService.class));
			
			artWorkDBService.findAll().forEach( a-> {
				logger.debug(a.getDisplayname() );
				artWorkDBService.generateAudioId( a, userDBService.findRoot() );
			});
		};
	}
	**/
	

	@Bean
	CommandLineRunner init(UserDBService userService, PasswordEncoder encoder) {

		return args -> {
			/**
			 * ((InstitutionDBService)
			 * ServiceLocator.getInstance().getBean(InstitutionDBService.class)).initAudit();
			 * 
			 * ((SiteDBService)
			 * ServiceLocator.getInstance().getBean(SiteDBService.class)).initAudit();
			 * ((SiteRecordDBService)
			 * ServiceLocator.getInstance().getBean(SiteRecordDBService.class)).initAudit();
			 * 
			 * 
			 * ((ArtExhibitionDBService)
			 * ServiceLocator.getInstance().getBean(ArtExhibitionDBService.class)).initAudit();
			 * ((ArtExhibitionRecordDBService)
			 * ServiceLocator.getInstance().getBean(ArtExhibitionRecordDBService.class)).initAudit();
			 * 
			 * ((ArtExhibitionGuideDBService)
			 * ServiceLocator.getInstance().getBean(ArtExhibitionGuideDBService.class)).initAudit();
			 * ((ArtExhibitionGuideRecordDBService)
			 * ServiceLocator.getInstance().getBean(ArtExhibitionGuideRecordDBService.class)).initAudit();
			 * 
			 * ((UserDBService)
			 * ServiceLocator.getInstance().getBean(UserDBService.class)).initAudit();
			 * 
			 * ((PersonDBService)
			 * ServiceLocator.getInstance().getBean(PersonDBService.class)).initAudit();
			 * ((PersonRecordDBService)
			 * ServiceLocator.getInstance().getBean(PersonRecordDBService.class)).initAudit();
			 * 
			 * ((ResourceDBService)
			 * ServiceLocator.getInstance().getBean(ResourceDBService.class)).initAudit();
			 * 
			 * 
			 * ((ArtWorkDBService)
			 * ServiceLocator.getInstance().getBean(ArtWorkDBService.class)).initAudit();
			 * ((ArtWorkRecordDBService)
			 * ServiceLocator.getInstance().getBean(ArtWorkRecordDBService.class)).initAudit();
			 * 
			 * 
			 * ((ArtExhibitionItemDBService)
			 * ServiceLocator.getInstance().getBean(ArtExhibitionItemDBService.class)).initAudit();
			 * ((ArtExhibitionItemRecordDBService)
			 * ServiceLocator.getInstance().getBean(ArtExhibitionItemRecordDBService.class)).initAudit();
			 * 
			 * 
			 * ((GuideContentDBService)
			 * ServiceLocator.getInstance().getBean(GuideContentDBService.class)).initAudit();
			 * ((GuideContentRecordDBService)
			 * ServiceLocator.getInstance().getBean(GuideContentRecordDBService.class)).initAudit();
			 * 
			 * ((AudioStudioDBService)
			 * ServiceLocator.getInstance().getBean(AudioStudioDBService.class)).initAudit();
			 **/

			// SiteDBService service = (SiteDBService)
			// ServiceLocator.getInstance().getBean(SiteDBService.class);
			// service.findAll().forEach( s -> service.createSequence(s));
			/**
			 * CommandService service = (CommandService)
			 * ServiceLocator.getInstance().getBean(CommandService.class); ResourceDBService
			 * r_service = (ResourceDBService) ServiceLocator.getInstance().getBean(
			 * ResourceDBService.class);
			 * 
			 * 
			 * r_service.findAll().forEach( r ->
			 * 
			 * { logger.debug(r.getName());
			 * 
			 * if (r.getMedia()!=null && r.getMedia().contains("audio")) service.run(new
			 * ResourceMetadataCommand(r.getId())); } );
			 */

			/**
			 * final User root = ((UserDBService)
			 * ServiceLocator.getInstance().getBean(UserDBService.class)).findRoot();
			 * 
			 * { final RoleInstitutionDBService rs=(RoleInstitutionDBService)
			 * ServiceLocator.getInstance().getBean(RoleInstitutionDBService.class); final
			 * InstitutionDBService is=(InstitutionDBService)
			 * ServiceLocator.getInstance().getBean(InstitutionDBService.class);
			 * 
			 * is.findAll().forEach( i -> {
			 * 
			 * if (!rs.findByInstitution(i).iterator().hasNext()) { rs.create("admin", i,
			 * root); } }); }
			 **/

			/**
			 * { final User root = ((UserDBService)
			 * ServiceLocator.getInstance().getBean(UserDBService.class)).findRoot(); final
			 * RoleSiteDBService rs=(RoleSiteDBService)
			 * ServiceLocator.getInstance().getBean(RoleSiteDBService.class); final
			 * SiteDBService is=(SiteDBService)
			 * ServiceLocator.getInstance().getBean(SiteDBService.class);
			 * 
			 * is.findAll().forEach( i -> {
			 * 
			 * //if (!rs.findBySite(i).iterator().hasNext()) { // rs.create("editor", i,
			 * root); //}
			 * 
			 * //if (!rs.findBySite(i).iterator().hasNext()) { // rs.create("admin", i,
			 * root); //} }); }
			 **/
		};
	}

	public ApplicationContext getAppContext() {
		return appContext;
	}
}
