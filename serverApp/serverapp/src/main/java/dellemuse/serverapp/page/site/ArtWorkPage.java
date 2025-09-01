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

@MountPath("/artwork/${id}")
public class ArtWorkPage extends BasePage {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ArtWorkPage.class.getName());

	private StringValue stringValue;

	private IModel<Site> siteModel;
	private IModel<ArtWork> artWorkModel;
	private List<IModel<ArtWork>> list;

	private Link<ArtWork> imageLink;
	private Image image;
	private WebMarkupContainer imageContainer;

	private ArtWorkEditor editor;

	private int current = 0;

	public ArtWorkPage() {
		super();
	}

	public ArtWorkPage(PageParameters parameters) {
		super(parameters);
		stringValue = getPageParameters().get("id");
	}

	public ArtWorkPage(IModel<ArtWork> model) {
		this(model, null);
	}

	public ArtWorkPage(IModel<ArtWork> model, List<IModel<ArtWork>> list) {
		setArtWorkModel(model);

		setList(list);

		if (list != null) {
			int n = 0;
			for (IModel<ArtWork> m : list) {
				if (model.getObject().getId().equals(m.getObject().getId())) {
					this.current = n;
					break;
				}
				n++;
			}
		}
		getPageParameters().add("id", model.getObject().getId().toString());
	}

	/**
	 * 
	 * 
	 */
	@Override
	public void onInitialize() {
		super.onInitialize();

		if (getArtWorkModel() == null) {
			if (stringValue != null) {
				Optional<ArtWork> o_aw = getArtWork(Long.valueOf(stringValue.toLong()));
				if (o_aw.isPresent()) {
					setArtWorkModel(new ObjectModel<ArtWork>(o_aw.get()));
				}
			}
		}

		if (getArtWorkModel() == null) {
			throw new RuntimeException("no ArtWork");
		}
		
		if (!getArtWorkModel().getObject().isDependencies()) {
			Optional<ArtWork> o_i = getArtWorkDBService().findByIdWithDeps(getArtWorkModel().getObject().getId());
			setArtWorkModel(new ObjectModel<ArtWork>(o_i.get()));
		}

		setSiteModel(new ObjectModel<Site>( getArtWorkModel().getObject().getSite() ));

		if (!getSiteModel().getObject().isDependencies()) {
			Optional<Site> o_i = getSiteDBService().findByIdWithDeps(getSiteModel().getObject().getId());
			setSiteModel(new ObjectModel<Site>(o_i.get()));
		}

	
		
		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(new HREFBCElement("/site/list", getLabel("sites")));
		bc.addElement(new HREFBCElement("/site/" + getSiteModel().getObject().getId().toString(),
				new Model<String>(getSiteModel().getObject().getDisplayname())));
		bc.addElement(new HREFBCElement("/site/artwork/" + getSiteModel().getObject().getId().toString(),
				getLabel("artworks")));

		bc.addElement(new BCElement(new Model<String>(getArtWorkModel().getObject().getDisplayname())));

		PageHeaderPanel<ArtWork> ph = new PageHeaderPanel<ArtWork>("page-header", getArtWorkModel(),
				new Model<String>(getArtWorkModel().getObject().getDisplayname()));

		ph.setBreadCrumb(bc);
		add(ph);
		add(new GlobalTopPanel("top-panel"));
		add(new GlobalFooterPanel<>("footer-panel"));

		this.editor = new ArtWorkEditor("artworkEditor", getArtWorkModel());
		add(this.editor);
		
		
		
		
		
		/**
		StringBuilder info = new StringBuilder();
		int n = 0;
		for (Person p : getArtWorkModel().getObject().getArtists()  ) {
			if (n++ > 0)
					info.append(", ");
				info.append(p.getDisplayname());
		}
		ph.setTagline(new Model<String>(info.toString()));

		if (isInfoGral()) {
			Label infoGral = new Label("infoGral", getInfoGral());
			infoGral.setEscapeModelStrings(false);
			addOrReplace(infoGral);
		} else {
			addOrReplace(new InvisiblePanel("infoGral"));
		}
		
		
		add( new Label("info", "info"));
		
		if (getList() != null) {
			ListNavigator<ArtWork> list = new ListNavigator<ArtWork>("navigator", this.current, getList()) {
				private static final long serialVersionUID = 1L;

				@Override
				protected IModel<String> getLabel(IModel<ArtWork> model) {
					return new Model<String>(model.getObject().getDisplayname());
				}

				@Override
				protected void navigate(int current) {
					setResponsePage(new ArtWorkPage(getList().get(current), getList()));
				}
			};
			add(list);
		} else {
			add(new InvisiblePanel("navigator"));
		}

		this.imageContainer = new WebMarkupContainer("imageContainer");
		this.imageContainer.setVisible(getArtWorkModel().getObject().getPhoto() != null);
		addOrReplace(this.imageContainer);

		this.imageLink = new Link<>("image-link", getArtWorkModel()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				// GuideContentPage.this.onImageClick(getModel());
			}
		};
		this.imageContainer.add(this.imageLink);

		// if (getImageSrc(getArtWorkModel()) != null) {
		// Url url = Url.parse(getImageSrc(getArtWorkModel()));
		// UrlResourceReference resourceReference = new UrlResourceReference(url);
		// this.image = new Image("image", resourceReference);
		// this.imageLink.addOrReplace(this.image);
		// } else {
		this.image = new Image("image", new UrlResourceReference(Url.parse("")));
		this.image.setVisible(false);
		this.imageLink.addOrReplace(image);
		// }
		///
		///
		 */

	}

	public IModel<Site> getSiteModel() {
		return siteModel;
	}

	public void setArtWorkModel(IModel<ArtWork> model) {
		this.artWorkModel = model;
	}

	public IModel<ArtWork> getArtWorkModel() {
		return artWorkModel;
	}

	public void setSiteModel(IModel<Site> siteModel) {
		this.siteModel = siteModel;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (siteModel != null)
			siteModel.detach();

		if (artWorkModel != null)
			artWorkModel.detach();

	}
	
	public List<IModel<ArtWork>> getList() {
		return list;
	}

	
	private String getInfoGral() {
		return TextCleaner.clean(getArtWorkModel().getObject().getSpec() + " <br/>" + " id: "
				+ getArtWorkModel().getObject().getId().toString());
	}

	private boolean isInfoGral() {
		return  getArtWorkModel() != null && 
				getArtWorkModel().getObject() != null && 
				getArtWorkModel().getObject().getId() != null || 
				getArtWorkModel().getObject().getSpec() != null;
	}

	public void setList(List<IModel<ArtWork>> list) {
		this.list = list;
	}


	private String getImageSrc(IModel<ArtWork> model) {

		// ArtWorkDBService db = (ArtWorkDBService)
		// ServiceLocator.getInstance().getBean(ArtWorkDBService.class);

		try {

			if (getArtWorkModel().getObject().getPhoto() == null)
				return null;

			ResourceThumbnailService ths = (ResourceThumbnailService) ServiceLocator.getInstance()
					.getBean(ResourceThumbnailService.class);
			return ths.getPresignedThumbnailUrl(getArtWorkModel().getObject().getPhoto(), ThumbnailSize.MEDIUM);

		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	private boolean isAudio() {
		return getArtWorkModel().getObject().getAudio() != null;
	}

	/**
	protected List<Person> getArtists(ArtWork aw) {
		PersonDBService service = (PersonDBService) ServiceLocator.getInstance().getBean(PersonDBService.class);
		List<Person> list = new ArrayList<Person>(); 
		for (Person person: aw.getArtists()) {
			list.add(service.findById(person.getId()).get());
		}
		return list;
	}
	**/
	
}
