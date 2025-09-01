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
import dellemuse.serverdb.importer.serializer.PersonImporterDeserialiser;
import dellemuse.serverdb.model.Person;
import dellemuse.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverdb.service.PersonDBService;
import dellemuse.serverdb.service.UserDBService;


@Service
public class PersonImporter extends BaseImporter {

    
    @JsonIgnore
    @Autowired
    private final PersonDBService personDBService;
    
    public PersonImporter(ServerDBSettings settings,  PersonDBService personDBService, UserDBService userDBService,  ObjectStorageService objectStorageService) {
        super(settings, userDBService,  objectStorageService, Person.class.getSimpleName().toLowerCase());

        this.personDBService=personDBService;
        PersonImporterDeserialiser personDeserializer = new PersonImporterDeserialiser(Person.class, personDBService, userDBService);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Person.class, personDeserializer);
        getObjectMapper().registerModule(module);
        
    }
    

    @Override
    protected boolean read(File file) throws StreamReadException, DatabindException, IOException {
        Person person = getObjectMapper().readValue(file, Person.class);
        return true;
    }



}
