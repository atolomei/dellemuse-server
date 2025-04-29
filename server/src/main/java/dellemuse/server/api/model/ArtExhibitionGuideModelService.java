package dellemuse.server.api.model;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import dellemuse.model.ArtExhibitionGuideModel;
import dellemuse.server.Settings;
import dellemuse.server.db.model.ArtExhibition;
import dellemuse.server.db.model.ArtExhibitionGuide;
import dellemuse.server.db.model.ArtWork;
import dellemuse.server.error.InternalErrorException;


@Service 
public class ArtExhibitionGuideModelService extends ModelService<ArtExhibitionGuide, ArtExhibitionGuideModel> {
    
    public ArtExhibitionGuideModelService(Settings settings) {
        super(settings, ArtWork.class, ArtExhibition.class);
    }
    
    @Override
    public ArtExhibitionGuideModel model(ArtExhibitionGuide artwork) {
        return artwork.model();
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
