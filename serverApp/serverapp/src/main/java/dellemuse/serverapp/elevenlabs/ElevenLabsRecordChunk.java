package dellemuse.serverapp.elevenlabs;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import dellemuse.model.JsonObject;


@JsonInclude(JsonInclude.Include.NON_NULL)
class ElevenLabsRecordChunk extends  ElevenLabsBaseRecord {
	 		
		@JsonIgnore
		public int chunkOrder;
		
		 
    public ElevenLabsRecordChunk() {
    }
    
    
    
}


