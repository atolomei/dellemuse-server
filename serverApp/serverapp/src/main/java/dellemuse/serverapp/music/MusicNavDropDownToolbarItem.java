package dellemuse.serverapp.music;

import org.apache.wicket.model.IModel;

import dellemuse.serverapp.serverdb.model.Music;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.TitleMenuItem;
import io.wktui.nav.toolbar.DropDownMenuToolbarItem;

public class MusicNavDropDownToolbarItem extends DropDownMenuToolbarItem<Music> {

	private static final long serialVersionUID = 1L;


	public MusicNavDropDownToolbarItem(String id,  IModel<Music> model, IModel<String> title, Align align) {
		super(id, model, title,align);
		 
	}

	
	public void onInitialize() {
		super.onInitialize();
		

		 addItem(new io.wktui.nav.menu.MenuItemFactory<Music>() {
				private static final long serialVersionUID = 1L;
				@Override
				public MenuItemPanel<Music> getItem(String id) {
					return new TitleMenuItem<Music>(id) {
						private static final long serialVersionUID = 1L;

						@Override
						public IModel<String> getLabel() {
							return getLabel("music");
						}
					};
				}
			});
		
	}
}
