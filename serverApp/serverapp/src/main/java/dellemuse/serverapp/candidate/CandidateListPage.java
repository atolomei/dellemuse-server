package dellemuse.serverapp.candidate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.help.Help;
import dellemuse.serverapp.help.HelpButtonToolbarItem;
import dellemuse.serverapp.icons.Icons;
import dellemuse.serverapp.page.ObjectListPage;
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.library.ObjectStateEnumSelector;
import dellemuse.serverapp.page.library.ObjectStateListSelector;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.Voice;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Artist;
import dellemuse.serverapp.serverdb.model.Candidate;
import dellemuse.serverapp.serverdb.model.CandidateStatus;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
import dellemuse.serverapp.serverdb.service.VoiceDBService;
import dellemuse.serverapp.serverdb.service.CandidateDBService;
import dellemuse.serverapp.serverdb.service.InstitutionDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.error.ErrorPanel;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.toolbar.ButtonCreateToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;

@AuthorizeInstantiation({ "ROLE_USER" })
@MountPath("/candidate/list")
public class CandidateListPage extends ObjectListPage<Candidate> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(CandidateListPage.class.getName());

	private List<ToolbarItem> mainToolbar;
	private List<ToolbarItem> listToolbar;

	@Override
	public boolean hasAccessRight(Optional<User> ouser) {
		if (ouser.isEmpty())
			return false;

		return isRoot() || isGeneralAdmin();
	}

	@Override
	public boolean canEdit() {
		return isRoot() || isGeneralAdmin();
	}

	@Override
	public boolean canCreate() {
		return isRoot() || isGeneralAdmin();
	}

	@Override
	public boolean canWrite(Candidate m) {
		return isRoot() || isGeneralAdmin();
	}

	@Override
	public boolean canDelete(Candidate m) {
		return isRoot() || isGeneralAdmin();
	}

	public CandidateListPage() {
		super();
		super.setIsExpanded(true);
	}

	public CandidateListPage(PageParameters parameters) {
		super(parameters);
		super.setIsExpanded(true);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

	}

	public String getHelpKey() {
		return Help.CANDIDATES;
	}

	@Override
	protected List<ToolbarItem> getListToolbarItems() {

		if (listToolbar != null)
			return listToolbar;

		listToolbar = new ArrayList<ToolbarItem>();

		IModel<String> selected = Model.of(getObjectStateEnumSelector().getLabel(getLocale()));
		ObjectStateListSelector s = new ObjectStateListSelector("item", selected, Align.TOP_LEFT);

		listToolbar.add(s);

		return listToolbar;
	}

	protected void onCreate() {

		try {

			Map<String, String> map = new HashMap<String, String>();

			map.put("name", "name");
			map.put("email", "email");
			map.put("phone", "phone");
			map.put("institutionName", "institutionName");

			Candidate in = getCandidateDBService().create(map, getSessionUser().get());

			in.setStatus(CandidateStatus.EVALUATION);
			getCandidateDBService().save(in, getSessionUser().get());

			setResponsePage(new CandidatePage(new ObjectModel<Candidate>(in), getList()));

		} catch (Exception e) {
			logger.error(e);
			setResponsePage(new ErrorPage(e));
		}
	}

	@Override
	protected List<ToolbarItem> getMainToolbarItems() {

		if (mainToolbar != null)
			return mainToolbar;

		mainToolbar = new ArrayList<ToolbarItem>();

		ButtonCreateToolbarItem<Void> create = new ButtonCreateToolbarItem<Void>("item") {
			private static final long serialVersionUID = 1L;

			public boolean isVisible() {
				return canCreate();
			}

			public boolean isEnabled() {
				return canCreate();
			}

			protected void onClick() {
				CandidateListPage.this.onCreate();
			}
		};
		create.setAlign(Align.TOP_LEFT);
		mainToolbar.add(create);

		HelpButtonToolbarItem h = new HelpButtonToolbarItem("item", Align.TOP_RIGHT);
		mainToolbar.add(h);

		return mainToolbar;
	}

	@Override
	protected WebMarkupContainer getObjectMenu(IModel<Candidate> model) {

		NavDropDownMenu<Candidate> menu = new NavDropDownMenu<Candidate>("menu", model, null);

		menu.setOutputMarkupId(true);
		menu.setTitleCss("d-block-inline d-sm-block-inline d-md-block-inline d-lg-none d-xl-none d-xxl-none ps-1 pe-1");
		menu.setIconCss("fa-solid fa-ellipsis d-block-inline d-sm-block-inline d-md-block-inline d-lg-block-inline d-xl-block-inline d-xxl-block-inline ps-1 pe-1");

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Candidate>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Candidate> getItem(String id) {

				return new LinkMenuItem<Candidate>(id) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage(new CandidatePage(getModel(), getList()));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("open");
					}
				};
			}
		});

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Candidate>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Candidate> getItem(String id) {

				return new AjaxLinkMenuItem<Candidate>(id, model) {

					private static final long serialVersionUID = 1L;

					public boolean isVisible() {
						return canEdit() && getModel().getObject().getState() != ObjectState.EDITION;
					}

					@Override
					public void onClick(AjaxRequestTarget target) {
						getModel().getObject().setState(ObjectState.EDITION);
						getCandidateDBService().save(getModel().getObject(), ObjectState.EDITION.getLabel(), getSessionUser().get());
						refresh(target);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("edit-mode");
					}
				};
			}
		});

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Candidate>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Candidate> getItem(String id) {

				return new AjaxLinkMenuItem<Candidate>(id, model) {

					private static final long serialVersionUID = 1L;

					public boolean isEnabled() {
						return canEdit() && getModel().getObject().getState() != ObjectState.PUBLISHED;
					}

					@Override
					public void onClick(AjaxRequestTarget target) {
						getModel().getObject().setState(ObjectState.PUBLISHED);
						getCandidateDBService().save(getModel().getObject(), ObjectState.PUBLISHED.getLabel(), getSessionUser().get());
						refresh(target);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("publish");
					}
				};
			}
		});

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Candidate>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Candidate> getItem(String id) {

				return new AjaxLinkMenuItem<Candidate>(id, model) {

					private static final long serialVersionUID = 1L;

					public boolean isVisible() {
						return canDelete(getModel().getObject()) && getModel().getObject().getState() != ObjectState.DELETED;
					}

					@Override
					public void onClick(AjaxRequestTarget target) {
						getModel().getObject().setState(ObjectState.DELETED);
						getCandidateDBService().save(getModel().getObject(), ObjectState.DELETED.getLabel(), getSessionUser().get());
						refresh(target);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("delete");
					}
				};
			}
		});

		/**
		 * menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Candidate>() { private
		 * static final long serialVersionUID = 1L;
		 * 
		 * @Override public MenuItemPanel<Candidate> getItem(String id) { return new
		 *           io.wktui.nav.menu.SeparatorMenuItem<Candidate>(id) { private static
		 *           final long serialVersionUID = 1L; }; } });
		 **/

		return menu;
	}

	@Override
	public Iterable<Candidate> getObjects(ObjectState os1) {
		return getObjects(os1, null);
	}

	@Override
	public Iterable<Candidate> getObjects(ObjectState os1, ObjectState os2) {

		CandidateDBService service = (CandidateDBService) ServiceLocator.getInstance().getBean(CandidateDBService.class);

		if (os1 == null && os2 == null)
			return service.findAllSorted();

		if (os2 == null)
			return service.findAllSorted(os1);

		if (os1 == null)
			return service.findAllSorted(os2);

		return service.findAllSorted(os1, os2);
	}

	@Override
	public Iterable<Candidate> getObjects() {
		CandidateDBService service = (CandidateDBService) ServiceLocator.getInstance().getBean(CandidateDBService.class);

		return service.findAllSorted();
	}

	@Override
	public IModel<String> getObjectInfo(IModel<Candidate> model) {
		return new Model<String>(TextCleaner.clean(model.getObject().getComments(), 280));
	}

	@Override
	public IModel<String> getObjectTitle(IModel<Candidate> model) {

		StringBuilder str = new StringBuilder();

		str.append(model.getObject().getDisplayname());

		// str.append(" <span class=\"text-secondary ms-2 me-2\"> ( " +
		// model.getObject().getLanguage() + " - " +
		// model.getObject().getLanguageRegion()+" ) </span>");

		if (model.getObject().getState() == ObjectState.DELETED)
			str.append(model.getObject().getDisplayname() + Icons.DELETED_ICON_HTML);

		if (model.getObject().getState() == ObjectState.EDITION)
			str.append(Icons.EDITION_ICON_HTML);

		return Model.of(str.toString());

	}

	@Override
	public void onClick(IModel<Candidate> model) {
		setResponsePage(new CandidatePage(model, getList()));
	}

	@Override
	public IModel<String> getPageTitle() {
		return getLabel("candidates");
	}

	@Override
	public IModel<String> getListPanelLabel() {
		return null;
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	protected void addHeaderPanel() {
		try {
			BreadCrumb<Void> bc = createBreadCrumb();
			bc.addElement(new BCElement(getLabel("candidates")));
			JumboPageHeaderPanel<Void> ph = new JumboPageHeaderPanel<Void>("page-header", null, getLabel("candidates"));
			ph.setBreadCrumb(bc);
			ph.setIcon(Candidate.getIcon());
			ph.setHeaderCss("mb-2 pb-2 border-none");
			add(ph);
		} catch (Exception e) {
			logger.error(e);
			addOrReplace(new ErrorPanel("page-header", e));
		}
	}

	@Override
	protected String getObjectTitleIcon(IModel<Candidate> model) {
		// TODO Auto-generated method stub
		return null;
	}

}
