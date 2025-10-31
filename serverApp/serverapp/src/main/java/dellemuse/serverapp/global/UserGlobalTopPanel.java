package dellemuse.serverapp.global;

import java.util.Optional;

import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;

import dellemuse.model.UserModel;
import dellemuse.model.util.ThumbnailSize;
import dellemuse.serverapp.page.user.UserPage;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.objectstorage.AvatarService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.media.InvisibleImage;
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

	
		if (getModel()==null || getModel().getObject()==null) {
			add(new InvisibleImage("userImage"));
		}
		else {
			AvatarService as = (AvatarService) ServiceLocator.getInstance().getBean(AvatarService.class);
			
			Optional<String> s = as.getDefaultAvatar(getModel().getObject());
			
			String presignedThumbnail = s.get();
			
			if (presignedThumbnail != null) {
				Url url = Url.parse(presignedThumbnail);
				UrlResourceReference resourceReference = new UrlResourceReference(url);
				Image image = new Image("userImage", resourceReference);
				addOrReplace(image);
			} else {
				add(new InvisibleImage("userImage"));
			}
		}
	
	}

	
	
	
	
	String baseCss="d-block-inline d-sm-block-inline d-md-block-inline d-lg-none  d-xl-none d-xxl-none ps-2 pe-2";
	
 	private NavDropDownMenu<Void> getMenu() {
			
			NavDropDownMenu<Void> menu = new NavDropDownMenu<Void>("userMenu");
			
			
			// menu.setIconCss("d-block-inline fa-2x fa-duotone fa-solid fa-user ps-2 pe-2");
			// menu.setIconCss("d-block-inline fa-user ps-2 pe-2");

			
			menu.setTitle(new Model<String>( getModel().getObject().getDisplayname()));

			
			menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {

				private static final long serialVersionUID = 1L;

				@Override
				public MenuItemPanel<Void> getItem(String id) {

					return new  LinkMenuItem<Void>(id) {

						private static final long serialVersionUID = 1L;
						
						@Override
						public void onClick() {
							setResponsePage( new UserPage(UserGlobalTopPanel.this.getModel()));
						}

						@Override
						public IModel<String> getLabel() {
							return getLabel("account");
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
							setResponsePage( new UserPage(UserGlobalTopPanel.this.getModel() ));
						}

						@Override
						public IModel<String> getLabel() {
							return getLabel("sign-out");

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
