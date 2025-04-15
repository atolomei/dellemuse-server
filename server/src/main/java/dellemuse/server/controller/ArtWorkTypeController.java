package dellemuse.server.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.server.db.model.ArtWorkType;
import dellemuse.server.db.service.ArtWorkTypeDBService;
import dellemuse.util.Logger;


@RestController
@RequestMapping(value = "/artworktype")
public class ArtWorkTypeController extends BaseController {
            
    static private Logger logger = dellemuse.util.Logger.getLogger(ArtWorkTypeController.class.getName());
    
    @JsonIgnore
    @Autowired
    private  ArtWorkTypeDBService db;
    
    
    public ArtWorkTypeController(ArtWorkTypeDBService db) {
      this.db = db;
    }
    
    @RequestMapping(value = "/getbyname/{name}", method = RequestMethod.GET)
    public List<ArtWorkType> exists(@PathVariable("name") String name) {
        return this.getDB().getByName(name); 
    }
    
    @GetMapping("/list")
    public Iterable<ArtWorkType> findAllartworktypes() {
        return this.getDB().getRepository().findAll();
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public Optional<ArtWorkType> get(@PathVariable("id") Long id) {
        return this.getDB().findById(id.longValue()); 
    }
    
    @PostMapping("/save")
    public ArtWorkType save(@RequestBody ArtWorkType artworktype) {
      return this.getDB().getRepository().save(artworktype);
    }

    @PostMapping("/create")
    public void create() {
        logger.debug("here");
    }

    @RequestMapping(value = "/exists/{id}", method = RequestMethod.GET)
    public Boolean exists(@PathVariable("id") Long id) {
        return this.getDB().getRepository().existsById(id); 
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) {
        this.getDB().getRepository().deleteById(id);
    }
    
    public ArtWorkTypeDBService getDB() {
        return db;
    }
}
