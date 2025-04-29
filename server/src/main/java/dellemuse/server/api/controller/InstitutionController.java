package dellemuse.server.api.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.InstitutionModel;
import dellemuse.model.logging.Logger;
import dellemuse.server.api.model.InstitutionModelService;
import dellemuse.server.db.model.Institution;
import dellemuse.server.db.service.InstitutionDBService;
import dellemuse.server.security.SecurityService;

@RestController
@RequestMapping(value = "/institution")
public class InstitutionController extends BaseController<Institution, InstitutionModel> {

    @SuppressWarnings("unused")
    static private dellemuse.model.logging.Logger logger = Logger.getLogger(InstitutionController.class.getName());

    @JsonIgnore
    @Autowired
    private final InstitutionDBService dbService;

    @JsonIgnore
    @Autowired
    private final InstitutionModelService modelService;

    @JsonIgnore
    @Autowired
    private final SecurityService securityService;
    
    public InstitutionController(InstitutionDBService dbService, InstitutionModelService modelService, SecurityService securityService) {
        super(securityService);
        this.modelService = modelService;
        this.dbService = dbService;
        this.securityService=securityService;
    }


    @Override
    public InstitutionDBService getDBService() {
        return dbService;
    }

    @Override
    public InstitutionModelService getModelService() {
        return modelService;
    }
}
