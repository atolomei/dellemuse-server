package dellemuse.serverapp.branded;

import org.apache.wicket.ajax.AjaxRequestTarget;

import io.wktui.event.SimpleAjaxWicketEvent;

public class SearchGlobalTopPanelEvent extends SimpleAjaxWicketEvent {
	
	public SearchGlobalTopPanelEvent(String name, AjaxRequestTarget target ) {
		super(name, target);
	}

	
}
