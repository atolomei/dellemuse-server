package dellemuse.serverapp.serverdb.model.record;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseIdNameSerializer;

import jakarta.persistence.Entity;
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
	@JsonProperty("artWork")
	private ArtWork artWork;

	public ArtWorkRecord() {
	}

	@Override
	public String getObjectClassName() {
		return   ArtWorkRecord.class.getSimpleName();
	}

	
	public ArtWork getArtWork() {
		return artWork;
	}

	public void setArtWork(ArtWork artwork) {
		this.artWork = artwork;
	}

	@Override
	public MultiLanguageObject getParentObject() {
		return this.artWork != null ? this.artWork : null;
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
