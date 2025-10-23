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
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.artwork.ArtWorkPage;
import dellemuse.serverapp.global.GlobalFooterPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.global.PageHeaderPanel;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.ObjectListPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.objectstorage.ObjectStorageService;
import dellemuse.serverapp.serverdb.service.ArtExhibitionDBService;
import dellemuse.serverapp.serverdb.service.ArtWorkDBService;
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
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;

/**
 * 
 */

@MountPath("/resource/list")
public class ResourceListPage extends ObjectListPage<Resource> {

	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	static private Logger logger = Logger.getLogger(ResourceListPage.class.getName());
 
	public  ResourceListPage() {
		super();
	}		
	
	public  ResourceListPage(PageParameters parameters) {
		 super(parameters);
		 setIsExpanded(true);
	 }
	
	 protected void addHeaderPanel() {
		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(new BCElement(getLabel("artworks")));
		JumboPageHeaderPanel<Void> ph = new JumboPageHeaderPanel<Void>("page-header", null, getLabel("resources"));
		ph.setBreadCrumb(bc);
		add(ph);
	}

	 

	@Override
	public Iterable<Resource> getObjects() {
		ResourceDBService service = (ResourceDBService) ServiceLocator.getInstance()
				.getBean(ResourceDBService.class);
		return service.findAllSorted();
	}

	@Override
	public IModel<String> getObjectInfo(IModel<Resource> model) {
		return new Model<String>(model.getObject().getInfo());
	}

	@Override
	public IModel<String> getObjectTitle(IModel<Resource> model) {
		return new Model<String>(model.getObject().getDisplayname());
	}

	@Override
	public void onClick(IModel<Resource> model) {
		// setResponsePage(new ArtWorkPage(model, getList()));
	}
	
	@Override
	public IModel<String> getPageTitle() {
		return getLabel("resources");
	}
	
	@Override
	protected IModel<String> getTitleLabel() {
		return null;
	 
	}
	@Override
	public IModel<String> getListPanelLabel() {
		return null;
	}
	
	@Override
	protected WebMarkupContainer getObjectMenu(IModel<Resource> model) {
		
		NavDropDownMenu<Resource> menu = new NavDropDownMenu<Resource>("menu", model, null);
		
		menu.setOutputMarkupId(true);

		menu.setLabelCss(ServerConstant.menuLabelCss);
		menu.setIconCss(ServerConstant.menuIconCss);

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Resource>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Resource> getItem(String id) {

				return new AjaxLinkMenuItem<Resource>(id) {

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

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Resource>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Resource> getItem(String id) {

				return new AjaxLinkMenuItem<Resource>(id) {

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
	public void onDetach() {
		super.onDetach();
	}

	@Override
	protected ListPanelMode getListPanelMode() {
		return  ListPanelMode.TITLE;
	}
   
	protected List<ToolbarItem> getToolbarItems() {return null;}

	
	@Override
	protected String getObjectImageSrc(IModel<Resource> model) {
		return getPresignedThumbnailSmall(model.getObject());
	}
	
	@Override
	protected void onCreate() {
		/**
		ArtWork in = getArtWorkDBService().create("new", getUserDBService().findRoot());
			IModel<Resource> m =  new ObjectModel<Resource>(in);
			getList().add(m);
			
			ArtWorkPage a=new ArtWorkPage(m, getList());
			//a.se
			setResponsePage(a);
		*/
	}

}
