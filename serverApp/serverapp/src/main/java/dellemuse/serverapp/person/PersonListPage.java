package dellemuse.serverapp.person;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.maven.model.Site;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
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
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.Voice;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
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


@AuthorizeInstantiation("ROLE_USER")
@MountPath("/person/list")
public class PersonListPage extends ObjectListPage<Person> {

	private static final long serialVersionUID = 1L;
	
	static private Logger logger = Logger.getLogger(PersonListPage.class.getName());
	private List<ToolbarItem> listToolbar;

	public  PersonListPage() {
		super();
		 setIsExpanded(true);
		 
	}		
	
	public String getHelpKey() {
		return Help.PERSON_LIST ;
	}
	
	
	
	public PersonListPage(PageParameters parameters) {
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

		{
		IModel<String> selected = Model.of(ObjectStateEnumSelector.ALL.getLabel(getLocale()));
		ObjectStateListSelector s = new ObjectStateListSelector("item", selected, Align.TOP_LEFT);
		listToolbar.add(s);
		}
		
		 
		
		return listToolbar;
	}

	
	protected void onCreate() {
		try {
			Person in = getPersonDBService().create("new", getUserDBService().findRoot());
			IModel<Person> m =  new ObjectModel<Person>(in);
			getList().add(m);
			setResponsePage(new PersonPage(m, getList()));
		} catch (Exception e) {
			logger.error(e);	
			setResponsePage(new ErrorPage(e));
							
		}
	}
	
	@Override
	protected WebMarkupContainer getObjectMenu(IModel<Person> model) {
		
		NavDropDownMenu<Person> menu = new NavDropDownMenu<Person>("menu", model, null);
		
		menu.setOutputMarkupId(true);

		menu.setTitleCss
("d-block-inline d-sm-block-inline d-md-block-inline d-lg-none d-xl-none d-xxl-none ps-1 pe-1");
		menu.setIconCss("fa-solid fa-ellipsis d-block-inline d-sm-block-inline d-md-block-inline d-lg-block-inline d-xl-block-inline d-xxl-block-inline ps-1 pe-1");

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Person>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Person> getItem(String id) {

				return new LinkMenuItem<Person>(id) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage( new PersonPage( getModel(), getList()));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("open");
					}
				};
			}
		});
		
		
		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Person>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Person> getItem(String id) {

				return new AjaxLinkMenuItem<Person>(id, model) {

					private static final long serialVersionUID = 1L;

					
					public boolean isEnabled() {
						return getModel().getObject().getState()!=ObjectState.PUBLISHED;
					}
				
					@Override
					public void onClick(AjaxRequestTarget target) {
						getModel().getObject().setState(ObjectState.PUBLISHED);
						getPersonDBService().save(getModel().getObject(), ObjectState.PUBLISHED.getLabel(), getSessionUser().get());
						refresh(target);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("publish");
					}
				};
			}
		});
		
		
		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Person>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Person> getItem(String id) {

				return new AjaxLinkMenuItem<Person>(id, model) {

					private static final long serialVersionUID = 1L;

					
					public boolean isEnabled() {
						return getModel().getObject().getState()!=ObjectState.EDITION;
					}
				
					@Override
					public void onClick(AjaxRequestTarget target) {
						getModel().getObject().setState(ObjectState.EDITION);
						getPersonDBService().save(getModel().getObject(), ObjectState.EDITION.getLabel(), getSessionUser().get());
						refresh(target);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("edit-mode");
					}
				};
			}
		});

		/**
		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Person>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Person> getItem(String id) {

				return new AjaxLinkMenuItem<Person>(id) {

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
		
		*/
		return menu;
	}
	
	
	@Override
	protected void addHeaderPanel() {
		BreadCrumb<Void> bc = createBreadCrumb();
	    bc.addElement(new BCElement( getLabel("persons")));
	    JumboPageHeaderPanel<Void> ph = new JumboPageHeaderPanel<Void>("page-header", null, getLabel("persons"));
		ph.setBreadCrumb(bc);
		ph.setIcon( "fa-duotone fa-solid fa-user-group" );
		ph.setHeaderCss("mb-0 pb-2 border-none");
		add(ph);
	}
	
	@Override
	public Iterable<Person> getObjects() {
		return super.getPersons();
	}

	@Override
	public Iterable<Person> getObjects(ObjectState os1) {
		 return this.getObjects(os1, null);
	}

	@Override
	public Iterable<Person> getObjects(ObjectState os1, ObjectState os2) {

		PersonDBService service = (PersonDBService) ServiceLocator.getInstance().getBean(PersonDBService.class);

		if (os1==null && os2==null)
			return service.findAllSorted();
	
		if (os2==null)
			return service.findAllSorted(os1);

		if (os1==null)
			return service.findAllSorted(os2);
		
		return service.findAllSorted(os1, os2);
	}
	 
	@Override
	public IModel<String> getObjectInfo(IModel<Person> model) {
		return new Model<String>(model.getObject().getInfo());
	}

	 	
	@Override
	public void onClick(IModel<Person> model) {
		setResponsePage(new PersonPage(model, getList()));
	}
	@Override
	public IModel<String> getPageTitle() {
		return getLabel("persons");
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
	protected String getObjectImageSrc(IModel<Person> model) {
		 if ( model.getObject().getPhoto()!=null) {
		 		Resource photo = getResource(model.getObject().getPhoto().getId()).get();
		 	    return getPresignedThumbnailSmall(photo);
	     }
		 return null;	
	}

	@Override
	protected List<ToolbarItem> getMainToolbarItems() {
		List<ToolbarItem> list = new ArrayList<ToolbarItem>();
		ButtonCreateToolbarItem<Void> create = new ButtonCreateToolbarItem<Void>() {
			private static final long serialVersionUID = 1L;
			protected void onClick() {
				PersonListPage.this.onCreate();
			}
		};
		create.setAlign(Align.TOP_LEFT);
		list.add(create);
		
		list.add(new HelpButtonToolbarItem("item",  Align.TOP_RIGHT));
		return list;
	}

	protected  WebMarkupContainer getSubmenu() {
		return null;
	}

	@Override
	protected String getObjectTitleIcon(IModel<Person> model) {
	 	return null;
	}


	
}
