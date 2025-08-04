package dellemuse.server.api.model;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import dellemuse.model.FloorModel;
import dellemuse.model.InstitutionModel;
import dellemuse.model.ResourceModel;
import dellemuse.model.RoomModel;
import dellemuse.model.SiteModel;
import dellemuse.server.Settings;
import dellemuse.server.db.model.Floor;
import dellemuse.server.db.model.Institution;
import dellemuse.server.db.model.Resource;
import dellemuse.server.db.model.Room;
import dellemuse.server.db.model.Site;
import dellemuse.server.db.service.RoomDBService;
import jakarta.transaction.Transactional;

@Service
public class RoomModelService extends ModelService<Room, RoomModel> {

    public RoomModelService(Settings settings, RoomDBService dbService) {
        super(settings, Site.class, SiteModel.class, dbService);
    }
    
    

    @Transactional
    @Override
    public RoomModel model(Room person) {
    	
    	
    	if (isDetached(person)) 
    		person = getDBService().findById(person.getId()).get();

    	
        String json = null;
        try {
            json = getObjectMapper().writeValueAsString(person);
            
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        RoomModel model;
        try {
            model = (RoomModel) getObjectMapper().readValue(json, RoomModel.class);
            return model;

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    
    
}
