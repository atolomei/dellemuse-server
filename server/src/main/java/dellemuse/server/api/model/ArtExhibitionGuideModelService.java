package dellemuse.server.api.model;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import dellemuse.model.ArtExhibitionGuideModel;
import dellemuse.model.ArtWorkModel;
import dellemuse.server.Settings;
import dellemuse.server.db.model.ArtExhibition;
import dellemuse.server.db.model.ArtExhibitionGuide;
import dellemuse.server.db.model.ArtWork;
import dellemuse.server.db.service.ArtExhibitionDBService;
import dellemuse.server.db.service.ArtExhibitionGuideDBService;
import dellemuse.server.error.InternalErrorException;
import jakarta.transaction.Transactional;


@Service 
public class ArtExhibitionGuideModelService extends ModelService<ArtExhibitionGuide, ArtExhibitionGuideModel> {
    
    public ArtExhibitionGuideModelService(Settings settings, ArtExhibitionGuideDBService dbService) {
        super(settings, ArtExhibitionGuide.class, ArtExhibitionGuideModel.class, dbService);
    }
    
    @Transactional
    @Override
    public ArtExhibitionGuideModel model(ArtExhibitionGuide src) {
    	
    	
    	if (isDetached(src)) 
  		   src = getDBService().findById(src.getId()).get();

    	  String json = null;
    	    
    	  try {
              json = getObjectMapper().writeValueAsString(src);
          } catch (JsonProcessingException e) {
              throw new RuntimeException(e);
          }
          
    	  try {
              return (ArtExhibitionGuideModel) getObjectMapper().readValue(json, ArtExhibitionGuideModel.class);
          } catch (JsonProcessingException e) {
              throw new RuntimeException(e);
          }
    	
    	
        //return artwork.model();
        
        
        
        
        
        
        
        
    }

    @Override
    public ArtExhibitionGuide source(ArtExhibitionGuideModel model) {
        String json = null;
        try {
            json = getObjectMapper().writeValueAsString(model);
        } catch (JsonProcessingException e) {
            throw new InternalErrorException(e, "write");
        }

        try {
            return (ArtExhibitionGuide) getObjectMapper().readValue(json, ArtExhibitionGuide.class);

        } catch (JsonProcessingException e) {
            throw new InternalErrorException(e, "read");
        }
    }
}
