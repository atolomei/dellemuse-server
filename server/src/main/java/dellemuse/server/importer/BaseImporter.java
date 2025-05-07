package dellemuse.server.importer;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.apache.commons.compress.harmony.unpack200.bytecode.forms.ThisInitMethodRefForm;
import org.codehaus.plexus.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dellemuse.server.BaseService;
import dellemuse.server.Settings;
import dellemuse.server.db.model.Person;
import dellemuse.server.db.service.UserDBService;
import dellemuse.server.objectstorage.ObjectStorageService;

public abstract class BaseImporter extends BaseService implements Importer {

    final String baseDir;
    
    final String classDir;
    
    @JsonIgnore
    final File inbox; 
    
    @JsonIgnore
    final File mediaDir; 

    @JsonIgnore
    final File processed;
    
    @JsonIgnore
    static private final ObjectMapper importerMapper = new ObjectMapper();
    
    
    static  {
        importerMapper.registerModule(new JavaTimeModule());
        importerMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        importerMapper.registerModule(new Jdk8Module());
        //mapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
    }

    @JsonIgnore
    @Autowired
    private final UserDBService userDBService;


    
    @JsonIgnore
    @Autowired
    private final ObjectStorageService objectStorageService;
    
    
    
    public File getMediaDir() {
            return this.mediaDir;
    }
    
    
    public BaseImporter(        Settings settings, 
                                UserDBService userDBService, 
                                ObjectStorageService objectStorageService,
                                String classDir)  {
        super(settings);
        
        this.objectStorageService = objectStorageService;
        this.userDBService=userDBService;
        
        this.baseDir=settings.getImporterBaseDir();
        this.classDir=classDir;
       
        this.inbox     = new File ( getBaseDir() + File.separator  + "inbox"        + File.separator + getClassDir());
        this.processed = new File ( getBaseDir()  + File.separator + "processed"    + File.separator + getClassDir());
        this.mediaDir   = new File ( getBaseDir() + File.separator  + "inbox"        + File.separator + getClassDir() + "media");
         
    }
    
    public String getClassDir() {
        return this.classDir;
    }
    
    public String getBaseDir() {
        return this.baseDir;
    }
    
    
    public File getInbox() {
        return this.inbox;
    }

    public File getProcessed() {
        return this.processed;
    }


    @Override
    @JsonIgnore 
    public ObjectMapper getObjectMapper() {
        return importerMapper;
    }

    
    public synchronized void execute() throws IOException {
        
        if (!getInbox().exists() || !getInbox().isDirectory()) {
                FileUtils.forceMkdir(getInbox());
                return;
        }
        
        
        for (File file: getInbox().listFiles()) {
            if (isJson(file)) {
                boolean success = read(file);
                if (success)
                    process(file);
            }
        }
    }

    
    protected abstract boolean read(File file) throws StreamReadException, DatabindException, IOException;

    
    protected void process( File file) throws IOException {
        File destination = new File ( getProcessed() + File.separator + file.getName());
        Files.move(file.toPath(), destination.toPath(),  StandardCopyOption.REPLACE_EXISTING);
    }

    protected boolean isJson(File file) {
        return file.getName().toLowerCase().endsWith(".json");
    }

    public UserDBService getUserDBService() {
        return userDBService;
    }


    public ObjectStorageService getObjectStorageService() {
        return objectStorageService;
    }

    

}
