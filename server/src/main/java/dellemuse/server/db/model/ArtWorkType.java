package dellemuse.server.db.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;

import dellemuse.model.ArtWorkTypeModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "artWorkType")
@JsonInclude(Include.NON_NULL)
public class ArtWorkType extends DelleMuseObject {


    public ArtWorkType() {
    }

    
    @Override
    public  ArtWorkTypeModel model() {
        try {
            return ( ArtWorkTypeModel) getObjectMapper().readValue(getObjectMapper().writeValueAsString(this),  ArtWorkTypeModel.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
