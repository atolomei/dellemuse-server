package dellemuse.server.importer.serializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.commons.io.FilenameUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.FSUtil;
import dellemuse.server.ServerConstant;
import dellemuse.server.db.model.ArtWork;
import dellemuse.server.db.model.ArtWorkArtist;
import dellemuse.server.db.model.ArtWorkType;
import dellemuse.server.db.model.Institution;
import dellemuse.server.db.model.Person;
import dellemuse.server.db.model.Resource;
import dellemuse.server.db.model.Site;
import dellemuse.server.db.service.PersonDBService;
import dellemuse.server.db.service.UserDBService;
import dellemuse.server.importer.BaseImporter;
import dellemuse.server.importer.InstitutionImporter;
import dellemuse.server.importer.SiteImporter;

public class SiteImporterDeserialiser extends StdDeserializer<Site> {
            
    private static final long serialVersionUID = 1L;

    static private Logger logger = Logger.getLogger(SiteImporterDeserialiser.class.getName());

    SiteImporter importer;
    
    public SiteImporterDeserialiser(Class<Site> personClass, SiteImporter impoter) {
        super(personClass);
        
        this.importer=impoter;
    }

 
	/**
     * 
     */
    @Override
    public Site deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {

        JsonNode node = p.getCodec().readTree(p);
        
        Optional<String> institutionShortName  = node.get("institutionShortName")!=null            ? Optional.of(node.get("institutionShortName").asText())            : Optional.empty();
        Optional<String> name       = node.get("name")!=null        	    ? Optional.of(node.get("name").asText())            : Optional.empty();
        Optional<String> address    = node.get("address")!=null     	    ? Optional.of(node.get("address").asText())         : Optional.empty();  
        Optional<String> shortName  = node.get("shortName")!=null  		    ? Optional.of(node.get("shortName").asText())       : Optional.empty();
        Optional<String> info       = node.get("info")!=null       	  	   	? Optional.of(node.get("info").asText())            : Optional.empty(); 
        Optional<String> photoFileName   = node.get("photo")!=null          ? Optional.of(node.get("photo").asText())     : Optional.empty();
        Optional<String> logoFileName   = node.get("logo")!=null            ? Optional.of(node.get("logo").asText())     : Optional.empty();

        if (institutionShortName.isEmpty())
            throw new RuntimeException("institutionShortName can not be null");
        
        Optional<Institution>  institution = this.importer.getInstitutionDBService().findByShortName(institutionShortName.get());
        
 
        
        Optional<Site> o_site = this.importer.getSiteDBService().findByShortName(shortName.get());
        
        if (o_site.isEmpty()) {
            
            if (institution.isEmpty())
                throw new RuntimeException("institution is null for new site");
            
            if (name.isEmpty())
                throw new RuntimeException("name can not be null for new site");
            
            Site site = this.importer.getSiteDBService().create(name.get(), institution.get(), shortName, address, info, this.importer.findRoot());
            
            if (photoFileName.isPresent()) {
                importFile(site, photoFileName.get(), "photo");
            }
            
            if (logoFileName.isPresent()) {
                importFile(site, logoFileName.get(), "logo");
            }
            
            logger.debug(site.toString());
            return site;
        }
        else {
            
            Site site = o_site.get();
            
            if (address.isPresent())   site.setAddress(address.get());
            if (shortName.isPresent()) site.setAddress(shortName.get());
            if (info.isPresent())      site.setAddress(info.get());
            
            site.setLastModified(OffsetDateTime.now());
            this.importer.getSiteDBService().save(site);
            
            if (photoFileName.isPresent()) {
                importFile(site, photoFileName.get(), "photo");
            }
            
            if (logoFileName.isPresent()) {
                importFile(site, logoFileName.get(), "logo");
            }

            logger.debug(site.toString());
            return site;
        }
    }

    
    private void importFile(Site site, String fileName, String field) throws IOException {

        File file = new File(getImporter().getMediaDir(), fileName);
        
        if (file.exists()) {
            
            String objectName = String.valueOf(getImporter().getResourceDBService().normalizeFileName(FileNameUtils.getBaseName(file.getName()))  + "-" + this.importer.getResourceDBService().newId());

            try (InputStream is = new FileInputStream(file)) {
                
            	getImporter().getObjectStorageService().putObject(
                        ServerConstant.MEDIA_BUCKET, objectName, 
                        is,
                        file.getName());
            
            } catch (FileNotFoundException e) {
                throw (new IOException("file not found -> " +  file.getAbsolutePath()));
            } 
            
            Resource resource = this.importer.getResourceDBService().create(
                    ServerConstant.MEDIA_BUCKET, 
                    objectName, 
                    file.getName(),
                    getMimeType(file.getName()), 
                    this.importer.getUserDBService().findRoot());
            
            if (field.equals("photo"))
            	site.setPhoto(resource);
            else if (field.equals("logo"))
            	site.setLogo(resource);
            	
            
            site.setLastModified(OffsetDateTime.now());
            
            this.importer.getSiteDBService().save(site);
        }
    }

    private SiteImporter getImporter() {
		return this.importer;
	}

    
    private String getMimeType(String fileName) {

        if (FSUtil.isImage(fileName)) {
            String str = FilenameUtils.getExtension(fileName);

            if (str.equals("jpg"))
                return "image/jpeg";

            if (str.equals("jpeg"))
                return "image/jpeg";

            return "image/" + str;
        }

        if (FSUtil.isPdf(fileName))
            return "application/pdf";

        if (FSUtil.isVideo(fileName))
            return "video/" + FilenameUtils.getExtension(fileName);

        if (FSUtil.isAudio(fileName))
            return "audio/" + FilenameUtils.getExtension(fileName);

        return "";
    }
}
