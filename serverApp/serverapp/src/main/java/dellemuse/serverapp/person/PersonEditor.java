package dellemuse.serverapp.person;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
 
import org.apache.wicket.model.PropertyModel;
 
import org.apache.wicket.model.util.ListModel;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.artist.ArtistCreateEvent;
import dellemuse.serverapp.editor.DBObjectEditor;
import dellemuse.serverapp.editor.ObjectUpdateEvent;
import dellemuse.serverapp.editor.SimpleAlertRow;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.site.SiteInfoEditor;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.event.SimpleAjaxWicketEvent;
import io.wktui.event.SimpleWicketEvent;
import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.EditButtons;
import io.wktui.form.button.SubmitButton;
import io.wktui.form.field.ChoiceField;
import io.wktui.form.field.FileUploadSimpleField;
import io.wktui.form.field.TextAreaField;
import io.wktui.form.field.TextField;
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import wktui.base.InvisiblePanel;

public class PersonEditor extends DBObjectEditor<Person> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteInfoEditor.class.getName());

	private ChoiceField<ObjectState> objectStateField;

	private TextField<String> nameField;
	private TextField<String> lastnameField;
	private TextField<String> nicknameField;
	private TextField<String> sexField;
	private TextField<String> addressField;
	private TextField<String> phoneField;
	private TextField<String> emailField;
	private TextField<String> webpageField;

	private FileUploadSimpleField<Resource> photoField;
	private IModel<Resource> photoModel;

	private TextAreaField<String> infoField;

	WebMarkupContainer cac;
	WebMarkupContainer aac;
	
	private boolean uploadedPhoto = false;

	/**
	 * @param id
	 * @param model
	 */
	public PersonEditor(String id, IModel<Person> model) {
		super(id, model);
	}

	
	private void setUpModel() {
	
		getModel().setObject( getPersonDBService().findById(getModel().getObject().getId()).get());
		
		if (getModel().getObject().getPhoto() != null) {
			Optional<Resource> o_r = getResourceDBService().findWithDeps(getModel().getObject().getPhoto().getId());
			setPhotoModel(new ObjectModel<Resource>(o_r.get()));
		}
	}
	
	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();
		
		add(new InvisiblePanel("error"));
	
		Form<Person> form = new Form<Person>("personForm", getModel());
		form.setOutputMarkupId(true);

		add(form);
		setForm(form);

		form.setFormState(FormState.VIEW);

		cac = new WebMarkupContainer("createArtistContainer") {
			public boolean isVisible() {
				return getArtistDBService().getByPerson(getModel().getObject()).isEmpty();
			}
		};
		
		
		AjaxLink<Void> ca =new AjaxLink<Void>("createArtist") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				getArtistDBService().create( PersonEditor.this.getModel().getObject(), getRootUser());
				fireScanAll(new ArtistCreateEvent("create-artist", target));
			}
		};
		form.add(cac);
		cac.add(ca);


		/**
		aac = new WebMarkupContainer("alreadyArtistContainer") {
			public boolean isVisible() {
				return getArtistDBService().getByPerson(getModel().getObject()).isPresent();
			}
		};
		
		AjaxLink<Void> aa =new AjaxLink<Void>("alreadyArtist") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				fire (new SimpleAjaxWicketEvent(ServerAppConstant.person_artist, target));
			}
		};
		form.add(aac);
		aac.add(aa);
		**/
		
		
		/**
		objectStateField = new ChoiceField<ObjectState>("state", new PropertyModel<ObjectState>(getModel(), "state"), getLabel("state")) {

			private static final long serialVersionUID = 1L;

			@Override
			public IModel<List<ObjectState>> getChoices() {
				return new ListModel<ObjectState>(getStates());
			}

			@Override
			protected String getDisplayValue(ObjectState value) {
				if (value == null)
					return null;
				return value.getLabel(getSessionUser().get().getLocale());
			}

			@Override
			protected String getIdValue(ObjectState value) {
				return String.valueOf(value.getId());
			}

		};
		form.add(objectStateField);
		 **/
		
		infoField = new TextAreaField<String>("info", new PropertyModel<String>(getModel(), "info"), getLabel("info"), 10);
		nameField = new TextField<String>("name", new PropertyModel<String>(getModel(), "name"), getLabel("name"));
		lastnameField = new TextField<String>("lastname", new PropertyModel<String>(getModel(), "lastname"), getLabel("lastname"));
		//nicknameField = new TextField<String>("nickname", new PropertyModel<String>(getModel(), "nickname"), getLabel("nickname"));
		sexField = new TextField<String>("sex", new PropertyModel<String>(getModel(), "sex"), getLabel("sex"));
		addressField = new TextField<String>("address", new PropertyModel<String>(getModel(), "address"), getLabel("address"));
		phoneField = new TextField<String>("phone", new PropertyModel<String>(getModel(), "phone"), getLabel("phone"));
		emailField = new TextField<String>("email", new PropertyModel<String>(getModel(), "email"), getLabel("email"));
		webpageField = new TextField<String>("webpage", new PropertyModel<String>(getModel(), "webpage"), getLabel("webpage"));
		photoField = new FileUploadSimpleField<Resource>("photo", getPhotoModel(), getLabel("photo")) {

			private static final long serialVersionUID = 1L;

			protected boolean processFileUploads(List<FileUpload> uploads) {
				return PersonEditor.this.processPhotoUpload(uploads);
			}

			public Image getImage() {
				if (getPhotoModel() == null)
					return null;
				return PersonEditor.this.getThumbnail(getPhotoModel().getObject());
			}

			public String getFileName() {
				if (getPhotoModel() == null)
					return null;
				return PersonEditor.this.getPhotoMeta(getPhotoModel().getObject());
			}

			public boolean isThumbnail() {
				return true;
			}
			
			@Override
			protected void onRemove(AjaxRequestTarget target) {
				logger.debug("onRemove");
			}
		};

		form.add(nameField);
		form.add(lastnameField);
		//form.add(nicknameField);
		// form.add(sexField);
		form.add(addressField);
		form.add(phoneField);
		form.add(emailField);
		form.add(photoField);
		form.add(webpageField);

		EditButtons<Person> buttons = new EditButtons<Person>("buttons", getForm(), getModel()) {

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
				
				if (!hasWritePermission())
					return false;
				
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
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (this.photoModel != null)
			this.photoModel.detach();
	}

	@Override
	public List<ToolbarItem> getToolbarItems() {

		List<ToolbarItem> list = new ArrayList<ToolbarItem>();

		AjaxButtonToolbarItem<Person> create = new AjaxButtonToolbarItem<Person>() {
			private static final long serialVersionUID = 1L;

			@Override	
			protected void onCick(AjaxRequestTarget target) {
				fire(new MenuAjaxEvent(ServerAppConstant.action_person_edit_info, target));
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

	protected void onCancel(AjaxRequestTarget target) {
		getForm().setFormState(FormState.VIEW);
		target.add(getForm());
	}

	protected void onEdit(AjaxRequestTarget target) {
		super.edit(target);
		target.add(getForm());
	}

	protected void onSave(AjaxRequestTarget target) {

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

	protected IModel<Resource> getPhotoModel() {
		return this.photoModel;
	}

	protected void setPhotoModel(ObjectModel<Resource> model) {
		this.photoModel = model;
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
					String objectName = getResourceDBService().normalizeFileName(FileNameUtils.getBaseName(upload.getClientFileName())) + "-" + String.valueOf(getResourceDBService().newId());

					Resource resource = createAndUploadFile(upload.getInputStream(), bucketName, objectName, upload.getClientFileName(), upload.getSize(), true);

					setPhotoModel(new ObjectModel<Resource>(resource));
					getModel().getObject().setPhoto(resource);

					uploadedPhoto = true;

				} catch (Exception e) {
					uploadedPhoto = false;
					addOrReplace(new SimpleAlertRow<Void>("error", e));
				}
			}
		} else {
			info("No file uploaded.");
			logger.debug("No file uploaded.");
		}

		return uploadedPhoto;
	}

}
