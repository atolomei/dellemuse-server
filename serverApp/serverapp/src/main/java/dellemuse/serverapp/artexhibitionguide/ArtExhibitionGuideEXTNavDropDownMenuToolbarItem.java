package dellemuse.serverapp.artexhibitionguide;

import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.serverapp.artexhibition.ArtExhibitionPage;
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.InstitutionDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.language.LanguageService;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.model.TextCleaner;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.toolbar.DropDownMenuToolbarItem;
 

public class ArtExhibitionGuideEXTNavDropDownMenuToolbarItem extends DropDownMenuToolbarItem<ArtExhibitionGuide> {

	private static final long serialVersionUID = 1L;

	
	
	public ArtExhibitionGuideEXTNavDropDownMenuToolbarItem(String id, IModel<ArtExhibitionGuide> model, Align align) {
		this(id, model, null, align);
		setTitle(getLabel("audio-guide-dropdown",TextCleaner.truncate(getModel().getObject().getName(), 24)));
	}

	public ArtExhibitionGuideEXTNavDropDownMenuToolbarItem(String id, IModel<ArtExhibitionGuide> model, IModel<String> title, Align align) {
		super(id, model, title, align);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		
		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibitionGuide>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel< ArtExhibitionGuide> getItem(String id) {

				return new LinkMenuItem<ArtExhibitionGuide>(id, getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick()  {
						ArtExhibitionGuidePage page = new ArtExhibitionGuidePage(getModel());
						page.setStartTab(ServerAppConstant.artexhibitionguide_info);
						setResponsePage(page);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("artexhibitionguide-info");
					}
				};
			}
		});
		
		
 
		
		for (Language la: getLanguageService().getLanguages()) {
			
			final String langCode = la.getLanguageCode();
			
			if (!langCode.equals(getModel().getObject().getMasterLanguage())) {
			
				addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibitionGuide>() {
	
					private static final long serialVersionUID = 1L;
	
					@Override
					public MenuItemPanel<ArtExhibitionGuide> getItem(String id) {
	
						return new  LinkMenuItem<ArtExhibitionGuide>(id, getModel()) {
							private static final long serialVersionUID = 1L;
							@Override
							public void onClick()  {
								 
								ArtExhibitionGuidePage page = new ArtExhibitionGuidePage(getModel());
								page.setStartTab(ServerAppConstant.object_translation_record_info+"-"+langCode);
								setResponsePage(page);
							
							}
	
							@Override
							public IModel<String> getLabel() {
								return getLabel("artexhibitionguide-record", langCode);
							}
						};
					}
				});
			}
		}
		 
	
	
	
	
	addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibitionGuide>() {
		
		private static final long serialVersionUID = 1L;

		@Override
		public MenuItemPanel<ArtExhibitionGuide> getItem(String id) {
			 
			return new io.wktui.nav.menu.SeparatorMenuItem<ArtExhibitionGuide>(id) {
				private static final long serialVersionUID = 1L;
			};
		}
	});
	
		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibitionGuide>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibitionGuide> getItem(String id) {

				return new  LinkMenuItem<ArtExhibitionGuide>(id, getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick( )  {
					 	 
						ArtExhibitionGuidePage page = new ArtExhibitionGuidePage(getModel());
						page.setStartTab(ServerAppConstant.artexhibitionguide_contents);
						setResponsePage(page);
					
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("artexhibitionguide-contents");
					}
				};
			}
		});
 
		
		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibitionGuide>() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibitionGuide> getItem(String id) {
				 
				return new io.wktui.nav.menu.SeparatorMenuItem<ArtExhibitionGuide>(id) {
					private static final long serialVersionUID = 1L;
				};
			}
		});
		
		
	
			 

		

		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibitionGuide>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibitionGuide> getItem(String id) {

				return new  LinkMenuItem<ArtExhibitionGuide>(id, getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick( )  {
						ArtExhibitionGuidePage page = new ArtExhibitionGuidePage(getModel());
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
		
		
		
		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibitionGuide>() {
				
				private static final long serialVersionUID = 1L;

				@Override
				public MenuItemPanel<ArtExhibitionGuide> getItem(String id) {
					 
					return new io.wktui.nav.menu.SeparatorMenuItem<ArtExhibitionGuide>(id) {
						private static final long serialVersionUID = 1L;
					};
				}
			});
			
		
		
		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibitionGuide>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibitionGuide> getItem(String id) {

				return new  LinkMenuItem<ArtExhibitionGuide>(id, getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick( )  {
						ArtExhibitionGuidePage page = new ArtExhibitionGuidePage(getModel());
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
	}

	protected LanguageService getLanguageService() {
		return (LanguageService) ServiceLocator.getInstance().getBean(LanguageService.class);
	}

}
