package dellemuse.serverapp.page.library;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ExternalImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.page.IExpandedPanel;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.site.SitePage;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import io.wktui.error.ErrorPanel;
import io.wktui.media.InvisibleImage;
import wktui.base.InvisiblePanel;

public class InstitutionExpandedPanel extends DBModelPanel<Institution> implements IExpandedPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(InstitutionExpandedPanel.class.getName());

	public InstitutionExpandedPanel(String id, IModel<Institution> model) {
		super(id, model);
		setOutputMarkupId(true);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		try {

			add(new InvisiblePanel("error"));

			Institution inst = getModel().getObject();

			// Thumbnail
			String imgSrc = null;
			if (inst.getPhoto() != null) {
				imgSrc = getImageSrc(inst.getPhoto());
			}
			if (imgSrc != null) {
				add(new ExternalImage("thumbnail", imgSrc));
			} else {
				add(new InvisibleImage("thumbnail"));
			}

			// Name
			String name = getLanguageObjectService().getObjectDisplayName(inst, getLocale());
			add(new Label("institutionName", name != null ? name : ""));

			// Subtitle
			String subtitle = inst.getSubtitle();
			Label subtitleLabel = new Label("subtitle", (subtitle != null && !subtitle.isEmpty()) ? subtitle : "");
			subtitleLabel.setVisible(subtitle != null && !subtitle.isEmpty());
			add(subtitleLabel);

			// Sites list
			List<Site> sites = inst.getSites();

			ListView<Site> sitesListView = new ListView<Site>("sitesList", sites) {

				private static final long serialVersionUID = 1L;

				@Override
				protected void populateItem(ListItem<Site> item) {
					Site site = item.getModelObject();

					Link<Site> siteLink = new Link<Site>("siteLink", new ObjectModel<Site>(site)) {
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick() {
							setResponsePage(new SitePage(getModel()));
						}
					};
					siteLink.add(new Label("siteName", site.getName() != null ? site.getName() : ""));
					item.add(siteLink);
				}
			};
			sitesListView.setVisible(sites != null && !sites.isEmpty());
			add(sitesListView);

		} catch (Exception e) {
			logger.error(e);
			addOrReplace(new InvisibleImage("thumbnail"));
			addOrReplace(new Label("institutionName", ""));
			addOrReplace(new Label("subtitle", "").setVisible(false));
			addOrReplace(new ListView<Site>("sitesList") {
				private static final long serialVersionUID = 1L;
				@Override
				protected void populateItem(ListItem<Site> item) {
				}
			}.setVisible(false));
			addOrReplace(new ErrorPanel("error", e));
		}
	}
}
