package dellemuse.server.controller;

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
import dellemuse.server.db.model.ArtWorkType;
import dellemuse.server.db.service.ArtWorkTypeDBService;
import dellemuse.server.model.ArtWorkTypeModelService;

@RestController
@RequestMapping(value = "/artworktype")
public class ArtWorkTypeController extends BaseController {
            
    static private dellemuse.model.logging.Logger logger = Logger.getLogger(ArtWorkTypeController.class.getName());
    
    @JsonIgnore
    @Autowired
    private  final ArtWorkTypeDBService dbService;
    
    @JsonIgnore
    @Autowired
    private final ArtWorkTypeModelService modelService;
    
    public ArtWorkTypeController(ArtWorkTypeDBService db, ArtWorkTypeModelService modelService) {
      this.modelService=modelService;
      this.dbService = db;
    }
    
    @RequestMapping(value = "/getbyname/{name}", method = RequestMethod.GET)
    public ResponseEntity<Optional<ArtWorkTypeModel>> get(@PathVariable("name") String name) {
        
        List<ArtWorkType> list = this.getDBService().getByName(name);
        if (list==null)
            return new ResponseEntity<Optional<ArtWorkTypeModel>>(Optional.empty(), HttpStatus.OK);
        
        ArtWorkType type = list.get(0);
        return new ResponseEntity<Optional<ArtWorkTypeModel>>(Optional.of(this.getModelService().getModel(type)), HttpStatus.OK);
        
    }
    
    
    @GetMapping("/list")
    public ResponseEntity<List<ArtWorkTypeModel>> findAll() {
        List<ArtWorkTypeModel> list = new ArrayList<ArtWorkTypeModel>();
        this.getDBService().getRepository().findAll().forEach( item -> list.add(this.getModelService().getModel(item)));
        return new ResponseEntity<List<ArtWorkTypeModel>>(list, HttpStatus.OK);
    }
    

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public ResponseEntity<ArtWorkTypeModel> get(@PathVariable("id") Long id) {
        Optional<ArtWorkType> item =  this.getDBService().findById(id.longValue());
        if (item.isPresent())
            return new ResponseEntity<ArtWorkTypeModel>(this.getModelService().getModel(item.get()), HttpStatus.OK);

        return null;
    }
    
    @PostMapping("/save")
    public ResponseEntity<ArtWorkTypeModel> save(@RequestBody ArtWorkTypeModel artworktypemodel) {
        this.getDBService().getRepository().save(this.getModelService().getSource(artworktypemodel));
        return new ResponseEntity<ArtWorkTypeModel>(artworktypemodel, HttpStatus.OK);
        
    }


    @PostMapping("/create")
    public void create() {
        logger.debug("create here");
    }

    @RequestMapping(value = "/exists/{id}", method = RequestMethod.GET)
    public Boolean exists(@PathVariable("id") Long id) {
        return this.getDBService().getRepository().existsById(id); 
    }

    @RequestMapping(value = "/existsbyname/{name}", method = RequestMethod.GET)
    public Boolean existsNyName(@PathVariable("name") Long id) {
        //return this.getDB().getRepository().existsById(id); 
        return Boolean.FALSE;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) {
        this.getDBService().getRepository().deleteById(id);
    }
    
    public ArtWorkTypeDBService getDBService() {
        return dbService;
    }
    
    public ArtWorkTypeModelService getModelService() {
        return modelService;
    }
}
