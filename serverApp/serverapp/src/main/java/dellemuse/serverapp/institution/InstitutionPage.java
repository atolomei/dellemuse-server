package dellemuse.serverapp.institution;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.audit.panel.AuditPanel;
import dellemuse.serverapp.editor.ObjectMarkAsDeleteEvent;
import dellemuse.serverapp.editor.ObjectRestoreEvent;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.help.Help;
import dellemuse.serverapp.help.HelpButtonToolbarItem;
import dellemuse.serverapp.page.MultiLanguageObjectPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.site.SitePage;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.InstitutionRecord;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
import dellemuse.serverapp.serverdb.model.security.RoleInstitution;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.event.SimpleAjaxWicketEvent;
import io.wktui.event.UIEvent;
import io.wktui.model.TextCleaner;
import io.wktui.nav.breadcrumb.BCElement;
import io.wktui.nav.breadcrumb.BreadCrumb;
import io.wktui.nav.breadcrumb.HREFBCElement;
import io.wktui.nav.breadcrumb.Navigator;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.LinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.SeparatorMenuItem;
import io.wktui.nav.menu.TitleMenuItem;

import io.wktui.nav.toolbar.DropDownMenuToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import wktui.base.INamedTab;
import wktui.base.NamedTab;

/**
 * site foto info - exhibitions
 */
@AuthorizeInstantiation({"ROLE_USER"})

@MountPath("/institution/${id}")
public class InstitutionPage extends MultiLanguageObjectPage<Institution, InstitutionRecord> {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(InstitutionPage.class.getName());

	private InstitutionMainPanel editor;
	private List<ToolbarItem> list;

	
	protected List<Language> getSupportedLanguages() {
		return  getLanguageService().getLanguages();
	}

	
	@Override
	public boolean hasAccessRight(Optional<User> ouser) {

		if (ouser.isEmpty())
			return false;

		User user = ouser.get();

		if (user.isRoot())
			return true;

		if (!user.isDependencies()) {
			user = getUserDBService().findWithDeps(user.getId()).get();
		}

		{
			Set<RoleGeneral> set = user.getRolesGeneral();

			if (set != null) {
				boolean isAccess = set.stream().anyMatch((p -> p.getKey().equals(RoleGeneral.ADMIN) || p.getKey().equals(RoleGeneral.AUDIT)));
				if (isAccess)
					return true;
			}
		}

		{
			final Long iid = getModel().getObject().getId();

			Set<RoleInstitution> set = user.getRolesInstitution();

			if (set != null) {
				boolean isAccess = set.stream().anyMatch((p -> p.getInstitution().getId().equals(iid) && (p.getKey().equals(RoleInstitution.ADMIN))));
				if (isAccess)
					return true;
			}
		}

		return false;
	}

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


	public String getHelpKey() {
		return Help.INSTITUTION_INFO;
	}
	
	
	@Override
	protected boolean isLanguage() {
		return false;
	}

	protected Optional<InstitutionRecord> loadTranslationRecord(String lang) {
		return getInstitutionRecordDBService().findByInstitution(getModel().getObject(), lang);
	}

	protected InstitutionRecord createTranslationRecord(String lang) {
		return getInstitutionRecordDBService().create(getModel().getObject(), lang, getSessionUser().get());
	}

	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<SimpleAjaxWicketEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(SimpleAjaxWicketEvent event) {

				logger.debug(event.toString());

				// ------
				// action
				// ------

				if (event.getName().equals(ServerAppConstant.action_institution_edit)) {
					InstitutionPage.this.onEdit(event.getTarget());
				}

				else if (event.getName().equals(ServerAppConstant.action_object_edit_meta)) {
					InstitutionPage.this.onEditMeta(event.getTarget());
				}

				else if (event.getName().equals(ServerAppConstant.action_object_edit_record)) {
					InstitutionPage.this.onEditRecord(event.getTarget(), event.getMoreInfo());
				}

				// ------
				// panel
				// ------

				else if (event.getName().equals(ServerAppConstant.institution_info)) {
					InstitutionPage.this.togglePanel(ServerAppConstant.institution_info, event.getTarget());
				}

				// else if
				// (event.getName().startsWith(ServerAppConstant.institutionrecord_info)) {
				// InstitutionPage.this.togglePanel(event.getName(), event.getTarget());
				// }

				else if (event.getName().equals(ServerAppConstant.institution_users_panel)) {
					InstitutionPage.this.togglePanel(ServerAppConstant.institution_users_panel, event.getTarget());
				}

				else if (event.getName().startsWith(ServerAppConstant.object_translation_record_info)) {

					InstitutionPage.this.togglePanel(event.getName(), event.getTarget());

				}

				else if (event.getName().equals(ServerAppConstant.object_meta)) {
					InstitutionPage.this.togglePanel(ServerAppConstant.object_meta, event.getTarget());
					// ArtExhibitionPage.this.getHeader().setPhotoVisible(true);
					// event.getTarget().add(ArtWorkPage.this.getHeader());
				}

				else if (event.getName().equals(ServerAppConstant.object_audit)) {
					InstitutionPage.this.togglePanel(ServerAppConstant.object_audit, event.getTarget());
				}

			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof MenuAjaxEvent)
					return true;
				return false;
			}
		});
	}

	protected void onDelete(AjaxRequestTarget target) {
		getInstitutionDBService().markAsDeleted(getModel().getObject(), getSessionUser().get());
		fireScanAll(new ObjectMarkAsDeleteEvent(target));
	}

	protected void onRestore(AjaxRequestTarget target) {
		getInstitutionDBService().restore(getModel().getObject(), getSessionUser().get());
		fireScanAll(new ObjectRestoreEvent(target));
	}

	@Override
	protected List<ToolbarItem> getToolbarItems() {

		if (list != null)
			return list;

		list = new ArrayList<ToolbarItem>();

		DropDownMenuToolbarItem<Institution> menu = new DropDownMenuToolbarItem<Institution>("item", getModel(), Align.TOP_RIGHT);
		menu.setTitle(Model.of(TextCleaner.truncate(getModel().getObject().getName(), 24) + " (Inst)"));

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Institution>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Institution> getItem(String id) {
				return new TitleMenuItem<Institution>(id) {

					private static final long serialVersionUID = 1L;

					@Override
					public IModel<String> getLabel() {
						return getLabel("information");
					}
				};
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
						fire(new MenuAjaxEvent(ServerAppConstant.institution_info, target));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("institution-info");
					}
				};
			}
		});

		/**
		for (Lang uage la : getLanguageService().getLan guages()) {

			final String langCode = la.getLanguageCode();

			if (!langCode.equals(getModel().getObject().getMasterLanguage())) {

				menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Institution>() {

					private static final long serialVersionUID = 1L;

					@Override
					public MenuItemPanel<Institution> getItem(String id) {

						return new AjaxLinkMenuItem<Institution>(id, getModel()) {
							private static final long serialVersionUID = 1L;

							@Override
							public void onClick(AjaxRequestTarget target) {
								fire(new MenuAjaxEvent(ServerAppConstant.object_translation_record_info + "-" + langCode, target));
							}

							@Override
							public IModel<String> getLabel() {
								return getLabel("institution-record", langCode);
							}
						};
					}
				});
			}
		}**/
		

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
						fire(new MenuAjaxEvent(ServerAppConstant.institution_users_panel, target));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("users");
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

				return new AjaxLinkMenuItem<Institution>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						fire(new MenuAjaxEvent(ServerAppConstant.object_meta, target));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("institution-meta");
					}
				};
			}
		});

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Institution>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Institution> getItem(String id) {
				return new io.wktui.nav.menu.SeparatorMenuItem<Institution>(id);
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

		getSites(getModel().getObject()).forEach(site -> {

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
							setResponsePage(new SitePage(new ObjectModel<Site>(InstitutionPage.this.getSite(siteId).get())));
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
						fire(new MenuAjaxEvent(ServerAppConstant.object_audit, target));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("audit");
					}
				};
			}
		});

		list.add(menu);

		
		HelpButtonToolbarItem h = new HelpButtonToolbarItem("item",  Align.TOP_RIGHT);
		list.add(h);
		
		
		return list;
	}

	@Override
	protected List<INamedTab> getInternalPanels() {

		List<INamedTab> tabs = super.createInternalPanels();

		NamedTab tab_1 = new NamedTab(Model.of("editor"), ServerAppConstant.institution_info) {

			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getEditor(panelId);
			}
		};
		tabs.add(tab_1);

		NamedTab tab_2 = new NamedTab(Model.of("users"), ServerAppConstant.institution_users_panel) {

			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getUsersPanel(panelId);
			}
		};
		tabs.add(tab_2);

		if (getStartTab() == null)
			setStartTab(ServerAppConstant.institution_info);

		return tabs;
	}

	protected Panel getAuditPanel(String id) {
		return new AuditPanel<Institution>(id, getModel());
	}

	protected IModel<String> getMainTitle() {
		return getObjectTitle(getModel().getObject());
	}

	protected IModel<String> getMainSubtitle() {

		return getObjectSubtitle(getModel().getObject());
		/**
		 * String masterLang = getModel().getObject().getMasterLanguage(); String
		 * userLang = getSessionUser().get().getLanguage();
		 * 
		 * if (masterLang==null || userLang==null) { return null; }
		 * 
		 * 
		 * if (masterLang.equals(userLang)) { if
		 * (getModel().getObject().getSubtitle()!=null) return
		 * Model.of(getModel().getObject().getSubtitle()); else return null; }
		 * 
		 * 
		 * StringBuilder str = new StringBuilder();
		 * 
		 * 
		 * return new Model<String>(getModel().getObject().getDisplayname());
		 */
	}

	@Override
	protected Panel createHeaderPanel() {

		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(new HREFBCElement("/institution/list", getLabel("institutions")));
		bc.addElement(new BCElement(getObjectTitle(getModel().getObject())));

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

		JumboPageHeaderPanel<Institution> ph = new JumboPageHeaderPanel<Institution>("page-header", getModel(), getMainTitle());

		StringBuilder str = new StringBuilder();

		if (!getModel().getObject().getMasterLanguage().equals(getSessionUser().get().getLanguage())) {

			str.append(getModel().getObject());
		}

		if (getModel().getObject().getSubtitle() != null) {
			ph.setTagline(Model.of(getModel().getObject().getSubtitle()));
		}

		if (getModel().getObject().getPhoto() != null)
			ph.setPhotoModel(new ObjectModel<Resource>(getModel().getObject().getPhoto()));
		else
			ph.setIcon(Institution.getIcon());

		ph.setBreadCrumb(bc);

		ph.setContext(getLabel("institution"));

		return ph;

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

	InstitutionUsersPanel iup;

	protected Panel getUsersPanel(String id) {
		if (iup == null)
			iup = new InstitutionUsersPanel(id, getModel());
		return iup;
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
	protected Class<?> getTranslationClass() {
		return InstitutionRecord.class;
	}

	protected void onEdit(AjaxRequestTarget target) {
		editor.getInstitutionEditor().edit(target);
	}

	protected void onEditMeta(AjaxRequestTarget target) {
		super.getMetaEditor().edit(target);
	}

	//protected void onEditRecord(AjaxRequestTarget target, String lang) {
	//	getRecordEditors().get(lang).edit(target);
	//}

}
