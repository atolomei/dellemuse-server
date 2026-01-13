package dellemuse.serverapp.branded;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.markup.html.WebMarkupContainer;

import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.audit.panel.AuditPanel;
import dellemuse.serverapp.branded.panel.BrandedGlobalTopPanel;
import dellemuse.serverapp.branded.panel.BrandedSiteSearcherPanel;
import dellemuse.serverapp.editor.SimpleAlertRow;
import dellemuse.serverapp.global.GlobalFooterPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.global.JumboPageHeaderPanel;

import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.page.BrandedBasePage;
import dellemuse.serverapp.page.DelleMuseObjectListItemPanel;
import dellemuse.serverapp.page.ObjectListItemExpandedPanel;
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.model.ObjectModel;

import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;

import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.language.LanguageObjectService;
import io.odilon.util.Check;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.menu.SeparatorMenuItem;
import io.wktui.nav.menu.TitleMenuItem;

import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;

import wktui.base.InvisiblePanel;

/**
 * 
 * site foto Info - exhibitions
 * 
 */

@MountPath("/ag/${id}")
public class BrandedSitePage extends BasePage {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(BrandedSitePage.class.getName());

	private StringValue stringValue;
	private IModel<Site> siteModel;

	private List<IModel<ArtExhibition>> listPermanent;
	private List<IModel<ArtExhibition>> listTemporary;
	private List<IModel<ArtExhibition>> listTemporaryPast;
	private List<IModel<ArtExhibition>> listTemporaryComing;
	private Panel searcher;

	private boolean listsLoaded = false;

	private WebMarkupContainer exhibitionsContainer;

	
	@Override
	protected boolean isDarkTheme() {
		return true;
	}
	
	@Override
	public boolean hasAccessRight(Optional<User> ouser) {
		if (getSiteModel().getObject().getState() == ObjectState.DELETED)
			return false;
		return true;
	}

	public BrandedSitePage() {
		super();
	}

	public BrandedSitePage(PageParameters parameters) {
		super(parameters);
		stringValue = getPageParameters().get("id");
	}

	public BrandedSitePage(IModel<Site> model) {
		Check.requireNonNullArgument(model, "model is null");
		Check.requireTrue(model.getObject() != null, "modelOjbect is null");
		setSiteModel(model);
		getPageParameters().add("id", model.getObject().getId().toString());
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		getPage().add( new org.apache.wicket.AttributeModifier("class", "branded branded  text-bg-dark sssss"));
		
		try {
			setUpModel();
		} catch (Exception e) {
			logger.error(e);
			addErrorPanels(e);
			return;
		}

		addHeader();
		addSearch();

		add(new BrandedGlobalTopPanel("top-panel", getSiteModel()));
		//add(new GlobalFooterPanel<>("footer-panel"));
		add(new InvisiblePanel("footer-panel"));

		
		add(new InvisiblePanel("error"));

		if (!hasAccessRight(getSessionUser())) {
			SimpleAlertRow<Void> r = new SimpleAlertRow<Void>("error");
			r.setText(getLabel("not-authorized"));
			add(r);
			addOrReplace(new InvisiblePanel("exhibitionsContainer"));
			addOrReplace(new InvisiblePanel("searchContainer"));
			return;
		}

		addExhibitions();
	}

	
	protected IModel<String> getObjectTitle(ArtExhibitionGuide o) {
		StringBuilder str = new StringBuilder();
		str.append(getLanguageObjectService().getObjectDisplayName(o, getLocale()));
		if (o.getState() == ObjectState.DELETED)
			return new Model<String>(str.toString() + ServerConstant.DELETED_ICON);
		return Model.of(str.toString());
	}

	protected IModel<String> getObjectTitle(ArtExhibition o) {
		StringBuilder str = new StringBuilder();
		str.append(getLanguageObjectService().getObjectDisplayName(o, getLocale()));
		if (o.getState() == ObjectState.DELETED)
			return new Model<String>(str.toString() + ServerConstant.DELETED_ICON);
		return Model.of(str.toString());
	}

	
	
	protected IModel<String> getObjectSubtitle(ArtExhibition o) {
		return Model.of(TextCleaner.clean(getLanguageObjectService().getObjectSubtitle(o, getLocale()), ServerConstant.INFO_MAX));
	}

	protected IModel<String> getObjectInfo(ArtExhibition o) {
		return Model.of(TextCleaner.clean(getLanguageObjectService().getInfo(o, getLocale()), ServerConstant.INFO_MAX));
	}

	protected IModel<String> getObjectIntro(ArtExhibition o) {
		return Model.of(TextCleaner.clean(getLanguageObjectService().getIntro(o, getLocale()), ServerConstant.INTRO_MAX));
	}


	
	protected Optional<ArtExhibitionGuide> getArtExhibitionGuide(IModel<ArtExhibition> model) {
		for (ArtExhibitionGuide g : getArtExhibitionDBService().getArtExhibitionGuides(model.getObject())) {
			if (g.isOfficial())
				return Optional.of(g);
		}
		return Optional.empty();
	}

	protected void addSearch() {
		this.searcher = new BrandedSiteSearcherPanel("search", getSiteModel());
		addOrReplace(this.searcher);

	}

	protected IModel<Institution> getInstitutionModel() {
		return new ObjectModel<Institution>(getInstitution(getSiteModel().getObject().getInstitution().getId()).get());
	}

	protected void addListeners() {
		super.addListeners();
	}

	protected void addExhibitions() {

		this.exhibitionsContainer = new WebMarkupContainer("exhibitionsContainer");
		add(this.exhibitionsContainer);

		{
			ListPanel<ArtExhibition> panel = new ListPanel<>("exhibitionsPermanent", getArtExhibitionsPermanent()) {
				
		 		private static final long serialVersionUID = 1L;

		 		@Override
				public IModel<String> getItemLabel(IModel<ArtExhibition> model) {
					return getObjectTitle(model.getObject());
				}
		 		
				protected List<IModel<ArtExhibition>> filter(List<IModel<ArtExhibition>> initialList, String filter) {
					List<IModel<ArtExhibition>> list = new ArrayList<IModel<ArtExhibition>>();
					final String str = filter.trim().toLowerCase();
					initialList.forEach(s -> {
						if (getObjectTitle(s.getObject()).getObject().toLowerCase().contains(str)) {
							list.add(s);
						}
					});
					return list;
				}
				
				
				@Override
				protected boolean isToolbar() {
					return false;
				}
				

				@Override
				protected Panel getListItemExpandedPanel(IModel<ArtExhibition> model, ListPanelMode mode) {
					return new ObjectListItemExpandedPanel<ArtExhibition>("expanded-panel", model, mode) {
						private static final long serialVersionUID = 1L;

						@Override
						protected IModel<String> getObjectSubtitle() {
							if (getMode() == ListPanelMode.TITLE)
								return null;
							return BrandedSitePage.this.getObjectSubtitle(getModel().getObject());
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
							Optional<ArtExhibitionGuide> g = getArtExhibitionGuide(getModel());
							if (g.isPresent())
								setResponsePage(new BrandedArtExhibitionGuidePage(new ObjectModel<ArtExhibitionGuide>(g.get())));
							else
								setResponsePage(new ErrorPage(Model.of("not found")));
													
						}

						protected IModel<String> getInfo() {
							return BrandedSitePage.this.getObjectInfo(getModel().getObject());
						}

						
					};
				}

				@Override
				protected Panel getListItemPanel(IModel<ArtExhibition> model) {
					DelleMuseObjectListItemPanel<ArtExhibition> panel = new DelleMuseObjectListItemPanel<ArtExhibition>("row-element", model, getListPanelMode()) {
						private static final long serialVersionUID = 1L;

						

					 
						@Override
						protected IModel<String> getObjectTitle() {
							return BrandedSitePage.this.getObjectTitle(getModel().getObject());
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
							Optional<ArtExhibitionGuide> g = getArtExhibitionGuide(getModel());
							if (g.isPresent())
								setResponsePage(new BrandedArtExhibitionGuidePage(new ObjectModel<ArtExhibitionGuide>(g.get())));
							else
								setResponsePage(new ErrorPage(Model.of("not found")));
						}

						@Override
						protected String getTitleIcon() {
							return BrandedSitePage.this.getObjectTitleIcon(getModel());
						}

						protected IModel<String> getInfo() {
							return BrandedSitePage.this.getObjectInfo(getModel().getObject());

						}

						@Override
						protected WebMarkupContainer getObjectMenu() {
							return null;
							//return BrandedSitePage.this.getObjectMenu(getModel());
						}
					

					};
					return panel;
				}
			};

			panel.setHasExpander(false);
			this.exhibitionsContainer.add(panel);
			panel.setListPanelMode(ListPanelMode.TITLE_TEXT_IMAGE);
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
						if (getObjectTitle(s.getObject()).getObject().toLowerCase().contains(str)) {
							list.add(s);
						}
					});
					return list;
				}


				
				@Override
				protected boolean isToolbar() {
					return false;
				}
				
		 		@Override
				public IModel<String> getItemLabel(IModel<ArtExhibition> model) {
					return getObjectTitle(model.getObject());
				}
		 		
		 		
				@Override
				protected Panel getListItemExpandedPanel(IModel<ArtExhibition> model, ListPanelMode mode) {
					return new ObjectListItemExpandedPanel<ArtExhibition>("expanded-panel", model, mode) {
						private static final long serialVersionUID = 1L;

						@Override
						protected IModel<String> getObjectSubtitle() {
							if (getMode() == ListPanelMode.TITLE)
								return null;
							return BrandedSitePage.this.getObjectSubtitle(getModel().getObject());

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
							Optional<ArtExhibitionGuide> g = getArtExhibitionGuide(getModel());
							if (g.isPresent())
								setResponsePage(new BrandedArtExhibitionGuidePage(new ObjectModel<ArtExhibitionGuide>(g.get())));
						}

						protected IModel<String> getInfo() {
							return BrandedSitePage.this.getObjectInfo(getModel().getObject());
						}

					};
				}

				@Override
				protected Panel getListItemPanel(IModel<ArtExhibition> model) {
					DelleMuseObjectListItemPanel<ArtExhibition> panel = new DelleMuseObjectListItemPanel<ArtExhibition>("row-element", model, getListPanelMode()) {
						private static final long serialVersionUID = 1L;

						@Override
						protected IModel<String> getObjectTitle() {
							return BrandedSitePage.this.getObjectTitle(getModel().getObject());
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
							Optional<ArtExhibitionGuide> g = getArtExhibitionGuide(getModel());
							if (g.isPresent())
								setResponsePage(new BrandedArtExhibitionGuidePage(new ObjectModel<ArtExhibitionGuide>(g.get())));
						}

						@Override
						protected IModel<String> getInfo() {
							return BrandedSitePage.this.getObjectInfo(getModel().getObject());

						}

						@Override
						protected String getTitleIcon() {
							return BrandedSitePage.this.getObjectTitleIcon(getModel());
						}

						@Override
						protected WebMarkupContainer getObjectMenu() {
							return null;

							//return BrandedSitePage.this.getObjectMenu(getModel());
						}

					};
					return panel;
				}
			};
			this.exhibitionsContainer.add(panel);

			panel.setListPanelMode(ListPanelMode.TITLE_TEXT_IMAGE);
			panel.setLiveSearch(false);
			panel.setHasExpander(false);
			panel.setSettings(true);
		}

		{
			ListPanel<ArtExhibition> panel = new ListPanel<>("exhibitionsTemporaryPast", getArtExhibitionsTemporaryPast()) {
				private static final long serialVersionUID = 1L;

				protected List<IModel<ArtExhibition>> filter(List<IModel<ArtExhibition>> initialList, String filter) {
					List<IModel<ArtExhibition>> list = new ArrayList<IModel<ArtExhibition>>();
					final String str = filter.trim().toLowerCase();
					initialList.forEach(s -> {
						if (getObjectTitle(s.getObject()).getObject().toLowerCase().contains(str)) {
							list.add(s);
						}
					});
					return list;
				}
				
				
				@Override
				protected boolean isToolbar() {
					return false;
				}
				

				@Override
				public IModel<String> getItemLabel(IModel<ArtExhibition> model) {
					return getObjectTitle(model.getObject());
				}
				
				@Override
				protected Panel getListItemExpandedPanel(IModel<ArtExhibition> model, ListPanelMode mode) {

					return new ObjectListItemExpandedPanel<ArtExhibition>("expanded-panel", model, mode) {

						private static final long serialVersionUID = 1L;

						@Override
						protected IModel<String> getObjectSubtitle() {
							if (getMode() == ListPanelMode.TITLE)
								return null;
							return BrandedSitePage.this.getObjectSubtitle(getModel().getObject());
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
							Optional<ArtExhibitionGuide> g = getArtExhibitionGuide(getModel());
							if (g.isPresent())
								setResponsePage(new BrandedArtExhibitionGuidePage(new ObjectModel<ArtExhibitionGuide>(g.get())));
						}

						protected IModel<String> getInfo() {
							return BrandedSitePage.this.getObjectInfo(getModel().getObject());

						}

					};
				}

				@Override
				protected Panel getListItemPanel(IModel<ArtExhibition> model) {
					DelleMuseObjectListItemPanel<ArtExhibition> panel = new DelleMuseObjectListItemPanel<ArtExhibition>("row-element", model, getListPanelMode()) {
						private static final long serialVersionUID = 1L;

						@Override
						protected IModel<String> getObjectTitle() {
							return BrandedSitePage.this.getObjectTitle(getModel().getObject());
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
							Optional<ArtExhibitionGuide> g = getArtExhibitionGuide(getModel());
							if (g.isPresent())
								setResponsePage(new BrandedArtExhibitionGuidePage(new ObjectModel<ArtExhibitionGuide>(g.get())));
						}

						@Override
						protected String getTitleIcon() {
							return BrandedSitePage.this.getObjectTitleIcon(getModel());
						}

						protected IModel<String> getInfo() {
							return BrandedSitePage.this.getObjectInfo(getModel().getObject());

						}

						@Override
						protected WebMarkupContainer getObjectMenu() {
							return null;
							//return BrandedSitePage.this.getObjectMenu(getModel());
						}
					};
					return panel;
				}
			};
			this.exhibitionsContainer.add(panel);

			panel.setListPanelMode(ListPanelMode.TITLE_TEXT_IMAGE);
			panel.setLiveSearch(false);
			panel.setHasExpander(false);
			panel.setSettings(true);
		}

		{
			ListPanel<ArtExhibition> panel = new ListPanel<>("exhibitionsTemporaryComing", getArtExhibitionsTemporaryComing()) {
				private static final long serialVersionUID = 1L;

				protected List<IModel<ArtExhibition>> filter(List<IModel<ArtExhibition>> initialList, String filter) {
					List<IModel<ArtExhibition>> list = new ArrayList<IModel<ArtExhibition>>();
					final String str = filter.trim().toLowerCase();
					initialList.forEach(s -> {
						if (getObjectTitle(s.getObject()).getObject().toLowerCase().contains(str)) {
							list.add(s);
						}
					});
					return list;
				}
				
				
				@Override
				protected boolean isToolbar() {
					return false;
				}
				

				@Override
				public IModel<String> getItemLabel(IModel<ArtExhibition> model) {
					return getObjectTitle(model.getObject());
				}
				
				@Override
				protected Panel getListItemExpandedPanel(IModel<ArtExhibition> model, ListPanelMode mode) {

					return new ObjectListItemExpandedPanel<ArtExhibition>("expanded-panel", model, mode) {

						private static final long serialVersionUID = 1L;

						@Override
						protected IModel<String> getObjectSubtitle() {
							if (getMode() == ListPanelMode.TITLE)
								return null;
							return BrandedSitePage.this.getObjectSubtitle(getModel().getObject());

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
							Optional<ArtExhibitionGuide> g = getArtExhibitionGuide(getModel());
							if (g.isPresent())
								setResponsePage(new BrandedArtExhibitionGuidePage(new ObjectModel<ArtExhibitionGuide>(g.get())));
						}

						protected IModel<String> getInfo() {
							return BrandedSitePage.this.getObjectInfo(getModel().getObject());

						}

					};
				}

				@Override
				protected Panel getListItemPanel(IModel<ArtExhibition> model) {
					DelleMuseObjectListItemPanel<ArtExhibition> panel = new DelleMuseObjectListItemPanel<ArtExhibition>("row-element", model, getListPanelMode()) {
						private static final long serialVersionUID = 1L;

						@Override
						protected IModel<String> getObjectTitle() {
							return BrandedSitePage.this.getObjectTitle(getModel().getObject());
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
						protected String getTitleIcon() {
							return BrandedSitePage.this.getObjectTitleIcon(getModel());
						}

						@Override
						public void onClick() {
							Optional<ArtExhibitionGuide> g = getArtExhibitionGuide(getModel());
							if (g.isPresent())
								setResponsePage(new BrandedArtExhibitionGuidePage(new ObjectModel<ArtExhibitionGuide>(g.get())));
							else
								setResponsePage(new ErrorPage(Model.of("not-found")));
									
						}

						protected IModel<String> getInfo() {
							return BrandedSitePage.this.getObjectInfo(getModel().getObject());

						}

						@Override
						protected WebMarkupContainer getObjectMenu() {
							return null;

							//return BrandedSitePage.this.getObjectMenu(getModel());
						}

					};
					return panel;
				}
			};
			this.exhibitionsContainer.add(panel);

			panel.setListPanelMode(ListPanelMode.TITLE_TEXT_IMAGE);
			panel.setLiveSearch(false);
			panel.setHasExpander(false);
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

		menu.setTitleCss("d-block-inline d-sm-block-inline d-md-block-inline d-lg-none d-xl-none d-xxl-none ps-1 pe-1");
		menu.setIconCss("fa-solid fa-ellipsis d-block-inline d-sm-block-inline d-md-block-inline d-lg-block-inline d-xl-block-inline d-xxl-block-inline ps-1 pe-1");

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibition> getItem(String id) {

				return new LinkMenuItem<ArtExhibition>(id) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						Optional<ArtExhibitionGuide> g = getArtExhibitionGuide(getModel());
						if (g.isPresent())
							setResponsePage(new BrandedArtExhibitionGuidePage(new ObjectModel<ArtExhibitionGuide>(g.get())));
						else
							setResponsePage(new ErrorPage(Model.of("No guide for -> " + getModel().getObject().getDisplayname())));
									
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
							return BrandedSitePage.this.getLabel("artexhibitionguides");
						}
					};
				}
			});

			for (ArtExhibitionGuide g : getArtExhibitionDBService().getArtExhibitionGuides(model.getObject())) {

				final String agname = TextCleaner.truncate(BrandedSitePage.this.getObjectTitle(g).getObject(), 24);
				final Long gid = g.getId();

				menu.addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {

					private static final long serialVersionUID = 1L;

					@Override
					public MenuItemPanel<ArtExhibition> getItem(String id) {

						return new LinkMenuItem<ArtExhibition>(id) {

							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
								setResponsePage(new RedirectPage("/ag/guide/" + gid));
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

	protected void addErrorPanels(Exception e) {

		if (getSessionUser().isPresent())
			addOrReplace(new GlobalTopPanel("top-panel", new ObjectModel<User>(getSessionUser().get())));
		else
			addOrReplace(new GlobalTopPanel("top-panel"));

		addOrReplace(new GlobalFooterPanel<>("footer-panel"));
		addOrReplace(new InvisiblePanel("exhibitionsContainer"));
		addOrReplace(new InvisiblePanel("searchContainer"));

		SimpleAlertRow<Void> r = new SimpleAlertRow<Void>("error", e);
		addOrReplace(r);
	}

	protected String getObjectTitleIcon(IModel<ArtExhibition> model) {
		if (getArtExhibitionDBService().isArtExhibitionGuides(model.getObject())) {
			return ServerAppConstant.headphoneIcon;
		}
		return null;
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

	}

	public IModel<Site> getSiteModel() {
		return siteModel;
	}

	public void setSiteModel(IModel<Site> siteModel) {
		this.siteModel = siteModel;
	}

	private void loadLists() {
		try {

			SiteDBService db = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);

			listPermanent = new ArrayList<IModel<ArtExhibition>>();
			listTemporary = new ArrayList<IModel<ArtExhibition>>();
			listTemporaryPast = new ArrayList<IModel<ArtExhibition>>();
			listTemporaryComing = new ArrayList<IModel<ArtExhibition>>();

			Iterable<ArtExhibition> la = null;
			
			if (getSiteModel().getObject().isSortAlphabetical())
				la = db.getArtExhibitions(getSiteModel().getObject().getId(),  ObjectState.PUBLISHED);
			else
				la = db.getArtExhibitionsByOrdinal(getSiteModel().getObject(),  ObjectState.PUBLISHED);

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

	private void addHeader() {

		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(new HREFBCElement("/ag/" + getSiteModel().getObject().getId().toString(), getLabel("audio-guides")));

		JumboPageHeaderPanel<Site> ph = new JumboPageHeaderPanel<Site>("page-header", getSiteModel(), getLabel("audio-guides"));
		ph.setImageLinkCss("jumbo-img jumbo-md mb-2 mb-lg-0  border-none bg-dark");
		ph.setHeaderCss("mb-2 mt-0 pt-0 pb-2 border-none");

		// ph.setIcon(GuideContent.getIcon());
		ph.setBreadCrumb(bc);

		// ph.setContext(getLabel("site"));
		// if (getSiteModel().getObject().getSubtitle() != null)
		// ph.setTagline(Model.of(getSiteModel().getObject().getSubtitle()));

		// if (getSiteModel().getObject().getPhoto() != null)
		// ph.setPhotoModel(new
		// ObjectModel<Resource>(getSiteModel().getObject().getPhoto()));
		// else {
		// ph.setIcon(Site.getIcon());
		// }
		add(ph);
	}

}
