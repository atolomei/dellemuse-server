package dellemuse.server.api.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.PersonModel;
import dellemuse.model.UserModel;
import dellemuse.model.logging.Logger;
import dellemuse.server.api.model.PersonModelService;
import dellemuse.server.api.model.UserModelService;
import dellemuse.server.db.model.Person;
import dellemuse.server.db.model.User;
import dellemuse.server.db.service.PersonDBService;
import dellemuse.server.db.service.UserDBService;
import dellemuse.server.security.SecurityService;

@RestController
@RequestMapping(value = "/user")
public class UsersController extends BaseController<User, UserModel> {

    @SuppressWarnings("unused")
    static private dellemuse.model.logging.Logger logger = Logger.getLogger(UsersController.class.getName());

    @JsonIgnore
    @Autowired
    private final UserDBService dbService;

    @JsonIgnore
    @Autowired
    private final UserModelService modelService;

    @JsonIgnore
    @Autowired
    private final SecurityService securityService;
    
    public UsersController(UserDBService dbService, UserModelService modelService, SecurityService securityService) {
        super(securityService);
        this.modelService = modelService;
        this.dbService = dbService;
        this.securityService=securityService;
    }

    @Override
    public UserDBService getDBService() {
        return dbService;
    }

    @Override
    public UserModelService getModelService() {
        return modelService;
    }
}
