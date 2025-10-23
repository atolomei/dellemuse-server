package dellemuse.serverapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.UserDBService;
import dellemuse.serverapp.serverdb.service.base.BaseService;


@Service
public class SecurityService extends BaseService {
    
    @Autowired
    private final UserDBService userDBService;

    public SecurityService(ServerDBSettings settings, UserDBService userDBService) {
        super(settings);
        this.userDBService=userDBService;
    }
    
    
    public User getRootUser() {
        return getUserDBService().findRoot();
    }

    public User getSessionUser() {
        return getUserDBService().findRoot();
    }

    
    protected UserDBService getUserDBService() {
        return userDBService;
    }


    
    

}
