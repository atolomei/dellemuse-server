package dellemuse.serverapp.serverdb.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.serverapp.icons.Icons;
import dellemuse.serverapp.page.PrefixUrl;

import dellemuse.serverapp.serverdb.model.serializer.DelleMuseUserSerializer;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "artist")
@JsonInclude(Include.NON_NULL)
public class Artist extends MultiLanguageObject {

	@OneToOne(fetch = FetchType.EAGER, targetEntity = Person.class)
	@JoinColumn(name = "person_id", nullable = false)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseUserSerializer.class)
	@JsonProperty("person")
	private Person person;
	
	@ManyToMany(mappedBy = "artists", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("artists")
    private Set<ArtWork> artworks = new HashSet<>();

    public Set<ArtWork> getArtworks() {
        return artworks;
    }
 
	public Artist() {
	}

	public void setArtworks(Set<ArtWork> artworks) {
		this.artworks = artworks;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Override
 public String getDisplayname() {
	 if (this.person!=null)
		 return this.person.getDisplayName();
	 return null;
 }

	@Override
	public String getObjectClassName() {
		return Artist.class.getSimpleName();
	}

	public String getPrefixUrl() {
		return PrefixUrl.Artist;
	}

	@Override
	public boolean equals(Object o) {

		if (o == null)
			return false;

		if (this == o)
			return true;

		if (!(o instanceof Artist))
			return false;

		if (this.getId() == null)
			return false;

		if ((o instanceof Artist)) {

			if (((Artist) o).getId() == null)
				return false;

			return ((Artist) o).getId().equals(getId());
		}

		return false;
	}

	public static final String getIcon() {
		return Icons.Artist;
	}

	public String getFirstLastname() {
		if (this.person==null)
			return null;
		return this.person.getFirstLastname();
	}

}
