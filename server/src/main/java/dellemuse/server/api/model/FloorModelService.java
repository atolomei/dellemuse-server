package dellemuse.server.api.model;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import dellemuse.model.FloorModel;
import dellemuse.model.InstitutionModel;
import dellemuse.model.ResourceModel;
import dellemuse.model.SiteModel;
import dellemuse.server.Settings;
import dellemuse.server.db.model.Floor;
import dellemuse.server.db.model.Institution;
import dellemuse.server.db.model.Resource;
import dellemuse.server.db.model.Site;
import dellemuse.server.db.service.ArtWorkTypeDBService;
import dellemuse.server.db.service.FloorDBService;
import jakarta.transaction.Transactional;

@Service
public class FloorModelService extends ModelService<Floor, FloorModel> {

    public FloorModelService(Settings settings, FloorDBService dbService) {
        super(settings, Site.class, SiteModel.class, dbService);
    }
    
    

    @Transactional
    @Override
    public FloorModel model(Floor src) {
        
    	
    	
    	if (isDetached(src)) 
   		   src = getDBService().findById(src.getId()).get();

    	
    	String json = null;
        try {
            json = getObjectMapper().writeValueAsString(src);
            
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        FloorModel model;
        try {
            model = (FloorModel) getObjectMapper().readValue(json, FloorModel.class);
            return model;

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    
    
}
