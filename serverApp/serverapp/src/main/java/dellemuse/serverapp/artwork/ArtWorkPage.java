package dellemuse.serverapp.artwork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

 
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
 
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
 
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
 
import org.wicketstuff.annotation.mount.MountPath;
 
import dellemuse.model.logging.Logger;
 
import dellemuse.model.util.ThumbnailSize;
 
import dellemuse.serverapp.editor.ObjectMarkAsDeleteEvent;
 
import dellemuse.serverapp.editor.ObjectRestoreEvent;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.help.Help;
import dellemuse.serverapp.help.HelpButtonToolbarItem;
import dellemuse.serverapp.page.MultiLanguageObjectPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.site.SiteNavDropDownMenuToolbarItem;
import dellemuse.serverapp.page.site.SitePage;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.ArtWorkRecord;
import dellemuse.serverapp.serverdb.model.record.GuideContentRecord;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
import dellemuse.serverapp.serverdb.model.security.RoleInstitution;
import dellemuse.serverapp.serverdb.model.security.RoleSite;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.ResourceThumbnailService;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.event.SimpleAjaxWicketEvent;
import io.wktui.event.SimpleWicketEvent;
import io.wktui.event.UIEvent;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.breadcrumb.Navigator;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import wktui.base.INamedTab;
import wktui.base.NamedTab;

/**
 * site foto Info - exhibitions
 */
@AuthorizeInstantiation({"ROLE_USER"})
@MountPath("/artwork/${id}")
public class ArtWorkPage extends MultiLanguageObjectPage<ArtWork, ArtWorkRecord> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ArtWorkPage.class.getName());
	
	private JumboPageHeaderPanel<ArtWork> ph;
	
	private IModel<Site> siteModel;

	private ArtWorkMainPanel editor;
	private List<ToolbarItem> list;

	
	
	public ArtWorkPage() {
		super();
	}

	public ArtWorkPage(PageParameters parameters) {
		super(parameters);
	}

	public ArtWorkPage(IModel<ArtWork> model) {
		this(model, null);
	}

	public ArtWorkPage(IModel<ArtWork> model, List<IModel<ArtWork>> list) {
		super(model, list);
	}

	
	@Override
	public boolean canEdit() {
		return isRoot() || isGeneralAdmin();
	}
	

	public String getHelpKey() {
		return Help.ARTWORK_INFO;
	}

	
	
	@Override
	public void onInitialize() {
		super.onInitialize();
	}

	public IModel<Site> getSiteModel() {
		return siteModel;
	}

	public void setSiteModel(IModel<Site> siteModel) {
		this.siteModel = siteModel;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (siteModel != null)
			siteModel.detach();

		if (editor!=null)
			editor.detach();

	}

	protected void onDelete(AjaxRequestTarget target) {
		getArtWorkDBService().markAsDeleted(getModel().getObject(), getSessionUser().get());
		fireScanAll(new ObjectMarkAsDeleteEvent(target));
	}

	protected void onRestore(AjaxRequestTarget target) {
		getArtWorkDBService().restore(getModel().getObject(), getSessionUser().get());
		fireScanAll(new ObjectRestoreEvent(target));
	}

	protected void onEdit(AjaxRequestTarget target) {
		this.editor.onEdit(target);
	}

	protected void onEditMeta(AjaxRequestTarget target) {
		getMetaEditor().onEdit(target);
	}

	protected boolean isSpecVisible() {
		return true;
	}

	protected List<Language> getSupportedLanguages() {
		return  getSiteModel().getObject().getLanguages();
	}

	
	@Override
	public boolean hasAccessRight(Optional<User> ouser) {
		
		if (ouser.isEmpty())
			return false;
		
		User user = ouser.get();  
		
		if (user.isRoot()) 
			return true;
		
		if (!user.isDependencies()) {
			user = getUserDBService().findWithDeps(user.getId()).get();
		}

		{
			Set<RoleGeneral> set = user.getRolesGeneral();
			if (set!=null) {
					boolean isAccess=set.stream().anyMatch((p -> p.getKey().equals(RoleGeneral.ADMIN) || p.getKey().equals(RoleGeneral.AUDIT) ));
					if (isAccess)
						return true;
			}
		}
		
		{
			final Long sid = getSiteModel().getObject().getId();
			Set<RoleSite> set = user.getRolesSite();
			if (set!=null) {
				boolean isAccess=set.stream().anyMatch((p -> p.getSite().getId().equals(sid) && (p.getKey().equals(RoleSite.ADMIN) || p.getKey().equals(RoleSite.EDITOR))));
				if (isAccess)
					return true;
			}
		}		
		
		{
			final Long iid = getSiteModel().getObject().getInstitution().getId();
			Set<RoleInstitution> set = user.getRolesInstitution();
			if (set!=null) {
				boolean isAccess=set.stream().anyMatch((p -> p.getInstitution().getId().equals(iid) && (p.getKey().equals(RoleInstitution.ADMIN) )));
				if (isAccess)
					return true;
			}
		}
		return false;
	}
	
	@Override
	protected Class<?> getTranslationClass() {
		return ArtWorkRecord.class;
	}
	@Override
	protected Optional<ArtWorkRecord> loadTranslationRecord(String lang) {
		return getArtWorkRecordDBService().findByArtWork(getModel().getObject(), lang);
	}

	@Override
	protected ArtWorkRecord createTranslationRecord(String lang) {
		return getArtWorkRecordDBService().create(getModel().getObject(), lang, getSessionUser().get());
	}

	@Override
	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<SimpleAjaxWicketEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(SimpleAjaxWicketEvent event) {

				logger.debug(event.toString());

				/**
				 * if (event.getName().equals(ServerAppConstant.action_site_edit)) {
				 * ArtWorkPage.this.onEdit(event.getTarget()); }
				 * 
				 * else if (event.getName().equals(ServerAppConstant.site_info)) {
				 * ArtWorkPage.this.togglePanel(ServerAppConstant.site_info, event.getTarget());
				 * }
				 * 
				 * if (event.getName().equals(ServerAppConstant.audit)) {
				 * ArtWorkPage.this.togglePanel(ServerAppConstant.audit, event.getTarget()); }
				 **/

				// edit --------------------------------------
				//
				//
				//
				if (event.getName().equals(ServerAppConstant.action_artwork_edit_info)) {
					ArtWorkPage.this.onEdit(event.getTarget());
				}

				else if (event.getName().equals(ServerAppConstant.action_object_edit_meta)) {
					ArtWorkPage.this.onEditMeta(event.getTarget());
				}

				else if (event.getName().equals(ServerAppConstant.action_object_edit_record)) {
					ArtWorkPage.this.onEditRecord(event.getTarget(), event.getMoreInfo());
				}

				// panels --------------------------------------
				//
				//
				//
				else if (event.getName().equals(ServerAppConstant.artwork_info)) {
					ArtWorkPage.this.togglePanel(ServerAppConstant.artwork_info, event.getTarget());
					// ArtExhibitionPage.this.getHeader().setPhotoVisible(true);
					// event.getTarget().add(ArtWorkPage.this.getHeader());
				} else if (event.getName().startsWith(ServerAppConstant.object_translation_record_info)) {
					ArtWorkPage.this.togglePanel(event.getName(), event.getTarget());
				}

				else if (event.getName().equals(ServerAppConstant.object_meta)) {
					ArtWorkPage.this.togglePanel(ServerAppConstant.object_meta, event.getTarget());
					// ArtExhibitionPage.this.getHeader().setPhotoVisible(true);
					// event.getTarget().add(ArtWorkPage.this.getHeader());
				}

				else if (event.getName().equals(ServerAppConstant.object_audit)) {
					ArtWorkPage.this.togglePanel(ServerAppConstant.object_audit, event.getTarget());
					// ArtExhibitionPage.this.getHeader().setPhotoVisible(true);
					// event.getTarget().add(ArtWorkPage.this.getHeader());
				}
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof MenuAjaxEvent)
					return true;
				return false;
			}
		});

		add(new io.wktui.event.WicketEventListener<SimpleWicketEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(SimpleWicketEvent event) {
				if (event.getName().equals(ServerAppConstant.site_action_home)) {
					setResponsePage(new SitePage(getSiteModel(), null));
				}
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof SimpleWicketEvent)
					return true;
				return false;
			}
		});

	}

	@Override
	protected Optional<ArtWork> getObject(Long id) {
		return getArtWork(id);
	}

	@Override
	protected IModel<String> getPageTitle() {
		return new Model<String>(getModel().getObject().getName());
	}

	
	
	@Override
	protected List<ToolbarItem> getToolbarItems() {

		if (list != null)
			return list;

		list = new ArrayList<ToolbarItem>();

		String name = getModel().getObject().getName();

		list.add(new ArtWorkNavDropDownMenuToolbarItem("item", getModel(), getLabel("artwork-menu-title", TextCleaner.truncate(name, 32)), Align.TOP_RIGHT));

		// site
		SiteNavDropDownMenuToolbarItem site = new SiteNavDropDownMenuToolbarItem("item", getSiteModel(), Align.TOP_RIGHT);
		site.add(new org.apache.wicket.AttributeModifier("class", "d-none d-xs-none d-sm-none d-md-block d-lg-block d-xl-block d-xxl-block text-md-center"));
		list.add(site);

		HelpButtonToolbarItem h = new HelpButtonToolbarItem("item",  Align.TOP_RIGHT);
		list.add(h);
	
		
		return list;

	}
	
	@Override
	protected List<INamedTab> getInternalPanels() {

		List<INamedTab> tabs = super.createInternalPanels();

		NamedTab tab_1 = new NamedTab(Model.of("editor"), ServerAppConstant.artwork_info) {

			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return ArtWorkPage.this.getEditor(panelId);
			}
		};
		tabs.add(tab_1);

		if (getStartTab() == null)
			setStartTab(ServerAppConstant.artwork_info);

		return tabs;
	}

	protected Panel getEditor(String id) {
		if (this.editor == null)
			editor = new ArtWorkMainPanel(id, getModel());
		return (editor);
	}

	
	
	@Override
	protected boolean isLanguage() {
		return false;
	}

	@Override
	protected Panel createHeaderPanel() {

		ph = new JumboPageHeaderPanel<ArtWork>("page-header", getModel(), getObjectTitle(getModel().getObject() ));

		ph.setImageLinkCss("jumbo-img jumbo-lg mb-2 mb-lg-0 border bg-body-tertiary");

		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(new HREFBCElement("/site/list", getLabel("sites")));
		bc.addElement(new HREFBCElement("/site/" + getSiteModel().getObject().getId().toString(), new Model<String>(getSiteModel().getObject().getDisplayname())));
		bc.addElement(new HREFBCElement("/site/artwork/" + getSiteModel().getObject().getId().toString(), getLabel("artworks")));

		bc.addElement(new BCElement(getObjectTitle(getModel().getObject() )));

		if (getModel().getObject().getArtists() != null)
			ph.setTagline(Model.of(getArtistStr(getModel().getObject())));

		if (getModel().getObject().getPhoto() != null) {
			ph.setPhotoModel(new ObjectModel<Resource>(getModel().getObject().getPhoto()));
		}
		else {
			ph.setIcon( ArtWork.getIcon());
			ph.setHeaderCss("mb-0 pb-5  pt-0 border-none");
		}
		

		if (getList() != null && getList().size() > 0) {
			Navigator<ArtWork> nav = new Navigator<ArtWork>("navigator", getCurrent(), getList()) {
				private static final long serialVersionUID = 1L;

				@Override
				public void navigate(int current) {
					setResponsePage(new ArtWorkPage(getList().get(current), getList()));
				}
			};
			bc.setNavigator(nav);
		}

		ph.setContext(getLabel("artwork"));

		ph.setBreadCrumb(bc);
	
		return ph;
	}

	@Override
	protected IRequestablePage getObjectPage(IModel<ArtWork> model, List<IModel<ArtWork>> list) {
		return new ArtWorkPage(model, list);
	}

	protected void setUpModel() {
		super.setUpModel();

		if (!getModel().getObject().isDependencies()) {
			Optional<ArtWork> o_i = getArtWorkDBService().findWithDeps(getModel().getObject().getId());
			setModel(new ObjectModel<ArtWork>(o_i.get()));
		}
 
		setSiteModel(new ObjectModel<Site>(getModel().getObject().getSite()));

		if (!getSiteModel().getObject().isDependencies()) {
			Optional<Site> o_i = getSiteDBService().findWithDeps(getSiteModel().getObject().getId());
			setSiteModel(new ObjectModel<Site>(o_i.get()));
		}
	}

	protected String getImageSrc(IModel<ArtWork> model) {

		try {
			if (getModel().getObject().getPhoto() == null)
				return null;
			ResourceThumbnailService ths = (ResourceThumbnailService) ServiceLocator.getInstance().getBean(ResourceThumbnailService.class);
			return ths.getPresignedThumbnailUrl(getModel().getObject().getPhoto(), ThumbnailSize.MEDIUM);

		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

}
