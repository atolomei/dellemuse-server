package dellemuse.server.api.model;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import dellemuse.model.ArtExhibitionModel;
import dellemuse.server.Settings;
import dellemuse.server.db.model.ArtExhibition;
import dellemuse.server.db.model.ArtWork;


@Service 
public class ArtExhibitionModelService extends ModelService<ArtExhibition, ArtExhibitionModel> {
    
    public ArtExhibitionModelService(Settings settings) {
        super(settings, ArtWork.class, ArtExhibition.class);
    }

    
    @Override
    public ArtExhibitionModel model(ArtExhibition artwork) {
        return artwork.model();
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
