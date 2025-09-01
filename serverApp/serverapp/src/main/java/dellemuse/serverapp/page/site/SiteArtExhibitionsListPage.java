package dellemuse.serverapp.page.site;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.ArtWorkDBService;
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

@MountPath("/site/exhibitions/${id}")
public class SiteArtExhibitionsListPage extends ObjectListPage<ArtExhibition> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteArtExhibitionsListPage.class.getName());

	private StringValue stringValue;

	IModel<Site> siteModel;

	public SiteArtExhibitionsListPage() {
		super();
	}

	public SiteArtExhibitionsListPage(PageParameters parameters) {
		super(parameters);
		stringValue = getPageParameters().get("id");
	}

	public SiteArtExhibitionsListPage(IModel<Site> siteModel) {
		super();
		setSiteModel(siteModel);
	}

	@Override
	public void onInitialize() {
		
		if (getSiteModel() == null) {
			if (stringValue != null) {
				Optional<Site> o_site = getSite(Long.valueOf(stringValue.toLong()));
				if (o_site.isPresent()) {
					setSiteModel(new ObjectModel<Site>(o_site.get()));
				}
			}
		}

		if (getSiteModel() == null) {
			throw new RuntimeException("no site");
		}
		
		super.onInitialize();

		addPageHeader();
	}

	
	public void addPageHeader() {
		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(new HREFBCElement("/site/list", getLabel("sites")));
		bc.addElement(new HREFBCElement("/site/" + getSiteModel().getObject().getId().toString(),
				new Model<String>(getSiteModel().getObject().getDisplayname())));
		bc.addElement(new BCElement(getLabel("artexhibitions")));
		PageHeaderPanel<Void> ph = new PageHeaderPanel<Void>("page-header", null,
				new Model<String>(getSiteModel().getObject().getDisplayname()));
		ph.setBreadCrumb(bc);
		add(ph);
	}

	public IRequestablePage getObjectPage(IModel<ArtExhibition> model) {
			return null;
	}

	
	public Iterable<ArtExhibition> getObjects() {
		
		SiteDBService service= (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
		
		
		return service.getArtExhibitions(getSiteModel().getObject().getId());
	
	}

	public IModel<String> getObjectInfo(IModel<ArtExhibition> model) {
		return new Model<String>( model.getObject().getIntro());
	}

	public IModel<String> getObjectTitle(IModel<ArtExhibition> model) {
		return new Model<String>( model.getObject().getDisplayname() );
	}

	public void onClick(IModel<ArtExhibition> model) {
		
	}

	public IModel<String> getPageTitle() {
		return new Model<String> ( getSiteModel().getObject().getDisplayname());
	}
	
	public IModel<String> getListPanelLabel() {
		return getLabel("artexhibitions");
		  
 }

	@Override
	public void onDetach() {
		super.onDetach();

		if (siteModel != null)
			siteModel.detach();
	}

	public IModel<Site> getSiteModel() {
		return siteModel;
	}

	public void setSiteModel(IModel<Site> siteModel) {
		this.siteModel = siteModel;
	}

}
