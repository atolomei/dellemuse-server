package dellemuse.serverapp.serverdb.model.record;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseIdNameSerializer;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "artExhibitionGuideRecord")
@JsonInclude(Include.NON_NULL)
public class ArtExhibitionGuideRecord extends TranslationRecord {

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = ArtExhibitionGuide.class)
    @JoinColumn(name = "artExhibitionGuide_id", referencedColumnName = "id", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonSerialize(using = DelleMuseIdNameSerializer.class)
    @JsonProperty("artExhibitionGuide")
    private ArtExhibitionGuide artExhibitionGuide;

    
    
   	public ArtExhibitionGuideRecord() {
    }

    public ArtExhibitionGuide getArtExhibitionGuide() {
        return artExhibitionGuide;
    }

    public void setArtExhibitionGuide(ArtExhibitionGuide artExhibitionGuide) {
        this.artExhibitionGuide = artExhibitionGuide;
    }

	@Override
	public MultiLanguageObject getParentObject() {
    	if (this.artExhibitionGuide!=null)
    		return this.artExhibitionGuide;
    	return null;
    }
	
};
