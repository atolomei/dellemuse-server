package dellemuse.serverapp.audiostudio;

import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;

import dellemuse.model.logging.Logger;

import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;

import dellemuse.serverapp.serverdb.model.AudioStudio;
import dellemuse.serverapp.serverdb.model.Music;

import io.wktui.media.AudioPlayer;
import wktui.base.InvisiblePanel;

public class AudioStudioTestMusicPanel extends DBModelPanel<AudioStudio> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(AudioStudioTestMusicPanel.class.getName());

	private IModel<Music> musicModel;
	private WebMarkupContainer test;

	/**
	 * @param id
	 * @param model
	 */
	public AudioStudioTestMusicPanel(String id, IModel<AudioStudio> model, IModel<Music> voice) {
		super(id, model);

		this.musicModel = voice;
		setOutputMarkupId(true);
	}

	public void onDetach() {
		super.onDetach();

		if (this.musicModel != null)
			this.musicModel.detach();
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		Optional<AudioStudio> o_a = getAudioStudioDBService().findWithDeps(getModel().getObject().getId());
		setModel(new ObjectModel<>(o_a.get()));

		Optional<Music> o_c = getMusicDBService().findWithDeps(getMusicModel().getObject().getId());
		setMusicModel(new ObjectModel<Music>(o_c.get()));

		String in = this.musicModel.getObject().getInfo();
		if (in != null)
			in = in.replace("\n", "<br/>");
		else
			in = "";

		Label info = new Label("info", in);

		info.setEscapeModelStrings(false);

		add(info);

		AjaxLink<Void> test = new AjaxLink<Void>("testlink") {
			@Override
			public void onClick(AjaxRequestTarget target) {

				if (AudioStudioTestMusicPanel.this.test.isVisible())
					AudioStudioTestMusicPanel.this.test.setVisible(false);
				else
					AudioStudioTestMusicPanel.this.test.setVisible(true);

				target.add(AudioStudioTestMusicPanel.this);
			}

			public boolean isEnabled() {
				return AudioStudioTestMusicPanel.this.getMusicModel().getObject().getAudio() != null;
			}
		};

		add(test);
		addTest();

	}

	private void addTest() {

		this.test = new WebMarkupContainer("test");
		this.test.setOutputMarkupId(true);

		if (getMusicModel().getObject().getAudio() != null) {

			String audioUrl = getPresignedUrl(getMusicModel().getObject().getAudio());

			Url url = Url.parse(audioUrl);
			UrlResourceReference resourceReference = new UrlResourceReference(url);

			AudioPlayer audio = new AudioPlayer("audioMusic", resourceReference);
			audio.setIncludeDownloadMenu(false);
			this.test.addOrReplace(audio);

			Label am = new Label("audioMeta", getAudioMeta(getMusicModel().getObject().getAudio()));
			am.setEscapeModelStrings(false);
			this.test.addOrReplace(am);
		} else {

			this.test.addOrReplace(new InvisiblePanel("audioMusic"));

			Label am = new Label("audioMeta", "");
			am.setEscapeModelStrings(false);
			this.test.addOrReplace(am);
			this.test.setVisible(false);
		}

		addOrReplace(this.test);

		this.test.setVisible(false);

	}

	public IModel<Music> getMusicModel() {
		return musicModel;
	}

	public void setMusicModel(IModel<Music> voiceModel) {
		this.musicModel = voiceModel;
	}

}
