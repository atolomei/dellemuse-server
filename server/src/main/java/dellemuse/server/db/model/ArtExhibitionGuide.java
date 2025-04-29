package dellemuse.server.db.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.model.ArtExhibitionGuideModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

@Entity
@Table(name = "artExhibitionGuide")
@JsonInclude(Include.NON_NULL)
public class ArtExhibitionGuide extends DelleMuseObject {

    @Column(name = "name")
    private String name;

    @Column(name = "nameKey")
    private String nameKey;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = ArtExhibition.class)
    @JoinColumn(name = "artExhibition_id", referencedColumnName="id", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonSerialize(using = DelleMuseIdSerializer.class)
    private ArtExhibition artExhibition;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Person.class)
    @JoinColumn(name = "publisher_id", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonProperty("publisher")
    @JsonSerialize(using = DelleMuseIdSerializer.class)
    private Person publisher;

    @OneToMany(fetch = FetchType.LAZY, targetEntity = GuideContent.class)
    @JoinColumn(name = "artExhibitionGuide_id", nullable = true, insertable=true)
    @JsonIgnore
    @OrderBy("lower(title) ASC")
    private List<GuideContent> contents;
    
    @Column(name = "title")
    private String title;

    @Column(name = "titleKey")
    private String titleKey;

    @Column(name = "subtitle")
    private String subtitle;

    @Column(name = "subTitleKey")
    private String subTitleKey;

    @Column(name = "info")
    private String info;

    @Column(name = "infoKey")
    private String infoKey;

    @OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
    @JoinColumn(name = "photo", nullable=true) 
    @JsonManagedReference
    @JsonBackReference
    @JsonProperty("photo")
    @JsonSerialize(using = DelleMuseIdSerializer.class)
    private Resource photo;
        
    @OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
    @JoinColumn(name = "video", nullable=true) 
    @JsonManagedReference
    @JsonBackReference
    @JsonProperty("video")
    @JsonSerialize(using = DelleMuseIdSerializer.class)
    private Resource video;

    @OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
    @JoinColumn(name = "audio", nullable=true) 
    @JsonManagedReference
    @JsonBackReference
    @JsonProperty("audio")
    @JsonSerialize(using = DelleMuseIdSerializer.class)
    private Resource audio;

    public ArtExhibitionGuide() {

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

    public ArtExhibition getArtExhibition() {
        return artExhibition;
    }

    public void setArtExhibition(ArtExhibition artExhibition) {
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
    
    @Override
    public ArtExhibitionGuideModel model() {
        try {
            return (ArtExhibitionGuideModel) getObjectMapper().readValue(getObjectMapper().writeValueAsString(this), ArtExhibitionGuideModel.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

};
