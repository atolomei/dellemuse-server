package dellemuse.serverapp.serverdb.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.serverapp.audiostudio.AudioStudioParentObject;
import dellemuse.serverapp.page.PrefixUrl;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseIdNameSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * 
 * Delete ->
 * 
 * 
 * Delete / Restore
 * 
 * 
 * 
 * 
 * 
 * 
 */
@Entity
@Table(name = "guideContent")
@JsonInclude(Include.NON_NULL)
public class GuideContent extends MultiLanguageObject implements AudioStudioParentObject {

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ArtExhibitionGuide.class)
	@JoinColumn(name = "artExhibitionGuide_id", nullable = false, insertable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	@JsonProperty("artExhibitionGuide")
	private ArtExhibitionGuide artExhibitionGuide;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = ArtExhibitionItem.class)
	@JoinColumn(name = "artExhibitionItem_id", nullable = true, insertable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
	@JsonProperty("artExhibitionItem")
	private ArtExhibitionItem artExhibitionItem;

	@Column(name = "guideOrder")
	private int guideOrder;

	public GuideContent() {
	}

	public String getPrefixUrl() {
		return PrefixUrl.GuideContent;
	}

	public ArtExhibitionGuide getArtExhibitionGuide() {
		return artExhibitionGuide;
	}

	public void setArtExhibitionGuide(ArtExhibitionGuide artExhibitionGuide) {
		this.artExhibitionGuide = artExhibitionGuide;
	}

	public ArtExhibitionItem getArtExhibitionItem() {
		return artExhibitionItem;
	}

	public void setArtExhibitionItem(ArtExhibitionItem artExhibitionItem) {
		this.artExhibitionItem = artExhibitionItem;
	}

	public int getGuideOrder() {
		return guideOrder;
	}

	public void setGuideOrder(int guideOrder) {
		this.guideOrder = guideOrder;
	}

};
