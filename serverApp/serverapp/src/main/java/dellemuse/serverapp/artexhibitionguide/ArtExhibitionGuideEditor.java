package dellemuse.serverapp.artexhibitionguide;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.media.audio.Audio;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;
import org.threeten.bp.OffsetDateTime;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.artexhibition.ArtExhibitionPage;
import dellemuse.serverapp.artwork.ArtWorkPage;
import dellemuse.serverapp.audiostudio.AudioStudioPage;
import dellemuse.serverapp.editor.DBObjectEditor;
import dellemuse.serverapp.editor.DBSiteObjectEditor;
import dellemuse.serverapp.editor.ObjectMarkAsDeleteEvent;
import dellemuse.serverapp.editor.ObjectRestoreEvent;
import dellemuse.serverapp.editor.ObjectUpdateEvent;
import dellemuse.serverapp.editor.SimpleAlertRow;

import dellemuse.serverapp.page.InternalPanel;

import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.AudioStudio;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.service.DTFormatter;
import io.wktui.error.AlertPanel;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.event.SimpleAjaxWicketEvent;
import io.wktui.event.UIEvent;
import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.EditButtons;

import io.wktui.form.field.ChoiceField;
import io.wktui.form.field.FileUploadSimpleField;
import io.wktui.form.field.StaticTextField;
import io.wktui.form.field.TextAreaField;
import io.wktui.form.field.TextField;
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import wktui.base.InvisiblePanel;

/**
 * 
 * 
 * alter table artexhibition add column spec text;
 * 
 */
public class ArtExhibitionGuideEditor extends DBSiteObjectEditor<ArtExhibitionGuide> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ArtExhibitionGuideEditor.class.getName());

	private StaticTextField<String> audioIdField;
	private TextField<String> nameField;
	private TextField<String> subtitleField;
	private TextAreaField<String> infoField;

	private FileUploadSimpleField<Resource> audioField;

	private IModel<Resource> photoModel;
	private IModel<Resource> audioModel;

	private boolean uploadedPhoto = false;
	private boolean uploadedAudio = false;

	private IModel<Site> siteModel;
	private IModel<ArtExhibition> artExhibitionModel;

	private Link<ArtExhibitionGuide> openAudioStudio;

	private String audioMeta;
	private boolean alertInfo = false;

	/**
	 * @param id
	 * @param model
	 */
	public ArtExhibitionGuideEditor(String id, IModel<ArtExhibitionGuide> model, IModel<Site> siteModel) {
		super(id, model);
		this.siteModel = siteModel;
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();
		addAlert();

		add(new InvisiblePanel("error"));

		Form<ArtExhibitionGuide> form = new Form<ArtExhibitionGuide>("form");

		add(form);
		setForm(form);

		this.nameField = new TextField<String>("name", new PropertyModel<String>(getModel(), "name"), getLabel("name"));
		this.subtitleField = new TextField<String>("subtitle", new PropertyModel<String>(getModel(), "subtitle"), getLabel("subtitle"));
		this.infoField = new TextAreaField<String>("info", new PropertyModel<String>(getModel(), "info"), getLabel("info"), 12);
		this.audioField = new FileUploadSimpleField<Resource>("audio", getAudioModel(), getLabel("audio")) {

			private static final long serialVersionUID = 1L;

			protected boolean processFileUploads(List<FileUpload> uploads) {
				return ArtExhibitionGuideEditor.this.processAudioUpload(uploads);
			}

			public Image getImage() {
				return null;
			}

			protected String getAudioSrc() {

				if (getAudioModel() == null || getAudioModel().getObject() == null)
					return null;
				return ArtExhibitionGuideEditor.this.getPresignedUrl(getAudioModel().getObject());
			}

			public String getFileName() {

				if (getAudioModel() != null || getAudioModel().getObject() == null) {
					if (audioMeta == null)
						audioMeta = ArtExhibitionGuideEditor.this.getAudioMeta(getAudioModel().getObject());
					return audioMeta;
				}
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

		audioIdField = new StaticTextField<String>("audioid", new PropertyModel<String>(getModel(), "audioId"), getLabel("audioid"));

		form.add(audioIdField);

		form.add(nameField);
		form.add(subtitleField);
		form.add(infoField);
		form.add(audioField);

		EditButtons<ArtExhibitionGuide> buttons = new EditButtons<ArtExhibitionGuide>("buttons-bottom", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onEdit(AjaxRequestTarget target) {
				ArtExhibitionGuideEditor.this.onEdit(target);
			}

			@Override
			public void onCancel(AjaxRequestTarget target) {
				ArtExhibitionGuideEditor.this.onCancel(target);
			}

			@Override
			public void onSave(AjaxRequestTarget target) {
				ArtExhibitionGuideEditor.this.onSave(target);
			}

			@Override
			public boolean isVisible() {
				
				if (!hasWritePermission())
					return false;
				
				return getForm().getFormState() == FormState.EDIT;
			}
		};
		form.add(buttons);

		EditButtons<ArtExhibitionGuide> b_buttons_top = new EditButtons<ArtExhibitionGuide>("buttons-top", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			public void onEdit(AjaxRequestTarget target) {
				ArtExhibitionGuideEditor.this.onEdit(target);
			}

			public void onCancel(AjaxRequestTarget target) {
				ArtExhibitionGuideEditor.this.onCancel(target);
			}

			public void onSave(AjaxRequestTarget target) {
				ArtExhibitionGuideEditor.this.onSave(target);
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

		this.openAudioStudio = new Link<ArtExhibitionGuide>("openAudioStudio", getModel()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				Optional<AudioStudio> oa = getAudioStudioDBService().findOrCreate(getModel().getObject(), getSessionUser().get());
				if (oa.isPresent())
					setResponsePage(new AudioStudioPage(new ObjectModel<AudioStudio>(oa.get())));
			}

			public boolean isEnabled() {
				return true;
			}
		};

		Label openArtworkLabel = new Label("openAudioStudioLabel", getLabel("open-audio-studio", getArtExhibitionModel().getObject().getDisplayname()));
		this.openAudioStudio.add(openArtworkLabel);
		getForm().add(openAudioStudio);

		// addItemsPanel();
	}

	@Override
	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<SimpleAjaxWicketEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(SimpleAjaxWicketEvent event) {
				reloadModel();
				alertInfo = true;
				event.getTarget().add(ArtExhibitionGuideEditor.this);
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof ObjectMarkAsDeleteEvent)
					return true;
				return false;
			}
		});

		add(new io.wktui.event.WicketEventListener<SimpleAjaxWicketEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(SimpleAjaxWicketEvent event) {
				reloadModel();
				alertInfo = true;
				event.getTarget().add(ArtExhibitionGuideEditor.this);
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof ObjectRestoreEvent)
					return true;
				return false;
			}
		});
	}

	protected void importgetArtExhibitionText(AjaxRequestTarget target) {

		String info = getArtExhibitionModel().getObject().getInfo();

		boolean reload = false;

		if (info != null && getModel().getObject().getInfo() == null) {
			getModel().getObject().setInfo(info);
			reload = true;
		} else if (info == null && getModel().getObject().getInfo() != null) {
			getModel().getObject().setInfo(info);
			reload = true;
		} else {
			if (!info.trim().equals(getModel().getObject().getInfo().trim())) {
				getModel().getObject().setInfo(info);
				reload = true;
			}
		}

		String subtitle = getArtExhibitionModel().getObject().getSubtitle();

		if (subtitle != null && getModel().getObject().getSubtitle() == null) {
			getModel().getObject().setSubtitle(subtitle);
			getForm().updateReload();
		} else if (subtitle == null && getModel().getObject().getSubtitle() != null) {
			getModel().getObject().setSubtitle(subtitle);
			getForm().updateReload();
		} else {
			if (!subtitle.trim().equals(getModel().getObject().getSubtitle().trim())) {
				getModel().getObject().setSubtitle(subtitle);
				getForm().updateReload();
			}
		}

		if (reload)
			getForm().updateReload();

		target.add(getForm());
	}

	public boolean isAlertInfo() {
		return alertInfo;
	}

	public void setAlertInfo(boolean alertInfo) {
		this.alertInfo = alertInfo;
	}

	public void onEdit(AjaxRequestTarget target) {
		super.edit(target);
	}

	List<ToolbarItem> list;

	@Override
	public List<ToolbarItem> getToolbarItems() {

		if (list != null)
			return list;

		list = new ArrayList<ToolbarItem>();

		AjaxButtonToolbarItem<ArtExhibition> create = new AjaxButtonToolbarItem<ArtExhibition>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
				fire(new MenuAjaxEvent(ServerAppConstant.action_guide_edit_info, target));
			}

			@Override
			public IModel<String> getButtonLabel() {
				return getLabel("edit");
			}
		};
		create.setAlign(Align.TOP_LEFT);
		list.add(create);

		AjaxButtonToolbarItem<GuideContent> delete = new AjaxButtonToolbarItem<GuideContent>() {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return (ArtExhibitionGuideEditor.this.getModel().getObject().getState() != ObjectState.DELETED);
			}

			@Override
			protected void onCick(AjaxRequestTarget target) {
				fire(new MenuAjaxEvent(ServerAppConstant.action_artexhibitionguide_delete, target));
			}

			@Override
			public IModel<String> getButtonLabel() {
				return null;
			}

			@Override
			public IModel<String> getButtonTitle() {
				return ArtExhibitionGuideEditor.this.getLabel("delete");
			}

			@Override
			public String getIconCss() {
				return "fa-duotone fa-trash";
			}

			@Override
			protected String getButtonCss() {
				return "btn btn-sm btn-outline-primary mt-1 ms-1";
			}

		};
		delete.setAlign(Align.TOP_LEFT);
		list.add(delete);

		AjaxButtonToolbarItem<GuideContent> restore = new AjaxButtonToolbarItem<GuideContent>() {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return (ArtExhibitionGuideEditor.this.getModel().getObject().getState() == ObjectState.DELETED);
			}

			@Override
			protected void onCick(AjaxRequestTarget target) {
				fire(new MenuAjaxEvent(ServerAppConstant.action_artexhibitionguide_restore, target));
			}

			@Override
			public IModel<String> getButtonLabel() {
				return ArtExhibitionGuideEditor.this.getLabel("restore");
			}

			@Override
			public IModel<String> getButtonTitle() {
				return ArtExhibitionGuideEditor.this.getLabel("restore");
			}

			@Override
			public String getIconCss() {
				return "fa-solid fa-check";
			}

			@Override
			protected String getButtonCss() {
				return "btn btn-sm btn-outline-primary mt-1 ms-1";
			}

		};
		restore.setAlign(Align.TOP_LEFT);
		list.add(restore);

		return list;
	}
	
	@Override
	public IModel<Site> getSiteModel() {
		return siteModel;
	}

	public void setSiteModel(IModel<Site> siteModel) {
		this.siteModel = siteModel;
	}

	public IModel<ArtExhibition> getArtExhibitionModel() {
		return artExhibitionModel;
	}

	public void setArtExhibitionModel(IModel<ArtExhibition> siteModel) {
		this.artExhibitionModel = siteModel;
	}

	public void reloadModel() {
		setUpModel();
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (photoModel != null)
			photoModel.detach();

		if (audioModel != null)
			audioModel.detach();

		if (siteModel != null)
			siteModel.detach();

		if (artExhibitionModel != null)
			artExhibitionModel.detach();
	}

	protected void onCancel(AjaxRequestTarget target) {
		super.cancel(target);
		audioMeta = null;
	}

	protected IModel<Resource> getPhotoModel() {
		return this.photoModel;
	}

	protected void setPhotoModel(ObjectModel<Resource> model) {
		this.photoModel = model;
	}

	protected IModel<Resource> getAudioModel() {
		return this.audioModel;
	}

	protected void setAudioModel(ObjectModel<Resource> model) {
		this.audioModel = model;
	}

	protected void onSubmit() {
		logger.debug("");
		logger.debug("onSubmit");
		logger.debug("");
	}

	protected void onSave(AjaxRequestTarget target) {

		try {
			getUpdatedParts().forEach(s -> logger.debug(s));

			save(getModelObject(), getSessionUser().get(), getUpdatedParts());

			uploadedPhoto = false;
			uploadedAudio = false;
			audioMeta = null;

			getForm().setFormState(FormState.VIEW);
			getForm().updateReload();
			fire(new ObjectUpdateEvent(target));

		} catch (Exception e) {

			addOrReplace(new SimpleAlertRow<Void>("error", e));
			logger.error(e);

		}

		target.add(this);
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

	protected boolean processAudioUpload(List<FileUpload> uploads) {

		if (this.uploadedAudio)
			return false;

		if (uploads != null && !uploads.isEmpty()) {

			for (FileUpload upload : uploads) {

				try {

					logger.debug("name -> " + upload.getClientFileName());
					logger.debug("Size -> " + upload.getSize());

					String bucketName = ServerConstant.MEDIA_BUCKET;

					String objectName = getResourceDBService().normalizeFileName(FileNameUtils.getBaseName(upload.getClientFileName())) + "-" + String.valueOf(getResourceDBService().newId());

					Resource resource = createAndUploadFile(upload.getInputStream(), bucketName, objectName, upload.getClientFileName(), upload.getSize());

					setAudioModel(new ObjectModel<Resource>(resource));
					getModel().getObject().setAudio(resource);

					uploadedAudio = true;

				} catch (Exception e) {
					uploadedAudio = false;
					error("Error saving file: " + e.getMessage());
				}
			}
		} else {
			info("No file uploaded.");
			logger.debug("No file uploaded.");
		}

		return uploadedPhoto;
	}

	/**
	 * 
	 * 
	 */
	protected void setUpModel() {

		Optional<ArtExhibitionGuide> o_i = getArtExhibitionGuideDBService().findWithDeps(getModel().getObject().getId());
		setModel(new ObjectModel<ArtExhibitionGuide>(o_i.get()));

		if (getModel().getObject().getPhoto() != null) {
			Optional<Resource> o_r = getResourceDBService().findWithDeps(getModel().getObject().getPhoto().getId());
			setPhotoModel(new ObjectModel<Resource>(o_r.get()));
		}

		if (getModel().getObject().getAudio() != null) {
			Optional<Resource> o_r = getResourceDBService().findWithDeps(getModel().getObject().getAudio().getId());
			setAudioModel(new ObjectModel<Resource>(o_r.get()));
		}

		if (getModel().getObject().getArtExhibition() != null) {

			Optional<ArtExhibition> o_ae = getArtExhibitionDBService().findWithDeps(getModel().getObject().getArtExhibition().getId());
			setArtExhibitionModel(new ObjectModel<ArtExhibition>(o_ae.get()));

			if (getSiteModel().getObject() != null) {
				Optional<Site> o_s = getSiteDBService().findWithDeps(getArtExhibitionModel().getObject().getSite().getId());
				setSiteModel(new ObjectModel<Site>(o_s.get()));
			}
		}
	}

	private void addAlert() {

		SimpleAlertRow<Void> alert = new SimpleAlertRow<Void>("simpleAlertRow") {
			private static final long serialVersionUID = 1L;

			public boolean isVisible() {
				return ArtExhibitionGuideEditor.this.isAlertInfo();
			}

			protected IModel<String> getText() {
				String s = ArtExhibitionGuideEditor.this.getLabel("artexhibition-guide-info").getObject();
				return ArtExhibitionGuideEditor.this.getLabel("object-deleted", s);
			}

			public int getAlertType() {
				return AlertPanel.WARNING;
			}
		};
		add(alert);

		this.alertInfo = getModel().getObject().getState() == ObjectState.DELETED;

	}

}
