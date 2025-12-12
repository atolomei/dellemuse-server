package dellemuse.serverapp.serverdb.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.serverapp.serverdb.model.serializer.DelleMuseIdNameSerializer;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseResourceSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "room")
@JsonInclude(Include.NON_NULL)
public class Room extends DelleMuseObject {

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = RoomType.class)
    @JoinColumn(name = "roomType_id", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonSerialize(using = DelleMuseIdNameSerializer.class)
    private RoomType roomType;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Floor.class)
    @JoinColumn(name = "floor_id", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonSerialize(using = DelleMuseIdNameSerializer.class)
    private Floor floor;

    @Column(name = "subtitle")
    private String subtitle;

    @Column(name = "subtitleKey")
    private String subTitleKey;

    @Column(name = "roomnumber")
    private String roomNumber;

    @Column(name = "roomnumberkey")
    private String roomNumberKey;

    @Column(name = "info")
    private String info;

    @Column(name = "infoKey")
    private String infoKey;

    @OneToOne(fetch = FetchType.EAGER, targetEntity = Resource.class)
    @JoinColumn(name = "photo", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonProperty("photo")
    @JsonSerialize(using = DelleMuseIdNameSerializer.class)
    private Resource photo;

    @OneToOne(fetch = FetchType.EAGER, targetEntity = Resource.class)
    @JoinColumn(name = "video", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonProperty("video")
    @JsonSerialize(using = DelleMuseResourceSerializer.class)
    private Resource video;

    @OneToOne(fetch = FetchType.EAGER, targetEntity = Resource.class)
    @JoinColumn(name = "audio", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonProperty("audio")    
    @JsonSerialize(using = DelleMuseResourceSerializer.class)
    private Resource audio;
    
    @OneToOne(fetch = FetchType.EAGER, targetEntity = Resource.class)
    @JoinColumn(name = "map", nullable = true)
    @JsonManagedReference
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
	
    public Room() {
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
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

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomNumberKey() {
        return roomNumberKey;
    }

    public void setRoomNumberKey(String roomNumberKey) {
        this.roomNumberKey = roomNumberKey;
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
