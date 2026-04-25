package dellemuse.serverapp.candidate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.audiostudio.Step3AudioStudioEditor;
import dellemuse.serverapp.editor.DBObjectEditor;
import dellemuse.serverapp.editor.ObjectUpdateEvent;
import dellemuse.serverapp.editor.SimpleAlertRow;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.AudioStudio;
import dellemuse.serverapp.serverdb.model.Candidate;
import dellemuse.serverapp.serverdb.model.CandidateStatus;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Music;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.CandidateDBService;
import dellemuse.serverapp.serverdb.service.InstitutionDBService;

import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.EditButtons;
import io.wktui.form.field.BooleanField;
import io.wktui.form.field.ChoiceField;
import io.wktui.form.field.Field;

import io.wktui.form.field.StaticTextField;
import io.wktui.form.field.TextAreaField;
import io.wktui.form.field.TextField;
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.error.AlertPanel;
import io.wktui.event.MenuAjaxEvent;
import wktui.base.InvisiblePanel;

public class CandidateEditor extends DBObjectEditor<Candidate> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(CandidateEditor.class.getName());

	private TextField<String> nameField;
	private TextField<String> lastnameField;
	private TextField<String> emailField;
	private BooleanField emailValidatedField;
	private TextField<String> phoneField;
	private TextField<String> institutionField;
	private TextAreaField<String> institutionAddressField;
	private TextAreaField<String> commentsField;
	private TextAreaField<String> internalcommentsField;
	private ChoiceField<CandidateStatus> statusField;
	private StaticTextField<String> validationEmailSentField;
	private StaticTextField<String> createdField;
	private StaticTextField<String> lastModifiedField;

	private List<ToolbarItem> x_list;
	private String userName;

	public CandidateEditor(String id, IModel<Candidate> model) {
		super(id, model);
		this.setOutputMarkupId(true);
	}

	@Override
	public void onDetach() {
		super.onDetach();

	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();

		add(new InvisiblePanel("error"));
		add(new InvisiblePanel("success"));

		Form<Candidate> form = new Form<Candidate>("candidateForm", getModel());

		form.setOutputMarkupId(true);
		add(form);
		setForm(form);

		nameField = new TextField<String>("name", new PropertyModel<String>(getModel(), "personName"), getLabel("personName"));
		lastnameField = new TextField<String>("lastname", new PropertyModel<String>(getModel(), "personLastname"), getLabel("personLastname"));
		emailField = new TextField<String>("email", new PropertyModel<String>(getModel(), "email"), getLabel("email"));
		emailValidatedField = new BooleanField("emailValidated", new PropertyModel<Boolean>(getModel(), "emailValidated"), getLabel("emailValidated"));
		phoneField = new TextField<String>("phone", new PropertyModel<String>(getModel(), "phone"), getLabel("phone"));

		institutionField = new TextField<String>("institutionName", new PropertyModel<String>(getModel(), "institutionName"), getLabel("institutionName"));
		institutionAddressField = new TextAreaField<String>("institutionAddress", new PropertyModel<String>(getModel(), "institutionAddress"), getLabel("institutionAddress"), 4);

		commentsField = new TextAreaField<String>("comments", new PropertyModel<String>(getModel(), "comments"), getLabel("comments"), 4);
		internalcommentsField = new TextAreaField<String>("internalcomments", new PropertyModel<String>(getModel(), "internalcomments"), getLabel("internalcomments"), 4);
		statusField = new ChoiceField<CandidateStatus>("status", new PropertyModel<CandidateStatus>(getModel(), "status"), getLabel("status")) {
			@Override
			public IModel<List<CandidateStatus>> getChoices() {
				return new ListModel<CandidateStatus>(CandidateStatus.getValues());
			}
		};
		validationEmailSentField = new StaticTextField<String>("validationEmailSent", new PropertyModel<String>(getModel(), "validationEmailSent"), getLabel("validationEmailSent"));
		createdField = new StaticTextField<String>("created", new PropertyModel<String>(getModel(), "created"), getLabel("created"));
		lastModifiedField = new StaticTextField<String>("lastModified", new PropertyModel<String>(getModel(), "lastModified"), getLabel("lastModified"));

		getForm().add(nameField);
		getForm().add(lastnameField);
		getForm().add(emailField);
		getForm().add(emailValidatedField);
		getForm().add(phoneField);
		getForm().add(institutionField);
		getForm().add(institutionAddressField);
		getForm().add(commentsField);
		getForm().add(internalcommentsField);
		getForm().add(statusField);
		getForm().add(validationEmailSentField);
		getForm().add(createdField);
		getForm().add(lastModifiedField);

		EditButtons<Candidate> buttons = new EditButtons<Candidate>("buttons", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			public void onEdit(AjaxRequestTarget target) {
				CandidateEditor.this.onEdit(target);
			}

			public void onCancel(AjaxRequestTarget target) {
				CandidateEditor.this.onCancel(target);
			}

			public void onSave(AjaxRequestTarget target) {
				CandidateEditor.this.onSave(target);
			}

			@Override
			public boolean isVisible() {

				if (!hasWritePermission())
					return false;

				return getForm().getFormState() == FormState.EDIT;
			}
		};

		getForm().add(buttons);

		EditButtons<Candidate> b_buttons_top = new EditButtons<Candidate>("buttons-top", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			public void onEdit(AjaxRequestTarget target) {
				CandidateEditor.this.onEdit(target);
			}

			public void onCancel(AjaxRequestTarget target) {
				CandidateEditor.this.onCancel(target);
			}

			public void onSave(AjaxRequestTarget target) {
				CandidateEditor.this.onSave(target);
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

	protected void setUpModel() {
		try {
			setModel(new ObjectModel<Candidate>(getCandidateDBService().findWithDeps(getModel().getObject().getId()).get()));
		} catch (Exception e) {
			logger.error(e);
			throw new RuntimeException(e);
		}
	}

	protected void onSave(AjaxRequestTarget target) {

		try {

			getCandidateDBService().save(getModelObject(), String.join(", ", getUpdatedParts()), getRootUser());

			
			getForm().setFormState(FormState.VIEW);
			getForm().updateReload();

			addOrReplace(new AlertPanel<Void>("success", AlertPanel.SUCCESS, getLabel("submitted-ok")));
			target.add(this);

			/**
			 * 
			 * StringBuilder str = new StringBuilder(); if (
			 * getModel().getObject().getPersonLastname() != null &&
			 * !getModel().getObject().getPersonLastname().isEmpty() &&
			 * getModel().getObject().getPersonName()!=null &&
			 * !getModel().getObject().getPersonName().isEmpty()) {
			 * 
			 * userName =
			 * getUserDBService().generateUserName(getModel().getObject().getPersonName(),
			 * getModel().getObject().getPersonLastname()); str.append("userName. " +
			 * userName); }
			 * 
			 * getForm().addOrReplace( new SimpleAlertRow<Void>("userInfo", null,
			 * Model.of(str.toString()), getLabel("userInfo"), AlertPanel.PRIMARY ) );
			 * 
			 * 
			 * List<Institution> cand = getInstitutionDBService().findByNameApprox(
			 * getModel().getObject().getInstitutionName() );
			 * 
			 * List<String> sCand = new ArrayList<String>(); cand.forEach( c ->
			 * sCand.add(c.getDisplayname()) ); getForm().addOrReplace( new
			 * SimpleAlertRow<Void>("institutionApprox", null, Model.of(String.join(", ",
			 * sCand)), getLabel("institutionApprox"), AlertPanel.PRIMARY ) );
			 */

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

	@Override
	public List<ToolbarItem> getToolbarItems() {

		if (x_list != null)
			return x_list;

		x_list = new ArrayList<ToolbarItem>();

		AjaxButtonToolbarItem<Candidate> create = new AjaxButtonToolbarItem<Candidate>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
				fire(new MenuAjaxEvent(ServerAppConstant.action_candidate_edit, target));
			}

			@Override
			public IModel<String> getButtonLabel() {
				return getLabel("edit");
			}

			@Override
			public boolean isVisible() {
				return true; // isRoot() || isGeneralAdmin();

			}

			@Override
			public boolean isEnabled() {
				return isRoot() || isGeneralAdmin();
			}
		};
		create.setAlign(Align.TOP_LEFT);
		x_list.add(create);

		// x_list.add(new HelpButtonToolbarItem("item", Align.TOP_RIGHT));

		return x_list;
	}

}
