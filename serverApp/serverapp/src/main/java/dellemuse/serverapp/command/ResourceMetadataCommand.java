package dellemuse.serverapp.command;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

import dellemuse.model.image.SimpleImageInfo;
import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.ResourceDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.serverdb.util.MediaUtil;
import io.odilon.client.error.ODClientException;

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

	@Override
	public void execute() {
		try {

			ResourceDBService service = (ResourceDBService) ServiceLocator.getInstance()
					.getBean(ResourceDBService.class);

			Optional<Resource> o = service.findById(getResourceId());

			if (o.isPresent()) {

				Resource resource = o.get();

				ObjectStorageService os = (ObjectStorageService) ServiceLocator.getInstance()
						.getBean(ObjectStorageService.class);

				if (resource.getMedia() != null && resource.getMedia().contains("image")) {

					if ((resource.getHeight() <= 0 && resource.getWidth() <= 0)) {

						try (InputStream is = os.getObject(resource.getBucketName(), resource.getObjectName())) {
							if (is != null) {
								try {
									SimpleImageInfo info = new SimpleImageInfo(is);
									this.width = info.getWidth();
									this.height = info.getHeight();
									this.mimeType = info.getMimeType();

									if (width > 0 && height > 0) {
										resource.setWidth(width);
										resource.setHeight(height);

										logger.debug("Saving image dimensions -> " + resource.getDisplayname());
										service.save(resource);

									}
								} catch (Exception e) {
									logger.error(e, resource.getDisplayname()+ "  | " + resource.getMedia(), ServerConstant.NOT_THROWN);
								}
							}
						}
					}
				} else if ( (resource.getMedia() != null) && (resource.getMedia().contains("audio")) && (resource.getDurationMilliseconds()<=0)) {

					File file = null;

					try (InputStream is = os.getObject(resource.getBucketName(), resource.getObjectName())) {

						if (is != null) {

							try {

								file = new File(getSettings().getWorkDir(), resource.getName());

								try (InputStream in = os.getClient().getObject(resource.getBucketName(),
										resource.getObjectName())) {
									Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
								} catch (ODClientException e) {
									logger.error(e, resource.getDisplayname()+ "  | " + resource.getMedia(), ServerConstant.NOT_THROWN);
									return;

								} catch (IOException e) {
									logger.error(e, resource.getDisplayname()+ "  | " + resource.getMedia(), ServerConstant.NOT_THROWN);
									return;
								}

								long duration = MediaUtil.getAudioDurationMilliseconds(file);
		
								if (duration > 0) {
									resource.setDurationMilliseconds(duration);
									service.save(resource);
								}
							} catch (Exception e) {
								logger.error(e, resource.getDisplayname()+ "  | " + resource.getMedia(), ServerConstant.NOT_THROWN);
								return;
							}
						}

					} finally {
						if (file != null && file.exists()) {
							try {
								FileUtils.forceDelete(file);
							} catch (IOException e) {
								logger.error(e,  file.getName(), ServerConstant.NOT_THROWN);
							}
						}
					}

				}
			}

		} catch (Exception e) {
			logger.error(e, ServerConstant.NOT_THROWN);
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
