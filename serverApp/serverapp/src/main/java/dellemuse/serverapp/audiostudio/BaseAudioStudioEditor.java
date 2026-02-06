package dellemuse.serverapp.audiostudio;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.editor.DBObjectEditor;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.AudioStudio;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Resource;

/**
 *
 * 
 */
public class BaseAudioStudioEditor extends DBObjectEditor<AudioStudio> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(BaseAudioStudioEditor.class.getName());

	private IModel<Resource> parentAudioModel;
	private IModel<Resource> audioSpeechModel;
	private IModel<Resource> audioSpeechMusicModel;

	private String parentName;
	private String parentInfo;
	private IModel<String> parentType;
	private Long parentId;
	private ObjectState parentState;

	private boolean isAccesibleVersion;
	
	
	private String prefix = "";
	
	
	protected int getObjectAudioSpeechMusicHash() {
		return getModel().getObject().getAudioSpeechMusicHash();
	}
	
	
	
	
	protected int getObjectAudioSpeechHash() {
		return getModel().getObject().getAudioSpeechHash();
	}
	
	protected Resource getObjectAudio() {
		return getModel().getObject().getAudioSpeech();
	}
	
	

	protected String getObjectInfo() {
		return  getModel().getObject().getInfo();
	}
	
	
	protected void setObjectInfo( String info ) {
		getModel().getObject().setInfo(info);
	}
	
	

	public BaseAudioStudioEditor(String id, IModel<AudioStudio> model, boolean isAccesibleVersion) {
		super(id, model);

		this.isAccesibleVersion = isAccesibleVersion;
		prefix = isAccesibleVersion ? "accesible-" : "";
		
	}

	protected String getPrefix() {
		return this.prefix;
	}
	
	
	public boolean isAccesibleVersion() {
		return this.isAccesibleVersion;
	}

	public void onDetach() {
		super.onDetach();

		if (this.parentAudioModel != null)
			this.parentAudioModel.detach();

		if (this.audioSpeechModel != null)
			this.audioSpeechModel.detach();

		if (this.audioSpeechMusicModel != null)
			this.audioSpeechMusicModel.detach();
	}

	public IModel<String> getVersion() {
		return this.isAccesibleVersion() ? getLabel("accesible") : getLabel("general");
	}

	/** 
	 * 
	 */
	@Override
	public void onInitialize() {
		super.onInitialize();
		setUpModel();
	}

	public void onEdit(AjaxRequestTarget target) {
		super.edit(target);
		// getForm().setFormState(FormState.EDIT);
		// target.add(getForm());
	}

	public IModel<Resource> getParentAudioModel() {
		return parentAudioModel;
	}

	public IModel<Resource> getAudioSpeechModel() {
		return audioSpeechModel;
	}

	public IModel<Resource> getAudioSpeechMusicModel() {
		return audioSpeechMusicModel;
	}

	public void setParentAudioModel(IModel<Resource> parentAudioModel) {
		this.parentAudioModel = parentAudioModel;
	}

	public void setAudioSpeechModel(IModel<Resource> audioSpeechModel) {
		this.audioSpeechModel = audioSpeechModel;
	}

	public void setAudioSpeechMusicModel(IModel<Resource> audioSpeechMusicModel) {
		this.audioSpeechMusicModel = audioSpeechMusicModel;
	}

	public String getParentName() {
		return parentName;
	}

	public String getParentInfo() {
		return parentInfo;
	}

	public IModel<String> getParentType() {
		return parentType;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public void setParentInfo(String parentInfo) {
		this.parentInfo = parentInfo;
	}

	public void setParentType(IModel<String> parentType) {
		this.parentType = parentType;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	protected ObjectState getParentObjectState() {
		return this.parentState;
	}

	protected void setUpModel() {

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

		if (po.getAudio() != null)
			parentAudioModel = new ObjectModel<Resource>(po.getAudio());

		parentName = po.getName();
		parentType = getLabel(po.getClass().getSimpleName().toLowerCase());
		parentId = po.getId();
		parentInfo = po.getInfo();
		parentState = po.getState();

		if (parentInfo != null && getModel().getObject().getInfo() != null) {
			if (!parentInfo.equals(getModel().getObject().getInfo())) {
				getModel().getObject().setInfo(parentInfo);
				save(getModel().getObject());
			}
		}

	}

}
