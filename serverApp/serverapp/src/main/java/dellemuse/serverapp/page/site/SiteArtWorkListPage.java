package dellemuse.serverapp.page.site;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.artwork.ArtWorkPage;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.global.PageHeaderPanel;
import dellemuse.serverapp.page.ObjectListPage;
import dellemuse.serverapp.page.library.ObjectStateEnumSelector;
import dellemuse.serverapp.page.library.ObjectStateListSelector;
import dellemuse.serverapp.page.library.SiteListPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.odilon.util.Check;
import io.wktui.event.SimpleWicketEvent;
import io.wktui.event.UIEvent;
import io.wktui.form.FormState;
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
import io.wktui.struct.list.ListPanelMode;

/**
 * Site Information Exhibitions Artworks Exhibitions
 */
@MountPath("/site/artwork/${id}")
public class SiteArtWorkListPage extends ObjectListPage<ArtWork> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteArtWorkListPage.class.getName());

	private StringValue stringValue;

	private IModel<Site> siteModel;

	protected List<ToolbarItem> getToolbarItemsLeft() {return null;}
	
	public SiteArtWorkListPage() {
		super();
		setIsExpanded(true);
	}

	public SiteArtWorkListPage(PageParameters parameters) {
		super(parameters);
		stringValue = getPageParameters().get("id");
		setIsExpanded(true);
		 
	}

	public SiteArtWorkListPage(IModel<Site> siteModel) {
		super();
		Check.requireNonNullArgument(siteModel, "siteModel is null");
		 
		setSiteModel(siteModel);
		getPageParameters().add("id", siteModel.getObject().getId().toString());
		super.setIsExpanded(true);
	}

 

	@Override
	public Iterable<ArtWork> getObjects() {
		return getArtWorks(getSiteModel().getObject());
	}
	
	@Override
	public Iterable<ArtWork> getObjects(ObjectState os1) {
		return getArtWorks(getSiteModel().getObject(), os1);
	}

	

	@Override
	public Iterable<ArtWork> getObjects(ObjectState os1, ObjectState os2) {
		return getArtWorks(getSiteModel().getObject(), os1, os2);
	}
	
	
	

	@Override
	public IModel<String> getObjectInfo(IModel<ArtWork> model) {
		String str = TextCleaner.clean(model.getObject().getInfo(), 280);
		return new Model<String>(str);
	}

	@Override
	public IModel<String> getObjectTitle(IModel<ArtWork> model) {
		StringBuilder str = new StringBuilder();
		str.append(model.getObject().getDisplayname());
		
		if (model.getObject().getState()==ObjectState.DELETED) 
			return new Model<String>(str.toString() + ServerConstant.DELETED_ICON);

		
		return Model.of(str.toString());
	}

	@Override
	public IModel<String> getObjectSubtitle(IModel<ArtWork> model) {
		return Model.of(getArtistStr(model.getObject()));
	}
	
	@Override
	public void onClick(IModel<ArtWork> model) {
		setResponsePage(new ArtWorkPage(model, getList()));
	}

	@Override
	protected WebMarkupContainer getObjectMenu(IModel<ArtWork> model) {
		
		NavDropDownMenu<ArtWork> menu = new NavDropDownMenu<ArtWork>("menu", model, null);
		
		menu.setOutputMarkupId(true);

		menu.setLabelCss("d-block-inline d-sm-block-inline d-md-block-inline d-lg-none d-xl-none d-xxl-none ps-1 pe-1");
		menu.setIconCss("fa-solid fa-ellipsis d-block-inline d-sm-block-inline d-md-block-inline d-lg-block-inline d-xl-block-inline d-xxl-block-inline ps-1 pe-1");

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<ArtWork>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtWork> getItem(String id) {

				return new AjaxLinkMenuItem<ArtWork>(id) {

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

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<ArtWork>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtWork> getItem(String id) {

				return new AjaxLinkMenuItem<ArtWork>(id) {

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
	public IModel<String> getPageTitle() {
		return getLabel("artworks");
	}

	@Override
	public IModel<String> getListPanelLabel() {
		return null;
	}

	public void onConfigure() {
		super.onConfigure();
	}
	
	public IModel<Site> getSiteModel() {
		return siteModel;
	}

	public void setSiteModel(IModel<Site> siteModel) {
		this.siteModel = siteModel;
	}
	@Override
	public void onInitialize() {
		
		if (getSiteModel() == null) {
			if (this.stringValue != null) {
				Optional<Site> o_site = getSite(Long.valueOf(stringValue.toLong()));
				if (o_site.isPresent()) {
					setSiteModel(new ObjectModel<Site>(o_site.get()));
				}
			}
		}
		if (getSiteModel() == null) {
			throw new RuntimeException("no site");
		}

		if (!getSiteModel().getObject().isDependencies()) {
			Optional<Site> o_site = findByIdWithDeps(Long.valueOf(getSiteModel().getObject().getId()));
			if (o_site.isPresent()) {
				setSiteModel(new ObjectModel<Site>(o_site.get()));
			}
		}
 
		super.onInitialize();
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (siteModel != null)
			siteModel.detach();
	}
	
	@Override
	protected void addHeaderPanel() {
		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(new HREFBCElement("/site/list", getLabel("sites")));
		bc.addElement(new HREFBCElement("/site/" + getSiteModel().getObject().getId().toString(),
				new Model<String>(getSiteModel().getObject().getDisplayname())));

		bc.addElement(new BCElement(getLabel("artwork")));
		JumboPageHeaderPanel<Void> ph = new JumboPageHeaderPanel<Void>("page-header", null,
				new Model<String>(getSiteModel().getObject().getDisplayname()));
		ph.setBreadCrumb(bc);
		
		 ph.setContext(getLabel("site"));
		
		if (getSiteModel().getObject().getSubtitle()!=null)
			ph.setTagline( Model.of( getSiteModel().getObject().getSubtitle()));

		 if (getSiteModel().getObject().getPhoto() != null)
				ph.setPhotoModel(new ObjectModel<Resource>(getSiteModel().getObject().getPhoto()));

		add(ph);
	}

	@Override
	protected List<ToolbarItem> getMainToolbarItems() {
		
		List<ToolbarItem> list = new ArrayList<ToolbarItem>();
		
		list.add(new SiteNavDropDownMenuToolbarItem("item", getSiteModel(),  Align.TOP_RIGHT ));
		
		ButtonCreateToolbarItem<Void> create = new ButtonCreateToolbarItem<Void>("item") {
			private static final long serialVersionUID = 1L;
			protected void onClick() {
				SiteArtWorkListPage.this.onCreate();
			}
		};
		create.setAlign(Align.TOP_LEFT);
		
		list.add(create);
		
		return list;
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
	protected void addListeners() {
		super.addListeners();
 
		add(new io.wktui.event.WicketEventListener<SimpleWicketEvent>() {
			private static final long serialVersionUID = 1L;
			@Override
			public void onEvent(SimpleWicketEvent event) {
				if (event.getName().equals(ServerAppConstant.action_site_home)) {
					setResponsePage( new SitePage( getSiteModel(), null));
				}
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof SimpleWicketEvent)
					return true;
				return false;
			}
		});
	}
	
	@Override
	protected IModel<String> getTitleLabel() {
		return getLabel("artworks");
	}
	
	
	@Override
	protected boolean isSettings() {
		return true;
	}

	@Override
	protected ListPanelMode getListPanelMode() {
		return ListPanelMode.TITLE;
	}

	 
	protected void onCreate() {
		ArtWork aw = getArtWorkDBService().create("new", getSiteModel().getObject(), getUserDBService().findRoot());
		IModel<ArtWork> m = new ObjectModel<ArtWork>(aw);
		getList().add(m);
		setResponsePage(new ArtWorkPage(m, getList()));
	}

	@Override
	protected String getObjectImageSrc(IModel<ArtWork> model) {
		if (model.getObject().getPhoto() != null) {
			Resource photo = getResource(model.getObject().getPhoto().getId()).get();
			return getPresignedThumbnailSmall(photo);
		}
		return null;
	}

	
}
