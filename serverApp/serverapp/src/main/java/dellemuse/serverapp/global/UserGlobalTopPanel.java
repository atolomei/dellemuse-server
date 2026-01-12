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
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.model.ObjectModelPanel;
import dellemuse.serverapp.page.user.UserPage;
import dellemuse.serverapp.person.PersonPage;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.objectstorage.AvatarService;
import dellemuse.serverapp.serverdb.service.PersonDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.media.InvisibleImage;
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavBar;
import io.wktui.nav.menu.NavDropDownMenu;
import wktui.base.LabelLinkPanel;
import wktui.base.ModelPanel;

public class UserGlobalTopPanel extends ObjectModelPanel<User> {

	private static final long serialVersionUID = 1L;

	String baseCss = "d-block-inline d-sm-block-inline d-md-block-inline d-lg-none  d-xl-none d-xxl-none ps-2 pe-2";

	public UserGlobalTopPanel(String id) {
		this(id, null);
	}

	public UserGlobalTopPanel(String id, IModel<User> model) {
		super(id, model);
		super.setOutputMarkupId(true);
	}

	private void addDefaultPhoto() {
		AvatarService as = (AvatarService) ServiceLocator.getInstance().getBean(AvatarService.class);

		Optional<String> s = as.getDefaultAvatar(getModel().getObject());
		String presignedThumbnail = s.get();
		if (presignedThumbnail != null) {
			Url url = Url.parse(presignedThumbnail);
			UrlResourceReference resourceReference = new UrlResourceReference(url);
			Image image = new Image("image", resourceReference);
			addOrReplace(image);
		} else {
			addOrReplace(new InvisibleImage("image"));
		}
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		add(getMenu());

		if (getModel() == null || getModel().getObject() == null) {
			add(new InvisibleImage("image"));
		} else {
			Optional<Person> o = getPersonDBService().getByUserWithDeps(getModel().getObject());
			if (o.isPresent()) {
				Person person = o.get();
				if (person.getPhoto() != null) {
					Image image = getThumbnail(person.getPhoto());
					addOrReplace(image);
				} else {
					addDefaultPhoto();
				}
			} else {
				addDefaultPhoto();
			}
		}

	}

	private NavDropDownMenu<Void> getMenu() {

		NavDropDownMenu<Void> menu = new NavDropDownMenu<Void>("userMenu");

		// menu.setIconCss("d-block-inline fa-2x fa-duotone fa-solid fa-user ps-2
		// pe-2");
		// menu.setIconCss("d-block-inline fa-user ps-2 pe-2");

		Optional<Person> o = getPersonDBService().getByUser(getModel().getObject());

		menu.setTitle(new Model<String>(getModel().getObject().getDisplayname()));

		if (o.isPresent())
			menu.setSubtitle(Model.of(o.get().getFirstLastname()));

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Void> getItem(String id) {

				return new LinkMenuItem<Void>(id) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage(new UserPage(UserGlobalTopPanel.this.getModel()));
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

				return new LinkMenuItem<Void>(id) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						Optional<Person> o = getPersonDBService().getByUser(UserGlobalTopPanel.this.getModel().getObject());
						if (o.isPresent())
							setResponsePage(new PersonPage(new ObjectModel<Person>(o.get())));
						else
							setResponsePage(new ErrorPage(Model.of("not found")));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("person");
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
				return new io.wktui.nav.menu.SeparatorMenuItem<Void>(id);
			}
		});

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Void> getItem(String id) {

				return new LinkMenuItem<Void>(id) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage(new RedirectPage("/logout"));
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

	protected PersonDBService getPersonDBService() {
		return (PersonDBService) ServiceLocator.getInstance().getBean(PersonDBService.class);
	}

}
