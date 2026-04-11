package dellemuse.serverapp.page.site;

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
import org.apache.wicket.util.string.StringValue;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.editor.ObjectMetaEditor;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.help.Help;
import dellemuse.serverapp.help.HelpButtonToolbarItem;
import dellemuse.serverapp.page.MultiLanguageObjectPage;
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.user.UserPasswordEditor;
import dellemuse.serverapp.page.user.UserRolesPanel;
import dellemuse.serverapp.person.PersonEditor;
import dellemuse.serverapp.person.PersonPage;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.Artist;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.SiteRecord;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
import dellemuse.serverapp.serverdb.model.security.RoleSite;
import dellemuse.serverapp.serverdb.service.PersonDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.error.ErrorPanel;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.event.SimpleAjaxWicketEvent;
import io.wktui.event.SimpleWicketEvent;
import io.wktui.event.UIEvent;

import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;

import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import wktui.base.DummyBlockPanel;
import wktui.base.INamedTab;

import wktui.base.NamedTab;

@AuthorizeInstantiation({ "ROLE_USER" })
@MountPath("/site/user/${id}/${userid}")
public class SiteUserPage extends MultiLanguageObjectPage<Site, SiteRecord> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteUserPage.class.getName());

	private List<ToolbarItem> list;

	private IModel<User> userModel;
	private StringValue sv;

	private PersonEditor personEditor;
	private UserPasswordEditor pwdeditor;
	private SiteUserEditor editor;

	private UserRolesPanel reditor;

	private ObjectMetaEditor<User>  metaEditor;
	
	
	protected IModel<Site> getSiteModel() {return getModel();}

	
	public SiteUserPage(PageParameters parameters) {
		super(parameters);
		if (getPageParameters() != null)
			sv = getPageParameters().get("userid");
	}

	public SiteUserPage() {
		super();
	}

	public SiteUserPage(IModel<Site> model, IModel<User> userModel) {
		super(model);
		this.userModel = userModel;
		getPageParameters().add("userid", userModel.getObject().getId().toString());
		sv = StringValue.valueOf(userModel.getObject().getId().toString());
	}

	public String getHelpKey() {
		return Help.SITE_USER_INFO;
	}

	@Override
	protected boolean calculateHasAccessRight(Optional<User> ouser) {

		if (ouser.isEmpty())
			return false;

		if (isRoot())
			return true;

		if (isGeneralAdmin())
			return true;

		User user = ouser.get();

		if (!user.isDependencies()) {
			user = getUserDBService().findWithDeps(user.getId()).get();
		}

		{
			Set<RoleGeneral> set = user.getRolesGeneral();
			if (set != null) {
				boolean isAccess = set.stream().anyMatch((p -> p.getKey().equals(RoleGeneral.ADMIN) || p.getKey().equals(RoleGeneral.AUDIT)));
				if (isAccess)
					return true;
			}
		}

		{
			final Long sid = getModel().getObject().getId();
			Set<RoleSite> set = user.getRolesSite();
			if (set != null) {
				boolean isAccess = set.stream().anyMatch((p -> p.getSite().getId().equals(sid) && (p.getKey().equals(RoleSite.ADMIN) || p.getKey().equals(RoleSite.EDITOR))));
				if (isAccess)
					return true;
			}
		}

		return false;
	}

	@Override
	protected Panel createHeaderPanel() {

		try {
			BreadCrumb<Void> bc = createBreadCrumb();
			bc.addElement(new HREFBCElement("/site/list", getLabel("sites")));
			bc.addElement(new HREFBCElement("/site/" + getModel().getObject().getId().toString(), getObjectTitle(getModel().getObject())));
			bc.addElement(new HREFBCElement("/site/users/" + getModel().getObject().getId().toString(), getLabel("users")));

			bc.addElement(new BCElement(getObjectTitle(this.userModel.getObject())));
			JumboPageHeaderPanel<Site> ph = new JumboPageHeaderPanel<Site>("page-header", getModel(), getObjectTitle(getModel().getObject()));
			ph.setBreadCrumb(bc);

			ph.setContext(getLabel("site"));

			if (getModel().getObject().getSubtitle() != null)
				ph.setTagline(getObjectSubtitle(getModel().getObject()));

			if (getModel().getObject().getPhoto() != null)
				ph.setPhotoModel(new ObjectModel<Resource>(getResourceDBService().findById(getModel().getObject().getPhoto().getId()).get()));

			return ph;

		} catch (Exception e) {
			logger.error(e);
			return new ErrorPanel("page-header", e);
		}
	}

	@Override
	protected IRequestablePage getObjectPage(IModel<Site> iModel, List<IModel<Site>> list2) {
		return new SitePage(iModel, list2);
	}

	protected Panel getSiteUserEditor(String id) {
		if (this.editor == null)
			this.editor = new SiteUserEditor(id, getModel(), this.userModel);
		return this.editor;
	}

	
	protected ObjectMetaEditor<User> getSiteUserStateEditor() {
		return this.metaEditor;
	}

	protected WebMarkupContainer getSiteUserStateEditor(String pid) {
		if (this. metaEditor == null) {
			this. metaEditor = new ObjectMetaEditor<User>(pid, getUserModel());
		}
		return this.metaEditor;
	}
	
	protected PersonEditor getPersonEditor() {
		return this.personEditor;
	}

	protected Panel getPersonEditor(String id) {
		if (this.personEditor == null) {
			Optional<Person> o = getPersonDBService().getByUser(getUserModel().getObject());
			this.personEditor = new PersonEditor(id, new ObjectModel<Person>(o.get()));
		}
		return this.personEditor;
	}

	protected PersonDBService getPersonDBService() {
		return (PersonDBService) ServiceLocator.getInstance().getBean(PersonDBService.class);
	}

	protected UserPasswordEditor getUserPasswordEditor() {
		return this.pwdeditor;
	}

	protected Panel getUserPasswordEditor(String id) {
		if (this.pwdeditor == null)
			this.pwdeditor = new UserPasswordEditor(id, getUserModel());
		return this.pwdeditor;
	}

	protected UserRolesPanel getUserRolesdEditor(String id) {
		if (this.reditor == null)
			this.reditor = new UserRolesPanel(id, getUserModel(), getModel(), true);
		return this.reditor;
	}

	protected List<ToolbarItem> getToolbarItems() {

		if (list != null)
			return list;

		list = new ArrayList<ToolbarItem>();

	 
		SiteUserNavDropDownMenuToolbarItem menu = new SiteUserNavDropDownMenuToolbarItem("item", userModel, getLabel("user"), Align.TOP_RIGHT);
		list.add(menu);

		// site
		SiteNavDropDownMenuToolbarItem site = new SiteNavDropDownMenuToolbarItem("item", getModel(), Align.TOP_RIGHT);
		site.add(new org.apache.wicket.AttributeModifier("class", "d-none d-xs-none d-sm-none d-md-block d-lg-block d-xl-block d-xxl-block text-md-center"));
		list.add(site);

		HelpButtonToolbarItem h = new HelpButtonToolbarItem("item", Align.TOP_RIGHT);
		list.add(h);

		return list;
	}

	@Override
	protected List<INamedTab> getInternalPanels() {

		List<INamedTab> tabs = super.createInternalPanels();

		NamedTab tab_1 = new NamedTab(Model.of("editor"), ServerAppConstant.site_user_editor) {

			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getSiteUserEditor(panelId);
			}
		};
		tabs.add(tab_1);

		NamedTab tab_2 = new NamedTab(Model.of("pwd"), ServerAppConstant.user_panel_password) {

			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getUserPasswordEditor(panelId);
			}
		};
		tabs.add(tab_2);

		NamedTab tab_3 = new NamedTab(Model.of("roles"), ServerAppConstant.user_panel_roles) {

			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getUserRolesdEditor(panelId);
			}
		};
		tabs.add(tab_3);

		NamedTab tab_4 = new NamedTab(Model.of("person"), ServerAppConstant.person_info) {

			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getPersonEditor(panelId);
			}
		};
		tabs.add(tab_4);

		
		NamedTab tab_5 = new NamedTab(Model.of("state"), ServerAppConstant.site_user_state_info) {

			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getSiteUserStateEditor(panelId);
			}
		};
		
		tabs.add(tab_5);
		
		
		if (getStartTab() == null)
			super.setStartTab(ServerAppConstant.site_user_editor);

		return tabs;
	}


	protected void onEdit(AjaxRequestTarget target) {
		// this.editor.onEdit(target);

	}

	@Override
	protected Class<?> getTranslationClass() {
		return SiteRecord.class;
	}

	@Override
	protected void setUpModel() {
		super.setUpModel();

		if (this.userModel == null) {
			if (this.sv != null) {
				Optional<User> o = getUserDBService().findById(Long.valueOf(this.sv.toLong()));
				if (o.isPresent()) {
					this.userModel = new ObjectModel<User>(o.get());
				}
			}
		}

		if (getModel() == null)
			throw new IllegalStateException("site is null");

		if (this.userModel == null)
			throw new IllegalStateException("user is null");

		if (!this.userModel.getObject().isDependencies()) {
			Optional<User> o_i = getUserDBService().findWithDeps(userModel.getObject().getId());
			this.userModel = new ObjectModel<User>(o_i.get());
		}

	}

	protected List<Language> getSupportedLanguages() {
		return getModel().getObject().getLanguages();
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (this.userModel != null)
			this.userModel.detach();
	}

	@Override
	protected Optional<SiteRecord> loadTranslationRecord(String lang) {
		return getSiteRecordDBService().findBySite(getModel().getObject(), lang);
	}

	@Override
	protected SiteRecord createTranslationRecord(String lang) {
		return getSiteRecordDBService().create(getModel().getObject(), lang, getSessionUser().get());
	}

	@Override
	protected boolean isOpensVisible() {
		return true;
	}

	@Override
	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<SimpleAjaxWicketEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(SimpleAjaxWicketEvent event) {

				logger.debug(event.toString());

				if (event.getName().equals(ServerAppConstant.site_user_state_info)) {
					SiteUserPage.this.togglePanel(ServerAppConstant.site_user_state_info, event.getTarget());
				}
				 
				if (	(event.getName().equals(ServerAppConstant.site_user_state_action_edit))  || 
						(event.getName().equals(ServerAppConstant.action_object_edit_meta ))) {
					SiteUserPage.this.getSiteUserStateEditor().onEdit(event.getTarget());
				}
				else if (event.getName().equals(ServerAppConstant.person_info)) {
					SiteUserPage.this.togglePanel(ServerAppConstant.person_info, event.getTarget());
				}
				
				
				if (event.getName().equals(ServerAppConstant.site_portal_action_edit)) {
					SiteUserPage.this.onEdit(event.getTarget());
				}

				else if (event.getName().equals(ServerAppConstant.user_panel_password)) {
					SiteUserPage.this.togglePanel(ServerAppConstant.user_panel_password, event.getTarget());
				}

				else if (event.getName().equals(ServerAppConstant.user_action_edit_pwd)) {
					SiteUserPage.this.getUserPasswordEditor().onEdit(event.getTarget());
				}

				else if (event.getName().equals(ServerAppConstant.site_user_editor)) {
					SiteUserPage.this.togglePanel(ServerAppConstant.site_user_editor, event.getTarget());
				}

				else if (event.getName().equals(ServerAppConstant.user_panel_roles)) {
					SiteUserPage.this.togglePanel(ServerAppConstant.user_panel_roles, event.getTarget());
				}

				else if (event.getName().equals(ServerAppConstant.site_page_info)) {
					SiteUserPage.this.togglePanel(ServerAppConstant.site_page_info, event.getTarget());

				} else if (event.getName().equals(ServerAppConstant.object_meta)) {
					SiteUserPage.this.togglePanel(ServerAppConstant.site_user_state_info, event.getTarget());
				}
				

			

				else if (event.getName().equals(ServerAppConstant.user_action_edit_info)) {
					SiteUserPage.this.getSiteUserEditor().onEdit(event.getTarget());
				}

				else if (event.getName().equals(ServerAppConstant.action_person_edit_info)) {
					SiteUserPage.this.getPersonEditor().onEdit(event.getTarget());
				}

				else if (event.getName().equals(ServerAppConstant.action_object_edit_record)) {
					SiteUserPage.this.onEditRecord(event.getTarget(), event.getMoreInfo());
				}

				else if (event.getName().startsWith(ServerAppConstant.object_translation_record_info)) {
					SiteUserPage.this.togglePanel(event.getName(), event.getTarget());
				}

				else if (event.getName().startsWith(ServerAppConstant.object_audit)) {
					if (event.getMoreInfo() != null) {
						SiteUserPage.this.togglePanel(ServerAppConstant.object_audit + "-" + event.getMoreInfo(), event.getTarget());

					} else {
						SiteUserPage.this.togglePanel(ServerAppConstant.object_audit, event.getTarget());

					}
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
				if (event.getName().equals(ServerAppConstant.user_panel_person)) {
					Optional<Person> p = getPersonDBService().getByUser(SiteUserPage.this.getUserModel().getObject());
					if (p.isPresent()) {
						setResponsePage(new PersonPage(new ObjectModel<Person>(p.get())));
					} else
						setResponsePage(new ErrorPage(Model.of("Person not found for user -> " + SiteUserPage.this.getUserModel().getObject().getDisplayname())));
				}
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof SimpleWicketEvent)
					return true;
				return false;
			}
		});

		add(new io.wktui.event.WicketEventListener<SimpleWicketEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(SimpleWicketEvent event) {
			
				logger.debug(event.toString());
				
				if (event.getName().equals(ServerAppConstant.site_action_home)) {
					setResponsePage(new SitePage(getModel(), getList()));
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

	protected SiteUserEditor getSiteUserEditor() {
		return this.editor;
	}

	@Override
	protected Optional<Site> getObject(Long id) {
		return getSite(id);
	}

	@Override
	protected IModel<String> getPageTitle() {
		return getLabel("user");
	}

	public IModel<User> getUserModel() {
		return userModel;
	}

	public void setUserModel(IModel<User> userModel) {
		this.userModel = userModel;
	}

}
