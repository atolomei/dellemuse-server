package dellemuse.server;

import java.time.OffsetDateTime;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.Constant;
import dellemuse.model.logging.Logger;
import dellemuse.server.test.TestListObjects;

@Component
public class DelleMuseStartupApplicationRunner implements ApplicationRunner {

	@SuppressWarnings("unused")
	static private Logger logger = Logger.getLogger(DelleMuseApplication.class.getName());
	static private Logger startupLogger = Logger.getLogger("StartupLogger");

	@JsonIgnore
	private final ApplicationContext appContext;

	
  	 @Autowired
	 TestListObjects test;
	 
    
	
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
		
		startupLogger.info	(Constant.SEPARATOR);
		startupLogger.info	("Startup at -> " + OffsetDateTime.now().toString());
		
		test.test();
		
	}
	
	public ApplicationContext getAppContext() {
		return appContext;
	}
	

}
