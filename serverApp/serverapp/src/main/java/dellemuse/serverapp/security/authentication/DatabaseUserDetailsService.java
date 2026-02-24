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

    @Override
    public UserDetails loadUserByUsername(String username) throws RuntimeException {

    	Optional<User> o_user =  getUserDBService().findByUsername(username);
        		
   		if (o_user.isEmpty())
      			throw new  RuntimeException("User not found: " + username);

            
        User user = getUserDBService().findWithDeps(o_user.get().getId()).get();
        
        logger.debug("DB USER = " + user.getUsername());
        logger.debug("DB PASS = " + user.getPassword());
        
        UserDetails ud = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRolesAsString().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet())
        );
        
        
        logger.debug(username);
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