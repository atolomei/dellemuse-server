package dellemuse.serverapp.page.person;

import java.util.List;
import java.util.Optional;

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.editor.DBObjectEditor;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.site.SiteInfoEditor;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.EditButtons;
import io.wktui.form.button.SubmitButton;
import io.wktui.form.field.FileUploadSimpleField;
import io.wktui.form.field.TextField;

public class PersonEditor extends DBObjectEditor<Person> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteInfoEditor.class.getName());

	private TextField<String> nameField;
	private TextField<String> lastnameField;
	private TextField<String> nicknameField;
	private TextField<String> sexField;
	private TextField<String> addressField;
	private TextField<String> phoneField;
	private TextField<String> emailField;
	private TextField<String> webpageField;
	
	private FileUploadSimpleField<Void> photoField;
	private IModel<Resource> photoModel;

	private boolean uploadedPhoto = false;
	
	/**
	 * @param id
	 * @param model
	 */
	public PersonEditor(String id, IModel<Person> model) {
		super(id, model);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		
		if (getModel().getObject().getPhoto()!=null) {
			Optional<Resource> o_r = getResourceDBService().findByIdWithDeps(getModel().getObject().getPhoto().getId());
			setPhotoModel(new ObjectModel<Resource>(o_r.get()));
		}
		 
	     Person person = getModel().getObject();
	        getPersonDBService().reloadIfDetached(person);
	        getModel().setObject(person);
	        
		
		Form<Person> form = new Form<Person>("personForm", getModel());
		form.setOutputMarkupId(true);
		
		add(form);
		setForm(form);
		
		form.setFormState(FormState.VIEW);

		nameField 			= new TextField<String>("name", 	Model.of(getModel().getObject().getName()),		 	getLabel("name"));
		lastnameField 		= new TextField<String>("lastname",	Model.of(getModel().getObject().getLastname()), 	getLabel("lastname"));
		nicknameField 		= new TextField<String>("nickname", Model.of(getModel().getObject().getNickname()), 	getLabel("nickname"));
		sexField 			= new TextField<String>("sex", 		Model.of(getModel().getObject().getSex()), 			getLabel("sex"));
		addressField 		= new TextField<String>("address", 	Model.of(getModel().getObject().getAddress()), 		getLabel("address"));
		phoneField 			= new TextField<String>("phone", 	Model.of(getModel().getObject().getPhone()), 		getLabel("phone"));
		emailField 			= new TextField<String>("email", 	Model.of(getModel().getObject().getEmail()), 		getLabel("email"));
		webpageField        = new TextField<String>("webpage",	Model.of(getModel().getObject().getWebpage()), 		getLabel("webpage"));
		
		

        photoField 			= new FileUploadSimpleField<Void>("photo", getLabel("photo")) {

			private static final long serialVersionUID = 1L;

			protected boolean processFileUploads(List<FileUpload> uploads) {
				return PersonEditor.this.processPhotoUpload(uploads);
			}

			public Image getImage() {
				if (getPhotoModel()==null)
					return null;
				return PersonEditor.this.getThumbnail(getPhotoModel().getObject());
			}

			public String getFileName() {
				if (getPhotoModel()==null)
					return null;
				return PersonEditor.this.getPhotoMeta( getPhotoModel().getObject() );
				
			}

			public boolean isThumbnail() {
				return true;
			}
		};
		
		
		form.add(nameField);
		form.add(lastnameField);
		form.add(nicknameField);
		//form.add(sexField);
		form.add(addressField);
		form.add(phoneField);
		form.add(emailField);
		form.add(photoField);
		form.add(webpageField);

		EditButtons<Person> buttons = new EditButtons<Person>("buttons", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			public void onEdit( AjaxRequestTarget target ) {
				 PersonEditor.this.onEdit(target);
			}

			public void onCancel( AjaxRequestTarget target ) {
				 PersonEditor.this.onCancel(target);
			}

			public void onSave(AjaxRequestTarget target ) {
				 PersonEditor.this.onSave(target);
			}
			
			@Override
			public boolean isVisible() {
				return getForm().getFormState() == FormState.EDIT;
			}
		};
		
		getForm().add(buttons);
	
		EditButtons<Person> b_buttons_top = new EditButtons<Person>("buttons-top", getForm(), getModel()) {

				private static final long serialVersionUID = 1L;

				public void onEdit(AjaxRequestTarget target) {
					PersonEditor.this.onEdit(target);
				}

				public void onCancel(AjaxRequestTarget target) {
					PersonEditor.this.onCancel(target);
				}

				public void onSave(AjaxRequestTarget target) {
					PersonEditor.this.onSave(target);
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

	
	@Override
	public void onDetach() {
		super.onDetach();
		
		if (photoModel!=null)
			photoModel.detach();
	}
	
	
	protected void onCancel(AjaxRequestTarget target) {
		getForm().setFormState(FormState.VIEW);
		target.add(getForm());
	}

	protected void onEdit(AjaxRequestTarget target) {
		super.edit(target);
		//getForm().setFormState(FormState.EDIT);
		target.add(getForm());
	}

	
	protected void onSave(AjaxRequestTarget target) {

		getForm().setFormState(FormState.VIEW);
		target.add(getForm());

		logger.debug("");
		logger.debug("onSubmit");
		logger.debug(getForm().isSubmitted());
		logger.debug("done");
		logger.debug("");

	}

	protected IModel<Resource> getPhotoModel() {
		return this.photoModel;
	}
	
	protected void setPhotoModel(ObjectModel<Resource> model) {
			this.photoModel=model;
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

	
}
