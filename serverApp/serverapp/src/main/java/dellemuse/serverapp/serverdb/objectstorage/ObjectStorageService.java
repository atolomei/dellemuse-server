
package dellemuse.serverapp.serverdb.objectstorage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.Constant;
import dellemuse.model.util.TimerThread;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.service.base.BaseService;
import dellemuse.serverapp.service.SystemService;
import io.odilon.client.ODClient;
import io.odilon.client.OdilonClient;
import io.odilon.client.error.ODClientException;
import io.odilon.errors.InternalCriticalException;
import io.odilon.model.ObjectMetadata;
import io.odilon.model.list.Item;
import io.odilon.model.list.ResultSet;
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

	public ObjectStorageService(ServerDBSettings settings) {
		super(settings);
	}
	
	
	public String getPublicUrl( Resource resource)  throws IOException {
		try {
			
			if (resource.isPublicAccess())
				return getClient().getPublicObjectUrl(resource.getBucketName(), resource.getObjectName());
			else
				return getClient().getPermanentPresignedObjectUrl(resource.getBucketName(), resource.getObjectName());
		} catch (ODClientException e) {
			throw new IOException(e);
		}
	}
	
	/**
	public String getPresignedStaticUrl( String bucketName, String objectName )  throws IOException {
		try {
			return getClient().getPermanentPresignedObjectUrl(bucketName, objectName);
		} catch (ODClientException e) {
			throw new IOException(e);
		}
	}
	
	
	
	public String getPublicUrl( String bucketName, String objectName )  throws IOException {
		try {
		
			return getClient().getPublicObjectUrl(bucketName, objectName);

		} catch (ODClientException e) {
			throw new IOException(e);
		}
	}
**/
	
	

	public ResultSet<Item<ObjectMetadata>> listObjects(String bucketName) throws IOException {

		try {
			return getClient().listObjects(bucketName);
		} catch (ODClientException e) {
			throw new IOException(e);
		}

	}

	public void putObject(String bucketName, String objectName, InputStream stream, String fileName, boolean publicAccess) throws IOException {
		try {
			logger.debug("putObject -> " + bucketName + "-" + objectName + " | " + fileName);
	//		getClient().putObjectStream(bucketName, objectName, stream, fileName );
			getClient().putObjectStream(
					bucketName,
					objectName,
					stream,
					Optional.of(fileName),
					Optional.empty(), 
					Optional.empty(), 
					Optional.empty(), 
					Optional.of( Boolean.valueOf(publicAccess)));

		} catch (ODClientException e) {
			throw new IOException(e);
		}
	}

	public ObjectMetadata getObjectMetadata(String bucketName, String objectName) throws IOException {
		try {
			return getClient().getObjectMetadata(bucketName, objectName);
		} catch (ODClientException e) {
			throw new RuntimeException(e);
		}
	}

	public InputStream getObject(String bucketName, String objectName) throws IOException {
		try {
			return getClient().getObject(bucketName, objectName);
		} catch (ODClientException e) {
			throw new IOException(e);
		}
	}

	public boolean existsBucket(String bucketName) throws IOException {

		try {
			return getClient().existsBucket(bucketName);
		} catch (ODClientException e) {
			throw new IOException(e);
		}
	}

	public boolean existsObject(String bucketName, String objectName) throws IOException {
		try {
			return getClient().existsObject(bucketName, objectName);
		} catch (ODClientException e) {
			throw new IOException(e);
		}
	}

	public void createBucket(String bucketName) throws IOException {

		try {
			getClient().createBucket(bucketName);
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

		startupLogger.info(ServerConstant.SEPARATOR);
		startupLogger.info("Starting connection to Odilon Object Storage");

		connect();

		this.timerConnect = new TimerThread() {

			public long getSleepTimeMillis() {
				return Constant.DEFAULT_SLEEP_TIME * 1;
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

		startupLogger.info("Connected to Odilon server -> " + this.client.getSchemaAndHost() + ":" + String.valueOf(this.port));

		startupLogger.debug(this.toString());
		startupLogger.debug("Startup -> " + this.getClass().getSimpleName());

	}

	protected String getEndPoint() {
		return this.endpoint;
	}

	private synchronized void connect() {

		
		logger.debug(this.endpoint);
		logger.debug(this.port);
		logger.debug(this.accessKey);
		logger.debug(this.secretKey);
		logger.debug(getSettings().isObjectStorageSSL());
			

		
		this.client = new ODClient(this.endpoint, this.port, this.accessKey, this.secretKey, getSettings().isObjectStorageSSL());


		this.client.setPresignedUrl(getSettings().getObjectStoragePresignedUrl(), getSettings().getObjectStoragePresignedPort(), getSettings().isObjectStoragePresignedSSL());

		logger.debug(this.getSettings().getObjectStoragePresignedUrl());
		logger.debug(getSettings().getObjectStoragePresignedPort());
		logger.debug(this.accessKey);
		logger.debug(this.secretKey);
		logger.debug(getSettings().isObjectStoragePresignedSSL());
		
		
		
		String ping = this.client.ping();

		if (ping == null || !ping.equals("ok"))
			throw new InternalCriticalException("Pïng error -> " + ping);
		try {

			if (!this.client.existsBucket(ServerConstant.AVATAR_BUCKET)) {
				startupLogger.debug("Creating bucket -> " + ServerConstant.AVATAR_BUCKET);
				this.client.createBucket(ServerConstant.AVATAR_BUCKET);
			}

			if (!this.client.existsBucket(ServerConstant.MEDIA_BUCKET)) {
				startupLogger.debug("Creating bucket -> " + ServerConstant.MEDIA_BUCKET);
				this.client.createBucket(ServerConstant.MEDIA_BUCKET);
			}

			if (!this.client.existsBucket(ServerConstant.QR_BUCKET)) {
				startupLogger.debug("Creating bucket -> " + ServerConstant.QR_BUCKET);
				this.client.createBucket(ServerConstant.QR_BUCKET);
			}

			if (!this.client.existsBucket(ServerConstant.THUMBNAIL_BUCKET)) {
				startupLogger.debug("Creating bucket -> " + ServerConstant.THUMBNAIL_BUCKET);
				this.client.createBucket(ServerConstant.THUMBNAIL_BUCKET);
			}

			if (!this.client.existsBucket(ServerConstant.AUDIO_SPEECH_BUCKET)) {
				startupLogger.debug("Creating bucket -> " + ServerConstant.AUDIO_SPEECH_BUCKET);
				this.client.createBucket(ServerConstant.AUDIO_SPEECH_BUCKET);
			}

			if (!this.client.existsBucket(ServerConstant.AUDIO_SPEECHMUSIC_BUCKET)) {
				startupLogger.debug("Creating bucket -> " + ServerConstant.AUDIO_SPEECHMUSIC_BUCKET);
				this.client.createBucket(ServerConstant.AUDIO_SPEECHMUSIC_BUCKET);
			}

			if (!this.client.existsBucket(ServerConstant.SERVER_RESOURCES_BUCKET)) {
				startupLogger.debug("Creating bucket -> " + ServerConstant.SERVER_RESOURCES_BUCKET);
				this.client.createBucket(ServerConstant.SERVER_RESOURCES_BUCKET);
			}

		} catch (ODClientException e) {
			throw new InternalCriticalException(e);
		}

		logger.debug("Connected to Odilon server -> " + this.client.getSchemaAndHost() + ":" + String.valueOf(this.port));

	}

}
