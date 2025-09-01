package dellemuse.serverdb.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;

import dellemuse.model.FloorTypeModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "floorType")
@JsonInclude(Include.NON_NULL)
public class FloorType extends DelleMuseObject {

 
    public FloorType() {
    }

    /**
    @Override
    public FloorTypeModel model() {
        try {
            return (FloorTypeModel) getObjectMapper().readValue(getObjectMapper().writeValueAsString(this), FloorTypeModel.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    **/
    
}
