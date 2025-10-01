package dellemuse.serverapp.serverdb.model;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.serverapp.serverdb.model.serializer.DelleMuseIdNameSerializer;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseListIdNameSerializer;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseResourceSerializer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

@Entity
@Table(name = "artExhibition")
@JsonInclude(Include.NON_NULL)
public class ArtExhibition extends DelleMuseObject {

	@OneToOne(fetch = FetchType.LAZY, targetEntity = Site.class)
	@JoinColumn(name = "site_id", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	private Site site;

	@ManyToOne(fetch = FetchType.EAGER, cascade = jakarta.persistence.CascadeType.DETACH, targetEntity = ArtExhibitionStatusType.class)
	@JoinColumn(name = "artExhibitionStatusType_id", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	private ArtExhibitionStatusType artExhibitionStatusType;

	@Column(name = "permanent")
	private boolean permanent;

	@Column(name = "opens")
	private String opens;

	@Column(name = "opensKey")
	private String opensKey;

	@Column(name = "fromDate")
	private OffsetDateTime fromDate;

	@Column(name = "toDate")
	private OffsetDateTime toDate;

	@Column(name = "subtitle")
	private String subtitle;

	@Column(name = "subtitleKey")
	private String subTitleKey;

	// descripcion
	//
	@Column(name = "info")
	private String info;

	@Column(name = "infoKey")
	private String infoKey;

	// en la lista de resultados
	//
	@Column(name = "intro")
	private String intro;

	
	@Column(name = "spec")
	private String spec;

	@Column(name = "shortname")
	private String shortname;

	
	@Column(name = "shortnameKey")
	private String shortnamekey;
	
	@Column(name = "introKey")
	private String introKey;

	@Column(name = "location")
	private String location;
	
	@Column(name = "mapurl")
	private String map;
	

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, targetEntity = ArtExhibitionItem.class)
	@JoinColumn(name = "artExhibition_id", nullable = true, insertable = true)
	@JsonSerialize(using = DelleMuseListIdNameSerializer.class)
	@OrderBy("lower(name) ASC")
	@JsonProperty("artExhibitionItems")
	private List<ArtExhibitionItem> artExhibitionItems;

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

	@Column(name = "website")
	private String website;

	public ArtExhibition() {
	}

	public ArtExhibitionStatusType getArtExhibitionStatusType() {
		return artExhibitionStatusType;
	}

	public void setArtExhibitionStatusType(ArtExhibitionStatusType artExhibitionStatusType) {
		this.artExhibitionStatusType = artExhibitionStatusType;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public boolean isPermanent() {
		return permanent;
	}

	public void setPermanent(boolean permanent) {
		this.permanent = permanent;
	}

	public OffsetDateTime getFromDate() {
		return fromDate;
	}

	public void setFromDate(OffsetDateTime fromDate) {
		this.fromDate = fromDate;
	}

	public OffsetDateTime getToDate() {
		return toDate;
	}

	public void setToDate(OffsetDateTime toDate) {
		this.toDate = toDate;
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

	public List<ArtExhibitionItem> getArtExhibitionItems() {
		return artExhibitionItems;
	}

	public void setArtExhibitionItems(List<ArtExhibitionItem> artExhibitionItems) {
		this.artExhibitionItems = artExhibitionItems;
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

	public void setVideo(Resource video) {
		this.video = video;
	}

	public Resource getAudio() {
		return audio;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setAudio(Resource audio) {
		this.audio = audio;
	}

	public String getOpens() {
		return opens;
	}

	public void setOpens(String opens) {
		this.opens = opens;
	}

	public String getOpensKey() {
		return opensKey;
	}

	public void setOpensKey(String opensKey) {
		this.opensKey = opensKey;
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

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getMap() {
		return map;
	}

	public void setMap(String map) {
		this.map = map;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getShortname() {
		return shortname;
	}

	public String getShortnamekey() {
		return shortnamekey;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public void setShortnamekey(String shortnamekey) {
		this.shortnamekey = shortnamekey;
	}

};
