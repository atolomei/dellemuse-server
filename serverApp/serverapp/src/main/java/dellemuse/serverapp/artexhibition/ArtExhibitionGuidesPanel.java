package dellemuse.serverapp.artexhibition;

import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
 
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import dellemuse.model.logging.Logger;
import dellemuse.serverapp.artexhibitionguide.ArtExhibitionGuidePage;
import dellemuse.serverapp.page.DelleMuseObjectListItemPanel;
import dellemuse.serverapp.page.InternalPanel;
import dellemuse.serverapp.page.ObjectListItemExpandedPanel;
import dellemuse.serverapp.page.ObjectListPage;
import dellemuse.serverapp.page.model.DBModelPanel;
import dellemuse.serverapp.page.model.ObjectModel;
import dellemuse.serverapp.page.person.ServerAppConstant;
import dellemuse.serverapp.serverdb.model.ArtExhibition;
import dellemuse.serverapp.serverdb.model.ArtExhibitionGuide;
import dellemuse.serverapp.serverdb.model.ArtExhibitionSection;
import dellemuse.serverapp.serverdb.model.DelleMuseAudit;
import dellemuse.serverapp.serverdb.model.GuideContent;
import dellemuse.serverapp.serverdb.service.ArtExhibitionGuideDBService;
import dellemuse.serverapp.serverdb.service.base.ServiceLocator;
import io.wktui.event.MenuAjaxEvent;
import io.wktui.form.FormState;
import io.wktui.nav.menu.AjaxLinkMenuItem;
import io.wktui.nav.menu.MenuItemPanel;
import io.wktui.nav.menu.NavDropDownMenu;
import io.wktui.nav.toolbar.AjaxButtonToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem;
import io.wktui.nav.toolbar.ToolbarItem.Align;
import io.wktui.struct.list.ListPanel;
import io.wktui.struct.list.ListPanelMode;

public class ArtExhibitionGuidesPanel extends DBModelPanel<ArtExhibition> implements InternalPanel {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	static private Logger logger = Logger.getLogger(ArtExhibitionGuidesPanel.class.getName());

	private List<IModel<ArtExhibitionGuide>> list;
	private FormState state = FormState.VIEW;
	private WebMarkupContainer itemsContainer;
	private ListPanel<ArtExhibitionGuide> panel;
	private List<ToolbarItem> t_list;

	/**
	 * @param id
	 * @param model
	 */
	
	public ArtExhibitionGuidesPanel(String id, IModel<ArtExhibition> model) {
		super(id, model);
	}

	@Override
	public void onInitialize() {
		super.onInitialize();
		
		this.itemsContainer =new WebMarkupContainer("itemsContainer");
		this.itemsContainer.setOutputMarkupId(true);
		add(this.itemsContainer);
		
		addItems();
	}

	@Override
	public void onDetach() {
		super.onDetach();

		if (this.list != null)
			this.list.forEach(t -> t.detach());
	}
	
	@Override
	public List<ToolbarItem> getToolbarItems() {
		
		if (t_list!=null)
			return t_list;
		
		t_list = new ArrayList<ToolbarItem>();

		AjaxButtonToolbarItem<ArtExhibitionGuide> create = new AjaxButtonToolbarItem<ArtExhibitionGuide>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onCick(AjaxRequestTarget target) {
				fire(new MenuAjaxEvent(ServerAppConstant.action_exhibition_guide_create, target));
			}

			@Override
			public IModel<String> getButtonLabel() {
				return getLabel("create");
			}
		};
		create.setAlign(Align.TOP_LEFT);
		t_list.add(create);

		return t_list;
	}

	public FormState getState() {
		return this.state;
	}
	
	public void setState(FormState state) {
		this.state=state;
	}

	public void onEdit(AjaxRequestTarget target) {
		setState(FormState.EDIT);
		target.add(this);
	}
	
	public void onGuideCreate(AjaxRequestTarget target) {
		
		ArtExhibitionGuideDBService service = (ArtExhibitionGuideDBService) ServiceLocator.getInstance().getBean( ArtExhibitionGuideDBService.class);

		String name = getModel().getObject().getName();

		if (getItems().size()<0)
			name = name + "-"+String.valueOf(getItems().size());

		service.create(name, getModel().getObject(), getSessionUser());
	
		resetItems();
		target.add(this.itemsContainer);
	}

	protected void onCancel(AjaxRequestTarget target) {
		setState(FormState.VIEW);
		target.add(this);
	}

	protected WebMarkupContainer getMenu(IModel<ArtExhibitionGuide> model) {
		NavDropDownMenu<ArtExhibitionGuide> menu = new NavDropDownMenu<ArtExhibitionGuide>("menu", model, null) {
			private static final long serialVersionUID = 1L;

			public boolean isVisible() {
				return true;
			}
		};
	
		menu.setOutputMarkupId(true);

		menu.setLabelCss("d-block-inline d-sm-block-inline d-md-block-inline d-lg-none d-xl-none d-xxl-none ps-1 pe-1");
		menu.setIconCss(
				"fa-solid fa-ellipsis d-block-inline d-sm-block-inline d-md-block-inline d-lg-block-inline d-xl-block-inline d-xxl-block-inline ps-1 pe-1");

		menu.addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibitionGuide>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibitionGuide> getItem(String id) {

				return new AjaxLinkMenuItem<ArtExhibitionGuide>(id) {

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

	/**	menu.addItem(new io.wktui.nav.menu.MenuItemFactory<ArtExhibitionGuide>() {

			private static final long serialVersionUID = 1L;

			@Override
			public MenuItemPanel<ArtExhibitionGuide> getItem(String id) {

				return new AjaxLinkMenuItem<ArtExhibitionGuide>(id) {

					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						// refresh(target);
					}

					@Override
					public IModel<String> getLabel() {
						return getLabel("delete");
					}
				};
			}
		});
		*/
		return menu;
	}

	
	
	
	protected IModel<String> getObjectInfo(IModel<ArtExhibitionGuide> model) {
		
		return Model.of(getInfo(model.getObject(), false ));
	}
	
	
	protected String getObjectImageSrc(IModel<ArtExhibitionGuide> model) {
		return super.getImageSrc(model.getObject());
	}
	
	protected IModel<String> getObjectSubtitle(IModel<ArtExhibitionGuide> model) {
		return null;
	}
	
	protected Panel getObjectListItemExpandedPanel(IModel<ArtExhibitionGuide> model, ListPanelMode mode) {

		model.setObject( super.findArtExhibitionGuideWithDeps(model.getObject().getId()).get() );
		
		return new ObjectListItemExpandedPanel<ArtExhibitionGuide>("expanded-panel", model, mode) {

			private static final long serialVersionUID = 1L;

			@Override
			protected IModel<String> getInfo() {
				return ArtExhibitionGuidesPanel.this.getObjectInfo(getModel());
			}

			@Override
			protected IModel<String> getObjectSubtitle() {
				return ArtExhibitionGuidesPanel.this.getObjectSubtitle(getModel());
			}

			@Override
			protected String getImageSrc() {
				return ArtExhibitionGuidesPanel.this.getObjectImageSrc(getModel());
			}
		};
	}
	
	private void addItems() {

		 this.panel = new ListPanel<ArtExhibitionGuide>("items") {
			
			private static final long serialVersionUID = 1L;

			public List<IModel<ArtExhibitionGuide>> getItems()  {
				return  ArtExhibitionGuidesPanel.this.getItems();
			}
			
			//@Override
			//protected void setItems(List<IModel<ArtExhibitionGuide>> list) {
			//	ArtExhibitionGuidesPanel.this.setList(list);
			//}
			
			@Override
			public Integer getTotalItems()  {
				return  Integer.valueOf(ArtExhibitionGuidesPanel.this.getItems().size() );
			}
			
			@Override
			protected List<IModel<ArtExhibitionGuide>> filter(List<IModel<ArtExhibitionGuide>> initialList,
					String filter) {
				return iFilter(initialList, filter);
/**
				List<IModel<ArtExhibitionGuide>> list = new ArrayList<IModel<ArtExhibitionGuide>>();
				final String str = filter.trim().toLowerCase();
				initialList.forEach(s -> {
					if (s.getObject().getDisplayname().toLowerCase().contains(str)) {
						list.add(s);
					}
				});
				return list;*/
			}


			@Override
			protected WebMarkupContainer getListItemExpandedPanel(IModel<ArtExhibitionGuide> model, ListPanelMode mode) {
				return ArtExhibitionGuidesPanel.this.getObjectListItemExpandedPanel(model, mode);
				
			}
			@Override
			protected Panel getListItemPanel(IModel<ArtExhibitionGuide> model) {
				
				DelleMuseObjectListItemPanel<ArtExhibitionGuide> panel = new DelleMuseObjectListItemPanel<ArtExhibitionGuide>("row-element",
						model, getListPanelMode()) {
					private static final long serialVersionUID = 1L;

					@Override
					protected String getTitleIcon() {
						if (getModel().getObject().getAudio()!=null)
							return ServerAppConstant.headphoneIcon;
						else
							return null;
					}

					@Override
					protected String getImageSrc() {
						return ArtExhibitionGuidesPanel.this.getObjectImageSrc( getModel() );
					}

					@Override
					public void onClick() {
						setResponsePage(new ArtExhibitionGuidePage(getModel(), getWorkingItems()));
					}

					@Override
					protected IModel<String> getInfo() {
						return ArtExhibitionGuidesPanel.this.getObjectInfo( getModel() );
					}

					@Override
					protected WebMarkupContainer getObjectMenu() {
						return ArtExhibitionGuidesPanel.this.getMenu(getModel());
					}
				};
				return panel;
			}
		};
		
		itemsContainer.add(panel);

		// panel.setTitle(getLabel("exhibitions-permanent"));
		panel.setListPanelMode(ListPanelMode.TITLE);
		panel.setLiveSearch(false);
		panel.setSettings(true);
		panel.setHasExpander(true);

	}
	
	
	protected List<IModel<ArtExhibitionGuide>> getItems() {
		if (this.list == null) {
			this.list = new ArrayList<IModel<ArtExhibitionGuide>>();
			getArtExhibitionIGuides(getModel().getObject()).forEach(item -> this.list.add(new ObjectModel<>(item)));
		}
		return this.list;
	}

	protected void setList(List<IModel<ArtExhibitionGuide>> list) {
		this.list=list;
	}

	private void resetItems() {
		this.list=null;
	}
	

}
