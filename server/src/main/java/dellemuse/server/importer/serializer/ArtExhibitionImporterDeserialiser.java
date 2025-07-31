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

import dellemuse.model.util.FSUtil;
import dellemuse.server.ServerConstant;
import dellemuse.server.db.model.ArtExhibition;
import dellemuse.server.db.model.ArtWork;
import dellemuse.server.db.model.ArtWorkArtist;
import dellemuse.server.db.model.ArtWorkType;
import dellemuse.server.db.model.Institution;
import dellemuse.server.db.model.Person;
import dellemuse.server.db.model.Resource;
import dellemuse.server.db.model.Site;
import dellemuse.server.db.service.PersonDBService;
import dellemuse.server.db.service.UserDBService;
import dellemuse.server.importer.ArtExhibitionGuideImporter;
import dellemuse.server.importer.ArtExhibitionImporter;
import dellemuse.server.importer.InstitutionImporter;
import dellemuse.server.importer.SiteImporter;

public class ArtExhibitionImporterDeserialiser extends StdDeserializer<ArtExhibition> {

    private static final long serialVersionUID = 1L;

    ArtExhibitionImporter importer;
    
     /**
     * 
     */
    public ArtExhibitionImporterDeserialiser(ArtExhibitionImporter importer) {
        super(ArtExhibition.class);
        this.importer=importer;
        
    }

    /**
     * 
     */
    @Override
    public ArtExhibition deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {

        JsonNode node = p.getCodec().readTree(p);

        Optional<String> name   = (node.get("nameKey")             != null) ? Optional.of(node.get("nameKey").asText())   : Optional.empty();
        //Optional<String> title  = (node.get("title")            != null) ? Optional.of(node.get("title").asText())  : Optional.empty();
        //Optional<String> info   = (node.get("info")             != null) ? Optional.of(node.get("info").asText())   : Optional.empty();
        
        
        if (name.isEmpty())
            throw new RuntimeException("nameKey can not be null");
        
        
        Optional<String> photo  = (node.get("photo")            != null) ? Optional.of(node.get("photo").asText())  : Optional.empty();
        Optional<String> audio  = (node.get("audio")            != null) ? Optional.of(node.get("audio").asText())  : Optional.empty();
        
        
        
        if (!photo.isEmpty()) {
            
            Optional<ArtExhibition> o_ae = this.importer.getArtExhibitionDBService().findByNameKey(name.get());
            
            if (o_ae.isPresent()) {
                
                ArtExhibition a=o_ae.get();
                
                File file = new File(this.importer.getMediaDir(), photo.get());
                
                
                if (file.exists()) {

                    String objectName = String.valueOf(importer.getResourceDBService().normalizeFileName(FileNameUtils.getBaseName(file.getName()))  + "-" + this.importer.getResourceDBService().newId());

                    try (InputStream is = new FileInputStream(file)) {
                        
                        this.importer.getObjectStorageService().putObject(
                                ServerConstant.MEDIA_BUCKET, 
                                objectName, 
                                is,
                                file.getName());
                    
                    }
                    
                    Resource resource = this.importer.getResourceDBService().create(
                            ServerConstant.MEDIA_BUCKET, 
                            objectName, 
                            file.getName(),
                            getMimeType(file.getName()), 
                            this.importer.getUserDBService().findRoot());
                    
                   a.setPhoto(resource);
                   
                   this.importer.getArtExhibitionDBService().save(a);
                   
                }
                else
                    throw (new IOException("file not found -> " + file.getAbsolutePath()));
            }
        }
        
        
        
        if (!audio.isEmpty()) {
            
            Optional<ArtExhibition> o_ae = this.importer.getArtExhibitionDBService().findByNameKey(name.get());
            
            if (o_ae.isPresent()) {
                
                ArtExhibition a=o_ae.get();
                
                File file = new File(this.importer.getMediaDir(), audio.get());
                
                
                if (file.exists()) {

                    String objectName = String.valueOf(importer.getResourceDBService().normalizeFileName(FileNameUtils.getBaseName(file.getName()))  + "-" + this.importer.getResourceDBService().newId());

                    try (InputStream is = new FileInputStream(file)) {
                        
                        this.importer.getObjectStorageService().putObject(
                                ServerConstant.MEDIA_BUCKET, 
                                objectName, 
                                is,
                                file.getName());
                    
                    }
                    
                    Resource resource = this.importer.getResourceDBService().create(
                            ServerConstant.MEDIA_BUCKET, 
                            objectName, 
                            file.getName(),
                            getMimeType(file.getName()), 
                            this.importer.getUserDBService().findRoot());
                    
                   a.setAudio(resource);
                   
                   this.importer.getArtExhibitionDBService().save(a);
                   
                }
                else
                    throw (new IOException("file not found -> " + file.getAbsolutePath()));
            }
        }
        
        
        
        
        
        
        
        
        
        
        
        
        return null;
        
        
        /**
        Optional<Person> o_person = personDBService.findByName(
                node.get("name").asText(), 
                (node.get("lastname")!=null) ? 
                        Optional.of(node.get("lastname").asText()):
                        Optional.empty()
                 );
 
        Institution person;
        
        if (o_person.isEmpty()) {
            person = personDBService.create(
                node.get("name").asText(), 
                userDBService.findRoot(),
                node.get("lastname")!=null          ? Optional.of(node.get("lastname").asText())    : Optional.empty(),
                node.get("sex")!=null               ? Optional.of(node.get("sex").asText())         : Optional.empty(),
                node.get("physicialid")!=null       ? Optional.of(node.get("physicialid").asText()) : Optional.of("-"), 
                node.get("address")!=null           ? Optional.of(node.get("address").asText())     : Optional.empty(),
                node.get("zip")!=null               ? Optional.of(node.get("zip").asText())         : Optional.empty(),                        
                node.get("phone")!=null             ? Optional.of(node.get("phone").asText())       : Optional.empty(),
                node.get("email")!=null             ? Optional.of(node.get("email").asText())       : Optional.empty()
                );
        
            return person;
        }

        person = o_person.get();
        return person;
        **/
        
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
}
