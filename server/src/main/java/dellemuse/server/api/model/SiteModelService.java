package dellemuse.server.api.model;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import dellemuse.model.InstitutionModel;
import dellemuse.model.ResourceModel;
import dellemuse.model.SiteModel;
import dellemuse.server.Settings;
import dellemuse.server.db.model.Institution;
import dellemuse.server.db.model.Resource;
import dellemuse.server.db.model.Site;
import dellemuse.server.db.service.SiteDBService;
import jakarta.transaction.Transactional;

@Service
public class SiteModelService extends ModelService<Site, SiteModel> {

    public SiteModelService(Settings settings, SiteDBService dbService) {
        super(settings, Site.class, SiteModel.class, dbService);
    }
 
    @Transactional
    @Override
    public SiteModel model(Site site) {
        
    	if (isDetached(site)) 
    		site = getDBService().findById(site.getId()).get();
    	
    	String json = null;
        try {
            json = getObjectMapper().writeValueAsString(site);
            
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        SiteModel model;
        try {
            model = (SiteModel) getObjectMapper().readValue(json, SiteModel.class);
            return model;

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    
    
}
