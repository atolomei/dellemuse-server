package dellemuse.serverapp.email;

import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.service.base.BaseService;
import dellemuse.serverapp.service.SystemService;

public class EmailService  extends BaseService implements SystemService  {

	public EmailService(ServerDBSettings settings) {
		super(settings);
	}

	
	
/**	
	public void send( EmailData data) {
		
	}
	
	**/
	
	public String getNoReplyEmailAddress() {
		return getSettings().getNoReplyEmailAddress();
	}
	
	
}
