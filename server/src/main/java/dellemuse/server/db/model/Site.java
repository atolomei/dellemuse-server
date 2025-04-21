package dellemuse.server.db.model;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Site")
public class Site extends DelleMuseObject {

    @Column(name = "name")
    private String name;

    @Column(name = "nameKey")
    private String nameKey;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = SiteType.class)
    @JoinColumn(name = "siteType_id", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonIgnore
    private SiteType siteType;

    @JsonProperty("siteTypeId")
    public Optional<Long> getSiteTypeId() {
        if (siteType!=null)
            return Optional.of(siteType.getId());
        return Optional.empty();
    }

    
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Institution.class)
    @JoinColumn(name = "institution_id", nullable = true)
    @JsonManagedReference
    @JsonBackReference
    @JsonIgnore
    private Institution institution;
    
    @JsonProperty("institutionId")
    public Optional<Long> getInstitutionId() {
        if (institution!=null)
            return Optional.of(institution.getId());
        return Optional.empty();
    }
    
    @Column(name = "title")
    private String title;

    @Column(name = "titleKey")
    private String titleKey;

    @Column(name = "subtitle")
    private String subtitle;

    @Column(name = "subTitleKey")
    private String subTitleKey;

    @Column(name = "info")
    private String info;

    @Column(name = "infoKey")
    private String infoKey;

    @Column(name = "address")
    private String address;

    @Column(name = "addressKey")
    private String addressKey;



    @OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
    @JoinColumn(name = "photo", nullable=true) 
    @JsonManagedReference
    @JsonBackReference
    @JsonIgnore
    private Resource photo;
        
    @OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
    @JoinColumn(name = "video", nullable=true) 
    @JsonManagedReference
    @JsonBackReference
    @JsonIgnore
    private Resource video;

    @OneToOne(fetch = FetchType.LAZY, targetEntity = Resource.class)
    @JoinColumn(name = "audio", nullable=true) 
    @JsonManagedReference
    @JsonBackReference
    @JsonIgnore
    private Resource audio;

    
    public Site() {
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



};
