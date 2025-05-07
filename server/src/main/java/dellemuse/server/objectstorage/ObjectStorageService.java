package dellemuse.server.objectstorage;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.server.BaseService;
import dellemuse.server.Settings;
import dellemuse.server.SystemService;
import io.odilon.client.ODClient;
import io.odilon.client.OdilonClient;
import io.odilon.client.error.ODClientException;
import io.odilon.errors.InternalCriticalException;
import jakarta.annotation.PostConstruct;


@Service
public class ObjectStorageService extends BaseService implements SystemService {

   @JsonIgnore
   private      OdilonClient client;
   
   private      String endpoint;
   
   private      int port;
   
   private      String accessKey;
   
   @JsonIgnore
   private      String secretKey;
 
    
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
     
        this.client = new ODClient(this.endpoint, this.port, this.accessKey, this.secretKey);
        
        String ping = this.client.ping();
        
        if (ping==null || !ping.equals("ok"))
            throw new InternalCriticalException( "Pïng error -> " + ping );
        
    }
}
