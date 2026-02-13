package dellemuse.serverapp.artexhibition;

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
import dellemuse.serverapp.editor.ObjectMarkAsDeleteEvent;
import dellemuse.serverapp.editor.ObjectRestoreEvent;
import dellemuse.serverapp.global.JumboPageHeaderPanel;

import dellemuse.serverapp.page.MultiLanguageObjectPage;
import dellemuse.serverapp.page.library.ObjectStateEnumSelector;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.model.ObjectWithDepModel;
import dellemuse.serverapp.page.site.SiteNavDropDownMenuToolbarItem;
import dellemuse.serverapp.page.site.SitePage;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.ArtExhibitionRecord;

import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
import dellemuse.serverapp.serverdb.model.security.RoleInstitution;
import dellemuse.serverapp.serverdb.model.security.RoleSite;
import io.wktui.error.ErrorPanel;
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
 * <p>site foto Info - exhibitions alter table artexhibition alter column fromdate
 * drop not null; ALTER TABLE dellemuse=# alter table artexhibition alter column
 * todate drop not null;
 * </p>
 */

@MountPath("/artexhibition/${id}")
public class ArtExhibitionPage extends MultiLanguageObjectPage<ArtExhibition, ArtExhibitionRecord> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ArtExhibitionPage.class.getName());

	private IModel<Site> siteModel;
	private ArtExhibitionEditor editor;
	private ArtExhibitionItemsPanel items;
	private ArtExhibitionGuidesPanel guides = null;
	private ArtExhibitionSectionsPanel sections = null;
	private List<ToolbarItem> list;

	

	public ArtExhibitionPage() {
		super();
		setObjectStateEnumSelector(ObjectStateEnumSelector.EDTIION_PUBLISHED);
	}

	public ArtExhibitionPage(PageParameters parameters) {
		super(parameters);
		setObjectStateEnumSelector(ObjectStateEnumSelector.EDTIION_PUBLISHED);

	}

	public ArtExhibitionPage(IModel<ArtExhibition> model) {
		this(model, null);
		setObjectStateEnumSelector(ObjectStateEnumSelector.EDTIION_PUBLISHED);

	}

	public ArtExhibitionPage(IModel<ArtExhibition> model, List<IModel<ArtExhibition>> list) {
		super(model, list);
		setObjectStateEnumSelector(ObjectStateEnumSelector.EDTIION_PUBLISHED);

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
	
	@Override
	protected Class<?> getTranslationClass() {
		return ArtExhibitionRecord.class;
	}

	protected void onEdit(AjaxRequestTarget target) {
		editor.onEdit(target);
	}

	protected void onGuideCreate(AjaxRequestTarget target) {
		guides.onGuideCreate(target);
	}

	protected void onItemsEdit(AjaxRequestTarget target) {
		items.onEdit(target);
	}

	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<SimpleAjaxWicketEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(SimpleAjaxWicketEvent event) {

				logger.debug(event.toString());

				// action

				if (event.getName().equals(ServerAppConstant.action_exhibition_info_edit)) {
					ArtExhibitionPage.this.onEdit(event.getTarget());
				}

				else if (event.getName().equals(ServerAppConstant.action_object_edit_meta)) {
					ArtExhibitionPage.this.getMetaEditor().onEdit(event.getTarget());
				} else if (event.getName().equals(ServerAppConstant.action_exhibition_items_edit)) {
					ArtExhibitionPage.this.onItemsEdit(event.getTarget());
				}

				else if (event.getName().equals(ServerAppConstant.action_object_edit_record)) {
					ArtExhibitionPage.this.onEditRecord(event.getTarget(), event.getMoreInfo());
				}

				if (event.getName().equals(ServerAppConstant.action_exhibition_guide_create)) {
					ArtExhibitionPage.this.onGuideCreate(event.getTarget());
				}

				// panels --------------------------------------
				//
				//
				//
				else if (event.getName().equals(ServerAppConstant.exhibition_info)) {
					ArtExhibitionPage.this.togglePanel(ServerAppConstant.exhibition_info, event.getTarget());
					event.getTarget().add(ArtExhibitionPage.this.getHeaderPanel());
				}

				else if (event.getName().startsWith(ServerAppConstant.object_translation_record_info)) {
					ArtExhibitionPage.this.togglePanel(event.getName(), event.getTarget());
				}

				else if (event.getName().equals(ServerAppConstant.exhibition_items)) {
					ArtExhibitionPage.this.togglePanel(ServerAppConstant.exhibition_items, event.getTarget());
					event.getTarget().add(ArtExhibitionPage.this.getHeaderPanel());
				}

				else if (event.getName().equals(ServerAppConstant.exhibition_sections)) {
					ArtExhibitionPage.this.togglePanel(ServerAppConstant.exhibition_sections, event.getTarget());

					event.getTarget().add(ArtExhibitionPage.this.getHeaderPanel());
				} else if (event.getName().equals(ServerAppConstant.exhibition_guides)) {
					ArtExhibitionPage.this.togglePanel(ServerAppConstant.exhibition_guides, event.getTarget());
					;
					event.getTarget().add(ArtExhibitionPage.this.getHeaderPanel());
				}

				else if (event.getName().equals(ServerAppConstant.object_meta)) {
					ArtExhibitionPage.this.togglePanel(ServerAppConstant.object_meta, event.getTarget());

					event.getTarget().add(ArtExhibitionPage.this.getHeaderPanel());
				}

				else if (event.getName().startsWith(ServerAppConstant.object_audit)) {
					if (event.getMoreInfo() != null) {
						ArtExhibitionPage.this.togglePanel(ServerAppConstant.object_audit + "-" + event.getMoreInfo(), event.getTarget());
						// SiteInfoPage.this.getHeader().setPhotoVisible(true);
					} else {
						ArtExhibitionPage.this.togglePanel(ServerAppConstant.object_audit, event.getTarget());
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

	@Override
	protected boolean isLanguage() {
		return false;
	}

	protected void onDelete(AjaxRequestTarget target) {
		getArtExhibitionDBService().markAsDeleted(getModel().getObject(), getSessionUser().get());
		fireScanAll(new ObjectMarkAsDeleteEvent(target));
	}

	protected void onRestore(AjaxRequestTarget target) {
		getArtExhibitionDBService().restore(getModel().getObject(), getSessionUser().get());
		fireScanAll(new ObjectRestoreEvent(target));
	}

	protected Optional<ArtExhibitionRecord> loadTranslationRecord(String lang) {
		return getArtExhibitionRecordDBService().findByArtExhibition(getModel().getObject(), lang);
	}

	protected ArtExhibitionRecord createTranslationRecord(String lang) {
		return getArtExhibitionRecordDBService().create(getModel().getObject(), lang, getSessionUser().get());
	}

	@Override
	protected boolean isOpensVisible() {
		return true;
	}

	@Override
	protected boolean isIntroVisible() {
		return true;
	}

	protected boolean isAudioVisible() {
		return false;
	}

	@Override
	protected List<ToolbarItem> getToolbarItems() {

		if (list != null)
			return list;

		list = new ArrayList<ToolbarItem>();

		String name = null;

		if (getModel().getObject().getShortname() != null)
			name = TextCleaner.truncate(getModel().getObject().getShortname(), 24);
		else
			name = TextCleaner.truncate(getModel().getObject().getName(), 24);

		list.add(new ArtExhibitionNavDropDownMenuToolbarItem("item", getModel(), getLabel("art-exhibition", name), Align.TOP_RIGHT));

		/** site */
		SiteNavDropDownMenuToolbarItem site = new SiteNavDropDownMenuToolbarItem("item", getSiteModel(), Align.TOP_RIGHT);
		site.add(new org.apache.wicket.AttributeModifier("class", "d-none d-xs-none d-sm-none d-md-block d-lg-block d-xl-block d-xxl-block text-md-center"));
		list.add(site);

		return list;
	}

	/**
	 * 
	 * 
	 */
	@Override
	protected List<INamedTab> getInternalPanels() {

		List<INamedTab> tabs = super.createInternalPanels();

		NamedTab tab_1 = new NamedTab(Model.of("editor"), ServerAppConstant.exhibition_info) {
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getEditor(panelId);
			}
		};
		tabs.add(tab_1);

		NamedTab tab_3 = new NamedTab(Model.of("items"), ServerAppConstant.exhibition_items) {
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getArtExhibitionItemsPanel(panelId);
			}
		};
		tabs.add(tab_3);

		NamedTab tab_4 = new NamedTab(Model.of("guides"), ServerAppConstant.exhibition_guides) {
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getArtExhibitionGuidesPanel(panelId);
			}
		};
		tabs.add(tab_4);

		NamedTab tab_5 = new NamedTab(Model.of("sections"), ServerAppConstant.exhibition_sections) {
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getArtExhibitionSectionsPanel(panelId);
			}
		};
		tabs.add(tab_5);

		if (getStartTab() == null)
			setStartTab(ServerAppConstant.exhibition_info);

		return tabs;
	}

	/**
	 * @param panelId
	 * @return
	 */

	protected WebMarkupContainer getArtExhibitionGuidesPanel(String panelId) {
		if (guides == null)
			guides = new ArtExhibitionGuidesPanel(panelId, getModel());
		return guides;
	}

	protected WebMarkupContainer getArtExhibitionSectionsPanel(String panelId) {
		if (sections == null)
			sections = new ArtExhibitionSectionsPanel(panelId, getModel(), getSiteModel());
		return sections;
	}

	protected ArtExhibitionItemsPanel getArtExhibitionItemsPanel(String id) {
		if (items == null)
			items = new ArtExhibitionItemsPanel(id,  getObjectStateEnumSelector(), getModel(),   getSiteModel());
		return items;
	}


	
	@Override
	protected Optional<ArtExhibition> getObject(Long id) {
		return getArtExhibition(id);
	}

	@Override
	protected IModel<String> getPageTitle() {
		return new Model<String>(getModel().getObject().getName());
	}

	@Override
	protected Panel createHeaderPanel() {

		try {
			BreadCrumb<Void> bc = createBreadCrumb();
			bc.addElement(new HREFBCElement("/site/list", getLabel("sites")));
			bc.addElement(new HREFBCElement("/site/" + getSiteModel().getObject().getId().toString(), getObjectTitle(getSiteModel().getObject())));
			bc.addElement(new HREFBCElement("/site/exhibitions/" + getSiteModel().getObject().getId().toString(), getLabel("exhibitions")));
			bc.addElement(new BCElement(getObjectTitle(getModel().getObject())));

			JumboPageHeaderPanel<ArtExhibition> header = new JumboPageHeaderPanel<ArtExhibition>("page-header", getModel(), getObjectTitle(getModel().getObject()));
			header.setContext(getLabel("artexhibition"));

			if (getList() != null && getList().size() > 0) {
				Navigator<ArtExhibition> nav = new Navigator<ArtExhibition>("navigator", getCurrent(), getList()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void navigate(int current) {
						setResponsePage(new ArtExhibitionPage(getList().get(current), getList()));
					}
				};
				bc.setNavigator(nav);
			}

			if (getModel().getObject().getPhoto() != null)
				header.setPhotoModel(new ObjectModel<Resource>(getModel().getObject().getPhoto()));

			 //if (getObjectSubtitle(getModel().getObject()) != null)
			//	 header.setTagline(getObjectSubtitle(getModel().getObject()));

			header.setBreadCrumb(bc);
			return header;

		} catch (Exception e) {
			logger.error(e);
			return new ErrorPanel("page-header", e);

		}
	}

	@Override
	protected IRequestablePage getObjectPage(IModel<ArtExhibition> model, List<IModel<ArtExhibition>> list) {
		return new ArtExhibitionPage(model, list);
	}

	@Override
	protected void setUpModel() {
		super.setUpModel();

		if (!getModel().getObject().isDependencies()) {
			Optional<ArtExhibition> o_i = getArtExhibitionDBService().findWithDeps(getModel().getObject().getId());
			setModel(new ObjectWithDepModel<ArtExhibition>(o_i.get()));
		}

		setSiteModel(new ObjectModel<Site>(getModel().getObject().getSite()));

		if (!getSiteModel().getObject().isDependencies()) {
			Optional<Site> o_i = getSiteDBService().findWithDeps(getSiteModel().getObject().getId());
			setSiteModel(new ObjectWithDepModel<Site>(o_i.get()));
		}
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
	}

	protected Panel getItemsPanel(String id) {
		if (this.items == null)
			this.items = new ArtExhibitionItemsPanel(id, this.getObjectStateEnumSelector(),  getModel(), getSiteModel());
		return this.items;
	}

	protected Panel getEditor(String id) {
		if (this.editor == null)
			this.editor = new ArtExhibitionEditor(id, getModel());
		return this.editor;
	}

}
