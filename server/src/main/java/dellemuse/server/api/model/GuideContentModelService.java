package dellemuse.server.api.model;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import dellemuse.model.ArtExhibitionGuideModel;
import dellemuse.model.ArtWorkModel;
import dellemuse.model.GuideContentModel;
import dellemuse.server.Settings;
import dellemuse.server.db.model.ArtWork;
import dellemuse.server.db.model.GuideContent;
import dellemuse.server.db.service.FloorDBService;
import dellemuse.server.db.service.GuideContentDBService;
import dellemuse.server.error.InternalErrorException;
import jakarta.transaction.Transactional;


@Service 
public class GuideContentModelService extends ModelService<GuideContent, GuideContentModel> {
    
    public GuideContentModelService(Settings settings, GuideContentDBService dbService) {
        super(settings, GuideContent.class, GuideContentModel.class, dbService);
    }

    @Transactional
    @Override
    public GuideContentModel model(GuideContent src) {
       

  	  String json = null;
  	    
   	
  	
  	if (isDetached(src)) 
 		   src = getDBService().findById(src.getId()).get();

  
  	
  	  try {
            json = getObjectMapper().writeValueAsString(src);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        
  	  try {
            return (GuideContentModel) getObjectMapper().readValue(json, GuideContentModel.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    	
    	// return artwork.model();
    }

    @Override
    public GuideContent source(GuideContentModel model) {
        String json = null;
        try {
            json = getObjectMapper().writeValueAsString(model);
        } catch (JsonProcessingException e) {
            throw new InternalErrorException(e, "write");
        }

        try {
            return (GuideContent) getObjectMapper().readValue(json, GuideContent.class);
        } catch (JsonProcessingException e) {
            throw new InternalErrorException(e, "read");
        }
    }
}
