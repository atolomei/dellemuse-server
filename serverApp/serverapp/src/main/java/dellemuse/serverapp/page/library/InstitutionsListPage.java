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
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.institution.InstitutionPage;
import dellemuse.serverapp.page.ObjectListPage;
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
import dellemuse.serverapp.serverdb.service.InstitutionDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.error.ErrorPanel;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
 
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.toolbar.ButtonCreateToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;

@MountPath("/institution/list")
public class InstitutionsListPage extends ObjectListPage<Institution> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(InstitutionsListPage.class.getName());

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

		Set<RoleGeneral> set =user.getRolesGeneral();
		if (set==null)
			return false;
		return set.stream().anyMatch((p -> p.getKey().equals(RoleGeneral.ADMIN) || p.getKey().equals(RoleGeneral.AUDIT) ));
	}
	
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

			protected void onClick() {
				InstitutionsListPage.this.onCreate();
			}
		};
		create.setAlign(Align.TOP_LEFT);
		mainToolbar.add(create);

		return mainToolbar;
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

				return new LinkMenuItem<Institution>(id) {

					private static final long serialVersionUID = 1L;

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
		return menu;
	}
	
	@Override
	public Iterable<Institution> getObjects(ObjectState os1) {
		return getObjects(os1, null);
	}	

	@Override
	public Iterable<Institution> getObjects(ObjectState os1, ObjectState os2) {

		InstitutionDBService service = (InstitutionDBService) ServiceLocator.getInstance().getBean(InstitutionDBService.class);

		if (os1==null && os2==null)
			return service.findAllSorted();
	
		if (os2==null)
			return service.findAllSorted(os1);

		if (os1==null)
			return service.findAllSorted(os2);
		
		return service.findAllSorted(os1, os2);
	}

	@Override
	public Iterable<Institution> getObjects() {
		InstitutionDBService service = (InstitutionDBService) ServiceLocator.getInstance().getBean(InstitutionDBService.class);
	 
		return service.findAllSorted();
	}

	@Override
	public IModel<String> getObjectInfo(IModel<Institution> model) {
		return new Model<String>(TextCleaner.clean(model.getObject().getInfo(), 280));
	}
	
/**
	@Override
	public IModel<String> getObjectTitle(IModel<Institution> model) {
		
		if (model.getObject().getState()==ObjectState.DELETED) 
			return new Model<String>(model.getObject().getDisplayname() + ServerConstant.DELETED_ICON);
	
		return new Model<String>(model.getObject().getDisplayname());
	}
**/
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
	protected String getObjectTitleIcon(IModel<Institution> model) {
		// TODO Auto-generated method stub
		return null;
	}

}
