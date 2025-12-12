package dellemuse.serverapp.page.site;

import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

import dellemuse.serverapp.institution.InstitutionPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.InstitutionDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.language.LanguageService;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.event.SimpleWicketEvent;
import io.wktui.model.TextCleaner;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.TitleMenuItem;
import io.wktui.nav.toolbar.DropDownMenuToolbarItem;

public class SiteInfoNavDropDownMenuToolbarItem extends DropDownMenuToolbarItem<Site> {

	private static final long serialVersionUID = 1L;

	public SiteInfoNavDropDownMenuToolbarItem(String id, IModel<Site> model, Align align) {
		this(id, model, null, align);
		setTitle(getLabel("site-information", TextCleaner.truncate(getModel().getObject().getDisplayname(), 24)));
	}

	public SiteInfoNavDropDownMenuToolbarItem(String id, IModel<Site> model, IModel<String> title, Align align) {
		super(id, model, title, align);
	}

	public Optional<Institution> getInstitution(Long id) {
		InstitutionDBService service = (InstitutionDBService) ServiceLocator.getInstance()
				.getBean(InstitutionDBService.class);
		return service.findById(id);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
 
		
		 addItem(new io.wktui.nav.menu.MenuItemFactory<Site>() {
			private static final long serialVersionUID = 1L;
			@Override
			public MenuItemPanel<Site> getItem(String id) {
				return new TitleMenuItem<Site>(id) {
					private static final long serialVersionUID = 1L;

					@Override
					public IModel<String> getLabel() {
						return getLabel("information");
					}
				};
			}
		});
		

		addItem(new io.wktui.nav.menu.MenuItemFactory<Site>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Site> getItem(String id) {

				return new AjaxLinkMenuItem<Site>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						fire(new MenuAjaxEvent(ServerAppConstant.site_page_info, target));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("information");
						//			return getLabel("site-info-record", getModel().getObject().getMasterLanguage());
					}
				};
			}
		});
		
		
		
		
		
		for (Language la : getLanguageService().getLanguages()) {

			final String langCode = la.getLanguageCode();

			if (!getModel().getObject().getMasterLanguage().equals(langCode)) {
				
				addItem(new io.wktui.nav.menu.MenuItemFactory<Site>() {

					private static final long serialVersionUID = 1L;

					@Override
					public MenuItemPanel<Site> getItem(String id) {

						return new AjaxLinkMenuItem<Site>(id, getModel()) {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick(AjaxRequestTarget target) {
								fire(new MenuAjaxEvent(ServerAppConstant.object_translation_record_info + "-" + langCode, target));
							}

							@Override
							public IModel<String> getLabel() {
								return getLabel("site-info-record", langCode);
							}
						};
					}
				});
			}
		}
		 
 
		addItem(new io.wktui.nav.menu.MenuItemFactory<Site>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Site> getItem(String id) {

				return new io.wktui.nav.menu.SeparatorMenuItem<Site>(id) {
					private static final long serialVersionUID = 1L;

					@Override
					public boolean isVisible() {
						return true;
					}
				};
			}
		});

		 
		addItem(new io.wktui.nav.menu.MenuItemFactory<Site>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Site> getItem(String id) {

				return new AjaxLinkMenuItem<Site>(id, getModel()) {
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
	 	
		
		for (Language la : getLanguageService().getLanguages()) {

			final String a_langCode = la.getLanguageCode();

			if (!getModel().getObject().getMasterLanguage().equals(a_langCode)) {
				
				addItem(new io.wktui.nav.menu.MenuItemFactory<Site>() {

					private static final long serialVersionUID = 1L;

					@Override
					public MenuItemPanel<Site> getItem(String id) {

						return new AjaxLinkMenuItem<Site>(id, getModel()) {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick(AjaxRequestTarget target) {
								fire(new MenuAjaxEvent(ServerAppConstant.object_audit+"-"+a_langCode, target, a_langCode));
							}

							@Override
							public IModel<String> getLabel() {
								return getLabel("audit-lang", a_langCode);
							}
						};
					}
				});
			}
		}
		 
		
	 	
		
		
	}

	protected IModel<Institution> getInstitutionModel() {
		return new ObjectModel<Institution>(getInstitution(getModel().getObject().getInstitution().getId()).get());
	}

	protected LanguageService getLanguageService() {
		return (LanguageService) ServiceLocator.getInstance().getBean(LanguageService.class);
	}

}
