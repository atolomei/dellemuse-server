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

import dellemuse.model.ArtExhibitionModel;
import dellemuse.model.logging.Logger;
import dellemuse.server.api.model.ArtExhibitionModelService;
import dellemuse.server.db.model.ArtExhibition;
import dellemuse.server.db.service.ArtExhibitionDBService;
import dellemuse.server.security.SecurityService;

@RestController
@RequestMapping(value = "/artexhibition")
public class ArtExhibitionController extends BaseController<ArtExhibition, ArtExhibitionModel> {

    @SuppressWarnings("unused")
    static private dellemuse.model.logging.Logger logger = Logger.getLogger(ArtExhibitionController.class.getName());

    @JsonIgnore
    @Autowired
    private final ArtExhibitionDBService dbService;

    @JsonIgnore
    @Autowired
    private final ArtExhibitionModelService modelService;

    @JsonIgnore
    @Autowired
    private final SecurityService securityService;

    public ArtExhibitionController(ArtExhibitionDBService db, ArtExhibitionModelService modelService, SecurityService securityService) {
        super(securityService);
        this.modelService = modelService;
        this.dbService = db;
        this.securityService=securityService;
    }
    
    @RequestMapping(value = "/getbyname/{name}", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<Optional<ArtExhibitionModel>> get(@PathVariable("name") String name) {
        List<ArtExhibition> list = this.getDBService().getByName(name);
        if (list == null || list.isEmpty())
            return new ResponseEntity<Optional<ArtExhibitionModel>>(Optional.empty(), HttpStatus.OK);
        return new ResponseEntity<Optional<ArtExhibitionModel>>(Optional.of(this.getModelService().model(list.get(0))), HttpStatus.OK);
    }

    @Override
    public ArtExhibitionDBService getDBService() {
        return dbService;
    }

    public ArtExhibitionModelService getModelService() {
        return modelService;
    }
    
    protected SecurityService getSecurityService() {
        return securityService;
    }
}






