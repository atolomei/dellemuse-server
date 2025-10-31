package dellemuse.serverapp.editor;

import org.apache.wicket.ajax.AjaxRequestTarget;

import io.wktui.event.SimpleAjaxWicketEvent;

public class ObjectUpdateEvent extends SimpleAjaxWicketEvent {


	public ObjectUpdateEvent(AjaxRequestTarget target ) {
		super(null, target, null);
 }
	
	public ObjectUpdateEvent(String name, AjaxRequestTarget target, String moreinfo) {
		super(name, target, moreinfo);
		 
	}

}
