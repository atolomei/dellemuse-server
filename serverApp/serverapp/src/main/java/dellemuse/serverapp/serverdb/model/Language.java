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
