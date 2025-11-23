package dellemuse.serverapp.elevenlabs;

/**
 * HTTP methods.
 * 
 * @author atolomei@novamens.com (Alejandro Tolomei)
 * 
 */
public enum Method {
  GET("GET"), 
  HEAD("HEAD"), 
  POST("POST"), 
  PUT("PUT"), 
  DELETE("DELETE");

	 
	private final String value;

	private Method(String value) {
		this.value = value;
	}

	public String getName() {
		return value;
	}

	public String toString() {
		return getName();
	}

}