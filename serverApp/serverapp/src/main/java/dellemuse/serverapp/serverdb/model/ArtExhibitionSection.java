package dellemuse.serverapp.serverdb.model;

import java.time.OffsetDateTime;
import java.util.List;

import org.apache.wicket.Component;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.serverapp.serverdb.model.serializer.DelleMuseIdNameSerializer;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseResourceSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
 
	public ArtExhibition getArtExhibition() {
		return artExhibition;
	}

	public void setArtExhibition(ArtExhibition artExhibition) {
		this.artExhibition = artExhibition;
	}
 

};
