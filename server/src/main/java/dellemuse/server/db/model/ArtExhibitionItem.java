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
@Table(name = "artExhibitionItem")
public class ArtExhibitionItem extends DelleMuseObject {

    @Column(name="name")
    private String name;

    @Column(name="nameKey")
    private String nameKey;
    
    
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = ArtWork.class)
    @JoinColumn(name = "artwork_id", nullable=true) 
    @JsonManagedReference
    @JsonBackReference
    @JsonIgnore
    private ArtWork artwork;

    
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Site.class)
    @JoinColumn(name = "site_id", nullable=true) 
    @JsonManagedReference
    @JsonBackReference
    @JsonIgnore
    private Site site;
    
    
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Floor.class)
    @JoinColumn(name = "floor_id", nullable=true) 
    @JsonManagedReference
    @JsonBackReference
    @JsonIgnore
    private Floor floor;


    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Room.class)
    @JoinColumn(name = "room_id", nullable=true) 
    @JsonManagedReference
    @JsonBackReference
    @JsonIgnore
    private Room room;

    @Column(name="ordinal")
    private int ordinal;
    
    @Column(name="readcode")
    private String readCode;
    
    @Column(name="qcode")
    private String qCode;
    
    @Column(name="title")
    private String title;
    
    @Column(name="titleKey")
    private String  titleKey;
    
    
    @Column(name="info")
    private String info;
    
    @Column(name="infoKey")
    private String infoKey; 
    

    public ArtExhibitionItem() {
        
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


    public ArtWork getArtwork() {
        return artwork;
    }


    public void setArtwork(ArtWork artwork) {
        this.artwork = artwork;
    }


    public Site getSite() {
        return site;
    }


    public void setSite(Site site) {
        this.site = site;
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


    public int getOrdinal() {
        return ordinal;
    }


    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
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

