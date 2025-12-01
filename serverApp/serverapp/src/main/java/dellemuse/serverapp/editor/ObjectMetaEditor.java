package dellemuse.serverapp.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.audit.AuditKey;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.DelleMuseObject;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.TranslateMode;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.DBService;
import io.wktui.error.ErrorPanel;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.EditButtons;
import io.wktui.form.field.ChoiceField;

import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import wktui.base.InvisiblePanel;

public class ObjectMetaEditor<T extends DelleMuseObject> extends DBObjectEditor<T> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ObjectMetaEditor.class.getName());

	private ChoiceField<ObjectState> objectStateField;
	private ChoiceField<Boolean> audioModeField;

	private ChoiceField<Language> masterLanguageField;
	private ChoiceField<TranslateMode> translateMode;

	private boolean isAudioAutoGenerate = false;
	private boolean isLanguage = false;
	
	private Language masterLanguage;
	private List<ToolbarItem> list;
	
	/**
	 * @param id
	 * @param model
	 */
	public ObjectMetaEditor(String id, IModel<T> model) {
		super(id, model);
	}

	

	@Override
	public List<ToolbarItem> getToolbarItems() {

		
		if (list!=null)
			return list;
		
		list = new ArrayList<ToolbarItem>();

		AjaxButtonToolbarItem<T> create = new AjaxButtonToolbarItem<T>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
				fire(new MenuAjaxEvent(ServerAppConstant.action_object_edit_meta, target));
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

	protected Locale getUserLocale() {
		return getSessionUser().getLocale();
	}


	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();

		try {
			
		
		Form<T> form = new Form<T>("form");
		add(form);
		setForm(form);

		if (getModel().getObject() instanceof MultiLanguageObject) {

			this.masterLanguageField = new ChoiceField<Language>("masterlanguage", new PropertyModel<Language>(ObjectMetaEditor.this, "MasterLanguage"), getLabel("masterlanguage")) {

				private static final long serialVersionUID = 1L;

				public boolean isVisible() {
					return ObjectMetaEditor.this.isLanguage();
					
				}

				@Override
				public IModel<List<Language>> getChoices() {
					return new ListModel<Language>(getLanguages());
				}

				@Override
				protected String getDisplayValue(Language value) {
					if (value == null)
						return null;
					return value.getLabel(getUserLocale());
				}
			};

			form.add(masterLanguageField);

			this.translateMode = new ChoiceField<TranslateMode>("translatemode", new PropertyModel<TranslateMode>(getModel(), "translateMode"), getLabel("translatemode")) {

				private static final long serialVersionUID = 1L;

				public boolean isVisible() {
					return false;
					//return ObjectMetaEditor.this.getModel().getObject() instanceof MultiLanguageObject;
				}

				@Override
				public IModel<List<TranslateMode>> getChoices() {
					return new ListModel<TranslateMode>(getTranslateModes());
				}

				@Override
				protected String getDisplayValue(TranslateMode value) {
					if (value == null)
						return null;
					return value.getLabel(getUserLocale());
				}
			};
			form.add(translateMode);
		} else {
			form.add(new InvisiblePanel("translatemode"));
			form.add(new InvisiblePanel("masterlanguage"));
		}

		this.objectStateField = new ChoiceField<ObjectState>("state", new PropertyModel<ObjectState>(getModel(), "state"), getLabel("state")) {

			private static final long serialVersionUID = 1L;

			@Override
			public IModel<List<ObjectState>> getChoices() {
				return new ListModel<ObjectState>(getStates());
			}

			@Override
			protected String getDisplayValue(ObjectState value) {
				if (value == null)
					return null;
				return value.getLabel(getLocale());
			}
		};
		form.add(this.objectStateField);

		this.audioModeField = new ChoiceField<Boolean>("audiomode", new PropertyModel<Boolean>(getModel(), "audioAutoGenerate"), getLabel("audiomode")) {

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
					return getLabel("yes").getObject();
				return getLabel("no").getObject();
			}

			public boolean isVisible() {
				return ObjectMetaEditor.this.isAudioAutoGenerate();
			}

		};
		form.add(this.audioModeField);

		EditButtons<T> buttons = new EditButtons<T>("buttons-bottom", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onEdit(AjaxRequestTarget target) {
				ObjectMetaEditor.this.onEdit(target);
			}

			@Override
			public void onCancel(AjaxRequestTarget target) {
				ObjectMetaEditor.this.onCancel(target);
			}

			@Override
			public void onSave(AjaxRequestTarget target) {
				ObjectMetaEditor.this.onSave(target);
			}

			@Override
			public boolean isVisible() {
				return getForm().getFormState() == FormState.EDIT;
			}
		};

		form.add(buttons);

		EditButtons<T> b_buttons_top = new EditButtons<T>("buttons-top", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			public void onEdit(AjaxRequestTarget target) {
				ObjectMetaEditor.this.onEdit(target);
			}

			public void onCancel(AjaxRequestTarget target) {
				ObjectMetaEditor.this.onCancel(target);
			}

			public void onSave(AjaxRequestTarget target) {
				ObjectMetaEditor.this.onSave(target);
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
		
		} catch (Exception e) {
			logger.error(e);
			add( new ErrorPanel("form", e));
		}
	}

	

	protected boolean isLanguage() {
		return isLanguage;
		
//		return ObjectMetaEditor.this.getModel().getObject() instanceof MultiLanguageObject;
	}

	public void setLanguage(boolean b) {
		this.isLanguage = b;
	}

	
	protected boolean isAudioAutoGenerate() {
		return isAudioAutoGenerate;
	}

	public void setAudioAutoGenerate(boolean b) {
		this.isAudioAutoGenerate = b;
	}

	protected List<Language> getLanguages() {
		return getLanguageService().getLanguagesSorted(Locale.ENGLISH);
	}

	protected List<TranslateMode> getTranslateModes() {
		return TranslateMode.getTranslateModes();
	}

	public Optional<Person> getPerson(Long value) {
		return super.getPerson(value);
	}

	public void onCancel(AjaxRequestTarget target) {
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

		if ((getModel().getObject() instanceof MultiLanguageObject) && getMasterLanguage() != null) {
			((MultiLanguageObject) getModel().getObject()).setMasterLanguage(getMasterLanguage().getLanguageCode());
		}
		save(getModelObject(), getSessionUser(), AuditKey.TRANSLATE);

		getForm().setFormState(FormState.VIEW);
		logger.debug("done");
		target.add(this);

		// TODO AT
		// fir e( ne w (target));
	}

	

	 

	protected void onSubmit() {
		logger.debug("");
		logger.debug("onSubmit");
		logger.debug("");
	}


	public Language getMasterLanguage() {
		return masterLanguage;
	}

	public void setMasterLanguage(Language masterLanguage) {
		this.masterLanguage = masterLanguage;
	}

	private void setUpModel() {
		if (getModel().getObject() instanceof MultiLanguageObject) {
			String s = ((MultiLanguageObject) getModel().getObject()).getMasterLanguage();
			if (s != null) {
				masterLanguage = getLanguageService().getLanguage(s);
			}
		}
	}
	
	public void save(T modelObject, User user, String msg) {
		DBService.getDBService(modelObject.getClass()).saveViaBaseClass((DelleMuseObject) modelObject, user, msg);
	}
	
	//private DBService<?, Long> getDBService(Class<? extends DelleMuseObject> clazz) {
	//	return DBService.getDBService(clazz);
	//}
}
