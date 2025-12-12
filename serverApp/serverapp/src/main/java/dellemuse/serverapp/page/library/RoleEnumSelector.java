package dellemuse.serverapp.page.library;

import java.util.Locale;
import java.util.ResourceBundle;

public enum RoleEnumSelector {
	
	ALL 		(1, "all"),
	GENERAL		(2, "general"),
	INSTITUTION	(3, "institution"),
	SITE		(4, "site");
	
	private final String label;
	private final int id;
	
	private RoleEnumSelector(int code, String label) {
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
		ResourceBundle res = ResourceBundle.getBundle(RoleEnumSelector.this.getClass().getName(), locale);
		return res.getString(this.label);
	}
	

	
}
