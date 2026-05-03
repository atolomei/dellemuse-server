package dellemuse.serverapp.floor;

import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.serverapp.editor.ObjectBaseNavDropDownMenuToolbarItem;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.site.SiteNavDropDownMenuToolbarItem;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.Floor;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.SeparatorMenuItem;
import io.wktui.nav.menu.TitleMenuItem;

public class FloorNavDropDownMenuToolbarItem extends ObjectBaseNavDropDownMenuToolbarItem<Floor> {

	private static final long serialVersionUID = 1L;

	private IModel<Site> siteModel;

	public FloorNavDropDownMenuToolbarItem(String id, IModel<Floor> model, Align align) {
		this(id, model, null, align);

		if (getModel() != null && getModel().getObject() != null) {
			setTitle(getObjectTitle(model.getObject()));
		}
	}

	public FloorNavDropDownMenuToolbarItem(String id, IModel<Floor> model, IModel<String> title, Align align) {
		super(id, model, title, align);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		if (siteModel != null)
			siteModel.detach();
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();

		// ---- Title header ----
		addItem(new io.wktui.nav.menu.MenuItemFactory<Floor>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Floor> getItem(String id) {
				return new TitleMenuItem<Floor>(id) {
					private static final long serialVersionUID = 1L;

					@Override
					public IModel<String> getLabel() {
						return getLabel("floor-title");
					}
				};
			}
		});

		// ---- Master language info ----
		addItem(new io.wktui.nav.menu.MenuItemFactory<Floor>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Floor> getItem(String id) {
				return new AjaxLinkMenuItem<Floor>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						fire(new MenuAjaxEvent(ServerAppConstant.floor_info, target));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("information-record", getModel().getObject().getMasterLanguage());
					}
				};
			}
		});

		// ---- Translation language items ----
		if (getSiteModel() != null) {
			for (Language la : getSiteModel().getObject().getLanguages()) {
				final String langCode = la.getLanguageCode();

				if (!getModel().getObject().getMasterLanguage().equals(langCode)) {
					addItem(new io.wktui.nav.menu.MenuItemFactory<Floor>() {
						private static final long serialVersionUID = 1L;

						@Override
						public MenuItemPanel<Floor> getItem(String id) {
							return new AjaxLinkMenuItem<Floor>(id, getModel()) {
								private static final long serialVersionUID = 1L;

								@Override
								public void onClick(AjaxRequestTarget target) {
									fire(new MenuAjaxEvent(ServerAppConstant.object_translation_record_info + "-" + langCode, target));
								}

								@Override
								public IModel<String> getLabel() {
									return getLabel("information-record", langCode);
								}
							};
						}
					});
				}
			}
		}

		// ---- Separator ----
		addItem(new io.wktui.nav.menu.MenuItemFactory<Floor>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Floor> getItem(String id) {
				return new SeparatorMenuItem<Floor>(id);
			}
		});

		// ---- Rooms ----
		addItem(new io.wktui.nav.menu.MenuItemFactory<Floor>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Floor> getItem(String id) {
				return new AjaxLinkMenuItem<Floor>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						fire(new MenuAjaxEvent(ServerAppConstant.floor_rooms, target));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("rooms");
					}
				};
			}
		});

		addAudit();
	}

	public IModel<Site> getSiteModel() {
		return siteModel;
	}

	public void setSiteModel(IModel<Site> siteModel) {
		this.siteModel = siteModel;
	}

	private void setUpModel() {
		if (getModel().getObject().getSite() != null) {
			SiteDBService svc = (SiteDBService) ServiceLocator.getInstance().getBean(SiteDBService.class);
			Optional<Site> o = svc.findWithDeps(getModel().getObject().getSite().getId());
			o.ifPresent(s -> setSiteModel(new ObjectModel<Site>(s)));
		}
	}
}
