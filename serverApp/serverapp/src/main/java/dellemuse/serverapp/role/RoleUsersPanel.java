package dellemuse.serverapp.role;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
 
import dellemuse.serverapp.page.DelleMuseObjectListItemPanel;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.MultipleSelectorPanel;
 
import dellemuse.serverapp.page.library.ObjectStateEnumSelector;
import dellemuse.serverapp.page.library.ObjectStateSelectEvent;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
 
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
 
import dellemuse.serverapp.serverdb.model.ObjectState;
 
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.security.Role;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
import io.wktui.event.UIEvent;
import io.wktui.form.FormState;
 
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.toolbar.Toolbar;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;
import wktui.base.InvisiblePanel;

public class RoleUsersPanel extends DBModelPanel<Role> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(RoleUsersPanel.class.getName());

	private List<IModel<User>> roleUsers;
 	private ListPanel<User> roleUsersPanel;
	private WebMarkupContainer listToolbarContainer;


	
	 
	private ObjectStateEnumSelector oses;

	public RoleUsersPanel(String id, IModel<Role> model) {
		super(id, model);
		setOutputMarkupId(true);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();
		addListToolbar();
		addRoleUsers();
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (this.roleUsers != null)
			this.roleUsers.forEach(t -> t.detach());
	}

	@Override
	public List<ToolbarItem> getToolbarItems() {
		return null;
	}

	public void setObjectStateEnumSelector(ObjectStateEnumSelector o) {
		this.oses = o;
	}

	public ObjectStateEnumSelector getObjectStateEnumSelector() {
		return this.oses;
	}

	protected void setList(List<IModel<User>> list) {
		this.roleUsers = list;
	}

 

	protected List<ToolbarItem> getListToolbarItems() {
		return null;
		/**
		if (listToolbar != null)
			return listToolbar;

		listToolbar = new ArrayList<ToolbarItem>();

		IModel<String> selected = Model.of(getObjectStateEnumSelector().getLabel(getLocale()));
		ObjectStateListSelector s = new ObjectStateListSelector("item", selected, Align.TOP_LEFT);

		listToolbar.add(s);

		return listToolbar;
	*/
		
	}

	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<ObjectStateSelectEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(ObjectStateSelectEvent event) {
				setObjectStateEnumSelector(event.getObjectStateEnumSelector());
				load();
				event.getTarget().add(RoleUsersPanel.this.roleUsersPanel);
				event.getTarget().add(RoleUsersPanel.this.listToolbarContainer);
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof ObjectStateSelectEvent)
					return true;
				return false;
			}
		});

	}

	 protected List<IModel<User>> getRoleUsers() {
		 
		 if ( this.roleUsers==null)
			 load();
		 
		 return this.roleUsers;
	 }
	 
	protected synchronized void load() {

		this.roleUsers = new ArrayList<IModel<User>>();

	 
		Role role;
		
		if (!getModel().getObject().isDependencies()) {
			role = getRoleDBService().findWithDeps(getModel().getObject()).get();
		}
		else
			role = getModel().getObject();
				
		role.getUsers().forEach(s -> this.roleUsers.add(new ObjectModel<User>(s)));

		/**
		 * if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.PUBLISHED)
		 * getObjects(ObjectState.PUBLISHED).forEach(s -> this.userRoles.add(new
		 * ObjectModel<Role>(s)));
		 * 
		 * if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.EDITION)
		 * getObjects(ObjectState.EDITION).forEach(s -> this.userRoles.add(new
		 * ObjectModel<Role>(s)));
		 * 
		 * else if (this.getObjectStateEnumSelector() == null) getObjects().forEach(s ->
		 * this.userRoles.add(new ObjectModel<Role>(s)));
		 * 
		 * else if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.ALL)
		 * getObjects().forEach(s -> this.userRoles.add(new
		 * ObjectModel<ArtExhibitionItem>(s)));
		 * 
		 * else if (this.getObjectStateEnumSelector() ==
		 * ObjectStateEnumSelector.DELETED) getObjects(ObjectState.DELETED).forEach(s ->
		 * this.userRoles.add(new ObjectModel<Role>(s)));
		 **/
		
		this.roleUsers.forEach(c -> logger.debug(c.getObject().toString()));
	
	}

	/**
	protected void onObjectRemove(IModel<Role> model, AjaxRequestTarget target) {
		getUserDBService().removeRole( getModel().getObject(), model.getObject(), getSessionUser());
		resetList();
		target.add(this.roleUsersPanel);
	}

	protected void onObjectSelect(IModel<Role> model, AjaxRequestTarget target) {
		getUserDBService().addRole( getModel().getObject(), model.getObject(), getSessionUser());
		resetList();
		target.add(this.roleUsersPanel);

	}
	
	protected IModel<String> getObjectInfo(IModel<Role> model) {
		return new Model<String>(model.getObject().getRoleDisplayName() + " (" + model.getObject().getDisplayClass(getLocale()) + ") ");
	}

	protected IModel<String> getObjectSubtitle(IModel<ArtExhibitionItem> model) {
		return null;
	}
**/

	protected IModel<String> getObjectTitle(IModel<User> model) {
		StringBuilder str = new StringBuilder();
		str.append(model.getObject().getName());

		if (model.getObject().getState() == ObjectState.DELETED)
			str.append(ServerConstant.DELETED_ICON);

		return Model.of(str.toString());
	}
	
	
	//private void resetList() {
	//	this.roleUsers = null;
	//}

	private void setUpModel() {
		setObjectStateEnumSelector(ObjectStateEnumSelector.EDTIION_PUBLISHED);
		Role u = getModel().getObject();
		getModel().setObject(getRoleDBService().findWithDeps(u).get());
	}

	
/**
	private WebMarkupContainer getMenu(IModel<Role> model) {
		NavDropDownMenu<Role> menu = new NavDropDownMenu<Role>("menu", model, null) {
			private static final long serialVersionUID = 1L;

			public boolean isVisible() {
				return true;
			}
		};

		menu.setOutputMarkupId(true);

		menu.setLabelCss("d-block-inline d-sm-block-inline d-md-block-inline d-lg-none d-xl-none d-xxl-none ps-1 pe-1");
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

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Role>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Role> getItem(String id) {

				return new AjaxLinkMenuItem<Role>(id) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						RoleUsersPanel.this.onObjectRemove(getModel(), target);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("remove");
					}
				};
			}
		});
		return menu;
	}
*/
	
	 
 
	/**
	 * 
	 * 
	 */
	private void addRoleUsers() {

		this.roleUsersPanel = new ListPanel<User>("users") {

			private static final long serialVersionUID = 1L;

			protected List<IModel<User>> filter(List<IModel<User>> initialList, String filter) {
				return iFilter(initialList, filter);
			}

			@Override
			protected WebMarkupContainer getListItemExpandedPanel(IModel<User> model, ListPanelMode mode) {
				return null;
				// return UserRolesPanel.this.getObjectListItemExpandedPanel(model, mode);

			}

			@Override
			protected Panel getListItemPanel(IModel<User> model) {

				DelleMuseObjectListItemPanel<User> panel = new DelleMuseObjectListItemPanel<User>("row-element", model, getListPanelMode()) {
					private static final long serialVersionUID = 1L;

					@Override
					protected IModel<String> getObjectTitle() {
						return RoleUsersPanel.this.getObjectTitle(getModel());
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
					protected String getTitleIcon() {
						return null;
					}

				};
				return panel;
			}

			@Override
			public List<IModel<User>> getItems() {
				return RoleUsersPanel.this. getRoleUsers();
			}

			// @Override
			// protected void setItems(List<IModel<ArtExhibitionItem>> list) {
			// ArtExhibitionItemsPanel.this.setList(list);
			// }

		};
		add(roleUsersPanel);

		// panel.setTitle(getLabel("exhibitions-permanent"));
		roleUsersPanel.setListPanelMode(ListPanelMode.TITLE);
		roleUsersPanel.setLiveSearch(false);
		roleUsersPanel.setSettings(true);
		roleUsersPanel.setHasExpander(true);
	}

	/**
	 * 
	 * 
	 */
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

}
