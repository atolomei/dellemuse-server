package dellemuse.serverapp.music;

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
import dellemuse.serverapp.icons.Icons;
import dellemuse.serverapp.page.ObjectListPage;
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.library.ObjectStateEnumSelector;
import dellemuse.serverapp.page.library.ObjectStateListSelector;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.Music;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
import dellemuse.serverapp.serverdb.service.MusicDBService;
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

@MountPath("/music/list")
public class MusicListPage extends ObjectListPage<Music> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(MusicListPage.class.getName());

	private List<ToolbarItem> mainToolbar;
	private List<ToolbarItem> listToolbar;

	
	
	public MusicListPage() {
		super();
		super.setIsExpanded(true);
	}

	public MusicListPage(PageParameters parameters) {
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
		return set.stream().anyMatch((p -> p.getKey().equals(RoleGeneral.ADMIN) || p.getKey().equals(RoleGeneral.AUDIT) ));
	}
	
	protected void onCreate() {

		try {
			Music in = getMusicDBService().create("new", getUserDBService().findRoot());
			setResponsePage(new  MusicPage(new ObjectModel<Music>(in), getList()));

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
				MusicListPage.this.onCreate();
			}
		};
		create.setAlign(Align.TOP_LEFT);
		mainToolbar.add(create);

		return mainToolbar;
	}

	@Override
	protected WebMarkupContainer getObjectMenu(IModel<Music> model) {

		NavDropDownMenu<Music> menu = new NavDropDownMenu<Music>("menu", model, null);

		menu.setOutputMarkupId(true);
		menu.setTitleCss("d-block-inline d-sm-block-inline d-md-block-inline d-lg-none d-xl-none d-xxl-none ps-1 pe-1");
		menu.setIconCss("fa-solid fa-ellipsis d-block-inline d-sm-block-inline d-md-block-inline d-lg-block-inline d-xl-block-inline d-xxl-block-inline ps-1 pe-1");

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Music>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Music> getItem(String id) {

				return new LinkMenuItem<Music>(id) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick () {
						setResponsePage( new MusicPage( getModel(), getList() ));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("open");
					}
				};
			}
		});
		
		
		
		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Music>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Music> getItem(String id) {

				return new AjaxLinkMenuItem<Music>(id) {

					private static final long serialVersionUID = 1L;

					
					public boolean isEnabled() {
						return getModel().getObject().getState()!=ObjectState.PUBLISHED;
					}
				
					@Override
					public void onClick(AjaxRequestTarget target) {
						getModel().getObject().setState(ObjectState.PUBLISHED);
						getMusicDBService().save(getModel().getObject());
						MusicListPage.this.refresh(target);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("publish");
					}
				};
			}
		});
		
		
		/**
		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Music>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Music> getItem(String id) {
				return new io.wktui.nav.menu.SeparatorMenuItem<Music>(id) {
					private static final long serialVersionUID = 1L;
				};
			}
		});
		
		
		*/
		
		
		
		
		
		
		
		return menu;
	}
	
	@Override
	public Iterable<Music> getObjects(ObjectState os1) {
		return getObjects(os1, null);
	}	

	@Override
	public Iterable<Music> getObjects(ObjectState os1, ObjectState os2) {

		MusicDBService service = (MusicDBService ) ServiceLocator.getInstance().getBean(MusicDBService .class);

		if (os1==null && os2==null)
			return service.findAllSorted();
	
		if (os2==null)
			return service.findAllSorted(os1);

		if (os1==null)
			return service.findAllSorted(os2);
		
		return service.findAllSorted(os1, os2);
	}

	@Override
	public Iterable<Music> getObjects() {
		MusicDBService  service = (MusicDBService ) ServiceLocator.getInstance().getBean(MusicDBService .class);
	 
		return service.findAllSorted();
	}

	@Override
	public IModel<String> getObjectInfo(IModel<Music> model) {
		return new Model<String>(TextCleaner.clean(model.getObject().getInfo(), 280));
	}
	
 
	@Override
	public IModel<String> getObjectTitle(IModel<Music> model) {
		
		StringBuilder str = new StringBuilder();

		str.append(model.getObject().getDisplayname());


 		/**
		if (model.getObject().getSex()!=null)
			str.append(" - " + model.getObject().getSex());

		str.append(" ( " + model.getObject().getLanguage() + " - " + model.getObject().getLanguageRegion()+" ) ");
		**/
		
		if (model.getObject().getState()==ObjectState.DELETED) 
			str.append(model.getObject().getDisplayname() + Icons.DELETED_ICON);
		
		if (model.getObject().getState() == ObjectState.EDITION)
			str.append(Icons.EDITION_ICON);
		
	
		
		
		
		return Model.of( str.toString());
	
	
	}
 
	@Override
	public void onClick(IModel<Music> model) {
		 setResponsePage(new MusicPage(model, getList()));
	}

	@Override
	public IModel<String> getPageTitle() {
		return getLabel("music");
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
	protected void addHeaderPanel() {
		try {
			BreadCrumb<Void> bc = createBreadCrumb();
			bc.addElement(new BCElement(getLabel("music")));
			JumboPageHeaderPanel<Void> ph = new JumboPageHeaderPanel<Void>("page-header", null, getLabel("music"));
			ph.setBreadCrumb(bc);
			ph.setIcon(Music.getIcon()  );
			ph.setHeaderCss("mb-2 pb-2 border-none");
			add(ph);
		} catch (Exception e) {
			logger.error(e);
			addOrReplace(new ErrorPanel("page-header", e));
		}
	}

	@Override
	protected String getObjectTitleIcon(IModel<Music> model) {
		// TODO Auto-generated method stub
		return null;
	}

}
