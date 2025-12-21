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
import dellemuse.serverapp.page.PrefixUrl;
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

//@Entity
//@Table(name = "music")
//@EntityListeners(ArtWorkEventListener.class)
//@JsonInclude(Include.NON_NULL)
public class MusicWork extends MultiLanguageObject {

	@ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinTable(name = "musicComposer", joinColumns = { @JoinColumn(name = "music_id") }, inverseJoinColumns = {
			@JoinColumn(name = "person_id") })
	@JsonSerialize(using = DelleMuseSetPersonSerializer.class)
	@JsonManagedReference
	@JsonBackReference
	@JsonProperty("composers")
	Set<Person> composers = new HashSet<>();

	
	
	/**
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ArtWorkType.class)
	@JoinColumn(name = "artworkType_id", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	private ArtWorkType artworkType;
	 **/
	
	@Column(name = "year")
	private int year;

	@Column(name = "url")
	private String url;
	
	
	public MusicWork() {
	}

	@Override
	public String getObjectClassName() {
		return MusicWork.class.getSimpleName();
	}
	
	public String getPrefixUrl() {
		return PrefixUrl.music;
	}
	
	
	public void setUrl( String url ) {
		this.url=url;
	}
	
	public String geturl() {
		return this.url;
	}
  

	public Set<Person> getComposers() {
		return composers;
	}
 
	
	public void setYear( int year ) {
		this.year=year;
	}
	
	public int getYear() {
		return this.year;
	}
	
	
};
