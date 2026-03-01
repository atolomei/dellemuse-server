package dellemuse.serverapp.test;

import java.util.Optional;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.serverdb.model.User;
import io.wktui.media.AudioPlayer;

 

/**
 * Demo page to test the custom AudioPlayer component without affecting production code.
 */
@MountPath("/test")
public class AudioPlayerDemoPage extends BasePage {

    private static final long serialVersionUID = 1L;

    public AudioPlayerDemoPage() {
        super();

        add(new Label("title", "AudioPlayer demo"));

        // public sample mp3 for testing (SoundHelix example)
        String audioUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3";
        UrlResourceReference ref = new UrlResourceReference(Url.parse(audioUrl));

        AudioPlayer player = new AudioPlayer("player", ref);
        player.setIncludeDownloadMenu(false);
        add(player);
    }

	@Override
	public boolean hasAccessRight(Optional<User> ouser) {
		return true;
	}
}
