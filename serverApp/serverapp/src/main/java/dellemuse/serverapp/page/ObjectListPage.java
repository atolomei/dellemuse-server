package dellemuse.serverapp.page;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Site;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
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
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.DelleMuseObject;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.service.PersonDBService;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;


/**
 * 
 * site 
 * foto 
 * Info - exhibitions
 * 
 */

public abstract class ObjectListPage<T extends DelleMuseObject> extends BasePage {

	private static final long serialVersionUID = 1L;
	
	static private Logger logger = Logger.getLogger(ObjectListPage.class.getName());

	private List<IModel<T>> list;
	private Link<T> create;
	private WebMarkupContainer createContainer;
	private Label createLabel;
	private ListPanel<T> panel;
	
	private boolean b_create = false;
	
	
	public  ObjectListPage() {
		super();
	}		
	
	public  ObjectListPage(PageParameters parameters) {
		 super(parameters);
	 }
	
	
	public void setCreate( boolean b) {
		this.b_create=b;
	}
	
	protected boolean isCreate() {
		return b_create;
	}
	  
	 	
    
    public abstract IRequestablePage getObjectPage(IModel<T> model);
    public abstract Iterable<T> getObjects();
    
    public abstract IModel<String> getObjectInfo(IModel<T> model);
    public abstract IModel<String> getObjectTitle(IModel<T> model);
    
    public abstract void onClick(IModel<T> model);

    
    public abstract IModel<String> getPageTitle();
    public abstract IModel<String> getListPanelLabel();
    
    
    public void setPageHeaderPanel(Panel panel) {
    	addOrReplace(panel);
    }

    public void addDefaultPageHeaderPanel() {
        BreadCrumb<Void> bc = createBreadCrumb();
        bc.addElement(new BCElement(getPageTitle()));
    	PageHeaderPanel<Void> ph = new PageHeaderPanel<Void>("page-header", null, getPageTitle());
		ph.setBreadCrumb(bc);
		add(ph);
	}

    public void onBeforeRender() {
		super.onBeforeRender();
		if ( get("page-header")==null)
			this.addDefaultPageHeaderPanel();
	}
    

	@Override
	public void onDetach() {
	    super.onDetach();
	    
	    if (list!=null)
	    	list.forEach(i->i.detach());
    }
	
	public List<IModel<T>> getList() {
		return list;
	}
	

	@Override
	public void onInitialize() {
		super.onInitialize();
		
    	add(new GlobalTopPanel("top-panel"));
		add(new GlobalFooterPanel<>("footer-panel"));
		
		createContainer = new WebMarkupContainer("createContainer") {
			private static final long serialVersionUID = 1L;
			public boolean isVisible() {
				return isCreate();
			}
		};
		
		add(createContainer);
	       
		create = new Link<T>("create", null) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				ObjectListPage.this.onCreate();
			}
		};
	
		createLabel = new Label("create", getCreateLabel());
		create.add(createLabel);
		
		createContainer.add(create);
		
		loadList();
		
        this.panel = new ListPanel<>("contents", getList()) {
            private static final long serialVersionUID = 1L;
            
            @Override
            protected Panel getListItemPanel(IModel<T> model, ListPanelMode mode) {

                ObjectListItemPanel<T> panel = new ObjectListItemPanel<>("row-element", model, mode) {
                    private static final long serialVersionUID = 1L;
                    @Override
                    public void onClick() {
                    	ObjectListPage.this.onClick( getModel());
                    }
                
					protected IModel<String> getInfo() {
                        return ObjectListPage.this.getObjectInfo(getModel());
                    }
       
					protected IModel<String> getObjectTitle() {
                        return ObjectListPage.this.getObjectTitle( getModel() );
                    }
					
					 @Override
	                  protected String getImageSrc() {
						   return ObjectListPage.this.getImageSrc( getModel() );
	                   }
						 
                };
                return panel;
            }
            
            protected void onClick(IModel<T> model) {
            	 ObjectListPage.this.onClick(model);
            
            }
            
            @Override
            public IModel<String> getItemLabel(IModel<T> model) {
                return new Model<String>(model.getObject().getDisplayname());
            }
            
            @Override
            protected List<IModel<T>> filter(List<IModel<T>> initialList, String filter) {
            	
            	List<IModel<T>> list = new ArrayList<IModel<T>>();
            	final String str = filter.trim().toLowerCase();
            	initialList.forEach(
                		s -> {
                if (s.getObject().getDisplayname().toLowerCase().contains(str)) {
                	 list.add(s);
                	}
               }
               );
               return list; 
            }
        };
    
        panel.setSettings(isSettings());
        panel.setTitle(getListPanelLabel());
        panel.setListPanelMode(getListPanelMode());
        add(panel);
	}
	
	
	protected String getImageSrc(IModel<T> model) {
		return null;
	}

	protected boolean isSettings() {
		return true;
	}

	protected ListPanelMode getListPanelMode() {
		return  ListPanelMode.TITLE;
	}

	protected IModel<String> getCreateLabel() {
		return getLabel("new");
	}

	protected void onCreate() {
		logger.debug("on create");
	}

    protected void loadList() {
		list = new ArrayList<IModel<T>>();
		getObjects().forEach(s -> list.add(new ObjectModel<T>(s)));

    }
    
    


	 

}

