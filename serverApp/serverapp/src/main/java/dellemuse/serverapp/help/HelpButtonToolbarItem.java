package dellemuse.serverapp.help;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

import dellemuse.serverapp.icons.Icons;
import dellemuse.serverapp.person.ServerAppConstant;
import io.wktui.event.HelpAjaxEvent;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;


/**
 * 
 * 
 */
public class HelpButtonToolbarItem extends AjaxButtonToolbarItem<String> {
			
	private static final long serialVersionUID = 1L;

	
	public HelpButtonToolbarItem(String id, Align a) {
		super(id);
		super.setAlign(a);
	}
	
	public HelpButtonToolbarItem(String id, IModel<String> title) {
		super(id, title);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
			}

	@Override
	public IModel<String> getButtonLabel() {
		return null;
	}
	
	protected String getIconCss() {
		return Icons.help;
	}

	@Override
	protected void onCick(AjaxRequestTarget target) {
		fire(new   HelpAjaxEvent(ServerAppConstant.help, target));
		
	}
	
	protected String getButtonCss() {
		return "fs-5 btn btn-sm btn-link mt-0";
	}
	
	public IModel<String> getButtonTitle() {
		return getLabel("help");
	}
	
}
