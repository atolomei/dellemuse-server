package dellemuse.serverapp.security.config;



import com.giffing.wicket.spring.boot.context.security.AuthenticatedWebSessionConfig;
import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;

import dellemuse.serverapp.page.security.LoginPage;

import org.apache.wicket.Page;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.protocol.http.WebSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class WicketSecurityConfig {
	 @Bean
	 public AuthenticatedWebSessionConfig authenticatedWebSessionConfig() {
		 return new AuthenticatedWebSessionConfig() {
			@Override
			public Class<? extends AbstractAuthenticatedWebSession> getAuthenticatedWebSessionClass() {
				return SecureWebSession.class;
			}
	      };
	 }
	 
}
