package dellemuse.server.importer;

import java.io.File;
import java.io.IOException;

import org.codehaus.plexus.util.FileUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.io.Files;

import dellemuse.server.Settings;
import dellemuse.server.db.model.ArtWork;
import dellemuse.server.db.model.Person;
import dellemuse.server.db.service.ArtWorkArtistDBService;
import dellemuse.server.db.service.ArtWorkDBService;
import dellemuse.server.db.service.PersonDBService;
import dellemuse.server.db.service.ResourceDBService;
import dellemuse.server.db.service.UserDBService;
import dellemuse.server.importer.serializer.ArtWorkImporterDeserializer;
import dellemuse.server.importer.serializer.PersonImporterDeserialiser;
import dellemuse.server.objectstorage.ObjectStorageService;

@Service
public class ArtWorkImporter extends BaseImporter {

    private final ResourceDBService         resourceDBService;
    private final ArtWorkDBService          artWorkDBService;
    private final PersonDBService           personDBService;
    //private final ArtWorkArtistDBService    artWorkArtistDBService;

    public ArtWorkImporter(Settings settings,  
            UserDBService           userDBService,  
            ObjectStorageService    objectStorageService, 
            ResourceDBService       resourceDBService,
            ArtWorkDBService        artWorkDBService,
            //ArtWorkArtistDBService  artWorkArtistDBService,
            PersonDBService         personDBService) {
        super(settings, userDBService, objectStorageService, ArtWork.class.getSimpleName().toLowerCase());
        
        this.resourceDBService=resourceDBService;
        this.artWorkDBService=artWorkDBService;
        this.personDBService = personDBService;
        //this.artWorkArtistDBService = artWorkArtistDBService;
        
        ArtWorkImporterDeserializer personDeserializer = new ArtWorkImporterDeserializer(ArtWork.class, this);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(ArtWork.class, personDeserializer);
        getObjectMapper().registerModule(module);
    }
 
    @SuppressWarnings("unused")
    @Override
    protected boolean read(File file) throws StreamReadException, DatabindException, IOException {
        
        ArtWork aw = getObjectMapper().readValue(file, ArtWork.class);
        return true;
    }

    public ResourceDBService getResourceDBService() {
        return resourceDBService;
    }

    public ArtWorkDBService getArtWorkDBService() {
        return  artWorkDBService;
    }

    public PersonDBService getPersonDBService() {
        return this.personDBService;
    }

    //public ArtWorkArtistDBService getArtWorkArtistDBService() {
    //    return artWorkArtistDBService;
    //}

}
