package dellemuse.serverapp.music;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.page.IExpandedPanel;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.serverdb.model.Music;
import dellemuse.serverapp.serverdb.model.Resource;
import io.wktui.media.AudioPlayer;
import wktui.base.InvisiblePanel;

public class MusicExpandedPanel extends DBModelPanel<Music> implements IExpandedPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(MusicExpandedPanel.class.getName());

	public MusicExpandedPanel(String id, IModel<Music> model) {
		super(id, model);
		setOutputMarkupId(true);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		Music music = getModel().getObject();

		// Name
		String name = music.getDisplayname();
		add(new Label("musicName", name != null ? name : ""));

		// Technical Info
		String techInfo = music.getTechnicalInfo();
		Label techInfoLabel = new Label("technicalInfo", (techInfo != null && !techInfo.isEmpty()) ? techInfo : "");
		techInfoLabel.setVisible(techInfo != null && !techInfo.isEmpty());
		add(techInfoLabel);

		// Audio Player
		Resource audio = music.getAudio();
		if (audio != null) {
			String audioUrl = getPresignedUrl(audio);
			if (audioUrl != null) {
				Url url = Url.parse(audioUrl);
				UrlResourceReference resourceReference = new UrlResourceReference(url);
				AudioPlayer audioPlayer = new AudioPlayer("audioPlayer", resourceReference);
				add(audioPlayer);
			} else {
				add(new InvisiblePanel("audioPlayer"));
			}
		} else {
			add(new InvisiblePanel("audioPlayer"));
		}
	}
}
