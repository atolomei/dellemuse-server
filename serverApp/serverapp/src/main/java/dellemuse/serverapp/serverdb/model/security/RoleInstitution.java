package dellemuse.serverapp.serverdb.model.security;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseIdNameSerializer;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseSetPersonSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "roleInstitution")
@JsonInclude(Include.NON_NULL)
public class RoleInstitution extends Role {
 	
	public static final String ADMIN = "admin";
 	public static final String AUDIT = "audit";

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Institution.class)
	@JoinColumn(name = "institution_id", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	private Institution institution;
	
	
	@ManyToMany(mappedBy = "rolesInstitution")
	@JsonSerialize(using = DelleMuseSetPersonSerializer.class)
	@JsonManagedReference
	@JsonProperty("users")
	private Set<User> users;

	public RoleInstitution() {
	}

	@Column(name = "key")
	private String key;
	
	public  String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key=key;
	}
	public Institution getInstitution() {
		return institution;
	}

	public void setInstitution(Institution institution) {
		this.institution = institution;
	}

	@Override
	public String getRoleDisplayName() {
		return ((getInstitution()!=null) ? (getInstitution().getName() + " - " ): "") + getName();
	}
	
	@Override
	public String getDisplayClass() {
		ResourceBundle res = ResourceBundle.getBundle(getClass().getName(), Locale.ENGLISH);
		return res.getString( "name");
	}
	
	@Override
	public String getDisplayClass(Locale locale) {
		ResourceBundle res = ResourceBundle.getBundle(getClass().getName(), locale);
		return res.getString( "name");
	}
	
	@Override
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@Override
	public boolean equals(Object o) {

		if (o==null)
			return false;
		
		if (this.getId()==null)
			return false;
	 
		if ((o instanceof RoleInstitution)) {
			
			if (((RoleInstitution) o).getId()==null)
					return false;
			
			return ((RoleInstitution) o).getId().equals(getId());
		}
		
		return false;
	}
};
