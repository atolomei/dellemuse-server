package dellemuse.serverapp.serverdb.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.serverapp.serverdb.model.serializer.DelleMuseIdSerializer;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseListIdSerializer;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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

    @OneToOne(fetch = FetchType.EAGER, targetEntity = Person.class)
    @JoinColumn(name = "person_id", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonProperty("person")
    @JsonSerialize(using = DelleMuseIdSerializer.class)
    private Person person;

    public FavoriteSite() {
    }

};
