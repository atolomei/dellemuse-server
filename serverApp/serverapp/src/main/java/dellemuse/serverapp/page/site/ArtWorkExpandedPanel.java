package dellemuse.serverapp.page.site;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ExternalImage;
import org.apache.wicket.model.IModel;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.page.IExpandedPanel;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.serverdb.model.ArtWork;
import io.wktui.error.ErrorPanel;
import io.wktui.media.InvisibleImage;
import wktui.base.InvisiblePanel;

public class ArtWorkExpandedPanel extends DBModelPanel<ArtWork> implements IExpandedPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ArtWorkExpandedPanel.class.getName());

	public ArtWorkExpandedPanel(String id, IModel<ArtWork> model) {
		super(id, model);
		setOutputMarkupId(true);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		try {

			add(new InvisiblePanel("error"));

			ArtWork aw = getModel().getObject();

			// Thumbnail
			String imgSrc = getImageSrc(aw);
			if (imgSrc != null) {
				add(new ExternalImage("thumbnail", imgSrc));
			} else {
				add(new InvisibleImage("thumbnail"));
			}

			// Name
			String name = getLanguageObjectService().getObjectDisplayName(aw, getLocale());
			add(new Label("artworkName", name != null ? name : ""));

			// Artists
			String artists = getArtistStr(aw);
			Label artistsLabel = new Label("artists", (artists != null && !artists.isEmpty()) ? artists : "");
			artistsLabel.setVisible(artists != null && !artists.isEmpty());
			add(artistsLabel);

			// Audio Id
			Long audioId = aw.getAudioId();
			Label audioIdLabel = new Label("audioId", audioId != null ? audioId.toString() : "");
			audioIdLabel.setVisible(audioId != null);
			add(audioIdLabel);

		} catch (Exception e) {
			logger.error(e);
			addOrReplace(new InvisibleImage("thumbnail"));
			addOrReplace(new Label("artworkName", ""));
			addOrReplace(new Label("artists", "").setVisible(false));
			addOrReplace(new Label("audioId", "").setVisible(false));
			addOrReplace(new ErrorPanel("error", e));
		}
	}
}
