package dellemuse.server.db.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.model.ArtWorkModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

@Entity
@Table(name = "favorite")
@JsonInclude(Include.NON_NULL)
public class FavoriteSite extends DelleMuseObject {

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Site.class)
    @JoinColumn(name = "site_id", nullable = true, insertable = false)
    @JsonSerialize(using = DelleMuseListIdSerializer.class)
    @JsonManagedReference
    @JsonBackReference
    @JsonProperty("sites")
    private List<Site> sites;

    @OneToOne(fetch = FetchType.LAZY, targetEntity = Person.class)
    @JoinColumn(name = "person_id", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonProperty("person")
    @JsonSerialize(using = DelleMuseIdSerializer.class)
    private Person person;


    public FavoriteSite() {
    }


    @Override
    public ArtWorkModel model() {
        try {
            return (ArtWorkModel) getObjectMapper().readValue(getObjectMapper().writeValueAsString(this), ArtWorkModel.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

};
