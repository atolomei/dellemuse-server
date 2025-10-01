package dellemuse.serverapp.artexhibition;

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
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.model.ObjectWithDepModel;
import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.page.site.ArtWorkEditor;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.DBService;
import dellemuse.serverapp.serverdb.service.InstitutionDBService;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.event.SimpleAjaxWicketEvent;
import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.EditButtons;
import io.wktui.form.button.SubmitButton;
import io.wktui.form.field.BooleanField;
import io.wktui.form.field.ChoiceField;
import io.wktui.form.field.FileUploadSimpleField;
import io.wktui.form.field.TextAreaField;
import io.wktui.form.field.TextField;
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import jakarta.transaction.Transactional;
import wktui.base.ModelPanel;


/**
 * 
 * 
 * alter table artexhibition add column spec text;
 * 
 */
public class ArtExhibitionEditor extends DBObjectEditor<ArtExhibition> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ArtExhibitionEditor.class.getName());

	private ArtExhibitionItemsPanel itemsPanel;
	
	static private final List<Boolean> b_list = new ArrayList<Boolean>();
	static {
		 b_list.add(Boolean.TRUE );
		 b_list.add(Boolean.FALSE);
	}
	

	private ChoiceField<ObjectState> objectStateField;
	
	private TextAreaField<String> opensField;

	private TextField<String> urlField;

	private TextField<String> nameField;
	private TextField<String> shortField;
	
	private TextField<String> subtitleField;
	private TextAreaField<String> specField;

	private TextAreaField<String> locationField;
	private TextAreaField<String> infoField;
	private TextAreaField<String> introField;
	private FileUploadSimpleField<Resource> photoField;

	private ChoiceField<Boolean> permanentField;

	private TextField<String> mapField;
	
	private TextField<String> fromField;
	private TextField<String> toField;
	
	private ChoiceField<Person> artistField;
	private ChoiceField<Boolean> c_useThumbnailField;

	
	//private TextAreaField<String> introField;
	//private BooleanField useThumbnailField;
	
	private IModel<Resource> photoModel;

	private boolean uploadedPhoto = false;

	
	private String from;
	private String to;
	
	IModel<Site> siteModel;
	
	/**
	 * @param id
	 * @param model
	 */
	public ArtExhibitionEditor(String id, IModel<ArtExhibition> model) {
		super(id, model);
	}

 

	
	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();

		Form<ArtExhibition> form = new Form<ArtExhibition>("form");
		add(form);
		setForm(form);
		
		objectStateField = new ChoiceField<ObjectState>("state", new PropertyModel<ObjectState>(getModel(), "state"), getLabel("state")) {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public IModel<List<ObjectState>> getChoices() {
				return new ListModel<ObjectState> (b_state);
			}
			
			@Override
			protected String getDisplayValue(ObjectState value) {
				if (value==null)
					return null;
				return value.getLabel( getLocale() );
			}
		};
		form.add(objectStateField);
		
		
		permanentField = new ChoiceField<Boolean>("permanent", new PropertyModel<Boolean>(getModel(), "permanent"), getLabel("duration")) {
			
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
					return getLabel("permanent").getObject();
				return getLabel("temporary").getObject();
			}
		};


		
		specField =  new TextAreaField<String>("spec", new PropertyModel<String>(getModel(), "spec"), getLabel("spec"), 4);
		opensField =  new TextAreaField<String>("opens", new PropertyModel<String>(getModel(), "opens"), getLabel("opens"), 4);
		mapField = new TextField<String>("map", new PropertyModel<String>(getModel(), "map"), getLabel("map"));
		fromField = new TextField<String>("from", new PropertyModel<String>(this, "from"), getLabel("from"));
		toField = new TextField<String>("to", new PropertyModel<String>(this, "to"), getLabel("to"));
		urlField = new TextField<String>("url", new PropertyModel<String>(getModel(), "website"), getLabel("url"));
		nameField = new TextField<String>("name", new PropertyModel<String>(getModel(), "name"), getLabel("name"));
		shortField= new TextField<String>("shortname", new PropertyModel<String>(getModel(), "shortname"), getLabel("shortname"));
		
		
		
		
		subtitleField = new TextField<String>("subtitle", new PropertyModel<String>(getModel(), "subtitle"), getLabel("subtitle"));
		locationField = new TextAreaField<String>("location", new PropertyModel<String>(getModel(), "location"), getLabel("location"), 3);
		introField = new TextAreaField<String>("intro", new PropertyModel<String>(getModel(), "intro"), getLabel("intro"), 5);
		infoField = new TextAreaField<String>("info", new PropertyModel<String>(getModel(), "info"), getLabel("info"), 12);
		
		photoField = new FileUploadSimpleField<Resource>("photo", getPhotoModel(), getLabel("photo")) {

			private static final long serialVersionUID = 1L;

			protected boolean processFileUploads(List<FileUpload> uploads) {
				return ArtExhibitionEditor.this.processPhotoUpload(uploads);
			}

			public Image getImage() {
				if (getPhotoModel() == null || getPhotoModel().getObject()==null )
					return null;
				return ArtExhibitionEditor.this.getThumbnail(getPhotoModel().getObject());
			}

			public String getFileName() {
				if (getModel()!=null  || getModel().getObject()==null )
					return ArtExhibitionEditor.this.getPhotoMeta( getModel().getObject() );
				return null;
			}

			public boolean isThumbnail() {
				return true;
			}
		};


	
		
		form.add(specField);
		form.add(locationField);

		form.add(opensField);
		form.add(mapField);
		form.add(permanentField);
	 	
		form.add(nameField);
		form.add(shortField);
		form.add(subtitleField);
	 	
		form.add(infoField);
		form.add(introField);

		form.add(photoField);
		form.add(urlField);
		form.add(fromField);
		form.add(toField);
		
		EditButtons<ArtExhibition> buttons = new EditButtons<ArtExhibition>("buttons-bottom", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onEdit(AjaxRequestTarget target) {
				ArtExhibitionEditor.this.onEdit(target);
			}

			@Override
			public void onCancel(AjaxRequestTarget target) {
				ArtExhibitionEditor.this.onCancel(target);
			}

			@Override
			public void onSave(AjaxRequestTarget target) {
				ArtExhibitionEditor.this.onSave(target);
			}

			@Override
			public boolean isVisible() {
				return getForm().getFormState() == FormState.EDIT;
			}
		};
		form.add(buttons);
		
		

		EditButtons<ArtExhibition> b_buttons_top = new EditButtons<ArtExhibition>("buttons-top", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			public void onEdit(AjaxRequestTarget target) {
				 ArtExhibitionEditor.this.onEdit(target);
			}

			public void onCancel(AjaxRequestTarget target) {
				ArtExhibitionEditor.this.onCancel(target);
			}

			public void onSave(AjaxRequestTarget target) {
				ArtExhibitionEditor.this.onSave(target);
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
		
		//addItemsPanel();
		
	}

	
	private void addItemsPanel() {
		
		if (itemsPanel==null) {
			itemsPanel =  new ArtExhibitionItemsPanel("itemsPanel", getModel(),  getSiteModel() );
		}
		add(itemsPanel);
	
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
		logger.debug("done");
		target.add(this);
	
	}

	
	
	@Override
	public List<ToolbarItem> getToolbarItems() {
		
	List<ToolbarItem> list = new ArrayList<ToolbarItem>();
		
		AjaxButtonToolbarItem<ArtExhibition> create = new AjaxButtonToolbarItem<ArtExhibition>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
 				fire(new SimpleAjaxWicketEvent(ServerAppConstant.action_site_edit, target));
			}
			@Override
			public IModel<String> getButtonLabel() {
				return getLabel("edit");
			}
		};
		create.setAlign(Align.TOP_LEFT);
		list.add(create);

		
		/**
		  
		AjaxButtonToolbarItem<ArtExhibition> items = new AjaxButtonToolbarItem<ArtExhibition>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
 				fire(new SimpleAjaxWicketEvent(ServerAppConstant.action_items, target));
			}
			@Override
			public IModel<String> getButtonLabel() {
				return getLabel("items");
			}
		};
		create.setAlign(Align.TOP_LEFT);
		list.add(items);
		 **/
		
		return list;
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


	private void setUpModel() {

		Optional<ArtExhibition> o_i = getArtExhibitionDBService().findWithDeps(getModel().getObject().getId());
		setModel(new ObjectModel<ArtExhibition>(o_i.get()));

		if (getModel().getObject().getPhoto() != null) {
			Optional<Resource> o_r = getResourceDBService().findWithDeps(getModel().getObject().getPhoto().getId());
			setPhotoModel(new ObjectModel<Resource>(o_r.get()));
		}
		
		if (getModel().getObject().getSite() != null) {
			Optional<Site> o_s = getSiteDBService().findWithDeps(getModel().getObject().getSite().getId());
			setSiteModel(new ObjectModel<Site>(o_s.get()));
		}
		
		
	}



	public String getFrom() {
		return from;
	}


	public String getTo() {
		return to;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setTo(String to) {
		this.to = to;
	}




	public IModel<Site> getSiteModel() {
		return siteModel;
	}




	public void setSiteModel(IModel<Site> siteModel) {
		this.siteModel = siteModel;
	}
	
	
}
