package dellemuse.serverapp.editor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
 

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.editor.DBObjectEditor;
import dellemuse.serverapp.guidecontent.GuideContentEditor;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.DelleMuseObject;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.record.ArtWorkRecord;
import dellemuse.serverapp.serverdb.model.record.TranslationRecord;
import dellemuse.serverapp.serverdb.service.DBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.DateTimeService;
import io.wktui.error.ErrorPanel;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.EditButtons;
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

	private TextField<String> 				nameField;
	private TextAreaField<String> 				opensField;

	private TextField<String> 				subtitleField;
	private TextAreaField<String> 			infoField;
	private TextAreaField<String> 			introField;
	private TextAreaField<String> 			specField;
	private FileUploadSimpleField<Resource> audioField;
	
	private IModel<Resource> audioModel;
	private boolean uploadedAudio = false;
	
	private AjaxLink<R> translate;
	private Label t_label;
	
	private IModel<T> sourceModel;
	
	/**
	 * @param id
	 * @param model
	 */
	public ObjectRecordEditor(String id, IModel<T> sourceModel, IModel<R> TranslationRecordModel) {
		super(id, TranslationRecordModel);
		 
		this.sourceModel=sourceModel;
		
	}

	 
	
	@Override
	public List<ToolbarItem> getToolbarItems() {
		
	List<ToolbarItem> list = new ArrayList<ToolbarItem>();
		
		AjaxButtonToolbarItem<Person> create = new AjaxButtonToolbarItem<Person>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
 				fire(new MenuAjaxEvent(ServerAppConstant.action_object_edit_record, target,  ObjectRecordEditor.this.getModel().getObject().getLanguage()));
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
	
	
	protected Locale getUserLocale() {
		return getSessionUser().getLocale();
	}
	
	
	private void setUpModel() {
		@SuppressWarnings("unchecked")
		Optional<R> o_i = (Optional<R>) getDBService(getModelObject().getClass()).findWithDeps(getModel().getObject().getId());
		setModel(new ObjectModel<R>(o_i.get()));
		
		
		if (getModel().getObject().getAudio() != null) {
			Optional<Resource> o_r = getResourceDBService().findWithDeps(getModel().getObject().getAudio().getId());
			setAudioModel(new ObjectModel<Resource>(o_r.get()));
		}
		
		
		
		
		
	}
	
	@Override
	public void onBeforeRender() {
		super.onBeforeRender();
		
		 
		
	}
	 
	
	
	private void loadForm() {
		
		Form<R> form = new Form<R>("form");

		addOrReplace(form);
		setForm(form);
	
		this.translate = new AjaxLink<R> ( "translate", getModel()) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				ObjectRecordEditor.this.onTranslate(target);
			}
			
			@Override
			public boolean isVisible() {
				return getForm().getFormState() == FormState.EDIT;
			}
		};
		
		form.add(this.translate);
		form.add(new InvisiblePanel("alert"));
		
		//getSourceModel().getObject().getMasterLanguage()
		
		Locale langLocale = Locale.forLanguageTag(getSourceModel().getObject().getMasterLanguage());
		Locale userLocale = getSessionUser().getLocale();
		
		t_label = new Label("translatesrc", getLabel( "translate-button-text",  langLocale.getDisplayLanguage(userLocale)));
		this.translate.add(t_label);
		
		// --------
		//
		//Label artWorkRecordInfo = new Label("artWorkRecordInfo", getLabel("artwork-record-info", getModel().getObject().getLanguage()));
		//form.add(artWorkRecordInfo);
		//
		// --------

		this.nameField  	= new TextField<String>					("name", 		new PropertyModel<String>(getModel(), "name"), getLabel("name")			);
		this.subtitleField  = new TextField<String>					("subtitle", 	new PropertyModel<String>(getModel(), "subtitle"), getLabel("subtitle")	);
		
		this.introField  	= new TextAreaField<String>				("intro", 		new PropertyModel<String>(getModel(), "intro"), getLabel("intro"), 6		);
		this.infoField  	= new TextAreaField<String>				("info", 		new PropertyModel<String>(getModel(), "info"), getLabel("info"), 20		);

		this.opensField  	= new TextAreaField<String>				("opens", 		new PropertyModel<String>(getModel(), "opens"), getLabel("opens"), 4		);

	
		this.specField  	= new TextAreaField<String>				("spec", 		new PropertyModel<String>(getModel(), "spec"), getLabel("spec"), 6		);

		
		this.infoField.setVisible( isInfoVisible());
		this.specField.setVisible( isSpecVisible());
		this.introField.setVisible( isIntroVisible());
		this.opensField.setVisible( isOpensVisible());
		
		
		form.add(nameField);
		form.add(subtitleField);

		form.add(introField);
		form.add(infoField);
		form.add(specField);
		form.add(opensField);
		
		// this.specField  	= new TextAreaField<String>	("spec", new PropertyModel<String>(getModel(), "spec"), getLabel("spec"), 8					);
		//form.add(specField);

		
		audioField = new FileUploadSimpleField<Resource>("audio", getAudioModel(), getLabel("audio")) {

			private static final long serialVersionUID = 1L;

			protected boolean processFileUploads(List<FileUpload> uploads) {
				return ObjectRecordEditor.this.processAudioUpload(uploads);
			}

			public Image getImage() {
				return null;
			}

			
			protected String getAudioSrc() {
			
				if (getAudioModel() == null || getAudioModel().getObject()==null )
					return null;
				return ObjectRecordEditor.this.getPresignedUrl(getAudioModel().getObject());
			}
			
			public String getFileName() {
				if (getAudioModel()!=null  || getAudioModel().getObject()==null )
					return ObjectRecordEditor.this.getAudioMeta( getAudioModel().getObject() );
				return null;
			}

		};
		
		
		audioField.setVisible(isAudioVisible());
		form.add(audioField);
		
		
		
		
		Label title = new Label("recordTitle", getLabel( "translate-information", getModel().getObject().getLanguage()));
		form.add(title);
				
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
	
	
	protected boolean processAudioUpload(List<FileUpload> uploads) {

		if (this.uploadedAudio)
			return false;

		if (uploads != null && !uploads.isEmpty()) {

			for (FileUpload upload : uploads) {
			
				try {

					logger.debug("name -> " + upload.getClientFileName());
					logger.debug("Size -> " + upload.getSize());

					String bucketName = ServerConstant.MEDIA_BUCKET;
					String objectName = getResourceDBService()
							.normalizeFileName(FileNameUtils.getBaseName(upload.getClientFileName())) + "-"
							+ String.valueOf(getResourceDBService().newId());

					Resource resource = createAndUploadFile(upload.getInputStream(), bucketName, objectName,
							upload.getClientFileName(), upload.getSize());

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

	
	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();
	    loadForm();
	 	
	    // --
	    //
	    // if (getModel().getObject().getName()==null)
		//  	 getModel().getObject().setName( getArtWorkModel().getObject().getName());
		//
	    // --
	    
		}

	
	
	
	/**
	 * 
	 * 
	 * 
	 * @param target
	 */
	protected void onTranslate(AjaxRequestTarget target) {
		
		boolean success = getTranslationService().translate(getSourceModel().getObject(), getModel().getObject());
		
		if (success)  {
			
			//logger.debug("name -> " + d_name);
			//logger.debug("subtitle -> " + d_subtitle);
			//logger.debug("info -> " + d_info);
		
			
			logger.debug( getModelObject().getName() );
			logger.debug( getModelObject().getSubtitle() );

			logger.debug( getModelObject().getIntro() );
			logger.debug( getModelObject().getInfo() );
			
			
			
			save(getModelObject());
			loadForm();
		}
		else {
				
			logger.debug(getModel().getObject().getLastModified().toString());
			
			String date=getDateTimeService().format(getModel().getObject().getLastModified());
			
			io.wktui.error.AlertPanel<R> alert=new io.wktui.error.AlertPanel<R>("alert", 
						io.wktui.error.AlertPanel.INFO, 
						null, 
						getModel(), 
						null,
						getLabel("translated-on", date));
				
			getForm().addOrReplace(alert);
		}
		
		target.add(this);
	}
		
	public Optional<Person> getPerson(Long value) {
		return super.getPerson(value);
	}

	public void onCancel(AjaxRequestTarget target) {
		super.cancel(target);
		// getForm().setFormState(FormState.VIEW);
		// target.add(getForm());
	}

	public void onEdit(AjaxRequestTarget target) {
		super.edit(target);
		// getForm().setFormState(FormState.EDIT);
		// target.add(getForm());
	}

	public void onSave(AjaxRequestTarget target) {
		logger.debug("onSave");
		logger.debug("updated parts:");
		getUpdatedParts().forEach(s -> logger.debug(s));
		logger.debug("saving...");
	
		save(getModelObject());
	 
		getForm().setFormState(FormState.VIEW);
		logger.debug("done");
		target.add(this);
	
		// ---
		// TODO AT
		// fir e( ne w (target));
		// --
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (sourceModel!=null)
			this.sourceModel.detach();
		
		
		if (audioModel != null)
			audioModel.detach();
		
	}
 
	protected void onSubmit() {
		logger.debug("");
		logger.debug("onSubmit");
		logger.debug("");
	}


	public void save(R modelObject) {
		 getDBService(modelObject.getClass() ).saveViaBaseClass((DelleMuseObject) modelObject);
	}
	
	private DBService<?,Long> getDBService(Class<? extends DelleMuseObject> clazz) {
		return  DBService.getDBService(clazz);
	}

	public IModel<T> getSourceModel() {
		return sourceModel;
	}

	public void setSourceModel(IModel<T> sourceModel) {
		this.sourceModel = sourceModel;
	}

 	boolean isInfoVisible = false;
 	boolean isOpensVisible = false;
 	boolean isSpecVisible = false;
	boolean isIntroVisible = false;
	boolean isAudioVisible = false;

	
	
	public void setAudioVisible(boolean introVisible) {
		this.isAudioVisible=introVisible;
	}
	
	public void setInfoVisible(boolean introVisible) {
		this.isInfoVisible=introVisible;
	}
	
	public void setOpensVisible(boolean introVisible) {
		this.isOpensVisible=introVisible;
	}

	
	public void setIntroVisible(boolean introVisible) {
		this.isIntroVisible=introVisible;
	}

	public void setSpecVisible(boolean introVisible) {
		this.isSpecVisible=introVisible;
	}

	
}
