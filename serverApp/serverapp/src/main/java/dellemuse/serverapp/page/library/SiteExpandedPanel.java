package dellemuse.serverapp.page.library;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ExternalImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.page.IExpandedPanel;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.site.SitePublishedPortalPage;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.Site;
import io.wktui.error.ErrorPanel;
import io.wktui.media.InvisibleImage;
import wktui.base.InvisiblePanel;

public class SiteExpandedPanel extends DBModelPanel<Site> implements IExpandedPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteExpandedPanel.class.getName());

	public SiteExpandedPanel(String id, IModel<Site> model) {
		super(id, model);
		setOutputMarkupId(true);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		try {

			add(new InvisiblePanel("error"));

			Site site = getModel().getObject();

			// Thumbnail
			String imgSrc = null;
			if (site.getPhoto() != null) {
				imgSrc = getImageSrc(site.getPhoto());
			}
			if (imgSrc != null) {
				add(new ExternalImage("thumbnail", imgSrc));
			} else {
				add(new InvisibleImage("thumbnail"));
			}

			// Name
			String name = getLanguageObjectService().getObjectDisplayName(site, getLocale());
			add(new Label("siteName", name != null ? name : ""));

		 

			// Master Language
			String masterLang = site.getMasterLanguage();
			String masterLangDisplay = "";
			if (masterLang != null && !masterLang.isEmpty()) {
				masterLangDisplay = Language.of(masterLang).getLabel(getLocale());
			}
			add(new Label("masterLanguage", masterLangDisplay));

			// Secondary Languages
			List<Language> allLanguages = site.getLanguages();
			String secondaryLangs = "";
			if (allLanguages != null && !allLanguages.isEmpty()) {
				secondaryLangs = allLanguages.stream()
						.filter(l -> !l.getLanguageCode().equals(masterLang))
						.map(l -> l.getLabel(getLocale()))
						.collect(Collectors.joining(", "));
			}
			Label secondaryLabel = new Label("secondaryLanguages", secondaryLangs);
			secondaryLabel.setVisible(!secondaryLangs.isEmpty());
			add(secondaryLabel);

			// Public Portal Link
			Link<Site> portalLink = new Link<Site>("portalLink", getModel()) {
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick() {
					setResponsePage(new SitePublishedPortalPage(getModel()));
				}
			};
			portalLink.setVisible(site.isPublicPortalEnabled());
			add(portalLink);
			String publicPortal= getPublicUrl(site);
			portalLink.add(new Label("publicPortal", publicPortal != null ? publicPortal : ""));
			

		} catch (Exception e) {
			logger.error(e);
			addOrReplace(new InvisibleImage("thumbnail"));
			addOrReplace(new Label("siteName", ""));
			addOrReplace(new Label("shortName", "").setVisible(false));
			addOrReplace(new Label("masterLanguage", ""));
			addOrReplace(new Label("secondaryLanguages", "").setVisible(false));
			addOrReplace(new Link<Void>("portalLink") {
				private static final long serialVersionUID = 1L;
				@Override
				public void onClick() {
				}
			}.setVisible(false));
			addOrReplace(new ErrorPanel("error", e));
		}
	}
}
