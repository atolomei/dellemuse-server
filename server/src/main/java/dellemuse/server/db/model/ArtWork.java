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
@Table(name = "artwork")
public class ArtWork extends DelleMuseObject {

    @Column(name="name")
    private String name;

    @Column(name="nameKey")
    private String nameKey;
    
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = ArtWorkType.class)
    @JoinColumn(name = "artworkType_id", nullable=true) 
    @JsonManagedReference
    @JsonBackReference
    @JsonIgnore
    private ArtWorkType artworkType;
    
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

    public ArtWork () {
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

    public ArtWorkType getArtworkType() {
        return artworkType;
    }

    public void setArtworkType(ArtWorkType artworkType) {
        this.artworkType = artworkType;
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
    
};    

