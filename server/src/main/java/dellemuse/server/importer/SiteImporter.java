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
import dellemuse.server.db.model.Person;
import dellemuse.server.db.model.Site;
import dellemuse.server.db.service.InstitutionDBService;
import dellemuse.server.db.service.PersonDBService;
import dellemuse.server.db.service.ResourceDBService;
import dellemuse.server.db.service.SiteDBService;
import dellemuse.server.db.service.UserDBService;
import dellemuse.server.importer.serializer.PersonImporterDeserialiser;
import dellemuse.server.importer.serializer.SiteImporterDeserialiser;
import dellemuse.server.objectstorage.ObjectStorageService;

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
    
    public SiteImporter(Settings settings,  SiteDBService siteDBService, UserDBService userDBService,  ObjectStorageService objectStorageService, InstitutionDBService institutionDBService, ResourceDBService resourceDBService) {
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
