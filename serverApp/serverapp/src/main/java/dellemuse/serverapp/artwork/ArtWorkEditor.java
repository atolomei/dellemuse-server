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
import org.apache.wicket.model.Model;
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
import dellemuse.serverapp.serverdb.model.Artist;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.ObjectType;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.EditButtons;
import io.wktui.form.field.ChoiceField;
import io.wktui.form.field.FileUploadSimpleField;
import io.wktui.form.field.MultipleSelectField;
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
	 

	private NumberField<Integer> c_numberField;
	private IModel<Resource> photoModel;

	private boolean uploadedPhoto = false;
	private List<Long> mainArtists;
	private IModel<Site> siteModel;

	private MultipleSelectField<Artist> mArtistField;

	private List<IModel<Artist>> selected;
	private List<IModel<Artist>> choices;

	private TextField<String> sourceField;
	private TextField<String> epochField;
	private ChoiceField<ObjectType> objectTypeField;
	
	
	public ArtWorkEditor(String id, IModel<ArtWork> model) {
		super(id, model);
	}

	/**
	public void setMainArtist(Long id) {
		mainArtists.add(id);
	}

	public Long getMainArtist() {
		if (mainArtists != null && mainArtists.size() > 0)
			return mainArtists.get(0);
		return null;
	}**/

	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();

		add(new InvisiblePanel("error"));

		Form<ArtWork> form = new Form<ArtWork>("form");
		
		add(form);
		setForm(form);

		Set<Artist> set = getModel().getObject().getArtists();

		if (set != null && set.size() > 0) {
			//setMainArtist(set.iterator().next().getId());
			set.forEach(i -> selected.add(new ObjectModel<Artist>(i)));
		}

	 	this.objectTypeField = new ChoiceField<ObjectType>("objectType", new PropertyModel<ObjectType>(getModel(), "objectType"), getLabel("objectType")) {

			private static final long serialVersionUID = 1L;

			@Override
			public IModel<List<ObjectType>> getChoices() {
				return new ListModel<ObjectType>(getObjectTypes());
			}

			@Override
			protected String getDisplayValue(ObjectType value) {
				if (value == null)
					return null;
				return value.getLabel(getLocale());
			}
		};
		form.add(this.objectTypeField);
		
		 
		
		mArtistField = new MultipleSelectField<Artist>("artists", selected, getLabel("artist"), getChoices()) {

			private static final long serialVersionUID = 1L;

			@Override
			protected IModel<String> getObjectTitle(IModel<Artist> model) {
				return ArtWorkEditor.this.getObjectTitle(model.getObject());
			}

			@Override
			protected IModel<String> getObjectSubtitle(IModel<Artist> model) {
				return ArtWorkEditor.this.getObjectSubtitle(model.getObject());
			}
			
			@Override
			protected void onObjectRemove(IModel<Artist> model, AjaxRequestTarget target) {
				ArtWorkEditor.this.getSelected().remove(model);
				target.add(getForm());
			}
		
			@Override
			protected void onObjectSelect(IModel<Artist> model, AjaxRequestTarget target) {
				ArtWorkEditor.this.getSelected().add(model);
				target.add(getForm());
			}
			
			@Override
			public boolean isVisible() {
				return ArtWorkEditor.this.getModel().getObject().getObjectType()==ObjectType.ARTWORK;
			}
		};
		
		this.sourceField 	= new TextField<String>("source", new PropertyModel<String>(getModel(), "source"), getLabel("source"));
		this.epochField 	= new TextField<String>("epoch", new PropertyModel<String>(getModel(), "epoch"), getLabel("epoch"));
		this.urlField 		= new TextField<String>("url", new PropertyModel<String>(getModel(), "url"), getLabel("url"));
		this.specField 		= new TextAreaField<String>("spec", new PropertyModel<String>(getModel(), "spec"), getLabel("spec"), 8);
		this.nameField 		= new TextField<String>("name", new PropertyModel<String>(getModel(), "name"), getLabel("name"));
		this.infoField 		= new TextAreaField<String>("info", new PropertyModel<String>(getModel(), "info"), getLabel("info"), 20);
		this.photoField 	= new FileUploadSimpleField<Resource>("photo", getPhotoModel(), getLabel("photo")) {

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
		
		form.add(mArtistField);
		form.add(sourceField);
		form.add(epochField);
		form.add(urlField);
		form.add(specField);
		form.add(nameField);
		form.add(infoField);
		form.add(photoField);
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

	protected List<ObjectType> getObjectTypes() {
		return ObjectType.getValues();
	}

	public List<IModel<Artist>> getSelected() {
		return selected;
	}

	public void setSelected(List<IModel<Artist>> selected) {
		this.selected = selected;
	}

	public Optional<Artist> getArtist(Long value) {
		return super.getArtist(value);
	}

	public void onCancel(AjaxRequestTarget target) {
		super.cancel(target);
	}

	public void onEdit(AjaxRequestTarget target) {
		super.edit(target);
	}

	public void onSave(AjaxRequestTarget target) {

		try {

			if (getUpdatedParts() == null || getUpdatedParts().size() == 0) {
				target.add(this);
				return;
			}

			getUpdatedParts().forEach(s -> logger.debug(s));

			if (this.getSelected() != null) {
				Set<Artist> set = new HashSet<Artist>();
				getSelected().forEach(i-> set.add(i.getObject()));
				getModel().getObject().setArtists(set);
			}
			else {
				getModel().getObject().setArtists(null);
			
			}
			
			save(getModelObject(), getSessionUser().get(), getUpdatedParts());

			this.uploadedPhoto = false;
			
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

		if (siteModel != null)
			siteModel.detach();
	}
	
	public IModel<Site> getSiteModel() {
		return siteModel;
	}

	public void setSiteModel(IModel<Site> siteModel) {
		this.siteModel = siteModel;
	}
	public List<IModel<Artist>> getChoices() {
		return choices;
	}

	public void setChoices(List<IModel<Artist>> choices) {
		this.choices = choices;
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
					addOrReplace(new SimpleAlertRow<Void>("error", e));
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
		
		mainArtists = new ArrayList<Long>();
		selected = new ArrayList<IModel<Artist>>();
		choices = new ArrayList<IModel<Artist>>();
		
		getArtistDBService().findAllSorted().forEach(a -> choices.add(new ObjectModel<Artist>(a)));

		
	}


}
