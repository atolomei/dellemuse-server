package dellemuse.serverapp.page.user;

import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.InstitutionDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.language.LanguageService;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.menu.TitleMenuItem;
import io.wktui.nav.toolbar.DropDownMenuToolbarItem;

public class UserNavDropDownMenuToolbarItem extends DropDownMenuToolbarItem<User> {

	private static final long serialVersionUID = 1L;

	public UserNavDropDownMenuToolbarItem(String id, IModel<User> model, IModel<String> title, Align align) {
		super(id, model, title, align);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		addItem(new io.wktui.nav.menu.MenuItemFactory<User>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<User> getItem(String id) {

				return new AjaxLinkMenuItem<User>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						fire(new MenuAjaxEvent(ServerAppConstant.user_info, target));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("information");
					}
				};
			}
		});

		addItem(new io.wktui.nav.menu.MenuItemFactory<User>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<User> getItem(String id) {

				return new AjaxLinkMenuItem<User>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						fire(new MenuAjaxEvent(ServerAppConstant.user_password, target));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("password");
					}
				};
			}
		});

		addItem(new io.wktui.nav.menu.MenuItemFactory<User>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<User> getItem(String id) {
				return new io.wktui.nav.menu.SeparatorMenuItem<User>(id);
			}
		});

		addItem(new io.wktui.nav.menu.MenuItemFactory<User>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<User> getItem(String id) {

				return new AjaxLinkMenuItem<User>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						fire(new MenuAjaxEvent(ServerAppConstant.object_meta, target));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("metadata");
					}
				};
			}
		});

		addItem(new io.wktui.nav.menu.MenuItemFactory<User>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<User> getItem(String id) {
				return new io.wktui.nav.menu.SeparatorMenuItem<User>(id);
			}
		});

		addItem(new io.wktui.nav.menu.MenuItemFactory<User>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<User> getItem(String id) {

				return new AjaxLinkMenuItem<User>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						fire(new MenuAjaxEvent(ServerAppConstant.object_audit, target));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("audit");
					}
				};
			}
		});

	}

	protected LanguageService getLanguageService() {
		return (LanguageService) ServiceLocator.getInstance().getBean(LanguageService.class);
	}

}
