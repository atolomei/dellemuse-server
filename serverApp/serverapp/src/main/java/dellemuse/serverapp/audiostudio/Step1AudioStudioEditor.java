package dellemuse.serverapp.audiostudio;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.media.audio.Audio;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.audit.AuditKey;
import dellemuse.serverapp.elevenlabs.LanguageCode;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.AudioStudio;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Resource;
import io.wktui.audio.AudioPlayer;
import io.wktui.error.AlertPanel;
import io.wktui.form.Form;
import io.wktui.form.button.SubmitButton;
import io.wktui.form.field.NumberField;
import wktui.base.InvisiblePanel;


/**
 * 
 * 
 * https://archive.org/download/LudwigVanBeethovenMoonlightSonataAdagioSostenutogetTune.net/Ludwig_Van_Beethoven_-_Moonlight_Sonata_Adagio_Sostenuto_%28get-tune.net%29.mp3");
 * 
 */
public class Step1AudioStudioEditor extends BaseAudioStudioEditor {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(Step1AudioStudioEditor.class.getName());

	private NumberField<Double> speedField;
	private NumberField<Double> styleField;
	private NumberField<Double> stabilityField;
	private NumberField<Double> similarityField;

	private Form<AudioStudio> form;

	private WebMarkupContainer step1;
	private WebMarkupContainer step1mp3;

	private Double similarity = 0.53;
	private Double stability = 0.82;
	private Double speed = 0.9;
	private Double audioStyle = 0.01;

	private boolean uploadedStep1 = false;

	public Step1AudioStudioEditor(String id, IModel<AudioStudio> model) {
		super(id, model);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		setup();
	}

	private void setup() {

		
		Map<String, String> map = getModel().getObject().getSettings();
		
		if (map!=null) {
			
			if (map.containsKey("speed")) 
				speed=Double.valueOf(map.get("speed"));
		
			if (map.containsKey("stability")) 
				stability =Double.valueOf(map.get("stability"));
		
			if (map.containsKey("similarity")) 
				similarity =Double.valueOf(map.get("similarity"));

			if (map.containsKey("audioStyle")) 
				audioStyle =Double.valueOf(map.get("audioStyle"));
		}
		
		step1 = new WebMarkupContainer("step1");

		step1.setOutputMarkupId(true);
		add(step1);
		
	
		form = new Form<AudioStudio>("form");
		setForm(form);

		step1.add(form);

		speedField = new NumberField<Double>("speed", new PropertyModel<Double>(this, "speed"), getLabel("speed"));
		similarityField = new NumberField<Double>("similarity", new PropertyModel<Double>(this, "similarity"), getLabel("similarity"));
		styleField = new NumberField<Double>("style", new PropertyModel<Double>(this, "audioStyle"), getLabel("style"));
		stabilityField = new NumberField<Double>("stability", new PropertyModel<Double>(this, "stability"), getLabel("stability"));

		form.add(speedField);
		form.add(similarityField);
		form.add(stabilityField);
		form.add(styleField);

		SubmitButton<AudioStudio> sm = new SubmitButton<AudioStudio>("generate", getModel(), getForm()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit(AjaxRequestTarget target) {

				getForm().updateModel();

				if (Step1AudioStudioEditor.this.getModel().getObject().getInfo() != null && Step1AudioStudioEditor.this.getModel().getObject().getInfo().length() == 0) {
					AlertPanel<Void> alert = new AlertPanel<Void>("error", AlertPanel.DANGER, getLabel("step1.no-text"));
					getForm().addOrReplace(alert);
				}

				else if (requiresGenerationAudioSpeech()) {
					step1AudioSpeech();
					addStep1MP3();
					AlertPanel<Void> alert = new AlertPanel<Void>("error", AlertPanel.SUCCESS, getLabel("audio-speech-generated-ok"));
					getForm().addOrReplace(alert);
					uploadedStep1 = true;

				} else {
					AlertPanel<Void> alert = new AlertPanel<Void>("error", AlertPanel.WARNING, getLabel("alredy-generated"));
					getForm().addOrReplace(alert);
					uploadedStep1 = true;
				}
				target.add(getForm());
			
			}

			@Override
			public boolean isEnabled() {

				if (getParentObjectState()== ObjectState.DELETED)
					return false;
			 
				 if (requiresGenerationAudioSpeech())
					 return true;
				 
				return false;
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
		
		AjaxLink<Void> nextStep2 = new AjaxLink<Void>("next") {
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
				fire(new AudioStudioAjaxEvent("step1.next", target));

			}
		};

		form.add(nextStep2);
		form.addOrReplace(new InvisiblePanel("error"));
		
		
		addInfo();
		
		addStep1MP3();
		edit();
	}

	
	private void addInfo() {
	
		if (getAudioSpeechModel() != null && getAudioSpeechModel().getObject() != null) {
			if (!requiresGenerationAudioSpeech() ) {
				AlertPanel<Void> alert = new AlertPanel<Void>("info", AlertPanel.INFO, getLabel("alredy-generated"));
				getForm().addOrReplace(alert);
				return;
			}
		}
		
		getForm().addOrReplace(new InvisiblePanel("info"));

		
	}
	

	private void step1AudioSpeech() {

		//if (getModel().getObject().getInfo() == null)
		//	return;

		//if (getModel().getObject().getInfo().length() == 0)
		//	return;

		
		getModel().getObject().setName(getParentName());
		getModel().getObject().setInfo(getParentInfo());
		

		getForm().updateModel();
		String text = getModel().getObject().getInfo();
		
		String language = getModel().getObject().getLanguage();
		String fileName = normalizeFileName(getParentName()) + "-" + getParentId().toString() + ".mp3";
		LanguageCode languageCode = LanguageCode.from(language);

		String dm_voice_id;

		if (languageCode.equals(LanguageCode.ES))
			dm_voice_id = "mariana";

		else if (languageCode.equals(LanguageCode.PT))
			dm_voice_id = "amanda";

		else if (languageCode.equals(LanguageCode.EN))
			dm_voice_id = "emily";

		else
			dm_voice_id = "emily";

		Optional<File> ofile = getElevenLabsService().generate(text, fileName, languageCode, dm_voice_id);

		if (ofile.isPresent()) {
			step1AudioSpeechUpload(ofile.get());
		}
	}

	private boolean step1AudioSpeechUpload(File file) {

		if (this.uploadedStep1)
			return false;

		try {

			String bucketName = ServerConstant.AUDIO_SPEECH_BUCKET;
			String objectName = getResourceDBService().normalizeFileName(FileNameUtils.getBaseName(file.getName())) + "-" + String.valueOf(getResourceDBService().newId());

			try (FileInputStream inputStream = new FileInputStream(file)) {

				Resource resource = createAndUploadFile(inputStream, bucketName, objectName, file.getName(), file.length());

				setAudioSpeechModel(new ObjectModel<Resource>(resource));

				getModel().getObject().setAudioSpeech(resource);
				getModel().getObject().setAudioSpeechHash(getHashAudioSpeech());
				
				logger.debug(("saving AudioStudio -> " + getModel().getObject().getName()));

				Map<String, String> map = getModel().getObject().getSettings();
				if (map == null)
					map = new HashMap<String, String>();

				if (getSpeed() != null)
					map.put("speed", getSpeed().toString());
				if (getStyle() != null)
					map.put("style", getStyle().toString());
				if (getStability() != null)
					map.put("stability", getStability().toString());
				if (getSimilarity() != null)
					map.put("similarity", getSimilarity().toString());

				getModel().getObject().setSettings(map);

				getAudioStudioDBService().save(getModel().getObject(), getSessionUser(), AuditKey.GENERATE_VOICE);
		
			}
			

			uploadedStep1 = true;
			logger.debug("saved Audio Studio ok -> " + getModel().getObject().toString());

		} catch (Exception e) {
			uploadedStep1 = false;
			error("Error saving file: " + e.getMessage());
		}
		return uploadedStep1;
	}

	private boolean requiresGenerationAudioSpeech() {

		if (getModel().getObject().getAudioSpeech() == null)
			return true;

		logger.debug(getHashAudioSpeech());
		logger.debug(getModel().getObject().getAudioSpeechHash());
		

		return (getHashAudioSpeech() != getModel().getObject().getAudioSpeechHash());
	}

	private int getHashAudioSpeech() {

		StringBuilder str = new StringBuilder();
		
		logger.debug(getModel().getObject().getInfo());
			
		if (getModel().getObject().getInfo() != null)
			str.append(getModel().getObject().getInfo().toLowerCase().trim());
		else
			str.append("");
		str.append("-");
		str.append(getSpeed().toString());
		str.append("-");
		str.append(getSimilarity().toString());
		str.append("-");
		str.append(getStability().toString());
		return str.toString().hashCode();
	}

	private void addStep1MP3() {

		this.step1mp3 = new WebMarkupContainer("step1MP3");

		if (getAudioSpeechModel() != null && getAudioSpeechModel().getObject() != null) {
			
			String audioUrl = getPresignedUrl(getAudioSpeechModel().getObject());
			
			
			Url url = Url.parse(audioUrl);
			UrlResourceReference resourceReference = new UrlResourceReference(url);
			Audio audio = new Audio("audioVoice", resourceReference);
			this.step1mp3.addOrReplace(audio);
			
			//AudioPlayer p=new AudioPlayer ("player", audioUrl);
			//this.step1mp3.addOrReplace(p);
					
			
			Label am = new Label("audioVoiceMetadata", getAudioMeta(getAudioSpeechModel().getObject()));
			am.setEscapeModelStrings(false);
			this.step1mp3.addOrReplace(am);
		} else {
			Url url = Url.parse("");

			UrlResourceReference resourceReference = new UrlResourceReference(url);
			 Audio audio = new Audio("audioVoice", resourceReference);
			this.step1mp3.addOrReplace(audio);

			//AudioPlayer p=new AudioPlayer ("player", "");
			//this.step1mp3.addOrReplace(p);
			
			Label am = new Label("audioVoiceMetadata", "");
			am.setEscapeModelStrings(false);
			this.step1mp3.addOrReplace(am);
			this.step1mp3.setVisible(false);
		}

		this.form.addOrReplace(this.step1mp3);

	}

	public Double getSimilarity() {
		return similarity;
	}

	public Double getStability() {
		return stability;
	}

	public Double getSpeed() {
		return speed;
	}

	public void setSimilarity(Double similarity) {
		this.similarity = similarity;
	}

	public void setStability(Double stability) {
		this.stability = stability;
	}

	public void setSpeed(Double speed) {
		this.speed = speed;
	}

	public Double getAudioStyle() {
		return audioStyle;
	}

	public void setAudioStyle(Double audioStyle) {
		this.audioStyle = audioStyle;
	}

}
