package dellemuse.serverapp.serverdb.model;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
 
**/

// import com.fasterxml.jackson.databind.annotation.JsonSerialize;



import dellemuse.serverapp.icons.Icons;
import dellemuse.serverapp.jpa.events.ArtExhibitionEventListener;
import dellemuse.serverapp.page.PrefixUrl;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseIdNameSerializer;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseListIdNameSerializer;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseResourceSerializer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;



import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@Entity
@Table(name = "artExhibition")
//@JsonInclude(Include.NON_NULL)
@EntityListeners(ArtExhibitionEventListener.class)
public class ArtExhibition extends MultiLanguageObject {

	public static String getIcon() {
		return Icons.ArtExhibition;
	}

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

	@Column(name = "fromDate")
	private OffsetDateTime fromDate;

	@Column(name = "toDate")
	private OffsetDateTime toDate;

	@Column(name = "shortname")
	private String shortname;

	@Column(name = "shortnameKey")
	private String shortnamekey;

	@Column(name = "location")
	private String location;

	@Column(name = "mapurl")
	private String map;

	@Column(name = "audio_id")
	private Long audioId;

	@Column(name = "qrcodetext")
	private String qrCodeText;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, targetEntity = ArtExhibitionItem.class)
	@JoinColumn(name = "artExhibition_id", nullable = true, insertable = true)
	@JsonSerialize(using = DelleMuseListIdNameSerializer.class)
	@OrderBy("lower(name) ASC")
	@JsonProperty("artExhibitionItems")
	private List<ArtExhibitionItem> artExhibitionItems;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, targetEntity = ArtExhibitionSection.class)
	@JoinColumn(name = "artExhibition_id", nullable = true, insertable = true)
	@JsonSerialize(using = DelleMuseListIdNameSerializer.class)
	@OrderBy("lower(name) ASC")
	@JsonProperty("artExhibitionSections")
	private List<ArtExhibitionSection> artExhibitionSections;

	@OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
	@JoinColumn(name = "qrcodepdf", nullable = true)
	@JsonManagedReference
	@JsonProperty("qrcodepdf")
	@JsonSerialize(using = DelleMuseResourceSerializer.class)
	private Resource QRCodePdf;

	@OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
	@JoinColumn(name = "audioNumberPng", nullable = true)
	@JsonManagedReference
	@JsonProperty("audioNumberPng")
	@JsonSerialize(using = DelleMuseResourceSerializer.class)
	private Resource audioNumberPng;

	@Column(name = "website")
	private String website;

	@Column(name = "ordinal")
	private int ordinal;


	@OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
	@JoinColumn(name = "qrcode", nullable = true)
	@JsonManagedReference
	@JsonProperty("qrcode")
	@JsonSerialize(using = DelleMuseResourceSerializer.class)
	private Resource qrcode;
	
	
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Floor.class)
	@JoinColumn(name = "floor_id", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	@JsonProperty("floor")
	private Floor floor;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Room.class)
	@JoinColumn(name = "room_id", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	@JsonProperty("room")
	private Room room;
	
	
	/** Normalized pin position on the Floor map (0.0=left/top, 1.0=right/bottom) */
	@Column(name = "map_floor_pos_x", nullable = true)
	@JsonProperty("mapFloorPosX")
	private Double mapFloorPosX;

	@Column(name = "map_floor_pos_y", nullable = true)
	@JsonProperty("mapFloorPosY")
	private Double mapFloorPosY;

	/** ID of the Floor whose map this pin belongs to; used to detect stale pins */
	@Column(name = "map_pos_floor_id", nullable = true)
	@JsonProperty("mapPosFloorId")
	private Long mapPosFloorId;
	
	
	
	/** Normalized pin position on the Room map (0.0=left/top, 1.0=right/bottom) */
	@Column(name = "map_pos_x", nullable = true)
	@JsonProperty("mapPosX")
	private Double mapPosX;

	@Column(name = "map_pos_y", nullable = true)
	@JsonProperty("mapPosY")
	private Double mapPosY;

	/** ID of the Room whose map this pin belongs to; used to detect stale pins */
	@Column(name = "map_pos_room_id", nullable = true)
	@JsonProperty("mapPosRoomId")
	private Long mapPosRoomId;

	
	
	
	
	
	
	public Resource getAudioNumberPng() {
		return audioNumberPng;
	}

	public void setAudioNumberPng(Resource audioNuumberPng) {
		this.audioNumberPng = audioNuumberPng;
	}


	public ArtExhibition() {
	}

	public final String getPrefixUrl() {
		return PrefixUrl.ArtExhibition;
	}

	public ArtExhibitionStatusType getArtExhibitionStatusType() {
		return artExhibitionStatusType;
	}

	public void setArtExhibitionStatusType(ArtExhibitionStatusType artExhibitionStatusType) {
		this.artExhibitionStatusType = artExhibitionStatusType;
	}

	@Override
	public boolean isSiteSecured() {
		return true;
	}

	@Override
	public String getObjectClassName() {
		return ArtExhibition.class.getSimpleName();
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public Long getAudioId() {
		return audioId;
	}

	public void setAudioId(Long audioId) {
		this.audioId = audioId;
	}

	public Floor getFloor() {
		return floor;
	}

	public Room getRoom() {
		return room;
	}

	public Double getMapFloorPosX() {
		return mapFloorPosX;
	}

	public Double getMapFloorPosY() {
		return mapFloorPosY;
	}

	public Long getMapPosFloorId() {
		return mapPosFloorId;
	}

	public Double getMapPosX() {
		return mapPosX;
	}

	public Double getMapPosY() {
		return mapPosY;
	}

	public Long getMapPosRoomId() {
		return mapPosRoomId;
	}

	public void setFloor(Floor floor) {
		this.floor = floor;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public void setMapFloorPosX(Double mapFloorPosX) {
		this.mapFloorPosX = mapFloorPosX;
	}

	public void setMapFloorPosY(Double mapFloorPosY) {
		this.mapFloorPosY = mapFloorPosY;
	}

	public void setMapPosFloorId(Long mapPosFloorId) {
		this.mapPosFloorId = mapPosFloorId;
	}

	public void setMapPosX(Double mapPosX) {
		this.mapPosX = mapPosX;
	}

	public void setMapPosY(Double mapPosY) {
		this.mapPosY = mapPosY;
	}

	public void setMapPosRoomId(Long mapPosRoomId) {
		this.mapPosRoomId = mapPosRoomId;
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

	public List<ArtExhibitionItem> getArtExhibitionItems() {
		return artExhibitionItems;
	}

	public void setArtExhibitionItems(List<ArtExhibitionItem> artExhibitionItems) {
		this.artExhibitionItems = artExhibitionItems;
	}

	public List<ArtExhibitionSection> getArtExhibitionSections() {
		return artExhibitionSections;
	}

	public void setArtExhibitionSections(List<ArtExhibitionSection> artExhibitionSec) {
		this.artExhibitionSections = artExhibitionSec;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getWebsite() {
		return website;
	}

	public String getQrCodeText() {
		return qrCodeText;
	}

	public void setQrCodeText(String qrCodeText) {
		this.qrCodeText = qrCodeText;
	}

	public Resource getQRCodePdf() {
		return QRCodePdf;
	}

	public void setQRCodePdf(Resource qRCodePdf) {
		QRCodePdf = qRCodePdf;
	}

	public Resource getQrcode() {
		return qrcode;
	}

	public void setQrcode(Resource qrcode) {
		this.qrcode = qrcode;
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

	public boolean isComing() {

		if (this.fromDate == null && this.toDate == null)
			return false;

		if (this.fromDate == null)
			return false;

		OffsetDateTime now = OffsetDateTime.now();

		if (this.toDate == null)
			return this.fromDate.isAfter(now);

		return this.fromDate.isAfter(now) && this.toDate.isAfter(now);
	}

	/**
	 * @return
	 */
	public boolean isOpen() {

		if (this.toDate == null)
			return true;

		OffsetDateTime now = OffsetDateTime.now();

		if (this.fromDate == null)
			return this.toDate.isBefore(now);

		return this.fromDate.isBefore(now) && this.toDate.isAfter(now);
	}

	/**
	 * @return
	 */
	public boolean isTerminated() {

		if (toDate == null)
			return false;

		OffsetDateTime now = OffsetDateTime.now();

		if (fromDate == null)
			return toDate.isBefore(now);

		return toDate.isBefore(now);
	}

	@Override
	public boolean equals(Object o) {

		if (o == null)
			return false;

		if (this == o)
			return true;

		if (!(o instanceof ArtExhibition))
			return false;

		if (this.getId() == null)
			return false;

		if ((o instanceof ArtExhibition)) {

			if (((ArtExhibition) o).getId() == null)
				return false;

			return ((ArtExhibition) o).getId().equals(getId());
		}

		return false;
	}

	public int getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}
};
