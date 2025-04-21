package dellemuse.server.db.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "artworkArtist")
public class ArtWorkArtist extends DelleMuseObject {

    @Column(name="name")
    private String name;

    @Column(name="nameKey")
    private String nameKey;
    

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = ArtWork.class)
    @JoinColumn(name = "artwork_id", nullable=true) 
    @JsonManagedReference
    @JsonBackReference
    @JsonIgnore
    private ArtWork artwork;
    

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Person.class)
    @JoinColumn(name = "person_id", nullable=true) 
    @JsonManagedReference
    @JsonBackReference
    @JsonIgnore
    private Person artist;

    public ArtWorkArtist() {
        
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

