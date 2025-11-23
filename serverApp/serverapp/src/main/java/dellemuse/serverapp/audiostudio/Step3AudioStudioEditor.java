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
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.editor.ObjectUpdateEvent;
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

public class Step3AudioStudioEditor extends BaseAudioStudioEditor {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(Step3AudioStudioEditor.class.getName());

	private Form<AudioStudio> form;
	private WebMarkupContainer step3;
	private WebMarkupContainer step3mp3;

	private boolean infoChanged = false;
 

	public Step3AudioStudioEditor(String id, IModel<AudioStudio> model) {
		super(id, model);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		setup();
	}

 
	private void setup() {

		step3 = new WebMarkupContainer("step2");
		step3.setOutputMarkupId(true);

		add(step3);

		form = new Form<AudioStudio>("form");
		setForm(form);

		step3.add(form);

	 
		SubmitButton<AudioStudio> sm = new SubmitButton<AudioStudio>("integrate", getModel(), getForm()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit(AjaxRequestTarget target) {
				Step3AudioStudioEditor.this.onSave(target);
			}

			@Override
			public boolean isEnabled() {

				if (getParentObjectState()== ObjectState.DELETED)
					return false;
				
				if ((getParentAudioModel()!=null) &&
					(getFinalAudioModel()!=null) &&
					(getFinalAudioModel().getObject().getId().equals(getParentAudioModel().getObject().getId())) )
					return false;
				
				if (Step3AudioStudioEditor.this.getModel().getObject().getAudioSpeech() != null) {
					return true;
				}

				if (Step3AudioStudioEditor.this.getModel().getObject().getAudioSpeechMusic() != null)
					return true;

				return false;
			}
			
			public IModel<String> getLabel() {
				return  getLabel("integrate", getParentName());
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
		 
		AjaxLink<Void> prev = new AjaxLink<Void>("prev2") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				fire(new AudioStudioAjaxEvent("step3.prev", target));
			}
		};
		
		form.add(prev);
	
		form.addOrReplace(new InvisiblePanel("error"));
		form.addOrReplace(new InvisiblePanel("info"));

		form.add(sm);
		
		addStep2MP3();
		
		edit();
	}

	private void onSave(AjaxRequestTarget target) {

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

	
		if (saveRequired) {	
			save(po);
		}
		
		this.infoChanged   = false;
		 
		
		getForm().updateReload();

		fire(new ObjectUpdateEvent(target));
		target.add(this);
	}

	/**
	 * 
	 * 
	 */
	private void addStep2MP3() {

		this.step3mp3 = new WebMarkupContainer("step3MP3");

		if (getAudioSpeechMusicModel() != null && getAudioSpeechMusicModel().getObject() != null) {
			String audioUrl = getPresignedUrl(getAudioSpeechMusicModel().getObject());
			Url url = Url.parse(audioUrl);
			UrlResourceReference resourceReference = new UrlResourceReference(url);
			Audio audio = new Audio("audioVoiceMusic", resourceReference);

			this.step3mp3.addOrReplace(audio);

			Label am = new Label("audioVoiceMusicMetadata", getAudioMeta(getAudioSpeechMusicModel().getObject()));
			am.setEscapeModelStrings(false);
			this.step3mp3.addOrReplace(am);
		} else {

			Url url = Url.parse("");
			UrlResourceReference resourceReference = new UrlResourceReference(url);
			Audio audio = new Audio("audioVoiceMusic", resourceReference);
			this.step3mp3.addOrReplace(audio);
			Label am = new Label("audioVoiceMusicMetadata", "");
			am.setEscapeModelStrings(false);
			this.step3mp3.addOrReplace(am);
			this.step3mp3.setVisible(false);
		}
		getForm().addOrReplace(this.step3mp3);
	}


	private IModel<Resource> getFinalAudioModel() {

		 if (getAudioSpeechMusicModel()!=null)
			 return  getAudioSpeechMusicModel();
		 
		if (getAudioSpeechModel()!=null)
			return getAudioSpeechModel();
		
		return null;
	}

}
