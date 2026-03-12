package dellemuse.serverapp.page.security;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.artexhibition.ArtExhibitionEditor;
import dellemuse.serverapp.editor.DBObjectEditor;
import dellemuse.serverapp.editor.SimpleAlertRow;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.site.SiteInfoEditor;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.PersistentToken;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.User;
import io.wktui.error.AlertPanel;
import io.wktui.event.MenuAjaxEvent;

import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.EditButtons;
import io.wktui.form.button.SubmitButton;
import io.wktui.form.field.Field;
import io.wktui.form.field.PasswordField;
import io.wktui.form.field.StaticTextField;
import io.wktui.form.field.TextField;
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import wktui.base.InvisiblePanel;

public class PasswordResetEditor extends DBObjectEditor<User>   {

	
Long perstitentTokenId;
	
	
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
	private TextField<String> passwordField;
	
	private String newPassword;
	

	public boolean hasWritePermission() {
		
		return getModel()!=null && getModel().getObject()!=null;

	}
	
	/**
	 * @param id
	 * @param model
	 */
	public PasswordResetEditor(String id, IModel<User> model, Long perstitentTokenId) {
		super(id, model);
		this.perstitentTokenId=perstitentTokenId;
	}
	

	public void onInitialize() {
		super.onInitialize();

		add(new InvisiblePanel("error"));
		add(new InvisiblePanel("success"));
		
		this.form = new Form<User>("personForm", getModel());
		add(this.form);

		
		Label no=new Label("notice", getLabel("notice", getModel().getObject().getFirstLastname(), getModel().getObject().getUsername()));
		no.setEscapeModelStrings(false);
		add(no);
		
		newPassword = "";
		
		this.form.setFormState(FormState.EDIT);

	//	this.nameField 		= new StaticTextField<String>("username", new PropertyModel<String>(getModel(), "username"), getLabel("username"));
		this.passwordField 	= new TextField<String>("password"		, new PropertyModel<String>( PasswordResetEditor.this, "newPassword"), getLabel("new-password")) {

			public boolean isVisible() {
				return getForm().getFormState()==FormState.EDIT;
			}

			public boolean isEnabled() {
				
				if (!hasWritePermission())
					return false;
				
				return getForm().getFormState()==FormState.EDIT;
			}
		};

		//this.form.add(nameField);
		this.form.add(passwordField);

		SubmitButton<User> sm = new SubmitButton<User>("send", getModel(), getForm()) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit(AjaxRequestTarget target) {
				PasswordResetEditor.this.onSave( target );
			}

			@Override
			public boolean isEnabled() {
				return true;
			}
			
			public IModel<String> getLabel() {
				return  getLabel("submit");
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
		

			public boolean isVisible() {
				return getForm().getFormState()==FormState.EDIT;
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

	 

	public Form<User> getForm() {
		return form;
	}

	public void setForm(Form<User> form) {
		this.form = form;
	}

	public void onCancel(AjaxRequestTarget target) {
		this.form.setFormState(FormState.VIEW);
		target.add(this);
	}

	
	public void onEdit(AjaxRequestTarget target) {
		this.form.setFormState(FormState.EDIT);
		target.add(this);
	}

	protected void onSave(AjaxRequestTarget target) {

		try {
			if (this.getNewPassword()!=null) 
			{
		        String hash = new BCryptPasswordEncoder().encode(getNewPassword() );
				getModel().getObject().setPassword(hash);
				save(getModelObject(), getModel().getObject(), getUpdatedParts());

				if (this.perstitentTokenId!=null)
				  getPersistentTokenDBServiceDBService().deleteById(perstitentTokenId);

				this.form.setFormState(FormState.VIEW);
			}
		
			addOrReplace(new SimpleAlertRow<String>("success", getLabel("password-reset-success", getModel().getObject().getFirstLastname(), getModel().getObject().getUsername()), AlertPanel.INFO));
			target.add(this);
		
		} catch (Exception e) {
			addOrReplace(new SimpleAlertRow<Void>("error", e));
			target.add(this);
			logger.error(e);

		}
	}

}
