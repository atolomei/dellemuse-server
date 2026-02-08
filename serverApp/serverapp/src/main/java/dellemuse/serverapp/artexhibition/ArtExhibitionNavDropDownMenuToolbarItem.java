package dellemuse.serverapp.artexhibition;

import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.serverapp.editor.ObjectBaseNavDropDownMenuToolbarItem;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;

import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.InstitutionDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import dellemuse.serverapp.service.language.LanguageService;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.TitleMenuItem;

public class ArtExhibitionNavDropDownMenuToolbarItem extends ObjectBaseNavDropDownMenuToolbarItem<ArtExhibition> {

	private static final long serialVersionUID = 1L;

	
	private IModel<Site> siteModel;
	
	
 
	
	public ArtExhibitionNavDropDownMenuToolbarItem(String id, IModel<ArtExhibition> model, Align align) {
		this(id, model, null, align);

		if (getModel() != null && getModel().getObject() != null) {

			
			if (model.getObject().getShortname() != null)
				setTitle(getLabel("art-exhibition", model.getObject().getShortname()));

			//else if (model.getObject().getName() != null)
			//	getLabel("art-exhibition", model.getObject().getName());
			else
				setTitle( getObjectTitle(model.getObject()));
		}
	}

	public ArtExhibitionNavDropDownMenuToolbarItem(String id, IModel<ArtExhibition> model, IModel<String> title, Align align) {
		super(id, model, title, align);
	}

	public Optional<Institution> getInstitution(Long id) {
		InstitutionDBService service = (InstitutionDBService) ServiceLocator.getInstance().getBean(InstitutionDBService.class);
		return service.findById(id);
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (siteModel!=null)
			siteModel.detach();
	}
	
	
	@Override
	public void onInitialize() {
		super.onInitialize();
		
		setUpModel();
		
		 

		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibition> getItem(String id) {
				return new TitleMenuItem<ArtExhibition>(id) {
					private static final long serialVersionUID = 1L;

					@Override
					public IModel<String> getLabel() {
						return getLabel("art-exhibition-title");
					}
				};
			}
		});

		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibition> getItem(String id) {

				return new AjaxLinkMenuItem<ArtExhibition>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						fire(new MenuAjaxEvent(ServerAppConstant.exhibition_info, target));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("information-record", getModel().getObject().getMasterLanguage());
					}
				};
			}
		});

		for (Language la : getSiteModel().getObject().getLanguages()) {
			

			final String langCode = la.getLanguageCode();

			if (!getModel().getObject().getMasterLanguage().equals(langCode)) {

				addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {

					private static final long serialVersionUID = 1L;

					@Override
					public MenuItemPanel<ArtExhibition> getItem(String id) {

						return new AjaxLinkMenuItem<ArtExhibition>(id, getModel()) {
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

		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibition> getItem(String id) {
				return new io.wktui.nav.menu.SeparatorMenuItem<ArtExhibition>(id) {
					private static final long serialVersionUID = 1L;
				};
			}
		});

		
		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibition> getItem(String id) {

				return new AjaxLinkMenuItem<ArtExhibition>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						fire(new MenuAjaxEvent(ServerAppConstant.exhibition_guides, target));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("exhibition-guides");
					}
				};
			}
		});

		
		
		
		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibition> getItem(String id) {
				return new io.wktui.nav.menu.SeparatorMenuItem<ArtExhibition>(id) {
					private static final long serialVersionUID = 1L;
				};
			}
		});
		
		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibition> getItem(String id) {

				return new AjaxLinkMenuItem<ArtExhibition>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						fire(new MenuAjaxEvent(ServerAppConstant.exhibition_sections, target));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("exhibition-sections");
					}
				};
			}
		});

		addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibition> getItem(String id) {

				return new AjaxLinkMenuItem<ArtExhibition>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						fire(new MenuAjaxEvent(ServerAppConstant.exhibition_items, target));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("items");
					}
				};
			}
		});

		 

		
		addAudit();

		/**
		 * 
		 * 
		 * addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() { private
		 * static final long serialVersionUID = 1L;
		 * 
		 * @Override public MenuItemPanel<ArtExhibition> getItem(String id) { return new
		 *           io.wktui.nav.menu.SeparatorMenuItem<ArtExhibition>(id) { private
		 *           static final long serialVersionUID = 1L; }; } });
		 * 
		 * 
		 * 
		 *           addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {
		 * 
		 *           private static final long serialVersionUID = 1L;
		 * 
		 * @Override public MenuItemPanel<ArtExhibition> getItem(String id) {
		 * 
		 *           return new AjaxLinkMenuItem<ArtExhibition>(id, getModel()) {
		 *           private static final long serialVersionUID = 1L;
		 * @Override public void onClick(AjaxRequestTarget target) { fire ( new
		 *           MenuAjaxEvent(ServerAppConstant.object_meta, target)); }
		 * 
		 * @Override public IModel<String> getLabel() { return getLabel("meta"); } }; }
		 *           });
		 * 
		 * 
		 * 
		 */

		 

		for (Language la : getSiteModel().getObject().getLanguages()) {
			

			final String a_langCode = la.getLanguageCode();

			if (!getModel().getObject().getMasterLanguage().equals(a_langCode)) {

				addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibition>() {

					private static final long serialVersionUID = 1L;

					@Override
					public MenuItemPanel<ArtExhibition> getItem(String id) {

						return new AjaxLinkMenuItem<ArtExhibition>(id, getModel()) {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick(AjaxRequestTarget target) {
								fire(new MenuAjaxEvent(ServerAppConstant.object_audit + "-" + a_langCode, target, a_langCode));
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

	


	public IModel<Site> getSiteModel() {
		return siteModel;
	}



	public void setSiteModel(IModel<Site> siteModel) {
		this.siteModel = siteModel;
	}
	
	private void setUpModel() {
		 
		 
		Optional<Site> o_site = getSiteDBService().findWithDeps(Long.valueOf(getModel().getObject().getSite().getId()));
		if (o_site.isPresent()) {
			setSiteModel(new ObjectModel<Site>(o_site.get()));
		}
 
}
}
