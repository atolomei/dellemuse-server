package dellemuse.serverapp.voice;

import org.apache.wicket.model.IModel;

import dellemuse.serverapp.serverdb.model.Voice;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.TitleMenuItem;
import io.wktui.nav.toolbar.DropDownMenuToolbarItem;

public class VoiceNavDropDownToolbarItem extends DropDownMenuToolbarItem<Voice> {

	private static final long serialVersionUID = 1L;


	public VoiceNavDropDownToolbarItem(String id,  IModel<Voice> model, IModel<String> title, Align align) {
		super(id, model, title,align);
		 
	}

	
	public void onInitialize() {
		super.onInitialize();
		

		 addItem(new io.wktui.nav.menu.MenuItemFactory<Voice>() {
				private static final long serialVersionUID = 1L;
				@Override
				public MenuItemPanel<Voice> getItem(String id) {
					return new TitleMenuItem<Voice>(id) {
						private static final long serialVersionUID = 1L;

						@Override
						public IModel<String> getLabel() {
							return getLabel("voice");
						}
					};
				}
			});
		
	}
}
