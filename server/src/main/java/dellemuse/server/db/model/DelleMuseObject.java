package dellemuse.server.db.model;

import java.time.OffsetDateTime;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dellemuse.model.DelleMuseModelObject;
import dellemuse.model.JsonObject;
import dellemuse.model.logging.Logger;
import dellemuse.server.db.model.serializer.DelleMuseUserSerializer;
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
public abstract class DelleMuseObject extends JsonObject implements Identifiable, Auditable {

	@JsonIgnore 
	static final private ObjectMapper hb6mapper = new ObjectMapper();
	  
	static private Logger logger =	Logger.getLogger(DelleMuseObject.class.getName());

	
	static  {
		
		hb6mapper.registerModule(new JavaTimeModule());
		hb6mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		hb6mapper.registerModule(new Jdk8Module());
		hb6mapper.registerModule(new Hibernate6Module());
		
		// hb6mapper.registerModule(new Hibernate6Module().configure(Hibernate6Module.Feature.FORCE_LAZY_LOADING, false));
		// mapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
	}

	
	@Override
	@JsonIgnore 
	public ObjectMapper getObjectMapper() {
		return hb6mapper;
	}

	
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_gen")
    @SequenceGenerator(name = "sequence_gen", sequenceName = "sequence_id", allocationSize = 1)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "nameKey")
    private String nameKey;
    
    @Column(name = "title")
    private String title;

    @Column(name = "titleKey")
    private String titleKey;

    @Column(name = "created")
    private OffsetDateTime created;

    @Column(name = "lastModified")
    private OffsetDateTime lastModified;

    @ManyToOne(fetch = FetchType.LAZY, cascade = jakarta.persistence.CascadeType.DETACH, targetEntity = User.class)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "lastModifiedUser", nullable = false)
    @JsonManagedReference
    @JsonBackReference
    @JsonProperty("lastModifiedUser")
    @JsonSerialize(using = DelleMuseUserSerializer.class)
    private User lastModifiedUser;

    public DelleMuseObject() {
    }

    @JsonIgnore
    public String getDisplayname() {
        return (getTitle()!=null) ? getTitle() : getName();
    }
    
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleKey() {
        return titleKey;
    }

    public void setTitleKey(String titleKey) {
        this.titleKey = titleKey;
    }

    public String getTitle() {
        if (title!=null)
            return title;
        return getName();
    }
    
	
	@Override
	public String toJSON() {
	  try {
		  return getObjectMapper().writeValueAsString(this);
		} catch (Exception e) {
				logger.error("Serialization does not work if this entity is detached (JPA FetchType.LAZY relationships)");
				return " { \"error\": \"" + e.getClass().getName() + (e.getMessage()!=null? (" | " + e.getMessage().replace("\"", "'" + "\"")) : "") + " }"; 
		}
	}
   
}
