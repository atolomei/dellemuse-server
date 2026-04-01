package dellemuse.serverapp.branded.panel;

import org.apache.wicket.AttributeModifier;

import org.apache.wicket.markup.html.image.Image;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;

import dellemuse.serverapp.branded.BrandedSitePage;

import dellemuse.serverapp.global.LanguagePanel;

import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.model.ObjectModelPanel;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import io.wktui.nav.menu.NavBar;

import wktui.base.LogoPanel;

public class BrandedGlobalTopPanel extends ObjectModelPanel<Site> {

	private static final long serialVersionUID = 1L;

	private String srcUrl;

	public BrandedGlobalTopPanel(String id) {
		this(id, null, null);
	}

	public BrandedGlobalTopPanel(String id, IModel<Site> model) {
		super(id, model);
	}

	public BrandedGlobalTopPanel(String id, IModel<Site> sModel, String srcUrl) {
		super(id, sModel);
		this.srcUrl = srcUrl;
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		{
			NavBar<Site> nav = new NavBar<Site>("navbarLeft", getModel());

			String logoUrl = getPresignedUrl(getModel().getObject().getLogo());

			Url url = Url.parse(logoUrl);
			UrlResourceReference resourceReference = new UrlResourceReference(url);
			Image image = new Image("logo", resourceReference);

			image.add(new AttributeModifier("style", "max-height:38px; max-width:102px;"));

			LogoPanel<Site> logoPanel = new LogoPanel<Site>("item", getModel(), image) {
				private static final long serialVersionUID = 1L;

				public void onClick() {
					setResponsePage(new BrandedSitePage(getModel()));
				}
			};

			nav.addNoCollapseLeft(logoPanel);
			add(nav);
		}

		{
			NavBar<Site> nav = new NavBar<Site>("navbarRight", getModel());

			
			BrandedQRScanPanel qr = new BrandedQRScanPanel("item");
			nav.addNoCollapseLeft(qr);
			
			BrandedSearchTopPanel search = new BrandedSearchTopPanel("item");
			nav.addNoCollapseLeft(search);

			if (getSessionUser().isPresent()) {
				nav.addNoCollapseLeft(new BrandedAccesibilityPanel("item", new ObjectModel<User>(getSessionUser().get())));
				LanguagePanel gt = new LanguagePanel("item", new ObjectModel<User>(getSessionUser().get()), getModel());
				nav.addNoCollapseLeft(gt);
			} else {
				nav.addNoCollapseLeft(new BrandedAccesibilityPanel("item", null));
				LanguagePanel gt = new LanguagePanel("item", null, getModel());
				nav.addNoCollapseLeft(gt);
			}

			
			
			
			add(nav);
		}

	}

}
