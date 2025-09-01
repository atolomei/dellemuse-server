package dellemuse.serverapp.serverdb.service.base;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.service.DBService;


/**
 * 
 */

@Service
public class ServiceLocator extends BaseService implements ApplicationContextAware {

	static ServiceLocator instance;


	private Map<Class<?>, DBService<?, Long>> map = new HashMap<Class<?>, DBService<?, Long>>();
	
	
	@Autowired
	private ApplicationContext applicationContext;
	
	static public  ServiceLocator getInstance() {
		return instance;
	}

	public ServiceLocator(ServerDBSettings settings) {
		super(settings);
	    instance=this;
	}
	
	public Object getBean(String name) {
		return getInstance().getApplicationContext().getBean(name);
	}
	
	public Object getBean(Class<?> clazz) {
		return getInstance().getApplicationContext().getBean(clazz);
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	public ApplicationContext getApplicationContext(){
		return this.applicationContext;
	}

	
	/**
	public void register(Class<?> entityClass, DBService<?, Long> dbService) {
		map.put(entityClass, dbService);
	}

	public DBService<?, Long> getDBService( Class<?> entityClass ) {
		return map.get(entityClass);
	}
	**/
	
}

