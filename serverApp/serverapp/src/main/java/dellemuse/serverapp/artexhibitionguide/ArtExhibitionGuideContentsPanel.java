package dellemuse.serverapp.artexhibitionguide;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.model.logging.Logger;
import dellemuse.model.util.ThumbnailSize;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.artexhibition.ArtExhibitionSectionsPanel;
import dellemuse.serverapp.artexhibitionitem.ArtExhibitionItemPage;
import dellemuse.serverapp.guidecontent.GuideContentPage;
import dellemuse.serverapp.page.DelleMuseObjectListItemPanel;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.MultipleSelectorPanel;
import dellemuse.serverapp.page.ObjectListItemExpandedPanel;
import dellemuse.serverapp.page.ObjectListItemPanel;
import dellemuse.serverapp.page.ObjectListPage;
import dellemuse.serverapp.page.library.ObjectStateEnumSelector;
import dellemuse.serverapp.page.library.ObjectStateListSelector;
import dellemuse.serverapp.page.library.ObjectStateSelectEvent;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtExhibitionItem;
import dellemuse.serverapp.serverdb.model.ArtExhibitionSection;
import dellemuse.serverapp.serverdb.model.ArtWork;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Person;
import dellemuse.serverapp.serverdb.model.Resource;
import dellemuse.serverapp.serverdb.model.Site;
import dellemuse.serverapp.serverdb.service.ArtExhibitionDBService;
import dellemuse.serverapp.serverdb.service.ArtExhibitionGuideDBService;
import dellemuse.serverapp.serverdb.service.ArtExhibitionItemDBService;
import dellemuse.serverapp.serverdb.service.GuideContentDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;

import io.wktui.event.UIEvent;
import io.wktui.form.FormState;
import io.wktui.model.TextCleaner;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.Toolbar;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;
import wktui.base.InvisiblePanel;

public class ArtExhibitionGuideContentsPanel extends DBModelPanel<ArtExhibitionGuide> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(ArtExhibitionGuideContentsPanel.class.getName());

	private List<IModel<GuideContent>> list;
	private List<IModel<ArtExhibitionItem>> aList;
	private IModel<Site> siteModel;
	private IModel<ArtExhibition> artExhibitionModel;

	private ObjectStateEnumSelector oses;

	private FormState state = FormState.VIEW;

	private MultipleSelectorPanel<ArtExhibitionItem> multipleSeletor;

	private AjaxLink<Void> add;
	private AjaxLink<Void> close;
	private WebMarkupContainer addContainerButtons;
	private ListPanel<GuideContent> itemsPanel;
	private WebMarkupContainer listToolbarContainer;
	private List<ToolbarItem> listToolbar;
	private List<ToolbarItem> t_list = new ArrayList<ToolbarItem>();

	/**
	 * @param id
	 * @param model
	 */

	public ArtExhibitionGuideContentsPanel(String id, IModel<ArtExhibitionGuide> model, IModel<Site> siteModel) {
		super(id, model);
		this.siteModel = siteModel;
		setOutputMarkupId(true);

	}

	@Override
	public void onInitialize() {
		super.onInitialize();

		setUpModel();

		addListToolbar();
		addItems();

		addSelector();

	}

	public void setObjectStateEnumSelector(ObjectStateEnumSelector o) {
		this.oses = o;
	}

	public ObjectStateEnumSelector getObjectStateEnumSelector() {
		return this.oses;
	}

	protected void loadList() {

		this.list = new ArrayList<IModel<GuideContent>>();

		if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.EDTIION_PUBLISHED)
			getObjects(ObjectState.EDITION, ObjectState.PUBLISHED).forEach(s -> this.list.add(new ObjectModel<GuideContent>(s)));

		if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.PUBLISHED)
			getObjects(ObjectState.PUBLISHED).forEach(s -> this.list.add(new ObjectModel<GuideContent>(s)));

		if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.EDITION)
			getObjects(ObjectState.EDITION).forEach(s -> this.list.add(new ObjectModel<GuideContent>(s)));

		else if (this.getObjectStateEnumSelector() == null)
			getObjects().forEach(s -> this.list.add(new ObjectModel<GuideContent>(s)));

		else if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.ALL)
			getObjects().forEach(s -> this.list.add(new ObjectModel<GuideContent>(s)));

		else if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.DELETED)
			getObjects(ObjectState.DELETED).forEach(s -> this.list.add(new ObjectModel<GuideContent>(s)));

		this.list.forEach(c -> logger.debug(c.toString()));
	}

	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<ObjectStateSelectEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(ObjectStateSelectEvent event) {
				setObjectStateEnumSelector(event.getObjectStateEnumSelector());
				loadList();
				event.getTarget().add(ArtExhibitionGuideContentsPanel.this.itemsPanel);
				event.getTarget().add(ArtExhibitionGuideContentsPanel.this.listToolbarContainer);
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof ObjectStateSelectEvent)
					return true;
				return false;
			}
		});

	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (this.list != null)
			this.list.forEach(t -> t.detach());

		if (siteModel != null)
			siteModel.detach();

		if (artExhibitionModel != null)
			artExhibitionModel.detach();

		if (aList != null)
			this.aList.forEach(t -> t.detach());
	}

	@Override
	public List<ToolbarItem> getToolbarItems() {
		return t_list;
	}

	public IModel<ArtExhibition> getArtExhibitionModel() {
		return artExhibitionModel;
	}

	public void setArtExhibitionModel(IModel<ArtExhibition> artExhibitionModel) {
		this.artExhibitionModel = artExhibitionModel;
	}

	public IModel<String> getObjectTitle(IModel<GuideContent> model) {
		StringBuilder str = new StringBuilder();
		str.append(model.getObject().getDisplayname());

		if (model.getObject().getState() == ObjectState.DELETED)
			return new Model<String>(str.toString() + ServerConstant.DELETED_ICON);

		return Model.of(str.toString());
	}

	public Iterable<GuideContent> getObjects() {
		return this.getObjects(null, null);
	}

	public Iterable<GuideContent> getObjects(ObjectState os1) {
		return this.getObjects(os1, null);
	}

	public Iterable<GuideContent> getObjects(ObjectState os1, ObjectState os2) {
		ArtExhibitionGuideDBService service = (ArtExhibitionGuideDBService) ServiceLocator.getInstance().getBean(ArtExhibitionGuideDBService.class);
		ArtExhibitionGuide guide = getModel().getObject();
		if (os1 == null && os2 == null)
			return service.getGuideContents(guide);
		if (os2 == null)
			return service.getArtExhibitionGuideContents(guide, os1);
		if (os1 == null)
			return service.getArtExhibitionGuideContents(guide, os2);
		return service.getArtExhibitionGuideContents(guide, os1, os2);
	}

	protected Panel getObjectListItemExpandedPanel(IModel<GuideContent> model, ListPanelMode mode) {

		GuideContent gc = super.findGuideContentWithDeps(model.getObject().getId()).get();
		model.setObject(gc);

		return new ObjectListItemExpandedPanel<GuideContent>("expanded-panel", model, mode) {

			private static final long serialVersionUID = 1L;

			@Override
			protected IModel<String> getInfo() {
				return ArtExhibitionGuideContentsPanel.this.getObjectInfo(getModel());
			}

			@Override
			protected IModel<String> getObjectSubtitle() {
				return ArtExhibitionGuideContentsPanel.this.getObjectSubtitle(getModel());
			}

			@Override
			protected String getImageSrc() {
				return ArtExhibitionGuideContentsPanel.this.getObjectImageSrc(getModel());
			}

			@Override
			protected String getIcon() {
				return isAudio(getModel()) ? "fa-solid fa-headphones iconOver" : null;
			}

		};
	}

	protected void onObjectRemove(IModel<GuideContent> model, AjaxRequestTarget target) {
		GuideContent item = model.getObject();
		// ArtExhibitionGuide ex= getModel().getObject();
		getGuideContentDBService().delete(item);
		resetList();
		target.add(this.itemsPanel);
	}

	protected void onObjectSelect(IModel<ArtExhibitionItem> model, AjaxRequestTarget target) {

		ArtExhibitionItem item = model.getObject();
		// ArtExhibition ex= getArtExhibitionModel().getObject();

		super.addItem(getModel().getObject(), item, getUserDBService().findRoot());
		resetList();
		target.add(this.itemsPanel);
	}

	protected void resetList() {
		this.list = null;
	}

	protected IModel<String> getObjectInfo(IModel<GuideContent> model) {
		if (!model.getObject().isDependencies()) {
			model.setObject(super.findGuideContentWithDeps(model.getObject().getId()).get());
		}
		return Model.of(TextCleaner.clean(getInfo(model.getObject()), 420 ));
	}

	protected IModel<String> getObjectSubtitle(IModel<GuideContent> model) {
		if (model.getObject().getSubtitle() != null)
			return Model.of(model.getObject().getSubtitle());
		return null;
	}

	protected String getObjectImageSrc(IModel<GuideContent> model) {
		return super.getImageSrc(model.getObject());
	}

	protected List<IModel<ArtExhibitionItem>> getArtExhibitionItems() {

		if (aList != null)
			return aList;

		aList = new ArrayList<IModel<ArtExhibitionItem>>();
		super.getArtExhibitionItems(this.getArtExhibitionModel().getObject()).forEach(i -> aList.add(new ObjectModel<ArtExhibitionItem>(i)));
		;
		return aList;
	}

	protected WebMarkupContainer getMenu(IModel<GuideContent> model) {
		NavDropDownMenu<GuideContent> menu = new NavDropDownMenu<GuideContent>("menu", model, null) {
			private static final long serialVersionUID = 1L;

			public boolean isVisible() {
				return true;
			}
		};

		menu.setOutputMarkupId(true);

		menu.setTitleCss("d-block-inline d-sm-block-inline d-md-block-inline d-lg-none d-xl-none d-xxl-none ps-1 pe-1");
		menu.setIconCss("fa-solid fa-ellipsis d-block-inline d-sm-block-inline d-md-block-inline d-lg-block-inline d-xl-block-inline d-xxl-block-inline ps-1 pe-1");

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<GuideContent>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<GuideContent> getItem(String id) {

				return new AjaxLinkMenuItem<GuideContent>(id) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						// refresh(target);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("open");
					}

				};
			}
		});

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<GuideContent>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<GuideContent> getItem(String id) {

				return new AjaxLinkMenuItem<GuideContent>(id) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						ArtExhibitionGuideContentsPanel.this.onObjectRemove(getModel(), target);
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

	private List<IModel<GuideContent>> getItems() {

		if (this.list == null) {
			// this.list = new ArrayList<IModel<GuideContent>>();
			// super.getGuideContents(getModel().getObject()).forEach(item ->
			// this.list.add(new ObjectModel<GuideContent>(item)));
			loadList();
		}
		return this.list;
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

	protected List<ToolbarItem> getListToolbarItems() {

		if (listToolbar != null)
			return listToolbar;

		listToolbar = new ArrayList<ToolbarItem>();

		IModel<String> selected = Model.of(ObjectStateEnumSelector.ALL.getLabel(getLocale()));
		ObjectStateListSelector s = new ObjectStateListSelector("item", selected, Align.TOP_LEFT);
		listToolbar.add(s);
		return listToolbar;
	}

	private void addSelector() {

		this.addContainerButtons = new WebMarkupContainer("addContainerButtons");
		this.addContainerButtons.setOutputMarkupId(true);

		add(this.addContainerButtons);

		this.add = new AjaxLink<Void>("add") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				setState(FormState.EDIT);
				target.add(addContainerButtons);
				// target.add(addContainer);

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
				// target.add(addContainer);
				target.add(multipleSeletor);
			}

			public boolean isVisible() {
				return getState() == FormState.EDIT;
			}
		};

		this.addContainerButtons.add(add);
		this.addContainerButtons.add(close);

		this.multipleSeletor = new MultipleSelectorPanel<ArtExhibitionItem>("multipleSelector", getArtExhibitionItems()) {

			private static final long serialVersionUID = 1L;

			protected IModel<String> getTitle() {
				return getLabel("exhibition-artworks");
			}

			public boolean isVisible() {
				return getState() == FormState.EDIT;
			}

			@Override
			protected void onClick(IModel<ArtExhibitionItem> model) {
				// TODO Auto-generated method stub
			}

			@Override
			protected void onObjectSelect(IModel<ArtExhibitionItem> model, AjaxRequestTarget target) {
				ArtExhibitionGuideContentsPanel.this.onObjectSelect(model, target);
			}

			@Override
			protected IModel<String> getObjectInfo(IModel<ArtExhibitionItem> model) {
				String str = TextCleaner.clean(model.getObject().getInfo(), 280);
				return new Model<String>(str);
			}

			@Override
			protected IModel<String> getObjectSubtitle(IModel<ArtExhibitionItem> model) {
				return Model.of(getArtistStr(model.getObject()));
			}

			@Override
			protected boolean isExpander() {
				return true;
			}

			@Override
			protected String getObjectImageSrc(IModel<ArtExhibitionItem> model) {
				return super.getImageSrc(model.getObject());
			}
		};

		this.multipleSeletor.setOutputMarkupId(true);

		this.addContainerButtons.add(this.multipleSeletor);
	}

	private void addItems() {

		this.itemsPanel = new ListPanel<GuideContent>("items") {

			private static final long serialVersionUID = 1L;

			@Override
			public IModel<String> getItemLabel(IModel<GuideContent> model) {
				return ArtExhibitionGuideContentsPanel.this.getObjectTitle(model.getObject());
			}

			@Override
			protected WebMarkupContainer getListItemExpandedPanel(IModel<GuideContent> model, ListPanelMode mode) {
				return ArtExhibitionGuideContentsPanel.this.getObjectListItemExpandedPanel(model, mode);

			}

			@Override
			protected Panel getListItemPanel(IModel<GuideContent> model) {

				DelleMuseObjectListItemPanel<GuideContent> panel = new DelleMuseObjectListItemPanel<GuideContent>("row-element", model, getListPanelMode()) {
					private static final long serialVersionUID = 1L;

					@Override
					protected String getTitleIcon() {
						if (getModel().getObject().getAudio() != null)
							return ServerAppConstant.headphoneIcon;
						else
							return null;
					}

					@Override
					protected IModel<String> getObjectTitle() {
						return ArtExhibitionGuideContentsPanel.this.getObjectTitle(getModel().getObject());
					}

					@Override
					protected String getImageSrc() {
						return ArtExhibitionGuideContentsPanel.this.getObjectImageSrc(getModel());
					}

					@Override
					public void onClick() {
						setResponsePage(new GuideContentPage(getModel(), ArtExhibitionGuideContentsPanel.this.getItems()));
					}

					@Override
					protected IModel<String> getInfo() {
						return ArtExhibitionGuideContentsPanel.this.getObjectInfo(getModel());
					}

					@Override
					protected WebMarkupContainer getObjectMenu() {
						return ArtExhibitionGuideContentsPanel.this.getMenu(getModel());
					}

					@Override
					protected String getIcon() {
						return isAudio(getModel()) ? "fa-solid fa-headphones iconOver" : null;
					}

				};
				return panel;
			}

			@Override
			public List<IModel<GuideContent>> getItems() {
				return ArtExhibitionGuideContentsPanel.this.getItems();
			}
 

		};
		add(itemsPanel);

		// panel.setTitle(getLabel("exhibitions-permanent"));
		itemsPanel.setListPanelMode(ListPanelMode.TITLE);
		itemsPanel.setLiveSearch(false);
		itemsPanel.setSettings(true);
		itemsPanel.setHasExpander(true);
	}

	protected void setList(List<IModel<GuideContent>> list) {
		this.list = list;
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

	protected boolean isAudio(IModel<GuideContent> model) {
		return model.getObject().getAudio() != null;
	}

	private void setUpModel() {
		ArtExhibition ae = getModel().getObject().getArtExhibition();
		setArtExhibitionModel(new ObjectModel<ArtExhibition>(ae));
		setObjectStateEnumSelector(ObjectStateEnumSelector.EDTIION_PUBLISHED);
	}

}
