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
import dellemuse.serverapp.artexhibition.ArtExhibitionPage;
import dellemuse.serverapp.global.GlobalFooterPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.global.PageHeaderPanel;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.ObjectListPage;
import dellemuse.serverapp.page.model.ObjectModel;
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

@MountPath("/exhibition/list")
public class ArtExhibitionListPage extends ObjectListPage<ArtExhibition> {

	private static final long serialVersionUID = 1L;
	
	static private Logger logger = Logger.getLogger(ArtExhibitionListPage.class.getName());

	

	public  ArtExhibitionListPage() {
		super();
		setCreate(true);
	}		
	
	public  ArtExhibitionListPage(PageParameters parameters) {
		 super(parameters);
		 setCreate(true);
	 }
	
  protected void addHeaderPanel() {
		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(new BCElement(getLabel("exhibitions")));
		PageHeaderPanel<Void> ph = new PageHeaderPanel<Void>("page-header", null, getLabel("exhibitions"));
		ph.setBreadCrumb(bc);
		add(ph);
	}

	@Override
	public IRequestablePage getObjectPage(IModel<ArtExhibition> model) {
		return null;
	}

	@Override
	public Iterable<ArtExhibition> getObjects() {
		ArtExhibitionDBService service = (ArtExhibitionDBService) ServiceLocator.getInstance()
				.getBean(ArtExhibitionDBService.class);
		return service.findAllSorted();
	}

	@Override
	public IModel<String> getObjectInfo(IModel<ArtExhibition> model) {
		return new Model<String>(model.getObject().getInfo());
	}

	@Override
	public IModel<String> getObjectTitle(IModel<ArtExhibition> model) {
		return new Model<String>(model.getObject().getDisplayname());
	}

	@Override
	public void onClick(IModel<ArtExhibition> model) {
		setResponsePage(new ArtExhibitionPage(model, getList()));
	}
	@Override
	public IModel<String> getPageTitle() {
		return getLabel("exhibitions");
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
   
	@Override
	protected String getImageSrc(IModel<ArtExhibition> model) {
		 if (model.getObject().getPhoto()!=null) {
		 		Resource photo = getResource(model.getObject().getPhoto().getId()).get();
		 	    return getPresignedThumbnailSmall(photo);
		     }
		  return null;	
	
	}
	

	protected List<ToolbarItem> getToolbarItems() {return null;}

	
	protected IModel<String> getTitleLabel() {
		return getLabel("exhibitions");
	}
	
	
	protected  WebMarkupContainer getSubmenu() {
		return null;
	}

	
	@Override
	protected void onCreate() {
			ArtExhibition in = getArtExhibitionDBService().create("new", getUserDBService().findRoot());
			IModel<ArtExhibition> m =  new ObjectModel<ArtExhibition>(in);
			getList().add(m);
			setResponsePage( new ArtExhibitionPage(m, getList()));
	}

}
