package dellemuse.serverapp.floor;

import java.util.Optional;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.Floor;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.language.LanguageObjectService;
import io.wktui.model.TextCleaner;
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.SeparatorMenuItem;
import io.wktui.nav.menu.TitleMenuItem;
import io.wktui.nav.toolbar.DropDownMenuToolbarItem;

/**
 * Floor dropdown menu toolbar item to be used from external pages (e.g. SiteRoomPage).
 * Uses page navigation (LinkMenuItem) instead of Ajax events, calling SiteFloorPage
 * and selecting the appropriate tab via setStartTab().
 *
 * @see FloorNavDropDownMenuToolbarItem (Ajax-based, used within SiteFloorPage itself)
 */
public class FloorEXTNavDropDownMenuToolbarItem extends DropDownMenuToolbarItem<Floor> {

	private static final long serialVersionUID = 1L;

	private IModel<Site> siteModel;

	public FloorEXTNavDropDownMenuToolbarItem(String id, IModel<Floor> model, IModel<Site> siteModel, Align align) {
		this(id, model, siteModel, null, align);

		if (model.getObject() != null) {
			setTitle(getLabel("floor-dropdown", getObjectTitle(model.getObject()).getObject()));
		}
	}

	public FloorEXTNavDropDownMenuToolbarItem(String id, IModel<Floor> model, IModel<Site> siteModel, IModel<String> title, Align align) {
		super(id, model, title, align);
		this.siteModel = siteModel;
	}

	public IModel<String> getObjectTitle(MultiLanguageObject o) {
		String s = getLanguageObjectService().getObjectDisplayName(o, getLocale());
		if (s == null)
			return Model.of("");
		return Model.of(TextCleaner.truncate(s, 20));
	}

	public LanguageObjectService getLanguageObjectService() {
		return (LanguageObjectService) ServiceLocator.getInstance().getBean(LanguageObjectService.class);
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

		if (siteModel == null) {
			setUpModel();
		}

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
				return new LinkMenuItem<Floor>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						SiteFloorPage page = new SiteFloorPage(getModel());
						page.setStartTab(ServerAppConstant.floor_info);
						setResponsePage(page);
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
							return new LinkMenuItem<Floor>(id, getModel()) {
								private static final long serialVersionUID = 1L;

								@Override
								public void onClick() {
									SiteFloorPage page = new SiteFloorPage(getModel());
									page.setStartTab(ServerAppConstant.object_translation_record_info + "-" + langCode);
									setResponsePage(page);
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
				return new LinkMenuItem<Floor>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						SiteFloorPage page = new SiteFloorPage(getModel());
						page.setStartTab(ServerAppConstant.floor_rooms);
						setResponsePage(page);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("rooms");
					}
				};
			}
		});
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
