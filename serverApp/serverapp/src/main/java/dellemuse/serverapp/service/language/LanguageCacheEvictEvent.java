package dellemuse.serverapp.service.language;

import org.springframework.context.ApplicationEvent;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.DellemuseObjectMapper;
import dellemuse.serverapp.serverdb.model.DelleMuseObject;
import io.odilon.model.SharedConstant;
 

public class LanguageCacheEvictEvent extends ApplicationEvent {

	   private static final long serialVersionUID = 1L;

	   @JsonIgnore
	   static private Logger logger = Logger.getLogger(LanguageCacheEvictEvent.class.getName());
	   
	   
	   @JsonIgnore
	   static final private ObjectMapper mapper = new DellemuseObjectMapper();

	   String objectClassName;
	   Long oid;

	   public LanguageCacheEvictEvent(String objectClassName, Long id) {
		   super(objectClassName+"-"+id.toString());
		   this.objectClassName = objectClassName;
		   this.oid=id;
	   }

	   
	   public String toJSON() {
	        try {
	            return getObjectMapper().writeValueAsString(this);
	        } catch (JsonProcessingException e) {
	            logger.error(e, SharedConstant.NOT_THROWN);
	            return "\"error\":\"" + e.getClass().getName() + " | " + e.getMessage() + "\"";
	        }
	    }

	    @JsonIgnore
	    public ObjectMapper getObjectMapper() {
	        return mapper;
	    }


		public String getObjectClassName() {
			return objectClassName;
		}


		public Long getOid() {
			return oid;
		}


		public void setObjectClassName(String className) {
			this.objectClassName = className;
		}


		public void setOid(Long oid) {
			this.oid = oid;
		}

}
