package dellemuse.serverapp.elevenlabs;


/**
 * 
 * 
 * 
 */
public enum ModelId {

	
	Eleven_multilingual_v2("eleven_multilingual_v2"),
	Eleven_v3("eleven_v3");
	
	private String name;
	
	
		
	private ModelId( String name) {
		this.name=name;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return getName();
	}
	
	
}
