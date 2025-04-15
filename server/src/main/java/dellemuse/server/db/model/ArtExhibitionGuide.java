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
@Table(name = "artExhibitionGuide")
public class ArtExhibitionGuide extends DelleMuseObject {

    @Column(name="name")
    private String name;

    @Column(name="nameKey")
    private String nameKey;
    
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = ArtExhibition.class)
    @JoinColumn(name = "artExhibition_id", nullable=true) 
    @JsonManagedReference
    @JsonBackReference
    @JsonIgnore
    private ArtExhibitionStatusType artExhibition;
    

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Person.class)
    @JoinColumn(name = "publisher_id", nullable=true) 
    @JsonManagedReference
    @JsonBackReference
    @JsonIgnore
    private Person publisher;

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
    
    
    public      ArtExhibitionGuide () {
        
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


    public ArtExhibitionStatusType getArtExhibition() {
        return artExhibition;
    }


    public void setArtExhibition(ArtExhibitionStatusType artExhibition) {
        this.artExhibition = artExhibition;
    }


    public Person getPublisher() {
        return publisher;
    }


    public void setPublisher(Person publisher) {
        this.publisher = publisher;
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

