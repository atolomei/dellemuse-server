package dellemuse.serverapp.editor;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.DelleMuseObject;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.language.LanguageObjectService;
import dellemuse.serverapp.service.language.LanguageService;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.toolbar.DropDownMenuToolbarItem;

public class ObjectBaseNavDropDownMenuToolbarItem<T extends DelleMuseObject> extends DropDownMenuToolbarItem<T> {

	private static final long serialVersionUID = 1L;

	public ObjectBaseNavDropDownMenuToolbarItem(String id, IModel<T> model, Align align) {
		this(id, model, null, align);
	}

	public ObjectBaseNavDropDownMenuToolbarItem(String id, IModel<T> model, IModel<String> title, Align align) {
		super(id, model, title, align);
	}

	public IModel<String> getObjectTitle(MultiLanguageObject o) {
		String s = getLanguageObjectService().getObjectDisplayName(o, getLocale());
		if (s == null)
			return null;
		return Model.of(s);
	}
	
	
	 
	public LanguageObjectService getLanguageObjectService() {
		return (LanguageObjectService) ServiceLocator.getInstance().getBean(LanguageObjectService.class);
	}
	
	protected void addAudit() {

		addItem(new io.wktui.nav.menu.MenuItemFactory<T>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<T> getItem(String id) {

				return new io.wktui.nav.menu.SeparatorMenuItem<T>(id) {
					private static final long serialVersionUID = 1L;
				};
			}
		});

		addItem(new io.wktui.nav.menu.MenuItemFactory<T>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<T> getItem(String id) {

				return new AjaxLinkMenuItem<T>(id, getModel()) {
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

		addItem(new io.wktui.nav.menu.MenuItemFactory<T>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<T> getItem(String id) {

				return new io.wktui.nav.menu.SeparatorMenuItem<T>(id) {
					private static final long serialVersionUID = 1L;
				};
			}
		});

		addItem(new io.wktui.nav.menu.MenuItemFactory<T>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<T> getItem(String id) {

				return new AjaxLinkMenuItem<T>(id, getModel()) {
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

	@Override
	public void onInitialize() {
		super.onInitialize();

	}
	
	

	protected SiteDBService getSiteDBService() {
		return (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
	}

	protected LanguageService getLanguageService() {
		return (LanguageService) ServiceLocator.getInstance().getBean(LanguageService.class);
	}

}
