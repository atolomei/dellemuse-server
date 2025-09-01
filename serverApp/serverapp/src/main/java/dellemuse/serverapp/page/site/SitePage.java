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
import dellemuse.model.util.ThumbnailSize;
import dellemuse.serverapp.global.GlobalFooterPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.global.PageHeaderPanel;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.page.ErrorPage;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.ArtWorkDBService;
import dellemuse.serverapp.serverdb.service.ResourceDBService;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.ResourceThumbnailService;
import io.odilon.util.Check;
import io.wktui.error.ErrorPanel;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.listNavigator.ListNavigator;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;
import jakarta.transaction.Transactional;
import wktui.base.InvisiblePanel;

/**
 * 
 * site foto Info - exhibitions
 * 
 */

@MountPath("/site/${id}")
public class SitePage extends BasePage {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SitePage.class.getName());

	private StringValue stringValue;
	private IModel<Site> siteModel;
	
	private Link<Site> linkInfo;
	private Link<Site> linkArtWork;
	private Link<Site> linkFloors;
	private Link<Site> linkContents;
	
	private Image image;
	private WebMarkupContainer imageContainer;
	
	private Link<Resource> imageLink;
	private List<IModel<ArtExhibition>> listPermanent;
	private List<IModel<ArtExhibition>> listTemporary;
	
	
	private List<IModel<Site>> siteList;
	private int current = 0;
	private WebMarkupContainer navigatorContainer;

	private Exception exceptionError;

	
	public SitePage() {
		super();
	}


	public SitePage(PageParameters parameters) {
		super(parameters);
		stringValue = getPageParameters().get("id");
	}

	public SitePage(IModel<Site> model,  List<IModel<Site>> list) {
		Check.requireNonNullArgument(model, "model is null");
		Check.requireTrue(model.getObject()!=null, "modelOjbect is null");
		setSiteModel( model );
		getPageParameters().add("id", model.getObject().getId().toString());
		this.setSiteList(list);
	}

	private void setUpModel() {

		try {
			if (getSiteModel() == null) {
				if (stringValue != null) {
					Optional<Site> o_site = findByIdWithDeps(Long.valueOf(stringValue.toLong()));
					if (o_site.isPresent()) {
						setSiteModel(new ObjectModel<Site>(o_site.get()));
					}
				}
			}
			else {
				if (!getSiteModel().getObject().isDependencies()) {
					Optional<Site> o_site = findByIdWithDeps(Long.valueOf(getSiteModel().getObject().getId()));
					if (o_site.isPresent()) {
						setSiteModel(new ObjectModel<Site>(o_site.get()));
					}
				}
			}
		} catch (Exception  e) {
			logger.error(e);
			exceptionError = e;
		}
	}
	
	
	/**
	 * Institution Site Artwork Person Exhibition ExhibitionItem GuideContent User
	 */
	@Transactional
	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();
		
		if (getSiteModel() == null) {
			setResponsePage(new ErrorPage(exceptionError));
			return;
		}
	
		 

		setCurrent();
	
		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(new HREFBCElement("/site/list", getLabel("sites")));
		bc.addElement(new BCElement(new Model<String>(getSiteModel().getObject().getDisplayname())));
		PageHeaderPanel<Site> ph = new PageHeaderPanel<Site>("page-header", getSiteModel(),
				new Model<String>(getSiteModel().getObject().getDisplayname()));
		ph.setBreadCrumb(bc);
		
		add(ph);

		add(new GlobalTopPanel("top-panel"));
		add(new GlobalFooterPanel<>("footer-panel"));

		addExhibitions();

		linkInfo = new Link<Site>("info", getSiteModel()) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				setResponsePage(new SiteInfoPage(getSiteModel()));
			}

		};
		
		add(linkInfo);

		
		
		linkArtWork = new Link<Site>("artwork", getSiteModel()) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				setResponsePage(new SiteArtWorkListPage(getSiteModel()));
			}
		};
		add(linkArtWork);

		

		linkFloors = new Link<Site>("floors", getSiteModel()) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				setResponsePage(new SiteFloorsPage(getSiteModel()));
			}
		};
		add(linkFloors);
		
		
		linkContents = new Link<Site>("contents", getSiteModel()) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				setResponsePage(new SiteGuideContentsListPage(getSiteModel()));
			}
		};
		add(linkContents);
		
		addNavigator();
		addImageAndInfo();
	
	}

	private void addNavigator() {
		this.navigatorContainer = new WebMarkupContainer("navigatorContainer");
		add(this.navigatorContainer);
		
		this.navigatorContainer.setVisible(getSiteList()!=null && getSiteList().size()>0);

		if (getSiteList() != null) {
			ListNavigator<Site> nav = new ListNavigator<Site>("navigator", this.current,
					getSiteList()) {
				private static final long serialVersionUID = 1L;

				@Override
				protected IModel<String> getLabel(IModel<Site> model) {
					return new Model<String>(model.getObject().getDisplayname());
				}

				@Override
				protected void navigate(int current) {
					setResponsePage(new SitePage(getSiteList().get(current), getList()));
				}
			};
			this.navigatorContainer.add(nav);
		} else {
			this.navigatorContainer.add(new InvisiblePanel("navigator"));
		}
	}
	
	
	 
	private void addImageAndInfo() {

		this.imageContainer = new WebMarkupContainer("imageContainer");
		this.imageContainer.setVisible(getSiteModel().getObject().getPhoto() != null);
		addOrReplace(this.imageContainer);
		this.imageLink = new Link<Resource>("image-link", null) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				logger.debug("on click");
			}
		};
		
		this.imageContainer.add(this.imageLink);
		
		Label info = new Label("info", TextCleaner.clean(getSiteModel().getObject().getIntro()));
		info.setEscapeModelStrings(false);
		this.imageContainer.add(info);
		
		String presignedThumbnail = null;
		
		if (getSiteModel().getObject().getPhoto()!=null) 
			presignedThumbnail = getPresignedThumbnailSmall(getSiteModel().getObject().getPhoto());
		
		if (presignedThumbnail != null) {
			Url url = Url.parse(presignedThumbnail);
			UrlResourceReference resourceReference = new UrlResourceReference(url);
			this.image = new Image("image", resourceReference);
			this.imageLink.addOrReplace(this.image);
		} else {
			this.image = new Image("image", new UrlResourceReference(Url.parse("")));
			this.image.setVisible(false);
			this.imageLink.addOrReplace(image);
		}
		
		
	}

	private void addExhibitions() {

		{
			ListPanel<ArtExhibition> panel = new ListPanel<>("exhibitionsPermanent", getArtExhibitionsPermanent()) {
				private static final long serialVersionUID = 1L;

				protected List<IModel<ArtExhibition>> filter(List<IModel<ArtExhibition>> initialList, String filter) {
					List<IModel<ArtExhibition>> list = new ArrayList<IModel<ArtExhibition>>();
					final String str = filter.trim().toLowerCase();
					initialList.forEach(s -> {
						if (s.getObject().getDisplayname().toLowerCase().contains(str)) {
							list.add(s);
						}
					});
					return list;
				}

				@Override
				protected Panel getListItemPanel(IModel<ArtExhibition> model) {
					ObjectListItemPanel<ArtExhibition> panel = new ObjectListItemPanel<ArtExhibition>("row-element",
							model, getListPanelMode()) {
						private static final long serialVersionUID = 1L;

						@Override
						protected String getImageSrc() {
							if (getModel().getObject().getPhoto() != null) {
								Resource photo = getResource(model.getObject().getPhoto().getId()).get();
								return getPresignedThumbnailSmall(photo);
							}
							return null;
						}

						@Override
						public void onClick() {
						}

						protected IModel<String> getInfo() {
							String str = TextCleaner.clean(getModel().getObject().getIntro());
							return new Model<String>(str);
						}
					};
					return panel;
				}
			};
			 add(panel);
			
			panel.setTitle(getLabel("exhibitions-permanent"));
	        panel.setListPanelMode(ListPanelMode.TITLE_TEXT_IMAGE);
			panel.setLiveSearch(false);
	        panel.setSettings(true);

		}

		{
			ListPanel<ArtExhibition> panel = new ListPanel<>("exhibitionsTemporary", getArtExhibitionsTemporary()) {
				private static final long serialVersionUID = 1L;

				protected List<IModel<ArtExhibition>> filter(List<IModel<ArtExhibition>> initialList, String filter) {
					List<IModel<ArtExhibition>> list = new ArrayList<IModel<ArtExhibition>>();
					final String str = filter.trim().toLowerCase();
					initialList.forEach(s -> {
						if (s.getObject().getDisplayname().toLowerCase().contains(str)) {
							list.add(s);
						}
					});
					return list;
				}

				@Override
				protected Panel getListItemPanel(IModel<ArtExhibition> model) {
					ObjectListItemPanel<ArtExhibition> panel = new ObjectListItemPanel<ArtExhibition>("row-element",
							model, getListPanelMode()) {
						private static final long serialVersionUID = 1L;

						@Override
						protected String getImageSrc() {
							if (getModel().getObject().getPhoto() != null) {
								Resource photo = getResource(model.getObject().getPhoto().getId()).get();
								return getPresignedThumbnailSmall(photo);
							}
							return null;
						}

						@Override
						public void onClick() {
						}

						protected IModel<String> getInfo() {
							String str = TextCleaner.clean(getModel().getObject().getIntro());
							return new Model<String>(str);
						}
					};
					return panel;
				}
			};
			add(panel);
			
			panel.setTitle(getLabel("exhibitions-temporary"));

	        panel.setListPanelMode(ListPanelMode.TITLE_TEXT_IMAGE);
			panel.setLiveSearch(false);
	        panel.setSettings(true);
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (siteModel != null)
			siteModel.detach();

		if (listPermanent != null)
			listPermanent.forEach(i -> i.detach());

		if (listTemporary != null)
			listTemporary.forEach(i -> i.detach());

		if (this.siteList!=null)
			this.siteList.forEach(e-> e.detach());
	}

	public IModel<Site> getSiteModel() {
		return siteModel;
	}

	public void setSiteModel(IModel<Site> siteModel) {
		this.siteModel = siteModel;
	}
	
	public List<IModel<Site>> getSiteList() {
		return siteList;
	}

	public void setSiteList(List<IModel<Site>> siteList) {
		this.siteList = siteList;
	}

	private void loadLists() {
		try {

			SiteDBService db = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);

			listPermanent = new ArrayList<IModel<ArtExhibition>>();
			listTemporary = new ArrayList<IModel<ArtExhibition>>();

			List<ArtExhibition> la = db.getArtExhibitions(getSiteModel().getObject());

			for (ArtExhibition a : la) {
				if (a.isPermanent())
					listPermanent.add(new ObjectModel<ArtExhibition>(a));
				else
					listTemporary.add(new ObjectModel<ArtExhibition>(a));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	private List<IModel<ArtExhibition>> getArtExhibitionsPermanent() {
		if (listPermanent == null || listTemporary == null) {
			loadLists();
		}
		return listPermanent;
	}

	private List<IModel<ArtExhibition>> getArtExhibitionsTemporary() {
		if (listPermanent == null || listTemporary == null) {
			loadLists();
		}
		return listTemporary;
	}


	private void setCurrent() {
		
		if (this.siteList==null)
			return;
		
		if (getSiteModel()==null)
			return;
		
		int n = 0;
		for (IModel<Site> m : this.siteList) {
			if ( getSiteModel().getObject().getId().equals(m.getObject().getId())) {
				current = n;
				break;
			}
			n++;
		}
	}

}
