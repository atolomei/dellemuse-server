package dellemuse.server.db.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.model.ArtWorkModel;
import dellemuse.server.db.model.serializer.DelleMuseIdNameSerializer;
import dellemuse.server.db.model.serializer.DelleMuseResourceSerializer;
import dellemuse.server.db.model.serializer.DelleMuseSetIdNameSerializer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

import jakarta.persistence.Table;

@Entity
@Table(name = "artwork")
@JsonInclude(Include.NON_NULL)
public class ArtWork extends DelleMuseObject {

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = ArtWorkType.class)
    @JoinColumn(name = "artworkType_id", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonSerialize(using = DelleMuseIdNameSerializer.class)
    private ArtWorkType artworkType;

    @Column(name = "subtitle")
    private String subtitle;

    @Column(name = "subtitleKey")
    private String subTitleKey;

    @JsonIgnore
    @Column(name = "info")
    private String info;

    @Column(name = "infoKey")
    private String infoKey;

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    @JoinTable(name = "ArtWorkArtist", joinColumns = { @JoinColumn(name = "artwork_id") }, inverseJoinColumns = {
    @JoinColumn(name = "person_id") })
    @JsonSerialize(using = DelleMuseSetIdNameSerializer.class)
    @JsonManagedReference
    @JsonBackReference
    @JsonProperty("artists")
    Set<Person> artists = new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER, targetEntity = Resource.class)
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

    @OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
    @JoinColumn(name = "audio", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonProperty("audio")
    @JsonSerialize(using = DelleMuseResourceSerializer.class)
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

    /**
     * @JsonIgnore public List<Person> getArtists() { List<Person> list = new
     *             ArrayList<Person>(); if (artWorkArtists != null)
     *             artWorkArtists.forEach(item -> list.add(item.getPerson()));
     *             return list; }
     **/

    @Override
    public ArtWorkModel model() {
        try {
            return (ArtWorkModel) getObjectMapper().readValue(getObjectMapper().writeValueAsString(this), ArtWorkModel.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<Person> getArtists() {
        return artists;
    }

    public void setArtists(Set<Person> artists) {
        this.artists = artists;
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
