package dellemuse.serverapp.page;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.MetaDataHeaderItem;
import org.apache.wicket.markup.head.PriorityHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.string.StringValue;

import dellemuse.model.ArtExhibitionGuideModel;
import dellemuse.model.ArtExhibitionItemModel;
import dellemuse.model.ArtExhibitionModel;
import dellemuse.model.ArtWorkModel;
import dellemuse.model.DelleMuseModelObject;
import dellemuse.model.GuideContentModel;
import dellemuse.model.ResourceModel;
import dellemuse.model.SiteModel;
import dellemuse.model.logging.Logger;
import dellemuse.model.ref.RefResourceModel;
import dellemuse.model.util.ThumbnailSize;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.ArtWorkDBService;
import dellemuse.serverapp.serverdb.service.InstitutionDBService;
import dellemuse.serverapp.serverdb.service.PersonDBService;
import dellemuse.serverapp.serverdb.service.ResourceDBService;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.UserDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.ResourceThumbnailService;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import wktui.bootstrap.Bootstrap;

public abstract class BasePage extends WebPage {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(BasePage.class.getName());

	static private IModel<String> model_xtitle;
	static private String xtitle;
	static private IModel<String> xdescription;
	static private String xfavicon;
	static private String xlanguage;
	static private String xrobots;
	static private String xrating;
	static private String xkeywords = "dellemuse web";

	private static final String XUA_Compatible = "IE=Edge";

	private Map<String, Integer> serverCall;

	// 1 Day
	static private final int COOKIE_DURATION = 86400 * 1;

	// public static JavaScriptResourceReference
	// getJavaScriptPopperResourceReference() {
	// return new JavaScriptResourceReference(BasePage.class,"popper.min.js");
	// }

	public static JavaScriptResourceReference getJavaScriptKbeeResourceReference() {
		return new JavaScriptResourceReference(BasePage.class, "kbee.js");
	}

	static {
		// xfavicon =
		// PropertiesFactory.getInstance("kbee").getProperties().getProperty("com.novamens.content.web.favicon",
		// "/images/favicon.gif");
		xlanguage = "English";
		xrobots = "NOINDEX, NOFOLLOW";
		xrating = "General";
	}

	// private static final String
	// DEFAULT_LATO_FONTS="https://fonts.googleapis.com/css2?family=Lato:ital,wght@0,400;0,900;1,400&display=swap";

	private static final ResourceReference BOOTSTRAP_CSS = Bootstrap.getCssResourceReference();
	private static final ResourceReference BOOTSTRAP_JS = Bootstrap.getJavaScriptResourceReference();

	// private static final ResourceReference POPPER_JS =
	// BasePage.getJavaScriptPopperResourceReference();
	private static final ResourceReference KBEE_JS = BasePage.getJavaScriptKbeeResourceReference();

	// private static final ResourceReference AW = new
	// CssResourceReference(BasePage.class, "./all.min.css");
	// private static final ResourceReference FONT_AWESOME_CSS = new
	// CssResourceReference(BasePage.class, "./dellemuse.css");

	private static final ResourceReference CSS = new CssResourceReference(BasePage.class, "./dellemuse.css");

	private String keywords = xkeywords;
	private String language = xlanguage;

	private String xuacompatible; // = "IE=8"; // IE=edge
	private String robots = xrobots;
	private String fonts = null;
	private String favicon = null;

	private ResourceReference rcss;

	private WebMarkupContainer wfont;
	private WebMarkupContainer wcss;
	private WebMarkupContainer fvicon;
	private WebMarkupContainer vp;
	private WebMarkupContainer desc;
	private WebMarkupContainer lang;
	private WebMarkupContainer kw;

	// private boolean initialized = false;

	// Map<Long, IModel<ArtExhibitionItemModel>> cacheArtExhibitionItem = new
	// HashMap<Long, IModel<ArtExhibitionItemModel>>();

	public BasePage() {
		super();
	}

	public BasePage(PageParameters parameters) {
		super(parameters);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		// if (cacheArtExhibitionItem != null)
		// cacheArtExhibitionItem.forEach((k, v) -> v.detach());
	}

	public BreadCrumb<Void> createBreadCrumb() {
		BreadCrumb<Void> bc = new BreadCrumb<>();
		bc.addElement(new HREFBCElement("/home", getLabel("home")));
		return bc;
	}

	public void setFonts(String fonts) {
		this.fonts = fonts;
	}

	public String getServerUrl() {
		String protocol = ((WebRequest) RequestCycle.get().getRequest()).getUrl().getProtocol();
		String host = ((WebRequest) RequestCycle.get().getRequest()).getUrl().getHost();
		Integer iport = ((WebRequest) RequestCycle.get().getRequest()).getUrl().getPort();
		String port = (iport.equals(80) || iport.equals(443) ? "" : (":" + iport.toString()));
		return protocol + "://" + host + port;
	}

	@Override
	public void onAfterRender() {
		super.onAfterRender();
		// cacheArtExhibitionItem.clear();
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		// cacheArtExhibitionItem = new HashMap<Long, IModel<ArtExhibitionItemModel>>();

		if (logger.isDebugEnabled())
			serverCall = new HashMap<String, Integer>();

		// ()!=null? getSession().getLocale().getLanguage() : null;

		this.language = Locale.forLanguageTag("es").getLanguage();

		if (getSession() != null)
			getSession().setLocale(Locale.forLanguageTag(this.language));

		this.wfont = new WebMarkupContainer("google-font");
		this.wfont.add(new AttributeModifier("rel", "stylesheet"));
		this.wfont.add(new AttributeModifier("href", getPageFonts()));
		this.wfont.setVisible(getPageFonts() != null);
		add(this.wfont);

		this.fvicon = new WebMarkupContainer("favicon");
		this.fvicon.add(new AttributeModifier("rel", "icon"));
		this.fvicon.add(new AttributeModifier("type", "image/x-icon"));
		this.fvicon.add(new AttributeModifier("href", getFavicon()));
		add(this.fvicon);

		this.lang = new WebMarkupContainer("language");
		this.lang.add(new AttributeModifier("name", "language"));
		this.lang.add(new AttributeModifier("language", new Model<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				// return getPageLanguage();
				return xlanguage;
			}
		}));

		add(this.lang);

		this.kw = new WebMarkupContainer("keywords");
		this.kw.add(new AttributeModifier("name", "keywords"));
		this.kw.add(new AttributeModifier("content", new Model<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return getPageKeywords();
			}
		}));
		add(this.kw);

		this.desc = new WebMarkupContainer("header-description");
		this.desc.add(new AttributeModifier("name", "description"));
		// this.desc.add(new AttributeModifier("content", getPageDescription()));
		this.desc.add(new AttributeModifier("name", "viewport"));
		this.desc.add(new AttributeModifier("content", new Model<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				// return "width=device-width, initial-scale=1.0, minimum-scale=1.0,
				// user-scalable=yes";
				return "width=device-width, initial-scale=1, shrink-to-fit=no";

			}
		}));
		add(this.desc);

		WebMarkupContainer rating = new WebMarkupContainer("rating");
		rating.add(new AttributeModifier("name", "rating"));
		rating.add(new AttributeModifier("content", xrating));
		add(rating);

		WebMarkupContainer wrobots = new WebMarkupContainer("robots");
		wrobots.add(new AttributeModifier("name", "robots"));
		wrobots.add(new AttributeModifier("content", new Model<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				return getPageRobots();
			}
		}));

		add(wrobots);

		setPageKeywords(keywords);

		this.wcss = new WebMarkupContainer("css");
		this.wcss.setVisible(false);
		add(this.wcss);

		this.vp = new WebMarkupContainer("viewport");
		add(vp);

		if (XUA_Compatible != null)
			setPageXUACompatible(XUA_Compatible);

	}

	@Override
	public void onBeforeRender() {
		super.onBeforeRender();
		// this.initialized=true;
		getServerCall().forEach((k, v) -> logger.debug(k + " -> " + v.toString()));
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);

		if (getPageXUACompatible() != null) {
			MetaDataHeaderItem headerItem = new MetaDataHeaderItem("meta");
			headerItem.addTagAttribute("http-equiv", "X-UA-Compatible");
			headerItem.addTagAttribute("content", getPageXUACompatible());

			response.render(new PriorityHeaderItem(headerItem));

			headerItem = new MetaDataHeaderItem("meta");
			headerItem.addTagAttribute("http-equiv", "Content-Security-Policy");
			headerItem.addTagAttribute("content", "img-src *");

			response.render(new PriorityHeaderItem(headerItem));
		}

		// response.render(JavaScriptHeaderItem.forUrl("popper.min.js"));
		// response.render(JavaScriptHeaderItem.forReference(POPPER_JS));
		response.render(JavaScriptHeaderItem.forReference(KBEE_JS));

		response.render(JavaScriptHeaderItem
				.forReference(getApplication().getJavaScriptLibrarySettings().getJQueryReference()));
		response.render(JavaScriptHeaderItem
				.forReference(getApplication().getJavaScriptLibrarySettings().getWicketAjaxReference()));

		response.render(CssHeaderItem.forReference(BOOTSTRAP_CSS));
		response.render(JavaScriptHeaderItem.forReference(BOOTSTRAP_JS));

		// response.render(CssHeaderItem.forReference(AW));

		response.render(CssHeaderItem.forReference(CSS));

		// response.render(JavaScriptHeaderItem.forReference(NotyJSReference.INSTANCE));
		// response.render(JavaScriptHeaderItem.forReference(NotyPackagedJSReference.INSTANCE));
		// response.render(JavaScriptHeaderItem.forReference(NotyThemeBootstrapJSReference.INSTANCE));

		if (getCssResource() != null)
			response.render(CssHeaderItem.forReference(getCssResource()));

		// if (!hasLateralMenu()) {
		// response.render(OnDomReadyHeaderItem.forScript("$('body').removeClass('sidebar-xs');"));
		// response.render(OnDomReadyHeaderItem.forScript("$('body').addClass('nosidebar');"));
		// }

	}

	protected void setPageFonts(String s) {
		fonts = s;
	}

	protected String getPageFonts() {
		return fonts;
	}

	protected void setPageRobots(String s) {
		robots = s;
	}

	protected String getPageRobots() {
		return robots;
	}

	protected void setPageXUACompatible(String uax) {
		this.xuacompatible = uax;
	}

	protected String getPageXUACompatible() {
		return xuacompatible;
	}

	protected void setPageLanguage(String language) {
		this.language = language;
	}

	protected String getPageLanguage() {
		return this.language;
	}

	protected void setFavicon(String desc) {
		this.favicon = desc;
	}

	protected String getFavicon() {
		return favicon != null ? favicon : xfavicon;
	}

	protected void setPageKeywords(String desc) {
		this.keywords = desc;
	}

	protected String getPageKeywords() {
		return keywords;
	}

	protected void setCss(ResourceReference rcss) {
		this.rcss = rcss;
	}

	protected ResourceReference getCssResource() {
		return rcss;
	}

	protected void mark(DelleMuseModelObject obj) {
		if (obj == null)
			return;
		mark(obj.getClass().getSimpleName() + "-" + obj.getDisplayname());
	}

	protected void mark(String apiCall) {
		if (!logger.isDebugEnabled())
			return;

		if (!serverCall.containsKey(apiCall))
			serverCall.put(apiCall, Integer.valueOf(0));

		serverCall.put(apiCall, Integer.valueOf((serverCall.get(apiCall).intValue() + 1)));
	}

	protected Map<String, Integer> getServerCall() {
		if (serverCall == null)
			serverCall = new HashMap<String, Integer>();
		return this.serverCall;
	}

	protected StringResourceModel getLabel(String key) {
		return new StringResourceModel(key, this);
	}

	protected IModel<String> getLabel(String key, String shortName) {
		return new StringResourceModel(key, this).setParameters(new Object[] { shortName });
	}

	/** Session User */

	public Optional<User> getSessionUser() {
		UserDBService service = (UserDBService) ServiceLocator.getInstance().getBean(UserDBService.class);
		User user = service.findRoot();
		if (user == null)
			return Optional.empty();
		return Optional.of(user);
	}

	/** DB Services */

	protected ArtWorkDBService getArtWorkDBService() {
		return (ArtWorkDBService) ServiceLocator.getInstance().getBean(ArtWorkDBService.class);
	}

	protected SiteDBService getSiteDBService() {
		return (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
	}

	protected ResourceDBService getResourceDBService() {
		return (ResourceDBService) ServiceLocator.getInstance().getBean(ResourceDBService.class);
	}

	public UserDBService getUserDBService() {
		return (UserDBService) ServiceLocator.getInstance().getBean(UserDBService.class);
	}

	protected InstitutionDBService getInstitutionDBService() {
		return (InstitutionDBService) ServiceLocator.getInstance().getBean(InstitutionDBService.class);
	}

	/** DB Services */

	public ArtWork lazyLoad(ArtWork s) {
		ArtWorkDBService service = (ArtWorkDBService) ServiceLocator.getInstance().getBean(ArtWorkDBService.class);
		ArtWork a = service.lazyLoad(s);
		return a;
	}

	public Optional<Person> getPerson(Long id) {
		PersonDBService service = (PersonDBService) ServiceLocator.getInstance().getBean(PersonDBService.class);
		return service.findById(id);
	}

	public Optional<Institution> getInstitution(Long id) {
		InstitutionDBService service = (InstitutionDBService) ServiceLocator.getInstance()
				.getBean(InstitutionDBService.class);
		return service.findById(id);
	}

	public Iterable<Person> getPersons() {
		PersonDBService service = (PersonDBService) ServiceLocator.getInstance().getBean(PersonDBService.class);
		return service.findAllSorted();
	}

	public Iterable<Site> getSites() {
		SiteDBService service = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
		return service.findAllSorted();
	}

	public Iterable<Site> getSites(Institution in) {
		InstitutionDBService service = (InstitutionDBService) ServiceLocator.getInstance()
				.getBean(InstitutionDBService.class);
		return service.getSites(in.getId());
	}

	public Iterable<ArtWork> getArtWorks(Site site) {
		SiteDBService service = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
		return service.getSiteArtWork(site);
	}

	public Optional<Site> getSite(Long id) {
		SiteDBService service = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
		return service.findById(id);
	}

	public Site getSite(ArtWork aw) {
		SiteDBService service = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
		Site site = service.findById(aw.getSite().getId()).get();
		return site;
	}

	public Optional<Site> findByIdWithDeps(Long id) {
		SiteDBService service = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
		return service.findByIdWithDeps(id);
	}

	public Optional<ArtWork> getArtWork(Long id) {
		ArtWorkDBService service = (ArtWorkDBService) ServiceLocator.getInstance().getBean(ArtWorkDBService.class);
		return service.findById(id);
	}

	public Optional<User> getUser(Long id) {
		UserDBService service = (UserDBService) ServiceLocator.getInstance().getBean(UserDBService.class);
		return service.findById(id);
	}

	public Optional<Resource> getResource(Long id) {
		ResourceDBService service = (ResourceDBService) ServiceLocator.getInstance().getBean(ResourceDBService.class);
		return service.findById(id);
	}

	public Iterable<Institution> getInstitutions() {
		InstitutionDBService service = (InstitutionDBService) ServiceLocator.getInstance()
				.getBean(InstitutionDBService.class);
		return service.findAllSorted();
	}

	public String getPresignedThumbnailSmall(Resource photo) {
		try {
			if (photo.isUsethumbnail()) {
				ResourceThumbnailService service = (ResourceThumbnailService) ServiceLocator.getInstance()
						.getBean(ResourceThumbnailService.class);
				String url = service.getPresignedThumbnailUrl(photo, ThumbnailSize.SMALL);
				mark("PresignedThumbnailUrl - " + photo.getDisplayname());
				return url;
			} else {
				mark("PresignedUrl - " + photo.getDisplayname());

				ObjectStorageService service = (ObjectStorageService) ServiceLocator.getInstance()
						.getBean(ObjectStorageService.class);
				return service.getClient().getPresignedObjectUrl(photo.getBucketName(), photo.getObjectName());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
