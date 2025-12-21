package dellemuse.serverapp.page.library;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.page.ObjectListPage;
import dellemuse.serverapp.role.RolePage;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.security.Role;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.serverdb.service.security.RoleDBService;
import io.wktui.error.ErrorPanel;
import io.wktui.event.UIEvent;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavDropDownMenu;
 
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.struct.list.ListPanelMode;

/**
 * 
 *  
 *   
 */

@MountPath("/security/roles")
public class RolesListPage extends ObjectListPage<Role> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(RolesListPage.class.getName());

	private List<ToolbarItem> listToolbar;
	private List<ToolbarItem> mainToolbar;

	private RoleEnumSelector selected;
	
	
	@Override
	public boolean hasAccessRight(Optional<User> ouser) {
		if (ouser.isEmpty())
			return false;
		
		User user = ouser.get();  if (user.isRoot()) return true;
		if (!user.isDependencies()) {
			user = getUserDBService().findWithDeps(user.getId()).get();
		}

		Set<RoleGeneral> set = user.getRolesGeneral();
		if (set==null)
			return false;
		return set.stream().anyMatch((p -> p.getKey().equals(RoleGeneral.ADMIN) || p.getKey().equals(RoleGeneral.AUDIT) ));
	}
	
	public RolesListPage() {
		super();
	}

	public RolesListPage(PageParameters parameters) {
		super(parameters);
	}

	public void onInitialize() {
		super.onInitialize();
		
		selected=RoleEnumSelector.ALL;
		super.setIsExpanded(false);
	}
	

	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<RoleSelectEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(RoleSelectEvent event) {
				selected=event.getRoleEnumSelector();
				logger.debug(event.toString());
				loadList();
				refresh(event.getTarget());
 			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof RoleSelectEvent)
					return true;
				return false;
			}
		});
 	}

 	@Override
	public Iterable<Role> getObjects() {
		
		RoleDBService service = (RoleDBService) ServiceLocator.getInstance().getBean(RoleDBService.class);

		if (selected==RoleEnumSelector.ALL)
			return service.findAllSorted(ObjectState.EDITION, ObjectState.PUBLISHED);
		
		if (selected==RoleEnumSelector.SITE) {
			List<Role> list = new ArrayList<Role>();
			service.findSiteAllSorted(ObjectState.EDITION, ObjectState.PUBLISHED).forEach( r -> list.add(r));
			return list;
		}
		
		if (selected==RoleEnumSelector.INSTITUTION) {
			List<Role> list = new ArrayList<Role>();
			service.findInstitutionAllSorted(ObjectState.EDITION, ObjectState.PUBLISHED).forEach( r -> list.add(r));
			return list;
		}
		
		if (selected==RoleEnumSelector.GENERAL) {
			List<Role> list = new ArrayList<Role>();
			service.findGeneralAllSorted(ObjectState.EDITION, ObjectState.PUBLISHED).forEach( r -> list.add(r));
			return list;
		}
		
		return service.findAllSorted(ObjectState.EDITION, ObjectState.PUBLISHED);
	}

	@Override
	public Iterable<Role> getObjects(ObjectState os1) {
		return this.getObjects();
	}

	@Override
	public Iterable<Role> getObjects(ObjectState os1, ObjectState os2) {
		return getObjects();
	}

	@Override
	public IModel<String> getObjectInfo(IModel<Role> model) {
		return new Model<String>(model.getObject().getName());
	}

	@Override
	public IModel<String> getObjectTitle(IModel<Role> model) {
		if (this.selected==RoleEnumSelector.ALL) {
			return new Model<String>(model.getObject().getRoleDisplayName() + " (" + model.getObject().getDisplayClass(getLocale()) + ") ");
		}
		return Model.of(model.getObject().getRoleDisplayName());
	}

	@Override
	public void onClick(IModel<Role> model) {
		 setResponsePage(new RolePage(model, getList()));
	}

	@Override
	public IModel<String> getPageTitle() {
		return null;
		// return getLabel("roles");
	}

	@Override
	public IModel<String> getListPanelLabel() {
		return null;
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	protected ListPanelMode getListPanelMode() {
		return ListPanelMode.TITLE;
	}

	@Override
	protected String getObjectImageSrc(IModel<Role> model) {
 	/**
		 * if (model.getObject().getPhoto()!=null) { Resource photo =
		 * getResource(model.getObject().getPhoto().getId()).get(); return
		 * getPresignedThumbnailSmall(photo); }
		 **/
		return null;
	}

 	@Override
	protected List<ToolbarItem> getMainToolbarItems() {

		/**
		if (mainToolbar != null)
			return mainToolbar;

		mainToolbar = new ArrayList<ToolbarItem>();

		ButtonCreateToolbarItem<Void> create = new ButtonCreateToolbarItem<Void>("item") {
			private static final long serialVersionUID = 1L;

			protected void onClick() {
				// InstitutionsListPage.this.onCreate();
			}
		};
		create.setAlign(Align.TOP_LEFT);
		mainToolbar.add(create);

		return mainToolbar;
	**/
		return null;
	}

	protected void addHeaderPanel() {
		try {
		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(new BCElement(getLabel("roles")));
		JumboPageHeaderPanel<Void> ph = new JumboPageHeaderPanel<Void>("page-header", null, getLabel("roles"));
		ph.setBreadCrumb(bc);
		ph.setContext(getLabel("security"));
		ph.setIcon(Role.getIcon());
		ph.setHeaderCss("mb-0 pb-2 border-none");
		add(ph);
		} catch (Exception e) {
			logger.error(e);
			addOrReplace(new ErrorPanel("page-header",e));
		}
 	}

	@Override
	protected List<ToolbarItem> getListToolbarItems() {

		if (listToolbar != null)
			return listToolbar;

		listToolbar = new ArrayList<ToolbarItem>();

		IModel<String> selected = Model.of(RoleEnumSelector.ALL.getLabel(getLocale()));
		RoleListSelector s = new RoleListSelector("item", selected, Align.TOP_LEFT);
		listToolbar.add(s);
		
		return listToolbar;
	}

	@Override
	protected WebMarkupContainer getObjectMenu(IModel<Role> model) {

		NavDropDownMenu<Role> menu = new NavDropDownMenu<Role>("menu", model, null);

		menu.setOutputMarkupId(true);

		menu.setTitleCss
("d-block-inline d-sm-block-inline d-md-block-inline d-lg-none d-xl-none d-xxl-none ps-1 pe-1");
		menu.setIconCss("fa-solid fa-ellipsis d-block-inline d-sm-block-inline d-md-block-inline d-lg-block-inline d-xl-block-inline d-xxl-block-inline ps-1 pe-1");

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Role>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Role> getItem(String id) {

				return new LinkMenuItem<Role>(id) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage(new RolePage(model, getList()));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("open");
					}
				};
			}
		});

		/**
		 * menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Role>() {
		 * 
		 * private static final long serialVersionUID = 1L;
		 * 
		 * @Override public MenuItemPanel<Role> getItem(String id) {
		 * 
		 *           return new AjaxLinkMenuItem<Role>(id) {
		 * 
		 *           private static final long serialVersionUID = 1L;
		 * 
		 * @Override public void onClick(AjaxRequestTarget target) { // refresh(target);
		 *           }
		 * 
		 * @Override public IModel<String> getLabel() { return getLabel("delete"); } };
		 *           } });
		 **/
		return menu;
	}

	protected IModel<String> getTitleLabel() {
		return null;
		//return getLabel("roles");
	}

	protected WebMarkupContainer getSubmenu() {
		return null;
	}

	protected void onCreate() {
		/**
		 * try { Role in = getRoleDBService().create("new",
		 * getUserDBService().findRoot()); IModel<Role> m = new ObjectModel<Role>(in);
		 * getList().add(m); setResponsePage(new RolePage(m, getList())); } catch
		 * (Exception e) { logger.error(e); setResponsePage(new ErrorPage(e));
		 * 
		 * }
		 **/
	}
	 
	@Override
	protected String getObjectTitleIcon(IModel<Role> model) {
		return null;
	}

}
