package dellemuse.serverapp.institution;

 

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
 

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.language.LanguageService;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.TitleMenuItem;
import io.wktui.nav.toolbar.DropDownMenuToolbarItem;
 
public class InstitutionNavDropDownMenuToolbarItem extends DropDownMenuToolbarItem<Institution> {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	static private Logger logger = Logger.getLogger(InstitutionNavDropDownMenuToolbarItem.class.getName());
	
	
	
	protected LanguageService getLanguageService() {
		return (LanguageService) ServiceLocator.getInstance().getBean(LanguageService.class);
	}
	
	
	public InstitutionNavDropDownMenuToolbarItem(String id, IModel<Institution> model, IModel<String> title, Align align) {
		super(id, model, title, align);
	}		

	@Override
	public void onInitialize() {
		super.onInitialize();
		
		
		
		
		 addItem(new io.wktui.nav.menu.MenuItemFactory<Institution>() {
				private static final long serialVersionUID = 1L;
				@Override
				public MenuItemPanel<Institution> getItem(String id) {
					return new TitleMenuItem<Institution>(id) {
				
						private static final long serialVersionUID = 1L;
				
						@Override
						public IModel<String> getLabel() {
							return getLabel("institution-info");
						}
					};
				}
			});

		
		addItem(new io.wktui.nav.menu.MenuItemFactory<Institution>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Institution> getItem(String id) {

				return new AjaxLinkMenuItem<Institution>(id, getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick(AjaxRequestTarget target)  {
						fire ( new MenuAjaxEvent(ServerAppConstant.institution_info, target));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("institution-record", getModel().getObject().getMasterLanguage());
					}
				};
			}
		});
		
	 	
		for (Language la: getLanguageService().getLanguages()) {
			
			final String langCode = la.getLanguageCode();
			
			if (!langCode.equals(getModel().getObject().getMasterLanguage())) {
			
				addItem(new io.wktui.nav.menu.MenuItemFactory<Institution>() {
	
					private static final long serialVersionUID = 1L;
	
					@Override
					public MenuItemPanel<Institution> getItem(String id) {
	
						return new AjaxLinkMenuItem<Institution>(id, getModel()) {
							private static final long serialVersionUID = 1L;
							@Override
							public void onClick(AjaxRequestTarget target)  {
								fire ( new MenuAjaxEvent(ServerAppConstant.object_translation_record_info+"-"+langCode, target));
							}
	
							@Override
							public IModel<String> getLabel() {
								return getLabel("institution-record", langCode);
							}
						};
					}
				});
			}
		}

		
		addItem(new io.wktui.nav.menu.MenuItemFactory<Institution>() {
	
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Institution> getItem(String id) {
				 
				return new io.wktui.nav.menu.SeparatorMenuItem<Institution>(id) {
					private static final long serialVersionUID = 1L;
					 
					@Override
					public boolean isVisible() {
						return true;
					}
				};
			}
		});
		
		addItem(new io.wktui.nav.menu.MenuItemFactory<Institution>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Institution> getItem(String id) {

				return new AjaxLinkMenuItem<Institution>(id, getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick(AjaxRequestTarget target)  {
						fire ( new MenuAjaxEvent(ServerAppConstant.object_meta, target));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("institution-meta");
					}
				};
			}
		});
	   
		addItem(new io.wktui.nav.menu.MenuItemFactory<Institution>() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Institution> getItem(String id) {
				 
				return new io.wktui.nav.menu.SeparatorMenuItem<Institution>(id) {
					private static final long serialVersionUID = 1L;
					 
					@Override
					public boolean isVisible() {
						return true;
					}
				};
			}
		});
		
		addItem(new io.wktui.nav.menu.MenuItemFactory<Institution>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Institution> getItem(String id) {

				return new AjaxLinkMenuItem<Institution>(id, getModel()) {
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
	}
	
	 
	
}
