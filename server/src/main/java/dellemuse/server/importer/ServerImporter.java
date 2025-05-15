package dellemuse.server.importer;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;

import dellemuse.server.BaseService;
import dellemuse.server.Settings;
import dellemuse.server.db.service.UserDBService;

@Service
public class ServerImporter extends BaseService {

    @JsonIgnore
    @Autowired
    final PersonImporter personImporter;
    
    @JsonIgnore
    @Autowired
    final UserImporter userImporter;

    
    @JsonIgnore
    @Autowired
    ArtWorkImporter  artWorkImporter;
    
    @JsonIgnore
    @Autowired
    ResourceImporter  resourceImporter;
    
    @JsonIgnore
    @Autowired
    InstitutionImporter  institutionImporter;
    

    @JsonIgnore
    @Autowired
    SiteImporter  siteImporter;

    
    public ServerImporter(
            Settings            settings, 
            PersonImporter      personImporter,
            UserImporter        userImporter,
            ArtWorkImporter     artWorkImporter,
            ResourceImporter    resourceImporter,
            InstitutionImporter institutionImporter,
            SiteImporter        siteImporter) {
        
        super(settings);

        this.personImporter         =   personImporter;
        this.userImporter           =   userImporter;
        this.artWorkImporter        =   artWorkImporter;
        this.resourceImporter       =   resourceImporter;
        this.institutionImporter    =   institutionImporter;
        this.siteImporter           =   siteImporter;
    }
    
    public void execute() throws IOException {
        
        resourceImporter.execute();
        personImporter.execute();
        userImporter.execute();
        artWorkImporter.execute();
        
    }

    

}
