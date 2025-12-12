package dellemuse.serverapp.role;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

@MountPath("/role/${id}")
public class RolePage extends ObjectPage<Role> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(RolePage.class.getName());

	private RoleUsersPanel rolesPanel;
 
	 

	public RolePage() {
		super();
	}

	public RolePage(PageParameters parameters) {
		super(parameters);
	}

	public RolePage(IModel<Role> model) {
		super(model);
	}

	public RolePage(IModel<Role> model, List<IModel<Role>> list) {
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

				 
				if (event.getName().equals(ServerAppConstant.role_panel_info)) {
					RolePage.this.togglePanel(ServerAppConstant.role_panel_info, event.getTarget());
					
				} else if (event.getName().equals(ServerAppConstant.object_audit)) {
					RolePage.this.togglePanel(ServerAppConstant.object_audit, event.getTarget());
				}
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof MenuAjaxEvent)
					return true;
				return false;
			}
		});
	}
	
	protected Panel getRoleUsersPanel(String id) {
		if (rolesPanel == null)
			rolesPanel  = new RoleUsersPanel(id, getModel());
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
			bc.addElement(new HREFBCElement("/security/roles", getLabel("roles")));
			bc.addElement(new BCElement(new Model<String>(getModel().getObject().getName())));
			JumboPageHeaderPanel<Role> ph = new JumboPageHeaderPanel<Role>("page-header", getModel(), 
					Model.of(getModel().getObject().getRoleDisplayName() + " (" + getModel().getObject().getDisplayClass(getLocale()) + ") ")
					);
		
			ph.setHeaderCss("mb-0 pb-2 border-none");
			ph.setIcon(Role.getIcon());
			ph.setBreadCrumb(bc);

			if (getList() != null && getList().size() > 0) {
				Navigator<Role> nav = new Navigator<Role>("navigator", getCurrent(), getList()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void navigate(int current) {
						setResponsePage(new RolePage(getList().get(current), getList()));
					}
				};
				bc.setNavigator(nav);
			}
			ph.setContext(getLabel("role"));
			return (ph);

		} catch (Exception e) {
			logger.error(e);
			return new ErrorPanel("page-header", e);
		}
	}

	@Override
	protected IRequestablePage getObjectPage(IModel<Role> model, List<IModel<Role>> list) {
		return new RolePage(model, list);
	}
 

	protected Panel getAuditPanel(String id) {
		return new AuditPanel<Role>(id, getModel());
	}

	@Override
	protected List<ToolbarItem> getToolbarItems() {
	 return null;
	}

	@Override
	protected List<INamedTab> getInternalPanels() {

		List<INamedTab> tabs = super.createInternalPanels();

		NamedTab tab_1 = new NamedTab(Model.of("editor"), ServerAppConstant.role_panel_info) {
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getRoleUsersPanel(panelId);
			}
		};
		tabs.add(tab_1);
 
		
		NamedTab audit = new NamedTab(Model.of("audit"), ServerAppConstant.object_audit) {

			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getAuditPanel(panelId);
			}
		};
		tabs.add(audit);
		
		
		if (getStartTab() == null)
			setStartTab(ServerAppConstant.role_panel_info);

		return tabs;
	}

	@Override
	protected Optional<Role> getObject(Long id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	protected void onEdit(AjaxRequestTarget target) {
		// TODO Auto-generated method stub
		
	}

}
