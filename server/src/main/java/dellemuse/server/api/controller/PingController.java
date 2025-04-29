package dellemuse.server.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.UserModel;
import dellemuse.model.logging.Logger;
import dellemuse.server.api.model.ModelService;
import dellemuse.server.db.model.User;
import dellemuse.server.db.service.DBService;
import dellemuse.server.security.SecurityService;

@RestController
public class PingController extends BaseController<User, UserModel> {

    @SuppressWarnings("unused")
    static private dellemuse.model.logging.Logger logger = Logger.getLogger(PingController.class.getName());

    @JsonIgnore
    @Autowired
    private final SecurityService securityService;

    public PingController(SecurityService securityService) {
        super(securityService);
        this.securityService=securityService;
    }
    
    @RequestMapping(value = "/ping", produces = "application/json", method = RequestMethod.GET)
    public ResponseEntity<String> ping() {
          return new ResponseEntity<String>("ok", HttpStatus.OK);
    }

    protected SecurityService getSecurityService() {
        return securityService;
    }
    
    @Override
    public DBService<User, Long> getDBService() {
        return null;
    }

    @Override
    public ModelService<User, UserModel> getModelService() {
        return null;
    }
}





