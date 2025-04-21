package dellemuse.server.db.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "room")
public class Room extends DelleMuseObject {

    @Column(name="name")
    private String name;

    @Column(name="nameKey")
    private String nameKey;
    
    
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = RoomType.class)
    @JoinColumn(name = "roomType_id", nullable=true) 
    @JsonManagedReference
    @JsonBackReference
    @JsonIgnore
    private RoomType roomType;
    

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Floor.class)
    @JoinColumn(name = "floor_id", nullable=true) 
    @JsonManagedReference
    @JsonBackReference
    @JsonIgnore
    private Floor floor;
    
    @Column(name="title")
    private    String title;
    
    @Column(name="titleKey")
    private    String  titleKey;

    @Column(name="subtitle")
    private    String subtitle;
    
    @Column(name="subTitleKey")
    private    String subTitleKey;

    
    @Column(name="roomnumber")
    private    String roomNumber;
    
    @Column(name="roomnumberkey")
    private    String  roomNumberKey;

    
    @Column(name="info")
    private    String info;
    
    @Column(name="infoKey")
    private    String infoKey; 

    @OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
    @JoinColumn(name = "photo", nullable=true) 
    @JsonManagedReference
    @JsonBackReference
    @JsonIgnore
    private Resource photo;
        
    @OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
    @JoinColumn(name = "video", nullable=true) 
    @JsonManagedReference
    @JsonBackReference
    @JsonIgnore
    private Resource video;

    @OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
    @JoinColumn(name = "audio", nullable=true) 
    @JsonManagedReference
    @JsonBackReference
    @JsonIgnore
    private Resource audio;
    

    public Room() {
        
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameKey() {
        return nameKey;
    }

    public void setNameKey(String nameKey) {
        this.nameKey = nameKey;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleKey() {
        return titleKey;
    }

    public void setTitleKey(String titleKey) {
        this.titleKey = titleKey;
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

