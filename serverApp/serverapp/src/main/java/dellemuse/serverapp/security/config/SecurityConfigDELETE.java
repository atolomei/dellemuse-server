package dellemuse.serverapp.security.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import dellemuse.model.logging.Logger;
 
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.DelegatingAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
 

//@Configuration
//@EnableWebSecurity(debug = true)
public class SecurityConfigDELETE {

	static private Logger logger = Logger.getLogger(SecurityConfigDELETE.class.getName());@Bean
   
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                		"/signin",
                        "/signin/**",
                        "/wicket/**",
                        "/wicket/resource/**",
                        "/css/**",
                        "/js/**",
                        "/images/**"
                ).permitAll()
                //.anyRequest().authenticated()
                // ⭐ Let Wicket handle page authorization
                .anyRequest().permitAll()
            		)
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(customEntryPoint())
            )
            
            .securityContext(context -> context
                    .securityContextRepository(new HttpSessionSecurityContextRepository())
                    .requireExplicitSave(false)
                )
            
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .sessionFixation(fix -> fix.migrateSession())
                )

            .exceptionHandling(ex -> ex
                    .authenticationEntryPoint(customEntryPoint())
                )
            .oauth2Login(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .formLogin(form -> form.disable())
            .logout(logout -> logout
            	    .logoutUrl("/logout")
            	    .logoutSuccessUrl("/signin?logout")   // ← change this
            	    .invalidateHttpSession(true)
            	    .deleteCookies("JSESSIONID")
            	    .permitAll()
            	);
            
            	//.logout(logout -> logout.logoutUrl("/logout").permitAll());
        
        
        
        
        
        return http.build();
    }

    //@Bean
    public AuthenticationEntryPoint customEntryPoint() {
        LoginUrlAuthenticationEntryPoint loginEntryPoint =
                new LoginUrlAuthenticationEntryPoint("/signin");

        return (request, response, authException) -> {
            // Evitar redirigir si ya estamos en /signin
            if (new AntPathRequestMatcher("/signin/**").matches(request)) {
                // No redirige, deja pasar la request
                return;
            }
            // Para todo lo demás, redirige al login
            loginEntryPoint.commence(request, response, authException);
        };
    }

	
		 
		
		
		
		/**
		
		http
	    .authorizeHttpRequests(auth -> auth
	        //.requestMatchers("/login").permitAll()
	    		.requestMatchers("/dellemuselogin").permitAll()
	    		.anyRequest().authenticated()
	    )
	   
	    .formLogin(login -> login.disable()) 
		
	   // .formLogin(form -> form
       //         .loginPage("/login") // Custom login page URL
       //         .permitAll()
       // )
	    
	    .logout(logout -> logout
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login?logout") // Custom logout success URL
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID") // Delete specific cookies
                // .addLogoutHandler(myCustomLogoutHandler) // Add custom logout handler
                .permitAll()
                );


		
		http.csrf().disable();
		
		http.csrf(csrf -> csrf.disable());
*/		
		 
		
		/**
		http
			.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers("/login").permitAll() 
				.anyRequest().authenticated()
				)
			
			.httpBasic(Customizer.withDefaults())
			
			.formLogin(form -> form
	                 .loginPage("/login") // Custom login page URL
	                 .permitAll()
	         )
		
			.logout(logout -> logout
					.logoutUrl("/logout")
					.logoutSuccessUrl("/login?logout") // Custom logout success URL
	                .invalidateHttpSession(true)
	                .deleteCookies("JSESSIONID") // Delete specific cookies
	                // .addLogoutHandler(myCustomLogoutHandler) // Add custom logout handler
	                .permitAll()
	                );
		
		 http.csrf(AbstractHttpConfigurer::disable);
		*/ 
		
		
	 

	/**
	@Bean
	public AuthenticationManager authenticationManager(
			UserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);

		return new ProviderManager(authenticationProvider);
	}
**/
	/**
	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails userDetails = User.withDefaultPasswordEncoder()
			.username("user")
			.password("11")
			.roles("USER")
			.build();

		return new InMemoryUserDetailsManager(userDetails);
	}

	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
**/
	
 
	
}
