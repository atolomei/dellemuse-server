package dellemuse.serverapp.serverdb.model;

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
   
    @Column(name = "size")
    @JsonProperty("size")
    private long size;

    @Column(name = "width")
    @JsonProperty("width")
    private long width;

    @Column(name = "height")
    @JsonProperty("height")
    private long height;
 
    
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

    public long getSize() {
    	return size;
    }
    
    public void setSize(long size) {
    	this.size=size;
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

	public long getWidth() {
		return width;
	}

	public long getHeight() {
		return height;
	}

	public void setWidth(long width) {
		this.width = width;
	}

	public void setHeight(long height) {
		this.height = height;
	}


};
