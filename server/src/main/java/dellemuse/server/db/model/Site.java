package dellemuse.server.db.model;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dellemuse.model.SiteModel;
import dellemuse.server.db.model.serializer.DelleMuseIdNameSerializer;
import dellemuse.server.db.model.serializer.DelleMuseIdSerializer;
import dellemuse.server.db.model.serializer.DelleMuseListIdNameSerializer;
import dellemuse.server.db.model.serializer.DelleMuseResourceSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

/**
 * 
 * Indexes are -> lower( title )
 * 
 */
@Entity
@Table(name = "Site")
@JsonInclude(Include.NON_NULL)
public class Site extends DelleMuseObject {

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = SiteType.class)
    @JoinColumn(name = "siteType_id", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonSerialize(using = DelleMuseIdNameSerializer.class)
    private SiteType siteType;

    @JsonProperty("siteTypeId")
    public Optional<Long> getSiteTypeId() {
        if (siteType != null)
            return Optional.of(siteType.getId());
        return Optional.empty();
    }

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Institution.class)
    @JoinColumn(name = "institution_id", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonSerialize(using = DelleMuseIdNameSerializer.class)
    private Institution institution;

    @Column(name = "shortName")
    private String shortName;

    @Column(name = "subtitle")
    private String subtitle;

    @Column(name = "subtitleKey")
    private String subTitleKey;

    @Column(name = "info")
    private String info;

    @Column(name = "intro")
    private String intro;
    
    @Column(name = "introKey")
    private String introKey;

    
    @Column(name = "abstract")
    private String siteAbstract;

    
    @Column(name = "infoKey")
    private String infoKey;

    @Column(name = "address")
    private String address;

    @Column(name = "addressKey")
    private String addressKey;

    @Column(name = "website")
    private String website;

    @Column(name = "mapurl")
    private String mapurl;

    @Column(name = "email")
    private String email;

    @Column(name = "instagram")
    private String instagram;

    @Column(name = "whatsapp")
    private String whatsapp;

    @Column(name = "opens")
    private String opens;
    
    @Column(name = "phone")
    private String phone;

    @Column(name = "twitter")
    private String twitter;

    @OneToMany(fetch = FetchType.EAGER, targetEntity = Floor.class)
    @JoinColumn(name = "site_id", nullable = true, insertable = false)
    @OrderBy("floorNumber ASC")
    @JsonSerialize(using = DelleMuseListIdNameSerializer.class)
    private List<Floor> floors;

    @OneToOne(fetch = FetchType.EAGER, targetEntity = Resource.class)
    @JoinColumn(name = "logo", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonProperty("logo")
    @JsonSerialize(using = DelleMuseResourceSerializer.class)
    private Resource logo;

    @OneToOne(fetch = FetchType.EAGER, targetEntity = Resource.class)
    @JoinColumn(name = "photo", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonProperty("photo")
    @JsonSerialize(using = DelleMuseResourceSerializer.class)
    private Resource photo;

    @OneToOne(fetch = FetchType.EAGER, targetEntity = Resource.class)
    @JoinColumn(name = "video", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonProperty("video")
    @JsonSerialize(using = DelleMuseResourceSerializer.class)
    private Resource video;

    @OneToOne(fetch = FetchType.EAGER, targetEntity = Resource.class)
    @JoinColumn(name = "audio", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonProperty("audio")
    @JsonSerialize(using = DelleMuseResourceSerializer.class)
    private Resource audio;

    @OneToOne(fetch = FetchType.EAGER, targetEntity = Resource.class)
    @JoinColumn(name = "map", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonProperty("map")
    @JsonSerialize(using = DelleMuseResourceSerializer.class)
    private Resource map;

    public Site() {
    }

    @Override
    public String getDisplayname() {
        return getName();
    }

    @Override
    public SiteModel model() {
        try {
            return (SiteModel) getObjectMapper().readValue(getObjectMapper().writeValueAsString(this), SiteModel.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Resource getLogo() {
        return logo;
    }

    public void setLogo(Resource logo) {
        this.logo = logo;
    }

    public SiteType getSiteType() {
        return siteType;
    }

    public void setSiteType(SiteType siteType) {
        this.siteType = siteType;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
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

    public String getInfo() {
        return info;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getMapurl() {
        return mapurl;
    }

    public void setMapurl(String mapurl) {
        this.mapurl = mapurl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSiteAbstract() {
        return siteAbstract;
    }

    public void setSiteAbstract(String siteAbstract) {
        this.siteAbstract = siteAbstract;
    }

    public String getOpens() {
        return opens;
    }

    public void setOpens(String opens) {
        this.opens = opens;
    }

    public Resource getMap() {
        return map;
    }

    public void setMap(Resource map) {
        this.map = map;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public Resource getPhoto() {
        return photo;
    }

    public void setPhoto(Resource photo) {
        this.photo = photo;
    }

    public Resource getVideo() {
        return video;
    }

    public void setVideo(Resource video) {
        this.video = video;
    }

    public Resource getAudio() {
        return audio;
    }

    public void setAudio(Resource audio) {
        this.audio = audio;
    }

    public void setFloors(List<Floor> floors) {
        this.floors = floors;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String welcome) {
        this.intro = welcome;
    }

    public String getInfoKey() {
        return infoKey;
    }

    public void setInfoKey(String infoKey) {
        this.infoKey = infoKey;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressKey() {
        return addressKey;
    }

    public void setAddressKey(String addressKey) {
        this.addressKey = addressKey;
    }

    public List<Floor> getFloors() {
        return floors;
    }

};
