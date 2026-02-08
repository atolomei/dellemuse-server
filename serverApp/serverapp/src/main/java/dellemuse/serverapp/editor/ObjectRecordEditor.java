package dellemuse.serverapp.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.audiostudio.AudioStudioPage;
import dellemuse.serverapp.audit.AuditKey;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.AudioStudio;
import dellemuse.serverapp.serverdb.model.DelleMuseObject;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.TranslationRecord;
import dellemuse.serverapp.serverdb.service.DBService;
import io.wktui.error.ErrorPanel;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.EditButtons;
import io.wktui.form.field.ChoiceField;
import io.wktui.form.field.FileUploadSimpleField;
import io.wktui.form.field.TextAreaField;
import io.wktui.form.field.TextField;
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;

import wktui.base.InvisiblePanel;

public class ObjectRecordEditor<T extends MultiLanguageObject, R extends TranslationRecord> extends DBObjectEditor<R> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ObjectRecordEditor.class.getName());

	private TextField<String> nameField;
	private TextAreaField<String> opensField;
	private TextField<String> subtitleField;
	private TextAreaField<String> infoField;
	private TextAreaField<String> introField;
	private TextAreaField<String> specField;
	private FileUploadSimpleField<Resource> audioField;

	
	private AjaxLink<R> translate;
	private Label t_label;
	private IModel<T> sourceModel;
	
	private IModel<ArtExhibitionGuide> artExhibitionGuideModel;
	
	private IModel<Resource> audioModel;
	private boolean uploadedAudio = false;
	
	private Link<R> openAudioStudio;

	private boolean isInfoVisible = false;
	private boolean isOpensVisible = false;
	private boolean isSpecVisible = false;
	private boolean isIntroVisible = false;
	private boolean isAudioVisible = false;

	/**
	 * @param id
	 * @param model
	 */
	public ObjectRecordEditor(String id, IModel<T> sourceModel, IModel<R> TranslationRecordModel) {
		super(id, TranslationRecordModel);
		this.sourceModel = sourceModel;
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();
		
		add(new InvisiblePanel("error"));
		loadForm();
	}

	@Override
	public List<ToolbarItem> getToolbarItems() {

		List<ToolbarItem> list = new ArrayList<ToolbarItem>();

		AjaxButtonToolbarItem<Person> create = new AjaxButtonToolbarItem<Person>() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return isMultiLanguageObject();
			}

			@Override
			protected void onCick(AjaxRequestTarget target) {
				fire(new MenuAjaxEvent(ServerAppConstant.action_object_edit_record, target, ObjectRecordEditor.this.getModel().getObject().getLanguage()));
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

	protected boolean isAudioStudio() {
		return ((getSourceModel().getObject() instanceof GuideContent) || (getSourceModel().getObject() instanceof ArtExhibitionGuide));
	}

	protected boolean isMultiLanguageObject() {
		return (getSourceModel().getObject() instanceof MultiLanguageObject);
	}

	public Optional<Person> getPerson(Long value) {
		return super.getPerson(value);
	}

	public void onCancel(AjaxRequestTarget target) {
		super.cancel(target);
		getForm().setFormState(FormState.VIEW);
		getForm().addOrReplace(new InvisiblePanel("error"));
		target.add(this);
	}

	public void onEdit(AjaxRequestTarget target) {
		super.edit(target);
		// getForm().setFormState(FormState.EDIT);
		// target.add(getForm());
	}

	public void onSave(AjaxRequestTarget target) {

		try {
			getUpdatedParts().forEach(s -> logger.debug(s));
			save(getModelObject(), getSessionUser().get(), getUpdatedParts());
			getForm().setFormState(FormState.VIEW);
		} catch (Exception e) {
			addOrReplace(new SimpleAlertRow<Void>("error", e));
			logger.error(e);
		}
		target.add(this);
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (sourceModel != null)
			this.sourceModel.detach();

		if (audioModel != null)
			audioModel.detach();
		
		if (this.artExhibitionGuideModel!=null)
			this.artExhibitionGuideModel.detach();
		
	}

	public IModel<T> getSourceModel() {
		return sourceModel;
	}

	public void setSourceModel(IModel<T> sourceModel) {
		this.sourceModel = sourceModel;
	}

	public void setAudioVisible(boolean introVisible) {
		this.isAudioVisible = introVisible;
	}

	public void setInfoVisible(boolean introVisible) {
		this.isInfoVisible = introVisible;
	}

	public void setOpensVisible(boolean introVisible) {
		this.isOpensVisible = introVisible;
	}

	public void setIntroVisible(boolean introVisible) {
		this.isIntroVisible = introVisible;
	}

	public void setSpecVisible(boolean introVisible) {
		this.isSpecVisible = introVisible;
	}

	public void save(R modelObject, User user, String auditMsg) {
		getDBService(modelObject.getClass()).saveViaBaseClass((DelleMuseObject) modelObject, user, auditMsg);
	}

	protected boolean isAudioStudioEnabled(R o) {
		return o.isAudioStudioEnabled();
	}

	protected Locale getUserLocale() {
		return getSessionUser().get().getLocale();
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

	protected IModel<Resource> getAudioModel() {
		return this.audioModel;
	}

	protected void setAudioModel(ObjectModel<Resource> model) {
		this.audioModel = model;
	}

	public IModel<ArtExhibitionGuide> getArtExhibitionGuideModel() {
		return artExhibitionGuideModel;
	}

	public void setArtExhibitionGuideModel(IModel<ArtExhibitionGuide> artExhibitionGuideModel) {
		this.artExhibitionGuideModel = artExhibitionGuideModel;
	}

	protected boolean isIntroVisible() {
		return this.isIntroVisible;
	}

	protected boolean isSpecVisible() {
		return this.isSpecVisible;
	}

	protected boolean isInfoVisible() {
		return this.isInfoVisible;
	}

	protected boolean isOpensVisible() {
		return this.isOpensVisible;
	}

	protected boolean isAudioVisible() {
		return this.isAudioVisible;
	}

	/**
	 * @param target
	 */
	protected void onTranslate(AjaxRequestTarget target) {

		try {
			boolean success = getTranslationService().translate(getSourceModel().getObject(), getModel().getObject());

			if (success) {
				save(getModelObject(), getSessionUser().get(), AuditKey.TRANSLATE);
				loadForm();

			} else {
				String date = getDateTimeService().format(getModel().getObject().getLastModified());
				io.wktui.error.AlertPanel<R> alert = new io.wktui.error.AlertPanel<R>("alert", io.wktui.error.AlertPanel.INFO, null, getModel(), null, getLabel("translated-on", date));
				getForm().addOrReplace(alert);
			}

			target.add(this);

		} catch (Exception e) {
			loadForm();
			getForm().addOrReplace(new ErrorPanel("error", e));
			target.add(this);
		}
	}

	protected void onSubmit() {
		logger.debug("");
		logger.debug("onSubmit");
		logger.debug("");
	}

	private DBService<?, Long> getDBService(Class<? extends DelleMuseObject> clazz) {
		return DBService.getDBService(clazz);
	}

	protected void setUpModel() {
		@SuppressWarnings("unchecked")
		Optional<R> o_i = (Optional<R>) getDBService(getModelObject().getClass()).findWithDeps(getModel().getObject().getId());
		setModel(new ObjectModel<R>(o_i.get()));
		
		if (getModel().getObject().getAudio() != null) {
			Optional<Resource> o_r = getResourceDBService().findWithDeps(getModel().getObject().getAudio().getId());
			setAudioModel(new ObjectModel<Resource>(o_r.get()));
		}

		
		
		
		
	}

	protected void loadForm() {

		Form<R> form = new Form<R>("form");

		form.setOutputMarkupId(true);
		addOrReplace(form);
		setForm(form);

		this.translate = new AjaxLink<R>("translate", getModel()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				ObjectRecordEditor.this.onTranslate(target);
			}

			@Override
			public boolean isVisible() {

				if (!hasWritePermission())
					return false;

				return getForm().getFormState() == FormState.EDIT;
			}
		};

		form.add(this.translate);
		form.add(new InvisiblePanel("alert"));

		Locale langLocale = Locale.forLanguageTag(getSourceModel().getObject().getMasterLanguage());
		Locale userLocale = getSessionUser().get().getLocale();

		t_label = new Label("translatesrc", getLabel("translate-button-text", langLocale.getDisplayLanguage(userLocale)));
		this.translate.add(t_label);

		this.nameField = new TextField<String>("name", new PropertyModel<String>(getModel(), "name"), getLabel("name"));
		this.subtitleField = new TextField<String>("subtitle", new PropertyModel<String>(getModel(), "subtitle"), getLabel("subtitle"));
		this.introField = new TextAreaField<String>("intro", new PropertyModel<String>(getModel(), "intro"), getLabel("intro"), 6);
		this.infoField = new TextAreaField<String>("info", new PropertyModel<String>(getModel(), "info"), getLabel("info"), 20);
		this.opensField = new TextAreaField<String>("opens", new PropertyModel<String>(getModel(), "opens"), getLabel("opens"), 4);
		this.specField = new TextAreaField<String>("spec", new PropertyModel<String>(getModel(), "spec"), getLabel("spec"), 6);

		this.infoField.setVisible(isInfoVisible());
		this.specField.setVisible(isSpecVisible());
		this.introField.setVisible(isIntroVisible());
		this.opensField.setVisible(isOpensVisible());

		form.add(nameField);
		form.add(subtitleField);

		form.add(introField);
		form.add(infoField);
		form.add(specField);
		form.add(opensField);

		audioField = new FileUploadSimpleField<Resource>("audio", getAudioModel(), getLabel("audio")) {

			private static final long serialVersionUID = 1L;

			protected boolean processFileUploads(List<FileUpload> uploads) {
				return ObjectRecordEditor.this.processAudioUpload(uploads);
			}

			protected void onRemove(AjaxRequestTarget target) {
				ObjectRecordEditor.this.onAudioRemove(target);
			}

			public Image getImage() {
				return null;
			}

			protected String getAudioSrc() {

				if (getAudioModel() == null || getAudioModel().getObject() == null)
					return null;
				return ObjectRecordEditor.this.getPresignedUrl(getAudioModel().getObject());
			}

			public String getFileName() {
				if (getAudioModel() != null || getAudioModel().getObject() == null)
					return ObjectRecordEditor.this.getAudioMeta(getAudioModel().getObject());
				return null;
			}

		};

		audioField.setVisible(isAudioVisible());
		form.add(audioField);

		Label title = new Label("recordTitle", getLabel("translate-information", getModel().getObject().getLanguage()));
		form.add(title);

		this.openAudioStudio = new Link<R>("openAudioStudio", getModel()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				Optional<AudioStudio> oa = getAudioStudioDBService().findOrCreate(getModel().getObject(), getSessionUser().get());
				if (oa.isPresent())
					openAudioStudio(new ObjectModel<AudioStudio>(oa.get()) );
				
				}

			@Override
			public boolean isVisible() {
				return isAudioStudioEnabled(getModel().getObject());
			}
		};

		Label openArtworkLabel = new Label("openAudioStudioLabel", getLabel("open-audio-studio", getModel().getObject().getDisplayname()));
		this.openAudioStudio.add(openArtworkLabel);
		form.add(openAudioStudio);

		EditButtons<R> buttons = new EditButtons<R>("buttons-bottom", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onEdit(AjaxRequestTarget target) {
				ObjectRecordEditor.this.onEdit(target);
			}

			@Override
			public void onCancel(AjaxRequestTarget target) {
				ObjectRecordEditor.this.onCancel(target);
			}

			@Override
			public void onSave(AjaxRequestTarget target) {
				ObjectRecordEditor.this.onSave(target);
			}

			@Override
			public boolean isVisible() {
				if (!hasWritePermission())
					return false;

				return getForm().getFormState() == FormState.EDIT;
			}
		};
		
		form.add(buttons);
		EditButtons<R> b_buttons_top = new EditButtons<R>("buttons-top", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			public void onEdit(AjaxRequestTarget target) {
				ObjectRecordEditor.this.onEdit(target);
			}

			public void onCancel(AjaxRequestTarget target) {
				ObjectRecordEditor.this.onCancel(target);
			}

			public void onSave(AjaxRequestTarget target) {
				ObjectRecordEditor.this.onSave(target);
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
		getForm().addOrReplace(new InvisiblePanel("error"));

	}

	protected void openAudioStudio(ObjectModel<AudioStudio> objectModel) {
			setResponsePage(new AudioStudioPage(objectModel, isAccesible() ));
	}

	protected boolean isAccesible() {
		if (getSourceModel().getObject() instanceof ArtExhibitionGuide) {
			return ( (ArtExhibitionGuide) getSourceModel().getObject()).isAccessible();
		}
		return false;
	}

	
	protected void onAudioRemove(AjaxRequestTarget target) {
		try {
			this.audioModel=null;
			getModel().getObject().setAudio(null);
			this.uploadedAudio = false;
			target.add(this);
		} catch (Exception e) {
			loadForm();
			getForm().addOrReplace(new ErrorPanel("error", e));
			target.add(this);
		}
	}
}
