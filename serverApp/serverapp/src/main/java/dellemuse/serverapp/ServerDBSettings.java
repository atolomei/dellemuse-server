package dellemuse.serverapp;

import java.io.File;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
 

import jakarta.annotation.PostConstruct;
import dellemuse.model.logging.Logger;

import dellemuse.serverapp.serverdb.model.Language;

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

	@Value("${server.port:8099}")
	protected int port;

	@Value("${dellemuse.serverapp.endpoint:http://localhost}")
	@NonNull
	protected String endpoint;

	@Value("${server.ssl.enabled:false}")
	protected String ishttps;

	@Value("${trafficTokens:10}")
	protected int maxTrafficTokens;

	@Value("${server.qr:https://dellemuse.app/qrcode/artwork/}")
	protected String qrurl;

	/** -------------------------------------- **/

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

	@Value("${avatar.dir:avatar}")
	protected String avatarDir;

	@Value("${work.dir:work}")
	protected String workDir;

	@Value("${qrcode.generation:true}")
	protected String qrcodeQenerationStr;

	protected boolean qrcodeQeneration;

	@Value("${google.translate.auth:null}")
	protected String googleTranslateAuth;

	/** -------------------------------------- **/

	@Value("${elevenlabs.api.key:sk_1d73569ea735c3013edd9bb49a6a652839942590f45944d7}")
	protected String elevenLabsAPIKey;

	@Value("${elevenlabs.api.host:api.elevenlabs.io}")
	private String elevenLabsAPIHost;

	@Value("${elevenlabs.api.text.to.speech.version:v1}")
	private String textToSpeechVersion;

	@Value("${elevenlabs.api.text.to.speech.servicename:text-to-speech}")
	private String textToSpeechServiceName;

	public String getTextToSpeechServiceName() {
		return this.textToSpeechServiceName;
	}

	public String getElevenLabsAPIHost() {
		return elevenLabsAPIHost;
	}

	@Value("${audio.cache.dir:audioCache}")
	private String audioDir;

	/** -------------------------------------- **/

	@Value("${server.locale:es}")
	protected String localeStr;

	/** -------------------------------------- **/
	@Value("${server.zoneid:America/Buenos_Aires}")
	protected String zoneid;

	/** -------------------------------------- **/
	@Value("${server.language:es}")
	protected String defaultLanguage;

	public ServerDBSettings() {
	}

	public OffsetDateTime getSystemStartTime() {
		return systemStarted;
	}

	public boolean isQRCodeGenerate() {
		return this.qrcodeQeneration;
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

	public String getGoogleTranslateAuthPath() {
		return googleTranslateAuth;
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

		if (objectStoragePresignedUrl == null || objectStoragePresignedUrl.equals("null"))
			objectStoragePresignedUrl = this.objectStorageUrl.replace("https://", "").replace("http://", "");

		if (objectStoragePresignedPort == -1)
			objectStoragePresignedPort = objectStoragePort;

		if (isSSLStr == null || isSSLStr.equals("null"))
			isPresignedSSL = this.isHTTPS();
		else
			isPresignedSSL = isSSLStr.toLowerCase().trim().equals("true");

		if (qrcodeQenerationStr == null || qrcodeQenerationStr.equals("null"))
			qrcodeQeneration = true;
		else
			qrcodeQeneration = qrcodeQenerationStr.toLowerCase().trim().equals("true");

		if (googleTranslateAuth != null && !googleTranslateAuth.equals("null"))
			googleTranslateAuth = googleTranslateAuth.trim();
		else
			googleTranslateAuth = null;
	}

	public String getAudioDownloadCacheDir() {
		return audioDir + File.separator + "download";
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

	public String getObjectStorageTempDir() {
		return this.workDir + File.separator + "objectStorage";
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

	public String getAvatarDir() {
		return avatarDir;
	}

	public String getQRServer() {
		return qrurl;
	}

	public String getElevenLabsAPIKey() {
		return elevenLabsAPIKey;
	}

	public String getAudioDir() {
		return audioDir;
	}

	public String getTextToSpeechVersion() {
		return textToSpeechVersion;
	}

	public int getAudioMaxCharsPerRequest() {
		return 4096;
	}

	public String getDefaultMasterLanguage() {
		return Language.of(Language.ES).getLanguageCode();
	}

	public Locale getDefaultLocale() {
		return Locale.forLanguageTag(localeStr);
	}

	public ZoneId getDefaultZoneId() {
		return ZoneId.of(zoneid);
	}

	private void checkDirs() {

		try {
			File dir = new File(this.audioDir);
			if ((!dir.exists()) || (!dir.isDirectory()))
				FileUtils.forceMkdir(dir);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		try {
			File dir = new File(this.audioDir, "download");
			if ((!dir.exists()) || (!dir.isDirectory()))
				FileUtils.forceMkdir(dir);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		try {
			File wDir = new File(this.workDir);
			if ((!wDir.exists()) || (!wDir.isDirectory()))
				FileUtils.forceMkdir(wDir);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		try {
			File thDir = new File(this.workDir, ServerConstant.THUMBNAIL_BUCKET);
			if ((!thDir.exists()) || (!thDir.isDirectory()))
				FileUtils.forceMkdir(thDir);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		try {
			File thDir = new File(this.workDir, "objectStorage");
			if ((!thDir.exists()) || (!thDir.isDirectory()))
				FileUtils.forceMkdir(thDir);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		try {
			File avDir = new File(this.avatarDir);
			if ((!avDir.exists()) || (!avDir.isDirectory()))
				FileUtils.forceMkdir(avDir);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public int getFileCacheInitialCapacity() {
		return 1000;
	}

	public long getFileCacheMaxCapacity() {
	 	return 100000;
	}

	public long getLanguageCacheDurationMinutes() {
		return 10;
	}
}
