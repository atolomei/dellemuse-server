package dellemuse.serverapp.page.library;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import io.wktui.event.SimpleAjaxWicketEvent;

public class RoleSelectEvent extends SimpleAjaxWicketEvent {

	private RoleEnumSelector es;
	
	public RoleEnumSelector getRoleEnumSelector() {
		return es;
	}
	
	public RoleSelectEvent(AjaxRequestTarget target) {
		super(null, target, null);
	}

	public RoleSelectEvent(RoleEnumSelector es, AjaxRequestTarget target) {
		super(es.getLabel(), target, null);
			this.es=es;
	}

	public IModel<String> getLabel() {
		return Model.of(this.es.getLabel());
	}
	
}
