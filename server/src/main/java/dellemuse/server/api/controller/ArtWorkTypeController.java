package dellemuse.server.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.ArtWorkTypeModel;
import dellemuse.model.logging.Logger;
import dellemuse.server.api.model.ArtWorkTypeModelService;
import dellemuse.server.db.model.ArtWorkType;
import dellemuse.server.db.service.ArtWorkTypeDBService;
import dellemuse.server.error.ObjectNotFoundException;
import dellemuse.server.security.SecurityService;

/**
 * 
 * 
 */
@RestController
@RequestMapping(value = "/artworktype")
public class ArtWorkTypeController extends BaseController<ArtWorkType, ArtWorkTypeModel> {
            
    @SuppressWarnings("unused")
    static private dellemuse.model.logging.Logger logger = Logger.getLogger(ArtWorkTypeController.class.getName());
    
    @JsonIgnore
    @Autowired
    private  final ArtWorkTypeDBService dbService;
    
    @JsonIgnore
    @Autowired
    private final ArtWorkTypeModelService modelService;
    
    public ArtWorkTypeController(ArtWorkTypeDBService db, ArtWorkTypeModelService modelService, SecurityService securityService) {
        super(securityService);
      this.modelService=modelService;
      this.dbService = db;
    }
    
    @RequestMapping(value = "/getbyname/{name}", method = RequestMethod.GET)
    public ResponseEntity<Optional<ArtWorkTypeModel>> get(@PathVariable("name") String name) {
        List<ArtWorkType> list = this.getDBService().getByName(name);
        if (list==null)
            return new ResponseEntity<Optional<ArtWorkTypeModel>>(Optional.empty(), HttpStatus.OK);
        return new ResponseEntity<Optional<ArtWorkTypeModel>>(Optional.of(this.getModelService().model(list.get(0))), HttpStatus.OK);
    }
    
    @GetMapping("/list")
    public ResponseEntity<List<ArtWorkTypeModel>> findAll() {
        List<ArtWorkTypeModel> list = new ArrayList<ArtWorkTypeModel>();
        this.getDBService().getRepository().findAll().forEach( item -> list.add(this.getModelService().model(item)));
        return new ResponseEntity<List<ArtWorkTypeModel>>(list, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public ResponseEntity<ArtWorkTypeModel> get(@PathVariable("id") Long id) {
        Optional<ArtWorkType> item =  this.getDBService().findById(id.longValue());
        if (!item.isPresent())
            throw new ObjectNotFoundException(dellemuse.model.error.ErrorCode.OBJECT_NOT_FOUND, 
                                              String.format(ArtWorkType.class.getSimpleName() + " does not exist -> id: %s", id));            
            return new ResponseEntity<ArtWorkTypeModel>(this.getModelService().model(item.get()), HttpStatus.OK);
    }
    
    @PostMapping("/save")
    public ResponseEntity<ArtWorkTypeModel> save(@RequestBody ArtWorkTypeModel artworktypemodel) {
        this.getDBService().getRepository().save(this.getModelService().source(artworktypemodel));
        return new ResponseEntity<ArtWorkTypeModel>(artworktypemodel, HttpStatus.OK);
    }

    public ArtWorkTypeDBService getDBService() {
        return dbService;
    }
    
    public ArtWorkTypeModelService getModelService() {
        return modelService;
    }
}

