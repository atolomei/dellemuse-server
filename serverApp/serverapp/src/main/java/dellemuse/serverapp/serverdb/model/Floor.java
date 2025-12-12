package dellemuse.serverapp.serverdb.model;

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
@Table(name = "floor")
@JsonInclude(Include.NON_NULL)
public class Floor extends DelleMuseObject {

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = FloorType.class)
	@JoinColumn(name = "floorType_id", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	@JsonProperty("floorType")
	private FloorType floorType;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Site.class)
	@JoinColumn(name = "site_id", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	@JsonProperty("site")
	private Site site;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, targetEntity = Room.class)
	@JoinColumn(name = "floor_id", nullable = true, insertable = false)
	@JsonSerialize(using = DelleMuseListIdNameSerializer.class)
	@OrderBy("roomNumber ASC")
	@JsonManagedReference
	@JsonBackReference
	@JsonProperty("rooms")
	private List<Room> rooms;

	@Column(name = "subtitle")
	private String subtitle;

	@Column(name = "subtitleKey")
	private String subTitleKey;

	@Column(name = "floornumber")
	private String floorNumber;

	@Column(name = "floornumberkey")
	private String floorNumberKey;

	@Column(name = "info")
	private String info;

	@Column(name = "infoKey")
	private String infoKey;

	@OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
	@JoinColumn(name = "photo", nullable = true)
	@JsonBackReference
	@JsonProperty("photo")
	@JsonSerialize(using = DelleMuseResourceSerializer.class)
	private Resource photo;

	@OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
	@JoinColumn(name = "video", nullable = true)
	@JsonBackReference
	@JsonProperty("video")
	@JsonSerialize(using = DelleMuseResourceSerializer.class)
	private Resource video;

	@OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
	@JoinColumn(name = "audio", nullable = true)
	@JsonBackReference
	@JsonProperty("audio")
	@JsonSerialize(using = DelleMuseResourceSerializer.class)
	private Resource audio;

	@OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
	@JoinColumn(name = "map", nullable = true)
	@JsonBackReference
	@JsonProperty("map")
	@JsonSerialize(using = DelleMuseResourceSerializer.class)
	private Resource map;

	@Column(name = "language")
	private String language;
	
	public String getLanguage() {
		return this.language;
	}

	public void setLanguage(String lang) {
		language = lang;
	}

	public Floor() {
	}

	public FloorType getFloorType() {
		return floorType;
	}

	public void setFloorType(FloorType floorType) {
		this.floorType = floorType;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
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

	public String getFloorNumber() {
		return floorNumber;
	}

	public void setFloorNumber(String floorNumber) {
		this.floorNumber = floorNumber;
	}

	public String getFloorNumberKey() {
		return floorNumberKey;
	}

	public void setFloorNumberKey(String floorNumberKey) {
		this.floorNumberKey = floorNumberKey;
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

};
