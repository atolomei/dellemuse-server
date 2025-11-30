package dellemuse.serverapp.page.library;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.Component;
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
import dellemuse.model.ArtWorkModel;
import dellemuse.model.GuideContentModel;
import dellemuse.model.ResourceModel;
import dellemuse.model.SiteModel;
import dellemuse.model.logging.Logger;
import dellemuse.model.util.ThumbnailSize;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.global.GlobalFooterPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.global.PageHeaderPanel;
import dellemuse.serverapp.guidecontent.GuideContentPage;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.ObjectListPage;
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.ArtExhibitionDBService;
import dellemuse.serverapp.serverdb.service.ArtWorkDBService;
import dellemuse.serverapp.serverdb.service.GuideContentDBService;
import dellemuse.serverapp.serverdb.service.InstitutionDBService;
import dellemuse.serverapp.serverdb.service.ResourceDBService;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.ResourceThumbnailService;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.toolbar.ButtonCreateToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;

/**
 * 
 * 
 * 
 */

@MountPath("/guidecontent/list")
public class GuideContentListPage extends ObjectListPage<GuideContent> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(GuideContentListPage.class.getName());

	private List<ToolbarItem> listToolbar;

	public GuideContentListPage() {
		super();
	}

	public GuideContentListPage(PageParameters parameters) {
		super(parameters);
	}

	@Override
	protected List<ToolbarItem> getListToolbarItems() {

		if (listToolbar != null)
			return listToolbar;

		listToolbar = new ArrayList<ToolbarItem>();

		IModel<String> selected = Model.of(ObjectStateEnumSelector.ALL.getLabel(getLocale()));
		ObjectStateListSelector s = new ObjectStateListSelector("item", selected, Align.TOP_LEFT);

		listToolbar.add(s);

		return listToolbar;
	}

	protected void addHeaderPanel() {

		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(new BCElement(getLabel("guide-contents")));
		JumboPageHeaderPanel<Void> ph = new JumboPageHeaderPanel<Void>("page-header", null, getLabel("guide-contents"));
		ph.setBreadCrumb(bc);

		ph.setContext(getLabel("guide-contents"));

		add(ph);
	}

	@Override
	public Iterable<GuideContent> getObjects() {
		GuideContentDBService service = (GuideContentDBService) ServiceLocator.getInstance().getBean(GuideContentDBService.class);
		return service.findAllSorted();
	}

	@Override
	public Iterable<GuideContent> getObjects(ObjectState os1) {
		return this.getObjects(os1, null);
	}

	@Override
	public Iterable<GuideContent> getObjects(ObjectState os1, ObjectState os2) {

		GuideContentDBService service = (GuideContentDBService) ServiceLocator.getInstance().getBean(GuideContentDBService.class);

		if (os1 == null && os2 == null)
			return service.findAllSorted();

		if (os2 == null)
			return service.findAllSorted(os1);

		if (os1 == null)
			return service.findAllSorted(os2);

		return service.findAllSorted(os1, os2);
	}

	@Override
	public IModel<String> getObjectInfo(IModel<GuideContent> model) {
		return new Model<String>(model.getObject().getInfo());
	}

	@Override
	public IModel<String> getObjectTitle(IModel<GuideContent> model) {

		if (model.getObject().getState() == ObjectState.DELETED)
			return new Model<String>(model.getObject().getDisplayname() + ServerConstant.DELETED_ICON);

		return new Model<String>(model.getObject().getDisplayname());
	}

	@Override
	public void onClick(IModel<GuideContent> model) {
		setResponsePage(new GuideContentPage(model, getList()));
	}

	@Override
	public IModel<String> getPageTitle() {
		return getLabel("guide-content");
	}

	@Override
	public IModel<String> getListPanelLabel() {
		return null;
	}

	@Override
	protected WebMarkupContainer getObjectMenu(IModel<GuideContent> model) {

		NavDropDownMenu<GuideContent> menu = new NavDropDownMenu<GuideContent>("menu", model, null);

		menu.setOutputMarkupId(true);

		menu.setLabelCss("d-block-inline d-sm-block-inline d-md-block-inline d-lg-none d-xl-none d-xxl-none ps-1 pe-1");
		menu.setIconCss("fa-solid fa-ellipsis d-block-inline d-sm-block-inline d-md-block-inline d-lg-block-inline d-xl-block-inline d-xxl-block-inline ps-1 pe-1");

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<GuideContent>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<GuideContent> getItem(String id) {

				return new AjaxLinkMenuItem<GuideContent>(id) {

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

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<GuideContent>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<GuideContent> getItem(String id) {

				return new AjaxLinkMenuItem<GuideContent>(id) {

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
	protected List<ToolbarItem> getMainToolbarItems() {
		
		List<ToolbarItem> list = new ArrayList<ToolbarItem>();
		
		ButtonCreateToolbarItem<Void> create = new ButtonCreateToolbarItem<Void>("item") {
			private static final long serialVersionUID = 1L;

			protected void onClick() {
				GuideContentListPage.this.onCreate();
			}
		};
		create.setAlign(Align.TOP_LEFT);

		return list;
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	protected ListPanelMode getListPanelMode() {
		return ListPanelMode.TITLE;
	}

	@Override
	protected String getObjectImageSrc(IModel<GuideContent> model) {
		if (model.getObject().getPhoto() != null) {
			Resource photo = getResource(model.getObject().getPhoto().getId()).get();
			return getPresignedThumbnailSmall(photo);
		}
		return null;
	}

	protected void onCreate() {
		try {
			//GuideContent in = getGuideContentDBService().create("new", getUserDBService().findRoot());
			//IModel<GuideContent> m = new ObjectModel<GuideContent>(in);
			//getList().add(m);

		} catch (Exception e) {
			logger.error(e);
			setResponsePage(new ErrorPage(e));

		}
	}
	
	protected IModel<String> getTitleLabel() {
		return getLabel("guide-contents");
	}

	protected WebMarkupContainer getSubmenu() {
		return null;
	}

	@Override
	protected String getObjectTitleIcon(IModel<GuideContent> model) {
		// TODO Auto-generated method stub
		return null;
	}

}
