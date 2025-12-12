
package dellemuse.serverapp.serverdb.objectstorage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.FSUtil;
import dellemuse.model.util.ThumbnailSize;
 
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.ServiceStatus;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.ResourceDBService;
import dellemuse.serverapp.serverdb.service.UserDBService;
import dellemuse.serverapp.serverdb.service.base.BaseService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.ResourceThumbnailService;
import dellemuse.serverapp.service.SystemService;
import io.odilon.client.error.ODClientException;
import jakarta.annotation.PostConstruct;

@Service
public class AvatarService extends BaseService implements SystemService {

    static private Logger logger = Logger.getLogger(AvatarService.class.getName());

    static private Logger startupLogger = Logger.getLogger("StartupLogger");

    @JsonIgnore
    private final ObjectStorageService objectStorageService;
    
    @JsonIgnore
    private final ResourceDBService resourceDBService;
    
    @JsonIgnore
    private final ResourceThumbnailService  resourceThumbnailService;
    
    @JsonIgnore
    private final UserDBService userDBService;
    
    private Map<Long, Long> map = new HashMap<Long, Long>();

    /**
     * 
     * 
     * @param settings
     * @param objectStorageService
     * @param resourceDBService
     * @param resourceThumbnailService
     * @param userDBService
     */
    public AvatarService(ServerDBSettings settings, ObjectStorageService objectStorageService, ResourceDBService resourceDBService, ResourceThumbnailService  resourceThumbnailService, UserDBService userDBService) {
        super(settings);
        this.objectStorageService=objectStorageService;
        this.resourceDBService=resourceDBService;
        this.resourceThumbnailService=resourceThumbnailService;
        this.userDBService=userDBService;
        
    }
 
    
    public Optional<String> getDefaultAvatar(User user) {
    	Long id = user.getId();
      	long key = id.longValue() % map.size();
      	
      	Long rId = map.get(Long.valueOf(key));
    
      	Optional<Resource> r = getResourceDBService().findById(rId);
  
      	if (r.isEmpty())
      		return Optional.empty();
      	
      	return Optional.of(getPresignedThumbnailSmall(r.get()));
    }
    
    
    @PostConstruct
    protected void onInit() {
    	
    	try {
		
    		if (!getObjectStorageService().existsBucket(ServerConstant.AVATAR_BUCKET) )
    			getObjectStorageService().createBucket(ServerConstant.AVATAR_BUCKET);
    		
    		importFiles();
    		loadFiles();
    
    		setStatus( ServiceStatus.RUNNING);
            startupLogger.debug("Startup -> " + this.getClass().getSimpleName());
            
    	} catch (Exception e) {
			throw new RuntimeException(e);
		}
    	
    }
    
	private void loadFiles() {
		 
		long index = 0;
		for (Resource resource:getResourceDBService().getDefaultAvatars()) {
			map.put(index++, resource.getId());	
		}
		
		// int cacheDurationSecs = ServerDBConstant.THUMBNAIL_CACHE_DURATION_SECS;
	    
		/**
		try {
		
			ResultSet<Item<ObjectMetadata>> set = getObjectStorageService().getClient().listObjects(ServerConstant.AVATAR_BUCKET);

			long index = 0;

			while (set.hasNext()) {
				Item<ObjectMetadata> item = set.next();
				if (item.isOk()) {
					ObjectMetadata meta = item.getObject();
					String url = getObjectStorageService().getClient().getPresignedObjectUrl(meta.getBucketName(), meta.getObjectName());
					map.put(Long.valueOf(index++), url);
				}
			}
		
		} catch (ODClientException e) {
			throw new RuntimeException(e);
		}
		**/
	}
	 

	
    private void importFiles() {
    	
         
      	File avatarDir = new File(getSettings().getAvatarDir());
    	
    	for ( File file: avatarDir.listFiles()) {
    		
    		if (!file.isDirectory()) {
    			
    			if (FSUtil.isImage(file.getName())) {
    					
					String objectName = String.valueOf(getResourceDBService().normalizeFileName(FileNameUtils.getBaseName(file.getName())) + "-" +  getResourceDBService().newId());

    					try {
										getObjectStorageService().getClient().putObject(ServerConstant.AVATAR_BUCKET, objectName, file);
										
										@SuppressWarnings("unused")
										Resource resource = getResourceDBService().create(
							                        ServerConstant.AVATAR_BUCKET, 
							                        objectName, 
							                        file.getName(),
							                        getMimeType(file.getName()), 
							                        file.length(),
							                        ServerConstant.AVATAR,
							                         getUserDBService().findRoot(),
							                         file.getName());
										
										logger.debug("uploaded -> " + file.getName());
										
										FileUtils.forceDelete(file);
		
								 
				
    					} catch (ODClientException | IOException e) {
								logger.error(e, ServerConstant.NOT_THROWN);
						}
    				}
    		}
    	}
    }

    
    private dellemuse.serverapp.serverdb.service.UserDBService getUserDBService() {
        return userDBService;
    }
    
	private ResourceDBService getResourceDBService() {
		return this.resourceDBService;
	}

	private ObjectStorageService getObjectStorageService() {
		return this.objectStorageService;
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

    private String getPresignedThumbnailSmall(Resource photo) {
 		try {
 			if (photo.isUsethumbnail()) {
 				ResourceThumbnailService service = (ResourceThumbnailService) ServiceLocator.getInstance()
 						.getBean(ResourceThumbnailService.class);
 				String url = service.getPresignedThumbnailUrl(photo, ThumbnailSize.MINI);
 				return url;
 			} else {
 				return getObjectStorageService().getClient().getPresignedObjectUrl(photo.getBucketName(), photo.getObjectName());
 			}
 		} catch (Exception e) {
 			throw new RuntimeException(e);
 		}
 	}
     


	 
}
