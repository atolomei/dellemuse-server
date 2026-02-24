package dellemuse.serverapp.security.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.service.base.BaseService;
import dellemuse.serverapp.service.SystemService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class ImpersonationService extends BaseService implements SystemService {

	private final UserDetailsService userDetailsService;

	public ImpersonationService(ServerDBSettings settings, UserDetailsService userDetailsService) {
		super(settings);
		this.userDetailsService = userDetailsService;

	}

	public void impersonate(String targetUsername, HttpServletRequest request, HttpServletResponse response) {

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
	}

	@Override
	public String toJSON() {
		return null;
	}

}
