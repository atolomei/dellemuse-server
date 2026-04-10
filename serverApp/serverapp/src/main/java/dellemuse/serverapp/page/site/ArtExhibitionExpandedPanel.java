package dellemuse.serverapp.page.site;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ExternalImage;
import org.apache.wicket.model.IModel;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.NumberFormatter;
import dellemuse.serverapp.page.IExpandedPanel;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.Resource;
import io.wktui.media.InvisibleImage;

public class ArtExhibitionExpandedPanel extends DBModelPanel<ArtExhibition> implements IExpandedPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ArtExhibitionExpandedPanel.class.getName());

	public ArtExhibitionExpandedPanel(String id, IModel<ArtExhibition> model) {
		super(id, model);
		setOutputMarkupId(true);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		ArtExhibition ex = getModel().getObject();

		// Thumbnail
		String imgSrc = getImageSrc(ex);
		if (imgSrc != null) {
			add(new ExternalImage("thumbnail", imgSrc));
		} else {
			add(new InvisibleImage("thumbnail"));
		}

		// Name
		String name = getLanguageObjectService().getObjectDisplayName(ex, getLocale());
		add(new Label("exhibitionName", name != null ? name : ""));

		// Subtitle
		String subtitle = ex.getSubtitle();
		Label subtitleLabel = new Label("subtitle", (subtitle != null && !subtitle.isEmpty()) ? subtitle : "");
		subtitleLabel.setVisible(subtitle != null && !subtitle.isEmpty());
		add(subtitleLabel);

		// Audio Id
		Long audioId = ex.getAudioId();
		Label audioIdLabel = new Label("audioId", audioId != null ? audioId.toString() : "");
		audioIdLabel.setVisible(audioId != null);
		add(audioIdLabel);

		/**
		// Duration (from audio resource)
		String durationStr = null;
		Resource audio = ex.getAudio();
		if (audio != null) {
			Long durationMs = getResourceDBService().findWithDeps(audio.getId()).get().getDurationMilliseconds();
			if (durationMs != null)
				durationStr = NumberFormatter.formatDuration(durationMs);
		}
		Label durationLabel = new Label("duration", durationStr != null ? durationStr : "");
		durationLabel.setVisible(durationStr != null);
		add(durationLabel);
**/
		
	}
}
