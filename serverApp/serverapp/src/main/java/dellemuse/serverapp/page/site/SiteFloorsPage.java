package dellemuse.serverapp.page.site;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.UrlResourceReference;
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
import dellemuse.model.ref.RefPersonModel;
import dellemuse.model.util.ThumbnailSize;
import dellemuse.serverapp.global.GlobalFooterPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.global.PageHeaderPanel;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.ArtWorkDBService;
import dellemuse.serverapp.serverdb.service.PersonDBService;
import dellemuse.serverapp.serverdb.service.ResourceDBService;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.ResourceThumbnailService;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.listNavigator.ListNavigator;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;
import wktui.base.InvisiblePanel;

/**
 * 
 * site foto Info - exhibitions
 * 
 */

@MountPath("/site/floors/${id}")
public class SiteFloorsPage extends BasePage {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteFloorsPage.class.getName());

	private StringValue stringValue;

	private IModel<Site> siteModel;

	private Image image;
	private WebMarkupContainer imageContainer;

	private int current = 0;

	public SiteFloorsPage() {
		super();
	}

	public SiteFloorsPage(PageParameters parameters) {
		super(parameters);
		stringValue = getPageParameters().get("id");
	}


	public SiteFloorsPage(IModel<Site> model) {
		setSiteModel(model);
		getPageParameters().add("id", model.getObject().getId().toString());
	}

	 
	
	@Override
	public void onInitialize() {
		super.onInitialize();

		if (getSiteModel() == null) {
			if (stringValue != null) {
				Optional<Site> o_aw = getSite(Long.valueOf(stringValue.toLong()));
				if (o_aw.isPresent()) {
					setSiteModel(new ObjectModel<Site>(o_aw.get()));
				}
			}
		}

		if (getSiteModel() == null) {
			throw new RuntimeException("no Site");
		}
 
 		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(	new HREFBCElement("/site/list", getLabel("sites")));
		bc.addElement(	new HREFBCElement("/site/" + getSiteModel().getObject().getId().toString(),
						new Model<String>(getSiteModel().getObject().getDisplayname())));

		bc.addElement(new BCElement(new Model<String>("floors")));

		PageHeaderPanel<Site> ph = new PageHeaderPanel<Site>("page-header", getSiteModel(),
				new Model<String>(getSiteModel().getObject().getDisplayname()));

		ph.setBreadCrumb(bc);
		add(ph);
		add(new GlobalTopPanel("top-panel"));
		add(new GlobalFooterPanel<>("footer-panel"));

	}

	public IModel<Site> getSiteModel() {
		return siteModel;
	}
 

	public void setSiteModel(IModel<Site> siteModel) {
		this.siteModel = siteModel;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (siteModel != null)
			siteModel.detach();

	}
 

}
