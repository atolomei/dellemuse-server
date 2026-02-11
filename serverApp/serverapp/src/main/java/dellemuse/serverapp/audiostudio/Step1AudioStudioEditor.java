package dellemuse.serverapp.audiostudio;

import java.io.File;
import java.io.FileInputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.attributes.IAjaxCallListener;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.media.audio.Audio;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.audit.AuditKey;
import dellemuse.serverapp.elevenlabs.ELVoice;
import dellemuse.serverapp.elevenlabs.LanguageCode;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.AudioStudio;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Voice;
import io.wktui.audio.AudioPlayer;
import io.wktui.error.AlertPanel;
import io.wktui.form.Form;
import io.wktui.form.button.EditButtons;
import io.wktui.form.button.SubmitButton;
import io.wktui.form.field.ChoiceField;
import io.wktui.form.field.NumberField;
 
import wktui.base.InvisiblePanel;


/**
 * 
 * 
 * https://archive.org/download/LudwigVanBeethovenMoonlightSonataAdagioSostenutogetTune.net/Ludwig_Van_Beethoven_-_Moonlight_Sonata_Adagio_Sostenuto_%28get-tune.net%29.mp3");
 * https://shorturl.at/uSRb4
 * 
 * 
 * CHOPIN 
 * https://dn721908.ca.archive.org/0/items/ChopinFourteenWaltzes/01%20No.%201%20in%20E%20flat%2C%20Op.%2018%20Grande%20valse%20brilliante.mp3
 * 
 * 
 * CANON PACHEBEL
 * https://dn721806.ca.archive.org/0/items/dm-2530-247/DM%202530%20247%E2%80%A2%282%29%20Pachebel%20J.%2C%20Canon%20et%20gigue%20en%20r%C3%A9%20mineur%20pour%203%20violons%20%26%20bc.mp3
 * 
 * 
 * ALBINONI
 * https://archive.org/download/dm-2530-247/DM%202530%20247%E2%80%A2%281%29%20Albinoni%20T.%2C%20Adagio%20en%20sol%20mineur.mp3
 * 
 */
public class Step1AudioStudioEditor extends BaseAudioStudioEditor {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(Step1AudioStudioEditor.class.getName());

	private NumberField<Double> speedField;
	private NumberField<Double> styleField;
	private NumberField<Double> stabilityField;
	private NumberField<Double> similarityField;
	private ChoiceField<Voice> voicesField;
	
	private Form<AudioStudio> form;

	private WebMarkupContainer step1;
	private WebMarkupContainer step1mp3;

	private Double similarity = 0.53;
	private Double stability = 0.82;
	private Double speed = 0.9;
	private Double audioStyle = 0.01;

	private boolean uploadedStep1 = false;
	
	 


	private IModel<Voice> voiceModel;

	
	 public void onDetach() {
		 super.onDetach();
		 
		 if (voiceModel!=null)
			 voiceModel.detach();
	 }

	
	

	public Step1AudioStudioEditor(String id, IModel<AudioStudio> model, boolean isAccesibleVersion) {
		super(id, model, isAccesibleVersion);
		
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		setup();
	}

	private void setup() {

		 
		
		
		Map<String, String> map = getModel().getObject().getSettings();
		
		if (map!=null) {
			
			if (map.containsKey(getPrefix()+"speed")) 
				speed=Double.valueOf(map.get(getPrefix()+"speed"));
		
			if (map.containsKey(getPrefix()+"stability")) 
				stability =Double.valueOf(map.get(getPrefix()+"stability"));
		
			if (map.containsKey(getPrefix()+"similarity")) 
				similarity =Double.valueOf(map.get(getPrefix()+"similarity"));

			if (map.containsKey(getPrefix()+"audioStyle")) 
				audioStyle =Double.valueOf(map.get(getPrefix()+"audioStyle"));
		}
		
		step1 = new WebMarkupContainer("step1");

		step1.setOutputMarkupId(true);
		add(step1);
		
	
		form = new Form<AudioStudio>("form");
		setForm(form);

		step1.add(form);

		//speedField = new NumberField<Double>("speed", new PropertyModel<Double>(this, "speed"), getLabel("speed"));
		//similarityField = new NumberField<Double>("similarity", new PropertyModel<Double>(this, "similarity"), getLabel("similarity"));
		//styleField = new NumberField<Double>("style", new PropertyModel<Double>(this, "audioStyle"), getLabel("style"));
		//stabilityField = new NumberField<Double>("stability", new PropertyModel<Double>(this, "stability"), getLabel("stability"));

		//form.add(speedField);
		//form.add(similarityField);
		//form.add(stabilityField);
		//form.add(styleField);
		
 	voicesField = new ChoiceField<Voice>("voice",  new PropertyModel<Voice>(this, "voice"), getLabel("voice")) {

			private static final long serialVersionUID = 1L;

			@Override
			public IModel<List<Voice>> getChoices() {
				return new ListModel<Voice>( getVoiceDBService().getVoices(Step1AudioStudioEditor.this.getModel().getObject().getLanguage()));
			}

			 

			@Override
			protected void onComponentTag(ComponentTag tag) {
			    super.onComponentTag(tag);
			 }
			
			@Override
			protected String getDisplayValue(Voice value) {
				try {
					return value.getName() +  (value.getSex()!=null? (" - " + value.getSex()):"") +  "  ( "+ value.getLanguage() +" - " + value.getLanguageRegion() + " )";
				}
				catch (Exception e) {
					return value.getName();	
				}
			}
		};
		form.add(voicesField);
		
		
		
		form.setOutputMarkupId(true);
		
		// Comportamiento Ajax
		voicesField.getInput().add(new AjaxFormComponentUpdatingBehavior("change") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
            	addTestVoicePanel( getVoice() );
                target.add( getForm() );
            }
        });
        
		
		
		addTestVoicePanel( getVoice() );
	
		SubmitButton<AudioStudio> sm = new SubmitButton<AudioStudio>("generate", getModel(), getForm()) {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit(AjaxRequestTarget target) {

				getForm().updateModel();

				if (Step1AudioStudioEditor.this.getObjectInfo() != null && 
					Step1AudioStudioEditor.this.getObjectInfo().length() == 0) {

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
					AlertPanel<Void> alert = new AlertPanel<Void>("error", AlertPanel.SUCCESS, getLabel("alredy-generated"));
					getForm().addOrReplace(alert);
					uploadedStep1 = true;
				}
				target.add(getForm());
			
			}

			@Override
			public boolean isEnabled() {

				if (!hasWritePermission())
					return false;
				
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

	

	protected void addTestVoicePanel( Voice voice ) {
		
		if (voice==null)
			getForm().addOrReplace( new InvisiblePanel("testVoice") );
		else {	
			AudioStudioTestVoicePanel panel = new AudioStudioTestVoicePanel( "testVoice", getModel(), getVoiceModel());
			getForm().addOrReplace( panel );
		}
	}

	
	/**
	protected List<Voice> getVoices() {
		if (vc==null)
			vc = getElevenLabsService().getVoices( getModel().getObject().getLanguage());
		
		vc.sort( new Comparator<ELVoice>() {
			@Override
			public int compare(ELVoice o1, ELVoice o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});
		return vc;
	}
	**/
	
	
	private void addInfo() {
	
		if (getAudioSpeechModel() != null && getAudioSpeechModel().getObject() != null) {
			if (!requiresGenerationAudioSpeech() ) {
				AlertPanel<Void> alert = new AlertPanel<Void>("info", AlertPanel.SUCCESS, getLabel("alredy-generated"));
				getForm().addOrReplace(alert);
				return;
			}
		}
		getForm().addOrReplace(new InvisiblePanel("info"));
	}
	

	
	
	private void step1AudioSpeech() {


		if (getVoiceModel()==null || getVoiceModel().getObject()==null) {
			error("Voice not selected");
			return;
		}
		
		
		String dm_voice_id = getVoiceModel().getObject().getVoiceId();
		
	 	getModel().getObject().setName(getParentName());
		
	 	setObjectInfo(getParentInfo());

		getForm().updateModel();
		String text = getModel().getObject().getInfo();
		
		String language = getModel().getObject().getLanguage();
		String fileName = normalizeFileName(getParentName()) + "-" + getPrefix() + getParentId().toString() + ".mp3";
		LanguageCode languageCode = LanguageCode.from(language);

		

		/**
		if 		(languageCode.equals(LanguageCode.ES)) 		dm_voice_id = "mariana";
		else if (languageCode.equals(LanguageCode.PT)) 		dm_voice_id = "amanda";
		else if (languageCode.equals(LanguageCode.EN))		dm_voice_id = "emily";
		else if (languageCode.equals(LanguageCode.FR))		dm_voice_id = "emily";
		else if (languageCode.equals(LanguageCode.IT))		dm_voice_id = "nicola";
		else if (languageCode.equals(LanguageCode.DUTCH))	dm_voice_id = "thomas";
		else if (languageCode.equals(LanguageCode.GER))		dm_voice_id = "leon";
		else												dm_voice_id = "emily";
**/
	
		
		
		
		Optional<File> ofile = getElevenLabsService().generate(text, fileName, languageCode, dm_voice_id);

		if (ofile.isPresent()) {
			step1AudioSpeechUpload(ofile.get());
		}
	}
	protected void setObjectAudioSpeechHash( int hash) {
		getModel().getObject().setAudioSpeechHash( hash );
	}
	
	
	protected void setObjectAudioSpeech( Resource resource) {
		getModel().getObject().setAudioSpeech(resource);
	}
	
	/**
	 * 
	 * 
	 * @param file
	 * @return
	 */
	private boolean step1AudioSpeechUpload(File file) {

		if (this.uploadedStep1)
			return false;

		try {

			String bucketName = ServerConstant.AUDIO_SPEECH_BUCKET;
			String objectName = getResourceDBService().normalizeFileName(FileNameUtils.getBaseName(file.getName())) + "-" + String.valueOf(getResourceDBService().newId());

			try (FileInputStream inputStream = new FileInputStream(file)) {

				Resource resource = createAndUploadFile(inputStream, bucketName, objectName, file.getName(), file.length());

				setAudioSpeechModel(new ObjectModel<Resource>(resource));
				
				setObjectAudioSpeech(resource);
				setObjectAudioSpeechHash(getHashAudioParameters());
				
				Map<String, String> map = getModel().getObject().getSettings();
				if (map == null)
					map = new HashMap<String, String>();

				if (getSpeed() != null)
					map.put( getPrefix()+"speed", getSpeed().toString());
				if (getStyle() != null)
					map.put(getPrefix()+"style", getStyle().toString());
				if (getStability() != null)
					map.put(getPrefix()+"stability", getStability().toString());
				if (getSimilarity() != null)
					map.put(getPrefix()+"similarity", getSimilarity().toString());
				if (getVoiceModel()!=null && getVoiceModel().getObject()!=null) {
					map.put("voiceid", getVoiceModel().getObject().getVoiceId());
				}
				
				getModel().getObject().setSettings(map);

				getAudioStudioDBService().save(getModel().getObject(), getSessionUser().get(), AuditKey.GENERATE_VOICE);
		
			}
			
			uploadedStep1 = true;
			
		} catch (Exception e) {
			uploadedStep1 = false;
			error("Error saving file: " + e.getMessage());
		}
		return uploadedStep1;
	}



	
	
	protected boolean requiresGenerationAudioSpeech() {
		
		if (getObjectAudio() == null)
			return true;
		
		
		return (getHashAudioParameters() != getObjectAudioSpeechHash());
	}


	
	
	private int getHashAudioParameters() {

		StringBuilder str = new StringBuilder();
			
		if (getObjectInfo() != null)
			str.append(getObjectInfo().toLowerCase().trim());
		else
			str.append("");
		
		str.append("-");
		str.append(getSpeed().toString());
		str.append("-");
		str.append(getSimilarity().toString());
		str.append("-");
		str.append(getStability().toString());
		
		if (getVoiceModel()!=null && getVoiceModel().getObject()!=null) {
			str.append("-");
			str.append(getVoiceModel().getObject().getVoiceId());
		}
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

	public void setVoice(Voice voice) {
		setVoiceModel( new ObjectModel<Voice>(voice));
	}

	
	public Voice getVoice() {
		if (getVoiceModel()==null)
			return null;
		
		return getVoiceModel().getObject();
	}
	
	public IModel<Voice> getVoiceModel() {
		return voiceModel;
	}

	public void setVoiceModel(IModel<Voice> mvoice) {
		this.voiceModel = mvoice;
	}

}
