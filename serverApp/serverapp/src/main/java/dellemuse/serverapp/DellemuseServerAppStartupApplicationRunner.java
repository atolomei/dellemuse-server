package dellemuse.serverapp;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.serverdb.service.ResourceDBService;
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

		// Locale.setDefault(Locale.forLanguageTag("en"));

		Locale.setDefault(Locale.ENGLISH);
		
		startupLogger.info(SEPARATOR);
		
		ServerDBSettings settings = getAppContext().getBean(ServerDBSettings.class);
		startupLogger.info    ("App name -> " + settings.getAppName());
		startupLogger.info    ("Port -> "     + settings.getPort());
		 
		startupLogger.info    ("Object Storage endpoint -> " + settings.getObjectStorageUrl());
	    startupLogger.info    ("Object Storage  port -> " + String.valueOf(settings.getObjectStoragePort()));
	     
		startupLogger.info(SEPARATOR);
		
		startupLogger.info	("Startup at -> " + DateTimeFormatter.RFC_1123_DATE_TIME.format(OffsetDateTime.now()));
		

		//
		// ResourceDBService service = (ResourceDBService) ServiceLocator.getInstance().getBean( ResourceDBService.class);
		// service.findAll().forEach( r -> service.checkAndSetSize(r) );
		//
		
	}
	
	public ApplicationContext getAppContext() {
		return appContext;
	}
}
