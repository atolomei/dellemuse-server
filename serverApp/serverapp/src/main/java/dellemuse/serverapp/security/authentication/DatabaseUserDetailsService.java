package dellemuse.serverapp.security.authentication;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.User;
 
import dellemuse.serverapp.serverdb.service.UserDBService;
import dellemuse.serverapp.serverdb.service.base.BaseService;
  
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
 
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DatabaseUserDetailsService extends BaseService implements UserDetailsService {
	
	static private Logger logger = Logger.getLogger(DatabaseUserDetailsService .class.getName());
	
	private final UserDBService userDBService;

    public DatabaseUserDetailsService(UserDBService userDBService, ServerDBSettings settings) {
    	super(settings);
        this.userDBService = userDBService;
    }

    
    private boolean looksLikeEmail(String v) {
	    return v.contains("@");
	}

	private boolean looksLikePhone(String v) {
	    return v.matches("^[0-9+()\\-\\s]{6,20}$");
	}
	
	 
	
    @Override
    public UserDetails loadUserByUsername(String identifier) throws RuntimeException {

    	
    	//Optional<User> o_user =  getUserDBService().findByEmail(email);
    	//Optional<User> o_user =	getUserDBService().findByUsernameOrEmailOrPhone(identifier);
    	//Optional<User> o_user =  getUserDBService().findByUsername(email);
    	//if (o_user.isEmpty())
      	//	throw new RuntimeException("User not found  by identifier -> " + identifier);
    	
    	Optional<User> oUser;

        if (looksLikeEmail(identifier)) {
            oUser = userDBService.findByEmail(User.normalizeEmail(identifier));
        } else if (looksLikePhone(identifier)) {
            oUser = userDBService.findByPhone(User.normalizePhone(identifier));
        } else {
            oUser = userDBService.findByUsernameOrEmailOrPhone(identifier);
        }

        if (oUser.isEmpty()) {
        	throw new RuntimeException("User not found  for identifier -> " + identifier);
        }
        
            
        User user = getUserDBService().findWithDeps(oUser.get().getId()).get();
        
        logger.debug("DB USER = " + user.getUsername());
        logger.debug("DB PASS = " + user.getPassword());
        
        UserDetails ud = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRolesAsString().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet())
        );
        
        
        logger.debug("Roles for -> " + user.getUsername() + " i. " + identifier);
        ud.getAuthorities().forEach(i-> logger.debug( i.toString()));
   
        return ud;
     
    }

	private UserDBService getUserDBService() {
		return this.userDBService;
	}
	
	
	
}



//retUser = new  org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), list);
//return retUser;

/**
	return new org.springframework.security.core.userdetails.User(
        user.getUsername(),
        user.getPassword(),
        user.getRoles().stream()
        .map(SimpleGrantedAuthority::new)
         .collect(Collectors.toSet())
);
**/

//org.springframework.security.core.userdetails.User retUser;
//SimpleGrantedAuthority sa = new SimpleGrantedAuthority("READ");
//List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
//list.add(sa);