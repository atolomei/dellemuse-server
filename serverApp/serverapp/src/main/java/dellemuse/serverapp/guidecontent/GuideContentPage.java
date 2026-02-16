package dellemuse.serverapp.guidecontent;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.artexhibition.ArtExhibitionEXTNavDropDownMenuToolbarItem;

import dellemuse.serverapp.artexhibitionguide.ArtExhibitionGuideEXTNavDropDownMenuToolbarItem;

import dellemuse.serverapp.editor.ObjectMarkAsDeleteEvent;
import dellemuse.serverapp.editor.ObjectRecordEditor;
import dellemuse.serverapp.editor.ObjectRestoreEvent;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.icons.Icons;
import dellemuse.serverapp.page.MultiLanguageObjectPage;

import dellemuse.serverapp.page.model.ObjectModel;

import dellemuse.serverapp.page.site.SiteNavDropDownMenuToolbarItem;
import dellemuse.serverapp.page.site.SitePage;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.GuideContentRecord;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
import dellemuse.serverapp.serverdb.model.security.RoleInstitution;
import dellemuse.serverapp.serverdb.model.security.RoleSite;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.event.SimpleAjaxWicketEvent;
import io.wktui.event.SimpleWicketEvent;
import io.wktui.event.UIEvent;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.breadcrumb.Navigator;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;

import wktui.base.INamedTab;
import wktui.base.NamedTab;

/**
 * site foto Info - exhibitions
 */

@MountPath("/guidecontent/${id}")
public class GuideContentPage extends MultiLanguageObjectPage<GuideContent, GuideContentRecord> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(GuideContentPage.class.getName());

	private IModel<ArtExhibitionGuide> artExhibitionGuideModel;
	private IModel<ArtExhibition> artExhibitionModel;
	private IModel<ArtExhibitionItem> artExhibitionItemModel;
	private IModel<Site> siteModel;
	private IModel<ArtWork> artWorkModel;
	private GuideContentEditor editor;
	private JumboPageHeaderPanel<GuideContent> header;
	private List<ToolbarItem> list;

	
	@Override
	protected void onEditRecord(AjaxRequestTarget target, String lang) {
		if (getRecordEditors().get(lang) instanceof ObjectRecordEditor)
			((ObjectRecordEditor<?,?>) getRecordEditors().get(lang)).edit(target);
	
	}

	@Override
	protected Panel getTranslateRecordEditor(String id, String lang) {

	
		if (getRecordEditors().containsKey(lang))
			return getRecordEditors().get(lang);

		IModel<GuideContentRecord> translationRecordModel = getTranslationRecordModel(lang);
		ObjectRecordEditor<GuideContent, GuideContentRecord> e = new ObjectRecordEditor<GuideContent, GuideContentRecord>(id, getModel(), translationRecordModel);

		e.setIntroVisible(isIntroVisible());
		e.setSpecVisible(isSpecVisible());
		e.setOpensVisible(isOpensVisible());
		e.setAudioVisible(isAudioVisible());
		e.setInfoVisible(isInfoVisible());

		super.getRecordEditors().put(lang, e);
		return e;
	}
	
	
	protected List<Language> getSupportedLanguages() {
		return  getSiteModel().getObject().getLanguages();
	}

	
	@Override
	public boolean hasAccessRight(Optional<User> ouser) {

		if (ouser.isEmpty())
			return false;

		User user = ouser.get();

		if (user.isRoot())
			return true;

		if (!user.isDependencies()) {
			user = getUserDBService().findWithDeps(user.getId()).get();
		}

		{
			Set<RoleGeneral> set = user.getRolesGeneral();
			if (set != null) {
				boolean isAccess = set.stream().anyMatch((p -> p.getKey().equals(RoleGeneral.ADMIN) || p.getKey().equals(RoleGeneral.AUDIT)));
				if (isAccess)
					return true;
			}
		}

		{
			final Long sid = getSiteModel().getObject().getId();

			Set<RoleSite> set = user.getRolesSite();
			if (set != null) {
				boolean isAccess = set.stream().anyMatch((p -> p.getSite().getId().equals(sid) && (p.getKey().equals(RoleSite.ADMIN) || p.getKey().equals(RoleSite.EDITOR))));
				if (isAccess)
					return true;
			}
		}

		{
			final Long iid = getSiteModel().getObject().getInstitution().getId();
			Set<RoleInstitution> set = user.getRolesInstitution();
			if (set != null) {
				boolean isAccess = set.stream().anyMatch((p -> p.getInstitution().getId().equals(iid) && (p.getKey().equals(RoleInstitution.ADMIN))));
				if (isAccess)
					return true;
			}
		}

		return false;
	}

	public GuideContentPage() {
		super();
	}

	public GuideContentPage(PageParameters parameters) {
		super(parameters);
	}

	public GuideContentPage(IModel<GuideContent> model) {
		this(model, null);
	}

	public GuideContentPage(IModel<GuideContent> model, List<IModel<GuideContent>> list) {
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

	public IModel<ArtExhibitionGuide> getArtExhibitionGuideModel() {
		return artExhibitionGuideModel;
	}

	public IModel<ArtExhibition> getArtExhibitionModel() {
		return artExhibitionModel;
	}

	public IModel<ArtExhibitionItem> getArtExhibitionItemModel() {
		return artExhibitionItemModel;
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

	public IModel<ArtWork> getArtWorkModel() {
		return artWorkModel;
	}

	public void setArtWorkModel(IModel<ArtWork> artWorkModel) {
		this.artWorkModel = artWorkModel;
	}

	
	 
	
	protected WebMarkupContainer getEditor(String id) {
		if (this.editor == null)
			this.editor = new GuideContentEditor(id, getModel(), getArtExhibitionGuideModel(), getArtExhibitionModel(), getSiteModel());
		return this.editor;
	}

	protected Optional<GuideContentRecord> loadTranslationRecord(String lang) {
		return getGuideContentRecordDBService().findByGuideContent(getModel().getObject(), lang);
	}

	protected GuideContentRecord createTranslationRecord(String lang) {
		return getGuideContentRecordDBService().create(getModel().getObject(), lang, getSessionUser().get());
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

		add(new io.wktui.event.WicketEventListener<SimpleAjaxWicketEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(SimpleAjaxWicketEvent event) {

				logger.debug(event.toString());

				/** Action */

				if (event.getName().equals(ServerAppConstant.action_guide_content_edit)) {
					GuideContentPage.this.onEdit(event.getTarget());
					return;
				}

				if (event.getName().equals(ServerAppConstant.action_guide_content_delete)) {
					GuideContentPage.this.onDelete(event.getTarget());
					return;
				}

				if (event.getName().equals(ServerAppConstant.action_guide_content_restore)) {
					GuideContentPage.this.onRestore(event.getTarget());
					return;
				}

				if (event.getName().equals(ServerAppConstant.action_object_edit_meta)) {
					GuideContentPage.this.getMetaEditor().onEdit(event.getTarget());
					return;
				}

				else if (event.getName().equals(ServerAppConstant.action_object_edit_record)) {
					GuideContentPage.this.onEditRecord(event.getTarget(), event.getMoreInfo());
					return;
				}

				/** Panels */

				else if (event.getName().equals(ServerAppConstant.guide_content_info)) {
					GuideContentPage.this.togglePanel(ServerAppConstant.guide_content_info, event.getTarget());
					GuideContentPage.this.getHeader().setPhotoVisible(true);
					event.getTarget().add(GuideContentPage.this.getHeader());
					return;
				}

				else if (event.getName().startsWith(ServerAppConstant.object_translation_record_info)) {
					GuideContentPage.this.togglePanel(event.getName(), event.getTarget());
					GuideContentPage.this.getHeader().setPhotoVisible(true);
					event.getTarget().add(GuideContentPage.this.getHeader());
					return;
				}

				else if (event.getName().equals(ServerAppConstant.object_meta)) {
					GuideContentPage.this.togglePanel(ServerAppConstant.object_meta, event.getTarget());
					GuideContentPage.this.getHeader().setPhotoVisible(true);
					event.getTarget().add(GuideContentPage.this.getHeader());
					return;
				}

				// else if (event.getName().equals(ServerAppConstant.object_audit)) {
				// GuideContentPage.this.togglePanel(ServerAppConstant.object_audit,
				// event.getTarget());
				// GuideContentPage.this.getHeader().setPhotoVisible(true);
				// event.getTarget().add(GuideContentPage.this.getHeader());
				// }

				else if (event.getName().startsWith(ServerAppConstant.object_audit)) {
					if (event.getMoreInfo() != null) {
						GuideContentPage.this.togglePanel(ServerAppConstant.object_audit + "-" + event.getMoreInfo(), event.getTarget());
						// SiteInfoPage.this.getHeader().setPhotoVisible(true);
					} else {
						GuideContentPage.this.togglePanel(ServerAppConstant.object_audit, event.getTarget());
						// SiteInfoPage.this.getHeader().setPhotoVisible(true);
					}
				}
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof MenuAjaxEvent)
					return true;
				return false;
			}
		});

		add(new io.wktui.event.WicketEventListener<SimpleWicketEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(SimpleWicketEvent event) {

				if (event.getName().equals(ServerAppConstant.site_action_home)) {
					setResponsePage(new SitePage(getSiteModel()));
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

	protected void onEdit(AjaxRequestTarget target) {
		this.editor.onEdit(target);
	}

	@Override
	protected List<ToolbarItem> getToolbarItems() {

		if (this.list != null)
			return this.list;

		this.list = new ArrayList<ToolbarItem>();

		/** audio de obra */
		list.add(new GuideContentNavDropDownMenuToolbarItem("item", getModel(), getSiteModel(),  getLabel("guide-content-dropdown", TextCleaner.truncate(getModel().getObject().getName(), 24)), Align.TOP_RIGHT));

		/** audio guia */
		ArtExhibitionGuideEXTNavDropDownMenuToolbarItem ag = new ArtExhibitionGuideEXTNavDropDownMenuToolbarItem("item", getArtExhibitionGuideModel(),getSiteModel(), Align.TOP_RIGHT);
		ag.add(new org.apache.wicket.AttributeModifier("class", "d-none d-xs-none d-sm-none d-md-none d-lg-block d-xl-block d-xxl-block text-md-center"));
		list.add(ag);

		/** exhibicion */
		ArtExhibitionEXTNavDropDownMenuToolbarItem ae = new ArtExhibitionEXTNavDropDownMenuToolbarItem("item", getArtExhibitionModel(), getSiteModel(), Align.TOP_RIGHT);
		ae.add(new org.apache.wicket.AttributeModifier("class", "d-none d-xs-none d-sm-none d-md-none d-lg-none d-xl-block d-xxl-block text-md-center"));
		list.add(ae);

		/** site */
		SiteNavDropDownMenuToolbarItem site = new SiteNavDropDownMenuToolbarItem("item", getSiteModel(), Align.TOP_RIGHT);
		site.add(new org.apache.wicket.AttributeModifier("class", "d-none d-xs-none d-sm-none d-md-none d-lg-none d-xl-block d-xxl-block text-md-center"));
		list.add(site);

		return this.list;
	}

	protected boolean isAudioVisible() {
		return true;
	}

	@Override
	protected Class<?> getTranslationClass() {
		return GuideContentRecord.class;
	}

	@Override
	protected IRequestablePage getObjectPage(IModel<GuideContent> model, List<IModel<GuideContent>> list) {
		return new GuideContentPage(model, list);
	}

	protected void onDelete(AjaxRequestTarget target) {
		getGuideContentDBService().markAsDeleted(getModel().getObject(), getSessionUser().get());
		fireScanAll(new ObjectMarkAsDeleteEvent(target));
	}

	protected void onRestore(AjaxRequestTarget target) {
		getGuideContentDBService().restore(getModel().getObject(), getSessionUser().get());
		fireScanAll(new ObjectRestoreEvent(target));
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

		bc.addElement(new HREFBCElement("/site/list", getLabel("sites")));

		bc.addElement(new HREFBCElement("/site/" + getSiteModel().getObject().getId().toString(), new Model<String>(getSiteModel().getObject().getDisplayname())));

		bc.addElement(new HREFBCElement("/site/exhibitions/" + getSiteModel().getObject().getId().toString(), getLabel("exhibitions")));
		bc.addElement(new HREFBCElement("/artexhibition/" + getArtExhibitionModel().getObject().getId().toString(), Model.of(getArtExhibitionModel().getObject().getDisplayname() + " (E)")));
		bc.addElement(new HREFBCElement("/guide/" + getArtExhibitionGuideModel().getObject().getId().toString(), Model.of(getArtExhibitionGuideModel().getObject().getDisplayname() + " (AG)")));

		bc.addElement(new BCElement(new Model<String>(getModel().getObject().getDisplayname())));

		StringBuilder str = new StringBuilder();
		str.append( getObjectTitle( getModel().getObject() ).getObject() );
		str.append(  getArtExhibitionGuideModel().getObject().isAccessible() ? Icons.ACCESIBLE_ICON_JUMBO: "" );
	
		header = new JumboPageHeaderPanel<GuideContent>("page-header", getModel(), Model.of ( str.toString() ));
		header.add(new org.apache.wicket.AttributeModifier("class", "row mt-0 mb-0 text-center imgReduced"));
		header.setContext(getLabel("guide-content"));

		// header.setIcon(GuideContent.getIcon());
		// header.setHeaderCss("mb-0 pb-2 border-none");

		if (getList() != null && getList().size() > 0) {
			Navigator<GuideContent> nav = new Navigator<GuideContent>("navigator", getCurrent(), getList()) {
				private static final long serialVersionUID = 1L;

				@Override
				public void navigate(int current) {
					setResponsePage(new GuideContentPage(getList().get(current), getList()));
				}
			};
			bc.setNavigator(nav);
		}

		if (getArtWorkModel().getObject().getPhoto() != null)
			this.header.setPhotoModel(new ObjectModel<Resource>(getArtWorkModel().getObject().getPhoto()));

		header.setBreadCrumb(bc);

		if (getList() != null && getList().size() > 0) {
			Navigator<GuideContent> nav = new Navigator<GuideContent>("navigator", getCurrent(), getList()) {
				private static final long serialVersionUID = 1L;

				@Override
				public void navigate(int current) {
					setResponsePage(new GuideContentPage(getList().get(current), getList()));
				}
			};
			bc.setNavigator(nav);
		}

		header.setTagline(Model.of(getArtistStr(getArtWorkModel().getObject())));

		return header;
	}

	@Override
	protected List<INamedTab> getInternalPanels() {

		List<INamedTab> tabs = super.createInternalPanels();

		NamedTab tab_1 = new NamedTab(Model.of("editor"), ServerAppConstant.guide_content_info) {

			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getEditor(panelId);
			}
		};
		tabs.add(tab_1);

		if (getStartTab() == null)
			setStartTab(ServerAppConstant.guide_content_info);

		return tabs;
	}

	private JumboPageHeaderPanel<?> getHeader() {
		return header;
	}

}
