package dellemuse.serverapp.audiostudio;
 
import java.util.List;
 
 
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.media.audio.Audio;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
 
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;

import dellemuse.model.logging.Logger;
 
import dellemuse.serverapp.audit.AuditKey;
import dellemuse.serverapp.editor.ObjectUpdateEvent;
import dellemuse.serverapp.editor.SimpleAlertRow;
 
import dellemuse.serverapp.serverdb.model.AudioStudio;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Resource;
 
import io.wktui.error.AlertPanel;
import io.wktui.form.Form;
import io.wktui.form.button.SubmitButton;
 
import wktui.base.InvisiblePanel;

public class Step3AudioStudioEditor extends BaseAudioStudioEditor {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(Step3AudioStudioEditor.class.getName());

	private Form<AudioStudio> form;
	private WebMarkupContainer step3;
	private WebMarkupContainer step3mp3;

	/**
	 * 
	 *  
	 *  
	 * @param id
	 * @param model
	 * @param isAccesibleVersion
	 */

	public Step3AudioStudioEditor(String id, IModel<AudioStudio> model, boolean isAccesibleVersion) {
		super(id, model, isAccesibleVersion);
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
				
				if (!hasWritePermission())
					return false;

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

	 
		boolean saveRequired = false;
		
		AudioStudioParentObject po = getAudioStudioDBService().findParentObjectWithDeps(getModelObject()).get();
	 
		
		saveRequired=false;
		
		if (!getModelObject().isDependencies()) {
			getModel().setObject( getAudioStudioDBService().findWithDeps( getModelObject().getId()).get());
		}
		
		if (getModelObject().getAudioSpeechMusic()!=null) {
			saveRequired=true;
			logger.debug( getModelObject().getAudioSpeechMusic().getId() );
			po.setAudio(getModelObject().getAudioSpeechMusic());
		}
		else if (getModelObject().getAudioSpeech()!=null) {
			saveRequired=true;
			
			logger.debug( getModelObject().getAudioSpeech().getId() );
			po.setAudio(getModelObject().getAudioSpeech());
		}
		
		if (saveRequired) {	
	
			logger.debug(po.getAudio().getId());
			save(po, getSessionUser().get(),  List.of(AuditKey.INTEGRATE_AUDIO));
			SimpleAlertRow<Void> p = new SimpleAlertRow<Void>("info");
			p.setAlertType(AlertPanel.INFO);
			p.setText( Model.of("successfully integrated"));
			p.setVisible(true);
			getForm().addOrReplace(p);
		
		}
		
		getForm().updateReload();
		fireScanAll(new ObjectUpdateEvent(target));
		target.add(this);
	}

	
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
