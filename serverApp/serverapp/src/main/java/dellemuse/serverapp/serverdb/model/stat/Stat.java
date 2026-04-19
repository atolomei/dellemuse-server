package dellemuse.serverapp.serverdb.model.stat;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
 
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
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Identifiable;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.security.Role;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseIdNameSerializer;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
 

/**
 * 


	CREATE SEQUENCE visit_sequence_id
	START WITH 100
	INCREMENT BY 1
	NO MINVALUE
	NO MAXVALUE
	CACHE 1;


	CREATE TABLE stat (
   						id				 		 bigint primary key default nextval('visit_sequence_id'),
						page_id			 		 varchar(255) not null,
						session_id		 		 varchar(255) not null,
						site_id					 bigint references site(id) on delete cascade,
						artexhibitionguide_id	 bigint references artexhibitionguide(id) on delete cascade,
						guidecontent_id  		 bigint references guidecontent(id) on delete cascade,
						ts			 			 timestamp with time zone DEFAULT now() not null
	);
	

alter table stat add column userAgent character varying(1024);

alter table stat add column artwork_id 	 bigint references artwork(id) on delete cascade,
	

  
  CREATE TABLE pooledString (
   						id				 		 bigint primary key not null,
						value 			 		 character varying (2048)
						);
 * 
 * 
 * 
 */

@Entity
@Table(name = "stat")
@JsonInclude(Include.NON_NULL)
public class Stat extends JsonObject implements Identifiable  {


	
	static public Stat of(String pid, String sessionId, ArtExhibitionGuide gc) {
		Stat r = new Stat();
		r.setPageId(pid);
		r.setArtExhibitionGuide(gc);
		r.setSessionId(sessionId);
		r.setTimestamp(OffsetDateTime.now());
		return r;
	}
	
	static public Stat of(String pid, String sessionId, GuideContent gc ) {
		Stat r = new Stat();
		r.setPageId(pid);
		r.setGuideContent(gc);
		 
		r.setSessionId(sessionId);
		r.setTimestamp(OffsetDateTime.now());
		return r;
	}
	
	
	static public Stat of(String pid, String sessionId, Site site) {
		Stat r = new Stat();
		r.setPageId(pid);
		r.setSite(site);
		r.setSessionId(sessionId);
		r.setTimestamp(OffsetDateTime.now());
		return r;
	}
	
	
	
	
	@JsonIgnore
	static final private ObjectMapper hb6mapper = new DellemuseObjectMapper();

	@JsonIgnore
	static final private JsonFactory factory = new JsonFactory();

	@JsonIgnore
	static private Logger logger = Logger.getLogger(Stat.class.getName());

	@JsonIgnore
	static final DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss XXX");

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "visit_sequence_id")
	@SequenceGenerator(name = "visit_sequence_id", sequenceName = "visit_sequence_id", allocationSize = 1)
	private Long id;
	
	@Column(name = "ts")
	private OffsetDateTime timestamp;

	@Column(name = "page_id")
	private String pageId;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Site.class)
	@JoinColumn(name = "site_id", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	private Site site;
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ArtExhibitionGuide.class)
	@JoinColumn(name = "artExhibitionGuide_id", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	private ArtExhibitionGuide artExhibitionGuide;
	
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = GuideContent.class)
	@JoinColumn(name = "guideContent_id", nullable = true)
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	private GuideContent guideContent;
	
	
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ArtWork.class)
	@JoinColumn(name = "artwork_id", nullable = true)
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	private ArtWork artWork;
	
	
	@Column(name = "session_id")
	public String sessionId;
	
	
	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}



	@Column(name = "userAgent")
	public String userAgent;
	
	@Transient
	private boolean dependecies = false;

	
	public Stat() {
	}
 
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public OffsetDateTime getTimestamp() {
		return timestamp;
	}

	public String getPageId() {
		return pageId;
	}

	public Site getSite() {
		return site;
	}

	public ArtExhibitionGuide getArtExhibitionGuide() {
		return artExhibitionGuide;
	}

	public GuideContent getGuideContent() {
		return guideContent;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setTimestamp(OffsetDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public void setArtExhibitionGuide(ArtExhibitionGuide artExhibitionGuide) {
		this.artExhibitionGuide = artExhibitionGuide;
	}

	public void setGuideContent(GuideContent guideContent) {
		this.guideContent = guideContent;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
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

		
		if (getId() != null)
			str.append("\"id\": " + getId().toString());
	 
		
		if (this.timestamp != null)
			str.append(", \"timestamp\": \"" + getDateTimeFormatter().format(timestamp) + "\"");

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
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	public ArtWork getArtWork() {
		return artWork;
	}

	public void setArtWork(ArtWork artwork) {
		this.artWork = artwork;
	}

	 

 	
	 
	 
}
