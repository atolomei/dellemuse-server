package dellemuse.serverdb.importer;

import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface Importer {

    
    public String getClassDir();
    public String getBaseDir();
    public File getInbox();
    public File getProcessed();
    public File getMediaDir();
    public ObjectMapper getObjectMapper();
}
