package dellemuse.serverapp.branded;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.branded.panel.BrandedArtExhibitionGuidePanel;
import dellemuse.serverapp.branded.panel.BrandedGlobalTopPanel;
import dellemuse.serverapp.branded.panel.BrandedSiteSearcherPanel;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.icons.Icons;
import dellemuse.serverapp.page.MultiLanguageObjectPage;

import dellemuse.serverapp.page.model.ObjectModel;

import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.AccesibilityMode;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionGuideRecord;
import io.wktui.error.ErrorPanel;
import io.wktui.event.UIEvent;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;

import io.wktui.nav.toolbar.ToolbarItem;
import jakarta.servlet.http.Cookie;
import wktui.base.INamedTab;
import wktui.base.InvisiblePanel;
import wktui.base.NamedTab;

/**
 * 
 * 
 * ArtExhibition
 * 
 * 
 */
@MountPath("/ag/guide/${id}")
public class BrandedArtExhibitionGuidePage extends MultiLanguageObjectPage<ArtExhibitionGuide, ArtExhibitionGuideRecord> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(BrandedArtExhibitionGuidePage.class.getName());

	private IModel<Site> siteModel;
	private IModel<ArtExhibition> artExhibitionModel;
	private List<IModel<GuideContent>> guideContentSearchList;
	private List<IModel<ArtExhibitionGuide>> artExhibitionSearchList;

	private String lang;
	private Locale locale = null;

	private boolean modelAlreadySet = false;
	private AccesibilityMode accesibilityMode = AccesibilityMode.GENERAL;

	private StringValue exhibitionIdValue;
	private boolean isError = false;
	private String errorStr = "";

	public BrandedArtExhibitionGuidePage() {
		super();
	}

	public BrandedArtExhibitionGuidePage(PageParameters parameters) {
		super(parameters);

		exhibitionIdValue = parameters.get("id");
		if (exhibitionIdValue.isEmpty()) {
			isError = true;
			errorStr = "exhibition id is missing";
		}
		setCookieLocale();
		setUpModel();

	}

	public BrandedArtExhibitionGuidePage(IModel<ArtExhibitionGuide> model) {
		this(model, null, null);
	}

	public BrandedArtExhibitionGuidePage(IModel<ArtExhibitionGuide> model, List<IModel<ArtExhibitionGuide>> list, String lang) {
		super(model, list);
		this.lang = lang;
		setCookieLocale();
		setUpModel();
	}

	protected boolean isError() {
		return isError;
	}

	protected String getErrorStr() {
		return errorStr;
	}

	@Override
	public boolean hasAccessRight(Optional<User> ouser) {

		if (getSiteModel().getObject().getState() != ObjectState.PUBLISHED)
			return false;

		if (getModel().getObject().getState() == ObjectState.EDITION)
			return false;

		if (!getSiteModel().getObject().isPublicPortalEnabled())
			return false;

		return true;
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

	}

	public IModel<Site> getSiteModel() {
		return this.siteModel;
	}

	public List<IModel<GuideContent>> getGuideContentSearchList() {
		return guideContentSearchList;
	}

	public List<IModel<ArtExhibitionGuide>> getArtExhibitionSearchList() {
		return artExhibitionSearchList;
	}

	public void setGuideContentSearchList(List<IModel<GuideContent>> guideContentSearchList) {
		this.guideContentSearchList = guideContentSearchList;
	}

	public void setArtExhibitionSearchList(List<IModel<ArtExhibitionGuide>> artExhibitionSearchList) {
		this.artExhibitionSearchList = artExhibitionSearchList;
	}

	public void setSiteModel(IModel<Site> siteModel) {
		this.siteModel = siteModel;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (this.siteModel != null)
			this.siteModel.detach();

		if (artExhibitionModel != null)
			this.artExhibitionModel.detach();

		if (guideContentSearchList != null)
			guideContentSearchList.forEach(m -> m.detach());

		if (artExhibitionSearchList != null)
			artExhibitionSearchList.forEach(m -> m.detach());

	}

	protected IModel<String> getMainClass() {
		return Model.of("branded text-bg-dark");
	}

	protected List<Language> getSupportedLanguages() {
		return getSiteModel().getObject().getLanguages();
	}

	@Override
	public Locale getLocale() {

		if (locale != null)
			return locale;

		if (lang != null) {
			logger.debug("setting language from parameter -> " + lang);
			locale = Locale.forLanguageTag(lang);
			getSession().setLocale(Locale.forLanguageTag(lang));
		} else {
			WebRequest request = (WebRequest) RequestCycle.get().getRequest();
			Cookie cookie = request.getCookie("lang");

			if (cookie != null) {
				String value = cookie.getValue();
				logger.debug("setting language from cookie -> " + value);
				locale = Locale.forLanguageTag(value);
				getSession().setLocale(Locale.forLanguageTag(value));

			} else if (getSessionUser().isEmpty()) {

				if (getSiteModel() == null) {
					logger.error("site model is null, cannot set language");
					return Locale.getDefault();
				}

				Language la = Language.of(getSiteModel().getObject().getMasterLanguage());
				String code = la.getLanguageCode();
				logger.debug("setting language from site master language -> " + code);
				locale = Locale.forLanguageTag(code);
				getSession().setLocale(Locale.forLanguageTag(code));

			} else {

				if (getSiteModel() != null) {
					Language la = Language.of(getSiteModel().getObject().getMasterLanguage());
					String code = la.getLanguageCode();
					logger.debug("setting language from site master language -> " + code);
					locale = Locale.forLanguageTag(code);
					getSession().setLocale(locale);

				} else {
					locale = Locale.getDefault();
					getSession().setLocale(locale);
				}

			}
		}
		return locale;
	}

	public AccesibilityMode getAccesibilityMode() {
		return accesibilityMode;
	}

	public void setAccesibilityMode(AccesibilityMode accesibilityMode) {
		this.accesibilityMode = accesibilityMode;
	}

	@Override
	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<AccesibilityAjaxEvent>() {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof AccesibilityAjaxEvent)
					return true;
				return false;
			}

			@Override
			public void onEvent(AccesibilityAjaxEvent event) {
				logger.debug("setting accesibility mode to " + event.getMode());
				setAccesibilityMode(event.getMode());
				event.getTarget().add(BrandedArtExhibitionGuidePage.this);
			}
		});

		add(new io.wktui.event.WicketEventListener<LangEvent>() {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof LangEvent)
					return true;
				return false;
			}

			@Override
			public void onEvent(LangEvent event) {
				setResponsePage(new BrandedArtExhibitionGuidePage(BrandedArtExhibitionGuidePage.this.getModel(), BrandedArtExhibitionGuidePage.this.getList(), event.getLang()));
			}
		});

		add(new io.wktui.event.WicketEventListener<SearchAudioEvent>() {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof SearchAudioEvent)
					return true;
				return false;
			}

			@Override
			public void onEvent(SearchAudioEvent event) {
				BrandedArtExhibitionGuidePage page = new BrandedArtExhibitionGuidePage(BrandedArtExhibitionGuidePage.this.getModel());
				page.setArtExhibitionSearchList(event.getArtExhibitionGuidesList());
				page.setGuideContentSearchList(event.getGuideContentsList());
				setResponsePage(page);
			}
		});
	}

	protected void setCookieLocale() {

		if (lang != null) {
			logger.debug("setting language from parameter -> " + lang);
			locale = Locale.forLanguageTag(lang);
			getSession().setLocale(Locale.forLanguageTag(lang));
			return;
		}

		WebRequest request = (WebRequest) RequestCycle.get().getRequest();
		Cookie cookie = request.getCookie("lang");

		if (cookie != null) {
			String value = cookie.getValue();
			logger.debug("setting language from cookie -> " + value);
			locale = Locale.forLanguageTag(value);
			lang = value;
			getSession().setLocale(Locale.forLanguageTag(value));
		}

		Cookie accCookie = request.getCookie("accessible");

		if (accCookie != null) {
			String value = accCookie.getValue();
			boolean isAccesible = value.equals("true");
			if (isAccesible) {
				logger.debug("setting accesibility mode to accessible from cookie");
				this.accesibilityMode = AccesibilityMode.ACCESIBLE;
			} else {
				logger.debug("setting accesibility mode to general from cookie");
				this.accesibilityMode = AccesibilityMode.GENERAL;
			}
		}

	}

	@Override
	protected boolean isDarkTheme() {
		return true;
	}

	@Override
	protected Panel createGlobalTopPanel(String id) {
		return new BrandedGlobalTopPanel("top-panel", getSiteModel());
	}


	@Override
	protected Panel createInitialSearchPanel() {
	if (getGuideContentSearchList() == null && getArtExhibitionSearchList() == null)
		return new InvisiblePanel("globalSearch");
	return createSearchPanel();
	}

	
	@Override
	protected Panel createSearchPanel() {
		return new BrandedSiteSearcherPanel("globalSearch", getSiteModel(), getGuideContentSearchList(), getArtExhibitionSearchList(), getAccesibilityMode());
	}

	public void setArtExhibitionModel(IModel<ArtExhibition> artExhibitionModel) {
		this.artExhibitionModel = artExhibitionModel;
	}

	@Override
	protected Optional<ArtExhibitionGuideRecord> loadTranslationRecord(String lang) {
		return getArtExhibitionGuideRecordDBService().findByArtExhibitionGuide(getModel().getObject(), lang);
	}

	@Override
	protected ArtExhibitionGuideRecord createTranslationRecord(String lang) {
		return getArtExhibitionGuideRecordDBService().create(getModel().getObject(), lang, getSessionUser().get());
	}

	@Override
	protected boolean isAudioVisible() {
		return true;
	}

	@Override
	protected boolean isAudioAutoGenerate() {
		return true;
	}

	@Override
	protected boolean isLanguage() {
		return false;
	}

	@Override
	protected List<ToolbarItem> getToolbarItems() {
		return null;
	}

	protected WebMarkupContainer getGuideContentsPanel(String id) {
		return new InvisiblePanel(id);
	}

	@Override
	protected List<INamedTab> getInternalPanels() {

		List<INamedTab> tabs = super.createInternalPanels();

		NamedTab tab_2 = new NamedTab(Model.of(ServerAppConstant.branded_exhibition_guide), ServerAppConstant.branded_exhibition_guide) {

			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return new BrandedArtExhibitionGuidePanel(panelId, BrandedArtExhibitionGuidePage.this.getModel(), BrandedArtExhibitionGuidePage.this.getSiteModel());
			}
		};
		tabs.add(tab_2);

		super.setStartTab(ServerAppConstant.branded_exhibition_guide);

		return tabs;
	}

	@Override
	protected Optional<ArtExhibitionGuide> getObject(Long id) {
		return super.getArtExhibitionGuide(id);
	}

	@Override
	protected IModel<String> getPageTitle() {
		return new Model<String>(getModel().getObject().getName());
	}

	@Override
	public BreadCrumb<Void> createBreadCrumb() {
		BreadCrumb<Void> bc = new BreadCrumb<>();
		// bc.addElement(new HREFBCElement("/home", getLabel("home")));
		return bc;
	}

	@Override
	protected Panel createHeaderPanel() {

		try {
			BreadCrumb<Void> bc = createBreadCrumb();

			bc.addElement(new HREFBCElement("/" + ServerConstant.AG + "/" + getSiteModel().getObject().getId().toString(), getLabel("exhibitions")));
			bc.addElement(new BCElement(getObjectTitle(getArtExhibitionModel().getObject())));

			StringBuilder str = new StringBuilder();
			str.append(getObjectTitle(getModel().getObject()).getObject());
			str.append(getModel().getObject().isAccessible() ? Icons.ACCESIBLE_ICON_JUMBO_HTML : "");

			JumboPageHeaderPanel<Site> ph = new JumboPageHeaderPanel<Site>("page-header", getSiteModel(), Model.of(str.toString()));
			ph.setBreadCrumb(bc);

			ph.add(new org.apache.wicket.AttributeModifier("class", "row mt-0 mb-0 text-center imgReduced"));

			ph.setImageLinkCss("jumbo-img p-1 border jumbo-md mb-0 mb-lg-0 border-none bg-dark");
			ph.setHeaderCss("mb-0 mt-0 pt-0 pb-2 border-none");

			ph.setContext(getLabel("exhibition-guide"));

			boolean isPhoto = false;
			
			if (getArtExhibitionModel().getObject().getPhoto() != null) {
				Resource r=getArtExhibitionModel().getObject().getPhoto();
				ph.setPhotoModel( new ObjectModel<Resource>(r));
				isPhoto = true;
			}

			if (!isPhoto) {
				ph.setHeaderCss("mb-0 mt-0 pt-0 pb-4 border-none");
				ph.setIcon(ArtExhibitionGuide.getIcon());
			}
			else {
				ph.setImageLinkCss("jumbo-img p-1 border jumbo-md mb-0 mb-lg-0   bg-dark");
				ph.setImageContainerCss("row mt-4 mb-0 text-center");
			}
			
			
			
			
			
			return ph;

		} catch (Exception e) {
			logger.error(e);
			return new ErrorPanel("page-header", e);
		}
	}

	@Override
	protected IRequestablePage getObjectPage(IModel<ArtExhibitionGuide> model, List<IModel<ArtExhibitionGuide>> list) {
		return new BrandedArtExhibitionGuidePage(model, list, null);
	}

	@Override
	protected void setUpModel() {

		if (modelAlreadySet)
			return;

		if (getModel() == null) {
			setUpModelFromParameters();
			modelAlreadySet = true;
			return;
		}

		if (!getModel().getObject().isDependencies()) {
			Optional<ArtExhibitionGuide> o_i = getArtExhibitionGuideDBService().findWithDeps(getModel().getObject().getId());
			setModel(new ObjectModel<ArtExhibitionGuide>(o_i.get()));
		}

		Optional<ArtExhibition> o_i = getArtExhibitionDBService().findWithDeps(getModel().getObject().getArtExhibition().getId());
		seArtExhibitionModel(new ObjectModel<ArtExhibition>(o_i.get()));

		Optional<Site> o_s = getSiteDBService().findWithDeps(o_i.get().getSite().getId());
		setSiteModel(new ObjectModel<Site>(o_s.get()));

		getPageParameters().set("id", getArtExhibitionModel().getObject().getId());
		
		modelAlreadySet = true;
	}
	
	
	private ArtExhibitionGuide selectGuide(ArtExhibition a) {
		
		ArtExhibitionGuide guide = null;

		List<ArtExhibitionGuide> list = getArtExhibitionDBService().getArtExhibitionGuides(a);

		if (getAccesibilityMode() == AccesibilityMode.ACCESIBLE) {
			for (ArtExhibitionGuide g : list) {
				if (g.isAccessible()) {
					guide = getArtExhibitionGuideDBService().findWithDeps(g.getId()).get();
					break;
				}
			}
			if (guide == null) {
				for (ArtExhibitionGuide g : list) {
					if (!g.isAccessible()) {
						guide = getArtExhibitionGuideDBService().findWithDeps(g.getId()).get();
						break;
					}
				}
			}
		} else {
			for (ArtExhibitionGuide g : list) {
				if (!g.isAccessible()) {
					guide = getArtExhibitionGuideDBService().findWithDeps(g.getId()).get();
					break;
				}
			}
		}
		
		
		return guide;
	}

	protected void setUpModelFromParameters() {

		if (this.exhibitionIdValue == null) {
			isError = true;
			errorStr = "exhibition id parameter is missing";
			return;
		}

		Optional<ArtExhibition> o = getArtExhibitionDBService().findWithDeps(Long.valueOf(this.exhibitionIdValue.toLong()));

		if (o.isEmpty()) {
			isError = true;
			errorStr = "exhibitionId not found for id " + this.exhibitionIdValue.toString();
			return;
		}

		setArtExhibitionModel(new ObjectModel<ArtExhibition>(o.get()));

		Optional<Site> o_s = getSiteDBService().findWithDeps(getArtExhibitionModel().getObject().getSite().getId());
		setSiteModel(new ObjectModel<Site>(o_s.get()));
		

		ArtExhibitionGuide guide = selectGuide(o.get());

		if (guide == null) {
			isError = true;
			errorStr = "no guide found for exhibition " + o.get().getName() + " with accesibility mode " + getAccesibilityMode();
			return;
		}

		setModel(new ObjectModel<ArtExhibitionGuide>(guide));


	}

	protected void seArtExhibitionModel(IModel<ArtExhibition> model) {
		this.artExhibitionModel = model;
	}

	protected IModel<ArtExhibition> getArtExhibitionModel() {
		return this.artExhibitionModel;
	}

	@Override
	protected Class<?> getTranslationClass() {
		return ArtExhibitionGuideRecord.class;
	}

}
