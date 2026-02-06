package dellemuse.serverapp.serverdb.model.record;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.serverapp.page.PrefixUrl;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseIdNameSerializer;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseResourceSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "guideContentRecord")
@JsonInclude(Include.NON_NULL)
public class GuideContentRecord extends TranslationRecord {

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = GuideContent.class)
	@JoinColumn(name = "guideContent_id", referencedColumnName = "id", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	@JsonProperty("guideContent")
	private GuideContent guideContent;

	@Column(name = "infoAccessible")
	private String infoAccessible;
	
	@Column(name = "infoAccesible_hash")
	private int infoAccessibleHash;
	
	@OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
	@JoinColumn(name = "audioAccessible", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonProperty("audioAccessible")
	@JsonSerialize(using = DelleMuseResourceSerializer.class)
	private Resource audioAccessible;
	
	public GuideContentRecord() {
	}

	@Override
	public String getObjectClassName() {
		return GuideContentRecord.class.getSimpleName();
	}

	public GuideContent getGuideContent() {
		return guideContent;
	}

	public void setGuideContent(GuideContent guideContent) {
		this.guideContent = guideContent;
	}

	@Override
	public MultiLanguageObject getParentObject() {
		return this.guideContent != null ? this.guideContent : null;
	}

	@Override
	public boolean isAudioStudioEnabled() {
		return true;
	}

	@Override
	public String getPrefixUrl() {
			return PrefixUrl.GuideContent;
	}

	public String getInfoAccessible() {
		return infoAccessible;
	}

	public void setInfoAccessible(String infoAccesible) {
		this.infoAccessible = infoAccesible;
	}

	public int getInfoAccessibleHash() {
		return infoAccessibleHash;
	}

	public void setInfoAccessibleHash(int infoAccesibleHash) {
		this.infoAccessibleHash = infoAccesibleHash;
	}

	public Resource getAudioAccessible() {
		return audioAccessible;
	}

	public void setAudioAccessible(Resource audioAccesible) {
		this.audioAccessible = audioAccesible;
	}
	
}
