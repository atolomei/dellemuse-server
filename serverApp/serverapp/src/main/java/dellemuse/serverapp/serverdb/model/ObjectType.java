package dellemuse.serverapp.serverdb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;


/**
 * 
 * 
 * 
 * 
 * ObjectSource
 * ------------
 * 
 * HumanMadeObject
 *      ArtWorkObject
 *      HistoricObject
 *      
 * NoHumanMadeObject
 * 
 * 
 */
public enum ObjectType {
	
	ARTWORK (0, "artwork"),
	HUMAN_MADE_OBJECT(1, "human-made-object"),
	NATURAL_OBJECT(2, "natural-object");
	
	private final String label;
	private final int id;
	
	private static final List<ObjectType> values;
	
	static  {
		values = new ArrayList<ObjectType>();
		
		values.add(ARTWORK);
		values.add(HUMAN_MADE_OBJECT);
		values.add(NATURAL_OBJECT);
	}

	
	public static List<ObjectType> getValues() {
		return values;
	}
	
	
	private ObjectType(int code, String label) {
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
		ResourceBundle res = ResourceBundle.getBundle(ObjectType.this.getClass().getName(), locale);
		return res.getString(this.label);
	}
	
	
	

	
}
