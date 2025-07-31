package dellemuse.server.api.model;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import dellemuse.model.UserModel;
import dellemuse.server.Settings;
import dellemuse.server.db.model.User;
import dellemuse.server.db.service.UserDBService;

/**
 * 
 * 
 */
@Service
public class UserModelService extends ModelService<User, UserModel> {

    public UserModelService(Settings settings,  UserDBService dbService) {
        super(settings, User.class, UserModel.class, dbService);
    }

    @Override
    public UserModel model(User item) {
    	
    	if (isDetached(item)) 
    		item = getDBService().findById(item.getId()).get();
    	
        String json = null;
        try {
            json = getObjectMapper().writeValueAsString(item);
            
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        try {
            return (UserModel) getObjectMapper().readValue(json, UserModel.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User source(UserModel model) {
        String json = null;
        try {
            json = getObjectMapper().writeValueAsString(model);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        try {
            return (User) getObjectMapper().readValue(json, User.class);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

}
