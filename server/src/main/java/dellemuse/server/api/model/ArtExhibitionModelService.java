package dellemuse.server.api.model;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import dellemuse.model.ArtExhibitionItemModel;
import dellemuse.model.ArtExhibitionModel;
import dellemuse.server.Settings;
import dellemuse.server.db.model.ArtExhibition;
import dellemuse.server.db.model.ArtWork;
import dellemuse.server.db.service.ArtExhibitionDBService;
import jakarta.transaction.Transactional;


@Service 
public class ArtExhibitionModelService extends ModelService<ArtExhibition, ArtExhibitionModel> {
    
    public ArtExhibitionModelService(Settings settings, ArtExhibitionDBService dbService) {
        super(settings, ArtExhibitionModel.class, ArtExhibitionModel.class,  dbService);
    }

    @Transactional
    @Override
    public ArtExhibitionModel model(ArtExhibition src) {
      //  return artwork.model();
    
    	String json = null;
      	
      	if (isDetached(src)) 
     		   src = getDBService().findById(src.getId()).get();

      	
    	  try {
              json = getObjectMapper().writeValueAsString(src);
          } catch (JsonProcessingException e) {
              throw new RuntimeException(e);
          }
          
    	  try {
              return (ArtExhibitionModel) getObjectMapper().readValue(json, ArtExhibitionModel.class);
          } catch (JsonProcessingException e) {
              throw new RuntimeException(e);
          }
    	
    }

    @Override
    public ArtExhibition source(ArtExhibitionModel model) {
        String json = null;
        try {
            json = getObjectMapper().writeValueAsString(model);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        try {
            return (ArtExhibition) getObjectMapper().readValue(json, ArtExhibition.class);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
