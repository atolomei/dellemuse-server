package dellemuse.serverapp.page.library;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.wicketstuff.annotation.mount.MountPath;

 
import dellemuse.model.logging.Logger;
 
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.global.PageHeaderPanel;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.ObjectListPage;
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.site.SitePage;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
import dellemuse.serverapp.serverdb.service.ArtWorkDBService;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.model.TextCleaner;
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
 * site foto Info - exhibitions
 * 
 */

@MountPath("/site/list")
public class SiteListPage extends ObjectListPage<Site> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteListPage.class.getName());

	
	private List<ToolbarItem> mainToolbar;
	private List<ToolbarItem> listToolbar;
	
	
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
	
	public SiteListPage() {
		super();
		super.setIsExpanded(true);
	}

	public SiteListPage(PageParameters parameters) {
		super(parameters);
		super.setIsExpanded(true);
	}

	@Override
	public Iterable<Site> getObjects() {
		SiteDBService service = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
		return service.findAllSorted();
	}

	
	@Override
	public Iterable<Site> getObjects(ObjectState os1) {
		 return this.getObjects(os1, null);
	}

	
	@Override
	public Iterable<Site> getObjects(ObjectState os1, ObjectState os2) {

		SiteDBService service = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);

		if (os1==null && os2==null)
			return service.findAllSorted();
	
		if (os2==null)
			return service.findAllSorted(os1);

		if (os1==null)
			return service.findAllSorted(os2);
		
		return service.findAllSorted(os1, os2);
	}

	
	
	
	
	
	
	
	@Override
	public IModel<String> getObjectInfo(IModel<Site> model) {
		return new Model<String>(TextCleaner.clean(model.getObject().getInfo(), 280));
	}

	/**
	@Override
	public IModel<String> getObjectTitle(IModel<Site> model) {
		
		if (model.getObject().getState()==ObjectState.DELETED) 
			return new Model<String>(model.getObject().getDisplayname() + ServerConstant.DELETED_ICON);

		return new Model<String>(model.getObject().getDisplayname());
	}
	**/

	@Override
	public void onClick(IModel<Site> model) {
		setResponsePage(new SitePage(model, getList()));
	}

	@Override
	public IModel<String> getPageTitle() {
		return getLabel("sites");
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
	public void onInitialize() {
		super.onInitialize();

	}

	@Override
	protected List<ToolbarItem> getListToolbarItems() {
		
		if (listToolbar!=null)
			return listToolbar;
		
		listToolbar = new ArrayList<ToolbarItem>();
	
		IModel<String> selected = Model.of(getObjectStateEnumSelector().getLabel(getLocale()));
		ObjectStateListSelector s = new ObjectStateListSelector("item",  selected, Align.TOP_LEFT);
		
		listToolbar.add(s);
		
		return listToolbar;
	}

	protected WebMarkupContainer getSubmenu() {
		return null;
	}

	
	protected void onCreate() {
		
		try {
			Site in = getSiteDBService().create("new", getUserDBService().findRoot());
			IModel<Site> m = new ObjectModel<Site>(in);
			getList().add(m);
			setResponsePage(new SitePage(m, getList()));
		} catch (Exception e) {
			logger.error(e);
			setResponsePage(new ErrorPage(e));

		}

	}

	protected void addHeaderPanel() {

		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(new BCElement(getLabel("sites")));
		JumboPageHeaderPanel<Void> ph = new JumboPageHeaderPanel<Void>("page-header", null, getLabel("sites"));
		ph.setBreadCrumb(bc);
		ph.setIcon(Site.getIcon()  );
		ph.setHeaderCss("mb-4 pb-2 border-none");

		add(ph);
	}

	
	
	@Override
	protected List<ToolbarItem> getMainToolbarItems() {

		if (mainToolbar!=null)
			return mainToolbar;
		
		mainToolbar = new ArrayList<ToolbarItem>();
		return mainToolbar;

	}

	@Override
	protected ListPanelMode getListPanelMode() {
		return ListPanelMode.TITLE_TEXT_IMAGE;
	}

	@Override
	protected String getObjectImageSrc(IModel<Site> model) {
		if (model.getObject().getPhoto() != null) {
			Resource photo = getResource(model.getObject().getPhoto().getId()).get();
			return getPresignedThumbnailSmall(photo);
		}
		return null;
	}

	@Override
	protected WebMarkupContainer getObjectMenu(IModel<Site> model) {

		NavDropDownMenu<Site> menu = new NavDropDownMenu<Site>("menu", model, null);

		menu.setOutputMarkupId(true);

		menu.setTitleCss
("d-block-inline d-sm-block-inline d-md-block-inline d-lg-none d-xl-none d-xxl-none ps-1 pe-1");
		menu.setIconCss("fa-solid fa-ellipsis d-block-inline d-sm-block-inline d-md-block-inline d-lg-block-inline d-xl-block-inline d-xxl-block-inline ps-1 pe-1");

		
	 

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Site>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Site> getItem(String id) {

				return new LinkMenuItem<Site>(id) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick () {
						setResponsePage( new SitePage( getModel(), getList() ));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("open");
					}
				};
			}
		});
		
		
		/**
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Site>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Site> getItem(String id) {

				return new AjaxLinkMenuItem<Site>(id) {

					private static final long serialVersionUID = 1L;

					
					public boolean isVisible() {
						return getModel().getObject().getState()!=ObjectState.PUBLISHED;
					}
				
					@Override
					public void onClick(AjaxRequestTarget target) {
						getModel().getObject().setState(ObjectState.PUBLISHED);
						getSiteDBService().save(getModel().getObject(), ObjectState.PUBLISHED.getLabel(), getSessionUser().get());
						refresh(target);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("publish");
					}
				};
			}
		});
		
		
		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Site>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Site> getItem(String id) {

				return new AjaxLinkMenuItem<Site>(id) {

					private static final long serialVersionUID = 1L;

					
					public boolean isVisible() {
						return getModel().getObject().getState()!=ObjectState.EDITION;
					}
				
					@Override
					public void onClick(AjaxRequestTarget target) {
						getModel().getObject().setState(ObjectState.EDITION);
						getSiteDBService().save(getModel().getObject(), ObjectState.EDITION.getLabel(), getSessionUser().get());
						refresh(target);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("edit-mode");
					}
				};
			}
		});
		
		*/
		
		
		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Site>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Site> getItem(String id) {
				return new io.wktui.nav.menu.SeparatorMenuItem<Site>(id) {
					private static final long serialVersionUID = 1L;
				};
			}
		});
		
		 
		
		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Site>() {

			private static final long serialVersionUID = 1L;

			
			@Override
			public MenuItemPanel<Site> getItem(String id) {

				return new AjaxLinkMenuItem<Site>(id) {

					private static final long serialVersionUID = 1L;

					public boolean isEnabled() {
						return getModel().getObject().getState()!=ObjectState.DELETED;
					}
					
					
					@Override
					public void onClick(AjaxRequestTarget target) {
						getModel().getObject().setState(ObjectState.DELETED);
						getSiteDBService().save(getModel().getObject(), ObjectState.EDITION.getLabel(), getSessionUser().get());
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
	protected String getObjectTitleIcon(IModel<Site> model) {
		// TODO Auto-generated method stub
		return null;
	}

}
