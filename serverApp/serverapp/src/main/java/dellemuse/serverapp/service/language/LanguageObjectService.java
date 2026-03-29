package dellemuse.serverapp.service.language;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.wicket.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.page.library.ArtWorkListPage;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.Artist;
import dellemuse.serverapp.serverdb.model.DelleMuseObject;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.record.ArtistRecord;
import dellemuse.serverapp.serverdb.model.record.PersonRecord;
import dellemuse.serverapp.serverdb.model.record.TranslationRecord;
import dellemuse.serverapp.serverdb.service.DBService;
import dellemuse.serverapp.serverdb.service.MultiLanguageObjectDBservice;
import dellemuse.serverapp.serverdb.service.RecordDBService;
import dellemuse.serverapp.serverdb.service.base.BaseService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
 
import io.odilon.model.ObjectMetadata;
 
import jakarta.annotation.PostConstruct;

@Service
public class LanguageObjectService extends BaseService implements ApplicationListener<LanguageCacheEvictEvent>  {

	static private Logger logger = Logger.getLogger(LanguageObjectService.class.getName());
	
	static public final String NAME ="name";
	static public final String SUBTITLE ="sub";
	static public final String INFO ="info";
	static public final String INTRO ="intro";
	static public final String PERSON ="person";
	static public final String ARTIST ="artist";
	
	
	static Map<Class<?>, DBService<?, Long>> map = new HashMap<Class<?>, DBService<?, Long>>();

	@JsonIgnore
	private Cache<String, String> cache;

	

	protected void onRemoval(Object key, Object value, RemovalCause cause) {
		if (cause.wasEvicted()) {

		}
	}


	protected String getKey(MultiLanguageObject o, String key, String lang) {
		return o.getObjectClassName()+"-"+o.getId().toString()+"-"+key+"-"+lang;
	}
	
	protected String getKey(String oClassName, Long oid, String key, String lang) {
		return oClassName+"-"+oid.toString()+"-"+key+"-"+lang;
	}
	 
	
	
	public static void register(Class<?> entityClass, DBService<?, Long> dbService) {
		map.put(entityClass, dbService);
	}

	public static DBService<?, Long> getDBService(Class<?> entityClass) {
		return map.get(entityClass);
	}

	public LanguageObjectService(ServerDBSettings settings) {
		super(settings);
	}
	
	private Cache<String, String> getCache() {
		return this.cache;
	}

	@SuppressWarnings("unchecked")
	public String getObjectDisplayName(MultiLanguageObject o, Locale locale) {
		
		String langNormalized = normalize( locale.getLanguage() );

		final String key = getKey(o, NAME, langNormalized);
		final String value = getCache().getIfPresent(key);
		
 		if (value!=null) 
  			return value;
		 
		String displayName;
	
		if (isSameLanguage(langNormalized, o.getMasterLanguage()))
			displayName = o.getDisplayname();
		else {

			RecordDBService<?, Long> service = MultiLanguageObjectDBservice.getRecordDBService(o.getClass());

			if (service==null) {
				logger.error("RecordDBService not found -> " + o.getClass().getName());
				return o.getDisplayname();
			}
			
			Optional<TranslationRecord> t = (Optional<TranslationRecord>) service.findByParentObject(o, langNormalized);

			if (t.isEmpty()) {
				displayName = o.getDisplayname();
			}
			else {
				displayName = t.get().getDisplayname();
				if (displayName == null)
					displayName = o.getDisplayname();
			}
		}
		
		if (displayName!=null)
			getCache().put(key, displayName);
		
		return displayName;
	}
	
	
	@SuppressWarnings("unchecked")
	public String getObjectSubtitle(MultiLanguageObject o, Locale locale) {
		
		String langNormalized = normalize( locale.getLanguage() );

		final String key = getKey(o, SUBTITLE, langNormalized);
		final String value = getCache().getIfPresent(key);
		
		if (value!=null) {
			return value;
		}
		
		String displayName;
		
		if (langNormalized.equals(o.getMasterLanguage())) {
			displayName = o.getSubtitle();
		}
		else {
			
			RecordDBService<?, Long> service = MultiLanguageObjectDBservice.getRecordDBService(o.getClass());
			
			
			if (service==null) {
				logger.error("RecordDBService not found -> " + o.getClass());
				return o.getSubtitle();
			}
			
			Optional<TranslationRecord> t = (Optional<TranslationRecord>) service.findByParentObject(o, langNormalized);
			
			if (t.isEmpty()) {
				displayName = o.getSubtitle();
			}
			else {
				displayName = service.findByParentObject(o,langNormalized).get().getSubtitle();
			
				if (displayName == null)
					displayName = o.getSubtitle();
			}
		}
		
		if (displayName!=null)
			getCache().put(key, displayName);
		
		return displayName;
	}

	/**
	 * 
	 * -1 no hay audio 
	 * 0 hay en el lang 
	 * 1 hay audio pero no es del lang sino del
	 * master lang
	 * 
	 * @param o
	 * @param locale
	 * @return
	 */
	
	public static int NO_AUDIO = -1;
	public static int AUDIO_SAME_LANG = 0;
	public static int AUDIO_NOT_SAME_LANG = 1;
	
	
	@SuppressWarnings({ "unchecked"  })
	public int compareAudioLanguage(MultiLanguageObject o, Locale locale) {

		Resource r;

		String langNormalized = normalize( locale.getLanguage() );

		logger.debug("compareAudioLanguage -> " +  langNormalized);
		logger.debug( o.getMasterLanguage() );
		
		
		if ( isSameLanguage( langNormalized, o.getMasterLanguage())) {
			r = o.getAudio();
			return ((r != null) ? AUDIO_SAME_LANG :  NO_AUDIO);
		} else {

			RecordDBService<?, Long> service = MultiLanguageObjectDBservice.getRecordDBService(o.getClass());

			if (service==null) {
				logger.error("RecordDBService not found -> " + o.getClass());
				return AUDIO_SAME_LANG;
			}

			
			Optional<TranslationRecord> t = (Optional<TranslationRecord>) service.findByParentObject(o,langNormalized);
			
			if (t.isEmpty())
				return ((o.getAudio() != null) ? AUDIO_NOT_SAME_LANG : NO_AUDIO);

			r = t.get().getAudio();

			if (r == null)
				return (o.getAudio() != null) ? AUDIO_NOT_SAME_LANG : NO_AUDIO;
			else
				return 0;
		}
	}

	@SuppressWarnings("unchecked")
	public Resource getAudio(MultiLanguageObject o, Locale locale) {

		Resource r;

		String langNormalized = normalize( locale.getLanguage() );
 
		/**
		 * pt and pt-BR are considered the same language, so if the master language is pt and the locale is pt-BR, 
		 * it will return the audio of the master language.
		 *  
		 */
		if (isSameLanguage (langNormalized, o.getMasterLanguage()) ) {
			r = o.getAudio();
		} else {

			RecordDBService<?, Long> service = MultiLanguageObjectDBservice.getRecordDBService(o.getClass());


			if (service==null) {
				logger.error("RecordDBService not found -> " + o.getClass());
				return o.getAudio();
			}

			
			Optional<TranslationRecord> t = (Optional<TranslationRecord>) service.findByParentObject(o, langNormalized);

			if (t.isEmpty())
				return o.getAudio();

			r = t.get().getAudio();

			if (r == null)
				r = o.getAudio();
		}
		return r;
	}

	protected String normalize(String language) {
		if (language.startsWith("pt")) {
			return "pt-BR";
		}
		if (language.startsWith("en")) {
			return "en";
		}
		
		if (language.startsWith("es")) {
			return "es";
		}
		
		return language;
	}


	private boolean isSameLanguage(String lang, String masterLang) {
		 
		if (lang.equals(masterLang))
			return true;
		
		//if (lang.startsWith("pt") && masterLang.startsWith("pt"))
		//	return true;
			
		return false;
	}

	

	@SuppressWarnings("unchecked")
	public String getIntro(MultiLanguageObject o, Locale locale) {

		
		String langNormalized = normalize( locale.getLanguage() );
		final String key = getKey(o, INTRO, langNormalized);
		final String value = getCache().getIfPresent(key);
		
		if (value!=null) {
		
			logger.debug( "cache hit -> " + key );

			return value;
		}
		
		String d;

		 
		if (langNormalized.equals(o.getMasterLanguage()))
			d = o.getIntro();
		else {
			RecordDBService<?, Long> service = MultiLanguageObjectDBservice.getRecordDBService(o.getClass());

			
			if (service==null) {
				logger.error("RecordDBService not found -> " + o.getClass());
				return o.getIntro();
			}
			
			
			Optional<TranslationRecord> t = (Optional<TranslationRecord>) service.findByParentObject(o, langNormalized);

			if (t.isEmpty())
				d=o.getIntro();
			else {
			d = t.get().getIntro();

			if (d == null)
				d = o.getIntro();
			}
		}
		
		if (d!=null)
			getCache().put(key, d);
		
		return d;
	}

	@SuppressWarnings("unchecked")
	public String getInfo(MultiLanguageObject o, Locale locale) {

		
		String langNormalized = normalize( locale.getLanguage() );
	
		final String key = getKey(o, INFO, langNormalized );
		final String value = getCache().getIfPresent(key);
		
		if (value!=null) {
			 

			return value;
		}
		
		String d;

		 
		if (langNormalized .equals(o.getMasterLanguage()))
			d = o.getInfo();
		else {
			RecordDBService<?, Long> service = MultiLanguageObjectDBservice.getRecordDBService(o.getClass());


			if (service==null) {
				logger.error("RecordDBService not found -> " + o.getClass());
				return o.getInfo();
			}
			
			
			
			Optional<TranslationRecord> t = (Optional<TranslationRecord>) service.findByParentObject(o,langNormalized );

			if (t.isEmpty()) {
				d=o.getInfo();
			}
			else {

			d = t.get().getInfo();

			if (d == null)
				d = o.getInfo();
			}
		}
		
		if (d!=null)
			getCache().put(key, d);
		
		return d;
	}

	
	
	
	public String getPersonFirstLastName(Artist p, Locale locale) {
		
		
		String langNormalized = normalize( locale.getLanguage() );
	
		
		final String key = getKey(p, ARTIST, langNormalized);
		final String value = getCache().getIfPresent(key);
		 
		if (value!=null) {
			return value;
		}
		
		String d;

		 
		if (langNormalized.equals(p.getMasterLanguage()))
			d = p.getFirstLastname();
		else {

			RecordDBService<?, Long> service = MultiLanguageObjectDBservice.getRecordDBService(p.getClass());

			if (service==null) {
				logger.error("RecordDBService not found -> " + p.getClass().getName());
				return p.getFirstLastname();
			}
			
			
			@SuppressWarnings("unchecked")
			Optional<ArtistRecord> t = (Optional<ArtistRecord>) service.findByParentObject(p,langNormalized);

			if (t.isEmpty())
				d= p.getFirstLastname();
			else {
				d = t.get().getFirstLastname();
				
				if (d == null)
					d = p.getFirstLastname();
			}
		}
		
		if (d!=null)
			getCache().put(key, d); 
		
		return d;
	}
	
	@SuppressWarnings("unchecked")
	public String getPersonFirstLastName(Person p, Locale locale) {


		String langNormalized = normalize( locale.getLanguage() );
	
		final String key = getKey(p, PERSON, langNormalized);
		final String value = getCache().getIfPresent(key);
		 
		
		if (value!=null) {
			 

			return value;
		}
		
		String d;

		 
		if (langNormalized.equals(p.getMasterLanguage()))
			d = p.getFirstLastname();
		else {

			RecordDBService<?, Long> service = MultiLanguageObjectDBservice.getRecordDBService(p.getClass());
			
			if (service==null) {
				logger.error("RecordDBService not found -> " + p.getClass().getName());
				return p.getFirstLastname();
			}
			
			
			Optional<PersonRecord> t = (Optional<PersonRecord>) service.findByParentObject(p,langNormalized);

			if (t.isEmpty())
				d= p.getFirstLastname();
			else {
				d = t.get().getFirstLastname();
				
				if (d == null)
					d = p.getFirstLastname();
			}
		}
		
		if (d!=null)
			getCache().put(key, d); 
		
		return d;
	}
	
	@PostConstruct
	protected synchronized void onInitialize() {

		this.cache = Caffeine.newBuilder().initialCapacity(getSettings().getFileCacheInitialCapacity()).maximumSize(getSettings().getFileCacheMaxCapacity()).expireAfterWrite(getSettings().getLanguageCacheDurationMinutes(), TimeUnit.MINUTES)
				.evictionListener((key, value, cause) -> {
					onRemoval(key, value, cause);
				}).removalListener((key, value, cause) -> {
					onRemoval(key, value, cause);
				}).build();
	}

	
	/**
	 * 
	 * 
	 * @Autowired
    @JsonIgnore
    private final ApplicationEventPublisher applicationEventPublisher;
    
	 */

	@Override
	public void onApplicationEvent(LanguageCacheEvictEvent event) {
	
		Language.getLanguages().forEach( (k,v) -> 
				{
					{
						String key = getKey(event.getObjectClassName(), event.getOid(), NAME, v.getLanguageCode());
						logger.debug("invalidate cache ->" + key);
						getCache().invalidate(key);
					}
					{
						String key = getKey(event.getObjectClassName(), event.getOid(), SUBTITLE, v.getLanguageCode());
						logger.debug("invalidate cache ->" + key);
						getCache().invalidate(key);
						
					}
					{
						String key = getKey(event.getObjectClassName(), event.getOid(), INFO, v.getLanguageCode());
						logger.debug("invalidate cache ->" + key);
						getCache().invalidate(key);
						
					}
					{
						String key = getKey(event.getObjectClassName(), event.getOid(), INTRO, v.getLanguageCode());
						logger.debug("invalidate cache ->" + key);
						getCache().invalidate(key);

					}
					if (event.getObjectClassName().equals(Person.class.getSimpleName())) {
						String key = getKey(event.getObjectClassName(), event.getOid(), PERSON, v.getLanguageCode());
						logger.debug("invalidate cache ->" + key);
						getCache().invalidate(key);
					}
				});
	
	}

	protected LanguageService getLanguageService() {
		return (LanguageService) ServiceLocator.getInstance().getBean(LanguageService.class);
	}
	
}
