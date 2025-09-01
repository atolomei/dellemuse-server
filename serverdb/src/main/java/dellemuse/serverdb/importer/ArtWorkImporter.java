package dellemuse.serverdb.importer;

import java.io.File;
import java.io.IOException;

import org.codehaus.plexus.util.FileUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.io.Files;

import dellemuse.serverdb.ServerDBSettings;
import dellemuse.serverdb.importer.serializer.ArtWorkImporterDeserializer;
import dellemuse.serverdb.importer.serializer.PersonImporterDeserialiser;
import dellemuse.serverdb.model.ArtWork;
import dellemuse.serverdb.model.Person;
import dellemuse.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverdb.service.ArtWorkArtistDBService;
import dellemuse.serverdb.service.ArtWorkDBService;
import dellemuse.serverdb.service.PersonDBService;
import dellemuse.serverdb.service.ResourceDBService;
import dellemuse.serverdb.service.UserDBService;

@Service
public class ArtWorkImporter extends BaseImporter {

    private final ResourceDBService         resourceDBService;
    private final ArtWorkDBService          artWorkDBService;
    private final PersonDBService           personDBService;
    //private final ArtWorkArtistDBService    artWorkArtistDBService;

    public ArtWorkImporter(ServerDBSettings settings,  
            UserDBService           userDBService,  
            ObjectStorageService    objectStorageService, 
            ResourceDBService       resourceDBService,
            ArtWorkDBService        artWorkDBService,
            PersonDBService         personDBService) {
        super(settings, userDBService, objectStorageService, ArtWork.class.getSimpleName().toLowerCase());
        
        this.resourceDBService=resourceDBService;
        this.artWorkDBService=artWorkDBService;
        this.personDBService = personDBService;
        
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
}
