package dellemuse.serverapp.page.user;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.DellemuseServer;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.email.EmailTemplateService;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.help.Help;
import dellemuse.serverapp.help.HelpButtonToolbarItem;
import dellemuse.serverapp.icons.Icons;
import dellemuse.serverapp.page.ObjectListPage;
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.library.ObjectStateEnumSelector;
import dellemuse.serverapp.page.library.ObjectStateListSelector;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.security.config.ImpersonationService;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Music;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.security.Role;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
import dellemuse.serverapp.serverdb.service.ArtExhibitionDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.error.AlertPanel;
import io.wktui.error.ErrorPanel;

import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.toolbar.ButtonCreateToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.struct.list.ListPanelMode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * site foto Info - exhibitions
 */

@AuthorizeInstantiation({ "ROLE_USER" })
@MountPath("/user/list")
public class UserListPage extends ObjectListPage<User> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(UserListPage.class.getName());

	private List<ToolbarItem> list;
	private List<ToolbarItem> listToolbar;

	public String getHelpKey() {
		return Help.USER_LIST;
	}


	public UserListPage() {
		super();
		setIsExpanded(true);
	}

	public UserListPage(PageParameters parameters) {
		super(parameters);
		setIsExpanded(true);
	}

	public ImpersonationService getImpersonationService() {
		return (ImpersonationService) ServiceLocator.getInstance().getBean(ImpersonationService.class);
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
	public boolean canWrite(User m) {
		return isRoot() || isGeneralAdmin();
	}

	@Override
	public boolean canDelete(User m) {
		return isRoot() || isGeneralAdmin();
	}
	
	public void impersonateUser(String username) {

		ServletWebRequest servletRequest = (ServletWebRequest) RequestCycle.get().getRequest();

		HttpServletRequest request = (HttpServletRequest) servletRequest.getContainerRequest();

		HttpServletResponse response = (HttpServletResponse) ((WebResponse) RequestCycle.get().getResponse()).getContainerResponse();

		getImpersonationService().impersonate(username, request, response);

		// restart session state for Wicket
		getSession().bind();

		// reload UI
		setResponsePage(getApplication().getHomePage());
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

		Set<RoleGeneral> set = user.getRolesGeneral();
		if (set == null)
			return false;
		return set.stream().anyMatch((p -> p.getKey().equals(RoleGeneral.ADMIN) || p.getKey().equals(RoleGeneral.AUDIT)));
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
	}

	@Override
	public Iterable<User> getObjects() {
		return super.getUsers();
	}

	@Override
	public Iterable<User> getObjects(ObjectState os1) {
		return this.getObjects(os1, null);
	}

	@Override
	public Iterable<User> getObjects(ObjectState os1, ObjectState os2) {
		return super.getUsers(os1, os2);
	}

	@Override
	public IModel<String> getObjectInfo(IModel<User> model) {
		return new Model<String>(model.getObject().getDisplayname());
	}

	@Override
	public IModel<String> getObjectTitle(IModel<User> model) {

		StringBuilder str = new StringBuilder();

		Optional<Person> op = getPersonDBService().getByUser(model.getObject());
		if (op.isPresent()) {
			str.append(op.get().getLastFirstname());
		}

		str.append(" <span class=\"text-secondary\">( " + model.getObject().getUsername() + " ) </span>");

		User o = model.getObject();

		if (o.getState() == ObjectState.DELETED)
			return new Model<String>(str.toString() + Icons.DELETED_ICON_HTML);

		if (o.getState() == ObjectState.EDITION)
			return new Model<String>(str.toString() + Icons.EDITION_ICON_HTML);

		return Model.of(str.toString());
	}

	@Override
	public void onClick(IModel<User> model) {
		setResponsePage(new UserPage(model, getList(), false));
	}

	@Override
	public IModel<String> getListPanelLabel() {
		return null;
	}

	@Override
	protected Panel getObjectListItemExpandedPanel(IModel<User> model, ListPanelMode mode) {
		return new UserExpandedPanel("expanded-panel", model);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public IModel<String> getPageTitle() {
		return null;
	}

	@Override
	protected String getObjectImageSrc(IModel<User> model) {

		Optional<Person> ouser = getPersonDBService().getByUser(model.getObject());

		if (ouser.isEmpty())
			return null;

		if (ouser.get().getPhoto() != null) {

			Optional<Resource> or = getResourceDBService().findById(ouser.get().getPhoto().getId());
			if (or.isPresent()) {
				return super.getPresignedThumbnailSmall(or.get());
			}
		}
		return null;

	}

	@Override
	protected List<ToolbarItem> getMainToolbarItems() {

		if (list != null)
			return list;

		list = new ArrayList<ToolbarItem>();

		ButtonCreateToolbarItem<User> create = new ButtonCreateToolbarItem<>("item") {
			private static final long serialVersionUID = 1L;

			protected void onClick() {
				UserListPage.this.onCreate();
			}
		};

		list.add(new HelpButtonToolbarItem("item", Align.TOP_RIGHT));

		list.add(create);

		return list;
	}

	@Override
	protected void addHeaderPanel() {

		try {
			BreadCrumb<Void> bc = createBreadCrumb();
			bc.addElement(new BCElement(getLabel("security")));
			bc.addElement(new BCElement(getLabel("users")));
			JumboPageHeaderPanel<Void> ph = new JumboPageHeaderPanel<Void>("page-header", null, getLabel("users"));
			ph.setBreadCrumb(bc);
			ph.setIcon(User.getIcon());
			ph.setHeaderCss("mb-0 pb-2 border-none");
			add(ph);
		} catch (Exception e) {
			logger.error(e);
			addOrReplace(new ErrorPanel("page-header", e));
		}
	}

	protected IModel<String> getTitleLabel() {
		return null;
	}

	protected void onCreate() {

		try {

			User in = getUserDBService().create("new", getUserDBService().findRoot());
			IModel<User> m = new ObjectModel<User>(in);
			getList().add(m);
			setResponsePage(new UserPage(m, getList(), false));

		} catch (Exception e) {
			logger.error(e);
			setResponsePage(new ErrorPage(e));

		}
	}

	@Override
	protected WebMarkupContainer getObjectMenu(IModel<User> model) {

		NavDropDownMenu<User> menu = new NavDropDownMenu<User>("menu", model, null);

		menu.setOutputMarkupId(true);

		menu.setTitleCss("d-block-inline d-sm-block-inline d-md-block-inline d-lg-none d-xl-none d-xxl-none ps-1 pe-1");
		menu.setIconCss("fa-solid fa-ellipsis d-block-inline d-sm-block-inline d-md-block-inline d-lg-block-inline d-xl-block-inline d-xxl-block-inline ps-1 pe-1");

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<User>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<User> getItem(String id) {

				return new LinkMenuItem<User>(id, model) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage(new UserPage(model, getList(), false));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("open");
					}
				};
			}
		});

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<User>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<User> getItem(String id) {

				return new AjaxLinkMenuItem<User>(id, model) {

					private static final long serialVersionUID = 1L;

					public boolean isVisible() {
						return getModel().getObject().getState() != ObjectState.PUBLISHED;
					}

					@Override
					public void onClick(AjaxRequestTarget target) {
						getModel().getObject().setState(ObjectState.PUBLISHED);
						getUserDBService().save(getModel().getObject(), ObjectState.PUBLISHED.getLabel(), getSessionUser().get());
						refresh(target);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("publish");
					}
				};
			}
		});

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<User>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<User> getItem(String id) {
				return new io.wktui.nav.menu.SeparatorMenuItem<User>(id) {
					private static final long serialVersionUID = 1L;
				};
			}
		});

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<User>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<User> getItem(String id) {
				return new LinkMenuItem<User>(id, model) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						try {
							impersonateUser(getModel().getObject().getUsername());

						} catch (Exception e) {
							logger.error(e);
							setResponsePage(new ErrorPage(e));
						}
					}

					@Override
					public boolean isVisible() {
						return isRoot() || isGeneralAdmin();
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("impersonate");
					}
				};

			}
		});

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<User>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<User> getItem(String id) {

				return new AjaxLinkMenuItem<User>(id, model) {

					private static final long serialVersionUID = 1L;

					public boolean isVisible() {
						return isRoot() || isGeneralAdmin();
					}

					@Override
					public void onClick(AjaxRequestTarget target) {
						UserListPage.this.sendWelcomeEmail(getModel().getObject());
						
						refresh(target);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("send-welcome-email");
					}
				};
			}
		});

		
		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<User>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<User> getItem(String id) {
				return new io.wktui.nav.menu.SeparatorMenuItem<User>(id);
			}
		});

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<User>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<User> getItem(String id) {
				return new AjaxLinkMenuItem<User>(id, model) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						try {
							getUserDBService().markAsDeleted(getModel().getObject(), getSessionUser().get());
						} catch (Exception e) {
							logger.error(e);
							UserListPage.this.setErrorPanel(new ErrorPanel("error", e, true));
						}
						refresh(target);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("delete");
					}
				};

			}
		});

		return menu;
	}

	protected boolean sendWelcomeEmail(User user) {

		String to = user.getEmail();
		String subject = getLabel("welcome").getObject();
		String text = "";

		if (to==null 	 || to.isEmpty()) {
			UserListPage.this.setErrorPanel(new AlertPanel<Void>("error", AlertPanel.WARNING, getLabel("user-no-email", user.getUsername())));
			return false;
		}
		
		try {

			Optional<Person> op = getPersonDBService().getByUser(user);

			text = getEmailTemplateService().render(EmailTemplateService.USER_WELCOME, Map.of("application", DellemuseServer.APPNAME, "personName", op.get().getFirstLastname(), "username", user.getUsername(), "forgotpassword",
					getServerUrl() + "/forgot", "signinpage", getServerUrl() + "/" + DellemuseServer.URL_SIGNIN));

			logger.debug("Sending email to -> " + to);
			logger.debug("Sending email subject -> " + subject);
			
		} catch (Exception e) {
			logger.error(e);
			UserListPage.this.setErrorPanel(new ErrorPanel("error", e, true));
			return false;
		}

		try {
			String sendEmail;
			sendEmail = getEmailService().sendHTML(to, subject, text);
			user.setWelcomeEmailSent(OffsetDateTime.now());
			getUserDBService().save(user, "Welcome email", getSessionUser().get());

			logger.debug("Email sent response -> " + sendEmail);
			
			UserListPage.this.setErrorPanel(new AlertPanel<Void>("error", AlertPanel.SUCCESS, getLabel("email-sent", to)));
			
			return true;

		} catch (Exception e) {
			logger.error(e);
			UserListPage.this.setErrorPanel(new ErrorPanel("error", e, true));
			return false;
		}

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

	@Override
	protected String getObjectTitleIcon(IModel<User> model) {
		return null;
	}
}
