package dellemuse.serverapp.serverdb.model.record;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.serverapp.page.PrefixUrl;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseIdNameSerializer;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "institutionRecord")
@JsonInclude(Include.NON_NULL)
public class InstitutionRecord extends TranslationRecord {

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Institution.class)
	@JoinColumn(name = "institution_id", referencedColumnName = "id", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	@JsonProperty("institution")
	private Institution institution;

	public InstitutionRecord() {
	}

	@Override
	public String getPrefixUrl() {
		return PrefixUrl.Institution;
	}

	@Override
	public String getTitle() {
		return getName();
	}

	public Institution getInstitution() {
		return institution;
	}

	public void setInstitution(Institution institution) {
		this.institution = institution;
	}

	@Override
	public MultiLanguageObject getParentObject() {
		return this.institution != null ? this.institution : null;
	}

	@Override
	public boolean isAudioStudioEnabled() {
		return false;
	}

};
