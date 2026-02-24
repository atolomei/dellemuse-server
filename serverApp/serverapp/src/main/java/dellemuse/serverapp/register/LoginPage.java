package dellemuse.serverapp.register;

import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.pages.RedirectPage;
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
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.wicketstuff.annotation.mount.MountPath;

import jakarta.servlet.http.HttpServletResponse;

import com.giffing.wicket.spring.boot.context.scan.WicketSignInPage;
import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.DellemuseServer;
import dellemuse.serverapp.global.GlobalFooterPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.page.DellemuseServerAppHomePage;
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
 * ZoneId -> geografia Locale -> idioma
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

	@SpringBean
	private SavedRequestAwareAuthenticationSuccessHandler successHandler;

	@Override
	public boolean hasAccessRight(Optional<User> ouser) {
		return true;
	}

	public LoginPage() {
		super();
	}

	public LoginPage(PageParameters parameters) {
		super(parameters);
		if (getPageParameters() != null) {
			
			if (getPageParameters().get("error") != null) {
				StringValue s = getPageParameters().get("error");
				if (!s.isEmpty()) {
					isError=true;
				}
			}
			if (getPageParameters().get("username") != null) {
				StringValue s = getPageParameters().get("username");
				if (!s.isEmpty()) {
					username = s.toString();
				}
			}
			
		}
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		if (getSession().isTemporary()) {
			getSession().bind();
		}

		Image imageBlack = new Image("miniLogo", new org.apache.wicket.request.resource.PackageResourceReference(LoginPage.class, "dellemuse-logo-blanco.png"));

		add(imageBlack);

		alertContainer = new WebMarkupContainer("alertContainer");
		add(alertContainer);

		if (isError()) {
			alert = new AlertPanel<Void>("alert", AlertPanel.DANGER, getLabel("username-password-invalid"));

			alertContainer.add(alert);
		} else {
			alertContainer.setVisible(false);
			alertContainer.add(new InvisiblePanel("alert"));
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

				    if (!session.signIn(username, password)) {
				        logger.error("Invalid username or password -> u." + username);
				        error("Invalid username or password");
				        return;
				    }

				    logger.debug("successful login -> " + username);

				    // VERY IMPORTANT → bind Wicket session immediately
				    getSession().bind();

				    // Force session creation at container level
				    ServletWebRequest servletRequest =
				        (ServletWebRequest) RequestCycle.get().getRequest();

				    HttpServletRequest request =
				        (HttpServletRequest) servletRequest.getContainerRequest();

				    request.getSession(true);

				    // Continue original destination OR go home
				    continueToOriginalDestination();
				    setResponsePage(getApplication().getHomePage());
			
			
			}

			@Override
			protected void onError() {
				logger.error("error");
			}
		};

		userNameField = new TextField<String>("username", new PropertyModel<String>(this, "username"), getLabel("username"));
		passwordField = new TextField<String>("password", new PropertyModel<String>(this, "password"), getLabel("password"));
		userNameField.setTitleCss("row mb-1");
		userNameField.setCss("text-center text-lg-center text-md-center text-sm-center text-xl-center textl-xxl-center form-control bg-dark text-light");
		passwordField.setCss("text-center text-lg-center text-md-center text-sm-center text-xl-center textl-xxl-center form-control bg-dark text-light");
		passwordField.setTitleCss("row mb-1");

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
		form.add(passwordField);
		add(form);
		edit();
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

	private Form<User> getForm() {
		return form;
	}

	private boolean isError() {
		return this.isError;
	}

}
