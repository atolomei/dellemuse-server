package dellemuse.serverapp.service.language;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.apache.wicket.model.Model;
import org.springframework.stereotype.Service;

import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.record.TranslationRecord;
import dellemuse.serverapp.serverdb.service.DBService;
import dellemuse.serverapp.serverdb.service.MultiLanguageObjectDBservice;
import dellemuse.serverapp.serverdb.service.RecordDBService;
import dellemuse.serverapp.serverdb.service.base.BaseService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;

@Service
public class LanguageObjectService extends BaseService {
  
	static Map<Class<?>, DBService<?, Long>> map = new HashMap<Class<?>, DBService<?, Long>>();
	
	public static void register(Class<?> entityClass, DBService<?, Long> dbService) {
		map.put(entityClass, dbService);
	}

	public static DBService<?, Long> getDBService(Class<?> entityClass) {
		return map.get(entityClass);
	}
	 
	
    public LanguageObjectService(ServerDBSettings settings) {
        super(settings);
    }
 
    
    @SuppressWarnings("unchecked")
	public String getObjectDisplayName(MultiLanguageObject o, Locale locale) {
    	
    	String displayName;
    	
		String lang=locale.getLanguage();
		if (lang.equals(o.getMasterLanguage()))
			displayName=o.getDisplayname();
		else {
		   RecordDBService<?, Long> service = MultiLanguageObjectDBservice.getRecordDBService(o.getClass());
		  	
		   Optional<TranslationRecord> t = (Optional<TranslationRecord>) service.findByParentObject(o, lang);
		   
		   if (t.isEmpty())
			   return o.getDisplayname();
		   
		   displayName= t.get().getDisplayname();

		   if (displayName==null)
				displayName=o.getDisplayname();
		}		
		return displayName;
    }
  
    
    public String getObjectSubtitle(MultiLanguageObject o, Locale locale) {
    	
    	String displayName;
    	
		String lang=locale.getLanguage();
		if (lang.equals(o.getMasterLanguage()))
			displayName=o.getSubtitle();
		else {
		   RecordDBService<?, Long> service = MultiLanguageObjectDBservice.getRecordDBService(o.getClass());
		  	displayName= service.findByParentObject(o, lang).get().getSubtitle();
			if (displayName==null)
				displayName=o.getSubtitle();
		}		
		return displayName;
    }

    
    public Resource getAudio(MultiLanguageObject o, Locale locale) {
    	Resource r;
    	String lang=locale.getLanguage();
		if (lang.equals(o.getMasterLanguage()))
			r=o.getAudio();
		else {
		
			RecordDBService<?, Long> service = MultiLanguageObjectDBservice.getRecordDBService(o.getClass());
		  	
		  	
			   Optional<TranslationRecord> t = (Optional<TranslationRecord>) service.findByParentObject(o, lang);
	 			
	  		   if (t.isEmpty())
	  				return o.getAudio();
	  			  		   
			r = t.get().getAudio();

			if (r==null)
				r=o.getAudio();
		}		
		return r;
    }
   
    public String getIntro(MultiLanguageObject o, Locale locale) {
		
		String d;
    	
		String lang=locale.getLanguage();
		if (lang.equals(o.getMasterLanguage()))
			d=o.getIntro();
		else {
		   RecordDBService<?, Long> service = MultiLanguageObjectDBservice.getRecordDBService(o.getClass());
		  	
		   Optional<TranslationRecord> t = (Optional<TranslationRecord>) service.findByParentObject(o, lang);
 			
  		   if (t.isEmpty())
			   return o.getIntro();
  		  
  		   d = t.get().getIntro();

		  	if (d==null)
				d=o.getIntro();
		}		
		return d;
	}
  
    public String getInfo(MultiLanguageObject o, Locale locale) {
		
  		String d;
      	
  		String lang=locale.getLanguage();
  		if (lang.equals(o.getMasterLanguage()))
  			d=o.getInfo();
  		else {
  		   RecordDBService<?, Long> service = MultiLanguageObjectDBservice.getRecordDBService(o.getClass());
  		  	
  		   Optional<TranslationRecord> t = (Optional<TranslationRecord>) service.findByParentObject(o, lang);
  			
  		   if (t.isEmpty())
			   return o.getInfo();
  		   
  		   d = t.get().getInfo();
  		
  		   if (d==null)
  				d=o.getInfo();
  		}		
  		return d;
  	}

    
    
}
