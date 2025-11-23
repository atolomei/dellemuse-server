package dellemuse.serverapp.audiostudio;

import org.apache.wicket.ajax.AjaxRequestTarget;

import io.wktui.event.SimpleAjaxWicketEvent;


public class AudioStudioAjaxEvent extends SimpleAjaxWicketEvent {

	public AudioStudioAjaxEvent(String name, AjaxRequestTarget target ) {
		super(name, target, null);
	}
	
	public AudioStudioAjaxEvent(String name, AjaxRequestTarget target, String moreinfo) {
		super(name, target, moreinfo);
	}

}
