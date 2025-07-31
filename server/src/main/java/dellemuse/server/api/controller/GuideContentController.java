package dellemuse.server.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.GuideContentModel;
import dellemuse.model.logging.Logger;
import dellemuse.server.api.model.GuideContentModelService;
import dellemuse.server.db.model.GuideContent;
import dellemuse.server.db.service.GuideContentDBService;
import dellemuse.server.security.SecurityService;

@RestController
@RequestMapping(value = "/guidecontent")
public class GuideContentController extends BaseController<GuideContent, GuideContentModel> {

    @SuppressWarnings("unused")
    static private dellemuse.model.logging.Logger logger = Logger.getLogger(GuideContentController.class.getName());

    @JsonIgnore
    @Autowired
    private final GuideContentDBService dbService;

    @JsonIgnore
    @Autowired
    private final GuideContentModelService modelService;

    @JsonIgnore
    @Autowired
    private final SecurityService securityService;

    public GuideContentController(GuideContentDBService db, GuideContentModelService modelService, SecurityService securityService) {
        super(securityService);
        this.modelService = modelService;
        this.dbService = db;
        this.securityService=securityService;
    }
    
    @Override
    @RequestMapping(value = "/get/{id}", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<GuideContentModel> get(@PathVariable("id") Long id) {
        ResponseEntity<GuideContentModel> model = super.get(id);
        return model;
        
    }
    
    @RequestMapping(value = "/getbyname/{name}", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<Optional<GuideContentModel>> get(@PathVariable("name") String name) {
        List<GuideContent> list = this.getDBService().getByName(name);
        if (list == null || list.isEmpty())
            return new ResponseEntity<Optional<GuideContentModel>>(Optional.empty(), HttpStatus.OK);
        return new ResponseEntity<Optional<GuideContentModel>>(Optional.of(this.getModelService().model(list.get(0))), HttpStatus.OK);
    }

    @Override
    public GuideContentDBService getDBService() {
        return dbService;
    }

    public GuideContentModelService getModelService() {
        return modelService;
    }
    
    protected SecurityService getSecurityService() {
        return securityService;
    }
}


