package dellemuse.serverapp.floor;

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

public class FloorEditor extends DBSiteObjectEditor<Floor> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(FloorEditor.class.getName());

	private TextField<String> nameField;
	private TextField<String> subtitleField;
	private TextField<String> floorNumberField;
	private TextAreaField<String> infoField;
	private FileUploadSimpleField<Resource> photoField;
	private IModel<Resource> photoModel;
	private IModel<Site> siteModel;
	private List<ToolbarItem> toolbarList;
	private boolean uploadedPhoto = false;

	public FloorEditor(String id, IModel<Floor> model) {
		super(id, model);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();

		add(new InvisiblePanel("error"));

		add(new Label("info", getLabel("floor-information", getModel().getObject().getMasterLanguage())));
		
		Form<Floor> form = new Form<Floor>("form");
		add(form);
		setForm(form);

		nameField = new TextField<String>("name", new PropertyModel<String>(getModel(), "name"), getLabel("name"));
		subtitleField = new TextField<String>("subtitle", new PropertyModel<String>(getModel(), "subtitle"), getLabel("subtitle"));
		floorNumberField = new TextField<String>("floornumber", new PropertyModel<String>(getModel(), "floorNumber"), getLabel("floornumber"));
		infoField = new TextAreaField<String>("info", new PropertyModel<String>(getModel(), "info"), getLabel("info"), 8);

		photoField = new FileUploadSimpleField<Resource>("photo", getPhotoModel(), getLabel("photo")) {
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean processFileUploads(List<FileUpload> uploads) {
				return FloorEditor.this.processPhotoUpload(uploads);
			}

			@Override
			public Image getImage() {
				if (getPhotoModel() == null || getPhotoModel().getObject() == null)
					return null;
				return FloorEditor.this.getThumbnail(getPhotoModel().getObject());
			}

			@Override
			public String getFileName() {
				if (getModel() != null && getModel().getObject() != null)
					return FloorEditor.this.getPhotoMeta(getModel().getObject());
				return null;
			}

			@Override
			public boolean isThumbnail() {
				return true;
			}

			@Override
			protected void onRemove(AjaxRequestTarget target) {
				// handled by remove action
			}
		};

		form.add(nameField);
		form.add(subtitleField);
		form.add(floorNumberField);
		form.add(infoField);
		form.add(photoField);

		EditButtons<Floor> buttonsTop = new EditButtons<Floor>("buttons-top", getForm(), getModel()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEdit(AjaxRequestTarget target) { FloorEditor.this.onEdit(target); }
			@Override
			public void onCancel(AjaxRequestTarget target) { FloorEditor.this.onCancel(target); }
			@Override
			public void onSave(AjaxRequestTarget target) { FloorEditor.this.onSave(target); }
			@Override
			public boolean isVisible() {
				return hasWritePermission() && getForm().getFormState() == FormState.EDIT;
			}
			@Override protected String getSaveClass()   { return "ps-0 btn btn-sm btn-link"; }
			@Override protected String getCancelClass() { return "ps-0 btn btn-sm btn-link"; }
		};
		form.add(buttonsTop);

		EditButtons<Floor> buttonsBottom = new EditButtons<Floor>("buttons-bottom", getForm(), getModel()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEdit(AjaxRequestTarget target) { FloorEditor.this.onEdit(target); }
			@Override
			public void onCancel(AjaxRequestTarget target) { FloorEditor.this.onCancel(target); }
			@Override
			public void onSave(AjaxRequestTarget target) { FloorEditor.this.onSave(target); }
			@Override
			public boolean isVisible() {
				return hasWritePermission() && getForm().getFormState() == FormState.EDIT;
			}
		};
		form.add(buttonsBottom);
	}

	public void onEdit(AjaxRequestTarget target) {
		super.edit(target);
		addOrReplace(new InvisiblePanel("error"));
		target.add(this);
	}

	protected void onCancel(AjaxRequestTarget target) {
		super.cancel(target);
	}

	protected void onSave(AjaxRequestTarget target) {
		if (getUpdatedParts() == null || getUpdatedParts().isEmpty())
			return;

		try {
			save(getModelObject(), getSessionUser().get(), getUpdatedParts());
			this.uploadedPhoto = false;
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
		if (toolbarList != null)
			return toolbarList;

		toolbarList = new ArrayList<>();

		AjaxButtonToolbarItem<Floor> editBtn = new AjaxButtonToolbarItem<Floor>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
				fire(new MenuAjaxEvent(ServerAppConstant.action_floor_info_edit, target));
			}

			@Override
			public IModel<String> getButtonLabel() {
				return getLabel("edit");
			}
		};
		editBtn.setAlign(Align.TOP_LEFT);
		toolbarList.add(editBtn);
		return toolbarList;
	}

	protected boolean processPhotoUpload(List<FileUpload> uploads) {
		if (this.uploadedPhoto)
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

					setPhotoModel(new ObjectModel<Resource>(resource));
					getModel().getObject().setPhoto(resource);
					uploadedPhoto = true;
				} catch (Exception e) {
					uploadedPhoto = false;
					error("Error saving file: " + e.getMessage());
				}
			}
		}
		return uploadedPhoto;
	}

	private void setUpModel() {
		Optional<Floor> o = getFloorDBService().findWithDeps(getModel().getObject().getId());
		setModel(new ObjectModel<Floor>(o.get()));

		if (getModel().getObject().getPhoto() != null) {
			Optional<Resource> o_r = getResourceDBService().findWithDeps(getModel().getObject().getPhoto().getId());
			setPhotoModel(new ObjectModel<Resource>(o_r.get()));
		}

		if (getModel().getObject().getSite() != null) {
			Optional<Site> o_s = getSiteDBService().findWithDeps(getModel().getObject().getSite().getId());
			setSiteModel(new ObjectModel<Site>(o_s.get()));
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (photoModel != null) photoModel.detach();
		if (siteModel != null)  siteModel.detach();
	}

	public IModel<Resource> getPhotoModel() { return photoModel; }
	public void setPhotoModel(ObjectModel<Resource> model) { this.photoModel = model; }

	@Override
	public IModel<Site> getSiteModel() { return siteModel; }
	public void setSiteModel(IModel<Site> siteModel) { this.siteModel = siteModel; }
}
