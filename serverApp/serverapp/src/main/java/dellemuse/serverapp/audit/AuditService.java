
package dellemuse.serverapp.audit;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.Constant;
import dellemuse.model.util.TimerThread;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.ServerDBSettings;
 
import dellemuse.serverapp.serverdb.service.base.BaseService;
import dellemuse.serverapp.service.SystemService;
import io.odilon.client.ODClient;
import io.odilon.client.OdilonClient;
import io.odilon.client.error.ODClientException;
import io.odilon.errors.InternalCriticalException;
import io.odilon.model.ObjectMetadata;
import io.odilon.model.list.Item;
import io.odilon.model.list.ResultSet;
import jakarta.annotation.PostConstruct;

@Service
public class AuditService extends BaseService implements SystemService {

	static private Logger logger = Logger.getLogger(AuditService.class.getName());

	static private Logger startupLogger = Logger.getLogger("StartupLogger");
 
	public AuditService(ServerDBSettings settings) {
		super(settings);
	}

	
	
	

}
