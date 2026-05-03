package dellemuse.serverapp.floor;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.icons.Icons;
import dellemuse.serverapp.page.DelleMuseObjectListItemPanel;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.Floor;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Room;
import dellemuse.serverapp.serverdb.model.User;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;

public class FloorRoomsPanel extends DBModelPanel<Floor> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(FloorRoomsPanel.class.getName());

	private List<IModel<Room>> roomList;
	private ListPanel<Room> roomsListPanel;

	public FloorRoomsPanel(String id, IModel<Floor> model) {
		super(id, model);
		setOutputMarkupId(true);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		loadRooms();
		addRoomsPanel();
		addRoomButton();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (this.roomList != null)
			this.roomList.forEach(m -> m.detach());
	}

	@Override
	public List<ToolbarItem> getToolbarItems() {
		return null;
	}

	// -----------------------------------------------------------------------

	protected synchronized void loadRooms() {
		this.roomList = new ArrayList<>();
		getRoomDBService().getRooms(getModel().getObject())
				.forEach(r -> roomList.add(new ObjectModel<Room>(r)));
	}

	protected List<IModel<Room>> getRooms() {
		if (this.roomList == null) loadRooms();
		return this.roomList;
	}

	protected IModel<String> getRoomTitle(IModel<Room> model) {
		StringBuilder sb = new StringBuilder();
		try {
			Room r = model.getObject();
			sb.append(r.getDisplayname());
			if (r.getRoomNumber() != null && !r.getRoomNumber().isEmpty())
				sb.append(" <span class=\"text-secondary\">(" + r.getRoomNumber() + ")</span>");
			if (r.getState() == ObjectState.DELETED)
				sb.append(Icons.DELETED_ICON_HTML);
			else if (r.getState() == ObjectState.EDITION)
				sb.append(Icons.EDITION_ICON_HTML);
		} catch (Exception e) {
			logger.error(e);
			sb.append(e.getClass().getSimpleName() + " " + e.getMessage());
		}
		return Model.of(sb.toString());
	}

	protected WebMarkupContainer getRoomMenu(IModel<Room> model) {

		NavDropDownMenu<Room> menu = new NavDropDownMenu<Room>("menu", model, null);
		menu.setOutputMarkupId(true);
		menu.setTitleCss("d-block-inline d-sm-block-inline d-md-block-inline d-lg-none d-xl-none d-xxl-none ps-1 pe-1");
		menu.setIconCss("fa-solid fa-ellipsis d-block-inline d-sm-block-inline d-md-block-inline d-lg-block-inline d-xl-block-inline d-xxl-block-inline ps-1 pe-1");

		// Open
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Room>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Room> getItem(String id) {
				return new AjaxLinkMenuItem<Room>(id, model) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						setResponsePage(new dellemuse.serverapp.room.SiteRoomPage(getModel()));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("open");
					}
				};
			}
		});

		// Publish
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Room>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Room> getItem(String id) {
				return new AjaxLinkMenuItem<Room>(id, model) {
					private static final long serialVersionUID = 1L;

					@Override
					public boolean isVisible() {
						return model.getObject().getState() != ObjectState.PUBLISHED;
					}

					@Override
					public void onClick(AjaxRequestTarget target) {
						model.getObject().setState(ObjectState.PUBLISHED);
						getRoomDBService().save(model.getObject(), getSessionUser().get());
						loadRooms();
						target.add(FloorRoomsPanel.this.roomsListPanel);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("publish");
					}
				};
			}
		});

		// Edition
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Room>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Room> getItem(String id) {
				return new AjaxLinkMenuItem<Room>(id, model) {
					private static final long serialVersionUID = 1L;

					@Override
					public boolean isVisible() {
						return model.getObject().getState() != ObjectState.EDITION;
					}

					@Override
					public void onClick(AjaxRequestTarget target) {
						model.getObject().setState(ObjectState.EDITION);
						getRoomDBService().save(model.getObject(), getSessionUser().get());
						loadRooms();
						target.add(FloorRoomsPanel.this.roomsListPanel);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("edition");
					}
				};
			}
		});

		// Delete
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Room>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Room> getItem(String id) {
				return new AjaxLinkMenuItem<Room>(id, model) {
					private static final long serialVersionUID = 1L;

					@Override
					public boolean isVisible() {
						return model.getObject().getState() != ObjectState.DELETED;
					}

					@Override
					public void onClick(AjaxRequestTarget target) {
						getRoomDBService().markAsDeleted(model.getObject(), getSessionUser().get());
						loadRooms();
						target.add(FloorRoomsPanel.this.roomsListPanel);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("delete");
					}
				};
			}
		});

		return menu;
	}

	private void addRoomsPanel() {
		this.roomsListPanel = new ListPanel<Room>("rooms-panel") {
			private static final long serialVersionUID = 1L;

			@Override
			public List<IModel<Room>> getItems() {
				return getRooms();
			}

			@Override
			public Integer getTotalItems() {
				return getRooms().size();
			}

			@Override
			protected Panel getListItemPanel(IModel<Room> model, ListPanelMode mode) {
				return new DelleMuseObjectListItemPanel<Room>("row-element", model, mode) {
					private static final long serialVersionUID = 1L;

					@Override
					protected WebMarkupContainer getObjectMenu() {
						return FloorRoomsPanel.this.getRoomMenu(getModel());
					}

					@Override
					public void onClick() {
						setResponsePage(new dellemuse.serverapp.room.SiteRoomPage(getModel()));
					}

					@Override
					protected IModel<String> getObjectTitle() {
						return FloorRoomsPanel.this.getRoomTitle(getModel());
					}

					@Override
					protected IModel<String> getInfo() {
						return null;
					}
				};
			}

			@Override
			public IModel<String> getItemLabel(IModel<Room> model) {
				return FloorRoomsPanel.this.getRoomTitle(model);
			}
		};

		this.roomsListPanel.setTitle(getLabel("rooms"));
		this.roomsListPanel.setListPanelMode(ListPanelMode.TITLE);
		this.roomsListPanel.setOutputMarkupId(true);
		add(this.roomsListPanel);
	}

	private void addRoomButton() {
		AjaxButtonToolbarItem<?> addRoomBtn = new AjaxButtonToolbarItem<>("add-room-btn", getLabel("add-room")) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onCick(AjaxRequestTarget target) {
				try {
					User user = getSessionUser().get();
					Room room = getRoomDBService().create("new room", FloorRoomsPanel.this.getModel().getObject(), user);
					FloorRoomsPanel.this.roomList.add(new ObjectModel<Room>(room));
					target.add(FloorRoomsPanel.this.roomsListPanel);
				} catch (Exception e) {
					logger.error(e);
				}
			}

			@Override
			public boolean isVisible() {
				return getSessionUser().isPresent();
			}
		};
		addRoomBtn.setOutputMarkupId(true);
		add(addRoomBtn);
	}
}
