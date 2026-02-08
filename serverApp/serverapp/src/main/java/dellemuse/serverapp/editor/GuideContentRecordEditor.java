package dellemuse.serverapp.editor;

import java.util.List;
import java.util.Optional;

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.audiostudio.AudioStudioPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.AudioStudio;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.record.GuideContentRecord;
import io.wktui.form.Form;
import io.wktui.form.field.FileUploadSimpleField;
import io.wktui.form.field.TextAreaField;

public class GuideContentRecordEditor extends ObjectRecordEditor<GuideContent, GuideContentRecord> {

	static private Logger logger = Logger.getLogger(GuideContentRecordEditor.class.getName());

	private IModel<Resource> audioAccesibleModel;
	private boolean uploadedAudioAccesible = false;
	
	private static final long serialVersionUID = 1L;

	private Link<GuideContentRecord> openAudioStudioAccesible;
	private TextAreaField<String> infoAccesibleField;
	private FileUploadSimpleField<Resource> audioAccesibleField;
	private IModel<ArtExhibitionGuide> artExhibitionGuideModel;
	
	
	public GuideContentRecordEditor(String id, IModel<GuideContent> sourceModel, IModel<GuideContentRecord> TranslationRecordModel) {
		super(id, sourceModel, TranslationRecordModel);
		 
	}

	
	@Override
	public void onInitialize() {
		super.onInitialize();
	}
	
	@Override
	protected boolean isAccesible() {
		return getArtExhibitionGuideModel().getObject().isAccessible();
	}
	
	@Override
	public void onDetach() {
		super.onDetach();

		if (artExhibitionGuideModel != null)
			this.artExhibitionGuideModel.detach();
	}

	@Override
	protected void setUpModel() {
		 super.setUpModel();
		 
		//if (getModel().getObject().getAudio() != null) {
		//	Optional<Resource> o_r = getResourceDBService().findWithDeps(getModel().getObject().getAudio().getId());
		//	setAudioModel(new ObjectModel<Resource>(o_r.get()));
		//}
		 
		 Optional<ArtExhibitionGuide> o_r = getArtExhibitionGuideDBService().findWithDeps(getSourceModel().getObject().getArtExhibitionGuide().getId());
		 if (o_r.isPresent()) {
			 setArtExhibitionGuideModel(new ObjectModel<ArtExhibitionGuide>(o_r.get()));
		 }
		 
	}

	@Override
	protected void loadForm() {
		super.loadForm();
		
		Form<GuideContentRecord> form = getForm();
		
		
		
		this.infoAccesibleField = new TextAreaField<String>("infoAccesible", new PropertyModel<String>(getModel(), "infoAccesible"), getLabel("infoAccesible"), 20);
		form.add(this.infoAccesibleField );
 		
		
		audioAccesibleField = new FileUploadSimpleField<Resource>("audioAccesible", getAudioModel(), getLabel("audioAccesible")) {

			private static final long serialVersionUID = 1L;

			protected boolean processFileUploads(List<FileUpload> uploads) {
				return GuideContentRecordEditor.this.processAudioAccesibleUpload(uploads);
			}

			protected void onRemove(AjaxRequestTarget target) {
				GuideContentRecordEditor.this.onAudioRemove(target);
			}

			public Image getImage() {
				return null;
			}

			protected String getAudioSrc() {

				if (getAudioModel() == null || getAudioModel().getObject() == null)
					return null;
				return GuideContentRecordEditor.this.getPresignedUrl(getAudioModel().getObject());
			}

			public String getFileName() {
				if (getAudioAccesibleModel() != null || getAudioAccesibleModel().getObject() == null)
					return GuideContentRecordEditor.this.getAudioMeta(getAudioAccesibleModel().getObject());
				return null;
			}

		};

		audioAccesibleField.setVisible(isAudioVisible());
		form.add(audioAccesibleField);
		
		

		Label title = new Label("recordTitle", getLabel("translate-information", getModel().getObject().getLanguage()));
		form.add(title);

		this.openAudioStudioAccesible = new Link<GuideContentRecord>("openAudioStudio", getModel()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				Optional<AudioStudio> oa = getAudioStudioDBService().findOrCreate(getModel().getObject(), getSessionUser().get());
				if (oa.isPresent())
					setResponsePage(new AudioStudioPage(new ObjectModel<AudioStudio>(oa.get()), getArtExhibitionGuideModel().getObject().isAccessible()  ));
				logger.error("audio studio not created for -> " + getModel().getObject().getDisplayname());
			}

			@Override
			public boolean isVisible() {
				return isAudioStudioEnabled(getModel().getObject());
			}
		};

		Label openArtworkLabel = new Label("openAccesibleAudioStudioLabel", getLabel("open-audio-studio", getModel().getObject().getDisplayname()));
		this.openAudioStudioAccesible.add(openArtworkLabel);
		form.add(openAudioStudioAccesible);
		
		
	}
	
	

	
	
	protected boolean processAudioAccesibleUpload(List<FileUpload> uploads) {

		if (this.uploadedAudioAccesible)
			return false;

		if (uploads != null && !uploads.isEmpty()) {

			for (FileUpload upload : uploads) {
				try {
 
					String bucketName = ServerConstant.MEDIA_BUCKET;
					String objectName = getResourceDBService().normalizeFileName(FileNameUtils.getBaseName(upload.getClientFileName())) + "-" + String.valueOf(getResourceDBService().newId());

					Resource resource = createAndUploadFile(upload.getInputStream(), bucketName, objectName, upload.getClientFileName(), upload.getSize());

					setAudioModel(new ObjectModel<Resource>(resource));
					getModel().getObject().setAudio(resource);

					uploadedAudioAccesible = true;

				} catch (Exception e) {
					uploadedAudioAccesible = false;
					error("Error saving file: " + e.getMessage());
				}
			}
		} else {
			info("No file uploaded.");
			logger.debug("No file uploaded.");
		}
		return uploadedAudioAccesible;
	}

	protected IModel<Resource> getAudioAccesibleModel() {
		return this.audioAccesibleModel;
	}

	protected void setAudioAccesibleModel(ObjectModel<Resource> model) {
		this.audioAccesibleModel = model;
	}


	public IModel<ArtExhibitionGuide> getArtExhibitionGuideModel() {
		return artExhibitionGuideModel;
	}


	public void setArtExhibitionGuideModel(IModel<ArtExhibitionGuide> artExhibitionGuideModel) {
		this.artExhibitionGuideModel = artExhibitionGuideModel;
	}
	
	
	

}
