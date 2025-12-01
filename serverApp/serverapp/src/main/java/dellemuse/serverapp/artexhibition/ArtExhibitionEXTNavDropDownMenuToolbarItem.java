package dellemuse.serverapp.artexhibition;

import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.InstitutionDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.language.LanguageService;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.event.SimpleWicketEvent;
import io.wktui.model.TextCleaner;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.TitleMenuItem;
import io.wktui.nav.toolbar.DropDownMenuToolbarItem;
 

public class ArtExhibitionEXTNavDropDownMenuToolbarItem extends DropDownMenuToolbarItem<ArtExhibition> {

	private static final long serialVersionUID = 1L;

	public ArtExhibitionEXTNavDropDownMenuToolbarItem(String id, IModel< ArtExhibition> model, Align align) {
		this(id, model, null, align);
		 
			if (model.getObject().getShortname()!=null)
				setTitle( getLabel("art-exhibition-dropdown", model.getObject().getShortname()) );
			else 
				setTitle( getLabel("art-exhibition-dropdown", TextCleaner.truncate(model.getObject().getName(),24) ));
		 		
	}
	
	public ArtExhibitionEXTNavDropDownMenuToolbarItem(String id, IModel<ArtExhibition> model, IModel<String> title, Align align) {
		super(id, model, title, align);
	}

	public Optional<Institution> getInstitution(Long id) {
		InstitutionDBService service = (InstitutionDBService) ServiceLocator.getInstance()
				.getBean(InstitutionDBService.class);
		return service.findById(id);
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
							return getLabel("exhibition-info");
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
					public void onClick()  {
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
		
		
		for (Language la: getLanguageService().getLanguages()) {
			
			final String langCode = la.getLanguageCode();
			
			if (!langCode.equals(getModel().getObject().getMasterLanguage())) {
			
				addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {
	
					private static final long serialVersionUID = 1L;
	
					@Override
					public MenuItemPanel<ArtExhibition> getItem(String id) {
	
						return new LinkMenuItem<ArtExhibition>(id, getModel()) {
							private static final long serialVersionUID = 1L;
							@Override
							public void onClick()  {
								ArtExhibitionPage page = new ArtExhibitionPage(getModel());
								page.setStartTab(ServerAppConstant.object_translation_record_info+"-"+langCode);
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
		public MenuItemPanel< ArtExhibition> getItem(String id) {

			return new LinkMenuItem<ArtExhibition>(id, getModel()) {
				private static final long serialVersionUID = 1L;
				@Override
				public void onClick()  {
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
			public MenuItemPanel< ArtExhibition> getItem(String id) {

				return new LinkMenuItem<ArtExhibition>(id, getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick()  {
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
			public MenuItemPanel< ArtExhibition> getItem(String id) {

				return new LinkMenuItem<ArtExhibition>(id, getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick()  {
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
			public MenuItemPanel< ArtExhibition> getItem(String id) {

				return new LinkMenuItem<ArtExhibition>(id, getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick()  {
						
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
			public MenuItemPanel< ArtExhibition> getItem(String id) {

				return new LinkMenuItem<ArtExhibition>(id, getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick()  {
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
		 
		for (Language la : getLanguageService().getLanguages()) {

			final String a_langCode = la.getLanguageCode();

			if (!getModel().getObject().getMasterLanguage().equals(a_langCode)) {
				
				addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {

					private static final long serialVersionUID = 1L;

					@Override
					public MenuItemPanel< ArtExhibition> getItem(String id) {

						return new  LinkMenuItem<ArtExhibition>(id, getModel()) {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick() {
								ArtExhibitionPage page = new ArtExhibitionPage(getModel());
								page.setStartTab(ServerAppConstant.object_audit+"-"+a_langCode);
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

}
