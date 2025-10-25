package dellemuse.serverapp.artexhibition;

import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Language;
 
import dellemuse.serverapp.serverdb.service.InstitutionDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.language.LanguageService;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.event.SimpleWicketEvent;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.toolbar.DropDownMenuToolbarItem;
 

public class ArtExhibitionAWNavDropDownMenuToolbarItem extends DropDownMenuToolbarItem<ArtExhibition> {

	private static final long serialVersionUID = 1L;

	public ArtExhibitionAWNavDropDownMenuToolbarItem(String id, IModel< ArtExhibition> model, Align align) {
		this(id, model, null, align);
		
		if (getModel()!=null && getModel().getObject()!=null) {
	
			if (model.getObject().getShortname()!=null)
				setLabel( getLabel("art-exhibition", model.getObject().getShortname()) );

			else if (model.getObject().getName()!=null)
				getLabel("art-exhibition",model.getObject().getName() );
		else
			setLabel( Model.of(model.getObject().getDisplayname()) );
		}		
	}
	
	public ArtExhibitionAWNavDropDownMenuToolbarItem(String id, IModel<ArtExhibition> model, IModel<String> title, Align align) {
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
			public MenuItemPanel< ArtExhibition> getItem(String id) {

				return new LinkMenuItem<ArtExhibition>(id, getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick()  {
						fire (new SimpleWicketEvent(ServerAppConstant.exhibition_info));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("exhibition-info");
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
	
						return new AjaxLinkMenuItem<ArtExhibition>(id, getModel()) {
							private static final long serialVersionUID = 1L;
							@Override
							public void onClick(AjaxRequestTarget target)  {
								fire ( new MenuAjaxEvent(ServerAppConstant.object_translation_record_info+"-"+langCode, target));
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
						fire (new SimpleWicketEvent(ServerAppConstant.exhibition_items));
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
						fire (new SimpleWicketEvent(ServerAppConstant.exhibition_guides));
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
						fire (new SimpleWicketEvent(ServerAppConstant.site_audit));
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
