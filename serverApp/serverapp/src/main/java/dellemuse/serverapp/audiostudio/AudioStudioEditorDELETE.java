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
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.media.audio.Audio;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.editor.DBObjectEditor;
import dellemuse.serverapp.editor.ObjectUpdateEvent;
import dellemuse.serverapp.elevenlabs.LanguageCode;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.AudioStudio;
import dellemuse.serverapp.serverdb.model.Resource;
import io.wktui.error.AlertPanel;
import io.wktui.form.Form;
import io.wktui.form.button.SubmitButton;

import io.wktui.form.field.NumberField;
import io.wktui.form.field.StaticTextField;
import io.wktui.form.field.TextAreaField;
import wktui.base.InvisiblePanel;

/**
 *
 * 
 */
public class AudioStudioEditorDELETE extends DBObjectEditor<AudioStudio> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(AudioStudioEditorDELETE.class.getName());

	 
	private IModel<AudioStudioParentObject> parentObjectModel;

	private StaticTextField<String> nameField;
	private StaticTextField<String> typeField;
	private StaticTextField<String> languageField;

	private TextAreaField<String> infoField;

	private NumberField<Double> speedField;
	private NumberField<Double> styleField;
	private NumberField<Double> stabilityField;
	private NumberField<Double> similarityField;

	private TextAreaField<String> musicUrlField;
	private NumberField<Integer> introDurationSecField;
	private NumberField<Integer> fadeDurationSecField;
	private NumberField<Integer> voiceOverlapDurationSecField;

	private IModel<Resource> parentAudioModel;

	private IModel<Resource> audioSpeechModel;
	private IModel<Resource> audioSpeechMusicModel;

	private boolean uploadedStep1 = false;
	private boolean uploadedStep2 = false;
	private boolean infoChanged = true;

	private AjaxLink<Void> step1Generate;
	private AjaxLink<Void> step2Generate;

	private WebMarkupContainer step1mp3;
	private WebMarkupContainer step2mp3;
	private WebMarkupContainer step3mp3;

	
	private WebMarkupContainer step1;
	private WebMarkupContainer step2;
	private WebMarkupContainer step3;

	static final int AUDIO_VOICE = 0;
	static final int AUDIO_VOICE_MUSIC = 1;
	static final int INTEGRATE = 2;

	private int status = AUDIO_VOICE;
	private String parentObjectUrl;

	private Double similarity = 1.0;
	private Double stability = 1.0;
	private Double speed = 1.0;
	private Double audioStyle = 1.0;

	private Integer introDurationSec = Integer.valueOf(20);
	private Integer fadeDurationSec = Integer.valueOf(12);
	private Integer voiceOverlapDurationSec = Integer.valueOf(5);	
	
	private String parentName;
	private String parentInfo;
	private IModel<String> parentType;
	private Long parentId;

	private PillPanel pill_1;
	private PillPanel pill_2;
	private PillPanel pill_3;

	private Form<AudioStudio> form;

	boolean parent_audio_exists			= false;
	boolean parent_audio_same_as_studio = false;
	

	/**
	 * @param id
	 * @param model
	 */
	public AudioStudioEditorDELETE(String id, IModel<AudioStudio> model, String parentObjejctUrl) {
		super(id, model);
		this.parentObjectUrl = parentObjejctUrl;
	}

	/** 
	 * 
	 */
	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();

	 
		generalInit();

		step1();
		step2();
		step3();

		setStatus(AUDIO_VOICE);
	}
	
	
	public void onEdit(AjaxRequestTarget target) {
		super.edit(target);
		// getForm().setFormState(FormState.EDIT);
		// target.add(getForm());
	}

	


	protected boolean requiresGenerationAudioSpeech() {

		if (getModel().getObject().getAudioSpeech() == null)
			return true;

		logger.debug(getHashAudioSpeech());
		logger.debug(getModel().getObject().getAudioSpeechHash());

		return getHashAudioSpeech() != getModel().getObject().getAudioSpeechHash();
	}

	


	/**
	 * 
	 * 
	 * 
	 */
	private void step2() {

		this.step2 = new WebMarkupContainer("step2") {
			private static final long serialVersionUID = 1L;

			public boolean isVisible() {
				return getStatus() == AUDIO_VOICE_MUSIC;
			}
		};

		AjaxLink<Void> prev = new AjaxLink<Void>("prev1") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				setStatus(AUDIO_VOICE);
				target.add(getForm());
			}
		};

		AjaxLink<Void> next = new AjaxLink<Void>("next3") {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled() {

				if (uploadedStep1 == true)
					return true;

				if (AudioStudioEditorDELETE.this.getAudioSpeechModel()!=null && AudioStudioEditorDELETE.this.getAudioSpeechModel().getObject() != null)
					return true;

				if (uploadedStep2 == true)
					return true;

				if (AudioStudioEditorDELETE.this.getAudioSpeechMusicModel()!=null && AudioStudioEditorDELETE.this.getAudioSpeechMusicModel().getObject() != null)
					return true;

				return false;
			}

			@Override
			public void onClick(AjaxRequestTarget target) {
				setStatus(INTEGRATE);
				target.add(getForm());
			}
		};

		this.step2.add(prev);
		this.step2.add(next);

		musicUrlField 					= new TextAreaField<String>("musicUrl", 			new PropertyModel<String>	(getModel(), "musicUrl"), getLabel("musicUrl"), 4);
		introDurationSecField 			= new NumberField<Integer>("introDurationSec", 	new PropertyModel<Integer>	(this, "introDurationSec"), getLabel("introDurationSec"));
		fadeDurationSecField 			= new NumberField<Integer>("fadeDurationSec", 		new PropertyModel<Integer>	(this, "fadeDurationSec"), getLabel("fadeDurationSec"));
		voiceOverlapDurationSecField 	= new NumberField<Integer>("voiceOverlapDurationSec", new PropertyModel<Integer>	(this, "voiceOverlapDurationSec"), getLabel("voiceOverlapDurationSec"));

		step2.add(musicUrlField);
		step2.add(introDurationSecField);
		step2.add(fadeDurationSecField);
		step2.add(voiceOverlapDurationSecField);
		
	 	
		
		AjaxLink<Void> gen = new AjaxLink<Void>("step2Generate") {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled() {
				return true;
			}

			@Override
			public void onClick(AjaxRequestTarget target) {

				getForm().updateModel();
				
				AudioStudioEditorDELETE.this.getModel().getObject().setMusicUrl(
						"https://archive.org/download/LudwigVanBeethovenMoonlightSonataAdagioSostenutogetTune.net/Ludwig_Van_Beethoven_-_Moonlight_Sonata_Adagio_Sostenuto_%28get-tune.net%29.mp3");
				
				if (AudioStudioEditorDELETE.this.getModel().getObject().getAudioSpeech()!=null) {
					Long voiceResourceId = AudioStudioEditorDELETE.this.getModel().getObject().getAudioSpeech().getId();
					String musicUrl 	 = AudioStudioEditorDELETE.this.getModel().getObject().getMusicUrl();
					
					musicUrl= "https://archive.org/download/LudwigVanBeethovenMoonlightSonataAdagioSostenutogetTune.net/Ludwig_Van_Beethoven_-_Moonlight_Sonata_Adagio_Sostenuto_%28get-tune.net%29.mp3";
	
					Integer introDurationSec=AudioStudioEditorDELETE.this.getIntroDurationSec();
					Integer fadeDurationSec=AudioStudioEditorDELETE.this.getFadeDurationSec();
					Integer voiceOverlapDurationSec=AudioStudioEditorDELETE.this.getVoiceOverlapDurationSec();
	
					IntegrateMusicCommand c = new IntegrateMusicCommand( voiceResourceId, musicUrl,  introDurationSec, fadeDurationSec, voiceOverlapDurationSec);
	
					c.execute();
					
					if (c.isSuccess()) {
						
						// save resource music + file
						
						File file = new File(c.getoutputFilePath());
						step2Upload(file);
						addStep2MP3();
					
						//save(AudioStudioEditor.this.getModel().getObject());
						
					}
					else  {
						
						String err=c.getErrorMsg();
						logger.error(err);
					
					}
				}
				
				target.add(getForm());
			}
		};

		step2.add(gen);
		step2.setOutputMarkupId(true);
		
		form.add(step2);

		addStep2MP3();
		step2.addOrReplace(new InvisiblePanel("step2Error"));
	}

	
 
	/**
	 * 
	 * 
	 */
	private void step3() {

		step3 = new WebMarkupContainer("step3") {
			private static final long serialVersionUID = 1L;

			public boolean isVisible() {
				return getStatus() == INTEGRATE;
			}
		};

		AjaxLink<Void> prev = new AjaxLink<Void>("prev2") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				setStatus(AUDIO_VOICE_MUSIC);
				target.add(getForm());
			}
		};
		
		step3.setOutputMarkupId(true);
		
		step3.add(prev);

		SubmitButton<AudioStudio> sm = new SubmitButton<AudioStudio>("integrate", getModel(), getForm()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit(AjaxRequestTarget target) {
				AudioStudioEditorDELETE.this.onSave(target);
			}

			@Override
			public boolean isEnabled() {

				if ((parentAudioModel!=null) &&
					(getFinalAudioModel()!=null) &&
					(getFinalAudioModel().getObject().getId().equals(parentAudioModel.getObject().getId())) )
					
					return false;
				
				if (uploadedStep1 || uploadedStep2)
					return true;

				if (AudioStudioEditorDELETE.this.getModel().getObject().getAudioSpeech() != null) {
					return true;
				}

				if (AudioStudioEditorDELETE.this.getModel().getObject().getAudioSpeechMusic() != null)
					return true;

				return false;
			}
			
			public IModel<String> getLabel() {
				return AudioStudioEditorDELETE.this.getLabel("integrate", parentName);
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

		step3.add(sm);

		/**
		 * 
		 * 1. P noE 
		 * 1.1 Studio noE  -> boton desactivado
		 *     Studio E    -> boton activado

		 * 2. P E
		 * 2.1 Studio noE  -> boton desactivado
		 * 2.2 Studio E    -> 
		 * 	2.2.1 P=Studio -> alert son iguales	
 *          2.2.2 P!=Studio -> alert atencion que se reemplazará
		 * 
		 * 
		 * 
		**/
		
		AlertPanel<Void> alert_1 = new AlertPanel<Void>("status", 
												AlertPanel.WARNING) {

			private static final long serialVersionUID = 1L;

			public boolean isVisible() {
			
				if (parentAudioModel==null)
					return false;
				
				if (getFinalAudioModel()==null)
					return false;
				
				if (getFinalAudioModel().getObject().getId().equals(parentAudioModel.getObject().getId()))
					return true;
			
				return false;
			}
			
			public IModel<String> getText() {
				
				if (parentAudioModel==null)
					return Model.of("");
				
				if (getFinalAudioModel()==null)
					return Model.of("");
				
				 if (getFinalAudioModel().getObject().getId().equals(parentAudioModel.getObject().getId()))
					 return getLabel("audio-are-the-same");
				 
				 return getLabel("studio-will-replace-existing");
			}
		};
	
		step3.addOrReplace(alert_1);
		
		addStep3MP3();
		
		step3.setOutputMarkupId(true);
		form.add(step3);
	}

	/**
	 * 
	 * 
	 */
	@SuppressWarnings("serial")
	private void step1() {

		step1 = new WebMarkupContainer("step1") {
			private static final long serialVersionUID = 1L;

			public boolean isVisible() {
				return getStatus() == AUDIO_VOICE;
			}
		};

		step1.setOutputMarkupId(true);
		form.add(step1);

		speedField = new NumberField<Double>("speed", new PropertyModel<Double>(this, "speed"), getLabel("speed")) {
			public boolean isEnabled() {
				return getStatus() == AUDIO_VOICE;
			}
		};

		similarityField = new NumberField<Double>("similarity", new PropertyModel<Double>(this, "similarity"), getLabel("similarity")) {
			public boolean isEnabled() {
				return super.isEnabled() && getStatus() == AUDIO_VOICE;
			}
		};

		styleField = new NumberField<Double>("style", new PropertyModel<Double>(this, "audioStyle"), getLabel("style")) {
			public boolean isEnabled() {
				return super.isEnabled() && getStatus() == AUDIO_VOICE;
			}
		};

		stabilityField = new NumberField<Double>("stability", new PropertyModel<Double>(this, "stability"), getLabel("stability")) {
			public boolean isEnabled() {
				return super.isEnabled() && getStatus() == AUDIO_VOICE;
			}
		};

		step1.add(speedField);
		step1.add(similarityField);
		step1.add(stabilityField);
		step1.add(styleField);

		this.step1Generate = new AjaxLink<Void>("generateAudioSpeech") {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled() {
				return true;
			}

			@Override
			public void onClick(AjaxRequestTarget target) {

				getForm().updateModel();

				if (AudioStudioEditorDELETE.this.getModel().getObject().getInfo()!=null &&
					AudioStudioEditorDELETE.this.getModel().getObject().getInfo().length()==0)
				{
					AlertPanel<Void> alert = new AlertPanel<Void>("step1Error", AlertPanel.WARNING, getLabel("step1.no-text"));
					step1.addOrReplace(alert);
				}
				
				//else if (step1!=null) {
				else if (requiresGenerationAudioSpeech()) {
					
					step1AudioSpeech();
					addStep1MP3();
					
					AlertPanel<Void> alert = new AlertPanel<Void>("step1Error", AlertPanel.SUCCESS, getLabel("audio-speech-generated-ok"));
					step1.addOrReplace(alert);
					
				} else {
					AlertPanel<Void> alert = new AlertPanel<Void>("step1Error", AlertPanel.WARNING, getLabel("alredy-generated"));
					step1.addOrReplace(alert);
				}
				uploadedStep1 = true;
				target.add(getForm());
			}
		};

		
		step1.add(step1Generate);

		AjaxLink<Void> nextAddMusic = new AjaxLink<Void>("next2") {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled() {

				if (AudioStudioEditorDELETE.this.getAudioSpeechModel()==null)
					return false;
				
				if (AudioStudioEditorDELETE.this.getAudioSpeechModel().getObject() != null)
					return true;

				return false;
			}

			@Override
			public void onClick(AjaxRequestTarget target) {
				setStatus(AUDIO_VOICE_MUSIC);
				target.add(getForm());
			}
		};

		step1.add(nextAddMusic);
		step1.addOrReplace(new InvisiblePanel("step1Error"));

		addStep1MP3();

	}
	
	
	/**
	 * 
	 * 
	 */
	private void addStep1MP3() {

		this.step1mp3 = new WebMarkupContainer("step1MP3");

		if (getAudioSpeechModel() != null && getAudioSpeechModel().getObject() != null) {
			String audioUrl = getPresignedUrl(getAudioSpeechModel().getObject());
			Url url = Url.parse(audioUrl);
			UrlResourceReference resourceReference = new UrlResourceReference(url);
			Audio audio = new Audio("audioVoice", resourceReference);

			this.step1mp3.addOrReplace(audio);

			Label am = new Label("audioVoiceMetadata", getAudioMeta(getAudioSpeechModel().getObject()));
			am.setEscapeModelStrings(false);
			this.step1mp3.addOrReplace(am);
		} else {

			Url url = Url.parse("");
			UrlResourceReference resourceReference = new UrlResourceReference(url);
			Audio audio = new Audio("audioVoice", resourceReference);
			this.step1mp3.addOrReplace(audio);
			Label am = new Label("audioVoiceMetadata", "");
			am.setEscapeModelStrings(false);
			this.step1mp3.addOrReplace(am);
			this.step1mp3.setVisible(false);
		}
		this.step1.addOrReplace(this.step1mp3);
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
		this.step2.addOrReplace(this.step2mp3);
	}

	
	
	/**
	 * 
	 * 
	 */
	private void addStep3MP3() {

		this.step3mp3 = new WebMarkupContainer("step3MP3");

		if (getAudioSpeechModel() != null && getAudioSpeechModel().getObject() != null) {
			String audioUrl = getPresignedUrl(getAudioSpeechModel().getObject());
			Url url = Url.parse(audioUrl);
			UrlResourceReference resourceReference = new UrlResourceReference(url);
			Audio audio = new Audio("audioVoice", resourceReference);

			this.step3mp3.addOrReplace(audio);

			Label am = new Label("audioVoiceMetadata", getAudioMeta(getAudioSpeechModel().getObject()));
			am.setEscapeModelStrings(false);
			this.step3mp3.addOrReplace(am);
		} else {

			Url url = Url.parse("");
			UrlResourceReference resourceReference = new UrlResourceReference(url);
			Audio audio = new Audio("audioVoice", resourceReference);
			this.step3mp3.addOrReplace(audio);
			Label am = new Label("audioVoiceMetadata", "");
			am.setEscapeModelStrings(false);
			this.step3mp3.addOrReplace(am);
			this.step3mp3.setVisible(false);
		}
		this.step3.addOrReplace(this.step3mp3);
	}
	
	
	
	
	
	
	
	
	protected void step1AudioSpeech() {

		if (getModel().getObject().getInfo() == null)
			return;

		if (getModel().getObject().getInfo().length() == 0)
			return;

		getForm().updateModel();
		String text = getModel().getObject().getInfo();
		String language = getModel().getObject().getLanguage();
		String fileName = normalizeFileName(parentName) + "-" + parentId.toString() + ".mp3";
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
			logger.debug(language);
			logger.debug(fileName);
		}
	}

	protected void onCancel(AjaxRequestTarget target) {
		super.cancel(target);
		// getForm().setFormState(FormState.VIEW);
		// target.add(getForm());
	}

	
	

	protected void onSave(AjaxRequestTarget target) {

		logger.debug("onSave");
		logger.debug("updated parts:");
		getUpdatedParts().forEach(s -> logger.debug(s));
		logger.debug("saving...");

		save(getModelObject());
		
		boolean saveRequired = false;
		
		AudioStudioParentObject po = getAudioStudioDBService().findParentObjectWithDeps(getModelObject()).get();
		
		if (getModelObject().getInfo()!=null) {
			
			if (po.getInfo()==null)
				this.infoChanged=true;
			else {	
				this.infoChanged=getModelObject().getInfo().trim().toLowerCase().hashCode()!=po.getInfo().trim().toLowerCase().hashCode();
			}
		}
		
		if (infoChanged) {
			saveRequired =true;
			po.setInfo(getModelObject().getInfo());
		
		}
		
		if (getModelObject().getAudioSpeechMusic()!=null) {
			saveRequired=true;
			po.setAudio(getModelObject().getAudioSpeechMusic());
		}
		else if (getModelObject().getAudioSpeech()!=null)
			saveRequired=true;
			po.setAudio(getModelObject().getAudioSpeech());

	
		 
		
		this.infoChanged   = false;
		this.uploadedStep1 = false;
		this.uploadedStep2 = false;
		
		getForm().updateReload();

		fire(new ObjectUpdateEvent(target));
		target.add(this);
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (audioSpeechModel != null)
			audioSpeechModel.detach();

		if (audioSpeechMusicModel != null)
			audioSpeechMusicModel.detach();
		
		if (parentAudioModel!=null)
			parentAudioModel.detach();
		
	}

	 
	public IModel<Resource> getAudioSpeechMusicModel() {
		return audioSpeechMusicModel;
	}

	public void setAudioSpeechModel(IModel<Resource> audioSpeechModel) {
		this.audioSpeechModel = audioSpeechModel;
	}

	public void setAudioSpeechMusicModel(IModel<Resource> audioSpeechMusicModel) {
		this.audioSpeechMusicModel = audioSpeechMusicModel;
	}

	public Double getSimilarity() {
		return similarity;
	}

	public Double getStability() {
		return stability;
	}

	public void setSimilarity(Double similarity) {
		this.similarity = similarity;
	}

	public void setStability(Double stability) {
		this.stability = stability;
	}

	public Double getAudioStyle() {
		return audioStyle;
	}

	public void setAudioStyle(Double style) {
		this.audioStyle = style;
	}

	public Double getSpeed() {
		return this.speed;
	}

	public void setSpeed(Double speed) {
		this.speed = speed;
	}
	 
	
	
	
	

	protected IModel<Resource> getFinalAudioModel() {

		 if (this.audioSpeechMusicModel!=null)
			 return  this.audioSpeechMusicModel;
		 
		if (this.audioSpeechModel!=null)
			return this.audioSpeechModel;
		
		return null;
	}

	
	protected IModel<Resource> getAudioSpeechModel() {
		return this.audioSpeechModel;
	}

	protected void setAudioSpeechModel(ObjectModel<Resource> model) {
		this.audioSpeechModel = model;
	}

	protected IModel<Resource> getAudiSpeechMusicModel() {
		return this.audioSpeechMusicModel;
	}

	protected void setAudioSpeechMusicModel(ObjectModel<Resource> model) {
		this.audioSpeechMusicModel = model;
	}

	protected void onSubmit() {
		logger.debug("");
		logger.debug("onSubmit");
		logger.debug("");
	}

	protected boolean step1AudioSpeechUpload(File file) {

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
				if (map==null)
					map=new HashMap<String, String>();

				if (getSpeed() != null)
					map.put("speed", getSpeed().toString());
				if (getStyle() != null)
					map.put("style", getStyle().toString());
				if (getStability() != null)
					map.put("stability", getStability().toString());
				if (getSimilarity() != null)
					map.put("similarity", getSimilarity().toString());
 
				getModel().getObject().setSettings(map);

				getAudioStudioDBService().save(getModel().getObject());

			}

			uploadedStep1 = true;

			logger.debug("saved Audio Studio ok -> " + getModel().getObject().toString());

		} catch (Exception e) {
			uploadedStep1 = false;
			error("Error saving file: " + e.getMessage());
		}

		return uploadedStep1;
	}

	protected boolean step2Upload(File file) {

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
				
				if (map==null)
					map=new HashMap<String, String>();

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

	private void setUpModel() {

		Optional<AudioStudio> o_a = getAudioStudioDBService().findWithDeps(getModel().getObject().getId());
		setModel(new ObjectModel<>(o_a.get()));

		if (getModel().getObject().getAudioSpeech() != null) {
			Optional<Resource> o_r = getResourceDBService().findWithDeps(getModel().getObject().getAudioSpeech().getId());
			setAudioSpeechModel(new ObjectModel<Resource>(o_r.get()));
		}

		if (getModel().getObject().getAudioSpeechMusic() != null) {
			Optional<Resource> o_r = getResourceDBService().findWithDeps(getModel().getObject().getAudioSpeechMusic().getId());
			setAudioSpeechMusicModel(new ObjectModel<Resource>(o_r.get()));
		}

		AudioStudioParentObject po = getAudioStudioDBService().findParentObjectWithDeps(getModelObject()).get();

		
		if (po.getAudio()!=null) {
			parentAudioModel = new ObjectModel<Resource>(po.getAudio());
		}
		
		
		Map<String, String> map = getModel().getObject().getSettings();
		if (map != null) {
			if (map.containsKey("similarity"))
				this.similarity = Double.valueOf(map.get("similarity"));
			if (map.containsKey("speed"))
				this.speed = Double.valueOf(map.get("speed"));
			if (map.containsKey("stability"))
				this.stability = Double.valueOf(map.get("stability"));
			if (map.containsKey("style"))
				this.stability = Double.valueOf(map.get("style"));
			if (map.containsKey("introDurationSec"))
				this.introDurationSec = Integer.valueOf(map.get("introDurationSec"));
			if (map.containsKey("fadeDurationSec"))
				this.fadeDurationSec = Integer.valueOf(map.get("fadeDurationSec"));
			if (map.containsKey("voiceOverlapDurationSec"))
				this.voiceOverlapDurationSec = Integer.valueOf(map.get("voiceOverlapDurationSec"));
		}


		parentName = po.getName();
		parentType = getLabel(po.getClass().getSimpleName().toLowerCase());
		parentId = po.getId();
		parentInfo = po.getInfo();
		
		getModel().getObject().setInfo(parentInfo);
		if (getModel().getObject().getMusicUrl()==null)
			getModel().getObject().setMusicUrl("");
			
		/**
		 * 
		 *
		 * https://archive.org/download/20220921-beethoven-piano-sonatas-nos.-8-14-21-47-wilhelm-kempff/%232%20Beethoven%20L.%20van%20%E2%80%A2%20Piano%20Sonata%20no.%2014%20in%20C%20sharp%20minor%20%E2%80%9CMoonlight%E2%80%9D%20op.%2027%20no.%202.mp3
		 * 
		 * https://archive.org/download/20220921-beethoven-piano-sonatas-nos.-8-14-21-47-wilhelm-kempff/%231%20Beethoven%20L.%20van%20%E2%80%A2%20Piano%20Sonata%20no.%208%20in%20C%20minor%20%E2%80%9CPath%C3%A9tique%E2%80%9D%20op.%2013.mp3
		 * 
		 * https://archive.org/download/20220921-beethoven-piano-sonatas-nos.-8-14-21-47-wilhelm-kempff/%234%20Beethoven%20L.%20van%20%E2%80%A2%20Piano%20Sonata%20no.%2023%20in%20F%20minor%20%E2%80%9CAppassionata%E2%80%9D%20op.%2057.mp3
		 * 
		 * https://archive.org/download/20220921-beethoven-piano-sonatas-nos.-8-14-21-47-wilhelm-kempff/%233%20Beethoven%20L.%20van%20%E2%80%A2%20Piano%20Sonata%20no.%2021%20in%20C%20major%20%E2%80%9CWaldstein%E2%80%9D%20op.%2053.mp3
		 * 
		 * 
		 */

	}

	private void generalInit() {

		form = new Form<AudioStudio>("form");

		add(form);
		setForm(form);

		// common
		nameField = new StaticTextField<String>("name", new PropertyModel<String>(getModel(), "name"), getLabel("name"));
		typeField = new StaticTextField<String>("type", parentType, getLabel("type"));
		languageField = new StaticTextField<String>("language", new PropertyModel<String>(getModel(), "language"), getLabel("language"));

		infoField = new TextAreaField<String>("info", new PropertyModel<String>(getModel(), "info"), getLabel("info"), 12) {

			private static final long serialVersionUID = 1L;

			public boolean isEnabled() {
				return super.isEnabled() && getStatus() == AUDIO_VOICE;
			};

		};

		form.add(infoField);
		form.add(nameField);
		form.add(typeField);
		form.add(languageField);

		Link<Void> li = new Link<Void>("parent-link") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage(new RedirectPage(parentObjectUrl));
			}

			public boolean isVisible() {
				return parentObjectUrl != null;
			}
		};

		Label parentObject = new Label("parent", parentName +" ("+parentType.getObject()+")");
		li.add(parentObject);
		add(li);

		pill_1 = new PillPanel("pill_1", getLabel("pill1.title"), getLabel("pill1.text"));
		getForm().add(pill_1);
		
		pill_2 = new PillPanel("pill_2", getLabel("pill2.title"), getLabel("pill2.text"));
		getForm().add(pill_2);
		
		pill_3 = new PillPanel("pill_3", getLabel("pill3.title"), getLabel("pill3.text"));
		getForm().add(pill_3);
 
	}

	private int getHashAudioSpeech() {

		StringBuilder str = new StringBuilder();
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

	
	protected int getStatus() {
		return this.status;
	}

	protected void setStatus(int status) {

		this.status = status;

		if (this.status == AUDIO_VOICE) {
			pill_1.setCss("pill pill-selected bg-dark text-white");
			pill_2.setCss("pill");
			pill_3.setCss("pill");
		} else if (this.status == AUDIO_VOICE_MUSIC) {
			pill_2.setCss("pill pill-selected bg-dark text-white");
			pill_1.setCss("pill");
			pill_3.setCss("pill");
		} else {
			pill_3.setCss("pill pill-selected bg-dark text-white");
			pill_1.setCss("pill");
			pill_2.setCss("pill");
		}

		step1.addOrReplace(new InvisiblePanel("step1Error"));

		edit();
	}

	
	//private List<String> getMusics() {
	//	return List.of("Chopin", "Beethoven", "Bach");
	//}

	
}
