package dellemuse.serverapp.serverdb.model;

import java.util.Locale;
import java.util.ResourceBundle;

public enum ObjectState {
	
	DRAFT (1, "draft"),
	EDTIION(2, "edition"),
	APPROVED(3, "approved"),
	PUBLISHED(4, "published"),
	ARCHIVED(5, "archived"),
	DELETED(6, "deleted");
	
	private final String label;
	private final int id;
	
	private ObjectState(int code, String label) {
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
		ResourceBundle res = ResourceBundle.getBundle(ObjectState.this.getClass().getName(), locale);
		return res.getString(this.label);
	}
	

	
}
