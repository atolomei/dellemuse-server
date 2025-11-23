package dellemuse.serverapp.elevenlabs;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;

@JsonInclude(JsonInclude.Include.NON_NULL)
class ElevenLabsRecord extends  ElevenLabsBaseRecord {

    public ElevenLabsRecord() {
    }

    public List<ElevenLabsRecordChunk> chunks() {
    	
    	List<ElevenLabsRecordChunk> list = new ArrayList<ElevenLabsRecordChunk>();
    	
    	List<String> texts = splitText(this.text,  getServerDBSettings().getAudioMaxCharsPerRequest());
    	
    	int n = 0;
    	
    	for (String str: texts) {

    		ElevenLabsRecordChunk rc = new ElevenLabsRecordChunk();
    		
    		rc.model_id=this.model_id;
    		
    		rc.text=str;
    		
    		if (this.seed!=null)
    			rc.seed=this.seed;
    		
    		rc.pronunciation_dictionary_locators = this.pronunciation_dictionary_locators;
    		
    		rc.language_code=this.language_code;
    		
    		rc.previous_text=this.previous_text;
    		
    		rc.next_text=this.next_text;
    		
    		rc.previous_request_ids=this.previous_request_ids;
    		
    		rc.next_request_ids=this.next_request_ids;
    		
    		rc.apply_text_normalization=this.apply_text_normalization;
    		
        	if (this.stability!=null)
        		rc.stability=this.stability;
        	
        	if (this.style!=null)
        		rc.style=this.style;

        	if (this.speed!=null)
        		rc.speed=this.speed;
        	
    		rc.chunkOrder=n++;
    		
    		list.add(rc);
    	}
    	return list;
    	
    }
   
    
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    
    protected ServerDBSettings getServerDBSettings() {
		return (ServerDBSettings) ServiceLocator.getInstance().getBean(ServerDBSettings.class);
	}
    
    
	private List<String> splitText(String text, int maxChars) {
        List<String> chunks = new ArrayList<>();
        int start = 0;
        while (start < text.length()) {
            int end = Math.min(text.length(), start + maxChars);
            if (end < text.length()) {
                int lastSpace = text.lastIndexOf(' ', end);
                if (lastSpace > start) end = lastSpace;
            }
            chunks.add(text.substring(start, end).trim());
            start = end;
        }
        return chunks;
    }
    
    
    
    
}


