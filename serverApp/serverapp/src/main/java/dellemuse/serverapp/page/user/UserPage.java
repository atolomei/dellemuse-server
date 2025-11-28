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

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.audit.panel.AuditPanel;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
 
import dellemuse.serverapp.page.ObjectPage;
 
import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
 
import io.wktui.event.MenuAjaxEvent;
import io.wktui.event.SimpleAjaxWicketEvent;
import io.wktui.event.UIEvent;
 
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.breadcrumb.Navigator;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
 
import io.wktui.nav.toolbar.DropDownMenuToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
 
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
	private List<ToolbarItem> x_list = null;
	
	
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
	
	
	@Override
	protected List<INamedTab> createInternalPanels() {
		
		List<INamedTab> list = super.createInternalPanels();
		
		NamedTab audit=new NamedTab(Model.of("audit"), ServerAppConstant.object_audit) {
			 
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getAuditPanel(panelId);
			}
		};
		
		list.add(audit);
		
		return list;
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

				if (event.getName().equals(ServerAppConstant.action_user_edit_info)) {
					UserPage.this.onEdit(event.getTarget());
				}
				else if (event.getName().equals(ServerAppConstant.user_info)) {
					UserPage.this.togglePanel(ServerAppConstant.user_info, event.getTarget());
				}
				else if (event.getName().equals(ServerAppConstant.object_meta)) {
					UserPage.this.togglePanel(ServerAppConstant.object_meta, event.getTarget());
				}
				else if (event.getName().equals(ServerAppConstant.object_audit)) {
					UserPage.this.togglePanel(ServerAppConstant.object_audit, event.getTarget());
				}
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof MenuAjaxEvent)
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
			editor=new UserEditor(id, getModel());
		return (editor);
	}

	@Override
	protected IModel<String> getPageTitle() {
		return new Model<String> (getModel().getObject().getName());
	}


	@Override
	protected Panel createHeaderPanel() {
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

		return(ph);
	}

	@Override
	protected IRequestablePage getObjectPage(IModel<User> model, List<IModel<User>> list) {
	 	return new UserPage( model, list );
	}
	 
	@Override
	protected void onEdit(AjaxRequestTarget target) {
		this.editor.onEdit(target);
	}
	@Override
	protected Panel getAuditPanel(String id) {
		return new AuditPanel<User>(id, getModel());
	}

	@Override
	protected List<ToolbarItem> getToolbarItems() {
		
		if (x_list==null)
			return (x_list);
		
		x_list = new ArrayList<ToolbarItem>();
		
	 	DropDownMenuToolbarItem<User> menu  = new DropDownMenuToolbarItem<User>("item", getModel(), Align.TOP_RIGHT);
		menu.setTitle(getLabel("menu"));

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<User>() {
				private static final long serialVersionUID = 1L;
				@Override
				public MenuItemPanel<User> getItem(String id) {
					return new AjaxLinkMenuItem<User>(id, getModel()) {
						private static final long serialVersionUID = 1L;
						@Override
						public void onClick(AjaxRequestTarget target)  {
							fire (new MenuAjaxEvent(ServerAppConstant.user_info, target));
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
							fire (new MenuAjaxEvent(ServerAppConstant.object_audit, target));
						}
						@Override
						public IModel<String> getLabel() {
							return getLabel("audit");
						}
					};
				}
			});
				 
		x_list.add(menu);
		
		return x_list;
	}
	
	/**
	 * 
	 * 
	 * 
	 */
	@Override
	protected List<INamedTab> getInternalPanels() {

		List<INamedTab> tabs = super.createInternalPanels();
		
		NamedTab tab_1=new NamedTab(Model.of("editor"), ServerAppConstant.user_info) {
		 	private static final long serialVersionUID = 1L;
			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getEditor(panelId);
			}
		};
		tabs.add(tab_1);
	 
		if (getStartTab()==null)
				setStartTab(ServerAppConstant.user_info);

		return tabs;
	}
	
}
