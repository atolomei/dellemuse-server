package dellemuse.serverdb.importer;

import java.io.File;
import java.io.IOException;

import org.codehaus.plexus.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.io.Files;

import dellemuse.serverdb.ServerDBSettings;
import dellemuse.serverdb.importer.serializer.InstitutionImporterDeserialiser;
import dellemuse.serverdb.importer.serializer.PersonImporterDeserialiser;
import dellemuse.serverdb.model.Institution;
import dellemuse.serverdb.model.Person;
import dellemuse.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverdb.service.InstitutionDBService;
import dellemuse.serverdb.service.PersonDBService;
import dellemuse.serverdb.service.UserDBService;


@Service
public class InstitutionImporter extends BaseImporter {

    @JsonIgnore
    @Autowired
    InstitutionDBService institutionDBService;
    
    
    public InstitutionImporter(     ServerDBSettings settings,  
                                    UserDBService userDBService,  
                                    ObjectStorageService objectStorageService,
                                    InstitutionDBService institutionDBService ) {
        
        super(settings, userDBService,  objectStorageService, Institution.class.getSimpleName().toLowerCase());
 
        this.institutionDBService=institutionDBService;
        
        InstitutionImporterDeserialiser personDeserializer = new InstitutionImporterDeserialiser(Institution.class, this);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Institution.class, personDeserializer);
        getObjectMapper().registerModule(module);
        
    }
    
    @Override
    protected boolean read(File file) throws StreamReadException, DatabindException, IOException {
        Institution in = getObjectMapper().readValue(file, Institution.class);
        return true;
    }


    public InstitutionDBService getInstitutionDBService() {
        return institutionDBService;
    }


    


}
