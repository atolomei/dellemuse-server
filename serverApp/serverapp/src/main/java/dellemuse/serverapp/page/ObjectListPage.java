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
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.toolbar.DropDownMenuToolbarItem;
import io.wktui.nav.toolbar.Toolbar;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;
import wktui.base.InvisiblePanel;


/**
 * 
 * site 
 * foto 
 * Info - exhibitions
 * "ps-0 pe-0 pt-0 pb-0 float-start w-100 toolbar"
 */

public abstract class ObjectListPage<T extends DelleMuseObject> extends BasePage {

	private static final long serialVersionUID = 1L;
	
	static private Logger logger = Logger.getLogger(ObjectListPage.class.getName());

	private List<IModel<T>> list;
	private ListPanel<T> panel;
	private WebMarkupContainer titleContainer;
	private Label title;
	
	private Link<T> create;
	private Label createLabel;
	private WebMarkupContainer createContainer;
	private WebMarkupContainer toolbarContainer;
	private WebMarkupContainer toolbar;
	
	//private WebMarkupContainer submenuContainer;
	
	private boolean b_create = false;
	
	protected abstract void addHeaderPanel();
	 
    public abstract IRequestablePage getObjectPage(IModel<T> model);
    public abstract Iterable<T> getObjects();
    
    public abstract IModel<String> getObjectInfo(IModel<T> model);
    public abstract IModel<String> getObjectTitle(IModel<T> model);
    
    public abstract void onClick(IModel<T> model);
    
    public abstract IModel<String> getPageTitle();
    public abstract IModel<String> getListPanelLabel();
    

    
	public  ObjectListPage() {
		super();
	}		
	
	public  ObjectListPage(PageParameters parameters) {
		 super(parameters);
	 }
	
	    
    public void setPageHeaderPanel(Panel panel) {
    	addOrReplace(panel);
    }

    public void addDefaultPageHeaderPanel() {
    	this.addOrReplace(new InvisiblePanel("page-header"));
   }

    public void onBeforeRender() {
		super.onBeforeRender();
		
		if ( get("page-header")==null)
			this.addDefaultPageHeaderPanel();
	}
    

	@Override
	public void onDetach() {
	    super.onDetach();
	    
	    if (this.list!=null)
	    	this.list.forEach(i->i.detach());
    }
	
	public List<IModel<T>> getList() {
		return this.list;
	}
	
	public void setCreate(boolean b) {
		this.b_create=b;
	}
	
	protected boolean isCreate() {
		return b_create;
	}
		
	
	protected boolean isTitle() {
		return getTitleLabel()!=null;
	}
	
	
	private void addToolbar() {
		
		this.toolbarContainer = new WebMarkupContainer("toolbarContainer") {
			private static final long serialVersionUID = 1L;
			public boolean isVisible() {
				return getToolbarItems()!=null && getToolbarItems().size()>0;
			}
		};
		add(this.toolbarContainer);
		

		//this.toolbar = new WebMarkupContainer("toolbar") {
		//	private static final long serialVersionUID = 1L;
		//	public boolean isVisible() {
		//		return isToolbar();
		//	}
		//};
		//this.toolbarContainer.add(this.toolbar);
		
		
		/**
		this.createContainer = new WebMarkupContainer("createContainer") {
			private static final long serialVersionUID = 1L;
			public boolean isVisible() {
				return isCreate();
			}
		};
		this.create = new Link<T>("create", null) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				ObjectListPage.this.onCreate();
			}
		};
		this.createLabel = new Label("create", getCreateLabel());
		this.create.add(this.createLabel);
		this.createContainer.add(this.create);
		**/
		
		
		//this.submenuContainer = new WebMarkupContainer("submenuContainer") {
		//	private static final long serialVersionUID = 1L;
		//	public boolean isVisible() {
		//		return isSubmenu();
		//	}
		//};
		//if (getSubmenu()!=null)
		//		this.submenuContainer.add(getSubmenu());
		//	else
		//		this.submenuContainer.add(new InvisiblePanel("submenu"));
		//this.toolbar.add(this.createContainer);
		//this.toolbar.add(this.submenuContainer);
		
	
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
		DropDownMenuToolbarItem<Void> ddm;
		ddm = new DropDownMenuToolbarItem<Void>("item", null, Model.of("Left"));
		ddm.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Void> getItem(String id) {

				return new  LinkMenuItem<Void>(id) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
					}
					@Override
					public IModel<String> getLabel() {
						return new Model<String>("Contáctenos");
					}
					@Override
					public String getBeforeClick() {
						return null;
					}
				};
			}
		});

		
		ddm.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Void> getItem(String id) {

				return new  LinkMenuItem<Void>(id) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
					}
					@Override
					public IModel<String> getLabel() {
						return new Model<String>("info");
					}
					@Override
					public String getBeforeClick() {
						return null;
					}
				};
			}
		});
		
		toolbarItems.addItem(ddm, Align.TOP_LEFT);
		

		DropDownMenuToolbarItem<Void> ddmR;
		ddmR = new DropDownMenuToolbarItem<Void>("item", null, Model.of("Right"));
		ddmR.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Void> getItem(String id) {

				return new  LinkMenuItem<Void>(id) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
					}
					@Override
					public IModel<String> getLabel() {
						return new Model<String>("R Contáctenos");
					}
					@Override
					public String getBeforeClick() {
						return null;
					}
				};
			}
		});

		
		ddmR.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Void> getItem(String id) {

				return new  LinkMenuItem<Void>(id) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
					}
					@Override
					public IModel<String> getLabel() {
						return new Model<String>("R info");
					}
					@Override
					public String getBeforeClick() {
						return null;
					}
				};
			}
		});
		
		toolbarItems.addItem(ddmR, Align.TOP_RIGHT);

		*/
		
				
	}
	
	
	protected abstract List<ToolbarItem> getToolbarItems();

	
	@Override
	public void onInitialize() {
		super.onInitialize();
		
    	add(new GlobalTopPanel("top-panel"));
		add(new GlobalFooterPanel<>("footer-panel"));
			
		addHeaderPanel();
		
		addToolbar();
		
		this.titleContainer = new WebMarkupContainer("titleContainer") {
			private static final long serialVersionUID = 1L;
			public boolean isVisible() {
				return isTitle();
			}
		};
		add(this.titleContainer);
	       
		
		if (isTitle()) {
			Label title =new Label("title", getTitleLabel());
			this.titleContainer.add(title);
		}
		else {
			this.titleContainer.add( new InvisiblePanel("title"));
					
		}
		
	 	
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
    
        this.panel.setSettings(isSettings());
        this.panel.setTitle(getListPanelLabel());
        this.panel.setListPanelMode(getListPanelMode());
        add(this.panel);
	}
	
	

	//protected boolean isSubmenu() {
	//	return getSubmenu()!=null;
	//}
	//protected abstract WebMarkupContainer getSubmenu();

	
	protected String getImageSrc(IModel<T> model) {
		return null;
	}

	protected boolean isSettings() {
		return true;
	}

	protected ListPanelMode getListPanelMode() {
		return ListPanelMode.TITLE;
	}

	protected IModel<String> getCreateLabel() {
		return getLabel("create");
	}
	protected IModel<String> getTitleLabel() {
		return null;
	}
	
	protected void onCreate() {
		logger.debug("on create");
	}

    protected void loadList() {
    	this.list = new ArrayList<IModel<T>>();
		getObjects().forEach(s -> this.list.add(new ObjectModel<T>(s)));

    }
    
    


	 

}

