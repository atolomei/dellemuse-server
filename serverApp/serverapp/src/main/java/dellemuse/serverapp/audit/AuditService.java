
package dellemuse.serverapp.audit;

import org.springframework.stereotype.Service;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerDBSettings;

import dellemuse.serverapp.serverdb.service.base.BaseService;
import dellemuse.serverapp.service.SystemService;

@Service
public class AuditService extends BaseService implements SystemService {

	@SuppressWarnings("unused")
	static private Logger logger = Logger.getLogger(AuditService.class.getName());

	public AuditService(ServerDBSettings settings) {
		super(settings);
	}

}
