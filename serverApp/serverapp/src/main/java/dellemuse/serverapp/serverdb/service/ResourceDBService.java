package dellemuse.serverapp.serverdb.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.Optional;

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.FSUtil;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.ServerDBConstant;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.object.service.ResourceService;
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.odilon.model.ObjectMetadata;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;




@Service
public class ResourceDBService extends DBService<Resource, Long> implements ApplicationContextAware {

  
    static private Logger logger = Logger.getLogger(ResourceDBService.class.getName());

    @JsonIgnore
    private ApplicationContext applicationContext;

    @PersistenceContext
    private EntityManager entityManager;

    public ResourceDBService(CrudRepository<Resource, Long> repository,
                             ServerDBSettings settings) {
        super(repository,  settings);
    }

    @PostConstruct
    protected void onInitialize() {
    	super.register(getEntityClass(), this);
    	 
    }
    
    
    
   	@SuppressWarnings("unused")
	@Transactional
       public Optional<Resource> findWithDeps(Long id) {
       
   		Optional<Resource> o_i = super.findById(id);
   		
   		if (o_i.isEmpty())
   			return o_i;
   		
   		Resource i = o_i.get();
   		
   		User user = i.getLastModifiedUser();

   		i.setDependencies(true);
   		
   		return o_i;

   	}
    
    @Transactional
    public Long newId() {
        return ((Number) entityManager.createNativeQuery("SELECT nextval('objectstorage_id')").getSingleResult()).longValue();
    }

    public String normalizeFileName(String name) {
        String str = name.replaceAll("[^\\x00-\\x7F]|[\\s]+", "-").toLowerCase().trim();
        if (str.length() < 100)
            return str;
        return str.substring(0, 100);
    }

    public String getMimeType(String fileName) {

    	if (FSUtil.isImage(fileName)) {
        
    		String ext = FilenameUtils.getExtension(fileName).toLowerCase();

            if (ext.equals("jpg") || ext.equals("jpeg"))
                return "image/jpeg";

            return "image/" + ext;
        }

        if (FSUtil.isPdf(fileName))
            return "application/pdf";

        if (FSUtil.isVideo(fileName))
            return "video/" + FilenameUtils.getExtension(fileName);

        if (FSUtil.isAudio(fileName))
            return "audio/" + FilenameUtils.getExtension(fileName);

        return "";
    }

    /**
     * 
     * @param objectName
     * @param fileName
     * @param stream
     */
    public void upload(String bucketName, String objectName, InputStream stream, String fileName) {
    	try {

    		ObjectStorageService service = (ObjectStorageService) ServiceLocator.getInstance().getBean(ObjectStorageService.class);
   			service.putObject(bucketName, objectName,stream,fileName);
   			logger.debug("putObject -> b:" + bucketName +"  | o:", objectName + " | f:" + fileName);

    	} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }
    
    
    
    @Override
    @Transactional
    public Resource create(String objectName, User createdBy) {
        return create(ServerDBConstant.MEDIA_BUCKET, objectName, objectName, null, 0, createdBy);
    }

    @Transactional
    public Resource create(String objectName, String name, long size, User createdBy) {
        return create(ServerDBConstant.MEDIA_BUCKET, objectName, name, null, size, createdBy);
    }

    @Transactional
    public Resource create(String bucketName, String objectName, String name, String media, long size, User createdBy) {
        Resource c = new Resource();
        c.setBucketName(bucketName);
        c.setObjectName(objectName);
        c.setName(name);
        c.setSize( size );
        c.setNameKey(nameKey(name));
        if (media != null)
            c.setMedia(media);
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        return getRepository().save(c);
    
    }

    
    @Transactional
    public void checkAndSetSize(Resource resource) {
    
    	if (resource.getSize()>0)
    		return;
    
    	ObjectStorageService service = (ObjectStorageService) ServiceLocator.getInstance().getBean(ObjectStorageService.class);
    	try {
			resource.setSize(service.getObjectMetadata(resource.getBucketName(), resource.getObjectName()).getLength());
			super.save(resource);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }
    
    public ResourceService getResourceService(Resource resource) {
        return this.applicationContext.getBean(ResourceService.class, resource);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    protected Class<Resource> getEntityClass() {
        return Resource.class;
    }


}









/**
 * 

@Service
public class ResourceDBService extends DBService<Resource, Long> implements ApplicationContextAware {

    @SuppressWarnings("unused")
    static private Logger logger = Logger.getLogger(ResourceDBService.class.getName());

    @JsonIgnore
    ApplicationContext applicationContext;

    public ResourceDBService(CrudRepository<Resource, Long> repository, EntityManagerFactory entityManagerFactory,
            Settings settings) {
        super(repository, entityManagerFactory, settings);

    }

    @Transactional
    public Long newId() {
        Long nextVal = (Long) getSessionFactory().getCurrentSession()
                .createNativeQuery("SELECT nextval('objectstorage_id')", Long.class).getSingleResult();
        return nextVal;
    }

    public String normalizeFileName(String name) {
        String str = name.replaceAll("[^\\x00-\\x7F]|[\\s]+", "-").toLowerCase().trim();
        if (str.length() < 100)
            return str;
        return str.substring(0, 100);
    }

    public String getMimeType(String fileName) {

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
 

    @Override
    @Transactional
    public Resource create(String objectName, User createdBy) {
        return create(ServerConstant.MEDIA_BUCKET, objectName, objectName, null, createdBy);
    }

    @Transactional
    public Resource create(String objectName, String name, User createdBy) {
        return create(ServerConstant.MEDIA_BUCKET, objectName, name, null, createdBy);
    }

    public void test() {
    }

    @Transactional
    public Resource create(String bucketName, String objectName, String name, String media, User createdBy) {
        Resource c = new Resource();
        c.setBucketName(bucketName);
        c.setObjectName(objectName);
        c.setName(name);
        c.setNameKey(nameKey(name));
        if (media != null)
            c.setMedia(media);
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        return getRepository().save(c);
    }

    @Override
    protected Class<Resource> getEntityClass() {
        return Resource.class;
    }

    public ResourceService getResourceService(Resource resource) {
        return this.applicationContext.getBean(ResourceService.class, resource);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    */

