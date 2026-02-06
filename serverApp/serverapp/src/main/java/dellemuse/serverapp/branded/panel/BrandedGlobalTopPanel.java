package dellemuse.serverapp.branded.panel;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;

import dellemuse.serverapp.global.LanguagePanel;
import dellemuse.serverapp.global.UserGlobalTopPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.model.ObjectModelPanel;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import io.wktui.nav.menu.NavBar;
import wktui.base.InvisiblePanel;
import wktui.base.LabelLinkPanel;
import wktui.base.LogoPanel;
import wktui.base.ModelPanel;


public class BrandedGlobalTopPanel extends ObjectModelPanel<Site> {

	private static final long serialVersionUID = 1L;

	
	private String srcUrl;
	
	public BrandedGlobalTopPanel(String id) {
		this(id, null,  null);
	}
	
	
	public BrandedGlobalTopPanel(String id, IModel<Site> model) {
		super(id, model);
	}
	
	
	public BrandedGlobalTopPanel(String id,  IModel<Site> userModel, String srcUrl) {
		super(id, userModel);
		this.srcUrl=srcUrl;
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		
	 
		{
			NavBar<Site> nav = new NavBar<Site>("navbarLeft" , getModel());
	
		 	String logoUrl = getPresignedUrl(getModel().getObject().getLogo());
			
			Url url = Url.parse(logoUrl);
			UrlResourceReference resourceReference = new UrlResourceReference(url);
			Image image = new Image("logo", resourceReference);
	
			image.add(new AttributeModifier("style", "max-height:38px; max-width:112px;"));
	
			LogoPanel<Site> logoPanel = new LogoPanel<Site>("item", getModel(), image) {
				private static final long serialVersionUID = 1L;
				public void onClick() {
					setResponsePage(new RedirectPage(getModel().getObject().getWebsite()));
				}
			};
	
		 	
			nav.addNoCollapseLeft(logoPanel);
			add(nav);  
		}
		
		
		{
			NavBar<Site> nav = new NavBar<Site>("navbarRight" , getModel());

			BrandedAccesibilityPanel b = new BrandedAccesibilityPanel("item",  new ObjectModel<User>(getSessionUser().get()));
			nav.addNoCollapseLeft(b);
	
			LanguagePanel gt = new LanguagePanel("item",  new ObjectModel<User>(getSessionUser().get()));
			nav.addNoCollapseLeft(gt);
			
			add(nav);  
			
		}
		
		
	}

}
