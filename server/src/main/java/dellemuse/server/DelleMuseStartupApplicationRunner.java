package dellemuse.server;

import java.io.File;
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
import dellemuse.model.util.Constant;
import dellemuse.server.importer.ServerImporter;
import dellemuse.server.objectstorage.ObjectStorageService;
import dellemuse.server.test.TestListObjects;

@Component
public class DelleMuseStartupApplicationRunner implements ApplicationRunner {

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(DelleMuseStartupApplicationRunner.class.getName());
    static private Logger startupLogger = Logger.getLogger("StartupLogger");

    @JsonIgnore
    private final ApplicationContext appContext;

    @Autowired
    TestListObjects test;

    @Autowired
    ServerImporter serverImporter;

    public DelleMuseStartupApplicationRunner(ApplicationContext appContext) {
        this.appContext = appContext;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (startupLogger.isDebugEnabled()) {
            startupLogger.debug("Command line args:");
            args.getNonOptionArgs().forEach(item -> startupLogger.debug(item));
            // startupLogger.debug(ServerConstant.SEPARATOR);
        }

        Locale.setDefault(Locale.ENGLISH);

        boolean iGeneral = initGeneral();
        if (iGeneral)
            startupLogger.info(Constant.SEPARATOR);

        boolean iKeys = initKeys();
        if (iKeys)
            startupLogger.info(Constant.SEPARATOR);

        
        Settings settings=appContext.getBean(Settings.class);
        //ObjectStorageService oservice =appContext.getBean(ObjectStorageService.class);
        
        startupLogger.info    ("App name -> " + settings.getAppName());
        startupLogger.info    ("Port -> "     + settings.getPort());
        
        startupLogger.info    ("Object Storage endpoint -> " + settings.getObjectStorageUrl());
        startupLogger.info    ("Object Storage  port -> " + String.valueOf(settings.getObjectStoragePort()));
        
        startupLogger.info("Startup at -> " + DateTimeFormatter.RFC_1123_DATE_TIME.format(OffsetDateTime.now()));

        test.test();
        //serverImporter.execute();
    }

    public ApplicationContext getAppContext() {
        return appContext;
    }

    private boolean initKeys() {
        Settings settingsService = getAppContext().getBean(Settings.class);
        if (settingsService.getAccessKey().equals("dellemuse") && settingsService.getSecretKey().equals("dellemuse")) {
            startupLogger.info("Dellemuse is running with default vaules for AccessKey and SecretKey (ie. dellemuse/dellemuse)");
            startupLogger.info("It is recommended to change their values in file -> ." + File.separator + "config" + File.separator
                    + "application.properties");
            return true;
        }
        return false;
    }

    private boolean initGeneral() {
        Settings settingsService = getAppContext().getBean(Settings.class);

        startupLogger.info("Https -> " + (settingsService.isHTTPS() ? "true" : "false"));
        startupLogger.info("Port-> " + String.valueOf(settingsService.getPort()));

        return true;
    }

}
