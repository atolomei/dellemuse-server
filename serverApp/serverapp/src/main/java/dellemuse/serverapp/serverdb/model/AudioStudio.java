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

import dellemuse.serverapp.audiostudio.AudioStudioParentObject;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionGuideRecord;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionItemRecord;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionRecord;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionSectionRecord;
import dellemuse.serverapp.serverdb.model.record.GuideContentRecord;

import dellemuse.serverapp.serverdb.model.serializer.DelleMuseIdNameSerializer;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseResourceSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "audiostudio")
@JsonInclude(Include.NON_NULL)
public class AudioStudio extends DelleMuseObject {

	@OneToOne(fetch = FetchType.LAZY, targetEntity = ArtExhibitionGuide.class)
	@JoinColumn(name = "artExhibitionGuide_id", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	private ArtExhibitionGuide artExhibitionGuide;

	@OneToOne(fetch = FetchType.LAZY, targetEntity = GuideContent.class)
	@JoinColumn(name = "guideContent_id", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	private GuideContent guideContent;

	// falta Sections

	@OneToOne(fetch = FetchType.LAZY, targetEntity = ArtExhibitionGuideRecord.class)
	@JoinColumn(name = "artExhibitionGuideRecord_id", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	private ArtExhibitionGuideRecord artExhibitionGuideRecord;

	@OneToOne(fetch = FetchType.LAZY, targetEntity = GuideContentRecord.class)
	@JoinColumn(name = "guideContentRecord_id", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	private GuideContentRecord guideContentRecord;

	@OneToOne(fetch = FetchType.LAZY, targetEntity = ArtExhibitionItemRecord.class)
	@JoinColumn(name = "artExhibitionItemRecord_id", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	private ArtExhibitionItemRecord artExhibitionItemRecord;

	@OneToOne(fetch = FetchType.LAZY, targetEntity = ArtExhibitionRecord.class)
	@JoinColumn(name = "artExhibitionRecord_id", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	private ArtExhibitionRecord artExhibitionRecord;

	@OneToOne(fetch = FetchType.LAZY, targetEntity = ArtExhibitionSectionRecord.class)
	@JoinColumn(name = "artExhibitionSectionRecord_id", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	private ArtExhibitionSectionRecord artExhibitionSectionRecord;

	/** ---------------------------- **/

	@Column(name = "info")
	private String info;

	@OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
	@JoinColumn(name = "audioSpeech", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonProperty("audioSpeech")
	@JsonSerialize(using = DelleMuseResourceSerializer.class)
	private Resource audioSpeech;

	@OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
	@JoinColumn(name = "audioSpeechMusic", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonProperty("audioSpeechMusic")
	@JsonSerialize(using = DelleMuseResourceSerializer.class)
	private Resource audioSpeechMusic;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "json_data", columnDefinition = "jsonb")
	private Map<String, String> settings;

	@Column(name = "musicUrl")
	private String musicUrl;

	@Column(name = "audio_speech_hash")
	private int audioSpeechHash;

	@Column(name = "audio_speech_music_hash")
	private int audioSpeechMusicHash;

	
	/** ---------------------------- **/
	
	
	public AudioStudio() {
	}

	public AudioStudioParentObject getParentOject() {

		if (artExhibitionGuide != null)
			return artExhibitionGuide;

		if (guideContent != null)
			return guideContent;

		if (artExhibitionGuideRecord != null)
			return artExhibitionGuideRecord;

		if (guideContentRecord != null)
			return guideContentRecord;

		return null;
	}

	public ArtExhibitionGuide getArtExhibitionGuide() {
		return artExhibitionGuide;
	}

	public GuideContent getGuideContent() {
		return guideContent;
	}

	public Resource getAudioSpeech() {
		return audioSpeech;
	}

	public Resource getAudioSpeechMusic() {
		return audioSpeechMusic;
	}

	public Map<String, String> getSettings() {
		return settings;
	}

	public String getInfo() {
		return info;
	}

	public void setArtExhibitionGuide(ArtExhibitionGuide artExhibitionGuide) {
		this.artExhibitionGuide = artExhibitionGuide;
	}

	public void setGuideContent(GuideContent guideContent) {
		this.guideContent = guideContent;
	}

	public void setAudioSpeechMusic(Resource audio) {
		this.audioSpeechMusic = audio;
	}

	public void setAudioSpeech(Resource speechaudio) {
		this.audioSpeech = speechaudio;
	}

	public void setSettings(Map<String, String> settings) {
		this.settings = settings;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public ArtExhibitionGuideRecord getArtExhibitionGuideRecord() {
		return artExhibitionGuideRecord;
	}

	public GuideContentRecord getGuideContentRecord() {
		return guideContentRecord;
	}

	public ArtExhibitionItemRecord getArtExhibitionItemRecord() {
		return artExhibitionItemRecord;
	}

	public ArtExhibitionRecord getArtExhibitionRecord() {
		return artExhibitionRecord;
	}

	public ArtExhibitionSectionRecord getArtExhibitionSectionRecord() {
		return artExhibitionSectionRecord;
	}

	public void setArtExhibitionGuideRecord(ArtExhibitionGuideRecord artExhibitionGuideRecord) {
		this.artExhibitionGuideRecord = artExhibitionGuideRecord;
	}

	public void setGuideContentRecord(GuideContentRecord guideContentRecord) {
		this.guideContentRecord = guideContentRecord;
	}

	public void setArtExhibitionItemRecord(ArtExhibitionItemRecord artExhibitionItemRecord) {
		this.artExhibitionItemRecord = artExhibitionItemRecord;
	}

	public void setArtExhibitionRecord(ArtExhibitionRecord artExhibitionRecord) {
		this.artExhibitionRecord = artExhibitionRecord;
	}

	public void setArtExhibitionSectionRecord(ArtExhibitionSectionRecord artExhibitionSectionRecord) {
		this.artExhibitionSectionRecord = artExhibitionSectionRecord;
	}

	public int getAudioSpeechHash() {
		return audioSpeechHash;
	}

	public int getAudioSpeechMusicHash() {
		return audioSpeechMusicHash;
	}

	public void setAudioSpeechHash(int audioSpeechHash) {
		this.audioSpeechHash = audioSpeechHash;
	}

	public void setAudioSpeechMusicHash(int audioSpeechMusicHash) {
		this.audioSpeechMusicHash = audioSpeechMusicHash;
	}

	public String getMusicUrl() {
		return musicUrl;
	}

	public void setMusicUrl(String musicUrl) {
		this.musicUrl = musicUrl;
	}

}
