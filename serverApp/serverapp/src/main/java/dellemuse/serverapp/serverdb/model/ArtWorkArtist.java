package dellemuse.serverapp.serverdb.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.serverapp.serverdb.model.serializer.DelleMuseIdSerializer;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

//@Entity
//@Table(name = "artworkArtist")
//@JsonInclude(Include.NON_NULL)
public class ArtWorkArtist extends DelleMuseObject {

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = ArtWork.class)
    @JoinColumn(name = "artwork_id", nullable = true)
    @JsonBackReference
    @JsonSerialize(using = DelleMuseIdSerializer.class)
    private ArtWork artwork;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Person.class)
    @JoinColumn(name = "person_id", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonSerialize(using = DelleMuseIdSerializer.class)
    private Person artist;

    public ArtWorkArtist() {
    }

    public ArtWork getArtwork() {
        return artwork;
    }

    public void setArtwork(ArtWork artwork) {
        this.artwork = artwork;
    }

    public Person getPerson() {
        return artist;
    }

    public void setPerson(Person person) {
        this.artist = person;
    }
};
