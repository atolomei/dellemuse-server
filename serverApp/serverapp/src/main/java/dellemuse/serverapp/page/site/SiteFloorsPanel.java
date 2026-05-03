package dellemuse.serverapp.page.site;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.floor.SiteFloorPage;
import dellemuse.serverapp.icons.Icons;
import dellemuse.serverapp.page.DelleMuseObjectListItemPanel;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.Floor;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Room;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.FloorDBService;
import dellemuse.serverapp.serverdb.service.RoomDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.Toolbar;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;
import wktui.base.InvisiblePanel;
/***
 * 
 * 
 
  
  
CREATE TABLE floorecord (
    id bigint Primary Key NOT NULL,
    floor_id bigint references floor(id) ON DELETE CASCADE,
    language character varying(24) default 'es' NOT NULL,
    name character varying(1024),
    subtitle character varying(1024),
    info text,
    photo bigint references resource(id) ON DELETE SET NULL,
    video bigint references resource(id) ON DELETE SET NULL,
    audio bigint references resource(id) ON DELETE SET NULL,
    created timestamp with time zone DEFAULT now() NOT NULL,
    lastmodified timestamp with time zone DEFAULT now() NOT NULL,
    lastmodifieduser bigint NOT NULL,
    draft text,
    namekey character varying(512),
    state integer DEFAULT 0,
    intro text,
    usethumbnail boolean DEFAULT true,
    name_hash bigint DEFAULT 0,
    subtitle_hash bigint DEFAULT 0,
    info_hash bigint DEFAULT 0,
    intro_hash bigint DEFAULT 0,
    spec text,    spec_hash bigint DEFAULT 0,
    otherjson text,    otherjson_hash bigint DEFAULT 0,
    opens text,    opens_hash bigint  DEFAULT 0,    audioautogenerate  boolean DEFAULT true,
    audioauto boolean  DEFAULT false,    infoaccessible      text,      infoaccesible_hash bigint default 0, audioaccessible   bigint references resource(id) ON DELETE SET NULL
);






id                 | bigint                   |           | not null | nextval('sequence_id'::regclass)
 guidecontent_id    | bigint                   |           |          |
 language           | character varying(24)    |           | not null |
 name               | character varying(1024)  |           |          |
 subtitle           | character varying(1024)  |           |          |
 info               | text                     |           |          |
 photo              | bigint                   |           |          |
 video              | bigint                   |           |          |
 audio              | bigint                   |           |          |
 created            | timestamp with time zone |           | not null | now()
 lastmodified       | timestamp with time zone |           | not null | now()
 lastmodifieduser   | bigint                   |           | not null |
 draft              | text                     |           |          |
 namekey            | character varying(512)   |           |          |
 state              | integer                  |           |          | 0
 usethumbnail       | boolean                  |           |          | true
 name_hash          | bigint                   |           |          | 0
 subtitle_hash      | bigint                   |           |          | 0
 info_hash          | bigint                   |           |          | 0
 intro_hash         | bigint                   |           |          | 0
 spec               | text                     |           |          |
 spec_hash          | bigint                   |           |          | 0
 otherjson          | text                     |           |          |
 otherjson_hash     | bigint                   |           |          | 0
 opens              | text                     |           |          |
 opens_hash         | bigint                   |           |          | 0
 audioautogenerate  | boolean                  |           |          | true
 intro              | text                     |           |          |
 audioauto          | boolean                  |           |          | false
 infoaccessible     | text                     |           |          |
 infoaccesible_hash | bigint                   |           |          | 0
 audioaccessible    | bigint                   |           |          |

  
 * 
 * 
 * 
 * 
 * 
 * 
 */
public class SiteFloorsPanel extends DBModelPanel<Site> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteFloorsPanel.class.getName());

	private List<IModel<Floor>> floorList;
	private List<IModel<Room>> roomList;

	private ListPanel<Floor> floorsListPanel;
	private ListPanel<Room> roomsListPanel;

	private IModel<Floor> selectedFloor;
	private WebMarkupContainer selectedFloorContainer;
	private Label selectedFloorLabel;

	public SiteFloorsPanel(String id, IModel<Site> model) {
		super(id, model);
		setOutputMarkupId(true);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		loadFloors();
		addFloorsPanel();
		//addSelectedFloorLabel();
		//addRoomsPanel();
		addFloorButton();
		//addRoomButton();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (this.floorList != null)
			this.floorList.forEach(m -> m.detach());
		if (this.roomList != null)
			this.roomList.forEach(m -> m.detach());
		if (this.selectedFloor != null)
			this.selectedFloor.detach();
	}

	@Override
	public List<ToolbarItem> getToolbarItems() {
		return null;
	}

	// -----------------------------------------------------------------------
	// Floors
	// -----------------------------------------------------------------------

	protected synchronized void loadFloors() {
		this.floorList = new ArrayList<>();
		getFloorDBService().getFloors(getModel().getObject()).forEach(f -> floorList.add(new ObjectModel<Floor>(f)));
	}

	protected synchronized void loadRooms(Floor floor) {
		this.roomList = new ArrayList<>();
		if (floor != null) {
			getRoomDBService().getRooms(floor).forEach(r -> roomList.add(new ObjectModel<Room>(r)));
		}
	}

	protected List<IModel<Floor>> getFloors() {
		if (this.floorList == null)
			loadFloors();
		return this.floorList;
	}

	protected List<IModel<Room>> getRooms() {
		if (this.roomList == null)
			this.roomList = new ArrayList<>();
		return this.roomList;
	}

	protected IModel<String> getFloorTitle(IModel<Floor> model) {

		StringBuilder sb = new StringBuilder();
		
		try {

			Floor f = model.getObject();

			String s = getLanguageObjectService().getObjectDisplayName(f, getLocale());
			sb.append(s);

			if (f.getFloorNumber() != null)
				sb.append(" <span class=\"text-secondary\">(" + f.getFloorNumber() + ")</span>");

			if (f.getState() == ObjectState.DELETED)
				sb.append(Icons.DELETED_ICON_HTML);
			else if (f.getState() == ObjectState.EDITION)
				sb.append(Icons.EDITION_ICON_HTML);
		} catch (Exception e) {
			logger.error(e);
			sb.append(e.getClass().getSimpleName() + " " + e.getMessage());
		}

		return Model.of(sb.toString());
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

	protected WebMarkupContainer getFloorMenu(IModel<Floor> model) {

		NavDropDownMenu<Floor> menu = new NavDropDownMenu<Floor>("menu", model, null);
		menu.setOutputMarkupId(true);
		menu.setTitleCss("d-block-inline d-sm-block-inline d-md-block-inline d-lg-none d-xl-none d-xxl-none ps-1 pe-1");
		menu.setIconCss("fa-solid fa-ellipsis d-block-inline d-sm-block-inline d-md-block-inline d-lg-block-inline d-xl-block-inline d-xxl-block-inline ps-1 pe-1");

		// Open
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Floor>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Floor> getItem(String id) {
				return new AjaxLinkMenuItem<Floor>(id, model) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						setResponsePage(new dellemuse.serverapp.floor.SiteFloorPage(getModel()));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("open");
					}
				};
			}
		});

		// Publish
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Floor>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Floor> getItem(String id) {
				return new AjaxLinkMenuItem<Floor>(id, model) {
					private static final long serialVersionUID = 1L;

					@Override
					public boolean isVisible() {
						return model.getObject().getState() != ObjectState.PUBLISHED;
					}

					@Override
					public boolean isEnabled() {
						return model.getObject().getState() != ObjectState.PUBLISHED;
					}

					@Override
					public void onClick(AjaxRequestTarget target) {
						model.getObject().setState(ObjectState.PUBLISHED);
						getFloorDBService().save(model.getObject(), getSessionUser().get());
						loadFloors();
						target.add(SiteFloorsPanel.this.floorsListPanel);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("publish");
					}
				};
			}
		});

		// Edition
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Floor>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Floor> getItem(String id) {
				return new AjaxLinkMenuItem<Floor>(id, model) {
					private static final long serialVersionUID = 1L;

					@Override
					public boolean isVisible() {
						return model.getObject().getState() != ObjectState.EDITION;
					}

					@Override
					public boolean isEnabled() {
						return model.getObject().getState() != ObjectState.EDITION;
					}

					@Override
					public void onClick(AjaxRequestTarget target) {
						model.getObject().setState(ObjectState.EDITION);
						getFloorDBService().save(model.getObject(), getSessionUser().get());
						loadFloors();
						target.add(SiteFloorsPanel.this.floorsListPanel);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("edit-mode");
					}
				};
			}
		});

		// Delete
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Floor>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Floor> getItem(String id) {
				return new AjaxLinkMenuItem<Floor>(id, model) {
					private static final long serialVersionUID = 1L;

					@Override
					public boolean isVisible() {
						return model.getObject().getState() != ObjectState.DELETED;
					}

					@Override
					public boolean isEnabled() {
						return model.getObject().getState() != ObjectState.DELETED;
					}

					@Override
					public void onClick(AjaxRequestTarget target) {
						getFloorDBService().markAsDeleted(model.getObject(), getSessionUser().get());
						loadFloors();
						target.add(SiteFloorsPanel.this.floorsListPanel);
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
					public boolean isEnabled() {
						return model.getObject().getState() != ObjectState.PUBLISHED;
					}

					@Override
					public void onClick(AjaxRequestTarget target) {
						model.getObject().setState(ObjectState.PUBLISHED);
						getRoomDBService().save(model.getObject(), getSessionUser().get());
						target.add(SiteFloorsPanel.this.roomsListPanel);
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
					public boolean isEnabled() {
						return model.getObject().getState() != ObjectState.EDITION;
					}

					@Override
					public void onClick(AjaxRequestTarget target) {
						model.getObject().setState(ObjectState.EDITION);
						getRoomDBService().save(model.getObject(), getSessionUser().get());
						target.add(SiteFloorsPanel.this.roomsListPanel);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("edit-mode");
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
					public boolean isEnabled() {
						return model.getObject().getState() != ObjectState.DELETED;
					}

					@Override
					public void onClick(AjaxRequestTarget target) {
						getRoomDBService().markAsDeleted(model.getObject(), getSessionUser().get());
						if (selectedFloor != null) {
							loadRooms(selectedFloor.getObject());
						}
						target.add(SiteFloorsPanel.this.roomsListPanel);
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

	/**
	 * Shows the selected floor name above the rooms list.
	 */
	private void addSelectedFloorLabel() {
		this.selectedFloorContainer = new WebMarkupContainer("selected-floor-container") {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return SiteFloorsPanel.this.selectedFloor != null;
			}
		};
		this.selectedFloorContainer.setOutputMarkupId(true);
		this.selectedFloorContainer.setOutputMarkupPlaceholderTag(true);

		this.selectedFloorLabel = new Label("selected-floor-label", new IModel<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				if (SiteFloorsPanel.this.selectedFloor == null)
					return "";
				return SiteFloorsPanel.this.selectedFloor.getObject().getDisplayname();
			}
		});
		this.selectedFloorLabel.setEscapeModelStrings(false);
		this.selectedFloorLabel.setOutputMarkupId(true);
		this.selectedFloorContainer.add(this.selectedFloorLabel);
		add(this.selectedFloorContainer);
	}
	
	private void addFloorsPanel() {
		this.floorsListPanel = new ListPanel<Floor>("floors-panel") {

			private static final long serialVersionUID = 1L;

			@Override
			public List<IModel<Floor>> getItems() {
				return getFloors();
			}

			@Override
			public Integer getTotalItems() {
				return Integer.valueOf(getFloors().size());
			}

			@Override
			protected Panel getListItemPanel(IModel<Floor> model, ListPanelMode mode) {
				DelleMuseObjectListItemPanel<Floor> row = new DelleMuseObjectListItemPanel<Floor>("row-element", model, mode) {
					private static final long serialVersionUID = 1L;

					@Override
					protected WebMarkupContainer getObjectMenu() {
						return SiteFloorsPanel.this.getFloorMenu(getModel());
					}

					@Override
					public void onClick() {
						
						setResponsePage(new  SiteFloorPage(getModel()));
						
						/*
						AjaxRequestTarget target = getRequestCycle().find(AjaxRequestTarget.class).orElse(null);
						SiteFloorsPanel.this.selectedFloor = getModel();
						SiteFloorsPanel.this.loadRooms(getModel().getObject());
						if (target != null) {
							target.add(SiteFloorsPanel.this.floorsListPanel);
							target.add(SiteFloorsPanel.this.roomsListPanel);
							target.add(SiteFloorsPanel.this.selectedFloorContainer);
						}
				*/
				
					
					}

					@Override
					protected IModel<String> getObjectTitle() {
						return SiteFloorsPanel.this.getFloorTitle(getModel());
					}

					@Override
					protected IModel<String> getInfo() {
						return null;
					}

					@Override
					protected String getCss() {
						String base = super.getCss();
						if (SiteFloorsPanel.this.selectedFloor != null
								&& SiteFloorsPanel.this.selectedFloor.getObject().getId() != null
								&& SiteFloorsPanel.this.selectedFloor.getObject().getId().equals(getModel().getObject().getId())) {
							return base + " dm-selected";
						}
						return base;
					}
				};
				return row;
			}

			@Override
			public IModel<String> getItemLabel(IModel<Floor> model) {
				return SiteFloorsPanel.this.getFloorTitle(model);
			}
		};

		this.floorsListPanel.setTitle(getLabel("floors"));
		this.floorsListPanel.setListPanelMode(ListPanelMode.TITLE);
		this.floorsListPanel.setOutputMarkupId(true);
		add(this.floorsListPanel);
	}

	private void addRoomsPanel() {
		this.roomList = new ArrayList<>();

		this.roomsListPanel = new ListPanel<Room>("rooms-panel") {

			private static final long serialVersionUID = 1L;

			@Override
			public List<IModel<Room>> getItems() {
				return getRooms();
			}

			@Override
			public Integer getTotalItems() {
				return Integer.valueOf(getRooms().size());
			}

			@Override
			protected Panel getListItemPanel(IModel<Room> model, ListPanelMode mode) {
				DelleMuseObjectListItemPanel<Room> row = new DelleMuseObjectListItemPanel<Room>("row-element", model, mode) {
					private static final long serialVersionUID = 1L;

					@Override
					protected WebMarkupContainer getObjectMenu() {
						return SiteFloorsPanel.this.getRoomMenu(getModel());
					}

					@Override
					public void onClick() {
						// placeholder for future Room editor
					}

					@Override
					protected IModel<String> getObjectTitle() {
						return SiteFloorsPanel.this.getRoomTitle(getModel());
					}

					@Override
					protected IModel<String> getInfo() {
						return null;
					}
				};
				return row;
			}

			@Override
			public IModel<String> getItemLabel(IModel<Room> model) {
				return SiteFloorsPanel.this.getRoomTitle(model);
			}
		};

		this.roomsListPanel.setTitle(getLabel("rooms"));
		this.roomsListPanel.setListPanelMode(ListPanelMode.TITLE);
		this.roomsListPanel.setOutputMarkupId(true);
		add(this.roomsListPanel);
	}

	private void addFloorButton() {
		AjaxButtonToolbarItem<?> addFloorBtn = new AjaxButtonToolbarItem<>("add-floor-btn", getLabel("add-floor")) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onCick(AjaxRequestTarget target) {
				try {
					User user = getSessionUser().get();
					Floor floor = getFloorDBService().create("new floor", SiteFloorsPanel.this.getModel().getObject(), user);
					SiteFloorsPanel.this.floorList.add(new ObjectModel<Floor>(floor));
					target.add(SiteFloorsPanel.this.floorsListPanel);
				} catch (Exception e) {
					logger.error(e);
				}
			}

			@Override
			public boolean isVisible() {
				return getSessionUser().isPresent();
			}
		};
		addFloorBtn.setOutputMarkupId(true);
		add(addFloorBtn);
	}

	private void addRoomButton() {
		AjaxButtonToolbarItem<?> addRoomBtn = new AjaxButtonToolbarItem<>("add-room-btn", getLabel("add-room")) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onCick(AjaxRequestTarget target) {
				try {
					if (selectedFloor == null) {
						return;
					}
					User user = getSessionUser().get();
					Room room = getRoomDBService().create("new room", selectedFloor.getObject(), user);
					SiteFloorsPanel.this.roomList.add(new ObjectModel<Room>(room));
					target.add(SiteFloorsPanel.this.roomsListPanel);
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
