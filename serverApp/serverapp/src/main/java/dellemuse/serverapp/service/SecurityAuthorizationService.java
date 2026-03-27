package dellemuse.serverapp.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Artist;
import dellemuse.serverapp.serverdb.model.DelleMuseObject;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
import dellemuse.serverapp.serverdb.model.security.RoleInstitution;
import dellemuse.serverapp.serverdb.model.security.RoleSite;
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
import dellemuse.serverapp.serverdb.service.MusicDBService;
import dellemuse.serverapp.serverdb.service.PersonDBService;
import dellemuse.serverapp.serverdb.service.ResourceDBService;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.UserDBService;
import dellemuse.serverapp.serverdb.service.VoiceDBService;
import dellemuse.serverapp.serverdb.service.base.BaseService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.serverdb.service.record.ArtExhibitionGuideRecordDBService;
import dellemuse.serverapp.serverdb.service.record.ArtExhibitionItemRecordDBService;
import dellemuse.serverapp.serverdb.service.record.ArtExhibitionRecordDBService;
import dellemuse.serverapp.serverdb.service.record.ArtExhibitionSectionRecordDBService;
import dellemuse.serverapp.serverdb.service.record.ArtWorkRecordDBService;
import dellemuse.serverapp.serverdb.service.record.ArtistRecordDBService;
import dellemuse.serverapp.serverdb.service.record.GuideContentRecordDBService;
import dellemuse.serverapp.serverdb.service.record.InstitutionRecordDBService;
import dellemuse.serverapp.serverdb.service.record.PersonRecordDBService;
import dellemuse.serverapp.serverdb.service.record.SiteRecordDBService;
import jakarta.transaction.Transactional;

@Service
public class SecurityAuthorizationService extends BaseService {

	@Autowired
	private final UserDBService userDBService;

	public SecurityAuthorizationService(ServerDBSettings settings, UserDBService userDBService) {
		super(settings);
		this.userDBService = userDBService;
	}

	public boolean canCreateObject(Class<? extends DelleMuseObject> objectClass, User user) {
		return true;
	}

	/**
	 * public T boolean canReadObject(T extends DelleMuseObject objectClass, User
	 * user) { return true; }
	 * 
	 * public boolean canWriteObject(Class<? extends DelleMuseObject> objectClass,
	 * User user) { return true; }
	 * 
	 * public boolean canDeleteObject(Class<? extends DelleMuseObject> objectClass,
	 * User user) { return true; }
	 **/
	
	@Transactional
	public <T extends MultiLanguageObject> boolean isSiteAdminOrEditor(Optional<User> o, T s) {

		if (s == null)
			return false;

		if (o.isEmpty())
			return false;

		User user = o.get();

		if (!user.isDependencies()) {
			user = getUserDBService().findWithDeps(user.getId()).get();
		}

		if (o.isEmpty())
			return false;
		
		if (s instanceof Site)
			return isSiteAdminOrEditor(o, s.getId());
		
		
		if (s instanceof ArtExhibition) {
			return isSiteAdminOrEditor(o, ((Site) ((ArtExhibition) s).getSite()).getId());
		}
		
		if (s instanceof ArtExhibitionItem) {
			return isSiteAdminOrEditor(o, ((Site) ((ArtExhibitionItem) s).getArtExhibition().getSite()).getId());
		}
		
		if (s instanceof ArtExhibitionGuide) {
			return isSiteAdminOrEditor(o, (Site) ((ArtExhibitionGuide) s).getArtExhibition().getSite());
		}
	
		if (s instanceof GuideContent) {
			return isSiteAdminOrEditor(o, ((Site) ((GuideContent) s).getArtExhibitionItem().getArtExhibition().getSite()).getId());
		}
		
		if (s instanceof Artist) {
			return isSiteAdminOrEditor(o, ((Site) ((Artist) s).getSite()).getId());
		}
		
		if (s instanceof ArtWork) {
			return isSiteAdminOrEditor(o, (Site) ((ArtWork) s).getSite());
		}
		
		return true;
	}

	
	
	@Transactional
	public boolean isSiteAdminOrEditor(Optional<User> o, Long siteId) {

		if (siteId == null)
			return false;

		if (o.isEmpty())
			return false;

		User user = o.get();

		if (!user.isDependencies()) {
			user = getUserDBService().findWithDeps(user.getId()).get();
		}
 

		return user.getRolesSite().stream().filter(ia -> (ia.getKey().equals(RoleInstitution.ADMIN) || ia.getKey().equals(RoleSite.EDITOR)) && (ia.getSite().getId().equals(siteId))).findAny().isPresent();

	}

	@Transactional
	public boolean isSiteAdmin(Optional<User> o, Long siteId) {

		if (siteId == null)
			return false;

		if (o.isEmpty())
			return false;

		User user = o.get();

		if (!user.isDependencies()) {
			user = getUserDBService().findWithDeps(user.getId()).get();
		}
 	 

		return user.getRolesSite().stream().filter(ia -> (ia.getKey().equals(RoleInstitution.ADMIN)) && (ia.getSite().getId().equals(siteId))).findAny().isPresent();

	}

	
	@Transactional
	public boolean isInstitutionAdminOrAudit(Optional<User> o, Long in) {

		if (in == null)
			return false;

		if (o.isEmpty())
			return false;

		User user = o.get();

		if (!user.isDependencies()) {
			user = getUserDBService().findWithDeps(user.getId()).get();
		}

		return user.getRolesInstitution().stream().filter(ia -> (ia.getKey().equals(RoleInstitution.ADMIN) || ia.getKey().equals(RoleInstitution.AUDIT)) && (ia.getInstitution().getId().equals(in ))).findAny().isPresent();

	}
	@Transactional
	public boolean isInstitutionAdmin(Optional<User> o, Long in) {

		if (in == null)
			return false;

		if (o.isEmpty())
			return false;

		User user = o.get();

		if (!user.isDependencies()) {
			user = getUserDBService().findWithDeps(user.getId()).get();
		}

		return user.getRolesInstitution().stream().filter(ia -> ia.getKey().equals(RoleInstitution.ADMIN) && ia.getInstitution().getId().equals(in)).findAny().isPresent();

	}
	
	@Transactional
	public boolean isGeneralAdminOrAudit(Optional<User> o) {

		if (o.isEmpty()) {
			return false;
		}

		User user = o.get();

		if (!user.isDependencies()) {
			user = getUserDBService().findWithDeps(user.getId()).get();
		}

		Set<RoleGeneral> set = user.getRolesGeneral();

		if (set == null)
			return false;

		return set.stream().anyMatch((p -> p.getKey().equals(RoleGeneral.ADMIN) || p.getKey().equals(RoleGeneral.AUDIT)));

	}

	@Transactional
	public boolean isGeneralAdmin(Optional<User> o) {

		if (o.isEmpty()) {
			return false;
		}

		User user = o.get();

		if (!user.isDependencies()) {
			user = getUserDBService().findWithDeps(user.getId()).get();
		}

		Set<RoleGeneral> set = user.getRolesGeneral();

		if (set == null)
			return false;

		return set.stream().anyMatch((p -> p.getKey().equals(RoleGeneral.ADMIN)));
	}

	@Transactional
	public boolean isGeneralAudit(Optional<User> o) {

		if (o.isEmpty()) {
			return false;
		}

		User user = o.get();

		if (!user.isDependencies()) {
			user = getUserDBService().findWithDeps(user.getId()).get();
		}

		Set<RoleGeneral> set = user.getRolesGeneral();

		if (set == null)
			return false;

		return set.stream().anyMatch((p -> p.getKey().equals(RoleGeneral.AUDIT)));
	}

	
	public User getRootUser() {
		return getUserDBService().findRoot();
	}

	protected UserDBService getUserDBService() {
		return userDBService;
	}

	/** DB Services */

	protected ArtExhibitionRecordDBService getArtExhibitionRecordDBService() {
		return (ArtExhibitionRecordDBService) ServiceLocator.getInstance().getBean(ArtExhibitionRecordDBService.class);
	}

	protected ArtExhibitionSectionDBService getArtExhibitionSectionDBService() {
		return (ArtExhibitionSectionDBService) ServiceLocator.getInstance().getBean(ArtExhibitionSectionDBService.class);
	}

	protected ArtExhibitionSectionRecordDBService getArtExhibitionSectionRecordDBService() {
		return (ArtExhibitionSectionRecordDBService) ServiceLocator.getInstance().getBean(ArtExhibitionSectionRecordDBService.class);
	}

	protected ArtExhibitionGuideDBService getArtExhibitionGuideDBService() {
		return (ArtExhibitionGuideDBService) ServiceLocator.getInstance().getBean(ArtExhibitionGuideDBService.class);
	}

	protected ArtExhibitionGuideRecordDBService getArtExhibitionGuideRecordDBService() {
		return (ArtExhibitionGuideRecordDBService) ServiceLocator.getInstance().getBean(ArtExhibitionGuideRecordDBService.class);
	}

	protected ArtistDBService getArtistDBService() {
		return (ArtistDBService) ServiceLocator.getInstance().getBean(ArtistDBService.class);
	}

	protected ArtistRecordDBService getArtistRecordDBService() {
		return (ArtistRecordDBService) ServiceLocator.getInstance().getBean(ArtistRecordDBService.class);
	}

	protected AudioStudioDBService getAudioStudioDBService() {
		return (AudioStudioDBService) ServiceLocator.getInstance().getBean(AudioStudioDBService.class);
	}

	protected ArtExhibitionItemDBService getArtExhibitionItemDBService() {
		return (ArtExhibitionItemDBService) ServiceLocator.getInstance().getBean(ArtExhibitionItemDBService.class);
	}

	protected ArtExhibitionItemRecordDBService getArtExhibitionItemRecordDBService() {
		return (ArtExhibitionItemRecordDBService) ServiceLocator.getInstance().getBean(ArtExhibitionItemRecordDBService.class);
	}

	protected ArtWorkDBService getArtWorkDBService() {
		return (ArtWorkDBService) ServiceLocator.getInstance().getBean(ArtWorkDBService.class);
	}

	protected ArtWorkRecordDBService getArtWorkRecordDBService() {
		return (ArtWorkRecordDBService) ServiceLocator.getInstance().getBean(ArtWorkRecordDBService.class);
	}

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

	protected PersonDBService getPersonDBService() {
		return (PersonDBService) ServiceLocator.getInstance().getBean(PersonDBService.class);
	}

	protected PersonRecordDBService getPersonRecordDBService() {
		return (PersonRecordDBService) ServiceLocator.getInstance().getBean(PersonRecordDBService.class);
	}

	protected SiteDBService getSiteDBService() {
		return (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
	}

	protected SiteRecordDBService getSiteRecordDBService() {
		return (SiteRecordDBService) ServiceLocator.getInstance().getBean(SiteRecordDBService.class);
	}

	protected ResourceDBService getResourceDBService() {
		return (ResourceDBService) ServiceLocator.getInstance().getBean(ResourceDBService.class);
	}

	public ObjectStorageService getObjectStorageService() {
		return (ObjectStorageService) ServiceLocator.getInstance().getBean(ObjectStorageService.class);
	}

	protected VoiceDBService getVoiceDBService() {
		return (VoiceDBService) ServiceLocator.getInstance().getBean(VoiceDBService.class);
	}

	protected MusicDBService getMusicDBService() {
		return (MusicDBService) ServiceLocator.getInstance().getBean(MusicDBService.class);
	}


}
