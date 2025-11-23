package dellemuse.serverapp.audiostudio;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.media.audio.Audio;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.AudioStudio;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Resource;
import io.wktui.error.AlertPanel;
import io.wktui.form.Form;
import io.wktui.form.button.SubmitButton;
import io.wktui.form.field.NumberField;
import io.wktui.form.field.TextAreaField;
import wktui.base.InvisiblePanel;

public class Step2AudioStudioEditor extends BaseAudioStudioEditor {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(Step2AudioStudioEditor.class.getName());

	private TextAreaField<String> musicUrlField;
	private NumberField<Integer> introDurationSecField;
	private NumberField<Integer> fadeDurationSecField;
	private NumberField<Integer> voiceOverlapDurationSecField;

	private Form<AudioStudio> form;

	private WebMarkupContainer step2;
	private WebMarkupContainer step2mp3;

	private AjaxLink<Void> generate;

	private Integer introDurationSec = Integer.valueOf(20);
	private Integer fadeDurationSec = Integer.valueOf(12);
	private Integer voiceOverlapDurationSec = Integer.valueOf(5);

	boolean uploadedStep2 = false;

	public Step2AudioStudioEditor(String id, IModel<AudioStudio> model) {
		super(id, model);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		setup();
	}

	public Integer getIntroDurationSec() {
		return introDurationSec;
	}

	public Integer getFadeDurationSec() {
		return fadeDurationSec;
	}

	public Integer getVoiceOverlapDurationSec() {
		return voiceOverlapDurationSec;
	}

	public void setIntroDurationSec(Integer introDurationSec) {
		this.introDurationSec = introDurationSec;
	}

	public void setFadeDurationSec(Integer fadeDurationSec) {
		this.fadeDurationSec = fadeDurationSec;
	}

	public void setVoiceOverlapDurationSec(Integer voiceOverlapDurationSec) {
		this.voiceOverlapDurationSec = voiceOverlapDurationSec;
	}

	
	public void onBeforeRender() {
		super.onBeforeRender();
		
		form.addOrReplace(new InvisiblePanel("error"));
		
	}
	private void setup() {

		step2 = new WebMarkupContainer("step2");
		step2.setOutputMarkupId(true);

		add(step2);

		form = new Form<AudioStudio>("form");
		setForm(form);

		step2.add(form);

		musicUrlField = new TextAreaField<String>("musicUrl", new PropertyModel<String>(getModel(), "musicUrl"), getLabel("musicUrl"), 4);
		introDurationSecField = new NumberField<Integer>("introDurationSec", new PropertyModel<Integer>(this, "introDurationSec"), getLabel("introDurationSec"));
		fadeDurationSecField = new NumberField<Integer>("fadeDurationSec", new PropertyModel<Integer>(this, "fadeDurationSec"), getLabel("fadeDurationSec"));
		voiceOverlapDurationSecField = new NumberField<Integer>("voiceOverlapDurationSec", new PropertyModel<Integer>(this, "voiceOverlapDurationSec"), getLabel("voiceOverlapDurationSec"));

		form.add(musicUrlField);
		form.add(introDurationSecField);
		form.add(fadeDurationSecField);
		form.add(voiceOverlapDurationSecField);

		
		SubmitButton<AudioStudio> sm = new SubmitButton<AudioStudio>("generate", getModel(), getForm()) {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled() {

				if (getParentObjectState()== ObjectState.DELETED)
					return false;
				
				return true;
			}
			
			@Override
			public void onSubmit(AjaxRequestTarget target) {

				getForm().updateModel();

				//Step2AudioStudioEditor.this.getModel().getObject()
				//		.setMusicUrl("https://archive.org/download/LudwigVanBeethovenMoonlightSonataAdagioSostenutogetTune.net/Ludwig_Van_Beethoven_-_Moonlight_Sonata_Adagio_Sostenuto_%28get-tune.net%29.mp3");

				String musicUrl = Step2AudioStudioEditor.this.getModel().getObject().getMusicUrl();
					
				if (musicUrl==null) {
					AlertPanel<Void> alert = new AlertPanel<Void>("error", AlertPanel.DANGER, Model.of("Music URL is empty"));
					getForm().addOrReplace(alert);
					target.add(getForm());
					return;
				}
				
				if (Step2AudioStudioEditor.this.getModel().getObject().getAudioSpeech() != null) {
					
					Long voiceResourceId = Step2AudioStudioEditor.this.getModel().getObject().getAudioSpeech().getId();
					// musicUrl = "https://archive.org/download/LudwigVanBeethovenMoonlightSonataAdagioSostenutogetTune.net/Ludwig_Van_Beethoven_-_Moonlight_Sonata_Adagio_Sostenuto_%28get-tune.net%29.mp3";
					Integer introDurationSec = Step2AudioStudioEditor.this.getIntroDurationSec();
					Integer fadeDurationSec = Step2AudioStudioEditor.this.getFadeDurationSec();
					Integer voiceOverlapDurationSec = Step2AudioStudioEditor.this.getVoiceOverlapDurationSec();

					IntegrateMusicCommand c = new IntegrateMusicCommand(voiceResourceId, musicUrl, introDurationSec, fadeDurationSec, voiceOverlapDurationSec);
					c.execute();

					if (c.isSuccess()) {
						// save resource music + file
						File file = new File(c.getoutputFilePath());
						step2Upload(file);
						addStep2MP3();
						// save(AudioStudioEditor.this.getModel().getObject());
					} else {

						String err = c.getErrorMsg();
						logger.error(err);

						AlertPanel<Void> alert = new AlertPanel<Void>("error", AlertPanel.WARNING, Model.of(err));
						getForm().addOrReplace(alert);
						
					}
					target.add(getForm());

				}
			
			}
 
			
			public IModel<String> getLabel() {
				return  getLabel("generate-audio");
			}

			@Override
			public String getRowCss() {
				return "d-inline-block float-left";
			}

			@Override
			public String getColCss() {
				return "d-inline-block float-left";
			}

			@Override
			public String getSaveCss() {
				return "btn btn-primary btn-md";
			}
		};
		
		form.add(sm);
		
		
		
		
		
		
		
		
		
		
		
		
		/**
		
		
		
		
		
		
		
		
		this.generate = new AjaxLink<Void>("generate") {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled() {
				return true;
			}

			@Override
			public void onClick(AjaxRequestTarget target) {

				getForm().updateModel();

				//Step2AudioStudioEditor.this.getModel().getObject()
				//		.setMusicUrl("https://archive.org/download/LudwigVanBeethovenMoonlightSonataAdagioSostenutogetTune.net/Ludwig_Van_Beethoven_-_Moonlight_Sonata_Adagio_Sostenuto_%28get-tune.net%29.mp3");

				String musicUrl = Step2AudioStudioEditor.this.getModel().getObject().getMusicUrl();
					
				if (musicUrl==null) {
					AlertPanel<Void> alert = new AlertPanel<Void>("error", AlertPanel.DANGER, Model.of("Music URL is empty"));
					getForm().addOrReplace(alert);
					target.add(getForm());
					return;
				}
				
				if (Step2AudioStudioEditor.this.getModel().getObject().getAudioSpeech() != null) {
					
					Long voiceResourceId = Step2AudioStudioEditor.this.getModel().getObject().getAudioSpeech().getId();
					// musicUrl = "https://archive.org/download/LudwigVanBeethovenMoonlightSonataAdagioSostenutogetTune.net/Ludwig_Van_Beethoven_-_Moonlight_Sonata_Adagio_Sostenuto_%28get-tune.net%29.mp3";
					Integer introDurationSec = Step2AudioStudioEditor.this.getIntroDurationSec();
					Integer fadeDurationSec = Step2AudioStudioEditor.this.getFadeDurationSec();
					Integer voiceOverlapDurationSec = Step2AudioStudioEditor.this.getVoiceOverlapDurationSec();

					IntegrateMusicCommand c = new IntegrateMusicCommand(voiceResourceId, musicUrl, introDurationSec, fadeDurationSec, voiceOverlapDurationSec);
					c.execute();

					if (c.isSuccess()) {
						// save resource music + file
						File file = new File(c.getoutputFilePath());
						step2Upload(file);
						addStep2MP3();
						// save(AudioStudioEditor.this.getModel().getObject());
					} else {

						String err = c.getErrorMsg();
						logger.error(err);

						AlertPanel<Void> alert = new AlertPanel<Void>("error", AlertPanel.WARNING, Model.of(err));
						getForm().addOrReplace(alert);
						
					}
					target.add(getForm());

				}
			}
		};

		form.add(generate);

*/
		
		AjaxLink<Void> next3 = new AjaxLink<Void>("next3") {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled() {

				if (getAudioSpeechModel() == null)
					return false;

				if (getAudioSpeechModel().getObject() != null)
					return true;

				return false;
			}

			@Override
			public void onClick(AjaxRequestTarget target) {
				fire(new AudioStudioAjaxEvent("step2.next", target));
			}
		};

		AjaxLink<Void> prev1 = new AjaxLink<Void>("prev1") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				fire(new AudioStudioAjaxEvent("step2.prev", target));
			}
		};

		form.add(prev1);
		form.add(next3);
		
		form.addOrReplace(new InvisiblePanel("error"));
		form.addOrReplace(new InvisiblePanel("info"));

		addStep2MP3();
		edit();
	}

	/**
	 * 
	 * 
	 */
	private void addStep2MP3() {

		this.step2mp3 = new WebMarkupContainer("step2MP3");

		if (getAudioSpeechMusicModel() != null && getAudioSpeechMusicModel().getObject() != null) {
			String audioUrl = getPresignedUrl(getAudioSpeechMusicModel().getObject());
			Url url = Url.parse(audioUrl);
			UrlResourceReference resourceReference = new UrlResourceReference(url);
			Audio audio = new Audio("audioVoiceMusic", resourceReference);

			this.step2mp3.addOrReplace(audio);

			Label am = new Label("audioVoiceMusicMetadata", getAudioMeta(getAudioSpeechMusicModel().getObject()));
			am.setEscapeModelStrings(false);
			this.step2mp3.addOrReplace(am);
		} else {

			Url url = Url.parse("");
			UrlResourceReference resourceReference = new UrlResourceReference(url);
			Audio audio = new Audio("audioVoiceMusic", resourceReference);
			this.step2mp3.addOrReplace(audio);
			Label am = new Label("audioVoiceMusicMetadata", "");
			am.setEscapeModelStrings(false);
			this.step2mp3.addOrReplace(am);
			this.step2mp3.setVisible(false);
		}
		getForm().addOrReplace(this.step2mp3);
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	private boolean step2Upload(File file) {

		if (this.uploadedStep2)
			return false;

		try {

			String bucketName = ServerConstant.AUDIO_SPEECHMUSIC_BUCKET;
			String objectName = getResourceDBService().normalizeFileName(FileNameUtils.getBaseName(file.getName())) + "-" + String.valueOf(getResourceDBService().newId());

			try (FileInputStream inputStream = new FileInputStream(file)) {

				Resource resource = createAndUploadFile(inputStream, bucketName, objectName, file.getName(), file.length());
				setAudioSpeechMusicModel(new ObjectModel<Resource>(resource));

				getModel().getObject().setAudioSpeechMusicHash(getHashAudioSpeechMusic());
				getModel().getObject().setAudioSpeechMusic(resource);

				Map<String, String> map = getModel().getObject().getSettings();

				if (map == null)
					map = new HashMap<String, String>();

				if (getIntroDurationSec() != null)
					map.put("introDurationSec", getIntroDurationSec().toString());

				if (getFadeDurationSec() != null)
					map.put("fadeDurationSec", getFadeDurationSec().toString());

				if (getVoiceOverlapDurationSec() != null)
					map.put("voiceOverlapDurationSec", getVoiceOverlapDurationSec().toString());

				getModel().getObject().setSettings(map);

				getAudioStudioDBService().save(getModel().getObject());

			}

			uploadedStep2 = true;

		} catch (Exception e) {
			uploadedStep2 = false;
			error("Error saving file " + e.getMessage());
		}

		return uploadedStep2;
	}

	/**
	 * 
	 * @return
	 */
	private int getHashAudioSpeechMusic() {

		StringBuilder str = new StringBuilder();
		str.append(getModel().getObject().getMusicUrl());
		str.append("-");
		str.append(this.getIntroDurationSec().toString());
		str.append("-");
		str.append(this.getFadeDurationSec().toString());
		str.append("-");
		str.append(this.getVoiceOverlapDurationSec().toString());
		return str.toString().hashCode();
	}

}
