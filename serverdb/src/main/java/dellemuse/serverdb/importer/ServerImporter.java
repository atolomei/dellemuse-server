package dellemuse.serverdb.importer;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.serverdb.ServerDBSettings;
import dellemuse.serverdb.service.base.BaseService;

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
    ArtWorkImporter artWorkImporter;

    @JsonIgnore
    @Autowired
    ResourceImporter resourceImporter;

    @JsonIgnore
    @Autowired
    InstitutionImporter institutionImporter;

    @JsonIgnore
    @Autowired
    ArtExhibitionGuideImporter artExhibitionGuideImporter;

    @JsonIgnore
    @Autowired
    ArtExhibitionImporter artExhibitionImporter;
    
    @JsonIgnore
    @Autowired
    GuideContentImporter guideContentImporter;

    @JsonIgnore
    @Autowired
    SiteImporter siteImporter;

    public ServerImporter(ServerDBSettings settings, PersonImporter personImporter, UserImporter userImporter,
            ArtWorkImporter artWorkImporter, ResourceImporter resourceImporter, InstitutionImporter institutionImporter,
            SiteImporter siteImporter, ArtExhibitionGuideImporter artExhibitionGuideImporter,
            GuideContentImporter guideContentImporter,
            ArtExhibitionImporter artExhibitionImporter) {

        super(settings);

        this.personImporter = personImporter;
        this.userImporter = userImporter;
        this.artWorkImporter = artWorkImporter;
        this.resourceImporter = resourceImporter;
        this.institutionImporter = institutionImporter;
        this.siteImporter = siteImporter;
        this.artExhibitionGuideImporter = artExhibitionGuideImporter;
        this.guideContentImporter = guideContentImporter;
        this.artExhibitionImporter=artExhibitionImporter;
        

    }

    public void execute() throws IOException {

    	
    	 //this.artExhibitionImporter.execute();
    	  
    	//this.siteImporter.execute();
    	//this.artWorkImporter.execute();
         
        // resourceImporter.execute();
        // personImporter.execute();
        // userImporter.execute();

        
    	
        
        // artExhibitionGuideImporter.execute();
        //this.guideContentImporter.execute();
        
         
        // this.artExhibitionImporter.execute();
        

    }

}
