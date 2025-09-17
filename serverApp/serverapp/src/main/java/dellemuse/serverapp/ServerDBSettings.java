package dellemuse.serverapp;

import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

//import dellemuse.model.logging.Logger;
import jakarta.annotation.PostConstruct;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.serverdb.ServerDBConstant;

/**
 * <p>
 * Server configuration defined in file {@code application.properties}
 * </p>
 * 
 * @author atolomei@novamens.com (Alejandro Tolomei)
 * 
 */
@Configuration
public class ServerDBSettings {

    static private Logger logger = Logger.getLogger(ServerDBSettings.class.getName());

    private static final OffsetDateTime systemStarted = OffsetDateTime.now();

    
    /** DELLEMUSE WEBAPP ----------------------------------------------------- */

    @Value("${dellemuse.webapp.accessKey:dellemuse}")
    @NonNull
    protected String accessKey;

    @Value("${dellemuse.webapp.secretKey:dellemuse}")
    @NonNull
    protected String secretKey;

    @Value("${server.port:8098}")
    protected int port;

    @Value("${dellemuse.serverapp.endpoint:http://localhost}")
    @NonNull
    protected String endpoint;

    @Value("${server.ssl.enabled:false}")
    protected String ishttps;
    
    @Value("${trafficTokens:10}")
    protected int maxTrafficTokens;

    
    /** **/
    
    @Value("${dispatcher.poolsize:10}")
    protected int poolsize;

    
    
    /** ObjectStorage **/
    
    @Value("${objectstorage.accessKey:odilon}")
    @NonNull
    protected String objectStorageAccessKey;

    @Value("${objectstorage.secretKey:odilon}")
    @NonNull
    protected String objectStorageSecretKey;

    @Value("${objectstorage.url:http://localhost}")
    @NonNull
    protected String objectStorageUrl;
    
    @Value("${objectstorage.port:9234}")
    @NonNull
    protected int objectStoragePort;
    
    @Value("${objectstorage.presigned.url:null}")
    protected String objectStoragePresignedUrl;
    
    @Value("${objectstorage.presigned.port:-1}")
    protected int objectStoragePresignedPort;

    @Value("${objectstorage.presigned.isSSL:null}")
    protected String isSSLStr;

    protected boolean isPresignedSSL; 
    
    
    /** Database */
    
    @Value("${driverClassName:org.postgresql.Driver}")
    protected String driverClassName;
    
    @Value("${database.username:postgres}")
    protected String dbuserName;
    
    @Value("${database. password:novamens}")
    protected String password;
    
    @Value("${database.url:jdbc:postgresql://localhost:5432/dellemuse}")
    protected String dburl;
    
    @Value("${app.name:dellemuseServer}")
    @NonNull
    protected String appName;

    
    /** Work */
    
    @Value("${importer.dir:importer}")
    protected String importerBaseDir;

    @Value("${work.dir:work}")
    protected String workDir;

    
    public ServerDBSettings() {
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
        
        if (objectStoragePresignedUrl==null || objectStoragePresignedUrl.equals("null")) 
            objectStoragePresignedUrl=this.objectStorageUrl.replace("https://", "").replace("http://", "");
        
        if (objectStoragePresignedPort==-1)
            objectStoragePresignedPort=objectStoragePort;

            if (isSSLStr==null || isSSLStr.equals("null"))
                isPresignedSSL = this.isHTTPS();
            else
                isPresignedSSL = isSSLStr.toLowerCase().trim().equals("true");
    }

    
    private void checkDirs() {

        try {
            File wDir = new File (workDir);
            if ((!wDir.exists()) || (!wDir.isDirectory()))
                FileUtils.forceMkdir(wDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        try {
            File thDir = new File (workDir, ServerDBConstant.THUMBNAIL_BUCKET);
            if ((!thDir.exists()) || (!thDir.isDirectory()))
                FileUtils.forceMkdir(thDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
    }

    
    public int getMaxTrafficTokens() {
        return maxTrafficTokens;
    }

    public boolean isHTTPS() {
        return this.ishttps != null && this.ishttps.toLowerCase().trim().equals("true");
    }

    public String getImporterBaseDir() {
        return importerBaseDir;
    }

    public String getObjectStorageAccessKey() {
        return objectStorageAccessKey;
    }

    public void setObjectStorageSAccessKey(String objectStorageAccessKey) {
        this.objectStorageAccessKey = objectStorageAccessKey;
    }

    public String getObjectStorageSecretKey() {
        return objectStorageSecretKey;
    }

    public void setObjectStorageSecretKey(String objectStorageSecretKey) {
        this.objectStorageSecretKey = objectStorageSecretKey;
    }

    public String getObjectStorageUrl() {
        return objectStorageUrl;
    }

    public void setObjectStorageUrl(String objectStorageUrl) {
        this.objectStorageUrl = objectStorageUrl;
    }

    public int getObjectStoragePort() {
           return objectStoragePort;
    }


    public String getAppName() {
        return appName;
    }

    public String getWorkDir() {
        return this.workDir;
    }

    public String getObjectStoragePresignedUrl() {
        return objectStoragePresignedUrl;
    }

    public void setObjectStoragePresignedUrl(String objectStoragePresignedUrl) {
        this.objectStoragePresignedUrl = objectStoragePresignedUrl;
    }

    public int getObjectStoragePresignedPort() {
        return objectStoragePresignedPort;
    }

    public void setObjectStoragePresignedPort(int objectStoragePresignedPort) {
        this.objectStoragePresignedPort = objectStoragePresignedPort;
    }

    public boolean isObjectStoragePresignedSSL() {
        return isPresignedSSL;
    }

    public void setObjectStoragePresignedSSL(boolean isPresignedSSL) {
        this.isPresignedSSL = isPresignedSSL;
    }

	public int getDispatcherPoolSize() {
		return this.poolsize;
	}


}

