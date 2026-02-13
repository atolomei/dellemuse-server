package dellemuse.serverapp.page.site;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.artexhibition.ArtExhibitionPage;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.icons.Icons;
import dellemuse.serverapp.page.ObjectListPage;
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.library.ArtWorkListPage;
import dellemuse.serverapp.page.library.ObjectStateEnumSelector;
import dellemuse.serverapp.page.library.ObjectStateListSelector;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
import dellemuse.serverapp.serverdb.model.security.RoleSite;
import io.odilon.util.Check;
import io.wktui.event.SimpleWicketEvent;
import io.wktui.event.UIEvent;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.menu.SeparatorMenuItem;
import io.wktui.nav.menu.TitleMenuItem;
import io.wktui.nav.toolbar.ButtonCreateToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;

/**
 * 
 * Site Information Exhibitions Artworks Exhibitions
 */
@MountPath("/site/exhibitions/${id}")
public class SiteArtExhibitionsListPage extends ObjectListPage<ArtExhibition> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteArtExhibitionsListPage.class.getName());

	private StringValue stringValue;
	private IModel<Site> siteModel;
	private List<ToolbarItem> listToolbar;

	

	public SiteArtExhibitionsListPage() {
		super();
	}

	public SiteArtExhibitionsListPage(PageParameters parameters) {
		super(parameters);
		setIsExpanded(true);
		this.stringValue = getPageParameters().get("id");
	}

	public SiteArtExhibitionsListPage(IModel<Site> siteModel) {
		super();
		Check.requireNonNullArgument(siteModel, "siteModel is null");
		setIsExpanded(true);
		getPageParameters().add("id", siteModel.getObject().getId().toString());
		setSiteModel(siteModel);
	}

	protected IModel<String> getTitleLabel() {
		return getLabel("exhibitions");
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
	
	@Override
	protected void addHeaderPanel() {
		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(new HREFBCElement("/site/list", getLabel("sites")));
		bc.addElement(new HREFBCElement("/site/" + getSiteModel().getObject().getId().toString(), new Model<String>(getSiteModel().getObject().getDisplayname())));

		bc.addElement(new BCElement(getLabel("artexhibitions")));
		JumboPageHeaderPanel<Void> ph = new JumboPageHeaderPanel<Void>("page-header", null, new Model<String>(getSiteModel().getObject().getDisplayname()));
		ph.setBreadCrumb(bc);

		ph.setContext(getLabel("site"));

		if (getSiteModel().getObject().getSubtitle() != null)
			ph.setTagline(Model.of(getSiteModel().getObject().getSubtitle()));

		if (getSiteModel().getObject().getPhoto() != null)
			ph.setPhotoModel(new ObjectModel<Resource>(getSiteModel().getObject().getPhoto()));

		add(ph);
	}

	@Override
	protected WebMarkupContainer getObjectMenu(IModel<ArtExhibition> model) {

		NavDropDownMenu<ArtExhibition> menu = new NavDropDownMenu<ArtExhibition>("menu", model, null);

		menu.setOutputMarkupId(true);

		menu.setTitleCss
("d-block-inline d-sm-block-inline d-md-block-inline d-lg-none d-xl-none d-xxl-none ps-1 pe-1");
		menu.setIconCss("fa-solid fa-ellipsis d-block-inline d-sm-block-inline d-md-block-inline d-lg-block-inline d-xl-block-inline d-xxl-block-inline ps-1 pe-1");

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibition> getItem(String id) {

				return new  LinkMenuItem<ArtExhibition>(id) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage ( new ArtExhibitionPage(model, getList()));
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

					
					public boolean isEnabled() {
						return getModel().getObject().getState()!=ObjectState.PUBLISHED;
					}
				
					@Override
					public void onClick(AjaxRequestTarget target) {
						getModel().getObject().setState(ObjectState.PUBLISHED);
						getArtExhibitionDBService().save(getModel().getObject());
						refresh(target);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("publish");
					}
				};
			}
		});
		
		
	 
		
		
		
		
		
		
		
		

		if (getArtExhibitionDBService().isArtExhibitionGuides(model.getObject())) {

			menu.addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {
				private static final long serialVersionUID = 1L;

				@Override
				public MenuItemPanel<ArtExhibition> getItem(String id) {
					return new SeparatorMenuItem<ArtExhibition>(id, model);
				}
			});

			menu.addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {
				private static final long serialVersionUID = 1L;

				@Override
				public MenuItemPanel<ArtExhibition> getItem(String id) {
					return new TitleMenuItem<ArtExhibition>(id) {

						private static final long serialVersionUID = 1L;

						@Override
						public IModel<String> getLabel() {
							return getLabel("artexhibition-guides");
						}
					};
				}
			});

			for (ArtExhibitionGuide g : getArtExhibitionDBService().getArtExhibitionGuides(model.getObject())) {

				final String agname = TextCleaner.truncate(getObjectTitle(g).getObject(), 24) +  (g.isAccessible()? Icons.Accesible : "");
				
				
				final Long gid = g.getId();

				menu.addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {

					private static final long serialVersionUID = 1L;

					@Override
					public MenuItemPanel<ArtExhibition> getItem(String id) {

						return new LinkMenuItem<ArtExhibition>(id) {

							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
								setResponsePage(new RedirectPage("/guide/" + gid));
							}

							@Override
							public IModel<String> getLabel() {
								return Model.of(agname);
							}
						};
					}
				});

			}

		}
		
		

		/**
		 * menu.addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {
		 * 
		 * private static final long serialVersionUID = 1L;
		 * 
		 * @Override public MenuItemPanel<ArtExhibition> getItem(String id) {
		 * 
		 *           return new AjaxLinkMenuItem<ArtExhibition>(id) {
		 * 
		 *           private static final long serialVersionUID = 1L;
		 * 
		 * @Override public void onClick(AjaxRequestTarget target) { // refresh(target);
		 *           }
		 * 
		 * @Override public IModel<String> getLabel() { return getLabel("delete"); } };
		 *           } });
		 */

		return menu;
	}

	public IRequestablePage getObjectPage(IModel<ArtExhibition> model) {
		return new ArtExhibitionPage(model, getList());
	}

	@Override
	public Iterable<ArtExhibition> getObjects() {
		return super.getSiteArtExhibitions(getSiteModel().getObject());
	}

	@Override
	public Iterable<ArtExhibition> getObjects(ObjectState os1) {
		return super.getSiteArtExhibitions(getSiteModel().getObject(), os1);
	}

	@Override
	public Iterable<ArtExhibition> getObjects(ObjectState os1, ObjectState os2) {
		return super.getSiteArtExhibitions(getSiteModel().getObject(), os1, os2);
	}

	@Override
	public IModel<String> getObjectInfo(IModel<ArtExhibition> model) {
		
		
		String intro = getLanguageObjectService().getIntro(model.getObject(), getLocale());
		
		if (intro!=null) 
			return Model.of(TextCleaner.truncate(intro, ServerConstant.INTRO_MAX));

		String info = getLanguageObjectService().getInfo(model.getObject(), getLocale());
		return Model.of(TextCleaner.truncate(info, ServerConstant.INTRO_MAX));
		
	}

	
	@Override
	protected String getObjectTitleIcon(IModel<ArtExhibition> model) {
		if (getArtExhibitionDBService().isArtExhibitionGuides(model.getObject()))
			return ServerAppConstant.headphoneIcon;
		else
			return null;
	}

	/**
	@Override
	public IModel<String> getObjectTitle(IModel<ArtExhibition> model) {
		if (model.getObject().getState() == ObjectState.DELETED)
			return new Model<String>(model.getObject().getDisplayname() + ServerConstant.DELETED_ICON);
		return new Model<String>(model.getObject().getDisplayname());
	}**/
	

	@Override
	public void onClick(IModel<ArtExhibition> model) {
		setResponsePage(new ArtExhibitionPage(model, getList()));
	}

	@Override
	public IModel<String> getPageTitle() {
		return new Model<String>(getSiteModel().getObject().getDisplayname());
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

	protected void onCreate() {
		try {
			ArtExhibition in = getArtExhibitionDBService().create("new", getSiteModel().getObject(), getUserDBService().findRoot());
			IModel<ArtExhibition> m = new ObjectModel<ArtExhibition>(in);
			getList().add(m);
			ArtExhibitionPage a = new ArtExhibitionPage(m, getList());
			setResponsePage(a);
		} catch (Exception e) {
			logger.error(e);
			setResponsePage(new ErrorPage(e));
		}
	}

	protected List<ToolbarItem> getMainToolbarItems() {

		List<ToolbarItem> list = new ArrayList<ToolbarItem>();

		list.add(new SiteNavDropDownMenuToolbarItem("item", getSiteModel(), Align.TOP_RIGHT));

		ButtonCreateToolbarItem<Void> create = new ButtonCreateToolbarItem<Void>("item") {
			private static final long serialVersionUID = 1L;

			protected void onClick() {
				SiteArtExhibitionsListPage.this.onCreate();
			}
		};
		create.setAlign(Align.TOP_LEFT);
		list.add(create);

		return list;
	}

	@Override
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

	@Override
	public IModel<String> getListPanelLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getObjectImageSrc(IModel<ArtExhibition> model) {
		if (model.getObject().getPhoto() != null) {
			Resource photo = getResource(model.getObject().getPhoto().getId()).get();
			return getPresignedThumbnailSmall(photo);
		}
		return null;
	}

}
