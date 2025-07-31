package dellemuse.server.api.model;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import dellemuse.model.InstitutionModel;
import dellemuse.model.PersonModel;
import dellemuse.model.ResourceModel;
import dellemuse.model.SiteModel;
import dellemuse.server.Settings;
import dellemuse.server.db.model.Institution;
import dellemuse.server.db.model.Person;
import dellemuse.server.db.model.Resource;
import dellemuse.server.db.model.Site;
import dellemuse.server.db.service.PersonDBService;
import dellemuse.server.db.service.ResourceDBService;
import jakarta.transaction.Transactional;

@Service
public class ResourceModelService extends ModelService<Resource, ResourceModel> {

    public ResourceModelService(Settings settings, ResourceDBService dbService) {
        super(settings, Resource.class, ResourceModel.class, dbService);
    }

    @Transactional
    @Override
    public ResourceModel model(Resource person) {
    	
    	if (isDetached(person)) 
    		person = getDBService().findById(person.getId()).get();

    	
        String json = null;
        try {
            json = getObjectMapper().writeValueAsString(person);
            
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        ResourceModel model;
        try {
            model = (ResourceModel) getObjectMapper().readValue(json, ResourceModel.class);
            return model;

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
