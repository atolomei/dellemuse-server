package dellemuse.serverapp.page.user;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.language.LanguageService;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.event.SimpleWicketEvent;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
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
						fire(new MenuAjaxEvent(ServerAppConstant.user_panel_info, target));
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

				return new  LinkMenuItem<User>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						fire(new SimpleWicketEvent(ServerAppConstant.user_panel_person));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("person");
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
						fire(new MenuAjaxEvent(ServerAppConstant.user_panel_password, target));
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

				return new AjaxLinkMenuItem<User>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						fire(new MenuAjaxEvent(ServerAppConstant.user_panel_roles, target));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("roles");
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
