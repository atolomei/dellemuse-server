package dellemuse.serverdb.model;

import com.fasterxml.jackson.core.JsonProcessingException;

import dellemuse.model.RoomModel;
import dellemuse.model.RoomTypeModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "roomType")
public class RoomType extends DelleMuseObject {


    public RoomType() {
    }


    /**
    @Override
    public RoomTypeModel model() {
        try {
            return (RoomTypeModel) getObjectMapper().readValue(getObjectMapper().writeValueAsString(this),RoomTypeModel.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    **/

}
