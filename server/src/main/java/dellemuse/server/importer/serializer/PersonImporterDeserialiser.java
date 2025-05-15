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

import dellemuse.server.db.model.ArtWork;
import dellemuse.server.db.model.ArtWorkArtist;
import dellemuse.server.db.model.ArtWorkType;
import dellemuse.server.db.model.Person;
import dellemuse.server.db.service.PersonDBService;
import dellemuse.server.db.service.UserDBService;

public class PersonImporterDeserialiser extends StdDeserializer<Person> {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private final PersonDBService personDBService;
    
    @JsonIgnore
    private final UserDBService userDBService;

    public PersonImporterDeserialiser(PersonDBService personDBService, UserDBService userDBService) {
        this(null, personDBService, userDBService);
    }

    /**
     * 
     */
    public PersonImporterDeserialiser(Class<Person> personClass, PersonDBService personDBService, UserDBService userDBService) {
        super(personClass);
        this.personDBService=personDBService;
        this.userDBService=userDBService;
    }

    /**
     * 
     */
    @Override
    public Person deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {

        JsonNode node = p.getCodec().readTree(p);

        Optional<Person> o_person = personDBService.findByName(
                node.get("name").asText(), 
                (node.get("lastname")!=null) ? 
                        Optional.of(node.get("lastname").asText()):
                        Optional.empty()
                 );
 
        Person person;
        
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
        
        
        
        
    }

}
