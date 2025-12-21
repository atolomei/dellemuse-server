package dellemuse.serverapp.serverdb.model.record;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
 
import dellemuse.serverapp.serverdb.model.ArtExhibitionSection;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
 
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseIdNameSerializer;
 
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
 
import jakarta.persistence.Table;

@Entity
@Table(name = "artExhibitionSectionRecord")
@JsonInclude(Include.NON_NULL)
public class ArtExhibitionSectionRecord extends TranslationRecord {

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ArtExhibitionSection.class)
	@JoinColumn(name = "artExhibitionsection_id", referencedColumnName = "id", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	@JsonProperty("artExhibitionSection")
	private ArtExhibitionSection artExhibitionSection;

	public ArtExhibitionSectionRecord() {
	}

	@Override
	public String getObjectClassName() {
		return   ArtExhibitionSectionRecord.class.getSimpleName();
	}

	
	public ArtExhibitionSection getArtExhibitionSection() {
		return artExhibitionSection;
	}
 
	public void setArtExhibitionSection (ArtExhibitionSection  artExhibitionSection ) {
		this.artExhibitionSection = artExhibitionSection ;
	}
	 
	@Override
	public boolean isAudioStudioEnabled() {
		return false;
	}
	
	@Override
	public MultiLanguageObject getParentObject() {
		 return this.artExhibitionSection !=null? this.artExhibitionSection : null;
	 }

	@Override
	public String getPrefixUrl() {
			throw new RuntimeException("not done");
	}
};
