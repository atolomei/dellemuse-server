package dellemuse.serverapp.page.security;

 
import java.time.OffsetDateTime;
import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
//import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.wicketstuff.annotation.mount.MountPath;

import jakarta.servlet.http.HttpServletResponse;


import com.giffing.wicket.spring.boot.context.scan.WicketSignInPage;
import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.DellemuseServer;
import dellemuse.serverapp.global.GlobalFooterPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.serverdb.ServerDBConstant;
import dellemuse.serverapp.serverdb.model.PersistentToken;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.PersonDBService;
import dellemuse.serverapp.serverdb.service.UserDBService;
 
import io.wktui.error.AlertPanel;
import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.SubmitButton;
import io.wktui.form.field.Field;
import io.wktui.form.field.TextField;
import wktui.base.InvisiblePanel;

/**
 * 
 * site foto Info - exhibitions
 * 
 * ZoneId -> geografia
 * Locale -> idioma
 * 
 */
@MountPath("/forgot")
public class ForgotPasswordPage extends BasePage {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ForgotPasswordPage.class.getName());
 
	private WebMarkupContainer alertContainer;	
	private AlertPanel<Void> alert;

	private Form<User> form;
	
	private String username = "";
	private String password = "";
	
	private TextField<String> userNameField;
	 

	private boolean isError = false;
	
	@SpringBean
	private UserDBService userDBService;
	
	@SpringBean
	private PersonDBService personDBService;
	
	//@SpringBean
	//private EmailService emailService;
	
	@Override
	public boolean hasAccessRight(Optional<User> ouser) {
		return true;
	}
	
	
	public ForgotPasswordPage() {
		super();
	}
	public ForgotPasswordPage(PageParameters parameters) {
		super(parameters);
		if (getPageParameters()!=null) {
			isError=(getPageParameters().get("error")!=null && getPageParameters().get("error").toString().length()>1);
			username=getPageParameters().get("username").toString();
		}
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		
		Image imageBlack = new Image(
			    "miniLogo",
			    new org.apache.wicket.request.resource.PackageResourceReference(
			        ForgotPasswordPage.class,
			        "dellemuse-logo-blanco.png"
			    )
			);
		
		add(imageBlack);
		
		alertContainer  = new WebMarkupContainer("alertContainer");
		add(alertContainer);
		
		if (isError()) {
			alert = new AlertPanel<Void>("alert", AlertPanel.DANGER, null, null, null, getLabel( "username-password-invalid"));
            alertContainer.add(alert);
        } else {
             alertContainer.setVisible(false);
             alertContainer.add( new InvisiblePanel("alert"));
         }
		 
	    form = new Form<User>("loginForm") {
	    	
				private static final long serialVersionUID = 1L;

				@Override
				protected void onInitialize() {
				    super.onInitialize();
				}
				 
				@Override
	            protected void onSubmit() {
	
					// lookup user by username/email/phone
					Optional<User> ou = userDBService.findByUsernameOrEmailOrPhone(username);
					
					if (ou.isPresent()) {
					
						User u = ou.get();
						// get person email
						Optional<dellemuse.serverapp.serverdb.model.Person> op = personDBService.getByUser(u);
						
						String email = null;
						String personName = u.getUsername();
						
						if (op.isPresent()) {
							personName = op.get().getDisplayname();
							email = op.get().getEmail();
						}
						
						
						if (email==null || email.trim().isEmpty()) {
							// fallback to user's email field
							email = u.getEmail();
						}
						
						
						
						if (email!=null && email.trim().length()>0) {
						
							
							
					    	String tokenValue = getSecurityService().nextSecureToken();
					    	 
					    	PersistentToken token = getPersistentTokenDBServiceDBService().create(
									u.getId().toString(), 
									User.class.getSimpleName(), 
									tokenValue,
									OffsetDateTime.now().plusDays(1) );
					    	
					    	Long tid = null;
					    
					    	try {
					    		
					    		tid = token.getId();
					    		
					    		String from = getServerDBSettings().getEmailFrom();
					    		String to = u.getEmail();
					    		
					    		String subject = "DelleMuse - password reset"; 
					    		String text = "please click the following link to validate your email address: " + getServerDBSettings().getEmailValidationServer() + "/password-reset?token=" +u.getId().toString()+"-"+tokenValue;
					    		
					    		String sendEmail= getEmailService().send(from, to, subject, text);
					    	
					    		logger.debug("Email sent response -> " + sendEmail);
					    		
					    	} catch (Exception e) {
					    		if (tid!=null)
					    			getPersistentTokenDBServiceDBService().findById(tid).ifPresent(t -> getPersistentTokenDBServiceDBService().delete(t));
					    		logger.error(e);
							}
							
							
							
							
							
							//String link = RequestCycle.get().urlFor(ResetPasswordPage.class, new PageParameters().add("user", u.getUsername())).toString();
							//String subject = "Reset your password";
							//String body = "Hello " + personName + "\n\nPlease follow the link to reset your password:\n" + link + "\n\nIf you didn't request this, ignore this email.";
							//emailService.sendEmail(email, subject, body);
							// show confirmation
							
							
							
							getPageParameters().set("sent", "true");
							setResponsePage(ForgotPasswordPage.class, getPageParameters());
							
							
							return;
						} else {
							// no email found
							error("No email associated with user");
							return;
						}
					} else {
						// no user found
						error("No user found with that username/email/phone");
						return;
					}

					/**
					 * 
		SecurityService service = ServiceLocator.getService(SecurityService.class);
		String token = service.nextSecureToken();
		service.addToken(person.getProfile(UserProfile.class).getUser(), token);
		String url = getServerUrl(person.getDomain()) +"/passwordrecovery?key=" + token;
		
		logger.debug(url);
		emaillogger.debug(url);
					 */
					
				}
				@Override
				protected void onError()
				{
				}
	    
	    };
	    
	    userNameField = new TextField<String>( "username",  new PropertyModel<String>(this, "username"), getLabel("username"));
	    userNameField.setTitleCss("row mb-1");
	    userNameField.setCss("text-center text-lg-center text-md-center text-sm-center text-xl-center textl-xxl-center form-control bg-dark text-light");
	    
		SubmitButton<User> buttons = new SubmitButton<User>("buttons-bottom", getForm()) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit(AjaxRequestTarget target) {
	
			
			
			}
			  public IModel<String> getLabel() {
			    	return getLabel("signin");
			  }
			  protected String getSaveCss() {
			        return "btn  text-light border-light btn-lg";
			    }
		};
		form.add(buttons);
	    
		form.add(userNameField);
	 
		
	    add(form);
		
	    edit();
	}
	
	private boolean isError() {
		return this.isError;
	}
	 

	private Form<User> getForm() {
		return form;
	}
	
	public void edit() {
		getForm().setFormState(FormState.EDIT);
		getForm().visitChildren(Field.class, new IVisitor<Field<?>, Void>() {
			@Override
			public void component(Field<?> field, IVisit<Void> visit) {
				field.editOn();

			}
		});
	}

	 
}