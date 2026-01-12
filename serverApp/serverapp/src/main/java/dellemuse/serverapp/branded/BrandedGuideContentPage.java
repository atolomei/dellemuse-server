package dellemuse.serverapp.branded;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.media.audio.Audio;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.UrlResourceReference;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.ArtExhibitionGuideModel;
import dellemuse.model.ArtWorkModel;
import dellemuse.model.GuideContentModel;
import dellemuse.model.SiteModel;
import dellemuse.model.logging.Logger;
import dellemuse.model.ref.RefResourceModel;
import dellemuse.serverapp.branded.panel.BrandedGlobalTopPanel;
import dellemuse.serverapp.branded.panel.BrandedGuideContentPanel;
import dellemuse.serverapp.branded.panel.BrandedSiteSearcherPanel;

import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.page.MultiLanguageObjectPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.site.SitePage;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionGuideRecord;
import dellemuse.serverapp.serverdb.model.record.GuideContentRecord;

import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.breadcrumb.Navigator;
import io.wktui.nav.toolbar.ToolbarItem;
import wktui.base.DummyBlockPanel;
import wktui.base.INamedTab;
import wktui.base.InvisiblePanel;
import wktui.base.NamedTab;

@MountPath("/ag/guidecontent/${id}")
public class BrandedGuideContentPage extends MultiLanguageObjectPage<GuideContent, GuideContentRecord> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(BrandedGuideContentPage.class.getName());

	private IModel<ArtExhibitionGuide> artExhibitionGuideModel;
	private IModel<ArtExhibition> artExhibitionModel;
	private IModel<ArtExhibitionItem> artExhibitionItemModel;
	private IModel<Site> siteModel;
	private IModel<ArtWork> artWorkModel;

	
	protected List<Language> getSupportedLanguages() {
		return  getSiteModel().getObject().getLanguages();
	}

	
	@Override
	protected boolean isDarkTheme() {
		return true;
	}
	
	@Override
	protected Optional<GuideContentRecord> loadTranslationRecord(String lang) {
		return getGuideContentRecordDBService().findByGuideContent(getModel().getObject(), lang);
	}

	@Override
	protected GuideContentRecord createTranslationRecord(String lang) {
		return getGuideContentRecordDBService().create(getModel().getObject(), lang, getSessionUser().get());
	}

	@Override
	protected Panel createGlobalTopPanel(String id) {
		return new BrandedGlobalTopPanel("top-panel", getSiteModel());
	}

	@Override
	protected Panel createSearchPanel() {
		return new BrandedSiteSearcherPanel("globalSearch", getSiteModel());
	}

	@Override
	public boolean hasAccessRight(Optional<User> ouser) {

		if (getModel().getObject().getState() == ObjectState.DELETED)
			return false;

		// if (getModel().getObject().getState()==ObjectState.EDITION)
		// return false;

		if (getSiteModel().getObject().getState() == ObjectState.DELETED)
			return false;

		if (getModel().getObject().getState() == ObjectState.DELETED)
			return false;

		return true;
	}

	public BrandedGuideContentPage() {
		super();
	}

	public BrandedGuideContentPage(PageParameters parameters) {
		super(parameters);
	}

	public BrandedGuideContentPage(IModel<GuideContent> model) {
		this(model, null);
	}

	public BrandedGuideContentPage(IModel<GuideContent> model, List<IModel<GuideContent>> list) {
		super(model, list);
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

		if (artExhibitionGuideModel != null)
			artExhibitionGuideModel.detach();

		if (artExhibitionModel != null)
			artExhibitionModel.detach();

		if (artExhibitionItemModel != null)
			artExhibitionItemModel.detach();

		if (artWorkModel != null)
			artWorkModel.detach();
	}

	public IModel<ArtExhibition> getArtExhibitionModel() {
		return artExhibitionModel;
	}

	public IModel<ArtExhibitionGuide> getArtExhibitionGuideModel() {
		return artExhibitionGuideModel;
	}

	public void setArtExhibitionGuideModel(IModel<ArtExhibitionGuide> artExhibitionGuideModel) {
		this.artExhibitionGuideModel = artExhibitionGuideModel;
	}

	public void setArtExhibitionModel(IModel<ArtExhibition> artExhibitionModel) {
		this.artExhibitionModel = artExhibitionModel;
	}

	public void setArtExhibitionItemModel(IModel<ArtExhibitionItem> artExhibitionItemModel) {
		this.artExhibitionItemModel = artExhibitionItemModel;
	}

	public IModel<ArtExhibitionItem> getArtExhibitionItemModel() {
		return artExhibitionItemModel;
	}

	public IModel<ArtWork> getArtWorkModel() {
		return artWorkModel;
	}

	public void setArtWorkModel(IModel<ArtWork> artWorkModel) {
		this.artWorkModel = artWorkModel;
	}

	@Override
	protected boolean isLanguage() {
		return false;
	}

	@Override
	protected boolean isAudioAutoGenerate() {
		return true;
	}

	protected void setUpModel() {
		super.setUpModel();

		if (!getModel().getObject().isDependencies()) {
			Optional<GuideContent> o_i = getGuideContentDBService().findWithDeps(getModel().getObject().getId());
			setModel(new ObjectModel<GuideContent>(o_i.get()));
		}

		ArtExhibitionItem item = getModel().getObject().getArtExhibitionItem();
		Optional<ArtExhibitionItem> o_i = getArtExhibitionItemDBService().findWithDeps(item.getId());
		this.setArtExhibitionItemModel(new ObjectModel<ArtExhibitionItem>(o_i.get()));

		ArtWork aw = getArtWorkDBService().findWithDeps(o_i.get().getArtWork().getId()).get();
		setArtWorkModel(new ObjectModel<ArtWork>(aw));

		ArtExhibitionGuide guide = getModel().getObject().getArtExhibitionGuide();
		Optional<ArtExhibitionGuide> o_g = getArtExhibitionGuideDBService().findWithDeps(guide.getId());
		this.setArtExhibitionGuideModel(new ObjectModel<ArtExhibitionGuide>(o_g.get()));

		ArtExhibition a = item.getArtExhibition();
		Optional<ArtExhibition> o_a = getArtExhibitionDBService().findWithDeps(a.getId());
		this.setArtExhibitionModel(new ObjectModel<ArtExhibition>(o_a.get()));

		Optional<Site> o_s = getSiteDBService().findWithDeps(getArtExhibitionModel().getObject().getSite().getId());
		setSiteModel(new ObjectModel<Site>(o_s.get()));
	}

	@Override
	protected void addListeners() {
		super.addListeners();

	}

	@Override
	protected Class<?> getTranslationClass() {
		return GuideContentRecord.class;
	}

	@Override
	protected List<ToolbarItem> getToolbarItems() {
		return null;
	}

	protected boolean isAudioVisible() {
		return true;
	}

	@Override
	protected IRequestablePage getObjectPage(IModel<GuideContent> model, List<IModel<GuideContent>> list) {
		return new BrandedGuideContentPage(model, list);
	}

	@Override
	protected Optional<GuideContent> getObject(Long id) {
		return getGuideContent(id);
	}

	@Override
	protected IModel<String> getPageTitle() {
		return new Model<String>(getModel().getObject().getName());
	}

	@Override
	protected Panel createHeaderPanel() {

		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(new HREFBCElement("/ag/" + getSiteModel().getObject().getId().toString(), getLabel("audio-guides")));
		bc.addElement(new HREFBCElement("/ag/guide/" + getArtExhibitionGuideModel().getObject().getId().toString(), getObjectTitle(getArtExhibitionGuideModel().getObject())));

		bc.addElement(new BCElement(getObjectTitle(getModel().getObject())));

		JumboPageHeaderPanel<Site> ph = new JumboPageHeaderPanel<Site>("page-header", getSiteModel(), getObjectTitle(getModel().getObject()));
		ph.setImageLinkCss("jumbo-img jumbo-md mb-0 mb-lg-0  border bg-none");
		ph.setHeaderCss("mb-0 mt-0 pt-0 pb-0 border-none");
		ph.add(new org.apache.wicket.AttributeModifier("class", "row mt-0 mb-0 text-center  "));

		boolean isPhoto = false;

		ph.setContext(getLabel("guide-content"));

		if (getModel().getObject().getPhoto() != null) {
			ph.setPhotoModel(new ObjectModel<Resource>(getModel().getObject().getPhoto()));
			isPhoto = true;
		} else if (getArtWorkModel().getObject().getPhoto() != null) {
			ph.setPhotoModel(new ObjectModel<Resource>(getArtWorkModel().getObject().getPhoto()));
			isPhoto = true;
		}

		if (!isPhoto) {
			ph.setHeaderCss("mb-0 mt-0 pt-0 pb-4 border-none");
			ph.setIcon(GuideContent.getIcon());
		}

		ph.setBreadCrumb(bc);
		ph.setTagline(Model.of(getArtistStr(getArtWorkModel().getObject())));

		if (getList() != null && getList().size() > 0) {
			Navigator<GuideContent> nav = new Navigator<GuideContent>("navigator", getCurrent(), getList()) {
				private static final long serialVersionUID = 1L;

				@Override
				public void navigate(int current) {
					setResponsePage(new BrandedGuideContentPage(getList().get(current), getList()));
				}
			};
			bc.setNavigator(nav);
		}

		return ph;
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
	}

	@Override
	protected List<INamedTab> getInternalPanels() {

		List<INamedTab> tabs = super.createInternalPanels();

		NamedTab tab_1 = new NamedTab(Model.of("guide"), ServerAppConstant.guide_content_info) {

			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return new BrandedGuideContentPanel(panelId, getModel(), getSiteModel());
			}
		};
		tabs.add(tab_1);

		if (getStartTab() == null)
			setStartTab(ServerAppConstant.guide_content_info);

		return tabs;
	}

}
