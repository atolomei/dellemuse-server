package dellemuse.server.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;

import dellemuse.server.db.model.ArtWork;
import dellemuse.server.db.model.ArtWorkArtist;
import dellemuse.server.db.model.ArtWorkType;
import dellemuse.server.db.model.Person;
import dellemuse.server.db.service.ArtWorkArtistDBService;
import dellemuse.server.db.service.ArtWorkDBService;
import dellemuse.server.db.service.PersonDBService;
import dellemuse.server.db.service.UserDBService;

public class ArtWorkImporterDeserializer extends StdDeserializer<ArtWork> {

    
    final ArtWorkDBService        artWorkDBService;
    final PersonDBService         personDBService;
    final UserDBService           userDBService;
    final ArtWorkArtistDBService  artWorkArtistDBService;  
    
    final ArtWorkImporter         artworkImporter;
    
    
    
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ArtWorkImporterDeserializer() {
        this(null, null, null, null, null, null);
    }
    
    
    public ArtWorkImporterDeserializer( Class<?> vc, 
                                        ArtWorkDBService            artWorkDBService,
                                        PersonDBService             personDBService,
                                        UserDBService               userDBService,
                                        ArtWorkArtistDBService      artWorkArtistDBService,
                                        ArtWorkImporter             artworkImporter) {
        super(vc);
        
        this.userDBService = userDBService;
        this.personDBService = personDBService;
        this.artWorkDBService = artWorkDBService;
        this.artWorkArtistDBService = artWorkArtistDBService;
        this.artworkImporter=artworkImporter;
        
    }

    /**
     * 
     * 
     * 
     */
    @Override
    public ArtWork deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {

        JsonNode node = jp.getCodec().readTree(jp);

        ArtWork aw = this.artWorkDBService.create(node.get("name").asText(), userDBService.findRoot());

        String artistFirstName = (node.get("artistFirstName") != null) ? node.get("artistFirstName").asText() : null;
        String artistLastName  = (node.get("artistLastName")  != null) ? node.get("artistLastName").asText()  : null;

        Optional<Person> o_person;
        
        Person person = null;
         
        if (artistFirstName==null) {    
            o_person = this.personDBService.findByLastName(artistLastName);
            person = o_person.isPresent() ? o_person.get() : null;
        }
        else {
            o_person = this.personDBService.findByName(artistFirstName, Optional.of(artistLastName));
            if (o_person.isEmpty())
                person = this.personDBService.create(artistFirstName, artistLastName, this.userDBService.findRoot());
            else
                person = o_person.get();
        }

        if (person!=null)
               this.artWorkArtistDBService.create(aw, person, this.userDBService.findRoot());
            
        
        if (node.get("photo")!=null) {

            String photoPath = node.get("photo").asText();
            
            File file = new File(this.artworkImporter.getMediaDir(), photoPath);
            
            if (file.exists()) {
                try (InputStream is = new FileInputStream(file)) {
                    this.artworkImporter.getObjectStorageService().putObject( "media", aw.getId().toString() , is, file.getName());                    

                    
                }
            }
                    
        }
        
        return aw;

    }

    /**
    private String findLastName(String artistname) {
        int idx = artistname.lastIndexOf(' ');
        if (idx == -1)
                return artistname;
        return  artistname.substring(idx + 1);
    }
    private String findFirstName(String artistname) {
        int idx = artistname.lastIndexOf(' ');
        if (idx == -1)
                return artistname;
        return  artistname.substring(idx + 1);
    }
    **/
}
