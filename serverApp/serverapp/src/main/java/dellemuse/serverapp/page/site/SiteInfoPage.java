package dellemuse.serverapp.page.site;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.logging.Logger;

import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.help.Help;
import dellemuse.serverapp.help.HelpButtonToolbarItem;
import dellemuse.serverapp.page.MultiLanguageObjectPage;

import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.SiteRecord;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
import dellemuse.serverapp.serverdb.model.security.RoleSite;

import io.wktui.error.ErrorPanel;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.event.SimpleAjaxWicketEvent;
import io.wktui.event.SimpleWicketEvent;
import io.wktui.event.UIEvent;

import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;

import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;

import wktui.base.INamedTab;

import wktui.base.NamedTab;

@AuthorizeInstantiation({"ROLE_USER"})
@MountPath("/site/info/${id}")
public class SiteInfoPage extends MultiLanguageObjectPage<Site, SiteRecord> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteInfoPage.class.getName());

	private SiteInfoEditor editor;
	private List<ToolbarItem> list;

	
	public SiteInfoPage() {
		super();
	}

	public SiteInfoPage(PageParameters parameters) {
		super(parameters);
	}

	public SiteInfoPage(IModel<Site> model) {
		super(model);
	}


	public String getHelpKey() {
		return Help.SITE_INFO;
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
			final Long sid = getModel().getObject().getId();

			Set<RoleSite> set = user.getRolesSite();
			if (set != null) {
				boolean isAccess = set.stream().anyMatch((p -> p.getSite().getId().equals(sid) && (p.getKey().equals(RoleSite.ADMIN) || p.getKey().equals(RoleSite.EDITOR))));
				if (isAccess)
					return true;
			}
		}

		return false;
	}


	@Override
	protected Panel createHeaderPanel() {

		try {
			BreadCrumb<Void> bc = createBreadCrumb();
			bc.addElement(new HREFBCElement("/site/list", getLabel("sites")));
			bc.addElement(new HREFBCElement("/site/" + getModel().getObject().getId().toString(), getObjectTitle(getModel().getObject())));
			bc.addElement(new BCElement(getLabel("general-info")));
			JumboPageHeaderPanel<Site> ph = new JumboPageHeaderPanel<Site>("page-header", getModel(), getObjectTitle(getModel().getObject()) );
			ph.setBreadCrumb(bc);

			ph.setContext(getLabel("site"));

			if (getModel().getObject().getSubtitle() != null)
				ph.setTagline( getObjectSubtitle(getModel().getObject() ));

			if (getModel().getObject().getPhoto() != null)
				ph.setPhotoModel(new ObjectModel<Resource>(getModel().getObject().getPhoto()));

			return ph;

		} catch (Exception e) {
			logger.error(e);
			return new ErrorPanel("page-header", e);
		}
	}

	@Override
	protected IRequestablePage getObjectPage(IModel<Site> iModel, List<IModel<Site>> list2) {
		return new SitePage(iModel, list2);
	}

	protected Panel getEditor(String id) {
		if (this.editor == null)
			this.editor = new SiteInfoEditor(id, getModel());
		return this.editor;
	}

	protected List<ToolbarItem> getToolbarItems() {

		if (list != null)
			return list;

		list = new ArrayList<ToolbarItem>();

		list.add(new SiteInfoNavDropDownMenuToolbarItem("item", getModel(), Align.TOP_RIGHT));

		// site
		SiteNavDropDownMenuToolbarItem site = new SiteNavDropDownMenuToolbarItem("item", getModel(), Align.TOP_RIGHT);
		site.add(new org.apache.wicket.AttributeModifier("class", "d-none d-xs-none d-sm-none d-md-block d-lg-block d-xl-block d-xxl-block text-md-center"));
		list.add(site);
		
		HelpButtonToolbarItem h = new HelpButtonToolbarItem("item",  Align.TOP_RIGHT);
		list.add(h);
		
		
		return list;
	}

	@Override
	protected List<INamedTab> getInternalPanels() {

		List<INamedTab> tabs = super.createInternalPanels();

		NamedTab tab_1 = new NamedTab(Model.of("editor"), ServerAppConstant.site_page_info) {

			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getEditor(panelId);
			}
		};
		tabs.add(tab_1);

		if (getStartTab() == null)
			super.setStartTab(ServerAppConstant.site_page_info);

		return tabs;
	}

	 
	protected void onEdit(AjaxRequestTarget target) {
		this.editor.onEdit(target);

	}

	@Override
	protected Class<?> getTranslationClass() {
		return SiteRecord.class;
	}
	protected void setUpModel() {
		super.setUpModel();

		if (!getModel().getObject().isDependencies()) {
			Optional<Site> o_i = getSiteDBService().findWithDeps(getModel().getObject().getId());
			setModel(new ObjectModel<Site>(o_i.get()));
		}
	}

	protected List<Language> getSupportedLanguages() {
		return getModel().getObject().getLanguages();
	}
	
	
	@Override
	public void onInitialize() {
		super.onInitialize();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	protected Optional<SiteRecord> loadTranslationRecord(String lang) {
		return getSiteRecordDBService().findBySite(getModel().getObject(), lang);
	}

	@Override
	protected SiteRecord createTranslationRecord(String lang) {
		return getSiteRecordDBService().create(getModel().getObject(), lang, getSessionUser().get());
	}

	@Override
	protected boolean isOpensVisible() {
		return true;
	}

	@Override
	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<SimpleAjaxWicketEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(SimpleAjaxWicketEvent event) {

				logger.debug(event.toString());

				if (event.getName().equals(ServerAppConstant.site_action_edit)) {
					SiteInfoPage.this.onEdit(event.getTarget());
				}

				else if (event.getName().equals(ServerAppConstant.action_object_edit_record)) {
					SiteInfoPage.this.onEditRecord(event.getTarget(), event.getMoreInfo());
				}

				else if (event.getName().equals(ServerAppConstant.site_page_info)) {
					SiteInfoPage.this.togglePanel(ServerAppConstant.site_page_info, event.getTarget());
				} else if (event.getName().equals(ServerAppConstant.object_meta)) {
					SiteInfoPage.this.togglePanel(ServerAppConstant.object_meta, event.getTarget());
				}

				else if (event.getName().startsWith(ServerAppConstant.object_translation_record_info)) {
					SiteInfoPage.this.togglePanel(event.getName(), event.getTarget());
				}

				else if (event.getName().startsWith(ServerAppConstant.object_audit)) {
					if (event.getMoreInfo() != null) {
						SiteInfoPage.this.togglePanel(ServerAppConstant.object_audit + "-" + event.getMoreInfo(), event.getTarget());
						// SiteInfoPage.this.getHeader().setPhotoVisible(true);
					} else {
						SiteInfoPage.this.togglePanel(ServerAppConstant.object_audit, event.getTarget());
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
					setResponsePage(new SitePage(getModel(), getList()));
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
	protected Optional<Site> getObject(Long id) {
		return getSite(id);
	}

	@Override
	protected IModel<String> getPageTitle() {
		return getLabel("info");
	}

}
