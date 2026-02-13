package dellemuse.serverapp.music;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
 
import org.apache.wicket.model.PropertyModel;
 
import org.apache.wicket.model.util.ListModel;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.ServerDBSettings;
import dellemuse.serverapp.artexhibitionguide.ArtExhibitionGuideEditor;
import dellemuse.serverapp.editor.DBObjectEditor;
import dellemuse.serverapp.editor.ObjectUpdateEvent;
import dellemuse.serverapp.editor.SimpleAlertRow;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.site.SiteInfoEditor;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.serverdb.util.MediaUtil;
import dellemuse.serverapp.service.LockService;
import dellemuse.serverapp.serverdb.model.Music;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.event.SimpleAjaxWicketEvent;
import io.wktui.event.SimpleWicketEvent;
import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.EditButtons;
import io.wktui.form.button.SubmitButton;
import io.wktui.form.field.ChoiceField;
import io.wktui.form.field.FileUploadSimpleField;
import io.wktui.form.field.TextAreaField;
import io.wktui.form.field.TextField;
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import wktui.base.InvisiblePanel;

public class MusicEditor extends DBObjectEditor<Music> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteInfoEditor.class.getName());

	private ChoiceField<ObjectState> objectStateField;

	private TextField<String> nameField;
	
	private FileUploadSimpleField<Resource> audioField;

	private TextAreaField<String> infoField;

	private String audioMeta;
	private List<ToolbarItem> x_list;
	
	
	/**
	 * @param id
	 * @param model
	 */
	public MusicEditor(String id, IModel<Music> model) {
		super(id, model);
	}
	
	

	
	
	@Override
	public List<ToolbarItem> getToolbarItems() {

		if (x_list != null)
			return x_list;

		x_list = new ArrayList<ToolbarItem>();

		AjaxButtonToolbarItem<Music> create = new AjaxButtonToolbarItem<Music>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
				fire(new MenuAjaxEvent(ServerAppConstant.action_music_edit, target));
			}

			@Override
			public IModel<String> getButtonLabel() {
				return getLabel("edit");
			}
		};
		create.setAlign(Align.TOP_LEFT);
		x_list.add(create);
		return x_list;
	}
	
	

	@Override
	public void onInitialize() {
		super.onInitialize();
		setUpModel();
				
		

		add(new InvisiblePanel("error"));

		Form<Music> form = new Form<Music>("musicForm", getModel());
		form.setOutputMarkupId(true);

		add(form);
		setForm(form);

		form.setFormState(FormState.VIEW);

		objectStateField = new ChoiceField<ObjectState>("state", new PropertyModel<ObjectState>(getModel(), "state"), getLabel("state")) {

			private static final long serialVersionUID = 1L;

			@Override
			public IModel<List<ObjectState>> getChoices() {
				return new ListModel<ObjectState>(getStates());
			}

			@Override
			protected String getDisplayValue(ObjectState value) {
				if (value == null)
					return null;
				return value.getLabel(getSessionUser().get().getLocale());
			}

			@Override
			protected String getIdValue(ObjectState value) {
				return String.valueOf(value.getId());
			}

		};
		form.add(objectStateField);

		infoField 	= new TextAreaField<String>("info", new PropertyModel<String>(getModel(), "info"), getLabel("info"), 10);
	
		nameField 	= new TextField<String>("name", new PropertyModel<String>(getModel(), "name"), getLabel("name"));

		audioField 	= new FileUploadSimpleField<Resource>("audio", getAudioModel(), getLabel("audio")) {

			private static final long serialVersionUID = 1L;

			protected boolean processFileUploads(List<FileUpload> uploads) {
				return MusicEditor.this.processAudioUpload(uploads);
			}

			public Image getImage() {
				return null;
			}

			protected String getAudioSrc() {

				if (getAudioModel() == null || getAudioModel().getObject() == null)
					return null;
				return MusicEditor.this.getPresignedUrl(getAudioModel().getObject());
			}

			public String getFileName() {
				if (audioMeta == null)
					audioMeta = MusicEditor.this.getAudioMeta(getAudioModel());
				return audioMeta;

			}
		};

		
	 

		form.add(nameField);
		form.add(infoField);
		form.add(audioField);

		
		AjaxLink<Void> importEx =new AjaxLink<Void>("extract") {

			public boolean isVisible() {
				return getForm().getFormState()==FormState.EDIT;
			}
			
			public boolean isEnabled() {
				return getForm().getFormState()==FormState.EDIT;
			}
			
			@Override
			public void onClick(AjaxRequestTarget target) {
				
				
				if (MusicEditor.this.getModel().getObject().getAudio()!=null) {

					Resource r = getResourceDBService().findById( MusicEditor.this.getModel().getObject().getAudio().getId() ).get();
					
					Map<String, String> map = extract(r);
					
					if (map!=null) {
						
					
					MusicEditor.this.getModel().getObject().setInfo(
							
							map.entrySet().stream()
						    .map(e -> e.getKey() + ": " + e.getValue()) 
						    .collect(Collectors.joining("\n"))
						    
							);
					MusicEditor.this.infoField.setValue(map.entrySet().stream()
						    .map(e -> e.getKey() + ": " + e.getValue()) 
						    .collect(Collectors.joining("\n")));
					MusicEditor.this.infoField.updateModel();
				
					
					if (map.containsKey("title")) {
						MusicEditor.this.getModel().getObject().setName(map.get("title"));
						MusicEditor.this.nameField.setValue(map.get("title"));
						MusicEditor.this.nameField.updateModel();
					
					}
					
				}
				target.add(MusicEditor.this);
					
				}
			}
		};
		
		form.add(importEx);
		
		
		
		EditButtons<Music> buttons = new EditButtons<Music>("buttons", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			public void onEdit(AjaxRequestTarget target) {
				MusicEditor.this.onEdit(target);
			}

			public void onCancel(AjaxRequestTarget target) {
				MusicEditor.this.onCancel(target);
			}

			public void onSave(AjaxRequestTarget target) {
				MusicEditor.this.onSave(target);
			}

			@Override
			public boolean isVisible() {
				
				if (!hasWritePermission())
					return false;
				
				return getForm().getFormState() == FormState.EDIT;
			}
		};

		getForm().add(buttons);

		EditButtons<Music> b_buttons_top = new EditButtons<Music>("buttons-top", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			public void onEdit(AjaxRequestTarget target) {
				MusicEditor.this.onEdit(target);
			}

			public void onCancel(AjaxRequestTarget target) {
				MusicEditor.this.onCancel(target);
			}

			public void onSave(AjaxRequestTarget target) {
				MusicEditor.this.onSave(target);
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

	private IModel<Resource> audioModel;
	private boolean uploadedAudio = false;
	
	
	
	@Override
	public void onDetach() {
		super.onDetach();
 
		
		if (this.audioModel!=null)
			this.audioModel.detach();
		
	}

	
	protected IModel<Resource> getAudioModel() {
		return this.audioModel;
	}

	protected void setAudioModel(ObjectModel<Resource> model) {
		this.audioModel = model;
	}
	
	
	 

	protected void onCancel(AjaxRequestTarget target) {
		getForm().setFormState(FormState.VIEW);
		target.add(getForm());
	}

	protected void onEdit(AjaxRequestTarget target) {
		super.edit(target);
		target.add(getForm());
	}

	protected void onSave(AjaxRequestTarget target) {

		try {
			save(getModelObject(), getSessionUser().get(), getUpdatedParts());

			uploadedAudio = false;

			getForm().setFormState(FormState.VIEW);

			getForm().updateReload();

			fireScanAll(new ObjectUpdateEvent(target));

		} catch (Exception e) {

			addOrReplace(new SimpleAlertRow<Void>("error", e));
			logger.error(e);

		}

		target.add(this);

	}
 

	
	protected boolean processAudioUpload(List<FileUpload> uploads) {

		if (this.uploadedAudio)
			return false;

		if (uploads != null && !uploads.isEmpty()) {

			for (FileUpload upload : uploads) {

				try {

					logger.debug("name -> " + upload.getClientFileName());
					logger.debug("Size -> " + upload.getSize());

					String bucketName = ServerConstant.MEDIA_BUCKET;
					String objectName = getResourceDBService().normalizeFileName(FileNameUtils.getBaseName(upload.getClientFileName())) + "-" + String.valueOf(getResourceDBService().newId());

					Resource resource = createAndUploadFile(upload.getInputStream(), bucketName, objectName, upload.getClientFileName(), upload.getSize());

					setAudioModel(new ObjectModel<Resource>(resource));
					getModel().getObject().setAudio(resource);

					
					uploadedAudio = true;

				} catch (Exception e) {
					uploadedAudio = false;
					error("Error saving file: " + e.getMessage());
				}
			}
		} else {
			info("No file uploaded.");
			logger.debug("No file uploaded.");
		}

		return uploadedAudio;
	}
	
	private void setUpModel() {

 		Music Music = getModel().getObject();
 
		getModel().setObject( getMusicDBService().findWithDeps(Music.getId()).get());
		
		 
		if (getModel().getObject().getAudio() != null) {
			Optional<Resource> o_r = getResourceDBService().findWithDeps(getModel().getObject().getAudio().getId());
			if (o_r.isPresent())
				setAudioModel(new ObjectModel<Resource>(o_r.get()));
		}
		

	}
	
	
	private  Map<String, String> extract(Resource resource) {
		
		File downloadedFile = null;
		
		try (InputStream is = getObjectStorageService().getObject(resource.getBucketName(), resource.getObjectName())) {
			
			if (is != null) {

					  downloadedFile = new File(getSettings().getWorkDir(), resource.getName());
					
					if (getLockService().getFileLock(downloadedFile.getAbsolutePath()).writeLock().tryLock(30, TimeUnit.SECONDS)) {
						
						try {
							try (InputStream in = getObjectStorageService().getClient().getObject(resource.getBucketName(),
									resource.getObjectName())) {
								Files.copy(in, downloadedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
							}
							
							if (FilenameUtils.getExtension( downloadedFile .getName()).equals("mp3")) {
								return Mp3MetadataExtractor.extractMetadata(downloadedFile);
							}
							
							
						} finally {
							getLockService().getFileLock(downloadedFile.getAbsolutePath()).writeLock().unlock();
						}
					} else {
						logger.error("lock not working");
					}
			}
		} catch (Exception e) {
				logger.error(e, resource.getDisplayname()+ "  | " + resource.getMedia(), ServerConstant.NOT_THROWN);
				return null;
				 
		} finally {
			if ((downloadedFile != null) && downloadedFile.exists()) {
				try {
					FileUtils.forceDelete(downloadedFile);
				} catch (IOException e) {
					logger.error(e,  downloadedFile.getName(), ServerConstant.NOT_THROWN);
				}
			}
		}
		
		return null;
		
		
		
		
		
		
		
		
		
		
	}
	public ServerDBSettings getSettings() {
		return (ServerDBSettings) ServiceLocator.getInstance().getBean(ServerDBSettings.class);

	}
	

	public LockService getLockService() {
		return  (LockService) ServiceLocator.getInstance().getBean(LockService.class);
	}
	
	
}
