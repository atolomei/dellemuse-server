package dellemuse.serverapp.elevenlabs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * HTTP methods.
 * 
 * @author atolomei@novamens.com (Alejandro Tolomei)
 * 
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public enum TextNormalization {
  AUTO("auto"), 
  ON("on"), 
  OFF("off");

	 
	private final String value;

	private TextNormalization(String value) {
		this.value = value;
	}

	@JsonValue
	public String getName() {
		return value;
	}

	public String toString() {
		return getName();
	}
	
}