package dellemuse.serverapp.page;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import dellemuse.serverapp.serverdb.model.DelleMuseObject;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.language.LanguageObjectService;
import io.wktui.struct.list.ListPanelMode;

public abstract class DelleMuseObjectListItemPanel<T extends DelleMuseObject> extends ObjectListItemPanel<T> {

	private static final long serialVersionUID = 1L;

	 
	
	public LanguageObjectService getLanguageObjectService() {
		return (LanguageObjectService) ServiceLocator.getInstance().getBean(LanguageObjectService.class);
	}
	
	public DelleMuseObjectListItemPanel(String id, IModel<T> model, ListPanelMode mode) {
		super(id, model, mode);
	}

	protected IModel<String> getObjectTitle() {

		//StringBuilder str = new StringBuilder();
		
		if (getModel().getObject() instanceof MultiLanguageObject) {
			String s = getLanguageObjectService().getObjectDisplayName((MultiLanguageObject) getModel().getObject(), getLocale());
			if (s == null)
				return null;
			return Model.of(s);
		}
		else
			return new Model<String>(getModel().getObject().getDisplayname());
	}

	@Override
	protected boolean isEqual(T o1, T o2) {
		return o1.getId().equals(DelleMuseObjectListItemPanel.this.getModel().getObject().getId());
	}

	protected IModel<String> getInfo() {
		return new Model<String>(getModel().getObject().toJSON());
	}

}
