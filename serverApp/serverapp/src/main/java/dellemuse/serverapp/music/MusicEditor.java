package dellemuse.serverapp.music;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
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
import org.aspectj.util.FileUtil;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.FSUtil;
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

	private ChoiceField<MusicGenre> genreField;
	
	private FileUploadSimpleField<Resource> audioField;

	private TextAreaField<String> infoField;
	private TextAreaField<String>  urlField;
	private TextAreaField<String> licenseField;

	
	private TextAreaField<String> technicalInfoField;

	
	private ChoiceField<Boolean> royaltyFreeField;
	
	
	private String audioMeta;
	private List<ToolbarItem> x_list;
	
	private static Map<String, String> T_KEYS = new ConcurrentHashMap<String, String>();
	
	static  {
		
		T_KEYS.put("bitrate", "bitrate");
		T_KEYS.put("durationSeconds", "durationSeconds");
		T_KEYS.put("audio format", "audio format");
		T_KEYS.put("size", "size");
		T_KEYS.put("format", "format");
		T_KEYS.put("track", "track");
		T_KEYS.put("tsampleRate", "sampleRate");
		T_KEYS.put("audioFormat", "audioFormat");
	}

	
private static Map<String, String> I_KEYS = new ConcurrentHashMap<String, String>();
	
	static  {
		
		I_KEYS.put("composer", "composer");
		I_KEYS.put("year", "year");
		I_KEYS.put("artist", "artist");
		I_KEYS.put("album", "album");
	}
	
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
			
			@Override
			public boolean isVisible() {
				return isRoot() || isGeneralAdmin();
				
			}
			
			@Override
			public boolean isEnabled() {
				return isRoot() || isGeneralAdmin();
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

	 
		royaltyFreeField = new ChoiceField<Boolean>("royaltyfree", new PropertyModel<Boolean>(getModel(), "royaltyFree"), getLabel("royaltyfree")) {

			private static final long serialVersionUID = 1L;

			@Override
			public IModel<List<Boolean>> getChoices() {
				return new ListModel<Boolean>(b_list);
			}

			@Override
			protected String getDisplayValue(Boolean value) {
				if (value == null)
					return null;
				if (value.booleanValue())
					return getLabel("yes").getObject();
				return getLabel("no").getObject();
			}
		};
		getForm().add(royaltyFreeField);
		
		

		genreField = new ChoiceField<MusicGenre>("genre", new PropertyModel<MusicGenre>(getModel(), "genre"), getLabel("genre")) {

			private static final long serialVersionUID = 1L;

			@Override
			public IModel<List<MusicGenre>> getChoices() {
				return new ListModel<MusicGenre>(MusicGenre.getValues());
			}

			@Override
			protected String getDisplayValue(MusicGenre value) {
				if (value == null)
					return null;
				 	return value.getLabel(getLocale());
				 
			}
		};
		getForm().add(genreField);
		
		
		
		infoField 	= new TextAreaField<String>("info", new PropertyModel<String>(getModel(), "info"), getLabel("info"), 10);
		urlField 	= new TextAreaField<String>("url", new PropertyModel<String>(getModel(), "url"), getLabel("url"), 3);
		licenseField = new TextAreaField<String>("license", new PropertyModel<String>(getModel(), "license"), getLabel("license"), 3);
		
		technicalInfoField = new TextAreaField<String>("technicalinfo", new PropertyModel<String>(getModel(), "technicalInfo"), getLabel("technicalinfo"), 5);
		form.add(technicalInfoField);
		
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
		
		form.add(licenseField);
		form.add(urlField);
		
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
				 
						String info = map.entrySet().stream()
							    .filter( e -> I_KEYS.containsKey(e.getKey()))
								.map(e -> e.getKey()+ ". " + e.getValue())   
							    .collect(Collectors.joining("\n"));

						MusicEditor.this.getModel().getObject().setInfo(info);
						MusicEditor.this.infoField.setValue(info);
						MusicEditor.this.infoField.updateModel();
					
						
						

						String t_info = map.entrySet().stream()
							    .filter( e -> T_KEYS.containsKey(e.getKey()))
								.map(e -> e.getKey()+ ". " + e.getValue()) 
							    .collect(Collectors.joining("\n"));

						MusicEditor.this.getModel().getObject().setTechnicalInfo(t_info);
						MusicEditor.this.technicalInfoField.setValue(t_info);
						MusicEditor.this.technicalInfoField.updateModel();

						
						
						if (map.containsKey("title")) {
							MusicEditor.this.getModel().getObject().setName(map.get("title").replace("-", " ").replace("_", " "));
							MusicEditor.this.nameField.setValue(map.get("title"));
							MusicEditor.this.nameField.updateModel();
						}
						if (map.containsKey("copyright")) {
							MusicEditor.this.getModel().getObject().setLicense(map.get("copyright"));
							MusicEditor.this.licenseField.setValue(map.get("copyright"));
							MusicEditor.this.licenseField.updateModel();
						}
						if (map.containsKey("url")) {
							MusicEditor.this.getModel().getObject().setUrl(map.get("url"));
							MusicEditor.this.urlField.setValue(map.get("url"));
							MusicEditor.this.urlField.updateModel();
						}
						else if (map.containsKey("comment") && map.get("comment").contains("http")) {
							MusicEditor.this.getModel().getObject().setUrl(map.get("comment"));
							MusicEditor.this.urlField.setValue(map.get("comment"));
							MusicEditor.this.urlField.updateModel();
						}
						
						
						if (MusicEditor.this.getModel().getObject().getUrl()!=null && MusicEditor.this.getModel().getObject().getUrl().toLowerCase().contains("wikimedia.org")) {
							MusicEditor.this.getModel().getObject().setRoyaltyFree(true);
							MusicEditor.this.royaltyFreeField.setValue(true);
							MusicEditor.this.royaltyFreeField.updateModel();
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

					Resource resource = createAndUploadFile(upload.getInputStream(), bucketName, objectName, upload.getClientFileName(), upload.getSize(), true);

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
							
							if (	FSUtil.isAudio( downloadedFile .getName())) { 	 
									Map<String, String> m1 = AudioRightsMetadataExtractor.extract(downloadedFile); 
									Map<String, String> m2 = AudioFileMetadataExtractor.extractMetadata(downloadedFile); 
									m2.forEach( (k,v) -> m1.put( k,v));
									return m1; 
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
