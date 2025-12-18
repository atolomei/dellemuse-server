package dellemuse.serverapp.page.site;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
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
 
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;
import wktui.base.InvisiblePanel;

public abstract class BaseSiteSearcherPanel extends DBModelPanel<Site>  implements InternalPanel {

	private static final long serialVersionUID = 1L;

	static private Logger logger = Logger.getLogger(BaseSiteSearcherPanel.class.getName());

	private Form<Void> form;
	private String audioId = "";
  	private TextField<String> aidField;
	private List<IModel<GuideContent>> list;
	private ListPanel<GuideContent> itemsPanel;
	
	private WebMarkupContainer itemsContainer;
	private WebMarkupContainer listToolbarContainer;
	
	
	
	
	private List<ToolbarItem> t_list = new ArrayList<ToolbarItem>();
 
	protected abstract void onClick(IModel<GuideContent> model, AjaxRequestTarget target);
	protected abstract List<ToolbarItem> getListToolbarItems();
	protected abstract void onClick(IModel<GuideContent> model);

	public BaseSiteSearcherPanel(String id, IModel<Site> model) {
		super(id, model);
		 
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
		
		itemsContainer = new WebMarkupContainer("itemsContainer");
		add( itemsContainer);
		itemsContainer.setVisible(false);
		itemsContainer.setOutputMarkupId(true);

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
		return Model.of(TextCleaner.clean( getLanguageObjectService().getInfo(model.getObject(), getLocale())));
	}
	
	protected IModel<String> getObjectSubtitle(IModel<GuideContent> model) {
		return null;
	}
	
	protected IModel<String> getObjectTitle(IModel<GuideContent> model) {
		StringBuilder str = new StringBuilder();
		str.append( getLanguageObjectService().getObjectDisplayName(model.getObject(), getLocale()));
		if (model.getObject().getState() == ObjectState.DELETED)
			return new Model<String>(str.toString() + ServerConstant.DELETED_ICON);

		return Model.of(str.toString());
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



	
	protected Panel getObjectListItemExpandedPanel(IModel<GuideContent> model, ListPanelMode mode) {

		model.setObject(super.findGuideContentWithDeps(model.getObject().getId()).get());
		
		return new ObjectListItemExpandedPanel<GuideContent>("expanded-panel", model, mode) {

			private static final long serialVersionUID = 1L;

			@Override
			protected IModel<String> getInfo() {
				return BaseSiteSearcherPanel.this.getObjectInfo(getModel());
			}

			@Override
			protected IModel<String> getObjectSubtitle() {
				return BaseSiteSearcherPanel.this.getObjectSubtitle(getModel());
			}

			@Override
			protected String getImageSrc() {
				return BaseSiteSearcherPanel.this.getObjectImageSrc(getModel());
			}
		};
	}
	
	protected WebMarkupContainer getMenu(IModel<GuideContent> model) {
	
		return null;
		/*
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
						BaseSiteSearcherPanel.this.onClick( getModel(), target);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("open");
					}
				};
			}
		});
		return menu;*/
		
	}
	
	protected  WebMarkupContainer getItemsContainer() {
		return this.itemsContainer;
	}
	
	protected void onSubmit(AjaxRequestTarget target) {
		this.form.updateModel();
		this.list = null;
		addItems();
		this.itemsContainer.setVisible(true);
		target.add(this);
	}
	
	@Override
	protected void addListeners() {
		super.addListeners();
	}
		
	protected Component getListToolbarContainer() {
		return this.listToolbarContainer;
	}

	protected Component getItemsPanel() {
		return this.itemsPanel;
	}

	protected void addForm() {
		
		this.form = new Form<Void>("form");
		add(this.form);

		// this.aidField = new TextField<String>("aid", new PropertyModel<String>(this, "audioId"), getLabel("search-by-aid"));
		
		this.aidField = new TextField<String>("aid", new PropertyModel<String>(this, "audioId"), null );
		
		this.aidField.setPlaceHolderLabel(getLabel("search-by-aid"));
		this.form.add(aidField);

		SubmitButton<Void> submit = new SubmitButton<Void>("submit") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				 BaseSiteSearcherPanel.this.onSubmit(target);
			}
			public IModel<String> getLabel() {
				return  BaseSiteSearcherPanel.this.getLabel("search");
			}
		};
		
		submit.setRowCss("d-inline-block");
		submit.setColCss("w-100 mt-xxl-0 mt-xl-0 mt-lg-0 mt-md-0 mt-sm-0 mt-xs-0 pt-0");
		
		this.form.add(submit);
		this.form.setFormState(FormState.EDIT);
		this.form.visitChildren(Field.class, new IVisitor<Field<?>, Void>() {
			@Override
			public void component(Field<?> field, IVisit<Void> visit) {
				field.editOn();
			}
		});
	}
	
	protected List<IModel<GuideContent>> getItems() {
		if (this.list == null) {
			this.list=generateList();
		}
		return this.list;
	}
	
	protected abstract List<IModel<GuideContent>> generateList();
	
	protected void reloadList() {
		this.list=generateList();
	}
	
	protected void addListToolbar() {

		this.listToolbarContainer = new WebMarkupContainer("listToolbarContainer") {
			private static final long serialVersionUID = 1L;
			
			public boolean isVisible() {
				return  (getListToolbarItems()!=null && getListToolbarItems().size()>0);
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
	
	protected void addItems() {

		this.itemsPanel = new ListPanel<GuideContent>("items") {
		
			private static final long serialVersionUID = 1L;

			protected List<IModel<GuideContent>> filter(List<IModel<GuideContent>> initialList, String filter) {
				return iFilter(initialList, filter);
			}
			
			@Override
			protected WebMarkupContainer getListItemExpandedPanel(IModel<GuideContent> model, ListPanelMode mode) {
				return BaseSiteSearcherPanel.this.getObjectListItemExpandedPanel(model, mode);
			}

			@Override
			protected Panel getListItemPanel(IModel<GuideContent> model) {
				
				DelleMuseObjectListItemPanel<GuideContent> panel = new DelleMuseObjectListItemPanel<GuideContent>("row-element",
						model, getListPanelMode()) {
					private static final long serialVersionUID = 1L;

					@Override
					protected IModel<String> getObjectTitle() {
						return BaseSiteSearcherPanel.this.getObjectTitle(getModel());
					}

					protected String getImageSrc() {
						return BaseSiteSearcherPanel.this.getObjectImageSrc(getModel());
					}

					@Override
					public void onClick() {
						BaseSiteSearcherPanel.this.onClick( getModel());
					}

					@Override
					protected IModel<String> getInfo() {
						return BaseSiteSearcherPanel.this.getObjectInfo(getModel());
					}

					@Override
					protected WebMarkupContainer getObjectMenu() {
						return BaseSiteSearcherPanel.this.getMenu(getModel());
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
				return  BaseSiteSearcherPanel.this.getItems();
			}
			
		};
		
		itemsContainer.addOrReplace(itemsPanel);
		itemsContainer.setVisible(true);
		
		itemsPanel.setListPanelMode(ListPanelMode.TITLE);
		itemsPanel.setLiveSearch(false);
		itemsPanel.setSettings(true);
		itemsPanel.setItemMenu(false);
		itemsPanel.setHasExpander(true);
	}

	
	
}
