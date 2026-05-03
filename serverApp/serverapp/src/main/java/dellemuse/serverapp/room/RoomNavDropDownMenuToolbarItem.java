package dellemuse.serverapp.room;

import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

import dellemuse.serverapp.editor.ObjectBaseNavDropDownMenuToolbarItem;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.Floor;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.Room;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.FloorDBService;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.SeparatorMenuItem;
import io.wktui.nav.menu.TitleMenuItem;

public class RoomNavDropDownMenuToolbarItem extends ObjectBaseNavDropDownMenuToolbarItem<Room> {

	private static final long serialVersionUID = 1L;

	private IModel<Site> siteModel;
	private IModel<Floor> floorModel;

	public RoomNavDropDownMenuToolbarItem(String id, IModel<Room> model, Align align) {
		this(id, model, null, align);
		if (getModel() != null && getModel().getObject() != null)
			setTitle(getObjectTitle(model.getObject()));
	}

	public RoomNavDropDownMenuToolbarItem(String id, IModel<Room> model, IModel<String> title, Align align) {
		super(id, model, title, align);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (siteModel != null)  siteModel.detach();
		if (floorModel != null) floorModel.detach();
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		setUpModel();

		// Title header
		addItem(new io.wktui.nav.menu.MenuItemFactory<Room>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Room> getItem(String id) {
				return new TitleMenuItem<Room>(id) {
					private static final long serialVersionUID = 1L;

					@Override
					public IModel<String> getLabel() {
						return getLabel("room-title");
					}
				};
			}
		});

		// Master language info
		addItem(new io.wktui.nav.menu.MenuItemFactory<Room>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Room> getItem(String id) {
				return new AjaxLinkMenuItem<Room>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						fire(new MenuAjaxEvent(ServerAppConstant.room_info, target));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("information-record", getModel().getObject().getMasterLanguage());
					}
				};
			}
		});

		// Translation items per non-master language
		if (getSiteModel() != null) {
			for (Language la : getSiteModel().getObject().getLanguages()) {
				final String langCode = la.getLanguageCode();
				if (!getModel().getObject().getMasterLanguage().equals(langCode)) {
					addItem(new io.wktui.nav.menu.MenuItemFactory<Room>() {
						private static final long serialVersionUID = 1L;

						@Override
						public MenuItemPanel<Room> getItem(String id) {
							return new AjaxLinkMenuItem<Room>(id, getModel()) {
								private static final long serialVersionUID = 1L;

								@Override
								public void onClick(AjaxRequestTarget target) {
									fire(new MenuAjaxEvent(ServerAppConstant.object_translation_record_info + "-" + langCode, target));
								}

								@Override
								public IModel<String> getLabel() {
									return getLabel("information-record", langCode);
								}
							};
						}
					});
				}
			}
		}

		 

		addAudit();
	}

	public IModel<Site> getSiteModel() { return siteModel; }
	public void setSiteModel(IModel<Site> siteModel) { this.siteModel = siteModel; }

	public IModel<Floor> getFloorModel() { return floorModel; }
	public void setFloorModel(IModel<Floor> floorModel) { this.floorModel = floorModel; }

	private void setUpModel() {
		Room room = getModel().getObject();
		if (room.getFloor() != null) {
			FloorDBService floorSvc = (FloorDBService) ServiceLocator.getInstance().getBean(FloorDBService.class);
			Optional<Floor> oFloor = floorSvc.findWithDeps(room.getFloor().getId());
			oFloor.ifPresent(f -> {
				setFloorModel(new ObjectModel<Floor>(f));
				if (f.getSite() != null) {
					SiteDBService siteSvc = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
					siteSvc.findWithDeps(f.getSite().getId()).ifPresent(s -> setSiteModel(new ObjectModel<Site>(s)));
				}
			});
		}
	}
}
