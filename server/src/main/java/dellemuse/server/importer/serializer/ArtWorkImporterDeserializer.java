package dellemuse.server.importer.serializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.commons.io.FileUtils;
import org.aspectj.util.FileUtil;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.Check;
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
            
    static private Logger logger = Logger.getLogger(ArtWorkImporterDeserializer.class.getName());
    
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
----+-------------------------------------------------------------------------------------------------------------
id  |                                                    name
----+-------------------------------------------------------------------------------------------------------------
    |
368 | Danseuse et admirateur derri├¿re la sc├¿ne (Bailarina y admirador tras la escena)
344 | Effet de neige ├á Louveciennes
352 | En observation - M.Fabre, Officier de reserve
350 | Femme aux champs (Campesina)
358 | La berge de La Seine (Orillas del Sena)
360 | La Coiffure (El peinado)La berge de La Seine (Orillas del Sena)
370 | La Nymphe surprise (La ninfa sorprendida)
362 | La Toilette apres le bain (El arreglo despu├⌐s del ba├▒o)
262 | Le bain de V├⌐nus (El ba├▒o de Venus)
364 | Le Moulin de la Galette
346 | Le Pont d`Argenteuil (El Puente de Argenteuil)
260 | Morro da favela II (Pueblito)
366 | Portrait d Ernest Hosched├⌐ et sa fille Marthe (Retrato del Se├▒or Hosched├⌐ y su hija)
356 | Portrait de Suzanne Valadon (Madame Suzanne Valadon, artiste peintre) (Retrato de Suzanne Valadon, pintora)
348 | Prairies du Valhermeil pr├¿s Pontoise
354 | Vahine no te miti (Femme a la mer) (Mujer del mar).
    |
*/
    

    @Override
    public ArtWork deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
        
        
        JsonNode node = jp.getCodec().readTree(jp);

        Check.requireNonNull(node, "node is null");
        Check.requireNonNull(node.get("photo"), "photo is null");
        
        String photoPath = node.get("photo").asText();
        File file = new File(this.artWorkImporter.getMediaDir(), photoPath);
        
        Check.requireTrue(file.exists(), file.getAbsolutePath() + " does not exits");

        
        Optional<String> nameKey      = (node.get("nameKey")      != null) ? Optional.of(node.get("nameKey").asText().trim())       : Optional.empty();
        Optional<String> artworkName  = (node.get("artWorkName")  != null) ? Optional.of(node.get("artWorkName").asText().trim())   : Optional.empty();
        
        ArtWork aw = null;
        
        if (nameKey.isPresent()) {
            List<ArtWork> list = this.artWorkImporter.getArtWorkDBService().getByNameKey(nameKey.get());  
            if (!list.isEmpty()) {
                aw = list.get(0);
                logger.debug( "found -> " + aw.getName());
            }
        }
        
        if (artworkName.isPresent()) {
            List<ArtWork> list = this.artWorkImporter.getArtWorkDBService().getNameLike(artworkName.get());  
            if (!list.isEmpty()) {
                aw = list.get(0);
                logger.debug( "found -> " + aw.getName());
            }
        }
        
        if (aw==null) {

            if (node.get("name")==null)
                throw new IllegalArgumentException("name is null");

            logger.debug( "creating art work -> " + node.get("name").asText());
            
            aw = this.artWorkImporter.getArtWorkDBService().create(node.get("name").asText(),  findRoot());
            addFields( aw, node);
        }

        
        String objectName = String.valueOf(this.artWorkImporter.getResourceDBService().normalizeFileName(FileNameUtils.getBaseName(file.getName())) + "-" + this.artWorkImporter.getResourceDBService().newId());
        
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
        this.artWorkImporter.getArtWorkDBService().save(aw);
        
        logger.debug( "saving  -> " + aw.getDisplayname());
        
        return aw;
    }
    
    
    private User findRoot() {
        return this.artWorkImporter.getUserDBService().findRoot();
        
    }

    
    private void addFields(ArtWork aw, JsonNode node) {

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
          Set<Person> set = new HashSet<Person>();
          set.add(person);
          aw.setArtists(set);
        }
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
