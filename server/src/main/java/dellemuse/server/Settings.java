package dellemuse.server;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.time.OffsetDateTime;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

//import dellemuse.model.logging.Logger;
import jakarta.annotation.PostConstruct;

import dellemuse.model.logging.Logger;

/**
 * <p>
 * Server configuration defined in file {@code application.properties}
 * </p>
 * 
 * @author atolomei@novamens.com (Alejandro Tolomei)
 * 
 */
@Configuration
public class Settings {

    static private Logger logger = Logger.getLogger(Settings.class.getName());

    private static final OffsetDateTime systemStarted = OffsetDateTime.now();

    /* default -> dellemuse */
    @Value("${accessKey:dellemuse}")
    @NonNull
    protected String accessKey;

    /* default -> dellemuse */
    @Value("${secretKey:dellemuse}")
    @NonNull
    protected String secretKey;
    

    /* default port -> 9876 */
    @Value("${server.port:9876}")
    protected int port;

    @Value("${server.ssl.enabled:false}")
    protected String ishttps;
    
    
    @Value("${app.name:dellemuse}")
    @NonNull
    protected String appName;

    @Value("${driverClassName:org.postgresql.Driver}")
    protected String driverClassName;
    
    @Value("${database.username:postgres}")
    protected String dbuserName;
    
    @Value("${database. password:novamens}")
    protected String password;
    
    @Value("${database.url:jdbc:postgresql://localhost:5432/dellemuse}")
    protected String dburl;

    
    public Settings() {
    }
    
    public OffsetDateTime getSystemStartTime() {
        return systemStarted;
    }
    
    public String getDriverClassName() {
        return driverClassName;
    }

    public String getDBUrl() {
        return dburl;
    }
    
    public String getDBUserName() {
        return dbuserName;
    }
    
    public String getDBPassword() {
        return password;
    }

    public int getPort() {
        return port;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }
    
    
    @PostConstruct
    protected void onInitialize() {
        
        checkDirs();
    }

    
    private void checkDirs() {

        //try {
        //    if ((!personImporterDir.exists()) || (!personImporterDir.isDirectory()))
        //        FileUtils.forceMkdir(personImporterDir);
        //} catch (IOException e) {
        //    throw new RuntimeException(e);
        //}
    }

    
    @Value("${trafficTokens:10}")
    protected int maxTrafficTokens;

    public int getMaxTrafficTokens() {
        return maxTrafficTokens;
    }

    public boolean isHTTPS() {
        return this.ishttps != null && this.ishttps.toLowerCase().trim().equals("true");
    }


}

