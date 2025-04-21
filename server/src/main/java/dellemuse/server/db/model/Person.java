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
@Table(name = "person")
public class Person extends DelleMuseObject {

    @Column(name="name")
    private String name;

    @Column(name="namekey")
    private String nameKey;
    
    @Column(name="lastname")
    private String lastname;

    @Column(name="lastnamekey")
    private String lastnameKey;
    
    @Column(name="nickname")
    private String nickname;
    
    @Column(name="sex")
    private String sex;
    
    @Column(name="physicalid")
    private String physicalid;

    @Column(name="address")
    private String address;

    @Column(name="zipcode")
    private String zipcode;

    @Column(name="phone")
    private String phone;
    
    @Column(name="email")
    private String email;


    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name = "user_id", nullable=true) 
    @JsonManagedReference
    @JsonBackReference
    @JsonIgnore
    private User user;
    
    @JsonProperty("userId")
    public Optional<Long> getUserId() {
        if (user!=null)
            return Optional.of(user.getId());
        return Optional.empty();
    }

    @Column(name="title")
    private String title;         

    @Column(name="titleKey")
    private String titleKey;      

    @Column(name="subtitle")
    private String subtitle;      
    
    @Column(name="subTitleKey")
    private String subTitleKey;   

    @Column(name="info")
    private String info; 

    @Column(name="infokey")
    private String infoKey;
    
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

    
    public Person() {
        
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


    public String getLastname() {
        return lastname;
    }


    public void setLastname(String lastname) {
        this.lastname = lastname;
    }


    public String getLastnameKey() {
        return lastnameKey;
    }


    public void setLastnameKey(String lastnameKey) {
        this.lastnameKey = lastnameKey;
    }


    public String getNickname() {
        return nickname;
    }


    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    public String getSex() {
        return sex;
    }


    public void setSex(String sex) {
        this.sex = sex;
    }


    public String getPhysicalid() {
        return physicalid;
    }


    public void setPhysicalid(String physicalid) {
        this.physicalid = physicalid;
    }


    public String getAddress() {
        return address;
    }


    public void setAddress(String address) {
        this.address = address;
    }


    public String getZipcode() {
        return zipcode;
    }


    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }


    public String getPhone() {
        return phone;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
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


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}





  

