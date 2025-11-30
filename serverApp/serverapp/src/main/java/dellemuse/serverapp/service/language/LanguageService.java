package dellemuse.serverapp.service.language;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.service.base.BaseService;

@Service
public class LanguageService extends BaseService {
    
	private Language defaultLanguage;
	
	private final List<Language> languages;
	
	private final List<Language> languages_eng;
	private final List<Language> languages_spa;
	private final List<Language> languages_pt;
	
	/**
	 * @param settings
	 */
    public LanguageService(ServerDBSettings settings) {
        super(settings);
    
    	languages = new ArrayList<Language>();
    	languages_eng= new ArrayList<Language>();
    	languages_spa= new ArrayList<Language>();
    	languages_pt= new ArrayList<Language>();
    	
		languages.add(new Language( Language.ES ));
		languages.add(new Language( Language.EN ));
		languages.add(new Language( Language.PT ));
		
		languages.forEach( l ->  
			{
				languages_spa.add(l);
				languages_eng.add(l);
				languages_pt.add(l);
			}		
		);
		
		languages_spa.sort(new Comparator<Language>() {
			@Override
			public int compare(Language o1, Language o2) {
				return o1.getLabel(Locale.forLanguageTag(Language.ES)).compareToIgnoreCase(o2.getLabel(Locale.forLanguageTag(Language.ES)));
			}
		});
		
		languages_eng.sort(new Comparator<Language>() {
			@Override
			public int compare(Language o1, Language o2) {
				return o1.getLabel(Locale.ENGLISH).compareToIgnoreCase(o2.getLabel(Locale.ENGLISH));
			}
		});
		
		languages_pt.sort(new Comparator<Language>() {
			@Override
			public int compare(Language o1, Language o2) {
				return o1.getLabel(Locale.forLanguageTag(Language.PT)).compareToIgnoreCase(o2.getLabel(Locale.forLanguageTag(Language.PT)));
			}
		});
		
		
		String s=getSettings().getDefaultMasterLanguage();
		for (Language l: getLanguages()) {
			if (l.getLanguageCode().equals(s)) {
				defaultLanguage=l;
				break;
			}
		}
		
		if (defaultLanguage==null) {
			defaultLanguage=getLanguages().get(0);
		}
	}

	public List<Language> getLanguages() {
		return languages;
	}
	
	
	
	public Language getDefaultLanguage() {
/**
		if (defaultLanguage==null) {
			synchronized (this) {
			String s=getSettings().getDefaultMasterLanguage();
			for (Language l: getLanguages()) {
				if (l.getLanguageCode().equals(s)) {
					defaultLanguage=l;
					return defaultLanguage;
				}
			}
			
			if (defaultLanguage==null)
				defaultLanguage=getLanguages().get(0);
			}
		}
	**/	
		return defaultLanguage;
	}
	
	
	public List<Language> getLanguagesSorted(Locale locale) {
		
		if (locale==Locale.ENGLISH)
			return languages_eng;
		
		if (locale==Locale.forLanguageTag(Language.PT))
			return languages_pt;

		if (locale==Locale.forLanguageTag(Language.ES))
			return languages_spa;

		throw new RuntimeException("locale not supported -> " + locale.getLanguage().toString() );
	}

	public List<String> getLanguagesStr(Locale locale) {
		List<String> list = new ArrayList<String>();
		getLanguagesSorted(locale).forEach( la -> list.add(la.getLabel(locale)));
		return list;
	}

	
	/**
	 * es
	 * en
	 * pt
	 * 
	 * @param code
	 * @return
	 */
	public Language getLanguage(String code) {
		for (Language la: getLanguages()) {
			if (code.equals(la.getLanguageCode()))
				return la;
		}
		return null;
	}
    
}
