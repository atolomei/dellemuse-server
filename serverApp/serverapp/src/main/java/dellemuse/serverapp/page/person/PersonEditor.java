package dellemuse.serverapp.page.person;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.site.SiteEditor;
import dellemuse.serverapp.serverdb.model.Person;
import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.EditButtons;
import io.wktui.form.button.SubmitButton;
import io.wktui.form.field.TextField;

public class PersonEditor extends DBModelPanel<Person> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteEditor.class.getName());

	private Form<Person> form;
	private TextField<String> nameField;
	private TextField<String> lastnameField;
	private TextField<String> nicknameField;
	private TextField<String> sexField;
	private TextField<String> addressField;
	private TextField<String> phoneField;
	private TextField<String> emailField;

	/**
	 * 
	 * 
	 * @param id
	 * @param model
	 */
	public PersonEditor(String id, IModel<Person> model) {
		super(id, model);
	}

	public void onInitialize() {
		super.onInitialize();

		this.form = new Form<Person>("personForm", getModel());
		add(this.form);

		this.form.setFormState(FormState.VIEW);

		nameField 			= new TextField<String>("name", 	Model.of(getModel().getObject().getName()),		 	getLabel("name"));
		lastnameField 		= new TextField<String>("lastname",	Model.of(getModel().getObject().getLastname()), 	getLabel("lastname"));
		nicknameField 		= new TextField<String>("nickname", Model.of(getModel().getObject().getNickname()), 	getLabel("nickname"));
		sexField 			= new TextField<String>("sex", 		Model.of(getModel().getObject().getSex()), 			getLabel("sex"));
		addressField 		= new TextField<String>("address", 	Model.of(getModel().getObject().getAddress()), 		getLabel("address"));
		phoneField 			= new TextField<String>("phone", 	Model.of(getModel().getObject().getPhone()), 		getLabel("phone"));
		emailField 			= new TextField<String>("email", 	Model.of(getModel().getObject().getEmail()), 		getLabel("email"));

		form.add(nameField);
		form.add(lastnameField);
		form.add(nicknameField);
		form.add(sexField);
		form.add(addressField);
		form.add(phoneField);
		form.add(emailField);

	
		EditButtons<Person> buttons = new EditButtons<Person>("buttons", this.form, getModel()) {

			private static final long serialVersionUID = 1L;

			public void onEdit( AjaxRequestTarget target ) {
				 PersonEditor.this.onEdit(target);
				
			}

			public void onCancel( AjaxRequestTarget target ) {
				 PersonEditor.this.onCancel(target);
				
			}

			public void onSave(AjaxRequestTarget target ) {
				 PersonEditor.this.onSave(target);
			}
		};
		
		this.form.add(buttons);
	
	}

	

	public Form<Person> getForm() {
		return form;
	}
 

	public void setForm(Form<Person> form) {
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
