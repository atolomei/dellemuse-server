package dellemuse.serverapp.artwork;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.editor.ObjectBaseNavDropDownMenuToolbarItem;
import dellemuse.serverapp.person.ServerAppConstant;

import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.language.LanguageService;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.TitleMenuItem;

public class ArtWorkNavDropDownMenuToolbarItem extends ObjectBaseNavDropDownMenuToolbarItem<ArtWork> {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	static private Logger logger = Logger.getLogger(ArtWorkNavDropDownMenuToolbarItem.class.getName());

	protected LanguageService getLanguageService() {
		return (LanguageService) ServiceLocator.getInstance().getBean(LanguageService.class);
	}

	public ArtWorkNavDropDownMenuToolbarItem(String id, IModel<ArtWork> model, IModel<String> title, Align align) {
		super(id, model, title, align);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtWork>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtWork> getItem(String id) {
				return new TitleMenuItem<ArtWork>(id) {
					private static final long serialVersionUID = 1L;

					@Override
					public IModel<String> getLabel() {
						return getLabel("information");
					}
				};
			}
		});

		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtWork>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtWork> getItem(String id) {

				return new AjaxLinkMenuItem<ArtWork>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						fire(new MenuAjaxEvent(ServerAppConstant.artwork_info, target));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("artwork-info-record", getModel().getObject().getMasterLanguage());
					}
				};
			}
		});

		for (Language la : getLanguageService().getLanguages()) {

			final String langCode = la.getLanguageCode();

			if (!langCode.equals(getModel().getObject().getMasterLanguage())) {
				addItem(new io.wktui.nav.menu.MenuItemFactory<ArtWork>() {

					private static final long serialVersionUID = 1L;

					@Override
					public MenuItemPanel<ArtWork> getItem(String id) {

						return new AjaxLinkMenuItem<ArtWork>(id, getModel()) {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick(AjaxRequestTarget target) {
								fire(new MenuAjaxEvent(ServerAppConstant.object_translation_record_info + "-" + langCode, target));
							}

							@Override
							public IModel<String> getLabel() {
								return getLabel("artwork-info-record", langCode);
							}
						};
					}
				});
			}
		}
		addAudit();
	}

}
