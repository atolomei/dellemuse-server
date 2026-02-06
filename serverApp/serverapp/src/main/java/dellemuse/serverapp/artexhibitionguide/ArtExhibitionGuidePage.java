package dellemuse.serverapp.artexhibitionguide;

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
import dellemuse.serverapp.editor.ObjectMarkAsDeleteEvent;
import dellemuse.serverapp.editor.ObjectRestoreEvent;
import dellemuse.serverapp.global.JumboPageHeaderPanel;

import dellemuse.serverapp.page.MultiLanguageObjectPage;

import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.site.SiteNavDropDownMenuToolbarItem;
import dellemuse.serverapp.page.site.SitePage;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionGuideRecord;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionRecord;
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

@MountPath("/guide/${id}")
public class ArtExhibitionGuidePage extends MultiLanguageObjectPage<ArtExhibitionGuide, ArtExhibitionGuideRecord> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ArtExhibitionGuidePage.class.getName());

	private IModel<Site> siteModel;
	private IModel<ArtExhibition> artExhibitionModel;

	private ArtExhibitionGuideContentsPanel guideContents;
	private JumboPageHeaderPanel<ArtExhibitionGuide> header;
	private ArtExhibitionGuideEditor editor;

	private List<ToolbarItem> list;

	
	protected List<Language> getSupportedLanguages() {
		return  getSiteModel().getObject().getLanguages();
	}

 
	public ArtExhibitionGuidePage() {
		super();
	}

	public ArtExhibitionGuidePage(PageParameters parameters) {
		super(parameters);
	}

	public ArtExhibitionGuidePage(IModel<ArtExhibitionGuide> model) {
		this(model, null);
	}

	public ArtExhibitionGuidePage(IModel<ArtExhibitionGuide> model, List<IModel<ArtExhibitionGuide>> list) {
		super(model, list);
	}

	public IModel<Site> getSiteModel() {
		return this.siteModel;
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
	protected Class<?> getTranslationClass() {
		return ArtExhibitionGuideRecord.class;
	}
	@Override
	protected boolean isLanguage() {
		return false;
	}

	//protected void onEditRecord(AjaxRequestTarget target, String lang) {
	//	getRecordEditors().get(lang).edit(target);
	//}

	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<SimpleAjaxWicketEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(SimpleAjaxWicketEvent event) {

				logger.debug(event.toString());

				// action -----------

				if (event.getName().equals(ServerAppConstant.action_guide_edit_info)) {
					ArtExhibitionGuidePage.this.onEdit(event.getTarget());
				}

				if (event.getName().equals(ServerAppConstant.action_object_edit_meta)) {
					ArtExhibitionGuidePage.this.getMetaEditor().onEdit(event.getTarget());
				}

				else if (event.getName().equals(ServerAppConstant.action_object_edit_record)) {
					ArtExhibitionGuidePage.this.onEditRecord(event.getTarget(), event.getMoreInfo());
				}

				else if (event.getName().equals(ServerAppConstant.action_artexhibitionguide_delete)) {
					ArtExhibitionGuidePage.this.onDelete(event.getTarget());
				} else if (event.getName().equals(ServerAppConstant.action_artexhibitionguide_restore)) {
					ArtExhibitionGuidePage.this.onRestore(event.getTarget());
				}
				// panels -----------
				else if (event.getName().equals(ServerAppConstant.artexhibitionguide_info)) {
					ArtExhibitionGuidePage.this.togglePanel(ServerAppConstant.artexhibitionguide_info, event.getTarget());
					ArtExhibitionGuidePage.this.getHeader().setPhotoVisible(true);
					event.getTarget().add(ArtExhibitionGuidePage.this.getHeader());
				}
				else if (event.getName().equals(ServerAppConstant.artexhibitionguide_contents)) {
					ArtExhibitionGuidePage.this.togglePanel(ServerAppConstant.artexhibitionguide_contents, event.getTarget());
					ArtExhibitionGuidePage.this.getHeader().setPhotoVisible(true);
					event.getTarget().add(ArtExhibitionGuidePage.this.getHeader());
				}
				else if (event.getName().equals(ServerAppConstant.object_meta)) {
					ArtExhibitionGuidePage.this.togglePanel(ServerAppConstant.object_meta, event.getTarget());
					ArtExhibitionGuidePage.this.getHeader().setPhotoVisible(true);
					event.getTarget().add(ArtExhibitionGuidePage.this.getHeader());
				} 
				else if (event.getName().startsWith(ServerAppConstant.object_translation_record_info)) {
					ArtExhibitionGuidePage.this.togglePanel(event.getName(), event.getTarget());
				}
				else if (event.getName().startsWith(ServerAppConstant.object_audit)) {
					if (event.getMoreInfo() != null) {
						ArtExhibitionGuidePage.this.togglePanel(ServerAppConstant.object_audit + "-" + event.getMoreInfo(), event.getTarget());
						ArtExhibitionGuidePage.this.getHeader().setPhotoVisible(true);
					} else {
						ArtExhibitionGuidePage.this.togglePanel(ServerAppConstant.object_audit, event.getTarget());
						ArtExhibitionGuidePage.this.getHeader().setPhotoVisible(true);
					}
					event.getTarget().add(ArtExhibitionGuidePage.this.getHeader());
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
					setResponsePage(new SitePage(getSiteModel(), null));
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

	protected void onDelete(AjaxRequestTarget target) {
		getArtExhibitionGuideDBService().markAsDeleted(getModel().getObject(), getSessionUser().get());
		fireScanAll(new ObjectMarkAsDeleteEvent(target));
	}

	protected void onRestore(AjaxRequestTarget target) {
		getArtExhibitionGuideDBService().restore(getModel().getObject(), getSessionUser().get());
		fireScanAll(new ObjectRestoreEvent(target));
	}

	protected void onGuideCreate(AjaxRequestTarget target) {
	}

	@Override
	protected List<ToolbarItem> getToolbarItems() {

		if (list != null)
			return list;

		list = new ArrayList<ToolbarItem>();
		list.add(new ArtExhibitionGuideNavDropDownMenuToolbarItem("item", 
				getModel(), 
				getSiteModel(),
				getLabel("audio-guide", TextCleaner.truncate(getModel().getObject().getName(), 24)), Align.TOP_RIGHT));

		ArtExhibitionEXTNavDropDownMenuToolbarItem ae = new ArtExhibitionEXTNavDropDownMenuToolbarItem("item", 
				getArtExhibitionModel(), 
				getSiteModel(),
				getLabel("art-exhibition", TextCleaner.truncate(getArtExhibitionModel().getObject().getName(), 24)),
				Align.TOP_RIGHT);

		ae.add(new org.apache.wicket.AttributeModifier("class", "d-none d-xs-none d-sm-block d-md-block d-lg-block d-xl-block d-xxl-block text-md-center"));
		list.add(ae);

		SiteNavDropDownMenuToolbarItem site = new SiteNavDropDownMenuToolbarItem("item", getSiteModel(), Model.of(getSiteModel().getObject().getShortName()), Align.TOP_RIGHT);
		site.add(new org.apache.wicket.AttributeModifier("class", "d-none d-xs-none d-sm-none d-md-block d-lg-block d-xl-block d-xxl-block text-md-center"));

		list.add(site);
		return list;
	}

	protected WebMarkupContainer getArtExhibitionGuideEditor(String id) {
		if (this.editor == null)
			this.editor = new ArtExhibitionGuideEditor(id, getModel(), getSiteModel());
		return this.editor;
	}

	protected WebMarkupContainer getGuideContentsPanel(String id) {
		if (this.guideContents == null)
			guideContents = new ArtExhibitionGuideContentsPanel(id, getModel(), getSiteModel());
		return guideContents;
	}

	@Override
	protected List<INamedTab> getInternalPanels() {

		List<INamedTab> tabs = super.createInternalPanels();

		NamedTab tab_1 = new NamedTab(Model.of("editor"), ServerAppConstant.artexhibitionguide_info) {
			private static final long serialVersionUID = 1L;
			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getArtExhibitionGuideEditor(panelId);
			}
		};
		tabs.add(tab_1);

		NamedTab tab_2 = new NamedTab(Model.of("contents"), ServerAppConstant.artexhibitionguide_contents) {
			private static final long serialVersionUID = 1L;
			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getGuideContentsPanel(panelId);
			}
		};
		tabs.add(tab_2);

		if (getStartTab() == null)
			setStartTab(ServerAppConstant.artexhibitionguide_info);

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

		BreadCrumb<Void> bc = createBreadCrumb();

		bc.addElement(new HREFBCElement("/site/list", getLabel("sites")));
		bc.addElement(new HREFBCElement("/site/" + getSiteModel().getObject().getId().toString(), new Model<String>(getSiteModel().getObject().getDisplayname())));
		bc.addElement(new HREFBCElement("/site/exhibitions/" + getSiteModel().getObject().getId().toString(), getLabel("exhibitions")));
		bc.addElement(new HREFBCElement("/artexhibition/" + getArtExhibitionModel().getObject().getId().toString(), Model.of(getArtExhibitionModel().getObject().getDisplayname() + " (E)")));
		bc.addElement(new BCElement(getObjectTitle( getModel().getObject())));
		this.header = new JumboPageHeaderPanel<ArtExhibitionGuide>("page-header", getModel(), getObjectTitle( getModel().getObject()));
		this.header.setContext(getLabel("exhibition-guide"));
		this.header.add(new org.apache.wicket.AttributeModifier("class", "row mt-0 mb-0 text-center imgReduced"));

		if (getList() != null && getList().size() > 0) {
			Navigator<ArtExhibitionGuide> nav = new Navigator<ArtExhibitionGuide>("navigator", getCurrent(), getList()) {
				private static final long serialVersionUID = 1L;

				@Override
				public void navigate(int current) {
					setResponsePage(new ArtExhibitionGuidePage(getList().get(current), getList()));
				}
			};
			bc.setNavigator(nav);
		}

		if (getModel().getObject().getPhoto() != null)
			header.setPhotoModel(new ObjectModel<Resource>(getModel().getObject().getPhoto()));
		else if (getArtExhibitionModel().getObject().getPhoto() != null)
			header.setPhotoModel(new ObjectModel<Resource>(getArtExhibitionModel().getObject().getPhoto()));

		//if (getModel().getObject().getSubtitle() != null)
		//	header.setTagline(getObjectSubtitle( getModel().getObject()));
		//else if ( getObjectSubtitle(getArtExhibitionModel().getObject())  != null)
	//		header.setTagline(getObjectSubtitle(getArtExhibitionModel().getObject()));

		this.header.setBreadCrumb(bc);

		return this.header;
	}

	@Override
	protected IRequestablePage getObjectPage(IModel<ArtExhibitionGuide> model, List<IModel<ArtExhibitionGuide>> list) {
		return new ArtExhibitionGuidePage(model, list);
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

	private JumboPageHeaderPanel<?> getHeader() {
		return this.header;
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
			final Long sid = getSiteModel().getObject().getInstitution().getId();
			Set<RoleInstitution> set = user.getRolesInstitution();
			if (set != null) {
				boolean isAccess = set.stream().anyMatch((p -> p.getInstitution().getId().equals(sid) && (p.getKey().equals(RoleInstitution.ADMIN))));
				if (isAccess)
					return true;
			}
		}

		return false;
	}
}
