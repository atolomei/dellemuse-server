package dellemuse.serverapp.serverdb.model;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

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
 */
@Entity
@Table(name = "audit")
@JsonInclude(Include.NON_NULL)
public class DelleMuseAudit extends JsonObject implements Identifiable, Auditable {

	@JsonIgnore
	static final private ObjectMapper hb6mapper = new DellemuseObjectMapper();

	@JsonIgnore
	static final private JsonFactory factory = new JsonFactory();

	@JsonIgnore
	static private Logger logger = Logger.getLogger(DelleMuseAudit.class.getName());

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
	@JoinColumn(name = "lastModifiedUser", nullable = false)
	@JsonManagedReference
	@JsonBackReference
	@JsonProperty("lastModifiedUser")
	@JsonSerialize(using = DelleMuseUserSerializer.class)
	private User user;

	@Column(name = "action")
	@Enumerated(EnumType.ORDINAL)
	private AuditAction action;

	@Column(name = "objectClassName")
	private String objectClassName;

	@Column(name = "objectId")
	private Long objectId;

	@Column(name = "description")
	private String description;

	@Column(name = "descriptionKey")
	private String descriptionKey;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "json_data", columnDefinition = "jsonb")
	private Map<String, String> audit;

	@Transient
	private boolean dependecies = false;

	

	/**
	 * 
	 */
	public DelleMuseAudit() {
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

		/**
		 * StringWriter stringWriter = new StringWriter();
		 * 
		 * try (JsonGenerator generator = factory.createGenerator(stringWriter)) {
		 * generator.writeStartObject(); generator.writeNumberField("id", getId());
		 * generator.writeStringField("name", getName());
		 * generator.writeStringField("title", getTitle()); if (this.getLastModified()
		 * != null) generator.writeStringField("lastModified",
		 * df.format(this.getLastModified())); generator.writeEndObject();
		 * 
		 * stringWriter.flush(); String jsonString = stringWriter.toString();
		 * 
		 * logger.debug(jsonString);
		 * 
		 * return jsonString;
		 * 
		 * } catch (Exception e) { logger.error(e); return " { \"error\": \"" +
		 * e.getClass().getName() + (e.getMessage() != null ? (" | " +
		 * e.getMessage().replace("\"", "'" + "\"")) : "") + " }"; }
		 **/

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

	@Override
	public String getName() {
		return (objectId != null ? objectId.toString() : "") + "-" + (objectClassName != null ? objectClassName : "") + "-" + (action != null ? action.getLabel() : "") + "-" + (lastModified != null ? df.format(lastModified) : "");
	}

	public AuditAction getAction() {
		return action;
	}

	public String getObjectClassName() {
		return objectClassName;
	}

	public Long getObjectId() {
		return objectId;
	}

	public String getDescription() {
		return description;
	}

	public Map<String, String> getAudit() {
		return audit;
	}

	public void setAction(AuditAction action) {
		this.action = action;
	}

	public void setObjectClassName(String objectClassName) {
		this.objectClassName = objectClassName;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setAudit(Map<String, String> audit) {
		this.audit = audit;
	}
	
	/**
	 * 
	 * 
	 * 
	 * @param id
	 * @param objectClassName
	 * @param user
	 * @param a
	 * @return
	 */
	
	/**
	public static DelleMuseAudit of(Long id, String objectClassName, User user, AuditAction a) {
		Check.requireNonNullArgument(id, "id is null");
		Check.requireNonNullArgument(objectClassName, " objectClassName is null");

		DelleMuseAudit audit = new DelleMuseAudit();
		OffsetDateTime now = OffsetDateTime.now();
		audit.setLastModified(now);
		audit.setAction(a);
		audit.setObjectId(id);
		audit.setUser(user);

		audit.setObjectClassName(objectClassName);
		return audit;
		
	}
*/
	
	public static DelleMuseAudit of(DelleMuseObject o, User user, AuditAction a) {

		Check.requireNonNullArgument(o, "DelleMuseObject is null");
		Check.requireTrue(o.getId() != null, "DelleMuseObject id is null");

		DelleMuseAudit audit = new DelleMuseAudit();
		OffsetDateTime now = OffsetDateTime.now();
		audit.setLastModified(now);
		audit.setAction(a);
		audit.setObjectId(o.getId());
		audit.setUser(user);
		audit.setObjectClassName(o.getObjectClassName());
		return audit;
	}

	 public static DelleMuseAudit of(DelleMuseObject o, User user, AuditAction a, String auditMsg) {
		return of(o, user, a, null, null);
	}

	public static DelleMuseAudit of(DelleMuseObject o, User user, AuditAction a, String auditMsg, Map<String, String> auditJson) {

		Check.requireNonNullArgument(o, "DelleMuseObject is null");
		Check.requireTrue(o.getId() != null, "DelleMuseObject id is null");

		DelleMuseAudit audit = new DelleMuseAudit();
		OffsetDateTime now = OffsetDateTime.now();
		audit.setLastModified(now);

		if (auditMsg != null)
			audit.setDescription(auditMsg);

		if (auditJson != null)
			audit.setAudit(auditJson);
		
		audit.setUser(user);
		audit.setAction(a);
		audit.setObjectId(o.getId());
		audit.setObjectClassName(o.getObjectClassName());
		return audit;
	}

	public static DelleMuseAudit ofArtExhibition(DelleMuseObject o, User addedBy, AuditAction update, String addItem, ArtExhibitionItem item) {
		
		Check.requireNonNullArgument(o, "DelleMuseObject is null");
		Check.requireTrue(o.getId() != null, "DelleMuseObject id is null");
		
		Map<String, String> map = new HashMap<String, String>();

		map.put("class", item.getClass().getSimpleName());
		map.put("action", String.valueOf(update.getId()));
		map.put("subaction", addItem);
		map.put("id", item.getId().toString());
		map.put("name", item.getName());
		
		return of(o, addedBy, update, addItem, map);
	}

	public static DelleMuseAudit ofUser(User u, User by, AuditAction update, String addRole, Role r) {
	
		Check.requireNonNullArgument(u, "User is null");
		Check.requireTrue(u.getId() != null, "User id is null");
		
		Map<String, String> map = new HashMap<String, String>();

		map.put("class", r.getClass().getSimpleName());
		map.put("action", String.valueOf(update.getId()));
		map.put("subaction", addRole);
		map.put("id", r.getId().toString());
		map.put("name", r.getName());
		
		return of(u, by, update, addRole, map);

	}
}
