package dellemuse.server.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.ArtExhibitionModel;
import dellemuse.model.FloorModel;
import dellemuse.model.RoomModel;
import dellemuse.model.SiteModel;
import dellemuse.model.logging.Logger;
import dellemuse.server.api.model.SiteModelService;
import dellemuse.server.db.model.Site;
import dellemuse.server.db.service.SiteDBService;
import dellemuse.server.error.ObjectNotFoundException;
import dellemuse.server.security.SecurityService;

/**
 * 
 * 
 * 
 * /site/artexhibitions/{siteid}
 * /site/floors/{siteid}
 * /site/floor/rooms/{floorid}
 * 
 */
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
    @GetMapping(value = "/shortname/{name}")
    public ResponseEntity<SiteModel> getByShortName(@PathVariable("name") String name) {
        Optional<Site> site= this.getDBService().findByShortName(name);
        if (site.isEmpty()) 
            throw new ObjectNotFoundException("site not found -> " + name);
        return new ResponseEntity<SiteModel>(site.get().model(), HttpStatus.OK);
    }
    
    /**
     * @return
     */
    @GetMapping(value = "/floors/{siteid}")
    public ResponseEntity<List<FloorModel>> findFloors(@PathVariable("siteid") Long siteid) {
        List<FloorModel> list = new ArrayList<FloorModel>();
        this.getDBService().getFloors(siteid).forEach(item -> list.add(item.model()));
        return new ResponseEntity<List<FloorModel>>(list, HttpStatus.OK);
    }


    /**
     * @return
     */
    @GetMapping(value = "/floor/rooms/{floorid}")
    public ResponseEntity<List<RoomModel>> findRooms(@PathVariable("floorid") Long siteid) {
        List<RoomModel> list = new ArrayList<RoomModel>();
        this.getDBService().getRooms(siteid).forEach(item -> list.add(item.model()));
        return new ResponseEntity<List<RoomModel>>(list, HttpStatus.OK);
    }

    /**
     * @return
     */
    @GetMapping(value = "/artexhibitions/{siteid}")
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

        
    

    @Override
    public SiteDBService getDBService() {
        return dbService;
    }

    @Override
    public SiteModelService getModelService() {
        return modelService;
    }
}
