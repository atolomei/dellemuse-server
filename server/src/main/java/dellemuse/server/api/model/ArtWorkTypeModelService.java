package dellemuse.server.api.model;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import dellemuse.server.Settings;

import dellemuse.server.db.model.ArtWorkType;
import dellemuse.server.db.service.ArtExhibitionDBService;
import dellemuse.server.db.service.ArtWorkTypeDBService;
import jakarta.transaction.Transactional;
import dellemuse.model.ArtExhibitionItemModel;
import dellemuse.model.ArtWorkTypeModel;

@Service
public class ArtWorkTypeModelService extends ModelService<ArtWorkType, ArtWorkTypeModel> {

    public ArtWorkTypeModelService(Settings settings, ArtWorkTypeDBService dbService) {
        super(settings, ArtWorkType.class, ArtWorkTypeModel.class, dbService);
    }

    @Transactional
    @Override
    public ArtWorkTypeModel model(ArtWorkType type) {
        
    	
    	  String json = null;
    	    
      	
      	if (isDetached(type)) 
     		   type = getDBService().findById(type.getId()).get();

      	
    	  try {
              json = getObjectMapper().writeValueAsString(type);
          } catch (JsonProcessingException e) {
              throw new RuntimeException(e);
          }
          
    	  try {
              return (ArtWorkTypeModel) getObjectMapper().readValue(json, ArtWorkTypeModel.class);
          } catch (JsonProcessingException e) {
              throw new RuntimeException(e);
          }
    	  
    	//return type.model();
    }
    
    @Override
    public ArtWorkType source(ArtWorkTypeModel model) {
        try {
            return (ArtWorkType) getObjectMapper().readValue(getObjectMapper().writeValueAsString(model), ArtWorkType.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
