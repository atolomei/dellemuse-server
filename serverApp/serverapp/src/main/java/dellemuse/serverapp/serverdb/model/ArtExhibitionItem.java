package dellemuse.serverapp.serverdb.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.serverapp.page.PrefixUrl;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseIdNameSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "artExhibitionItem")
@JsonInclude(Include.NON_NULL)
public class ArtExhibitionItem extends MultiLanguageObject {

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ArtExhibition.class)
	@JoinColumn(name = "artExhibition_id", referencedColumnName = "id", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	@JsonProperty("artExhibition")
	private ArtExhibition artExhibition;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ArtWork.class)
	@JoinColumn(name = "artwork_id", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	@JsonProperty("artWork")
	private ArtWork artWork;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Floor.class)
	@JoinColumn(name = "floor_id", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	@JsonProperty("floor")
	private Floor floor;

	@Column(name = "floorStr")
	@JsonProperty("floorStr")
	private String floorStr;

	@Column(name = "roomStr")
	@JsonProperty("roomStr")
	private String roomStr;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Room.class)
	@JoinColumn(name = "room_id", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	@JsonProperty("room")
	private Room room;

	@Column(name = "artExhibitionOrder")
	@JsonProperty("artExhibitionOrder")
	private int artExhibitionOrder;

	@Column(name = "readcode")
	@JsonProperty("readcode")
	private String readCode;

	@Column(name = "qcode")
	@JsonProperty("QRCode")
	private String qRCode;

	@Column(name = "mapurl")
	@JsonProperty("mapurl")
	private String mapurl;

	@Column(name = "website")
	@JsonProperty("website")
	private String website;
	
	@Column(name = "audio_id")
	private Long audioId;


	public ArtExhibitionItem() {
	}

	@Override
	public String getObjectClassName() {
		return ArtExhibitionItem.class.getSimpleName();
	}

	public String getPrefixUrl() {
		return PrefixUrl.ArtExhibitionItem;
	}

	 

	public void setArtWork(ArtWork artwork) {
		this.artWork = artwork;
	}

	public Floor getFloor() {
		return floor;
	}

	public void setFloor(Floor floor) {
		this.floor = floor;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public String getReadCode() {
		return readCode;
	}

	public void setReadCode(String readCode) {
		this.readCode = readCode;
	}

	public String getQRCode() {
		return qRCode;
	}

	public void setQRCode(String qCode) {
		this.qRCode = qCode;
	}

	public Long getAudioId() {
		return audioId;
	}

	public void setAudioId(Long audioId) {
		this.audioId = audioId;
	}

	public ArtExhibition getArtExhibition() {
		return artExhibition;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public void setArtExhibition(ArtExhibition artExhibition) {
		this.artExhibition = artExhibition;
	}

	public String getMapurl() {
		return mapurl;
	}

	public void setMapurl(String mapurl) {
		this.mapurl = mapurl;
	}

	public String getWesite() {
		return website;
	}

	public void setWesite(String wesite) {
		this.website = wesite;
	}

	public int getExhibitionOrder() {
		return artExhibitionOrder;
	}

	public void setExhibitionOrder(int exhibitionOrder) {
		this.artExhibitionOrder = exhibitionOrder;
	}

	public ArtWork getArtWork() {
		return artWork;
	}

	public String getFloorStr() {
		return floorStr;
	}

	public String getRoomStr() {
		return roomStr;
	}

	public int getArtExhibitionOrder() {
		return artExhibitionOrder;
	}

	public String getqRCode() {
		return qRCode;
	}

	public void setArtwork(ArtWork artwork) {
		this.artWork = artwork;
	}

	public void setFloorStr(String floorStr) {
		this.floorStr = floorStr;
	}

	public void setRoomStr(String roomStr) {
		this.roomStr = roomStr;
	}

	public void setArtExhibitionOrder(int artExhibitionOrder) {
		this.artExhibitionOrder = artExhibitionOrder;
	}

	public void setqRCode(String qRCode) {
		this.qRCode = qRCode;
	}

};
