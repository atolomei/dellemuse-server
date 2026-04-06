package dellemuse.serverapp.page.security;

import java.time.OffsetDateTime;
import java.util.Map;
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
import dellemuse.serverapp.email.EmailTemplateService;
import dellemuse.serverapp.global.GlobalFooterPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.serverdb.ServerDBConstant;
import dellemuse.serverapp.serverdb.model.PersistentToken;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.PersonDBService;
import dellemuse.serverapp.serverdb.service.UserDBService;

import io.wktui.error.AlertPanel;
import io.wktui.error.ErrorPanel;
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
 * ZoneId -> geografia Locale -> idioma
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

	public ForgotPasswordPage() {
		super();
	}

	public ForgotPasswordPage(PageParameters parameters) {
		super(parameters);
		if (getPageParameters() != null) {
			isError = (getPageParameters().get("error") != null && getPageParameters().get("error").toString().length() > 1);
			username = getPageParameters().get("username").toString();
		}
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		Image imageBlack = new Image("miniLogo", new org.apache.wicket.request.resource.PackageResourceReference(ForgotPasswordPage.class, "dellemuse-logo-blanco.png"));

		add(imageBlack);

		add(new InvisiblePanel("alert"));

		form = new Form<User>("loginForm") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onInitialize() {
				super.onInitialize();
			}

			@Override
			protected void onSubmit() {

			}

			@Override
			protected void onError() {
			}

		};

		userNameField = new TextField<String>("username", new PropertyModel<String>(this, "username"), getLabel("username"));
		userNameField.setTitleCss("row mb-1");
		userNameField.setCss("text-center text-lg-center text-md-center text-sm-center text-xl-center textl-xxl-center form-control bg-dark text-light");

		SubmitButton<User> buttons = new SubmitButton<User>("buttons-bottom", getForm()) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit(AjaxRequestTarget target) {

				if (username == null || username.trim().isEmpty()) {
					ForgotPasswordPage.this.addOrReplace(new AlertPanel<Void>("alert", AlertPanel.WARNING, null, getLabel("enter-username-email-phone")));
					target.add(ForgotPasswordPage.this);
					return;
				}

				// lookup user by username/email/phone
				Optional<User> ou = getUserDBService().findByUsernameOrEmailOrPhone(username);

				if (ou.isPresent()) {

					User u = ou.get();
					// get person email
					Optional<dellemuse.serverapp.serverdb.model.Person> op = getPersonDBService().getByUser(u);

					String email = null;
					String personName = u.getFirstLastname();

					if (op.isPresent()) {
						personName = op.get().getName() != null ? op.get().getName() : op.get().getFirstLastname();
						email = op.get().getEmail();
					}

					if (email == null || email.trim().isEmpty()) {
						// fallback to user's email field
						email = u.getEmail();
					}

					if (email != null && email.trim().length() > 0) {

						String tokenValue = getSecurityService().nextSecureToken();

						PersistentToken token = getPersistentTokenDBServiceDBService().create(u.getId().toString(), User.class.getSimpleName(), tokenValue, OffsetDateTime.now().plusDays(1));

						Long tid = null;

						try {

							tid = token.getId();

							String to = u.getEmail();

							String subject = getLabel("password-reset-subject").getObject();
							String url = getServerDBSettings().getEmailValidationServer() + "/password-reset/" + u.getId().toString() + "-" + tokenValue + "-" + getLocale().getLanguage();

							String text = getEmailTemplateService().render(EmailTemplateService.PASSWORD_RESET, Map.of("application", DellemuseServer.APPNAME, "personName", personName, "resetLink", url));

							logger.debug(url);

							logger.debug("Sending email to -> " + to);
							logger.debug("Sending email subject -> " + subject);

							String sendEmail = getEmailService().sendHTML(to, subject, text);

							logger.debug("Email sent response -> " + sendEmail);

						} catch (Exception e) {
							if (tid != null)
								getPersistentTokenDBServiceDBService().findById(tid).ifPresent(t -> getPersistentTokenDBServiceDBService().delete(t));
							ForgotPasswordPage.this.addOrReplace(new ErrorPanel("alert", e));
							logger.error(e);
						}

						String css = "col-xxl-12 col-xl-12 col-lg-12 col-md-12 col-sm-12 text-center text-lg-center text-xl-center text-xxl-center";
						AlertPanel<Void> s = new AlertPanel<Void>("alert", AlertPanel.SUCCESS, null, getLabel("email-sent"));
						s.setAlertTextContainerCss(css);
						ForgotPasswordPage.this.addOrReplace(s);

					} else {
						ForgotPasswordPage.this.addOrReplace(new AlertPanel<Void>("alert", AlertPanel.DANGER, null, getLabel("no-email-associated")));
					}
				} else {
					ForgotPasswordPage.this.addOrReplace(new AlertPanel<Void>("alert", AlertPanel.DANGER, null, getLabel("no-user-found")));
				}

				target.add(ForgotPasswordPage.this);

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

	@Override
	public boolean hasAccessRight(Optional<User> ouser) {
		return true;
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