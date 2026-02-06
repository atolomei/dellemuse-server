package dellemuse.serverapp.page.site;

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
import dellemuse.serverapp.page.DellemuseServerAppHomePage;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.MultipleSelectorPanel;
 
import dellemuse.serverapp.page.library.ObjectStateEnumSelector;
import dellemuse.serverapp.page.library.ObjectStateSelectEvent;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.user.UserPage;
import dellemuse.serverapp.role.RolePage;
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

public class SiteRolesPanel extends DBModelPanel<Site> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteRolesPanel.class.getName());

	private List<IModel<RoleSite>> roles;
 	private ListPanel<RoleSite> rolesPanel;
	private WebMarkupContainer listToolbarContainer;

	// private List<ToolbarItem> t_list = new ArrayList<ToolbarItem>();

	private ObjectStateEnumSelector oses;

	public SiteRolesPanel(String id, IModel<Site> model) {
		super(id, model);
		setOutputMarkupId(true);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();
		addListToolbar();
		addRolesPanel();

	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (this.roles != null)
			this.roles.forEach(t -> t.detach());
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

	protected void setList(List<IModel<RoleSite>> list) {
		this.roles = list;
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
				event.getTarget().add(SiteRolesPanel.this.rolesPanel);
				event.getTarget().add(SiteRolesPanel.this.listToolbarContainer);
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof ObjectStateSelectEvent)
					return true;
				return false;
			}
		});

	}

	 protected List<IModel<RoleSite>> getUsers() {
		 if (this.roles==null)
			 load();
		 return this.roles;
	 }
	 
	protected synchronized void load() {

		this.roles = new ArrayList<IModel<RoleSite>>();
	 
		Site site;
		
		if (!getModel().getObject().isDependencies()) {
			site = getSiteDBService().findWithDeps(getModel().getObject().getId()).get();
		}
		else
			site = getModel().getObject();
			
		getRoleSiteDBService().findBySite(site).forEach( u -> roles.add( new ObjectModel<RoleSite>(u)) );
	}
 	
	protected IModel<String> getObjectTitle(IModel<RoleSite> model) {
		StringBuilder str = new StringBuilder();
		str.append(model.getObject().getRoleDisplayName());
	
		RoleSite o  = model.getObject();
		
		if (o.getState() == ObjectState.DELETED)
			return new Model<String>(str.toString() + Icons.DELETED_ICON);
		
		if (o.getState() == ObjectState.EDITION)
			return new Model<String>(str.toString() + Icons.EDITION_ICON);

		return Model.of(str.toString());
	}

	private void setUpModel() {
		setObjectStateEnumSelector(ObjectStateEnumSelector.EDTIION_PUBLISHED);
		Site s = getModel().getObject();
		getModel().setObject(getSiteDBService().findWithDeps(s.getId()).get());
	}

	
/**
	private WebMarkupContainer getMenu(IModel<RoleSite> model) {
		NavDropDownMenu<RoleSite> menu = new NavDropDownMenu<RoleSite>("menu", model, null) {
			private static final long serialVersionUID = 1L;

			public boolean isVisible() {
				return true;
			}
		};

		menu.setOutputMarkupId(true);

		menu.setTitleCss
("d-block-inline d-sm-block-inline d-md-block-inline d-lg-none d-xl-none d-xxl-none ps-1 pe-1");
		menu.setIconCss("fa-solid fa-ellipsis d-block-inline d-sm-block-inline d-md-block-inline d-lg-block-inline d-xl-block-inline d-xxl-block-inline ps-1 pe-1");

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<RoleSite>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<RoleSite> getItem(String id) {

				return new AjaxLinkMenuItem<RoleSite>(id) {

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

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<RoleSite>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<RoleSite> getItem(String id) {

				return new AjaxLinkMenuItem<RoleSite>(id) {

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
	
	
	private void addRolesPanel() {

		this.rolesPanel = new ListPanel<RoleSite>("roles") {

			private static final long serialVersionUID = 1L;

			
			@Override
			public IModel<String> getItemLabel(IModel<RoleSite> model) {
				return SiteRolesPanel.this.getObjectTitle(model);
			}

			@Override
			protected WebMarkupContainer getListItemExpandedPanel(IModel<RoleSite> model, ListPanelMode mode) {
				
				IModel<Role> m=new ObjectModel<Role>( model.getObject());
				
				return new RoleUsersPanel("expanded-panel", m, true) {
					
					private static final long serialVersionUID = 1L;
					
					@Override
					protected boolean isTitleVisible() {
						return false;
					}
					
					@Override
					protected boolean isToolbar() {
						return false;
					}
				};
			}

			@Override
			protected Panel getListItemPanel(IModel<RoleSite> model) {

				DelleMuseObjectListItemPanel<RoleSite> panel = new DelleMuseObjectListItemPanel<RoleSite>("row-element", model, getListPanelMode()) {
					private static final long serialVersionUID = 1L;

					@Override
					protected IModel<String> getObjectTitle() {
						return SiteRolesPanel.this.getObjectTitle(getModel());
					}

					@Override
					protected String getImageSrc() {
						return null;
					}

					@Override
					public void onClick() {
					  setResponsePage(new RolePage( new ObjectModel<Role>(getModel().getObject())));
					}
					 
					@Override
					protected String getTitleIcon() {
						return null;
					}

				};
				return panel;
			}

			@Override
			public List<IModel<RoleSite>> getItems() {
				return SiteRolesPanel.this.getUsers();
			}
		};
		add(rolesPanel);

		rolesPanel.setListPanelMode(ListPanelMode.TITLE);
		rolesPanel.setLiveSearch(false);
		rolesPanel.setSettings(true);
		rolesPanel.setHasExpander(true);
	}

}
