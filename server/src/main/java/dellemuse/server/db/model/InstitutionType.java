package dellemuse.server.db.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;

import dellemuse.model.InstitutionTypeModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "institutionType")
@JsonInclude(Include.NON_NULL)
public class InstitutionType extends DelleMuseObject {

    @Column(name = "name")
    private String name;

    @Column(name = "nameKey")
    private String nameKey;

    public InstitutionType() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameKey() {
        return nameKey;
    }

    public void setNameKey(String nameKey) {
        this.nameKey = nameKey;
    }

    @Override
    public InstitutionTypeModel model() {
        try {
            return (InstitutionTypeModel) getObjectMapper().readValue(getObjectMapper().writeValueAsString(this), InstitutionTypeModel.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
