package dellemuse.serverapp.page.user;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.site.SiteInfoEditor;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.User;
import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.EditButtons;
import io.wktui.form.button.SubmitButton;
import io.wktui.form.field.TextField;

public class UserEditor extends DBModelPanel<User> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteInfoEditor.class.getName());

	private Form<User> form;
	private TextField<String> nameField;
	
	
	/**
	 * 
	 * 
	 * @param id
	 * @param model
	 */
	public UserEditor(String id, IModel<User> model) {
		super(id, model);
	}

	public void onInitialize() {
		super.onInitialize();

		this.form = new Form<User>("personForm", getModel());
		add(this.form);

		this.form.setFormState(FormState.VIEW);

		nameField = new TextField<String>("username", 	Model.of(getModel().getObject().getUsername()),		 	getLabel("username"));

		form.add(nameField);

	
		EditButtons<User> buttons = new EditButtons<User>("buttons", this.form, getModel()) {

			private static final long serialVersionUID = 1L;

			public void onEdit( AjaxRequestTarget target ) {
				 UserEditor.this.onEdit(target);
				
			}

			public void onCancel( AjaxRequestTarget target ) {
				 UserEditor.this.onCancel(target);
				
			}

			public void onSave(AjaxRequestTarget target ) {
				 UserEditor.this.onSave(target);
			}
		};
		
		this.form.add(buttons);
	
	}

	

	public Form<User> getForm() {
		return form;
	}
 

	public void setForm(Form<User> form) {
		this.form = form;
	}
 
	
	protected void onCancel(AjaxRequestTarget target) {
		this.form.setFormState(FormState.VIEW);
		target.add(this.form);
	}

	protected void onEdit(AjaxRequestTarget target) {
		this.form.setFormState(FormState.EDIT);
		target.add(this.form);
	}

	
	protected void onSave(AjaxRequestTarget target) {

		this.form.setFormState(FormState.VIEW);
		target.add(this.form);

		logger.debug("");
		logger.debug("onSubmit");
		logger.debug(form.isSubmitted());
		logger.debug("done");
		logger.debug("");

	}

	
}
