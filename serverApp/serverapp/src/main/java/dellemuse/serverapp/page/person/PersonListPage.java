package dellemuse.serverapp.page.person;

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

import com.giffing.wicket.spring.boot.context.scan.WicketHomePage;

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
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.site.SiteArtExhibitionsListPage;
import dellemuse.serverapp.page.site.SiteNavDropDownMenuToolbarItem;
import dellemuse.serverapp.page.site.SitePage;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.service.PersonDBService;
import dellemuse.serverapp.serverdb.service.SiteDBService;
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
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;


/**
 * 
 * site 
 * foto 
 * Info - exhibitions
 * 
 */

@MountPath("/person/list")
public class PersonListPage extends ObjectListPage<Person> {

	private static final long serialVersionUID = 1L;
	
	static private Logger logger = Logger.getLogger(PersonListPage.class.getName());

	
	public  PersonListPage() {
		super();
		 setIsExpanded(true);
		 
	}		
	
	public  PersonListPage(PageParameters parameters) {
		 super(parameters);
		 setIsExpanded(true);
	}
	 	
	@Override
	protected void onCreate() {
			Person in = getPersonDBService().create("new", getUserDBService().findRoot());
			IModel<Person> m =  new ObjectModel<Person>(in);
			getList().add(m);
			setResponsePage(new PersonPage(m, getList()));
	}
	@Override
	protected WebMarkupContainer getObjectMenu(IModel<Person> model) {
		
		NavDropDownMenu<Person> menu = new NavDropDownMenu<Person>("menu", model, null);
		
		menu.setOutputMarkupId(true);

		menu.setLabelCss("d-block-inline d-sm-block-inline d-md-block-inline d-lg-none d-xl-none d-xxl-none ps-1 pe-1");
		menu.setIconCss("fa-solid fa-ellipsis d-block-inline d-sm-block-inline d-md-block-inline d-lg-block-inline d-xl-block-inline d-xxl-block-inline ps-1 pe-1");

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
						return getLabel("edit");
					}
				};
			}
		});

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
		return menu;
	}
	
	
	@Override
	protected void addHeaderPanel() {
		BreadCrumb<Void> bc = createBreadCrumb();
	    bc.addElement(new BCElement( getLabel("persons")));
	    JumboPageHeaderPanel<Void> ph = new JumboPageHeaderPanel<Void>("page-header", null, getLabel("persons"));
		ph.setBreadCrumb(bc);
		add(ph);
	}

	
	 
	
	@Override
	public Iterable<Person> getObjects() {
		return super.getPersons();
	}

	@Override
	public IModel<String> getObjectInfo(IModel<Person> model) {
		return new Model<String>(model.getObject().getInfo());
	}

	@Override
	public IModel<String> getObjectTitle(IModel<Person> model) {
		return new Model<String>(model.getObject().getLastFirstname());
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
	protected List<ToolbarItem> getToolbarItems() {
		List<ToolbarItem> list = new ArrayList<ToolbarItem>();
		ButtonCreateToolbarItem<Void> create = new ButtonCreateToolbarItem<Void>() {
			private static final long serialVersionUID = 1L;
			protected void onClick() {
				PersonListPage.this.onCreate();
			}
		};
		create.setAlign(Align.TOP_LEFT);

		list.add(create);
		
		return list;
	}

	protected  WebMarkupContainer getSubmenu() {
		return null;
	}


	
}
