package dellemuse.serverapp.serverdb.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.repository.CrudRepository;

import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.DelleMuseObject;

public abstract class MultiLanguageObjectDBservice<T extends DelleMuseObject, I> extends DBService<T, I> {

	
	private static Map<Class<?>, RecordDBService<?, Long>> recordDBmap = new HashMap<Class<?>, RecordDBService<?, Long>>();

	public static void registerRecordDB(Class<?> entityClass, RecordDBService<?, Long> dbService) {
		recordDBmap.put(entityClass, dbService);
	}
	public static RecordDBService<?, Long> getRecordDBService(Class<?> entityClass) {
		return recordDBmap.get(entityClass);
	}
	
	
	public MultiLanguageObjectDBservice(CrudRepository<T, I> repository, ServerDBSettings settings) {
		super(repository, settings);
	}




 
	
}
