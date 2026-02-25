package dellemuse.serverapp.security.authentication;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.User;
 
import dellemuse.serverapp.serverdb.service.UserDBService;
import dellemuse.serverapp.serverdb.service.base.BaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
 
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomOAuth2UserService  extends DefaultOAuth2UserService   {
	
	static private Logger logger = Logger.getLogger(CustomOAuth2UserService .class.getName());
	
	 @Autowired
	private final UserDBService userDBService;

	 
	 @Autowired
	 private final ServerDBSettings settings;

    public CustomOAuth2UserService(UserDBService userDBService, ServerDBSettings settings) {
        this.userDBService = userDBService;
        this.settings=settings;
    }

    
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        OAuth2User oauthUser = super.loadUser(userRequest);

        String email = oauthUser.getAttribute("email");
        String googleId = oauthUser.getAttribute("sub");

        Optional<User> oUser = userDBService.findByEmail(email);

        User user;

        if (oUser.isPresent()) {
            user = oUser.get();

            // link Google account if not already linked
            if (user.getProviderId() == null) {
                user.setAuthProvider("GOOGLE");
                user.setProviderId(googleId);
                userDBService.save(user);
            }

        } else {
            // OPTIONAL: auto-create user
            user = new User();
            user.setUsername(email);
            user.setEmail(email);
            user.setAuthProvider("GOOGLE");
            user.setProviderId(googleId);
            userDBService.save(user);
        }

        return new DefaultOAuth2User(
                user.getRolesAsString().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet()),
                oauthUser.getAttributes(),
                "email"
        );
    }

}