package dellemuse.serverapp.room;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.editor.DBSiteObjectEditor;
import dellemuse.serverapp.editor.ObjectUpdateEvent;
import dellemuse.serverapp.editor.SimpleAlertRow;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.Floor;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Room;
import dellemuse.serverapp.serverdb.model.Site;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.EditButtons;
import io.wktui.form.field.FileUploadSimpleField;
import io.wktui.form.field.TextField;
import io.wktui.form.field.TextAreaField;
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import wktui.base.InvisiblePanel;

public class RoomEditor extends DBSiteObjectEditor<Room> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(RoomEditor.class.getName());

	private TextField<String> nameField;
	private TextField<String> subtitleField;
	private TextField<String> roomNumberField;
	private TextAreaField<String> infoField;
	private FileUploadSimpleField<Resource> mapField;
	private IModel<Resource> mapModel;
	private IModel<Site> siteModel;
	private IModel<Floor> floorModel;
	private List<ToolbarItem> toolbarList;
	private boolean uploadedMap = false;
	private boolean mapRemoved  = false;

	public RoomEditor(String id, IModel<Room> model) {
		super(id, model);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		
		setUpModel();

		add(new InvisiblePanel("error"));

		add(new Label("info", getLabel("room-information", getModel().getObject().getMasterLanguage())));
		
		Form<Room> form = new Form<Room>("form");
		add(form);
		setForm(form);

		nameField       = new TextField<String>("name",       new PropertyModel<String>(getModel(), "name"),       getLabel("name"));
		subtitleField   = new TextField<String>("subtitle",   new PropertyModel<String>(getModel(), "subtitle"),   getLabel("subtitle"));
		roomNumberField = new TextField<String>("roomnumber", new PropertyModel<String>(getModel(), "roomNumber"), getLabel("roomnumber"));
		infoField       = new TextAreaField<String>("info",   new PropertyModel<String>(getModel(), "info"),       getLabel("info"), 8);

		mapField = new FileUploadSimpleField<Resource>("map", getMapModel(), getLabel("map")) {
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean processFileUploads(List<FileUpload> uploads) {
				return RoomEditor.this.processMapUpload(uploads);
			}

			@Override
			public Image getImage() {
				if (getMapModel() == null || getMapModel().getObject() == null)
					return null;
				return RoomEditor.this.getThumbnail(getMapModel().getObject());
			}

			@Override
			public String getFileName() {
				if (getModel() != null && getModel().getObject() != null)
					return RoomEditor.this.getPhotoMeta(getModel().getObject());
				return null;
			}

			@Override
			public boolean isThumbnail() { return true; }

			@Override
			protected void onRemove(AjaxRequestTarget target) {
				mapModel = null;
				mapRemoved = true;
				uploadedMap = false;
				setUpdatedPart("map");
				target.add(RoomEditor.this);
			}
		};

		form.add(nameField);
		form.add(subtitleField);
		form.add(roomNumberField);
		form.add(infoField);
		form.add(mapField);

		EditButtons<Room> buttonsTop = new EditButtons<Room>("buttons-top", getForm(), getModel()) {
			private static final long serialVersionUID = 1L;

			@Override public void onEdit(AjaxRequestTarget t)   { RoomEditor.this.onEdit(t); }
			@Override public void onCancel(AjaxRequestTarget t) { RoomEditor.this.onCancel(t); }
			@Override public void onSave(AjaxRequestTarget t)   { RoomEditor.this.onSave(t); }
			@Override public boolean isVisible() { return hasWritePermission() && getForm().getFormState() == FormState.EDIT; }
			@Override protected String getSaveClass()   { return "ps-0 btn btn-sm btn-link"; }
			@Override protected String getCancelClass() { return "ps-0 btn btn-sm btn-link"; }
		};
		form.add(buttonsTop);

		EditButtons<Room> buttonsBottom = new EditButtons<Room>("buttons-bottom", getForm(), getModel()) {
			private static final long serialVersionUID = 1L;

			@Override public void onEdit(AjaxRequestTarget t)   { RoomEditor.this.onEdit(t); }
			@Override public void onCancel(AjaxRequestTarget t) { RoomEditor.this.onCancel(t); }
			@Override public void onSave(AjaxRequestTarget t)   { RoomEditor.this.onSave(t); }
			@Override public boolean isVisible() { return hasWritePermission() && getForm().getFormState() == FormState.EDIT; }
		};
		form.add(buttonsBottom);
	}

	public void onEdit(AjaxRequestTarget target) {
		super.edit(target);
		addOrReplace(new InvisiblePanel("error"));
		target.add(this);
	}

	protected void onCancel(AjaxRequestTarget target) { super.cancel(target); }

	protected void onSave(AjaxRequestTarget target) {
		if (getUpdatedParts() == null || getUpdatedParts().isEmpty()) return;
		try {
			// If the map was removed, apply that to the freshly-reloaded model object
			// (the model detaches between requests so we need to re-apply here)
			if (mapRemoved)
				getModel().getObject().setMap(null);

			save(getModelObject(), getSessionUser().get(), getUpdatedParts());
			this.uploadedMap = false;
			this.mapRemoved  = false;
			getForm().setFormState(FormState.VIEW);
			getForm().updateReload();
			fireScanAll(new ObjectUpdateEvent(target));
		} catch (Exception e) {
			addOrReplace(new SimpleAlertRow<Void>("error", e));
			getForm().setFormState(FormState.VIEW);
			logger.error(e);
		}
		target.add(this);
	}

	@Override
	public List<ToolbarItem> getToolbarItems() {
		if (toolbarList != null) return toolbarList;
		toolbarList = new ArrayList<>();
		AjaxButtonToolbarItem<Room> editBtn = new AjaxButtonToolbarItem<Room>() {
			private static final long serialVersionUID = 1L;

			@Override protected void onCick(AjaxRequestTarget target) {
				fire(new MenuAjaxEvent(ServerAppConstant.action_room_info_edit, target));
			}

			@Override public IModel<String> getButtonLabel() { return getLabel("edit"); }
		};
		editBtn.setAlign(Align.TOP_LEFT);
		toolbarList.add(editBtn);
		return toolbarList;
	}

	protected boolean processMapUpload(List<FileUpload> uploads) {
		if (this.uploadedMap)
			return false;

		if (uploads != null && !uploads.isEmpty()) {
			for (FileUpload upload : uploads) {
				try {
					String bucketName = ServerConstant.MEDIA_BUCKET;
					String objectName = getResourceDBService().normalizeFileName(
						org.apache.commons.compress.utils.FileNameUtils.getBaseName(upload.getClientFileName()))
						+ "-" + getResourceDBService().newId();

					Resource resource = createAndUploadFile(
						upload.getInputStream(), bucketName, objectName,
						upload.getClientFileName(), upload.getSize(), true);

					setMapModel(new ObjectModel<Resource>(resource));
					getModel().getObject().setMap(resource);
					uploadedMap = true;
				} catch (Exception e) {
					uploadedMap = false;
					error("Error saving file: " + e.getMessage());
				}
			}
		}
		return uploadedMap;
	}

	private void setUpModel() {
		Optional<Room> o = getRoomDBService().findWithDeps(getModel().getObject().getId());
		setModel(new ObjectModel<Room>(o.get()));

		if (getModel().getObject().getMap() != null) {
			Optional<Resource> o_r = getResourceDBService().findWithDeps(getModel().getObject().getMap().getId());
			o_r.ifPresent(r -> setMapModel(new ObjectModel<Resource>(r)));
		}

		if (getModel().getObject().getFloor() != null) {
			Optional<Floor> of = getFloorDBService().findWithDeps(getModel().getObject().getFloor().getId());
			of.ifPresent(f -> {
				setFloorModel(new ObjectModel<Floor>(f));
				if (f.getSite() != null)
					getSiteDBService().findWithDeps(f.getSite().getId())
						.ifPresent(s -> setSiteModel(new ObjectModel<Site>(s)));
			});
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (mapModel != null)   mapModel.detach();
		if (siteModel != null)  siteModel.detach();
		if (floorModel != null) floorModel.detach();
	}

	public IModel<Resource> getMapModel() { return mapModel; }
	public void setMapModel(ObjectModel<Resource> model) { this.mapModel = model; }

	@Override
	public IModel<Site> getSiteModel() { return siteModel; }
	public void setSiteModel(IModel<Site> siteModel) { this.siteModel = siteModel; }

	public IModel<Floor> getFloorModel() { return floorModel; }
	public void setFloorModel(IModel<Floor> floorModel) { this.floorModel = floorModel; }
}