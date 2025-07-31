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
import dellemuse.server.db.model.User;
import dellemuse.server.db.service.PersonDBService;
import dellemuse.server.db.service.UserDBService;

public class UserImporterDeserialiser extends StdDeserializer<User> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;


    @JsonIgnore
    private final PersonDBService personDBService;

    
    @JsonIgnore
    private final UserDBService userDBService;

    
    public UserImporterDeserialiser(PersonDBService personDBService, UserDBService userDBService) {
        this(null, personDBService, userDBService);
    }

    public UserImporterDeserialiser(Class<Person> personClass, PersonDBService personDBService, UserDBService userDBService) {
        super(personClass);

        this.personDBService=personDBService;
        this.userDBService=userDBService;
    }

    @Override
    public User deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {

        JsonNode node = p.getCodec().readTree(p);

        Optional<Person> o_person = getPersonDBService().findByDisplayName(node.get("person").asText());
        User user = null;
        if (!o_person.isEmpty()) {
            user = getUserDBService().create(node.get("name").asText(), o_person.get(), getUserDBService().findRoot());
            return user;
        }
        return null;
    }

    public PersonDBService getPersonDBService() {
        return personDBService;
    }

    public UserDBService getUserDBService() {
        return userDBService;
    }

}
