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

public class CandidateUserEditor extends DBObjectEditor<Candidate> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(CandidateUserEditor.class.getName());

	private List<ToolbarItem> x_list;
	private String userName;

	public CandidateUserEditor(String id, IModel<Candidate> model) {
		super(id, model);
		this.setOutputMarkupId(true);
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (list != null) {
			for (IModel<Institution> m : list) {
				m.detach();
			}
		}
	}

	List<IModel<Institution>> list;

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

		StringBuilder str = new StringBuilder();

		if (	getModel().getObject().getPersonLastname() != null && 
				!getModel().getObject().getPersonLastname().isEmpty() && 
				getModel().getObject().getPersonName() != null && 
				!getModel().getObject().getPersonName().isEmpty()) {

			userName = getUserDBService().generateUserName(getModel().getObject().getPersonName(), getModel().getObject().getPersonLastname());
			str.append("<b>Username a crear</b>.  " + userName);

			if (getModel().getObject().getInstitution() != null)
				str.append("<br/><b>Institution</b>.  " + getModel().getObject().getInstitution().getName());
		}

		getForm().addOrReplace(new SimpleAlertRow<Void>("userInfo", null, Model.of(str.toString()), getLabel("userInfo"), AlertPanel.PRIMARY));

		EditButtons<Candidate> buttons = new EditButtons<Candidate>("buttons", getForm(), getModel()) {

			private static final long serialVersionUID = 1L;

			public void onEdit(AjaxRequestTarget target) {
				CandidateUserEditor.this.onEdit(target);
			}

			public void onCancel(AjaxRequestTarget target) {
				CandidateUserEditor.this.onCancel(target);
			}

			public void onSave(AjaxRequestTarget target) {
				CandidateUserEditor.this.onSave(target);
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
				CandidateUserEditor.this.onEdit(target);
			}

			public void onCancel(AjaxRequestTarget target) {
				CandidateUserEditor.this.onCancel(target);
			}

			public void onSave(AjaxRequestTarget target) {
				CandidateUserEditor.this.onSave(target);
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

		AjaxLink<Candidate> createUser = new AjaxLink<Candidate>("createUser", getModel()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				User u = CandidateUserEditor.this.createUser();
				
				if (u != null) {
					CandidateUserEditor.this.addOrReplace(new AlertPanel<Void>("success", AlertPanel.SUCCESS, getLabel("user-created-ok", u.getUsername(), u.getEmail())));
				}
				
				target.add(CandidateUserEditor.this);
				
			}

			@Override
			public boolean isVisible() {
				
				if (getModelObject().getInstitution() == null)
					return false;
				
				return hasWritePermission();
			}
		};


		AjaxLink<Candidate> sendEmail = new AjaxLink<Candidate>("sendEmail", getModel()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				CandidateUserEditor.this.sendEmail();
			}

			@Override
			public boolean isVisible() {

				if (getModelObject().getUser() == null)
					return false;

				return hasWritePermission();
			}
		};

		getForm().add(sendEmail);

		getForm().add(createUser);

		/**
		 * --
		 * 
		 * form.updateModel(); form.updateReload(); form.visitChildren(Field.class, new
		 * IVisitor<Field<?>, Void>() {
		 * 
		 * @Override public void component(Field<?> field, IVisit<Void> visit) {
		 *           field.editOn(); } });
		 **/

	}

	protected void sendEmail() {
		// TODO Auto-generated method stub

	}

	protected User createUser() {
		 User u = getUserDBService().createUserFromCandidate(userName, getModel().getObject(), getSessionUser().get());
		 return u;
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

			StringBuilder str = new StringBuilder();
			if (getModel().getObject().getPersonLastname() != null && !getModel().getObject().getPersonLastname().isEmpty() && getModel().getObject().getPersonName() != null && !getModel().getObject().getPersonName().isEmpty()) {

				userName = getUserDBService().generateUserName(getModel().getObject().getPersonName(), getModel().getObject().getPersonLastname());
				str.append("userName. " + userName);
			}

			getForm().addOrReplace(new SimpleAlertRow<Void>("userInfo", null, Model.of(str.toString()), getLabel("userInfo"), AlertPanel.PRIMARY));

			List<Institution> cand = getInstitutionDBService().findByNameApprox(getModel().getObject().getInstitutionName());

			List<String> sCand = new ArrayList<String>();
			cand.forEach(c -> sCand.add(c.getDisplayname()));
			getForm().addOrReplace(new SimpleAlertRow<Void>("institutionApprox", null, Model.of(String.join(", ", sCand)), getLabel("institutionApprox"), AlertPanel.PRIMARY));

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
				fire(new MenuAjaxEvent(ServerAppConstant.action_candidate_user_edit, target));
			}

			@Override
			public IModel<String> getButtonLabel() {
				return getLabel("edit");
			}

			@Override
			public boolean isVisible() {
				return isRoot() || isGeneralAdmin();
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
