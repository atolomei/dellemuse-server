package dellemuse.serverapp.page.model;

import java.io.IOException;
import java.io.InputStream;

import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;

import dellemuse.model.util.ThumbnailSize;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.ArtWorkDBService;
import dellemuse.serverapp.serverdb.service.InstitutionDBService;
import dellemuse.serverapp.serverdb.service.ResourceDBService;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.UserDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.ResourceThumbnailService;
import wktui.base.ModelPanel;

public class DBModelPanel<T> extends ModelPanel<T> {

	private static final long serialVersionUID = 1L;

	public DBModelPanel(String id, IModel<T> model) {
		super(id, model);
	}
	
	protected Iterable<Institution> getInstitutions() {
		InstitutionDBService s = (InstitutionDBService) ServiceLocator.getInstance().getBean(InstitutionDBService.class);
		return s.findAllSorted();
	}

	protected InstitutionDBService getInstitutionDBService() {
		return (InstitutionDBService) ServiceLocator.getInstance().getBean(InstitutionDBService.class);
	}

	
	protected SiteDBService getSiteDBService() {
		return (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
	}
	
	protected ArtWorkDBService getArtWorkDBService() {
		return (ArtWorkDBService) ServiceLocator.getInstance().getBean(ArtWorkDBService.class);
	}

	
	protected ResourceDBService getResourceDBService() {
		return  (ResourceDBService) ServiceLocator.getInstance().getBean(ResourceDBService.class);
	}
	
	public UserDBService getUserDBService() {
		return (UserDBService) ServiceLocator.getInstance().getBean(UserDBService.class);
	}
	
	
	
	public Image getThumbnail(Resource resource) {
		
		if (resource == null)
			return null;
		String presignedThumbnail = getPresignedThumbnailSmall(resource);
		Image image;
		if (presignedThumbnail != null) {
			Url url = Url.parse(presignedThumbnail);
			UrlResourceReference resourceReference = new UrlResourceReference(url);
			image = new Image("image", resourceReference);
		} else {
			image = new Image("image", new UrlResourceReference(Url.parse("")));
			image.setVisible(false);
		}
		return image;
	}
	
	
	public String getPresignedThumbnailSmall(Resource photo) {
		try {
				if (photo.isUsethumbnail()) {
					ResourceThumbnailService service=(ResourceThumbnailService) ServiceLocator.getInstance().getBean(ResourceThumbnailService .class);
					String url = service.getPresignedThumbnailUrl( photo, ThumbnailSize.SMALL);
					//mark("PresignedThumbnailUrl - " + photo.getDisplayname());
					return url;
				} else {
				//mark("PresignedUrl - " + photo.getDisplayname());
				
				ObjectStorageService service=(ObjectStorageService) ServiceLocator.getInstance().getBean(ObjectStorageService.class);
				return service.getClient().getPresignedObjectUrl( photo.getBucketName(), photo.getObjectName());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	
	public Resource uploadFile(InputStream inputStream, String bucketName, String objectName, String fileName, long size) {

		try (InputStream is = inputStream) {
			getResourceDBService().upload(bucketName, objectName, is, fileName);
			User user = getUserDBService().findRoot();
			Resource resource = getResourceDBService().create(bucketName, objectName, fileName, fileName, size, user);
			
			return resource;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
	
}

