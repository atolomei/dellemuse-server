package dellemuse.serverapp.page.library;

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
import dellemuse.serverapp.page.site.ArtWorkPage;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.ArtExhibitionDBService;
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
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;

/**
 * 
 */

@MountPath("/artwork/list")
public class ArtWorkListPage extends ObjectListPage<ArtWork> {

	private static final long serialVersionUID = 1L;
	
	static private Logger logger = Logger.getLogger(ArtWorkListPage.class.getName());
 


	
	public  ArtWorkListPage() {
		super();
		setCreate(true);
	}		
	
	public  ArtWorkListPage(PageParameters parameters) {
		 super(parameters);
		 setCreate(true);
	 }
	
	 protected void addHeaderPanel() {

		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(new BCElement(getLabel("artworks")));
		PageHeaderPanel<Void> ph = new PageHeaderPanel<Void>("page-header", null, getLabel("artworks"));
		ph.setBreadCrumb(bc);
		add(ph);
	}

	@Override
	public IRequestablePage getObjectPage(IModel<ArtWork> model) {
		return null;
	}

	@Override
	public Iterable<ArtWork> getObjects() {
		ArtWorkDBService service = (ArtWorkDBService) ServiceLocator.getInstance()
				.getBean(ArtWorkDBService.class);
		return service.findAllSorted();
	}

	@Override
	public IModel<String> getObjectInfo(IModel<ArtWork> model) {
		return new Model<String>(model.getObject().getInfo());
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
		return getLabel("artwork");
	}
	
	@Override
	protected IModel<String> getTitleLabel() {
		return null;
	 
	}
	@Override
	public IModel<String> getListPanelLabel() {
		return null;
	}

	 
	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	protected ListPanelMode getListPanelMode() {
		return  ListPanelMode.TITLE;
	}
   
	protected List<ToolbarItem> getToolbarItems() {return null;}

	
	protected  WebMarkupContainer getSubmenu() {
		return null;
	}

	
	@Override
	protected String getImageSrc(IModel<ArtWork> model) {
		 if (model.getObject().getPhoto()!=null) {
		 		Resource photo = getResource(model.getObject().getPhoto().getId()).get();
		 	    return getPresignedThumbnailSmall(photo);
		     }
		  return null;	
	}
	
	@Override
	protected void onCreate() {
		ArtWork in = getArtWorkDBService().create("new", getUserDBService().findRoot());
			IModel<ArtWork> m =  new ObjectModel<ArtWork>(in);
			getList().add(m);
			setResponsePage( new ArtWorkPage(m, getList()));
	}

}
