package dellemuse.serverapp.serverdb.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.serverapp.page.PrefixUrl;
import dellemuse.serverapp.serverdb.model.record.TranslationRecord;
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
@Table(name = "floorRecord")
@JsonInclude(Include.NON_NULL)
public class FloorRecord extends TranslationRecord {
	
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Floor.class)
	@JoinColumn(name = "floor_id", referencedColumnName = "id", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonSerialize(using = DelleMuseIdNameSerializer.class)
    @JsonProperty("floor")
    private Floor floor;

	@Column(name = "infoAccessible")
	private String infoAccessible;
	
	@Column(name = "infoAccesible_hash")
	private int infoAccessibleHash;
	
	@OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
	@JoinColumn(name = "audioAccessible", nullable = true)
	@JsonManagedReference
	@JsonBackReference
	@JsonProperty("audioAccessible")
	@JsonSerialize(using = DelleMuseResourceSerializer.class)
	private Resource audioAccessible;
	
    
	@Override
	public String getObjectClassName() {
		return FloorRecord.class.getSimpleName();
	}


	@Override
	public String getPrefixUrl() {
		return PrefixUrl.FloorRecord;
	}


	@Override
	public MultiLanguageObject getParentObject() {
		return this.floor;
	}

	public Floor getFloor() {
		return floor;
	}

	public void setFloor(Floor floor) {
		this.floor = floor;
	}

	public String getInfoAccessible() {
		return infoAccessible;
	}

	public void setInfoAccessible(String infoAccessible) {
		this.infoAccessible = infoAccessible;
	}

	public int getInfoAccessibleHash() {
		return infoAccessibleHash;
	}

	public void setInfoAccessibleHash(int infoAccessibleHash) {
		this.infoAccessibleHash = infoAccessibleHash;
	}

	public Resource getAudioAccessible() {
		return audioAccessible;
	}

	public void setAudioAccessible(Resource audioAccessible) {
		this.audioAccessible = audioAccessible;
	}


	@Override
	public boolean isAudioStudioEnabled() {
		return false;
	}
 
};