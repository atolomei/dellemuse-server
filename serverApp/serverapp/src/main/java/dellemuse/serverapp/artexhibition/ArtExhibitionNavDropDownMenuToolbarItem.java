package dellemuse.serverapp.artexhibition;

import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.service.InstitutionDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.toolbar.DropDownMenuToolbarItem;
 

public class ArtExhibitionNavDropDownMenuToolbarItem extends DropDownMenuToolbarItem<ArtExhibition> {

	private static final long serialVersionUID = 1L;

	
	
	public ArtExhibitionNavDropDownMenuToolbarItem(String id, IModel< ArtExhibition> model, Align align) {
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
	
	public ArtExhibitionNavDropDownMenuToolbarItem(String id, IModel<ArtExhibition> model, IModel<String> title, Align align) {
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

				return new AjaxLinkMenuItem<ArtExhibition>(id, getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick(AjaxRequestTarget target)  {
						fire ( new io.wktui.event.SimpleAjaxWicketEvent(ServerAppConstant.site_info, target));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("info");
					}
				};
			}
		});
		
		
/**
		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibition> getItem(String id) {

				return new AjaxLinkMenuItem<ArtExhibition>(id, getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick(AjaxRequestTarget target)  {
						fire ( new io.wktui.event.SimpleAjaxWicketEvent(ServerAppConstant.exhibition_audio, target));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("audio");
					}
				};
			}
		});
**/ 		
		
		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibition> getItem(String id) {

				return new AjaxLinkMenuItem<ArtExhibition>(id, getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick(AjaxRequestTarget target)  {
						fire ( new io.wktui.event.SimpleAjaxWicketEvent(ServerAppConstant.exhibition_items, target));
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

				return new AjaxLinkMenuItem<ArtExhibition>(id, getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick(AjaxRequestTarget target)  {
						fire ( new io.wktui.event.SimpleAjaxWicketEvent(ServerAppConstant.exhibition_guides, target));
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
			public MenuItemPanel<ArtExhibition> getItem(String id) {

				return new AjaxLinkMenuItem<ArtExhibition>(id, getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick(AjaxRequestTarget target)  {
						fire ( new io.wktui.event.SimpleAjaxWicketEvent(ServerAppConstant.audit, target));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("audit");
					}
				};
			}
		});

 
		
		
	 
		
		/**
		 
		addItem(new io.wktui.nav.menu.MenuItemFactory<Site>() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Site> getItem(String id) {
				 
				return new io.wktui.nav.menu.SeparatorMenuItem<Site>(id) {
					private static final long serialVersionUID = 1L;
					 
					@Override
					public boolean isVisible() {
						return true;
					}
				};
			}
		});
 **/
		
	 
		
	 
	}
	

}
