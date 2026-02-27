package dellemuse.serverapp.page.site;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.UrlResourceReference;
import org.apache.wicket.util.string.StringValue;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.ArtExhibitionGuideModel;
import dellemuse.model.ArtExhibitionModel;
import dellemuse.model.ArtWorkModel;
import dellemuse.model.GuideContentModel;
import dellemuse.model.ResourceModel;
import dellemuse.model.SiteModel;
import dellemuse.model.logging.Logger;
import dellemuse.model.util.ThumbnailSize;
import dellemuse.serverapp.artexhibition.ArtExhibitionPage;
import dellemuse.serverapp.audit.panel.AuditPanel;
import dellemuse.serverapp.global.GlobalFooterPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.global.PageHeaderPanel;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.ObjectPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.user.UserPage;
import dellemuse.serverapp.person.PersonPage;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
import dellemuse.serverapp.serverdb.model.security.RoleSite;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.event.SimpleAjaxWicketEvent;
import io.wktui.event.SimpleWicketEvent;
import io.wktui.event.UIEvent;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.struct.list.ListPanel;
import wktui.base.DummyBlockPanel;
import wktui.base.INamedTab;
import wktui.base.InvisiblePanel;
import wktui.base.NamedTab;

/**
 * 
 * site foto Info - exhibitions
 * 
 */
@AuthorizeInstantiation({ "ROLE_USER" })
@MountPath("/site/state/${id}")
public class SiteStatePage extends ObjectPage<Site> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteStatePage.class.getName());

	private SiteInfoEditor editor;



	public SiteStatePage() {
		super();
	}

	public SiteStatePage(PageParameters parameters) {
		super(parameters);
	}

	public SiteStatePage(IModel<Site> model) {
		super(model);
	}

	@Override
	protected Panel createSearchPanel() {
		return null;
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
	

	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<SimpleAjaxWicketEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(SimpleAjaxWicketEvent event) {
				logger.debug(event.toString());

				if (event.getName().equals(ServerAppConstant.site_action_edit)) {
					// SiteStatePage.this.onEdit(event.getTarget());
				}

				else if (event.getName().equals(ServerAppConstant.site_page_info)) {
					SiteStatePage.this.togglePanel(ServerAppConstant.site_page_info, event.getTarget());
				} else if (event.getName().equals(ServerAppConstant.object_meta)) {
					SiteStatePage.this.togglePanel(ServerAppConstant.object_meta, event.getTarget());
				}

				else if (event.getName().equals(ServerAppConstant.action_object_edit_meta)) {
					SiteStatePage.this.getMetaEditor().onEdit(event.getTarget());

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

	@Override
	protected Panel createHeaderPanel() {

		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(new HREFBCElement("/site/list", getLabel("sites")));
		bc.addElement(new HREFBCElement("/site/" + getModel().getObject().getId().toString(), getObjectTitle(getModel().getObject())));
		bc.addElement(new BCElement(getLabel("general-info")));
		JumboPageHeaderPanel<Site> ph = new JumboPageHeaderPanel<Site>("page-header", getModel(), getObjectTitle(getModel().getObject()));
		ph.setBreadCrumb(bc);

		ph.setContext(getLabel("site"));

		if (getModel().getObject().getSubtitle() != null)
			ph.setTagline(Model.of(getModel().getObject().getSubtitle()));

		if (getModel().getObject().getPhoto() != null)
			ph.setPhotoModel(new ObjectModel<Resource>(getModel().getObject().getPhoto()));

		return (ph);
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
		List<ToolbarItem> list = new ArrayList<ToolbarItem>();

		/**
		 * AjaxButtonToolbarItem<Site> create = new AjaxButtonToolbarItem<Site>() {
		 * private static final long serialVersionUID = 1L;
		 * 
		 * @Override protected void onCick(AjaxRequestTarget target) {
		 *           SiteInfoPage.this.togglePanel(PANEL_EDITOR, target);
		 *           SiteInfoPage.this.onEdit(target); }
		 * 
		 * @Override public IModel<String> getButtonLabel() { return getLabel("edit"); }
		 *           };
		 * 
		 *           create.setAlign(Align.TOP_LEFT); list.add(create);
		 * 
		 * 
		 * 
		 *           AjaxButtonToolbarItem<Person> audit = new
		 *           AjaxButtonToolbarItem<Person>() { private static final long
		 *           serialVersionUID = 1L;
		 * 
		 * @Override protected void onCick(AjaxRequestTarget target) {
		 *           SiteInfoPage.this.togglePanel(PANEL_AUDIT, target); }
		 * @Override public IModel<String> getButtonLabel() { return getLabel("audit");
		 *           } }; audit.setAlign(Align.TOP_RIGHT); list.add(audit);
		 */

		list.add(new SiteNavDropDownMenuToolbarItem("item", getModel(), Align.TOP_RIGHT));

		return list;
	}

	protected Panel getAuditPanel(String id) {
		return new AuditPanel<Site>(id, getModel());
	}

	@Override
	protected List<INamedTab> getInternalPanels() {

		List<INamedTab> tabs = super.createInternalPanels();

	 
		NamedTab audit = new NamedTab(Model.of("audit"), ServerAppConstant.object_audit) {

			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getAuditPanel(panelId);
			}
		};
		tabs.add(audit);

		if (getStartTab() == null)
			super.setStartTab(ServerAppConstant.object_meta);

		return tabs;
	}

	protected void setUpModel() {
		super.setUpModel();

		if (!getModel().getObject().isDependencies()) {
			Optional<Site> o_i = getSiteDBService().findWithDeps(getModel().getObject().getId());
			setModel(new ObjectModel<Site>(o_i.get()));
		}
	}

	/**
	 * Institution Site Artwork Person Exhibition ExhibitionItem GuideContent User
	 */
	@Override
	public void onInitialize() {
		super.onInitialize();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	protected boolean isLanguage() {
		return false;
	}
	
}
