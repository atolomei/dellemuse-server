package dellemuse.serverapp.serverdb.model;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dellemuse.model.JsonObject;
import dellemuse.model.logging.Logger;
import dellemuse.serverapp.DellemuseObjectMapper;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseUserSerializer;
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
import jakarta.persistence.Transient;

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

	protected DateTimeFormatter getDateTimeFormatter() { return df;}
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_gen")
	@SequenceGenerator(name = "sequence_gen", sequenceName = "sequence_id", allocationSize = 1)
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "nameKey")
	private String nameKey;

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
	private ObjectState state;

	@Column(name = "language")
	private  String language;

	
	@Column(name = "audioAutoGenerate")
	private  boolean audioAutoGenerate;
	
	
	@Transient
	private  boolean dependecies = false;

	
	public DelleMuseObject() {
	}
	
	public boolean isDependencies() {
		return dependecies;
	}
	
	public void setDependencies( boolean b) {
		this.dependecies=b;
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
	
	public void setObjectState( ObjectState state) {
		this.state=state;
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

	
	// @Column(name = "title")
	// private String title;

	// @Column(name = "titleKey")
	// private String titleKey;
	
	//public void setTitle(String title) {
	//	this.title = title;
	//}

	//public String getTitleKey() {
	//	return titleKey;
	//}

	//public void setTitleKey(String titleKey) {
	//	this.titleKey = titleKey;
	//}

	public String getTitle() {
		//if (title != null)
		//	return title;
		return getName();
	}
	
	public String getLanguage() {
		return this.language;
	}
	
	public void setLanguage( String lang) {
		 language=lang;
	}
	

	@Override
	@JsonIgnore
	public ObjectMapper getObjectMapper() {
		return hb6mapper;
	}
	
	
	protected String baseJSON() {
		StringBuilder str = new StringBuilder();
		
		str.append(getClass().getSimpleName());
		
		str.append("\"id\": "+ getId().toString());
		
		if (name!=null)
			str.append(", \"name\": \""+ getName().toString()+"\"");
		
		//if (title!=null)
		//	str.append(", \"title\": \""+ getName().toString()+"\"");
		
		if (lastModified!=null)
			str.append(", \"lastModified\": \""+ getDateTimeFormatter().format(getLastModified()));

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
		

		/**
		StringWriter stringWriter = new StringWriter();

		try (JsonGenerator generator = factory.createGenerator(stringWriter)) {
			generator.writeStartObject();
			generator.writeNumberField("id", getId());
			generator.writeStringField("name", getName());
			generator.writeStringField("title", getTitle());
			if (this.getLastModified() != null)
				generator.writeStringField("lastModified", df.format(this.getLastModified()));
			generator.writeEndObject();
			
			stringWriter.flush();
			String jsonString = stringWriter.toString();

			logger.debug(jsonString);
			
			return jsonString;

		} catch (Exception e) {
			logger.error(e);
			return " { \"error\": \"" + e.getClass().getName()
					+ (e.getMessage() != null ? (" | " + e.getMessage().replace("\"", "'" + "\"")) : "") + " }";
		}
		**/
		
	}

	@Override
	public String toJSON() {
		try {

			return getObjectMapper().writeValueAsString(this);

		} catch (Exception e) {

			logger.error("Serialization does not work if this entity is detached (JPA FetchType.LAZY relationships)");
			return " { \"error\": \"" + e.getClass().getName()
					+ (e.getMessage() != null ? (" | " + e.getMessage().replace("\"", "'" + "\"")) : "") + " }";
		}
	}

	public boolean isAudioAutoGenerate() {
		return audioAutoGenerate;
	}

	public void setState(ObjectState state) {
		this.state = state;
	}

	public void setAudioAutoGenerate(boolean audioAutoGenerate) {
		this.audioAutoGenerate = audioAutoGenerate;
	}

}
