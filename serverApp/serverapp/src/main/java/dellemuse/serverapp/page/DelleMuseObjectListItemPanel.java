package dellemuse.serverapp.page;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import dellemuse.serverapp.serverdb.model.DelleMuseObject;
import io.wktui.struct.list.ListPanelMode;

public abstract class DelleMuseObjectListItemPanel<T extends DelleMuseObject> extends ObjectListItemPanel<T> {

	private static final long serialVersionUID = 1L;

	public DelleMuseObjectListItemPanel(String id, IModel<T> model, ListPanelMode mode) {
		super(id, model, mode);
	}
 
	protected IModel<String> getObjectTitle() {
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
