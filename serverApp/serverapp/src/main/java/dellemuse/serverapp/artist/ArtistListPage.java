package dellemuse.serverapp.artist;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.global.GlobalFooterPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.global.PageHeaderPanel;
import dellemuse.serverapp.help.Help;
import dellemuse.serverapp.help.HelpButtonToolbarItem;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.ObjectListPage;
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.library.ObjectStateEnumSelector;
import dellemuse.serverapp.page.library.ObjectStateListSelector;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.Artist;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.Voice;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
import dellemuse.serverapp.serverdb.service.ArtistDBService;
import dellemuse.serverapp.serverdb.service.DBService;
import dellemuse.serverapp.serverdb.service.PersonDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
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

@MountPath("/artist/list")
public class ArtistListPage extends ObjectListPage<Artist> {

	private static final long serialVersionUID = 1L;
	
	static private Logger logger = Logger.getLogger(ArtistListPage.class.getName());
	
	private List<ToolbarItem> listToolbar;

	public String getHelpKey() {
		return Help.ARTIST_LIST;
	}
	
	public  ArtistListPage() {
		super();
		 setIsExpanded(true);
		 
	}		
	
	public ArtistListPage(PageParameters parameters) {
		 super(parameters);
		 setIsExpanded(true);
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
		
		if (set==null)
			return false;
		
		return set.stream().anyMatch((p -> p.getKey().equals(RoleGeneral.ADMIN) || p.getKey().equals(RoleGeneral.AUDIT) ));
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
/**
		try {
			Person in = getPersonDBService().create("new", getUserDBService().findRoot());
			IModel<Artist> m =  new ObjectModel<Artist>(in);
			getList().add(m);
			setResponsePage(new PersonPage(m, getList()));
		} catch (Exception e) {
			logger.error(e);	
			setResponsePage(new ErrorPage(e));
							
		}
	**/
	
	}
	
	@Override
	protected WebMarkupContainer getObjectMenu(IModel<Artist> model) {
		
		NavDropDownMenu<Artist> menu = new NavDropDownMenu<Artist>("menu", model, null);
		
		menu.setOutputMarkupId(true);

		menu.setTitleCss
("d-block-inline d-sm-block-inline d-md-block-inline d-lg-none d-xl-none d-xxl-none ps-1 pe-1");
		menu.setIconCss("fa-solid fa-ellipsis d-block-inline d-sm-block-inline d-md-block-inline d-lg-block-inline d-xl-block-inline d-xxl-block-inline ps-1 pe-1");

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Artist>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Artist> getItem(String id) {

				return new LinkMenuItem<Artist>(id, model) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage( new ArtistPage( getModel(), getList()));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("open");
					}
				};
			}
		});
		
		
		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Artist>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Artist> getItem(String id) {

				return new AjaxLinkMenuItem<Artist>(id, model) {

					private static final long serialVersionUID = 1L;

					
					public boolean isVisible() {
						return getModel().getObject().getState()!=ObjectState.PUBLISHED;
					}
				
					@Override
					public void onClick(AjaxRequestTarget target) {
						getModel().getObject().setState(ObjectState.PUBLISHED);
						getArtistDBService().save(getModel().getObject(), ObjectState.PUBLISHED.getLabel(), getSessionUser().get());
						refresh(target);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("publish");
					}
				};
			}
		});
		


		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Artist>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Artist> getItem(String id) {

				return new AjaxLinkMenuItem<Artist>(id, model) {

					private static final long serialVersionUID = 1L;

					
					public boolean isVisible() {
						return getModel().getObject().getState()!=ObjectState.EDITION;
					}
				
					@Override
					public void onClick(AjaxRequestTarget target) {
						getModel().getObject().setState(ObjectState.EDITION);
						getArtistDBService().save(getModel().getObject(), ObjectState.EDITION.getLabel(), getSessionUser().get());
						refresh(target);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("edit-mode");
					}
				};
			}
		});

		
		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Artist>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Artist> getItem(String id) {

				return new AjaxLinkMenuItem<Artist>(id,model) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						getModel().getObject().setState(ObjectState.DELETED);
						getArtistDBService().save(getModel().getObject(), ObjectState.DELETED.getLabel(), getSessionUser().get());
						refresh(target);
					}


					public boolean isVisible() {
						return getModel().getObject().getState()!=ObjectState.DELETED;
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
	    bc.addElement(new BCElement( getLabel("artists")));
	    JumboPageHeaderPanel<Void> ph = new JumboPageHeaderPanel<Void>("page-header", null, getLabel("artists"));
		ph.setBreadCrumb(bc);
		ph.setIcon( Artist.getIcon() );
		ph.setHeaderCss("mb-0 pb-2 border-none");
		add(ph);
	}
	
	
	@Override
	public Iterable<Artist> getObjects() {
		return getArtistDBService().findAllSorted();
		
	}
 

	@Override
	public Iterable<Artist> getObjects(ObjectState os1) {
		 return this.getObjects(os1, null);
	}

	@Override
	public Iterable<Artist> getObjects(ObjectState os1, ObjectState os2) {

		ArtistDBService service = (ArtistDBService) ServiceLocator.getInstance().getBean(ArtistDBService.class);

		if (os1==null && os2==null)
			return service.findAllSorted();
	
		if (os2==null)
			return service.findAllSorted(os1);

		if (os1==null)
			return service.findAllSorted(os2);
		
		return service.findAllSorted(os1, os2);
	}
	 
	@Override
	public IModel<String> getObjectInfo(IModel<Artist> model) {
		return new Model<String>(model.getObject().getInfo());
	}

	 	
	@Override
	public void onClick(IModel<Artist> model) {
		setResponsePage(new ArtistPage(model, getList()));
	}
	@Override
	public IModel<String> getPageTitle() {
		return getLabel("artists");
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
	protected ListPanelMode getListPanelMode() {
		return  ListPanelMode.TITLE;
	}
   
	@Override
	protected String getObjectImageSrc(IModel<Artist> model) {
		 if ( model.getObject().getPhoto()!=null) {
		 		Resource photo = getResource(model.getObject().getPhoto().getId()).get();
		 	    return getPresignedThumbnailSmall(photo);
	     }
		 return null;	
	}

	@Override
	protected List<ToolbarItem> getMainToolbarItems() {
		List<ToolbarItem> list = new ArrayList<ToolbarItem>();
		HelpButtonToolbarItem h = new HelpButtonToolbarItem("item",  Align.TOP_RIGHT);
		list.add(h);
		return list;
	}

	protected  WebMarkupContainer getSubmenu() {
		return null;
	}

	@Override
	protected String getObjectTitleIcon(IModel<Artist> model) {
	 	return null;
	}


	
}
