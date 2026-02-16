package dellemuse.serverapp.artexhibition;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.model.logging.Logger;

import dellemuse.serverapp.artexhibitionitem.ArtExhibitionItemPage;
import dellemuse.serverapp.page.DelleMuseObjectListItemPanel;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.MultipleSelectorPanel;
import dellemuse.serverapp.page.ObjectListItemExpandedPanel;
import dellemuse.serverapp.page.library.ObjectStateEnumSelector;
import dellemuse.serverapp.page.library.ObjectStateListSelector;
import dellemuse.serverapp.page.library.ObjectStateSelectEvent;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.site.SiteArtWorkListPage;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;

import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.ArtExhibitionDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.event.UIEvent;
import io.wktui.form.FormState;
import io.wktui.model.TextCleaner;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.toolbar.Toolbar;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.panel.SimpleHelpPanel;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;
import wktui.base.InvisiblePanel;

public class ArtExhibitionItemsPanel extends DBModelPanel<ArtExhibition> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ArtExhibitionItemsPanel.class.getName());

	private IModel<Site> siteModel;
	private FormState state = FormState.VIEW;

	private List<IModel<ArtWork>> list;
	private List<IModel<ArtExhibitionItem>> selected;

	private MultipleSelectorPanel<ArtWork> multipleSeletor;

	private AjaxLink<Void> add;
	private AjaxLink<Void> close;

	private WebMarkupContainer addContainerButtons;
	private WebMarkupContainer listToolbarContainer;

	private ListPanel<ArtExhibitionItem> selectedPanel;

	private List<ToolbarItem> listToolbar;
	private List<ToolbarItem> t_list = new ArrayList<ToolbarItem>();

	private ObjectStateEnumSelector oses;

	public ArtExhibitionItemsPanel(String id, ObjectStateEnumSelector oses, IModel<ArtExhibition> model, IModel<Site> siteModel) {
		super(id, model);
		this.siteModel = siteModel;
		this.oses=oses;
		setOutputMarkupId(true);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();

		addListToolbar();
		addSelectedPanel();
		addMultipleSelector();
		
		SimpleHelpPanel<Void> s= new SimpleHelpPanel<Void>("helpPanel");
		add(s);
	
		s.setLinkLabel(getLabel("help"));
		s.setHelpText(getLabel("help-text", "/site/artwork/"+getSiteModel().getObject().getId().toString()));
		
		
				
		add ( new Link<Void>("siteArtworks") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				setResponsePage( new SiteArtWorkListPage( getSiteModel()));
			}
			
		});
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (this.selected != null)
			this.selected.forEach(t -> t.detach());

		if (siteModel != null)
			siteModel.detach();

		if (list != null)
			this.list.forEach(t -> t.detach());
	}

	protected IModel<String> getObjectInfo(IModel<ArtExhibitionItem> model) {
		if (!model.getObject().isDependencies())
			model.setObject(super.findArtExhibitionItemWithDeps(model.getObject().getId()).get());
		return Model.of(TextCleaner.clean(getInfo(model.getObject())));
	}

	protected IModel<String> getObjectSubtitle(IModel<ArtExhibitionItem> model) {
		return null;
	}

	protected String getObjectImageSrc(IModel<ArtExhibitionItem> model) {
		return super.getImageSrc(model.getObject());
	}

	protected IModel<String> getObjectTitle(IModel<ArtExhibitionItem> model) {
		return super.getObjectTitle(model.getObject());
	}

	@Override
	public List<ToolbarItem> getToolbarItems() {
		return t_list;
	}

	public void setObjectStateEnumSelector(ObjectStateEnumSelector o) {
		this.oses = o;
	}

	public ObjectStateEnumSelector getObjectStateEnumSelector() {
		return this.oses;
	}

	@Override
	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<ObjectStateSelectEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(ObjectStateSelectEvent event) {
				setObjectStateEnumSelector(event.getObjectStateEnumSelector());
				loadList();
				event.getTarget().add(ArtExhibitionItemsPanel.this.selectedPanel);
				event.getTarget().add(ArtExhibitionItemsPanel.this.listToolbarContainer);
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof ObjectStateSelectEvent)
					return true;
				return false;
			}
		});

	}

	protected synchronized void loadList() {

		this.selected = new ArrayList<IModel<ArtExhibitionItem>>();

		if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.EDTIION_PUBLISHED)
			getObjects(ObjectState.EDITION, ObjectState.PUBLISHED).forEach(s -> this.selected.add(new ObjectModel<ArtExhibitionItem>(s)));

		if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.PUBLISHED)
			getObjects(ObjectState.PUBLISHED).forEach(s -> this.selected.add(new ObjectModel<ArtExhibitionItem>(s)));

		if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.EDITION)
			getObjects(ObjectState.EDITION).forEach(s -> this.selected.add(new ObjectModel<ArtExhibitionItem>(s)));

		else if (this.getObjectStateEnumSelector() == null)
			getObjects().forEach(s -> this.selected.add(new ObjectModel<ArtExhibitionItem>(s)));

		else if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.ALL)
			getObjects().forEach(s -> this.selected.add(new ObjectModel<ArtExhibitionItem>(s)));

		else if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.DELETED)
			getObjects(ObjectState.DELETED).forEach(s -> this.selected.add(new ObjectModel<ArtExhibitionItem>(s)));

		 
	}

	protected Panel getObjectListItemExpandedPanel(IModel<ArtExhibitionItem> model, ListPanelMode mode) {

		model.setObject(super.findArtExhibitionItemWithDeps(model.getObject().getId()).get());

		return new ObjectListItemExpandedPanel<ArtExhibitionItem>("expanded-panel", model, mode) {

			private static final long serialVersionUID = 1L;

			@Override
			protected IModel<String> getInfo() {
				return ArtExhibitionItemsPanel.this.getObjectInfo(getModel());
			}

			@Override
			protected IModel<String> getObjectSubtitle() {
				return ArtExhibitionItemsPanel.this.getObjectSubtitle(getModel());
			}

			@Override
			protected String getImageSrc() {
				return ArtExhibitionItemsPanel.this.getObjectImageSrc(getModel());
			}

		};
	}

	protected void onObjectRemove(IModel<ArtExhibitionItem> model, AjaxRequestTarget target) {

		ArtExhibitionItem item = model.getObject();
		ArtExhibition ex = getModel().getObject();

		super.removeItem(ex, item, getUserDBService().findRoot());
		resetSelected();
		target.add(this.selectedPanel);
	}

	protected void onObjectSelect(IModel<ArtWork> model, AjaxRequestTarget target) {
		ArtWork item = model.getObject();
		ArtExhibition ex = getModel().getObject();
		super.addItem(ex, item, getUserDBService().findRoot());
		resetSelected();
		target.add(this.selectedPanel);
	}

	private void resetSelected() {
		this.selected = null;
	}

	
	protected void refresh(AjaxRequestTarget target) {
		target.add(this.selectedPanel);
	}
	
	
	protected List<IModel<ArtWork>> getArtWorks() {

		if (list != null)
			return list;

		list = new ArrayList<IModel<ArtWork>>();
		getSiteArtWorks(this.siteModel.getObject()).forEach(i -> list.add(new ObjectModel<ArtWork>(i)));

		return list;
	}

	protected WebMarkupContainer getMenu(IModel<ArtExhibitionItem> model) {
		NavDropDownMenu<ArtExhibitionItem> menu = new NavDropDownMenu<ArtExhibitionItem>("menu", model, null) {
			private static final long serialVersionUID = 1L;

			public boolean isVisible() {
				return true;
			}
		};

		menu.setOutputMarkupId(true);

		menu.setTitleCss("d-block-inline d-sm-block-inline d-md-block-inline d-lg-none d-xl-none d-xxl-none ps-1 pe-1");
		menu.setIconCss("fa-solid fa-ellipsis d-block-inline d-sm-block-inline d-md-block-inline d-lg-block-inline d-xl-block-inline d-xxl-block-inline ps-1 pe-1");

		
		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibitionItem>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibitionItem> getItem(String id) {

				return new AjaxLinkMenuItem<ArtExhibitionItem>(id) {

					private static final long serialVersionUID = 1L;

					
					public boolean isEnabled() {
						return getModel().getObject().getState()!=ObjectState.PUBLISHED;
					}
				
					@Override
					public void onClick(AjaxRequestTarget target) {
						getModel().getObject().setState(ObjectState.PUBLISHED);
						getArtExhibitionItemDBService().save(getModel().getObject(), ObjectState.PUBLISHED.getLabel(), getSessionUser().get());
						target.add(ArtExhibitionItemsPanel.this);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("publish");
					}
				};
			}
		});
		
		
		
	
		

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibitionItem>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibitionItem> getItem(String id) {

				return new AjaxLinkMenuItem<ArtExhibitionItem>(id) {

					private static final long serialVersionUID = 1L;

					
					public boolean isEnabled() {
						return getModel().getObject().getState()!=ObjectState.EDITION;
					}
				
					@Override
					public void onClick(AjaxRequestTarget target) {
						getModel().getObject().setState(ObjectState.EDITION);
						getArtExhibitionItemDBService().save(getModel().getObject(), ObjectState.EDITION.getLabel(), getSessionUser().get());
						 refresh(target);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("edit-mode");
					}
				};
			}
		});
		
		
		
		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibitionItem>() {
			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibitionItem> getItem(String id) {
				return new io.wktui.nav.menu.SeparatorMenuItem<ArtExhibitionItem>(id) {
					private static final long serialVersionUID = 1L;
				};
			}
		});
		
		
		
		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibitionItem>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibitionItem> getItem(String id) {

				return new AjaxLinkMenuItem<ArtExhibitionItem>(id) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						ArtExhibitionItemsPanel.this.onObjectRemove(getModel(), target);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("remove");
					}
				};
			}
		});
		return menu;
	}

	private void addMultipleSelector() {

		this.addContainerButtons = new WebMarkupContainer("addContainerButtons");
		this.addContainerButtons.setOutputMarkupId(true);

		add(this.addContainerButtons);

		this.add = new AjaxLink<Void>("add") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				setState(FormState.EDIT);
				target.add(addContainerButtons);
			}

			public boolean isVisible() {
				return getState() == FormState.VIEW;
			}
		};

		this.close = new AjaxLink<Void>("close") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				setState(FormState.VIEW);
				target.add(addContainerButtons);
				target.add(multipleSeletor);
			}

			public boolean isVisible() {
				return getState() == FormState.EDIT;
			}
		};

		this.addContainerButtons.add(add);
		this.addContainerButtons.add(close);

		this.multipleSeletor = new MultipleSelectorPanel<ArtWork>("multipleSelector", getArtWorks()) {

			private static final long serialVersionUID = 1L;

			protected IModel<String> getTitle() {
				return getLabel("artworks");
			}

			public boolean isVisible() {
				return getState() == FormState.EDIT;
			}

			@Override
			protected void onClick(IModel<ArtWork> model) {
				logger.error("on click");
			}

			@Override
			protected void onObjectSelect(IModel<ArtWork> model, AjaxRequestTarget target) {
				ArtExhibitionItemsPanel.this.onObjectSelect(model, target);
			}

			@Override
			protected IModel<String> getObjectInfo(IModel<ArtWork> model) {
				String str = TextCleaner.clean(model.getObject().getInfo(), 280);
				return new Model<String>(str);
			}

			@Override
			protected IModel<String> getObjectSubtitle(IModel<ArtWork> model) {
				return Model.of(getArtistStr(model.getObject()));
			}

			@Override
			protected boolean isExpander() {
				return true;
			}

			@Override
			protected String getObjectImageSrc(IModel<ArtWork> model) {
				if (model.getObject().getPhoto() != null) {
					Resource photo = getResource(model.getObject().getPhoto().getId()).get();
					return getPresignedThumbnailSmall(photo);
				}
				return null;
			}
		};

		this.multipleSeletor.setOutputMarkupId(true);
		this.addContainerButtons.add(this.multipleSeletor);
	}

	/**
	 * 
	 * 
	 * 
	 */

	protected void addSelectedPanel() {

		this.selectedPanel = new ListPanel<ArtExhibitionItem>("items") {

			private static final long serialVersionUID = 1L;

			@Override
			public IModel<String> getItemLabel(IModel<ArtExhibitionItem> model) {
				return ArtExhibitionItemsPanel.this.getObjectTitle(model.getObject());
			}

			@Override
			protected WebMarkupContainer getListItemExpandedPanel(IModel<ArtExhibitionItem> model, ListPanelMode mode) {
				return ArtExhibitionItemsPanel.this.getObjectListItemExpandedPanel(model, mode);
			}

			@Override
			protected Panel getListItemPanel(IModel<ArtExhibitionItem> model) {

				DelleMuseObjectListItemPanel<ArtExhibitionItem> panel = new DelleMuseObjectListItemPanel<ArtExhibitionItem>("row-element", model, getListPanelMode()) {
					private static final long serialVersionUID = 1L;

					@Override
					protected IModel<String> getObjectTitle() {
						return ArtExhibitionItemsPanel.this.getObjectTitle(getModel());
					}

					@Override
					protected String getImageSrc() {
						return ArtExhibitionItemsPanel.this.getObjectImageSrc(getModel());
					}

					@Override
					public void onClick() {
						setResponsePage(new ArtExhibitionItemPage(getModel(), ArtExhibitionItemsPanel.this.getItems()));
					}

					@Override
					protected IModel<String> getInfo() {
						return ArtExhibitionItemsPanel.this.getObjectInfo(getModel());
					}

					@Override
					protected WebMarkupContainer getObjectMenu() {
						return ArtExhibitionItemsPanel.this.getMenu(getModel());
					}

					@Override
					protected String getTitleIcon() {
						if (getModel().getObject().getAudio() != null)
							return ServerAppConstant.headphoneIcon;
						else
							return null;
					}

				};
				return panel;
			}

			@Override
			public List<IModel<ArtExhibitionItem>> getItems() {
				return ArtExhibitionItemsPanel.this.getItems();
			}
		};
		add(selectedPanel);

		selectedPanel.setListPanelMode(ListPanelMode.TITLE);
		selectedPanel.setLiveSearch(false);
		selectedPanel.setSettings(true);
		selectedPanel.setHasExpander(true);
	}

	public Iterable<ArtExhibitionItem> getObjects() {
		return this.getObjects(null, null);
	}

	public Iterable<ArtExhibitionItem> getObjects(ObjectState os1) {
		return this.getObjects(os1, null);
	}

	public Iterable<ArtExhibitionItem> getObjects(ObjectState os1, ObjectState os2) {

		ArtExhibitionDBService service = (ArtExhibitionDBService) ServiceLocator.getInstance().getBean(ArtExhibitionDBService.class);
		ArtExhibition guide = getModel().getObject();

		if (os1 == null && os2 == null)
			return service.getArtExhibitionItems(guide);

		if (os2 == null)
			return service.getArtExhibitionItems(guide, os1);

		if (os1 == null)
			return service.getArtExhibitionItems(guide, os2);

		return service.getArtExhibitionItems(guide, os1, os2);
	}

	public FormState getState() {
		return this.state;
	}

	public void setState(FormState state) {
		this.state = state;
	}

	protected void onCancel(AjaxRequestTarget target) {
		setState(FormState.VIEW);
		target.add(this);
	}

	public void onEdit(AjaxRequestTarget target) {
		setState(FormState.EDIT);
		target.add(this);
	}

	protected void setList(List<IModel<ArtExhibitionItem>> list) {
		this.selected = list;
	}

	protected List<ToolbarItem> getListToolbarItems() {

		if (listToolbar != null)
			return listToolbar;

		listToolbar = new ArrayList<ToolbarItem>();

		IModel<String> selected = Model.of(getObjectStateEnumSelector().getLabel(getLocale()));
		ObjectStateListSelector s = new ObjectStateListSelector("item", selected, Align.TOP_LEFT);

		listToolbar.add(s);
		return listToolbar;
	}

	private void addListToolbar() {

		this.listToolbarContainer = new WebMarkupContainer("listToolbarContainer") {
			private static final long serialVersionUID = 1L;
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

	private void setUpModel() {
		setObjectStateEnumSelector(ObjectStateEnumSelector.EDTIION_PUBLISHED);
	}

	private List<IModel<ArtExhibitionItem>> getItems() {
		if (this.selected == null) {
			loadList();
		}
		return this.selected;
	}

	public IModel<Site> getSiteModel() {
		return siteModel;
	}

	public void setSiteModel(IModel<Site> siteModel) {
		this.siteModel = siteModel;
	}
}
