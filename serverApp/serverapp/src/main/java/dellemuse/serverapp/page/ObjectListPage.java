package dellemuse.serverapp.page;

import java.util.ArrayList;
import java.util.List;
 
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
 
import org.apache.wicket.request.mapper.parameter.PageParameters;
 
import dellemuse.model.logging.Logger;
import dellemuse.serverapp.global.GlobalFooterPanel;
import dellemuse.serverapp.global.GlobalTopPanel;
 
import dellemuse.serverapp.page.model.ObjectModel;
 
import dellemuse.serverapp.serverdb.model.DelleMuseObject;
 
import io.wktui.nav.toolbar.Toolbar;
import io.wktui.nav.toolbar.ToolbarItem;
 
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;
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
	private Label title;

	private Link<T> create;
	private Label createLabel;
	private WebMarkupContainer createContainer;
	private WebMarkupContainer toolbarContainer;
	private WebMarkupContainer toolbar;

	 

	private boolean b_expand = false;

	protected abstract void addHeaderPanel();

	public abstract Iterable<T> getObjects();

	 
	protected abstract IModel<String> getObjectInfo(IModel<T> model);

	protected abstract IModel<String> getObjectTitle(IModel<T> model);

	protected abstract void onClick(IModel<T> model);

	protected abstract IModel<String> getPageTitle();

	protected abstract IModel<String> getListPanelLabel();

	protected abstract List<ToolbarItem> getToolbarItems();

	protected WebMarkupContainer getObjectMenu(IModel<T> model) {
		return null;
	}

	public IModel<String> getObjectSubtitle(IModel<T> model) {
		return null;
	}

	protected String getObjectImageSrc(IModel<T> model) {
		return null;
	}

	public ObjectListPage() {
		super();
	}

	public ObjectListPage(PageParameters parameters) {
		super(parameters);
	}

	public void setPageHeaderPanel(Panel panel) {
		addOrReplace(panel);
	}

	public void addDefaultPageHeaderPanel() {
		this.addOrReplace(new InvisiblePanel("page-header"));
	}

	public void onBeforeRender() {
		super.onBeforeRender();

		if (get("page-header") == null)
			this.addDefaultPageHeaderPanel();
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

	@Override
	public void onInitialize() {
		super.onInitialize();

		add(new GlobalTopPanel("top-panel"));
		add(new GlobalFooterPanel<>("footer-panel"));

		addHeaderPanel();
		addToolbar();

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

		loadList();

		this.panel = new ListPanel<>("contents", getList()) {

			private static final long serialVersionUID = 1L;

			@Override
			protected Panel getListItemExpandedPanel(IModel<T> model, ListPanelMode mode) {
				return ObjectListPage.this.getObjectListItemExpandedPanel(model, mode);
			}

			@Override
			protected Panel getListItemPanel(IModel<T> model, ListPanelMode mode) {

				ObjectListItemPanel<T> panel = new ObjectListItemPanel<>("row-element", model, mode) {

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

					protected IModel<String> getObjectTitle() {
						return ObjectListPage.this.getObjectTitle(getModel());
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
				return new Model<String>(model.getObject().getDisplayname());
			}

			@Override
			protected List<IModel<T>> filter(List<IModel<T>> initialList, String filter) {

				List<IModel<T>> list = new ArrayList<IModel<T>>();
				final String str = filter.trim().toLowerCase();
				initialList.forEach(s -> {
					if (s.getObject().getDisplayname().toLowerCase().contains(str)) {
						list.add(s);
					}
				});
				return list;
			}
		};

		this.panel.setHasExpander(this.isIsExpanded());
		this.panel.setSettings(isSettings());
		this.panel.setTitle(getListPanelLabel());
		this.panel.setListPanelMode(getListPanelMode());
		add(this.panel);
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

	protected void onCreate() {
		logger.debug("on create");
	}

	protected void loadList() {
		this.list = new ArrayList<IModel<T>>();
		getObjects().forEach(s -> this.list.add(new ObjectModel<T>(s)));

	}

	private void addToolbar() {

		this.toolbarContainer = new WebMarkupContainer("toolbarContainer") {
			private static final long serialVersionUID = 1L;

			public boolean isVisible() {
				return getToolbarItems() != null && getToolbarItems().size() > 0;
			}
		};
		add(this.toolbarContainer);

		List<ToolbarItem> list = getToolbarItems();

		if (list != null && list.size() > 0) {
			Toolbar toolbarItems = new Toolbar("toolbarItems");
			list.forEach(t -> toolbarItems.addItem(t));
			this.toolbarContainer.add(toolbarItems);
		} else {
			this.toolbarContainer.add(new InvisiblePanel("toolbarItems"));
		}
	}

}
