package dellemuse.serverdb.model;

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

    /**
      
    @Override
    public UserModel model() {
        try {
            return (UserModel) getObjectMapper().readValue(getObjectMapper().writeValueAsString(this),UserModel.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    */
}
