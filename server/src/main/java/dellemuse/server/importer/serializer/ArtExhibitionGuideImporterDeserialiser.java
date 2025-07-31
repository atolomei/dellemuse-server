package dellemuse.server.importer.serializer;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import dellemuse.server.db.model.ArtExhibition;
import dellemuse.server.db.model.ArtExhibitionGuide;
import dellemuse.server.db.model.ArtWork;
import dellemuse.server.db.model.ArtWorkArtist;
import dellemuse.server.db.model.ArtWorkType;
import dellemuse.server.db.model.Institution;
import dellemuse.server.db.model.Person;
import dellemuse.server.db.model.Site;
import dellemuse.server.db.service.PersonDBService;
import dellemuse.server.db.service.UserDBService;
import dellemuse.server.importer.ArtExhibitionGuideImporter;
import dellemuse.server.importer.InstitutionImporter;
import dellemuse.server.importer.SiteImporter;

public class ArtExhibitionGuideImporterDeserialiser extends StdDeserializer<ArtExhibitionGuide> {

    private static final long serialVersionUID = 1L;

     /**
     * 
     */
    public ArtExhibitionGuideImporterDeserialiser(Class<ArtExhibitionGuide> personClass, ArtExhibitionGuideImporter impoter) {
        super(personClass);
    }

    /**
     * 
     */
    @Override
    public ArtExhibitionGuide deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {

        JsonNode node = p.getCodec().readTree(p);

        
        
        
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

}
