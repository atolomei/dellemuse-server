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
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.toolbar.DropDownMenuToolbarItem;

public class SiteNavDropDownMenuToolbarItem extends DropDownMenuToolbarItem<Site> {

	private static final long serialVersionUID = 1L;

	public SiteNavDropDownMenuToolbarItem(String id, IModel<Site> model, Align align) {
		this(id, model, null, align);

		if (getModel() != null && getModel().getObject() != null) {
			if (model.getObject().getShortName() != null)
				setTitle(getLabel("site-header", model.getObject().getShortName()));
			else
				setTitle(getLabel("site-header", model.getObject().getDisplayname()));
		}
	}

	public SiteNavDropDownMenuToolbarItem(String id, IModel<Site> model, IModel<String> title, Align align) {
		super(id, model, title, align);
	}

	public Optional<Institution> getInstitution(Long id) {
		InstitutionDBService service = (InstitutionDBService) ServiceLocator.getInstance().getBean(InstitutionDBService.class);
		return service.findById(id);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		addItem(new io.wktui.nav.menu.MenuItemFactory<Site>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Site> getItem(String id) {

				return new LinkMenuItem<Site>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						fire(new SimpleWicketEvent(ServerAppConstant.site_action_home));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("sitehome", getModel().getObject().getName());
					}
				};
			}
		});

		addItem(new io.wktui.nav.menu.MenuItemFactory<Site>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Site> getItem(String id) {

				return new io.wktui.nav.menu.SeparatorMenuItem<Site>(id) {
					private static final long serialVersionUID = 1L;
				};
			}
		});

		addItem(new io.wktui.nav.menu.MenuItemFactory<Site>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Site> getItem(String id) {

				return new LinkMenuItem<Site>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage(new SiteInfoPage(getModel()));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("site-general-info");
					}
				};
			}
		});

		addItem(new io.wktui.nav.menu.MenuItemFactory<Site>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Site> getItem(String id) {

				return new io.wktui.nav.menu.SeparatorMenuItem<Site>(id) {
					private static final long serialVersionUID = 1L;
				};
			}
		});

		addItem(new io.wktui.nav.menu.MenuItemFactory<Site>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Site> getItem(String id) {

				return new LinkMenuItem<Site>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage(new InstitutionPage(getInstitutionModel()));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("institution");
					}
				};
			}
		});

		addItem(new io.wktui.nav.menu.MenuItemFactory<Site>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Site> getItem(String id) {

				return new LinkMenuItem<Site>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage(new SiteFloorsPage(getModel()));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("floors");
					}
				};
			}
		});

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

				return new LinkMenuItem<Site>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage(new SiteArtExhibitionsListPage(getModel()));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("exhibitions");
					}
				};
			}
		});

		/**
		 * addItem(new io.wktui.nav.menu.MenuItemFactory<Site>() {
		 * 
		 * private static final long serialVersionUID = 1L;
		 * 
		 * @Override public MenuItemPanel<Site> getItem(String id) {
		 * 
		 *           return new LinkMenuItem<Site>(id, getModel()) { private static
		 *           final long serialVersionUID = 1L;
		 * @Override public void onClick() { setResponsePage( new
		 *           SiteGuideContentsListPage( getModel() )); }
		 * 
		 * @Override public IModel<String> getLabel() { return getLabel("contents"); }
		 *           }; } });
		 **/

		addItem(new io.wktui.nav.menu.MenuItemFactory<Site>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Site> getItem(String id) {

				return new LinkMenuItem<Site>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage(new SiteArtWorkListPage(getModel()));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("artworks");
					}
				};
			}
		});

		addItem(new io.wktui.nav.menu.MenuItemFactory<Site>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Site> getItem(String id) {

				return new LinkMenuItem<Site>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage(new SiteArtistsListPage(getModel()));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("artists");
					}
				};
			}
		});

		addItem(new io.wktui.nav.menu.MenuItemFactory<Site>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Site> getItem(String id) {

				return new LinkMenuItem<Site>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage(new SiteSearcherPage(getModel()));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("search-audio-guide");
					}
				};
			}
		});

		
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

				return new LinkMenuItem<Site>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage(new SiteUsersPage(getModel()));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("users");
					}
				};
			}
		});

		addItem(new io.wktui.nav.menu.MenuItemFactory<Site>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Site> getItem(String id) {

				return new LinkMenuItem<Site>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage(new SiteRolesPage(getModel()));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("roles");
					}
				};
			}
		});

		
		addItem(new io.wktui.nav.menu.MenuItemFactory<Site>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Site> getItem(String id) {

				return new io.wktui.nav.menu.SeparatorMenuItem<Site>(id) {
					private static final long serialVersionUID = 1L;
				};
			}
		});

		addItem(new io.wktui.nav.menu.MenuItemFactory<Site>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Site> getItem(String id) {

				return new LinkMenuItem<Site>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						setResponsePage(new SiteStatePage(getModel()));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("metadata");
					}
				};
			}
		});
	}

	protected IModel<Institution> getInstitutionModel() {
		return new ObjectModel<Institution>(getInstitution(getModel().getObject().getInstitution().getId()).get());
	}

	protected LanguageService getLanguageService() {
		return (LanguageService) ServiceLocator.getInstance().getBean(LanguageService.class);
	}

}
