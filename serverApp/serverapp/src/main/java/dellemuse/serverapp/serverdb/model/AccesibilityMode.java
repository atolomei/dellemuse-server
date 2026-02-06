package dellemuse.serverapp.serverdb.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;


/**
 
 * 
 */
public enum AccesibilityMode {
	
	GENERAL 	(0, "general"),
	ACCESIBLE   (1, "accessible");
	
	static private final List<AccesibilityMode> list = new ArrayList<AccesibilityMode>();

	static {
		list.add(GENERAL);
		list.add(ACCESIBLE);
	}
	 
	
	static  final public List<AccesibilityMode> getModes() { 
			return list;
	}

	private final String label;
	private final int id;
	
	private AccesibilityMode(int code, String label) {
		this.label = label;
		this.id = code; 
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
		ResourceBundle res = ResourceBundle.getBundle(AccesibilityMode.this.getClass().getName(), locale);
		return res.getString(this.label);
	}

	
}
