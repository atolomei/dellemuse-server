package dellemuse.serverapp.audiostudio;

import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.media.audio.Audio;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.artexhibition.ArtExhibitionPage;
import dellemuse.serverapp.editor.SimpleAlertRow;
import dellemuse.serverapp.elevenlabs.ELVoice;
import dellemuse.serverapp.guidecontent.GuideContentEditor;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.site.SitePage;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.AudioStudio;
import dellemuse.serverapp.serverdb.model.Music;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Voice;
import io.wktui.error.AlertPanel;

import io.wktui.event.UIEvent;
import wktui.base.InvisiblePanel;

/**
 *
 * 
 */
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
		
		if (this.musicModel!=null)
			this.musicModel.detach();
	}


	@Override
	public void onInitialize() {
		super.onInitialize();
		
		Optional<AudioStudio> o_a = getAudioStudioDBService().findWithDeps(getModel().getObject().getId());
		setModel(new ObjectModel<>(o_a.get()));
		
		Optional<Music> o_c = getMusicDBService().findWithDeps(getMusicModel().getObject().getId());
		setMusicModel(new ObjectModel<Music>(o_c.get()));

		
		String in= this.musicModel.getObject().getInfo();
		if (in !=null)
			in=in.replace("\n", "<br/>");
		else
			in = "";
		
		Label info = new Label("info", in);

		info.setEscapeModelStrings(false);
		
		add(info);

		AjaxLink<Void> test = new AjaxLink<Void>("testlink") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				
				if (AudioStudioTestMusicPanel.this.test.isVisible()) 
					AudioStudioTestMusicPanel.this.test.setVisible( false );
				else
					AudioStudioTestMusicPanel.this.test.setVisible( true );
				
				target.add(AudioStudioTestMusicPanel.this);
			}
			
			public boolean isEnabled() {
				return AudioStudioTestMusicPanel.this.getMusicModel().getObject().getAudio()!=null;
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
			Audio audio = new Audio("audioMusic", resourceReference);
			this.test.addOrReplace(audio);

			Label am = new Label("audioMeta", getAudioMeta(getMusicModel().getObject().getAudio()));
			am.setEscapeModelStrings(false);
			this.test.addOrReplace(am);
		} else {

			Url url = Url.parse("");

			UrlResourceReference resourceReference = new UrlResourceReference(url);
			Audio audio = new Audio("audioVoice", resourceReference);
			this.test.addOrReplace(audio);

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
