package dellemuse.serverapp.page.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;

import dellemuse.serverapp.page.DelleMuseObjectListItemPanel;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.MultipleSelectorPanel;

import dellemuse.serverapp.page.library.ObjectStateEnumSelector;
import dellemuse.serverapp.page.library.ObjectStateListSelector;
import dellemuse.serverapp.page.library.ObjectStateSelectEvent;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;

import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;

import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.security.Role;

import io.wktui.event.UIEvent;
import io.wktui.form.FormState;

import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.toolbar.Toolbar;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;
import wktui.base.InvisiblePanel;

public class UserExpandedPanel extends DBModelPanel<User> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(UserExpandedPanel.class.getName());

	private List<IModel<Role>> userRoles;
	 

	private FormState state = FormState.VIEW;

	private MultipleSelectorPanel<Role> multipleRoleSeletor;
	private AjaxLink<Void> add;
	private AjaxLink<Void> close;
	private WebMarkupContainer addContainerButtons;
	private ListPanel<Role> userRolesPanel;

	//private WebMarkupContainer listToolbarContainer;

	private List<ToolbarItem> listToolbar;
	private List<ToolbarItem> t_list = new ArrayList<ToolbarItem>();

	
	IModel<Person> pmodel;
	private ObjectStateEnumSelector oses;

	public UserExpandedPanel(String id, IModel<User> model) {
		super(id, model);
		setOutputMarkupId(true);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();
		addUserRoles();

		Person person = getPersonDBService().getByUser( getModel().getObject()).get();
		pmodel = new ObjectModel<Person>(person);
		
		add(new Label("name",  pmodel.getObject().getLastFirstname()));
		add(new Label("email", (pmodel.getObject().getEmail()!=null)? (pmodel.getObject().getEmail()) : "-"));
		add(new Label("phone", (pmodel.getObject().getPhone()!=null)? (pmodel.getObject().getPhone()) : "-"));
		
		
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (this.userRoles != null)
			this.userRoles.forEach(t -> t.detach());
		
		if (pmodel!=null)
			pmodel.detach();

		 
	}

	@Override
	public List<ToolbarItem> getToolbarItems() {
		return t_list;
	}

	public void setObjectStateEnumSelector(ObjectStateEnumSelector o) {
		this.oses = o;
	}

	public ObjectStateEnumSelector getObjectStateEnumSelector() {
		return this.oses;
	}

	protected void setList(List<IModel<Role>> list) {
		this.userRoles = list;
	}

	public FormState getState() {
		return this.state;
	}

	public void setState(FormState state) {
		this.state = state;
	}

	protected void onCancel(AjaxRequestTarget target) {
		setState(FormState.VIEW);
		target.add(this);
	}

	public void onEdit(AjaxRequestTarget target) {
		setState(FormState.EDIT);
		target.add(this);
	}

	protected List<ToolbarItem> getListToolbarItems() {
		return null;
		/**
		 * if (listToolbar != null) return listToolbar;
		 * 
		 * listToolbar = new ArrayList<ToolbarItem>();
		 * 
		 * IModel<String> selected =
		 * Model.of(getObjectStateEnumSelector().getLabel(getLocale()));
		 * ObjectStateListSelector s = new ObjectStateListSelector("item", selected,
		 * Align.TOP_LEFT);
		 * 
		 * listToolbar.add(s);
		 * 
		 * return listToolbar;
		 */

	}

	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<ObjectStateSelectEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(ObjectStateSelectEvent event) {
				setObjectStateEnumSelector(event.getObjectStateEnumSelector());
				loadUserRolesList();
				event.getTarget().add(UserExpandedPanel.this.userRolesPanel);
				//event.getTarget().add(UserExpandedPanel.this.listToolbarContainer);
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof ObjectStateSelectEvent)
					return true;
				return false;
			}
		});

	}

	protected synchronized void loadUserRolesList() {

		this.userRoles = new ArrayList<IModel<Role>>();
		User user;
		if (!getModel().getObject().isDependencies()) {
			user = getUserDBService().findWithDeps(getModel().getObject().getId()).get();
		} else
			user = getModel().getObject();

		getUserDBService().getUserRoles(user).forEach(s -> this.userRoles.add(new ObjectModel<Role>(s)));
	}

	protected IModel<String> getObjectInfo(IModel<Role> model) {
		return new Model<String>(model.getObject().getRoleDisplayName() + " (" + model.getObject().getDisplayClass(getLocale()) + ") ");
	}

	protected IModel<String> getObjectSubtitle(IModel<ArtExhibitionItem> model) {
		return null;
	}

	protected IModel<String> getObjectTitle(IModel<Role> model) {
		StringBuilder str = new StringBuilder();
		str.append(model.getObject().getRoleDisplayName() + " (" + model.getObject().getDisplayClass(getLocale()) + ") ");

		if (model.getObject().getState() == ObjectState.DELETED)
			str.append(ServerConstant.DELETED_ICON);

		return Model.of(str.toString());
	}

	private void resetList() {
		this.userRoles = null;
	}

	private void setUpModel() {
		setObjectStateEnumSelector(ObjectStateEnumSelector.EDTIION_PUBLISHED);
		User u = getModel().getObject();
		getModel().setObject(getUserDBService().findWithDeps(u.getId()).get());
	}

	 

	private WebMarkupContainer getMenu(IModel<Role> model) {
		NavDropDownMenu<Role> menu = new NavDropDownMenu<Role>("menu", model, null) {
			private static final long serialVersionUID = 1L;

			public boolean isVisible() {
				return true;
			}
		};

		menu.setOutputMarkupId(true);

		menu.setTitleCss
("d-block-inline d-sm-block-inline d-md-block-inline d-lg-none d-xl-none d-xxl-none ps-1 pe-1");
		menu.setIconCss("fa-solid fa-ellipsis d-block-inline d-sm-block-inline d-md-block-inline d-lg-block-inline d-xl-block-inline d-xxl-block-inline ps-1 pe-1");

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Role>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Role> getItem(String id) {

				return new AjaxLinkMenuItem<Role>(id) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						// refresh(target);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("open");
					}

				};
			}
		});

		 
		return menu;
	}

	private List<IModel<Role>> getUserRoles() {

		if (this.userRoles == null) {
			loadUserRolesList();
		}
		return this.userRoles;
	}

	 
	 
	private void addUserRoles() {

		this.userRolesPanel = new ListPanel<Role>("userRoles") {

			private static final long serialVersionUID = 1L;

			protected List<IModel<Role>> filter(List<IModel<Role>> initialList, String filter) {
				return iFilter(initialList, filter);
			}

			@Override
			protected WebMarkupContainer getToolbar() {
				return new InvisiblePanel("toolbar");
			}

			@Override
			protected WebMarkupContainer getListItemExpandedPanel(IModel<Role> model, ListPanelMode mode) {
				return null;
			}

			@Override
			protected Panel getListItemPanel(IModel<Role> model) {

				DelleMuseObjectListItemPanel<Role> panel = new DelleMuseObjectListItemPanel<Role>("row-element", model, getListPanelMode()) {
					private static final long serialVersionUID = 1L;

					@Override
					protected IModel<String> getObjectTitle() {
						return UserExpandedPanel.this.getObjectTitle(getModel());
					}

					@Override
					protected String getImageSrc() {
						return null;
						// return UserRolesPanel.this.getObjectImageSrc(getModel());
					}

					@Override
					public void onClick() {
						// setResponsePage(new
						// ArtExhibitionItemPage(getModel(),UserRolesPanel.this.getItems()));
					}

					@Override
					protected IModel<String> getInfo() {
						return UserExpandedPanel.this.getObjectInfo(getModel());
					}

					@Override
					protected WebMarkupContainer getObjectMenu() {
						return null;	
						//return UserExpandedPanel.this.getMenu(getModel());
					}

					@Override
					protected String getTitleIcon() {
						return null;
					}

				};
				
				return panel;
			}

			@Override
			public List<IModel<Role>> getItems() {
				return UserExpandedPanel.this.getUserRoles();
			}

			// @Override
			// protected void setItems(List<IModel<ArtExhibitionItem>> list) {
			// ArtExhibitionItemsPanel.this.setList(list);
			// }

		};
		add(userRolesPanel);

		// panel.setTitle(getLabel("exhibitions-permanent"));
		userRolesPanel.setListPanelMode(ListPanelMode.TITLE);
		userRolesPanel.setLiveSearch(false);
		userRolesPanel.setSettings(false);
		userRolesPanel.setHasExpander(false);
		userRolesPanel.setItemMenu(false);
	}

	/**
	 * 
	 * 
	
	private void addListToolbar() {

		this.listToolbarContainer = new WebMarkupContainer("listToolbarContainer") {
			private static final long serialVersionUID = 1L;
		};

		this.listToolbarContainer.setOutputMarkupId(true);
		add(this.listToolbarContainer);

		List<ToolbarItem> list = getListToolbarItems();

		if (list != null && list.size() > 0) {

			Toolbar toolbarItems = new Toolbar("listToolbar");
			list.forEach(t -> toolbarItems.addItem(t));

			this.listToolbarContainer.add(toolbarItems);

		} else {
			this.listToolbarContainer.add(new InvisiblePanel("listToolbar"));
		}
	}
 */
}
