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
import dellemuse.serverapp.page.library.ObjectStateEnumSelector;
import dellemuse.serverapp.page.library.ObjectStateSelectEvent;
import dellemuse.serverapp.page.model.ObjectModel;

import dellemuse.serverapp.serverdb.model.DelleMuseObject;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.Institution;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.model.User;
import io.wktui.error.ErrorPanel;
import io.wktui.event.UIEvent;
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

	private WebMarkupContainer mainToolbarContainer;
	private WebMarkupContainer mainToolbar;

	private WebMarkupContainer listToolbarContainer;
	private WebMarkupContainer listToolbar;

	private boolean b_expand = false;
	private ObjectStateEnumSelector oses;

	

	protected abstract void addHeaderPanel();

	public abstract Iterable<T> getObjects();

	public abstract Iterable<T> getObjects(ObjectState os1);

	public abstract Iterable<T> getObjects(ObjectState os1, ObjectState os2);


	protected abstract IModel<String> getObjectInfo(IModel<T> model);
	protected abstract IModel<String> getObjectTitle(IModel<T> model);
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

	public void addDefaultPageHeaderPanel() {
		this.addOrReplace(new InvisiblePanel("page-header"));
	}


	public void setObjectStateEnumSelector(ObjectStateEnumSelector o) {
		this.oses = o;
	}

	public ObjectStateEnumSelector getObjectStateEnumSelector() {
		return this.oses;
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

	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<ObjectStateSelectEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(ObjectStateSelectEvent event) {

				setObjectStateEnumSelector(event.getObjectStateEnumSelector());

				logger.debug(event.toString());

				loadList();

				event.getTarget().add(ObjectListPage.this.panel);
				event.getTarget().add(ObjectListPage.this.listToolbarContainer);

			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof ObjectStateSelectEvent)
					return true;
				return false;
			}
		});

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

		try {
			add(new GlobalTopPanel("top-panel", new ObjectModel<User>(getSessionUser().get())));
			add(new GlobalFooterPanel<>("footer-panel"));

		} catch (Exception e) {
			logger.error(e);
			addOrReplace(new ErrorPanel("top-panel", e));
			addOrReplace(new InvisiblePanel("footer-panel"));
		}

		try {

			// addDefaultPageHeaderPanel();
			addHeaderPanel();
			addMainToolbar();
			addListToolbar();

		} catch (Exception e) {
			logger.error(e);
			addOrReplace(new ErrorPanel("page-header", e));
			addOrReplace(new InvisiblePanel("mainToolbarContainer"));
			addOrReplace(new InvisiblePanel("listToolbarContainer"));
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

			this.panel = new ListPanel<>("contents") {

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

					DelleMuseObjectListItemPanel<T> panel = new DelleMuseObjectListItemPanel<>("row-element", model, mode) {

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

		} catch (Exception e) {
			logger.error(e);
			addOrReplace(new ErrorPanel("contents", e));
		}
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

		this.list.forEach(c -> logger.debug(c.toString()));
	}

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
			list.forEach(t -> toolbarItems.addItem(t));
			this.mainToolbarContainer.add(toolbarItems);
		} else {
			this.mainToolbarContainer.add(new InvisiblePanel("mainToolbar"));
		}
	}

	private void addListToolbar() {

		this.listToolbarContainer = new WebMarkupContainer("listToolbarContainer") {
			private static final long serialVersionUID = 1L;

			public boolean isVisible() {
				return getMainToolbarItems() != null && getMainToolbarItems().size() > 0;
			}
		};

		this.listToolbarContainer.setOutputMarkupId(true);
		add(this.listToolbarContainer);

		List<ToolbarItem> list = getListToolbarItems();

		if (list != null && list.size() > 0) {
			Toolbar toolbarItems = new Toolbar("listToolbar");
			list.forEach(t -> toolbarItems.addItem(t));
			this.listToolbarContainer.add(toolbarItems);
		} else {
			this.listToolbarContainer.add(new InvisiblePanel("listToolbar"));
		}
	}

}
