package dellemuse.serverapp.security.authentication;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class AuthenticationManagerConfig {

	 private final DatabaseUserDetailsService userDetailsService;
	 
	 private final PasswordEncoder passwordEncoder;
	 
	 public AuthenticationManagerConfig(DatabaseUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
	        this.userDetailsService = userDetailsService;
	        this.passwordEncoder = passwordEncoder;
	    }
	 
	 @Bean
	 public DaoAuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder,
	                                                         DatabaseUserDetailsService userDetailsService) {
	     DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	     provider.setUserDetailsService(userDetailsService);
	     provider.setPasswordEncoder(passwordEncoder);
	     return provider;
	 }
	 
/**
	    @Bean
	    public DaoAuthenticationProvider authenticationProvider() {
	        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	        provider.setUserDetailsService(userDetailsService);
	        provider.setPasswordEncoder(passwordEncoder);
	        return provider;
	    }
**/
	 
	    @Bean
	    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
	        AuthenticationManager manager = config.getAuthenticationManager();
	        // Alternatively, if you want total control:
	        // return new ProviderManager(List.of(authenticationProvider()));
	        return manager;
	    }
	
	    
	    //@Bean
		//public PasswordEncoder passwordEncoder() {
		//    return new BCryptPasswordEncoder();
	    //	}
}
