package dellemuse.serverapp.institution;

import java.io.InputStream;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.ThumbnailSize;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.editor.DBObjectEditor;
import dellemuse.serverapp.editor.ObjectUpdateEvent;
import dellemuse.serverapp.editor.SimpleAlertRow;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.User;
import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.EditButtons;
import io.wktui.form.field.ChoiceField;
import io.wktui.form.field.FileUploadSimpleField;
import io.wktui.form.field.MultipleSelectField;
import io.wktui.form.field.TextAreaField;
import io.wktui.form.field.TextField;
import io.wktui.form.field.ZoneIdField;
import wktui.base.InvisiblePanel;

public class InstitutionEditor extends DBObjectEditor<Institution> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(InstitutionEditor.class.getName());

	private Form<Institution> form;
	private ZoneIdField zoneIdField;
	private TextField<String> nameField;
	private TextAreaField<String> subtitleField;
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

	
	private ChoiceField<Language> masterLanguageField;
	private MultipleSelectField<Language> languagesField;

	private List<IModel<Language>> langSelected;
	private List<IModel<Language>> langChoices;
	
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

		if (photoModel != null)
			photoModel.detach();

		if (logoModel != null)
			logoModel.detach();
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		Optional<Institution> o_i = getInstitutionDBService().findWithDeps(getModel().getObject().getId());
		setModel(new ObjectModel<Institution>(o_i.get()));

		if (getModel().getObject().getPhoto() != null) {
			Optional<Resource> o_r = getResourceDBService().findWithDeps(getModel().getObject().getPhoto().getId());
			setPhotoModel(new ObjectModel<Resource>(o_r.get()));
		}

		if (getModel().getObject().getLogo() != null) {
			Optional<Resource> o_r = getResourceDBService().findWithDeps(getModel().getObject().getLogo().getId());
			setLogoModel(new ObjectModel<Resource>(o_r.get()));
		}

		
		add(new InvisiblePanel("error"));

		this.form = new Form<Institution>("institutionForm", getModel());

		add(this.form);
		setForm(this.form);

		
		
		
		
		langSelected = new ArrayList<IModel<Language>>();
		langChoices = new ArrayList<IModel<Language>>();
		
		Language.getLanguages().forEach((k,v) -> langChoices.add(Model.of(v)));
		

		List<Language> l_list = getModel().getObject().getLanguages();
		
		if (l_list != null && l_list.size() > 0) {
			l_list.forEach(i ->  {
				if (!i.getLanguageCode().equals( getModelObject().getMasterLanguage()))
						langSelected.add(Model.of(i));
			});
		}
		
	languagesField = new MultipleSelectField<Language>("languages", langSelected, getLabel("languages"), langChoices) {
		
			
			private static final long serialVersionUID = 1L;

			@Override
			protected IModel<String> getObjectTitle(IModel<Language> model) {
				return Model.of( model.getObject().getLabel(getUserLocale()) );
			}

			@Override
			protected IModel<String> getObjectSubtitle(IModel<Language> model) {
				return Model.of( model.getObject().getLabel(getUserLocale()) );
			}
			
			@Override
			protected void onObjectRemove(IModel<Language> model, AjaxRequestTarget target) {
				langSelected.remove(model);
				target.add(getForm());
			}
		
			@Override
			protected void onObjectSelect(IModel<Language> model, AjaxRequestTarget target) {
				langSelected.add(model);
				target.add(getForm());
			}
		};
		
		form.add(languagesField);
		
		
		this.masterLanguageField = new ChoiceField<Language>("masterlanguage", new PropertyModel<Language>(getModel(), "ML"), getLabel("masterlanguage")) {

			private static final long serialVersionUID = 1L;

			@Override
			public IModel<List<Language>> getChoices() {
				return new org.apache.wicket.model.util.ListModel<Language>(getLanguages());
			}

			@Override
			protected String getDisplayValue(Language value) {
				if (value == null)
					return null;
				return value.getLabel(getUserLocale());
			}
		};
		
		form.add(masterLanguageField);
		
		
		
		
		zoneIdField = new ZoneIdField("zoneid", new PropertyModel<ZoneId>(getModel(), "zoneId"), getLabel("zoneid"));
		nameField = new TextField<String>("name", new PropertyModel<String>(getModel(), "name"), getLabel("name"));
		subtitleField = new TextAreaField<String>("subtitle", new PropertyModel<String>(getModel(), "subtitle"), getLabel("subtitle"), 4);
		shortNameField = new TextField<String>("shortName", new PropertyModel<String>(getModel(), "shortName"), getLabel("shortName"));
		addressField = new TextAreaField<String>("address", new PropertyModel<String>(getModel(), "address"), getLabel("address"), 4);
		websiteField = new TextField<String>("website", new PropertyModel<String>(getModel(), "website"), getLabel("website"));
		mapurlField = new TextField<String>("mapurl", new PropertyModel<String>(getModel(), "mapUrl"), getLabel("mapurl"));
		emailField = new TextField<String>("email", new PropertyModel<String>(getModel(), "email"), getLabel("email"));
		phoneField = new TextAreaField<String>("phone", new PropertyModel<String>(getModel(), "phone"), getLabel("phone"), 4);
		instagramField = new TextField<String>("instagram", new PropertyModel<String>(getModel(), "instagram"), getLabel("instagram"));
		whatsappField = new TextField<String>("whatsapp", new PropertyModel<String>(getModel(), "whatsapp"), getLabel("whatsapp"));
		infoField = new TextAreaField<String>("info", new PropertyModel<String>(getModel(), "info"), getLabel("info"), 10);

		photoField = new FileUploadSimpleField<Resource>("photo", getLabel("photo")) {

			private static final long serialVersionUID = 1L;

			protected boolean processFileUploads(List<FileUpload> uploads) {
				return InstitutionEditor.this.processPhotoUpload(uploads);
			}

			public Image getImage() {
				return InstitutionEditor.this.getPhotoThumbnail();
			}

			public String getFileName() {
				if (getModel() == null)
					return null;
				return InstitutionEditor.this.getPhotoMeta(getModel().getObject());
			}

			public boolean isThumbnail() {
				return true;
			}

			@Override
			protected void onRemove(AjaxRequestTarget target) {
				logger.debug("onRemove");
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
		form.add(subtitleField);
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
		form.add(zoneIdField);

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

				if (!hasWritePermission())
					return false;

				return getForm().getFormState() == FormState.EDIT;
			}
		};

		EditButtons<Institution> b_buttons_top = new EditButtons<Institution>("buttons-top", getForm(), getModel()) {

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
		getForm().add(b_buttons);
	}

	protected Image getLogoThumbnail() {

		if (getLogoModel() == null)
			return null;

		String presignedThumbnail = getPresignedThumbnail(getLogoModel().getObject(), ThumbnailSize.SMALL);

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
		return getPhotoModel().getObject().getDisplayname() + (getPhotoModel().getObject().getSize() != 0 ? " ( " + formatFileSize(getPhotoModel().getObject().getSize()) + " )" : "");
	}

	protected String getLogoFileName() {

		if (getLogoModel() == null)
			return null;

		return getLogoModel().getObject().getDisplayname() + (getLogoModel().getObject().getSize() != 0 ? " ( " + formatFileSize(getLogoModel().getObject().getSize()) + " )" : "");
	}

	/**
	 * @return
	 */
	protected Image getPhotoThumbnail() {

		if (getPhotoModel() == null)
			return null;

		try {
			String presignedThumbnail = getPresignedThumbnail(getPhotoModel().getObject(), ThumbnailSize.SMALL);
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
		} catch (Exception e) {
			logger.error(e, ServerConstant.NOT_THROWN);
			return null;
		}

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
					String objectName = getResourceDBService().normalizeFileName(FileNameUtils.getBaseName(upload.getClientFileName())) + "-" + String.valueOf(getResourceDBService().newId());

					try (InputStream is = upload.getInputStream()) {

						getResourceDBService().upload(bucketName, objectName, upload.getInputStream(), upload.getClientFileName(), true);

						User user = getUserDBService().findRoot();

						Resource resource = getResourceDBService().create(bucketName, objectName, upload.getClientFileName(), getResourceDBService().getMimeType(upload.getClientFileName()), upload.getSize(), null, user,
								upload.getClientFileName(), true);

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
					String objectName = getResourceDBService().normalizeFileName(FileNameUtils.getBaseName(upload.getClientFileName())) + "-" + String.valueOf(getResourceDBService().newId());

					try (InputStream is = upload.getInputStream()) {

						getResourceDBService().upload(bucketName, objectName, upload.getInputStream(), upload.getClientFileName(), true);
						User user = getUserDBService().findRoot();

						Resource resource = getResourceDBService().create(bucketName, objectName, upload.getClientFileName(), getResourceDBService().getMimeType(upload.getClientFileName()), upload.getSize(), null, user,
								upload.getClientFileName(), true);

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

	protected IModel<Resource> getPhotoModel() {
		return this.photoModel;
	}

	protected void setPhotoModel(ObjectModel<Resource> model) {
		this.photoModel = model;
	}

	protected IModel<Resource> getLogoModel() {
		return this.logoModel;
	}

	protected void setLogoModel(ObjectModel<Resource> model) {
		this.logoModel = model;
	}

	protected void onCancel(AjaxRequestTarget target) {
		super.cancel(target);
	}

	/**
	 * 
	 */
	protected void onEdit(AjaxRequestTarget target) {
		super.edit(target);

	}

	@Override
	public void update(AjaxRequestTarget target) {
		updateModel();
		target.add(this);
		logger.debug(getModelObject().toString());
		logger.debug("done");

	}

	/**
	 * @param target
	 */
	protected void onSave(AjaxRequestTarget target) {

		updateModel();
		getUpdatedParts().forEach(s -> logger.debug(s));

		try {
			save(getModelObject(), getSessionUser().get(), getUpdatedParts());
			uploadedPhoto = false;
			getForm().setFormState(FormState.VIEW);
			getForm().updateReload();
			fireScanAll(new ObjectUpdateEvent(target));

		} catch (Exception e) {

			addOrReplace(new SimpleAlertRow<Void>("error", e));
			logger.error(e);

		}
		target.add(this);

	}

}
