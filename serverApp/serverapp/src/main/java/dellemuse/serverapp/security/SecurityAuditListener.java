package dellemuse.serverapp.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.DelleMuseAuditDBService;
import dellemuse.serverapp.serverdb.service.UserDBService;

@Component
public class SecurityAuditListener {

	private static final Logger logger = Logger.getLogger(SecurityAuditListener.class.getName());

	@Autowired
	private DelleMuseAuditDBService auditService;

	@Autowired
	private UserDBService userService;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@EventListener
	public void handleAuthenticationSuccess(AuthenticationSuccessEvent event) {
		Authentication auth = event.getAuthentication();
		String identifier = auth.getName();
		logger.debug("handleAuthenticationSuccess for identifier: " + identifier);
		
		Optional<User> user = userService.findByUsernameOrEmailOrPhone(identifier);

		user.ifPresent(u -> {
			logger.debug("Audit SIGNIN for user: " + u.getEmail() + " (id: " + u.getId() + ")");
			auditService.save(DelleMuseAudit.ofSignin(u));
		});
		
		if (user.isEmpty()) {
			logger.warn("No user found for identifier: " + identifier + " during SIGNIN audit");
		}
	}

	@EventListener
	public void handleLogoutSuccess(LogoutSuccessEvent event) {
		Authentication auth = event.getAuthentication();
		logger.debug("handleLogoutSuccess -> auth: " + (auth != null ? auth.getName() : "null"));
		if (auth != null) {
			String identifier = auth.getName();
			Optional<User> user = userService.findByUsernameOrEmailOrPhone(identifier);

			user.ifPresent(u -> {
				logger.debug("Audit SIGNOUT for user: " + u.getEmail() + " (id: " + u.getId() + ")");
				auditService.save(DelleMuseAudit.ofSignout(u));
			});
			
			if (user.isEmpty()) {
				logger.warn("No user found for identifier: " + identifier + " during SIGNOUT audit");
			}
		}
	}
}