package dellemuse.server.db.model;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;

import dellemuse.model.PersonModel;
import dellemuse.model.ResourceModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "file")
@JsonInclude(Include.NON_NULL)
public class Resource extends DelleMuseObject {


    @Column(name = "bucket")
    private String bucket;

    @Column(name = "objectName")
    private String objectName;

    @Column(name = "info")
    private String info;

    @Column(name = "infoKey")
    private String infoKey;
    
    @Column(name = "media")
    private String media;

    // private String infoKey;
    // File file
    // InputStream
    // InputStream

    public Resource() {
    }

    
    
    @Override
    public ResourceModel model() {
        try {
            return (ResourceModel) getObjectMapper().readValue(getObjectMapper().writeValueAsString(this), ResourceModel.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    
    public java.io.File getFile() throws IOException {
        return null;
    }

    public InputStream getInputStream() throws IOException {

        return null;

//        try {
        // return getService(KBFSResourceService.class).getObject();
        // }
        // catch (FileServerException e) {
        // logger.error(e);
        // throw new IOException(e);
        // }
    }

};
