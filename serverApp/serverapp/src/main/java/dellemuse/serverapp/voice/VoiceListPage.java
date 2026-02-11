package dellemuse.serverapp.voice;

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
import dellemuse.serverapp.serverdb.model.Voice;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
import dellemuse.serverapp.serverdb.service.VoiceDBService;
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

@MountPath("/voice/list")
public class VoiceListPage extends ObjectListPage<Voice> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(VoiceListPage.class.getName());

	private List<ToolbarItem> mainToolbar;
	private List<ToolbarItem> listToolbar;

	
	
	public VoiceListPage() {
		super();
		super.setIsExpanded(true);
	}

	public VoiceListPage(PageParameters parameters) {
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
			Voice in = getVoiceDBService().create("new", getUserDBService().findRoot());
			setResponsePage(new  VoicePage(new ObjectModel<Voice>(in), getList()));

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
				VoiceListPage.this.onCreate();
			}
		};
		create.setAlign(Align.TOP_LEFT);
		mainToolbar.add(create);

		return mainToolbar;
	}

	@Override
	protected WebMarkupContainer getObjectMenu(IModel<Voice> model) {

		NavDropDownMenu<Voice> menu = new NavDropDownMenu<Voice>("menu", model, null);

		menu.setOutputMarkupId(true);
		menu.setTitleCss("d-block-inline d-sm-block-inline d-md-block-inline d-lg-none d-xl-none d-xxl-none ps-1 pe-1");
		menu.setIconCss("fa-solid fa-ellipsis d-block-inline d-sm-block-inline d-md-block-inline d-lg-block-inline d-xl-block-inline d-xxl-block-inline ps-1 pe-1");

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Voice>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Voice> getItem(String id) {

				return new LinkMenuItem<Voice>(id) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick () {
						//setResponsePage( new ElevenLabsVoicePage( getModel() ));
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
	public Iterable<Voice> getObjects(ObjectState os1) {
		return getObjects(os1, null);
	}	

	@Override
	public Iterable<Voice> getObjects(ObjectState os1, ObjectState os2) {

		VoiceDBService service = (VoiceDBService ) ServiceLocator.getInstance().getBean(VoiceDBService .class);

		if (os1==null && os2==null)
			return service.findAllSorted();
	
		if (os2==null)
			return service.findAllSorted(os1);

		if (os1==null)
			return service.findAllSorted(os2);
		
		return service.findAllSorted(os1, os2);
	}

	@Override
	public Iterable<Voice> getObjects() {
		VoiceDBService  service = (VoiceDBService ) ServiceLocator.getInstance().getBean(VoiceDBService .class);
	 
		return service.findAllSorted();
	}

	@Override
	public IModel<String> getObjectInfo(IModel<Voice> model) {
		return new Model<String>(TextCleaner.clean(model.getObject().getInfo(), 280));
	}
	
 
	@Override
	public IModel<String> getObjectTitle(IModel<Voice> model) {
		
		StringBuilder str = new StringBuilder();

		str.append(model.getObject().getDisplayname());

		//String s= Language.of(model.getObject().getLanguage()).getLabel(getLocale());
		
		str.append(" ( " + model.getObject().getLanguage() + " - " + model.getObject().getLanguageRegion()+" ) ");
		
		if (model.getObject().getState()==ObjectState.DELETED) 
			str.append(model.getObject().getDisplayname() + Icons.DELETED_ICON);
		
		if (model.getObject().getState() == ObjectState.EDITION)
			str.append(Icons.EDITION_ICON);
		
	
		
		
		
		return Model.of( str.toString());
	
	
	}
 
	@Override
	public void onClick(IModel<Voice> model) {
		// setResponsePage(new ElevenLabsVoicePage(model, getList()));
	}

	@Override
	public IModel<String> getPageTitle() {
		return getLabel("voices");
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
			bc.addElement(new BCElement(getLabel("voices")));
			JumboPageHeaderPanel<Void> ph = new JumboPageHeaderPanel<Void>("page-header", null, getLabel("voices"));
			ph.setBreadCrumb(bc);
			ph.setIcon(Voice.getIcon()  );
			ph.setHeaderCss("mb-2 pb-2 border-none");
			add(ph);
		} catch (Exception e) {
			logger.error(e);
			addOrReplace(new ErrorPanel("page-header", e));
		}
	}

	@Override
	protected String getObjectTitleIcon(IModel<Voice> model) {
		// TODO Auto-generated method stub
		return null;
	}

}
