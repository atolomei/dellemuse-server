package dellemuse.serverapp.help;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.editor.DBObjectEditor;
import dellemuse.serverapp.editor.SimpleAlertRow;
import dellemuse.serverapp.serverdb.model.User;

import io.wktui.error.AlertPanel;
import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.SubmitButton;
import io.wktui.form.field.Field;
import io.wktui.form.field.TextAreaField;

import wktui.base.InvisiblePanel;

public class ContactSupportEditor extends DBObjectEditor<User> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ContactSupportEditor.class.getName());

	private String comments;
	private boolean submitted = false;

	public ContactSupportEditor(String id, IModel<User> model) {
		super(id, model);
		setOutputMarkupId(true);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		add(new InvisiblePanel("error"));
		add(new InvisiblePanel("success"));

		Form<User> form = new Form<User>("contactSupportForm", getModel());
		form.setOutputMarkupId(true);
		form.setFormState( FormState.EDIT);
		
		add(form);
		setForm(form);

		TextAreaField<String> commentsField = new TextAreaField<String>(
				"comments",
				new PropertyModel<String>(this, "comments"),
				getLabel("comments"),
				6);

		form.add(commentsField);
		
		
		

		SubmitButton<User> sendButton = new SubmitButton<User>("send", getModel(), form) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit(AjaxRequestTarget target) {
				ContactSupportEditor.this.onSend(target);
			}

			@Override
			public boolean isVisible() {
				return !submitted;
			}

			@Override
			public boolean isEnabled() {
				return true;
			}

			@Override
			public IModel<String> getLabel() {
				return ContactSupportEditor.this.getLabel("send");
			}

			@Override
			public String getSaveCss() {
				return "btn btn-md btn-primary";
			}
		};

		form.add(sendButton);
		form.updateModel();
		form.updateReload();
		form.visitChildren(Field.class, new IVisitor<Field<?>, Void>() {
			@Override
			public void component(Field<?> field, IVisit<Void> visit) {
				field.editOn();
			}
		});
	}

	protected void onSend(AjaxRequestTarget target) {

		try {

			if (comments == null || comments.isBlank()) {
				addOrReplace(new AlertPanel<Void>("error", AlertPanel.WARNING, getLabel("comments-required")));
				target.add(this);
				return;
			}

			String username  = getModel().getObject().getDisplayname();
			String userEmail = getModel().getObject().getEmail();
			String timestamp = OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z"));
			String supportTo = getServerDBSettings().getEmailSupport();

			String subject = getLabel("email-subject").getObject() + " - " + username;

			String body = getLabel("email-from-label").getObject()    + ": " + username  + "\n"
					    + getLabel("email-email-label").getObject()    + ": " + (userEmail != null ? userEmail : "") + "\n"
					    + getLabel("email-time-label").getObject()     + ": " + timestamp + "\n\n"
					    + getLabel("email-comments-label").getObject() + ":\n" + comments;

			logger.debug("----------------");
			logger.debug("Sending support email: " + subject + "\n" + username + "\n" + timestamp + "\n" + comments);
			logger.debug("----------------");
			
			getEmailService().sendText(supportTo, subject, body);

			submitted = true;
			addOrReplace(new AlertPanel<Void>("success", AlertPanel.SUCCESS, getLabel("sent-ok")));
			addOrReplace(new InvisiblePanel("error"));
			target.add(this);

		} catch (Exception e) {
			logger.error(e);
			addOrReplace(new SimpleAlertRow<Void>("error", e));
			target.add(this);
		}
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
}
