package dellemuse.server.api.model;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import dellemuse.model.ArtWorkModel;
import dellemuse.model.GuideContentModel;
import dellemuse.server.Settings;
import dellemuse.server.db.model.ArtWork;
import dellemuse.server.db.model.GuideContent;
import dellemuse.server.error.InternalErrorException;


@Service 
public class GuideContentModelService extends ModelService<GuideContent, GuideContentModel> {
    
    public GuideContentModelService(Settings settings) {
        super(settings, GuideContent.class, GuideContentModel.class);
    }


    @Override
    public GuideContentModel model(GuideContent artwork) {
        return artwork.model();
    }

    @Override
    public GuideContent source(GuideContentModel model) {
        String json = null;
        try {
            json = getObjectMapper().writeValueAsString(model);
        } catch (JsonProcessingException e) {
            throw new InternalErrorException(e, "write");
        }

        try {
            return (GuideContent) getObjectMapper().readValue(json, GuideContent.class);
        } catch (JsonProcessingException e) {
            throw new InternalErrorException(e, "read");
        }
    }
}
