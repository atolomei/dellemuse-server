package dellemuse.serverapp.global;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.DellemuseServer;
import dellemuse.serverapp.branded.panel.BrandedArtExhibitionGuidePanel;
import dellemuse.serverapp.help.HelpButtonToolbarItem;
import dellemuse.serverapp.serverdb.model.User;
import io.wktui.media.InvisibleImage;
import io.wktui.nav.menu.NavBar;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import wktui.base.InvisiblePanel;
import wktui.base.LabelLinkPanel;
import wktui.base.ModelPanel;

public class GlobalTopPanel extends ModelPanel<User> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(GlobalTopPanel.class.getName());

	private boolean isSearch = false;
	private String srcUrl;

	private Panel userGlobalTopPanel;

	public GlobalTopPanel(String id) {
		this(id, null, null);
	}

	public GlobalTopPanel(String id, IModel<User> model) {
		super(id, model);
	}

	public GlobalTopPanel(String id, IModel<User> userModel, String srcUrl) {
		super(id, userModel);
		this.srcUrl = srcUrl;
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		NavBar<Void> nav = new NavBar<Void>("navbarLeft");

		LabelLinkPanel logo = new LabelLinkPanel("item") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick() {
				setResponsePage(new RedirectPage("/home"));
			}
		};

		logo.setLabel(Model.of("dellemuse"));
		logo.setSubtitle(getLabel("dellemuse-tagline"));
		logo.setSubtitleCss("subtitle d-none d-lg-inline d-xl-inline d-xxl-inline");

		 
 

		nav.addNoCollapseLeft(logo);

	 

		logo.setLinkStyle("text-decoration: none;");
	 

		add(nav);

		Link<Void> find = new Link<Void>("find") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				// setResponsePage(new SiteSearchArtWorkPage(getSiteModel()));
			}

			public boolean isVisible() {
				return isSearch();
			}
		};
		add(find);

		Link<Void> sup = new Link<Void>("signup") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				// setResponsePage( new DellemuseWebSignupPage());
			}
		};

		sup.setVisible(false);
		add(sup);

		Link<Void> si = new Link<Void>("signin") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				// setResponsePage( new DellemuseWebSigninPage());
			}
		};

		si.setVisible(false);
		add(si);

		if (getModel() != null) {
			this.userGlobalTopPanel = new UserGlobalTopPanel("userGlobalTopPanel", getModel());
		} else {
			this.userGlobalTopPanel = new InvisiblePanel("userGlobalTopPanel");
		}

		add(this.userGlobalTopPanel);

		HelpButtonToolbarItem h = new HelpButtonToolbarItem("help", Align.TOP_RIGHT);
		add(h);
		
	}

 
	public boolean isSearch() {
		return isSearch;
	}

	public void setSearch(boolean isSearch) {
		this.isSearch = isSearch;
	}

}
