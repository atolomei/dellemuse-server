package dellemuse.server.api.model;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import dellemuse.model.ArtExhibitionItemModel;
import dellemuse.model.ArtExhibitionModel;
import dellemuse.server.Settings;
import dellemuse.server.db.model.ArtExhibition;
import dellemuse.server.db.model.ArtExhibitionItem;
import dellemuse.server.db.model.ArtWork;


@Service 
public class ArtExhibitionItemModelService extends ModelService<ArtExhibitionItem, ArtExhibitionItemModel> {
    
    public ArtExhibitionItemModelService(Settings settings) {
        super(settings, ArtExhibitionItem.class, ArtExhibitionItemModel.class);
    }

    
    @Override
    public ArtExhibitionItemModel model(ArtExhibitionItem artwork) {
        return artwork.model();
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
