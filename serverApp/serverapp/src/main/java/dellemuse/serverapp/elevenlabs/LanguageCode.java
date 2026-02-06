package dellemuse.serverapp.elevenlabs;

/**
 * 
 * Language code (ISO 639-1) used to enforce a language for the model and text normalization. 
 * If the model does not support provided language code, an error will be returned.
 *  
 */
public enum LanguageCode {

	EN("en"),
	ES("es"),
	FR("fr"),
	IT("it"),
	GER("ger"),
	PT("pt"),
	DUTCH("dutch"),
	SV("sv");
		
	
	private String name;
	
			
	private LanguageCode(String name) {
		this.name=name;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return getName();
	}
	
	public static LanguageCode from( String lang ) {

		if (lang.equals("es")) return ES;
		if (lang.equals("en")) return EN;
		if (lang.equals("fr")) return FR;
		if (lang.equals("it")) return IT;
		if (lang.equals("pt")) return PT;
		if (lang.equals("sv")) return SV;
		if (lang.equals("ger")) return GER;
		if (lang.equals("dutch")) return DUTCH;
				
		return EN;
	}
}
