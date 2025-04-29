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

import dellemuse.model.ArtExhibitionModel;
import dellemuse.model.SiteModel;
import dellemuse.model.logging.Logger;
import dellemuse.server.api.model.SiteModelService;
import dellemuse.server.db.model.Site;
import dellemuse.server.db.service.SiteDBService;
import dellemuse.server.security.SecurityService;

@RestController
@RequestMapping(value = "/site")
public class SiteController extends BaseController<Site, SiteModel> {

    static private dellemuse.model.logging.Logger logger = Logger.getLogger(SiteController.class.getName());

    @JsonIgnore
    @Autowired
    private final SiteDBService dbService;

    @JsonIgnore
    @Autowired
    private final SiteModelService modelService;

    @JsonIgnore
    @Autowired
    private final SecurityService securityService;

    public SiteController(SiteDBService dbService, SiteModelService modelService, SecurityService securityService) {
        super(securityService);
        this.modelService = modelService;
        this.dbService = dbService;
        this.securityService = securityService;
    }

    
    /**
     * @return
     */
    @GetMapping(value = "/exhibitions")
    public ResponseEntity<List<ArtExhibitionModel>> findArtExhibitions(@PathVariable("siteid") Long siteid) {
                    
        
        List<ArtExhibitionModel> list = new ArrayList<ArtExhibitionModel>();

        logger.debug("list -> " + getModelService().getClass().getSimpleName());

        if (logger.isDebugEnabled()) {
            this.getDBService().getArtExhibitions(siteid).forEach(item -> logger.debug(item.toString()));
        }

        this.getDBService().getArtExhibitions(siteid).forEach(item -> list.add(item.model()));

        if (logger.isDebugEnabled()) {
            list.forEach(item -> logger.debug(item.toString()));
        }

        return new ResponseEntity<List<ArtExhibitionModel>>(list, HttpStatus.OK);
        
    }

        
    /**
     * @return
     */
    @GetMapping(value = "/listbyinstitution/{institutionid}")
    public ResponseEntity<List<SiteModel>> findByInstitution(@PathVariable("institutionid") Long institutionid) {

        List<SiteModel> list = new ArrayList<SiteModel>();

        /**
        logger.debug("list -> " + getModelService().getClass().getSimpleName());

        if (logger.isDebugEnabled()) {
            this.getDBService().getSites(institutionid).forEach(item -> logger.debug(item.toString()));
        }

        this.getDBService().findSites(institutionid).forEach(item -> list.add(this.getModelService().model(item)));

        if (logger.isDebugEnabled()) {
            list.forEach(item -> logger.debug(item.toString()));
        }

        return new ResponseEntity<List<SiteModel>>(list, HttpStatus.OK);
        */
        return null;
        
    }

    @Override
    public SiteDBService getDBService() {
        return dbService;
    }

    @Override
    public SiteModelService getModelService() {
        return modelService;
    }
}
