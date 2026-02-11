package dellemuse.serverapp.elevenlabs;

import java.io.Serializable;

import dellemuse.model.JsonObject;

public class ELVoice extends JsonObject implements Serializable {

	private String id;
	private String voiceId;
	private String language;
	private String languageRegion;
	private String name;
	private String comment;
	
	private VoiceSettings voiceSettings;
	

	public ELVoice( String id, String voiceId, String language, String languageRegion, String name, String comment) {
					this(id,voiceId, language,   languageRegion, name, comment, null);
	}		
	public ELVoice( String id, String voiceId, String language, String languageRegion, String name, String comment, VoiceSettings voiceSettings) {
		this.voiceId=voiceId;
		this.id=id;
		this.name=name;
		this.language=language;
		this.languageRegion=languageRegion;
		this.comment=comment;
		this.voiceSettings=voiceSettings;
		
	}

	public VoiceSettings getVoiceSettings() {
		return this.voiceSettings;
	}
	
	public String getId() {
		return id;
	}
	public String getLanguage() {
		return language;
	}
	public String getLanguageRegion() {
		return languageRegion;
	}
	public void setLanguageRegion(String languageRegion) {
		this.languageRegion = languageRegion;
	}
	public void setVoiceSettings(VoiceSettings voiceSettings) {
		this.voiceSettings = voiceSettings;
	}
	public String getName() {
		return name;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getVoiceId() {
		return voiceId;
	}

	public void setVoiceId(String voiceId) {
		this.voiceId = voiceId;
	}

	
	
}
