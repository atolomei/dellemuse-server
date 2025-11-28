package dellemuse.serverapp.page;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;

import dellemuse.serverapp.serverdb.model.DelleMuseObject;
import io.wktui.event.UIEvent;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;
import io.wktui.struct.list.ListPanelWicketEvent;
 
import wktui.base.InvisiblePanel;
import wktui.base.ModelPanel;

public abstract class DelleMuseObjectListItemPanel<T extends DelleMuseObject> extends ObjectListItemPanel<T> {

	private static final long serialVersionUID = 1L;

	/**
	 * @param id
	 * @param model
	 * @param mode
	 */

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
