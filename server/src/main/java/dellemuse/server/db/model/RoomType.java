package dellemuse.server.db.model;

import com.fasterxml.jackson.core.JsonProcessingException;

import dellemuse.model.RoomModel;
import dellemuse.model.RoomTypeModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "roomType")
public class RoomType extends DelleMuseObject {

    @Column(name = "name")
    private String name;

    @Column(name = "nameKey")
    private String nameKey;

    public RoomType() {
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
    public RoomTypeModel model() {
        try {
            return (RoomTypeModel) getObjectMapper().readValue(getObjectMapper().writeValueAsString(this),RoomTypeModel.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
