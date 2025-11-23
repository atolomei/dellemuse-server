package dellemuse.serverapp.security.config;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;


import dellemuse.model.logging.Logger;

public class MyAuthtenticatedWebSession extends AuthenticatedWebSession {

	static private Logger logger = Logger.getLogger(MyAuthtenticatedWebSession.class.getName());
 
	private static final long serialVersionUID = 1L;

	@Autowired
	@SpringBean(name = "authenticationManager")
    private AuthenticationManager authenticationManager;

	 
	public MyAuthtenticatedWebSession(Request request) {
		super(request);
		//logger.debug(this.getClass().getName());
		//logger.error("SHOULD NOT BE HERE");
		
	}

    @Override
    public boolean authenticate(String username, String password) {
        
    	throw new RuntimeException("SHOULD NOT BE HERE");

    	/**
    	try {
        	
    		logger.debug("u:"  + username + " | p:  +" + password);
            
    		Authentication authentication = authenticationManager.authenticate(
                 new UsernamePasswordAuthenticationToken(username, password)
             );
             SecurityContextHolder.getContext().setAuthentication(authentication);
             return authentication.isAuthenticated();
         } catch (AuthenticationException e) {
             return false;
         }
         **/
     }
     
     @Override
     public Roles getRoles() {
         
    	 throw new RuntimeException("SHOULD NOT BE HERE");
    	 /**
    	 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         
    	 logger.debug("getRoles");
         
    	 if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {

        	 // Map Spring Security roles to Wicket roles if needed, or just return them
             
        	 return new Roles(authentication.getAuthorities().stream()
                 .map(grantedAuthority -> grantedAuthority.getAuthority())
                 .toArray(String[]::new));
         }
         return new Roles();
   **/
    	 
     }

}
