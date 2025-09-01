package dellemuse.serverapp.page.site;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.Component;
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
import dellemuse.serverapp.page.ObjectListPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.ArtWorkDBService;
import dellemuse.serverapp.serverdb.service.InstitutionDBService;
import dellemuse.serverapp.serverdb.service.ResourceDBService;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.ResourceThumbnailService;
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

@MountPath("/site/list")
public class SiteListPage extends ObjectListPage<Site> {

	private static final long serialVersionUID = 1L;
	
	static private Logger logger = Logger.getLogger(SiteListPage.class.getName());

	// private StringValue stringValue;

	public  SiteListPage() {
		super();
		setCreate(true);
	}		
	
	public  SiteListPage(PageParameters parameters) {
		 super(parameters);
		 setCreate(true);
	 }
	 	
	
	@Override
	protected void onCreate() {
			Site in = getSiteDBService().create("new", getUserDBService().findRoot());
			IModel<Site> m =  new ObjectModel<Site>(in);
			getList().add(m);
			setResponsePage( new SitePage(m,getList()));
	}

	
	public void addPageHeader() {
		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(new BCElement(getLabel("sites")));
		PageHeaderPanel<Void> ph = new PageHeaderPanel<Void>("page-header", null, getLabel("sites"));
		ph.setBreadCrumb(bc);
		add(ph);
	}

	@Override
	public IRequestablePage getObjectPage(IModel<Site> model) {
		return null;
	}

	@Override
	public Iterable<Site> getObjects() {
		SiteDBService service = (SiteDBService) ServiceLocator.getInstance()
				.getBean(SiteDBService.class);
		return service.findAllSorted();
	}

	@Override
	public IModel<String> getObjectInfo(IModel<Site> model) {
		return new Model<String>(model.getObject().getInfo());
	}

	@Override
	public IModel<String> getObjectTitle(IModel<Site> model) {
		return new Model<String>(model.getObject().getDisplayname());
	}

	@Override
	public void onClick(IModel<Site> model) {
		setResponsePage(new SitePage(model, getList()));
	}
	@Override
	public IModel<String> getPageTitle() {
		return getLabel("institutions");
	}

	@Override
	public IModel<String> getListPanelLabel() {
		return getLabel("list");
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	
	
	@Override
	public void onInitialize() {
		super.onInitialize();

		addPageHeader();
		
		/**
		
        BreadCrumb<Void> bc = createBreadCrumb();
        bc.addElement(new BCElement( getLabel("sites")));
        
    	PageHeaderPanel<Void> ph = new PageHeaderPanel<Void>("page-header", null, getLabel("sites"));
		ph.setBreadCrumb(bc);
		add(ph);

        add(new GlobalTopPanel("top-panel"));
		add(new GlobalFooterPanel<>("footer-panel"));

		List<IModel<Site>> list = new ArrayList<IModel<Site>>();

		getSites().forEach(s -> list.add(new ObjectModel<Site>(s)));
        
        ListPanel<Site> panel = new ListPanel<>("contents", list) {
            private static final long serialVersionUID = 1L;
            
            @Override
            protected Panel getListItemPanel(IModel<Site> model, ListPanelMode mode) {

                ObjectListItemPanel<Site> panel = new ObjectListItemPanel<>("row-element", model, mode) {
                    private static final long serialVersionUID = 1L;
                    @Override
                    public void onClick() {
                    	setResponsePage( new SitePage( getModel() ) );
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
            
            
            protected void onClick(IModel<Site> model) {
             	setResponsePage( new SitePage( model ) );
                
            }
            
            @Override
            public IModel<String> getItemLabel(IModel<Site> model) {
                return new Model<String>(model.getObject().getDisplayname());
            }
            
            @Override
            protected List<IModel<Site>> filter(List<IModel<Site>> initialList, String filter) {
            	
            	List<IModel<Site>> list = new ArrayList<IModel<Site>>();

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
        add(panel);
        
        panel.setListPanelMode(ListPanelMode.TITLE_TEXT_IMAGE);
		panel.setLiveSearch(false);
		panel.setSettings(false);
		*/
		
	}
    
	@Override
	protected ListPanelMode getListPanelMode() {
		return  ListPanelMode.TITLE_TEXT_IMAGE;
	}
   
	
	protected String getImageSrc(IModel<Site> model) {
		 if ( model.getObject().getPhoto()!=null) {
		 		Resource photo = getResource(model.getObject().getPhoto().getId()).get();
		 	    return getPresignedThumbnailSmall(photo);
		     }
		  return null;	
	}

 	 

}
