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
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.InstitutionDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.toolbar.DropDownMenuToolbarItem;
 

public class ArtExhibitionGuideNavDropDownMenuToolbarItem extends DropDownMenuToolbarItem<ArtExhibitionGuide> {

	private static final long serialVersionUID = 1L;

	
	
	public ArtExhibitionGuideNavDropDownMenuToolbarItem(String id, IModel< ArtExhibitionGuide> model, Align align) {
		this(id, model, null, align);
		
		if (getModel().getObject()!=null) {
			if (model.getObject().getName()!=null)
				setLabel( Model.of(model.getObject().getName()) );
		else
			setLabel(Model.of(model.getObject().getDisplayname()) );
		}		
	}

	public ArtExhibitionGuideNavDropDownMenuToolbarItem(String id, IModel<ArtExhibitionGuide> model, IModel<String> title, Align align) {
		super(id, model, title, align);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		
		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibitionGuide>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel< ArtExhibitionGuide> getItem(String id) {

				return new AjaxLinkMenuItem<ArtExhibitionGuide>(id, getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick(AjaxRequestTarget target)  {
						fire ( new io.wktui.event.SimpleAjaxWicketEvent(ServerAppConstant.guide_info, target));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("info");
					}
				};
			}
		});
		
		
 
		
		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibitionGuide>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibitionGuide> getItem(String id) {

				return new AjaxLinkMenuItem<ArtExhibitionGuide>(id, getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick(AjaxRequestTarget target)  {
						fire ( new io.wktui.event.SimpleAjaxWicketEvent(ServerAppConstant.guide_contents, target));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("artwork-audio-guides");
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
					 
					@Override
					public boolean isVisible() {
						return true;
					}
				};
			}
		});
		
		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibitionGuide>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibitionGuide> getItem(String id) {

				return new AjaxLinkMenuItem<ArtExhibitionGuide>(id, getModel()) {
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
