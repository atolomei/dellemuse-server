package dellemuse.serverapp.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.maven.model.Site;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
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
import dellemuse.serverapp.global.PageHeaderPanel;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.DelleMuseObject;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.service.PersonDBService;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.odilon.util.Check;
import io.wktui.error.ErrorPanel;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.listNavigator.ListNavigator;
import io.wktui.nav.toolbar.Toolbar;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;
import wktui.base.DummyBlockPanel;
import wktui.base.InvisiblePanel;


/**
 * 
 * site 
 * foto 
 * Info - exhibitions
 * 
 */

public abstract class ObjectPage<T extends DelleMuseObject> extends BasePage {

	private static final long serialVersionUID = 1L;
	
	 
	static private Logger logger = Logger.getLogger(ObjectPage.class.getName());

	private StringValue stringValue;
	private IModel<T> model;

	private List<IModel<T>> list;
	private int current = 0;
	private WebMarkupContainer navigatorContainer;
	private WebMarkupContainer internalPanelContainer;
	 
	protected abstract Optional<T> getObject(Long id);
	protected abstract IModel<String> getPageTitle();
	protected abstract void addHeaderPanel();
	protected abstract IRequestablePage getObjectPage(IModel<T> iModel, List<IModel<T>> list2);
	
	
	protected void addListeners() {
			super.addListeners();
	}
	
	public ObjectPage() {
		super();
	}		
	
	public  ObjectPage(PageParameters parameters) {
		 super(parameters);
		 stringValue = getPageParameters().get("id");
	 }
	
	public ObjectPage(IModel<T> model) {
		setModel(model);
		getPageParameters().add( "id", model.getObject().getId().toString());
	}
	

	public ObjectPage(IModel<T> model, List<IModel<T>> list) {
		Check.requireNonNullArgument(model, "model is null");
		Check.requireTrue(model.getObject()!=null, "modelOjbect is null");
		setModel(model);
		setList(list);
		getPageParameters().add( "id", model.getObject().getId().toString());
	}
	
    public IModel<T> getModel() {
    		return this.model;		
	}
 	
    
    public void setModel(IModel<T> model) {
    		this.model=model;		
	}

    
	public List<IModel<T>> getList() {
		return list;
	}

	public void setList(List<IModel<T>>siteList) {
		this.list = siteList;
	}
	
	
    
    public void setPageHeaderPanel(Panel panel) {
    	addOrReplace(panel);
    }

    public void addDefaultPageHeaderPanel() {
    	this.addOrReplace(new InvisiblePanel("page-header"));
   }

    public void onBeforeRender() {
		super.onBeforeRender();
		
		if (get("page-header")==null)
			this.addDefaultPageHeaderPanel();
		
		if (get("editor")==null)
			this.addOrReplace(new ErrorPanel("editor", new Model<String>("no editor")));
	}

    
    
    
    public void togglePanel(int panelOrder, AjaxRequestTarget target) {
    
    	this.currentPanel = panelOrder;
    	List<ITab> tabs = getInternalPanels();
		WebMarkupContainer pa=tabs.get(currentPanel).getPanel("internalPanel");
		internalPanelContainer.addOrReplace(pa);
		target.add(internalPanelContainer);
    }
    
    
	@Override
	public void onDetach() {
	    super.onDetach();
	    
	    if (getList()!=null)
	    	getList().forEach(i->i.detach());
    }
	
	 
	@Override
	public void onInitialize() {
		super.onInitialize();
		
		try {
			setUpModel();
		} catch (Exception e) {
			addErrorPanels(e);
			return;
		}
	
		setCurrent();
		
    	add(new GlobalTopPanel("top-panel"));
		add(new GlobalFooterPanel<>("footer-panel"));
		
		addHeaderPanel();
		addNavigator();
		
		/**
		Panel e=getEditor();

		if (!e.getId().equals("editor")) {
			addOrReplace(new ErrorPanel("editor", null, new Model<String>( "Editor must have id='editor'")));
		}
		else {
			addOrReplace(e);
		}
		 **/
		
		addToolbar();
		
		internalPanelContainer = new WebMarkupContainer("internalPanelContainer");
		internalPanelContainer.setOutputMarkupId(true);
		add(internalPanelContainer);
		List<ITab> tabs = getInternalPanels();
		WebMarkupContainer pa=tabs.get(getCurrentPanel()).getPanel("internalPanel");
		internalPanelContainer.add(pa);
	}


	protected int getCurrentPanel() {
		return this.currentPanel;
	}
	
	protected abstract List<ITab> getInternalPanels();
	
	/**{
		
		List<ITab> tabs = new ArrayList<ITab>();
		
		AbstractTab tab_1=new AbstractTab(Model.of("1")) {
			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return new DummyBlockPanel(panelId, Model.of("0"));
			}
		};
		tabs.add(tab_1);
		
		AbstractTab tab_2=new AbstractTab(Model.of("2")) {
			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return new DummyBlockPanel(panelId, Model.of("1"));
			}
		};
		
		tabs.add(tab_2);
		
		return tabs;
	}
	**/
	
	private int currentPanel = 0;
	

	private AjaxLink<T> edit;
	private Label editLabel;
	private WebMarkupContainer editContainer;
	private WebMarkupContainer toolbarContainer;
	private WebMarkupContainer toolbar;
	private WebMarkupContainer submenuContainer;
	private boolean b_edit = false;

	

	protected abstract List<ToolbarItem> getToolbarItems();

	
	private void addToolbar() {
		
		this.toolbarContainer = new WebMarkupContainer("toolbarContainer") {
			private static final long serialVersionUID = 1L;
			public boolean isVisible() {
				return getToolbarItems()!=null && getToolbarItems().size()>0;
			}
		};
		
		add(this.toolbarContainer);

		
		List<ToolbarItem> list = getToolbarItems();

		if (list!=null && list.size()>0) {
			Toolbar toolbarItems = new Toolbar("toolbarItems");
			list.forEach(t -> toolbarItems.addItem(t));
			this.toolbarContainer.add(toolbarItems);
		}
		else {
			this.toolbarContainer.add( new InvisiblePanel("toolbarItems"));
		}
		
		
		/**
		this.toolbar = new WebMarkupContainer("toolbar") {
			private static final long serialVersionUID = 1L;
			public boolean isVisible() {
				return isEdit() || isSubmenu();
			}
		};
		this.toolbarContainer.add(this.toolbar);
		
		
		this.editContainer = new WebMarkupContainer("editContainer") {
			private static final long serialVersionUID = 1L;
			public boolean isVisible() {
				return isEdit();
			}
		};
		this.edit= new AjaxLink<T>("editLink", getModel()) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				ObjectPage.this.onEdit(target);
			}
		};
		this.editLabel = new Label("edit", getEditLabel());
		this.edit.add(this.editLabel);
		this.editContainer.add(this.edit);
		
		
		
		this.submenuContainer = new WebMarkupContainer("submenuContainer") {
			private static final long serialVersionUID = 1L;
			public boolean isVisible() {
				return isSubmenu();
			}
		};
		
		if (getSubmenu()!=null)
			this.submenuContainer.add(getSubmenu());
		else
			this.submenuContainer.add(new InvisiblePanel("submenu"));
			
		
		this.toolbar.add(this.editContainer);
		this.toolbar.add(this.submenuContainer);
	    
		/**
		WebMarkupContainer sm=getSubmenu();
		
		if (sm==null) {
			submenuContainer.add(  new InvisiblePanel("submenu") );
			submenuContainer.setVisible(false);
		}
		else {
			submenuContainer.add(sm);
			submenuContainer.setVisible(sm.isVisible());
		}
	**/
		
	}
	
	protected IModel<String> getEditLabel() {
		return getLabel("edit");
	}
	
	
	protected abstract void onEdit(AjaxRequestTarget target);
    
    
    public void setEdit(boolean b) {
		this.b_edit=b;
	}
	
	protected boolean isEdit() {
		return b_edit;
	}
    
    
    
	protected void setUpModel() {
		if (getModel()==null) {
			if (stringValue!=null) {
				Optional<T> o =  getObject(Long.valueOf(stringValue.toLong()));  
				if (o.isPresent()) {
					setModel(new ObjectModel<T>(o.get()));
				}
			}
		}
		if (getModel()==null) {
			throw new RuntimeException("no objectModel");
		}
	}
    
	
	protected int getCurrent() {
		return this.current;
	}

	
	protected void setCurrent() {
		
		if (this.getList()==null)
			return;
		
		if (getModel()==null)
			return;
		
		int n = 0;

		for (IModel<T> m : this.getList()) {
			if ( getModel().getObject().getId().equals(m.getObject().getId())) {
				current = n;
				logger.debug("current -> " + String.valueOf(current));
				break;
			}
			n++;
		}
	}

	protected void addErrorPanels(Exception e) {
		
		addOrReplace(new GlobalTopPanel("top-panel"));
		addOrReplace(new GlobalFooterPanel<>("footer-panel"));
		addOrReplace(new ErrorPanel("editor", e));
		addOrReplace(new InvisiblePanel("navigatorContainer"));
		
		
	}

	
	private void addNavigator() {
		this.navigatorContainer = new WebMarkupContainer("navigatorContainer");
		add(this.navigatorContainer);
		
		this.navigatorContainer.setVisible(getList()!=null && getList().size()>0);

		if (getList() != null) {
			ListNavigator<T> nav = new ListNavigator<T>("navigator", this.current,
					getList()) {
				private static final long serialVersionUID = 1L;

				@Override
				protected IModel<String> getLabel(IModel<T> model) {
					return new Model<String>(model.getObject().getDisplayname());
				}

				@Override
				protected void navigate(int current) {
					setResponsePage( getObjectPage(getList().get(current), getList()));
				}
			};
			this.navigatorContainer.add(nav);
		} else {
			this.navigatorContainer.add(new InvisiblePanel("navigator"));
		}
	}
	 

}

