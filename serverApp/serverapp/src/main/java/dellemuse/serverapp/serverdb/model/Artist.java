package dellemuse.serverapp.serverdb.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.serverapp.icons.Icons;
import dellemuse.serverapp.page.PrefixUrl;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseIdNameSerializer;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseUserSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "artist")
@JsonInclude(Include.NON_NULL)
public class Artist extends MultiLanguageObject {

	@OneToOne(fetch = FetchType.EAGER, targetEntity = Person.class)
	@JoinColumn(name = "person_id", nullable = true)
	@JsonBackReference
	@JsonSerialize(using = DelleMuseUserSerializer.class)
	@JsonProperty("person")
	private Person person;

	@JsonIgnore
	@Column(name = "sortlastfirstname")
	private String sortlastfirstname;
	
	@Column(name = "lastname")
	private String lastname;

	@ManyToMany(mappedBy = "artists", fetch = FetchType.LAZY)
	@JsonIgnoreProperties("artists")
	private Set<ArtWork> artworks = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "artist_sites", joinColumns = @JoinColumn(name = "artist_id"), inverseJoinColumns = @JoinColumn(name = "site_id"))
	@JsonIgnoreProperties("artistSites")
	private Set<Site> artistSites = new HashSet<>();

	@Column(name = "nickname")
	private String nickname;
	
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Site.class)
	@JoinColumn(name = "site_id", nullable = false)
	@JsonManagedReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	private Site site;
	
	
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
		setName(person.getName());
		setLastname(person.getLastname());
	}

	@Override
	public String getDisplayname() {
		//if (this.person != null)
		//	return this.person.getDisplayName();
		  return getLastFirstname(); 
	}

	
	@JsonIgnore
	public String getTitle() {
		return getFirstLastname();
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

	 

	public Set<Site> getArtistSites() {
		return artistSites;
	}

	public void setArtistSites(Set<Site> artistSites) {
		this.artistSites = artistSites;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public void setLastname(String lastname) {
		this.lastname = lastname;
		generateSortName();
	}
	
	public String getLastname() {
		return lastname;
	}

	public String getLastFirstname() {

		StringBuilder str = new StringBuilder();

		if (getLastname() != null) {
			str.append(getLastname());
		}

		if (getName() != null && getName().length() > 0) {
			if (str.length() > 0)
				str.append(", ");

			str.append(getName());
		}
		return str.toString();
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
	
	
	private void generateSortName() {
		this.sortlastfirstname = (lastname != null ? lastname.toLowerCase().trim() : "") + (getName() != null ? (" " + getName().toLowerCase().trim()) : "");
	}

	public String getSortlastfirstname() {
		return sortlastfirstname;
	}

	public Site getSite() {
		return site;
	}

	public void setSortlastfirstname(String sortlastfirstname) {
		this.sortlastfirstname = sortlastfirstname;
	}

	public void setSite(Site site) {
		this.site = site;
	}
	

}
