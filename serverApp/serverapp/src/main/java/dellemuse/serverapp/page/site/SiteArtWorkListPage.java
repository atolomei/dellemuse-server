package dellemuse.serverapp.page.site;

import java.util.Optional;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.global.PageHeaderPanel;
import dellemuse.serverapp.page.ObjectListPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.struct.list.ListPanelMode;

/**
 * Site Information
 * Exhibitions
 * Artworks
 * Exhibitions
 */
@MountPath("/site/artwork/${id}")
public class SiteArtWorkListPage extends ObjectListPage<ArtWork> {

	private static final long serialVersionUID = 1L;
	
	static private Logger logger = Logger.getLogger(SiteArtWorkListPage.class.getName());

	private StringValue stringValue;

	private IModel<Site> siteModel;
	
	
	public SiteArtWorkListPage() {
		super();
		setCreate(true);
	}		
	
	public  SiteArtWorkListPage(PageParameters parameters) {
	 super(parameters);
	 	stringValue = getPageParameters().get("id");
	 	setCreate(true);
	}
	 	
	public SiteArtWorkListPage(IModel<Site> siteModel) {
		super();
		setCreate(true);
		setSiteModel(siteModel);
	}
	
	public void addPageHeader() {
	    BreadCrumb<Void> bc = createBreadCrumb();
	    bc.addElement(new HREFBCElement( "/site/list", getLabel("sites")));
        bc.addElement(new HREFBCElement( "/site/"+ getSiteModel().getObject().getId().toString(), 
        			  new Model<String>( getSiteModel().getObject().getDisplayname())) );
        
        bc.addElement(new BCElement(getLabel("artwork")));
		PageHeaderPanel<Void> ph = new PageHeaderPanel<Void>("page-header", null, new Model<String>(getSiteModel().getObject().getDisplayname()));
		ph.setBreadCrumb(bc);
		add(ph);
	}
	
	@Override
	public IRequestablePage getObjectPage(IModel<ArtWork> model) {
		return null;
	}

	@Override
	public Iterable<ArtWork> getObjects() {
		SiteDBService service = (SiteDBService) ServiceLocator.getInstance()
				.getBean(SiteDBService.class);
		return service.getSiteArtWork( getSiteModel().getObject().getId());
	}
	
	@Override
	public IModel<String> getObjectInfo(IModel<ArtWork> model) {
		String str = TextCleaner.clean(model.getObject().getInfo(), 280);
		return new Model<String>(str);
	}

	@Override
	public IModel<String> getObjectTitle(IModel<ArtWork> model) {
		return new Model<String>(model.getObject().getDisplayname());
	}

	@Override
	public void onClick(IModel<ArtWork> model) {
		 setResponsePage(new ArtWorkPage(model, getList()));
	}
	@Override
	public IModel<String> getPageTitle() {
		return getLabel("artworks");
	}

	@Override
	public IModel<String> getListPanelLabel() {
		return getLabel("list");
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

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

	@Override
	protected void onCreate() {
		ArtWork aw = getArtWorkDBService().create("new", getSiteModel().getObject(), getUserDBService().findRoot());
		IModel<ArtWork> m =  new ObjectModel<ArtWork>(aw);
	  	getList().add(m);
	  	setResponsePage( new ArtWorkPage(m, getList()));
	}

	@Override
	protected String getImageSrc(IModel<ArtWork> model) {
		if (model.getObject().getPhoto()!=null) {
    		Resource photo = getResource(model.getObject().getPhoto().getId()).get();
    	    return getPresignedThumbnailSmall(photo);
        }
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
	