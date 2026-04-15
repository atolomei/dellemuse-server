package dellemuse.serverapp.page.library;

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
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.help.Help;
import dellemuse.serverapp.help.HelpButtonToolbarItem;
import dellemuse.serverapp.icons.Icons;
import dellemuse.serverapp.institution.InstitutionPage;
import dellemuse.serverapp.page.ObjectListPage;
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
import dellemuse.serverapp.serverdb.model.security.RoleSite;
import dellemuse.serverapp.serverdb.service.InstitutionDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.error.ErrorPanel;
import io.wktui.model.TextCleaner;
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

@MountPath("/institution/list")
@AuthorizeInstantiation({"ROLE_USER"})
public class InstitutionsListPage extends ObjectListPage<Institution> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(InstitutionsListPage.class.getName());

	private List<ToolbarItem> mainToolbar;
	private List<ToolbarItem> listToolbar;

	
	
	public InstitutionsListPage() {
		super();
		super.setIsExpanded(true);
	}

	public InstitutionsListPage(PageParameters parameters) {
		super(parameters);
		super.setIsExpanded(true);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		User user = getSessionUser().orElse(null);
		if (user != null && !user.isDependencies()) {
			user = getUserDBService().findWithDeps(user.getId()).orElse(null);
			setSessionUser(user);
		}
	}

 
	@Override
	public String getHelpKey() {
		return Help.INSTITUTION_LIST;
	}
	
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

		Set<RoleGeneral> set =user.getRolesGeneral();

		if (set==null)
			return false;
		
		if (set.stream().anyMatch((p -> p.getKey().equals(RoleGeneral.ADMIN) || p.getKey().equals(RoleGeneral.AUDIT) )))
			return true;
		
		if (!user.getRolesInstitution().isEmpty())
			return true;
		
		return false;
		
					
		
	}
	
	
	
	@Override
	public boolean canRead(Institution in) {
		
		Optional<User> ouser = getSessionUser();
		
		if (ouser.isEmpty())
			return false;
		
		User user = ouser.get();  
		
		if (user.isRoot()) 
			return true;
		
		if (isGeneralAdminOrAudit())
			return true;

	 	
		if (isInstitutionAdminOrAudit(in))
			return true;
		
		return false;
	}

	
	
	@Override
	public boolean canWrite(Institution in) {
		
		Optional<User> ouser = getSessionUser();
		
		if (ouser.isEmpty())
			return false;
		
		User user = ouser.get();  
		
		if (user.isRoot()) 
			return true;
		
		if (!user.isDependencies()) {
			user = getUserDBService().findWithDeps(user.getId()).get();
		}

		Set<RoleGeneral> set =user.getRolesGeneral();
		if (set==null)
			return false;
		
		return set.stream().anyMatch((p -> p.getKey().equals(RoleGeneral.ADMIN) ));
		
	}
	
	@Override
	public boolean canCreate() {
		
		Optional<User> ouser = getSessionUser();
		
		if (ouser.isEmpty())
			return false;
		
		User user = ouser.get();  
		
		if (user.isRoot()) 
			return true;
		
		if (!user.isDependencies()) {
			user = getUserDBService().findWithDeps(user.getId()).get();
		}

		Set<RoleGeneral> set =user.getRolesGeneral();
		if (set==null)
			return false;
		
		return set.stream().anyMatch((p -> p.getKey().equals(RoleGeneral.ADMIN)));
	}
	
	
	
	protected void onCreate() {

		try {
			Institution in = getInstitutionDBService().create("new", getUserDBService().findRoot());
			setResponsePage(new InstitutionPage(new ObjectModel<Institution>(in), getList()));

		} catch (Exception e) {
			logger.error(e);
			setResponsePage(new ErrorPage(e));

		}
	}

	@Override
	protected List<ToolbarItem> getMainToolbarItems() {

		if (mainToolbar != null)
			return mainToolbar;

		mainToolbar = new ArrayList<ToolbarItem>();

		ButtonCreateToolbarItem<Void> create = new ButtonCreateToolbarItem<Void>("item") {
			private static final long serialVersionUID = 1L;

			public boolean isEnabled() {
				return canCreate();
			}

			public boolean isVisible() {
				return canCreate();
			}
			
			protected void onClick() {
				InstitutionsListPage.this.onCreate();
			}
		};
		create.setAlign(Align.TOP_LEFT);
		mainToolbar.add(create);

		mainToolbar.add(new HelpButtonToolbarItem("item",  Align.TOP_RIGHT));
		
		return mainToolbar;
	}
	

	@Override
	protected List<ToolbarItem> getListToolbarItems() {

		if (listToolbar != null)
			return listToolbar;

		listToolbar = new ArrayList<ToolbarItem>();

		IModel<String> selected = Model.of(getObjectStateEnumSelector().getLabel(getLocale()));
		ObjectStateListSelector s = new ObjectStateListSelector("item", selected, Align.TOP_LEFT);
		listToolbar.add(s);

		return listToolbar;
	}

	

	@Override
	protected WebMarkupContainer getObjectMenu(IModel<Institution> model) {

		NavDropDownMenu<Institution> menu = new NavDropDownMenu<Institution>("menu", model, null);

		menu.setOutputMarkupId(true);
		menu.setTitleCss("d-block-inline d-sm-block-inline d-md-block-inline d-lg-none d-xl-none d-xxl-none ps-1 pe-1");
		menu.setIconCss("fa-solid fa-ellipsis d-block-inline d-sm-block-inline d-md-block-inline d-lg-block-inline d-xl-block-inline d-xxl-block-inline ps-1 pe-1");

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Institution>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Institution> getItem(String id) {

				return new LinkMenuItem<Institution>(id, model) {

					private static final long serialVersionUID = 1L;

					public boolean isEnabled() {
						return canRead(model.getObject());
					}

					public boolean isVisible() {
						return canRead(model.getObject());
					}
					
					
					@Override
					public void onClick () {
						setResponsePage( new InstitutionPage( getModel() ));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("open");
					}
				};
			}
		});
		
		
		
	
		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Institution>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Institution> getItem(String id) {

				return new AjaxLinkMenuItem<Institution>(id, model) {

					private static final long serialVersionUID = 1L;

					
					public boolean isVisible() {
						return getModel().getObject().getState()!=ObjectState.PUBLISHED;
					}
				
					@Override
					public void onClick(AjaxRequestTarget target) {
						getModel().getObject().setState(ObjectState.PUBLISHED);
						getInstitutionDBService().save(getModel().getObject(), ObjectState.PUBLISHED.getLabel(), getSessionUser().get());
						refresh(target);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("publish");
					}
				};
			}
		});
		

		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Institution>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Institution> getItem(String id) {

				return new AjaxLinkMenuItem<Institution>(id, model) {

					private static final long serialVersionUID = 1L;

					
					public boolean isVisible() {
						return getModel().getObject().getState()!=ObjectState.EDITION;
					}
				
					@Override
					public void onClick(AjaxRequestTarget target) {
						getModel().getObject().setState(ObjectState.EDITION);
						getInstitutionDBService().save(getModel().getObject(), ObjectState.EDITION.getLabel(), getSessionUser().get());
						refresh(target);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("edit-mode");
					}
				};
			}
		});
		
		

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Institution>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Institution> getItem(String id) {

				return new AjaxLinkMenuItem<Institution>(id, model) {

					private static final long serialVersionUID = 1L;

					
					public boolean isVisible() {
						return getModel().getObject().getState()!=ObjectState.DELETED;
					}
				
					@Override
					public void onClick(AjaxRequestTarget target) {
						getModel().getObject().setState(ObjectState.DELETED);
						getInstitutionDBService().save(getModel().getObject(), ObjectState.DELETED.getLabel(), getSessionUser().get());
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
	public Iterable<Institution> getObjects(ObjectState os1) {
		return getObjects(os1, null);
	}	

	@Override
	public Iterable<Institution> getObjects(ObjectState os1, ObjectState os2) {

		
		if (isRoot() || isGeneralAdminOrAudit()) {
		
			InstitutionDBService service = (InstitutionDBService) ServiceLocator.getInstance().getBean(InstitutionDBService.class);
	
			if (os1==null && os2==null)
				return service.findAllSorted();
		
			if (os2==null)
				return service.findAllSorted(os1);
	
			if (os1==null)
				return service.findAllSorted(os2);
			
			return service.findAllSorted(os1, os2);
		}
		else {
			List<Institution> list = new ArrayList<Institution>();

			if (!getSessionUser().get().isDependencies())
				setSessionUser(getUserDBService().findWithDeps(getSessionUser().get().getId()).get());
			
			getSessionUser().get().getRolesInstitution().stream().forEach(r -> {
			
				if (os1==null && os2==null) {
					list.add(r.getInstitution());
				}
				else if (os1!=null && r.getState()==os1)
					list.add(r.getInstitution());
				else if (os2!=null && r.getState()==os2)
					list.add(r.getInstitution());
			});
			return list;
		}
	}

	@Override
	public Iterable<Institution> getObjects() {
		if (isRoot() || isGeneralAdminOrAudit()) {
			
			InstitutionDBService service = (InstitutionDBService) ServiceLocator.getInstance().getBean(InstitutionDBService.class);
			return service.findAllSorted();
		}
		else {
			
			List<Institution> list = new ArrayList<Institution>();
			getSessionUser().get().getRolesInstitution().stream().forEach(r -> {
				list.add(r.getInstitution());
			});
			return list;
			
		}
	
	}

	@Override
	public IModel<String> getObjectInfo(IModel<Institution> model) {
		return new Model<String>(TextCleaner.clean(model.getObject().getInfo(), 280));
	}
 
	@Override
	public void onClick(IModel<Institution> model) {
		setResponsePage(new InstitutionPage(model, getList()));
	}

	@Override
	public IModel<String> getPageTitle() {
		return getLabel("institutions");
	}

	@Override
	public IModel<String> getListPanelLabel() {
		return null;
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	
	protected IModel<String> getObjectTitle(IModel<Institution> model) {
	 
			StringBuilder str = new StringBuilder();
			str.append(model.getObject().getName());
			
			if (model.getObject().getState() == ObjectState.DELETED)
				str.append(Icons.DELETED_ICON_HTML);
	
			if (model.getObject().getState() == ObjectState.EDITION)
				str.append(Icons.EDITION_ICON_HTML);
			
			return Model.of(str.toString());
		}
	
	
	@Override
	protected String getObjectImageSrc(IModel<Institution> model) {
		if (model.getObject().getPhoto() != null) {
			Resource photo = getResource(model.getObject().getPhoto().getId()).get();
			return getPresignedThumbnailSmall(photo);
		}
		return null;
	}

	@Override
	protected void addHeaderPanel() {
		try {
			BreadCrumb<Void> bc = createBreadCrumb();
			bc.addElement(new BCElement(getLabel("institutions")));
			JumboPageHeaderPanel<Void> ph = new JumboPageHeaderPanel<Void>("page-header", null, getLabel("institutions"));
			ph.setBreadCrumb(bc);
			ph.setIcon(Institution.getIcon()  );
			ph.setHeaderCss("mb-2 pb-2 border-none");
			add(ph);
		} catch (Exception e) {
			logger.error(e);
			addOrReplace(new ErrorPanel("page-header", e));
		}
	}

	@Override
	protected Panel getObjectListItemExpandedPanel(IModel<Institution> model, ListPanelMode mode) {
		model.setObject(getInstitutionDBService().findWithDeps(model.getObject().getId()).get());
		return new InstitutionExpandedPanel("expanded-panel", model);
	}

	@Override
	protected String getObjectTitleIcon(IModel<Institution> model) {
		// TODO Auto-generated method stub
		return null;
	}

}
