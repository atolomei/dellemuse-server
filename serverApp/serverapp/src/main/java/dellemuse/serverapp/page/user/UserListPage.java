package dellemuse.serverapp.page.user;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;

import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.icons.Icons;
import dellemuse.serverapp.page.ObjectListPage;
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.library.ObjectStateEnumSelector;
import dellemuse.serverapp.page.library.ObjectStateListSelector;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.ObjectState;

import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.security.Role;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;

import io.wktui.error.ErrorPanel;

import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.toolbar.ButtonCreateToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.struct.list.ListPanelMode;

/**
 * site foto Info - exhibitions
 */

@MountPath("/user/list")
public class UserListPage extends ObjectListPage<User> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(UserListPage.class.getName());

	private List<ToolbarItem> list;
	private List<ToolbarItem> listToolbar;

	@Override
	public boolean hasAccessRight(Optional<User> ouser) {
		if (ouser.isEmpty())
			return false;

		User user = ouser.get();
		if (user.isRoot())
			return true;
		if (!user.isDependencies()) {
			user = getUserDBService().findWithDeps(user.getId()).get();
		}

		Set<RoleGeneral> set = user.getRolesGeneral();
		if (set == null)
			return false;
		return set.stream().anyMatch((p -> p.getKey().equals(RoleGeneral.ADMIN) || p.getKey().equals(RoleGeneral.AUDIT)));
	}

	public UserListPage() {
		super();
		setIsExpanded(true);
	}

	public UserListPage(PageParameters parameters) {
		super(parameters);
		setIsExpanded(true);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
	}

	@Override
	public Iterable<User> getObjects() {
		return super.getUsers();
	}

	@Override
	public Iterable<User> getObjects(ObjectState os1) {
		return this.getObjects(os1, null);
	}

	@Override
	public Iterable<User> getObjects(ObjectState os1, ObjectState os2) {
		return super.getUsers(os1, os2);
	}

	@Override
	public IModel<String> getObjectInfo(IModel<User> model) {
		return new Model<String>(model.getObject().getDisplayname());
	}

	@Override
	public IModel<String> getObjectTitle(IModel<User> model) {

		StringBuilder str = new StringBuilder();
		str.append(model.getObject().getName());
	
		User o  = model.getObject();
		
		if (o.getState() == ObjectState.DELETED)
			return new Model<String>(str.toString() + Icons.DELETED_ICON);
		
		if (o.getState() == ObjectState.EDITION)
			return new Model<String>(str.toString() + Icons.EDITION_ICON);

		return Model.of(str.toString());
	}

	@Override
	public void onClick(IModel<User> model) {
		setResponsePage(new UserPage(model, getList()));
	}

	@Override
	public IModel<String> getListPanelLabel() {
		return null;
	}

	@Override
	protected Panel getObjectListItemExpandedPanel(IModel<User> model, ListPanelMode mode) {
		return new UserExpandedPanel("expanded-panel", model);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public IModel<String> getPageTitle() {
		return null;
	}

	@Override
	protected String getObjectImageSrc(IModel<User> model) {
		return null;
	}

	@Override
	protected List<ToolbarItem> getMainToolbarItems() {

		if (list != null)
			return list;

		list = new ArrayList<ToolbarItem>();

		ButtonCreateToolbarItem<User> create = new ButtonCreateToolbarItem<>("item") {
			private static final long serialVersionUID = 1L;

			protected void onClick() {
				UserListPage.this.onCreate();
			}
		};

		list.add(create);
		return list;
	}

	@Override
	protected void addHeaderPanel() {

		try {
			BreadCrumb<Void> bc = createBreadCrumb();
			bc.addElement(new BCElement(getLabel("security")));
			bc.addElement(new BCElement(getLabel("users")));
			JumboPageHeaderPanel<Void> ph = new JumboPageHeaderPanel<Void>("page-header", null, getLabel("users"));
			ph.setBreadCrumb(bc);
			ph.setIcon(User.getIcon());
			ph.setHeaderCss("mb-0 pb-2 border-none");
			add(ph);
		} catch (Exception e) {
			logger.error(e);
			addOrReplace(new ErrorPanel("page-header", e));
		}
	}

	protected IModel<String> getTitleLabel() {
		return null;
	}

	protected void onCreate() {

		try {

			User in = getUserDBService().create("new", getUserDBService().findRoot());
			IModel<User> m = new ObjectModel<User>(in);
			getList().add(m);
			setResponsePage(new UserPage(m, getList()));

		} catch (Exception e) {
			logger.error(e);
			setResponsePage(new ErrorPage(e));

		}
	}

	@Override
	protected WebMarkupContainer getObjectMenu(IModel<User> model) {

		NavDropDownMenu<User> menu = new NavDropDownMenu<User>("menu", model, null);

		menu.setOutputMarkupId(true);

		menu.setTitleCss("d-block-inline d-sm-block-inline d-md-block-inline d-lg-none d-xl-none d-xxl-none ps-1 pe-1");
		menu.setIconCss("fa-solid fa-ellipsis d-block-inline d-sm-block-inline d-md-block-inline d-lg-block-inline d-xl-block-inline d-xxl-block-inline ps-1 pe-1");

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<User>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<User> getItem(String id) {

				return new LinkMenuItem<User>(id) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage(new UserPage(model));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("open");
					}
				};
			}
		});

		
		
		

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<User>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<User> getItem(String id) {

				return new AjaxLinkMenuItem<User>(id) {

					private static final long serialVersionUID = 1L;

					
					public boolean isEnabled() {
						return getModel().getObject().getState()!=ObjectState.PUBLISHED;
					}
				
					@Override
					public void onClick(AjaxRequestTarget target) {
						getModel().getObject().setState(ObjectState.PUBLISHED);
						getUserDBService().save(getModel().getObject());
						refresh(target);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("publish");
					}
				};
			}
		});
		
		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<User>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<User> getItem(String id) {
				return new io.wktui.nav.menu.SeparatorMenuItem<User>(id) {
					private static final long serialVersionUID = 1L;
				};
			}
		});
		
		
		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<User>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<User> getItem(String id) {
				return new io.wktui.nav.menu.SeparatorMenuItem<User>(id) {
					@Override
					public boolean isVisible() {
						return isRoot();
					}
				};
			}
		});  
		
		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<User>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<User> getItem(String id) {
				return new LinkMenuItem<User>(id) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick( ) {
						try {
							getUserDBService().setSessionUser( getModel().getObject() );
							setResponsePage( new UserListPage());
						} catch (Exception e) {
							logger.error(e);
							setResponsePage ( new ErrorPage (e));
						}
					}
					@Override
					public boolean isVisible() {
						return true; // isRoot();
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("impersonate");
					}
				};

			}
		});
		
		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<User>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<User> getItem(String id) {
				return new io.wktui.nav.menu.SeparatorMenuItem<User>(id);
			}
		});  
		
		
		
		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<User>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<User> getItem(String id) {
				return new AjaxLinkMenuItem<User>(id) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						try {
							getUserDBService().markAsDeleted(getModel().getObject(), getSessionUser().get());
						} catch (Exception e) {
							logger.error(e);
							UserListPage.this.setErrorPanel(new ErrorPanel("error", e, true));
						}
						refresh(target);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("delete");
					}
				};

			}
		});

		return menu;
	}

	@Override
	protected List<ToolbarItem> getListToolbarItems() {

		if (listToolbar != null)
			return listToolbar;

		listToolbar = new ArrayList<ToolbarItem>();

		IModel<String> selected = Model.of(ObjectStateEnumSelector.ALL.getLabel(getLocale()));
		ObjectStateListSelector s = new ObjectStateListSelector("item", selected, Align.TOP_LEFT);

		listToolbar.add(s);

		return listToolbar;
	}

	@Override
	protected String getObjectTitleIcon(IModel<User> model) {
		return null;
	}
}
