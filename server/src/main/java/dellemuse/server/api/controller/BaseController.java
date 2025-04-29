package dellemuse.server.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.DelleMuseModelObject;
import dellemuse.model.JsonObject;
import dellemuse.model.logging.Logger;
import dellemuse.server.api.model.ModelService;
import dellemuse.server.db.model.DelleMuseObject;
import dellemuse.server.db.service.DBService;
import dellemuse.server.error.ObjectNotFoundException;
import dellemuse.server.security.SecurityService;
import jakarta.annotation.PostConstruct;
/**
 * 
 * "/get/{id}"
 * "/list"
 * "/save"
 *  "/create/{name}"
 * "/exists/{id}"
 * "/delete/{id}"
 * 
 * 
 * @param <T>
 * @param <M>
 */
public abstract class BaseController<T extends DelleMuseObject, M extends DelleMuseModelObject>  extends JsonObject implements ApplicationContextAware {
                    
    private static final Logger logger = Logger.getLogger(BaseController.class.getName());
    
    @JsonIgnore
    @Autowired
    private ApplicationContext applicationContext;
    
    @JsonIgnore
    @Autowired
    private final SecurityService securityService;

    @Autowired
    public BaseController(SecurityService securityService) {
        this.securityService=securityService;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
    
    /**
     * @param id
     * @return
     */
    @RequestMapping(value = "/get/{id}", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<M> get(@PathVariable("id") Long id) {
        Optional<T> item = this.getDBService().findById(id.longValue());

        if (!item.isPresent())
            throw new ObjectNotFoundException(  dellemuse.model.error.ErrorCode.OBJECT_NOT_FOUND, 
                                                String.format(" id not exist -> id: %s", id));            

        return new ResponseEntity<M>( (M) this.getModelService().model(item.get()), HttpStatus.OK);
    }
    
    /**
     * @return
     */
    @GetMapping(value = "/list")
    public ResponseEntity<List<M>> findAll() {
        List<M> list = new ArrayList<M>();
        
        logger.debug( "list -> "+ getModelService().getClass().getSimpleName());
        
        if (logger.isDebugEnabled()) {
            this.getDBService().getRepository().findAll().forEach(item -> logger.debug(item.toString()));
        }
        
        this.getDBService().getRepository().findAll().forEach(item -> list.add(this.getModelService().model(item)));

        if (logger.isDebugEnabled()) {
            list.forEach(item -> logger.debug(item.toString()));
        }
        
        return new ResponseEntity<List<M>>(list, HttpStatus.OK);
    }
    
    @PostMapping(value = "/save", produces = "application/json")
    public ResponseEntity<M> save(@RequestBody M artworktypemodel) {
        this.getDBService().getRepository().save(this.getModelService().source(artworktypemodel));
        return new ResponseEntity<M>(artworktypemodel, HttpStatus.OK);
    }
    
    @PostMapping(value = "/create/{name}", produces = "application/json")
    public ResponseEntity<M> create(@PathVariable("name") String name) {
        T item = this.getDBService().create(name, getSecurityService().getRootUser());
        @SuppressWarnings("unchecked")
        M model = (M) item.model();
        return new ResponseEntity<M>(model, HttpStatus.OK);
    }

    @RequestMapping(value = "/exists/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Boolean> exists(@PathVariable("id") Long id) {
        return new ResponseEntity<Boolean>(Boolean.valueOf(this.getDBService().getRepository().existsById(id)), HttpStatus.OK);
    }

    /**
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<Boolean> delete(@PathVariable("id") Long id) {
        getDBService().getRepository().deleteById(id);
        return new ResponseEntity<Boolean>(Boolean.valueOf(true), HttpStatus.OK);
    }
    
    public abstract DBService<T, Long> getDBService();
    public abstract ModelService<T, M> getModelService();

    protected SecurityService getSecurityService() {
        return securityService;
    }
    
    @PostConstruct
    protected void init() {
    }
    
}
