package dellemuse.serverapp.page.site;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.artexhibition.ArtExhibitionPage;

import dellemuse.serverapp.editor.ObjectMetaEditor;
import dellemuse.serverapp.editor.SimpleAlertRow;
import dellemuse.serverapp.global.GlobalFooterPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.institution.InstitutionPage;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.page.DelleMuseObjectListItemPanel;
import dellemuse.serverapp.page.ObjectListItemExpandedPanel;
import dellemuse.serverapp.page.ObjectListPage;
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
import dellemuse.serverapp.serverdb.model.security.RoleSite;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.odilon.util.Check;
import io.wktui.error.ErrorPanel;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.event.SimpleAjaxWicketEvent;
import io.wktui.event.UIEvent;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.breadcrumb.Navigator;
import io.wktui.nav.listNavigator.ListNavigator;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.menu.SeparatorMenuItem;
import io.wktui.nav.menu.TitleMenuItem;
import io.wktui.nav.toolbar.ButtonToolbarItem;
import io.wktui.nav.toolbar.LinkButtonToolbarItem;
import io.wktui.nav.toolbar.Toolbar;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;

import wktui.base.InvisiblePanel;

/**
 * 
 * site foto Info - exhibitions
 * 
 */

@MountPath("/site/${id}")
public class SitePage extends BasePage {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SitePage.class.getName());

	private StringValue stringValue;
	private IModel<Site> siteModel;

	private Link<Site> linkInfo;
	private Link<Site> linkInst;

	private Link<Site> linkArtWork;
	private Link<Site> linkArtists;

	private Link<Site> linkFloors;

	private Link<Site> linkExhibitions;

	private List<IModel<ArtExhibition>> listPermanent;
	private List<IModel<ArtExhibition>> listTemporary;
	private List<IModel<ArtExhibition>> listTemporaryPast;
	private List<IModel<ArtExhibition>> listTemporaryComing;

	private List<IModel<Site>> siteList;
	private int current = 0;
	private WebMarkupContainer navigatorContainer;

	private WebMarkupContainer exToolbarContainer;

	private ObjectMetaEditor<Site> metaEditor;

	private boolean listsLoaded = false;

	private WebMarkupContainer exhibitionsContainer;

	private WebMarkupContainer linksContainer;

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

	public SitePage() {
		super();
	}

	public SitePage(PageParameters parameters) {
		super(parameters);
		stringValue = getPageParameters().get("id");
	}

	public SitePage(IModel<Site> model) {
		this(model, null);
	}

	public SitePage(IModel<Site> model, List<IModel<Site>> list) {
		Check.requireNonNullArgument(model, "model is null");
		Check.requireTrue(model.getObject() != null, "modelOjbect is null");
		setSiteModel(model);
		getPageParameters().add("id", model.getObject().getId().toString());
		this.setSiteList(list);
	}

	protected void addErrorPanels(Exception e) {

		if (getSessionUser().isPresent())
			addOrReplace(new GlobalTopPanel("top-panel", new ObjectModel<User>(getSessionUser().get())));
		else
			addOrReplace(new GlobalTopPanel("top-panel"));

		addOrReplace(new GlobalFooterPanel<>("footer-panel"));

		addOrReplace(new InvisiblePanel("navigatorContainer"));
		addOrReplace(new InvisiblePanel("exhibitionsContainer"));
		addOrReplace(new InvisiblePanel("linksContainer"));
		addOrReplace(new InvisiblePanel("toolbarContainer"));
		addOrReplace(new InvisiblePanel("siteInfoContainer"));
		addOrReplace(new InvisiblePanel("securityContainer"));

		SimpleAlertRow<Void> r = new SimpleAlertRow<Void>("error", e);
		addOrReplace(r);

	}

	/**
	 * Institution Site Artwork Person Exhibition ExhibitionItem GuideContent User
	 */
	@Override
	public void onInitialize() {
		super.onInitialize();

		try {
			setUpModel();
		} catch (Exception e) {
			logger.error(e);
			addErrorPanels(e);
			return;
		}

		setCurrent();
		addHeader();

		add(new GlobalTopPanel("top-panel", new ObjectModel<User>(getSessionUser().get())));
		add(new GlobalFooterPanel<>("footer-panel"));

		if (!hasAccessRight(getSessionUser())) {
			SimpleAlertRow<Void> r = new SimpleAlertRow<Void>("error");
			r.setText(getLabel("not-authorized"));
			add(r);
			add(new InvisiblePanel("navigatorContainer"));
			add(new InvisiblePanel("exhibitionsContainer"));
			add(new InvisiblePanel("linksContainer"));
			add(new InvisiblePanel("toolbarContainer"));
			add(new InvisiblePanel("siteInfoContainer"));
			addOrReplace(new InvisiblePanel("securityContainer"));
			return;
		}

		add(new InvisiblePanel("error"));

		addSiteInfo();
		addCatalog();
		addExhibitions();
		addNavigator();
		addToolbar();
		addSecurityPanel();

	}

	protected IModel<Institution> getInstitutionModel() {
		return new ObjectModel<Institution>(getInstitution(getSiteModel().getObject().getInstitution().getId()).get());
	}

	protected int getCurrent() {
		return this.current;
	}

	protected Panel getMetaEditor(String id) {
		if (this.metaEditor == null)
			metaEditor = new ObjectMetaEditor<Site>(id, getSiteModel());
		return (metaEditor);
	}

	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<SimpleAjaxWicketEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(SimpleAjaxWicketEvent event) {

				if (event.getName().equals(ServerAppConstant.action_object_edit_meta)) {
					SitePage.this.metaEditor.onEdit(event.getTarget());
				}
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof MenuAjaxEvent)
					return true;
				return false;
			}
		});

	}

	private void addNavigator() {

		this.navigatorContainer = new WebMarkupContainer("navigatorContainer");
		add(this.navigatorContainer);

		this.navigatorContainer.setVisible(getSiteList() != null && getSiteList().size() > 0);

		if (getSiteList() != null) {
			ListNavigator<Site> nav = new ListNavigator<Site>("navigator", getCurrent(), getSiteList()) {
				private static final long serialVersionUID = 1L;

				@Override
				protected IModel<String> getLabel(IModel<Site> model) {
					return new Model<String>(model.getObject().getDisplayname());
				}

				@Override
				protected void navigate(int current) {
					setResponsePage(new SitePage(getSiteList().get(current), getList()));
				}
			};
			this.navigatorContainer.add(nav);
		} else {
			this.navigatorContainer.add(new InvisiblePanel("navigator"));
		}
	}

	/*
	 * ¨*
	 * 
	 * 
	 * 
	 */
	private void addExhibitions() {

		this.exhibitionsContainer = new WebMarkupContainer("exhibitionsContainer");
		add(this.exhibitionsContainer);

		this.exToolbarContainer = new WebMarkupContainer("exToolbarContainer");
		this.exToolbarContainer.setOutputMarkupId(true);
		this.exhibitionsContainer.add(this.exToolbarContainer);

		Toolbar toolbarItems = new Toolbar("toolbarItems");
		List<ToolbarItem> list = new ArrayList<ToolbarItem>();

		ButtonToolbarItem<Site> create = new LinkButtonToolbarItem<Site>(getSiteModel(), getLabel("create")) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick() {

				SitePage.this.onCreateExhibition();

			}
		};
		list.add(create);

		list.forEach(t -> toolbarItems.addItem(t));
		this.exToolbarContainer.addOrReplace(toolbarItems);

		{
			ListPanel<ArtExhibition> panel = new ListPanel<>("exhibitionsPermanent", getArtExhibitionsPermanent()) {
				private static final long serialVersionUID = 1L;

				protected List<IModel<ArtExhibition>> filter(List<IModel<ArtExhibition>> initialList, String filter) {
					List<IModel<ArtExhibition>> list = new ArrayList<IModel<ArtExhibition>>();
					final String str = filter.trim().toLowerCase();
					initialList.forEach(s -> {
						if (s.getObject().getDisplayname().toLowerCase().contains(str)) {
							list.add(s);
						}
					});
					return list;
				}

				@Override
				protected Panel getListItemExpandedPanel(IModel<ArtExhibition> model, ListPanelMode mode) {
					logger.debug("expand");
					return new ObjectListItemExpandedPanel<ArtExhibition>("expanded-panel", model, mode) {

						private static final long serialVersionUID = 1L;

						@Override
						protected IModel<String> getObjectSubtitle() {
							if (getMode() == ListPanelMode.TITLE)
								return null;
							return Model.of(getModel().getObject().getSubtitle());
						}

						@Override
						protected String getImageSrc() {
							if (getModel().getObject().getPhoto() != null) {
								Resource photo = getResource(model.getObject().getPhoto().getId()).get();
								return getPresignedThumbnailSmall(photo);
							}
							return null;
						}

						@Override
						public void onClick() {
							setResponsePage(new ArtExhibitionPage(getModel(), getWorkingItems()));
						}

						protected IModel<String> getInfo() {
							String str = TextCleaner.clean(getModel().getObject().getIntro());
							return new Model<String>(str);
						}

					};
				}

				@Override
				protected Panel getListItemPanel(IModel<ArtExhibition> model) {
					DelleMuseObjectListItemPanel<ArtExhibition> panel = new DelleMuseObjectListItemPanel<ArtExhibition>("row-element", model, getListPanelMode()) {
						private static final long serialVersionUID = 1L;

						@Override
						protected String getImageSrc() {
							if (getModel().getObject().getPhoto() != null) {
								Resource photo = getResource(model.getObject().getPhoto().getId()).get();
								return getPresignedThumbnailSmall(photo);
							}
							return null;
						}

						@Override
						public void onClick() {
							setResponsePage(new ArtExhibitionPage(getModel(), getWorkingItems()));
						}

						@Override
						protected String getTitleIcon() {
							return SitePage.this.getObjectTitleIcon(getModel());
						}

						protected IModel<String> getInfo() {
							String str = TextCleaner.clean(getModel().getObject().getIntro());
							return new Model<String>(str);
						}

						@Override
						protected WebMarkupContainer getObjectMenu() {
							return SitePage.this.getObjectMenu(getModel());
						}

					};
					return panel;
				}
			};

			panel.setHasExpander(true);
			this.exhibitionsContainer.add(panel);
			panel.setListPanelMode(ListPanelMode.TITLE);
			panel.setLiveSearch(false);
			panel.setSettings(true);

		}

		{
			ListPanel<ArtExhibition> panel = new ListPanel<>("exhibitionsTemporary", getArtExhibitionsTemporary()) {
				private static final long serialVersionUID = 1L;

				protected List<IModel<ArtExhibition>> filter(List<IModel<ArtExhibition>> initialList, String filter) {
					List<IModel<ArtExhibition>> list = new ArrayList<IModel<ArtExhibition>>();
					final String str = filter.trim().toLowerCase();
					initialList.forEach(s -> {
						if (s.getObject().getDisplayname().toLowerCase().contains(str)) {
							list.add(s);
						}
					});
					return list;
				}

				@Override
				protected Panel getListItemExpandedPanel(IModel<ArtExhibition> model, ListPanelMode mode) {
					logger.debug("expand");
					return new ObjectListItemExpandedPanel<ArtExhibition>("expanded-panel", model, mode) {

						private static final long serialVersionUID = 1L;

						@Override
						protected IModel<String> getObjectSubtitle() {
							if (getMode() == ListPanelMode.TITLE)
								return null;
							return Model.of(getModel().getObject().getSubtitle());
						}

						@Override
						protected String getImageSrc() {
							if (getModel().getObject().getPhoto() != null) {
								Resource photo = getResource(model.getObject().getPhoto().getId()).get();
								return getPresignedThumbnailSmall(photo);
							}
							return null;
						}

						@Override
						public void onClick() {
							setResponsePage(new ArtExhibitionPage(getModel(), getWorkingItems()));
						}

						protected IModel<String> getInfo() {
							String str = TextCleaner.clean(getModel().getObject().getIntro());
							return new Model<String>(str);
						}

					};
				}

				@Override
				protected Panel getListItemPanel(IModel<ArtExhibition> model) {
					DelleMuseObjectListItemPanel<ArtExhibition> panel = new DelleMuseObjectListItemPanel<ArtExhibition>("row-element", model, getListPanelMode()) {
						private static final long serialVersionUID = 1L;

						@Override
						protected String getImageSrc() {
							if (getModel().getObject().getPhoto() != null) {
								Resource photo = getResource(model.getObject().getPhoto().getId()).get();
								return getPresignedThumbnailSmall(photo);
							}
							return null;
						}

						@Override
						public void onClick() {
							setResponsePage(new ArtExhibitionPage(getModel(), getWorkingItems()));
						}

						@Override
						protected IModel<String> getInfo() {
							String str = TextCleaner.clean(getModel().getObject().getIntro());
							return new Model<String>(str);
						}

						@Override
						protected String getTitleIcon() {
							return SitePage.this.getObjectTitleIcon(getModel());
						}

						@Override
						protected WebMarkupContainer getObjectMenu() {
							return SitePage.this.getObjectMenu(getModel());
						}

					};
					return panel;
				}
			};
			this.exhibitionsContainer.add(panel);

			// panel.setTitle(getLabel("exhibitions-temporary"));

			panel.setListPanelMode(ListPanelMode.TITLE);
			panel.setLiveSearch(false);
			panel.setHasExpander(true);
			panel.setSettings(true);
		}

		{
			ListPanel<ArtExhibition> panel = new ListPanel<>("exhibitionsTemporaryPast", getArtExhibitionsTemporaryPast()) {
				private static final long serialVersionUID = 1L;

				protected List<IModel<ArtExhibition>> filter(List<IModel<ArtExhibition>> initialList, String filter) {
					List<IModel<ArtExhibition>> list = new ArrayList<IModel<ArtExhibition>>();
					final String str = filter.trim().toLowerCase();
					initialList.forEach(s -> {
						if (s.getObject().getDisplayname().toLowerCase().contains(str)) {
							list.add(s);
						}
					});
					return list;
				}

				@Override
				protected Panel getListItemExpandedPanel(IModel<ArtExhibition> model, ListPanelMode mode) {
					logger.debug("expand");
					return new ObjectListItemExpandedPanel<ArtExhibition>("expanded-panel", model, mode) {

						private static final long serialVersionUID = 1L;

						@Override
						protected IModel<String> getObjectSubtitle() {
							if (getMode() == ListPanelMode.TITLE)
								return null;
							return Model.of(getModel().getObject().getSubtitle());
						}

						@Override
						protected String getImageSrc() {
							if (getModel().getObject().getPhoto() != null) {
								Resource photo = getResource(model.getObject().getPhoto().getId()).get();
								return getPresignedThumbnailSmall(photo);
							}
							return null;
						}

						@Override
						public void onClick() {
							setResponsePage(new ArtExhibitionPage(getModel(), getWorkingItems()));
						}

						protected IModel<String> getInfo() {
							String str = TextCleaner.clean(getModel().getObject().getIntro());
							return new Model<String>(str);
						}

					};
				}

				@Override
				protected Panel getListItemPanel(IModel<ArtExhibition> model) {
					DelleMuseObjectListItemPanel<ArtExhibition> panel = new DelleMuseObjectListItemPanel<ArtExhibition>("row-element", model, getListPanelMode()) {
						private static final long serialVersionUID = 1L;

						@Override
						protected String getImageSrc() {
							if (getModel().getObject().getPhoto() != null) {
								Resource photo = getResource(model.getObject().getPhoto().getId()).get();
								return getPresignedThumbnailSmall(photo);
							}
							return null;
						}

						@Override
						public void onClick() {
							setResponsePage(new ArtExhibitionPage(getModel(), getWorkingItems()));
						}

						@Override
						protected String getTitleIcon() {
							return SitePage.this.getObjectTitleIcon(getModel());
						}

						protected IModel<String> getInfo() {
							String str = TextCleaner.clean(getModel().getObject().getIntro());
							return new Model<String>(str);
						}

						@Override
						protected WebMarkupContainer getObjectMenu() {
							return SitePage.this.getObjectMenu(getModel());
						}
					};
					return panel;
				}
			};
			this.exhibitionsContainer.add(panel);

			panel.setListPanelMode(ListPanelMode.TITLE);
			panel.setLiveSearch(false);
			panel.setHasExpander(true);
			panel.setSettings(true);
		}

		{
			ListPanel<ArtExhibition> panel = new ListPanel<>("exhibitionsTemporaryComing", getArtExhibitionsTemporaryComing()) {
				private static final long serialVersionUID = 1L;

				protected List<IModel<ArtExhibition>> filter(List<IModel<ArtExhibition>> initialList, String filter) {
					List<IModel<ArtExhibition>> list = new ArrayList<IModel<ArtExhibition>>();
					final String str = filter.trim().toLowerCase();
					initialList.forEach(s -> {
						if (s.getObject().getDisplayname().toLowerCase().contains(str)) {
							list.add(s);
						}
					});
					return list;
				}

				@Override
				protected Panel getListItemExpandedPanel(IModel<ArtExhibition> model, ListPanelMode mode) {
					logger.debug("expand");
					return new ObjectListItemExpandedPanel<ArtExhibition>("expanded-panel", model, mode) {

						private static final long serialVersionUID = 1L;

						@Override
						protected IModel<String> getObjectSubtitle() {
							if (getMode() == ListPanelMode.TITLE)
								return null;
							return Model.of(getModel().getObject().getSubtitle());
						}

						@Override
						protected String getImageSrc() {
							if (getModel().getObject().getPhoto() != null) {
								Resource photo = getResource(model.getObject().getPhoto().getId()).get();
								return getPresignedThumbnailSmall(photo);
							}
							return null;
						}

						@Override
						public void onClick() {
							setResponsePage(new ArtExhibitionPage(getModel(), getWorkingItems()));
						}

						protected IModel<String> getInfo() {
							String str = TextCleaner.clean(getModel().getObject().getIntro());
							return new Model<String>(str);
						}

					};
				}

				@Override
				protected Panel getListItemPanel(IModel<ArtExhibition> model) {
					DelleMuseObjectListItemPanel<ArtExhibition> panel = new DelleMuseObjectListItemPanel<ArtExhibition>("row-element", model, getListPanelMode()) {
						private static final long serialVersionUID = 1L;

						@Override
						protected String getImageSrc() {
							if (getModel().getObject().getPhoto() != null) {
								Resource photo = getResource(model.getObject().getPhoto().getId()).get();
								return getPresignedThumbnailSmall(photo);
							}
							return null;
						}

						@Override
						protected String getTitleIcon() {
							return SitePage.this.getObjectTitleIcon(getModel());
						}

						@Override
						public void onClick() {
							setResponsePage(new ArtExhibitionPage(getModel(), getWorkingItems()));
						}

						protected IModel<String> getInfo() {
							String str = TextCleaner.clean(getModel().getObject().getIntro());
							return new Model<String>(str);
						}

						@Override
						protected WebMarkupContainer getObjectMenu() {
							return SitePage.this.getObjectMenu(getModel());
						}

					};
					return panel;
				}
			};
			this.exhibitionsContainer.add(panel);

			panel.setListPanelMode(ListPanelMode.TITLE);
			panel.setLiveSearch(false);
			panel.setHasExpander(true);
			panel.setSettings(true);
		}

	}

	protected WebMarkupContainer getObjectMenu(IModel<ArtExhibition> model) {

		NavDropDownMenu<ArtExhibition> menu = new NavDropDownMenu<ArtExhibition>("menu", model, null) {
			private static final long serialVersionUID = 1L;

			public boolean isVisible() {
				return true;
			}
		};

		menu.setOutputMarkupId(true);

		menu.setLabelCss("d-block-inline d-sm-block-inline d-md-block-inline d-lg-none d-xl-none d-xxl-none ps-1 pe-1");
		menu.setIconCss("fa-solid fa-ellipsis d-block-inline d-sm-block-inline d-md-block-inline d-lg-block-inline d-xl-block-inline d-xxl-block-inline ps-1 pe-1");

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibition> getItem(String id) {

				return new LinkMenuItem<ArtExhibition>(id) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage(new ArtExhibitionPage(getModel()));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("open");
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
							return getLabel("artexhibitionguides");
						}
					};
				}
			});

			for (ArtExhibitionGuide g : getArtExhibitionDBService().getArtExhibitionGuides(model.getObject())) {

				final String agname = TextCleaner.truncate(g.getDisplayname(), 24);
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

		return menu;

	}

	protected String getObjectTitleIcon(IModel<ArtExhibition> model) {
		if (getArtExhibitionDBService().isArtExhibitionGuides(model.getObject())) {
			return ServerAppConstant.headphoneIcon;
		}
		return null;
	}

	protected void onCreateExhibition() {
		ArtExhibition ae = createExhibition(getSiteModel().getObject());
		setResponsePage(new ArtExhibitionPage(new ObjectModel<ArtExhibition>(ae)));
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (siteModel != null)
			siteModel.detach();

		if (listPermanent != null)
			listPermanent.forEach(i -> i.detach());

		if (listTemporary != null)
			listTemporary.forEach(i -> i.detach());

		if (listTemporaryPast != null)
			listTemporaryPast.forEach(i -> i.detach());

		if (listTemporaryComing != null)
			listTemporaryComing.forEach(i -> i.detach());

		if (this.siteList != null)
			this.siteList.forEach(e -> e.detach());
	}

	public IModel<Site> getSiteModel() {
		return siteModel;
	}

	public void setSiteModel(IModel<Site> siteModel) {
		this.siteModel = siteModel;
	}

	public List<IModel<Site>> getSiteList() {
		return siteList;
	}

	public void setSiteList(List<IModel<Site>> siteList) {
		this.siteList = siteList;
	}

	private void loadLists() {
		try {

			SiteDBService db = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);

			listPermanent = new ArrayList<IModel<ArtExhibition>>();
			listTemporary = new ArrayList<IModel<ArtExhibition>>();
			listTemporaryPast = new ArrayList<IModel<ArtExhibition>>();
			listTemporaryComing = new ArrayList<IModel<ArtExhibition>>();

			Iterable<ArtExhibition> la = db.getArtExhibitions(getSiteModel().getObject(), ObjectState.EDITION, ObjectState.PUBLISHED);

			for (ArtExhibition a : la) {
				if (a.isPermanent())
					listPermanent.add(new ObjectModel<ArtExhibition>(a));
				else {
					if (a.isOpen())
						listTemporary.add(new ObjectModel<ArtExhibition>(a));
					else if (a.isTerminated())
						listTemporaryPast.add(new ObjectModel<ArtExhibition>(a));
					else if (a.isComing())
						listTemporaryComing.add(new ObjectModel<ArtExhibition>(a));
				}
			}

			listsLoaded = true;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	private List<IModel<ArtExhibition>> getArtExhibitionsPermanent() {
		if (!listsLoaded) {
			loadLists();
		}
		return listPermanent;
	}

	private List<IModel<ArtExhibition>> getArtExhibitionsTemporary() {
		if (!listsLoaded) {
			loadLists();
		}
		return listTemporary;
	}

	private List<IModel<ArtExhibition>> getArtExhibitionsTemporaryPast() {
		if (!listsLoaded) {
			loadLists();
		}
		return listTemporaryPast;
	}

	private List<IModel<ArtExhibition>> getArtExhibitionsTemporaryComing() {
		if (!listsLoaded) {
			loadLists();
		}
		return listTemporaryComing;
	}

	private void setCurrent() {

		if (this.siteList == null)
			return;

		if (getSiteModel() == null)
			return;

		int n = 0;
		for (IModel<Site> m : this.siteList) {
			if (getSiteModel().getObject().getId().equals(m.getObject().getId())) {
				current = n;
				break;
			}
			n++;
		}
	}

	private void setUpModel() {

		try {
			if (getSiteModel() == null) {
				if (stringValue != null) {
					Optional<Site> o_site = findByIdWithDeps(Long.valueOf(stringValue.toLong()));
					if (o_site.isPresent()) {
						setSiteModel(new ObjectModel<Site>(o_site.get()));
					}
				}
			} else {
				if (!getSiteModel().getObject().isDependencies()) {
					Optional<Site> o_site = findByIdWithDeps(Long.valueOf(getSiteModel().getObject().getId()));
					if (o_site.isPresent()) {
						setSiteModel(new ObjectModel<Site>(o_site.get()));
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	private void addToolbar() {

		WebMarkupContainer toolbarContainer = new WebMarkupContainer("toolbarContainer");
		add(toolbarContainer);

		Toolbar toolbarItems = new Toolbar("toolbarItems");

		List<ToolbarItem> list = new ArrayList<ToolbarItem>();
		list.add(new SiteNavDropDownMenuToolbarItem("item", getSiteModel(), Align.TOP_RIGHT));

		list.forEach(t -> toolbarItems.addItem(t));
		toolbarContainer.add(toolbarItems);
	}

	private void addHeader() {

		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(new HREFBCElement("/site/list", getLabel("sites")));
		bc.addElement(new BCElement(new Model<String>(getSiteModel().getObject().getDisplayname())));
		JumboPageHeaderPanel<Site> ph = new JumboPageHeaderPanel<Site>("page-header", getSiteModel(), new Model<String>(getSiteModel().getObject().getDisplayname()));
		ph.setBreadCrumb(bc);

		ph.setContext(getLabel("site"));

		if (getSiteModel().getObject().getSubtitle() != null)
			ph.setTagline(Model.of(getSiteModel().getObject().getSubtitle()));

		if (getSiteModel().getObject().getPhoto() != null)
			ph.setPhotoModel(new ObjectModel<Resource>(getSiteModel().getObject().getPhoto()));
		else {
			ph.setIcon(Site.getIcon());
		}

		if (getSiteList() != null && getSiteList().size() > 0) {
			Navigator<Site> nav = new Navigator<Site>("navigator", getCurrent(), getSiteList()) {
				private static final long serialVersionUID = 1L;

				@Override
				public void navigate(int current) {
					setResponsePage(new SitePage(getList().get(current), getList()));
				}
			};
			bc.setNavigator(nav);
		}
		add(ph);
	}

	private void addSecurityPanel() {

		WebMarkupContainer s = new WebMarkupContainer("securityContainer");
		add(s);
		
		Link<Site> u = new Link<Site>("users", getSiteModel()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new SiteUsersPage(getSiteModel()));
			}
		};
		s.add(u);


		Link<Site> r = new Link<Site>("roles", getSiteModel()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new SiteRolesPage(getSiteModel()));
			}
		};

		
		
		s.add(r);
	}

	private void addSiteInfo() {

		WebMarkupContainer s = new WebMarkupContainer("siteInfoContainer");
		add(s);

		linkInfo = new Link<Site>("info", getSiteModel()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new SiteInfoPage(getSiteModel()));
			}
		};

		s.add(linkInfo);

		linkInst = new Link<Site>("institution", getSiteModel()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new InstitutionPage(getInstitutionModel()));

			}
		};
		s.add(linkInst);

		linkFloors = new Link<Site>("floors", getSiteModel()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new SiteFloorsPage(getSiteModel()));
			}
		};
		s.add(linkFloors);

	}

	private void addCatalog() {

		this.linksContainer = new WebMarkupContainer("linksContainer");
		add(this.linksContainer);

		linkArtWork = new Link<Site>("artwork", getSiteModel()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new SiteArtWorkListPage(getSiteModel()));
			}
		};
		this.linksContainer.add(linkArtWork);

		linkArtists = new Link<Site>("artists", getSiteModel()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new SiteArtistsListPage(getSiteModel()));
			}
		};
		this.linksContainer.add(linkArtists);

		/**
		 * linkContents = new Link<Site>("contents", getSiteModel()) { private static
		 * final long serialVersionUID = 1L;
		 * 
		 * @Override public void onClick() { setResponsePage(new
		 *           SiteGuideContentsListPage(getSiteModel())); } }; add(linkContents);
		 **/

		linkExhibitions = new Link<Site>("exhibitions", getSiteModel()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new SiteArtExhibitionsListPage(getSiteModel()));
			}
		};
		this.linksContainer.add(linkExhibitions);
	}

}
