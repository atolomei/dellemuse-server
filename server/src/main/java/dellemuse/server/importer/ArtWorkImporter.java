package dellemuse.server.importer;

import java.io.File;
import java.io.IOException;

import org.codehaus.plexus.util.FileUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.google.common.io.Files;

import dellemuse.server.Settings;
import dellemuse.server.db.model.ArtWork;
import dellemuse.server.db.model.Person;
import dellemuse.server.db.service.UserDBService;
import dellemuse.server.objectstorage.ObjectStorageService;

@Service
public class ArtWorkImporter extends BaseImporter {


    public ArtWorkImporter(Settings settings,  UserDBService userDBService,  ObjectStorageService objectStorageService) {
        super(settings, userDBService, objectStorageService, ArtWork.class.getSimpleName().toLowerCase());
    }
 
 
    @Override
    protected boolean read(File file) throws StreamReadException, DatabindException, IOException {
        
        ArtWork aw = getObjectMapper().readValue(file, ArtWork.class);
        return true;
    }

   
    

}
