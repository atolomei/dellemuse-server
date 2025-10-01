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
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.global.PageHeaderPanel;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.ObjectPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.person.PersonPage;
import dellemuse.serverapp.page.person.ServerAppConstant;
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
import io.wktui.event.SimpleAjaxWicketEvent;
import io.wktui.event.UIEvent;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.breadcrumb.Navigator;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.DropDownMenuToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.struct.list.ListPanel;
import wktui.base.DummyBlockPanel;
import wktui.base.INamedTab;
import wktui.base.NamedTab;


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

	
	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<SimpleAjaxWicketEvent>() {
			private static final long serialVersionUID = 1L;
			@Override
			public void onEvent(SimpleAjaxWicketEvent event) {
				logger.debug(event.toString());

				if (event.getName().equals(ServerAppConstant.action_site_edit)) {
					UserPage.this.onEdit(event.getTarget());
				}
			
				else if (event.getName().equals(ServerAppConstant.site_info)) {
					UserPage.this.togglePanel(ServerAppConstant.site_info, event.getTarget());
				}
			
				else if (event.getName().equals(ServerAppConstant.audit)) {
					UserPage.this.togglePanel(ServerAppConstant.audit, event.getTarget());
				}
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof SimpleAjaxWicketEvent)
					return true;
				return false;
			}
		});
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
      	JumboPageHeaderPanel<User> ph = new JumboPageHeaderPanel<User>("page-header", getModel(), 
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

		ph.setContext(getLabel("user"));

		
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

	 
	
	@Override
	protected List<ToolbarItem> getToolbarItems() {
		
		List<ToolbarItem> list = new ArrayList<ToolbarItem>();
		
		 
		
	 	DropDownMenuToolbarItem<User> menu  = new DropDownMenuToolbarItem<User>("item", getModel(), Align.TOP_RIGHT);
		menu.setLabel(getLabel("menu"));

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<User>() {
				private static final long serialVersionUID = 1L;
				@Override
				public MenuItemPanel<User> getItem(String id) {
					return new AjaxLinkMenuItem<User>(id, getModel()) {
						private static final long serialVersionUID = 1L;
						@Override
						public void onClick(AjaxRequestTarget target)  {
							fire (new io.wktui.event.SimpleAjaxWicketEvent(ServerAppConstant.site_info, target));
						}
						@Override
						public IModel<String> getLabel() {
							return getLabel("info");
						}
					};
				}
			});
				 

		 
		 menu.addItem(new io.wktui.nav.menu.MenuItemFactory<User>() {

				private static final long serialVersionUID = 1L;

				@Override
				public MenuItemPanel<User> getItem(String id) {
					return new AjaxLinkMenuItem<User>(id, getModel()) {
						private static final long serialVersionUID = 1L;
						@Override
						public void onClick(AjaxRequestTarget target)  {
							fire (new io.wktui.event.SimpleAjaxWicketEvent(ServerAppConstant.audit, target));
						}
						@Override
						public IModel<String> getLabel() {
							return getLabel("audit");
						}
					};
				}
			});
				 
		list.add(menu);
		
		
		
		return list;
	}
	
	
	
	@Override
	protected List<INamedTab> getInternalPanels() {

		List<INamedTab> tabs = new ArrayList<INamedTab>();
		
		NamedTab tab_1=new NamedTab(Model.of("editor"), ServerAppConstant.site_info) {
		 
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getEditor(panelId);
			}
		};
		tabs.add(tab_1);
	  
		NamedTab tab_2=new NamedTab(Model.of("audit"), ServerAppConstant.audit) {
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
