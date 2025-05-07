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

    public InstitutionType() {
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
