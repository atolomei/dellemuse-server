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
@Table(name = "guideContent")
@JsonInclude(Include.NON_NULL)
public class GuideContent extends MultiLanguageObject {

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = ArtExhibitionGuide.class)
    @JoinColumn(name = "artExhibitionGuide_id", nullable = false, insertable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonSerialize(using = DelleMuseIdNameSerializer.class)
    @JsonProperty("artExhibitionGuide")
    private ArtExhibitionGuide artExhibitionGuide;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = ArtExhibitionItem.class)
    @JoinColumn(name = "artExhibitionItem_id", nullable = true, insertable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonSerialize(using = DelleMuseIdNameSerializer.class)
    @JsonProperty("artExhibitionItem")
    private ArtExhibitionItem artExhibitionItem;

    @Column(name = "guideOrder")
    private int guideOrder;
 

    @Column(name = "info")
    private String info;

    @Column(name = "infoKey")
    private String infoKey;

    @OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
    @JoinColumn(name = "photo", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonProperty("photo")
    @JsonSerialize(using = DelleMuseResourceSerializer.class)
    private Resource photo;

    @OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
    @JoinColumn(name = "video", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonProperty("video")
    @JsonSerialize(using = DelleMuseResourceSerializer.class)
    private Resource video;

    @Column(name = "videoKey")
    private String videoKey;
    
    
    @OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
    @JoinColumn(name = "audio", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonProperty("audio")
    @JsonSerialize(using = DelleMuseResourceSerializer.class)
    private Resource audio;

    @Column(name = "audioKey")
    private String audioKey;
    
    
    
    
    public GuideContent() {
    }

    public ArtExhibitionGuide getArtExhibitionGuide() {
        return artExhibitionGuide;
    }

    public void setArtExhibitionGuide(ArtExhibitionGuide artExhibitionGuide) {
        this.artExhibitionGuide = artExhibitionGuide;
    }

    public ArtExhibitionItem getArtExhibitionItem() {
        return artExhibitionItem;
    }

    public void setArtExhibitionItem(ArtExhibitionItem artExhibitionItem) {
        this.artExhibitionItem = artExhibitionItem;
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

    public String getAudioKey() {
        return audioKey;
    }

    public void setAudioKey(String infoKey) {
        this.audioKey = infoKey;
    }
    
    
    public int getGuideOrder() {
        return guideOrder;
    }

    public void setGuideOrder(int guideOrder) {
        this.guideOrder = guideOrder;
    }

    public Resource getPhoto() {
        return photo;
    }

    public void setPhoto(Resource photo) {
        this.photo = photo;
    }

    public Resource getVideo() {
        return video;
    }

    public void setVideo(Resource video) {
        this.video = video;
    }

    public Resource getAudio() {
        return audio;
    }

    public void setAudio(Resource audio) {
        this.audio = audio;
    }
    
};
