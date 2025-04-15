package dellemuse.server.db.model;

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
@Table(name = "artwork")
public class ArtWork extends DelleMuseObject {

    @Column(name="name")
    private String name;

    @Column(name="nameKey")
    private String nameKey;
    
    @Column(name="artworkType_id")
    @OneToOne(fetch = FetchType.LAZY, targetEntity = ArtWorkType.class)
    @JoinColumn(name = "artworkType_id", nullable=true) 
    @JsonManagedReference
    @JsonBackReference
    @JsonIgnore
    private ArtWorkType artworkType;

    
    @Column(name="title")
    private    String title;
    
    @Column(name="titleKey")
    private    String  titleKey;

    @Column(name="subtitle")
    private    String subtitle;
    
    @Column(name="subTitleKey")
    private    String subTitleKey;

    @Column(name="info")
    private    String info;
    
    @Column(name="infoKey")
    private    String infoKey; 


    //@Column(name="created")
    //photo               bytea,
    
    @Column(name="photoKey")
    String  photoKey;
    
    //@Column(name="created")
    //video               bytea,
    
    @Column(name="videoKey")
    String videoKey;
    
    //@Column(name="created")
    //audio               bytea,
    
    @Column(name="audioKey")
    String  audioKey;
    
};    

