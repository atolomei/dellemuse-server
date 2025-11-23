package dellemuse.serverapp.elevenlabs;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import dellemuse.model.JsonObject;


/**
 * 
 * Model

Eleven Multilingual v2

Speed

1.1

Stability

91%

Similarity boost

48%

Style

1%

Speaker boost

Enabled

 * 
 * 
 * 
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ElevenLabsBaseRecord extends JsonObject {


	
	
	/**
	 *  Identifier of the model that will be used, you can query them using GET /v1/models. 
	 *  The model needs to have support for text to speech, you can check this using the can_do_text_to_speech property.
	 */
	public String model_id;
	
	
	
	/**
	 * Text to translate
	 */
	public String text;
	
	
	
	/**
	 * If specified, our system will make a best effort to sample deterministically, 
	 * such that repeated requests with the same seed and parameters should return the same result.
	 *  Determinism is not guaranteed. Must be integer between 0 and 4294967295.
	 */
	public Integer seed;
	
	
	
	/**
	 * A list of pronunciation dictionary locators (id, version_id) to be applied to the text. 
	 * They will be applied in order. You may have up to 3 locators per request
	 */
	public List<String> pronunciation_dictionary_locators;
	
	
	
	/** 
	 *  Language code (ISO 639-1) used to enforce a language for the model and text normalization.
	 *  If the model does not support provided language code, an error will be returned. 
	 *  */
	public String language_code;
	
	
	
	/**
	 * The text that came before the text of the current request.
	 *  Can be used to improve the speech's continuity when concatenating
	 *  together multiple generations or to influence the speech's continuity 
	 *  in the current generation.
	 */
	public String previous_text;
	
	
	

	
	/**The text that comes after the text of the current request.
	 *  Can be used to improve the speech's continuity when concatenating together multiple generations or to influence the speech's continuity in the current generation.
	**/
	public String next_text;
	
	
	
	/**A list of request_id of the samples that were generated 
	 * before this generation. Can be used to improve the speech's 
	 * continuity when splitting up a large task into multiple requests. 
	 * The results will be best when the same model is used across 
	 * the generations. In case both previous_text and previous_request_ids 
	 * is send, previous_text will be ignored. A maximum of 3 request_ids 
	 * can be send.
	**/
	public List<String> previous_request_ids;
	
	
	/**A list of request_id of the samples that come after this generation.
	 *	next_request_ids is especially useful for maintaining the speech's 
	 *	continuity when regenerating a sample that has had some audio quality issues. For example, if you have generated 3 speech clips, and you want to improve clip 2, passing the request id of clip 3 as a next_request_id (and that of clip 1 as a previous_request_id) will help maintain natural flow in the combined speech. The results will be best when the same model is used across the generations. In case both next_text and next_request_ids is send, next_text will be ignored. 
	 * A maximum of 3 request_ids can be send
	**/
	public List<String> next_request_ids;
	
	
	/**
	 * This parameter controls text normalization with three modes: 'auto', 
	 * 'on', and 'off'. When set to 'auto', the system will automatically 
	 * decide whether to apply text normalization (e.g., spelling out numbers). 
	 * With 'on', text normalization will always be applied, while with 'off', 
	 * it will be skipped. For 'eleven_turbo_v2_5' and 'eleven_flash_v2_5' models,
	 *  text normalization can only be enabled with Enterprise plans.
	 */
	public TextNormalization apply_text_normalization;
	

	
	
	/**
	voice_settings": {
    "stability": 0.5,
    "style": 0,
    "speed": 1,
    "use_speaker_boost": true,
    "similarity_boost": 0.75
    }**/

	/** Determines how stable the voice is and the randomness between each generation. 
	 * Lower values introduce broader emotional range for the voice.
	   Higher values can result in a monotonous voice with limited emotion.
	    
	   range [0.0 - 1.0]
	 **/
	public Double stability;
	
	
	
	/**  Determines the style exaggeration of the voice. This setting attempts to
	 *  amplify the style of the original speaker. It does consume additional 
	 *  computational resources and might increase latency if set to anything other than 0.
	 */
	public Double style;
	 
	

	/** Adjusts the speed of the voice. A value of 1.0 is the default speed, while values less than 1.0 
	 * slow down the speech, and values greater than 1.0 speed it up
	 */
	public Double speed;
	
	
	/**
	 * Voice settings overriding stored settings for the given voice. 
	 * They are applied only on the given request.
 	 */
	public VoiceSettings voice_settings;
	
	

}
