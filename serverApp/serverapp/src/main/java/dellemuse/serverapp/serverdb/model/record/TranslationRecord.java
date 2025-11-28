package dellemuse.serverapp.serverdb.model.record;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.serverapp.audiostudio.AudioStudioParentObject;
import dellemuse.serverapp.serverdb.model.DelleMuseObject;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseResourceSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;


/**
 * 
 * values [ {"name", "text", "hashSrc"}, ]
 * fieldName
 * translation text
 * hashOfsrc
 * 
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonInclude(Include.NON_NULL)
public abstract class TranslationRecord extends DelleMuseObject implements AudioStudioParentObject {

	@Column(name = "subtitle")
	private String subtitle;
	
	@Column(name = "intro")
	private String intro;

	@Column(name = "info")
	private String info;

	@Column(name = "otherJson")
	private String otherJson;

	@Column(name = "otherJson_hash")
	private int otherJsonHash;
	
	@OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
	@JoinColumn(name = "photo", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonProperty("photo")
	@JsonSerialize(using = DelleMuseResourceSerializer.class)
	private Resource photo;

	@OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
	@JoinColumn(name = "video", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonProperty("video")
	@JsonSerialize(using = DelleMuseResourceSerializer.class)
	private Resource video;

	@OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
	@JoinColumn(name = "audio", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonProperty("audio")
	@JsonSerialize(using = DelleMuseResourceSerializer.class)
	private Resource audio;
	
	/**
	 * by default it is true, sometimes the thumbnail 
	 * generated is not correct, for those images we dont use thumbnail
	 * 
	 */
	//@Column(name = "usethumbnail")
	//@JsonProperty("usethumbnail")
	//private boolean usethumbnail;
	
	@Column(name = "subtitle_hash")
	private int subtitleHash;
	
	@Column(name = "name_hash")
	private int nameHash;
	
	@Column(name = "info_hash")
	private int infoHash;
	
	@Column(name = "intro_hash")
	private int introHash;
	
	@Column(name = "spec")
	private String spec;

	@Column(name = "spec_hash")
	private int specHash;
	
	@Column(name = "opens")
	private String opens;

	@Column(name = "opens_hash")
	private int opensHash;
	
	@Column(name = "audioAuto")
	private boolean audioAuto;
	
	public abstract MultiLanguageObject getParentObject();

	
	public int getSubtitleHash() {
		return subtitleHash;
	}

	public int getNameHash() {
		return nameHash;
	}

	public int getInfoHash() {
		return infoHash;
	}

	public int getIntroHash() {
		return introHash;
	}

	public void setSubtitleHash(int subtitleHash) {
		this.subtitleHash = subtitleHash;
	}

	public void setNameHash(int nameHash) {
		this.nameHash = nameHash;
	}

	public void setInfoHash(int infoHash) {
		this.infoHash = infoHash;
	}

	public int getOtherJsonHash() {
		return otherJsonHash;
	}

	public void setOtherJsonHash(int otherJsonHash) {
		this.otherJsonHash = otherJsonHash;
	}

	public void setIntroHash(int introHash) {
		this.introHash = introHash;
	}
	
	public String getOtherJson() {
		return otherJson;
	}

	public void setOtherJson(String otherJson) {
		this.otherJson = otherJson;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getIntro() {
		return intro;
	}

	public Resource getPhoto() {
		return photo;
	}

	public Resource getVideo() {
		return video;
	}

	public Resource getAudio() {
		return audio;
	}

	//public boolean isUsethumbnail() {
	//	return usethumbnail;
	//}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public void setPhoto(Resource photo) {
		this.photo = photo;
	}

	public void setVideo(Resource video) {
		this.video = video;
	}

	public void setAudio(Resource audio) {
		this.audio = audio;
	}

	//public void setUsethumbnail(boolean usethumbnail) {
	//	this.usethumbnail = usethumbnail;
	//}

	@Override
	protected String baseJSON() {
		
		StringBuilder str = new StringBuilder();
		
		 
		str.append("  \"id\": "+ getId().toString());
		
		if (getName()!=null)
			str.append(", \"name\": \""+ getName().toString()+"\"");

		if (getLanguage()!=null)
			str.append(", \"language\": \""+ getLanguage()+"\"");
		
		
		if (getSubtitle()!=null)
			str.append(", \"subtitle\": \""+ getSubtitle().toString()+"\"");
		
		
		if (getLastModified()!=null)
			str.append(", \"lastModified\": \""+ getDateTimeFormatter().format(getLastModified())+"\"");

		return str.toString();
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(getClass().getSimpleName());
		str.append(" {");
		str.append(baseJSON());
		if(getParentObject()!=null) {
			str.append(", \""+ getParentObject() +"\": {\"id\""+ getParentObject().getId().toString()+"}");
		}
		str.append(" }");

		return str.toString();
	}


	public String getSpec() {
		return spec;
	}

	public int getSpecHash() {
		return specHash;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public void setSpecHash(int specHash) {
		this.specHash = specHash;
	}

	public void setOpens(String opens) {
		this.opens=opens;
	}
	
	public String getOpens() {
		return opens;
	}
	 
	public int getOpensHash() {
		return opensHash;
	}
	 
	public void setOpensHash(int h) {
		 opensHash=h;
	}

	public boolean isAudioAuto() {
		return audioAuto;
	}

	public void setAudioAuto(boolean audioAuto) {
		this.audioAuto = audioAuto;
	}


	public abstract boolean isAudioStudioEnabled();
	
}



