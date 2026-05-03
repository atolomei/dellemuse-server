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
import dellemuse.serverapp.audit.panel.AuditPanel;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.page.ObjectPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
import dellemuse.serverapp.serverdb.model.security.RoleSite;
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

/**
 * site foto Info - exhibitions
 * 
 * 
 * 
 
 alter table floor add column info text;
 alter table room add column info text;
  
 alter table room add column masterlanguage character varying(24) default 'es';
 alter table floor add column masterlanguage character varying(24) default 'es';

  
  
alter table floor add column opens text;
alter table room add column opens text;


alter table room add column spec text;
alter table floor add column spec text;



alter table floor add column speechaudio bigint references resource(id) on delete set null;
alter table room add column speechaudio bigint references resource(id) on delete set null;


alter table room add column translation integer default 0;
alter table floor add column translation integer default 0;




 */
@AuthorizeInstantiation({"ROLE_USER"})
@MountPath("/site/floors/${id}")
public class SiteFloorsPage extends ObjectPage<Site> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteFloorsPage.class.getName());

	private Panel editor;

	
	public SiteFloorsPage() {
		super();
	}

	public SiteFloorsPage(PageParameters parameters) {
		super(parameters);
	}

	public SiteFloorsPage(IModel<Site> model) {
		super(model);
	}
	
	@Override
	protected boolean calculateHasAccessRight(Optional<User> ouser) {

		if (ouser.isEmpty())
			return false;

		User user = ouser.get();  if (user.isRoot()) return true;
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
	public void onInitialize() {
		super.onInitialize();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	protected Panel getAuditPanel(String id) {
		return new AuditPanel<Site>(id, getModel());
	}

	protected Panel getEditor(String id) {
		if (this.editor == null)
			this.editor = new SiteFloorsPanel(id, getModel());
		return this.editor;
	}

	@Override
	protected IRequestablePage getObjectPage(IModel<Site> iModel, List<IModel<Site>> list2) {
		return new SitePage(iModel, list2);
	}

	@Override
	protected Panel createHeaderPanel() {

		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(new HREFBCElement("/site/list", getLabel("sites")));
		bc.addElement(new HREFBCElement("/site/" + getModel().getObject().getId().toString(), new Model<String>(getModel().getObject().getDisplayname())));

		bc.addElement(new BCElement(new Model<String>("floors")));
		JumboPageHeaderPanel<Site> ph = new JumboPageHeaderPanel<Site>("page-header", getModel(), new Model<String>(getModel().getObject().getDisplayname()));
		ph.setBreadCrumb(bc);

		ph.setContext(getLabel("site"));

		if (getModel().getObject().getSubtitle() != null)
			ph.setTagline(Model.of(getModel().getObject().getSubtitle()));

		if (getModel().getObject().getPhoto() != null)
			ph.setPhotoModel(new ObjectModel<Resource>(getModel().getObject().getPhoto()));

		return (ph);
	}

	@Override
	protected Optional<Site> getObject(Long id) {
		return getSite(id);
	}

	@Override
	protected IModel<String> getPageTitle() {
		return getLabel("floors");
	}

	@Override
	protected Panel createSearchPanel() { return null;}


	@Override
	protected void setUpModel() {
		super.setUpModel();

		if (!getModel().getObject().isDependencies()) {
			Optional<Site> o_i = getSiteDBService().findWithDeps(getModel().getObject().getId());
			setModel(new ObjectModel<Site>(o_i.get()));
		}
	}

	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<SimpleAjaxWicketEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(SimpleAjaxWicketEvent event) {

				logger.debug(event.toString());

				if (event.getName().equals(ServerAppConstant.site_action_edit)) {
					//SiteFloorsPage.this.onEdit(event.getTarget());
				}

				if (event.getName().equals(ServerAppConstant.site_page_floors)) {
					SiteFloorsPage.this.togglePanel(ServerAppConstant.site_page_floors, event.getTarget());
				}

				else if (event.getName().equals(ServerAppConstant.site_page_info)) {
					SiteFloorsPage.this.togglePanel(ServerAppConstant.site_page_info, event.getTarget());
				}

				else if (event.getName().equals(ServerAppConstant.object_meta)) {
					SiteFloorsPage.this.togglePanel(ServerAppConstant.object_meta, event.getTarget());
				}

				else if (event.getName().equals(ServerAppConstant.object_audit)) {
					SiteFloorsPage.this.togglePanel(ServerAppConstant.object_audit, event.getTarget());
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

	protected List<ToolbarItem> getToolbarItems() {

		List<ToolbarItem> list = new ArrayList<ToolbarItem>();

		String name = getModel().getObject().getShortName() != null ? getModel().getObject().getShortName() : getModel().getObject().getName();
		list.add(new SiteNavDropDownMenuToolbarItem("item", getModel(), Model.of(name), Align.TOP_RIGHT));

		return list;
	}

	@Override
	protected List<INamedTab> getInternalPanels() {

		List<INamedTab> tabs = super.createInternalPanels();

		NamedTab tab_1 = new NamedTab(Model.of("editor"), ServerAppConstant.site_page_floors) {

			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getEditor(panelId);
			}
		};
		tabs.add(tab_1);
		

		NamedTab audit = new NamedTab(Model.of("audit"), ServerAppConstant.object_audit) {

			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getAuditPanel(panelId);
			}
		};
		tabs.add(audit);

		
		if (getStartTab() == null)
			super.setStartTab(ServerAppConstant.site_page_floors);

		return tabs;
	}

}
