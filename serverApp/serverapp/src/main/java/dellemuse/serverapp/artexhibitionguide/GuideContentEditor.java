package dellemuse.serverapp.artexhibitionguide;

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
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.event.SimpleAjaxWicketEvent;
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

/**
 * 
 * alter table artexhibition add column spec text;
 * 
 */
public class GuideContentEditor extends DBObjectEditor<GuideContent> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(GuideContentEditor.class.getName());

	 

	private ChoiceField<ObjectState> objectStateField;
	
	private TextField<String> nameField;
	private TextField<String> shortField;
	private TextField<String> subtitleField;
	private TextField<String> urlField;
	private TextAreaField<String> specField;
	private TextAreaField<String> locationField;
	private TextAreaField<String> infoField;
	private TextAreaField<String> introField;
	
	//private FileUploadSimpleField<Resource> photoField;
	private FileUploadSimpleField<Resource> audioField;
	
	private TextField<String> mapField;
	private TextField<String> fromField;
	private TextField<String> toField;
	private ChoiceField<Person> artistField;
	private ChoiceField<Boolean> c_useThumbnailField;
	
	private IModel<Resource> photoModel;
	private IModel<Resource> audioModel;
	
	private boolean uploadedPhoto = false;
	private boolean uploadedAudio = false;

	private IModel<Site> siteModel;
	private IModel<ArtExhibition> artExhibitionModel;
	private IModel<ArtExhibitionGuide> artExhibitionGuideModel;
	private IModel<ArtExhibitionItem> artExhibitionItemModel;
	
	/**
	 * @param id
	 * @param model
	 */
	public GuideContentEditor(String id, 	IModel<GuideContent> model, 
											IModel<ArtExhibitionGuide> artExhibitionGuideModel, 
											IModel<ArtExhibition> artExhibitionModel, 
											IModel<Site> siteModel) {
		super(id, model);
		this.artExhibitionGuideModel=artExhibitionGuideModel;
		this.artExhibitionModel=artExhibitionModel;
		this.siteModel=siteModel;
	}
	
	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();

		Form<GuideContent> form = new Form<GuideContent>("form");
		add(form);
		setForm(form);
	
		objectStateField = new ChoiceField<ObjectState>("state", new PropertyModel<ObjectState>(getModel(), "state"), getLabel("state")) {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public IModel<List<ObjectState>> getChoices() {
				return new ListModel<ObjectState> (getStates());
			}
			
			@Override
			protected String getDisplayValue(ObjectState value) {
				if (value==null)
					return null;
				return value.getLabel( getLocale());
			}
		};
		
		nameField = new TextField<String>("name", new PropertyModel<String>(getModel(), "name"), getLabel("name"));
		subtitleField = new TextField<String>("subtitle", new PropertyModel<String>(getModel(), "subtitle"), getLabel("subtitle"));
	 	infoField = new TextAreaField<String>("info", new PropertyModel<String>(getModel(), "info"), getLabel("audio-info"), 12);
	
	 	
	 	/**
	 	photoField = new FileUploadSimpleField<Resource>("photo", getPhotoModel(), getLabel("photo")) {

			private static final long serialVersionUID = 1L;

			protected boolean processFileUploads(List<FileUpload> uploads) {
				return GuideContentEditor.this.processPhotoUpload(uploads);
			}

			public Image getImage() {
				if (getPhotoModel() == null || getPhotoModel().getObject()==null )
					return null;
				return GuideContentEditor.this.getThumbnail(getPhotoModel().getObject());
			}

			public String getFileName() {
				if (getModel()!=null  || getModel().getObject()==null )
					return GuideContentEditor.this.getPhotoMeta( getModel().getObject() );
				return null;
			}

			public boolean isThumbnail() {
				return true;
			}
		};
       **/
		
		audioField = new FileUploadSimpleField<Resource>("audio", getAudioModel(), getLabel("audio")) {

			private static final long serialVersionUID = 1L;

			protected boolean processFileUploads(List<FileUpload> uploads) {
				return GuideContentEditor.this.processAudioUpload(uploads);
			}

			public Image getImage() {
				return null;
			}

			
			protected String getAudioSrc() {
			
				if (getAudioModel() == null || getAudioModel().getObject()==null )
					return null;
				return GuideContentEditor.this.getPresignedUrl(getAudioModel().getObject());
			}
			
			public String getFileName() {
				if (getAudioModel()!=null  || getAudioModel().getObject()==null )
					return GuideContentEditor.this.getAudioMeta( getAudioModel().getObject() );
				return null;
			}

		};

		form.add(nameField);
		form.add(subtitleField);
		form.add(infoField);
		form.add(audioField);
		form.add(objectStateField);
		
		EditButtons<GuideContent> buttons = new EditButtons<GuideContent>("buttons-bottom", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onEdit(AjaxRequestTarget target) {
				GuideContentEditor.this.onEdit(target);
			}

			@Override
			public void onCancel(AjaxRequestTarget target) {
				GuideContentEditor.this.onCancel(target);
			}

			@Override
			public void onSave(AjaxRequestTarget target) {
				GuideContentEditor.this.onSave(target);
			}

			@Override
			public boolean isVisible() {
				return getForm().getFormState() == FormState.EDIT;
			}
		};
		form.add(buttons);
	 
		EditButtons<GuideContent> b_buttons_top = new EditButtons<GuideContent>("buttons-top", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			public void onEdit(AjaxRequestTarget target) {
				 GuideContentEditor.this.onEdit(target);
			}

			public void onCancel(AjaxRequestTarget target) {
				GuideContentEditor.this.onCancel(target);
			}

			public void onSave(AjaxRequestTarget target) {
				GuideContentEditor.this.onSave(target);
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

	
	protected void onCancel(AjaxRequestTarget target) {
		super.cancel(target);
		// getForm().setFormState(FormState.VIEW);
		// target.add(getForm());
	}

	public void onEdit(AjaxRequestTarget target) {
		super.edit(target);
		// getForm().setFormState(FormState.EDIT);
		// target.add(getForm());
	}

	protected void onSave(AjaxRequestTarget target) {
		logger.debug("onSave");
		logger.debug("updated parts:");
		getUpdatedParts().forEach(s -> logger.debug(s));
		logger.debug("saving...");
		
		save(getModelObject());
		
		uploadedPhoto = false;
		
		getForm().setFormState(FormState.VIEW);
		logger.debug("done");
		target.add(this);
	}

	@Override
	public List<ToolbarItem> getToolbarItems() {
		
	List<ToolbarItem> list = new ArrayList<ToolbarItem>();
		
		AjaxButtonToolbarItem<ArtExhibition> create = new AjaxButtonToolbarItem<ArtExhibition>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
 				fire(new MenuAjaxEvent(ServerAppConstant.action_guide_content_edit, target));
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
	
	public IModel<ArtExhibitionGuide> getArtExhibitionGuideModel() {
		return artExhibitionGuideModel;
	}

	public void setArtExhibitionGuideModel(IModel<ArtExhibitionGuide> artExhibitionGuideModel) {
		this.artExhibitionGuideModel = artExhibitionGuideModel;
	}
	
	@Override
	public void onDetach() {
		super.onDetach();

		if (photoModel != null)
			photoModel.detach();
		
		if (audioModel != null)
			audioModel.detach();
		
		if (siteModel!=null)
			siteModel.detach();
		
		if (artExhibitionModel!=null)
			artExhibitionModel.detach();
		
		if (artExhibitionItemModel!=null)
			artExhibitionItemModel.detach();
	}

	protected IModel<Resource> getPhotoModel() {
		return this.photoModel;
	}

	protected void setPhotoModel(ObjectModel<Resource> model) {
		this.photoModel = model;
	}

	protected IModel<Resource> getAudioModel() {
		return this.audioModel;
	}

	protected void setAudioModel(ObjectModel<Resource> model) {
		this.audioModel = model;
	}
	
	protected void onSubmit() {
		logger.debug("");
		logger.debug("onSubmit");
		logger.debug("");
	}

	protected boolean processPhotoUpload(List<FileUpload> uploads) {

		if (this.uploadedPhoto)
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

					setPhotoModel(new ObjectModel<Resource>(resource));
					getModel().getObject().setPhoto(resource);

					uploadedPhoto = true;

				} catch (Exception e) {
					uploadedPhoto = false;
					error("Error saving file: " + e.getMessage());
				}
			}
		} else {
			info("No file uploaded.");
			logger.debug("No file uploaded.");
		}

		return uploadedPhoto;
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
	
	private void setUpModel() {

		Optional<GuideContent> o_i = getGuideContentDBService().findWithDeps(getModel().getObject().getId());
		setModel(new ObjectModel<GuideContent>(o_i.get()));

		if (getModel().getObject().getPhoto() != null) {
			Optional<Resource> o_r = getResourceDBService().findWithDeps(getModel().getObject().getPhoto().getId());
			setPhotoModel(new ObjectModel<Resource>(o_r.get()));
		}

		if (getModel().getObject().getAudio() != null) {
			Optional<Resource> o_r = getResourceDBService().findWithDeps(getModel().getObject().getAudio().getId());
			setAudioModel(new ObjectModel<Resource>(o_r.get()));
		}
		
		if (getModel().getObject().getArtExhibitionItem()!=null) {
			Optional<ArtExhibitionItem> o_ae = getArtExhibitionItemDBService().findWithDeps(getModel().getObject().getArtExhibitionItem().getId());
			setArtExhibitionItemModel(new ObjectModel<ArtExhibitionItem>(o_ae.get()));
		}

		Optional<ArtExhibition> o_a = getArtExhibitionDBService().findWithDeps(getArtExhibitionModel().getObject().getId());
		setArtExhibitionModel(new ObjectModel<>(o_a.get()));
		
		Optional<Site> o_s = getSiteDBService().findWithDeps(getArtExhibitionModel().getObject().getSite().getId());
		setSiteModel(new ObjectModel<Site>(o_s.get()));
	}
	
	
}
