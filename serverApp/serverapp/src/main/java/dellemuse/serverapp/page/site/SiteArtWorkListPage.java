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
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.global.PageHeaderPanel;
import dellemuse.serverapp.page.ObjectListPage;
import dellemuse.serverapp.page.library.SiteListPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.odilon.util.Check;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.breadcrumb.Navigator;
import io.wktui.nav.toolbar.ButtonCreateToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.struct.list.ListPanelMode;

/**
 * Site Information Exhibitions Artworks Exhibitions
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

	public SiteArtWorkListPage(PageParameters parameters) {
		super(parameters);
		stringValue = getPageParameters().get("id");
		setCreate(true);
	}

	public SiteArtWorkListPage(IModel<Site> siteModel) {
		super();
		Check.requireNonNullArgument(siteModel, "siteModel is null");
		setCreate(true);
		setSiteModel(siteModel);
		getPageParameters().add("id", siteModel.getObject().getId().toString());
	}

	protected void addHeaderPanel() {
		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(new HREFBCElement("/site/list", getLabel("sites")));
		bc.addElement(new HREFBCElement("/site/" + getSiteModel().getObject().getId().toString(),
				new Model<String>(getSiteModel().getObject().getDisplayname())));

		bc.addElement(new BCElement(getLabel("artwork")));
		JumboPageHeaderPanel<Void> ph = new JumboPageHeaderPanel<Void>("page-header", null,
				new Model<String>(getSiteModel().getObject().getDisplayname()));
		ph.setBreadCrumb(bc);
		// ph.setTagline(getLabel("artworks"));
		if (getSiteModel().getObject().getPhoto() != null)
			ph.setPhotoModel(new ObjectModel<Resource>(getSiteModel().getObject().getPhoto()));

		add(ph);
	}

	
	protected List<ToolbarItem> getToolbarItems() {
		List<ToolbarItem> list = new ArrayList<ToolbarItem>();
		list.add(new SiteNavDropDownMenuToolbarItem("item", getSiteModel(),  Align.TOP_RIGHT ));
		
		ButtonCreateToolbarItem<Void> create = new ButtonCreateToolbarItem<Void>("item") {
			private static final long serialVersionUID = 1L;
			protected void onClick() {
				SiteArtWorkListPage.this.onCreate();
			}
		};
		create.setAlign(Align.TOP_LEFT);
		
		list.add(create);
		
		return list;
	}
	
	
	protected List<ToolbarItem> getToolbarItemsLeft() {return null;}

	
	protected IModel<String> getTitleLabel() {
		return getLabel("artworks");
	}

	@Override
	public IRequestablePage getObjectPage(IModel<ArtWork> model) {
		return null;
	}

	@Override
	public Iterable<ArtWork> getObjects() {
		SiteDBService service = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
		return service.getSiteArtWork(getSiteModel().getObject().getId());
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
		return null;
	}

	public void onConfigure() {
		super.onConfigure();
		logger.debug("on configure");
	}

	@Override
	public void onInitialize() {

		logger.debug("onInitialize()");
		
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

		if (!getSiteModel().getObject().isDependencies()) {
			Optional<Site> o_site = findByIdWithDeps(Long.valueOf(getSiteModel().getObject().getId()));
			if (o_site.isPresent()) {
				setSiteModel(new ObjectModel<Site>(o_site.get()));
			}
		}
 
		super.onInitialize();
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

	protected boolean isSettings() {
		return true;
	}

	protected ListPanelMode getListPanelMode() {
		return ListPanelMode.TITLE;
	}

	@Override
	protected void onCreate() {
		ArtWork aw = getArtWorkDBService().create("new", getSiteModel().getObject(), getUserDBService().findRoot());
		IModel<ArtWork> m = new ObjectModel<ArtWork>(aw);
		getList().add(m);
		setResponsePage(new ArtWorkPage(m, getList()));
	}

	@Override
	protected String getImageSrc(IModel<ArtWork> model) {
		if (model.getObject().getPhoto() != null) {
			Resource photo = getResource(model.getObject().getPhoto().getId()).get();
			return getPresignedThumbnailSmall(photo);
		}
		return null;
	}

	//protected WebMarkupContainer getSubmenu() {
	//	return new SiteNavDropDownMenuToolbarItem("submenu", getSiteModel(), Model.of(getSiteModel().getObject().getShortName()));
	//}
	
}
