package dellemuse.serverapp.page.user;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import dellemuse.model.logging.Logger;
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

import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import wktui.base.InvisiblePanel;

public class UserPasswordEditor extends DBObjectEditor<User> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteInfoEditor.class.getName());

	private Form<User> form;
	private StaticTextField<String> nameField;
	private PasswordField passwordField;

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

		this.form.setFormState(FormState.VIEW);

		this.nameField 		= new StaticTextField<String>("username"	, new PropertyModel<String>(getModel(), "username"), getLabel("username"));
		this.passwordField 	= new PasswordField("password"				, new PropertyModel<String>(getModel(), "password"), getLabel("new-password"));

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
				fire(new MenuAjaxEvent(ServerAppConstant.site_action_edit, target));
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
		target.add(this.form);
	}

	protected void onSave(AjaxRequestTarget target) {

		try {

			if (getModel().getObject().getPassword() == null)
				throw new IllegalArgumentException("pwd is null");

			this.form.setFormState(FormState.VIEW);
			target.add(this.form);
			save(getModelObject(), getSessionUser(), getUpdatedParts());
		
		} catch (Exception e) {
			addOrReplace(new SimpleAlertRow<Void>("error", e));
			logger.error(e);

		}
	}

}
