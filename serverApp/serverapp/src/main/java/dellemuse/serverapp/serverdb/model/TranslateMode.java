package dellemuse.serverapp.serverdb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 
 */
public enum TranslateMode {

	NONE (0, "none"),
	TRANSLATE_MASTER( 1, "translate-master");

	final int id;
	final String name;
	
	private TranslateMode( int code, String name) {
		this.id=code;
		this.name=name;
	}
	public int getId() {
		return id;
	}

	public String toString() {
		return getLabel();
	}
	
	public String getLabel() {
		return getLabel(Locale.getDefault());
	}

	public String getLabel(Locale locale) {
		ResourceBundle res = ResourceBundle.getBundle(TranslateMode.this.getClass().getName(), locale);
		return res.getString(this.name);
	}


	static List<TranslateMode> list = new ArrayList<TranslateMode>();
	static {
		list.add(NONE);
		list.add(TRANSLATE_MASTER);
	}
	
	
	static public List<TranslateMode> getTranslateModes() {
		return list;
	}
	
}
