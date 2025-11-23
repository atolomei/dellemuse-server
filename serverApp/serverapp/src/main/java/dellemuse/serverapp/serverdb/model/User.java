package dellemuse.serverapp.serverdb.model;

import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.threeten.bp.ZoneId;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "users")
@JsonInclude(Include.NON_NULL)
public class User extends DelleMuseObject {

	
	@Column(name = "zoneId")
	private String zoneId;
	
	@Column(name = "password")
	private String password;


	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "user_roles",  joinColumns = @JoinColumn(name = "user_id"))
	@Column(name = "role")
	private Set<String> roles;
	
	
	public String getUsername() {
		return this.getName();
	}
	
	public void setUsername( String username ) {
		setName(username);
	}
	
	public User() {
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	//public Set<SimpleGrantedAuthority> getRoles() {
	//	return null;
	//}

	


 
}
