package dellemuse.serverapp.branded;

import java.util.List;
import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.branded.panel.BrandedArtExhibitionGuidePanel;
import dellemuse.serverapp.branded.panel.BrandedGlobalTopPanel;
import dellemuse.serverapp.branded.panel.BrandedSiteSearcherPanel;
import dellemuse.serverapp.global.JumboPageHeaderPanel;

import dellemuse.serverapp.page.MultiLanguageObjectPage;

import dellemuse.serverapp.page.model.ObjectModel;

import dellemuse.serverapp.person.ServerAppConstant;
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

import wktui.base.INamedTab;
import wktui.base.InvisiblePanel;
import wktui.base.NamedTab;

@MountPath("/ag/guide/${id}")
public class BrandedArtExhibitionGuidePage extends MultiLanguageObjectPage<ArtExhibitionGuide, ArtExhibitionGuideRecord> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(BrandedArtExhibitionGuidePage.class.getName());

	private IModel<Site> siteModel;
	private IModel<ArtExhibition> artExhibitionModel;

	private List<IModel<GuideContent>> guideContentSearchList;
	private List<IModel<ArtExhibitionGuide>> artExhibitionSearchList;

	public BrandedArtExhibitionGuidePage() {
		super();
	}

	public BrandedArtExhibitionGuidePage(PageParameters parameters) {
		super(parameters);
	}

	public BrandedArtExhibitionGuidePage(IModel<ArtExhibitionGuide> model) {
		this(model, null);
	}

	public BrandedArtExhibitionGuidePage(IModel<ArtExhibitionGuide> model, List<IModel<ArtExhibitionGuide>> list) {
		super(model, list);
	}
	
	protected List<Language> getSupportedLanguages() {
		return  getSiteModel().getObject().getLanguages();
	}

	
	@Override
	protected void addListeners() {
		super.addListeners();
		
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
				setResponsePage( page );
			}
		});
	}
	
	
	@Override
	protected boolean isDarkTheme() {
		return true;
	}
	
	@Override
	public boolean hasAccessRight(Optional<User> ouser) {

		if (getModel().getObject().getState() == ObjectState.DELETED)
			return false;

		 if (getModel().getObject().getState()==ObjectState.EDITION)
		 return false;

		
		return true;
	}

	protected IModel<String> getMainClass() {
		return Model.of("branded text-bg-dark");
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
	}

	@Override
	protected Panel createGlobalTopPanel(String id) {
		return new BrandedGlobalTopPanel("top-panel", getSiteModel());
	}

	@Override
	protected Panel createSearchPanel() {
		return new BrandedSiteSearcherPanel("globalSearch", getSiteModel(),  getGuideContentSearchList(), getArtExhibitionSearchList());
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
	protected Panel createHeaderPanel() {

		try {

			BreadCrumb<Void> bc = createBreadCrumb();

			bc.addElement(new HREFBCElement("/ag/" + getSiteModel().getObject().getId().toString(), getLabel("audio-guides")));
			bc.addElement(new BCElement(getObjectTitle(getArtExhibitionModel().getObject())));

			JumboPageHeaderPanel<Site> ph = new JumboPageHeaderPanel<Site>("page-header", getSiteModel(), getObjectTitle(getArtExhibitionModel().getObject()));
			ph.setBreadCrumb(bc);

			ph.add(new org.apache.wicket.AttributeModifier("class", "row mt-0 mb-0 text-center imgReduced"));

			ph.setImageLinkCss("jumbo-img jumbo-md mb-2 mb-lg-0 border-none bg-dark");
			ph.setHeaderCss("mb-0 mt-0 pt-0 pb-2 border-none");

			boolean isPhoto = false;

			ph.setContext(getLabel("exhibition-guide"));

			/**
			if (getModel().getObject().getPhoto() != null) {
				ph.setPhotoModel(new ObjectModel<Resource>(getModel().getObject().getPhoto()));
				isPhoto = true;
			} else if (getArtExhibitionModel().getObject().getPhoto() != null) {
				ph.setPhotoModel(new ObjectModel<Resource>(getArtExhibitionModel().getObject().getPhoto()));
				isPhoto = true;
			}

			if (!isPhoto) {
				ph.setHeaderCss("mb-0 mt-0 pt-0 pb-4 border-none");
				ph.setIcon(GuideContent.getIcon());
			}
*/
			
			//IModel<String> s = getObjectSubtitle(getArtExhibitionModel().getObject());

			//if (s.getObject().length() > 0)
		//		ph.setTagline(s);

			//else if (getArtExhibitionModel().getObject().getSubtitle() != null)
			//	ph.setTagline(Model.of(getArtExhibitionModel().getObject().getSubtitle()));
			return ph;

		} catch (Exception e) {
			logger.error(e);
			return new ErrorPanel("page-header", e);
		}
	}

	@Override
	protected IRequestablePage getObjectPage(IModel<ArtExhibitionGuide> model, List<IModel<ArtExhibitionGuide>> list) {
		return new BrandedArtExhibitionGuidePage(model, list);
	}

	@Override
	protected void setUpModel() {
		super.setUpModel();

		if (!getModel().getObject().isDependencies()) {
			Optional<ArtExhibitionGuide> o_i = getArtExhibitionGuideDBService().findWithDeps(getModel().getObject().getId());
			setModel(new ObjectModel<ArtExhibitionGuide>(o_i.get()));
		}

		Optional<ArtExhibition> o_i = getArtExhibitionDBService().findWithDeps(getModel().getObject().getArtExhibition().getId());
		seArtExhibitionModel(new ObjectModel<ArtExhibition>(o_i.get()));

		Optional<Site> o_s = getSiteDBService().findWithDeps(o_i.get().getSite().getId());
		setSiteModel(new ObjectModel<Site>(o_s.get()));
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
