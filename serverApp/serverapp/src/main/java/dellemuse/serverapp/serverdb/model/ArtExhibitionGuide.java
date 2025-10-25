package dellemuse.serverapp.serverdb.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.model.ArtExhibitionGuideModel;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseIdNameSerializer;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseListIdNameSerializer;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseResourceSerializer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

@Entity
@Table(name = "artExhibitionGuide")
@JsonInclude(Include.NON_NULL)
public class ArtExhibitionGuide extends MultiLanguageObject {

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = ArtExhibition.class)
    @JoinColumn(name = "artExhibition_id", referencedColumnName = "id", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonSerialize(using = DelleMuseIdNameSerializer.class)
    @JsonProperty("artExhibition")
    private ArtExhibition artExhibition;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Person.class)
    @JoinColumn(name = "publisher_id", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonProperty("publisher")
    @JsonSerialize(using = DelleMuseIdNameSerializer.class)
    private Person publisher;

    @OneToMany(fetch = FetchType.LAZY, targetEntity = GuideContent.class, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "artExhibitionGuide_id", nullable = true, insertable = true)
    @JsonSerialize(using = DelleMuseListIdNameSerializer.class)
    @OrderBy("lower(name) ASC")
    @JsonManagedReference
    @JsonBackReference
    @JsonProperty("contents")
    private List<GuideContent> contents;

    @Column(name = "artExhibitionGuideOrder")
    private int artExhibitionGuideOrder;

    @Column(name = "official")
    private boolean official;
 
     

    public ArtExhibitionGuide() {
    }

    public ArtExhibition getArtExhibition() {
        return artExhibition;
    }

    public void setArtExhibition(ArtExhibition artExhibition) {
        this.artExhibition = artExhibition;
    }

    public Person getPublisher() {
        return publisher;
    }

    public void setPublisher(Person publisher) {
        this.publisher = publisher;
    }

    public List<GuideContent> getGuideContents() {
        return contents;
    }

    public void setContents(List<GuideContent> contents) {
        this.contents = contents;
    }

    public boolean isOfficial() {
        return official;
    }

    public void setOfficial(boolean official) {
        this.official = official;
    }
 

    public int getArtExhibitionGuideOrder() {
        return artExhibitionGuideOrder;
    }

    public void setArtExhibitionGuideOrder(int artExhibitionGuideOrder) {
        this.artExhibitionGuideOrder = artExhibitionGuideOrder;
    }

    
};
