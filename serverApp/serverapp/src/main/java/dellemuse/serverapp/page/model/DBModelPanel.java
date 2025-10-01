package dellemuse.serverapp.page.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;

import dellemuse.model.util.NumberFormatter;
import dellemuse.model.util.ThumbnailSize;
import dellemuse.serverapp.command.CommandService;
import dellemuse.serverapp.command.ImageCalculateDimensionsCommand;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.ArtExhibitionDBService;
import dellemuse.serverapp.serverdb.service.ArtExhibitionGuideDBService;
import dellemuse.serverapp.serverdb.service.ArtExhibitionItemDBService;
import dellemuse.serverapp.serverdb.service.ArtWorkDBService;
import dellemuse.serverapp.serverdb.service.GuideContentDBService;
import dellemuse.serverapp.serverdb.service.InstitutionDBService;
import dellemuse.serverapp.serverdb.service.PersonDBService;
import dellemuse.serverapp.serverdb.service.ResourceDBService;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.UserDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.ResourceThumbnailService;
import io.wktui.model.TextCleaner;
import wktui.base.ModelPanel;

public class DBModelPanel<T> extends ModelPanel<T> {

	private static final long serialVersionUID = 1L;

	public DBModelPanel(String id, IModel<T> model) {
		super(id, model);
	}
	
	
	protected String getImageSrc(Resource res) {

		if (res == null)
			return null;
		
		if (!res.isDependencies())
			res = findResourceWithDeps(res.getId()).get();
			
		return getPresignedThumbnail(res, ThumbnailSize.MEDIUM);
	}
	
	
	protected String getImageSrc(ArtWork aw) {
		
		if (aw == null)
			return null;
		
		if (!aw.isDependencies())
			aw = findArtWorkWithDeps(aw.getId()).get();
		
		return getImageSrc(aw.getPhoto());
	}

	
	protected String getImageSrc(ArtExhibitionItem item) {
	
		if (item == null)
			return null;
		
		if (!item.isDependencies())
			item = findArtExhibitionItemWithDeps(item.getId()).get();
		
		return getImageSrc(item.getArtWork());
	}
	
	
	protected String getImageSrc(ArtExhibition ex) {
		
		if (ex == null)
			return null;
		
		if (!ex.isDependencies())
			ex = findArtExhibitionWithDeps(ex.getId()).get();
			
		return this.getImageSrc( ex.getPhoto());
	}
	
	
	protected String getImageSrc(ArtExhibitionGuide g) {

		if (g == null)
			return null;
		
		if (!g.isDependencies())  
			g=this.findArtExhibitionGuideWithDeps(g.getId()).get();
		 
		
		Resource res = g.getPhoto();
		
		if (res!=null)
			return getImageSrc(res);
		
		return getImageSrc(g.getArtExhibition());
		
	}
	
	protected String getImageSrc(GuideContent g) {

		if (g == null)
			return null;
		
		if (!g.isDependencies())  
			g=this.findGuideContentWithDeps(g.getId()).get();
		 
		Resource res = g.getPhoto();
		
		if (res!=null)
			return getImageSrc(res);
		
		return getImageSrc(g.getArtExhibitionItem());
	}

	
	protected User getRootUser() {
		return getUserDBService().findRoot();
	}
	
	/** Save */
	public void save(ArtExhibition ex) {
		getArtExhibitionDBService().save(ex);
	}
	
	public void removeItem(ArtExhibition ex, ArtExhibitionItem item, User removedBy) {
		getArtExhibitionDBService().removeItem(ex, item, removedBy);
	}

	public void removeItem(ArtExhibitionGuide ex, GuideContent item, User removedBy) {
		getArtExhibitionGuideDBService().removeItem(ex, item, removedBy);
	}
	
	
	public void addItem(ArtExhibition ex, ArtWork item, User addedBy) {
		getArtExhibitionDBService().addItem(ex, item, addedBy);
	}

	
	public void addItem(ArtExhibitionGuide g, ArtExhibitionItem item, User addedBy) {
		getGuideContentDBService().create(g, item, addedBy);
	}
	
	
	/** Deps */
	
	public Optional<ArtWork> findArtWorkWithDeps(Long id) {
		ArtWorkDBService service = (ArtWorkDBService) ServiceLocator.getInstance().getBean(ArtWorkDBService.class);
		return service.findWithDeps(id);
	}
	

	public Optional<ArtExhibition> findArtExhibitionWithDeps(Long id) {
		ArtExhibitionDBService service = (ArtExhibitionDBService) ServiceLocator.getInstance().getBean(ArtExhibitionDBService.class);
		return service.findWithDeps(id);
	}
	
	
	public Optional<ArtExhibitionItem> findArtExhibitionItemWithDeps(Long id) {
		ArtExhibitionItemDBService service = (ArtExhibitionItemDBService) ServiceLocator.getInstance().getBean(ArtExhibitionItemDBService.class);
		return service.findWithDeps(id);
	}
	
	
	public Optional<ArtExhibitionGuide> findArtExhibitionGuideWithDeps(Long id) {
		ArtExhibitionGuideDBService service = (ArtExhibitionGuideDBService) ServiceLocator.getInstance().getBean(ArtExhibitionGuideDBService.class);
		return service.findWithDeps(id);
	}
	
	public Optional<GuideContent> findGuideContentWithDeps(Long id) {
		GuideContentDBService service = (GuideContentDBService) ServiceLocator.getInstance().getBean(GuideContentDBService.class);
		return service.findWithDeps(id);
	}
	
	
	public Optional<Resource> findResourceWithDeps(Long id) {
		ResourceDBService service = (ResourceDBService) ServiceLocator.getInstance().getBean(ResourceDBService.class);
		return service.findWithDeps(id);
	}

	
	
	/** Deps END */
	
	
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

	public Iterable<Site> getSites(Institution in) {
		InstitutionDBService service = (InstitutionDBService) ServiceLocator.getInstance()
				.getBean(InstitutionDBService.class);
		return service.getSites(in.getId());
	}
	
	public Iterable<GuideContent> getGuideContents(ArtExhibitionGuide o) {
		return getArtExhibitionGuideDBService().getGuideContents(o);
	}
	
	public Iterable<ArtWork> getArtWorks(Person person) {
	 	return getSiteDBService().findDistinctArtWorkByPersonId( person.getId());
	}
	
	public Iterable<ArtWork> getSiteArtWorks(Site site) {
		return getSiteDBService().getSiteArtWorks(site);
		
	} 

	/** DBService  ------------------------- */

	protected InstitutionDBService getInstitutionDBService() {
		return (InstitutionDBService) ServiceLocator.getInstance().getBean(InstitutionDBService.class);
	}
	
	protected GuideContentDBService getGuideContentDBService() {
		return (GuideContentDBService) ServiceLocator.getInstance().getBean(GuideContentDBService.class);
	}
	
	protected  ArtExhibitionItemDBService getArtExhibitionItemDBService() {
		return ( ArtExhibitionItemDBService) ServiceLocator.getInstance().getBean(ArtExhibitionItemDBService.class);
	}

	protected SiteDBService getSiteDBService() {
		return (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
	}
	
	protected  ArtExhibitionDBService getArtExhibitionDBService() {
		return ( ArtExhibitionDBService) ServiceLocator.getInstance().getBean(ArtExhibitionDBService.class);
	}

	protected  ArtExhibitionGuideDBService getArtExhibitionGuideDBService() {
		return ( ArtExhibitionGuideDBService) ServiceLocator.getInstance().getBean(ArtExhibitionGuideDBService.class);
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

	/** DBService  ------------------------- */

	protected Iterable<ArtExhibitionItem> getArtExhibitionItems(ArtExhibition o) {
		return getArtExhibitionDBService().getArtExhibitionItems(o);
	}
	
	protected Iterable<ArtExhibitionGuide> getArtExhibitionIGuides(ArtExhibition o) {
		return getArtExhibitionDBService().getArtExhibitionGuides(o);
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
			str.append(" px ");
			str.append(" x ");
			str.append(		String.valueOf( NumberFormatter.formatNumber( resource.getWidth() )));
			str.append(" px ");
		}
		return str.toString();
	}
	

	
	public Image getThumbnail(Resource resource) {
		
		if (resource == null)
			return null;
		
		if (!resource.isDependencies()) {
			resource=this.getResourceDBService().findWithDeps(resource.getId()).get();
		}

		
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
			
			if (photo == null)
				return null;
			
			if (!photo.isDependencies()) {
				photo=this.getResourceDBService().findWithDeps(photo.getId()).get();
			}
			
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
	
	
	
	public String getPresignedThumbnailSmall(Resource photo) {
		try {

			if (photo == null)
				return null;
			
			if (!photo.isDependencies()) {
				photo=this.getResourceDBService().findWithDeps(photo.getId()).get();
			}
			
			if (photo.isUsethumbnail()) {
				ResourceThumbnailService service = (ResourceThumbnailService) ServiceLocator.getInstance()
						.getBean(ResourceThumbnailService.class);
				String url = service.getPresignedThumbnailUrl(photo, ThumbnailSize.SMALL);
				//mark("PresignedThumbnailUrl - " + photo.getDisplayname());
				return url;
			} else {
				//mark("PresignedUrl - " + photo.getDisplayname());

				ObjectStorageService service = (ObjectStorageService) ServiceLocator.getInstance()
						.getBean(ObjectStorageService.class);
				return service.getClient().getPresignedObjectUrl(photo.getBucketName(), photo.getObjectName());
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

	
	public String getArtistStr(ArtExhibitionItem item) {
	
		if (item==null)
			return null; 
		
		if (!item.isDependencies()) 
			item=this.findArtExhibitionItemWithDeps(item.getId()).get();
		
		return getArtistStr(item.getArtWork());
	}
	
	public String getArtistStr(ArtWork aw) {
		
		if (aw==null)
			return null; 
		
		if (!aw.isDependencies()) 
			aw=this.findArtWorkWithDeps(aw.getId()).get();
		
		StringBuilder info = new StringBuilder();
		
		int n = 0;
		for (Person p : aw.getArtists()) {
			if (n++ > 0)
				info.append(", ");
			info.append(p.getDisplayname());
		}
		String str = TextCleaner.truncate(info.toString(), 220);
		return str;
	}

	
	public String getInfo(GuideContent e) {
		return e.getInfo();
	}
	
	
	public String getInfo(ArtExhibition e) {
		return e.getInfo();
	}
	
	public String getInfo(ArtExhibitionGuide g) {

		if (g.getInfo()!=null) 
			return g.getInfo();
			
		if (!g.isDependencies())
			g=this.findArtExhibitionGuideWithDeps(g.getId()).get();
			
		return getInfo(g.getArtExhibition());
	}
	
	public String getInfo(ArtExhibitionItem item ) {

		StringBuilder str = new StringBuilder();
		
		boolean LF = false;

		if (item.getArtWork()!=null) {
			str.append(getArtistStr(item.getArtWork()));
			LF = true;
		}

		if (LF) {
			str.append("\n");
			LF = false;
		}
		
		if (item.getFloor()!=null) {
			str.append(item.getFloor().getName());
			LF=true;
		}

		if (LF) {
			str.append("\n");
			LF = false;
		}
		
		if (item.getRoom()!=null) {
			str.append( item.getRoom().getName());
			LF = true;
		}

		if (LF) {
			str.append("\n");
			LF = false;
		}
		
		if (item.getReadCode()!=null) {
			str.append(  getLabel("code").getObject() +". " + item.getReadCode());
			LF = false;
		}
		else {
			str.append(  getLabel("id").getObject() + ". " +  String.valueOf( item.getId() ));
			LF = false;
		}

		if (LF) {
			str.append("\n");
			LF = false;
		}

		if (item.getInfo()!=null) {
			str.append( "Info: " + TextCleaner.clean(item.getInfo()));
			LF = false;
		}
		
		return str.toString();
	}

	protected String getAudioMeta(Resource resource) {
		
		if (resource == null)
			return null;
		
		StringBuilder str =  new StringBuilder();

		String media = resource.getMedia();
		
		str.append(resource.getName().toLowerCase());
		
		if (resource.getSize()>0) {
			str.append(" - " + NumberFormatter.formatFileSize(resource.getSize()) );
		}

		if (media!=null) {
			if (media.contains("audio")) {
				str.append(" - " + media);
				str.append(	" - [duration] ");
			}
		}
		return str.toString();
	}
	

	
}

/**
 *   alter table resource add column if not exists height integer default 0;
 *   alter table resource add column if not exists width integer default 0;
 */

