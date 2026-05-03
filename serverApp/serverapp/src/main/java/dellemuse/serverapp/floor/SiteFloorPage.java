package dellemuse.serverapp.floor;

import java.util.ArrayList;
import java.util.List;
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
import dellemuse.serverapp.audit.panel.AuditPanel;
import dellemuse.serverapp.editor.ObjectMarkAsDeleteEvent;
import dellemuse.serverapp.editor.ObjectRestoreEvent;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.help.HelpButtonToolbarItem;
import dellemuse.serverapp.page.MultiLanguageObjectPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.model.ObjectWithDepModel;
import dellemuse.serverapp.page.site.SiteNavDropDownMenuToolbarItem;
import dellemuse.serverapp.page.site.SitePage;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.Floor;
import dellemuse.serverapp.serverdb.model.FloorRecord;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
import dellemuse.serverapp.serverdb.model.security.RoleInstitution;
import dellemuse.serverapp.serverdb.model.security.RoleSite;
import dellemuse.serverapp.serverdb.service.record.FloorRecordDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.error.ErrorPanel;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.event.SimpleAjaxWicketEvent;
import io.wktui.event.SimpleWicketEvent;
import io.wktui.event.UIEvent;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import wktui.base.INamedTab;
import wktui.base.NamedTab;

@AuthorizeInstantiation({"ROLE_USER"})
@MountPath("/floor/${id}")
public class SiteFloorPage extends MultiLanguageObjectPage<Floor, FloorRecord> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteFloorPage.class.getName());

	private IModel<Site> siteModel;
	private FloorEditor editor;
	private FloorRoomsPanel roomsPanel;
	private List<ToolbarItem> toolbarList;

	// ------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------

	public SiteFloorPage() {
		super();
	}

	public SiteFloorPage(PageParameters parameters) {
		super(parameters);
	}

	public SiteFloorPage(IModel<Floor> model) {
		this(model, null);
	}

	public SiteFloorPage(IModel<Floor> model, List<IModel<Floor>> list) {
		super(model, list);
	}

	// ------------------------------------------------------------------
	// Permissions
	// ------------------------------------------------------------------

	public boolean canRead(Floor o) {
		if (getSessionUser().isEmpty()) 
			return false;
		
		if (isRoot())           
			return true;
		
		if (isGeneralAdmin())   
			return true;
		
		if (siteModel != null && isSiteAdminOrEditor(getSiteModel().getObject())) 
			return true;
		
		return false;
	}

	public boolean canWrite(Floor o) { return canRead(o); }
	public boolean canDelete(Floor o) { return canRead(o); }

	private FloorRecordDBService getFloorRecordDBService() {
		return (FloorRecordDBService) ServiceLocator.getInstance().getBean(FloorRecordDBService.class);
	}

	@Override
	protected boolean calculateHasAccessRight(Optional<User> ouser) {
		if (ouser.isEmpty()) return false;
		User user = ouser.get();
		if (user.isRoot()) return true;
		if (!user.isDependencies())
			user = getUserDBService().findWithDeps(user.getId()).get();

		Set<RoleGeneral> genRoles = user.getRolesGeneral();
		if (genRoles != null) {
			if (genRoles.stream().anyMatch(p -> p.getKey().equals(RoleGeneral.ADMIN) || p.getKey().equals(RoleGeneral.AUDIT)))
				return true;
		}

		if (siteModel != null) {
			final Long sid = getSiteModel().getObject().getId();
			Set<RoleSite> siteRoles = user.getRolesSite();
			if (siteRoles != null) {
				if (siteRoles.stream().anyMatch(p -> p.getSite().getId().equals(sid) && (p.getKey().equals(RoleSite.ADMIN) || p.getKey().equals(RoleSite.EDITOR))))
					return true;
			}

			final Long iid = getSiteModel().getObject().getInstitution().getId();
			Set<RoleInstitution> instRoles = user.getRolesInstitution();
			if (instRoles != null) {
				if (instRoles.stream().anyMatch(p -> p.getInstitution().getId().equals(iid) && p.getKey().equals(RoleInstitution.ADMIN)))
					return true;
			}
		}
		return false;
	}

	// ------------------------------------------------------------------
	// Model setup
	// ------------------------------------------------------------------

	@Override
	protected void setUpModel() {
		super.setUpModel();

		if (!getModel().getObject().isDependencies()) {
			Optional<Floor> o = getFloorDBService().findWithDeps(getModel().getObject().getId());
			setModel(new ObjectWithDepModel<Floor>(o.get()));
		}

		if (getModel().getObject().getSite() != null) {
			setSiteModel(new ObjectModel<Site>(getModel().getObject().getSite()));
			if (!getSiteModel().getObject().isDependencies()) {
				Optional<Site> o = getSiteDBService().findWithDeps(getSiteModel().getObject().getId());
				setSiteModel(new ObjectWithDepModel<Site>(o.get()));
			}
		}
	}

	@Override
	protected Optional<Floor> getObject(Long id) {
		return getFloorDBService().findById(id);
	}

	// ------------------------------------------------------------------
	// Translation record
	// ------------------------------------------------------------------

	@Override
	protected Class<?> getTranslationClass() {
		return FloorRecord.class;
	}

	@Override
	protected Optional<FloorRecord> loadTranslationRecord(String lang) {
		return getFloorRecordDBService().findByFloor(getModel().getObject(), lang);
	}

	@Override
	protected FloorRecord createTranslationRecord(String lang) {
		return getFloorRecordDBService().create(getModel().getObject(), lang, getSessionUser().get());
	}

	@Override
	protected List<Language> getSupportedLanguages() {
		return siteModel != null ? getSiteModel().getObject().getLanguages() : new ArrayList<>();
	}

	/** Override to use FloorRecordEditor instead of the generic ObjectRecordEditor */
	@Override
	protected Panel getTranslateRecordEditor(String id, String lang) {
		if (getRecordEditors().containsKey(lang))
			return getRecordEditors().get(lang);

		IModel<FloorRecord> recordModel = getTranslationRecordModel(lang);
		FloorRecordEditor e = new FloorRecordEditor(id, getModel(), recordModel);

		getRecordEditors().put(lang, e);
		return e;
	}

	// ------------------------------------------------------------------
	// Actions / events
	// ------------------------------------------------------------------

	protected void onEdit(AjaxRequestTarget target) {
		editor.onEdit(target);
	}

	protected void onDelete(AjaxRequestTarget target) {
		getFloorDBService().markAsDeleted(getModel().getObject(), getSessionUser().get());
		fireScanAll(new ObjectMarkAsDeleteEvent(target));
	}

	protected void onRestore(AjaxRequestTarget target) {
		getFloorDBService().restore(getModel().getObject(), getSessionUser().get());
		fireScanAll(new ObjectRestoreEvent(target));
	}

	@Override
	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<SimpleAjaxWicketEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(SimpleAjaxWicketEvent event) {

				
				// fire(new MenuAjaxEvent(ServerAppConstant.object_meta, target));
				
				if (event.getName().equals(ServerAppConstant.action_floor_info_edit)) {
					
					SiteFloorPage.this.onEdit(event.getTarget());
				}
				else if (event.getName().equals(ServerAppConstant.object_meta)) {
					SiteFloorPage.this.togglePanel(ServerAppConstant.object_meta, event.getTarget());
					
				} else if (event.getName().equals(ServerAppConstant.action_object_edit_record)) {
				
					SiteFloorPage.this.onEditRecord(event.getTarget(), event.getMoreInfo());
				
				} else if (event.getName().equals(ServerAppConstant.floor_info)) {
				
					SiteFloorPage.this.togglePanel(ServerAppConstant.floor_info, event.getTarget());
					event.getTarget().add(SiteFloorPage.this.getHeaderPanel());
				
				} else if (event.getName().equals(ServerAppConstant.floor_rooms)) {
					
					SiteFloorPage.this.togglePanel(ServerAppConstant.floor_rooms, event.getTarget());
					event.getTarget().add(SiteFloorPage.this.getHeaderPanel());
				
				} else if (event.getName().startsWith(ServerAppConstant.object_translation_record_info)) {
					SiteFloorPage.this.togglePanel(event.getName(), event.getTarget());
				
				} else if (event.getName().startsWith(ServerAppConstant.object_audit)) {
				
					if (event.getMoreInfo() != null)
						SiteFloorPage.this.togglePanel(ServerAppConstant.object_audit + "-" + event.getMoreInfo(), event.getTarget());
					else
						SiteFloorPage.this.togglePanel(ServerAppConstant.object_audit, event.getTarget());
				}
			}

			@Override
			public boolean handle(UIEvent event) {
				return event instanceof MenuAjaxEvent;
			}
		});

		add(new io.wktui.event.WicketEventListener<SimpleWicketEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(SimpleWicketEvent event) {
				if (event.getName().equals(ServerAppConstant.site_action_home) && siteModel != null) {
					setResponsePage(new SitePage(getSiteModel(), null));
				}
			}

			@Override
			public boolean handle(UIEvent event) {
				return event instanceof SimpleWicketEvent;
			}
		});
	}

	// ------------------------------------------------------------------
	// Toolbar
	// ------------------------------------------------------------------

	@Override
	protected List<ToolbarItem> getToolbarItems() {
		if (toolbarList != null)
			return toolbarList;

		toolbarList = new ArrayList<>();

		String name = TextCleaner.truncate(getObjectTitle(getModel().getObject()).getObject(), 24);
		toolbarList.add(new FloorNavDropDownMenuToolbarItem("item", getModel(), getLabel("floor", name), Align.TOP_RIGHT));

		if (siteModel != null) {
			SiteNavDropDownMenuToolbarItem site = new SiteNavDropDownMenuToolbarItem("item", getSiteModel(), Align.TOP_RIGHT);
			site.add(new org.apache.wicket.AttributeModifier("class",
				"d-none d-xs-none d-sm-none d-md-block d-lg-block d-xl-block d-xxl-block text-md-center"));
			toolbarList.add(site);
		}

		toolbarList.add(new HelpButtonToolbarItem("item", Align.TOP_RIGHT));

		return toolbarList;
	}

	// ------------------------------------------------------------------
	// Internal panels (tabs)
	// ------------------------------------------------------------------

	@Override
	protected List<INamedTab> getInternalPanels() {
		List<INamedTab> tabs = super.createInternalPanels();

		tabs.add(0, new NamedTab(Model.of("editor"), ServerAppConstant.floor_info) {
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getFloorEditor(panelId);
			}
		});

		tabs.add(1, new NamedTab(Model.of("rooms"), ServerAppConstant.floor_rooms) {
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getFloorRoomsPanel(panelId);
			}
		});

		if (getStartTab() == null)
			setStartTab(ServerAppConstant.floor_info);

		return tabs;
	}

	protected FloorEditor getFloorEditor(String id) {
		if (this.editor == null)
			this.editor = new FloorEditor(id, getModel());
		return this.editor;
	}

	protected FloorRoomsPanel getFloorRoomsPanel(String id) {
		if (this.roomsPanel == null)
			this.roomsPanel = new FloorRoomsPanel(id, getModel());
		return this.roomsPanel;
	}

	protected Panel getEditor(String id) {
		return getFloorEditor(id);
	}

	// ------------------------------------------------------------------
	// Page header
	// ------------------------------------------------------------------

	@Override
	protected IModel<String> getPageTitle() {
		return new Model<String>(getModel().getObject().getName());
	}

	@Override
	protected Panel createHeaderPanel() {
		try {
			BreadCrumb<Void> bc = createBreadCrumb();
			bc.addElement(new HREFBCElement("/site/list", getLabel("sites")));
			if (siteModel != null) {
				bc.addElement(new HREFBCElement("/site/" + getSiteModel().getObject().getId(), getObjectTitle(getSiteModel().getObject())));
				bc.addElement(new HREFBCElement("/site/floors/" + getSiteModel().getObject().getId(), getLabel("floors")));
			}
			bc.addElement(new BCElement(getObjectTitle(getModel().getObject())));

			JumboPageHeaderPanel<Floor> header = new JumboPageHeaderPanel<Floor>("page-header", getModel(), getObjectTitle(getModel().getObject()));
			header.setContext(getLabel("floor"));
			header.setBreadCrumb(bc);

			if (getModel().getObject().getPhoto() != null)
				header.setPhotoModel(new ObjectModel<Resource>(getModel().getObject().getPhoto()));
			else
				header.setIcon(Floor.getIcon());
			return header;

		} catch (Exception e) {
			logger.error(e);
			return new ErrorPanel("page-header", e);
		}
	}

	// ------------------------------------------------------------------
	// Navigation
	// ------------------------------------------------------------------

	@Override
	protected IRequestablePage getObjectPage(IModel<Floor> model, List<IModel<Floor>> list) {
		return new SiteFloorPage(model, list);
	}

	// ------------------------------------------------------------------
	// Site model
	// ------------------------------------------------------------------

	@Override
	public IModel<Site> getSiteModel() {
		return siteModel;
	}

	public void setSiteModel(IModel<Site> siteModel) {
		this.siteModel = siteModel;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (siteModel != null) siteModel.detach();
	}

	// ------------------------------------------------------------------
	// Visibility flags (Floor record has no opens/intro/spec/audio)
	// ------------------------------------------------------------------

	@Override
	protected boolean isOpensVisible()  { return false; }
	@Override
	protected boolean isIntroVisible()  { return false; }
	@Override
	protected boolean isSpecVisible()   { return false; }
	@Override
	protected boolean isAudioVisible()  { return false; }
	@Override
	protected boolean isInfoVisible()   { return true; }
	@Override
	protected boolean isLanguage()      { return false; }
}
