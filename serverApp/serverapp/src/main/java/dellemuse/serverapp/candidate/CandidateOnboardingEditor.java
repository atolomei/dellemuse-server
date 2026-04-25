package dellemuse.serverapp.candidate;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.DellemuseServer;
import dellemuse.serverapp.audiostudio.Step3AudioStudioEditor;
import dellemuse.serverapp.editor.DBObjectEditor;
import dellemuse.serverapp.editor.ObjectUpdateEvent;
import dellemuse.serverapp.editor.SimpleAlertRow;
import dellemuse.serverapp.email.EmailTemplateService;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.site.SiteInfoEditor;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.AudioStudio;
import dellemuse.serverapp.serverdb.model.Candidate;
import dellemuse.serverapp.serverdb.model.CandidateStatus;
import dellemuse.serverapp.serverdb.model.Music;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.PersistentToken;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.CandidateDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.SecurityService;
import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.EditButtons;
import io.wktui.form.button.SubmitButton;
import io.wktui.form.field.ChoiceField;
import io.wktui.form.field.Field;

import io.wktui.form.field.TextAreaField;
import io.wktui.form.field.TextField;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.ButtonCreateToolbarItem;
import io.wktui.error.AlertPanel;
import io.wktui.error.ErrorPanel;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import wktui.base.InvisiblePanel;

public class CandidateOnboardingEditor extends DBObjectEditor<Candidate> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(CandidateOnboardingEditor.class.getName());

	private TextField<String> nameField;
	private TextField<String> lastnameField;
	private TextField<String> emailField;
	private TextField<String> phoneField;
	private TextField<String> institutionField;
	private TextAreaField<String> institutionAddressField;
	private TextAreaField<String> commentsField;
	private TextField<String> passwordField;

	private String slang;

	public CandidateOnboardingEditor(String id, String slang) {
		super(id, null);
		this.setOutputMarkupId(true);
		this.slang = slang;
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public Locale getLocale() {
		return Locale.forLanguageTag(slang);
	}

	boolean submitted = false;

	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();

		add(new InvisiblePanel("error"));
		add(new InvisiblePanel("success"));

		Form<Candidate> form = new Form<Candidate>("candidateForm", getModel());
		form.setFormState(FormState.EDIT);

		form.setOutputMarkupId(true);
		add(form);
		setForm(form);

		nameField = new TextField<String>("name", new PropertyModel<String>(getModel(), "personName"), getLabel("personName"));
		lastnameField = new TextField<String>("lastname", new PropertyModel<String>(getModel(), "personLastname"), getLabel("personLastname"));
		emailField = new TextField<String>("email", new PropertyModel<String>(getModel(), "email"), getLabel("email"));
		phoneField = new TextField<String>("phone", new PropertyModel<String>(getModel(), "phone"), getLabel("phone"));
		institutionField = new TextField<String>("institution", new PropertyModel<String>(getModel(), "institutionName"), getLabel("institutionName"));
		institutionAddressField = new TextAreaField<String>("institutionAddress", new PropertyModel<String>(getModel(), "institutionAddress"), getLabel("institutionAddress"), 4);
		commentsField = new TextAreaField<String>("comments", new PropertyModel<String>(getModel(), "comments"), getLabel("comments"), 4);
		passwordField = new TextField<String>("password", new PropertyModel<String>(getModel(), "password"), getLabel("password"));

		getForm().add(nameField);
		getForm().add(lastnameField);
		getForm().add(emailField);
		getForm().add(phoneField);
		getForm().add(institutionField);
		getForm().add(institutionAddressField);
		getForm().add(commentsField);
		getForm().add(passwordField);

		SubmitButton<Candidate> sm = new SubmitButton<Candidate>("send", getModel(), getForm()) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit(AjaxRequestTarget target) {
				CandidateOnboardingEditor.this.onSave(target);
			}

			@Override
			public boolean isVisible() {
				return !submitted;
			}

			@Override
			public boolean isEnabled() {
				return true;
			}

			public IModel<String> getLabel() {
				return getLabel("submit");
			}

			@Override
			public String getRowCss() {
				return "d-inline-block float-left";
			}

			@Override
			public String getColCss() {
				return "d-inline-block float-left";
			}

			@Override
			public String getSaveCss() {
				return "btn btn-primary btn-lg";
			}
		};

		getForm().add(sm);
		form.updateModel();
		form.updateReload();
		form.visitChildren(Field.class, new IVisitor<Field<?>, Void>() {
			@Override
			public void component(Field<?> field, IVisit<Void> visit) {
				field.editOn();
			}
		});
	}

	protected void setUpModel() {

		try {

			Map<String, String> map = new HashMap<String, String>();

			map.put("language", slang);

			Candidate c = getCandidateDBService().create(map, getRootUser());
			setModel(new ObjectModel<Candidate>(c));

		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
	}

	protected SecurityService getSecurityService() {
		return (SecurityService) ServiceLocator.getInstance().getBean(SecurityService.class);
	}

	protected void onSave(AjaxRequestTarget target) {

		try {

			if ((getModel().getObject().getEmail() == null || getModel().getObject().getEmail().isEmpty()) || (getModel().getObject().getInstitutionName() == null || getModel().getObject().getInstitutionName().isEmpty())
					|| (getModel().getObject().getPassword() == null || getModel().getObject().getPassword().isEmpty()) || (getModel().getObject().getPersonLastname() == null || getModel().getObject().getPersonLastname().isEmpty())) {

				SimpleAlertRow<Void> r = new SimpleAlertRow<Void>("error");
				r.setText(getLabel("mandatory-fields-not-filled"));
				addOrReplace(r);
				target.add(this);
				return;
			}

			// --------- Save Candidate on Database -----------

			getModel().getObject().setStatus(CandidateStatus.SUBMITTED);
			getModel().getObject().setObjectState(ObjectState.PUBLISHED);

			String hash = new BCryptPasswordEncoder().encode(getModel().getObject().getPassword());
			getModel().getObject().setPassword(hash);

			CandidateDBService service = (CandidateDBService) ServiceLocator.getInstance().getBean(CandidateDBService.class);
			service.save(getModelObject(), String.join(", ", getUpdatedParts()), getRootUser());

			submitted = true;

			getModel().getObject().setPassword("");

			getForm().setFormState(FormState.VIEW);

			getForm().updateReload();

			addOrReplace(new AlertPanel<Void>("success", AlertPanel.SUCCESS, getLabel("submitted-ok", getModel().getObject().getEmail())));
			addOrReplace(new InvisiblePanel("error"));

			target.add(this);

			fireScanAll(new ObjectUpdateEvent(target));

		} catch (Exception e) {
			addOrReplace(new SimpleAlertRow<Void>("error", e));
		}
		target.add(this);
	}

	protected void onCancel(AjaxRequestTarget target) {
		getForm().setFormState(FormState.VIEW);
		target.add(getForm());
	}

	protected void onEdit(AjaxRequestTarget target) {
		super.edit(target);
		target.add(this);
	}

}