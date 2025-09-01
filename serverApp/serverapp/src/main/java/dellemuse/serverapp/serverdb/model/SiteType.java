package dellemuse.serverapp.serverdb.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;

import dellemuse.model.SiteModel;
import dellemuse.model.SiteTypeModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "siteType")
@JsonInclude(Include.NON_NULL)
public class SiteType extends DelleMuseObject {


    public SiteType() {

    }

/**
    @Override
    public SiteTypeModel model() {
        try {
            return (SiteTypeModel) getObjectMapper().readValue(getObjectMapper().writeValueAsString(this),SiteTypeModel.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    */
}
