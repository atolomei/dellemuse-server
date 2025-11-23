package dellemuse.serverapp.elevenlabs;

import com.fasterxml.jackson.annotation.JsonInclude;

import dellemuse.model.JsonObject;


/**
 * 
 * Voice settings overriding stored settings for the given voice. They are applied only on the given request.
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoiceSettings extends JsonObject {

	public Double stability;
	
	public Double style;
	
    public Double speed;
    
    public Boolean use_speaker_boost;
    
    public Double similarity_boost;

    public Integer seed;

}
