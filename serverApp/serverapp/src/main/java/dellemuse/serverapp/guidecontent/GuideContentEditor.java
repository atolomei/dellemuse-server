package dellemuse.serverapp.guidecontent;

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
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
 

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.artwork.ArtWorkPage;
import dellemuse.serverapp.audiostudio.AudioStudioPage;
import dellemuse.serverapp.editor.DBObjectEditor;
import dellemuse.serverapp.editor.ObjectMarkAsDeleteEvent;
import dellemuse.serverapp.editor.ObjectRestoreEvent;
import dellemuse.serverapp.editor.ObjectUpdateEvent;
import dellemuse.serverapp.editor.SimpleAlertRow;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.AudioStudio;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import io.wktui.error.AlertPanel;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.event.SimpleAjaxWicketEvent;
import io.wktui.event.UIEvent;
import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.EditButtons;
import io.wktui.form.field.FileUploadSimpleField;
import io.wktui.form.field.StaticTextField;
import io.wktui.form.field.TextAreaField;
import io.wktui.form.field.TextField;
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import wktui.base.InvisiblePanel;

/**
 * 
 * alter table artexhibition add column spec text;
 * 
 */
public class GuideContentEditor extends DBObjectEditor<GuideContent> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(GuideContentEditor.class.getName());
	
	private TextField<String> nameField;
	private TextField<String> shortField;
	private TextField<String> subtitleField;
	private TextField<String> urlField;
	private TextAreaField<String> specField;
	private TextAreaField<String> locationField;
	private TextAreaField<String> infoField;
	private TextAreaField<String> introField;
	private StaticTextField<String> audioIdField;
	private FileUploadSimpleField<Resource> audioField;
	
	private IModel<Resource> photoModel;
	private IModel<Resource> audioModel;
	
	private boolean uploadedPhoto = false;
	private boolean uploadedAudio = false;

	private IModel<Site> siteModel;
	private IModel<ArtExhibition> artExhibitionModel;
	private IModel<ArtExhibitionGuide> artExhibitionGuideModel;
	private IModel<ArtExhibitionItem> artExhibitionItemModel;
	private IModel<ArtWork> artWorkModel;
	private Link<ArtWork> openArtwork;
	private AjaxLink<ArtWork> importArtwork;
	
	private String audioMeta;

	//private WebMarkupContainer infoContainer;
	private AlertPanel<Void> info;
	private Link<GuideContent> openAudioStudio;
	
	private boolean alertInfo = false;
	

	/**
	 * @param id
	 * @param model
	 */
	public GuideContentEditor(String id, IModel<GuideContent> model, 
										 IModel<ArtExhibitionGuide> artExhibitionGuideModel, 
										 IModel<ArtExhibition> artExhibitionModel, 
										 IModel<Site> siteModel) {
		super(id, model);
	
		this.artExhibitionGuideModel=artExhibitionGuideModel;
		this.artExhibitionModel=artExhibitionModel;
		this.siteModel=siteModel;
	}
	
	@Override
	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<SimpleAjaxWicketEvent>() {
			private static final long serialVersionUID = 1L;
			@Override
			public void onEvent(SimpleAjaxWicketEvent event) {
				reloadModel();
				alertInfo=true;
				event.getTarget().add(GuideContentEditor.this);
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof ObjectMarkAsDeleteEvent)
					return true;
				return false;
			}
		});
	
	
		add(new io.wktui.event.WicketEventListener<SimpleAjaxWicketEvent>() {
			private static final long serialVersionUID = 1L;
			@Override
			public void onEvent(SimpleAjaxWicketEvent event) {
				reloadModel();
				alertInfo=true;
				event.getTarget().add(GuideContentEditor.this);
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof ObjectRestoreEvent)
					return true;
				return false;
			}
		});
	}
	
	@Override
	public  void onBeforeRender() {
		super.onBeforeRender();
		/**
		if (getModel().getObject().getState()==ObjectState.DELETED) {
			String s=getLabel("audio-guide-info").getObject();
			this.info=new AlertPanel<Void>("info", AlertPanel.WARNING,   getLabel("object-deleted", s));
			infoContainer.addOrReplace(info);
			this.alertInfo=true;
			getForm().setEnabled(false);
		}
		else {
			infoContainer.addOrReplace(new InvisiblePanel("info"));
			this.alertInfo=false;
		}	
		**/
	}
	
	
	private void addAlert() {
		SimpleAlertRow<Void> alert = new SimpleAlertRow<Void>("simpleAlertRow") {
			private static final long serialVersionUID = 1L;
			
			public boolean isVisible() {
				return GuideContentEditor.this.isAlertInfo();
			}
			
			protected IModel<String> getText() {
				String s=GuideContentEditor.this.getLabel("audio-guide-info").getObject();
				return  GuideContentEditor.this.getLabel("object-deleted", s);
			}
			
			public int getAlertType() {
				return AlertPanel.WARNING;
			}
		};
		add(alert);
		
		this.alertInfo=getModel().getObject().getState()==ObjectState.DELETED;
		
	}
	/**
	 * 
	 * 
	 */
	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();
		addAlert();
				
	 	Form<GuideContent> form = new Form<GuideContent>("form");
	
		add(form);
		setForm(form);
 			
		nameField 		= new TextField<String>("name", new PropertyModel<String>(getModel(), "name"), getLabel("name"));
		subtitleField 	= new TextField<String>("subtitle", new PropertyModel<String>(getModel(), "subtitle"), getLabel("subtitle"));
	 	infoField 		= new TextAreaField<String>("info", new PropertyModel<String>(getModel(), "info"), getLabel("audio-info"), 12);
		audioField 		= new FileUploadSimpleField<Resource>("audio", getAudioModel(), getLabel("audio")) {

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
					if (audioMeta==null)
						audioMeta = GuideContentEditor.this.getAudioMeta( getAudioModel()  );
					return audioMeta;
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
						 alert.setOutputMarkupId(false);
							
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
							    alert.setOutputMarkupId(false);
								 
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
		
		
		audioIdField = new StaticTextField<String>("audioid", new PropertyModel<String>(getModel(), "audioId"), getLabel("audioid"));
	
		form.add(audioIdField);
		
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
		
		this.openArtwork = new Link<ArtWork>("openArtwork", getArtWorkModel()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage( new ArtWorkPage(getArtWorkModel()));
			}
			
					
		};
		
		Label openArtworkLabel  = new Label("openArtworkLabel", getLabel("open-artwork", getArtWorkModel().getObject().getDisplayname()));
		this.openArtwork.add(openArtworkLabel);
		getForm().add(openArtwork);
		
		this.importArtwork = new AjaxLink<ArtWork>("importArtwork", getArtWorkModel()) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				GuideContentEditor.this.importArtWorkText(target);
				
			}
			@Override
			public boolean isVisible() {
				return getForm().getFormState() == FormState.EDIT;
			}
		};
		
		
		this.openAudioStudio = new Link<GuideContent>("openAudioStudio", getModel()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				Optional<AudioStudio> oa = getAudioStudioDBService().findOrCreate( getModel().getObject(), getSessionUser());
				if (oa.isPresent())
					setResponsePage(new AudioStudioPage( new ObjectModel<AudioStudio>( oa.get())));
				else
					setResponsePage(new ErrorPage(Model.of("no audio studio")));
			}
			
			public boolean isEnabled() {
				return true;
			}
		};
		
		Label openLabel  = new Label("openAudioStudioLabel", getLabel("open-audio-studio", getModel().getObject().getDisplayname()));
		this.openAudioStudio.add(openLabel);
		getForm().add(openAudioStudio);
		
		
		Label importArtworkLabel  = new Label("importArtworkLabel", getLabel("import-artwork", getArtWorkModel().getObject().getDisplayname()));
		this.importArtwork.add(importArtworkLabel);
		getForm().add(this.importArtwork);
	}

 
	protected void importArtWorkText(AjaxRequestTarget target) {
		
		String info = getArtWorkModel().getObject().getInfo();
	
		if (info!=null && getModel().getObject().getInfo()==null) {
			getModel().getObject().setInfo(info);
			getForm().updateReload();
		}
		else if (info==null && getModel().getObject().getInfo()!=null) {
			getModel().getObject().setInfo(info);
			getForm().updateReload();
	
		}
		else {
			if (!info.trim().equals(getModel().getObject().getInfo().trim())) {
				getModel().getObject().setInfo(info);
				getForm().updateReload();
			}
		}
		target.add(getForm());
	}


	public IModel<ArtWork> getArtWorkModel() {
		return this.artWorkModel;
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
		
		getForm().updateReload();
		fire (new ObjectUpdateEvent(target));
		
		target.add(this);
	}

	@Override
	public List<ToolbarItem> getToolbarItems() {
		
	List<ToolbarItem> list = new ArrayList<ToolbarItem>();
		
		AjaxButtonToolbarItem<GuideContent> create = new AjaxButtonToolbarItem<GuideContent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return (GuideContentEditor.this.getModel().getObject().getState()!=ObjectState.DELETED);
			}

			
			@Override
			public boolean isEnabled() {
				return (GuideContentEditor.this.getModel().getObject().getState()!=ObjectState.DELETED);
			}
		
			@Override
			protected void onCick(AjaxRequestTarget target) {
 				fire(new MenuAjaxEvent(ServerAppConstant.action_guide_content_edit, target));
			}
			@Override
			public IModel<String> getButtonTitle() {
				return GuideContentEditor.this.getLabel("edit");
			}
			
			public String getIconCss() {
				return "fa-regular fa-pen-to-square";
			}
			
		};
		
		create.setAlign(Align.TOP_LEFT);
		list.add(create);


		AjaxButtonToolbarItem<GuideContent> delete = new AjaxButtonToolbarItem<GuideContent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return (GuideContentEditor.this.getModel().getObject().getState()!=ObjectState.DELETED);
			}
			
			@Override
			protected void onCick(AjaxRequestTarget target) {
 				 fire(new MenuAjaxEvent(ServerAppConstant.action_guide_content_delete, target));
			}
			
			@Override
			public IModel<String> getButtonLabel() {
				return null;
			}
			
			@Override
			public IModel<String> getButtonTitle() {
				return GuideContentEditor.this.getLabel("delete");
			}
		
			
			@Override
			public String getIconCss() {
				return "fa-regular fa-trash";
			}
			
			
			
			protected String getButtonCss() {
				return "btn btn-sm btn-outline-primary mt-1 ms-1";
			}
			
		};
		delete.setAlign(Align.TOP_LEFT);
		list.add(delete);
		
		
		AjaxButtonToolbarItem<GuideContent> restore = new AjaxButtonToolbarItem<GuideContent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return (GuideContentEditor.this.getModel().getObject().getState()==ObjectState.DELETED);
			}
			
			@Override
			protected void onCick(AjaxRequestTarget target) {
				fire(new MenuAjaxEvent(ServerAppConstant.action_guide_content_restore, target));
			}
			
			@Override
			public IModel<String> getButtonLabel() {
				return GuideContentEditor.this.getLabel("restore");
			}
			
			@Override
			public IModel<String> getButtonTitle() {
				return GuideContentEditor.this.getLabel("restore");
			}
			
			@Override
			public String getIconCss() {
				return "fa-solid fa-check";
			}
			
			protected String getButtonCss() {
				return "btn btn-sm btn-outline-primary mt-1 ms-1";
			}
			
		};
		restore.setAlign(Align.TOP_LEFT);
		list.add(restore);
		
		

		
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
	
		if (artWorkModel!=null)
			artWorkModel.detach();
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
	
	
	public void reloadModel() {
		setUpModel();
	}
	
	
	private void setUpModel() {

		Optional<GuideContent> o_i = getGuideContentDBService().findWithDeps(getModel().getObject().getId());
		setModel(new ObjectModel<GuideContent>(o_i.get()));

		
		if (getModel().getObject().getArtExhibitionItem()!=null) {
			Optional<ArtExhibitionItem> o_ai = getArtExhibitionItemDBService().findWithDeps(getModel().getObject().getArtExhibitionItem().getId());
			
			if (o_ai.get().getArtwork()!=null) {
				Optional<ArtWork> o_aw = getArtWorkDBService().findWithDeps(o_ai.get().getArtwork().getId());
				setArtWorkModel(o_aw.get());
			}
		}
			
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



	protected void setArtWorkModel(ArtWork artWork) {
			this.artWorkModel=new ObjectModel<ArtWork>(artWork);
	}

	public boolean isAlertInfo() {
		return alertInfo;
	}

	public void setAlertInfo(boolean alertInfo) {
		this.alertInfo = alertInfo;
	}
	
	
}
