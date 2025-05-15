package dellemuse.server.importer.serializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;

import dellemuse.server.ServerConstant;
import dellemuse.server.db.model.ArtWork;
import dellemuse.server.db.model.ArtWorkArtist;
import dellemuse.server.db.model.ArtWorkType;
import dellemuse.server.db.model.Person;
import dellemuse.server.db.model.Resource;
import dellemuse.server.db.model.User;
import dellemuse.server.db.service.ArtWorkArtistDBService;
import dellemuse.server.db.service.ArtWorkDBService;
import dellemuse.server.db.service.PersonDBService;
import dellemuse.server.db.service.ResourceDBService;
import dellemuse.server.db.service.UserDBService;
import dellemuse.server.importer.ArtWorkImporter;

public class ArtWorkImporterDeserializer extends StdDeserializer<ArtWork> {

    final ArtWorkImporter         artWorkImporter;
    private static final long serialVersionUID = 1L;

    public ArtWorkImporterDeserializer() {
        this(null, null);
    }

    public ArtWorkImporterDeserializer( Class<?> vc, 
                                        ArtWorkImporter artWorkImporter) {
        super(vc);
        
        this.artWorkImporter=artWorkImporter;
    }

    /**
     * 
     * 
     * 
     */
    @Override
    public ArtWork deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {

        JsonNode node = jp.getCodec().readTree(jp);

        if (node.get("name")==null)
            throw new RuntimeException("name is null");

        
        ArtWork aw = this.artWorkImporter.getArtWorkDBService().create(node.get("name").asText(),  findRoot());

        
        String artistFirstName  = (node.get("artistFirstName")  != null) ? node.get("artistFirstName").asText() : null;
        String artistLastName   = (node.get("artistLastName")   != null) ? node.get("artistLastName").asText()  : null;
        String title            = (node.get("title")            != null) ? node.get("title").asText()           : null;
        
        if (title!=null)
            aw.setTitle(title);

        Optional<Person> o_person;
        
        Person person = null;
         
        if (artistFirstName==null) {    
            o_person =  this.artWorkImporter.getPersonDBService().findByLastName(artistLastName);
            person = o_person.isPresent() ? o_person.get() : null;
        }
        else {
            o_person = this.artWorkImporter.getPersonDBService().findByName(artistFirstName, Optional.of(artistLastName));
            if (o_person.isEmpty())
                person = this.artWorkImporter.getPersonDBService().create(artistFirstName, artistLastName,  findRoot());
            else
                person = o_person.get();
        }


        if (person==null) {
            if (artistLastName==null)
                throw new RuntimeException("no artist");
            person = this.artWorkImporter.getPersonDBService().create(artistFirstName, artistLastName,  findRoot());
        }
        
        if (person!=null) {
            //ArtWorkArtist awa = this.artWorkImporter.getArtWorkArtistDBService().create(aw, person, findRoot());
            //List<ArtWorkArtist> list = new ArrayList<ArtWorkArtist>();
            //list.add(awa);
            //aw.setArtWorkArtists(null);
            
          Set<Person> set = new HashSet<Person>();
          set.add(person);
          aw.setArtists(set);
        }

        
        if (node.get("photo")!=null) {
            String photoPath = node.get("photo").asText();
            File file = new File(this.artWorkImporter.getMediaDir(), photoPath);
            if (file.exists()) {
                String objectName = String.valueOf(this.artWorkImporter.getResourceDBService().normalizeFileName(file.getName()) + "-" + this.artWorkImporter.getResourceDBService().newId());
                try (InputStream is = new FileInputStream(file)) {
                    this.artWorkImporter.getObjectStorageService().putObject( ServerConstant.MEDIA_BUCKET, objectName , is, file.getName());                    
                }
                Resource resource = this.artWorkImporter.getResourceDBService().create(
                        ServerConstant.MEDIA_BUCKET, 
                        objectName, 
                        file.getName(), 
                        this.artWorkImporter.getResourceDBService().getMimeType(file.getName()),
                        this.artWorkImporter.getUserDBService().findRoot()
                       );
                aw.setPhoto(resource);
            }
        }
        
        this.artWorkImporter.getArtWorkDBService().save(aw);
        return aw;
    }

    
    private User findRoot() {
        return this.artWorkImporter.getUserDBService().findRoot();
        
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
