package dellemuse.serverapp.page.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
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
import dellemuse.model.ArtWorkModel;
import dellemuse.model.GuideContentModel;
import dellemuse.model.ResourceModel;
import dellemuse.model.SiteModel;
import dellemuse.model.logging.Logger;
import dellemuse.model.util.ThumbnailSize;
import dellemuse.serverapp.global.GlobalFooterPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.global.PageHeaderPanel;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.ObjectPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.person.PersonPage;
import dellemuse.serverapp.page.site.ArtWorkEditor;
import dellemuse.serverapp.page.site.InstitutionPage;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.ArtWorkDBService;
import dellemuse.serverapp.serverdb.service.ResourceDBService;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.ResourceThumbnailService;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.breadcrumb.Navigator;
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.struct.list.ListPanel;
import wktui.base.DummyBlockPanel;


/**
 * 
 * 
 */

@MountPath("/user/${id}")
public class UserPage extends ObjectPage<User> {

	private static final long serialVersionUID = 1L;
	
	static private Logger logger = Logger.getLogger(UserPage.class.getName());

	private UserEditor editor;
	
	public UserPage() {
		super();
	}		
	
	
	public  UserPage(PageParameters parameters) {
		 super(parameters);
	 }
	 	
	public UserPage(IModel<User> model) {
		super(model);
	}

	public UserPage(IModel<User> model, List<IModel<User>> list) {
		super( model, list );
	}

	
	/**
	 * 
	
	 * Institution
	 * Site
	 * Artwork
	 * LIST(S)  Exhibition
	 * LIST(ex) ExhibitionItem
	 * Guide(S)
	 * GuideContent(G)
	 *
	 * Person
	 * Roles
	 * User
	 * 
	 */
	@Override
	public void onInitialize() {
		super.onInitialize();
	}

	@Override
	protected Optional<User> getObject(Long id) {
		return getUser( id );
	}

	protected Panel getEditor(String id) {

		if (editor==null)
			editor = new UserEditor(id, getModel());
		return (editor);
	}

	@Override
	protected IModel<String> getPageTitle() {
		return new Model<String> (getModel().getObject().getName());
	}


	@Override
	protected void addHeaderPanel() {
		BreadCrumb<Void> bc = createBreadCrumb();
        bc.addElement(new HREFBCElement( "/user/list", getLabel("users")));
        bc.addElement(new BCElement( new Model<String>( getModel().getObject().getUsername())));
      	PageHeaderPanel<User> ph = new PageHeaderPanel<User>("page-header", getModel(), 
    			new Model<String>(getModel().getObject().getDisplayname() ));
		ph.setBreadCrumb(bc);
		
		
		if (getList()!=null && getList().size()>0) {
			Navigator<User> nav = new Navigator<User>("navigator", getCurrent(), getList()) {
				private static final long serialVersionUID = 1L;
				@Override
				public void navigate(int current) {
					setResponsePage( new UserPage( getList().get(current), getList() ));
				}
			};
			bc.setNavigator(nav);
		}

		
		
		add(ph);
	}


	@Override
	protected IRequestablePage getObjectPage(IModel<User> model, List<IModel<User>> list) {
	 	return new UserPage( model, list );
	}


	 

	@Override
	protected void onEdit(AjaxRequestTarget target) {
		// TODO Auto-generated method stub
		
	}

	static final int PANEL_EDITOR = 0;
	static final int PANEL_AUDIT = 1;
	
	@Override
	protected List<ToolbarItem> getToolbarItems() {
		
		List<ToolbarItem> list = new ArrayList<ToolbarItem>();
		
		AjaxButtonToolbarItem<User> create = new AjaxButtonToolbarItem<User>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
				UserPage.this.togglePanel(PANEL_EDITOR, target);
				UserPage.this.onEdit(target);
			}
		};
		create.setAlign(Align.TOP_LEFT);

			

		AjaxButtonToolbarItem<Person> audit = new AjaxButtonToolbarItem<Person>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
				UserPage.this.togglePanel(PANEL_AUDIT, target);
			}
			@Override
			public IModel<String> getButtonLabel() {
				return getLabel("audit");
			}
		};
		audit.setAlign(Align.TOP_RIGHT);
		list.add(audit);
		
		
		
		
		return list;
	}
	
	
	
	@Override
	protected List<ITab> getInternalPanels() {
		
		List<ITab> tabs = new ArrayList<ITab>();
		
		AbstractTab tab_1=new AbstractTab(Model.of("editor")) {
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getEditor(panelId);
			}
		};
		tabs.add(tab_1);
		
		AbstractTab tab_2=new AbstractTab(Model.of("audit")) {
			private static final long serialVersionUID = 1L;
			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return new DummyBlockPanel(panelId,getLabel("audit"));
			}
		};
		tabs.add(tab_2);
	
		return tabs;
	}

	
	
	
	
	
	
	
}
