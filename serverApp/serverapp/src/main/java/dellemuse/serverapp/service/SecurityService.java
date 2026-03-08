package dellemuse.serverapp.service;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.security.RoleSite;
import dellemuse.serverapp.serverdb.service.UserDBService;
import dellemuse.serverapp.serverdb.service.base.BaseService;
import jakarta.transaction.Transactional;



@Service
public class SecurityService extends BaseService {


	@SuppressWarnings("unused")
	private class SecurityRecord {
		@SuppressWarnings("unused")
		public String token;
		public String parameter;
		public String user_id;
		public Serializable id;
		public Instant expiration;
	}
	/****/

	
	
	private SecureRandom random = new SecureRandom();

	@Autowired
    private final UserDBService userDBService;
	

    public SecurityService(ServerDBSettings settings, UserDBService userDBService) {
        super(settings);
        this.userDBService=userDBService;
    }
	
	
	@Transactional
	public Set<Site> getWriteAuthorizedSites(User user) {

		Set<Site> sites= new HashSet<Site>();
		
		if (!user.isDependencies())
			user=getUserDBService().findWithDeps(user.getId()).orElseThrow(()-> new RuntimeException("User not found"));
		
		user.getRolesInstitution().forEach ( r ->
				{
					Institution i = r.getInstitution();
					List<Site> ls = i.getSites();
					ls.forEach( s -> sites.add(s));
				}
		);
				
		user.getRolesSite().stream().filter(ia -> (ia.getKey().equals(RoleSite.ADMIN) || ia.getKey().equals(RoleSite.EDITOR))).forEach ( r -> sites.add(r.getSite()));
		return sites;
	}
	
	
	@Transactional
	public Set<Site> getAdminAuthorizedSites(User user) {

		Set<Site> sites= new HashSet<Site>();
		
		if (!user.isDependencies())
			user=getUserDBService().findWithDeps(user.getId()).orElseThrow(()-> new RuntimeException("User not found"));
		
		user.getRolesInstitution().forEach ( r ->
				{
					Institution i = r.getInstitution();
					List<Site> ls = i.getSites();
					ls.forEach( s -> sites.add(s));
				}
		);
				
		user.getRolesSite().stream().filter(ia -> (ia.getKey().equals(RoleSite.ADMIN))).forEach ( r -> sites.add(r.getSite()));
		return sites;
	}
	
    

    
    
    public User getRootUser() {
        return getUserDBService().findRoot();
    }

     
    protected UserDBService getUserDBService() {
        return userDBService;
    }

    
	public String nextSecureToken() {
		return new BigInteger(130, this.random).toString(32);
	}
    
    
	private  Map<String, SecurityRecord> user_tokens = new ConcurrentHashMap<String, SecurityRecord>(ServerConstant.ONE_DAY_MILISECONDS);
	
	
	public void addToken(User user, String token, String parameter, int duration_minutes) {
		SecurityRecord record = new SecurityRecord();
		record.token = token;
		record.parameter=parameter;
		record.user_id=user.getId().toString();
		
		// duration_minutes expiration period
		record.expiration=Instant.now().plusSeconds(60*duration_minutes);
		user_tokens.put(token, record);
	
	}
	
}
