package dellemuse.serverapp.serverdb.model;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.model.JsonObject;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseIdNameSerializer;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseIdSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/*@Entity
@Table(name = "artworkArtist")
@JsonInclude(Include.NON_NULL)
*/
public class ArtWorkArtist extends  JsonObject implements Identifiable  {

   // @ManyToOne(fetch = FetchType.LAZY, targetEntity = ArtWork.class)
    //@JoinColumn(name = "artwork_id", referencedColumnName = "id", nullable = false)
    //@JsonIgnore
    private ArtWork artwork;
    
    
    
   // @ManyToOne(fetch = FetchType.LAZY, targetEntity = Person.class)
    //@JoinColumn(name = "person_id", referencedColumnName = "id", nullable = false)
    //@JsonIgnore
    private Person person;
    
    @Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_gen")
	@SequenceGenerator(name = "sequence_gen", sequenceName = "sequence_id", allocationSize = 1)
	private Long id;

	@Column(name = "name")
	private String name;

 	@Column(name = "title")
	private String title;

	@Column(name = "titleKey")
	private String titleKey;

	@Column(name = "created")
	private OffsetDateTime created;

	@Column(name = "lastModified")
	private OffsetDateTime lastModified;

	@Transient
	private  boolean dependecies = false;
	
	public boolean isDependencies() {
		return dependecies;
	}
	
	public void setDependencies( boolean b) {
		this.dependecies=b;
	}
	 
	@JsonIgnore
	public String getDisplayname() {
		return (getName() != null) ? getName() : "null";
	}
	 
    public ArtWorkArtist() {
    }

    public ArtWork getArtwork() {
        return artwork;
    }

    public void setArtwork(ArtWork artwork) {
        this.artwork = artwork;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

	 
	public OffsetDateTime getCreated() {
		return this.created;
	}

	 
	public OffsetDateTime getLastModified() {
		return this.lastModified;
	}
	 

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getTitle() {
		return this.title;
	}

};
