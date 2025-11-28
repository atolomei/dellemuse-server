package dellemuse.serverapp.serverdb.model;

import java.util.Locale;
import java.util.ResourceBundle;

public enum AuditAction {
	
	CREATE (0, "create"),
	READ(1, "read"),
	UPDATE(2, "update"),
	DELETE(3, "delete");	
	
	private final String label;
	private final int id;
	
	private  AuditAction(int code, String label) {
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
		ResourceBundle res = ResourceBundle.getBundle( AuditAction.this.getClass().getName(), locale);
		return res.getString(this.label);
	}

}
