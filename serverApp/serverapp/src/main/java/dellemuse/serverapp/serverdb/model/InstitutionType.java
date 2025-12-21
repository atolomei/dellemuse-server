package dellemuse.serverapp.serverdb.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "institutionType")
@JsonInclude(Include.NON_NULL)
public class InstitutionType extends DelleMuseObject {

	@Column(name = "language")
	private String language;
	
	public String getLanguage() {
		return this.language;
	}

	public void setLanguage(String lang) {
		language = lang;
	}

    public InstitutionType() {
    }

    @Override
	public String getObjectClassName() {
		return InstitutionType.class.getSimpleName();
	}
}
