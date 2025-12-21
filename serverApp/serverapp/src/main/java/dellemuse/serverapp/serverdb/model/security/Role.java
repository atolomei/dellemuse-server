package dellemuse.serverapp.serverdb.model.security;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import dellemuse.serverapp.page.PrefixUrl;
import dellemuse.serverapp.serverdb.model.DelleMuseObject;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonInclude(Include.NON_NULL)
public abstract class Role extends DelleMuseObject {
 	
	@ManyToMany
    @JoinTable(
        name = "roles_privileges", 
        joinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(
          name = "privilege_id", referencedColumnName = "id"))
    private Set<Privilege> privileges;
	
	
	public abstract Set<User> getUsers();
	
	@Override
	public int hashCode() {
	    return getId() != null ? getId().hashCode() : 0;
	}
	
	
	public Role() {
	}
	
	@Override
	public String getObjectClassName() {
		return Role.class.getSimpleName();
	}

	
	public String getRoleDisplayName() {
		return getName();
	}
	
	public final String getPrefixUrl() {
		return PrefixUrl.Role;
	}
	
	@Override
	public String getDisplayClass() {
		ResourceBundle res = ResourceBundle.getBundle(getClass().getName(), Locale.ENGLISH);
		return res.getString( this.getClass().getSimpleName().toLowerCase());
	}
	
	@Override
	public String getDisplayClass(Locale locale) {
		ResourceBundle res = ResourceBundle.getBundle(getClass().getName(), locale);
		return res.getString( this.getClass().getSimpleName().toLowerCase());
	}

	public static final String getIcon() {
		return "fa-duotone fa-id-badge";
	}
 	
	
	
	
};
