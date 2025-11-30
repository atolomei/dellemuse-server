package dellemuse.serverapp.page.library;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;


import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.artexhibition.ArtExhibitionPage;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.page.ObjectListPage;
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.service.ArtExhibitionDBService;
import dellemuse.serverapp.serverdb.service.InstitutionDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.struct.list.ListPanelMode;

/**
 * 
 *  
 *   
 */

@MountPath("/exhibition/list")
public class ArtExhibitionListPage extends ObjectListPage<ArtExhibition> {

	private static final long serialVersionUID = 1L;
	
	static private Logger logger = Logger.getLogger(ArtExhibitionListPage.class.getName());

	public ArtExhibitionListPage() {
		super();
	}		
	
	public ArtExhibitionListPage(PageParameters parameters) {
		 super(parameters);
	 }

private List<ToolbarItem> listToolbar;
	
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
	
	 
	@Override
	public Iterable<ArtExhibition> getObjects() {
		ArtExhibitionDBService service = (ArtExhibitionDBService) ServiceLocator.getInstance()
				.getBean(ArtExhibitionDBService.class);
		return service.findAllSorted();
	}

	
	@Override
	public Iterable<ArtExhibition> getObjects(ObjectState os1) {
		 return this.getObjects(os1, null);
	}

	
	@Override
	public Iterable<ArtExhibition> getObjects(ObjectState os1, ObjectState os2) {

		ArtExhibitionDBService service = (ArtExhibitionDBService) ServiceLocator.getInstance().getBean(ArtExhibitionDBService.class);

		if (os1==null && os2==null)
			return service.findAllSorted();
	
		if (os2==null)
			return service.findAllSorted(os1);

		if (os1==null)
			return service.findAllSorted(os2);
		
		return service.findAllSorted(os1, os2);
	}

	
	
	@Override
	public IModel<String> getObjectInfo(IModel<ArtExhibition> model) {
		return new Model<String>(model.getObject().getInfo());
	}

	@Override
	public IModel<String> getObjectTitle(IModel<ArtExhibition> model) {
		return new Model<String>(model.getObject().getDisplayname());
	}

	@Override
	public void onClick(IModel<ArtExhibition> model) {
		setResponsePage(new ArtExhibitionPage(model, getList()));
	}
	@Override
	public IModel<String> getPageTitle() {
		return getLabel("exhibitions");
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
	protected ListPanelMode getListPanelMode() {
		return  ListPanelMode.TITLE;
	}
   
	@Override
	protected String getObjectImageSrc(IModel<ArtExhibition> model) {
		 if (model.getObject().getPhoto()!=null) {
		 		Resource photo = getResource(model.getObject().getPhoto().getId()).get();
		 	    return getPresignedThumbnailSmall(photo);
		     }
		  return null;	
	}
	
	@Override
	protected List<ToolbarItem> getMainToolbarItems() {
		return null;
	}

	protected void addHeaderPanel() {
			BreadCrumb<Void> bc = createBreadCrumb();
			bc.addElement(new BCElement(getLabel("exhibitions")));
			JumboPageHeaderPanel<Void> ph = new JumboPageHeaderPanel<Void>("page-header", null, getLabel("exhibitions"));
			ph.setBreadCrumb(bc);
			
			 ph.setContext(getLabel("exhibitions"));
			
			add(ph);
		}

	
	@Override
	protected WebMarkupContainer getObjectMenu(IModel<ArtExhibition> model) {
		
		NavDropDownMenu<ArtExhibition> menu = new NavDropDownMenu<ArtExhibition>("menu", model, null);
		
		menu.setOutputMarkupId(true);

		menu.setLabelCss("d-block-inline d-sm-block-inline d-md-block-inline d-lg-none d-xl-none d-xxl-none ps-1 pe-1");
		menu.setIconCss("fa-solid fa-ellipsis d-block-inline d-sm-block-inline d-md-block-inline d-lg-block-inline d-xl-block-inline d-xxl-block-inline ps-1 pe-1");

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibition> getItem(String id) {

				return new AjaxLinkMenuItem<ArtExhibition>(id) {

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

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibition> getItem(String id) {

				return new AjaxLinkMenuItem<ArtExhibition>(id) {

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
	
	
	protected IModel<String> getTitleLabel() {
		return getLabel("exhibitions");
	}
	
	
	protected  WebMarkupContainer getSubmenu() {
		return null;
	}
	
	 
	protected void onCreate() {
		try {
			ArtExhibition in = getArtExhibitionDBService().create("new", getUserDBService().findRoot());
			IModel<ArtExhibition> m =  new ObjectModel<ArtExhibition>(in);
			getList().add(m);
			setResponsePage(new ArtExhibitionPage(m, getList()));
		} catch (Exception e) {
			logger.error(e);	
			setResponsePage(new ErrorPage(e));
							
		}
	}

	@Override
	protected String getObjectTitleIcon(IModel<ArtExhibition> model) {
		if ( getArtExhibitionDBService().isArtExhibitionGuides(model.getObject()) )
			return ServerAppConstant.headphoneIcon;
		else
			return null;
	}

}
