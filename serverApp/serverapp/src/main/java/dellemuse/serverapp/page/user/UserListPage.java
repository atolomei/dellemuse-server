package dellemuse.serverapp.page.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Site;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.wicketstuff.annotation.mount.MountPath;

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;

import dellemuse.model.ArtExhibitionGuideModel;
import dellemuse.model.ArtExhibitionModel;
import dellemuse.model.GuideContentModel;
import dellemuse.model.SiteModel;
import dellemuse.model.logging.Logger;
import dellemuse.serverapp.global.GlobalFooterPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.global.PageHeaderPanel;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.ObjectListPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.PersonDBService;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.UserDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.toolbar.ButtonCreateToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;


/**
 * site 
 * foto 
 * Info - exhibitions
 */

@MountPath("/user/list")
public class UserListPage extends ObjectListPage<User> {

	private static final long serialVersionUID = 1L;
	
	static private Logger logger = Logger.getLogger(UserListPage.class.getName());

	protected  WebMarkupContainer getSubmenu() {
		return null;
	}

	
	protected IModel<String> getTitleLabel() {
		return null;
	}
	  
	public  UserListPage() {
		super();
		 setCreate(true);
	}		
	
	public  UserListPage(PageParameters parameters) {
		 super(parameters);
		 setCreate(true);
	}
	
	@Override
	protected void onCreate() {
			User in = getUserDBService().create("new", getUserDBService().findRoot());
			IModel<User> m =  new ObjectModel<User>(in);
			getList().add(m);
			//setResponsePage( new UserPage(m,getList()));
	}

	@Override
	protected void addHeaderPanel() {
		 BreadCrumb<Void> bc = createBreadCrumb();
	        bc.addElement(new BCElement( getLabel("users")));
	        
	    	JumboPageHeaderPanel<Void> ph = new JumboPageHeaderPanel<Void>("page-header", null, getLabel("users"));
			ph.setBreadCrumb(bc);
			add(ph);
	}
	
	@Override
	public void onInitialize() {
		super.onInitialize();
	}
    
	@Override
	public IRequestablePage getObjectPage(IModel<User> model) {
		return new UserPage(model);
	}
	
	@Override
	public Iterable<User> getObjects() {
		return super.getUsers();
	}

	@Override
	public IModel<String> getObjectInfo(IModel<User> model) {
		return new Model<String>(model.getObject().getDisplayname());
	}

	@Override
	public IModel<String> getObjectTitle(IModel<User> model) {
		return new Model<String>(model.getObject().getDisplayname());
	}

	@Override
	public void onClick(IModel<User> model) {
		setResponsePage(new UserPage(model, getList()));
	}
	
	@Override
	protected List<ToolbarItem> getToolbarItems() {
	
		List<ToolbarItem> list = new ArrayList<ToolbarItem>();

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
	public IModel<String> getListPanelLabel() {
		return null;
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public IModel<String> getPageTitle() {
		return null;
	}

}
