package dellemuse.serverapp.audiostudio;

import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Resource;

public interface AudioStudioParentObject {

 
	public String getInfo();
	public void setInfo(String info);

	public String getName();
	public Long getId();
	
	public Resource getAudio();
	public void setAudio(Resource audioSpeech);
	
	
	public String getPrefixUrl();
	public ObjectState getState();
	
}
