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
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "siteArtist")
@JsonInclude(Include.NON_NULL)
public class SiteArtist extends DelleMuseObject {

	public static final String getIcon() {
		return Icons.Artist;
	}
	
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Artist.class)
	@JoinColumn(name = "artist_id", nullable = false)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseUserSerializer.class)
	@JsonProperty("artist")
	private Artist artist;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Site.class)
	@JoinColumn(name = "site_id", nullable = false)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseUserSerializer.class)
	@JsonProperty("site")
	private Site site;

	public SiteArtist() {
	}

	public Artist getArtist() {
		return artist;
	}

	public Site getSite() {
		return site;
	}

	public void setArtist(Artist artist) {
		this.artist = artist;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	@Override
	public String getObjectClassName() {
		return SiteArtist.class.getSimpleName();
	}

	public String getPrefixUrl() {
		return PrefixUrl.SiteArtist;
	}

	@Override
	public boolean equals(Object o) {

		if (o == null)
			return false;

		if (this == o)
			return true;

		if (!(o instanceof SiteArtist))
			return false;

		if (this.getId() == null)
			return false;

		if ((o instanceof SiteArtist)) {

			if (((SiteArtist) o).getId() == null)
				return false;

			return ((SiteArtist) o).getId().equals(getId());
		}

		return false;
	}

	
	public String getFirstLastname() {
		if (this.artist == null)
			return null;
		return this.artist.getFirstLastname();
	}

}
