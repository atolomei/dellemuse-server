package dellemuse.serverapp.page.site;

import java.util.ArrayList;
import java.util.Comparator;
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
import dellemuse.serverapp.page.library.ObjectStateSelectEvent;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.user.UserExpandedPanel;
import dellemuse.serverapp.page.user.UserPage;
import dellemuse.serverapp.role.RoleUsersPanel;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
 
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.security.Role;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
import dellemuse.serverapp.serverdb.model.security.RoleSite;
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

public class SiteUsersPanel extends DBModelPanel<Site> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteUsersPanel.class.getName());

	private List<IModel<User>> users;
 	private ListPanel<User> usersPanel;
	private WebMarkupContainer listToolbarContainer;

	// private List<ToolbarItem> t_list = new ArrayList<ToolbarItem>();

	private ObjectStateEnumSelector oses;

	public SiteUsersPanel(String id, IModel<Site> model) {
		super(id, model);
		setOutputMarkupId(true);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();
		addListToolbar();
		addUsersPanel();

	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (this.users != null)
			this.users.forEach(t -> t.detach());
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
		this.users = list;
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
				event.getTarget().add(SiteUsersPanel.this.usersPanel);
				event.getTarget().add(SiteUsersPanel.this.listToolbarContainer);
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof ObjectStateSelectEvent)
					return true;
				return false;
			}
		});

	}

	 protected List<IModel<User>> getUsers() {
		 if (this.users==null)
			 load();
		 return this.users;
	 }
	 
	protected synchronized void load() {

		this.users = new ArrayList<IModel<User>>();
	 
		Site site;
		
		if (!getModel().getObject().isDependencies()) {
			site = getSiteDBService().findWithDeps(getModel().getObject().getId()).get();
		}
		else
			site = getModel().getObject();
			
		getUserDBService().getSiteUsers( site ).forEach( u -> users.add( new ObjectModel<User>(u)) );
		
		this.users.sort( new Comparator<IModel<User>>() {
			@Override
			public int compare(IModel<User> o1, IModel<User> o2) {
			 	return o1.getObject().getUsername().compareToIgnoreCase(o2.getObject().getUsername());
			}
		});
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
	
		User o  = model.getObject();
		
		if (o.getState() == ObjectState.DELETED)
			return new Model<String>(str.toString() + Icons.DELETED_ICON_HTML);
		
		if (o.getState() == ObjectState.EDITION)
			return new Model<String>(str.toString() + Icons.EDITION_ICON_HTML);


		return Model.of(str.toString());
	}
 

	private void setUpModel() {
		setObjectStateEnumSelector(ObjectStateEnumSelector.EDTIION_PUBLISHED);
		Site s = getModel().getObject();
		getModel().setObject(getSiteDBService().findWithDeps(s.getId()).get());
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
	
	
	private void addUsersPanel() {

		this.usersPanel = new ListPanel<User>("users") {

			private static final long serialVersionUID = 1L;

			@Override
			public IModel<String> getItemLabel(IModel<User> model) {
				return SiteUsersPanel.this.getObjectTitle(model);
			}


			@Override
			protected WebMarkupContainer getListItemExpandedPanel(IModel<User> model, ListPanelMode mode) {
					return new UserExpandedPanel("expanded-panel", model);
			}

			@Override
			protected Panel getListItemPanel(IModel<User> model) {

				DelleMuseObjectListItemPanel<User> panel = new DelleMuseObjectListItemPanel<User>("row-element", model, getListPanelMode()) {
					private static final long serialVersionUID = 1L;

					@Override
					protected IModel<String> getObjectTitle() {
						return SiteUsersPanel.this.getObjectTitle(getModel());
					}

					@Override
					protected String getImageSrc() {
						return null;
						// return UserRolesPanel.this.getObjectImageSrc(getModel());
					}

					@Override
					public void onClick() {
						  setResponsePage(new UserPage(getModel()));
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
				return SiteUsersPanel.this.getUsers();
			}
 		};

 		add(usersPanel);
		 
		usersPanel.setListPanelMode(ListPanelMode.TITLE);
		usersPanel.setLiveSearch(false);
		usersPanel.setSettings(true);
		usersPanel.setHasExpander(true);
	}

}
