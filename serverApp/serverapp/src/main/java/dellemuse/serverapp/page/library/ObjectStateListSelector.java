package dellemuse.serverapp.page.library;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.toolbar.DropDownMenuToolbarItem;

/**
 * 
 * 
 * 
 */
public class ObjectStateListSelector extends DropDownMenuToolbarItem<Void> {

	private static final long serialVersionUID = 1L;

	private IModel<String> title;

	public ObjectStateListSelector(String id, IModel<String> title, Align align) {
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
						ObjectStateListSelector.this.setTitle(Model.of(ObjectStateEnumSelector.EDTIION_PUBLISHED.getLabel(getLocale())));
						ObjectStateListSelector.this.addTitlePanel();
						fire(new ObjectStateSelectEvent(ObjectStateEnumSelector.EDTIION_PUBLISHED, target));
					}

					@Override
					public IModel<String> getLabel() {
						return Model.of(ObjectStateEnumSelector.EDTIION_PUBLISHED.getLabel(getLocale()));
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
						ObjectStateListSelector.this.setTitle(Model.of(ObjectStateEnumSelector.PUBLISHED.getLabel(getLocale())));
						ObjectStateListSelector.this.addTitlePanel();
						fire(new ObjectStateSelectEvent(ObjectStateEnumSelector.PUBLISHED, target));
					}

					@Override
					public IModel<String> getLabel() {
						return Model.of(ObjectStateEnumSelector.PUBLISHED.getLabel(getLocale()));
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
						ObjectStateListSelector.this.setTitle(Model.of(ObjectStateEnumSelector.EDITION.getLabel(getLocale())));
						ObjectStateListSelector.this.addTitlePanel();
						fire(new ObjectStateSelectEvent(ObjectStateEnumSelector.EDITION, target));
					}

					@Override
					public IModel<String> getLabel() {
						return Model.of(ObjectStateEnumSelector.EDITION.getLabel(getLocale()));
					}
				};
			}
		});

		addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Void> getItem(String id) {
				return new io.wktui.nav.menu.SeparatorMenuItem<Void>(id);
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
						ObjectStateListSelector.this.setTitle(Model.of(ObjectStateEnumSelector.DELETED.getLabel(getLocale())));
						ObjectStateListSelector.this.addTitlePanel();
						fire(new ObjectStateSelectEvent(ObjectStateEnumSelector.DELETED, target));
					}

					@Override
					public IModel<String> getLabel() {
						return Model.of(ObjectStateEnumSelector.DELETED.getLabel(getLocale()));
					}
				};
			}
		});

		addItem(new io.wktui.nav.menu.MenuItemFactory<Void>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Void> getItem(String id) {
				return new io.wktui.nav.menu.SeparatorMenuItem<Void>(id);
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
						ObjectStateListSelector.this.setTitle(Model.of(ObjectStateEnumSelector.ALL.getLabel(getLocale())));
						ObjectStateListSelector.this.addTitlePanel();
						fire(new ObjectStateSelectEvent(ObjectStateEnumSelector.ALL, target));
					}

					@Override
					public IModel<String> getLabel() {
						return Model.of(ObjectStateEnumSelector.ALL.getLabel(getLocale()));
					}
				};
			}
		});
	}
}
