package dellemuse.serverapp.audiostudio;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.record.TranslationRecord;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;

public class AudioStudioParentObjectProvider<T extends AudioStudioParentObject> implements IDetachable {

	private static final long serialVersionUID = 1L;

	private IModel<T> parentModel;
	
	public AudioStudioParentObjectProvider( IModel<T> parentModel ) {
		this.parentModel=parentModel;
	}

	public T getModelObject() {
		return getParentModel().getObject();
	}

	public IModel<String> getParentClassLabel() {
		try {
			StringResourceModel s = new StringResourceModel(getModelObject().getClass().getSimpleName().toLowerCase());
			return s;
		}
		catch (Exception e) {
			return Model.of(getModelObject().getClass().getSimpleName().toLowerCase());
		}
	}
	
	@Override
	public void detach() {
	 parentModel.detach();
	}

	public IModel<T> getParentModel() {
		return parentModel;
	}

	public void setParentModel(IModel<T> parentModel) {
		this.parentModel = parentModel;
	}



	/** 
	public Site getSite() {
		
		
		ServiceLocator.getInstance().get
		if (getModelObject() instanceof TranslationRecord) {
			
		}
		else if (getModelObject() instanceof ArtExhibition) {
			
			getModelObject()
			
			
			
		}
		
		return null;
		
		
	}
	**/
	
	
	public ArtExhibition getArtExhibition() {
		

		
		return null;
	}

	
	
	
	
	
	
	
	
	
}
