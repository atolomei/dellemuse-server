package dellemuse.server.db.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;

import dellemuse.model.ArtExhibitionModel;
import dellemuse.model.ArtExhibitionStatusTypeModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "artExhibitionStatusType")
@JsonInclude(Include.NON_NULL)
public class ArtExhibitionStatusType extends DelleMuseObject {

    @Column(name="name")
    private String name;
    
    @Column(name="nameKey")
    private String nameKey;
    
    public ArtExhibitionStatusType() {
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
    public ArtExhibitionStatusTypeModel model() {
        try {
            return (ArtExhibitionStatusTypeModel) getObjectMapper().readValue(getObjectMapper().writeValueAsString(this), ArtExhibitionStatusTypeModel.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
