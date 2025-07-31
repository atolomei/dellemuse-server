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

import dellemuse.model.ArtExhibitionItemModel;
import dellemuse.model.ArtExhibitionModel;
import dellemuse.model.ArtWorkModel;
import dellemuse.model.FloorModel;
import dellemuse.model.GuideContentModel;
import dellemuse.model.RoomModel;
import dellemuse.model.SiteModel;
import dellemuse.model.logging.Logger;
import dellemuse.model.util.Check;
import dellemuse.server.api.model.ArtExhibitionItemModelService;
import dellemuse.server.api.model.ArtExhibitionModelService;
import dellemuse.server.api.model.ArtWorkModelService;
import dellemuse.server.api.model.FloorModelService;
import dellemuse.server.api.model.GuideContentModelService;
import dellemuse.server.api.model.RoomModelService;
import dellemuse.server.api.model.SiteModelService;

import dellemuse.server.db.model.Site;
import dellemuse.server.db.service.SiteDBService;
import dellemuse.server.error.ObjectNotFoundException;
import dellemuse.server.security.SecurityService;

/**
 * 
 * /site/artexhibitions/{siteid}
 *
 * /site/floors/{siteid}
 * /site/floor/rooms/{floorid}
 * 
 * /site/artexhibitionitems/{siteid}
 * /site/guidecontents/{siteid}
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
    private final FloorModelService floorModelService;
    
    @JsonIgnore
    @Autowired
    private final RoomModelService roomModelService;
    
    @JsonIgnore
    @Autowired
    private final ArtExhibitionModelService artExhibitionModelService;
    
    @JsonIgnore
    @Autowired
    private final ArtExhibitionItemModelService artExhibitionItemModelService;
    
    @JsonIgnore
    @Autowired
    private final ArtWorkModelService artWorkModelService;
    
    @JsonIgnore
    @Autowired
    private final GuideContentModelService guideContentModelService;
    
    @JsonIgnore
    @Autowired
    private final SecurityService securityService;

    
    public SiteController(SiteDBService dbService, SiteModelService modelService, 
    		SecurityService securityService, 
    		FloorModelService floorModelService, 
    		RoomModelService roomModelService,
    		ArtExhibitionModelService artExhibitionModelService,
     		ArtExhibitionItemModelService artExhibitionItemModelService,
     		 ArtWorkModelService artWorkModelService,
     		GuideContentModelService guideContentModelService
     		
    		) {
    	
        super(securityService);
  
        this.modelService = modelService;
        this.dbService = dbService;
        this.securityService = securityService;
        this.floorModelService=floorModelService;
        this.roomModelService = roomModelService;
        this.artExhibitionModelService=artExhibitionModelService;
        this.artExhibitionItemModelService=artExhibitionItemModelService;
        this.artWorkModelService= artWorkModelService;
        this.guideContentModelService=guideContentModelService;
        
    }

    /**
     * @return
     */
    @GetMapping(value = "/shortname/{name}")
    public ResponseEntity<SiteModel> getByShortName(@PathVariable("name") String name) {
    	Check.requireNonNull(name, "name is null");
        Optional<Site> site= this.getDBService().findByShortName(name.toLowerCase().trim());
        if (site.isEmpty()) 
            throw new ObjectNotFoundException("site not found -> " + name);
        
        return new ResponseEntity<SiteModel>(modelService.model(site.get()), HttpStatus.OK);
    }
    
    /**
     * @return
     */
    @GetMapping(value = "/floors/{siteid}")
    public ResponseEntity<List<FloorModel>> findFloors(@PathVariable("siteid") Long siteid) {
    	
    	Check.requireNonNull(siteid, "siteid is null");
    	
    	List<FloorModel> list = new ArrayList<FloorModel>();
        
        this.getDBService().getFloors(siteid).forEach(item -> list.add(floorModelService.model(item)));
        
        return new ResponseEntity<List<FloorModel>>(list, HttpStatus.OK);
    }

    /**
     * @return
     */
    @GetMapping(value = "/floor/rooms/{floorid}")
    public ResponseEntity<List<RoomModel>> findRooms(@PathVariable("floorid") Long siteid) {
    	Check.requireNonNull(siteid, "siteid is null");
    	
        List<RoomModel> list = new ArrayList<RoomModel>();
        this.getDBService().getRooms(siteid).forEach(item -> list.add( roomModelService.model(item)));
        return new ResponseEntity<List<RoomModel>>(list, HttpStatus.OK);
    }

    /**
     * @return
     */
    @GetMapping(value = "/artexhibitions/{siteid}")
    public ResponseEntity<List<ArtExhibitionModel>> findArtExhibitions(@PathVariable("siteid") Long siteid) {
                    
    	Check.requireNonNull(siteid, "siteid is null");
    	
        List<ArtExhibitionModel> list = new ArrayList<ArtExhibitionModel>();

     

        this.getDBService().getArtExhibitions(siteid).forEach(item -> list.add(artExhibitionModelService.model(item)));

        if (logger.isDebugEnabled()) {
            list.forEach(item -> logger.debug(item.toString()));
        }
        return new ResponseEntity<List<ArtExhibitionModel>>(list, HttpStatus.OK);
    }


    /**
     * @return
     */
    @GetMapping(value = "/artexhibitionitems/{siteid}")
    public ResponseEntity<List<ArtExhibitionItemModel>> findSiteArtExhibitionItems(@PathVariable("siteid") Long siteid) {
    
    	Check.requireNonNull(siteid, "siteid is null");
        
        List<ArtExhibitionItemModel> list = new ArrayList<ArtExhibitionItemModel>();
 
        this.getDBService().getSiteArtExhibitionItems(siteid).forEach(item -> list.add( artExhibitionItemModelService.model(item)));

        if (logger.isDebugEnabled()) {
            list.forEach(item -> logger.debug(item.toString()));
        }
        return new ResponseEntity<List<ArtExhibitionItemModel>>(list, HttpStatus.OK);
    }
    
    

    /**
     * @return
     */
    @GetMapping(value = "/artworks/{siteid}")
    public ResponseEntity<List<ArtWorkModel>> findSiteArtWorks(@PathVariable("siteid") Long siteid) {
                    
     	Check.requireNonNull(siteid, "siteid is null");
        
        List<ArtWorkModel> list = new ArrayList<ArtWorkModel>();

      

        this.getDBService().getSiteArtWork(siteid).forEach(item -> list.add( artWorkModelService.model( item )));

        if (logger.isDebugEnabled()) {
            list.forEach(item -> logger.debug(item.toString()));
        }
        return new ResponseEntity<List<ArtWorkModel>>(list, HttpStatus.OK);
    }


    /**
     * @return
     */
    @GetMapping(value = "/guidecontents/{siteid}")
    public ResponseEntity<List<GuideContentModel>> findSiteGuideContents(@PathVariable("siteid") Long siteid) {
                          
    	Check.requireNonNull(siteid, "siteid is null");
    	
        List<GuideContentModel> list = new ArrayList<GuideContentModel>();

       

        this.getDBService().getSiteGuideContent(siteid).forEach(item -> list.add( guideContentModelService.model( item )));

        if (logger.isDebugEnabled()) {
            list.forEach(item -> logger.debug(item.toString()));
        }
        return new ResponseEntity<List<GuideContentModel>>(list, HttpStatus.OK);
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
