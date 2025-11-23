package dellemuse.serverapp.editor;

import org.apache.wicket.ajax.AjaxRequestTarget;

import io.wktui.event.SimpleAjaxWicketEvent;

public class ObjectMarkAsDeleteEvent extends SimpleAjaxWicketEvent {


	public ObjectMarkAsDeleteEvent(AjaxRequestTarget target ) {
		super(null, target, null);
 }
	
	public ObjectMarkAsDeleteEvent(String name, AjaxRequestTarget target, String moreinfo) {
		super(name, target, moreinfo);
		 
	}

}
