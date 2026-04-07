package dellemuse.serverapp.serverdb.service.base;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import dellemuse.model.JsonObject;
import dellemuse.serverapp.DellemuseObjectMapper;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.ServiceStatus;
import dellemuse.serverapp.serverdb.service.ElevenLabsRequestDBService;
import dellemuse.serverapp.service.language.LanguageService;


public abstract class BaseService extends  JsonObject {
                        
    @JsonIgnore
    static final private ObjectMapper mapper = new DellemuseObjectMapper();

    @JsonIgnore
    @Autowired
    private final ServerDBSettings settings;
    
    @JsonIgnore
    private ServiceStatus status;

    public BaseService(ServerDBSettings settings) {
        this.status = ServiceStatus.STOPPED;        
        this.settings=settings;
    }

    public ServerDBSettings getSettings() {
        return settings;
    }
    
    public void setStatus(ServiceStatus status) {
        this.status = status;
    }

    public ServiceStatus getStatus() {
        return this.status;
    }
    
	protected LanguageService getLanguageService() {
		return (LanguageService) ServiceLocator.getInstance().getBean(LanguageService.class);
	}

	protected ElevenLabsRequestDBService getElevenLabsRequestDBService() {
		return (ElevenLabsRequestDBService) ServiceLocator.getInstance().getBean(ElevenLabsRequestDBService.class);
	}
	
}
