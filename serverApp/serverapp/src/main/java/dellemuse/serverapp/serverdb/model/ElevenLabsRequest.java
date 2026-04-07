package dellemuse.serverapp.serverdb.model;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
import dellemuse.serverapp.serverdb.model.security.Role;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseUserSerializer;
import io.odilon.util.Check;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
 
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * 
 * 
 * CREATE TABLE audit ( id bigint primary key default nextval('audit_id'),
 * lastmodified timestamp with time zone DEFAULT now() not null,
 * lastmodifieduser bigint references users(id) on delete restrict not null,
 * action bigint not null, objectClassName character varying(512) not null,
 * objectId bigint not null, description text, json_data jsonb );
 * 
 * 
 * 
 *
 * 
 * 
 * 
 
  
  
  CREATE TABLE elevenlabsrequest (
    id  bigint  DEFAULT nextval('public.audit_id'::regclass) NOT NULL,
    lastmodified  timestamp with time zone DEFAULT now() NOT NULL,
    calledbyuser  bigint  NOT NULL references users(id) on delete cascade,
    siteid  bigint  references  site(id) on delete cascade,
    size integer NOT NULL default 0);
    
    
   
   
   
 */
@Entity
@Table(name = "elevenlabsrequest")
@JsonInclude(Include.NON_NULL)
public class ElevenLabsRequest extends JsonObject implements Identifiable, Auditable {

	@JsonIgnore
	static final private ObjectMapper hb6mapper = new DellemuseObjectMapper();

	@JsonIgnore
	static final private JsonFactory factory = new JsonFactory();

	@JsonIgnore
	static private Logger logger = Logger.getLogger(ElevenLabsRequest.class.getName());

	@JsonIgnore
	static final DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss XXX");

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "audit_gen")
	@SequenceGenerator(name = "audit_gen", sequenceName = "audit_id", allocationSize = 1)
	private Long id;

	@Column(name = "lastModified")
	private OffsetDateTime lastModified;

	@ManyToOne(fetch = FetchType.LAZY, cascade = jakarta.persistence.CascadeType.DETACH, targetEntity = User.class)
	@Fetch(FetchMode.SELECT)
	@JoinColumn(name = "calledbyuser", nullable = false)
	@JsonManagedReference
	@JsonBackReference
	@JsonProperty("user")
	@JsonSerialize(using = DelleMuseUserSerializer.class)
	private User user;

	@Column(name = "size")
	private int size;

	@Column(name = "siteId")
	private Long siteId;
	
	@Transient
	private boolean dependecies = false;

	public ElevenLabsRequest() {
	}

	@JsonIgnore
	public String getDisplayname() {
		return getName();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public OffsetDateTime getLastModified() {
		return lastModified;
	}

	public void setLastModified(OffsetDateTime lastModified) {
		this.lastModified = lastModified;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTitle() {
		return getName();
	}

	public boolean isDependencies() {
		return dependecies;
	}

	public void setDependencies(boolean b) {
		this.dependecies = b;
	}

	@Override
	@JsonIgnore
	public ObjectMapper getObjectMapper() {
		return hb6mapper;
	}

	protected DateTimeFormatter getDateTimeFormatter() {
		return df;
	}

	protected String baseJSON() {
		StringBuilder str = new StringBuilder();

		str.append(getClass().getSimpleName());

		str.append("\"id\": " + getId().toString());
		// str.append("\"user\": { \"id\": " + getUser().getId().toString() + ",
		// username: \"" + getUser().getUsername() + "\" }");

		if (lastModified != null)
			str.append(", \"lastModified\": \"" + getDateTimeFormatter().format(getLastModified()) + "\"");

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
	public OffsetDateTime getCreated() {
		return this.getLastModified();
	}

	@Override
	public User getLastModifiedUser() {
		return this.user;
	}

 	
	public static ElevenLabsRequest of( User user, int size, Optional<Long> siteId) {

		ElevenLabsRequest audit = new ElevenLabsRequest();
		OffsetDateTime now = OffsetDateTime.now();
		audit.setLastModified(now);
		audit.setUser(user);
		audit.setSize(size);
		
		if (siteId.isPresent())
			audit.setSiteId(siteId.get());
		return audit;
	}

	@Override
	public String getName() {
	 
		return null;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

	 
}
