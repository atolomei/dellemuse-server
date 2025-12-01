package dellemuse.serverapp.artexhibitionitem;

import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.InstitutionDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.language.LanguageService;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.event.SimpleWicketEvent;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.TitleMenuItem;
import io.wktui.nav.toolbar.DropDownMenuToolbarItem;
 

public class ArtExhibitionItemNavDropDownMenuToolbarItem extends DropDownMenuToolbarItem<ArtExhibitionItem> {

	private static final long serialVersionUID = 1L;

	public ArtExhibitionItemNavDropDownMenuToolbarItem(String id, IModel< ArtExhibitionItem> model, Align align) {
		this(id, model, null, align);
		setTitle( getLabel("art-exhibition-item", model.getObject().getDisplayname()) );
	}
	
	public ArtExhibitionItemNavDropDownMenuToolbarItem(String id, IModel<ArtExhibitionItem> model, IModel<String> title, Align align) {
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
	
		 addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibitionItem>() {
				private static final long serialVersionUID = 1L;
				@Override
				public MenuItemPanel<ArtExhibitionItem> getItem(String id) {
					return new TitleMenuItem<ArtExhibitionItem>(id) {
						private static final long serialVersionUID = 1L;

						@Override
						public IModel<String> getLabel() {
							return getLabel("information");
						}
					};
				}
			});
		 
		 
		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibitionItem>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel< ArtExhibitionItem> getItem(String id) {

				return new  AjaxLinkMenuItem<ArtExhibitionItem>(id, getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick(AjaxRequestTarget target) { 
						fire ( new MenuAjaxEvent(ServerAppConstant.artexhibition_item_info, target));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("exhibitionitem-record", getModel().getObject().getMasterLanguage());
					}
				};
			}
		});
		
		
		
		
		for (Language la: getLanguageService().getLanguages()) {
			
			final String langCode = la.getLanguageCode();
			
			if (!langCode.equals(getModel().getObject().getMasterLanguage())) {
			
				addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibitionItem>() {
	
					private static final long serialVersionUID = 1L;
	
					@Override
					public MenuItemPanel<ArtExhibitionItem> getItem(String id) {
	
						return new AjaxLinkMenuItem<ArtExhibitionItem>(id, getModel()) {
							private static final long serialVersionUID = 1L;
							@Override
							public void onClick(AjaxRequestTarget target)  {
								fire ( new MenuAjaxEvent(ServerAppConstant.object_translation_record_info+"-"+langCode, target));
							}
							
							@Override
							public IModel<String> getLabel() {
								return getLabel("exhibitionitem-record", langCode);
							}
						};
					}
				});
			}
		}
		
		
 
		
		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibitionItem>() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibitionItem> getItem(String id) {
				 
				return new io.wktui.nav.menu.SeparatorMenuItem<ArtExhibitionItem>(id) {
					private static final long serialVersionUID = 1L;
	 			};
			}
		});
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibitionItem>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibitionItem> getItem(String id) {

				return new AjaxLinkMenuItem<ArtExhibitionItem>(id, getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick(AjaxRequestTarget target)  {
						fire ( new MenuAjaxEvent(ServerAppConstant.object_meta, target));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("metadata");
					}
				};
			}
		});

		
		
		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibitionItem>() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibitionItem> getItem(String id) {
				 
				return new io.wktui.nav.menu.SeparatorMenuItem<ArtExhibitionItem>(id) {
					private static final long serialVersionUID = 1L;
	 			};
			}
		});
		
		
		
		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibitionItem>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibitionItem> getItem(String id) {

				return new AjaxLinkMenuItem<ArtExhibitionItem>(id, getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick(AjaxRequestTarget target)  {
						fire ( new MenuAjaxEvent(ServerAppConstant.object_audit, target));
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
				
				addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibitionItem>() {

					private static final long serialVersionUID = 1L;

					@Override
					public MenuItemPanel<ArtExhibitionItem> getItem(String id) {

						return new AjaxLinkMenuItem<ArtExhibitionItem>(id, getModel()) {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick(AjaxRequestTarget target) {
								fire(new MenuAjaxEvent(ServerAppConstant.object_audit+"-"+a_langCode, target, a_langCode));
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
