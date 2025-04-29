package dellemuse.server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dellemuse.server.BaseService;
import dellemuse.server.Settings;
import dellemuse.server.db.model.User;
import dellemuse.server.db.service.UserDBService;


@Service
public class SecurityService extends BaseService {

    
    @Autowired
    private final UserDBService userDBService;
    

    public SecurityService(Settings settings, UserDBService userDBService) {
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
