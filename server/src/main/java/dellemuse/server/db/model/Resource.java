package dellemuse.server.db.model;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;

import dellemuse.model.ResourceModel;
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
    
    // File file
    // InputStream
    // InputStream

    public Resource() {
    }
    
    @Override
    public String getDisplayname() {
        return getName();
    }
    
    @Override
    public ResourceModel model() {
        try {
            return (ResourceModel) getObjectMapper().readValue(getObjectMapper().writeValueAsString(this), ResourceModel.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    
    
    //public java.io.File getFile() throws IOException {
    //    return null;
    //}

    //public InputStream getInputStream() throws IOException {

     //   return null;

//        try {
        // return getService(KBFSResourceService.class).getObject();
        // }
        // catch (FileServerException e) {
        // logger.error(e);
        // throw new IOException(e);
        // }
    //}


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

};
