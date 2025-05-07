package dellemuse.server.importer;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;

import dellemuse.server.BaseService;
import dellemuse.server.Settings;
import dellemuse.server.db.service.UserDBService;


@Service
public class ServerImporter extends BaseService {

    
    @JsonIgnore
    @Autowired
    final PersonImporter personImporter;
    
    @JsonIgnore
    @Autowired
    final UserImporter userImporter;

    ArtWorkImporter  artWorkImporter;
    
    
    
    public ServerImporter(Settings settings, 
            PersonImporter personImporter,
            UserImporter userImporter,
            ArtWorkImporter  artWorkImporter) {
        
        super(settings);

        this.personImporter=personImporter;
        this.userImporter=userImporter;
        this.artWorkImporter=artWorkImporter;
     
    }
    

    public void execute() throws IOException {
        
        personImporter.execute();
        userImporter.execute();
        artWorkImporter.execute();
        
    }

    

}
