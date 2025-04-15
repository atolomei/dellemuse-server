package dellemuse.server;



import java.time.OffsetDateTime;
import java.util.Locale;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.util.Logger;



@Component
public class DelleMuseStartupApplicationRunner implements ApplicationRunner {

	@SuppressWarnings("unused")
	static private Logger logger = Logger.getLogger(DelleMuseApplication.class.getName());
	static private Logger startupLogger = Logger.getLogger("StartupLogger");

	@JsonIgnore
	private final ApplicationContext appContext;

	public DelleMuseStartupApplicationRunner(ApplicationContext appContext) {
		this.appContext = appContext;
	}
	
	@Override
	public void run(ApplicationArguments args) throws Exception {

		if (startupLogger.isDebugEnabled()) {
			startupLogger.debug("Command line args:");
			args.getNonOptionArgs().forEach( item -> startupLogger.debug(item));
			//startupLogger.debug(ServerConstant.SEPARATOR);
		}

		Locale.setDefault(Locale.ENGLISH);
		
		//boolean iGeneral = initGeneral();
		//if(iGeneral)
		//	startupLogger.info(ServerConstant.SEPARATOR);
		
		startupLogger.info	(ServerConstant.SEPARATOR);
		
		startupLogger.info	("Startup at -> " + OffsetDateTime.now().toString());
	}
	
	
	public ApplicationContext getAppContext() {
		return appContext;
	}
	
	
}
