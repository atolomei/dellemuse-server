package dellemuse.serverapp.global;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.model.UserModel;
import dellemuse.serverapp.serverdb.model.User;
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavBar;
import io.wktui.nav.menu.NavDropDownMenu;
import wktui.base.LabelLinkPanel;
import wktui.base.ModelPanel;


public class UserGlobalTopPanel extends ModelPanel<User> {

	private static final long serialVersionUID = 1L;

	public UserGlobalTopPanel(String id) {
		this(id, null);
	}
	
	public UserGlobalTopPanel(String id, IModel<User> model) {
		super(id, model);
		super.setOutputMarkupId(true);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		
		add(getMenu());
	}
	
	String baseCss="d-block-inline d-sm-block-inline d-md-block-inline d-lg-none  d-xl-none d-xxl-none ps-2 pe-2";
	
 	private NavDropDownMenu<Void> getMenu() {
			
			NavDropDownMenu<Void> menu = new NavDropDownMenu<Void>("userMenu");
			
			
			menu.setIconCss("d-block-inline fa-2x fa-duotone fa-solid fa-user ps-2 pe-2");
			menu.setTitle(new Model<String>( getModel().getObject().getDisplayname()));

			
			menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {

				private static final long serialVersionUID = 1L;

				@Override
				public MenuItemPanel<Void> getItem(String id) {

					return new  LinkMenuItem<Void>(id) {

						private static final long serialVersionUID = 1L;
						
						@Override
						public void onClick() {
						}

						@Override
						public IModel<String> getLabel() {
							return new Model<String>("Account");
						}

						@Override
						public String getBeforeClick() {
							return null;
						}
					};
				}
			});

			menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {

				private static final long serialVersionUID = 1L;

				@Override
				public MenuItemPanel<Void> getItem(String id) {

					return new  LinkMenuItem<Void>(id) {

						private static final long serialVersionUID = 1L;
						
						@Override
						public void onClick() {
						}

						@Override
						public IModel<String> getLabel() {
							return new Model<String>("sign outAccount");
						}

						@Override
						public String getBeforeClick() {
							return null;
						}
					};
				}
			});

			
			return menu;
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
