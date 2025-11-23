package dellemuse.serverapp.page.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Site;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.ArtExhibitionGuideModel;
import dellemuse.model.ArtExhibitionModel;
import dellemuse.model.GuideContentModel;
import dellemuse.model.SiteModel;
import dellemuse.model.logging.Logger;
import dellemuse.serverapp.global.GlobalFooterPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.global.PageHeaderPanel;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.ObjectListPage;
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.PersonDBService;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.UserDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.toolbar.ButtonCreateToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;

/**
 * site foto Info - exhibitions
 */

@MountPath("/user/list")
public class UserListPage extends ObjectListPage<User> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(UserListPage.class.getName());

	protected IModel<String> getTitleLabel() {
		return null;
	}

	public UserListPage() {
		super();
		setIsExpanded(true);

	}

	public UserListPage(PageParameters parameters) {
		super(parameters);
		setIsExpanded(true);
	}

	protected void onCreate() {

		try {
			User in = getUserDBService().create("new", getUserDBService().findRoot());
			IModel<User> m = new ObjectModel<User>(in);
			getList().add(m);
			// setResponsePage( new UserPage(m,getList()));
		} catch (Exception e) {
			logger.error(e);
			setResponsePage(new ErrorPage(e));

		}
	}

	@Override
	protected WebMarkupContainer getObjectMenu(IModel<User> model) {

		NavDropDownMenu<User> menu = new NavDropDownMenu<User>("menu", model, null);

		menu.setOutputMarkupId(true);

		menu.setLabelCss("d-block-inline d-sm-block-inline d-md-block-inline d-lg-none d-xl-none d-xxl-none ps-1 pe-1");
		menu.setIconCss("fa-solid fa-ellipsis d-block-inline d-sm-block-inline d-md-block-inline d-lg-block-inline d-xl-block-inline d-xxl-block-inline ps-1 pe-1");

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<User>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<User> getItem(String id) {

				return new AjaxLinkMenuItem<User>(id) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						// refresh(target);
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

				return new AjaxLinkMenuItem<User>(id) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						// refresh(target);
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

	@Override
	protected void addHeaderPanel() {
		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(new BCElement(getLabel("users")));

		JumboPageHeaderPanel<Void> ph = new JumboPageHeaderPanel<Void>("page-header", null, getLabel("users"));
		ph.setBreadCrumb(bc);
		add(ph);
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
		return new Model<String>(model.getObject().getDisplayname());
	}

	@Override
	public void onClick(IModel<User> model) {
		setResponsePage(new UserPage(model, getList()));
	}

	@Override
	protected List<ToolbarItem> getMainToolbarItems() {

		List<ToolbarItem> list = new ArrayList<ToolbarItem>();

		ButtonCreateToolbarItem<User> create = new ButtonCreateToolbarItem<>("item") {
			private static final long serialVersionUID = 1L;

			protected void onClick() {
				UserListPage.this.onCreate();
			}
		};

		list.add(create);

		return list;

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
	public IModel<String> getPageTitle() {
		return null;
	}

	@Override
	protected String getObjectImageSrc(IModel<User> model) {
		// if (model.getObject().getPhoto() != null) {
		// Resource photo = getResource(model.getObject().getPhoto().getId()).get();
		// return getPresignedThumbnailSmall(photo);
		// }
		return null;
	}

	@Override
	protected List<ToolbarItem> getListToolbarItems() {
		// TODO Auto-generated method stub
		return null;
	}

}
