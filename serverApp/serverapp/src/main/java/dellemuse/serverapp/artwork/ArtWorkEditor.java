package dellemuse.serverapp.artwork;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
import dellemuse.serverapp.editor.DBSiteObjectEditor;
import dellemuse.serverapp.editor.ObjectUpdateEvent;
import dellemuse.serverapp.editor.SimpleAlertRow;
 
import dellemuse.serverapp.page.model.ObjectModel;
 
import dellemuse.serverapp.serverdb.model.ArtWork;
 
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.EditButtons;
import io.wktui.form.field.ChoiceField;
import io.wktui.form.field.FileUploadSimpleField;
import io.wktui.form.field.NumberField;
import io.wktui.form.field.TextAreaField;
import io.wktui.form.field.TextField;
import wktui.base.InvisiblePanel;

public class ArtWorkEditor extends DBSiteObjectEditor<ArtWork> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ArtWorkEditor.class.getName());

	private TextField<String> urlField;
	private TextField<String> nameField;
	private TextAreaField<String> infoField;
	private TextAreaField<String> specField;
	private FileUploadSimpleField<Resource> photoField;
	private ChoiceField<Long> artistField;

	private NumberField<Integer> c_numberField;
	private IModel<Resource> photoModel;

	private boolean uploadedPhoto = false;
	private List<Long> mainArtists;
	private IModel<Site> siteModel;
	/**
	 * @param id
	 * @param model
	 */
	public ArtWorkEditor(String id, IModel<ArtWork> model) {
		super(id, model);
	}

	/**
	 * 
	 * @param id
	 */
	public void setMainArtist(Long id) {
		mainArtists.add(id);
	}

	public Long getMainArtist() {
		if (mainArtists != null && mainArtists.size() > 0)
			return mainArtists.get(0);
		return null;
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();

		add(new InvisiblePanel("error"));

		mainArtists = new ArrayList<Long>();
		
		
	 	
		Set<Person> set = getModel().getObject().getArtists();
		
		if (set!=null && set.size()>0) {
			setMainArtist(set.iterator().next().getId());
		}

	 

		Form<ArtWork> form = new Form<ArtWork>("form");
		add(form);
		setForm(form);

		this.urlField = new TextField<String>("url", new PropertyModel<String>(getModel(), "url"), getLabel("url"));
		this.specField = new TextAreaField<String>("spec", new PropertyModel<String>(getModel(), "spec"), getLabel("spec"), 8);
		this.nameField = new TextField<String>("name", new PropertyModel<String>(getModel(), "name"), getLabel("name"));
		this.infoField = new TextAreaField<String>("info", new PropertyModel<String>(getModel(), "info"), getLabel("info"), 20);
		this.photoField = new FileUploadSimpleField<Resource>("photo", getPhotoModel(), getLabel("photo")) {

			private static final long serialVersionUID = 1L;

			protected boolean processFileUploads(List<FileUpload> uploads) {
				return ArtWorkEditor.this.processPhotoUpload(uploads);
			}

			public Image getImage() {

				if (getModel() == null)
					return null;

				if (getPhotoModel() == null)
					return null;
				return ArtWorkEditor.this.getThumbnail(getModel().getObject());
			}

			public String getFileName() {
				if (getModel() != null)
					return ArtWorkEditor.this.getPhotoMeta(getModel().getObject());
				return null;
			}

			public boolean isThumbnail() {
				return true;
			}
			
			@Override
			protected void onRemove(AjaxRequestTarget target) {
				logger.debug("onRemove");
			} 

		};

		c_numberField = new NumberField<Integer>("year", new PropertyModel<Integer>(getModel(), "year"), getLabel("year"));
		artistField   = new ChoiceField<Long>("artist", new PropertyModel<Long>(this, "mainArtist"), getLabel("artist")) {

			private static final long serialVersionUID = 1L;

			@Override
			public IModel<List<Long>> getChoices() {
				List<Long> list = new ArrayList<Long>();
				StreamSupport.stream(getPersons().spliterator(), false).collect(Collectors.toList()).forEach(i -> list.add(i.getId()));
				return new ListModel<Long>(list);
			}

			@Override
			protected String getDisplayValue(Long value) {

				if (value == null)
					return null;

				Optional<Person> o = getPerson(value);

				if (o.isPresent())
					return getPerson(value).get().getLastFirstname();

				return "";
			}
		};

		form.add(urlField);
		form.add(specField);
		form.add(nameField);
		form.add(infoField);
		form.add(photoField);

		form.add(artistField);
		form.add(c_numberField);

		EditButtons<ArtWork> buttons = new EditButtons<ArtWork>("buttons-bottom", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onEdit(AjaxRequestTarget target) {
				ArtWorkEditor.this.onEdit(target);
			}

			@Override
			public void onCancel(AjaxRequestTarget target) {
				ArtWorkEditor.this.onCancel(target);
			}

			@Override
			public void onSave(AjaxRequestTarget target) {
				ArtWorkEditor.this.onSave(target);
			}

			@Override
			public boolean isVisible() {
				
				if (!hasWritePermission())
					return false;
				
				return getForm().getFormState() == FormState.EDIT;
			}
		};
		form.add(buttons);

		EditButtons<ArtWork> b_buttons_top = new EditButtons<ArtWork>("buttons-top", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			public void onEdit(AjaxRequestTarget target) {
				ArtWorkEditor.this.onEdit(target);
			}

			public void onCancel(AjaxRequestTarget target) {
				ArtWorkEditor.this.onCancel(target);
			}

			public void onSave(AjaxRequestTarget target) {
				ArtWorkEditor.this.onSave(target);
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

	public Optional<Person> getPerson(Long value) {
		return super.getPerson(value);
	}

	public void onCancel(AjaxRequestTarget target) {
		super.cancel(target);
	}

	public void onEdit(AjaxRequestTarget target) {
		super.edit(target);
	}

	public void onSave(AjaxRequestTarget target) {

		try {

			getUpdatedParts().forEach(s -> logger.debug(s));

			if (this.getMainArtist() != null) {
				Long id = this.getMainArtist();
				Person person = getPerson(id).get();
				Set<Person> set = new HashSet<Person>();
				set.add(person);
				getModel().getObject().setArtists(set);
			}

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

	@Override
	public void onDetach() {
		super.onDetach();

		if (photoModel != null)
			photoModel.detach();
	
	
		if (siteModel!=null)
			siteModel.detach();
		
	}
	
	

	protected IModel<Resource> getPhotoModel() {
		return this.photoModel;
	}

	protected void setPhotoModel(ObjectModel<Resource> model) {
		this.photoModel = model;
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
					String objectName = getResourceDBService().normalizeFileName(FileNameUtils.getBaseName(upload.getClientFileName())) + "-" + String.valueOf(getResourceDBService().newId());

					Resource resource = createAndUploadFile(upload.getInputStream(), bucketName, objectName, upload.getClientFileName(), upload.getSize());

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

	private void setUpModel() {

		Optional<ArtWork> o_i = getArtWorkDBService().findWithDeps(getModel().getObject().getId());
		setModel(new ObjectModel<ArtWork>(o_i.get()));

		if (getModel().getObject().getPhoto() != null) {
			Optional<Resource> o_r = getResourceDBService().findWithDeps(getModel().getObject().getPhoto().getId());
			setPhotoModel(new ObjectModel<Resource>(o_r.get()));
		}
		
		if (getModel().getObject().getSite() != null) {
			Optional<Site> o_s = getSiteDBService().findWithDeps(getModel().getObject().getSite().getId());
			setSiteModel(new ObjectModel<Site>(o_s.get()));
		}
	
	
	}
	
	
	
	public IModel<Site> getSiteModel() {
		return siteModel;
	}

	public void setSiteModel(IModel<Site> siteModel) {
		this.siteModel = siteModel;
	}
}
