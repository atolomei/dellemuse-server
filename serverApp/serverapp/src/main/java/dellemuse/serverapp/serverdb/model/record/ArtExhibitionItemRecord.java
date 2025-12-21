package dellemuse.serverapp.serverdb.model.record;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseIdNameSerializer;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "artExhibitionItemRecord")
@JsonInclude(Include.NON_NULL)
public class ArtExhibitionItemRecord extends TranslationRecord {

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ArtExhibitionItem.class)
	@JoinColumn(name = "artExhibitionItem_id", referencedColumnName = "id", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	@JsonProperty("artExhibitionItem")
	private ArtExhibitionItem artExhibitionItem;

	public ArtExhibitionItemRecord() {
	}

	@Override
	public String getObjectClassName() {
		return   ArtExhibitionItemRecord.class.getSimpleName();
	}
	
	public ArtExhibitionItem getArtExhibitionItem() {
		return artExhibitionItem;
	}

	public void setArtExhibitionItem(ArtExhibitionItem artExhibitionItem) {
		this.artExhibitionItem = artExhibitionItem;
	}

	@Override
	public MultiLanguageObject getParentObject() {
		return this.artExhibitionItem != null ? this.artExhibitionItem : null;
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
