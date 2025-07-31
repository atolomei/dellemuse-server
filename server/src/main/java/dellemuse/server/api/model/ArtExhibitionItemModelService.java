package dellemuse.server.api.model;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import dellemuse.model.ArtExhibitionGuideModel;
import dellemuse.model.ArtExhibitionItemModel;
import dellemuse.model.ArtExhibitionModel;
import dellemuse.server.Settings;
import dellemuse.server.db.model.ArtExhibition;
import dellemuse.server.db.model.ArtExhibitionItem;
import dellemuse.server.db.model.ArtWork;
import dellemuse.server.db.service.ArtExhibitionItemDBService;
import jakarta.transaction.Transactional;


@Service 
public class ArtExhibitionItemModelService extends ModelService<ArtExhibitionItem, ArtExhibitionItemModel> {
    
    public ArtExhibitionItemModelService(Settings settings, ArtExhibitionItemDBService dbService) {
        super(settings, ArtExhibitionItem.class, ArtExhibitionItemModel.class, dbService);
    }

    @Transactional
    @Override
    public ArtExhibitionItemModel model(ArtExhibitionItem src) {
       
    	
    	if (isDetached(src)) 
   		   src = getDBService().findById(src.getId()).get();

  	  String json = null;
  	    
  	  try {
            json = getObjectMapper().writeValueAsString(src);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        
  	  try {
            return (ArtExhibitionItemModel) getObjectMapper().readValue(json, ArtExhibitionItemModel.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    	
    	// return artwork.model();
    }

    @Override
    public ArtExhibitionItem source(ArtExhibitionItemModel model) {
        String json = null;
        try {
            json = getObjectMapper().writeValueAsString(model);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        try {
            return (ArtExhibitionItem) getObjectMapper().readValue(json, ArtExhibitionItem.class);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
