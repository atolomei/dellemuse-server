package dellemuse.serverapp.security.config;

import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.DelleMuseAuditDBService;
import dellemuse.serverapp.serverdb.service.UserDBService;
import dellemuse.serverapp.serverdb.service.base.BaseService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.SystemService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class ImpersonationService extends BaseService implements SystemService {

	static private Logger logger = Logger.getLogger(ImpersonationService.class.getName());

	private final UserDetailsService userDetailsService;

	public ImpersonationService(ServerDBSettings settings, UserDetailsService userDetailsService) {
		super(settings);
		this.userDetailsService = userDetailsService;
	}

	public void impersonate(String targetUsername, HttpServletRequest request, HttpServletResponse response) {

		// capture the current admin BEFORE replacing the SecurityContext
		String adminIdentifier = SecurityContextHolder.getContext().getAuthentication() != null
				? SecurityContextHolder.getContext().getAuthentication().getName()
				: null;

		// load target user
		UserDetails targetUser = userDetailsService.loadUserByUsername(targetUsername);

		// create new authentication
		org.springframework.security.core.Authentication newAuth = new UsernamePasswordAuthenticationToken(targetUser, targetUser.getPassword(), targetUser.getAuthorities());

		// replace authentication
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(newAuth);
		SecurityContextHolder.setContext(context);

		// persist in session
		new HttpSessionSecurityContextRepository().saveContext(context, request, response);

		// audit the impersonation with WHO did it (admin) and WHO is being impersonated (target)
		try {
			Optional<User> oTarget = getUserDBService().findByUsernameOrEmailOrPhone(targetUsername);
			Optional<User> oAdmin  = adminIdentifier != null
					? getUserDBService().findByUsernameOrEmailOrPhone(adminIdentifier)
					: Optional.empty();

			if (oTarget.isPresent() && oAdmin.isPresent()) {
				logger.debug("Audit IMPERSONATION: admin " + oAdmin.get().getEmail() + " -> " + oTarget.get().getEmail());
				getAuditService().save(DelleMuseAudit.ofImpersonation(oTarget.get(), oAdmin.get()));
			} else {
				logger.warn("Could not fully audit impersonation: target=" + targetUsername + " admin=" + adminIdentifier);
			}
		} catch (Exception e) {
			logger.error("Failed to save impersonation audit for: " + targetUsername + " -> " + e.getMessage());
		}
	}

	protected UserDBService getUserDBService() {
		return (UserDBService) ServiceLocator.getInstance().getBean(UserDBService.class);
	}

	protected DelleMuseAuditDBService getAuditService() {
		return (DelleMuseAuditDBService) ServiceLocator.getInstance().getBean(DelleMuseAuditDBService.class);
	}

	@Override
	public String toJSON() {
		return null;
	}
}