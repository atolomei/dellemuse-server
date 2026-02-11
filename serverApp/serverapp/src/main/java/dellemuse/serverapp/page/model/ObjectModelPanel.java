package dellemuse.serverapp.page.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.NumberFormatter;
import dellemuse.model.util.ThumbnailSize;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.command.CommandService;
import dellemuse.serverapp.elevenlabs.ElevenLabsService;
import dellemuse.serverapp.icons.Icons;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.ArtExhibitionSection;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Artist;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Identifiable;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
 
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.ArtExhibitionDBService;
import dellemuse.serverapp.serverdb.service.ArtExhibitionGuideDBService;
import dellemuse.serverapp.serverdb.service.ArtExhibitionItemDBService;
import dellemuse.serverapp.serverdb.service.ArtExhibitionSectionDBService;
import dellemuse.serverapp.serverdb.service.ArtWorkDBService;
import dellemuse.serverapp.serverdb.service.ArtistDBService;
import dellemuse.serverapp.serverdb.service.AudioStudioDBService;
import dellemuse.serverapp.serverdb.service.GuideContentDBService;
import dellemuse.serverapp.serverdb.service.InstitutionDBService;
import dellemuse.serverapp.serverdb.service.PersonDBService;
import dellemuse.serverapp.serverdb.service.ResourceDBService;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.UserDBService;
import dellemuse.serverapp.serverdb.service.VoiceDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.serverdb.service.record.ArtExhibitionGuideRecordDBService;
import dellemuse.serverapp.serverdb.service.record.ArtExhibitionItemRecordDBService;
import dellemuse.serverapp.serverdb.service.record.ArtExhibitionRecordDBService;
import dellemuse.serverapp.serverdb.service.record.ArtWorkRecordDBService;
import dellemuse.serverapp.serverdb.service.record.GuideContentRecordDBService;
import dellemuse.serverapp.serverdb.service.record.InstitutionRecordDBService;
import dellemuse.serverapp.serverdb.service.record.PersonRecordDBService;
import dellemuse.serverapp.serverdb.service.record.SiteRecordDBService;
import dellemuse.serverapp.serverdb.service.security.RoleDBService;
import dellemuse.serverapp.serverdb.service.security.RoleGeneralDBService;
import dellemuse.serverapp.serverdb.service.security.RoleInstitutionDBService;
import dellemuse.serverapp.serverdb.service.security.RoleSiteDBService;
import dellemuse.serverapp.service.DateTimeService;
import dellemuse.serverapp.service.ResourceThumbnailService;
import dellemuse.serverapp.service.language.LanguageObjectService;
import dellemuse.serverapp.service.language.LanguageService;
import dellemuse.serverapp.service.language.TranslationService;
import io.wktui.model.TextCleaner;
import wktui.base.ModelPanel;

public class ObjectModelPanel<T> extends ModelPanel<T> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ObjectModelPanel.class.getName());

	public ObjectModelPanel(String id, IModel<T> model) {
		super(id, model);
	}

	protected <S extends Identifiable> List<IModel<S>> iFilter(List<IModel<S>> initialList, String filter) {
		List<IModel<S>> list = new ArrayList<IModel<S>>();
		final String str = filter.trim().toLowerCase();
		initialList.forEach(s -> {
			if (s.getObject().getName().toLowerCase().contains(str)) {
				list.add(s);
			}
		});
		return list;
	}
	
	
	/** Save --------------------------------------------------------- */

	
	protected IModel<String> getObjectTitle(MultiLanguageObject o) {
		StringBuilder str = new StringBuilder();
		str.append(getLanguageObjectService().getObjectDisplayName(o, getLocale()));
		
		if (o.getState() == ObjectState.DELETED)
			return new Model<String>(str.toString() + Icons.DELETED_ICON);

		if (o.getState() == ObjectState.EDITION)
			return new Model<String>(str.toString() + Icons.EDITION_ICON);
		
		return Model.of(str.toString());
	}
	
	
	protected IModel<String> getObjectSubtitle(MultiLanguageObject o) {
		StringBuilder str = new StringBuilder();
		str.append(getLanguageObjectService().getObjectSubtitle(o, getLocale()));
		return Model.of(str.toString());
	}
	
	protected IModel<String> getObjectInfo(MultiLanguageObject o) {
		StringBuilder str = new StringBuilder();
		str.append(getLanguageObjectService().getInfo(o, getLocale()));
		return Model.of(str.toString());
	}
	
	
	/** Save --------------------------------------------------------- */


	public LanguageService getLanguageService() {
		return (LanguageService) ServiceLocator.getInstance().getBean(LanguageService.class);
	}


	public LanguageObjectService getLanguageObjectService() {
		return (LanguageObjectService) ServiceLocator.getInstance().getBean(LanguageObjectService.class);
	}
	
	
	
	/** Deps --------------------------------------------------------- */

	public Optional<ArtWork> findArtWorkWithDeps(Long id) {
		ArtWorkDBService service = (ArtWorkDBService) ServiceLocator.getInstance().getBean(ArtWorkDBService.class);
		return service.findWithDeps(id);
	}

	public Optional<ArtExhibition> findArtExhibitionWithDeps(Long id) {
		ArtExhibitionDBService service = (ArtExhibitionDBService) ServiceLocator.getInstance().getBean(ArtExhibitionDBService.class);
		return service.findWithDeps(id);
	}

	public Optional<ArtExhibitionSection> findArtExhibitionSectionWithDeps(Long id) {
		ArtExhibitionSectionDBService service = (ArtExhibitionSectionDBService) ServiceLocator.getInstance().getBean(ArtExhibitionSectionDBService.class);
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

	public Optional<Artist> getArtist(Long id) {
		ArtistDBService service = (ArtistDBService) ServiceLocator.getInstance().getBean(ArtistDBService.class);
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

	public Iterable<Artist> getArtists() {
		ArtistDBService service = (ArtistDBService) ServiceLocator.getInstance().getBean(ArtistDBService.class);
		return service.findAllSorted();
	}
	
	public Iterable<Site> getSites(Institution in) {
		InstitutionDBService service = (InstitutionDBService) ServiceLocator.getInstance().getBean(InstitutionDBService.class);
		return service.getSites(in.getId());
	}

	public Iterable<GuideContent> getGuideContents(ArtExhibitionGuide o) {
		return getArtExhibitionGuideDBService().getGuideContents(o);
	}

	public Iterable<GuideContent> getSiteGuideContents(Site s) {
		return getGuideContentDBService().getBySite(s);
	}

	public Iterable<ArtWork> getArtWorks(Artist a) {
		if (a.isDependencies())
			a=getArtistDBService().findWithDeps(a.getId()).get();
		return a.getArtworks();
	}

	public Iterable<ArtWork> getSiteArtWorks(Site site) {
		return getSiteDBService().getSiteArtWorks(site, ObjectState.EDITION, ObjectState.PUBLISHED);
	}

	public Iterable<GuideContent> getGuideContens(ArtExhibitionItem o) {
		return getArtExhibitionItemDBService().getGuideContents(o);
	}


	/**
	 * 
	 * @param resource
	 * @return
	 */
	public Image getThumbnail(Resource resource) {

		if (resource == null)
			return null;

		if (!resource.isDependencies()) {
			resource = this.getResourceDBService().findWithDeps(resource.getId()).get();
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

	public String getPresignedThumbnail(Resource photo, ThumbnailSize size) {
		try {

			if (photo == null)
				return null;

			if (!photo.isDependencies()) {
				photo = this.getResourceDBService().findWithDeps(photo.getId()).get();
			}
			
			if (	(photo.getMedia() == null) || 
					(photo.getMedia().length()==0) ||
					 photo.getMedia().endsWith("svg+xml") ||
					 photo.getMedia().endsWith("svg") ||
					 photo.getMedia().endsWith("webp")) {
			
				ObjectStorageService service = (ObjectStorageService) ServiceLocator.getInstance().getBean(ObjectStorageService.class);
				return service.getClient().getPresignedObjectUrl(photo.getBucketName(), photo.getObjectName());
			
			} else if (photo.isUsethumbnail()) {
				ResourceThumbnailService service = (ResourceThumbnailService) ServiceLocator.getInstance().getBean(ResourceThumbnailService.class);
				String url = service.getPresignedThumbnailUrl(photo, size);
				// mark("PresignedThumbnailUrl - " + photo.getDisplayname());
				return url;
			} else {
				// mark("PresignedUrl - " + photo.getDisplayname());

				ObjectStorageService service = (ObjectStorageService) ServiceLocator.getInstance().getBean(ObjectStorageService.class);
				return service.getClient().getPresignedObjectUrl(photo.getBucketName(), photo.getObjectName());
			}
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
	}

	public String getPresignedThumbnailSmall(Resource photo) {
		try {

			if (photo == null)
				return null;

			if (!photo.isDependencies()) {
				photo = this.getResourceDBService().findWithDeps(photo.getId()).get();
			}

			if ((photo.getMedia() != null) && (photo.getMedia().endsWith("svg") || photo.getMedia().endsWith("webp"))) {
				ObjectStorageService service = (ObjectStorageService) ServiceLocator.getInstance().getBean(ObjectStorageService.class);
				return service.getClient().getPresignedObjectUrl(photo.getBucketName(), photo.getObjectName());
			}

			else if (photo.isUsethumbnail()) {
				ResourceThumbnailService service = (ResourceThumbnailService) ServiceLocator.getInstance().getBean(ResourceThumbnailService.class);
				String url = service.getPresignedThumbnailUrl(photo, ThumbnailSize.SMALL);
				// mark("PresignedThumbnailUrl - " + photo.getDisplayname());
				return url;
			} else {
				// mark("PresignedUrl - " + photo.getDisplayname());

				ObjectStorageService service = (ObjectStorageService) ServiceLocator.getInstance().getBean(ObjectStorageService.class);
				return service.getClient().getPresignedObjectUrl(photo.getBucketName(), photo.getObjectName());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getPresignedUrl(Resource resource) {
		try {
			
			if (resource==null)
				return "";
		
			Optional<Integer> urlExpiresInSeconds = Optional.of(360);
			Optional<Integer> objectCacheExpiresInSeconds = Optional.of(360);
			String url = getObjectStorageService().getClient().getPresignedObjectUrl(resource.getBucketName(), resource.getObjectName(), urlExpiresInSeconds, objectCacheExpiresInSeconds);
			
			logger.debug(url);

			return url;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public ObjectStorageService getObjectStorageService() {
		return (ObjectStorageService) ServiceLocator.getInstance().getBean(ObjectStorageService.class);
	}

	public String getArtistStr(ArtExhibitionItem item) {

		if (item == null)
			return null;

		if (!item.isDependencies())
			item = this.findArtExhibitionItemWithDeps(item.getId()).get();

		return getArtistStr(item.getArtWork());
	}

	public String getArtistStr(ArtWork aw) {

		if (aw == null)
			return null;

		if (!aw.isDependencies())
			aw = this.findArtWorkWithDeps(aw.getId()).get();

		StringBuilder info = new StringBuilder();

		int n = 0;
	
		for (Artist a : aw.getArtists()) {
			if (n++ > 0)
				info.append(", ");
			info.append(a.getFirstLastname());
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

	public String getInfo(ArtExhibitionSection e) {
		return e.getInfo();
	}

	public String getInfo(ArtExhibitionGuide g, boolean useExhibitionIfNull) {

		if (!g.isDependencies())
			g = this.findArtExhibitionGuideWithDeps(g.getId()).get();

		List<String> l = new ArrayList<String>();

		StringBuilder str = new StringBuilder();

		if (g.getPublisher() != null)
			l.add("publisher: " + g.getPublisher().getDisplayName());

		if (g.getAudio() != null)
			l.add("audio: " + g.getAudio().getDisplayname());

		if (g.getGuideContents() != null)
			l.add("artworks: " + String.valueOf(g.getGuideContents().size()));

		if (g.getInfo() != null) {
			l.add("info: " + g.getInfo());
		} else if (useExhibitionIfNull) {
			l.add("info: " + getInfo(g.getArtExhibition()));
		}

		l.forEach(item -> str.append(((str.length() > 0) ? "\n" : "") + item));

		return TextCleaner.clean(str.toString());
	}

	public String getInfo(ArtExhibitionItem item) {

		StringBuilder str = new StringBuilder();

		boolean LF = false;

		if (item.getArtWork() != null) {
			str.append(getArtistStr(item.getArtWork()));
			LF = true;
		}

		if (LF) {
			str.append("\n");
			LF = false;
		}

		if (item.getFloor() != null) {
			str.append(item.getFloor().getName());
			LF = true;
		}

		if (LF) {
			str.append("\n");
			LF = false;
		}

		if (item.getRoom() != null) {
			str.append(item.getRoom().getName());
			LF = true;
		}

		if (LF) {
			str.append("\n");
			LF = false;
		}

		if (item.getReadCode() != null) {
			str.append(getLabel("code").getObject() + ". " + item.getReadCode());
			LF = false;
		} else {
			str.append(getLabel("id").getObject() + ". " + String.valueOf(item.getId()));
			LF = false;
		}

		if (LF) {
			str.append("\n");
			LF = false;
		}

		if (item.getInfo() != null) {
			str.append("Info: " + TextCleaner.clean(item.getInfo()));
			LF = false;
		}

		return str.toString();
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

		return this.getImageSrc(ex.getPhoto());
	}

	protected String getImageSrc(ArtExhibitionSection ex) {

		if (ex == null)
			return null;

		if (!ex.isDependencies())
			ex = findArtExhibitionSectionWithDeps(ex.getId()).get();

		return this.getImageSrc(ex.getPhoto());
	}

	protected String getImageSrc(ArtExhibitionGuide g) {

		if (g == null)
			return null;

		if (!g.isDependencies())
			g = this.findArtExhibitionGuideWithDeps(g.getId()).get();

		Resource res = g.getPhoto();

		if (res != null)
			return getImageSrc(res);

		return getImageSrc(g.getArtExhibition());

	}

	protected String getImageSrc(GuideContent g) {

		if (g == null)
			return null;

		if (!g.isDependencies())
			g = this.findGuideContentWithDeps(g.getId()).get();

		Resource res = g.getPhoto();

		if (res != null)
			return getImageSrc(res);

		return getImageSrc(g.getArtExhibitionItem());
	}

	protected User getRootUser() {
		return getUserDBService().findRoot();
	}

	protected Optional<User> getSessionUser() {
		User user=getUserDBService().getSessionUser();
		if (user==null)
			return Optional.empty();
		return Optional.of(user);
	}

	
	/** DBService ------------------------- */

	protected InstitutionDBService getInstitutionDBService() {
		return (InstitutionDBService) ServiceLocator.getInstance().getBean(InstitutionDBService.class);
	}

	protected InstitutionRecordDBService getInstitutionRecordDBService() {
		return (InstitutionRecordDBService) ServiceLocator.getInstance().getBean(InstitutionRecordDBService.class);
	}

	protected GuideContentDBService getGuideContentDBService() {
		return (GuideContentDBService) ServiceLocator.getInstance().getBean(GuideContentDBService.class);
	}

	protected GuideContentRecordDBService getGuideContentRecordDBService() {
		return (GuideContentRecordDBService) ServiceLocator.getInstance().getBean(GuideContentRecordDBService.class);
	}

	protected ArtExhibitionItemDBService getArtExhibitionItemDBService() {
		return (ArtExhibitionItemDBService) ServiceLocator.getInstance().getBean(ArtExhibitionItemDBService.class);
	}

	protected ArtExhibitionItemRecordDBService getArtExhibitionItemRecordDBService() {
		return (ArtExhibitionItemRecordDBService) ServiceLocator.getInstance().getBean(ArtExhibitionItemRecordDBService.class);
	}

	protected DateTimeService getDateTimeService() {
		return (DateTimeService) ServiceLocator.getInstance().getBean(DateTimeService.class);
	}

	protected RoleDBService getRoleDBService() {
		return (RoleDBService) ServiceLocator.getInstance().getBean(RoleDBService.class);
	}

	protected RoleGeneralDBService getRoleGeneralDBService() {
		return (RoleGeneralDBService) ServiceLocator.getInstance().getBean(RoleGeneralDBService.class);
	}

	protected RoleSiteDBService getRoleSiteDBService() {
		return (RoleSiteDBService) ServiceLocator.getInstance().getBean(RoleSiteDBService.class);
	}
	
	protected RoleInstitutionDBService getRoleInstitutionDBService() {
		return (RoleInstitutionDBService) ServiceLocator.getInstance().getBean(RoleInstitutionDBService.class);
	}
	
	public AudioStudioDBService getAudioStudioDBService() {
		return (AudioStudioDBService) ServiceLocator.getInstance().getBean(AudioStudioDBService.class);
	}
	
	protected SiteDBService getSiteDBService() {
		return (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
	}

	protected SiteRecordDBService getSiteRecordDBService() {
		return (SiteRecordDBService) ServiceLocator.getInstance().getBean(SiteRecordDBService.class);
	}

	protected ArtExhibitionDBService getArtExhibitionDBService() {
		return (ArtExhibitionDBService) ServiceLocator.getInstance().getBean(ArtExhibitionDBService.class);
	}

	protected ArtExhibitionRecordDBService getArtExhibitionRecordDBService() {
		return (ArtExhibitionRecordDBService) ServiceLocator.getInstance().getBean(ArtExhibitionRecordDBService.class);
	}

	protected ArtExhibitionGuideDBService getArtExhibitionGuideDBService() {
		return (ArtExhibitionGuideDBService) ServiceLocator.getInstance().getBean(ArtExhibitionGuideDBService.class);
	}

	protected ArtExhibitionGuideRecordDBService getArtExhibitionGuideRecordDBService() {
		return (ArtExhibitionGuideRecordDBService) ServiceLocator.getInstance().getBean(ArtExhibitionGuideRecordDBService.class);
	}

	protected ArtWorkDBService getArtWorkDBService() {
		return (ArtWorkDBService) ServiceLocator.getInstance().getBean(ArtWorkDBService.class);
	}

	protected ArtWorkRecordDBService getArtWorkRecordDBService() {
		return (ArtWorkRecordDBService) ServiceLocator.getInstance().getBean(ArtWorkRecordDBService.class);
	}

	protected ArtistDBService getArtistDBService() {
		return (ArtistDBService) ServiceLocator.getInstance().getBean(ArtistDBService.class);
	}

	
	protected PersonDBService getPersonDBService() {
		return (PersonDBService) ServiceLocator.getInstance().getBean(PersonDBService.class);
	}

	protected PersonRecordDBService getPersonRecordDBService() {
		return (PersonRecordDBService) ServiceLocator.getInstance().getBean(PersonRecordDBService.class);
	}

	protected ResourceDBService getResourceDBService() {
		return (ResourceDBService) ServiceLocator.getInstance().getBean(ResourceDBService.class);
	}

	protected UserDBService getUserDBService() {
		return (UserDBService) ServiceLocator.getInstance().getBean(UserDBService.class);
	}

	protected CommandService getCommandService() {
		return (CommandService) ServiceLocator.getInstance().getBean(CommandService.class);
	}

	protected TranslationService getTranslationService() {
		return (TranslationService) ServiceLocator.getInstance().getBean(TranslationService.class);
	}

	protected ElevenLabsService getElevenLabsService() {
		return (ElevenLabsService) ServiceLocator.getInstance().getBean(ElevenLabsService.class);
	}

	protected VoiceDBService getVoiceDBService() {
		return (VoiceDBService) ServiceLocator.getInstance().getBean(VoiceDBService.class);
	}

	
	/** DBService ------------------------- */

	protected Iterable<ArtExhibitionItem> getArtExhibitionItems(ArtExhibition o) {
		return getArtExhibitionDBService().getArtExhibitionItems(o);
	}

	protected Iterable<ArtExhibitionSection> getArtExhibitionSections(ArtExhibition o) {
		return getArtExhibitionDBService().getArtExhibitionSections(o);
	}

	protected Iterable<ArtExhibitionGuide> getArtExhibitionIGuides(ArtExhibition o) {
		return getArtExhibitionDBService().getArtExhibitionGuides(o);
	}

	protected Optional<Resource> getResource(Long id) {
		return getResourceDBService().findById(id);
	}

	protected String getPhotoMeta(Resource resource) {

		if (resource == null)
			return null;

		String media = resource.getMedia();

		StringBuilder str = new StringBuilder();

		str.append(resource.getName().toLowerCase());

		if (resource.getSize() > 0) {
			str.append(" - " + NumberFormatter.formatFileSize(resource.getSize()));
		}

		if (media != null) {
			if (media.contains("image") && (resource.getHeight() > 0 || resource.getWidth() > 0))
				str.append(" - " + String.valueOf(NumberFormatter.formatNumber(resource.getHeight())));
			str.append(" px ");
			str.append(" x ");
			str.append(String.valueOf(NumberFormatter.formatNumber(resource.getWidth())));
			str.append(" px ");
		}
		return str.toString();
	}

	protected String getAudioMeta(IModel<Resource> model) {
		if (model == null || model.getObject() == null)
			return "";
		return getAudioMeta(model.getObject());
	}

	protected String getAudioMeta(Resource resource) {

		if (resource == null)
			return "";

		if (!resource.isDependencies())
			resource = getResourceDBService().findWithDeps(resource.getId()).get();

		StringBuilder str = new StringBuilder();
		String media = resource.getMedia();
		str.append(resource.getName().toLowerCase());

		if (media != null) {
			if (media.contains("audio")) {
				// str.append(" - " + media);
				if (resource.getDurationMilliseconds() > 0) {
					str.append(" - " + NumberFormatter.formatDuration(resource.getDurationMilliseconds()));
				}
			}
		}

		if (resource.getSize() > 0) {
			str.append(" - " + NumberFormatter.formatFileSize(resource.getSize()));
		}

		str.append("<br/>");
		str.append(getLabel("manually-uploaded", resource.getLastModifiedUser().getName(), getDateTimeService().timeElapsed(resource.getLastModified())).getObject());

		return str.toString();
	}
}
