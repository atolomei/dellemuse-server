package dellemuse.serverapp.page;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
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
import org.apache.wicket.request.resource.ResourceReference;

import dellemuse.model.DelleMuseModelObject;
import dellemuse.model.logging.Logger;
import dellemuse.model.util.ThumbnailSize;

import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.ArtExhibitionSection;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Artist;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.ArtExhibitionDBService;
import dellemuse.serverapp.serverdb.service.ArtExhibitionGuideDBService;
import dellemuse.serverapp.serverdb.service.ArtExhibitionItemDBService;
import dellemuse.serverapp.serverdb.service.ArtExhibitionSectionDBService;
import dellemuse.serverapp.serverdb.service.ArtWorkDBService;
import dellemuse.serverapp.serverdb.service.AudioStudioDBService;
import dellemuse.serverapp.serverdb.service.GuideContentDBService;
import dellemuse.serverapp.serverdb.service.InstitutionDBService;
import dellemuse.serverapp.serverdb.service.PersonDBService;
import dellemuse.serverapp.serverdb.service.ResourceDBService;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.UserDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.serverdb.service.record.ArtExhibitionGuideRecordDBService;
import dellemuse.serverapp.serverdb.service.record.ArtExhibitionItemRecordDBService;
import dellemuse.serverapp.serverdb.service.record.ArtExhibitionRecordDBService;
import dellemuse.serverapp.serverdb.service.record.ArtExhibitionSectionRecordDBService;
import dellemuse.serverapp.serverdb.service.record.ArtWorkRecordDBService;
import dellemuse.serverapp.serverdb.service.record.GuideContentRecordDBService;
import dellemuse.serverapp.serverdb.service.record.InstitutionRecordDBService;
import dellemuse.serverapp.serverdb.service.record.PersonRecordDBService;
import dellemuse.serverapp.serverdb.service.record.SiteRecordDBService;
import dellemuse.serverapp.service.DateTimeService;
import dellemuse.serverapp.service.ResourceThumbnailService;
import dellemuse.serverapp.service.language.LanguageObjectService;
import dellemuse.serverapp.service.language.LanguageService;
import io.wktui.event.UIEvent;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import wktui.base.UIEventListener;
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

	// public static JavaScriptResourceReference
	// getJavaScriptKbeeResourceReference() {
	// return new JavaScriptResourceReference(BasePage.class, "kbee.js");
	// }

	static {
		// xfavicon =
		// PropertiesFactory.getInstance("kbee").getProperties().getProperty("com.novamens.content.web.favicon",
		// "/images/favicon.gif");
		xlanguage = "English";
		xrobots = "NOINDEX, NOFOLLOW";
		xrating = "General";
	}

	private static final ResourceReference BOOTSTRAP_CSS = Bootstrap.getCssResourceReference();
	private static final ResourceReference BOOTSTRAP_JS = Bootstrap.getJavaScriptResourceReference();

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

	public abstract boolean hasAccessRight(Optional<User> ouser);

	public BasePage() {
		super();
	}

	public BasePage(PageParameters parameters) {
		super(parameters);
	}

	@Override
	public void onDetach() {
		super.onDetach();
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
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		addListeners();

		if (logger.isDebugEnabled())
			serverCall = new HashMap<String, Integer>();

		Optional<User> ou = getSessionUser();

		if (ou.isPresent() && ou.get().getLanguage() != null) {
			this.language = getSessionUser().get().getLanguage();
		} else {
			this.language = Locale.getDefault().getLanguage();
		}

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

		/**
		 * WebMarkupContainer html = new WebMarkupContainer("html"); //
		 * html.add(AttributeModifier.replace("lang", getPageLanguage())); //
		 * html.add(AttributeModifier.replace("dir", isRtl() ? "rtl" : "ltr")); //
		 * html.add(AttributeModifier.append("class", "base-page"));
		 * 
		 * html.add(AttributeModifier.replace( "data-bs-theme", new IModel<String>() {
		 * 
		 * private static final long serialVersionUID = 1L;
		 * 
		 * @Override public String getObject() { return isDarkTheme() ? "dark" : null; }
		 *           } )); super.add(html);
		 **/

	}

	protected boolean isDarkTheme() {

		return false;
	}

	protected void addListeners() {
	}

	@Override
	public void onBeforeRender() {
		super.onBeforeRender();

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

		response.render(JavaScriptHeaderItem.forReference(getApplication().getJavaScriptLibrarySettings().getJQueryReference()));
		response.render(JavaScriptHeaderItem.forReference(getApplication().getJavaScriptLibrarySettings().getWicketAjaxReference()));

		response.render(CssHeaderItem.forReference(BOOTSTRAP_CSS));
		response.render(JavaScriptHeaderItem.forReference(BOOTSTRAP_JS));

		response.render(CssHeaderItem.forReference(CSS));

		if (getCssResource() != null)
			response.render(CssHeaderItem.forReference(getCssResource()));

		if (isDarkTheme()) {
			String script = "document.documentElement.setAttribute('data-bs-theme','dark');";
			response.render(JavaScriptHeaderItem.forScript(script, "set-bs-theme"));
		}

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

	protected IModel<String> getLabel(String key, String... parameter) {
		StringResourceModel model = new StringResourceModel(key, this, null);
		model.setParameters((Object[]) parameter);
		return model;
	}

	/** Session User */

	public Optional<User> getSessionUser() {
		UserDBService service = (UserDBService) ServiceLocator.getInstance().getBean(UserDBService.class);
		User user = service.getSessionUser();
		if (user == null)
			return Optional.empty();
		return Optional.of(user);
	}

	/** DB Services */

	protected ArtExhibitionDBService getArtExhibitionDBService() {
		return (ArtExhibitionDBService) ServiceLocator.getInstance().getBean(ArtExhibitionDBService.class);
	}

	protected ArtExhibitionRecordDBService getArtExhibitionRecordDBService() {
		return (ArtExhibitionRecordDBService) ServiceLocator.getInstance().getBean(ArtExhibitionRecordDBService.class);
	}

	protected ArtExhibitionSectionDBService getArtExhibitionSectionDBService() {
		return (ArtExhibitionSectionDBService) ServiceLocator.getInstance().getBean(ArtExhibitionSectionDBService.class);
	}

	protected ArtExhibitionSectionRecordDBService getArtExhibitionSectionRecordDBService() {
		return (ArtExhibitionSectionRecordDBService) ServiceLocator.getInstance().getBean(ArtExhibitionSectionRecordDBService.class);
	}

	protected ArtExhibitionGuideDBService getArtExhibitionGuideDBService() {
		return (ArtExhibitionGuideDBService) ServiceLocator.getInstance().getBean(ArtExhibitionGuideDBService.class);
	}

	protected ArtExhibitionGuideRecordDBService getArtExhibitionGuideRecordDBService() {
		return (ArtExhibitionGuideRecordDBService) ServiceLocator.getInstance().getBean(ArtExhibitionGuideRecordDBService.class);
	}

	protected AudioStudioDBService getAudioStudioDBService() {
		return (AudioStudioDBService) ServiceLocator.getInstance().getBean(AudioStudioDBService.class);
	}

	protected ArtExhibitionItemDBService getArtExhibitionItemDBService() {
		return (ArtExhibitionItemDBService) ServiceLocator.getInstance().getBean(ArtExhibitionItemDBService.class);
	}

	protected ArtExhibitionItemRecordDBService getArtExhibitionItemRecordDBService() {
		return (ArtExhibitionItemRecordDBService) ServiceLocator.getInstance().getBean(ArtExhibitionItemRecordDBService.class);
	}

	protected ArtWorkDBService getArtWorkDBService() {
		return (ArtWorkDBService) ServiceLocator.getInstance().getBean(ArtWorkDBService.class);
	}

	protected ArtWorkRecordDBService getArtWorkRecordDBService() {
		return (ArtWorkRecordDBService) ServiceLocator.getInstance().getBean(ArtWorkRecordDBService.class);
	}

	protected InstitutionDBService getInstitutionDBService() {
		return (InstitutionDBService) ServiceLocator.getInstance().getBean(InstitutionDBService.class);
	}

	protected InstitutionRecordDBService getInstitutionRecordDBService() {
		return (InstitutionRecordDBService) ServiceLocator.getInstance().getBean(InstitutionRecordDBService.class);
	}

	protected GuideContentDBService getGuideContentDBService() {
		return (GuideContentDBService) ServiceLocator.getInstance().getBean(GuideContentDBService.class);
	}

	protected GuideContentRecordDBService getGuideContentRecordDBService() {
		return (GuideContentRecordDBService) ServiceLocator.getInstance().getBean(GuideContentRecordDBService.class);
	}

	protected PersonDBService getPersonDBService() {
		return (PersonDBService) ServiceLocator.getInstance().getBean(PersonDBService.class);
	}

	protected PersonRecordDBService getPersonRecordDBService() {
		return (PersonRecordDBService) ServiceLocator.getInstance().getBean(PersonRecordDBService.class);
	}

	protected SiteDBService getSiteDBService() {
		return (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
	}

	protected SiteRecordDBService getSiteRecordDBService() {
		return (SiteRecordDBService) ServiceLocator.getInstance().getBean(SiteRecordDBService.class);
	}

	protected ResourceDBService getResourceDBService() {
		return (ResourceDBService) ServiceLocator.getInstance().getBean(ResourceDBService.class);
	}

	public UserDBService getUserDBService() {
		return (UserDBService) ServiceLocator.getInstance().getBean(UserDBService.class);
	}

	public LanguageObjectService getLanguageObjectService() {
		return (LanguageObjectService) ServiceLocator.getInstance().getBean(LanguageObjectService.class);
	}

	protected DateTimeService getDateTimeService() {
		return (DateTimeService) ServiceLocator.getInstance().getBean(DateTimeService.class);
	}

	protected Optional<ArtExhibitionSection> getArtExhibitionSection(Long id) {
		return null;
	}

	protected LanguageService getLanguageService() {
		return (LanguageService) ServiceLocator.getInstance().getBean(LanguageService.class);
	}

	public Optional<ArtWork> findArtWorkByIdWithDeps(Long id) {
		ArtWorkDBService service = (ArtWorkDBService) ServiceLocator.getInstance().getBean(ArtWorkDBService.class);
		return service.findWithDeps(id);
	}

	public Optional<Person> getPerson(Long id) {
		PersonDBService service = (PersonDBService) ServiceLocator.getInstance().getBean(PersonDBService.class);
		return service.findById(id);
	}

	public Optional<Institution> getInstitution(Long id) {
		InstitutionDBService service = (InstitutionDBService) ServiceLocator.getInstance().getBean(InstitutionDBService.class);
		return service.findById(id);
	}

	/** Iterable */

	public Iterable<Person> getPersons() {
		PersonDBService service = (PersonDBService) ServiceLocator.getInstance().getBean(PersonDBService.class);
		return service.findAllSorted();
	}

	public Iterable<Site> getSites() {
		SiteDBService service = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
		return service.findAllSorted();
	}

	public Iterable<Site> getSites(ObjectState o1, ObjectState o2) {
		SiteDBService service = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
		return service.findAllSorted(o1, o2);
	}

	public Iterable<Site> getSites(Institution in) {
		InstitutionDBService service = (InstitutionDBService) ServiceLocator.getInstance().getBean(InstitutionDBService.class);
		return service.getSites(in.getId());
	}

	public Iterable<ArtExhibition> getSiteArtExhibitions(Site site) {
		SiteDBService service = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
		return service.getArtExhibitions(site.getId());
	}

	public Iterable<ArtExhibition> getSiteArtExhibitions(Site site, ObjectState os1) {
		SiteDBService service = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
		return service.getArtExhibitions(site.getId(), os1);
	}

	public Iterable<ArtExhibition> getSiteArtExhibitions(Site site, ObjectState os1, ObjectState os2) {
		SiteDBService service = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
		return service.getArtExhibitions(site.getId(), os1, os2);
	}

	public Iterable<ArtWork> getArtWorks(Site site) {
		SiteDBService service = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
		return service.getSiteArtWorks(site);
	}

	public Iterable<ArtWork> getArtWorks(Site site, ObjectState os1) {
		SiteDBService service = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
		return service.getSiteArtWorks(site, os1);
	}

	public Iterable<ArtWork> getArtWorks(Site site, ObjectState os1, ObjectState os2) {
		SiteDBService service = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
		return service.getSiteArtWorks(site, os1, os2);
	}

	public Iterable<User> getUsers() {
		UserDBService service = (UserDBService) ServiceLocator.getInstance().getBean(UserDBService.class);
		return service.findAllSorted();
	}

	public Iterable<User> getUsers(ObjectState os1, ObjectState os2) {
		UserDBService service = (UserDBService) ServiceLocator.getInstance().getBean(UserDBService.class);
		return service.findAllSorted(os1, os2);
	}

	public Iterable<GuideContent> getGuideContents(Site site) {
		SiteDBService service = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
		return service.getSiteGuideContent(site.getId());
	}

	public Iterable<GuideContent> getGuideContents(Site site, ObjectState os1) {
		SiteDBService service = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
		return service.getSiteGuideContent(site.getId(), os1);
	}

	public Iterable<GuideContent> getGuideContents(Site site, ObjectState os1, ObjectState os2) {
		SiteDBService service = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
		return service.getSiteGuideContent(site.getId(), os1, os2);
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

	public Optional<ArtExhibition> getArtExhibition(Long id) {
		ArtExhibitionDBService service = (ArtExhibitionDBService) ServiceLocator.getInstance().getBean(ArtExhibitionDBService.class);
		return service.findById(id);
	}

	public Optional<Site> findByIdWithDeps(Long id) {
		SiteDBService service = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
		return service.findWithDeps(id);
	}

	/** Object */

	public Optional<ArtWork> getArtWork(Long id) {
		ArtWorkDBService service = (ArtWorkDBService) ServiceLocator.getInstance().getBean(ArtWorkDBService.class);
		return service.findById(id);
	}

	public String getArtistStr(ArtWork aw) {

		if (!aw.isDependencies()) {
			aw = this.findArtWorkByIdWithDeps(aw.getId()).get();
		}

		StringBuilder info = new StringBuilder();
		int n = 0;

		for (Artist p : aw.getArtists()) {
			if (n++ > 0)
				info.append(", ");
			info.append(getLanguageObjectService().getPersonFirstLastName(p, getLocale()));
		}

		String str = TextCleaner.truncate(info.toString(), 220);
		return str;
	}

	protected Optional<ArtExhibitionItem> getArtExhibitionItem(Long id) {
		ArtExhibitionItemDBService service = (ArtExhibitionItemDBService) ServiceLocator.getInstance().getBean(ArtExhibitionItemDBService.class);
		return service.findById(id);
	}

	public Optional<ArtExhibitionGuide> getArtExhibitionGuide(Long id) {
		ArtExhibitionGuideDBService service = (ArtExhibitionGuideDBService) ServiceLocator.getInstance().getBean(ArtExhibitionGuideDBService.class);
		return service.findById(id);
	}

	public Optional<GuideContent> getGuideContent(Long id) {
		GuideContentDBService service = (GuideContentDBService) ServiceLocator.getInstance().getBean(GuideContentDBService.class);
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
		InstitutionDBService service = (InstitutionDBService) ServiceLocator.getInstance().getBean(InstitutionDBService.class);
		return service.findAllSorted();
	}

	public String getPresignedThumbnailSmall(Resource photo) {
		try {
			if (photo.isUsethumbnail()) {
				ResourceThumbnailService service = (ResourceThumbnailService) ServiceLocator.getInstance().getBean(ResourceThumbnailService.class);
				String url = service.getPresignedThumbnailUrl(photo, ThumbnailSize.SMALL);
				mark("PresignedThumbnailUrl - " + photo.getDisplayname());
				return url;
			} else {
				mark("PresignedUrl - " + photo.getDisplayname());
				ObjectStorageService service = (ObjectStorageService) ServiceLocator.getInstance().getBean(ObjectStorageService.class);
				return service.getClient().getPresignedObjectUrl(photo.getBucketName(), photo.getObjectName());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public IModel<String> getObjectSubtitle(MultiLanguageObject o) {
		StringBuilder str = new StringBuilder();
		str.append(getLanguageObjectService().getObjectSubtitle(o, getLocale()));
		return Model.of(str.toString());
	}

	public IModel<String> getObjectTitle(MultiLanguageObject o) {
		StringBuilder str = new StringBuilder();
		str.append(getLanguageObjectService().getObjectDisplayName(o, getLocale()));
		return Model.of(str.toString());
	}

	public ArtExhibition createExhibition(Site site) {

		return getArtExhibitionDBService().create("new", site, getUserDBService().findRoot());
	}

	@SuppressWarnings("unchecked")
	public void fireScanAll(UIEvent event) {
		for (UIEventListener<UIEvent> listener : getBehaviors(UIEventListener.class)) {
			if (listener.handle(event)) {
				listener.onEvent(event);
			}
		}
		fire(event, getPage().iterator(), false);
	}

	@SuppressWarnings("unchecked")
	public void fire(UIEvent event) {
		boolean handled = false;
		for (UIEventListener<UIEvent> listener : getPage().getBehaviors(UIEventListener.class)) {
			if (listener.handle(event)) {
				listener.onEvent(event);
				handled = true;
				break;
			}
		}
		if (!handled)
			fire(event, getPage().iterator());
	}

	public boolean fire(UIEvent event, Iterator<Component> components) {
		return fire(event, components, true);
	}

	@SuppressWarnings("unchecked")
	public boolean fire(UIEvent event, Iterator<Component> components, boolean stop_first_hit) {
		boolean handled = false;
		while (components.hasNext()) {
			Component component = components.next();
			for (UIEventListener<UIEvent> listener : component.getBehaviors(UIEventListener.class)) {
				if (listener.handle(event)) {
					listener.onEvent(event);
					if (stop_first_hit) {
						handled = true;
						break;
					}
				}
			}
			if (!handled) {
				if (component instanceof MarkupContainer) {
					handled = fire(event, ((MarkupContainer) component).iterator(), stop_first_hit);
				}
			} else {
				break;
			}
		}
		return handled;
	}

}
