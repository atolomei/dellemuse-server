package dellemuse.serverapp.serverdb.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import dellemuse.serverapp.serverdb.model.record.SiteRecord;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "artExhibitionStatusType")
@JsonInclude(Include.NON_NULL)
public class ArtExhibitionStatusType extends DelleMuseObject {

	@Column(name = "language")
	private String language;

	public String getLanguage() {
		return this.language;
	}

	public void setLanguage(String lang) {
		language = lang;
	}

	public ArtExhibitionStatusType() {
	}
	
	@Override
	public String getObjectClassName() {
		return ArtExhibitionStatusType.class.getSimpleName();
	}

}
