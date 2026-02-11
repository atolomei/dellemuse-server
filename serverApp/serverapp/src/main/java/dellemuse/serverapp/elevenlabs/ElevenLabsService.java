package dellemuse.serverapp.elevenlabs;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

 
import dellemuse.model.logging.Logger;
import dellemuse.model.util.RandomIDGenerator;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.ServiceStatus;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.record.SiteRecord;
import dellemuse.serverapp.serverdb.model.record.TranslationRecord;
import dellemuse.serverapp.serverdb.service.base.BaseService;
import dellemuse.serverapp.service.LockService;
import io.odilon.util.Check;
import jakarta.annotation.PostConstruct;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class ElevenLabsService extends BaseService {

	private static Logger logger = Logger.getLogger(ElevenLabsService.class.getName());
 
	static private Logger startupLogger = Logger.getLogger("StartupLogger");
	
	@JsonIgnore
	private AtomicBoolean serviceEnabled = new AtomicBoolean(false);
		
	private final OffsetDateTime created = OffsetDateTime.now();

	@JsonIgnore
	@Autowired
	private final LockService lockService;

	@JsonIgnore
	private String userAgent = ClientConstant.DEFAULT_USER_AGENT;

	@JsonIgnore
	private OkHttpClient httpClient;

	@JsonIgnore
	private Scheme scheme = Scheme.HTTP;
	
	@JsonIgnore
	private final RandomIDGenerator rand = new RandomIDGenerator();	
	
	@JsonIgnore
	private Map <String, ELVoice> voices = new ConcurrentHashMap<String, ELVoice>();
	
	private String apiKey;
	
	private String apiHost;

	private int port;
	
	
	public List<ELVoice> getVoices(String lang) {
	
		List<ELVoice> list = new ArrayList<ELVoice>();
		
		voices.forEach( (k,v) -> {
			if (v.getLanguage().equals(lang))
					list.add(v);
		});
		return list;
		
		
	}
	
	
	/**
	 * @param settings
	 * @param lockService
	 */
	public ElevenLabsService(ServerDBSettings settings, LockService lockService) {
		super(settings);
		this.lockService=lockService;
	}
 
	/**
	 * 
	 * 
	 * @param text
	 * @param audioFileName
	 * @param languageCode
	 * @param dm_voice_id
	 * @return
	 */
	
	/**
	public Optional<File> generate(String text, String audioFileName, LanguageCode languageCode) {

		
		String dm_voice_id;
		
		if (languageCode.equals(LanguageCode.ES))
			dm_voice_id = "mariana";

		else if (languageCode.equals(LanguageCode.PT))
			dm_voice_id = "amanda";

		else if (languageCode.equals(LanguageCode.EN))
			dm_voice_id = "emily";

		else
			dm_voice_id = "emily";
		
		return generate(text, audioFileName, languageCode, dm_voice_id, true);
	}
	**/
	
	
	public Optional<File> generate(String text, String audioFileName, LanguageCode languageCode, String vid) {
		return generate(text, audioFileName, languageCode, vid, true);
	}
	

	public Optional<File> generate(String text, String audioFileName, LanguageCode languageCode, String vid, boolean enableLogging) {

		if (text==null || text.length()==0)
			return Optional.empty();
		
		Check.requireNonNullStringArgument(audioFileName, "audioFileName can not be null");
		Check.requireNonNullArgument(languageCode, "languageCode can not be null");
		Check.requireNonNullStringArgument(vid, "vid can not be null");
		
		
		//if (!getVoices().containsKey(dm_voice_id))
		//	throw new IllegalArgumentException("voice not found -> " + dm_voice_id);
		
		String voice_id  = vid; // getVoices().get(dm_voice_id).getVoiceId();
		//VoiceSettings vs =  getVoices().get(dm_voice_id).getVoiceSettings();
		
		
		
		
		
		String output_format = OutputFormat.Opus_48000_192.getName();
		 
		String relativePath[] = new String [3];

		relativePath[0]=  getSettings().getTextToSpeechVersion(); 
		relativePath[1]=  getSettings().getTextToSpeechServiceName();  
		relativePath[2]=  voice_id;
	
	    HttpUrl.Builder urlBuilder = new HttpUrl.Builder();
         
        urlBuilder.scheme(this.scheme.toString());
        urlBuilder.host(this.apiHost);
        
        if (this.port > 0)
            urlBuilder.port(this.port);
        
        for (String str: relativePath)
             urlBuilder.addEncodedPathSegment(str);
	        
        urlBuilder.addEncodedQueryParameter("output_format", output_format);

        
        /**
         * When enable_logging is set to false zero retention mode will be used for the request. 
         * This will mean history features are unavailable for this request, including request stitching. 
         * Zero retention mode may only be used by enterprise customers.
         */
        if (!enableLogging)
            urlBuilder.addEncodedQueryParameter("enableLogging", "false");
        	
        
        HttpUrl url = urlBuilder.build();

        logger.debug(url.toString());
         
		 ElevenLabsRecord rec = new ElevenLabsRecord();
		 
		 rec.model_id = ModelId.Eleven_multilingual_v2.toString();
		 rec.text = text;
		 rec.language_code=languageCode.toString();
		
		 //if (vs!=null)
			 rec.voice_settings= getDefaultVoiceSettings();
		 	
		 List<byte[]> audioChunks = new ArrayList<>();
	     
		 int counter = 1;
	        
	     List<ElevenLabsRecordChunk> chunks = rec.chunks();
	        
	     List<String> chunkRequestId = new ArrayList<String>();
	     
	     for (ElevenLabsRecordChunk chunk : chunks) {
	 
	        	try {
	        		
	                String requestId = UUID.randomUUID().toString();

	                if (chunkRequestId.size()>0) {
	                	List<String> ids = new ArrayList<String>();
	                	if (chunkRequestId.size()>2) {
	                		ids.add(chunkRequestId.get(chunkRequestId.size()-3));
	                		ids.add(chunkRequestId.get(chunkRequestId.size()-2));
	                	}
	                	else if (chunkRequestId.size()>1) {
	                		ids.add(chunkRequestId.get(chunkRequestId.size()-2));
	                	}
	                	ids.add(chunkRequestId.get(chunkRequestId.size()-1));
	                	chunk.previous_request_ids=ids;
	                }
	               
	        		String jsonBody = getObjectMapper().writeValueAsString(chunk);
	                RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json"));
	                
	                // requestBuilder.header("Authorization", "Basic " + Base64.getEncoder().encodeToString((getAccessKey() + ":" + getSecretKey()).getBytes()));
	                Request.Builder requestBuilder = new Request.Builder();
	                requestBuilder.url(url);
	                requestBuilder.header("xi-api-key", this.apiKey);
	                requestBuilder.header("Content-Type", ClientConstant.APPLICATION_JSON);
	                requestBuilder.header("Host", this.shouldOmitPortInHostHeader(url) ? url.host() : (url.host() + ":" + url.port()));
	                requestBuilder.header("User-Agent", this.userAgent);
	                requestBuilder.header("Date", ClientConstant.HTTP_DATE.format(OffsetDateTime.now()));
	                requestBuilder.header("Accept", "audio/mpeg");
	                requestBuilder.header("X-Request-ID", requestId);
	                
	                // requestBuilder.header("Accept-Charset", "utf-8");
	                // requestBuilder.header("Accept-Encoding", "gzip, deflate");
	                
	                requestBuilder.post(body);
	                
	                Request request = requestBuilder.build();
	                
		            logger.debug(request.toString());

	                try (Response response = this.httpClient.newCall(request).execute()) {
	                  
	                	if (!response.isSuccessful()) {
	                        throw new RuntimeException("Chunk error -> " + counter + " |  code: " + response.code() + " | msg: " + response.message());
	                    }

		                chunkRequestId.add(requestId);
	                	
	                    try (InputStream inputStream = response.body().byteStream()) {
	                        byte[] buffer = inputStream.readAllBytes();
	                        audioChunks.add(buffer);
	                        logger.debug("Fragmento " + counter + " descargado, tamaño: " + buffer.length + " bytes");
	                    }
	                    counter++;
	                }

	            } catch (IOException e) {
	            	  throw new RuntimeException(e);
	            }
	        }

	        File file = new File( getSettings().getAudioDir(), audioFileName );
	         
	        try (FileOutputStream fos = new FileOutputStream(file)) {
	            for (byte[] chunkBytes : audioChunks) {
	                fos.write(chunkBytes);
	            }
	            logger.debug(file.getAbsolutePath());

	        } catch (IOException e) {
	        	 throw new RuntimeException(e);
	        }
		    return Optional.of(file);
		    
		/**
		 * curl -X POST "https://api.elevenlabs.io/v1/text-to-speech / 9rvdnhrYoXoUt4igKpBw ? output_format=opus_48000_192" \
     -H "xi-api-key: sk_1d73569ea735c3013edd9bb49a6a652839942590f45944d7" \
     -H "Content-Type: application/json" \
     -d '{
  "text": "Entre 1920 e 1950, o turismo na Itália se transformou em um fenômeno de massa, e o cartaz foi uma ferramenta fundamental: uma síntese de arte, design e promoção cultural, uma peça publicitária que era também uma obra de arte, refletindo a cultura e a estética de sua época.",
  "model_id": "eleven_multilingual_v2"
}'
		 */
	}

	
	VoiceSettings voiceSettings;
	
	public synchronized VoiceSettings getDefaultVoiceSettings() {
		
		if (voiceSettings!=null)
			return voiceSettings;
		
		 voiceSettings = new VoiceSettings();
		
		
		 voiceSettings.speed= Double.valueOf(1.1);
		 voiceSettings.stability=Double.valueOf(0.91);
		 voiceSettings.similarity_boost=Double.valueOf(0.48);
		 voiceSettings.style=Double.valueOf(0.01);
	
		 return voiceSettings;
	}


	public String getAPIKey() {
		return this.apiKey;
	}
	
	public OffsetDateTime getOffsetDateTimeCreated() {
		return this.created;
	}
 	
	public Map <String, ELVoice> getVoices() { 
		return this.voices; 
	}
	
	public boolean isServiceEnabled() {
		return this.serviceEnabled.get();
	}
	 
	public LockService getLockService() {
		return this.lockService;
	}
		
	@PostConstruct
	protected void onInit() {
		try {
	
			this.apiKey = getSettings().getElevenLabsAPIKey();
			this.apiHost = getSettings().getElevenLabsAPIHost();
			this.port = 443;
		
			loadVoices();
			initOkHttpClient();
				
			setStatus(ServiceStatus.RUNNING);
		} catch (Exception e) {
			setStatus(ServiceStatus.STOPPED);
		}
	}
		
	
	/**
	 * 
	 * 

	  insert into voice (id, name, voiceId, language, languageRegion, info, created, lastmodified, lastmodifieduser) VALUES (nextval('sequence_id'), 'amanda', 'oi8rgjIfLgJRsQ6rbZh3', 'pt', 'brazil', 
	  'A sweet, feminine, and youthful Brazilian Portuguese voice with a neutral accent. Naturally warm and expressive, she brings a gentle charm to every word. Ideal for narrations, educational content, and conversational dialogue where clarity, softness, and an inviting tone are essential.', now(), now(), (select id from users where name='root'));
	 
	 
	 
	 	  insert into voice (id, name, voiceId, language, languageRegion, info, created, lastmodified, lastmodifieduser) VALUES (nextval('sequence_id'), 'amanda', 'oi8rgjIfLgJRsQ6rbZh3', 'pt', 'brazil', 
	  'A sweet, feminine, and youthful Brazilian Portuguese voice with a neutral accent. Naturally warm and expressive, she brings a gentle charm to every word. Ideal for narrations, educational content, and conversational dialogue where clarity, softness, and an inviting tone are essential.', now(), now(), (select id from users where name='root'));




	 * 
	 * 
	 * 
	 * 
	 */
	private synchronized void loadVoices() {
		
		voices = new ConcurrentHashMap<String, ELVoice>();
		
		voices.put("amanda", 	
				   new ELVoice("amanda",	 
						       "oi8rgjIfLgJRsQ6rbZh3", "pt" ,
						       "brasil",
						       "Amanda Kelly",
						       "A sweet, feminine, and youthful Brazilian Portuguese "
						       + "voice with a neutral accent. Naturally warm and expressive, she brings a gentle charm to every word. "
						       + "Ideal for narrations, educational content, and conversational dialogue where clarity, softness, and "
						       + "an inviting tone are essential."));

		 
		VoiceSettings voiceSettings = new VoiceSettings();
		 voiceSettings.speed= Double.valueOf(1.1);
		 voiceSettings.stability=Double.valueOf(0.91);
		 voiceSettings.similarity_boost=Double.valueOf(0.48);
		 voiceSettings.style=Double.valueOf(0.01);
		
		 
		 /** spa 
		  * 
		  
		    	  insert into voice (id, name, voiceId, language, languageRegion, info, created, lastmodified, lastmodifieduser) VALUES (nextval('sequence_id'), 'mariana', '9rvdnhrYoXoUt4igKpBw', 'es', 'latin anmerica',  'Intimate and Assertive.', now(), now(), (select id from users where name='root'));


   	  insert into voice (id, name, voiceId, language, languageRegion, info, created, lastmodified, lastmodifieduser) VALUES (nextval('sequence_id'), 'mariana', '9rvdnhrYoXoUt4igKpBw', 'es', 'latin anmerica',  'Intimate and Assertive.', now(), now(), (select id from users where name='root'));
   	  insert into voice (id, name, voiceId, language, languageRegion, info, created, lastmodified, lastmodifieduser) VALUES  (nextval('sequence_id'), 'ernesto', 'TOFW0dONbX4o9MmkxwBB', 'es', 'argentina',  'xplainer & Informative videos - Natural and kind voice. Middle-age man with Spanish peninsular accent', now(), now(), (select id from users where name='root'));
   	  insert into voice (id, name, voiceId, language, languageRegion, info, created, lastmodified, lastmodifieduser) VALUES  (nextval('sequence_id'), 'victor', 'TcMKZRsVE5V7xf6qCp9fF', 'es', 'latin america',  'Calm, Soft and Neutral.', now(), now(), (select id from users where name='root'));
   	  insert into voice (id, name, voiceId, language, languageRegion, info, created, lastmodified, lastmodifieduser) VALUES  (nextval('sequence_id'), 'mario', 'uJOittXFsgvpY3q1g8vB', 'es', 'spain',  'Middle-aged man. Warm, natural voice with a conversational style. Ideal for educational vlogs, podcasts, online training, and clear explanations (finance, tech, lifestyle', now(), now(), (select id from users where name='root'));

	insert into voice (id, name, voiceId, language, languageRegion, info, created, lastmodified, lastmodifieduser) VALUES  (nextval('sequence_id'), 'jonathan', 'PIGsltMj3gFMR34aFDI3', 'en', 'american', 
	 'A calm, trustworthy, confident voice to narrate your story, audiobooks, articles and other media.', now(), now(), (select id from users where name='root'));


		 
		   
		   
		   
		  	insert into voice (id, name, sex, voiceId, language, languageRegion, info, created, lastmodified, lastmodifieduser) VALUES  (nextval('sequence_id'), 'tamsin', 'female', 'dAlhI9qAHVIjXuVppzhW', 'en', 'british',  '
		
 
 
 Antoine - E-learning Instructor

Antoine - A male adult voice, composed and formal, with a well-controlled neutral tone. Its timbre resembles that of an actor accustomed to delivering precise and articulate speech


insert into voice (id, name, sex, voiceId, language, languageRegion, info, created, lastmodified, lastmodifieduser) VALUES  (nextval('sequence_id'), 'antoine', 'male', 'nbiTBaMRdSobTQJDzIWm', 'fr', 
'france', 'A male adult voice, composed and formal, with a well-controlled neutral tone. Its timbre resembles that of an actor accustomed to delivering precise and articulate speech', now(), now(), (select id from users where name='root'));
		

		   insert into voice (id, name, sex, voiceId, language, languageRegion, info, created, lastmodified, lastmodifieduser) VALUES  (nextval('sequence_id'), 
		   'Anaïs', 'female', '5OnMHwgTFgvPVwE8jP6B', 'fr', 
'france', 'Middle aged French female voice. Warm and clear, ideal for podcasting, e-learning and news content.', now(), now(), (select id from users where name='root'));

		   
		   Anaïs - Instructor

Anaïs - Middle aged French female voice. Warm and clear, ideal for podcasting, e-learning and news content.





insert into voice (id, name, sex, voiceId, language, languageRegion, info, created, lastmodified, lastmodifieduser) VALUES  (nextval('sequence_id'), 
		   'Marie Line', 'female', 'u5l0VNCfzO5oqrKTuA1e', 'fr', 
'france', 'My smiling, calm, and empathetic voice of French origin inspires confidence', now(), now(), (select id from users where name='root'));


u5l0VNCfzO5oqrKTuA1e

Marie Line - Energetic and Clear

Marie Line - Narrator - My smiling, calm, and empathetic voice of French origin inspires confidence. The quality of my diction and my recording studio allow me to offer you a very... read more

		  * 
		  * **/

		 voices.put("mariana",	
				new ELVoice("mariana",			
				"9rvdnhrYoXoUt4igKpBw", 
				"es",
				"latin america",
				"Mariana",
				"Mariana -Intimate and Assertive",
				voiceSettings));
	
		
		voices.put("marcela",	
				new ELVoice("marcela",			
				"YM9MbhkdpZ8JR7EP49hA", 
				"es" ,
				"spain",
				"Marcela",
				"Marcela - A youthful, delicate, friendly, and subtle voice, perfect for writing ebooks, podcasts, and any content for social media or work.",
				voiceSettings));
		
		
		voices.put("ernesto",	
				new ELVoice("ernesto",			
				"TOFW0dONbX4o9MmkxwBB", 
				"es",
				"argentina",
				"Ernesto",
				"Ernesto T. - Explainer & Informative videos - Natural and kind voice. Middle-age man with Spanish peninsular accent..",
				voiceSettings));
		
		
		
		voices.put("victor",	
				new ELVoice("victor",			
				"cMKZRsVE5V7xf6qCp9fF", 
				"es",
				"latin american",
				"Victor",
				" Calm, Soft and Neutral.",
				voiceSettings));

		
		voices.put("mario",	
				new ELVoice("mario",			
				"uJOittXFsgvpY3q1g8vB", 
				"es",
				"spain",
				"Mario",
				"Middle-aged man. Warm, natural voice with a conversational style. Ideal for educational vlogs, podcasts, online training, and clear explanations (finance, tech, lifestyle",
				voiceSettings));
		
   /** en **/
		
		
  /**
   *
   * 
   	  insert into voice (id, name, voiceId, language, languageRegion, info, created, lastmodified, lastmodifieduser) VALUES  (nextval('sequence_id'), 
   	  'emily', 'XB0fDUnXU5powFXDhCwa', 'en', 'us',  '', now(), now(), (select id from users where name='root'));


	  insert into voice (id, name, voiceId, language, languageRegion, info, created, lastmodified, lastmodifieduser) VALUES  (nextval('sequence_id'), 
   	  'nicola', '9sKbNSlHXq99bttvf8rRF', 'it', 'italia',  'Nicola Loruso', now(), now(), (select id from users where name='root'));


	  insert into voice (id, name, voiceId, language, languageRegion, info, created, lastmodified, lastmodifieduser) VALUES  (nextval('sequence_id'), 
   	  'thomas', 'tvFp0BgJPrEXGoDhDIA4', 'dutch', 'dutch',  '', now(), now(), (select id from users where name='root'));

	 
	  insert into voice (id, name, voiceId, language, languageRegion, info, created, lastmodified, lastmodifieduser) VALUES  (nextval('sequence_id'), 
   	  'leon', 'MJ0RnG71ty4LH3dvNfSd4', 'ger', 'germany',  '', now(), now(), (select id from users where name='root'));


	  insert into voice (id, name, voiceId, language, languageRegion, info, created, lastmodified, lastmodifieduser) VALUES  (nextval('sequence_id'), 
   	  'vincent', 'S9WrLrqYPJzmQyWPWbZ5', 'en', 'us',  'Vincent Sparks - Deep American Voice - Professional American Deep Male English Voiceover. Great for Informative videos.', now(), now(), (select id from users where name='root'));


	  insert into voice (id, name, voiceId, language, languageRegion, info, created, lastmodified, lastmodifieduser) VALUES  (nextval('sequence_id'), 
   	  'nigel', 'l9yjqAhh8GXv7ZJxsLZO', 'en', 'british',  'A distinct, seasoned English male voice that captivates audiences with warmth, clarity, and authenticity.', now(), now(), (select id from users where name='root'));


	  insert into voice (id, name, voiceId, language, languageRegion, info, created, lastmodified, lastmodifieduser) VALUES  (nextval('sequence_id'), 
   	  'shaun', 'lRKCbSROXui75bk1SVpy8', 'en', 'british',  ' ', now(), now(), (select id from users where name='root'));


RKCbSROXui75bk1SVpy8

   */

		
		
		voices.put("emily",	     
				new ELVoice("emily",			
				"XB0fDUnXU5powFXDhCwa", 
				"en" ,
				"us",
				"Emily",
				"Emily"));
	
	
		
		
		
		
		
		
		
		
		
		
		
		 /** it **/

		
		voices.put("nicola",	
				new ELVoice("nicola",			
				"9sKbNSlHXq99bttvf8rRF", 
				"it", 
				"italia",
				"Nicola",
				"Nicola Loruso",
				voiceSettings));
		
	
		voices.put("thomas",	
				new ELVoice("thomas",			
				"tvFp0BgJPrEXGoDhDIA4", 
				"dutch",
				"dutch",
				"Thomas",
				"Thomas",
				voiceSettings));
	
		
		voices.put("leon",	
				new ELVoice("leon",			
				"MJ0RnG71ty4LH3dvNfSd", 
				"ger",
				"germany",
				"Leon",
				"Leon",
				voiceSettings));
		
		
	
	
	
	
	
	}
	
	
	/**
	 * 
	 * 
	 * 
	 * 
	 */
	  
	private void initOkHttpClient() {
	
		 this.scheme = Scheme.HTTPS;
		 List<Protocol> protocol = new ArrayList<>();
	     protocol.add(Protocol.HTTP_1_1);

	     boolean isSecure = true;
	     boolean acceptAllCertificates = true;

		 Cache cache = new Cache(new File(getCacheWorkDir()), ClientConstant.HTTP_CACHE_SIZE);

	     /**
	     String host = getSettings().getElevenLabsAPIHost();
	     HttpUrl url = HttpUrl.parse(host);
	     if (url == null)
	         throw new IllegalArgumentException("url is null for host -> " + host);

	     HttpUrl.Builder urlBuilder = url.newBuilder();

         urlBuilder.scheme(this.scheme.toString());

          if (port > 0)
              urlBuilder.port(port);

         //this.httpUrl = urlBuilder.build();
	       */
	     
           
    	   Builder builder = (new OkHttpClient()).newBuilder();
    	   
           if (!isSecure) {
        	
        	   this.httpClient = builder
        			   .connectTimeout	(ClientConstant.DEFAULT_CONNECTION_TIMEOUT, TimeUnit.SECONDS)
        			   .writeTimeout	(ClientConstant.DEFAULT_CONNECTION_TIMEOUT, TimeUnit.SECONDS)
        			   .readTimeout		(ClientConstant.DEFAULT_CONNECTION_TIMEOUT, TimeUnit.SECONDS)
        			   .protocols		(protocol).cache(cache)
        			   .build();
	            	
	            }
	            
	       else if (acceptAllCertificates) {
	                try {

	                	 final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
	                         @Override
	                         public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	                         }

	                         @Override
	                         public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
	                         }

	                         @Override
	                         public X509Certificate[] getAcceptedIssuers() {
	                             return new X509Certificate[] {};
	                         }
	                     } };

	                  final SSLContext sslContext = SSLContext.getInstance("SSL");
	                  sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
	                  final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

	                  this.httpClient = builder
	                    		 .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
	             				 .hostnameVerifier(new HostnameVerifier() {
	             									                    @Override
	             									                    public boolean verify(String hostname, SSLSession session) {
	             									                        return true;
	             									                    }
	             									                })
	             				  .connectTimeout	(ClientConstant.DEFAULT_CONNECTION_TIMEOUT, TimeUnit.SECONDS)
	             				  .writeTimeout		(ClientConstant.DEFAULT_CONNECTION_TIMEOUT, TimeUnit.SECONDS)
	             				  .readTimeout		(ClientConstant.DEFAULT_CONNECTION_TIMEOUT, TimeUnit.SECONDS)
	             				  .cache(cache) 
	             				  .build();
	             	    	    
	                } catch (KeyManagementException | NoSuchAlgorithmException e) {
	                    throw new IllegalStateException(e);
	                }
	            }
	         else {
	            
	            	this.httpClient = builder
	            			.connectTimeout	(ClientConstant.DEFAULT_CONNECTION_TIMEOUT, TimeUnit.SECONDS)
	            			.writeTimeout	(ClientConstant.DEFAULT_CONNECTION_TIMEOUT, TimeUnit.SECONDS)
	            			.readTimeout	(ClientConstant.DEFAULT_CONNECTION_TIMEOUT, TimeUnit.SECONDS)
	            			.protocols		(protocol)
	            			.cache(cache)
	            			.build();
	            }
           
           logger.debug("init okHttpClient ok -> " + this.httpClient.toString());
	}
	
	
	
	private boolean shouldOmitPortInHostHeader(HttpUrl url) {
        return (url.scheme().equals("http") && url.port() == 80) || (url.scheme().equals("https") && url.port() == 443);
	}

    
    private String getCacheWorkDir() {
        return getHomeDirAbsolutePath() + File.separator + "tmp" + File.separator + rand.randomString(6);
    }

    private String getHomeDirAbsolutePath() {
        if (isLinux())
            return ClientConstant.linux_home;
        return ClientConstant.windows_home;
    }
    private boolean isLinux() {
        if (System.getenv("OS") != null && System.getenv("OS").toLowerCase().contains("windows"))
            return false;
        return true;
    }
 
  
}
