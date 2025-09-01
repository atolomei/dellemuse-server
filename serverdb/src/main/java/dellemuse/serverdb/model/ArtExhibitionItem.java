package dellemuse.serverdb.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.serverdb.model.serializer.DelleMuseIdNameSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "artExhibitionItem")
@JsonInclude(Include.NON_NULL)
public class ArtExhibitionItem extends DelleMuseObject {

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
	@JsonProperty("artwork")
	private ArtWork artwork;

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

	@Column(name = "artExhibitionOrder")
	@JsonProperty("artExhibitionOrder")
	private int artExhibitionOrder;

	@Column(name = "readcode")
	@JsonProperty("readcode")
	private String readCode;

	@Column(name = "qcode")
	@JsonProperty("qcode")
	private String qCode;

	@Column(name = "info")
	@JsonProperty("info")
	private String info;

	@Column(name = "infoKey")
	@JsonProperty("infoKey")
	private String infoKey;

	@Column(name = "mapurl")
	@JsonProperty("mapurl")
	private String mapurl;

	@Column(name = "website")
	@JsonProperty("website")
	private String wesite;

	public ArtExhibitionItem() {
	}
 
	
	public ArtWork getArtwork() {
		return artwork;
	}

	public void setArtwork(ArtWork artwork) {
		this.artwork = artwork;
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

	public String getqCode() {
		return qCode;
	}

	public void setqCode(String qCode) {
		this.qCode = qCode;
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

	public ArtExhibition getArtExhibition() {
		return artExhibition;
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
		return wesite;
	}

	public void setWesite(String wesite) {
		this.wesite = wesite;
	}

	public int getExhibitionOrder() {
		return artExhibitionOrder;
	}

	public void setExhibitionOrder(int exhibitionOrder) {
		this.artExhibitionOrder = exhibitionOrder;
	}

};
