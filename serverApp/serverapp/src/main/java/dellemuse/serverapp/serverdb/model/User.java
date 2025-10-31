package dellemuse.serverapp.serverdb.model;

import java.util.Locale;

import org.threeten.bp.ZoneId;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "users")
@JsonInclude(Include.NON_NULL)
public class User extends DelleMuseObject {

	
	// @Transient
	// private Locale locale;
	// public Locale getLocale() {
	//	return locale;
	// }
	
	@Column(name = "zoneId")
	private String zoneId;
	
    public User() {
    }
    
    public String getUsername() {
        return getName();
    }

    public String getDisplayname() {
        return getUsername();
    }
    
	public Locale getLocale() {
		return Locale.forLanguageTag(getLanguage());
	}
	
	public void setZoneId(ZoneId zid) {
		this.zoneId=zid.getId();
	}
	
	public ZoneId getZoneId() {
		return ZoneId.of(zoneId);
	}



 
}
