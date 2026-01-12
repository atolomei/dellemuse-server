package dellemuse.serverapp.serverdb.model.security;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseSetPersonSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "roleGeneral")
@JsonInclude(Include.NON_NULL)
public class RoleGeneral extends Role {

	public static final String ADMIN = "admin";
	public static final String AUDIT = "audit";

	@ManyToMany(mappedBy = "rolesGeneral")
	@JsonSerialize(using = DelleMuseSetPersonSerializer.class)
	@JsonManagedReference
	@JsonProperty("users")
	private Set<User> users;

	@Column(name = "key")
	private String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public RoleGeneral() {
	}

	public String getRoleDisplayName() {
		return getName();
	}

	@Override
	public String getDisplayClass() {
		ResourceBundle res = ResourceBundle.getBundle(getClass().getName(), Locale.ENGLISH);
		return res.getString("name");
	}

	@Override
	public String getDisplayClass(Locale locale) {
		ResourceBundle res = ResourceBundle.getBundle(getClass().getName(), locale);
		return res.getString("name");
	}

	@Override
	public boolean equals(Object o) {

		if (o == null)
			return false;

		if (this == o)
			return true;

		if (!(o instanceof RoleGeneral))
			return false;

		if (this.getId() == null)
			return false;

		if ((o instanceof RoleGeneral)) {
			if (((RoleGeneral) o).getId() == null)
				return false;
			return ((RoleGeneral) o).getId().equals(getId());
		}

		return false;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

};
