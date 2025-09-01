package dellemuse.serverdb.importer;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.module.SimpleModule;

import dellemuse.serverdb.ServerDBSettings;
import dellemuse.serverdb.importer.serializer.ArtExhibitionImporterDeserialiser;
import dellemuse.serverdb.model.ArtExhibition;
import dellemuse.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverdb.service.ArtExhibitionDBService;
import dellemuse.serverdb.service.PersonDBService;
import dellemuse.serverdb.service.ResourceDBService;
import dellemuse.serverdb.service.UserDBService;

@Service
public class ArtExhibitionImporter extends BaseImporter {
    
    @JsonIgnore
    @Autowired
    private final ResourceDBService resourceDBService;

    @JsonIgnore
    @Autowired
    ArtExhibitionDBService artExhibitionDBService;
    
    @JsonIgnore
    @Autowired
    private final PersonDBService personDBService;
    
    public ArtExhibitionImporter(ServerDBSettings settings,  
            ArtExhibitionDBService artExhibitionDBService,
            PersonDBService personDBService, 
            UserDBService userDBService,
            ResourceDBService resourceDBService,
            ObjectStorageService objectStorageService) {
        
        super(settings, userDBService,  objectStorageService, ArtExhibition.class.getSimpleName().toLowerCase());

        this.personDBService=personDBService;
        this.artExhibitionDBService=artExhibitionDBService;

        this.resourceDBService=resourceDBService;
        
        ArtExhibitionImporterDeserialiser personDeserializer = new ArtExhibitionImporterDeserialiser(this);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(ArtExhibition.class, personDeserializer);
        getObjectMapper().registerModule(module);
        
    }
    
    public ArtExhibitionDBService getArtExhibitionDBService() {
        return artExhibitionDBService;
    }

    public void setArtExhibitionDBService(ArtExhibitionDBService artExhibitionDBService) {
        this.artExhibitionDBService = artExhibitionDBService;
    }

    public PersonDBService getPersonDBService() {
        return personDBService;
    }

    public ResourceDBService getResourceDBService() {
        return resourceDBService;
    }

    @Override
    protected boolean read(File file) throws StreamReadException, DatabindException, IOException {
        ArtExhibition object = getObjectMapper().readValue(file, ArtExhibition.class);
        return true;
    }


}
