package dellemuse.serverapp.serverdb.model;

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

   
}
