package dellemuse.serverapp.help;

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
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.help.Help;
import dellemuse.serverapp.help.HelpButtonToolbarItem;
import dellemuse.serverapp.page.ObjectPage;
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.model.ObjectWithDepModel;
import dellemuse.serverapp.person.PersonPage;
import dellemuse.serverapp.person.ServerAppConstant;

import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;

import dellemuse.serverapp.serverdb.model.User;

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

@AuthorizeInstantiation({ "ROLE_USER" })
@MountPath("/help/${key}")
public class HelpManualPage extends ObjectPage<User> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(HelpManualPage.class.getName());

	private List<ToolbarItem> userMenu = null;

	private IModel<Person> personModel;
	private HelpManualPagePanel helpPanel;
	
	private String manualPageId;
	

	public HelpManualPage() {
		super();
	}

	public HelpManualPage(PageParameters parameters) {
		super(parameters);
		this.manualPageId = parameters.get("key").toOptionalString();
		getPageParameters().add("key", manualPageId);
	}

	public HelpManualPage(String pageId, IModel<User> model) {
		super(model);
		this.manualPageId=pageId;
		getPageParameters().add("key", pageId);
	}

	public String getHelpKey() {
		return Help.HELP_MANUAL;
	}

	public IModel<Person> getPersonModel() {
		return personModel;
	}

	public void setPersonModel(IModel<Person> personModel) {
		this.personModel = personModel;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (getPersonModel() != null)
			getPersonModel().detach();
	}

	@Override
	protected List<INamedTab> createInternalPanels() {

		List<INamedTab> list = super.createInternalPanels();

		NamedTab audit = new NamedTab(Model.of("audit"), ServerAppConstant.help) {

			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getHelpPanel(panelId);
			}
		};

		list.add(audit);
		return list;
	}

	protected WebMarkupContainer getHelpPanel(String panelId) {
		helpPanel = new HelpManualPagePanel(panelId, getManualPageId(), getModel());
		return helpPanel;
	}

	
	@Override
	public void onInitialize() {
		super.onInitialize();
	}

	
	@Override
	protected boolean calculateHasAccessRight(Optional<User> ouser) {
		return true;
	
	}
	
	protected boolean isMetaEditEnabled() {
		return false;
	}
	
	@Override
	protected void setUpModel() {
		
		/**super.setUpModel();

		if (!getModel().getObject().isDependencies()) {
			Optional<User> o_i = getUserDBService().findWithDeps(getModel().getObject().getId());
			setModel(new ObjectWithDepModel<User>(o_i.get()));
		}

		Optional<Person> o = getPersonDBService().getByUserWithDeps(getModel().getObject());
		if (o.isPresent())
			setPersonModel(new ObjectModel<Person>(o.get()));
**/
		
	}

	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<SimpleAjaxWicketEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(SimpleAjaxWicketEvent event) {
				logger.debug(event.toString());
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof MenuAjaxEvent)
					return true;
				return false;
			}
		});

		 
	}

	 
	@Override
	protected Optional<User> getObject(Long id) {
		return getUser(id);
	}

	 
 

	@Override
	protected IModel<String> getPageTitle() {
		return new Model<String>(getModel().getObject().getName());
	}

	@Override
	protected Panel createHeaderPanel() {

		try {
			BreadCrumb<Void> bc = createBreadCrumb();
			bc.addElement(new BCElement( getLabel("help")));
			JumboPageHeaderPanel<User> ph = new JumboPageHeaderPanel<User>("page-header", getModel(), getLabel(getManualPageId()));
			ph.setHeaderCss("mb-0 pb-2 border-none");
			ph.setBreadCrumb(bc);
 			ph.setContext(getLabel("help"));
			return (ph);

		} catch (Exception e) {
			logger.error(e);
			return new ErrorPanel("page-header", e);
		}
	}

	 

	@Override
	protected IRequestablePage getObjectPage(IModel<User> model, List<IModel<User>> list) {
		//return new HelpManualPage(model, "ss");
		return null;
	}
 
	public String getManualPageId() {
		return manualPageId;
	}

	public void setManualPageId(String pageId) {
		this.manualPageId = pageId;
	}

	protected Panel getAuditPanel(String id) {
		return new AuditPanel<User>(id, getModel());
	}

	 

	 

	@Override
	protected List<ToolbarItem> getToolbarItems() {

		if (userMenu != null)
			return (userMenu);

		userMenu = new ArrayList<ToolbarItem>();
		//UserNavDropDownMenuToolbarItem menu = new UserNavDropDownMenuToolbarItem("item", getModel(), getLabel("user"), Align.TOP_RIGHT);
		//userMenu.add(menu);

		//HelpButtonToolbarItem h = new HelpButtonToolbarItem("item", Align.TOP_RIGHT);
		//userMenu.add(h);

		return userMenu;
	}

	@Override
	protected List<INamedTab> getInternalPanels() {

		List<INamedTab> tabs = super.createInternalPanels();

	  

		NamedTab tab_3 = new NamedTab(Model.of("roles"), ServerAppConstant.help) {
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getHelpPanel(panelId);
			}
		};
		tabs.add(tab_3);
	 
		if (getStartTab() == null)
			setStartTab(ServerAppConstant.help);

		return tabs;
	}

	@Override
	protected Panel createSearchPanel() {
		 
		return null;
	}

}