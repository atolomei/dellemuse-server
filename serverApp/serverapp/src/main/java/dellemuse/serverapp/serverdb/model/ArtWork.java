package dellemuse.serverapp.serverdb.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.serverapp.serverdb.model.serializer.DelleMuseIdNameSerializer;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseResourceSerializer;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseSetPersonSerializer;
import jakarta.persistence.CascadeType;
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
@Table(name = "artwork")
@JsonInclude(Include.NON_NULL)
public class ArtWork extends DelleMuseObject {

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

	@Column(name = "subtitle")
	private String subtitle;

	@Column(name = "spec")
	private String spec;

	@Column(name = "subtitleKey")
	private String subTitleKey;

	@Column(name = "info")
	private String info;

	@Column(name = "infoKey")
	private String infoKey;

	@Column(name = "intro")
	private String intro;

	@Column(name = "introKey")
	private String introKey;

	/**
	 * by default it is true, sometimes the thumbnail generated is not correct, for
	 * those images we dont use thumbnail
	 */
	@Column(name = "usethumbnail")
	@JsonProperty("usethumbnail")
	private boolean usethumbnail;

	@OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
	@JoinColumn(name = "photo", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonProperty("photo")
	@JsonSerialize(using = DelleMuseResourceSerializer.class)
	private Resource photo;

	@OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
	@JoinColumn(name = "video", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonProperty("video")
	@JsonSerialize(using = DelleMuseResourceSerializer.class)
	private Resource video;

	@OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
	@JoinColumn(name = "audio", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonProperty("audio")
	@JsonSerialize(using = DelleMuseResourceSerializer.class)
	private Resource audio;

	public ArtWork() {
	}

	public ArtWorkType getArtworkType() {
		return artworkType;
	}

	public void setArtworkType(ArtWorkType artworkType) {
		this.artworkType = artworkType;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getSubTitleKey() {
		return subTitleKey;
	}

	public void setSubTitleKey(String subTitleKey) {
		this.subTitleKey = subTitleKey;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getInfoKey() {
		return infoKey;
	}

	public void setInfoKey(String infoKey) {
		this.infoKey = infoKey;
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

	public Resource getPhoto() {
		return photo;
	}

	public void setPhoto(Resource photo) {
		this.photo = photo;
	}

	public Resource getVideo() {
		return video;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public void setVideo(Resource video) {
		this.video = video;
	}

	public Resource getAudio() {
		return audio;
	}

	public void setAudio(Resource audio) {
		this.audio = audio;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getIntroKey() {
		return introKey;
	}

	public void setIntroKey(String introKey) {
		this.introKey = introKey;
	}

	public boolean isUsethumbnail() {
		return usethumbnail;
	}

	public void setUsethumbnail(boolean usethumbnail) {
		this.usethumbnail = usethumbnail;
	}
};
