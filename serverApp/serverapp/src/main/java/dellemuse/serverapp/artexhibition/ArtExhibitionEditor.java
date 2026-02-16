package dellemuse.serverapp.artexhibition;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
 
import java.time.ZonedDateTime;
 
import java.util.ArrayList;
 
import java.util.List;
import java.util.Locale;
import java.util.Optional;
 

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
 
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
 
import dellemuse.serverapp.editor.DBSiteObjectEditor;
import dellemuse.serverapp.editor.ObjectUpdateEvent;
import dellemuse.serverapp.editor.SimpleAlertRow;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
 
import dellemuse.serverapp.service.DTFormatter;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.EditButtons;
import io.wktui.form.field.ChoiceField;
import io.wktui.form.field.FileUploadSimpleField;
import io.wktui.form.field.NumberField;
import io.wktui.form.field.TextAreaField;
import io.wktui.form.field.TextField;
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.panel.SimpleHelpPanel;
import wktui.base.InvisiblePanel;

/**
 * horario información técnica alter table artexhibition add column spec text;
 */
public class ArtExhibitionEditor extends DBSiteObjectEditor<ArtExhibition> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ArtExhibitionEditor.class.getName());

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
	private IModel<Resource> photoModel;
	private NumberField<Integer> ordinalield;
	private boolean uploadedPhoto = false;

	private String from;
	private String to;

	private IModel<Site> siteModel;
	private List<ToolbarItem> x_list;

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

		
		add( new Label( "exhibition-general-info", getLabel("exhibition-general-info", getModel().getObject().getMasterLanguage())));
		
		
		add(new InvisiblePanel("error"));

		Form<ArtExhibition> form = new Form<ArtExhibition>("form");

		add(form);
		setForm(form);

		if (getModel().getObject().getFromDate() != null)
			setFrom(getDateTimeService().format(getModel().getObject().getFromDate(), DTFormatter.day_of_year));

		if (getModel().getObject().getToDate() != null)
			setTo(getDateTimeService().format(getModel().getObject().getToDate(), DTFormatter.day_of_year));

		permanentField = new ChoiceField<Boolean>("permanent", new PropertyModel<Boolean>(getModel(), "permanent"), getLabel("duration")) {

			private static final long serialVersionUID = 1L;

			@Override
			public IModel<List<Boolean>> getChoices() {
				return new ListModel<Boolean>(b_list);
			}

			@Override
			protected String getDisplayValue(Boolean value) {
				if (value == null)
					return null;
				if (value.booleanValue())
					return getLabel("permanent").getObject();
				return getLabel("temporary").getObject();
			}
		};

		specField = new TextAreaField<String>("spec", new PropertyModel<String>(getModel(), "spec"), getLabel("spec"), 4);
		opensField = new TextAreaField<String>("opens", new PropertyModel<String>(getModel(), "opens"), getLabel("opens"), 4);
		mapField = new TextField<String>("map", new PropertyModel<String>(getModel(), "map"), getLabel("map"));
		
		fromField = new TextField<String>("from", new PropertyModel<String>(this, "from"), getLabel("from")) {
			private static final long serialVersionUID = 1L;

			public boolean isVisible() {
				return !ArtExhibitionEditor.this.getModel().getObject().isPermanent();
			}
			
			public boolean isEnabled() {
				return !ArtExhibitionEditor.this.getModel().getObject().isPermanent();
			}
		};

	
		
		toField = new TextField<String>("to", new PropertyModel<String>(this, "to"), getLabel("to")) {
			private static final long serialVersionUID = 1L;

			public boolean isVisible() {
				return !ArtExhibitionEditor.this.getModel().getObject().isPermanent();
			}
			
			public boolean isEnabled() {
				return !ArtExhibitionEditor.this.getModel().getObject().isPermanent();
			}
		};
		
		fromField.setHelpPanel( new SimpleHelpPanel<>("help") {
			public IModel<String> getLinkLabel() {
				return ArtExhibitionEditor.this.getLabel("date-help-label");
			}
			
			public IModel<String> getHelpText() {
				return ArtExhibitionEditor.this.getLabel("date-format-help");
			}
		});
 
		
		
		toField.setHelpPanel( new SimpleHelpPanel<>("help") {
			public IModel<String> getLinkLabel() {
				return ArtExhibitionEditor.this.getLabel("date-help-label");
			}
			
			public IModel<String> getHelpText() {
				return ArtExhibitionEditor.this.getLabel("date-format-help");
			}
		});

		

		urlField = new TextField<String>("url", new PropertyModel<String>(getModel(), "website"), getLabel("url"));
		nameField = new TextField<String>("name", new PropertyModel<String>(getModel(), "name"), getLabel("name"));
		shortField = new TextField<String>("shortname", new PropertyModel<String>(getModel(), "shortname"), getLabel("shortname"));
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
				if ((getPhotoModel() == null) || (getPhotoModel().getObject() == null))
					return null;
				return ArtExhibitionEditor.this.getThumbnail(getPhotoModel().getObject());
			}

			public String getFileName() {
				if ((getModel() != null) && (getModel().getObject() != null))
					return ArtExhibitionEditor.this.getPhotoMeta(getModel().getObject());
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
		
		
	 
		ordinalield = new NumberField<Integer>("ordinal", new PropertyModel<Integer>(getModel(), "ordinal"), getLabel("ordinal"));
	
		form.add(ordinalield);
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
				
				if (!hasWritePermission())
					return false;
				
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

	protected void onCancel(AjaxRequestTarget target) {
		super.cancel(target);
	}

	public void onEdit(AjaxRequestTarget target) {
		super.edit(target);
	}

	protected void onSave(AjaxRequestTarget target) {

		getUpdatedParts().forEach(s -> logger.debug(s));

		
		if( getUpdatedParts()==null || getUpdatedParts().size()==0) {
		
			return;
		}
		
		logger.debug("saving...");

		if (getSiteModel().getObject().getZoneId() == null)
			throw new IllegalArgumentException("zone id is null");

		if (getSiteModel().getObject().getLanguage() == null)
			throw new IllegalArgumentException("Language is null");

		ZoneId zoneId = ZoneId.of(getSiteModel().getObject().getZoneIdStr());

		try {

			if (fromField.isUpdated()) {
				if (getFrom() != null) {
					String la = getModel().getObject().getLanguage();
					logger.debug(la);
					LocalDate d_from = getDateTimeService().parseFlexibleDate(getFrom(), Locale.forLanguageTag(getModel().getObject().getLanguage()));
					LocalTime localTime = LocalTime.MIN; // 00:00:00
					java.time.LocalDateTime localDateTime = java.time.LocalDateTime.of(d_from, localTime);
					ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
					OffsetDateTime offsetDateTime = zonedDateTime.toOffsetDateTime();
					getModel().getObject().setFromDate(offsetDateTime);
					setFrom(getDateTimeService().format(offsetDateTime, DTFormatter.day_of_year));
				}
			}
			
			if (toField.isUpdated()) {
				if (getTo() != null) {
					LocalDate d_to = getDateTimeService().parseFlexibleDate(getTo(), Locale.forLanguageTag(getModel().getObject().getLanguage()));
					LocalTime localTime = LocalTime.MIN; // 00:00:00
					java.time.LocalDateTime localDateTime = java.time.LocalDateTime.of(d_to, localTime);
					ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
					OffsetDateTime offsetDateTime = zonedDateTime.toOffsetDateTime();
					getModel().getObject().setToDate(offsetDateTime);
					setTo(getDateTimeService().format(offsetDateTime, DTFormatter.day_of_year));
				}
			}
			save(getModelObject(), getSessionUser().get(), getUpdatedParts());
			this.uploadedPhoto = false;
			getForm().setFormState(FormState.VIEW);
			getForm().updateReload();
			fireScanAll(new ObjectUpdateEvent(target));

		} catch (Exception e) {
			addOrReplace(new SimpleAlertRow<Void>("error", e));
			getForm().setFormState(FormState.VIEW);
			logger.error(e);
		}
		target.add(this);
	}

	@Override
	public List<ToolbarItem> getToolbarItems() {

		if (x_list != null)
			return x_list;

		x_list = new ArrayList<ToolbarItem>();

		AjaxButtonToolbarItem<ArtExhibition> create = new AjaxButtonToolbarItem<ArtExhibition>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
				fire(new MenuAjaxEvent(ServerAppConstant.action_exhibition_info_edit, target));
			}

			@Override
			public IModel<String> getButtonLabel() {
				return getLabel("edit");
			}
		};
		create.setAlign(Align.TOP_LEFT);
		x_list.add(create);
		return x_list;
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
