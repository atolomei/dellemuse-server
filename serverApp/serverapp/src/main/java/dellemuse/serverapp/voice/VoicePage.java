package dellemuse.serverapp.voice;

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
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.model.ObjectWithDepModel;
import dellemuse.serverapp.page.user.UserNavDropDownMenuToolbarItem;
import dellemuse.serverapp.person.PersonPage;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.Voice;
import dellemuse.serverapp.serverdb.model.security.Role;
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
import io.wktui.nav.breadcrumb.Navigator;

import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;

import wktui.base.INamedTab;
import wktui.base.NamedTab;


@AuthorizeInstantiation({"ROLE_USER"})
@MountPath("/voice/${id}")
public class VoicePage extends ObjectPage<Voice> {
	 
	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(VoicePage.class.getName());

	private VoiceEditor editor;
	private List<ToolbarItem> voiceMenu = null;

	 
	public VoicePage() {
		super();
	}

	public VoicePage(PageParameters parameters) {
		super(parameters);
	}

	public VoicePage(IModel<Voice> model) {
		super(model);
	}

	public VoicePage(IModel<Voice> model, List<IModel<Voice>> list) {
		super(model, list);
	}

	
	@Override
	public boolean hasAccessRight(Optional<User> ouser) {
		
		if (ouser.isEmpty())
			return false;

		//User user = ouser.get(); 
		
		//if (user.isRoot()) 
		//	return true;
		
		return true;
		
		/**
		
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

		 

		return false;
		*/
	}


	
	
	@Override
	public void onDetach() {
		super.onDetach();

	}
 


	@Override
	public void onInitialize() {
		super.onInitialize();
	}

	@Override
	protected void setUpModel() {
		super.setUpModel();

		if (!getModel().getObject().isDependencies()) {
			Optional<Voice> o_i = getVoiceDBService().findWithDeps(getModel().getObject().getId());
			setModel(new ObjectWithDepModel<Voice>(o_i.get()));
		}
	}
	
	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<SimpleAjaxWicketEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(SimpleAjaxWicketEvent event) {

				logger.debug(event.toString());
				if (event.getName().equals(ServerAppConstant.action_voice_edit)) { 
					VoicePage.this.onEdit(event.getTarget());
				}
				
				/**
				if (event.getName().equals(ServerAppConstant.Voice_action_edit_info)) { 
					VoicePage.this.onEdit(event.getTarget());
					
				} else if (event.getName().equals(ServerAppConstant.Voice_panel_info)) {
					VoicePage.this.togglePanel(ServerAppConstant.Voice_panel_info, event.getTarget());
				} else if (event.getName().equals(ServerAppConstant.Voice_panel_password)) {
					VoicePage.this.togglePanel(ServerAppConstant.Voice_panel_password, event.getTarget());
				} else if (event.getName().equals(ServerAppConstant.object_meta)) {
					VoicePage.this.togglePanel(ServerAppConstant.object_meta, event.getTarget());
				} else if (event.getName().equals(ServerAppConstant.object_audit)) {
					VoicePage.this.togglePanel(ServerAppConstant.object_audit, event.getTarget());
				} else if (event.getName().equals(ServerAppConstant.Voice_panel_roles)) {
					VoicePage.this.togglePanel(ServerAppConstant.Voice_panel_roles, event.getTarget());
				}
				else if (event.getName().equals(ServerAppConstant.action_object_edit_meta)) {
					VoicePage.this.getMetaEditor().onEdit(event.getTarget());
				}
				*/
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
	protected Optional<Voice> getObject(Long id) {
		return getVoiceDBService().findById(id);
	}

	protected Panel getEditor(String id) {
		if (editor == null)
			editor = new VoiceEditor(id, getModel());
		return (editor);

	}

	@Override
	protected IModel<String> getPageTitle() {
		return new Model<String>(getModel().getObject().getName());
	}

	@Override
	protected Panel createHeaderPanel() {

		try {
			BreadCrumb<Void> bc = createBreadCrumb();
			bc.addElement(new HREFBCElement("/voice/list", getLabel("voices")));
			bc.addElement(new BCElement(new Model<String>(getModel().getObject().getName())));
			JumboPageHeaderPanel<Voice> ph = new JumboPageHeaderPanel<Voice>("page-header", getModel(), new Model<String>(getModel().getObject().getDisplayname()));
			ph.setHeaderCss("mb-0 pb-2 border-none");
			ph.setIcon(Voice.getIcon());
			ph.setBreadCrumb(bc);

			if (getList() != null && getList().size() > 0) {
				Navigator<Voice> nav = new Navigator<Voice>("navigator", getCurrent(), getList()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void navigate(int current) {
						setResponsePage(new VoicePage(getList().get(current), getList()));
					}
				};
				bc.setNavigator(nav);
			}
			ph.setContext(getLabel("voice"));
			return (ph);

		} catch (Exception e) {
			logger.error(e);
			return new ErrorPanel("page-header", e);
		}
	}

	@Override
	protected IRequestablePage getObjectPage(IModel<Voice> model, List<IModel<Voice>> list) {
		return new VoicePage(model, list);
	}

	 
	protected void onEdit(AjaxRequestTarget target) {
		this.editor.onEdit(target);
	}

	protected Panel getAuditPanel(String id) {
		return new AuditPanel<Voice>(id, getModel());
	}
	
	

	@Override
	protected List<ToolbarItem> getToolbarItems() {

		if (voiceMenu != null)
			return (voiceMenu);

		voiceMenu = new ArrayList<ToolbarItem>();
		
		VoiceNavDropDownToolbarItem vn = new VoiceNavDropDownToolbarItem("item", getModel(), getLabel("voice"), Align.TOP_RIGHT);
		voiceMenu.add(vn);

		return voiceMenu;
	}
	
	 
	
	

	/**
	@Override
	protected List<INamedTab> createInternalPanels() {

		List<INamedTab> list = super.createInternalPanels();

		NamedTab editor = new NamedTab(Model.of("editor"), ServerAppConstant.voice_info) {

			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getEditor(panelId);
			}
		};
		
		NamedTab audit = new NamedTab(Model.of("audit"), ServerAppConstant.object_audit) {

			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getAuditPanel(panelId);
			}
		};

		list.add(audit);
		return list;
	}
	*/
	
	@Override
	protected List<INamedTab> getInternalPanels() {

		List<INamedTab> tabs = super.createInternalPanels();

		 
		NamedTab tab_1 = new NamedTab(Model.of("editor"), ServerAppConstant.voice_info) {
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
			setStartTab(ServerAppConstant.voice_info);

		return tabs;
	}

	@Override
	protected Panel createSearchPanel() { 
		return null;
	}

}
