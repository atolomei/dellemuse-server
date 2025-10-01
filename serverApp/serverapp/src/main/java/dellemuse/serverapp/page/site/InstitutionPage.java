package dellemuse.serverapp.page.site;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.UrlResourceReference;
import org.apache.wicket.util.string.StringValue;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.global.GlobalFooterPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.global.PageHeaderPanel;
import dellemuse.serverapp.page.BasePage;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.ObjectPage;
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.person.PersonPage;
import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.SiteDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.event.SimpleAjaxWicketEvent;
import io.wktui.event.UIEvent;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.breadcrumb.Navigator;
import io.wktui.nav.listNavigator.ListNavigator;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.SeparatorMenuItem;
import io.wktui.nav.menu.TitleMenuItem;
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.DropDownMenuToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;
import wktui.base.DummyBlockPanel;
import wktui.base.INamedTab;
import wktui.base.InvisiblePanel;
import wktui.base.NamedTab;

/**
 * site foto Info - exhibitions
 */

@MountPath("/institution/${id}")
public class InstitutionPage extends ObjectPage<Institution> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(InstitutionPage.class.getName());

	private Image image;
	private WebMarkupContainer imageContainer;
	private Link<Resource> imageLink;
	private InstitutionMainPanel editor;

	public InstitutionPage() {
		super();
	}

	public InstitutionPage(PageParameters parameters) {
		super(parameters);

	}

	public InstitutionPage(IModel<Institution> model) {
		this(model, null);
	}

	public InstitutionPage(IModel<Institution> model, List<IModel<Institution>> list) {
		super(model, list);

	}

	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<SimpleAjaxWicketEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(SimpleAjaxWicketEvent event) {
				logger.debug(event.toString());

				if (event.getName().equals(ServerAppConstant.action_site_edit)) {
					InstitutionPage.this.onEdit(event.getTarget());
				}

				else if (event.getName().equals(ServerAppConstant.site_info)) {
					InstitutionPage.this.togglePanel(ServerAppConstant.site_info, event.getTarget());
				}

				else if (event.getName().equals(ServerAppConstant.audit)) {
					InstitutionPage.this.togglePanel(ServerAppConstant.audit, event.getTarget());
				}
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof SimpleAjaxWicketEvent)
					return true;
				return false;
			}
		});
	}

	@Override
	protected List<ToolbarItem> getToolbarItems() {

		List<ToolbarItem> list = new ArrayList<ToolbarItem>();

		DropDownMenuToolbarItem<Institution> menu = new DropDownMenuToolbarItem<Institution>("item", getModel(),
				Align.TOP_RIGHT);
		menu.setLabel(Model.of( TextCleaner.truncate ( getModel().getObject().getName(), 24) +" (I)" ));

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Institution>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Institution> getItem(String id) {
				return new AjaxLinkMenuItem<Institution>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						fire(new io.wktui.event.SimpleAjaxWicketEvent(ServerAppConstant.site_info, target));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("institution-info");
					}
				};
			}
		});

		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Institution>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Institution> getItem(String id) {
				return new SeparatorMenuItem<Institution>(id, getModel());
			}
		});
		

		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Institution>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Institution> getItem(String id) {
				return new TitleMenuItem<Institution>(id) {
					private static final long serialVersionUID = 1L;

					@Override
					public IModel<String> getLabel() {
						return InstitutionPage.this.getLabel("sites");
					}
				};
			}
		});
		
		

		
		getSites(getModel().getObject()).forEach( site ->  {
			
			final Long siteId = site.getId();
			final String siteName = site.getDisplayname();
					
			
			menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Institution>() {

				private static final long serialVersionUID = 1L;

				@Override
				public MenuItemPanel<Institution> getItem(String id) {
					return new LinkMenuItem<Institution>(id, getModel()) {
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick() {
								InstitutionPage.this.getSite(siteId);
								setResponsePage( new SitePage(new ObjectModel<Site>(InstitutionPage.this.getSite(siteId).get())));
						}

						@Override
						public IModel<String> getLabel() {
							return Model.of(siteName);
						}
					};
				}
			});
		});
		
		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Institution>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Institution> getItem(String id) {
				return new SeparatorMenuItem<Institution>(id, getModel());
			}
		});

		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Institution>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Institution> getItem(String id) {
				return new AjaxLinkMenuItem<Institution>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						fire(new io.wktui.event.SimpleAjaxWicketEvent(ServerAppConstant.audit, target));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("audit");
					}
				};
			}
		});
		
		
		
		
		
		

		list.add(menu);
		return list;
	}

	@Override
	protected List<INamedTab> getInternalPanels() {

		List<INamedTab> tabs = new ArrayList<INamedTab>();

		NamedTab tab_1 = new NamedTab(Model.of("editor"), ServerAppConstant.site_info) {

			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getEditor(panelId);
			}
		};
		tabs.add(tab_1);

		NamedTab tab_2 = new NamedTab(Model.of("audit"), ServerAppConstant.audit) {
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return new DummyBlockPanel(panelId, getLabel("audit"));
			}
		};
		tabs.add(tab_2);

		return tabs;
	}

	@Override
	protected void addHeaderPanel() {
		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(new HREFBCElement("/institution/list", getLabel("institutions")));
		bc.addElement(new BCElement(new Model<String>(getModel().getObject().getDisplayname())));

		if (getList() != null && getList().size() > 0) {
			Navigator<Institution> nav = new Navigator<Institution>("navigator", getCurrent(), getList()) {
				private static final long serialVersionUID = 1L;

				@Override
				public void navigate(int current) {
					setResponsePage(new InstitutionPage(getList().get(current), getList()));
				}
			};
			bc.setNavigator(nav);
		}

		JumboPageHeaderPanel<Institution> ph = new JumboPageHeaderPanel<Institution>("page-header", getModel(),
				new Model<String>(getModel().getObject().getDisplayname()));

		if (getModel().getObject().getSubtitle() != null)
			ph.setTagline(Model.of(getModel().getObject().getSubtitle()));

		if (getModel().getObject().getPhoto() != null)
			ph.setPhotoModel(new ObjectModel<Resource>(getModel().getObject().getPhoto()));

		ph.setBreadCrumb(bc);

		 ph.setContext(getLabel("institution"));
		 
		add(ph);

	}

	@Override
	protected IRequestablePage getObjectPage(IModel<Institution> model, List<IModel<Institution>> list) {
		return new InstitutionPage(model, list);
	}

	protected void setUpModel() {
		super.setUpModel();

		if (!getModel().getObject().isDependencies()) {
			Optional<Institution> o_i = getInstitutionDBService().findWithDeps(getModel().getObject().getId());
			setModel(new ObjectModel<Institution>(o_i.get()));
		}

	}

	protected Panel getEditor(String id) {
		if (editor == null)
			editor = new InstitutionMainPanel(id, getModel());
		return editor;
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	protected Optional<Institution> getObject(Long id) {
		return getInstitution(id);
	}

	@Override
	protected IModel<String> getPageTitle() {
		return getLabel("institution");
	}

	@Override
	protected void onEdit(AjaxRequestTarget target) {
		editor.getInstitutionEditor().edit(target);

	}

}
