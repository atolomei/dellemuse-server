package dellemuse.serverapp.serverdb.model.record;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.serverapp.page.PrefixUrl;
import dellemuse.serverapp.serverdb.model.Artist;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
 
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseIdNameSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.Table;

@Entity
@Table(name = "artistRecord")
@JsonInclude(Include.NON_NULL)
public class ArtistRecord extends TranslationRecord {

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Artist.class)
	@JoinColumn(name = "artist_id", referencedColumnName = "id", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	@JsonProperty("artist")
	private Artist artist;

	@JsonProperty("lastname")
	@Column(name = "lastname")
	private String lastname;

	
	
	public ArtistRecord() {
	}
	
	@Override
	public String getObjectClassName() {
		return ArtistRecord.class.getSimpleName();
	}

	
	public String getFirstLastname() {

		StringBuilder str = new StringBuilder();

		if (getName() != null) {
			str.append(getName());
		}

		if (getLastname() != null && getLastname().length() > 0) {
			if (str.length() > 0)
				str.append(" ");
			str.append(getLastname());
		}
		return str.toString();
	}
	
	 
	
	
	public String getTitle() {
		if (super.getTitle() != null)
			super.getTitle();
		return getName();
	}

	@JsonIgnore
	public String getDisplayName() {

		if (getTitle() != null)
			return getTitle();

		if (getName() != null)
			return getName();

		return "null";
	}

	@Override
	public boolean isAudioStudioEnabled() {
		return false;
	}

	public void setName(String name) {
		super.setName(name);
		 
	}

	
	@Override
	public String getPrefixUrl() {
		return PrefixUrl.Artist;
	}

	
	public void setArtist(Artist a) {
		this.artist = a;
	}

	public String getLastFirstname() {

		StringBuilder str = new StringBuilder();

		if (getName() != null && getName().length() > 0) {
			if (str.length() > 0)
				str.append(", ");

			str.append(getName());
		}
		return str.toString();
	}

	 

	@Override
	public MultiLanguageObject getParentObject() {
		return this.artist != null ? this.artist : null;
	}

	public Artist getArtist() {
		return artist;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	public String getLastname() {
		return lastname;
	}


}
