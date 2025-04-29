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

import dellemuse.model.ArtExhibitionGuideModel;
import dellemuse.model.ArtExhibitionModel;
import dellemuse.model.ArtWorkModel;
import dellemuse.model.logging.Logger;
import dellemuse.server.api.model.ArtExhibitionGuideModelService;
import dellemuse.server.api.model.ArtExhibitionModelService;
import dellemuse.server.api.model.ArtWorkModelService;
import dellemuse.server.db.model.ArtExhibition;
import dellemuse.server.db.model.ArtExhibitionGuide;
import dellemuse.server.db.model.ArtWork;
import dellemuse.server.db.service.ArtExhibitionDBService;
import dellemuse.server.db.service.ArtExhibitionGuideDBService;
import dellemuse.server.db.service.ArtWorkDBService;
import dellemuse.server.security.SecurityService;

@RestController
@RequestMapping(value = "/artexhibitionguide")
public class ArtExhibitionGuideController extends BaseController<ArtExhibitionGuide, ArtExhibitionGuideModel> {

    @SuppressWarnings("unused")
    static private dellemuse.model.logging.Logger logger = Logger.getLogger(ArtExhibitionGuideController.class.getName());

    @JsonIgnore
    @Autowired
    private final ArtExhibitionGuideDBService dbService;

    @JsonIgnore
    @Autowired
    private final ArtExhibitionGuideModelService modelService;

    @JsonIgnore
    @Autowired
    private final SecurityService securityService;

    public ArtExhibitionGuideController(ArtExhibitionGuideDBService db, ArtExhibitionGuideModelService modelService, SecurityService securityService) {
        super(securityService);
        this.modelService = modelService;
        this.dbService = db;
        this.securityService=securityService;
    }
    
    @RequestMapping(value = "/getbyname/{name}", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<Optional<ArtExhibitionGuideModel>> get(@PathVariable("name") String name) {
        List<ArtExhibitionGuide> list = this.getDBService().getByName(name);
        if (list == null || list.isEmpty())
            return new ResponseEntity<Optional<ArtExhibitionGuideModel>>(Optional.empty(), HttpStatus.OK);
        return new ResponseEntity<Optional<ArtExhibitionGuideModel>>(Optional.of(this.getModelService().model(list.get(0))), HttpStatus.OK);
    }

    @Override
    public ArtExhibitionGuideDBService getDBService() {
        return dbService;
    }

    public ArtExhibitionGuideModelService getModelService() {
        return modelService;
    }
    
    protected SecurityService getSecurityService() {
        return securityService;
    }
}






