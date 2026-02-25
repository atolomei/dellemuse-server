package dellemuse.serverapp.page.user;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.artexhibition.ArtExhibitionEditor;
import dellemuse.serverapp.editor.DBObjectEditor;
import dellemuse.serverapp.editor.SimpleAlertRow;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.site.SiteInfoEditor;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.User;
import io.wktui.event.MenuAjaxEvent;

import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.EditButtons;

import io.wktui.form.field.PasswordField;
import io.wktui.form.field.StaticTextField;
import io.wktui.form.field.TextField;
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import wktui.base.InvisiblePanel;

public class UserPasswordEditor extends DBObjectEditor<User> implements InternalPanel {

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteInfoEditor.class.getName());

	private Form<User> form;
	private StaticTextField<String> nameField;
	private StaticTextField<String> emailField;
	
	//private PasswordField passwordField;

	private TextField<String> passwordField;

	
	private String newPassword;
	
	/**
	 * @param id
	 * @param model
	 */
	public UserPasswordEditor(String id, IModel<User> model) {
		super(id, model);
	}

	public void onInitialize() {
		super.onInitialize();

		add(new InvisiblePanel("error"));

		this.form = new Form<User>("personForm", getModel());
		add(this.form);

		newPassword = "";
		
		this.form.setFormState(FormState.VIEW);

		this.nameField 		= new StaticTextField<String>("username"			, new PropertyModel<String>(getModel(), "username"), getLabel("username"));
		this.emailField 		= new StaticTextField<String>("email"			, new PropertyModel<String>(getModel(), "email"), getLabel("email"));
		
		
		this.passwordField 	= new TextField<String>("password"					, new PropertyModel<String>( UserPasswordEditor.this, "newPassword"), getLabel("new-password")) {
			
			public boolean isEnabled() {
				return getForm().getFormState()==FormState.EDIT;
			}
		};

		this.form.add(emailField);
		this.form.add(nameField);
		this.form.add(passwordField);

		EditButtons<User> buttons = new EditButtons<User>("buttons", this.form, getModel()) {

			private static final long serialVersionUID = 1L;

			public void onEdit(AjaxRequestTarget target) {
				UserPasswordEditor.this.onEdit(target);
			}

			public void onCancel(AjaxRequestTarget target) {
				UserPasswordEditor.this.onCancel(target);
			}

			public void onSave(AjaxRequestTarget target) {
				UserPasswordEditor.this.onSave(target);
			}

			protected String getSaveClass() {
				return "ps-0 btn btn-sm btn-link";
			}

			protected String getCancelClass() {
				return "ps-0 btn btn-sm btn-link";
			}

			@Override
			public boolean isVisible() {
				
					if (!hasWritePermission())
						return false;
			
					return getForm().getFormState() == FormState.EDIT;
			}

		};

		this.form.add(buttons);
	}

	/**
	 * 
	 * 
	 */
	@Override
	public List<ToolbarItem> getToolbarItems() {

		List<ToolbarItem> list = new ArrayList<ToolbarItem>();

		AjaxButtonToolbarItem<Person> create = new AjaxButtonToolbarItem<Person>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
				fire(new MenuAjaxEvent(ServerAppConstant.user_action_edit_pwd, target));
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
		target.add(this);
	}

	protected void onSave(AjaxRequestTarget target) {

		try {

			//if (getModel().getObject().getPassword() == null)
			//	throw new IllegalArgumentException("pwd is null");

			
			if (this.getNewPassword()!=null) 
			{
		        String hash = new BCryptPasswordEncoder().encode(getNewPassword() );
				getModel().getObject().setPassword(hash);

				save(getModelObject(), getSessionUser().get(), getUpdatedParts());
				
				
			}
			this.form.setFormState(FormState.VIEW);
			target.add(this);
		
		} catch (Exception e) {
			addOrReplace(new SimpleAlertRow<Void>("error", e));
			logger.error(e);

		}
	}

}
