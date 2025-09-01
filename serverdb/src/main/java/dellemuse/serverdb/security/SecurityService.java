package dellemuse.serverdb.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dellemuse.serverdb.ServerDBSettings;
import dellemuse.serverdb.model.User;
import dellemuse.serverdb.service.UserDBService;
import dellemuse.serverdb.service.base.BaseService;


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

    protected UserDBService getUserDBService() {
        return userDBService;
    }


    
    

}
