package dellemuse.serverapp.elevenlabs;


/**
 * 
 * 
 * 
 */
public enum OutputFormat {

	
	Opus_48000_192("opus_48000_192");
	
	private String name;
	
	
		
	private OutputFormat( String name) {
		this.name=name;
	}
	
	public String getName() {
		return name;
	}
	
	
}
