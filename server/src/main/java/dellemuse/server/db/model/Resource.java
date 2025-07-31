package dellemuse.server.db.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "resource")
@JsonInclude(Include.NON_NULL)
public class Resource extends DelleMuseObject {

    @Column(name = "bucketName")
    private String bucketName;

    @Column(name = "objectName")
    private String objectName;

    @Column(name = "media")
    private String media;

    @Column(name = "info")
    private String info;

    @Column(name = "infoKey")
    private String infoKey;
    
    @Column(name = "usethumbnail")
    @JsonProperty("usethumbnail")
    private boolean usethumbnail;
   

    public Resource() {
    }
    
    @Override
    public String getDisplayname() {
        return getName();
    }
  
    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucket) {
        this.bucketName = bucket;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
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



    public String getMedia() {
        return media;
    }



    public void setMedia(String media) {
        this.media = media;
    }

	public boolean isUsethumbnail() {
		return usethumbnail;
	}

	public void setUsethumbnail(boolean usethumbnail) {
		this.usethumbnail = usethumbnail;
	}
};
