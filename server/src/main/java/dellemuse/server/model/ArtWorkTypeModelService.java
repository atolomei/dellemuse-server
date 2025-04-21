package dellemuse.server.model;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import dellemuse.server.Settings;

import dellemuse.server.db.model.ArtWorkType;
import dellemuse.model.ArtWorkTypeModel;

@Service
public class ArtWorkTypeModelService extends ModelService<ArtWorkType, ArtWorkTypeModel> {

    public ArtWorkTypeModelService(Settings settings) {
        super(settings);
    }

    @Override
    public ArtWorkTypeModel getModel(ArtWorkType type) {

        String json = null;

        try {
            json = getObjectMapper().writeValueAsString(type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        ArtWorkTypeModel model;
        try {
            model = (ArtWorkTypeModel) getObjectMapper().readValue(json, dellemuse.model.ArtWorkTypeModel.class);
            return model;

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArtWorkType getSource(ArtWorkTypeModel model) {

        String json = null;
        try {
            json = getObjectMapper().writeValueAsString(model);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        ArtWorkType type;
        try {
            type = (ArtWorkType) getObjectMapper().readValue(json, ArtWorkType.class);
            return type;

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
