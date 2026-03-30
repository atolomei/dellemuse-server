package dellemuse.serverapp.branded;

import org.apache.wicket.ajax.AjaxRequestTarget;

import dellemuse.serverapp.serverdb.model.AccesibilityMode;
import io.wktui.event.SimpleAjaxWicketEvent;

public class AccesibilityAjaxEvent extends SimpleAjaxWicketEvent {

	AccesibilityMode mode;

	public AccesibilityAjaxEvent(String name, AccesibilityMode mode, AjaxRequestTarget target) {
		super(name, target);
		this.mode = mode;

	}

	public AccesibilityMode getMode() {
		return mode;
	}

	public void setMode(AccesibilityMode mode) {
		this.mode = mode;
	}

}
