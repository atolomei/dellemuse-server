package dellemuse.serverapp.serverdb.model;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
 

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.serverdb.model.security.Role;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
import dellemuse.serverapp.serverdb.model.security.RoleInstitution;
import dellemuse.serverapp.serverdb.model.security.RoleSite;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseListIdNameSerializer;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseSetPersonSerializer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
@JsonInclude(Include.NON_NULL)
public class User extends DelleMuseObject {

	static private Logger logger = Logger.getLogger(User.class.getName());


	public static final String getIcon() {
		return "fa-duotone fa-solid fa-user";
	}

	
	@JsonProperty("zoneId")
	@Column(name = "zoneId")
	private String zoneId;

	@JsonProperty("password")
	@Column(name = "password")
	private String password;
 
	@ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "general_role_id") })
	@JsonSerialize(using = DelleMuseSetPersonSerializer.class)
	@JsonManagedReference
	@JsonProperty("rolesGeneral")
	private Set<RoleGeneral> rolesGeneral;

	@ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "institution_role_id") })
	@JsonSerialize(using = DelleMuseSetPersonSerializer.class)
	@JsonManagedReference
	@JsonProperty("rolesInstitution")
	private Set<RoleInstitution> rolesInstitution;
 
	@ManyToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = { @JoinColumn(name = "site_role_id") })
	@JsonSerialize(using = DelleMuseSetPersonSerializer.class)
	@JsonManagedReference
	@JsonBackReference
	@JsonProperty("rolesSite")
	private Set<RoleSite> rolesSite;

	@Column(name = "language")
	private String language;
	
	@Override
	public String getObjectClassName() {
		return User.class.getSimpleName();
	}

	
	
	public User() {
	}

	public String getLanguage() {
		 return this.language;
	}

	public void setLanguage(String lang) {
		language = lang;
	}

	public String getUsername() {
		return this.getName();
	}

	public void setUsername(String username) {
		setName(username);
	}

	public String getDisplayname() {
		return getUsername();
	}

	public Locale getLocale() {
		return Locale.forLanguageTag(getLanguage());
	}

	public void setLocale(Locale locale) {
		setLanguage(locale.getLanguage());
	}

	
	public void setZoneId(ZoneId z) {
		this.setZoneIdStr(z.getId());
	}

	public ZoneId getZoneId() {
		if (getZoneIdStr()==null)
			return ZoneId.systemDefault();
		return ZoneId.of(zoneId);
		
	}

	public String getZoneIdStr() {
		return zoneId;
	}
	
	public void setZoneIdStr(String zoneid) {
		this.zoneId = zoneid;
	}
	
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<RoleGeneral> getRolesGeneral() {
		return rolesGeneral;
	}

	public void setRolesGeneral(Set<RoleGeneral> roles) {
		this.rolesGeneral = roles;
	}

	public Set<RoleInstitution> getRolesInstitution() {
		return rolesInstitution;
	}

	public void setRolesInstitution(Set<RoleInstitution> roles) {
		this.rolesInstitution = roles;
	}

	public Set<RoleSite> getRolesSite() {
		return rolesSite;
	}

	public void setRolesSite(Set<RoleSite> roles) {
		this.rolesSite = roles;
	}
	
	@Override
	public int hashCode() {
	    return getId() != null ? getId().hashCode() : 0;
	}
	
	@Override
	public boolean equals(Object o) {
	    
		if (o==null) return false;
	    
		if (this == o) return true;
	    
	    if (!(o instanceof User)) return false;
	    
	    User other = (User) o;
	    return getId() != null && getId().equals(other.getId());
	}
	
	
	
	public List<String> getRolesAsString() {

		List<String> list = new ArrayList<String>();

		// getRolesGeneral().forEach( r -> list.add(
		// r.getClass().getSimpleName()+"-"+r.getName()));
		// getRolesInstitution().forEach( r -> list.add(
		// r.getClass().getSimpleName()+"-"+r.getInstitution().getId().toString()));
		// getRolesSite().forEach( r -> list.add(
		// r.getClass().getSimpleName()+"-"+r.getSite().getId().toString()));

		// list.forEach( r -> logger.debug(r));
		return list;
	}

	public boolean hasRole(Role r) {

		if (r == null)
			return false;

		if (r.getId() == null)
			return false;

		if (r instanceof RoleGeneral) {
			if (getRolesGeneral() != null) {
				for (RoleGeneral g : getRolesGeneral()) {
					if (g.getId().equals(r.getId()))
						return true;
				}
			}
			return false;
		}

		if (r instanceof RoleSite) {
			if (getRolesSite() != null) {
				for (RoleSite g : getRolesSite()) {
					if (g.getId().equals(r.getId()))
						return true;
				}
			}
			return false;
		}

		if (r instanceof RoleInstitution) {
			if (getRolesInstitution() != null) {
				for (RoleInstitution g : getRolesInstitution()) {
					if (g.getId().equals(r.getId()))
						return true;
				}
			}
			return false;
		}
		throw new IllegalArgumentException("role not supported -> " + r.getClass().getName());
	}

	public void removeRole(Role r) {

		if (r == null)
			return;

		if (r instanceof RoleGeneral) {

			if (getRolesGeneral() != null && getRolesGeneral().contains((RoleGeneral) r))
				getRolesGeneral().remove(r);

			return;
		}

		if (r instanceof RoleSite) {
			if (getRolesSite() != null && getRolesSite().contains((RoleSite) r))
				getRolesSite().remove(r);

			return;

		}

		if (r instanceof RoleInstitution) {
			if (getRolesInstitution() != null && getRolesInstitution().contains((RoleInstitution) r))
				getRolesInstitution().remove(r);

			return;

		}

		throw new IllegalArgumentException("role not supported -> " + r.getClass().getName());

	}

	public void addRole(Role r) {

		if (r == null)
			return;

		if (r instanceof RoleGeneral) {

			if (getRolesGeneral() == null)
				setRolesGeneral(new HashSet<RoleGeneral>());

			if (!getRolesGeneral().contains((RoleGeneral) r))
				getRolesGeneral().add((RoleGeneral) r);

			return;
		}

		if (r instanceof RoleSite) {

			if (getRolesSite() == null)
				setRolesSite(new HashSet<RoleSite>());

			if (!getRolesSite().contains((RoleSite) r))
				getRolesSite().add((RoleSite) r);

			return;

		}

		if (r instanceof RoleInstitution) {

			if (getRolesInstitution() == null)
				setRolesInstitution(new HashSet<RoleInstitution>());

			if (!getRolesInstitution().contains((RoleInstitution) r))
				getRolesInstitution().add((RoleInstitution) r);

			return;

		}

		throw new IllegalArgumentException("role not supported -> " + r.getClass().getName());

	}

	public boolean isRoot() {
		return getUsername()!=null && getUsername().equals("root");
	}

	// public Set<SimpleGrantedAuthority> getRoles() {
	// return null;
	// }

}
