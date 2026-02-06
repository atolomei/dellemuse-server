package dellemuse.serverapp.serverdb.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

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
import dellemuse.serverapp.serverdb.model.AuditAction;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.object.service.ResourceService;
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

@Service
public class ResourceDBService extends DBService<Resource, Long> implements ApplicationContextAware {
  
    static private Logger logger = Logger.getLogger(ResourceDBService.class.getName());

    @JsonIgnore
    private ApplicationContext applicationContext;

    @PersistenceContext
    private EntityManager entityManager;

    public ResourceDBService(CrudRepository<Resource, Long> repository, ServerDBSettings settings) {
        super(repository, settings);
    }

    
    @Transactional
    public Resource create(String objectName, User createdBy) {
        return create(ServerConstant.MEDIA_BUCKET, objectName, objectName, null, 0, null, createdBy, objectName);
    }

    @Transactional
    public Resource create(String objectName, String name, long size, User createdBy) {
        return create(ServerConstant.MEDIA_BUCKET, objectName, name, null, size, null, createdBy, name);
    }
    
    @Transactional
    public Resource create(String bucketName, String objectName, String name, String media, long size, String tag, User createdBy, String fileName) {
      
    	Resource c = new Resource();
        
    	c.setBucketName(bucketName);
        c.setObjectName(objectName);
        c.setName(name);
        c.setFileName(fileName);
        c.setSize(size);
        c.setTag(tag);
        c.setState( ObjectState.PUBLISHED);
        
        if (media != null)
            c.setMedia(media);
        else 
        	c.setMedia(getMimeType(name));
               
        c.setUsethumbnail(	getMimeType(name).toLowerCase().endsWith("png") || 
        					getMimeType(name).toLowerCase().endsWith("jpg") || 
        					getMimeType(name).toLowerCase().endsWith("jpeg"));
        
        c.setCreated(OffsetDateTime.now());
        c.setLastModified(OffsetDateTime.now());
        c.setLastModifiedUser(createdBy);
        
        getRepository().save(c);
        getDelleMuseAuditDBService().save(DelleMuseAudit.of(c, createdBy,  AuditAction.CREATE));
		
        return c;
    }
    
   
    
 	@Transactional
       public Optional<Resource> findWithDeps(Long id) {
       
   		Optional<Resource> o_i = super.findById(id);
   		
   		if (o_i.isEmpty())
   			return o_i;
   		
   		Resource i = o_i.get();
   		
   		User user = i.getLastModifiedUser();
   		
   		if (user!=null) {
   			user = getUserDBService().findWithDeps(user.getId()).get();
   			i.setLastModifiedUser(user);
   		}
   		
   		i.setDependencies(true);
   		return o_i;

   	}
    
    @Transactional
    public Long newId() {
        return ((Number) getEntityManager().createNativeQuery("SELECT nextval('objectstorage_id')").getSingleResult()).longValue();
    }

    public String normalizeFileName(String name) {

        String str = name.replaceAll("[^\\x00-\\x7F]|[\\s]+", "-").toLowerCase().trim();
        str=str.replace(",", "");
        
        if (str.length() < 100)
            return str;
        return str.substring(0, 100);
    }

    public String getMimeType(String fileName) {

    	if (fileName==null)
    		return null;
    	
    	if (FSUtil.isImage(fileName)) {
        
    		String ext = FilenameUtils.getExtension(fileName).toLowerCase();

    		
            if (ext.equals("jpg") || ext.equals("jpeg"))
                return "image/jpeg";

            if (ext.equals("svg"))
            	return "image/svg+xml";
            			
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
    
    @Transactional
	public List<Resource> getDefaultAvatars() {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Resource> cq = cb.createQuery(Resource.class);
		Root<Resource> root = cq.from(Resource.class);
		cq.select(root).where(cb.equal(root.get("tag"), "avatar"));
		cq.orderBy(cb.asc( cb.lower(root.get("name"))));
		return getEntityManager().createQuery(cq).getResultList();
	}
    
    
   

   
    /**
     *  getCommandService().run(new ResourceMetadataCommand(resource.getId()));
     * */
    
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

    
    @PostConstruct
    protected void onInitialize() {
    	super.register(getEntityClass(), this);
    }
    
    @Override
	public String getObjectClassName() {
		 return  Resource.class.getSimpleName().toLowerCase();
	} 

    @Override
    protected Class<Resource> getEntityClass() {
        return Resource.class;
    }
}




 