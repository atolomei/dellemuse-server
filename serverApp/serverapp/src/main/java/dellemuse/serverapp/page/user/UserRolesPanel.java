package dellemuse.serverapp.page.user;

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
import dellemuse.serverapp.icons.Icons;
import dellemuse.serverapp.page.DelleMuseObjectListItemPanel;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.MultipleSelectorPanel;

import dellemuse.serverapp.page.library.ObjectStateEnumSelector;
import dellemuse.serverapp.page.library.ObjectStateListSelector;
import dellemuse.serverapp.page.library.ObjectStateSelectEvent;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.role.RolePage;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;

import dellemuse.serverapp.serverdb.model.ObjectState;

import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.security.Role;

import io.wktui.event.UIEvent;
import io.wktui.form.FormState;

import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.toolbar.Toolbar;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;
import wktui.base.InvisiblePanel;

public class UserRolesPanel extends DBModelPanel<User> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(UserRolesPanel.class.getName());

	private List<IModel<Role>> userRoles;
	private List<IModel<Role>> allRoles;

	private FormState state = FormState.VIEW;

	private MultipleSelectorPanel<Role> multipleRoleSeletor;
	private AjaxLink<Void> add;
	private AjaxLink<Void> close;
	private WebMarkupContainer addContainerButtons;
	private ListPanel<Role> userRolesPanel;

	private WebMarkupContainer listToolbarContainer;

	private List<ToolbarItem> listToolbar;
	private List<ToolbarItem> t_list = new ArrayList<ToolbarItem>();

	private ObjectStateEnumSelector oses;

	public UserRolesPanel(String id, IModel<User> model) {
		super(id, model);
		setOutputMarkupId(true);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();
		addListToolbar();
		addUserRoles();
		addSelector();
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (this.userRoles != null)
			this.userRoles.forEach(t -> t.detach());

		if (allRoles != null)
			this.allRoles.forEach(t -> t.detach());
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
				event.getTarget().add(UserRolesPanel.this.userRolesPanel);
				event.getTarget().add(UserRolesPanel.this.listToolbarContainer);
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

		// if (this.getObjectStateEnumSelector() ==
		// ObjectStateEnumSelector.EDTIION_PUBLISHED) {
		// }

		User user;

		if (!getModel().getObject().isDependencies()) {
			user = getUserDBService().findWithDeps(getModel().getObject().getId()).get();
		} else
			user = getModel().getObject();

		getUserDBService().getUserRoles(user).forEach(s -> this.userRoles.add(new ObjectModel<Role>(s)));

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

		this.userRoles.forEach(c -> logger.debug(c.getObject().toString()));

	}

	protected void onObjectRemove(IModel<Role> model, AjaxRequestTarget target) {
		try {
			getUserDBService().removeRole(getModel().getObject(), model.getObject(), getSessionUser().get());
			resetList();
			target.add(this.userRolesPanel);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	protected void onObjectSelect(IModel<Role> model, AjaxRequestTarget target) {
		getUserDBService().addRole(getModel().getObject(), model.getObject(), getSessionUser().get());
		resetList();
		target.add(this.userRolesPanel);

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

		Role o =  model.getObject();
		
		if (o.getState() == ObjectState.DELETED)
			return new Model<String>(str.toString() + Icons.DELETED_ICON_HTML);
		
		if (o.getState() == ObjectState.EDITION)
			return new Model<String>(str.toString() + Icons.EDITION_ICON_HTML);


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

	private List<IModel<Role>> getAllRoles() {

		if (allRoles != null)
			return allRoles;

		allRoles = new ArrayList<IModel<Role>>();

		getRoleDBService().findAllSorted().forEach(i -> allRoles.add(new ObjectModel<Role>(i)));

		return allRoles;
	}

	private WebMarkupContainer getMenu(IModel<Role> model) {
		NavDropDownMenu<Role> menu = new NavDropDownMenu<Role>("menu", model, null) {
			private static final long serialVersionUID = 1L;

			public boolean isVisible() {
				return true;
			}
		};

		menu.setOutputMarkupId(true);

		menu.setTitleCss("d-block-inline d-sm-block-inline d-md-block-inline d-lg-none d-xl-none d-xxl-none ps-1 pe-1");
		menu.setIconCss("fa-solid fa-ellipsis d-block-inline d-sm-block-inline d-md-block-inline d-lg-block-inline d-xl-block-inline d-xxl-block-inline ps-1 pe-1");

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Role>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Role> getItem(String id) {

				return new LinkMenuItem<Role>(id) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage( new RolePage( getModel()));
					}

					@Override
					public IModel<String> getLabel() {
						return UserRolesPanel.this.getLabel("open");
					}

				};
			}
		});

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Role>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Role> getItem(String id) {

				return new AjaxLinkMenuItem<Role>(id, model) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						UserRolesPanel.this.onObjectRemove(getModel(), target);
					}

					@Override
					public IModel<String> getLabel() {
						return UserRolesPanel.this.getLabel("remove");
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

	private void addSelector() {

		this.addContainerButtons = new WebMarkupContainer("addContainerButtons");
		this.addContainerButtons.setOutputMarkupId(true);

		add(this.addContainerButtons);

		this.add = new AjaxLink<Void>("add") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				setState(FormState.EDIT);
				target.add(addContainerButtons);
				// target.add(addContainer);
			}

			public boolean isVisible() {
				return getState() == FormState.VIEW;
			}
		};

		this.close = new AjaxLink<Void>("close") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				setState(FormState.VIEW);
				target.add(addContainerButtons);
				// target.add(addContainer);
				target.add(multipleRoleSeletor);
			}

			public boolean isVisible() {
				return getState() == FormState.EDIT;
			}
		};

		this.addContainerButtons.add(add);
		this.addContainerButtons.add(close);

		this.multipleRoleSeletor = new MultipleSelectorPanel<Role>("multipleRolesSelector", getAllRoles()) {

			private static final long serialVersionUID = 1L;

			protected IModel<String> getTitle() {
				return getLabel("roles");
			}

			public boolean isVisible() {
				return getState() == FormState.EDIT;
			}

			@Override
			protected void onClick(IModel<Role> model) {
				// TODO Auto-generated method stub
			}

			@Override
			protected IModel<String> getObjectTitle(IModel<Role> model) {
				return UserRolesPanel.this.getObjectTitle(model);
				//return Model.of(model.getObject().getRoleDisplayName() + " (" + model.getObject().getDisplayClass(getLocale()) + ") ");
			}

			@Override
			protected void onObjectSelect(IModel<Role> model, AjaxRequestTarget target) {
				UserRolesPanel.this.onObjectSelect(model, target);
			}

			@Override
			protected IModel<String> getObjectInfo(IModel<Role> model) {
				// String str = TextCleaner.clean(model.getObject().getInfo(), 280);
				return new Model<String>("not done");
			}

			@Override
			protected IModel<String> getObjectSubtitle(IModel<Role> model) {
				return null;
			}

			@Override
			protected boolean isExpander() {
				return true;
			}

			@Override
			protected String getObjectImageSrc(IModel<Role> model) {
				return null;
			}
		};

		this.multipleRoleSeletor.setOutputMarkupId(true);

		this.addContainerButtons.add(this.multipleRoleSeletor);
	}

	/**
	 * 
	 * 
	 */
	private void addUserRoles() {

		this.userRolesPanel = new ListPanel<Role>("userRoles") {

			private static final long serialVersionUID = 1L;

			
			@Override
			public IModel<String> getItemLabel(IModel<Role> model) {
				return UserRolesPanel.this.getObjectTitle(model);
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
						return UserRolesPanel.this.getObjectTitle(getModel());
					}

					@Override
					protected String getImageSrc() {
						return null;
					}

					@Override
					public void onClick() {
						  setResponsePage(new RolePage(getModel()));
					}

					@Override
					protected IModel<String> getInfo() {
						return UserRolesPanel.this.getObjectInfo(getModel());
					}

					@Override
					protected WebMarkupContainer getObjectMenu() {
						return UserRolesPanel.this.getMenu(getModel());
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
				return UserRolesPanel.this.getUserRoles();
			}
 

		};
		add(userRolesPanel);

		 
		userRolesPanel.setListPanelMode(ListPanelMode.TITLE);
		userRolesPanel.setLiveSearch(false);
		userRolesPanel.setSettings(true);
		userRolesPanel.setHasExpander(false);
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
