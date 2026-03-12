package dellemuse.serverapp.page.security;

import java.util.Locale;
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
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.PersistentToken;
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

@MountPath("/password-reset/${token}")
public class PasswordResetPage extends BasePage {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(PasswordResetPage.class.getName());

	@Override
	public boolean hasAccessRight(Optional<User> ouser) {
		return true;
	}

	public PasswordResetPage() {
		this(null);
	}

	public PasswordResetPage(PageParameters parameters) {
		super(parameters);
		 token = getPageParameters().get("token").toString();
		
		if (token == null || token.length() == 0) {
			return;
		}
		
		String arr[] = token.split("-");
		
		if (arr.length != 3) {
			return;
		}

		 userid = arr[0];
		 tokenstr = arr[1];
		 lang = arr[2];
		
		if (lang == null)
			this.lang = Locale.getDefault().getLanguage();

		getSession().setLocale(Locale.forLanguageTag(lang));
	}

	@Override
	public Locale getLocale() {
		if (lang == null)
			return Locale.getDefault();
		return Locale.forLanguageTag(lang);
	}

	String userid;
	String tokenstr;
	String token;
	String lang;

	@Override
	public void onInitialize() {
		super.onInitialize();

		add(new GlobalTopPanel("top-panel"));
		add(new InvisiblePanel("footer-panel"));
		add(new InvisiblePanel("editor"));
		add(new InvisiblePanel("error"));


		if (token == null || token.length() == 0) {
			addOrReplace(new AlertPanel<Void>("error", AlertPanel.DANGER, new StringResourceModel("invalidtoken", this, null)));
			return;
		}


		if (userid==null || tokenstr==null || lang==null) {
			addOrReplace(new AlertPanel<Void>("error", AlertPanel.DANGER, new StringResourceModel("invalidtoken", this, null)));
			return;
		}


		Iterable<PersistentToken> opt = getPersistentTokenDBServiceDBService().findByToken(tokenstr);

		if (opt == null) {
			addOrReplace(new AlertPanel<Void>("error", AlertPanel.DANGER, new StringResourceModel("invalidtoken", this, null)));
			return;
		}

		for (PersistentToken t : opt) {
			if (t.getEntityClass().equals(User.class.getSimpleName()) && t.getEntity().equals(userid)) {
				User c = getUserDBService().findById(Long.parseLong(userid)).orElse(null);
				if (c != null) {
					addOrReplace(new PasswordResetEditor("editor", new ObjectModel<User>(c), t.getId()));
					return;
				}
			}
		}

		addOrReplace(new AlertPanel<Void>("error", AlertPanel.DANGER, new StringResourceModel("invalidtoken", this, null)));

	}

}
