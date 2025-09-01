package dellemuse.serverdb.importer;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.module.SimpleModule;

import dellemuse.model.logging.Logger;
import dellemuse.serverdb.ServerDBSettings;
import dellemuse.serverdb.importer.serializer.ResourceImporterDeserialiser;
import dellemuse.serverdb.model.Resource;
import dellemuse.serverdb.object.service.ResourceService;
import dellemuse.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverdb.service.PersonDBService;
import dellemuse.serverdb.service.ResourceDBService;
import dellemuse.serverdb.service.UserDBService;

@Service
public class ResourceImporter extends BaseImporter {

    static private Logger logger = Logger.getLogger(ResourceImporter.class.getName());

    @JsonIgnore
    @Autowired
    private final ResourceDBService resourceDBService;

    @JsonIgnore
    @Autowired
    private final ObjectStorageService objectStorageService;

    @JsonIgnore
    @Autowired
    private final UserDBService userDBService;

    @JsonIgnore
    @Autowired
    private final PersonDBService personDBService;

    public ResourceImporter(ServerDBSettings settings, PersonDBService personDBService, ResourceDBService resourceDBService,
            UserDBService userDBService, ObjectStorageService objectStorageService) {

        super(settings, userDBService, objectStorageService, Resource.class.getSimpleName().toLowerCase());

        this.resourceDBService = resourceDBService;
        this.objectStorageService = objectStorageService;
        this.userDBService = userDBService;
        this.personDBService = personDBService;

        ResourceImporterDeserialiser personDeserializer = new ResourceImporterDeserialiser(this);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Resource.class, personDeserializer);
        getObjectMapper().registerModule(module);
    }

    @Override
    protected boolean read(File file) throws StreamReadException, DatabindException, IOException {

        Resource resource = getObjectMapper().readValue(file, Resource.class);

        //ResourceService service = this.resourceDBService.getResourceService(resource);
        //String url = service.getPresignedUrl();
        //logger.debug(url);
        
        return true;
    }

    public ResourceDBService getResourceDBService() {
        return resourceDBService;
    }

    public ObjectStorageService getObjectStorageService() {
        return objectStorageService;
    }

    public UserDBService getUserDBService() {
        return userDBService;
    }

    public PersonDBService getPersonDBService() {
        return personDBService;
    }

}
