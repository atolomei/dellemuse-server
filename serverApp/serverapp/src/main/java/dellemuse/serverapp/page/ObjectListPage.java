package dellemuse.serverapp.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.icons.Icons;
import dellemuse.serverapp.page.library.ObjectStateEnumSelector;
import dellemuse.serverapp.page.library.ObjectStateSelectEvent;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.serverdb.model.DelleMuseObject;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.ObjectState;

import dellemuse.serverapp.serverdb.model.User;
import dellemuse.serverapp.serverdb.model.security.RoleGeneral;
import io.wktui.error.AlertHelpPanel;
import io.wktui.error.AlertPanel;
import io.wktui.error.ErrorPanel;
import io.wktui.event.CloseErrorPanelAjaxEvent;
import io.wktui.event.HelpAjaxEvent;
import io.wktui.event.UIEvent;
import io.wktui.nav.toolbar.Toolbar;
import io.wktui.nav.toolbar.ToolbarItem;

import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;
import wktui.base.DummyBlockPanel;
import wktui.base.InvisiblePanel;

/**
 * 
 * site foto Info - exhibitions "ps-0 pe-0 pt-0 pb-0 float-start w-100 toolbar"
 */

public abstract class ObjectListPage<T extends DelleMuseObject> extends BasePage {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ObjectListPage.class.getName());

	private List<IModel<T>> list;
	private ListPanel<T> panel;
	private WebMarkupContainer titleContainer;

	private WebMarkupContainer mainToolbarContainer;

	private WebMarkupContainer contentsContainerContainer;
	private WebMarkupContainer errorContainer;
	private WebMarkupContainer listToolbarContainer;
	private WebMarkupContainer helpContainer;
	
	boolean isHelpVisible = false;

	
	private Panel errorPanel;
	private boolean b_expand = false;

	private ObjectStateEnumSelector oses;

	protected abstract void addHeaderPanel();

	public abstract Iterable<T> getObjects();

	public abstract Iterable<T> getObjects(ObjectState os1);

	public abstract Iterable<T> getObjects(ObjectState os1, ObjectState os2);

	protected abstract IModel<String> getObjectInfo(IModel<T> model);

	protected IModel<String> getObjectTitle(IModel<T> model) {
		if (model.getObject() instanceof MultiLanguageObject) {
			
			StringBuilder str = new StringBuilder();
			str.append(getLanguageObjectService().getObjectDisplayName(((MultiLanguageObject) model.getObject()), getLocale()));
			
			if (model.getObject().getState() == ObjectState.DELETED)
				str.append(Icons.DELETED_ICON_HTML);
		

			if (model.getObject().getState() == ObjectState.EDITION)
				str.append(Icons.EDITION_ICON_HTML);
			
			
			return Model.of(str.toString());
		}
		return Model.of(model.getObject().getDisplayname() + ((model.getObject().getState() == ObjectState.DELETED) ? Icons.DELETED_ICON_HTML : ""));
	}

	protected abstract String getObjectTitleIcon(IModel<T> model);

	protected abstract void onClick(IModel<T> model);

	protected abstract IModel<String> getPageTitle();

	protected abstract IModel<String> getListPanelLabel();

	protected abstract List<ToolbarItem> getMainToolbarItems();

	protected abstract List<ToolbarItem> getListToolbarItems();

	public ObjectListPage() {
		super();
	}

	public ObjectListPage(PageParameters parameters) {
		super(parameters);
	}

	public void setPageHeaderPanel(Panel panel) {
		addOrReplace(panel);
	}

	public void setObjectStateEnumSelector(ObjectStateEnumSelector o) {
		this.oses = o;
	}

	public ObjectStateEnumSelector getObjectStateEnumSelector() {
		return this.oses;
	}

	protected Panel getErrorPanel() {
		return this.errorPanel;
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		
		if (getObjectStateEnumSelector()==null)
			setObjectStateEnumSelector(ObjectStateEnumSelector.EDTIION_PUBLISHED);
		
		
		helpContainer = new WebMarkupContainer("helpContainer");
		helpContainer.setOutputMarkupId(true);
		add(helpContainer);
		helpContainer.add( new InvisiblePanel("help"));
		//helpContainer.setVisible(false);
		
		
		
		contentsContainerContainer = new WebMarkupContainer("contentsContainer");
		contentsContainerContainer.setOutputMarkupId(true);
		add(contentsContainerContainer);

		errorContainer = new WebMarkupContainer("errorContainer") {
			private static final long serialVersionUID = 1L;

			public boolean isVisible() {
				return (getErrorPanel() != null) && getErrorPanel().isVisible();
			}
		};
		errorContainer.setOutputMarkupId(true);
		errorContainer.add(new InvisiblePanel("error"));
		contentsContainerContainer.add(errorContainer);

		try {
			add(new GlobalTopPanel("top-panel", new ObjectModel<User>(getSessionUser().get())));
			// super.add(new GlobalFooterPanel<>("footer-panel"));
			add(new InvisiblePanel("footer-panel"));

		} catch (Exception e) {
			logger.error(e);
			addOrReplace(new ErrorPanel("top-panel", e));
			addOrReplace(new InvisiblePanel("footer-panel"));
		}

		try {

			addHeaderPanel();

		} catch (Exception e) {
			addOrReplace(new ErrorPanel("page-header", e));
			logger.error(e);
		}

		if (!hasAccessRight(getSessionUser())) {
			addOrReplace(new InvisiblePanel("mainToolbarContainer"));
			addOrReplace(new InvisiblePanel("listToolbarContainer"));
			addOrReplace(new InvisiblePanel("titleContainer"));
			contentsContainerContainer.addOrReplace(new ErrorPanel("contents", getLabel("not-authorized")));
			return;
		}

		try {
			addMainToolbar();
			addListToolbar();

		} catch (Exception e) {
			logger.error(e);
			addOrReplace(new ErrorPanel("page-header", e));
			addOrReplace(new InvisiblePanel("mainToolbarContainer"));
			addOrReplace(new InvisiblePanel("listToolbarContainer"));
			addOrReplace(new InvisiblePanel("titleContainer"));
		}

		try {

			this.titleContainer = new WebMarkupContainer("titleContainer") {
				private static final long serialVersionUID = 1L;

				public boolean isVisible() {
					return isTitle();
				}
			};
			add(this.titleContainer);
			if (isTitle()) {
				Label title = new Label("title", getTitleLabel());
				this.titleContainer.add(title);
			} else {
				this.titleContainer.add(new InvisiblePanel("title"));

			}
		} catch (Exception e) {
			logger.error(e);
			this.titleContainer = new ErrorPanel("titleContainer", e);
			this.titleContainer.add(new InvisiblePanel("title"));
			addOrReplace(this.titleContainer);
		}

		try {

			loadList();

			this.panel = new ListPanel<T>("contents") {

				private static final long serialVersionUID = 1L;

				@Override
				public List<IModel<T>> getItems() {
					return ObjectListPage.this.getList();
				}

				@Override
				public Integer getTotalItems() {
					return Integer.valueOf(ObjectListPage.this.getList().size());
				}

				@Override
				protected Panel getListItemExpandedPanel(IModel<T> model, ListPanelMode mode) {
					return ObjectListPage.this.getObjectListItemExpandedPanel(model, mode);
				}

				@Override
				protected Panel getListItemPanel(IModel<T> model, ListPanelMode mode) {

					DelleMuseObjectListItemPanel<T> panel = new DelleMuseObjectListItemPanel<T>("row-element", model, mode) {

						private static final long serialVersionUID = 1L;

						@Override
						protected WebMarkupContainer getObjectMenu() {
							return ObjectListPage.this.getObjectMenu(getModel());
						}

						@Override
						public void onClick() {
							ObjectListPage.this.onClick(getModel());
						}

						protected IModel<String> getInfo() {
							return ObjectListPage.this.getObjectInfo(getModel());
						}

						@Override
						protected IModel<String> getObjectTitle() {
							return ObjectListPage.this.getObjectTitle(getModel());
						}

						@Override
						protected String getTitleIcon() {
							return ObjectListPage.this.getObjectTitleIcon(getModel());
						}

						protected IModel<String> getObjectSubtitle() {
							if (getMode() == ListPanelMode.TITLE)
								return null;
							return ObjectListPage.this.getObjectSubtitle(getModel());
						}

						@Override
						protected String getImageSrc() {
							return ObjectListPage.this.getObjectImageSrc(getModel());
						}

					};
					return panel;
				}

				protected void onClick(IModel<T> model) {
					ObjectListPage.this.onClick(model);

				}

				@Override
				public IModel<String> getItemLabel(IModel<T> model) {
					return ObjectListPage.this.getObjectTitle(model);
				}
			};

			this.panel.setHasExpander(this.isIsExpanded());
			this.panel.setSettings(isSettings());
			this.panel.setTitle(getListPanelLabel());
			this.panel.setListPanelMode(getListPanelMode());
			
			//this.panel.setLiveSearch(true);
			
					
			contentsContainerContainer.add(this.panel);

		} catch (Exception e) {
			logger.error(e);
			contentsContainerContainer.addOrReplace(new ErrorPanel("contents", e));
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (this.list != null)
			this.list.forEach(i -> i.detach());
	}

	public List<IModel<T>> getList() {
		return this.list;
	}

	public void setIsExpanded(boolean b) {
		this.b_expand = b;
	}

	protected boolean isIsExpanded() {
		return b_expand;
	}

	protected boolean isTitle() {
		return getTitleLabel() != null;
	}

	protected void refresh(AjaxRequestTarget target) {
		target.add(this);
		
		//target.add(this.contentsContainerContainer);
		//target.add(this.listToolbarContainer);
	}

	
	
	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<HelpAjaxEvent>() { 
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(HelpAjaxEvent event) {
				if (isHelpVisible) {
					isHelpVisible=false;
					helpContainer.get("help").setVisible(false);
				}
				else {
					//helpContainer.setVisible(true);
					helpContainer.addOrReplace(getHelpPanel("help", getHelpKey(), getLocale().getLanguage() ));
					isHelpVisible=true;
				}
				
				event.getTarget().add(helpContainer);
				//refresh( event.getTarget() );
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof HelpAjaxEvent)
					return true;
				return false;
			}
		});

		
		
		
		add(new io.wktui.event.WicketEventListener<CloseErrorPanelAjaxEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(CloseErrorPanelAjaxEvent event) {
				setErrorPanel(new InvisiblePanel("error"));
				refresh(event.getTarget());
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof CloseErrorPanelAjaxEvent)
					return true;
				return false;
			}
		});
		
		
		
		add(new io.wktui.event.WicketEventListener<ObjectStateSelectEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(ObjectStateSelectEvent event) {
				setObjectStateEnumSelector(event.getObjectStateEnumSelector());
				loadList();
				refresh(event.getTarget());
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof ObjectStateSelectEvent)
					return true;
				return false;
			}
		});

		add(new io.wktui.event.WicketEventListener<CloseErrorPanelAjaxEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(CloseErrorPanelAjaxEvent event) {
				setErrorPanel(new InvisiblePanel("error"));
				refresh(event.getTarget());
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof CloseErrorPanelAjaxEvent)
					return true;
				return false;
			}
		});

	}

	
	public String getHelpKey() {
		return this.getClass().getSimpleName().toLowerCase();
	}

	protected Panel getHelpPanel(String id, String key, String lang) {
		String h=getHelpService().gethelp(key, lang);
		if (h==null) {
			h=key + "-" + lang+ " not found";
		}
	 	AlertPanel<Void> a = new AlertHelpPanel<>(id, Model.of(h));
	 	a.add( new org.apache.wicket.AttributeModifier("class", "help"));
	 	return a;
	}
	
	protected Panel getObjectListItemExpandedPanel(IModel<T> model, ListPanelMode mode) {

		return new ObjectListItemExpandedPanel<T>("expanded-panel", model, mode) {

			private static final long serialVersionUID = 1L;

			@Override
			protected IModel<String> getInfo() {
				return ObjectListPage.this.getObjectInfo(getModel());
			}

			@Override
			protected IModel<String> getObjectSubtitle() {
				return ObjectListPage.this.getObjectSubtitle(getModel());
			}

			@Override
			protected String getImageSrc() {
				return ObjectListPage.this.getObjectImageSrc(getModel());
			}
		};
	}

	public boolean hasAccessRight(Optional<User> ouser) {
		
		if (ouser==null)
			return false;
		
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
		
		return false;
	}
	
	

	protected void setErrorPanel(Panel panel) {
		if (!panel.getId().equals("error"))
			throw new IllegalArgumentException("id must be -> error");
		errorPanel = panel;
		errorContainer.addOrReplace(errorPanel);
	}

	protected void setList(List<IModel<T>> list) {
		this.list = list;
	}

	protected boolean isSettings() {
		return true;
	}

	protected ListPanelMode getListPanelMode() {
		return ListPanelMode.TITLE;
	}

	protected IModel<String> getTitleLabel() {
		return null;
	}

	protected WebMarkupContainer getObjectMenu(IModel<T> model) {
		return null;
	}

	public IModel<String> getObjectSubtitle(IModel<T> model) {
		return null;
	}

	protected String getObjectImageSrc(IModel<T> model) {
		return null;
	}

	/**
	 * 
	 * 
	 */

	protected void loadList() {

		this.list = new ArrayList<IModel<T>>();

		if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.EDTIION_PUBLISHED)
			getObjects(ObjectState.EDITION, ObjectState.PUBLISHED).forEach(s -> this.list.add(new ObjectModel<T>(s)));

		if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.PUBLISHED)
			getObjects(ObjectState.PUBLISHED).forEach(s -> this.list.add(new ObjectModel<T>(s)));

		if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.EDITION)
			getObjects(ObjectState.EDITION).forEach(s -> this.list.add(new ObjectModel<T>(s)));

		else if (this.getObjectStateEnumSelector() == null)
			getObjects().forEach(s -> this.list.add(new ObjectModel<T>(s)));

		else if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.ALL)
			getObjects().forEach(s -> this.list.add(new ObjectModel<T>(s)));

		else if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.DELETED)
			getObjects(ObjectState.DELETED).forEach(s -> this.list.add(new ObjectModel<T>(s)));
	}

	/**
	 * 
	 * 
	 */

	private void addMainToolbar() {

		this.mainToolbarContainer = new WebMarkupContainer("mainToolbarContainer") {
			private static final long serialVersionUID = 1L;

			public boolean isVisible() {
				return getMainToolbarItems() != null && getMainToolbarItems().size() > 0;
			}
		};

		add(this.mainToolbarContainer);

		List<ToolbarItem> list = getMainToolbarItems();

		if (list != null && list.size() > 0) {
			Toolbar toolbarItems = new Toolbar("mainToolbar");
			toolbarItems.setToolbarCss("navbar pb-1 pt-1 mt-0 mb-0");
			list.forEach(t -> toolbarItems.addItem(t));
			this.mainToolbarContainer.add(toolbarItems);
		} else {
			this.mainToolbarContainer.add(new InvisiblePanel("mainToolbar"));
		}
	}

	/**
	 * 
	 * 
	 */

	private void addListToolbar() {

		this.listToolbarContainer = new WebMarkupContainer("listToolbarContainer") {
			private static final long serialVersionUID = 1L;

			public boolean isVisible() {
				boolean b = (getListToolbarItems() != null) && (getListToolbarItems().size() > 0);
				return b;
			}
		};

		this.listToolbarContainer.setOutputMarkupId(true);
		add(this.listToolbarContainer);

		List<ToolbarItem> list = getListToolbarItems();

		if (list != null && list.size() > 0) {
			Toolbar toolbarItems = new Toolbar("listToolbar");
			toolbarItems.setToolbarCss("navbar pb-1 pt-1 mt-0 mb-0");
			list.forEach(t -> toolbarItems.addItem(t));
			this.listToolbarContainer.add(toolbarItems);
		} else {
			this.listToolbarContainer.add(new InvisiblePanel("listToolbar"));
		}
	}

}
