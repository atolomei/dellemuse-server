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
import dellemuse.serverapp.editor.ObjectMetaEditor;
import dellemuse.serverapp.global.GlobalFooterPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.global.PageHeaderPanel;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.model.ObjectWithDepModel;
import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
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
import wktui.base.INamedTab;
import wktui.base.InvisiblePanel;
import wktui.base.NamedTab;


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
	private int currentIndex = 0;
	private WebMarkupContainer toolbarContainer;
	
	private List<INamedTab> ipanels;
	private List<INamedTab> tabs;
	private WebMarkupContainer currentPanel;

	
	private ObjectMetaEditor<T> metaEditor;
	
	
	protected abstract Optional<T> getObject(Long id);
	protected abstract IModel<String> getPageTitle();
	protected abstract void addHeaderPanel();
	protected abstract IRequestablePage getObjectPage(IModel<T> iModel, List<IModel<T>> list);
	protected abstract List<INamedTab> getInternalPanels();
	protected abstract List<ToolbarItem> getToolbarItems();

	protected abstract void onEdit(AjaxRequestTarget target);
	
	
	
	
	public ObjectPage() {
		super();
	}		
	

	
	public  ObjectPage(PageParameters parameters) {
		 super(parameters);
		 stringValue = getPageParameters().get("id");
	 }
	
	public ObjectPage(IModel<T> model) {
		setModel(model);
		getPageParameters().add("id", model.getObject().getId().toString());
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
	}

    
    
    private String startingTab = null;
    
    public void setStartTab(String tabName) {
    	this.startingTab=tabName;
    }

    /**
    public void setStartTab(String tabName) {
		int tabOrder=getTab(tabName);
		this.currentIndex=tabOrder;
	}
	**/
    
    private int getTab(String name) {
    	
    	List<INamedTab> tabs =  getIPanels();
    	
    	int current = 0;
    	int selected = 0;
    	for (INamedTab tab: tabs) {
    		if (tab.getName().equals(name)) {
	    			selected = current;
	    			break;
    		}
    		current++;
    	}

    	return selected;
    }
    
    
    
    
    public void togglePanel(String name, AjaxRequestTarget target ) {
    	togglePanel(getTab(name ), target );
    } 	

    
    public void togglePanel(int panelOrder, AjaxRequestTarget target ) {
   
    	if (this.currentIndex==panelOrder) {
    		target.add(this.toolbarContainer);
    		target.add(this.internalPanelContainer);
    		return;
    	}
    	
    	this.currentIndex = panelOrder;
    	this.currentPanel= getIPanels().get(getCurrentIndex()).getPanel("internalPanel");
		this.internalPanelContainer.addOrReplace(this.currentPanel);
	
		addToolbar();

		target.add(this.toolbarContainer);
		target.add(this.internalPanelContainer);
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
			
			logger.error(e);
			addErrorPanels(e);
			return;
		}

		this.toolbarContainer = new WebMarkupContainer("toolbarContainer") {
			private static final long serialVersionUID = 1L;
			public boolean isVisible() {
				return getToolbarItems()!=null && getToolbarItems().size()>0;
			}
		};
		this.toolbarContainer.setOutputMarkupId(true);
		add(this.toolbarContainer);

		internalPanelContainer = new WebMarkupContainer("internalPanelContainer");
		internalPanelContainer.setOutputMarkupId(true);
		add(internalPanelContainer);
		
		setCurrent();
		
    	add(new GlobalTopPanel("top-panel"));
		add(new GlobalFooterPanel<>("footer-panel"));
		
		addHeaderPanel();
		addNavigator();
		
		tabs = getIPanels();
		
		if (this.startingTab!=null) {
				int tabOrder=getTab(this.startingTab);
				this.currentIndex=tabOrder;
		}
		
		currentPanel = tabs.get(getCurrentIndex()).getPanel("internalPanel");
		internalPanelContainer.add(currentPanel);
		
		addToolbar();

		
	}

	
	protected int getCurrentIndex() {
		return this.currentIndex;
	}
	
	protected WebMarkupContainer getCurrentPanel() {
		return this.currentPanel;
	}
	
	
	
	
	
	protected List<INamedTab> getTabs() {
		return this.tabs;
	}
	
	protected ObjectMetaEditor<T> getMetaEditor() {return this.metaEditor;}

	
	
	
	protected IModel<String> getEditLabel() {
		return getLabel("edit");
	}
	
	protected void setUpModel() {
		if (getModel()==null) {
			if (this.stringValue!=null) {
				Optional<T> o =  getObject(Long.valueOf(this.stringValue.toLong()));  
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
			if (getModel().getObject().getId().equals(m.getObject().getId())) {
				current = n;
				 
				break;
			}
			n++;
		}
	}

	protected void addErrorPanels(Exception e) {
		
		addOrReplace(new GlobalTopPanel("top-panel"));
		addOrReplace(new InvisiblePanel("page-header"));
		addOrReplace( new InvisiblePanel("toolbarContainer"));
		internalPanelContainer = new WebMarkupContainer("internalPanelContainer");
		add(internalPanelContainer);
		internalPanelContainer.add(new ErrorPanel("internalPanel", e));
		addOrReplace(new InvisiblePanel("navigatorContainer"));
		addOrReplace(new GlobalFooterPanel<>("footer-panel"));
		
	}

	protected void addListeners() {
			super.addListeners();
	}


	protected Panel getMetaEditor(String id) {
		if (this.metaEditor==null)
			metaEditor = new ObjectMetaEditor<T>(id, getModel());
		return (metaEditor);
	}
	
	
	
    
	private void addToolbar() {
		
		WebMarkupContainer wCurrent = getCurrentPanel();
	
		Toolbar toolbarItems = new Toolbar("toolbarItems");
		List<ToolbarItem> list = new ArrayList<ToolbarItem>();
		List<ToolbarItem> localItems = null;
		List<ToolbarItem> globalItems = getToolbarItems();
		
		if (wCurrent instanceof InternalPanel)
			localItems = ((InternalPanel) wCurrent).getToolbarItems();

		if (localItems!=null)
			list.addAll(localItems);
		
		if (globalItems!=null)
			list.addAll(globalItems);
		
		if (list.size()>0) {
			list.forEach(t -> toolbarItems.addItem(t));
			this.toolbarContainer.addOrReplace(toolbarItems);
		}
		else {
			this.toolbarContainer.addOrReplace(new InvisiblePanel("toolbarItems"));
		}
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
					setResponsePage(getObjectPage(getList().get(current), getList()));
				}
			};
			this.navigatorContainer.add(nav);
		} else {
			this.navigatorContainer.add(new InvisiblePanel("navigator"));
		}
	}

	

	protected List<INamedTab> createInternalPanels() {
		
		List<INamedTab> tabs = new ArrayList<INamedTab>();
		
		NamedTab tab_2=new NamedTab(Model.of("metainfo"), ServerAppConstant.object_meta) {
			 
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getMetaEditor(panelId);
			}
		};
		tabs.add(tab_2);
		
		return tabs;
	
		
	}

	private List<INamedTab> getIPanels() {
		if (ipanels==null) {
			ipanels =getInternalPanels();
		}
		return ipanels;
	}

	

}


