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
import dellemuse.serverdb.importer.serializer.SiteImporterDeserialiser;
import dellemuse.serverdb.model.Person;
import dellemuse.serverdb.model.Site;
import dellemuse.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverdb.service.InstitutionDBService;
import dellemuse.serverdb.service.PersonDBService;
import dellemuse.serverdb.service.ResourceDBService;
import dellemuse.serverdb.service.SiteDBService;
import dellemuse.serverdb.service.UserDBService;

@Service
public class SiteImporter extends BaseImporter {

    @JsonIgnore
    @Autowired
    private final SiteDBService siteDBService;
    
    
    @JsonIgnore
    @Autowired
    private final InstitutionDBService institutionDBService;
    
    @JsonIgnore
    @Autowired
    private final ResourceDBService resourceDBService;
    
    public SiteImporter(ServerDBSettings settings,  SiteDBService siteDBService, UserDBService userDBService,  ObjectStorageService objectStorageService, InstitutionDBService institutionDBService, ResourceDBService resourceDBService) {
        super(settings, userDBService,  objectStorageService, Site.class.getSimpleName().toLowerCase());

        this.siteDBService=siteDBService;
        this.institutionDBService=institutionDBService;
        this.resourceDBService=resourceDBService;
        
        SiteImporterDeserialiser siteDeserializer = new SiteImporterDeserialiser(Site.class, this);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Site.class, siteDeserializer);
        getObjectMapper().registerModule(module);
       
    }
    
    @Override
    protected boolean read(File file) throws StreamReadException, DatabindException, IOException {
        Site site = getObjectMapper().readValue(file, Site.class);
        return true;
    }

    public SiteDBService getSiteDBService() {
        return  siteDBService;
    }

    public InstitutionDBService getInstitutionDBService() {
        return institutionDBService;
    }
    
    public ResourceDBService getResourceDBService() {
        return resourceDBService;
    }
    

}
