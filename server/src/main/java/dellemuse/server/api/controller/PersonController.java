package dellemuse.server.api.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.PersonModel;
import dellemuse.model.logging.Logger;
import dellemuse.server.api.model.PersonModelService;
import dellemuse.server.db.model.Person;
import dellemuse.server.db.service.PersonDBService;
import dellemuse.server.security.SecurityService;

@RestController
@RequestMapping(value = "/person")
public class PersonController extends BaseController<Person, PersonModel> {

    @SuppressWarnings("unused")
    static private dellemuse.model.logging.Logger logger = Logger.getLogger(PersonController.class.getName());

    @JsonIgnore
    @Autowired
    private final PersonDBService dbService;

    @JsonIgnore
    @Autowired
    private final PersonModelService modelService;

    @JsonIgnore
    @Autowired
    private final SecurityService securityService;
    
    public PersonController(PersonDBService dbService, PersonModelService modelService, SecurityService securityService) {
        super(securityService);
        this.modelService = modelService;
        this.dbService = dbService;
        this.securityService=securityService;
    }

    @Override
    public PersonDBService getDBService() {
        return dbService;
    }

    @Override
    public PersonModelService getModelService() {
        return modelService;
    }
}
