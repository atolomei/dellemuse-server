package dellemuse.serverapp.page.site;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.ListModel;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.NumberFormatter;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.editor.DBObjectEditor;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.DBService;
import dellemuse.serverapp.serverdb.service.InstitutionDBService;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.EditButtons;
import io.wktui.form.button.SubmitButton;
import io.wktui.form.field.BooleanField;
import io.wktui.form.field.ChoiceField;
import io.wktui.form.field.FileUploadSimpleField;
import io.wktui.form.field.TextAreaField;
import io.wktui.form.field.TextField;
import jakarta.transaction.Transactional;
import wktui.base.ModelPanel;

public class ArtWorkEditor extends DBObjectEditor<ArtWork> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ArtWorkEditor.class.getName());


	static private final List<Boolean> b_list = new ArrayList<Boolean>();
	static {
		 b_list.add(Boolean.TRUE );
		 b_list.add(Boolean.FALSE);
	}

	
	private TextField<String> nameField;
	private TextAreaField<String> infoField;
	private FileUploadSimpleField<Resource> photoField;
	private ChoiceField<Long> artistField;
	private ChoiceField<Boolean> c_useThumbnailField;

	//private TextAreaField<String> introField;
	//private BooleanField useThumbnailField;
	
	private IModel<Resource> photoModel;

	private boolean uploadedPhoto = false;

	/**
	 * @param id
	 * @param model
	 */
	public ArtWorkEditor(String id, IModel<ArtWork> model) {
		super(id, model);
	}

	private void setUpModel() {

		Optional<ArtWork> o_i = getArtWorkDBService().findByIdWithDeps(getModel().getObject().getId());
		setModel(new ObjectModel<ArtWork>(o_i.get()));

		if (getModel().getObject().getPhoto() != null) {
			Optional<Resource> o_r = getResourceDBService().findByIdWithDeps(getModel().getObject().getPhoto().getId());
			setPhotoModel(new ObjectModel<Resource>(o_r.get()));
		}
	}
	
 

	public void setMainArtist(Person p) {
		Set<Person> set = new HashSet<>();
		set.add(p);
		getModel().getObject().setArtists(set);
	}

	public Long getMainArtist() {
		
		 if (getModel()==null)
			 return null;
		 
		
		Set<Person> list = getModel().getObject().getArtists();
		
		if (list==null || list.isEmpty())
			return null;
		
		return list.iterator().next().getId();
		
	}
	
	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();

		Form<ArtWork> form = new Form<ArtWork>("form");
		add(form);
		setForm(form);

		//      introField = new TextAreaField<String>("intro", new PropertyModel<String>(getModel(), "intro"),
		//		getLabel("intro"), 8);
		//      useThumbnailField = new BooleanField("usethumbnail", new PropertyModel<Boolean>(getModel(), "info"),
		//		getLabel("usethumbnail"));
				
		
		nameField = new TextField<String>("name", new PropertyModel<String>(getModel(), "name"), getLabel("name"));
		infoField = new TextAreaField<String>("info", new PropertyModel<String>(getModel(), "info"), getLabel("info"), 30);
		photoField = new FileUploadSimpleField<Resource>("photo", getPhotoModel(), getLabel("photo")) {

			private static final long serialVersionUID = 1L;

			protected boolean processFileUploads(List<FileUpload> uploads) {
				return ArtWorkEditor.this.processPhotoUpload(uploads);
			}

			public Image getImage() {
				if (getPhotoModel() == null)
					return null;
				return ArtWorkEditor.this.getThumbnail(getModel().getObject());
			}

			public String getFileName() {
				if (getModel()!=null)
					return ArtWorkEditor.this.getPhotoMeta( getModel().getObject() );
				return null;
			}

			public boolean isThumbnail() {
				return true;
			}
		};

		c_useThumbnailField = new ChoiceField<Boolean>("usethumbnail", new PropertyModel<Boolean>(getModel(), "usethumbnail"), getLabel("usethumbnail")) {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public IModel<List<Boolean>> getChoices() {
				return new ListModel<Boolean> (b_list);
			}
			
			@Override
			protected String getDisplayValue(Boolean value) {
				if (value==null)
					return null;
				if (value.booleanValue())
					return getLabel("yes").getObject();
				return getLabel("no").getObject();
			}
		};
		 
		
		artistField = new ChoiceField<Long>("artist", new PropertyModel<Long>( this, "mainArtist"), getLabel("artist")) {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public IModel<List<Long>> getChoices() {
				List<Long> list = new ArrayList<Long>();
				StreamSupport.stream(getPersons().spliterator(), false).collect(Collectors.toList()).forEach( i -> list.add( i.getId()));
				return new ListModel<Long>(list); 
			}
			
			@Override
			protected String getDisplayValue(Long value) {

				if (value==null)
					return null;
				
				Optional<Person> o=getPerson( value );
				
				if (o.isPresent())
					return getPerson( value ).get().getDisplayName();
			
				return "";
			}
		};
		 
		form.add(nameField);
		form.add(infoField);
		form.add(photoField);
		form.add(c_useThumbnailField);
		form.add(artistField);

		
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

	protected void onCancel(AjaxRequestTarget target) {
		super.cancel(target);
		// getForm().setFormState(FormState.VIEW);
		// target.add(getForm());
	}

	protected void onEdit(AjaxRequestTarget target) {
		super.edit(target);
		// getForm().setFormState(FormState.EDIT);
		// target.add(getForm());
	}

	protected void onSave(AjaxRequestTarget target) {
		logger.debug("onSave");
		logger.debug("updated parts:");
		getUpdatedParts().forEach(s -> logger.debug(s));
		logger.debug("saving...");
		
		Long id=artistField.getModel().getObject();
		Person person = getPerson(id).get();
		Set<Person> set = new HashSet<Person>();
		set.add(person);
		getModel().getObject().setArtists(set);
		
		save(getModelObject());
		uploadedPhoto = false;
		getForm().setFormState(FormState.VIEW);
		logger.debug("done");
		target.add(this);
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (photoModel != null)
			photoModel.detach();
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

}
