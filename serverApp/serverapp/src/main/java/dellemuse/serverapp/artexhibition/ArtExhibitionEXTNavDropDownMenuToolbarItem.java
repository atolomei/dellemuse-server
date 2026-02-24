package dellemuse.serverapp.artexhibition;

import java.util.Optional;

import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.serverapp.icons.Icons;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.ArtExhibitionDBService;
import dellemuse.serverapp.serverdb.service.InstitutionDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.language.LanguageObjectService;
import dellemuse.serverapp.service.language.LanguageService;

import io.wktui.model.TextCleaner;

import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.TitleMenuItem;
import io.wktui.nav.toolbar.DropDownMenuToolbarItem;

public class ArtExhibitionEXTNavDropDownMenuToolbarItem extends DropDownMenuToolbarItem<ArtExhibition> {

	private static final long serialVersionUID = 1L;

	IModel<Site> siteModel;
	
	 
	
	
	public ArtExhibitionEXTNavDropDownMenuToolbarItem(String id, IModel<ArtExhibition> model, IModel<Site> siteModel, Align align) {
		this(id, model, siteModel, null, align);

		if (model.getObject().getShortname() != null)
			setTitle(getLabel("art-exhibition-dropdown", model.getObject().getShortname()));
		else
			setTitle( getLabel("art-exhibition-dropdown", getObjectTitle(model.getObject()).getObject() ));
		
	}

	public ArtExhibitionEXTNavDropDownMenuToolbarItem(String id, IModel<ArtExhibition> model, IModel<Site> siteModel, IModel<String> title, Align align) {
		super(id, model, title, align);
		this.siteModel=siteModel;
	}
	
	public IModel<String> getObjectTitle(MultiLanguageObject o) {
		String s = getLanguageObjectService().getObjectDisplayName(o, getLocale());
		if (s == null)
			return null;
		return Model.of(TextCleaner.truncate(s,20));
	}

	public LanguageObjectService getLanguageObjectService() {
		return (LanguageObjectService) ServiceLocator.getInstance().getBean(LanguageObjectService.class);
	}
		
	public void onDetach() {
		super.onDetach();

		if (siteModel!=null)
			siteModel.detach();
	}
	
	public Optional<Institution> getInstitution(Long id) {
		return ((InstitutionDBService) ServiceLocator.getInstance().getBean(InstitutionDBService.class)).findById(id);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibition> getItem(String id) {
				return new TitleMenuItem<ArtExhibition>(id) {
					private static final long serialVersionUID = 1L;

					@Override
					public IModel<String> getLabel() {
						return getLabel("art-exhibition-title");
					}
				};
			}
		});

		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibition> getItem(String id) {

				return new LinkMenuItem<ArtExhibition>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						ArtExhibitionPage page = new ArtExhibitionPage(getModel());
						page.setStartTab(ServerAppConstant.exhibition_info);
						setResponsePage(page);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("exhibition-record", getModel().getObject().getMasterLanguage());
					}
				};
			}
		});

		for (Language la: getSiteModel().getObject().getLanguages()) {

			final String langCode = la.getLanguageCode();

			if (!langCode.equals(getModel().getObject().getMasterLanguage())) {

				addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {
					private static final long serialVersionUID = 1L;

					@Override
					public MenuItemPanel<ArtExhibition> getItem(String id) {

						return new LinkMenuItem<ArtExhibition>(id, getModel()) {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
								ArtExhibitionPage page = new ArtExhibitionPage(getModel());
								page.setStartTab(ServerAppConstant.object_translation_record_info + "-" + langCode);
								setResponsePage(page);
							}

							@Override
							public IModel<String> getLabel() {
								return getLabel("exhibition-record", langCode);
							}
						};
					}
				});
			}
		}

		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibition> getItem(String id) {
				return new io.wktui.nav.menu.SeparatorMenuItem<ArtExhibition>(id) {
					private static final long serialVersionUID = 1L;
				};
			}
		});

		

		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibition> getItem(String id) {
				return new TitleMenuItem<ArtExhibition>(id) {
					private static final long serialVersionUID = 1L;

					@Override
					public IModel<String> getLabel() {
						return getLabel("exhibition-guides");
					}
				};
			}
		});
		
		
		for (ArtExhibitionGuide g: getArtExhibitionDBService().getArtExhibitionGuides( getModel().getObject(), ObjectState.PUBLISHED, ObjectState.EDITION)) {
			
			final String agname = TextCleaner.truncate(getObjectTitle(g).getObject(), 24) +  (g.isAccessible()? Icons.ACCESIBLE_ICON_HTML : "");
			
			final String gid 	= g.getId().toString();
					
			addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {

				private static final long serialVersionUID = 1L;

				@Override
				public MenuItemPanel<ArtExhibition> getItem(String id) {

					return new LinkMenuItem<ArtExhibition>(id, getModel()) {
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick( ) {
							 setResponsePage (new RedirectPage("/guide/" + gid));
						}

						@Override
						public IModel<String> getLabel() {
							return Model.of( agname );
						}
					};
				}
			});
		}
		
	
		
		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibition> getItem(String id) {

				return new io.wktui.nav.menu.SeparatorMenuItem<ArtExhibition>(id) {
					private static final long serialVersionUID = 1L;

					@Override
					public boolean isVisible() {
						return true;
					}
				};
			}
		});
		
		
		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibition> getItem(String id) {

				return new LinkMenuItem<ArtExhibition>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						ArtExhibitionPage page = new ArtExhibitionPage(getModel());
						page.setStartTab(ServerAppConstant.exhibition_guides);
						setResponsePage(page);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("exhibition-guides");
					}
				};
			}
		});
		
		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibition> getItem(String id) {
				return new io.wktui.nav.menu.SeparatorMenuItem<ArtExhibition>(id) {
					private static final long serialVersionUID = 1L;
				};
			}
		});

		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibition> getItem(String id) {

				return new LinkMenuItem<ArtExhibition>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						ArtExhibitionPage page = new ArtExhibitionPage(getModel());
						page.setStartTab(ServerAppConstant.exhibition_sections);
						setResponsePage(page);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("sections");
					}
				};
			}
		});

		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibition> getItem(String id) {

				return new LinkMenuItem<ArtExhibition>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						ArtExhibitionPage page = new ArtExhibitionPage(getModel());
						page.setStartTab(ServerAppConstant.exhibition_items);
						setResponsePage(page);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("items");
					}
				};
			}
		});

	

		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibition> getItem(String id) {

				return new io.wktui.nav.menu.SeparatorMenuItem<ArtExhibition>(id) {
					private static final long serialVersionUID = 1L;

					@Override
					public boolean isVisible() {
						return true;
					}
				};
			}
		});

		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibition> getItem(String id) {

				return new LinkMenuItem<ArtExhibition>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						ArtExhibitionPage page = new ArtExhibitionPage(getModel());
						page.setStartTab(ServerAppConstant.object_meta);
						setResponsePage(page);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("metadata");
					}
				};
			}
		});

		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibition> getItem(String id) {

				return new io.wktui.nav.menu.SeparatorMenuItem<ArtExhibition>(id) {
					private static final long serialVersionUID = 1L;

					@Override
					public boolean isVisible() {
						return true;
					}
				};
			}
		});

		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibition> getItem(String id) {

				return new LinkMenuItem<ArtExhibition>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						ArtExhibitionPage page = new ArtExhibitionPage(getModel());
						page.setStartTab(ServerAppConstant.object_audit);
						setResponsePage(page);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("audit");
					}
				};
			}
		});

		for (Language la: getSiteModel().getObject().getLanguages()) {

			final String a_langCode = la.getLanguageCode();

			if (!getModel().getObject().getMasterLanguage().equals(a_langCode)) {

				addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {

					private static final long serialVersionUID = 1L;

					@Override
					public MenuItemPanel<ArtExhibition> getItem(String id) {

						return new LinkMenuItem<ArtExhibition>(id, getModel()) {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
								ArtExhibitionPage page = new ArtExhibitionPage(getModel());
								page.setStartTab(ServerAppConstant.object_audit + "-" + a_langCode);
								setResponsePage(page);
							}

							@Override
							public IModel<String> getLabel() {
								return getLabel("audit-lang", a_langCode);
							}
						};
					}
				});
			}
		}
	}

	protected LanguageService getLanguageService() {
		return (LanguageService) ServiceLocator.getInstance().getBean(LanguageService.class);
	}

	public IModel<Site> getSiteModel() {
		return siteModel;
	}

	public void setSiteModel(IModel<Site> siteModel) {
		this.siteModel = siteModel;
	}
	

	protected ArtExhibitionDBService getArtExhibitionDBService() {
		return (ArtExhibitionDBService) ServiceLocator.getInstance().getBean(ArtExhibitionDBService.class);
	}
	
}
