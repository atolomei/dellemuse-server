package dellemuse.serverapp.voice;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.page.IExpandedPanel;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Voice;
import io.wktui.media.AudioPlayer;
import wktui.base.InvisiblePanel;

public class VoiceExpandedPanel extends DBModelPanel<Voice> implements IExpandedPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(VoiceExpandedPanel.class.getName());

	public VoiceExpandedPanel(String id, IModel<Voice> model) {
		super(id, model);
		setOutputMarkupId(true);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		Voice voice = getModel().getObject();

		// Name
		String name = voice.getDisplayname();
		add(new Label("voiceName", name != null ? name : ""));

		// Sex
		String sex = voice.getSex();
		Label sexLabel = new Label("sex", (sex != null && !sex.isEmpty()) ? sex : "");
		sexLabel.setVisible(sex != null && !sex.isEmpty());
		add(sexLabel);

		// Language
		String language = voice.getLanguage();
		String languageRegion = voice.getLanguageRegion();
		StringBuilder langStr = new StringBuilder();
		if (language != null && !language.isEmpty()) {
			langStr.append(language);
			if (languageRegion != null && !languageRegion.isEmpty()) {
				langStr.append(" - ").append(languageRegion);
			}
		}
		add(new Label("language", langStr.toString()));

		// Audio Player
		Resource audio = voice.getAudio();
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
