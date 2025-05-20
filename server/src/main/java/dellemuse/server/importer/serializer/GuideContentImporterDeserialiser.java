package dellemuse.server.importer.serializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.apache.commons.compress.utils.FileNameUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import dellemuse.server.ServerConstant;
import dellemuse.server.db.model.ArtExhibition;
import dellemuse.server.db.model.ArtExhibitionGuide;
import dellemuse.server.db.model.ArtWork;
import dellemuse.server.db.model.ArtWorkArtist;
import dellemuse.server.db.model.ArtWorkType;
import dellemuse.server.db.model.GuideContent;
import dellemuse.server.db.model.Institution;
import dellemuse.server.db.model.Person;
import dellemuse.server.db.model.Resource;
import dellemuse.server.db.model.Site;
import dellemuse.server.db.service.PersonDBService;
import dellemuse.server.db.service.UserDBService;
import dellemuse.server.importer.ArtExhibitionGuideImporter;
import dellemuse.server.importer.ArtExhibitionImporter;
import dellemuse.server.importer.GuideContentImporter;
import dellemuse.server.importer.InstitutionImporter;
import dellemuse.server.importer.SiteImporter;

public class GuideContentImporterDeserialiser extends StdDeserializer<GuideContent> {

    private static final long serialVersionUID = 1L;

    GuideContentImporter importer;
     /**
     * 
     */
    public GuideContentImporterDeserialiser(Class<GuideContent> personClass,  GuideContentImporter importer) {
        super(personClass);
        this.importer=importer;
    
    }

    /**
     * 
     */
    @Override
    public GuideContent deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {

        JsonNode node = p.getCodec().readTree(p);

        Optional<String> guideContentNameKey        = (node.get("guideContent-nameKey")  != null) ? Optional.of(node.get("guideContent-nameKey").asText())  : Optional.empty();
        
        Optional<String> guideNameKey               = (node.get("guide-nameKey")         != null) ? Optional.of(node.get("guide-nameKey").asText()): Optional.empty();
        Optional<Integer> guideOrder                = (node.get("guideOrder")             != null) ? Optional.of(node.get("guideOrder").asInt())  : Optional.empty();
        Optional<String> audio                      = (node.get("audio")                  != null) ? Optional.of(node.get("audio").asText())  : Optional.empty();
 
        if (guideContentNameKey.isPresent() ) {
            String key = guideContentNameKey.get();
            
            List<GuideContent> list = this.importer.getGuideContentDBService().getByNameKey(key);
            
            if (!list.isEmpty()) {
                
                GuideContent content = list.get(0);
                
                if (!audio.isEmpty()) {
                    File file = new File(this.importer.getMediaDir(), audio.get());
                    if (file.exists()) {
                        String objectName = String.valueOf(this.importer.getResourceDBService().normalizeFileName( FileNameUtils.getBaseName(file.getName())) + "-" + this.importer.getResourceDBService().newId());
                
                        try (InputStream is = new FileInputStream(file)) {
                            this.importer.getObjectStorageService().putObject( ServerConstant.MEDIA_BUCKET, objectName , is, file.getName());                    
                        }
                        
                        Resource resource = this.importer.getResourceDBService().create(
                                ServerConstant.MEDIA_BUCKET, 
                               objectName, 
                                file.getName(), 
                                this.importer.getResourceDBService().getMimeType(file.getName()),
                                this.importer.getUserDBService().findRoot()
                               );
                        content.setAudio(resource);
                        this.importer.getGuideContentDBService().save(content);
                    }
                }
                return content;
            }
        }
                
        return null;
    }

}
