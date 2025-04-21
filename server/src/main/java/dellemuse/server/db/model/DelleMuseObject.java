package dellemuse.server.db.model;


import java.time.OffsetDateTime;
import java.util.Optional;


import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import dellemuse.model.JsonObject;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class DelleMuseObject extends JsonObject implements Identifiable, Auditable {

	@Id
	@Column(name="id")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sequence_gen")
	@SequenceGenerator(name = "sequence_gen", sequenceName = "sequence_id", allocationSize = 1)
	private Long id;
	
	@Column(name="created")
	private OffsetDateTime created;
	
	@Column(name="lastmodified")
	private OffsetDateTime lastModified;
	
	
	@ManyToOne(fetch = FetchType.LAZY, cascade=jakarta.persistence.CascadeType.DETACH, targetEntity = User.class)
	@Fetch(FetchMode.SELECT)
    @JoinColumn(name = "lastModifiedUser", nullable=false)
	@JsonManagedReference
    @JsonBackReference
    @JsonIgnore
    private User lastModifiedUser;
	
	@JsonProperty("lastModifiedUserId")
    public Optional<Long> getLastModifiedUserId() {
        return (lastModifiedUser!=null) ?
                Optional.of(lastModifiedUser.getId()) : Optional.empty();
    }
	
	
	
	/**
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
	*/
	   
	public DelleMuseObject() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public OffsetDateTime getCreated() {
		return created;
	}

	public void setCreated(OffsetDateTime created) {
		this.created = created;
	}
	
	public OffsetDateTime getLastModified() {
		return lastModified;
	}

	public void setLastModified(OffsetDateTime lastModified) {
		this.lastModified = lastModified;
	}

	public User getLastModifiedUser() {
		return lastModifiedUser;
	}

	public void setLastModifiedUser(User lastmodifiedUser) {
	        this.lastModifiedUser = lastmodifiedUser;
	}
	
	

	
}
