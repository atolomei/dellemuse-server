package dellemuse.serverapp.page.site;

import java.util.ArrayList;
import java.util.List;

 
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
 
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.ServerConstant;
import dellemuse.serverapp.guidecontent.GuideContentPage;
import dellemuse.serverapp.page.DelleMuseObjectListItemPanel;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.ObjectListItemExpandedPanel;
import dellemuse.serverapp.page.library.ObjectStateEnumSelector;
import dellemuse.serverapp.page.library.ObjectStateListSelector;
import dellemuse.serverapp.page.library.ObjectStateSelectEvent;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.person.ServerAppConstant;
 
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.model.ObjectState;
import dellemuse.serverapp.serverdb.model.Site;
 
import io.wktui.event.UIEvent;
import io.wktui.form.Form;
import io.wktui.form.FormState;
import io.wktui.form.button.SubmitButton;
import io.wktui.form.field.Field;
 
import io.wktui.form.field.TextField;
import io.wktui.model.TextCleaner;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.toolbar.Toolbar;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;
import wktui.base.InvisiblePanel;

public class SiteSearcherPanel extends DBModelPanel<Site>  implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(SiteSearcherPanel.class.getName());

	private Form<Void> form;
	private String audioId = "";
  	private TextField<String> aidField;
	private List<IModel<GuideContent>> list;
	private ListPanel<GuideContent> itemsPanel;
	private WebMarkupContainer listToolbarContainer;
	private List<ToolbarItem> listToolbar;
	private List<ToolbarItem> t_list = new ArrayList<ToolbarItem>();
	private ObjectStateEnumSelector oses;
	
	public SiteSearcherPanel(String id, IModel<Site> model) {
		super(id, model);
		oses=ObjectStateEnumSelector.ALL;
		setOutputMarkupId (true);
	}

	public String getAudioId() { 
		return this.audioId;
	}
	
	public void setAudioId(String aid) { 
		this.audioId=aid;
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		addListToolbar();
		addForm();
		add(new InvisiblePanel("items"));
	}

	@Override
	public List<ToolbarItem> getToolbarItems() {
		return t_list;
	}
	 
	@Override
	public void onDetach() {
		super.onDetach();
		if (this.list != null)
			this.list.forEach(i -> i.detach());
	}

	public void setObjectStateEnumSelector(ObjectStateEnumSelector o) {
		this.oses = o;
	}

	public ObjectStateEnumSelector getObjectStateEnumSelector() {
		return this.oses;
	}

	public Iterable<GuideContent> getObjects() {
		 return this.getObjects(null, null);
	}
	
	public Iterable<GuideContent> getObjects(ObjectState os1) {
		 return this.getObjects(os1, null);
	}

	public Iterable<GuideContent> getObjects(ObjectState os1, ObjectState os2) {
		return getSiteGuideContents(getModel().getObject());
	}

	protected IModel<String> getObjectInfo(IModel<GuideContent> model) {
	 	return Model.of(TextCleaner.clean( getInfo( model.getObject())));
	}
	
	protected IModel<String> getObjectSubtitle(IModel<GuideContent> model) {
		return null;
	}

	protected String getObjectImageSrc(IModel<GuideContent> model) {
		return super.getImageSrc(model.getObject());
	}
	
	protected void setList(List<IModel<GuideContent>> list) {
		this.list = list;
	}

	protected List<IModel<GuideContent>> getList() {
		return this.list;
	}

	protected IModel<String> getObjectTitle(IModel<GuideContent> model) {
		StringBuilder str = new StringBuilder();
		str.append(model.getObject().getDisplayname());

		if (model.getObject().getState() == ObjectState.DELETED)
			return new Model<String>(str.toString() + ServerConstant.DELETED_ICON);

		return Model.of(str.toString());
	}

	protected synchronized void loadList() {

		this.list = new ArrayList<IModel<GuideContent>>();
		
		Long l_aid = null;
		
		try {
			if (getAudioId()!=null && getAudioId().length()>0)
				l_aid = Long.valueOf(getAudioId());
		} catch (Exception e) {
			l_aid = Long.valueOf(-1);
		}
		
		if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.EDTIION_PUBLISHED)
			getGuideContentDBService().getByAudioId( getModel().getObject(), l_aid, ObjectState.EDITION, ObjectState.PUBLISHED).forEach(s -> this.list.add(new ObjectModel<GuideContent>(s)));
	
		else if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.PUBLISHED)
			getGuideContentDBService().getByAudioId( getModel().getObject(), l_aid, ObjectState.PUBLISHED).forEach(s -> this.list.add(new ObjectModel<GuideContent>(s)));
		
		else if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.EDITION)
			getGuideContentDBService().getByAudioId( getModel().getObject(), l_aid, ObjectState.EDITION).forEach(s -> this.list.add(new ObjectModel<GuideContent>(s)));
	
		else if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.ALL)
			getGuideContentDBService().getByAudioId( getModel().getObject(), l_aid).forEach(s -> this.list.add(new ObjectModel<GuideContent>(s)));
	
		else if (this.getObjectStateEnumSelector() == ObjectStateEnumSelector.DELETED)
			getGuideContentDBService().getByAudioId( getModel().getObject(), l_aid, ObjectState.DELETED).forEach(s -> this.list.add(new ObjectModel<GuideContent>(s)));
		 
		this.list.forEach(c -> logger.debug(c.getObject().toString()));
		
		logger.debug("ok");
	
	}
	
	protected Panel getObjectListItemExpandedPanel(IModel<GuideContent> model, ListPanelMode mode) {

		model.setObject(super.findGuideContentWithDeps(model.getObject().getId()).get());
		
		return new ObjectListItemExpandedPanel<GuideContent>("expanded-panel", model, mode) {

			private static final long serialVersionUID = 1L;

			@Override
			protected IModel<String> getInfo() {
				return SiteSearcherPanel.this.getObjectInfo(getModel());
			}

			@Override
			protected IModel<String> getObjectSubtitle() {
				return SiteSearcherPanel.this.getObjectSubtitle(getModel());
			}

			@Override
			protected String getImageSrc() {
				return SiteSearcherPanel.this.getObjectImageSrc(getModel());
			}
		};
	}
	
	protected WebMarkupContainer getMenu(IModel<GuideContent> model) {
		
		NavDropDownMenu<GuideContent> menu = new NavDropDownMenu<GuideContent>("menu", model, null) {
			private static final long serialVersionUID = 1L;

			public boolean isVisible() {
				return true;
			}
		};
	
		menu.setOutputMarkupId(true);

		menu.setLabelCss("d-block-inline d-sm-block-inline d-md-block-inline d-lg-none d-xl-none d-xxl-none ps-1 pe-1");
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
		return menu;
	}

	protected void onSubmit(AjaxRequestTarget target) {
		this.form.updateModel();
		this.list = null;
		addItems();
		target.add(this);
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

	protected void addListeners() {
		super.addListeners();

		add(new io.wktui.event.WicketEventListener<ObjectStateSelectEvent>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onEvent(ObjectStateSelectEvent event) {
				setObjectStateEnumSelector(event.getObjectStateEnumSelector());
				loadList();
				event.getTarget().add(  itemsPanel);
				event.getTarget().add(  listToolbarContainer);
			}

			@Override
			public boolean handle(UIEvent event) {
				if (event instanceof ObjectStateSelectEvent)
					return true;
				return false;
			}
		});
	}
	
	private void addForm() {
		
		this.form = new Form<Void>("form");
		add(this.form);

		this.aidField = new TextField<String>("aid", new PropertyModel<String>(this, "audioId"), getLabel("aid"));
		this.form.add(aidField);

		SubmitButton<Void> submit = new SubmitButton<Void>("submit") {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				 SiteSearcherPanel.this.onSubmit(target);
			}

			public IModel<String> getLabel() {
				return  SiteSearcherPanel.this.getLabel("search");
			}
		};
		this.form.add(submit);
		
		
		this.form.setFormState(FormState.EDIT);
		this.form.visitChildren(Field.class, new IVisitor<Field<?>, Void>() {
			@Override
			public void component(Field<?> field, IVisit<Void> visit) {
				field.editOn();
			}
		});
	}
	
	private List<IModel<GuideContent>> getItems() {
		if (this.list == null) {
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
	
	private void addItems() {

		this.itemsPanel = new ListPanel<GuideContent>("items") {
		
			private static final long serialVersionUID = 1L;

			protected List<IModel<GuideContent>> filter(List<IModel<GuideContent>> initialList, String filter) {
				return iFilter(initialList, filter);
			}
			
			@Override
			protected WebMarkupContainer getListItemExpandedPanel(IModel<GuideContent> model, ListPanelMode mode) {
				return SiteSearcherPanel.this.getObjectListItemExpandedPanel(model, mode);
			}

			@Override
			protected Panel getListItemPanel(IModel<GuideContent> model) {
				
				DelleMuseObjectListItemPanel<GuideContent> panel = new DelleMuseObjectListItemPanel<GuideContent>("row-element",
						model, getListPanelMode()) {
					private static final long serialVersionUID = 1L;

					@Override
					protected IModel<String> getObjectTitle() {
						return SiteSearcherPanel.this.getObjectTitle(getModel());
					}

					protected String getImageSrc() {
						return SiteSearcherPanel.this.getObjectImageSrc(getModel());
					}

					@Override
					public void onClick() {
						setResponsePage(new GuideContentPage(getModel(),SiteSearcherPanel.this.getItems()));
					}

					@Override
					protected IModel<String> getInfo() {
						return SiteSearcherPanel.this.getObjectInfo(getModel());
					}

					@Override
					protected WebMarkupContainer getObjectMenu() {
						return SiteSearcherPanel.this.getMenu(getModel());
					}
				
					@Override
					protected String getTitleIcon() {
						if (getModel().getObject().getAudio()!=null)
							return ServerAppConstant.headphoneIcon;
						else
							return null;
					}
				};
				return panel;
			}
		
			@Override
			public List<IModel<GuideContent>> getItems()  {
				return  SiteSearcherPanel.this.getItems();
			}
			
		};
		addOrReplace(itemsPanel);

		itemsPanel.setListPanelMode(ListPanelMode.TITLE);
		itemsPanel.setLiveSearch(false);
		itemsPanel.setSettings(true);
		itemsPanel.setHasExpander(true);
	}

	
	
}
