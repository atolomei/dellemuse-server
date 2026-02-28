package dellemuse.serverapp.role;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.icons.Icons;
import dellemuse.serverapp.institution.InstitutionUsersPanel;
import dellemuse.serverapp.page.DelleMuseObjectListItemPanel;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.MultipleSelectorPanel;

import dellemuse.serverapp.page.library.ObjectStateEnumSelector;
import dellemuse.serverapp.page.library.ObjectStateSelectEvent;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.user.UserPage;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;

import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Person;
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

	@SuppressWarnings("unused")
	static private Logger logger = Logger.getLogger(RoleUsersPanel.class.getName());

	private List<IModel<User>> roleUsers;
	private ListPanel<User> roleUsersPanel;
	private WebMarkupContainer listToolbarContainer;

	private ObjectStateEnumSelector oses;

	boolean titleVisible = true;
	public RoleUsersPanel(String id, IModel<Role> model, boolean titleVisible) {
		super(id, model);
		this.titleVisible=titleVisible;
		setOutputMarkupId(true);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();
		addTitlePanel();
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

	protected void addTitlePanel() {
		add(new WebMarkupContainer("titleContainer") {
			private static final long serialVersionUID = 1L;

			public boolean isVisible() {
				return RoleUsersPanel.this.isTitleVisible();
			}
		});

	}

	protected boolean isTitleVisible() {
		return this.titleVisible;
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

		if (this.roleUsers == null)
			load();

		return this.roleUsers;
	}

	protected synchronized void load() {

		this.roleUsers = new ArrayList<IModel<User>>();
		Role role;
		if (!getModel().getObject().isDependencies()) {
			role = getRoleDBService().findWithDeps(getModel().getObject()).get();
		} else
			role = getModel().getObject();
		role.getUsers().forEach(s -> this.roleUsers.add(new ObjectModel<User>(s)));
		
		
		this.roleUsers.sort( new Comparator<IModel<User>>() {
			@Override
			public int compare(IModel<User> o1, IModel<User> o2) {
				 return o1.getObject().getUsername().compareToIgnoreCase(o2.getObject().getUsername());
			}
		});
		
		
		
		
	}

	protected IModel<String> getObjectTitle(IModel<User> model) {
		StringBuilder str = new StringBuilder();
		str.append(model.getObject().getName());
		
		User o  = model.getObject();
		
		
		Optional<Person> op = getPersonDBService().getByUser(o);
		
		if (op.isPresent()) {
				str.append(" <span class=\"text-secondary\"> - " + op.get().getFirstLastname()+ " </span>");
		}
		
		if (o.getState() == ObjectState.DELETED)
			return new Model<String>(str.toString() + Icons.DELETED_ICON_HTML);
		
		if (o.getState() == ObjectState.EDITION)
			return new Model<String>(str.toString() + Icons.EDITION_ICON_HTML);

		
		return Model.of(str.toString());
	}

	private void setUpModel() {
		setObjectStateEnumSelector(ObjectStateEnumSelector.EDTIION_PUBLISHED);
		Role u = getModel().getObject();
		getModel().setObject(getRoleDBService().findWithDeps(u).get());
	}
 
	/**
	 * 
	 * 
	 */
	private void addRoleUsers() {

		this.roleUsersPanel = new ListPanel<User>("users") {

			private static final long serialVersionUID = 1L;

			@Override
			public IModel<String> getItemLabel(IModel<User> model) {
				return  RoleUsersPanel.this.getObjectTitle(model);
			}
			
			@Override
			protected boolean isToolbar() {
				return RoleUsersPanel.this.isToolbar();
			}

			 

			@Override
			protected WebMarkupContainer getListItemExpandedPanel(IModel<User> model, ListPanelMode mode) {
				return null;
				 
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
				return RoleUsersPanel.this.getRoleUsers();
			}

		};
		add(roleUsersPanel);

		roleUsersPanel.setListPanelMode(ListPanelMode.TITLE);
		roleUsersPanel.setLiveSearch(false);
		roleUsersPanel.setSettings(true);
		roleUsersPanel.setHasExpander(false);

	}

	protected boolean isToolbar() {
		return true;
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
