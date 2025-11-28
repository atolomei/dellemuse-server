package dellemuse.serverapp.page.site;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
import dellemuse.serverapp.editor.ObjectMetaEditor;
import dellemuse.serverapp.editor.ObjectUpdateEvent;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Language;
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

public class SiteInfoEditor extends DBObjectEditor<Site> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteInfoEditor.class.getName());
		
	
    // ChoiceField<Institution>  institutionField;


	private ChoiceField<Language> masterLanguageField;
	
	private TextField<String> nameField;
	private TextAreaField<String> subtitleField;


	private TextField<String> shortNameField;
	private TextAreaField<String> infoField;
	private TextAreaField<String> opensField;
	private TextAreaField<String> addressField;
    
	private TextField<String> websiteField;
	private TextField<String> mapurlField;
    
	private TextField<String> emailField;
	private TextAreaField<String> phoneField;
	private TextField<String> instagramField;
	private TextField<String> whatsappField;
    
	private FileUploadSimpleField<Void> photoField;
	private FileUploadSimpleField<Resource> logoField;

	private IModel<Resource> photoModel;
	private IModel<Resource> logoModel;

	private boolean uploadedPhoto = false;
	private boolean uploadedLogo = false;

	
	/**
	 * @param id
	 * @param model
	 */
	public SiteInfoEditor(String id, IModel<Site> model) {
		super(id, model);
	}
	
	@Override
	public void onInitialize() {
		super.onInitialize();
		
		Optional<Site> o_i = getSiteDBService().findWithDeps(getModel().getObject().getId());
		setModel(new ObjectModel<Site>(o_i.get()));
		
		if (getModel().getObject().getPhoto()!=null) {
			Optional<Resource> o_r = getResourceDBService().findWithDeps(getModel().getObject().getPhoto().getId());
			setPhotoModel(new ObjectModel<Resource>(o_r.get()));
		}
		
		if (getModel().getObject().getLogo()!=null) {
			Optional<Resource> o_r = getResourceDBService().findWithDeps(getModel().getObject().getLogo().getId());
			setLogoModel(new ObjectModel<Resource>(o_r.get()));
		}
		
		Form<Site> form = new Form<Site>("siteForm", getModel());
    	add(form);
		setForm(form);
       
        Site site = getModel().getObject();
        getSiteDBService().reloadIfDetached(site);
        getModel().setObject(site);
        
         List<Institution> list = new ArrayList<Institution>();
        getInstitutions().forEach(x -> list.add(x));
     
        //  StreamSupport.stream(getInstitutions().spliterator(), false).collect(Collectors.toList());
        /**
         * 
         * 
         * ------------
         * abm inst
         * abm site
         * abm awork
         * abm exhi
         * abm guide
         * ------------
         * 
        institutionField  	= new ChoiceField<Institution>("institution", null, getLabel("institution")) {
        	private static final long serialVersionUID = 1L;
			protected String getDisplayValue(Institution value) {
        		if (value==null)
        			return null;
        		return value.getDisplayname();
        	}
        };
        **/
        //institutionField.setChoices(new ListModel<Institution>( StreamSupport.stream(getInstitutions().spliterator(), false).collect(Collectors.toList()) ));
        
       
        
		
        nameField 			= new TextField<String>("name", 		new PropertyModel<String>(getModel(), "name"), getLabel("name"));
	 	subtitleField		= new TextAreaField<String>("subtitle", new PropertyModel<String>(getModel(), "subtitle"), getLabel("subtitle"), 4);
        shortNameField  	= new TextField<String>("shortName", 	new PropertyModel<String>(getModel(), "shortName"), getLabel("shortName"));
        infoField  			= new TextAreaField<String>("info", 	new PropertyModel<String>(getModel(), "info"), getLabel("info"), 8);
        opensField  		= new TextAreaField<String>("opens", 	new PropertyModel<String>(getModel(), "opens"), getLabel("opens"), 8);
        addressField  		= new TextAreaField<String>("address", 	new PropertyModel<String>(getModel(), "address"), getLabel("address"), 3);
        websiteField  		= new TextField<String>("website", 		new PropertyModel<String>(getModel(), "website"), getLabel("website"));
        mapurlField  		= new TextField<String>("mapurl", 		new PropertyModel<String>(getModel(), "mapurl"), getLabel("mapurl"));
        emailField 			= new TextField<String>("email", 		new PropertyModel<String>(getModel(), "email"), getLabel("email"));
        phoneField 			= new TextAreaField<String>("phone", 	new PropertyModel<String>(getModel(), "phone"), getLabel("phone"), 3);
        instagramField  	= new TextField<String>("instagram", 	new PropertyModel<String>(getModel(), "instagram"), getLabel("instagram"));
        whatsappField  		= new TextField<String>("whatsapp", 	new PropertyModel<String>(getModel(), "whatsapp"), getLabel("whatsapp"));
      
        photoField 			= new FileUploadSimpleField<Void>("photo", getLabel("photo")) {

			private static final long serialVersionUID = 1L;

			protected boolean processFileUploads(List<FileUpload> uploads) {
				return SiteInfoEditor.this.processPhotoUpload(uploads);
			}

			public Image getImage() {
				if (getPhotoModel()==null)
					return null;
				return SiteInfoEditor.this.getThumbnail(getPhotoModel().getObject());
			}

			public String getFileName() {
				if (getPhotoModel()==null)
					return null;
				return SiteInfoEditor.this.getPhotoMeta( getPhotoModel().getObject() );
				
			}

			public boolean isThumbnail() {
				return true;
			}
		};

		logoField = new FileUploadSimpleField<Resource>("logo", getLabel("logo")) {

			private static final long serialVersionUID = 1L;

			public Image getImage() {
				if (getLogoModel()==null)
					return null;
				return SiteInfoEditor.this.getThumbnail(getLogoModel().getObject());
			}

			public String getFileName() {
				return SiteInfoEditor.this.getLogoFileName();
			}

			public boolean isThumbnail() {
				return true;
			}

			protected boolean processFileUploads(List<FileUpload> uploads) {
				return SiteInfoEditor.this.processLogoUpload(uploads);
			}
		};
	 
		
		this.masterLanguageField = new ChoiceField<Language>("masterlanguage", new PropertyModel<Language>(getModel(), "ML"), getLabel("masterlanguage")) {

			private static final long serialVersionUID = 1L;

			@Override
			public IModel<List<Language>> getChoices() {
				return new ListModel<Language>(getLanguages());
			}

			@Override
			protected String getDisplayValue(Language value) {
				if (value == null)
					return null;
				return value.getLabel(getUserLocale());
			}
		};

		form.add(masterLanguageField);
 		form.add( nameField );
		form.add( subtitleField );
        form.add( shortNameField ); 
        form.add( infoField ); 
        form.add( opensField );
        form.add( addressField );
        form.add( websiteField );
        form.add( mapurlField );
        form.add( emailField );
        form.add( phoneField );
        form.add( instagramField );
        form.add( whatsappField );
    	form.add( photoField );
		form.add( logoField );

		EditButtons<Site> buttons = new EditButtons<Site>("buttons-bottom", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onEdit( AjaxRequestTarget target ) {
				 SiteInfoEditor.this.onEdit(target);
			}
			@Override
			public void onCancel( AjaxRequestTarget target ) {
				SiteInfoEditor.this.onCancel(target);
			}
			@Override
			public void onSave(AjaxRequestTarget target ) {
				SiteInfoEditor.this.onSave(target);
			}
			
			@Override
			public boolean isVisible() {
				return getForm().getFormState() == FormState.EDIT;
			}
		};
        form.add(buttons);
        
        
    	
		EditButtons<Site> b_buttons_top = new EditButtons<Site>("buttons-top", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			public void onEdit(AjaxRequestTarget target) {
				SiteInfoEditor.this.onEdit(target);
			}

			public void onCancel(AjaxRequestTarget target) {
				SiteInfoEditor.this.onCancel(target);
			}

			public void onSave(AjaxRequestTarget target) {
				SiteInfoEditor.this.onSave(target);
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
	
	
	protected List<Language> getLanguages() {
		return getLanguageService().getLanguagesSorted(Locale.ENGLISH);
	}
	
	@Override
	public List<ToolbarItem> getToolbarItems() {
		
	List<ToolbarItem> list = new ArrayList<ToolbarItem>();
		
		AjaxButtonToolbarItem<Person> create = new AjaxButtonToolbarItem<Person>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
 				fire(new MenuAjaxEvent(ServerAppConstant.action_site_edit, target));
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
	
	@Override
	public void onDetach() {
		super.onDetach();
		
		if (photoModel!=null)
			photoModel.detach();
	
		if (logoModel!=null)
			logoModel.detach();
		
	
	}
	
	protected void onCancel(AjaxRequestTarget target) {
		super.cancel(target);
		//getForm().setFormState(FormState.VIEW);
		//target.add(getForm());
	}

	protected void onEdit(AjaxRequestTarget target) {
		super.edit(target);
		target.add(getForm());
		
		//getForm().setFormState(FormState.EDIT);
		//target.add(getForm());
	}

	
	protected void onSave(AjaxRequestTarget target) {

			logger.debug("onSave");
			logger.debug("updated parts:");
			getUpdatedParts().forEach(s -> logger.debug(s));
			logger.debug("saving...");

			
			getModelObject().setLanguage(PARENT_PATH);
			
			
			save(getModelObject(), getSessionUser(), getUpdatedParts());
			uploadedPhoto = false;
			getForm().setFormState(FormState.VIEW);
			getForm().updateReload();
			fire (new ObjectUpdateEvent(target));
		
			target.add(this);


	}
	
	

	protected IModel<Resource> getPhotoModel() {
		return this.photoModel;
	}
	
	protected void setPhotoModel(ObjectModel<Resource> model) {
			this.photoModel=model;
	}
	
	protected IModel<Resource> getLogoModel() {
		return this.logoModel;
	}
	
	protected void setLogoModel(ObjectModel<Resource> model) {
			this.logoModel=model;
	}

	protected void onSubmit() {
	        
	        logger.debug("");
		    logger.debug("onSubmit");
		    logger.debug( getForm().isSubmitted());
		    logger.debug( emailField.getValue());
		    logger.debug("done");
	        logger.debug("");
	        
		    /**
		    emailField.updateModel();
		    logger.debug(emailField.getModel().getObject().toString());
		    logger.debug(emailField.getValue().toString());
		    logger.debug(((org.apache.wicket.markup.html.form.TextField) emailField.getInput()).getValue());
		    logger.debug( (String) emailField.getDefaultModelObject());
	        **/
	    
	 
	 
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

						Resource resource = createAndUploadFile(upload.getInputStream(), bucketName, objectName, upload.getClientFileName(), upload.getSize());
						
						setPhotoModel(new ObjectModel<Resource>(resource));
						getModel().getObject().setPhoto(resource);

						uploadedPhoto=true;
						
				 

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



	

		protected boolean processLogoUpload(List<FileUpload> uploads) {

			if (this.uploadedLogo)
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
						
						Resource resource = createAndUploadFile(upload.getInputStream(), bucketName, objectName, upload.getClientFileName(), upload.getSize());
						setLogoModel(new ObjectModel<Resource>(resource));
						getModel().getObject().setLogo(resource);
						
						uploadedLogo = true;
 

					} catch (Exception e) {
						error("Error saving file: " + e.getMessage());
					}
				}
			} else {
				info("No file uploaded.");
				logger.debug("No file uploaded.");
			}

			return uploadedLogo;
		}
	 
		
		protected Image getLogoThuembnail() {

			return getThumbnail(getLogoModel().getObject());
			
			/**
			if (getLogoModel() == null)
				return null;

			String presignedThumbnail = getPresignedThumbnailSmall(getLogoModel().getObject());

			Image image;

			if (presignedThumbnail != null) {
				Url url = Url.parse(presignedThumbnail);
				UrlResourceReference resourceReference = new UrlResourceReference(url);
				image = new Image("image", resourceReference);
			} else {
				image = new Image("image", new UrlResourceReference(Url.parse("")));
				image.setVisible(false);
			}
			return image;
			 */
		}
		
		
		 
		/**
		 * @return
		 */
		protected Image getPhotoThumbnail() {
			return getThumbnail(getPhotoModel().getObject());
			
			/**
			if (getPhotoModel() == null)
				return null;
			String presignedThumbnail = getPresignedThumbnailSmall(getPhotoModel().getObject());
			Image image;
			if (presignedThumbnail != null) {
				Url url = Url.parse(presignedThumbnail);
				UrlResourceReference resourceReference = new UrlResourceReference(url);
				image = new Image("image", resourceReference);
			} else {
				image = new Image("image", new UrlResourceReference(Url.parse("")));
				image.setVisible(false);
			}
			return image;
			**/
			
		}
		
		protected String getPhotoFileName() {
			if (getPhotoModel() == null)
				return null;
			return getPhotoModel().getObject().getDisplayname() + (getPhotoModel().getObject().getSize() != 0
					? " ( " + formatFileSize(getPhotoModel().getObject().getSize()) + " )"
					: "");
		}
		
		protected String getLogoFileName() {
			if (getLogoModel() == null)
				return null;
			return getLogoModel().getObject().getDisplayname() + (getPhotoModel().getObject().getSize() != 0
					? " ( " + formatFileSize(getLogoModel().getObject().getSize()) + " )"
					: "");
		}

	 
}
