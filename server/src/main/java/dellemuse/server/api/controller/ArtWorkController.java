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

import dellemuse.model.ArtWorkModel;
import dellemuse.model.logging.Logger;
import dellemuse.server.api.model.ArtWorkModelService;
import dellemuse.server.db.model.ArtWork;
import dellemuse.server.db.service.ArtWorkDBService;
import dellemuse.server.security.SecurityService;

@RestController
@RequestMapping(value = "/artwork")
public class ArtWorkController extends BaseController<ArtWork, ArtWorkModel> {

    @SuppressWarnings("unused")
    static private dellemuse.model.logging.Logger logger = Logger.getLogger(ArtWorkController.class.getName());

    @JsonIgnore
    @Autowired
    private final ArtWorkDBService dbService;

    @JsonIgnore
    @Autowired
    private final ArtWorkModelService modelService;

    @JsonIgnore
    @Autowired
    private final SecurityService securityService;

    public ArtWorkController(ArtWorkDBService db, ArtWorkModelService modelService, SecurityService securityService) {
        super(securityService);
        this.modelService = modelService;
        this.dbService = db;
        this.securityService=securityService;
    }
    
    @RequestMapping(value = "/getbyname/{name}", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<Optional<ArtWorkModel>> get(@PathVariable("name") String name) {
        List<ArtWork> list = this.getDBService().getByName(name);
        if (list == null || list.isEmpty())
            return new ResponseEntity<Optional<ArtWorkModel>>(Optional.empty(), HttpStatus.OK);
        return new ResponseEntity<Optional<ArtWorkModel>>(Optional.of(this.getModelService().model(list.get(0))), HttpStatus.OK);
    }

    @Override
    public ArtWorkDBService getDBService() {
        return dbService;
    }

    public ArtWorkModelService getModelService() {
        return modelService;
    }
    
    protected SecurityService getSecurityService() {
        return securityService;
    }
}


/**
@RequestMapping(value = "/list", produces = "application/json", method = RequestMethod.GET)
public ResponseEntity<List<ArtWorkModel>> findAll() {
    List<ArtWorkModel> list = new ArrayList<ArtWorkModel>();
    this.getDBService().getRepository().findAll().forEach(item -> list.add(this.getModelService().model(item)));
    return new ResponseEntity<List<ArtWorkModel>>(list, HttpStatus.OK);
}

@PostMapping(value = "/save", produces = "application/json")
//@PostMapping("/save")
public ResponseEntity<ArtWorkModel> save(@RequestBody ArtWorkModel artworktypemodel) {
    this.getDBService().getRepository().save(this.getModelService().source(artworktypemodel));
    return new ResponseEntity<ArtWorkModel>(artworktypemodel, HttpStatus.OK);
}

@PostMapping(value = "/create/{name}", produces = "application/json")
//@PostMapping("/create/{name}")
public ResponseEntity<ArtWorkModel> create(@PathVariable("name") String name) {
    ArtWork artWork = this.getDBService().create(name, getSecurityService().getRootUser());
    return new ResponseEntity<ArtWorkModel>(artWork.model(), HttpStatus.OK);
}

@RequestMapping(value = "/exists/{id}", method = RequestMethod.GET, produces = "application/json")
public ResponseEntity<Boolean> exists(@PathVariable("id") Long id) {
    return new ResponseEntity<Boolean>(Boolean.valueOf(this.getDBService().getRepository().existsById(id)), HttpStatus.OK);
}

@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
public ResponseEntity<Boolean> delete(@PathVariable("id") Long id) {
    this.getDBService().getRepository().deleteById(id);
    return new ResponseEntity<Boolean>(Boolean.valueOf(true), HttpStatus.OK);
}
*/
