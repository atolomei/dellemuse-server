package dellemuse.server.db.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.model.ArtWorkModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import jakarta.persistence.Table;

@Entity
@Table(name = "artwork")
@JsonInclude(Include.NON_NULL)
public class ArtWork extends DelleMuseObject {

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = ArtWorkType.class)
    @JoinColumn(name = "artworkType_id", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonSerialize(using = DelleMuseIdSerializer.class)
    private ArtWorkType artworkType;

    @Column(name = "subtitle")
    private String subtitle;

    @Column(name = "subtitleKey")
    private String subTitleKey;

    @Column(name = "info")
    private String info;

    @Column(name = "infoKey")
    private String infoKey;

    @OneToMany(fetch = FetchType.EAGER, targetEntity = ArtWorkArtist.class)
    @JoinColumn(name = "artwork_id", nullable = true, insertable = false)
    @JsonSerialize(using = DelleMuseListIdSerializer.class)
    @JsonManagedReference
    @JsonBackReference
    private List<ArtWorkArtist> artWorkArtists;

    @OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
    @JoinColumn(name = "photo", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonProperty("photo")
    @JsonSerialize(using = DelleMuseIdSerializer.class)
    private Resource photo;

    @OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
    @JoinColumn(name = "video", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonProperty("video")
    @JsonSerialize(using = DelleMuseIdSerializer.class)
    private Resource video;

    @OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
    @JoinColumn(name = "audio", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonProperty("audio")
    @JsonSerialize(using = DelleMuseIdSerializer.class)
    private Resource audio;

    public ArtWork() {
    }


    public ArtWorkType getArtworkType() {
        return artworkType;
    }

    public void setArtworkType(ArtWorkType artworkType) {
        this.artworkType = artworkType;
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

    @JsonIgnore
    public List<Person> getArtists() {
        List<Person> list = new ArrayList<Person>();
        if (artWorkArtists != null)
            artWorkArtists.forEach(item -> list.add(item.getPerson()));
        return list;
    }

    
    @Override
    public ArtWorkModel model() {
        try {
            return (ArtWorkModel) getObjectMapper().readValue(getObjectMapper().writeValueAsString(this), ArtWorkModel.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

};
