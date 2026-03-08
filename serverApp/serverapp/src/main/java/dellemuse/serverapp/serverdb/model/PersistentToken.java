package dellemuse.serverapp.serverdb.model;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import dellemuse.model.JsonObject;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "token")
@JsonInclude(Include.NON_NULL)
public class PersistentToken extends JsonObject  {

    @JsonProperty("id")
    private Long id;
	  
	@Column(name = "entity")
	private String entity;
	
	@Column(name = "object")
	private String object;
	
	@Column(name = "token")
	private String token;
	
	@JsonProperty("created")
	private OffsetDateTime created;
	
	@JsonProperty("expires")
	private OffsetDateTime expires;

	public PersistentToken() {
	}

}

/**
 
  CREATE SEQUENCE token_id
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
  
  CREATE TABLE token (
    id bigint DEFAULT nextval('token_id') NOT NULL,
    entity character varying(512),
    object character varying(512),
    token character varying(512),
    created timestamp with time zone DEFAULT now() NOT NULL,
    expires timestamp with time zone DEFAULT now() NOT NULL,
    draft text
);

*/


 