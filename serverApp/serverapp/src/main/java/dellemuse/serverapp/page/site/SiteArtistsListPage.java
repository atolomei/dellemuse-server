package dellemuse.serverapp.page.site;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.global.PageHeaderPanel;
import dellemuse.serverapp.page.ObjectListItemExpandedPanel;
import dellemuse.serverapp.page.ObjectListPage;
import dellemuse.serverapp.page.library.ObjectStateEnumSelector;
import dellemuse.serverapp.page.library.ObjectStateListSelector;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.person.PersonPage;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Artist;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
import dellemuse.serverapp.serverdb.model.security.RoleSite;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.odilon.util.Check;
import io.wktui.event.SimpleWicketEvent;
import io.wktui.event.UIEvent;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
 
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.struct.list.ListPanelMode;

/**
 * Site Information Exhibitions Artworks Exhibitions
 */
@MountPath("/site/artists/${id}")
public class SiteArtistsListPage extends ObjectListPage<Artist> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteArtistsListPage.class.getName());

	private StringValue stringValue;

	private IModel<Site> siteModel;

	

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

		{
			Set<RoleGeneral> set = user.getRolesGeneral();

			if (set != null) {
				boolean isAccess = set.stream().anyMatch((p -> p.getKey().equals(RoleGeneral.ADMIN) || p.getKey().equals(RoleGeneral.AUDIT)));
				if (isAccess)
					return true;
			}
		}

		{
			final Long sid = getSiteModel().getObject().getId();

			Set<RoleSite> set = user.getRolesSite();
			if (set != null) {
				boolean isAccess = set.stream().anyMatch((p -> p.getSite().getId().equals(sid) && (p.getKey().equals(RoleSite.ADMIN) || p.getKey().equals(RoleSite.EDITOR))));
				if (isAccess)
					return true;
			}
		}

		return false;
	}
	
	
	public SiteArtistsListPage() {
		super();
		setIsExpanded(true);
	}

	public SiteArtistsListPage(PageParameters parameters) {
		super(parameters);
		stringValue = getPageParameters().get("id");
		setIsExpanded(true);
	}

	public SiteArtistsListPage(IModel<Site> siteModel) {
		super();
		Check.requireNonNullArgument(siteModel, "siteModel is null");

		setSiteModel(siteModel);
		getPageParameters().add("id", siteModel.getObject().getId().toString());
		setIsExpanded(true);

	}

	@Override
	public Iterable<Artist> getObjects() {
		SiteDBService service = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
		return service.getArtistsBySiteId(getSiteModel().getObject().getId());
	}

	@Override
	public Iterable<Artist> getObjects(ObjectState os1) {
		throw new RuntimeException("not done");
		//SiteDBService service = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
		//return service.getArtistsBySiteId(getSiteModel().getObject().getId(), os1);
	}

	@Override
	public Iterable<Artist> getObjects(ObjectState os1, ObjectState os2) {
		throw new RuntimeException("not done");
		//SiteDBService service = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
		//return service.getArtistsBySiteId(getSiteModel().getObject().getId(), os1, os2);
	}

	@Override
	public IModel<String> getObjectInfo(IModel<Artist> model) {
		
		
		
		
		String str = TextCleaner.clean(model.getObject().getPerson().getInfo(), 280);
		return new Model<String>(str);
	}

 
	public IModel<String> getObjectSubtitle(IModel<Artist> model) {
		if (model.getObject().getPerson().getSubtitle()==null)
			return null;
		String str = TextCleaner.clean(model.getObject().getPerson().getSubtitle(), 280);
		return new Model<String>(str);
	}
	
	@Override
	public void onClick(IModel<Artist> model) {
		Person person=model.getObject().getPerson();
		if (person!=null)
			setResponsePage(new PersonPage( new ObjectModel<Person>(person)));
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
	public void onConfigure() {
		super.onConfigure();
		logger.debug("on configure");
	}

	@Override
	public void onBeforeRender() {
		super.onBeforeRender();
		logger.debug("onBeforeRender");
	}

	
	@Override
	public void onInitialize() {
		
		if (getSiteModel() == null) {
			if (stringValue != null) {
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

	
	public void setUpModel() {
	
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (siteModel != null)
			siteModel.detach();
	}

	public IModel<Site> getSiteModel() {
		return siteModel;
	}

	public void setSiteModel(IModel<Site> siteModel) {
		this.siteModel = siteModel;
	}

	protected boolean isSettings() {
		return true;
	}

	protected ListPanelMode getListPanelMode() {
		return ListPanelMode.TITLE;
	}

	protected IModel<String> getTitleLabel() {
		return getLabel("artists");
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

	protected List<ToolbarItem> getMainToolbarItems() {
		List<ToolbarItem> list = new ArrayList<ToolbarItem>();
		list.add(new SiteNavDropDownMenuToolbarItem("item", getSiteModel(), Align.TOP_RIGHT));
		return list;
	}

	protected List<ToolbarItem> getToolbarItemsLeft() {
		return null;
	}


	@Override
	protected Panel getObjectListItemExpandedPanel(IModel<Artist> model, ListPanelMode mode) {
		ArtistArtWorksPanel panel = new ArtistArtWorksPanel("expanded-panel", model, getSiteModel());
		return panel;
	}
	

	@Override
	protected String getObjectImageSrc(IModel<Artist> model) {
		if (model != null && model.getObject().getPerson().getPhoto() != null) {
			Resource photo = getResource(model.getObject().getPhoto().getId()).get();
			return getPresignedThumbnailSmall(photo);
		}
		return null;
	}

	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<SimpleWicketEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(SimpleWicketEvent event) {
				if (event.getName().equals(ServerAppConstant.site_action_home)) {
					setResponsePage(new SitePage(getSiteModel(), null));
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

	protected void addHeaderPanel() {

		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(new HREFBCElement("/site/list", getLabel("sites")));
		bc.addElement(new HREFBCElement("/site/" + getSiteModel().getObject().getId().toString(), new Model<String>(getSiteModel().getObject().getDisplayname())));

		bc.addElement(new BCElement(getLabel("artists")));
		JumboPageHeaderPanel<Void> ph = new JumboPageHeaderPanel<Void>("page-header", null, new Model<String>(getSiteModel().getObject().getDisplayname()));

		ph.setContext(getLabel("site"));
	
		if (getSiteModel().getObject().getSubtitle() != null)
			ph.setTagline(Model.of(getSiteModel().getObject().getSubtitle()));

		if (getSiteModel().getObject().getPhoto() != null)
			ph.setPhotoModel(new ObjectModel<Resource>(getSiteModel().getObject().getPhoto()));

		ph.setBreadCrumb(bc);
		add(ph);
	}

	@Override
	protected String getObjectTitleIcon(IModel<Artist> model) {
		return null;
	}

}
