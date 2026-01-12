package dellemuse.serverapp.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.editor.ObjectMarkAsDeleteEvent;
import dellemuse.serverapp.editor.ObjectMetaEditor;
import dellemuse.serverapp.editor.ObjectRestoreEvent;
import dellemuse.serverapp.editor.ObjectUpdateEvent;
import dellemuse.serverapp.global.GlobalFooterPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
import dellemuse.serverapp.page.library.ObjectStateEnumSelector;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.DelleMuseObject;
import dellemuse.serverapp.serverdb.model.MultiLanguageObject;
import dellemuse.serverapp.serverdb.model.User;

import io.odilon.util.Check;

import io.wktui.error.ErrorPanel;

import io.wktui.event.UIEvent;
import io.wktui.nav.listNavigator.ListNavigator;
import io.wktui.nav.toolbar.Toolbar;
import io.wktui.nav.toolbar.ToolbarItem;
import wktui.base.INamedTab;
import wktui.base.InvisiblePanel;
import wktui.base.NamedTab;

/**
 * site foto Info - exhibitions
 */

public abstract class ObjectPage<T extends DelleMuseObject> extends BasePage {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ObjectPage.class.getName());

	private StringValue stringValue;
	private IModel<T> model;

	private List<IModel<T>> list;
	private int current = 0;

	private WebMarkupContainer mainMarkupContainer;

	private WebMarkupContainer navigatorContainer;
	private WebMarkupContainer internalPanelContainer;
	private int currentIndex = 0;
	private WebMarkupContainer toolbarContainer;

	private List<INamedTab> ipanels;
	private List<INamedTab> tabs;
	private WebMarkupContainer currentPanel;
	private ObjectMetaEditor<T> metaEditor;

	private String startingTab = null;

	private Panel pageHeader;
	private Panel globalSearch;

	protected abstract Optional<T> getObject(Long id);

	protected abstract IModel<String> getPageTitle();

	protected abstract IRequestablePage getObjectPage(IModel<T> iModel, List<IModel<T>> list);

	protected abstract List<INamedTab> getInternalPanels();

	protected abstract List<ToolbarItem> getToolbarItems();

	protected abstract Panel createHeaderPanel();

	public boolean hasAccessRight(Optional<User> ouser) {
		return true;
	}

	public ObjectPage() {
		super();
	}

	public ObjectPage(PageParameters parameters) {
		super(parameters);
		stringValue = getPageParameters().get("id");
	}

	public ObjectPage(IModel<T> model) {
		setModel(model);
		getPageParameters().add("id", model.getObject().getId().toString());
	}

	public ObjectPage(IModel<T> model, List<IModel<T>> list) {
		Check.requireNonNullArgument(model, "model is null");
		Check.requireTrue(model.getObject() != null, "modelOjbect is null");
		setModel(model);
		setList(list);
		getPageParameters().add("id", model.getObject().getId().toString());
	}

	public IModel<T> getModel() {
		return this.model;
	}

	public void setModel(IModel<T> model) {
		this.model = model;
	}

	public List<IModel<T>> getList() {
		return list;
	}

	public void setList(List<IModel<T>> siteList) {
		this.list = siteList;
	}
	private ObjectStateEnumSelector oses;

	public void setObjectStateEnumSelector(ObjectStateEnumSelector o) {
		this.oses = o;
	}

	public ObjectStateEnumSelector getObjectStateEnumSelector() {
		return this.oses;
	}
	
	public void setPageHeaderPanel(Panel panel) {
		addOrReplace(panel);
	}

	public void addDefaultPageHeaderPanel() {
		this.addOrReplace(new InvisiblePanel("page-header"));
	}

	@Override
	public void onBeforeRender() {
		super.onBeforeRender();

	}

	public void setStartTab(String tabName) {
		this.startingTab = tabName;
	}

	public String getStartTab() {
		return this.startingTab;
	}

	public void togglePanel(String name, AjaxRequestTarget target) {
		togglePanel(getTab(name), target);
	}

	public void togglePanel(int panelOrder, AjaxRequestTarget target) {

		if (this.currentIndex == panelOrder) {
			target.add(this.toolbarContainer);
			target.add(this.internalPanelContainer);
			return;
		}

		this.currentIndex = panelOrder;
		this.currentPanel = getIPanels().get(getCurrentIndex()).getPanel("internalPanel");
		this.internalPanelContainer.addOrReplace(this.currentPanel);

		addToolbar();

		target.add(this.toolbarContainer);
		target.add(this.internalPanelContainer);
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (getList() != null)
			getList().forEach(i -> i.detach());
	}

	public MarkupContainer add(final Component c) {
		mainMarkupContainer.add(c);
		return this;
	}

	public MarkupContainer addOrReplace(final Component c) {
		mainMarkupContainer.addOrReplace(c);
		return this;
	}

	protected IModel<String> getMainClass() {
		return Model.of("eeeeeeee");
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		mainMarkupContainer = new WebMarkupContainer("mainContainer");
		mainMarkupContainer.add(AttributeModifier.replace("class", getMainClass()));

		super.addOrReplace(mainMarkupContainer);

		try {

			setUpModel();

		} catch (Exception e) {
			logger.error(e);
			addErrorPanels(e);
			return;
		}

		addGlobalSearch();

		super.add(createGlobalTopPanel("top-panel"));
		// super.add(new GlobalFooterPanel<>("footer-panel"));

		super.add(new InvisiblePanel("footer-panel"));

		this.toolbarContainer = new WebMarkupContainer("toolbarContainer") {
			private static final long serialVersionUID = 1L;

			public boolean isVisible() {
				return getToolbarItems() != null && getToolbarItems().size() > 0;
			}
		};
		this.toolbarContainer.setOutputMarkupId(true);
		add(this.toolbarContainer);

		internalPanelContainer = new WebMarkupContainer("internalPanelContainer");
		internalPanelContainer.setOutputMarkupId(true);
		add(internalPanelContainer);

		setCurrent();
		initHeaderPanel();

		if (!this.hasAccessRight(getSessionUser())) {
			this.toolbarContainer.addOrReplace(new InvisiblePanel("toolbarItems"));
			addOrReplace(new InvisiblePanel("navigatorContainer"));
			internalPanelContainer.add(new ErrorPanel("internalPanel", getLabel("not-authorized")));
			return;

		}

		addNavigator();

		tabs = getIPanels();

		if (this.startingTab != null) {
			int tabOrder = getTab(this.startingTab);
			this.currentIndex = tabOrder;
		}
		currentPanel = tabs.get(getCurrentIndex()).getPanel("internalPanel");
		internalPanelContainer.add(currentPanel);

		addToolbar();
	}

	protected Panel createGlobalTopPanel(String id) {
		return new GlobalTopPanel("top-panel", new ObjectModel<User>(getSessionUser().get()));
	}

	protected int getCurrentIndex() {
		return this.currentIndex;
	}

	protected WebMarkupContainer getCurrentPanel() {
		return this.currentPanel;
	}

	protected List<INamedTab> getTabs() {
		return this.tabs;
	}

	protected ObjectMetaEditor<T> getMetaEditor() {
		return this.metaEditor;
	}

	protected abstract Panel createSearchPanel();

	protected void addGlobalSearch() {

		try {
			Panel panel = createSearchPanel();
			if (panel == null)
				this.globalSearch = new InvisiblePanel("globalSearch");
			else
				this.globalSearch = panel;
			addOrReplace(this.globalSearch);
		} catch (Exception e) {
			logger.error(e);
			addOrReplace(new ErrorPanel("globalSearch", e));
		}

	}

	protected void initHeaderPanel() {
		try {
			Panel panel = createHeaderPanel();
			if (panel == null)
				pageHeader = new InvisiblePanel("page-header");
			else
				pageHeader = panel;

			addOrReplace(pageHeader);

		} catch (Exception e) {
			logger.error(e);
			addOrReplace(new ErrorPanel("page-header", e));
		}
	}

	protected void setUpModel() {
		if (getModel() == null) {
			if (this.stringValue != null) {
				Optional<T> o = getObject(Long.valueOf(this.stringValue.toLong()));
				if (o.isPresent()) {
					setModel(new ObjectModel<T>(o.get()));
				}
			}
		}

		if (getModel() == null)
			throw new IllegalStateException("ObjectModel is null");
	}

	protected int getCurrent() {
		return this.current;
	}

	protected Panel getHeaderPanel() {
		return this.pageHeader;
	}

	protected void setCurrent() {

		if (this.getList() == null)
			return;

		if (getModel() == null)
			return;

		int n = 0;

		for (IModel<T> m : this.getList()) {
			if (getModel().getObject().getId().equals(m.getObject().getId())) {
				current = n;

				break;
			}
			n++;
		}
	}

	protected void addErrorPanels(Exception e) {

		if (getSessionUser().isPresent())
			addOrReplace(new GlobalTopPanel("top-panel", new ObjectModel<User>(getSessionUser().get())));
		else
			addOrReplace(new GlobalTopPanel("top-panel"));

		addOrReplace(new InvisiblePanel("page-header"));
		addOrReplace(new InvisiblePanel("toolbarContainer"));

		this.internalPanelContainer = new WebMarkupContainer("internalPanelContainer");
		addOrReplace(this.internalPanelContainer);

		this.internalPanelContainer.add(new ErrorPanel("internalPanel", e));

		addOrReplace(new InvisiblePanel("navigatorContainer"));
		addOrReplace(new GlobalFooterPanel<>("footer-panel"));
	}

	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<ObjectUpdateEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(ObjectUpdateEvent event) {
				setUpModel();
				initHeaderPanel();
				event.getTarget().add(pageHeader);
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof ObjectUpdateEvent)
					return true;
				return false;
			}
		});

		add(new io.wktui.event.WicketEventListener<ObjectRestoreEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(ObjectRestoreEvent event) {
				setUpModel();
				initHeaderPanel();
				event.getTarget().add(pageHeader);
				event.getTarget().add(toolbarContainer);
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof ObjectRestoreEvent)
					return true;
				return false;
			}
		});

		add(new io.wktui.event.WicketEventListener<ObjectMarkAsDeleteEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(ObjectMarkAsDeleteEvent event) {
				setUpModel();
				initHeaderPanel();
				event.getTarget().add(pageHeader);
				event.getTarget().add(toolbarContainer);
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof ObjectMarkAsDeleteEvent)
					return true;
				return false;
			}
		});
	}

	protected Panel getMetaEditor(String id) {
		if (this.metaEditor == null) {
			metaEditor = new ObjectMetaEditor<T>(id, getModel()) {

				public boolean isEditEnabled() {
					return ObjectPage.this.isMetaEditEnabled();
				}

			};
			metaEditor.setLanguage(ObjectPage.this.isLanguage());
			metaEditor.setAudioAutoGenerate(ObjectPage.this.isAudioAutoGenerate());
		}

		return (metaEditor);
	}

	protected boolean isMetaEditEnabled() {
		return true;
	}

	protected boolean isLanguage() {
		return getModel().getObject() instanceof MultiLanguageObject;
	}

	protected boolean isAudioAutoGenerate() {
		return false;
	}

	protected List<INamedTab> createInternalPanels() {

		List<INamedTab> tabs = new ArrayList<INamedTab>();
		NamedTab tab_2 = new NamedTab(Model.of("metainfo"), ServerAppConstant.object_meta) {
			private static final long serialVersionUID = 1L;

			@Override
			public WebMarkupContainer getPanel(String panelId) {
				return getMetaEditor(panelId);
			}
		};
		tabs.add(tab_2);
		return tabs;
	}

	private int getTab(String name) {

		List<INamedTab> tabs = getIPanels();

		int current = 0;
		int selected = 0;
		for (INamedTab tab : tabs) {
			if (tab.getName().equals(name)) {
				// logger.debug("Selected title -> " + tab.getTitle().getObject().toString());
				selected = current;
				break;
			}
			current++;
		}

		return selected;
	}

	private void addToolbar() {

		try {
			WebMarkupContainer wCurrent = getCurrentPanel();

			Toolbar toolbarItems = new Toolbar("toolbarItems");
			List<ToolbarItem> list = new ArrayList<ToolbarItem>();
			List<ToolbarItem> localItems = null;
			List<ToolbarItem> globalItems = getToolbarItems();

			if (wCurrent instanceof InternalPanel)
				localItems = ((InternalPanel) wCurrent).getToolbarItems();

			if (localItems != null)
				list.addAll(localItems);

			if (globalItems != null)
				list.addAll(globalItems);

			if (list.size() > 0) {
				list.forEach(t -> toolbarItems.addItem(t));
				this.toolbarContainer.addOrReplace(toolbarItems);
			} else {
				this.toolbarContainer.addOrReplace(new InvisiblePanel("toolbarItems"));
			}
		} catch (Exception e) {
			logger.error(e);
			this.toolbarContainer.addOrReplace(new ErrorPanel("toolbarItems", e));
		}
	}

	private void addNavigator() {

		this.navigatorContainer = new WebMarkupContainer("navigatorContainer");
		add(this.navigatorContainer);

		try {
			this.navigatorContainer.setVisible(getList() != null && getList().size() > 0);

			if (getList() != null) {
				ListNavigator<T> nav = new ListNavigator<T>("navigator", this.current, getList()) {
					private static final long serialVersionUID = 1L;

					@Override
					protected IModel<String> getLabel(IModel<T> model) {
						return new Model<String>(model.getObject().getDisplayname());
					}

					@Override
					protected void navigate(int current) {
						setResponsePage(getObjectPage(getList().get(current), getList()));
					}
				};
				this.navigatorContainer.add(nav);
			} else {
				this.navigatorContainer.add(new InvisiblePanel("navigator"));
			}
		} catch (Exception e) {
			logger.error(e);
			this.navigatorContainer.add(new ErrorPanel("navigator", e));
		}
	}

	private List<INamedTab> getIPanels() {
		if (ipanels == null)
			ipanels = getInternalPanels();
		return ipanels;
	}

	public Panel getGlobalSearch() {
		return globalSearch;
	}

	public void setGlobalSearch(Panel globalSearch) {
		this.globalSearch = globalSearch;
	}

}
