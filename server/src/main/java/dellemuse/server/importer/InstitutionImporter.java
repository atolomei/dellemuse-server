package dellemuse.server.importer;

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

import dellemuse.server.Settings;
import dellemuse.server.db.model.Institution;
import dellemuse.server.db.model.Person;
import dellemuse.server.db.service.InstitutionDBService;
import dellemuse.server.db.service.PersonDBService;
import dellemuse.server.db.service.UserDBService;
import dellemuse.server.importer.serializer.InstitutionImporterDeserialiser;
import dellemuse.server.importer.serializer.PersonImporterDeserialiser;
import dellemuse.server.objectstorage.ObjectStorageService;


@Service
public class InstitutionImporter extends BaseImporter {

    @JsonIgnore
    @Autowired
    InstitutionDBService institutionDBService;
    
    
    public InstitutionImporter(     Settings settings,  
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
