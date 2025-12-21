package dellemuse.serverapp.register;

 
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.wicketstuff.annotation.mount.MountPath;

import jakarta.servlet.http.HttpServletResponse;


import com.giffing.wicket.spring.boot.context.scan.WicketSignInPage;
import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.DellemuseServer;
import dellemuse.serverapp.global.GlobalFooterPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.serverdb.model.User;
import io.wktui.error.AlertPanel;
import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.SubmitButton;
import io.wktui.form.field.Field;
import io.wktui.form.field.TextField;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import wktui.base.InvisiblePanel;

/**
 * 
 * site foto Info - exhibitions
 * 
 * 
 * 
 * ZoneId -> geografia
 * Locale -> idioma
 * 
 * 
 * 
 */

@WicketSignInPage
@MountPath("/signin")
public class LoginPage extends BasePage {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(LoginPage.class.getName());
 
	
	private WebMarkupContainer alertContainer;	
	private AlertPanel<Void> alert;

	private Form<User> form;
	
	private String username = "";
	private String password = "";
	
	private TextField<String> userNameField;
	private TextField<String> passwordField;

	private boolean isError = false;
	
	
	@Override
	public boolean hasAccessRight(Optional<User> ouser) {
		return true;
	}
	
	
	
	
	public LoginPage() {
		super();
	}
	public LoginPage(PageParameters parameters) {
		super(parameters);
		if (getPageParameters()!=null) {
			isError=(getPageParameters().get("error")!=null && getPageParameters().get("error").toString().length()>1);
			username=getPageParameters().get("username").toString();
		}
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		
		if (getSession().isTemporary()) {
	        getSession().bind(); //   fuerza creación temprana de sesión
	    }
	 	
		//add(new GlobalTopPanel("top-panel"));
		//add(new GlobalFooterPanel<Void>("footer-panel"));
		//add(new FeedbackPanel("feedback"));
		//add(new InvisiblePanel("page-header"));
		
		Image image = new Image(
			    "logo",
			    new org.apache.wicket.request.resource.PackageResourceReference(
			        LoginPage.class,
			        "dellemuse-logo-blanco.png"
			    )
			);
		
		
		add(image);
		
		
		alertContainer  = new WebMarkupContainer("alertContainer");
		add(alertContainer);
		
		if (isError()) {
			alert = new  AlertPanel<Void>("alert", AlertPanel.DANGER, getLabel( "username-password-invalid"));
			alertContainer.add(alert);
		}
		else {
			alertContainer.setVisible(false);
			alertContainer.add( new InvisiblePanel("alert"));
		}
		 
	    form = new Form<User>("loginForm") {
	    	
				private static final long serialVersionUID = 1L;

				@Override
				protected void onInitialize() {
				    super.onInitialize();
				    if (getSession().isTemporary()) {
				        getSession().bind(); // fuerza la creación de la sesión Wicket
				    }
				}
				 
				@Override
	            protected void onSubmit() {
	
					SecureWebSession session = (SecureWebSession) getSession();
	       
	                if (session.signIn(username, password)) {
 	       
	                	ServletWebRequest servletRequest = (ServletWebRequest) RequestCycle.get().getRequest();
	                	HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest.getContainerRequest();
	                	WebResponse webResponse = (WebResponse) RequestCycle.get().getResponse();
	                    HttpServletResponse httpServletResponse = (HttpServletResponse) webResponse.getContainerResponse();
	                	HttpSession httpSession = httpServletRequest.getSession(true); // create if missing
	                	// store the Spring SecurityContext under the standard key
	                	httpSession.setAttribute(
	                	    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
	                	    SecurityContextHolder.getContext()
	                	);
	                	
	                	logger.debug("LOGIN: sessionId= "+ httpSession.getId()  + " springContextPresent=" +
	                		    (httpSession.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY
	                		    ) != null));
	                	isError = false;

	                	completeLogin();
		                    
	                } else {
	                	logger.debug("Invalid username or password");
	                	PageParameters parameters = new PageParameters();
	                	parameters.add("error", "invalid-username-or-password");
	                	parameters.add("username", username);
		              	isError = true;
		            	setResponsePage( new LoginPage(parameters));
	                    error("Invalid username or password.");
	                }
	            }
				@Override
				protected void onError()
				{
					logger.debug("dedd");
					

                	/**
                	ServletWebResponse webResponse = (ServletWebResponse) RequestCycle.get().getResponse();
                	HttpServletResponse httpResponse = webResponse.getContainerResponse();
                	try {
						httpResponse.flushBuffer();
					} catch (IOException e) {
						logger.error(e);
					} **/
                	 	
               
                	
                	//logger.debug("Sign in -> " + username +" | " + password);
                	//logger.debug("login session id: " + httpSession.getId());
                	
                	/**
                	Enumeration<String> names = httpSession.getAttributeNames();
                	while (names.hasMoreElements()) {
                	    String n = names.nextElement();
                	    logger.debug("attr: " + n);
                	}
                	**/

				}
	    
	    };
	    
	    userNameField = new TextField<String>( "username",  new PropertyModel<String>(this, "username"), getLabel("username"));
	    passwordField = new TextField<String>( "password",  new PropertyModel<String>(this, "password"), getLabel("password"));
	    
	    userNameField.setCss("text-center text-lg-center text-md-center text-sm-center text-xl-center textl-xxl-center form-control");
	    passwordField.setCss("text-center text-lg-center text-md-center text-sm-center text-xl-center textl-xxl-center form-control");
	    
	    
		SubmitButton<User> buttons = new SubmitButton<User>("buttons-bottom", getForm()) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit(AjaxRequestTarget target) {
				logger.debug("error");
				//ArtExhibitionEditor.this.onEdit(target);
			}
			  public IModel<String> getLabel() {
			    	return getLabel("signin");
			  }
			  protected String getSaveCss() {
			        return "btn btn-primary btn-lg";
			    }
		};
		form.add(buttons);
	    
		form.add(userNameField);
		form.add(passwordField);
		
	    add(form);
		
	    edit();
	}
	
	private boolean isError() {
		return this.isError;
	}
	
	/**
	 private void authenticateUser(String username, String password) {
	        try {
	            Authentication authentication =
	                authenticationManager.authenticate(
	                    new UsernamePasswordAuthenticationToken(username, password)
	                );
	            SecureWebSession wsession = (SecureWebSession) getSession();
	          
	            SecurityContextHolder.getContext().setAuthentication(authentication);

	            // 2️⃣ Persist to HTTP session
	            HttpServletRequest request =
	                ((ServletWebRequest) RequestCycle.get().getRequest()).getContainerRequest();
	            HttpSession session = request.getSession(true);
	            session.setAttribute(
	                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
	                SecurityContextHolder.getContext()
	            );
	        
	        } catch (Exception ex) {
	            error("Invalid username or password");
	        }
	    }
	 */
	

	private Form<User> getForm() {
		return form;
	}
	
	public void edit() {
		getForm().setFormState(FormState.EDIT);
		getForm().visitChildren(Field.class, new IVisitor<Field<?>, Void>() {
			@Override
			public void component(Field<?> field, IVisit<Void> visit) {
				// if (focus==null) {
				// target.focusComponent(field.getInput());
				// focus = field;
				// }
				field.editOn();

			}
		});
	}

	@SpringBean
    private SavedRequestAwareAuthenticationSuccessHandler successHandler;
	
	
	   private void completeLogin() {

	        // --- Integrar Spring Security ---
	        ServletWebRequest servletRequest = (ServletWebRequest) RequestCycle.get().getRequest();
	        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest.getContainerRequest();
	        WebResponse webResponse = (WebResponse) RequestCycle.get().getResponse();
	        HttpServletResponse httpServletResponse = (HttpServletResponse) webResponse.getContainerResponse();

	        // --- Redirección ---
	        try {
	            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	            // Intenta usar SavedRequest de Spring Security primero
	            successHandler.onAuthenticationSuccess(httpServletRequest, httpServletResponse, auth);
	            
	            logger.debug("successHandler");
	            
	            
	        } catch (Exception e) {
	            // fallback Wicket: continueToOriginalDestination
	            try {
	                
	                continueToOriginalDestination();
                    String url = urlFor(getApplication().getHomePage(), null).toString();
                    RequestCycle.get().scheduleRequestHandlerAfterCurrent(
                        new  RedirectRequestHandler(url)
                    );
	            	
	            } catch (Exception ex) {
	                // fallback final al HomePage
	                String url = urlFor(getApplication().getHomePage(), null).toString();
	                RequestCycle.get().scheduleRequestHandlerAfterCurrent(
	                        new RedirectRequestHandler(url)
	                );
	            }
	        }

	        isError = false;
	    }
	
}

