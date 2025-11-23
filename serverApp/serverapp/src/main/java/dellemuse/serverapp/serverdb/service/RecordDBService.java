package dellemuse.serverapp.serverdb.service;

import org.springframework.data.repository.CrudRepository;

import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.DelleMuseObject;
import dellemuse.serverapp.serverdb.model.User;

public abstract class RecordDBService<T extends DelleMuseObject, I> extends DBService<T, I> {

	public RecordDBService(CrudRepository<T, I> repository, ServerDBSettings settings) {
		super(repository, settings);
	}

	
	
	
	
}
