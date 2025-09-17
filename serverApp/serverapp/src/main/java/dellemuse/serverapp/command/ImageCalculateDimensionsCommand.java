package dellemuse.serverapp.command;

import java.io.InputStream;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

import dellemuse.model.image.SimpleImageInfo;
import dellemuse.model.logging.Logger;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.ResourceDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;

public class ImageCalculateDimensionsCommand extends Command {

	static private Logger logger =	Logger.getLogger(ImageCalculateDimensionsCommand.class.getName());

	@JsonProperty("resourceId")
	private Long resourceId;
	
	@JsonProperty("height")
	private int height;
	
	@JsonProperty("width")
	private int width;

	@JsonProperty("mimeType")
	private String mimeType;
	
	
	public ImageCalculateDimensionsCommand(Long resourceId) {
		this.resourceId=resourceId;
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

	@Override
	public void execute() {
		try {
			
			ResourceDBService service = (ResourceDBService) ServiceLocator.getInstance().getBean(ResourceDBService.class);
			
			Optional<Resource> o = service.findById(getResourceId());
			
			
			if (o.isPresent()) {
				
				Resource resource = o.get();

				if (resource.getMedia()!=null && resource.getMedia().contains("image")) {
					
						ObjectStorageService os = (ObjectStorageService) ServiceLocator.getInstance().getBean(ObjectStorageService .class);
		
						try (InputStream is = os.getObject(resource.getBucketName(), resource.getObjectName())) {
							if (is!=null) {
								try {
											SimpleImageInfo info = new SimpleImageInfo(is);
											this.width=info.getWidth();
											this.height=info.getHeight();
											this.mimeType=info.getMimeType();
							
											if (width>0 && height>0) {
												resource.setWidth(width);
												resource.setHeight(height);
											
												logger.debug("Saving image dimensions -> " + resource.getDisplayname());
												service.save(resource);
											
											}
								} catch (Exception e) {
											logger.error(e);
								}
							}
						}
				}
			}
			
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

}
 