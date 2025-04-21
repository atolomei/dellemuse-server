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
@Table(name = "floor")
public class Floor extends DelleMuseObject {

    @Column(name="name")
    private String name;

    @Column(name="nameKey")
    private String nameKey;
    

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = FloorType.class)
    @JoinColumn(name = "floorType_id", nullable=true) 
    @JsonManagedReference
    @JsonBackReference
    @JsonIgnore
    private FloorType floorType;
    
   
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Site.class)
    @JoinColumn(name = "site_id", nullable=true) 
    @JsonManagedReference
    @JsonBackReference
    @JsonIgnore
    private Site site;

    
    @Column(name="title")
    private    String title;
    
    @Column(name="titleKey")
    private    String  titleKey;

    @Column(name="subtitle")
    private    String subtitle;
    
    @Column(name="subTitleKey")
    private    String subTitleKey;

    
    @Column(name="floornumber")
    private    String floorNumber;
    
    @Column(name="floornumberkey")
    private    String  floorNumberKey;

    
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
    
    public Floor() {
        
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

