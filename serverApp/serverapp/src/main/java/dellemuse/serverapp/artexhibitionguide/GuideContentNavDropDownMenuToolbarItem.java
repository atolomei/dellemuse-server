package dellemuse.serverapp.artexhibitionguide;

import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.InstitutionDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.language.LanguageService;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.toolbar.DropDownMenuToolbarItem;
 

public class GuideContentNavDropDownMenuToolbarItem extends DropDownMenuToolbarItem<ArtExhibitionGuide> {

	private static final long serialVersionUID = 1L;

	
	/**
	public GuideContentNavDropDownMenuToolbarItem(String id, IModel< ArtExhibitionGuide> model, Align align) {
		this(id, model, null, align);
		 
	}
	**/

	public GuideContentNavDropDownMenuToolbarItem(String id, IModel<ArtExhibitionGuide> model, IModel<String> title, Align align) {
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
							setResponsePage(new ArtExhibitionGuidePage(GuideContentNavDropDownMenuToolbarItem.this.getModel()));
					
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("guide-content-audio");
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
					public MenuItemPanel< ArtExhibitionGuide> getItem(String id) {
	
						return new AjaxLinkMenuItem<ArtExhibitionGuide>(id, getModel()) {
							private static final long serialVersionUID = 1L;
							@Override
							public void onClick(AjaxRequestTarget target)  {
								fire ( new MenuAjaxEvent(ServerAppConstant.artworkrecord_info+"-"+langCode, target));
							}
	
							@Override
							public IModel<String> getLabel() {
								return getLabel("guide-content-record", langCode);
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
				return new io.wktui.nav.menu.SeparatorMenuItem<ArtExhibitionGuide>(id);
			}
		});

		
	 
 
		
		
		
		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibitionGuide>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibitionGuide> getItem(String id) {

				return new LinkMenuItem<ArtExhibitionGuide>(id, getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick()  {
						setResponsePage(new ArtExhibitionGuidePage(GuideContentNavDropDownMenuToolbarItem.this.getModel()));					
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
