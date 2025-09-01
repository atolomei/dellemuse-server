package dellemuse.serverapp.page.site;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
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
import dellemuse.serverapp.serverdb.model.ArtExhibition;
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
 * 
 * Site Information Exhibitions Artworks Exhibitions
 * 
 * 
 */

@MountPath("/institution/list")
public class InstitutionsListPage extends ObjectListPage<Institution> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(InstitutionsListPage.class.getName());

	public InstitutionsListPage() {
		super();
		setCreate(true);
	}

	public InstitutionsListPage(PageParameters parameters) {
		super(parameters);
		setCreate(true);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		
		addPageHeader();
	}

	
	@Override
	protected void onCreate() {
			Institution in = getInstitutionDBService().create("new", getUserDBService().findRoot());
			setResponsePage( new InstitutionPage(new ObjectModel<Institution>(in),  getList()));
	}

	
	public void addPageHeader() {
		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(new BCElement(getLabel("institutions")));
		PageHeaderPanel<Void> ph = new PageHeaderPanel<Void>("page-header", null, getLabel("institutions"));
		ph.setBreadCrumb(bc);
		add(ph);
	}

	@Override
	public IRequestablePage getObjectPage(IModel<Institution> model) {
		return null;
	}

	@Override
	public Iterable<Institution> getObjects() {
		InstitutionDBService service = (InstitutionDBService) ServiceLocator.getInstance()
				.getBean(InstitutionDBService.class);
		return service.findAllSorted();
	}

	@Override
	public IModel<String> getObjectInfo(IModel<Institution> model) {
		return new Model<String>(model.getObject().getInfo());
	}

	@Override
	public IModel<String> getObjectTitle(IModel<Institution> model) {
		return new Model<String>(model.getObject().getDisplayname());
	}

	@Override
	public void onClick(IModel<Institution> model) {
		setResponsePage(new InstitutionPage(model, getList()));
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

	protected void onCreate(AjaxRequestTarget target) {
	
	
		target.add(null);
	
	}

	@Override
	protected String getImageSrc(IModel<Institution> model) {
		 if ( model.getObject().getPhoto()!=null) {
		 		Resource photo = getResource(model.getObject().getPhoto().getId()).get();
		 	    return getPresignedThumbnailSmall(photo);
		     }
		  return null;	
	}
	
}
