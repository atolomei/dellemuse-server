package dellemuse.serverapp.artist;

import org.apache.wicket.ajax.AjaxRequestTarget;

import io.wktui.event.SimpleAjaxWicketEvent;

public class ArtistCreateEvent extends SimpleAjaxWicketEvent {

	public ArtistCreateEvent(String name, AjaxRequestTarget target) {
		super(name, target);
		 
	}

}
