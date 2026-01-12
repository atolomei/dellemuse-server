package dellemuse.serverapp.serverdb.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.serverapp.page.PrefixUrl;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseIdNameSerializer;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.Table;

@Entity
@Table(name = "artExhibitionSection")
@JsonInclude(Include.NON_NULL)
public class ArtExhibitionSection extends MultiLanguageObject {

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ArtExhibition.class)
	@JoinColumn(name = "artExhibition_id", referencedColumnName = "id", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	@JsonProperty("artExhibition")
	private ArtExhibition artExhibition;

	public ArtExhibitionSection() {
	}

	@Override
	public String getObjectClassName() {
		return ArtExhibitionSection.class.getSimpleName();
	}

	public String getPrefixUrl() {
		return PrefixUrl.ArtExhibitionSection;
	}

	public ArtExhibition getArtExhibition() {
		return artExhibition;
	}

	public void setArtExhibition(ArtExhibition artExhibition) {
		this.artExhibition = artExhibition;
	}

};
