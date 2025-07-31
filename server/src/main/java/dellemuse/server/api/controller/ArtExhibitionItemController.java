package dellemuse.server.api.controller;

import java.util.ArrayList;
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
import dellemuse.model.ArtExhibitionItemModel;
import dellemuse.model.ArtExhibitionModel;
import dellemuse.model.logging.Logger;
import dellemuse.server.api.model.ArtExhibitionItemModelService;
import dellemuse.server.api.model.ArtExhibitionModelService;
import dellemuse.server.db.model.ArtExhibition;
import dellemuse.server.db.model.ArtExhibitionGuide;
import dellemuse.server.db.model.ArtExhibitionItem;
import dellemuse.server.db.service.ArtExhibitionDBService;
import dellemuse.server.db.service.ArtExhibitionItemDBService;
import dellemuse.server.error.ObjectNotFoundException;
import dellemuse.server.security.SecurityService;

@RestController
@RequestMapping(value = "/artexhibitionitem")
public class ArtExhibitionItemController extends BaseController<ArtExhibitionItem, ArtExhibitionItemModel> {

    @SuppressWarnings("unused")
    static private dellemuse.model.logging.Logger logger = Logger.getLogger(ArtExhibitionItemController.class.getName());

    @JsonIgnore
    @Autowired
    private final ArtExhibitionItemDBService dbService;

    @JsonIgnore
    @Autowired
    private final ArtExhibitionItemModelService modelService;

    @JsonIgnore
    @Autowired
    private final SecurityService securityService;

    public ArtExhibitionItemController(ArtExhibitionItemDBService db, ArtExhibitionItemModelService modelService, SecurityService securityService) {
        super(securityService);
        this.modelService = modelService;
        this.dbService = db;
        this.securityService=securityService;
    }
    
    @Override
    public ArtExhibitionItemDBService getDBService() {
        return dbService;
    }

    public ArtExhibitionItemModelService getModelService() {
        return modelService;
    }
    
    protected SecurityService getSecurityService() {
        return securityService;
    }
}

