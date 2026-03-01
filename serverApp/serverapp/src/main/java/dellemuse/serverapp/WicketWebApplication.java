package dellemuse.serverapp;

import org.apache.wicket.Page;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AnnotationsRoleAuthorizationStrategy;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.stereotype.Component;
import com.giffing.wicket.spring.boot.starter.app.WicketBootSecuredWebApplication;
import com.giffing.wicket.spring.boot.starter.configuration.extensions.external.spring.security.SecureWebSession;

import dellemuse.serverapp.page.DellemuseServerAppHomePage;
import dellemuse.serverapp.page.security.LoginPage;
import io.wktui.media.AudioPlayerDemoPage;

@Component
public class WicketWebApplication extends WicketBootSecuredWebApplication {

	@Override
	protected Class<? extends WebPage> getSignInPageClass() {
		return LoginPage.class;
	}

	@Override
	public Class<? extends Page> getHomePage() {
		return DellemuseServerAppHomePage.class;
	}

	@Override
	protected Class<? extends AuthenticatedWebSession> getWebSessionClass() {
		return SecureWebSession.class;
	}

	@Override
	public void init() {
		super.init();

		getComponentInstantiationListeners().add(new SpringComponentInjector(this));

		// ⭐ Enable Wicket page authorization
		getSecuritySettings().setAuthorizationStrategy(new AnnotationsRoleAuthorizationStrategy(this));

		// Mount demo page for testing the custom AudioPlayer
		mountPage("/audio-player-demo", AudioPlayerDemoPage.class);
	}

}