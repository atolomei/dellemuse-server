
package dellemuse.server.objectstorage;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.Constant;
import dellemuse.model.util.TimerThread;
import dellemuse.server.BaseService;
import dellemuse.server.ServerConstant;
import dellemuse.server.Settings;
import dellemuse.server.SystemService;
import io.odilon.client.ODClient;
import io.odilon.client.OdilonClient;
import io.odilon.client.error.ODClientException;
import io.odilon.errors.InternalCriticalException;
import jakarta.annotation.PostConstruct;

@Service
public class ObjectStorageService extends BaseService implements SystemService {

    static private Logger logger = Logger.getLogger(ObjectStorageService.class.getName());

    static private Logger startupLogger = Logger.getLogger("StartupLogger");

    @JsonIgnore
    private OdilonClient client;

    @JsonProperty("endpoint")
    private String endpoint;

    @JsonProperty("port")
    private int port;

    @JsonProperty("accessKey")
    private String accessKey;

    @JsonProperty("secretKey")
    private String secretKey;

    @JsonIgnore
    private TimerThread timerConnect;
    
    
    public ObjectStorageService(Settings settings) {
        super(settings);
    }

    public void putObject(String bucketName, String objectName, InputStream stream, String fileName) throws IOException {
        try {
            getClient().putObjectStream(bucketName, objectName, stream, fileName);
        } catch (ODClientException e) {
            throw new IOException(e);
        }
    }

    public InputStream getObject(String bucketName, String objectName) throws IOException {
        try {
            return getClient().getObject(bucketName, objectName);
        } catch (ODClientException e) {
            throw new IOException(e);
        }
    }

    public OdilonClient getClient() {
        return this.client;
    }

    
    @PostConstruct
    protected void onInit() {

        this.endpoint = getSettings().getObjectStorageUrl();
        this.port = getSettings().getObjectStoragePort();
        this.accessKey = getSettings().getObjectStorageAccessKey();
        this.secretKey = getSettings().getObjectStorageSecretKey();

        connect();

        this.timerConnect = new TimerThread() {
            
            public long getSleepTimeMillis() {
                return Constant.DEFAULT_SLEEP_TIME*1;
            }
            
            @Override
            public void onTimer() {

                String ping;
                boolean requireReconnect = false;
                try {
                    ping = getClient().ping();
                    if (ping != null && ping.equals("ok")) {
                        requireReconnect = false;
                    } else {
                        requireReconnect = true;
                    }

                } catch (Exception e) {
                    logger.error(e);
                    logger.error("Server will try to reconnect to -> " + getEndPoint());
                    requireReconnect = true;
                }

                if (requireReconnect) {
                    try {
                        connect();
                    } catch (Exception e) {
                        logger.error(e);
                    }
                }
            }
        };

        Thread thread = new Thread(timerConnect);
        thread.setDaemon(true);
        thread.setName(ObjectStorageService.class.getSimpleName() + " - connection timer");
        thread.start();

        startupLogger.info("Connected to Odilon server -> " + this.client.getSchemaAndHost() + ":" + String.valueOf( this.port ));

        startupLogger.debug(this.toString());
        startupLogger.debug("Startup -> " + this.getClass().getSimpleName());
    }

    protected String getEndPoint() {
        return this.endpoint;
    }
    
    
    private synchronized void connect() {
        
        this.client = new ODClient(this.endpoint, this.port, this.accessKey, this.secretKey);

        this.client.setPresignedUrl(    getSettings().getObjectStoragePresignedUrl(),
                                        getSettings().getObjectStoragePresignedPort(), 
                                        getSettings().isObjectStoragePresignedSSL()
                                   );
        
        String ping = this.client.ping();

        if (ping == null || !ping.equals("ok"))
            throw new InternalCriticalException("Pïng error -> " + ping);
        try {

            if (!this.client.existsBucket(ServerConstant.MEDIA_BUCKET)) {
                startupLogger.debug("Creating bucket -> " + ServerConstant.MEDIA_BUCKET);
                this.client.createBucket(ServerConstant.MEDIA_BUCKET);
            }

            if (!this.client.existsBucket(ServerConstant.THUMBNAIL_BUCKET)) {
                startupLogger.debug("Creating bucket -> " + ServerConstant.THUMBNAIL_BUCKET);
                this.client.createBucket(ServerConstant.THUMBNAIL_BUCKET);
            }

        } catch (ODClientException e) {
            throw new InternalCriticalException(e);
        }
   
        logger.debug("Connected to Odilon server -> " + this.client.getSchemaAndHost() + ":" + String.valueOf( this.port ));
        
    }
}
