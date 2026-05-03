package dellemuse.serverapp.room;

import java.util.List;
import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.editor.ObjectRecordEditor;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Room;
import dellemuse.serverapp.serverdb.model.RoomRecord;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.record.RoomRecordDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.form.field.FileUploadSimpleField;
import io.wktui.form.field.TextAreaField;

public class RoomRecordEditor extends ObjectRecordEditor<Room, RoomRecord> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(RoomRecordEditor.class.getName());

	private IModel<Resource> audioAccessibleModel;
	private boolean uploadedAudioAccessible = false;

	private FileUploadSimpleField<Resource> audioAccessibleField;
	private TextAreaField<String> infoAccessibleField;

	private IModel<Site> siteModel;

	public RoomRecordEditor(String id, IModel<Room> sourceModel, IModel<RoomRecord> recordModel) {
		super(id, sourceModel, recordModel);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		Optional<RoomRecord> o = ((RoomRecordDBService) ServiceLocator.getInstance().getBean(RoomRecordDBService.class)).findWithDeps(getModel().getObject().getId());
		if (o.isPresent())
			setModel(new ObjectModel<RoomRecord>(o.get()));
		/**
		 * infoAccessibleField = new TextAreaField<String>( "infoAccessible", new
		 * PropertyModel<String>(getModel(), "infoAccessible"),
		 * getLabel("info-accessible"), 4);
		 * 
		 * audioAccessibleField = new FileUploadSimpleField<Resource>("audioAccessible",
		 * getAudioAccessibleModel(), getLabel("audio-accessible")) { private static
		 * final long serialVersionUID = 1L;
		 * 
		 * @Override protected boolean processFileUploads(List<FileUpload> uploads) {
		 *           return RoomRecordEditor.this.processAudioAccessibleUpload(uploads);
		 *           }
		 * 
		 * @Override protected void onRemove(AjaxRequestTarget target) {
		 *           RoomRecordEditor.this.onAudioAccessibleRemove(target); }
		 * 
		 * @Override public Image getImage() { return null; }
		 * 
		 * @Override protected String getAudioSrc() { if (getAudioAccessibleModel() ==
		 *           null || getAudioAccessibleModel().getObject() == null) return null;
		 *           try { return
		 *           getObjectStorageService().getPublicUrl(getAudioAccessibleModel().getObject());
		 *           } catch (Exception e) { logger.error(e); return null; } }
		 * 
		 * @Override public String getAudioMetadata() { if (getAudioAccessibleModel() !=
		 *           null && getAudioAccessibleModel().getObject() != null) return
		 *           RoomRecordEditor.this.getAudioMeta(getAudioAccessibleModel().getObject());
		 *           return null; } };
		 * 
		 *           getForm().add(infoAccessibleField);
		 *           getForm().add(audioAccessibleField);
		 * 
		 */
	}

	@Override
	protected void setUpModel() {
		super.setUpModel();
		RoomRecord rr = getModel().getObject();
		if (rr.getAudioAccessible() != null) {
			Optional<Resource> o = getResourceDBService().findWithDeps(rr.getAudioAccessible().getId());
			o.ifPresent(r -> setAudioAccessibleModel(new ObjectModel<Resource>(r)));
		}
	}

	protected boolean processAudioAccessibleUpload(List<FileUpload> uploads) {
		if (this.uploadedAudioAccessible)
			return false;
		if (uploads != null && !uploads.isEmpty()) {
			for (FileUpload upload : uploads) {
				try {
					String bucketName = ServerConstant.MEDIA_BUCKET;
					String objectName = getResourceDBService().normalizeFileName(org.apache.commons.compress.utils.FileNameUtils.getBaseName(upload.getClientFileName())) + "-" + getResourceDBService().newId();
					Resource resource = createAndUploadFile(upload.getInputStream(), bucketName, objectName, upload.getClientFileName(), upload.getSize(), false);
					setAudioAccessibleModel(new ObjectModel<Resource>(resource));
					getModel().getObject().setAudioAccessible(resource);
					uploadedAudioAccessible = true;
				} catch (Exception e) {
					uploadedAudioAccessible = false;
					error("Error saving file: " + e.getMessage());
				}
			}
		}
		return uploadedAudioAccessible;
	}

	protected void onAudioAccessibleRemove(AjaxRequestTarget target) {
		try {
			this.audioAccessibleModel = null;
			getModel().getObject().setAudioAccessible(null);
			this.uploadedAudioAccessible = false;
			target.add(this);
		} catch (Exception e) {
			logger.error(e);
			target.add(this);
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (audioAccessibleModel != null)
			audioAccessibleModel.detach();
		if (siteModel != null)
			siteModel.detach();
	}

	public IModel<Resource> getAudioAccessibleModel() {
		return audioAccessibleModel;
	}

	public void setAudioAccessibleModel(IModel<Resource> model) {
		this.audioAccessibleModel = model;
	}

	public IModel<Site> getSourceSiteModel() {
		return siteModel;
	}

	public void setSourceSiteModel(IModel<Site> model) {
		this.siteModel = model;
	}
}
