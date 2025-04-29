package dellemuse.server.api.model;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import dellemuse.server.Settings;

import dellemuse.server.db.model.ArtWorkType;
import dellemuse.model.ArtWorkTypeModel;

@Service
public class ArtWorkTypeModelService extends ModelService<ArtWorkType, ArtWorkTypeModel> {

    public ArtWorkTypeModelService(Settings settings) {
        super(settings, ArtWorkType.class, ArtWorkTypeModel.class);
    }

    @Override
    public ArtWorkTypeModel model(ArtWorkType type) {
        return type.model();
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
