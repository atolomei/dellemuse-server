package dellemuse.serverapp.person;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.wicketstuff.annotation.mount.MountPath;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.audit.panel.AuditPanel;
import dellemuse.serverapp.editor.ObjectMarkAsDeleteEvent;
import dellemuse.serverapp.editor.ObjectRestoreEvent;
import dellemuse.serverapp.global.JumboPageHeaderPanel;
import dellemuse.serverapp.page.MultiLanguageObjectPage;
import dellemuse.serverapp.page.error.ErrorPage;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.user.UserPage;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.Language;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.record.PersonRecord;
import dellemuse.serverapp.serverdb.model.record.SiteRecord;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
import dellemuse.serverapp.serverdb.model.security.RoleInstitution;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.event.SimpleAjaxWicketEvent;
import io.wktui.event.SimpleWicketEvent;
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
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.ButtonCreateToolbarItem;
import io.wktui.nav.toolbar.DropDownMenuToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.struct.list.ListPanel;
import wktui.base.DummyBlockPanel;
import wktui.base.INamedTab;
import wktui.base.NamedTab;

/**
 * 
 * site foto Info - exhibitions
 * 
 */

@MountPath("/person/${id}")
public class PersonPage extends  MultiLanguageObjectPage<Person, PersonRecord> {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	static private Logger logger = Logger.getLogger(PersonPage.class.getName());

	private PersonEditor editor;
	private List<ToolbarItem> list;

	protected List<Language> getSupportedLanguages() {
		return  getLanguageService().getLanguages();
	}

	
	@Override
	public boolean hasAccessRight(Optional<User> ouser) {
		
		if (ouser.isEmpty())
			return false;
	
		{
			
			User user = ouser.get();  
			
			if (user.isRoot()) 
				return true;
			
			if (!user.isDependencies()) {
				user = getUserDBService().findWithDeps(user.getId()).get();
			}

			Set<RoleGeneral> set = user.getRolesGeneral();
		
			if (set!=null) {
					boolean isAccess=set.stream().anyMatch((p -> p.getKey().equals(RoleGeneral.ADMIN) || p.getKey().equals(RoleGeneral.AUDIT) ));
					if (isAccess)
						return true;
			}
		}
	
		return false;
	}
	
	
	public PersonPage() {
		super();
	}
	
	public PersonPage(PageParameters parameters) {
		super(parameters);
	}

	public PersonPage(IModel<Person> model) {
		super(model);
	}

	public PersonPage(IModel<Person> model, List<IModel<Person>> list) {
		super(model, list);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
	}

	protected void setUpModel() {
		super.setUpModel();

		if (!getModel().getObject().isDependencies()) {
			Optional<Person> o_i = getPersonDBService().findWithDeps(getModel().getObject().getId());
			setModel(new ObjectModel<Person>(o_i.get()));
		}

	}

	
	
	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	protected Optional<Person> getObject(Long id) {
		return getPerson(id);
	}

	protected Panel getAuditPanel(String id) {
		return new AuditPanel<Person>(id, getModel());
	}
	
	@Override
	protected IModel<String> getPageTitle() {
		return new Model<String>(getModel().getObject().getDisplayName());
	}

	@Override
	protected Class<?> getTranslationClass() {
		return PersonRecord.class;
	}
	@Override
	protected Panel createHeaderPanel() {

		BreadCrumb<Void> bc = createBreadCrumb();
		bc.addElement(new HREFBCElement("/person/list", getLabel("persons")));
		bc.addElement(new BCElement(new Model<String>(getModel().getObject().getFirstLastname())));

		 
		JumboPageHeaderPanel<Person> ph = new JumboPageHeaderPanel<Person>("page-header", getModel(),
				new Model<String>(getModel().getObject().getFirstLastname()));
		ph.setBreadCrumb(bc);
		
		 ph.setContext(getLabel("person-title"));
		 
		 if (getModel().getObject().getPhoto()!=null) {
			 ph.setPhotoModel(new ObjectModel<Resource>(getModel().getObject().getPhoto()));
			 ph.setHeaderCss("mb-0 pb-0 border-none");	
		
		 }
		 else {
			 ph.setIcon( Person.getIcon());
			 ph.setHeaderCss("mb-0 pb-2 border-none");	
		 }
 		 
		if (getList() != null && getList().size() > 0) {
			Navigator<Person> nav = new Navigator<Person>("navigator", getCurrent(), getList()) {
				private static final long serialVersionUID = 1L;

				@Override
				public void navigate(int current) {
					setResponsePage(new PersonPage(getList().get(current), getList()));
				}
			};
			bc.setNavigator(nav);
		}

		return(ph);
	}

	
	protected Panel getEditor(String id) {
		if (this.editor == null)
			this.editor = new PersonEditor(id, getModel());
		return this.editor;
	
	}

	@Override
	protected IRequestablePage getObjectPage(IModel<Person> model, List<IModel<Person>> list) {
		return new PersonPage(model, list);
	}

	 
	protected void onEdit(AjaxRequestTarget target) {
		this.editor.onEdit(target);
	}
 
	@Override
	protected List<ToolbarItem> getToolbarItems() {

		if (list!=null)
			return list;
		
 		list = new ArrayList<ToolbarItem>();
			
		DropDownMenuToolbarItem<Person> menu = new DropDownMenuToolbarItem<Person>("item", getModel(), Align.TOP_RIGHT);
		menu.setTitle(getLabel("person", getModel().getObject().getDisplayName()));

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Person>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Person> getItem(String id) {
				return new AjaxLinkMenuItem<Person>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						fire(new MenuAjaxEvent(ServerAppConstant.person_info, target));
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("info");
					}
				};
			}
		});

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Person>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Person> getItem(String id) {

				return new  LinkMenuItem<Person>(id, getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick() {
						fire(new SimpleWicketEvent(ServerAppConstant.person_user));
					}
					@Override
					public IModel<String> getLabel() {
						return getLabel("user");
					}
				};
			}
		});
 		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Person>() {
			private static final long serialVersionUID = 1L;
			@Override
			public MenuItemPanel<Person> getItem(String id) {
				return new SeparatorMenuItem<Person>(id, getModel());
			}
		});
		
 		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Person>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<Person> getItem(String id) {

				return new AjaxLinkMenuItem<Person>(id, getModel()) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick(AjaxRequestTarget target)  {
						fire ( new MenuAjaxEvent(ServerAppConstant.object_meta, target));
					}
					@Override
					public IModel<String> getLabel() {
						return getLabel("meta");
					}
				};
			}
		});		
 		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Person>() {
			private static final long serialVersionUID = 1L;
			@Override
			public MenuItemPanel<Person> getItem(String id) {
				return new SeparatorMenuItem<Person>(id, getModel());
			}
		});

		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<Person>() {
			private static final long serialVersionUID = 1L;
			@Override
			public MenuItemPanel<Person> getItem(String id) {
				return new AjaxLinkMenuItem<Person>(id, getModel()) {
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
		
		return list;
	}

	protected Optional<PersonRecord> loadTranslationRecord(String lang) {
		return getPersonRecordDBService().findByPerson(getModel().getObject(), lang);
	}
	
	protected PersonRecord createTranslationRecord(String lang) {
		return getPersonRecordDBService().create(getModel().getObject(), lang, getSessionUser().get());
	}

	protected void addListeners() {
		super.addListeners();
  
		add(new io.wktui.event.WicketEventListener<SimpleAjaxWicketEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(SimpleAjaxWicketEvent event) {

				if (event.getName().equals(ServerAppConstant.action_person_edit_info)) {
					PersonPage.this.onEdit(event.getTarget());
				}
				else if (event.getName().equals(ServerAppConstant.action_object_edit_record)) {
					PersonPage.this.onEditRecord(event.getTarget(), event.getMoreInfo());
				}
				else if (event.getName().equals(ServerAppConstant.person_info)) {
					PersonPage.this.togglePanel(ServerAppConstant.person_info, event.getTarget());
				}
				else if (event.getName().equals(ServerAppConstant.object_meta)) {
					PersonPage.this.togglePanel(ServerAppConstant.object_meta, event.getTarget());
				}
				else if (event.getName().startsWith(ServerAppConstant.object_translation_record_info)) {
					PersonPage.this.togglePanel(event.getName(), event.getTarget());
				}
				else if (event.getName().equals(ServerAppConstant.object_audit)) {
					PersonPage.this.togglePanel(ServerAppConstant.object_audit, event.getTarget());
				}
				else if (event.getName().equals(ServerAppConstant.action_object_edit_meta)) {
					PersonPage.this.getMetaEditor().onEdit(event.getTarget());
				}
				
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof MenuAjaxEvent)
					return true;
				return false;
			}
		});
		
		
		add(new io.wktui.event.WicketEventListener<SimpleWicketEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(SimpleWicketEvent event) {
				if (event.getName().equals(ServerAppConstant.person_user)) {
					User user= PersonPage.this.getModel().getObject().getUser();
					if (user!=null) {
						setResponsePage(new UserPage(new ObjectModel<User>( user)));
					}
					else
						setResponsePage(new ErrorPage(Model.of("user not found for person -> " + PersonPage.this.getModel().getObject().getDisplayname())));
				}
			}
			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof SimpleWicketEvent)
					return true;
				return false;
			}
		});
	}

		
	protected void onDelete(AjaxRequestTarget target) {
		getPersonDBService().markAsDeleted( getModel().getObject(), getSessionUser().get() );
		fireScanAll(new ObjectMarkAsDeleteEvent(target));
	}
	
	protected void onRestore(AjaxRequestTarget target) {
		getPersonDBService().restore( getModel().getObject(), getSessionUser().get() );
		fireScanAll(new ObjectRestoreEvent(target));
	}

	protected List<INamedTab> getInternalPanels() {

		List<INamedTab> tabs = super.createInternalPanels();

		NamedTab tab_1 = new NamedTab(Model.of("editor"), ServerAppConstant.person_info) {
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getEditor(panelId);
			}
		};
		tabs.add(tab_1);

		if (getStartTab()==null)
			setStartTab( ServerAppConstant.person_info );
		
		return tabs;
	}

}
