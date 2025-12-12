package dellemuse.serverapp.page.library;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.toolbar.DropDownMenuToolbarItem;

public class RoleListSelector extends DropDownMenuToolbarItem<Void> {

	private static final long serialVersionUID = 1L;

	private IModel<String> title;

	public RoleListSelector(String id, IModel<String> title, Align align) {
		super(id, align);
		setTitle(title);
	}

	@Override
	public IModel<String> getTitle() {
		return this.title;
	}

	@Override
	public void setTitle(IModel<String> label) {
		this.title = label;
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Void> getItem(String id) {

				return new AjaxLinkMenuItem<Void>(id, getModel()) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						RoleListSelector.this.setTitle(Model.of(RoleEnumSelector.ALL.getLabel(getLocale())));
						RoleListSelector.this.addTitlePanel();
						fire(new RoleSelectEvent(RoleEnumSelector.ALL, target));
					}

					@Override
					public IModel<String> getLabel() {
						return Model.of(RoleEnumSelector.ALL.getLabel(getLocale()));
					}
				};
			}
		});

		addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Void> getItem(String id) {

				return new AjaxLinkMenuItem<Void>(id, getModel()) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						RoleListSelector.this.setTitle(Model.of(RoleEnumSelector.GENERAL.getLabel(getLocale())));
						RoleListSelector.this.addTitlePanel();
						fire(new RoleSelectEvent(RoleEnumSelector.GENERAL, target));
					}

					@Override
					public IModel<String> getLabel() {
						return Model.of(RoleEnumSelector.GENERAL.getLabel(getLocale()));
					}
				};
			}
		});

		addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Void> getItem(String id) {

				return new AjaxLinkMenuItem<Void>(id, getModel()) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						RoleListSelector.this.setTitle(Model.of(RoleEnumSelector.INSTITUTION.getLabel(getLocale())));
						RoleListSelector.this.addTitlePanel();
						fire(new RoleSelectEvent(RoleEnumSelector.INSTITUTION, target));
					}

					@Override
					public IModel<String> getLabel() {
						return Model.of(RoleEnumSelector.INSTITUTION.getLabel(getLocale()));
					}
				};
			}
		});
		
		addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Void> getItem(String id) {

				return new AjaxLinkMenuItem<Void>(id, getModel()) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						RoleListSelector.this.setTitle(Model.of(RoleEnumSelector.SITE.getLabel(getLocale())));
						RoleListSelector.this.addTitlePanel();
						fire(new RoleSelectEvent(RoleEnumSelector.SITE, target));
					}

					@Override
					public IModel<String> getLabel() {
						return Model.of(RoleEnumSelector.SITE.getLabel(getLocale()));
					}
				};
			}
		});
	}
}
