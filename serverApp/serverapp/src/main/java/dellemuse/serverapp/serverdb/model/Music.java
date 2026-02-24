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
import dellemuse.serverapp.music.MusicGenre;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseResourceSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "music")
@JsonInclude(Include.NON_NULL)
public class Music extends DelleMuseObject {

	@Column(name = "license")
	private String license;

	@Column(name = "genre")
	@Enumerated(EnumType.ORDINAL)
	private MusicGenre genre;
	
	@Column(name = "url")
	private String url;
	
	@Column(name = "composerStr")
	private String composerStr;
		
	
	@Column(name = "info")
	private String info;
		
	@Column(name = "technical_info")
	private String technicalInfo;
	
	@Column(name = "royaltyFree")
	private boolean royaltyFree;
		
	
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "info_json", columnDefinition = "jsonb")
	private Map<String, String> infoJson;

	@OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
	@JoinColumn(name = "audio", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonProperty("audio")
	@JsonSerialize(using = DelleMuseResourceSerializer.class)
	private Resource audio;
	
	
	public Music() {
	 
	}
	
	public static String getIcon() {
		return Icons.Music;
	}

	public String getUrl() {
		return url;
	}

	public String getComposerStr() {
		return composerStr;
	}

	public String getInfo() {
		return info;
	}

	public Map<String, String> getInfoJson() {
		return infoJson;
	}

	public Resource getAudio() {
		return audio;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setComposerStr(String composerStr) {
		this.composerStr = composerStr;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public void setInfoJson(Map<String, String> infoJson) {
		this.infoJson = infoJson;
	}

	public void setAudio(Resource audio) {
		this.audio = audio;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public boolean isRoyaltyFree() {
		return royaltyFree;
	}

	public void setRoyaltyFree(boolean royaltyFree) {
		this.royaltyFree = royaltyFree;
	}

	public String getTechnicalInfo() {
		return technicalInfo;
	}

	public void setTechnicalInfo(String technicalInfo) {
		this.technicalInfo = technicalInfo;
	}

	public MusicGenre getGenre() {
		return genre;
	}

	public void setGenre(MusicGenre genre) {
		this.genre = genre;
	}

	
}


/**

 

					
*/