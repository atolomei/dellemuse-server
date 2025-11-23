package dellemuse.serverapp.page.library;

import java.util.Locale;
import java.util.ResourceBundle;

public enum ObjectStateEnumSelector {
	
	ALL (1, "all"),
	EDITION(2, "edition"),
	EDTIION_PUBLISHED(3, "edition"),
	PUBLISHED(4, "active"),
	ARCHIVED(5, "archived"),
	DELETED(6, "deleted");
	
	private final String label;
	private final int id;
	
	private ObjectStateEnumSelector(int code, String label) {
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
		ResourceBundle res = ResourceBundle.getBundle(ObjectStateEnumSelector.this.getClass().getName(), locale);
		return res.getString(this.label);
	}
	

	
}
