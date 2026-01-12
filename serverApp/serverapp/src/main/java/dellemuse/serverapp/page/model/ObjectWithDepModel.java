package dellemuse.serverapp.page.model;

import java.util.Optional;

import dellemuse.model.logging.Logger;
 
import dellemuse.serverapp.serverdb.model.DelleMuseObject;
import dellemuse.serverapp.serverdb.service.DBService;

public class ObjectWithDepModel<T extends DelleMuseObject> extends ObjectModel<T> {
	 
	static private Logger logger = Logger.getLogger(ObjectWithDepModel.class.getName());


	private static final long serialVersionUID = 1L;

	public ObjectWithDepModel(T object) {
		super(object);
		logger.debug(object.getDisplayname());
	}

	 
	@Override
	public void setObject(T object) {
	
		if (!object.isDependencies()) {
			Optional<?> o = load();
			object = (T) o.get();
		}
	
		super.setObject(object);
	}
	
	@Override
	protected Optional<?> load() {
		
		// return super.load();
		
		DBService<?,Long> service = DBService.getDBService(getObjectClass());
		//Optional<?> o =	service.findByIdWithDeps(getId());

		
		if (service==null) {
			logger.debug(" DBService is null -> " +getObjectClass() );
		}
		
		Optional<?> o =	service.findById(getId());

		return o;
	}


}


