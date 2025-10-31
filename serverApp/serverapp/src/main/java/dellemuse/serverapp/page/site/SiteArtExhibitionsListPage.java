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
import dellemuse.serverapp.artexhibition.ArtExhibitionPage;
import dellemuse.serverapp.artwork.ArtWorkPage;
import dellemuse.serverapp.global.GlobalFooterPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.global.PageHeaderPanel;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.ObjectListPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.ArtWorkDBService;
import dellemuse.serverapp.serverdb.service.ResourceDBService;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.ResourceThumbnailService;
import io.odilon.util.Check;
import io.wktui.event.SimpleWicketEvent;
import io.wktui.event.UIEvent;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.toolbar.ButtonCreateToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
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
		setIsExpanded(true);
		stringValue = getPageParameters().get("id");
		 
	}

	public SiteArtExhibitionsListPage(IModel<Site> siteModel) {
		super();
		Check.requireNonNullArgument(siteModel, "siteModel is null");
		setIsExpanded(true);
		getPageParameters().add("id", siteModel.getObject().getId().toString());
		setSiteModel(siteModel);
	}
	
	protected IModel<String> getTitleLabel() {
		return getLabel("exhibitions");
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
		
		if (!getSiteModel().getObject().isDependencies()) {
			Optional<Site> o_site = findByIdWithDeps(Long.valueOf(getSiteModel().getObject().getId()));
			if (o_site.isPresent()) {
				setSiteModel(new ObjectModel<Site>(o_site.get()));
			}
		}
		
		super.onInitialize();

	}

 

 
 @Override
	protected void addHeaderPanel() {
		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(new HREFBCElement("/site/list", getLabel("sites")));
		bc.addElement(new HREFBCElement("/site/" + getSiteModel().getObject().getId().toString(),
				new Model<String>(getSiteModel().getObject().getDisplayname())));

		bc.addElement(new BCElement(getLabel("artexhibitions")));
		JumboPageHeaderPanel<Void> ph = new JumboPageHeaderPanel<Void>("page-header", null,
				new Model<String>(getSiteModel().getObject().getDisplayname()));
		ph.setBreadCrumb(bc);
		
		ph.setContext(getLabel("site"));

		if (getSiteModel().getObject().getSubtitle()!=null)
			ph.setTagline( Model.of( getSiteModel().getObject().getSubtitle()));

		 if (getSiteModel().getObject().getPhoto() != null)
			ph.setPhotoModel(new ObjectModel<Resource>(getSiteModel().getObject().getPhoto()));

		add(ph);
	}

 
 @Override
	protected WebMarkupContainer getObjectMenu(IModel<ArtExhibition> model) {
		
		NavDropDownMenu<ArtExhibition> menu = new NavDropDownMenu<ArtExhibition>("menu", model, null);
		
		menu.setOutputMarkupId(true);

		menu.setLabelCss("d-block-inline d-sm-block-inline d-md-block-inline d-lg-none d-xl-none d-xxl-none ps-1 pe-1");
		menu.setIconCss("fa-solid fa-ellipsis d-block-inline d-sm-block-inline d-md-block-inline d-lg-block-inline d-xl-block-inline d-xxl-block-inline ps-1 pe-1");

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibition> getItem(String id) {

				return new AjaxLinkMenuItem<ArtExhibition>(id) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						// refresh(target);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("edit");
					}
				};
			}
		});

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibition> getItem(String id) {

				return new AjaxLinkMenuItem<ArtExhibition>(id) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						// refresh(target);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("delete");
					}
				};
			}
		});
		return menu;
	}
	
 
	public IRequestablePage getObjectPage(IModel<ArtExhibition> model) {
			return new ArtExhibitionPage(model);
	}

	
	//protected IModel<String> getTitleLabel() {
	//	return getLabel("artexhibitions");
	//}

		
	public Iterable<ArtExhibition> getObjects() {
		return super.getSiteArtExhibitions(getSiteModel().getObject());
		
		//SiteDBService service= (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
		//return service.getArtExhibitions(getSiteModel().getObject().getId());

	}

	
	public IModel<String> getObjectInfo(IModel<ArtExhibition> model) {
		return new Model<String>( model.getObject().getIntro());
	}

	public IModel<String> getObjectTitle(IModel<ArtExhibition> model) {
		return new Model<String>( model.getObject().getDisplayname() );
	}

	public void onClick(IModel<ArtExhibition> model) {
		setResponsePage(new ArtExhibitionPage(model));
	}

	public IModel<String> getPageTitle() {
		return new Model<String> ( getSiteModel().getObject().getDisplayname());
	}
	
	//public IModel<String> getListPanelLabel() {
	//	return getLabel("artexhibitions");
	//}

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

	
	@Override
	protected void onCreate() {
		try {
		ArtExhibition in = getArtExhibitionDBService().create("new", getSiteModel().getObject(), getUserDBService().findRoot());
		IModel<ArtExhibition> m =  new ObjectModel<ArtExhibition>(in);
		getList().add(m);
		ArtExhibitionPage a=new ArtExhibitionPage(m, getList());
		setResponsePage(a);
		} catch (Exception e) {
			logger.error(e);
			//setResponsePage(new ErrorPage(e));
			
		}
	}

	
	
	protected List<ToolbarItem> getToolbarItems() {
		
		List<ToolbarItem> list = new ArrayList<ToolbarItem>();
		
		list.add(new SiteNavDropDownMenuToolbarItem(	"item", 
														getSiteModel(), 
														Align.TOP_RIGHT));
		

		ButtonCreateToolbarItem<Void> create = new ButtonCreateToolbarItem<Void>("item") {
			private static final long serialVersionUID = 1L;
			protected void onClick() {
				SiteArtExhibitionsListPage.this.onCreate();
			}
		};
		create.setAlign(Align.TOP_LEFT);
		list.add(create);
		
		
		return list;
	}
	
	
	@Override
	protected void addListeners() {
		super.addListeners();
 
		add(new io.wktui.event.WicketEventListener<SimpleWicketEvent>() {
			private static final long serialVersionUID = 1L;
			@Override
			public void onEvent(SimpleWicketEvent event) {
				if (event.getName().equals(ServerAppConstant.action_site_home)) {
					setResponsePage( new SitePage( getSiteModel(), null));
				}
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof SimpleWicketEvent)
					return true;
				return false;
			}
		});
	}

	@Override
	public IModel<String> getListPanelLabel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected String getObjectImageSrc(IModel<ArtExhibition> model) {
		if (model.getObject().getPhoto() != null) {
			Resource photo = getResource(model.getObject().getPhoto().getId()).get();
			return getPresignedThumbnailSmall(photo);
		}
		return null;
	}
	
}
