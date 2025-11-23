package dellemuse.serverapp.command;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import dellemuse.model.image.SimpleImageInfo;
import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.ServerDBSettings;

import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.ResourceDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.serverdb.util.MediaUtil;
 

public class ResourceMetadataCommand extends Command {

	static private Logger logger = Logger.getLogger(ResourceMetadataCommand.class.getName());

	@JsonProperty("resourceId")
	private Long resourceId;

	@JsonProperty("height")
	private int height;

	@JsonProperty("width")
	private int width;

	@JsonProperty("durationMilliseconds")
	private long durationMilliseconds;

	@JsonProperty("mimeType")
	private String mimeType;

	@JsonIgnore
	ResourceDBService resourceDBService;
	
	
	@JsonIgnore
	ObjectStorageService objectStorageService;
	

	
	@Override
	public void execute() {
		
		resourceDBService = (ResourceDBService) ServiceLocator.getInstance()
				.getBean(ResourceDBService.class);


		objectStorageService = (ObjectStorageService) ServiceLocator.getInstance()
				.getBean(ObjectStorageService.class);

		
		try {

			Optional<Resource> o = resourceDBService.findById(getResourceId());
			
			if (o.isPresent()) {
				
					Resource resource = o.get();

					getLockService().getObjectLock(resource.getId()).writeLock().lock();
					
					try { 
						if (resource.getMedia()!=null) {
							if (resource.getMedia().contains("image")) {
								processImage(resource);
							} else if  (resource.getMedia().contains("audio")) {
								processAudio(resource);
							}
						}
					} finally  {
						getLockService().getObjectLock(resource.getId()).writeLock().unlock();
					}
			}

		} catch (Exception e) {
			logger.error(e, ServerConstant.NOT_THROWN);
		}
	}
	
	public long getDurationMilliseconds() {
		return this.durationMilliseconds;
	}

	public ResourceMetadataCommand(Long resourceId) {
		this.resourceId = resourceId;
	}

	public int getHeight() {
		return this.height;
	}

	public int getWidth() {
		return this.width;
	}

	public String getMimeType() {
		return this.mimeType;
	}

	
	/**
	 * 
	 * @param resource
	 */
	private void processImage(Resource resource ) {
		
		if ((resource.getHeight() <= 0 && resource.getWidth() <= 0)) {
			
			if ( !(resource.getMedia().contains("webp") || resource.getMedia().contains("svg"))) {
				
				try (InputStream is = objectStorageService.getObject(resource.getBucketName(), resource.getObjectName())) {
					if (is != null) {
							SimpleImageInfo info = new SimpleImageInfo(is);
							this.width = info.getWidth();
							this.height = info.getHeight();
							this.mimeType = info.getMimeType();

							if (width > 0 && height > 0) {
								resource.setWidth(width);
								resource.setHeight(height);
								resource.setAudit("");
								logger.debug("Saving image dimensions -> " + resource.getDisplayname());
								resourceDBService.save(resource);

							}
					}
				} catch (Exception e) {
					resource.setAudit(e.getClass().getSimpleName() + "| " + (e.getMessage()!=null?e.getMessage():""));
					resourceDBService.save(resource);
					logger.error(e, resource.getDisplayname()+ "  | " + resource.getMedia(), ServerConstant.NOT_THROWN);
				}
			}
		}
	}
	
	private void processAudio(Resource resource) {
	 
	 
		if (resource.getMedia().contains("audio") && (resource.getDurationMilliseconds()<=0)) {

			File file = null;
	
			try (InputStream is = objectStorageService.getObject(resource.getBucketName(), resource.getObjectName())) {
	
				if (is != null) {
	
						file = new File(getSettings().getWorkDir(), resource.getName());
						
						if (getLockService().getFileLock(file.getAbsolutePath()).writeLock().tryLock(30, TimeUnit.SECONDS)) {
							
							try {
								try (InputStream in = objectStorageService.getClient().getObject(resource.getBucketName(),
										resource.getObjectName())) {
									Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
								}
								
								//long duration = MediaUtil.getAudioDurationMilliseconds(file);
								//if (duration > 0) {
								//	resource.setDurationMilliseconds(duration);
								//	resourceDBService.save(resource);
								//}
								
								int d= MediaUtil.getAudioDurationInSeconds(file);
								
								if (d > 0) {
									
									double d_size = Double.valueOf(resource.getSize());
									double d_dur  = Double.valueOf(d);
									
									if (d_size>0) {
										double coc = d_size  / d_dur;
										if (coc>5000 || d_dur<180) {
											logger.debug("Saving audio duration -> " + resource.getDisplayname() + " | " + String.valueOf(d) + "secs" + " | coc:" + String.valueOf(coc) + " bytes/sec");
											resource.setDurationMilliseconds( Double.valueOf( Double.valueOf(d).doubleValue() * 1000.0 ).longValue());
											resource.setAudit("");
											resourceDBService.save(resource);
										}
										else {
											resource.setAudit("Error -> " +  String.valueOf(d) + " secs" + " | " + String.valueOf(coc) + " bytes/sec");
											resourceDBService.save(resource);
										}
										
									}
								}
							} finally {
								getLockService().getFileLock(file.getAbsolutePath()).writeLock().unlock();
							}
						} else {
							logger.error("lock not working");
						}
				}
			} catch (Exception e) {
					logger.error(e, resource.getDisplayname()+ "  | " + resource.getMedia(), ServerConstant.NOT_THROWN);
					resource.setAudit(e.getClass().getSimpleName() + "| " + (e.getMessage()!=null?e.getMessage():""));
					resourceDBService.save(resource);
					return;
			} finally {
				if ((file != null) && file.exists()) {
					try {
						FileUtils.forceDelete(file);
					} catch (IOException e) {
						logger.error(e,  file.getName(), ServerConstant.NOT_THROWN);
					}
				}
			}
		}  
	}
	
	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public ServerDBSettings getSettings() {
		return (ServerDBSettings) ServiceLocator.getInstance().getBean(ServerDBSettings.class);

	}
}
