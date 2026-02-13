package dellemuse.serverapp.voice;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
 
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
 
import org.apache.wicket.model.PropertyModel;
 
import org.apache.wicket.model.util.ListModel;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
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
import dellemuse.serverapp.serverdb.model.Voice;
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

public class VoiceEditor extends DBObjectEditor<Voice> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteInfoEditor.class.getName());

	private ChoiceField<ObjectState> objectStateField;

	private TextField<String> nameField;
	private TextField<String> voiceIdField;
	private TextField<String> languageField;
	private TextField<String> sexField;
	private TextField<String> languageRegionField;

	
	private FileUploadSimpleField<Resource> audioField;
	private FileUploadSimpleField<Resource> photoField;
	private IModel<Resource> photoModel;

	private TextAreaField<String> infoField;

 
	private String audioMeta;
	private List<ToolbarItem> x_list;
	
	
	/**
	 * @param id
	 * @param model
	 */
	public VoiceEditor(String id, IModel<Voice> model) {
		super(id, model);
	}
	
	private void setUpModel() {

 		Voice voice = getModel().getObject();
 
		getModel().setObject( getVoiceDBService().findWithDeps(voice.getId()).get());
		
		 
		if (getModel().getObject().getAudio() != null) {
			Optional<Resource> o_r = getResourceDBService().findWithDeps(getModel().getObject().getAudio().getId());
			if (o_r.isPresent())
				setAudioModel(new ObjectModel<Resource>(o_r.get()));
		}
		

	}
	

	
	
	@Override
	public List<ToolbarItem> getToolbarItems() {

		if (x_list != null)
			return x_list;

		x_list = new ArrayList<ToolbarItem>();

		AjaxButtonToolbarItem<Voice> create = new AjaxButtonToolbarItem<Voice>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
				fire(new MenuAjaxEvent(ServerAppConstant.action_voice_edit, target));
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

		Form<Voice> form = new Form<Voice>("voiceForm", getModel());
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
				return VoiceEditor.this.processAudioUpload(uploads);
			}

			public Image getImage() {
				return null;
			}

			protected String getAudioSrc() {

				if (getAudioModel() == null || getAudioModel().getObject() == null)
					return null;
				return VoiceEditor.this.getPresignedUrl(getAudioModel().getObject());
			}

			public String getFileName() {
				if (audioMeta == null)
					audioMeta = VoiceEditor.this.getAudioMeta(getAudioModel());
				return audioMeta;

			}
		};

		
		voiceIdField  = new TextField<String>("voiceid", new PropertyModel<String>(getModel(), "voiceId"), getLabel("voiceid"));
		form.add(voiceIdField);
		
		languageField = new TextField<String>("language", new PropertyModel<String>(getModel(), "name"), getLabel("name"));
		form.add(languageField);

		sexField = new TextField<String>("sex", new PropertyModel<String>(getModel(), "sex"), getLabel("sex"));
		form.add(sexField);

		
		languageRegionField  = new TextField<String>("languageRegion", new PropertyModel<String>(getModel(), "languageRegion"), getLabel("languageRegion"));
		form.add(languageRegionField);

		
	 

		form.add(nameField);
		form.add(infoField);
		form.add(audioField);

		
		EditButtons<Voice> buttons = new EditButtons<Voice>("buttons", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			public void onEdit(AjaxRequestTarget target) {
				VoiceEditor.this.onEdit(target);
			}

			public void onCancel(AjaxRequestTarget target) {
				VoiceEditor.this.onCancel(target);
			}

			public void onSave(AjaxRequestTarget target) {
				VoiceEditor.this.onSave(target);
			}

			@Override
			public boolean isVisible() {
				
				if (!hasWritePermission())
					return false;
				
				return getForm().getFormState() == FormState.EDIT;
			}
		};

		getForm().add(buttons);

		EditButtons<Voice> b_buttons_top = new EditButtons<Voice>("buttons-top", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			public void onEdit(AjaxRequestTarget target) {
				VoiceEditor.this.onEdit(target);
			}

			public void onCancel(AjaxRequestTarget target) {
				VoiceEditor.this.onCancel(target);
			}

			public void onSave(AjaxRequestTarget target) {
				VoiceEditor.this.onSave(target);
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

		if (this.photoModel != null)
			this.photoModel.detach();
		
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

	protected IModel<Resource> getPhotoModel() {
		return this.photoModel;
	}

	protected void setPhotoModel(ObjectModel<Resource> model) {
		this.photoModel = model;
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
	
}
