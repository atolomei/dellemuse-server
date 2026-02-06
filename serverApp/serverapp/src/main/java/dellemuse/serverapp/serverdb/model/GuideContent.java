package dellemuse.serverapp.serverdb.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.serverapp.audiostudio.AudioStudioParentObject;
import dellemuse.serverapp.icons.Icons;
import dellemuse.serverapp.page.PrefixUrl;
 
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseIdNameSerializer;
import dellemuse.serverapp.serverdb.model.serializer.DelleMuseResourceSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 *  
 *  ExhibitionGuide 
 *  ExhibitionItem
 *  ExhibitionSection
 *  Exhibition....
 *  
 *  
 *  Standard
 *  Accesible only
 *  
 *  
 *  primaryAccessibleContent
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
	
	@Column(name = "infoAccessible")
	private String infoAccessible;

	
	/**
	 * true -> la version Accesible solo muestra este content
	 * false -> la version accesible muestra el primary y este como complementario
	 */
	@Column(name = "infoAccessibleIsPrimary")
	private boolean infoAccessibleIsPrimary;

	
	/**
	 * true -> este GuideContent se lista unicamente en la version Accesible
	 * false -> este GuideContent se lista en la version Standard y en la Accesible
	 */
	@Column(name = "onlyAccesibleVersion")
	private boolean onlyAccesibleVersion;

	
	@OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
	@JoinColumn(name = "audioAccessible", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonProperty("audioAccessible")
	@JsonSerialize(using = DelleMuseResourceSerializer.class)
	private Resource audioAccessible;
	
	
	
	/**
	 * 
	 * CREATE SEQUENCE if not exists audio_id START 1; alter table
	 * artexhibitionguide add column audio_id bigint; alter table guidecontent add
	 * column audio_id bigint;
	 * 
	 */
	@Column(name = "audio_id")
	private Long audioId;

	public GuideContent() {
	}

	@Override
	public String getObjectClassName() {
		return GuideContent.class.getSimpleName();
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

	public Long getAudioId() {
		return audioId;
	}

	public void setAudioId(Long audioId) {
		this.audioId = audioId;
	}
	
	@Override
	public boolean equals(Object o) {

		if (o==null)
			return false;
		 
		if (this == o) return true;

		if (!(o instanceof GuideContent)) return false;
		 
		if (this.getId()==null)
			return false;
	 
		if ((o instanceof GuideContent)) {
			
			if (((GuideContent) o).getId()==null)
					return false;
			
			return ((GuideContent) o).getId().equals(getId());
		}
		
		return false;
	}
	
	
	public static String getIcon() {
		return Icons.GuideContent;
	}

	public void setInfoAccessible(String infoAccesible) {
		this.infoAccessible = infoAccesible;
	}

	public String getInfoAccessible() {
		return infoAccessible;
	}

	
	
	public boolean isInfoAccessibleIsPrimary() {
		return infoAccessibleIsPrimary;
	}

	public boolean isOnlyAccessibleVersion() {
		return onlyAccesibleVersion;
	}

	public Resource getAudioAccessible() {
		return audioAccessible;
	}


	public void setInfoAccessibleIsPrimary(boolean infoAccessibleIsPrimary) {
		this.infoAccessibleIsPrimary = infoAccessibleIsPrimary;
	}

	public void setOnlyAccessibleVersion(boolean onlyAccesibleVersion) {
		this.onlyAccesibleVersion = onlyAccesibleVersion;
	}

	public void setAudioAccessible(Resource audioAccesible) {
		this.audioAccessible = audioAccesible;
	}
	
};
