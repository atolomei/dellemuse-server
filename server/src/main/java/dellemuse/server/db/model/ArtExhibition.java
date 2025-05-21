package dellemuse.server.db.model;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.model.ArtExhibitionModel;
import dellemuse.server.db.model.serializer.DelleMuseIdNameSerializer;
import dellemuse.server.db.model.serializer.DelleMuseListIdNameSerializer;
import dellemuse.server.error.InternalErrorException;
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
@Table(name = "artExhibition")
@JsonInclude(Include.NON_NULL)
public class ArtExhibition extends DelleMuseObject {

    @ManyToOne(fetch = FetchType.EAGER, cascade = jakarta.persistence.CascadeType.DETACH, targetEntity = ArtExhibitionStatusType.class)
    @JoinColumn(name = "artExhibitionStatusType_id", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonSerialize(using = DelleMuseIdNameSerializer.class)
    private ArtExhibitionStatusType artExhibitionStatusType;

    @OneToOne(fetch = FetchType.EAGER, targetEntity = Site.class)
    @JoinColumn(name = "site_id", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonSerialize(using = DelleMuseIdNameSerializer.class)
    private Site site;

    @Column(name = "permanent")
    private boolean permanent;

    @Column(name = "fromDate")
    private OffsetDateTime fromDate;

    @Column(name = "toDate")
    private OffsetDateTime toDate;

    @Column(name = "subtitle")
    private String subtitle;

    @Column(name = "subtitleKey")
    private String subTitleKey;

    @Column(name = "info")
    private String info;

    @Column(name = "infoKey")
    private String infoKey;

    @OneToMany(fetch = FetchType.EAGER, targetEntity = ArtExhibitionItem.class)
    @JoinColumn(name = "artExhibition_id", nullable = true, insertable = true)
    @JsonSerialize(using = DelleMuseListIdNameSerializer.class)
    @OrderBy("lower(title) ASC")
    private List<ArtExhibitionItem> artExhibitionItems;

    @OneToOne(fetch = FetchType.EAGER, targetEntity = Resource.class)
    @JoinColumn(name = "photo", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonProperty("photo")
    @JsonSerialize(using = DelleMuseIdNameSerializer.class)
    private Resource photo;

    @OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
    @JoinColumn(name = "video", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonProperty("video")
    @JsonSerialize(using = DelleMuseIdNameSerializer.class)
    private Resource video;

    @OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
    @JoinColumn(name = "audio", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonProperty("audio")
    @JsonSerialize(using = DelleMuseIdNameSerializer.class)
    private Resource audio;

    public ArtExhibition() {
    }

    @Override
    public ArtExhibitionModel model() {
        try {
            return (ArtExhibitionModel) getObjectMapper().readValue(getObjectMapper().writeValueAsString(this),
                    ArtExhibitionModel.class);
        } catch (JsonProcessingException e) {
            throw new InternalErrorException(e);
        }
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

    public List<ArtExhibitionItem> getArtExhibitionItems() {
        return artExhibitionItems;
    }

    public void setArtExhibitionItems(List<ArtExhibitionItem> artExhibitionItems) {
        this.artExhibitionItems = artExhibitionItems;
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
