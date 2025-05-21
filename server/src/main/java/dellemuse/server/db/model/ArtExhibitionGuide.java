package dellemuse.server.db.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.model.ArtExhibitionGuideModel;
import dellemuse.server.db.model.serializer.DelleMuseIdNameSerializer;
import dellemuse.server.db.model.serializer.DelleMuseListIdNameSerializer;
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

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = ArtExhibition.class)
    @JoinColumn(name = "artExhibition_id", referencedColumnName = "id", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonSerialize(using = DelleMuseIdNameSerializer.class)
    private ArtExhibition artExhibition;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Person.class)
    @JoinColumn(name = "publisher_id", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonProperty("publisher")
    @JsonSerialize(using = DelleMuseIdNameSerializer.class)
    private Person publisher;

    @OneToMany(fetch = FetchType.EAGER, targetEntity = GuideContent.class)
    @JoinColumn(name = "artExhibitionGuide_id", nullable = true, insertable = true)
    @JsonSerialize(using = DelleMuseListIdNameSerializer.class)
    @OrderBy("lower(title) ASC")
    private List<GuideContent> contents;

    @Column(name = "artExhibitionGuideOrder")
    private int artExhibitionGuideOrder;

    @Column(name = "official")
    private boolean official;

    @Column(name = "subtitle")
    private String subtitle;

    @Column(name = "subtitleKey")
    private String subTitleKey;

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
    @JsonSerialize(using = DelleMuseIdNameSerializer.class)
    private Resource video;

    @OneToOne(fetch = FetchType.EAGER, targetEntity = Resource.class)
    @JoinColumn(name = "audio", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonProperty("audio")
    @JsonSerialize(using = DelleMuseIdNameSerializer.class)
    private Resource audio;

    public ArtExhibitionGuide() {
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

    public List<GuideContent> getContents() {
        return contents;
    }

    public void setContents(List<GuideContent> contents) {
        this.contents = contents;
    }

    public boolean isOfficial() {
        return official;
    }

    public void setOfficial(boolean official) {
        this.official = official;
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

    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }

    public int getArtExhibitionGuideOrder() {
        return artExhibitionGuideOrder;
    }

    public void setArtExhibitionGuideOrder(int artExhibitionGuideOrder) {
        this.artExhibitionGuideOrder = artExhibitionGuideOrder;
    }

    @Override
    public ArtExhibitionGuideModel model() {
        try {
            return (ArtExhibitionGuideModel) getObjectMapper().readValue(getObjectMapper().writeValueAsString(this),
                    ArtExhibitionGuideModel.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
};
