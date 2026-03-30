package dellemuse.serverapp.candidate;

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
import dellemuse.serverapp.help.Help;
import dellemuse.serverapp.help.HelpButtonToolbarItem;

import dellemuse.serverapp.page.ObjectPage;
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.model.ObjectWithDepModel;
import dellemuse.serverapp.page.user.UserNavDropDownMenuToolbarItem;
import dellemuse.serverapp.person.PersonPage;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.Candidate;
import dellemuse.serverapp.serverdb.model.Music;
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
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.breadcrumb.Navigator;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.TitleMenuItem;
import io.wktui.nav.toolbar.DropDownMenuToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;

import wktui.base.INamedTab;
import wktui.base.NamedTab;

@AuthorizeInstantiation({ "ROLE_USER" })
@MountPath("/candidate/${id}")
public class CandidatePage extends ObjectPage<Candidate> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(CandidatePage.class.getName());

	private CandidateEditor editor;

	private CandidateInstitutionEditor institutionEditor;
	private CandidateUserEditor userEditor;

	private List<ToolbarItem> listMenu = null;

	public CandidatePage() {
		super();
	}

	public CandidatePage(PageParameters parameters) {
		super(parameters);
	}

	public CandidatePage(IModel<Candidate> model) {
		super(model);
	}

	public CandidatePage(IModel<Candidate> model, List<IModel<Candidate>> list) {
		super(model, list);
	}

	@Override
	public void onDetach() {
		super.onDetach();

	}

	public String getHelpKey() {
		return Help.CANDIDATE_INFO;
	}

	@Override
	public boolean hasAccessRight(Optional<User> ouser) {

		if (ouser.isEmpty())
			return false;

		return isRoot() || isGeneralAdmin();

	}

	@Override
	public void onInitialize() {
		super.onInitialize();
	}

	@Override
	protected void setUpModel() {
		super.setUpModel();

		if (!getModel().getObject().isDependencies()) {
			Optional<Candidate> o_i = getCandidateDBService().findWithDeps(getModel().getObject().getId());
			setModel(new ObjectWithDepModel<Candidate>(o_i.get()));
		}
	}

	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<SimpleAjaxWicketEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(SimpleAjaxWicketEvent event) {

				logger.debug(event.toString());

				if (event.getName().equals(ServerAppConstant.action_candidate_edit)) {
					CandidatePage.this.onEdit(event.getTarget());
				}
				if (event.getName().equals(ServerAppConstant.action_candidate_insitution_edit)) {
					CandidatePage.this.onInstitutionEdit(event.getTarget());
				}
				if (event.getName().equals(ServerAppConstant.action_candidate_user_edit)) {
					CandidatePage.this.onUserEdit(event.getTarget());
				} else if (event.getName().equals(ServerAppConstant.candidate_info)) {
					CandidatePage.this.togglePanel(ServerAppConstant.candidate_info, event.getTarget());
				} else if (event.getName().equals(ServerAppConstant.candidate_user)) {
					CandidatePage.this.togglePanel(ServerAppConstant.candidate_user, event.getTarget());
				} else if (event.getName().equals(ServerAppConstant.candidate_institution)) {
					CandidatePage.this.togglePanel(ServerAppConstant.candidate_institution, event.getTarget());
				}

				/**
				 * if (event.getName().equals(ServerAppConstant.Music_action_edit_info)) {
				 * MusicPage.this.onEdit(event.getTarget());
				 * 
				 * } else if (event.getName().equals(ServerAppConstant.Music_panel_info)) {
				 * MusicPage.this.togglePanel(ServerAppConstant.Music_panel_info,
				 * event.getTarget()); } else if
				 * (event.getName().equals(ServerAppConstant.Music_panel_password)) {
				 * MusicPage.this.togglePanel(ServerAppConstant.Music_panel_password,
				 * event.getTarget()); } else if
				 * (event.getName().equals(ServerAppConstant.object_meta)) {
				 * MusicPage.this.togglePanel(ServerAppConstant.object_meta, event.getTarget());
				 * } else if (event.getName().equals(ServerAppConstant.object_audit)) {
				 * MusicPage.this.togglePanel(ServerAppConstant.object_audit,
				 * event.getTarget()); } else if
				 * (event.getName().equals(ServerAppConstant.Music_panel_roles)) {
				 * MusicPage.this.togglePanel(ServerAppConstant.Music_panel_roles,
				 * event.getTarget()); } else if
				 * (event.getName().equals(ServerAppConstant.action_object_edit_meta)) {
				 * MusicPage.this.getMetaEditor().onEdit(event.getTarget()); }
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

	protected void onUserEdit(AjaxRequestTarget target) {
		getUserEditor().onEdit(target);
	}

	protected void onInstitutionEdit(AjaxRequestTarget target) {
		getInstitutionEditor().onEdit(target);
	}

	private CandidateInstitutionEditor getInstitutionEditor() {
		return this.institutionEditor;
	}

	private CandidateInstitutionEditor getInstitutionEditor(String id) {
		if (institutionEditor == null)
			institutionEditor = new CandidateInstitutionEditor(id, getModel());
		return (institutionEditor);
	}

	private CandidateUserEditor getUserEditor() {
		return this.userEditor;
	}

	private CandidateUserEditor getUserEditor(String id) {
		if (userEditor == null)
			userEditor = new CandidateUserEditor(id, getModel());
		return (userEditor);
	}

	@Override
	protected Optional<Candidate> getObject(Long id) {
		return getCandidateDBService().findById(id);
	}

	protected Panel getEditor(String id) {
		if (editor == null)
			editor = new CandidateEditor(id, getModel());
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
			bc.addElement(new HREFBCElement("/candidate/list", getLabel("candidate")));
			bc.addElement(new BCElement(new Model<String>(getModel().getObject().getDisplayname())));
			JumboPageHeaderPanel<Candidate> ph = new JumboPageHeaderPanel<Candidate>("page-header", getModel(), new Model<String>(getModel().getObject().getDisplayname()));
			ph.setHeaderCss("mb-0 pb-2 border-none");
			ph.setIcon(Candidate.getIcon());
			ph.setBreadCrumb(bc);

			if (getList() != null && getList().size() > 0) {
				Navigator<Candidate> nav = new Navigator<Candidate>("navigator", getCurrent(), getList()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void navigate(int current) {
						setResponsePage(new CandidatePage(getList().get(current), getList()));
					}
				};
				bc.setNavigator(nav);
			}
			ph.setContext(getLabel("candidate"));
			return (ph);

		} catch (Exception e) {
			logger.error(e);
			return new ErrorPanel("page-header", e);
		}
	}

	@Override
	protected IRequestablePage getObjectPage(IModel<Candidate> model, List<IModel<Candidate>> list) {
		return new CandidatePage(model, list);
	}

	protected void onEdit(AjaxRequestTarget target) {
		this.editor.onEdit(target);
	}

	protected Panel getAuditPanel(String id) {
		return new AuditPanel<Candidate>(id, getModel());
	}

	@Override
	protected List<ToolbarItem> getToolbarItems() {

		if (listMenu != null)
			return (listMenu);

		listMenu = new ArrayList<ToolbarItem>();

		DropDownMenuToolbarItem<Candidate> menu = new DropDownMenuToolbarItem<Candidate>("item", getModel(), Align.TOP_RIGHT);

		menu.setTitle(Model.of(TextCleaner.truncate(getModel().getObject().getInstitutionName(), 24)));

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Candidate>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Candidate> getItem(String id) {
				return new TitleMenuItem<Candidate>(id) {

					private static final long serialVersionUID = 1L;

					@Override
					public IModel<String> getLabel() {
						return getLabel("information");
					}
				};
			}
		});

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Candidate>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Candidate> getItem(String id) {
				return new AjaxLinkMenuItem<Candidate>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						fire(new MenuAjaxEvent(ServerAppConstant.candidate_info, target));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("info");
					}
				};
			}
		});

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Candidate>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Candidate> getItem(String id) {
				return new AjaxLinkMenuItem<Candidate>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						fire(new MenuAjaxEvent(ServerAppConstant.candidate_institution, target));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("institution");
					}
				};
			}
		});

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Candidate>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Candidate> getItem(String id) {
				return new AjaxLinkMenuItem<Candidate>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						fire(new MenuAjaxEvent(ServerAppConstant.candidate_user, target));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("user");
					}
				};
			}
		});

		listMenu.add(menu);

		listMenu.add(new HelpButtonToolbarItem("item", Align.TOP_RIGHT));

		return listMenu;
	}

	@Override
	protected List<INamedTab> getInternalPanels() {

		List<INamedTab> tabs = super.createInternalPanels();

		NamedTab tab_1 = new NamedTab(Model.of("editor"), ServerAppConstant.candidate_info) {
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getEditor(panelId);
			}
		};
		tabs.add(tab_1);

		NamedTab tab_2 = new NamedTab(Model.of("i-editor"), ServerAppConstant.candidate_institution) {
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getInstitutionEditor(panelId);
			}
		};
		tabs.add(tab_2);

		NamedTab tab_3 = new NamedTab(Model.of("u-editor"), ServerAppConstant.candidate_user) {
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getUserEditor(panelId);
			}
		};
		tabs.add(tab_3);

		NamedTab audit = new NamedTab(Model.of("audit"), ServerAppConstant.object_audit) {

			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getAuditPanel(panelId);
			}
		};
		tabs.add(audit);

		if (getStartTab() == null)
			setStartTab(ServerAppConstant.candidate_info);

		return tabs;
	}

	@Override
	protected Panel createSearchPanel() {
		return null;
	}

}