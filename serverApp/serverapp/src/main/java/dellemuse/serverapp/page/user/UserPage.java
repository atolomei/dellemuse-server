package dellemuse.serverapp.page.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.audit.panel.AuditPanel;
import dellemuse.serverapp.global.JumboPageHeaderPanel;

import dellemuse.serverapp.page.ObjectPage;
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.person.PersonPage;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.security.Role;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
import dellemuse.serverapp.serverdb.model.security.RoleSite;
import io.wktui.error.ErrorPanel;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.event.SimpleAjaxWicketEvent;
import io.wktui.event.SimpleWicketEvent;
import io.wktui.event.UIEvent;

import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.breadcrumb.Navigator;

import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;

import wktui.base.INamedTab;
import wktui.base.NamedTab;

@MountPath("/user/${id}")
public class UserPage extends ObjectPage<User> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(UserPage.class.getName());

	private UserRolesPanel rolesPanel;
	private UserPasswordEditor passwordEditor;
	private UserEditor editor;
	private List<ToolbarItem> userMenu = null;

	
	protected boolean isMetaEditEnabled() {
		if (getModel().getObject().isRoot())
			return false;
		return true;
	}
	
	@Override
	public boolean hasAccessRight(Optional<User> ouser) {

		if (ouser.isEmpty())
			return false;

		if (ouser.get().getId().equals(getModel().getObject().getId()))
			return true;
		
		User user = ouser.get();  if (user.isRoot()) return true;
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
	public UserPage() {
		super();
	}

	public UserPage(PageParameters parameters) {
		super(parameters);
	}

	public UserPage(IModel<User> model) {
		super(model);
	}

	public UserPage(IModel<User> model, List<IModel<User>> list) {
		super(model, list);
	}

	@Override
	protected List<INamedTab> createInternalPanels() {

		List<INamedTab> list = super.createInternalPanels();

		NamedTab audit = new NamedTab(Model.of("audit"), ServerAppConstant.object_audit) {

			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getAuditPanel(panelId);
			}
		};

		list.add(audit);
		return list;
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
	}

	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<SimpleAjaxWicketEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(SimpleAjaxWicketEvent event) {

				logger.debug(event.toString());

				if (event.getName().equals(ServerAppConstant.user_action_edit_info)) { 
					UserPage.this.onEdit(event.getTarget());
					
				} else if (event.getName().equals(ServerAppConstant.user_panel_info)) {
					UserPage.this.togglePanel(ServerAppConstant.user_panel_info, event.getTarget());
				} else if (event.getName().equals(ServerAppConstant.user_panel_password)) {
					UserPage.this.togglePanel(ServerAppConstant.user_panel_password, event.getTarget());
				} else if (event.getName().equals(ServerAppConstant.object_meta)) {
					UserPage.this.togglePanel(ServerAppConstant.object_meta, event.getTarget());
				} else if (event.getName().equals(ServerAppConstant.object_audit)) {
					UserPage.this.togglePanel(ServerAppConstant.object_audit, event.getTarget());
				} else if (event.getName().equals(ServerAppConstant.user_panel_roles)) {
					UserPage.this.togglePanel(ServerAppConstant.user_panel_roles, event.getTarget());
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
					Optional<Person> p = getPersonDBService().getByUser(UserPage.this.getModel().getObject());
					if (p.isPresent()) {
						setResponsePage(new PersonPage(new ObjectModel<Person>(p.get())));
					} else
						setResponsePage(new ErrorPage(Model.of("not found for user -> " + UserPage.this.getModel().getObject().getDisplayname())));
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
	protected Optional<User> getObject(Long id) {
		return getUser(id);
	}

	protected Panel getEditor(String id) {
		if (editor == null)
			editor = new UserEditor(id, getModel());
		return (editor);
	}

	protected Panel getPasswordEditor(String id) {
		if (passwordEditor == null)
			passwordEditor = new UserPasswordEditor(id, getModel());
		return (passwordEditor);
	}
	
	protected Panel getUserRolesPanel(String id) {
		if (rolesPanel == null)
			rolesPanel = new UserRolesPanel(id, getModel());
		return (rolesPanel);
	}
	
	@Override
	protected IModel<String> getPageTitle() {
		return new Model<String>(getModel().getObject().getName());
	}

	@Override
	protected Panel createHeaderPanel() {

		try {
			BreadCrumb<Void> bc = createBreadCrumb();
			bc.addElement(new HREFBCElement("/user/list", getLabel("users")));
			bc.addElement(new BCElement(new Model<String>(getModel().getObject().getUsername())));
			JumboPageHeaderPanel<User> ph = new JumboPageHeaderPanel<User>("page-header", getModel(), new Model<String>(getModel().getObject().getDisplayname()));
		
			ph.setHeaderCss("mb-0 pb-2 border-none");
			ph.setIcon(User.getIcon());
			
			ph.setBreadCrumb(bc);

			if (getList() != null && getList().size() > 0) {
				Navigator<User> nav = new Navigator<User>("navigator", getCurrent(), getList()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void navigate(int current) {
						setResponsePage(new UserPage(getList().get(current), getList()));
					}
				};
				bc.setNavigator(nav);
			}
			ph.setContext(getLabel("user"));
			return (ph);

		} catch (Exception e) {
			logger.error(e);
			return new ErrorPanel("page-header", e);
		}
	}

	@Override
	protected IRequestablePage getObjectPage(IModel<User> model, List<IModel<User>> list) {
		return new UserPage(model, list);
	}

	@Override
	protected void onEdit(AjaxRequestTarget target) {
		this.editor.onEdit(target);
	}

	protected Panel getAuditPanel(String id) {
		return new AuditPanel<User>(id, getModel());
	}
	
	

	@Override
	protected List<ToolbarItem> getToolbarItems() {

		if (userMenu != null)
			return (userMenu);

		userMenu = new ArrayList<ToolbarItem>();
		UserNavDropDownMenuToolbarItem menu = new UserNavDropDownMenuToolbarItem("item", getModel(), getLabel("user"), Align.TOP_RIGHT);
		userMenu.add(menu);

		return userMenu;
	}

	@Override
	protected List<INamedTab> getInternalPanels() {

		List<INamedTab> tabs = super.createInternalPanels();

		NamedTab tab_1 = new NamedTab(Model.of("editor"), ServerAppConstant.user_panel_info) {
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getEditor(panelId);
			}
		};
		tabs.add(tab_1);

		NamedTab tab_2 = new NamedTab(Model.of("password"), ServerAppConstant.user_panel_password) {
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getPasswordEditor(panelId);
			}
		};
		tabs.add(tab_2);

		NamedTab tab_3 = new NamedTab(Model.of("roles"), ServerAppConstant.user_panel_roles) {
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getUserRolesPanel(panelId);
			}
		};
		tabs.add(tab_3);
		
		NamedTab audit = new NamedTab(Model.of("audit"), ServerAppConstant.object_audit) {

			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getAuditPanel(panelId);
			}
		};
		tabs.add(audit);
		
		
		if (getStartTab() == null)
			setStartTab(ServerAppConstant.user_panel_info);

		return tabs;
	}

}
