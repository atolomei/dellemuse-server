package dellemuse.server.db.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.model.ArtExhibitionItemModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "artExhibitionItem")
@JsonInclude(Include.NON_NULL)
public class ArtExhibitionItem extends DelleMuseObject {

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = ArtWork.class)
    @JoinColumn(name = "artwork_id", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonSerialize(using = DelleMuseIdNameSerializer.class)
    private ArtWork artwork;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = ArtExhibition.class)
    @JoinColumn(name = "artExhibition_id", referencedColumnName = "id", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonSerialize(using = DelleMuseIdNameSerializer.class)
    private ArtExhibition artExhibition;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Site.class)
    @JoinColumn(name = "site_id", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonSerialize(using = DelleMuseIdNameSerializer.class)
    private Site site;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Floor.class)
    @JoinColumn(name = "floor_id", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonSerialize(using = DelleMuseIdNameSerializer.class)
    private Floor floor;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Room.class)
    @JoinColumn(name = "room_id", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonSerialize(using = DelleMuseIdNameSerializer.class)
    private Room room;

    @Column(name = "artExhibitionOrder")
    private int artExhibitionOrder;

    @Column(name = "readcode")
    private String readCode;

    @Column(name = "qcode")
    private String qCode;

    @Column(name = "title")
    private String title;

    @Column(name = "titleKey")
    private String titleKey;

    @Column(name = "info")
    private String info;

    @Column(name = "infoKey")
    private String infoKey;

    @Column(name = "mapurl")
    private String mapurl;

    @Column(name = "website")
    private String wesite;

    public ArtExhibitionItem() {
    }

    @Override
    public ArtExhibitionItemModel model() {
        try {
            return (ArtExhibitionItemModel) getObjectMapper().readValue(getObjectMapper().writeValueAsString(this),
                    ArtExhibitionItemModel.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public ArtWork getArtwork() {
        return artwork;
    }

    public void setArtwork(ArtWork artwork) {
        this.artwork = artwork;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getReadCode() {
        return readCode;
    }

    public void setReadCode(String readCode) {
        this.readCode = readCode;
    }

    public String getqCode() {
        return qCode;
    }

    public void setqCode(String qCode) {
        this.qCode = qCode;
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

    public ArtExhibition getArtExhibition() {
        return artExhibition;
    }

    public void setArtExhibition(ArtExhibition artExhibition) {
        this.artExhibition = artExhibition;
    }

    public String getMapurl() {
        return mapurl;
    }

    public void setMapurl(String mapurl) {
        this.mapurl = mapurl;
    }

    public String getWesite() {
        return wesite;
    }

    public void setWesite(String wesite) {
        this.wesite = wesite;
    }

    public int getExhibitionOrder() {
        return artExhibitionOrder;
    }

    public void setExhibitionOrder(int exhibitionOrder) {
        this.artExhibitionOrder = exhibitionOrder;
    }

};
