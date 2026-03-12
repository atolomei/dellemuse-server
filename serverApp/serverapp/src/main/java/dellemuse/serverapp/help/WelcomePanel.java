package dellemuse.serverapp.help;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.resource.UrlResourceReference;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.DellemuseServer;
import dellemuse.serverapp.branded.panel.BrandedArtExhibitionGuidePanel;
import dellemuse.serverapp.page.DellemuseServerAppHomePage;
import dellemuse.serverapp.page.model.ObjectModelPanel;
import dellemuse.serverapp.page.security.LoginPage;
import dellemuse.serverapp.serverdb.model.User;
import io.wktui.media.InvisibleImage;
import io.wktui.nav.menu.NavBar;
import jakarta.servlet.http.Cookie;
import wktui.base.InvisiblePanel;
import wktui.base.LabelLinkPanel;
import wktui.base.ModelPanel;

public class WelcomePanel extends ObjectModelPanel<User> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(WelcomePanel.class.getName());
 

	public WelcomePanel(String id) {
		this(id,  null);
	}

	public WelcomePanel(String id, IModel<User> model) {
		super(id, model);
	}

	 

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		Image imageBlack = new Image("miniLogo", new org.apache.wicket.request.resource.PackageResourceReference(LoginPage.class, "dellemuse-logo-blanco.png"));
		add(imageBlack);
	
		
		Link<Void> aslink = new Link<Void>("audio-studio-link") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new RedirectPage("https://youtu.be/9fKGECJAftk?si=DYihh345IjiR08Y5"));
			}
		};
		add(aslink);
	
		
		
		
		Link<Void> exlink = new Link<Void>("ex-link") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new RedirectPage("https://youtu.be/qREFkyV7IS0"));
			}
		};
		add(exlink);
		
		
		Link<Void> tempclose = new Link<Void>("tempclose") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				getSessionUser().get().setShowWelcome(false);
				getUserDBService().save(getSessionUser().get(), "welcome screen: false", getSessionUser().get()); 
				setResponsePage(new DellemuseServerAppHomePage());
			}
		};
		add(tempclose);
		
		
		Link<Void> close = new Link<Void>("close") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				getSessionUser().get().setShowWelcome(false);
				getUserDBService().save(getSessionUser().get(), "welcome screen: false", getSessionUser().get()); 
				setResponsePage(new DellemuseServerAppHomePage());
			}
		};
		add(close);
	
		
		
		
		
		
		
		
	}
 
	 

}
