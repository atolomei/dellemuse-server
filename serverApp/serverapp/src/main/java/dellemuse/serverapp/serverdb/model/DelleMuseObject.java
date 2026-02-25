package dellemuse.serverapp.serverdb.model;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
 
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
 
import dellemuse.model.JsonObject;
import dellemuse.model.logging.Logger;
import dellemuse.serverapp.DellemuseObjectMapper;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseUserSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Transient;

/**
 * 
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonInclude(Include.NON_NULL)
public abstract class DelleMuseObject extends JsonObject implements Identifiable, Auditable {

	@JsonIgnore
	static final private ObjectMapper hb6mapper = new DellemuseObjectMapper();

	@JsonIgnore
	static final private JsonFactory factory = new JsonFactory();

	@JsonIgnore
	static private Logger logger = Logger.getLogger(DelleMuseObject.class.getName());

	@JsonIgnore
	static final DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss XXX");

	protected DateTimeFormatter getDateTimeFormatter() {
		return df;
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_gen")
	@SequenceGenerator(name = "sequence_gen", sequenceName = "sequence_id", allocationSize = 1)
	private Long id;

	@Column(name = "name")
	private String name;

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

	@Column(name = "state")
	@Enumerated(EnumType.ORDINAL)
	private ObjectState state;

	@Transient
	private boolean dependecies = false;

	public DelleMuseObject() {
	}
	
	public String getDisplayClass() {
		ResourceBundle res = ResourceBundle.getBundle(DelleMuseObject.class.getName(), Locale.ENGLISH);
		return res.getString("name");
	}
	
	public String getDisplayClass(Locale locale) {
		ResourceBundle res = ResourceBundle.getBundle(DelleMuseObject.class.getName(), locale);
		return res.getString("name");
	}
	
	/**
	 * used by Audit
	 */
	public String getObjectClassName() {
			return this.getClass().getSimpleName();
	}
	
	public boolean isDependencies() {
		return dependecies;
	}

	public void setDependencies(boolean b) {
		this.dependecies = b;
	}

	@JsonIgnore
	public String getDisplayname() {
		return (getTitle() != null) ? getTitle() : getName();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ObjectState getState() {
		return this.state;
	}
	
	public void setState(ObjectState state) {
		this.state = state;
	}

	public void setObjectState(ObjectState state) {
		this.state = state;
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

	public String getTitle() {
		return getName();
	}

	@Override
	@JsonIgnore
	public ObjectMapper getObjectMapper() {
		return hb6mapper;
	}

	protected String baseJSON() {
		StringBuilder str = new StringBuilder();
		str.append("\"id\": " + getId().toString());

		if (name != null)
			str.append(", \"name\": \"" + getName().toString() + "\"");

	 	if (lastModified != null)
			str.append(", \"lastModified\": \"" + getDateTimeFormatter().format(getLastModified()));

		return str.toString();
	}

	@Override
	public String toString() {

		StringBuilder str = new StringBuilder();
		str.append(getClass().getSimpleName());
		str.append(" { ");
		str.append(baseJSON());
		str.append(" } ");

		return str.toString();
 
	}

	@Override
	public String toJSON() {
		try {

			return getObjectMapper().writeValueAsString(this);

		} catch (Exception e) {

			logger.error("Serialization does not work if this entity is detached (JPA FetchType.LAZY relationships)");
			return " { \"error\": \"" + e.getClass().getName() + (e.getMessage() != null ? (" | " + e.getMessage().replace("\"", "'" + "\"")) : "") + " }";
		}
	}
	
	@Override
	public int hashCode() {
	    return getId() != null ? getId().hashCode() : 0;
	}

	
	@Override
	public boolean equals(Object o) {

		if (o==null)
			return false;
		 
		if (this == o) 
			return true;

		if (!(o instanceof DelleMuseObject)) 
			return false;
		 
		if (this.getId()==null)
			return false;
	 
		if ((o.getClass().getName().equals(this.getClass().getName()))) {
			
			if (((DelleMuseObject) o).getId()==null)
					return false;
			
			return ((DelleMuseObject) o).getId().equals(getId());
		}
		
		return false;
	}
}
