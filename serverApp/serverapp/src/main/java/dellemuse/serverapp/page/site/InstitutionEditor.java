package dellemuse.serverapp.page.site;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.NumberFormatter;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.editor.DBObjectEditor;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.InstitutionDBService;
import dellemuse.serverapp.serverdb.service.ResourceDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.EditButtons;
import io.wktui.form.field.FileUploadSimpleField;
import io.wktui.form.field.NumberField;
import io.wktui.form.field.TextAreaField;
import io.wktui.form.field.TextField;

public class InstitutionEditor extends DBObjectEditor<Institution> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(InstitutionEditor.class.getName());

	private Form<Institution> form;

	private TextField<String> nameField;
	private TextField<String> shortNameField;
	private TextAreaField<String> addressField;
	private TextField<String> websiteField;
	private TextField<String> mapurlField;
	private TextField<String> emailField;
	private TextAreaField<String> phoneField;
	private TextField<String> instagramField;
	private TextField<String> whatsappField;
	private TextAreaField<String> infoField;
	
	private FileUploadSimpleField<Resource> photoField;
	private FileUploadSimpleField<Resource> logoField;
	
	private IModel<Resource> photoModel;
	private IModel<Resource> logoModel;

	private boolean uploadedPhoto = false;
	private boolean uploadedLogo = false;

	/**
	 * @param id
	 * @param model
	 */
	public InstitutionEditor(String id, IModel<Institution> model) {
		super(id, model);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		
		if (photoModel!=null)
			photoModel.detach();
	
		if (logoModel!=null)
			logoModel.detach();
		
	
	}

	
	
	@Override
	public void onInitialize() {
		super.onInitialize();

		Optional<Institution> o_i = getInstitutionDBService().findByIdWithDeps(getModel().getObject().getId());
		setModel(new ObjectModel<Institution>(o_i.get()));
		
		if (getModel().getObject().getPhoto()!=null) {
			Optional<Resource> o_r = getResourceDBService().findByIdWithDeps(getModel().getObject().getPhoto().getId());
			setPhotoModel(new ObjectModel<Resource>(o_r.get()));
		}
		
		if (getModel().getObject().getLogo()!=null) {
			Optional<Resource> o_r = getResourceDBService().findByIdWithDeps(getModel().getObject().getLogo().getId());
			setLogoModel(new ObjectModel<Resource>(o_r.get()));
		}
		
		this.form = new Form<Institution>("institutionForm", getModel());

		add(this.form);
		setForm(this.form);

	 	nameField 		= new TextField<String>("name", new PropertyModel<String>(getModel(), "name"), getLabel("name"));
		shortNameField 	= new TextField<String>("shortName", new PropertyModel<String>(getModel(), "shortName"), getLabel("shortName"));
		addressField 	= new TextAreaField<String>("address", new PropertyModel<String>(getModel(), "address"), getLabel("address"), 4);
		websiteField 	= new TextField<String>("website", new PropertyModel<String>(getModel(), "website"), getLabel("website"));
		mapurlField 	= new TextField<String>("mapurl", new PropertyModel<String>(getModel(), "mapUrl"), getLabel("mapurl"));
		emailField 		= new TextField<String>("email", new PropertyModel<String>(getModel(), "email"), getLabel("email"));
		phoneField 		= new TextAreaField<String>("phone", new PropertyModel<String>(getModel(), "phone"), getLabel("phone"), 4);
		instagramField 	= new TextField<String>("instagram", new PropertyModel<String>(getModel(), "instagram"), getLabel("instagram"));
		whatsappField 	= new TextField<String>("whatsapp", new PropertyModel<String>(getModel(), "whatsapp"), getLabel("whatsapp"));
		infoField 		= new TextAreaField<String>("info", new PropertyModel<String>(getModel(), "info"), getLabel("info"), 10);
		photoField 		= new FileUploadSimpleField<Resource>("photo", getLabel("photo")) {

			private static final long serialVersionUID = 1L;

			protected boolean processFileUploads(List<FileUpload> uploads) {
				return InstitutionEditor.this.processPhotoUpload(uploads);
			}

			public Image getImage() {
				return InstitutionEditor.this.getPhotoThumbnail();
			}

			public String getFileName() {
				return InstitutionEditor.this.getPhotoFileName();
			}

			public boolean isThumbnail() {
				return true;
			}
		};


		logoField = new FileUploadSimpleField<Resource>("logo", getLabel("logo")) {

			private static final long serialVersionUID = 1L;

			protected boolean processFileUploads(List<FileUpload> uploads) {
				return InstitutionEditor.this.processLogoUpload(uploads);
			}

			public Image getImage() {
				return InstitutionEditor.this.getLogoThumbnail();
			}

			public String getFileName() {
				return InstitutionEditor.this.getLogoFileName();
			}

			public boolean isThumbnail() {
				return true;
			}
		};

	 	form.add(nameField);
		form.add(shortNameField);
		form.add(infoField);
		form.add(addressField);
		form.add(websiteField);
		form.add(mapurlField);
		form.add(emailField);
		form.add(phoneField);
		form.add(instagramField);
		form.add(whatsappField);
		form.add(photoField);
		form.add(logoField);

		
		AjaxLink<Institution> edit_link = new AjaxLink<Institution>("edit-link", getModel()) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				onEdit(target);
				target.add(getForm());
			}

			@Override
			public boolean isVisible() {
				return getForm().getFormState() == FormState.VIEW;
			}
		};

		getForm().add(edit_link);

		EditButtons<Institution> b_buttons = new EditButtons<Institution>("buttons-bottom", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			public void onEdit(AjaxRequestTarget target) {
				InstitutionEditor.this.onEdit(target);
			}

			public void onCancel(AjaxRequestTarget target) {
				InstitutionEditor.this.onCancel(target);
			}

			public void onSave(AjaxRequestTarget target) {
				InstitutionEditor.this.onSave(target);
			}

			@Override
			public boolean isVisible() {
				return getForm().getFormState() == FormState.EDIT;
			}
		};

		getForm().add(b_buttons);
	}

	protected Image getLogoThumbnail() {

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
	
	
 

	/**
	 * 
	 * @return
	 */
	protected Image getPhotoThumbnail() {
		
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
		
	}

	protected boolean processPhotoUpload(List<FileUpload> uploads) {

		if (this.uploadedPhoto)
			return false;

		if (uploads != null && !uploads.isEmpty()) {
			for (FileUpload upload : uploads) {
				try {
					logger.debug("name -> " + upload.getClientFileName());
					logger.debug("Size -> " + upload.getSize());

					// upload photo to ObjectStorage
					String bucketName = ServerConstant.MEDIA_BUCKET;
					String objectName = getResourceDBService()
							.normalizeFileName(FileNameUtils.getBaseName(upload.getClientFileName())) + "-"
							+ String.valueOf(getResourceDBService().newId());

					try (InputStream is = upload.getInputStream()) {
						getResourceDBService().upload(bucketName, objectName, upload.getInputStream(),
								upload.getClientFileName());
						User user = getUserDBService().findRoot();

						Resource resource = getResourceDBService().create(bucketName, objectName,
								upload.getClientFileName(), upload.getClientFileName(), upload.getSize(), user);
						
						setPhotoModel(new ObjectModel<Resource>(resource));

						getModel().getObject().setPhoto(resource);

						uploadedPhoto = true;

						return true;
					}

				} catch (Exception e) {
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

					try (InputStream is = upload.getInputStream()) {
						getResourceDBService().upload(bucketName, objectName, upload.getInputStream(),
								upload.getClientFileName());
						User user = getUserDBService().findRoot();

						Resource resource = getResourceDBService().create(bucketName, objectName,
								upload.getClientFileName(), upload.getClientFileName(), upload.getSize(), user);
						
						setLogoModel(new ObjectModel<Resource>(resource));

						getModel().getObject().setLogo(resource);

						uploadedLogo = true;

						return true;
					}

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

	
	/**
	public Resource getPhoto() {
		if (getPhotoModel()==null)
			return null;
		return getPhotoModel().getObject();
	}
**/
	
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
	
	protected void onCancel(AjaxRequestTarget target) {
		super.cancel(target);
	}

	/**
	 * 
	 */
	protected void onEdit(AjaxRequestTarget target) {
		super.edit(target);
		// getForm().setFormState(FormState.EDIT);
		// target.add(this);
	}

	@Override
	public void update(AjaxRequestTarget target) {

		updateModel();


		target.add(this);

		logger.debug(getModelObject().toString());
		logger.debug("done");

		// try {
		// if (!getUpdatedParts().isEmpty()) {
		//
		// }

		// getModelObject();
	}

	/**
	 * 
	 * @param target
	 */
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

}
