package dellemuse.serverapp.serverdb.model.record;
 
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseIdNameSerializer;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "artExhibitionRecord")
//@EntityListeners(ArtWorkEventListener.class)
@JsonInclude(Include.NON_NULL)

public class ArtExhibitionRecord extends TranslationRecord {
 	 
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ArtExhibition.class)
	@JoinColumn(name = "artExhibition_id", nullable = false)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	@JsonProperty("artExhibition")
	private ArtExhibition artExhibition;
	
	public ArtExhibitionRecord() {
	}

	public ArtExhibition getExhibition() {
		return artExhibition;
	}

	public void setExhibition(ArtExhibition artExhibition) {
		this.artExhibition = artExhibition;
	}
  
	public ArtExhibition getArtExhibition() {
		return artExhibition;
	}

	public void setArtExhibition(ArtExhibition artExhibition) {
		this.artExhibition = artExhibition;
	}
	
	@Override
	public MultiLanguageObject getParentObject() {
		 return this.artExhibition !=null? this.artExhibition : null;
	 }
	
	@Override
	public boolean isAudioStudioEnabled() {
		return false;
	}
	
	@Override
	public String getPrefixUrl() {
		throw new RuntimeException("not done");
	}
	
};
