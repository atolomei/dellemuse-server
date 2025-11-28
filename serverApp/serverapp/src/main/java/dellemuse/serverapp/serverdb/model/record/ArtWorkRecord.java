package dellemuse.serverapp.serverdb.model.record;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

 
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseIdNameSerializer;
 
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "artWorkRecord")
//@EntityListeners(ArtWorkEventListener.class)
@JsonInclude(Include.NON_NULL)

public class ArtWorkRecord extends TranslationRecord {

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ArtWork.class)
	@JoinColumn(name = "artwork_id", nullable = false)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	@JsonProperty("artwork")
	private ArtWork artwork;
 
	public ArtWorkRecord() {
	}

	public ArtWork getArtwork() {
		return artwork;
	}

	public void setArtwork(ArtWork artwork) {
		this.artwork = artwork;
	}

	@Override
	public MultiLanguageObject getParentObject() {
		 return this.artwork !=null? this.artwork : null;
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
