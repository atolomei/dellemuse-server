package dellemuse.server.api.model;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import dellemuse.model.ArtWorkModel;
import dellemuse.server.Settings;
import dellemuse.server.db.model.ArtWork;
import dellemuse.server.error.InternalErrorException;


@Service 
public class ArtWorkModelService extends ModelService<ArtWork, ArtWorkModel> {
    
    public ArtWorkModelService(Settings settings) {
        super(settings, ArtWork.class, ArtWorkModel.class);
    }

    @Override
    public ArtWorkModel model(ArtWork artwork) {
        return artwork.model();
    }

    @Override
    public ArtWork source(ArtWorkModel model) {
        String json = null;
        try {
            json = getObjectMapper().writeValueAsString(model);
        } catch (JsonProcessingException e) {
            throw new InternalErrorException(e, "write");
        }

        try {
            return (ArtWork) getObjectMapper().readValue(json, ArtWork.class);
        } catch (JsonProcessingException e) {
            throw new InternalErrorException(e, "read");
        }
    }
}
