package dellemuse.serverapp.serverdb.model;

import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.model.JsonObject;
import dellemuse.serverapp.elevenlabs.VoiceSettings;
import dellemuse.serverapp.icons.Icons;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseResourceSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

//@Entity
//@Table(name = "cachedurl")
@JsonInclude(Include.NON_NULL)
public class CachedURL extends JsonObject  {

	@Column(name = "id")
	private String id;
	
	@Column(name = "url")
	private String url;
	
	
	public CachedURL() {
	 
	}



	public String getId() {
		return id;
	}



	public String getUrl() {
		return url;
	}



	public void setId(String sid) {
		this.id = sid;
	}


	public void setUrl(String url) {
		this.url = url;
	}
	

	
}

