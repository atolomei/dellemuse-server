package dellemuse.server.api.model;

import org.springframework.stereotype.Service;

import dellemuse.model.InstitutionModel;
import dellemuse.server.Settings;
import dellemuse.server.db.model.Institution;

@Service
public class InstitutionModelService extends ModelService<Institution, InstitutionModel> {

    public InstitutionModelService(Settings settings) {
        super(settings, Institution.class, InstitutionModel.class);
    }

    /**
    @Override
    public InstitutionModel model(Institution item) {
        String json = null;
        try {
            json = getObjectMapper().writeValueAsString(item);
            
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        try {
            return (InstitutionModel) getObjectMapper().readValue(json, InstitutionModel.class);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Institution source(InstitutionModel model) {
        String json = null;
        try {

            json = getObjectMapper().writeValueAsString(model);
            
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        try {
            return (Institution) getObjectMapper().readValue(json, Institution.class);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    */

}
