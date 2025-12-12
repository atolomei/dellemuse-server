package dellemuse.serverapp.security.authentication;

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

        User user =   o_user.get();     

        //org.springframework.security.core.userdetails.User retUser;
        //SimpleGrantedAuthority sa = new SimpleGrantedAuthority("READ");
        //List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        //list.add(sa);
        
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRolesAsString().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet())
        );
        
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
    }

	private UserDBService getUserDBService() {
		return this.userDBService;
	}
	
	
	
}
