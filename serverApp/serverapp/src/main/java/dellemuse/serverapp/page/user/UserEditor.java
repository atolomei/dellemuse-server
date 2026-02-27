package dellemuse.serverapp.page.user;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

import org.apache.wicket.model.PropertyModel;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.editor.DBObjectEditor;
import dellemuse.serverapp.editor.ObjectUpdateEvent;
import dellemuse.serverapp.editor.SimpleAlertRow;
import dellemuse.serverapp.page.InternalPanel;

import dellemuse.serverapp.page.site.SiteInfoEditor;
import dellemuse.serverapp.person.ServerAppConstant;

import dellemuse.serverapp.serverdb.model.User;
import io.wktui.event.MenuAjaxEvent;

import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.EditButtons;
import io.wktui.form.field.LocaleField;
import io.wktui.form.field.StaticTextField;
import io.wktui.form.field.TextField;
import io.wktui.form.field.ZoneIdField;
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import wktui.base.InvisiblePanel;

public class UserEditor extends DBObjectEditor<User> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteInfoEditor.class.getName());

	private Form<User> form;
	private TextField<String> nameField;
	private ZoneIdField zoneidField;
	private List<ToolbarItem> list;
	private LocaleField localeField;
	// private StaticTextField<String> emailField;

	/**
	 * @param id
	 * @param model
	 */
	public UserEditor(String id, IModel<User> model) {
		super(id, model);
	}

	/**
	 * IModel<Locale> mLocale;
	 * 
	 * public void setUserLocale( IModel<Locale> locale) { mlocale=Model.of(); }
	 **/

	@Override
	public void onInitialize() {
		super.onInitialize();

		add(new InvisiblePanel("error"));

		this.form = new Form<User>("personForm", getModel());
		add(this.form);

		logger.debug("user locale -> " + getModel().getObject().getLocale().getLanguage());

		
		// this.emailField = new StaticTextField<String>("email", new PropertyModel<String>(getModel(), "email"), getLabel("email"));

		
		
		// this.form.add(emailField);
		
		this.nameField = new TextField<String>("username", new PropertyModel<String>(getModel(), "username"), getLabel("username"));
		this.zoneidField = new ZoneIdField("zoneid", new PropertyModel<ZoneId>(getModel(), "zoneId"), getLabel("zoneid"));
		this.localeField = new LocaleField("locale", new PropertyModel<Locale>(getModel(), "locale"), getLabel("locale"));

		if (getModel().getObject().isRoot())
			this.nameField.setReadOnly(true);

		this.form.add(nameField);
		this.form.add(zoneidField);
		this.form.add(localeField);

		this.form.setFormState(FormState.VIEW);

		EditButtons<User> buttons = new EditButtons<User>("buttons", this.form, getModel()) {

			private static final long serialVersionUID = 1L;

			public void onEdit(AjaxRequestTarget target) {
				UserEditor.this.onEdit(target);
			}

			public void onCancel(AjaxRequestTarget target) {
				UserEditor.this.onCancel(target);
			}

			public void onSave(AjaxRequestTarget target) {
				UserEditor.this.onSave(target);
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

	@Override
	public List<ToolbarItem> getToolbarItems() {

		if (list != null)
			return list;

		list = new ArrayList<ToolbarItem>();

		AjaxButtonToolbarItem<User> create = new AjaxButtonToolbarItem<User>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
				fire(new MenuAjaxEvent(ServerAppConstant.user_action_edit_info, target));
			}

			@Override
			public IModel<String> getButtonLabel() {
				return getLabel("edit");
			}

			@Override
			public boolean isVisible() {

				if (!hasWritePermission())
					return false;

				return true;
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
		super.edit(target);
		target.add(this);
	}

	protected void onSave(AjaxRequestTarget target) {

		try {
			this.form.setFormState(FormState.VIEW);
			target.add(this.form);
			save(getModelObject(), getSessionUser().get(), getUpdatedParts());
			getForm().updateReload();
			fireScanAll(new ObjectUpdateEvent(target));

		} catch (Exception e) {

			addOrReplace(new SimpleAlertRow<Void>("error", e));
			logger.error(e);

		}
	}

}
