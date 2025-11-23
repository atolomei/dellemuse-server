package dellemuse.serverapp.editor;

import org.apache.wicket.ajax.AjaxRequestTarget;

import io.wktui.event.SimpleAjaxWicketEvent;

public class ObjectRestoreEvent extends SimpleAjaxWicketEvent {

	public ObjectRestoreEvent(AjaxRequestTarget target ) {
		super(null, target, null);
	}
	
	public ObjectRestoreEvent(String name, AjaxRequestTarget target, String moreinfo) {
		super(name, target, moreinfo);
	}

}
