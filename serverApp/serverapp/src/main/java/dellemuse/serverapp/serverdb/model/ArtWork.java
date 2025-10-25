package dellemuse.serverapp.serverdb.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.serverapp.jpa.events.ArtWorkEventListener;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseIdNameSerializer;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseResourceSerializer;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseSetPersonSerializer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "artwork")
@EntityListeners(ArtWorkEventListener.class)
@JsonInclude(Include.NON_NULL)

public class ArtWork extends MultiLanguageObject {

	@OneToOne(fetch = FetchType.LAZY, targetEntity = Site.class)
	@JoinColumn(name = "site_owner_id", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	private Site site;

	@ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinTable(name = "ArtWorkArtist", joinColumns = { @JoinColumn(name = "artwork_id") }, inverseJoinColumns = {
			@JoinColumn(name = "person_id") })
	@JsonSerialize(using = DelleMuseSetPersonSerializer.class)
	@JsonManagedReference
	@JsonBackReference
	@JsonProperty("artists")
	Set<Person> artists = new HashSet<>();

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ArtWorkType.class)
	@JoinColumn(name = "artworkType_id", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	private ArtWorkType artworkType;


	  
 
	
	@Column(name = "year")
	private int year;

	@OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
	@JoinColumn(name = "qrcode", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonProperty("qrcode")
	@JsonSerialize(using = DelleMuseResourceSerializer.class)
	private Resource qrcode;
	
	@Column(name = "url")
	private String url;

	
	public ArtWork() {
	}

	public void setUrl( String url ) {
		this.url=url;
	}
	
	public String geturl() {
		return this.url;
	}

	public ArtWorkType getArtworkType() {
		return artworkType;
	}

	public void setArtworkType(ArtWorkType artworkType) {
		this.artworkType = artworkType;
	}

	
 

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Set<Person> getArtists() {
		return artists;
	}

	public void setArtists(Set<Person> artists) {
		this.artists = artists;
	}
	


	public Resource getQRCode() {
		return this.qrcode;
	}

	public void setQRCode(Resource qrcode) {
		this.qrcode = qrcode;
	}
	


	
 

	
	
	
	public void setYear( int year ) {
		this.year=year;
	}
	
	public int getYear() {
		return this.year;
	}
	
	
};
