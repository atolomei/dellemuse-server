package dellemuse.serverapp;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Set;

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
import dellemuse.serverapp.command.CommandService;
import dellemuse.serverapp.command.EmtyQRCodesCommand;
import dellemuse.serverapp.command.ResourceMetadataCommand;
import dellemuse.serverapp.command.SetDefaultPasswordCommand;
import dellemuse.serverapp.elevenlabs.TestElevenLabsCommand;
import dellemuse.serverapp.security.google.GoogleAuth;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.repository.UserRepository;
import dellemuse.serverapp.serverdb.service.ResourceDBService;
import dellemuse.serverapp.serverdb.service.UserDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;


@Component
public class DellemuseServerAppStartupApplicationRunner implements ApplicationRunner {

	@SuppressWarnings("unused")
	static private Logger logger = Logger.getLogger(DellemuseServerAppStartupApplicationRunner.class.getName());
	static private Logger startupLogger = Logger.getLogger("StartupLogger");
	
	static public final String SEPARATOR = "---------------------------------";
	
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
			args.getNonOptionArgs().forEach( item -> startupLogger.debug(item));
		}

		Locale.setDefault(Locale.ENGLISH);
		
		startupLogger.info(SEPARATOR);
		
		ServerDBSettings settings = getAppContext().getBean(ServerDBSettings.class);
		startupLogger.info    ("App name -> " + settings.getAppName());
		startupLogger.info    ("Port -> "     + settings.getPort());
		 
		startupLogger.info    ("Object Storage endpoint -> " + settings.getObjectStorageUrl());
	    startupLogger.info    ("Object Storage  port -> " + String.valueOf(settings.getObjectStoragePort()));
	     
		startupLogger.info(SEPARATOR);
		
		startupLogger.info	("Startup at -> " + DateTimeFormatter.RFC_1123_DATE_TIME.format(OffsetDateTime.now()));
	
		/**
		CommandService service = (CommandService) ServiceLocator.getInstance().getBean(CommandService.class);
		ResourceDBService rs = (ResourceDBService) ServiceLocator.getInstance().getBean(ResourceDBService.class);
		rs.findAll().forEach( r -> {
			service.run( new ImageCalculateDimensionsCommand(r.getId()));
			logger.debug(r.toString());
		});
		logger.debug("done");
		**/
		
		//CommandService service = (CommandService) ServiceLocator.getInstance().getBean(CommandService.class);
		//service.run( new EmtyQRCodesCommand() );
				
		//
		// ResourceDBService service = (ResourceDBService) ServiceLocator.getInstance().getBean( ResourceDBService.class);
		// service.findAll().forEach( r -> service.checkAndSetSize(r) );
		//
		
		
		//GoogleAuth g = new GoogleAuth();
		//g.execute();
	}
	
	@Bean
	CommandLineRunner init(UserDBService userService, PasswordEncoder encoder) {
	   
		return args -> {
			/**
			CommandService service = (CommandService) ServiceLocator.getInstance().getBean(CommandService.class);
			ResourceDBService r_service = (ResourceDBService) ServiceLocator.getInstance().getBean( ResourceDBService.class);

			
			r_service.findAll().forEach( r -> 
			
			{	logger.debug(r.getName());
			
				if (r.getMedia()!=null && r.getMedia().contains("audio"))
					service.run(new ResourceMetadataCommand(r.getId()));
			}
					);
			*/
			
		};
	}
	
	public ApplicationContext getAppContext() {
		return appContext;
	}
}
