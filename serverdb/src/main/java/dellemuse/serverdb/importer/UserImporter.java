package dellemuse.serverdb.importer;

import java.io.File;
import java.io.IOException;

import org.codehaus.plexus.util.FileUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.google.common.io.Files;

import dellemuse.serverdb.ServerDBSettings;
import dellemuse.serverdb.model.ArtWork;
import dellemuse.serverdb.model.Person;
import dellemuse.serverdb.model.User;
import dellemuse.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverdb.service.UserDBService;


@Service
public class UserImporter extends BaseImporter {


    public UserImporter(ServerDBSettings settings,  UserDBService userDBService,  ObjectStorageService objectStorageService) {
        super(settings, userDBService, objectStorageService, ArtWork.class.getSimpleName().toLowerCase());
    }
 
    @Override
    protected boolean read(File file) throws StreamReadException, DatabindException, IOException {

        User user = getObjectMapper().readValue(file, User.class);
        return false;
    }


}
