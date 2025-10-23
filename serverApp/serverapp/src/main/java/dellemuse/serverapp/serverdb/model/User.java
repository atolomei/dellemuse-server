package dellemuse.serverapp.serverdb.model;

import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
@JsonInclude(Include.NON_NULL)
public class User extends DelleMuseObject {

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
 
}
