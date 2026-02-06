package dellemuse.serverapp.serverdb.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import dellemuse.model.JsonObject;


/**
 * 
 *   ISO 639 alpha-2 or alpha-3 language code
 *  
 */
public class Language extends JsonObject implements Serializable {

	private static final long serialVersionUID = 1L;
	
	static public final String EN = "en";
	static public final String ES = "es";
	static public final String PT = "pt-BR";
	static public final String FR = "fr";

	
	static public final String IT = "it";
	static public final String GER = "ger";

	
	
	static private final Map<String, Language> map = new HashMap<String, Language>();
	static {
		map.put(PT, new Language(PT));
		map.put(ES, new Language(ES));
		map.put(EN, new Language(EN));
		map.put(FR, new Language(FR));
		map.put(IT, new Language(IT));
		map.put(GER, new Language(GER));
	}
	
	
	static public final Language of(String code) {
		if (code==null)
			return null;
		return map.get(code);
	}
	
	static  public Map<String, Language> getLanguages() { 
			return map;
	}

	
	
	
	private String languageCode;

	public Language( String languageCode ) {
		this.languageCode=languageCode;
	}

	public String getLabel(Locale locale) {
		ResourceBundle res = ResourceBundle.getBundle( this.getClass().getName(), locale);
		return res.getString(this.languageCode);
	}

	public String getLanguageCode() {
		return languageCode;
	}
	
}
