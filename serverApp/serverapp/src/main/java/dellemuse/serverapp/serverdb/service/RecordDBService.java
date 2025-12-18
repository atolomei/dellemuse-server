package dellemuse.serverapp.serverdb.service;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.DelleMuseObject;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionGuideRecord;
import dellemuse.serverapp.serverdb.model.record.TranslationRecord;
import jakarta.transaction.Transactional;
 

public abstract class RecordDBService<T extends TranslationRecord, I> extends DBService<T, I> {

	public RecordDBService(CrudRepository<T, I> repository, ServerDBSettings settings) {
		super(repository, settings);
	}

	
	
	public abstract Optional<T> findByParentObject(MultiLanguageObject o, String lang);
	
	
}
