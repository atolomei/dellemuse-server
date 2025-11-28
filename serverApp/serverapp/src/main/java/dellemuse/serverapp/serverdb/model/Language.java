package dellemuse.serverapp.serverdb.model;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

import dellemuse.model.JsonObject;

public class Language extends JsonObject implements Serializable {

	private static final long serialVersionUID = 1L;
	
	static public final String EN = "en";
	static public final String ES = "es";
	static public final String PT = "pt-BR";
	static public final String FR = "fr";

	
	static public final Language of(String code) {

		if (code==null)
			return null;
		
		if (code.equals(EN)) return new Language(EN);
		if (code.equals(ES)) return new Language(ES);
		if (code.equals(PT)) return new Language(PT);
		if (code.equals(FR)) return new Language(FR);

		return new Language(EN);

		
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
