package dellemuse.serverapp.artexhibitionitem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.editor.DBSiteObjectEditor;
import dellemuse.serverapp.editor.ObjectUpdateEvent;
import dellemuse.serverapp.editor.SimpleAlertRow;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.Floor;
import dellemuse.serverapp.serverdb.model.Room;
import dellemuse.serverapp.serverdb.model.Site;
import io.wktui.event.MenuAjaxEvent;

import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.EditButtons;
import io.wktui.form.field.ChoiceField;
import io.wktui.form.field.TextField;
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import wktui.base.InvisiblePanel;

public class ArtExhibitionItemEditor extends DBSiteObjectEditor<ArtExhibitionItem> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ArtExhibitionItemEditor.class.getName());

	private TextField<String> nameField;
	private TextField<String> orderField;
	private TextField<String> readCodeField;
	private TextField<String> qrCodeField;

	private ChoiceField<Floor> floorSelector;
	private ChoiceField<Room> roomSelector;
	private RoomMapPinPanel mapPinPanel;
	private FloorMapPinPanel floorMapPinPanel;

	/** Serializable IDs used by LoadableDetachableModel choices */
	private Long siteIdForFloors;
	private Long selectedFloorId;
	private Long selectedRoomId;

	/** Serializable room choices model – promoted to field so floor onUpdate can detach it */
	private LoadableDetachableModel<List<Room>> roomChoicesModel;

	private IModel<Site> siteModel;
	private IModel<ArtExhibition> artExhibitionModel;
	private IModel<ArtExhibitionItem> artExhibitionItemModel;

	/**
	 * @param id
	 * @param model
	 */
	public ArtExhibitionItemEditor(String id, IModel<ArtExhibitionItem> model, IModel<ArtExhibition> artExhibitionModel, IModel<Site> siteModel) {
		super(id, model);
		this.artExhibitionModel = artExhibitionModel;
		this.siteModel = siteModel;
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();

		add(new InvisiblePanel("error"));

		Form<ArtExhibitionItem> form = new Form<ArtExhibitionItem>("form");
		add(form);
		setForm(form);

		this.nameField = new TextField<String>("name", new PropertyModel<String>(getModel(), "name"), getLabel("name"));
		this.orderField = new TextField<String>("order", new PropertyModel<String>(getModel(), "exhibitionOrder"), getLabel("order"));
		this.readCodeField = new TextField<String>("readcode", new PropertyModel<String>(getModel(), "readCode"), getLabel("readcode"));
		this.qrCodeField = new TextField<String>("qrcode", new PropertyModel<String>(getModel(), "qRCode"), getLabel("qrcode"));
		this.qrCodeField.setVisible(false);

		// Store the site ID (serializable Long) for use in the floor choices model
		if (getSiteModel().getObject() != null)
			siteIdForFloors = getSiteModel().getObject().getId();

		// Store the currently selected floor ID (getId() is safe on Hibernate proxy)
		Floor proxyFloor = getModelObject().getFloor();
		if (proxyFloor != null)
			selectedFloorId = proxyFloor.getId();

		// Store the currently selected room ID
		Room proxyRoom = getModelObject().getRoom();
		if (proxyRoom != null)
			selectedRoomId = proxyRoom.getId();

		// LoadableDetachableModel for floor choices
		IModel<List<Floor>> floorChoicesModel = new LoadableDetachableModel<List<Floor>>() {
			private static final long serialVersionUID = 1L;
			@Override
			protected List<Floor> load() {
				if (siteIdForFloors == null) return new ArrayList<>();
				return getFloorDBService().getFloors(siteIdForFloors);
			}
		};

		// Proxy-safe floor selection model: resolves from choices list by ID each access
		IModel<Floor> floorSelectionModel = new IModel<Floor>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Floor getObject() {
				if (selectedFloorId == null) return null;
				return floorChoicesModel.getObject().stream()
						.filter(f -> f.getId().equals(selectedFloorId))
						.findFirst().orElse(null);
			}
			@Override
			public void setObject(Floor f) {
				selectedFloorId = (f != null) ? f.getId() : null;
				getModelObject().setFloor(f);
			}
		};

		this.floorSelector = new ChoiceField<Floor>("floor", floorSelectionModel, getLabel("floor")) {
			private static final long serialVersionUID = 1L;

			@Override
			public IModel<List<Floor>> getChoices() {
				return floorChoicesModel;
			}

			@Override
			protected String getDisplayValue(Floor value) {
				if (value == null) return null;
				return value.getName();
			}

			@Override
			protected String getIdValue(Floor value) {
				if (value == null) return null;
				return value.getId().toString();
			}

			@Override
			public void onUpdate(AjaxRequestTarget target) {
				Floor selected = getValue();
				selectedFloorId = (selected != null) ? selected.getId() : null;
				selectedRoomId = null;
				getModelObject().setFloor(selected);
				getModelObject().setRoom(null);
				roomSelector.setValue(null);
				roomChoicesModel.detach();
				if (mapPinPanel != null)
					mapPinPanel.setRoom(null, target);
				if (floorMapPinPanel != null)
					floorMapPinPanel.setFloor(selected, target);
				target.add(roomSelector);
			}

			@Override
			public boolean isNullValid() { return true; }
		};

		// LoadableDetachableModel for room choices – promoted to field so onUpdate can detach it
		roomChoicesModel = new LoadableDetachableModel<List<Room>>() {
			private static final long serialVersionUID = 1L;
			@Override
			protected List<Room> load() {
				if (selectedFloorId == null) return new ArrayList<>();
				return getRoomDBService().getRooms(selectedFloorId);
			}
		};

		// Proxy-safe room selection model
		IModel<Room> roomSelectionModel = new IModel<Room>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Room getObject() {
				if (selectedRoomId == null) return null;
				return roomChoicesModel.getObject().stream()
						.filter(r -> r.getId().equals(selectedRoomId))
						.findFirst().orElse(null);
			}
			@Override
			public void setObject(Room r) {
				selectedRoomId = (r != null) ? r.getId() : null;
				getModelObject().setRoom(r);
			}
		};

		this.roomSelector = new ChoiceField<Room>("room", roomSelectionModel, getLabel("room")) {
			private static final long serialVersionUID = 1L;

			@Override
			public IModel<List<Room>> getChoices() {
				return roomChoicesModel;
			}

			@Override
			protected String getDisplayValue(Room value) {
				if (value == null) return null;
				return value.getName();
			}

			@Override
			protected String getIdValue(Room value) {
				if (value == null) return null;
				return value.getId().toString();
			}

			@Override
			public boolean isNullValid() { return true; }

			@Override
			public void onUpdate(AjaxRequestTarget target) {
				Room selected = getValue();
				selectedRoomId = (selected != null) ? selected.getId() : null;
				getModelObject().setRoom(selected);
				if (mapPinPanel != null)
					mapPinPanel.setRoom(selected, target);
			}
		};
		this.roomSelector.setOutputMarkupId(true);

		// Construct map pin panel – initial room resolved from model
		Room initialRoom = roomSelectionModel.getObject();
		mapPinPanel = new RoomMapPinPanel<ArtExhibitionItem>("map-pin", getModel(), this,
			new MapPinCallbacks<ArtExhibitionItem>() {
				private static final long serialVersionUID = 1L;
				public Double getPinX(ArtExhibitionItem i)      { return i.getMapPosX(); }
				public Double getPinY(ArtExhibitionItem i)      { return i.getMapPosY(); }
				public Long   getPinEntityId(ArtExhibitionItem i){ return i.getMapPosRoomId(); }
				public void setPinX(ArtExhibitionItem i, Double x)    { i.setMapPosX(x); }
				public void setPinY(ArtExhibitionItem i, Double y)    { i.setMapPosY(y); }
				public void setPinEntityId(ArtExhibitionItem i, Long id){ i.setMapPosRoomId(id); }
			});
		if (initialRoom != null)
			mapPinPanel.setRoom(initialRoom, null);
		mapPinPanel.setOutputMarkupId(true);

		// Construct floor map pin panel – initial floor resolved from model
		Floor initialFloor = floorSelectionModel.getObject();
		floorMapPinPanel = new FloorMapPinPanel<ArtExhibitionItem>("floor-map-pin", getModel(), this,
			new MapPinCallbacks<ArtExhibitionItem>() {
				private static final long serialVersionUID = 1L;
				public Double getPinX(ArtExhibitionItem i)      { return i.getMapFloor_PosX(); }
				public Double getPinY(ArtExhibitionItem i)      { return i.getMapFloorPosY(); }
				public Long   getPinEntityId(ArtExhibitionItem i){ return i.getMapPosFloorId(); }
				public void setPinX(ArtExhibitionItem i, Double x)    { i.setMapFloor_PosX(x); }
				public void setPinY(ArtExhibitionItem i, Double y)    { i.setMapFloorPosY(y); }
				public void setPinEntityId(ArtExhibitionItem i, Long id){ i.setMapPosFloorId(id); }
			});
		if (initialFloor != null)
			floorMapPinPanel.setFloor(initialFloor, null);
		floorMapPinPanel.setOutputMarkupId(true);

		form.add(nameField);
		form.add(floorSelector);
		form.add(floorMapPinPanel);
		form.add(roomSelector);
		form.add(mapPinPanel);
		form.add(orderField);
		form.add(readCodeField);
		form.add(qrCodeField);

		EditButtons<ArtExhibitionItem> buttons = new EditButtons<ArtExhibitionItem>("buttons-bottom", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onEdit(AjaxRequestTarget target) {
				ArtExhibitionItemEditor.this.onEdit(target);
			}

			@Override
			public void onCancel(AjaxRequestTarget target) {
				ArtExhibitionItemEditor.this.onCancel(target);
			}

			@Override
			public void onSave(AjaxRequestTarget target) {
				ArtExhibitionItemEditor.this.onSave(target);
			}

			@Override
			public boolean isVisible() {

				if (!hasWritePermission())
					return false;

				return getForm().getFormState() == FormState.EDIT;
			}
		};
		form.add(buttons);

		EditButtons<ArtExhibitionItem> b_buttons_top = new EditButtons<ArtExhibitionItem>("buttons-top", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			public void onEdit(AjaxRequestTarget target) {
				ArtExhibitionItemEditor.this.onEdit(target);
			}

			public void onCancel(AjaxRequestTarget target) {
				ArtExhibitionItemEditor.this.onCancel(target);
			}

			public void onSave(AjaxRequestTarget target) {
				ArtExhibitionItemEditor.this.onSave(target);
			}

			@Override
			public boolean isVisible() {

				if (!hasWritePermission())
					return false;

				return getForm().getFormState() == FormState.EDIT;
			}

			protected String getSaveClass() {
				return "ps-0 btn btn-sm btn-link";
			}

			protected String getCancelClass() {
				return "ps-0 btn btn-sm btn-link";
			}

		};
		getForm().add(b_buttons_top);
	}

	protected void onCancel(AjaxRequestTarget target) {
		super.cancel(target);
	}

	public void onEdit(AjaxRequestTarget target) {
		super.edit(target);
	}

	@Override
	public List<ToolbarItem> getToolbarItems() {

		List<ToolbarItem> list = new ArrayList<ToolbarItem>();

		AjaxButtonToolbarItem<ArtExhibition> create = new AjaxButtonToolbarItem<ArtExhibition>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
				fire(new MenuAjaxEvent(ServerAppConstant.action_exhibition_item_info_edit, target));
			}

			@Override
			public IModel<String> getButtonLabel() {
				return getLabel("edit");
			}
		};
		create.setAlign(Align.TOP_LEFT);
		list.add(create);

		return list;
	}

	public IModel<ArtExhibitionItem> getArtExhibitionItemModel() {
		return artExhibitionItemModel;
	}

	public void setArtExhibitionItemModel(IModel<ArtExhibitionItem> artExhibitionItemModel) {
		this.artExhibitionItemModel = artExhibitionItemModel;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (siteModel != null)
			siteModel.detach();

		if (artExhibitionModel != null)
			artExhibitionModel.detach();

		if (artExhibitionItemModel != null)
			artExhibitionItemModel.detach();
	}

	public IModel<Site> getSiteModel() {
		return siteModel;
	}

	public void setSiteModel(IModel<Site> siteModel) {
		this.siteModel = siteModel;
	}

	public IModel<ArtExhibition> getArtExhibitionModel() {
		return artExhibitionModel;
	}

	public void setArtExhibitionModel(IModel<ArtExhibition> siteModel) {
		this.artExhibitionModel = siteModel;
	}

	protected void onSubmit() {
		logger.debug("");
		logger.debug("onSubmit");
		logger.debug("");
	}

	protected void onSave(AjaxRequestTarget target) {

		try {
			// Apply pin coordinates from panel fields into model before saving
			if (mapPinPanel != null)
				mapPinPanel.applyToModel();
			if (floorMapPinPanel != null)
				floorMapPinPanel.applyToModel();

			// Mark pins as updated if dirty
			if (mapPinPanel != null && mapPinPanel.isPinDirty())
				setUpdatedPart("mapPos");
			if (floorMapPinPanel != null && floorMapPinPanel.isPinDirty())
				setUpdatedPart("mapFloorPos");

			// Always save when form is in EDIT state
			if (getUpdatedParts().isEmpty())
				setUpdatedPart("no-change");

			getUpdatedParts().forEach(s -> logger.debug(s));
			save(getModelObject(), getSessionUser().get(), getUpdatedParts());
			getForm().setFormState(FormState.VIEW);
			getForm().updateReload();
			fireScanAll(new ObjectUpdateEvent(target));

		} catch (Exception e) {
			addOrReplace(new SimpleAlertRow<Void>("error", e));
			logger.error(e);
		}
		target.add(this);
	}

	private void setUpModel() {

		Optional<ArtExhibitionItem> o_i = getArtExhibitionItemDBService().findWithDeps(getModel().getObject().getId());
		setModel(new ObjectModel<ArtExhibitionItem>(o_i.get()));

		Optional<ArtExhibition> o_a = getArtExhibitionDBService().findWithDeps(getArtExhibitionModel().getObject().getId());
		setArtExhibitionModel(new ObjectModel<>(o_a.get()));

		Optional<Site> o_s = getSiteDBService().findWithDeps(getArtExhibitionModel().getObject().getSite().getId());
		setSiteModel(new ObjectModel<Site>(o_s.get()));
	}

}
