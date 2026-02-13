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
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Voice;
import io.wktui.error.AlertPanel;

import io.wktui.event.UIEvent;
import wktui.base.InvisiblePanel;

/**
 *
 * 
 */
public class AudioStudioTestVoicePanel extends DBModelPanel<AudioStudio> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(AudioStudioTestVoicePanel.class.getName());

	private IModel<Voice> voiceModel;
	private WebMarkupContainer test;

	/**
	 * @param id
	 * @param model
	 */
	public AudioStudioTestVoicePanel(String id, IModel<AudioStudio> model, IModel<Voice> voice) {
		super(id, model);

		this.voiceModel = voice;
		setOutputMarkupId(true);
	}
	
	public void onDetach() {
		super.onDetach();
		
		if (this.voiceModel!=null)
			this.voiceModel.detach();
	}


	@Override
	public void onInitialize() {
		super.onInitialize();

		
		Optional<AudioStudio> o_a = getAudioStudioDBService().findWithDeps(getModel().getObject().getId());
		setModel(new ObjectModel<>(o_a.get()));
		
		Optional<Voice> o_c = getVoiceDBService().findWithDeps(getVoiceModel().getObject().getId());
		setVoiceModel(new ObjectModel<Voice>(o_c.get()));
		
		
		//Label name = new Label("name", this.voice.getName());
		//add(name);

		//Label lang = new Label("language", this.voice.getLanguage() + " (" + this.voice.getLanguageRegion() + ")");
		//add(lang);

		Label info = new Label("info", this.voiceModel.getObject().getInfo());
		add(info);

		AjaxLink<Void> test = new AjaxLink<Void>("testlink") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				
				if (AudioStudioTestVoicePanel.this.test.isVisible()) 
					AudioStudioTestVoicePanel.this.test.setVisible( false );
				else
					AudioStudioTestVoicePanel.this.test.setVisible( true );
				
				target.add(AudioStudioTestVoicePanel.this);
			}
		};

		
		add(test);
		addTest(); 

		
	}

	private void addTest() {

		this.test = new WebMarkupContainer("test");
		this.test.setOutputMarkupId(true);

		if (getVoiceModel().getObject().getAudio() != null) {

			// poner Voice en vez de AudioStudio
			String audioUrl = getPresignedUrl(getVoiceModel().getObject().getAudio());

			Url url = Url.parse(audioUrl);
			UrlResourceReference resourceReference = new UrlResourceReference(url);
			Audio audio = new Audio("audioVoice", resourceReference);
			this.test.addOrReplace(audio);

			Label am = new Label("audioMeta", getAudioMeta(getVoiceModel().getObject().getAudio()));
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

	public IModel<Voice> getVoiceModel() {
		return voiceModel;
	}

	public void setVoiceModel(IModel<Voice> voiceModel) {
		this.voiceModel = voiceModel;
	}

}
