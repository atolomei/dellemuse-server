package dellemuse.server.importer.serializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.commons.io.FilenameUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.FSUtil;
import dellemuse.server.ServerConstant;
import dellemuse.server.db.model.ArtWork;
import dellemuse.server.db.model.ArtWorkArtist;
import dellemuse.server.db.model.ArtWorkType;
import dellemuse.server.db.model.Person;
import dellemuse.server.db.model.Resource;
import dellemuse.server.db.service.PersonDBService;
import dellemuse.server.db.service.ResourceDBService;
import dellemuse.server.db.service.UserDBService;
import dellemuse.server.importer.ResourceImporter;
import dellemuse.server.objectstorage.ObjectStorageService;

public class ResourceImporterDeserialiser extends StdDeserializer<Resource> {
            
    static private Logger logger = Logger.getLogger(ResourceImporterDeserialiser.class.getName());
    
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private final ResourceImporter resourceImporter;

    public ResourceImporterDeserialiser(ResourceImporter resourceImporter) {
        super(Resource.class);
        this.resourceImporter = resourceImporter;
    }

    /**
     * 
     */
    @Override
    public Resource deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {

        JsonNode node = p.getCodec().readTree(p);

        Resource resource = null;
                
        if (node.get("path") != null) {

            String path = node.get("path").asText();

            String name = null;

            if (node.get("name") != null)
                name = node.get("name").asText();

            File file = new File(this.resourceImporter.getMediaDir(), path);

            if (file.exists()) {

                if (name == null)
                    name = file.getName();

                String objectName = String.valueOf(resourceImporter.getResourceDBService().normalizeFileName(FileNameUtils.getBaseName(file.getName()))  + "-" + this.resourceImporter.getResourceDBService().newId());

                try (InputStream is = new FileInputStream(file)) {
                    
                    this.resourceImporter.getObjectStorageService().putObject(
                            ServerConstant.MEDIA_BUCKET, objectName, 
                            is,
                            file.getName());
                
                }
                
                resource = this.resourceImporter.getResourceDBService().create(
                        ServerConstant.MEDIA_BUCKET, 
                        objectName, 
                        name,
                        getMimeType(file.getName()), 
                        this.resourceImporter.getUserDBService().findRoot());
            }
            else
                throw (new IOException("file not found -> " + path));
        }
        else
            throw (new IOException("must provide a path to file -> " + node.toString()));

        return resource;
    }

    private String getMimeType(String fileName) {

        if (FSUtil.isImage(fileName)) {
            String str = FilenameUtils.getExtension(fileName);

            if (str.equals("jpg"))
                return "image/jpeg";

            if (str.equals("jpeg"))
                return "image/jpeg";

            return "image/" + str;
        }

        if (FSUtil.isPdf(fileName))
            return "application/pdf";

        if (FSUtil.isVideo(fileName))
            return "video/" + FilenameUtils.getExtension(fileName);

        if (FSUtil.isAudio(fileName))
            return "audio/" + FilenameUtils.getExtension(fileName);

        return "";
    }
    
/**
    private String normalize(String name) {
        String str = name.replaceAll("[^\\x00-\\x7F]|[\\s]+", "-").toLowerCase().trim();
        if (str.length() < 100)
            return str;
        return str.substring(0, 100);
    }
**/
    
}
