package dellemuse.serverapp.serverdb.model.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import dellemuse.serverapp.page.PrefixUrl;
import dellemuse.serverapp.serverdb.model.DelleMuseObject;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "privilege")
@JsonInclude(Include.NON_NULL)
public class Privilege extends DelleMuseObject {
	
	public Privilege() {
	}
	
	public final String getPrefixUrl() {
		return PrefixUrl.Privilege;
	}
	 
};
