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
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseIdNameSerializer;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

	public GuideContentRecord() {
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
};
