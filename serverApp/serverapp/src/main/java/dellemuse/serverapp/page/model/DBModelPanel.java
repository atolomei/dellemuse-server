package dellemuse.serverapp.page.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;

import dellemuse.model.util.NumberFormatter;
import dellemuse.model.util.ThumbnailSize;
import dellemuse.serverapp.command.CommandService;
import dellemuse.serverapp.command.ImageCalculateDimensionsCommand;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.ArtExhibitionDBService;
import dellemuse.serverapp.serverdb.service.ArtWorkDBService;
import dellemuse.serverapp.serverdb.service.InstitutionDBService;
import dellemuse.serverapp.serverdb.service.PersonDBService;
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
	

	
	public Optional<Person> getPerson(Long id) {
		PersonDBService service = (PersonDBService) ServiceLocator.getInstance().getBean(PersonDBService.class);
		return service.findById(id);
	}

	
	/** Iterable */

	protected Iterable<Institution> getInstitutions() {
		InstitutionDBService s = (InstitutionDBService) ServiceLocator.getInstance().getBean(InstitutionDBService.class);
		return s.findAllSorted();
	}

	public Iterable<Person> getPersons() {
		PersonDBService service = (PersonDBService) ServiceLocator.getInstance().getBean(PersonDBService.class);
		return service.findAllSorted();
	}

	protected Iterable<ArtExhibitionItem> getArtExhibitionItems(ArtExhibition o) {
		return getArtExhibitionDBService().getArtExhibitionItems(o);
	}
	
	public Iterable<Site> getSites(Institution in) {
		InstitutionDBService service = (InstitutionDBService) ServiceLocator.getInstance()
				.getBean(InstitutionDBService.class);
		return service.getSites(in.getId());
	}

	
	
	/** DBService */

	protected InstitutionDBService getInstitutionDBService() {
		return (InstitutionDBService) ServiceLocator.getInstance().getBean(InstitutionDBService.class);
	}
	
	protected SiteDBService getSiteDBService() {
		return (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
	}
	
	protected  ArtExhibitionDBService getArtExhibitionDBService() {
	 return ( ArtExhibitionDBService) ServiceLocator.getInstance().getBean(ArtExhibitionDBService.class);
	}

	protected ArtWorkDBService getArtWorkDBService() {
		return (ArtWorkDBService) ServiceLocator.getInstance().getBean(ArtWorkDBService.class);
	}
	
	protected PersonDBService getPersonDBService() {
		return  (PersonDBService) ServiceLocator.getInstance().getBean(PersonDBService.class);
	}
	
	protected ResourceDBService getResourceDBService() {
		return  (ResourceDBService) ServiceLocator.getInstance().getBean(ResourceDBService.class);
	}
	
	protected UserDBService getUserDBService() {
		return (UserDBService) ServiceLocator.getInstance().getBean(UserDBService.class);
	}
	
	protected CommandService getCommandService() {
		return (CommandService) ServiceLocator.getInstance().getBean(CommandService.class);
	}

	protected Optional<Resource> getResource( Long id) {
		return getResourceDBService().findById(id);
	}

	protected String getPhotoMeta(Resource resource) {
	
		if (resource == null)
			return null;
		
		String media = resource.getMedia();
		
		StringBuilder str =  new StringBuilder();
		
		str.append(resource.getName().toLowerCase());
		
		if (resource.getSize()>0) {
			str.append(" - " + NumberFormatter.formatFileSize(resource.getSize()) );
		}

		if (media!=null) {
			if (media.contains("image") && (resource.getHeight()>0 || resource.getWidth()>0))
			str.append(	" - " + String.valueOf( NumberFormatter.formatNumber( resource.getHeight() )));
			str.append(" x ");
			str.append(		String.valueOf( NumberFormatter.formatNumber( resource.getWidth() )));
		}
		
				return str.toString();
	}

	
	public Image getThumbnail(Resource resource) {
		
		if (resource == null)
			return null;
		
		String presignedThumbnail = getPresignedThumbnail(resource, ThumbnailSize.SMALL);
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
	
	
	public String getPresignedThumbnail(Resource photo, ThumbnailSize size ) {
		try {
			
			if (photo.getMedia().contains(".svg") || photo.getMedia().contains(".webp") ) {
				ObjectStorageService service=(ObjectStorageService) ServiceLocator.getInstance().getBean(ObjectStorageService.class);
				return service.getClient().getPresignedObjectUrl( photo.getBucketName(), photo.getObjectName());
			}
			else if (photo.isUsethumbnail()) {
				ResourceThumbnailService service=(ResourceThumbnailService) ServiceLocator.getInstance().getBean(ResourceThumbnailService .class);
				String url = service.getPresignedThumbnailUrl( photo, size );
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

	
	public Resource createAndUploadFile(InputStream inputStream, String bucketName, String objectName, String fileName, long size) {

		try (InputStream is = inputStream) {
			getResourceDBService().upload(bucketName, objectName, is, fileName);
			User user = getUserDBService().findRoot();
			Resource resource = getResourceDBService().create(bucketName, objectName, fileName,getResourceDBService().getMimeType(fileName), size, user);
			getCommandService().run(new ImageCalculateDimensionsCommand(resource.getId()));
			return resource;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}

/**
 *   alter table resource add column if not exists height integer default 0;
 *   alter table resource add column if not exists width integer default 0;
 */

