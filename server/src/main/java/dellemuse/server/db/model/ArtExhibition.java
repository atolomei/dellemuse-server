package dellemuse.server.db.model;

import java.time.OffsetDateTime;

import javax.persistence.CascadeType;

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
@Table(name = "artExhibition")
public class ArtExhibition extends DelleMuseObject {

    @Column(name="name")
    private String name;

    @Column(name="nameKey")
    private String nameKey;
    
    @ManyToOne(fetch = FetchType.LAZY, cascade=jakarta.persistence.CascadeType.DETACH, targetEntity = ArtExhibitionStatusType.class)
    @JoinColumn(name = "artExhibitionStatusType_id", nullable=true) 
    @JsonManagedReference
    @JsonBackReference
    @JsonIgnore
    private ArtExhibitionStatusType artExhibitionStatusType;
    

    @OneToOne(fetch = FetchType.LAZY, targetEntity = Site.class)
    @JoinColumn(name = "site_id", nullable=true) 
    @JsonManagedReference
    @JsonBackReference
    @JsonIgnore
    private Site site;

    @Column(name="permanent")
    private    boolean permanent;
    
    @Column(name="fromDate")
    private OffsetDateTime fromDate;
    
    @Column(name="toDate")
    private OffsetDateTime toDate;
    
    @Column(name="title")
    private    String title;
    
    @Column(name="titleKey")
    private    String  titleKey;

    @Column(name="subtitle")
    private    String subtitle;
    
    @Column(name="subTitleKey")
    private    String subTitleKey;

    @Column(name="info")
    private    String info;
    
    @Column(name="infoKey")
    private    String infoKey; 

    @Column(name="address")
    private    String address;
    
    @Column(name="addressKey")
    private    String  addressKey;

    @Column(name="moreinfo")
    private    String moreinfo;
    
    @Column(name="moreinfoKey")
    private    String  moreinfoKey;

    //@Column(name="created")
    //photo               bytea,
    
    @Column(name="photoKey")
    String  photoKey;
    
    //@Column(name="created")
    //video               bytea,
    
    @Column(name="videoKey")
    String videoKey;
    
    //@Column(name="created")
    //audio               bytea,
    
    @Column(name="audioKey")
    String  audioKey;
    
    public ArtExhibition() {
        
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressKey() {
        return addressKey;
    }

    public void setAddressKey(String addressKey) {
        this.addressKey = addressKey;
    }

    public String getMoreinfo() {
        return moreinfo;
    }

    public void setMoreinfo(String moreinfo) {
        this.moreinfo = moreinfo;
    }

    public String getMoreinfoKey() {
        return moreinfoKey;
    }

    public void setMoreinfoKey(String moreinfoKey) {
        this.moreinfoKey = moreinfoKey;
    }

    public String getPhotoKey() {
        return photoKey;
    }

    public void setPhotoKey(String photoKey) {
        this.photoKey = photoKey;
    }

    public String getVideoKey() {
        return videoKey;
    }

    public void setVideoKey(String videoKey) {
        this.videoKey = videoKey;
    }

    public String getAudioKey() {
        return audioKey;
    }

    public void setAudioKey(String audioKey) {
        this.audioKey = audioKey;
    }
    
    
    
};    

