package dellemuse.server.api.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.InstitutionModel;
import dellemuse.model.SiteModel;
import dellemuse.model.logging.Logger;
import dellemuse.server.api.model.InstitutionModelService;
import dellemuse.server.api.model.SiteModelService;
import dellemuse.server.db.model.Institution;
import dellemuse.server.db.service.InstitutionDBService;
import dellemuse.server.security.SecurityService;


/**
 * 
 * /institution/get/{id}
 * /institution/list
 * /institution/save [ InstitutionModel ]
 * /institution/create/{name}
 * /institution/exists/{id}
 * /institution/delete/{id}
 *
 * /institution/sites/{institutionid}
 * 
 */
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
    private final SiteModelService siteModelService;

    
    @JsonIgnore
    @Autowired
    private final SecurityService securityService;
    
    public InstitutionController(InstitutionDBService dbService, InstitutionModelService modelService, SecurityService securityService, SiteModelService siteModelService) {
        super(securityService);
        this.modelService = modelService;
        this.dbService = dbService;
        this.securityService=securityService;
       this.siteModelService=siteModelService;
        
    }

    /**
     * @return
     */
    @GetMapping(value = "/sites/{institutionid}")
    public ResponseEntity<List<SiteModel>> findByInstitution(@PathVariable("institutionid") Long institutionid) {

        List<SiteModel> list = new ArrayList<SiteModel>();
        
        this.getDBService().getSites(institutionid).forEach(item -> list.add( siteModelService.model(item)));

        return new ResponseEntity<List<SiteModel>>(list, HttpStatus.OK);
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
