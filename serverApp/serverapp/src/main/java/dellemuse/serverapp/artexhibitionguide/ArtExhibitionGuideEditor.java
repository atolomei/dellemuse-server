package dellemuse.serverapp.artexhibitionguide;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
 

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;
import org.threeten.bp.OffsetDateTime;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.artexhibition.ArtExhibitionPage;
import dellemuse.serverapp.artwork.ArtWorkPage;
import dellemuse.serverapp.editor.DBObjectEditor;
import dellemuse.serverapp.editor.ObjectUpdateEvent;
import dellemuse.serverapp.guidecontent.GuideContentEditor;
import dellemuse.serverapp.page.InternalPanel;
 
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.person.ServerAppConstant;
 
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.service.DTFormatter;
import io.wktui.error.AlertPanel;
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
 * 
 * alter table artexhibition add column spec text;
 * 
 */
public class ArtExhibitionGuideEditor extends DBObjectEditor<ArtExhibitionGuide> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ArtExhibitionGuideEditor.class.getName());

	private ChoiceField<ObjectState> objectStateField;
	
	private TextField<String> nameField;
	private TextField<String> subtitleField;
	private TextAreaField<String> infoField;
	
	private FileUploadSimpleField<Resource> photoField;
	private FileUploadSimpleField<Resource> audioField;
	
	private IModel<Resource> photoModel;
	private IModel<Resource> audioModel;
	
	private boolean uploadedPhoto = false;
	private boolean uploadedAudio = false;
	
	private IModel<Site> siteModel;
	private IModel<ArtExhibition> artExhibitionModel;

	private Link<ArtExhibition> openAe;
	private AjaxLink<ArtExhibition> importAe;
	
	

	private String audioMeta;
	
	/**
	 * @param id
	 * @param model
	 */
	public ArtExhibitionGuideEditor(String id, IModel<ArtExhibitionGuide> model, IModel<Site> siteModel) {
		super(id, model);
		this.siteModel=siteModel;
	}
	
	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();

		Form<ArtExhibitionGuide> form = new Form<ArtExhibitionGuide>("form");
		add(form);
		setForm(form);
 
		
		this.nameField 		= new TextField<String>("name", new PropertyModel<String>(getModel(), "name"), getLabel("name"));
		this.subtitleField 	= new TextField<String>("subtitle", new PropertyModel<String>(getModel(), "subtitle"), getLabel("subtitle"));
		this.infoField 		= new TextAreaField<String>("info", new PropertyModel<String>(getModel(), "info"), getLabel("info"), 12);
		
		/**this.photoField 	= new FileUploadSimpleField<Resource>("photo", getPhotoModel(), getLabel("photo")) {

			private static final long serialVersionUID = 1L;

			protected boolean processFileUploads(List<FileUpload> uploads) {
				return ArtExhibitionGuideEditor.this.processPhotoUpload(uploads);
			}

			public Image getImage() {
				if (getPhotoModel() == null || getPhotoModel().getObject()==null )
					return null;
				return ArtExhibitionGuideEditor.this.getThumbnail(getPhotoModel().getObject());
			}

			public String getFileName() {
				if (getModel()!=null  || getModel().getObject()==null )
					return ArtExhibitionGuideEditor.this.getPhotoMeta( getModel().getObject() );
				return null;
			}

			public boolean isThumbnail() {
				return true;
			}
		};
		*/
		
		this.audioField = new FileUploadSimpleField<Resource>("audio", getAudioModel(), getLabel("audio")) {

			private static final long serialVersionUID = 1L;

			protected boolean processFileUploads(List<FileUpload> uploads) {
				return ArtExhibitionGuideEditor.this.processAudioUpload(uploads);
			}

			public Image getImage() {
				return null;
			}

			protected String getAudioSrc() {
				
				if (getAudioModel() == null || getAudioModel().getObject()==null )
					return null;
				return ArtExhibitionGuideEditor.this.getPresignedUrl(getAudioModel().getObject());
			}
			
			public String getFileName() {
				
				if (getAudioModel()!=null  || getAudioModel().getObject()==null ) {
					if (audioMeta==null)
						audioMeta = ArtExhibitionGuideEditor.this.getAudioMeta( getAudioModel().getObject() );
					return audioMeta;
				}
				return null;
			}

			public boolean isThumbnail() {
				return true;
			}
			/**
			protected WebMarkupContainer getFeedbackPanel() {
			
				if (getModel()!=null && getModel().getObject()!=null) {
					if (getModel().getObject().isAudioAutoGenerate()) {
						AlertPanel<Void> alert = new  AlertPanel<Void>("feedback", 
								AlertPanel.INFO, 
								null, 
								null, 
								null,
								getLabel( "generated",  getDateTimeService().format(getModel().getObject().getLastModified(), DTFormatter.Month_Day_Year_hh_mm )));

						alert.add( new org.apache.wicket.AttributeModifier("style"," float:left; width:100%; margin-top:1px;"));		
						return alert;
					}
					else {
						AlertPanel<Void> alert = new  AlertPanel<Void>("feedback", 
								AlertPanel.INFO, 
								null, 
								null, 
								null,
								getLabel( "manually-uploaded",	getModel().getObject().getLastModifiedUser().getName(), 
																getDateTimeService().format(getModel().getObject().getLastModified(), DTFormatter.Month_Day_Year_hh_mm )));
								alert.add( new org.apache.wicket.AttributeModifier("style"," float:left; width:100%; margin-top:1px;"));		

						return alert;
						
						}
					}
					
					return null;
				}
				*/
		};
 	 
 		form.add(nameField);
		form.add(subtitleField);
		form.add(infoField);
		form.add(audioField);
		
		EditButtons<ArtExhibitionGuide> buttons = new EditButtons<ArtExhibitionGuide>("buttons-bottom", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onEdit(AjaxRequestTarget target) {
				ArtExhibitionGuideEditor.this.onEdit(target);
			}

			@Override
			public void onCancel(AjaxRequestTarget target) {
				ArtExhibitionGuideEditor.this.onCancel(target);
			}

			@Override
			public void onSave(AjaxRequestTarget target) {
				ArtExhibitionGuideEditor.this.onSave(target);
			}

			@Override
			public boolean isVisible() {
				return getForm().getFormState() == FormState.EDIT;
			}
		};
		form.add(buttons);
		
		

		EditButtons<ArtExhibitionGuide> b_buttons_top = new EditButtons<ArtExhibitionGuide>("buttons-top", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			public void onEdit(AjaxRequestTarget target) {
				 ArtExhibitionGuideEditor.this.onEdit(target);
			}

			public void onCancel(AjaxRequestTarget target) {
				ArtExhibitionGuideEditor.this.onCancel(target);
			}

			public void onSave(AjaxRequestTarget target) {
				ArtExhibitionGuideEditor.this.onSave(target);
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
		
		
		
		this.openAe = new Link<ArtExhibition>("openAe", getArtExhibitionModel()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage( new ArtExhibitionPage(getArtExhibitionModel()));
			}
			
			public boolean isEnabled() {
				return true;
			}
		};
		
		Label openArtworkLabel  = new Label("openAeLabel", getLabel("open-artexhibition", getArtExhibitionModel().getObject().getDisplayname()));
		this.openAe.add(openArtworkLabel);
		getForm().add(openAe);
		 
	
		
		this.importAe = new AjaxLink<ArtExhibition>("importAe", getArtExhibitionModel()) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				ArtExhibitionGuideEditor.this.importgetArtExhibitionText(target);
			}
			public boolean isVisible() {
				return getForm().getFormState()==FormState.EDIT;
			}

		};
		
		
		Label importArtworkLabel  = new Label("importAeLabel", getLabel("import-artexhibition", getArtExhibitionModel().getObject().getDisplayname()));
		this.importAe.add(importArtworkLabel);
		getForm().add(this.importAe);
		
		//addItemsPanel();
		
	}

  

	protected void importgetArtExhibitionText(AjaxRequestTarget target) {
	
		String info = getArtExhibitionModel().getObject().getInfo();
	
		boolean reload = false;
		
		if (info!=null && getModel().getObject().getInfo()==null) {
			getModel().getObject().setInfo(info);
			reload = true;
		}
		else if (info==null && getModel().getObject().getInfo()!=null) {
			getModel().getObject().setInfo(info);
			reload = true;
		}
		else {
			if (!info.trim().equals(getModel().getObject().getInfo().trim())) {
				getModel().getObject().setInfo(info);
				reload = true;
			}
		}
		
		String subtitle = getArtExhibitionModel().getObject().getSubtitle();
		
		if (subtitle!=null && getModel().getObject().getSubtitle()==null) {
			getModel().getObject().setSubtitle(subtitle);
			getForm().updateReload();
		}
		else if (subtitle==null && getModel().getObject().getSubtitle()!=null) {
			getModel().getObject().setSubtitle(subtitle);
			getForm().updateReload();
		}
		else {
			if (!subtitle.trim().equals(getModel().getObject().getSubtitle().trim())) {
				getModel().getObject().setSubtitle(subtitle);
				getForm().updateReload();
			}
		}
	
		if (reload)
			getForm().updateReload();
			
		target.add(getForm());
		
	}

	protected void onCancel(AjaxRequestTarget target) {
		super.cancel(target);
		audioMeta=null;
		// getForm().setFormState(FormState.VIEW);
		// target.add(getForm());
	}

	public void onEdit(AjaxRequestTarget target) {
		super.edit(target);
		// getForm().setFormState(FormState.EDIT);
		// target.add(getForm());
	}



 	@Override
	public List<ToolbarItem> getToolbarItems() {
		
	List<ToolbarItem> list = new ArrayList<ToolbarItem>();
		
		AjaxButtonToolbarItem<ArtExhibition> create = new AjaxButtonToolbarItem<ArtExhibition>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
 				fire(new MenuAjaxEvent(ServerAppConstant.action_guide_edit_info, target));
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

	protected void onSave(AjaxRequestTarget target) {
		logger.debug("onSave");
		logger.debug("updated parts:");
		getUpdatedParts().forEach(s -> logger.debug(s));
		logger.debug("saving...");
		
		
		save(getModelObject());
		
		uploadedPhoto = false;
		uploadedAudio = false;
		audioMeta=null;

		getForm().setFormState(FormState.VIEW);
		getForm().updateReload();
		fire (new ObjectUpdateEvent(target));
	;
		target.add(this);
	
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

		return uploadedPhoto;
	}




	private void setUpModel() {

		Optional<ArtExhibitionGuide> o_i = getArtExhibitionGuideDBService().findWithDeps(getModel().getObject().getId());
		setModel(new ObjectModel<ArtExhibitionGuide>(o_i.get()));

		if (getModel().getObject().getPhoto() != null) {
			Optional<Resource> o_r = getResourceDBService().findWithDeps(getModel().getObject().getPhoto().getId());
			setPhotoModel(new ObjectModel<Resource>(o_r.get()));
		}

		if (getModel().getObject().getAudio() != null) {
			Optional<Resource> o_r = getResourceDBService().findWithDeps(getModel().getObject().getAudio().getId());
			setAudioModel(new ObjectModel<Resource>(o_r.get()));
		}
		
		if (getModel().getObject().getArtExhibition()!=null) {
			
			Optional<ArtExhibition> o_ae = getArtExhibitionDBService().findWithDeps(getModel().getObject().getArtExhibition().getId());
			setArtExhibitionModel(new ObjectModel<ArtExhibition>(o_ae.get()));
		
			if (getSiteModel().getObject() != null) {
				Optional<Site> o_s = getSiteDBService().findWithDeps(getArtExhibitionModel().getObject().getSite().getId());
				setSiteModel(new ObjectModel<Site>(o_s.get()));
			}
		}
	}

	
	
}
