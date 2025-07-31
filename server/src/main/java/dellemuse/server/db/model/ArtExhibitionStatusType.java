package dellemuse.server.db.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;

import dellemuse.model.ArtExhibitionStatusTypeModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "artExhibitionStatusType")
@JsonInclude(Include.NON_NULL)
public class ArtExhibitionStatusType extends DelleMuseObject {

    public ArtExhibitionStatusType() {
    }

    /**
    @Override
    public ArtExhibitionStatusTypeModel model() {
        try {
            return (ArtExhibitionStatusTypeModel) getObjectMapper().readValue(getObjectMapper().writeValueAsString(this), ArtExhibitionStatusTypeModel.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    **/
}
