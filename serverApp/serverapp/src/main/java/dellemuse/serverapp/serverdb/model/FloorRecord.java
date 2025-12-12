package dellemuse.serverapp.serverdb.model;

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
@Table(name = "floorRecord")
@JsonInclude(Include.NON_NULL)
public class FloorRecord extends DelleMuseObject {

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = ArtExhibitionGuide.class)
    @JoinColumn(name = "floor_id", referencedColumnName = "id", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonSerialize(using = DelleMuseIdNameSerializer.class)
    @JsonProperty("floor")
    private Floor floor;
	
    @Column(name = "subtitle")
    private String subtitle;

    @Column(name = "floornumber")
    private String floorNumber;

    @Column(name = "info")
    private String info;

    @OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
    @JoinColumn(name = "photo", nullable = true)
    @JsonBackReference
    @JsonProperty("photo")
    @JsonSerialize(using = DelleMuseResourceSerializer.class)
    private Resource photo;

    @OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
    @JoinColumn(name = "video", nullable = true)
    @JsonBackReference
    @JsonProperty("video")
    @JsonSerialize(using = DelleMuseResourceSerializer.class)
    private Resource video;

    @OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
    @JoinColumn(name = "audio", nullable = true)
    @JsonBackReference
    @JsonProperty("audio")
    @JsonSerialize(using = DelleMuseResourceSerializer.class)
    private Resource audio;

    @Column(name = "language")
	private String language;
	
	public String getLanguage() {
		return this.language;
	}

	public void setLanguage(String lang) {
		language = lang;
	}

    public FloorRecord() {
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(String floorNumber) {
        this.floorNumber = floorNumber;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
  
};
