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

import dellemuse.serverapp.elevenlabs.VoiceSettings;
import dellemuse.serverapp.icons.Icons;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseResourceSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "voice")
@JsonInclude(Include.NON_NULL)
public class Voice extends DelleMuseObject {

	@Column(name = "voiceId")
	private String voiceId;
	
	@Column(name = "language")
	private String language;
		
	@Column(name = "languageRegion")
	private String languageRegion;
	
	@Column(name = "info")
	private String info;
		
	@Column(name = "sex")
	private String sex;
		
	
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "voiceSettings", columnDefinition = "jsonb")
	private Map<String, String> voiceSettings;


	@OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
	@JoinColumn(name = "audio", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonProperty("audio")
	@JsonSerialize(using = DelleMuseResourceSerializer.class)
	private Resource audio;
	
	
	
	
	public Voice() {
	 
	}
	
	public static String getIcon() {
		return Icons.Voice;
	}

	
	
	public String getVoiceId() {
		return voiceId;
	}

	public String getLanguage() {
		return language;
	}

	public String getLanguageRegion() {
		return languageRegion;
	}

 
	public Map<String, String> getVoiceSettings() {
		return voiceSettings;
	}

	public void setVoiceId(String voiceId) {
		this.voiceId = voiceId;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void setLanguageRegion(String languageRegion) {
		this.languageRegion = languageRegion;
	}

	 

	public void setVoiceSettings(Map<String, String> voiceSettings) {
		this.voiceSettings = voiceSettings;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Resource getAudio() {
		return audio;
	}

	public void setAudio(Resource audio) {
		this.audio = audio;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
}


/**



CREATE TABLE elvoice (
						
						id					 bigint primary key default nextval('sequence_id'),

						name				 character varying(512) not null,
						nameKey				 character varying(512),

						title		 		 character varying(1024),
						titleKey			 character varying(512),

						url	 			     character varying(4096),
						state			     integer default 3,
						
						voiceid				 character varying(512),
						language		  	 character varying(64) default 'es',
						languageRegion       character varying(64),
						comment			 	 text,
						voiceSettings		 jsonb,	
						
						info		 		 text,
						infoKey 			 character varying(512),
						
						draft				 text,
						
						audio				 bigint references resource(id) on delete restrict,
						
						created				 timestamp with time zone DEFAULT now() not null,
						lastmodified		 timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	 bigint references users(id) on delete restrict not null
						
						);


					
*/