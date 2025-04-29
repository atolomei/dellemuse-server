package dellemuse.server.db.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.model.FloorModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

@Entity
@Table(name = "floor")
@JsonInclude(Include.NON_NULL)
public class Floor extends DelleMuseObject {

    @Column(name = "name")
    private String name;

    @Column(name = "nameKey")
    private String nameKey;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = FloorType.class)
    @JoinColumn(name = "floorType_id", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonSerialize(using = DelleMuseIdSerializer.class)
    private FloorType floorType;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Site.class)
    @JoinColumn(name = "site_id", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonSerialize(using = DelleMuseIdSerializer.class)
    private Site site;
    
    @OneToMany(fetch = FetchType.LAZY, targetEntity = Room.class)
    @JoinColumn(name = "floor_id", nullable = true, insertable=false)
    @JsonIgnore
    @OrderBy("roomNumber ASC")
    private List<Room> rooms;

    @Column(name = "title")
    private String title;

    @Column(name = "titleKey")
    private String titleKey;

    @Column(name = "subtitle")
    private String subtitle;

    @Column(name = "subTitleKey")
    private String subTitleKey;
    
    @Column(name = "floornumber")
    private String floorNumber;

    @Column(name = "floornumberkey")
    private String floorNumberKey;

    @Column(name = "info")
    private String info;

    @Column(name = "infoKey")
    private String infoKey;

    public Floor() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameKey() {
        return nameKey;
    }

    public void setNameKey(String nameKey) {
        this.nameKey = nameKey;
    }

    public FloorType getFloorType() {
        return floorType;
    }

    public void setFloorType(FloorType floorType) {
        this.floorType = floorType;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleKey() {
        return titleKey;
    }

    public void setTitleKey(String titleKey) {
        this.titleKey = titleKey;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getSubTitleKey() {
        return subTitleKey;
    }

    public void setSubTitleKey(String subTitleKey) {
        this.subTitleKey = subTitleKey;
    }

    public String getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(String floorNumber) {
        this.floorNumber = floorNumber;
    }

    public String getFloorNumberKey() {
        return floorNumberKey;
    }

    public void setFloorNumberKey(String floorNumberKey) {
        this.floorNumberKey = floorNumberKey;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfoKey() {
        return infoKey;
    }

    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }

    @Override
    public FloorModel model() {
        try {
            return (FloorModel) getObjectMapper().readValue(getObjectMapper().writeValueAsString(this), FloorModel.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

};
