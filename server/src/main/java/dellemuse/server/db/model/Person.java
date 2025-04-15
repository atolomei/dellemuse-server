package dellemuse.server.db.model;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "person")
public class Person extends DelleMuseObject {

    @Column(name="name")
    private String name;

    @Column(name="lastname")
    private String lastname;
    
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

    @Column(name="timestamp")
    private OffsetDateTime timestamp;
    
    @Column(name="user_id")
    @OneToOne(fetch = FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name = "user_id", nullable=true) 
    @JsonManagedReference
    @JsonBackReference
    @JsonIgnore
    private User user;

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
    
    //private String photo;               bytea,

    @Column(name="photokey")
    private String photoKey; 
    
    //private String video;               bytea,

    @Column(name="videokey")
    private String videoKey; 
    
    //private String audio;               bytea,
    @Column(name="audiokey")
    private String audioKey;  
    
}





  

