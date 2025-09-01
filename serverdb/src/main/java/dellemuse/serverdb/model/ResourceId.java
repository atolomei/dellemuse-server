package dellemuse.serverdb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
@JsonInclude(Include.NON_NULL)
public class ResourceId {
    
    @JsonIgnore
    @Id 
    @SequenceGenerator(name="objectstorage_sequence",sequenceName="objectstorage_id", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="objectstorage_sequence")
    @Column(name="id", unique=true, nullable=false)
    long id;
    
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id=id;
    }
}
