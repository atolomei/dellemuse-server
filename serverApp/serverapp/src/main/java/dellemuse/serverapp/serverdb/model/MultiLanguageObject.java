package dellemuse.serverapp.serverdb.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.serverapp.jpa.events.ArtWorkEventListener;
import dellemuse.serverapp.jpa.events.MultiLanguageObjectEventListener;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseResourceSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

/**
 * extraField_1 extraField_1_hash extraField_2 extraField_2_hash extraField_3
 * extraField_3_hash
 * 
 * address opens
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonInclude(Include.NON_NULL)
@EntityListeners(MultiLanguageObjectEventListener.class)
public abstract class MultiLanguageObject extends DelleMuseObject {

	/**
	 * language -> it is the language of this content masterLanguage -> it is the
	 * language used by all the TranslationRecords associated with this object to
	 * translate
	 */
	@Column(name = "masterlanguage")
	private String masterLanguage;

	@Column(name = "subtitle")
	private String subtitle;

	@Column(name = "info")
	private String info;

	@Column(name = "intro")
	private String intro;

	@Column(name = "spec")
	private String spec;

	@Column(name = "opens")
	private String opens;
	
	@Column(name = "translation")
	@JsonProperty("translateMode")
	private TranslateMode translateMode;
	
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

	@OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
	@JoinColumn(name = "speechaudio", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonProperty("speechaudio")
	@JsonSerialize(using = DelleMuseResourceSerializer.class)
	private Resource speechaudio;

	@Column(name = "language")
	private String language;

	public String getLanguage() {
		return this.language;
	}

	public void setLanguage(String lang) {
		language = lang;
	}

	public abstract String getPrefixUrl();

	public void setTranslateMode(TranslateMode m) {
		this.translateMode = m;
	}

	public TranslateMode getTranslateMode() {
		return this.translateMode;
	}

	public String getMasterLanguage() {
		return this.masterLanguage;
	}

	public void setML(Language la) {
		this.setMasterLanguage(la.getLanguageCode());
	}

	@JsonIgnore
	public Language getML() {
		return Language.of(getMasterLanguage());
	}

	public void setMasterLanguage(String m) {
		this.masterLanguage = m;
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

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public Resource getPhoto() {
		return photo;
	}

	public void setPhoto(Resource photo) {
		this.photo = photo;
	}

	public Resource getVideo() {
		return video;
	}

	public void setVideo(Resource video) {
		this.video = video;
	}

	public Resource getAudio() {
		return audio;
	}

	public void setAudio(Resource audio) {
		this.audio = audio;
	}

	public Resource getSpeechAudio() {
		return speechaudio;
	}

	public void setSpeechAudio(Resource audio) {
		this.speechaudio = audio;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getOpens() {
		return opens;
	}

	public void setOpens(String opens) {
		this.opens = opens;
	}

}
