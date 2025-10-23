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

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;

import dellemuse.model.ArtExhibitionGuideModel;
import dellemuse.model.ArtExhibitionModel;
import dellemuse.model.ArtWorkModel;
import dellemuse.model.GuideContentModel;
import dellemuse.model.ResourceModel;
import dellemuse.model.SiteModel;
import dellemuse.model.logging.Logger;
import dellemuse.model.util.ThumbnailSize;
import dellemuse.serverapp.global.GlobalFooterPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.global.PageHeaderPanel;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.ObjectListPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.site.SitePage;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.struct.list.ListPanelMode;

/**
 * 
 * site foto Info - exhibitions
 * 
 */

@MountPath("/site/list")
public class SiteListPage extends ObjectListPage<Site> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteListPage.class.getName());

	protected WebMarkupContainer getSubmenu() {
		return null;
	}

	public SiteListPage() {
		super();
		super.setIsExpanded(true);
	}

	public SiteListPage(PageParameters parameters) {
		super(parameters);
		super.setIsExpanded(true);
	}


	@Override
	public Iterable<Site> getObjects() {
		SiteDBService service = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
		return service.findAllSorted();
	}

	@Override
	public IModel<String> getObjectInfo(IModel<Site> model) {
		return new Model<String>(TextCleaner.clean(model.getObject().getInfo(), 280));
	}

	@Override
	public IModel<String> getObjectTitle(IModel<Site> model) {
		return new Model<String>(model.getObject().getDisplayname());
	}

	@Override
	public void onClick(IModel<Site> model) {
		setResponsePage(new SitePage(model, getList()));
	}

	@Override
	public IModel<String> getPageTitle() {
		return getLabel("sites");
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
	public void onInitialize() {
		super.onInitialize();

	}

	
	@Override
	protected void onCreate() {
		Site in = getSiteDBService().create("new", getUserDBService().findRoot());
		IModel<Site> m = new ObjectModel<Site>(in);
		getList().add(m);
		setResponsePage(new SitePage(m, getList()));
	}

	protected void addHeaderPanel() {

		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(new BCElement(getLabel("sites")));
		JumboPageHeaderPanel<Void> ph = new JumboPageHeaderPanel<Void>("page-header", null, getLabel("sites"));
		ph.setBreadCrumb(bc);
		add(ph);
	}

	
	@Override
	protected List<ToolbarItem> getToolbarItems() {
	
		List<ToolbarItem> list = new ArrayList<ToolbarItem>();

	 
		return list;
		
	}


	@Override
	protected ListPanelMode getListPanelMode() {
		return ListPanelMode.TITLE_TEXT_IMAGE;
	}

	@Override
	protected String getObjectImageSrc(IModel<Site> model) {
		if (model.getObject().getPhoto() != null) {
			Resource photo = getResource(model.getObject().getPhoto().getId()).get();
			return getPresignedThumbnailSmall(photo);
		}
		return null;
	}

	 
	 @Override
		protected WebMarkupContainer getObjectMenu(IModel<Site> model) {
			
			NavDropDownMenu<Site> menu = new NavDropDownMenu<Site>("menu", model, null);
			
			menu.setOutputMarkupId(true);

			menu.setLabelCss("d-block-inline d-sm-block-inline d-md-block-inline d-lg-none d-xl-none d-xxl-none ps-1 pe-1");
			menu.setIconCss("fa-solid fa-ellipsis d-block-inline d-sm-block-inline d-md-block-inline d-lg-block-inline d-xl-block-inline d-xxl-block-inline ps-1 pe-1");

			menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Site>() {

				private static final long serialVersionUID = 1L;

				@Override
				public MenuItemPanel<Site> getItem(String id) {

					return new AjaxLinkMenuItem<Site>(id) {

						private static final long serialVersionUID = 1L;

						@Override
						public void onClick(AjaxRequestTarget target) {
							// refresh(target);
						}

						@Override
						public IModel<String> getLabel() {
							return getLabel("edit");
						}
					};
				}
			});

			menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Site>() {

				private static final long serialVersionUID = 1L;

				@Override
				public MenuItemPanel<Site> getItem(String id) {

					return new AjaxLinkMenuItem<Site>(id) {

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
		

}
