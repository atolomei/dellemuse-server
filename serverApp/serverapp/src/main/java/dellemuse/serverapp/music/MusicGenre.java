package dellemuse.serverapp.music;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 
 * 
 */
public enum MusicGenre {

	CLASSICAL(0, "classical"), 
	POP(1, "pop");
	
	
	
	
	private final String label;
	private final int id;

	private static final List<MusicGenre> values;

	static {
		values = new ArrayList<MusicGenre>();

		values.add(CLASSICAL);
		values.add(POP);

	}

	public static List<MusicGenre> getValues() {
		return values;
	}

	private MusicGenre(int code, String label) {
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
		ResourceBundle res = ResourceBundle.getBundle(MusicGenre.this.getClass().getName(), locale);
		return res.getString(this.label);
	}

}
