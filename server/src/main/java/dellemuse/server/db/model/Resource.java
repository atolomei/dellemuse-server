package dellemuse.server.db.model;

import java.io.IOException;
import java.io.InputStream;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "file")
public class Resource extends DelleMuseObject {

    @Column(name = "name")
    private String name;

    @Column(name = "nameKey")
    private String nameKey;

    @Column(name = "bucket")
    private String bucket;

    @Column(name = "objectName")
    private String objectName;

    @Column(name = "info")
    private String info;

    @Column(name = "infoKey")
    private String infoKey;

    // @JsonIgnore
    // private String infoKey;
    // File file
    // InputStream
    // InputStream

    public Resource() {
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
