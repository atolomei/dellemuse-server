package dellemuse.serverapp.page.site;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.global.PageHeaderPanel;
import dellemuse.serverapp.guidecontent.GuideContentPage;
import dellemuse.serverapp.page.ObjectListPage;
import dellemuse.serverapp.page.library.ObjectStateEnumSelector;
import dellemuse.serverapp.page.library.ObjectStateListSelector;
import dellemuse.serverapp.page.library.SiteListPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.odilon.util.Check;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.toolbar.ButtonCreateToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.struct.list.ListPanelMode;

/**
 * Site Information
 * Exhibitions
 * Artworks
 * Exhibitions
 */
@MountPath("/site/guidecontents/${id}")
public class SiteContentGuideListPage extends ObjectListPage<GuideContent> {

	private static final long serialVersionUID = 1L;
	
	static private Logger logger = Logger.getLogger(SiteContentGuideListPage.class.getName());

	private StringValue stringValue;

	private IModel<Site> siteModel;
	
	//@Override
	//protected WebMarkupContainer getSubmenu() {
//		return new SiteNavDropDownMenuToolbarItem("submenu", getSiteModel(), Model.of(getSiteModel().getObject().getShortName(), Align.TOP_RIGHT ));//
	// }

	
	protected IModel<String> getTitleLabel() {
		return getLabel("guide-contents");
	}

	
	
	public SiteContentGuideListPage() {
		super();
		 setIsExpanded(true);
	}		
	
	public  SiteContentGuideListPage(PageParameters parameters) {
	 super(parameters);
	 	stringValue = getPageParameters().get("id");
	 	 setIsExpanded(true);
	}
	 	
	public SiteContentGuideListPage(IModel<Site> siteModel) {
		super();
		Check.requireNonNullArgument(siteModel, "siteModel is null");
		 
		setSiteModel(siteModel);
		getPageParameters().add("id", siteModel.getObject().getId().toString());
		 setIsExpanded(true);
	}
	
	protected void addHeaderPanel() {
	    BreadCrumb<Void> bc = createBreadCrumb();
	    bc.addElement(new HREFBCElement( "/site/list", getLabel("sites")));
        bc.addElement(new HREFBCElement( "/site/"+ getSiteModel().getObject().getId().toString(), 
        			  new Model<String>( getSiteModel().getObject().getDisplayname())) );
        
        bc.addElement(new BCElement(getLabel("guide-contents")));
	
        
        JumboPageHeaderPanel<Void> ph = new JumboPageHeaderPanel<Void>("page-header", null, new Model<String>(getSiteModel().getObject().getDisplayname()));
		ph.setBreadCrumb(bc);

		ph.setContext(getLabel("site"));

		
		add(ph);
	}
	
	 
	@Override
	public Iterable<GuideContent> getObjects() {
		return super.getGuideContents(getSiteModel().getObject());
	}
	
	@Override
	public Iterable<GuideContent> getObjects(ObjectState os1) {
		return super.getGuideContents(getSiteModel().getObject(), os1);
	}



	@Override
	public Iterable<GuideContent> getObjects(ObjectState os1, ObjectState os2) {
		return super.getGuideContents(getSiteModel().getObject(), os1, os2);
	}
	
 
	
	
	
	
	
	
	
	@Override
	public IModel<String> getObjectInfo(IModel<GuideContent> model) {
		String str = TextCleaner.clean(model.getObject().getInfo(), 280);
		return new Model<String>(str);
	}

	/**
	@Override
	public IModel<String> getObjectTitle(IModel<GuideContent> model) {
		
		if (model.getObject().getState()==ObjectState.DELETED) 
			return new Model<String>(model.getObject().getDisplayname() + ServerConstant.DELETED_ICON);

		return new Model<String>(model.getObject().getDisplayname());
	}
**/
	
	@Override
	public void onClick(IModel<GuideContent> model) {
		 setResponsePage(new GuideContentPage(model, getList()));
	}
	@Override
	public IModel<String> getPageTitle() {
		return getLabel("guide-contents");
	}

	@Override
	public IModel<String> getListPanelLabel() {
		return null;
	}

	@Override
	public void onInitialize() {
	
		if (getSiteModel()==null) {
			if (stringValue!=null) {
				Optional<Site> o_site= getSite(Long.valueOf(stringValue.toLong()));
				if (o_site.isPresent()) {
					setSiteModel(new ObjectModel<Site>(o_site.get()));
				}
			}
		}
		if (getSiteModel()==null) {
			throw new RuntimeException("no site");
		}

		super.onInitialize();
	}
    
	@Override
	public void onDetach() {
	    super.onDetach();
	    
	    if (siteModel!=null)
	    	siteModel.detach();
	}

	public IModel<Site> getSiteModel() {
		return siteModel;
	}

	public void setSiteModel(IModel<Site> siteModel) {
		this.siteModel = siteModel;
	}
	
	protected boolean isSettings() {
		return true;
	}
	
	protected ListPanelMode getListPanelMode() {
		return  ListPanelMode.TITLE;
	}

	 
	protected void onCreate() {
//		GuideContent g = getSiteDBService().createG .create("new", getSiteModel().getObject(), getUserDBService().findRoot());
//		IModel<GuideContent> m =  new ObjectModel<GuideContent>(aw);
//	  	getList().add(m);
//	  	setResponsePage( new ArtWorkPage(m, getList()));
	}

	@Override
	protected String getObjectImageSrc(IModel<GuideContent> model) {
		if (model.getObject().getPhoto()!=null) {
    		Resource photo = getResource(model.getObject().getPhoto().getId()).get();
    	    return getPresignedThumbnailSmall(photo);
        }
        return null;
	}
	
	protected List<ToolbarItem> getMainToolbarItems() {
		List<ToolbarItem> list = new ArrayList<ToolbarItem>();
		list.add(new SiteNavDropDownMenuToolbarItem("item", getSiteModel(), Model.of(getSiteModel().getObject().getShortName()), Align.TOP_RIGHT ) );
	
		ButtonCreateToolbarItem<Void> create = new ButtonCreateToolbarItem<Void>("item") {
			private static final long serialVersionUID = 1L;
			protected void onClick() {
				SiteContentGuideListPage.this.onCreate();
			}
		};
		
		list.add(create);
		
		
		return list;
	}
	
private List<ToolbarItem> listToolbar;
	
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
	protected String getObjectTitleIcon(IModel<GuideContent> model) {
		// TODO Auto-generated method stub
		return null;
	}

	 
}









/**
BreadCrumb<Void> bc = createBreadCrumb();
bc.addElement(new HREFBCElement( "/site/list", getLabel("sites")));
bc.addElement(new HREFBCElement( "/site/"+ getSiteModel().getObject().getId().toString(), 
			  new Model<String>(getSiteModel().getObject().getDisplayname())));

bc.addElement(new BCElement( getLabel("artwork")));
PageHeaderPanel<Void> ph = new PageHeaderPanel<Void>("page-header", null, new Model<String>(getSiteModel().getObject().getDisplayname()));
ph.setBreadCrumb(bc);
add(ph);


add(new GlobalTopPanel("top-panel"));
add(new GlobalFooterPanel<>("footer-panel"));

list = new ArrayList<IModel<ArtWork>>();
getArtWorks(getSiteModel().getObject() ).forEach(s -> list.add(new ObjectModel<ArtWork>(s)));


ListPanel<ArtWork> panel = new ListPanel<>("contents", getList()) {

private static final long serialVersionUID = 1L;

@Override
protected Panel getListItemPanel(IModel<ArtWork> model) {

    ObjectListItemPanel<ArtWork> panel = new ObjectListItemPanel<>("row-element", model, getListPanelMode()) {
        private static final long serialVersionUID = 1L;
        @Override
        public void onClick() {
        	setResponsePage( new ArtWorkPage( getModel(), getList() ) );
        }
        
        protected IModel<String> getInfo() {
            String str = TextCleaner.clean(model.getObject().getInfo(), 280);
            return new Model<String>(str);
        }
        
        @Override
        protected String getImageSrc() {
        	if ( getModel().getObject().getPhoto()!=null) {
        		Resource photo = getResource(model.getObject().getPhoto().getId()).get();
        	    return getPresignedThumbnailSmall(photo);
            }
            return null;
        }
    };
    return panel;
}
protected void onClick(IModel<ArtWork> model) {
	setResponsePage( new ArtWorkPage(model, getList() ) );
}

@Override
public IModel<String> getItemLabel(IModel<ArtWork> model) {
    return new Model<String>(model.getObject().getDisplayname());
}

@Override
protected List<IModel<ArtWork>> filter(List<IModel<ArtWork>> initialList, String filter) {
	
	List<IModel<ArtWork>> list = new ArrayList<IModel<ArtWork>>();

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

panel.setListPanelMode(ListPanelMode.TITLE);
panel.setLiveSearch(true);
panel.setSettings(true);
panel.setTitle(getLabel("artworks"));
add(panel);
**/

//List<IModel<ArtWork>>  list;

	//public List<IModel<ArtWork>> getList() {
	//	return list;
	//}
	